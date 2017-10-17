/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * <p/>
 * This file is free software: you may redistribute and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 * <p/>
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 * <p/>
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 * <p/>
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.deploy.engine.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.apache.maven.project.MavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.engine.AbstractEngine;
import org.novaforge.beaver.deployment.plugin.deploy.engine.ConstantEngine;
import org.novaforge.beaver.deployment.plugin.deploy.engine.HTTPService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.RepositoryService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SSLService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.ant.AntFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec.ExecFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec.ExecResult;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file.CopyDirVisitor;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file.DeleteDirVisitor;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file.FilesFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file.OwnerDirVisitor;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file.OwnerDirVisitorWindows;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file.PropertiesMerger;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.groovy.GroovyFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.groovy.GroovyLog;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * {@link EngineImpl} is real implementation of API. It will do the job.
 *
 * @author Guillaume Lamirand
 * @version 2.0
 * @see org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
 * @see org.novaforge.beaver.deployment.plugin.deploy.engine.AbstractEngine
 */
public class EngineImpl extends AbstractEngine
{

  private final UserService       userService;
  private final RepositoryService repositoryService;
  private final SSLService        sslService;
  private final SystemdService    systemdService;
  private final HTTPService       httpService;

  public EngineImpl()
  {
    this(true, true);
  }

  public EngineImpl(final boolean pSystemdSystem, final boolean pYumSystem)
  {
    userService = new UserServiceImpl();
    repositoryService = new RepositoryServiceImpl(pYumSystem);
    sslService = new SSLServiceImpl();
    systemdService = new SystemdServiceImpl(pSystemdSystem);
    httpService = new HTTPServiceImpl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserService getUserService()
  {
    return userService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryService getRepositoryService()
  {
    return repositoryService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SSLService getSSLService()
  {
    return sslService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SystemdService getSystemdService()
  {
    return systemdService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HTTPService getHTTPService()
  {
    return httpService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyFile(final String pFileName, final String pKey, final String pValue)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        "Add property " + pKey + "-" + pValue + " to propertiles file: " + pFileName);
    final Properties properties = new Properties();
    try (FileInputStream inStream = new FileInputStream(pFileName))
    {
      properties.load(inStream);
    }
    catch (final IOException e)
    {
      throw new BeaverException("Cannot open an inputStream on property file.", e);
    }
    properties.setProperty(pKey, pValue);
    FileOutputStream outStream = null;
    try
    {
      outStream = new FileOutputStream(pFileName);
      properties.setProperty(pKey, pValue);
      properties.store(outStream, null);
    }
    catch (final IOException e)
    {
      throw new BeaverException("Cannot open an outputStream to property file.", e);
    }
    finally
    {
      if (outStream != null)
      {
        try
        {
          outStream.close();
        }
        catch (final IOException e)
        {
          throw new BeaverException("Cannot close outputStream on property file.", e);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int addLineToFile(final String pFile, final String pAfter, final String pLine)
      throws BeaverException
  {
    int lineAdded = 0;
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Add" + pLine + " after " + pAfter + " into " + pFile);
    final File file = new File(pFile);
    final File fileTmp = new File(file.getParent() + File.separatorChar + file.getName() + "_tmp");
    try (BufferedWriter buffTmp = new BufferedWriter(new FileWriter(fileTmp, true));)
    {

      final Scanner scanner = new Scanner(file);
      while (scanner.hasNext())
      {
        final String currentLine = scanner.nextLine();
        buffTmp.write(currentLine);
        buffTmp.newLine();
        if (currentLine.contains(pAfter))
        {
          buffTmp.write(pLine);
          buffTmp.newLine();
          lineAdded++;
        }
      }
      buffTmp.flush();
      buffTmp.close();
      scanner.close();

      FilesFacade.deleteSilent(file);
      final boolean succesRename = fileTmp.renameTo(new File(pFile));
      if (succesRename == false)
      {
        throw new BeaverException("addLineToFile : Cannot rename temporary file used to add your line into "
            + pFile);
      }
    }
    catch (final FileNotFoundException e)
    {
      throw new BeaverException("addLineToFile : File not found", e);
    }
    catch (final IOException e)
    {
      throw new BeaverException("addLineToFile : IOException", e);
    }
    finally
    {
      FilesFacade.deleteSilent(fileTmp);
    }
    return lineAdded;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeGroup(final boolean pRecursive, final String pGroup, final String pTarget)
      throws BeaverException
  {
    final boolean throwsOnError = true;
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Change group owner of " + pTarget + " by " + pGroup);
    try
    {
      final Path path = Paths.get(pTarget);
      if ((Files.isDirectory(path)) && (pRecursive))
      {
        Files.walkFileTree(path, new OwnerDirVisitor(null, pGroup, throwsOnError));
      }
      else
      {
        FilesFacade.setPosixAttribute(pGroup, null, path, throwsOnError);
      }
    }
    catch (final IOException e)
    {
      throw new BeaverException(String.format("Unable to update owner and group for [%s]", pTarget), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void concatFiles(final String pFileSrc, final String pFileDes) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Concat " + pFileSrc + " and " + pFileDes + "into" + pFileDes);
    try
    {
      AntFacade.concatFiles(pFileSrc, pFileDes);
    }
    catch (final BeaverException e)
    {
      throw new BeaverException("Cannot concat " + pFileSrc + " and " + pFileDes + " into " + pFileDes + ".",
          e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copy(final String pSrc, final String pDes) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Copy " + pSrc + " to " + pDes);

    final Path source = Paths.get(pSrc);
    final Path target = Paths.get(pDes);
    if (Files.exists(source))
    {
      try
      {
        if (Files.isDirectory(source))
        {
          Files.walkFileTree(source, new CopyDirVisitor(source, target, StandardCopyOption.COPY_ATTRIBUTES,
              StandardCopyOption.REPLACE_EXISTING));
        }
        else
        {
          Path targetFile = target;
          if (Files.exists(target) == false)
          {
            Files.createDirectories(target);
            targetFile = target.resolve(source.getFileName().toString());
          }
          else if (Files.isDirectory(target))
          {
            targetFile = target.resolve(source.getFileName().toString());
          }
          Files.copy(source, targetFile, StandardCopyOption.COPY_ATTRIBUTES,
              StandardCopyOption.REPLACE_EXISTING);
        }
      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format("Unalble to copy [%s] to [%s].", pSrc, pDes), e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copyToFile(final String pSrc, final String pDes) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Copy " + pSrc + " to " + pDes);
    final Path source = Paths.get(pSrc);
    final Path target = Paths.get(pDes);
    if (Files.exists(source))
    {
      if (Files.isDirectory(source) == false)
      {
        try
        {
          final Path parent = target.getParent();
          if (Files.exists(parent) == false)
          {
            Files.createDirectories(parent);
          }
          Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        }
        catch (final IOException e)
        {
          throw new BeaverException(String.format("Unable to copy file [%s] to [%s].", pSrc, pDes), e);
        }
      }
      else
      {
        throw new BeaverException(String.format(
            "The target destination has to be a file and not a directory [%s].", pDes));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createDirectory(final String pDirectory) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Create the directory : " + pDirectory);
    final Path directory = Paths.get(pDirectory);
    if (Files.exists(directory) == false)
    {
      try
      {
        Files.createDirectories(directory);
      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format("Unable to create directory [%s].", pDirectory), e);
      }
    }
    else
    {
      BeaverLogger.getFilelogger().warn(pDirectory + " is already existing");

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createNewFile(final String pFile) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Create the file : " + pFile);
    final Path file = Paths.get(pFile);
    if (Files.exists(file) == false)
    {
      try
      {
        Files.createFile(file);
      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format("Unable to create file [%s].", pFile), e);
      }
    }
    else
    {
      BeaverLogger.getFilelogger().warn(pFile + " is already existing");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSymlink(final String pResource, final String pTargetLink) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Create a symlink for the resource [%s] to [%s]", pResource, pTargetLink));
    final Path source = Paths.get(pResource);
    final Path target = Paths.get(pTargetLink);
    if (Files.exists(source))
    {
      try
      {
        Files.createDirectories(target.getParent());
        Files.createSymbolicLink(target, source);

        final PosixFileAttributes posixFileAttributes = Files.readAttributes(source,
            PosixFileAttributes.class);
        final Set<PosixFilePermission> posixPermissions = posixFileAttributes.permissions();
        Files.setPosixFilePermissions(target, posixPermissions);
        Files.getFileAttributeView(target, PosixFileAttributeView.class, LinkOption.NOFOLLOW_LINKS).setGroup(
            posixFileAttributes.group());
        Files.getFileAttributeView(target, PosixFileAttributeView.class, LinkOption.NOFOLLOW_LINKS).setOwner(
            posixFileAttributes.owner());

      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format("Unable to create symbolic link from [%s] to [%s].",
            pResource, pTargetLink), e);
      }
      catch (final UnsupportedOperationException e)
      {
        // Some file systems do not support symbolic links.
        BeaverLogger.getFilelogger().warn(
            String.format("Your operating system [%s] doesn't support symbolic links.", getEnvironment()));
      }
    }
    else
    {
      throw new BeaverException(String.format("The resource [%s] doesn't exist", pResource));
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String pElementToDelete) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(String.format("Delete the element [%s].", pElementToDelete));
    final Path path = Paths.get(pElementToDelete);
    if (Files.exists(path, LinkOption.NOFOLLOW_LINKS))
    {

      try
      {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
        {
          Files.walkFileTree(path, new DeleteDirVisitor());
        }
        else
        {
          Files.deleteIfExists(path);
        }
      }
      catch (final IOException e)
      {
        throw new BeaverException(
            String.format("Unable to delete the file/directory [%s]", pElementToDelete), e);
      }
    }
    else
    {
      BeaverLogger.getFilelogger().warn(pElementToDelete + " doesn't exist.");

    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeScript(final String pScript) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Execute " + pScript);
    final File scriptFile = new File(pScript);
    if (isLinux())
    {
      try
      {
        AntFacade.executeBashScript(scriptFile);
      }
      catch (final BeaverException e)
      {
        throw new BeaverException("Cannot execute the script " + pScript, e);
      }
    }
    else if (isWindows())
    {
      try
      {
        AntFacade.executeBatScript(scriptFile);
      }
      catch (final BeaverException e)
      {
        throw new BeaverException("Cannot execute the script " + pScript, e);
      }
    }
  }

  @Override
  public void executeCommand(final String pCommand, final String... pParams) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Execute [%s] with arguments [%s]", pCommand, Arrays.toString(pParams)));

    final int returnCode = ExecFacade.execCommandWithParams(pCommand, pParams);
    if (ExecFacade.isSuccess(returnCode) == false)
    {
      throw new BeaverException(String.format("Cannot execute the command [%s] with arguments [%s]",
          pCommand, Arrays.toString(pParams)));

    }
  }

  @Override
  public ExecResult executeCommandAndGetResult(final String pCommand, final String... pParams)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Execute [%s] with arguments [%s]", pCommand, Arrays.toString(pParams)));

    final ExecResult retour = ExecFacade.execCommandWithParamsAndGetResult(pCommand, pParams);
    if (ExecFacade.isSuccess(retour.getExitCode()) == false)
    {
      throw new BeaverException(String.format("Cannot execute the command [%s] with arguments [%s]",
          pCommand, Arrays.toString(pParams)));
    }
    return retour;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDatabaseExists(final String pMysqlBin, final String pMysqlPort, final String pUser,
      final String pPwd, final String pDatabase)
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Test database");
    BeaverLogger.getFilelogger().info("****** Mysql: " + pMysqlBin);
    BeaverLogger.getFilelogger().info("****** Port: " + pMysqlPort);
    BeaverLogger.getFilelogger().info("****** User: " + pUser);
    BeaverLogger.getFilelogger().info("****** Database: " + pDatabase);
    final String command = "\"\" " + pDatabase;
    final GroovyLog mysqlReturn = GroovyFacade.mysqlExecute(pMysqlBin, pMysqlPort, pUser, pPwd, command);

    return mysqlReturn.getCodeReturn() == 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeMysqlScript(final String pMysqlBin, final String pMysqlPort, final String pUser,
      final String pPwd, final String pScript) throws BeaverException
  {

    String script = pScript;
    if (isWindows())
    {
      script = script.replace("\\", "/");
    }
    final String command = String.format("source %s;", script);
    executeMysql(pMysqlBin, pMysqlPort, pUser, pPwd, command);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeMysql(final String pMysqlBin, final String pMysqlPort, final String pUser,
      final String pPwd, final String pCommand) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Execute mysql script");
    BeaverLogger.getFilelogger().info("****** Mysql: " + pMysqlBin);
    BeaverLogger.getFilelogger().info("****** Port: " + pMysqlPort);
    BeaverLogger.getFilelogger().info("****** User: " + pUser);
    BeaverLogger.getFilelogger().info("****** Comment: " + pCommand);
    final GroovyLog mysqlReturn = GroovyFacade.mysqlExecute(pMysqlBin, pMysqlPort, pUser, pPwd, pCommand);
    if (mysqlReturn.getCodeReturn() == 0)
    {
      BeaverLogger.getFilelogger().debug(mysqlReturn.getOut());
    }
    else
    {
      final StringBuilder errorBuilder = new StringBuilder();
      errorBuilder.append("\n Cannot execute mysql script '");
      errorBuilder.append(pCommand);
      errorBuilder.append("' ");
      errorBuilder.append(mysqlReturn.getOut());
      errorBuilder.append("\n Error: ");
      errorBuilder.append(mysqlReturn.getErr());
      throw new BeaverException(errorBuilder.toString());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void replaceElementFilter(final String pDirectory, final String pToken, final String pValue,
      final String pIncludes, final String pExcludes) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Replace " + pToken + " by " + pValue);
    BeaverLogger.getFilelogger().info("in " + pDirectory);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("including : '");
    stringBuilder.append(pIncludes);
    stringBuilder.append("' and excluding : '");
    stringBuilder.append(pExcludes);
    stringBuilder.append("'");
    BeaverLogger.getFilelogger().info(stringBuilder.toString());
    final File replaceDir = new File(pDirectory);
    try
    {
      AntFacade.replaceToken(replaceDir, pToken, pValue, pIncludes, pExcludes);
    }
    catch (final BeaverException e)
    {
      final StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Cannot replace the the token ");
      stringBuilder1.append(pToken);
      stringBuilder1.append(" by ");
      stringBuilder1.append(pValue);
      stringBuilder1.append(" in the directory ");
      stringBuilder1.append(pDirectory);
      stringBuilder1.append(" including ");
      stringBuilder1.append(pIncludes);
      stringBuilder1.append(" and excluding ");
      stringBuilder1.append(pExcludes);
      throw new BeaverException(stringBuilder1.toString(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void replaceElement(final String pFile, final String pToken, final String pValue)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Replace " + pToken + " by " + pValue);
    BeaverLogger.getFilelogger().info("in " + pFile);
    final File replaceFile = new File(pFile);
    if (replaceFile.exists())
    {
      try
      {
        AntFacade.replaceToken(replaceFile, pToken, pValue, "", "");
      }
      catch (final BeaverException e)
      {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot replace the token ");
        stringBuilder.append(pToken);
        stringBuilder.append(" by ");
        stringBuilder.append(pValue);
        stringBuilder.append(" in the file ");
        stringBuilder.append(pFile);
        throw new BeaverException(stringBuilder.toString(), e);

      }
    }
    else
    {
      throw new BeaverException("Replace element: " + pFile + " doesn't exist.");
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void replaceExpression(final String pFile, final String pPatern, final String pSubstitution)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Replace regular expression ");
    stringBuilder.append(pPatern);
    stringBuilder.append(" by ");
    stringBuilder.append(pSubstitution);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());
    BeaverLogger.getFilelogger().info("in " + pFile);
    final File replaceFile = new File(pFile);
    try
    {
      AntFacade.replaceRegExp(replaceFile, pPatern, pSubstitution);
    }
    catch (final BeaverException e)
    {
      final StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Cannot replace the regular expression ");
      stringBuilder1.append(pPatern);
      stringBuilder1.append(" by ");
      stringBuilder1.append(pSubstitution);
      stringBuilder1.append(" in the file ");
      stringBuilder1.append(pFile);
      throw new BeaverException(stringBuilder1.toString(), e);

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPermissions(final boolean pRecursive, final String pRight, final String pElement)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Define permission : ");
    stringBuilder.append(pRight);
    stringBuilder.append(" on ");
    stringBuilder.append(pElement);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());
    final File dirFile = new File(pElement);
    AntFacade.setPermission(pRecursive, pRight, dirFile);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPermissionsOnFiles(final boolean pRecursive, final String pRight, final String pElement)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Define permission on sub-files : ");
    stringBuilder.append(pRight);
    stringBuilder.append(" on ");
    stringBuilder.append(pElement);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());

    setPermissionsOnElements(ConstantEngine.FILE, pRight, pElement);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPermissionsOnDirectories(final boolean pRecursive, final String pRight, final String pElement)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Define permission on sub-directories: ");
    stringBuilder.append(pRight);
    stringBuilder.append(" on ");
    stringBuilder.append(pElement);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());

    setPermissionsOnElements(ConstantEngine.DIR, pRight, pElement);

  }

  /**
   * It will set the permissions either on files or directories
   *
   * @param pTypeElement
   *          represents the type of element.
   * @param pRight
   *          represents the right.
   * @param pElement
   *          represents the first element.
   * @throws BeaverException
   */
  private void setPermissionsOnElements(final String pTypeElement, final String pRight, final String pElement)
      throws BeaverException
  {
    String type = "";

    if (isLinux())
    {
      if (ConstantEngine.FILE.equals(pTypeElement))
      {
        type = ConstantEngine.TYPE_FILE;
      }
      else if (ConstantEngine.DIR.equals(pTypeElement))
      {
        type = ConstantEngine.TYPE_DIR;
      }

      final StringBuilder command = new StringBuilder();
      command.append(ConstantEngine.FIND).append(ConstantEngine.SPACE).append(pElement)
          .append(ConstantEngine.SPACE).append(ConstantEngine.FIND_TYPE).append(ConstantEngine.SPACE)
          .append(type).append(ConstantEngine.SPACE).append(ConstantEngine.FIND_EXEC)
          .append(ConstantEngine.SPACE).append(ConstantEngine.CHMOD).append(ConstantEngine.SPACE)
          .append(pRight).append(ConstantEngine.SPACE).append(ConstantEngine.FIND_EXEC_END);

      final int exitCode = ExecFacade.execCommandLine(command.toString());
      if (ExecFacade.isSuccess(exitCode) == false)
      {
        throw new BeaverException(String.format(
            "An error occured during modification of permission for [%s]", pElement));
      }
    }
    else
    {
      // TODO
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOwner(final boolean pRecursive, final String pGroup, final String pUser,
      final String pDirectory) throws BeaverException
  {
    setOwner(pRecursive, pGroup, pUser, pDirectory, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOwner(final boolean pRecursive, final String pGroup, final String pUser,
      final String pDirectory, final boolean throwsOnError) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(String.format("Define new owner and group for [%s]", pDirectory));
    try
    {
      final Path path = Paths.get(pDirectory);
      if (isLinux())
      {
        if ((Files.isDirectory(path)) && (pRecursive))
        {
          Files.walkFileTree(path, new OwnerDirVisitor(pUser, pGroup, throwsOnError));
        }
        else
        {
          FilesFacade.setPosixAttribute(pGroup, pUser, path, throwsOnError);
        }
      }
      else
      {
        if ((Files.isDirectory(path)) && (pRecursive))
        {
          Files.walkFileTree(path, new OwnerDirVisitorWindows(pUser, throwsOnError));
        }
        else
        {
          FilesFacade.setOwnerAttribute(pUser, path, throwsOnError);
        }
      }
    }
    catch (final IOException e)
    {
      throw new BeaverException(String.format("Unable to update owner and group for [%s]", pDirectory), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unpackFile(final String pSrcFile, final String pDesDir) throws BeaverException
  {
    unpackFile(Paths.get(pSrcFile), Paths.get(pDesDir));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void pack(final String pSrc, final String pDes) throws BeaverException
  {
    final File src = new File(pSrc);
    final File des = new File(pDes);
    pack(src, des);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void move(final String pRegEx, final String pDestination) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Move element regarding '").append(pRegEx).append("' to ").append(pDestination);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());

    int exitCode = 1;
    if (isLinux())
    {
      exitCode = ExecFacade.execCommandWithParams(ConstantEngine.MOVE_LINUX, pRegEx, pDestination);
    }
    else if (isWindows())
    {
      exitCode = ExecFacade.execCommandWithParams(ConstantEngine.CMD_EXE_WINDOWS,
          ConstantEngine.SLASH_C_WINDOWS, ConstantEngine.MOVE_WINDOWS, pRegEx, pDestination);
    }

    if (ExecFacade.isSuccess(exitCode) == false)
    {
      throw new BeaverException(String.format("Cannot move element with expresion regular [%s] to [%s]",
          pRegEx, pDestination));
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moveDir(final String pSource, final String pDestination) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Move source :").append(pSource).append(" to ").append(pDestination);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());
    final Path source = Paths.get(pSource);
    final Path target = Paths.get(pDestination);
    try
    {
      Files.createDirectories(target);
      Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }
    catch (final IOException e)
    {
      throw new BeaverException(
          String.format("Unable to move directory [%s] to [%s]", pSource, pDestination), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commentLines(final String pFile, final String pStart, final String pEnd, final String pType)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Comment lines between ").append(pStart).append(" and ").append(pEnd)
        .append(" with ").append(pType).append(" into ").append(pFile);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());
    final File file = new File(pFile);
    if (file.isFile())
    {
      final StringBuilder tmpFileName = new StringBuilder();
      tmpFileName.append(file.getParent()).append("/").append(file.getName()).append("_tmp");
      final File fileTmp = new File(tmpFileName.toString());
      try (BufferedWriter buffTmp = new BufferedWriter(new FileWriter(fileTmp, true));)
      {

        final Scanner scanner = new Scanner(file);
        boolean isIn = false;
        while (scanner.hasNext())
        {
          final String currentLine = scanner.nextLine();
          boolean startCommented = false;
          if (currentLine.contains(pStart))
          {
            isIn = true;
            writeMainComment(isIn, buffTmp, currentLine, pType, startCommented);
            startCommented = true;
          }
          if (isIn == true)
          {
            if (currentLine.contains(pEnd))
            {
              isIn = false;
              writeMainComment(isIn, buffTmp, currentLine, pType, startCommented);
            }
            else
            {
              buffTmp.write(getCommentLine(currentLine, pType));
            }
          }
          else
          {
            buffTmp.write(currentLine);
          }

          buffTmp.newLine();
        }
        buffTmp.flush();
        buffTmp.close();
        scanner.close();

        FilesFacade.deleteSilent(file);
        final boolean succeed = fileTmp.renameTo(new File(pFile));
        if (succeed == false)
        {
          throw new BeaverException("Fail to rename temporaty file to original file.");
        }
      }
      catch (final FileNotFoundException e)
      {
        throw new BeaverException("commentsLines : File not found", e);
      }
      catch (final IOException e)
      {
        throw new BeaverException("commentsLines : IOException", e);
      }
      finally
      {
        FilesFacade.deleteSilent(fileTmp);
      }
    }
    else
    {
      throw new BeaverException(String.format("File [%s] doesn't exist or is a directory.", pFile));
    }

  }

  private void writeMainComment(final boolean pIsIn, final BufferedWriter pTmp, final String pLine,
      final String pType, final boolean pStartJustCommented) throws IOException
  {
    if (ConstantEngine.XML_TYPE.equals(pType))
    {
      writeXMLComment(pIsIn, pTmp, pLine, pStartJustCommented);
    }
    else
    {
      pTmp.write(getCommentLine(pLine, pType));
    }
  }

  private void writeXMLComment(final boolean pIsIn, final BufferedWriter pTmp, final String pLine,
      final boolean pStartJustCommented) throws IOException
  {
    if (pIsIn == true)
    {
      pTmp.write(ConstantEngine.XML_COMMENT_BEGIN);
      pTmp.newLine();
      pTmp.write(pLine);
    }
    else
    {
      if (pStartJustCommented == false)
      {
        pTmp.write(pLine);
      }
      pTmp.newLine();
      pTmp.write(ConstantEngine.XML_COMMENT_END);
    }

  }

  private String getCommentLine(final String pLine, final String pType)
  {
    final StringBuilder commentLine = new StringBuilder();
    if (ConstantEngine.SHELL_TYPE.equals(pType))
    {
      commentLine.append(ConstantEngine.SHELL_COMMENT).append(pLine);

    }
    else if (ConstantEngine.SCRIPT_TYPE.equals(pType))
    {
      commentLine.append(ConstantEngine.SCRIPT_COMMENT).append(pLine);
    }
    else if (ConstantEngine.XML_TYPE.equals(pType))
    {
      commentLine.append(pLine);
    }
    else
    {
      BeaverLogger.getFilelogger().warn("Unexisting type of comment; should be 'shell','script' or 'xml'");

    }
    return commentLine.toString();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteLines(final String pFile, final String pStart, final String pEnd) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Delete lines between ").append(pStart).append(" and ").append(pEnd)
        .append(" into ").append(pFile);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());
    final File file = new File(pFile);
    final File fileTmp = new File(file.getParent() + "/" + file.getName() + "_tmp");
    try (BufferedWriter buffTmp = new BufferedWriter(new FileWriter(fileTmp, true));)
    {

      final Scanner scanner = new Scanner(file);
      boolean isIn = false;
      while (scanner.hasNext())
      {
        final String currentLine = scanner.nextLine();
        if (currentLine.contains(pStart))
        {
          isIn = true;
        }
        if (isIn == false)
        {
          buffTmp.write(currentLine);
          buffTmp.newLine();
        }
        if (currentLine.contains(pEnd))
        {
          isIn = false;
        }
      }
      buffTmp.flush();
      buffTmp.close();
      scanner.close();

      FilesFacade.deleteSilent(file);
      final boolean succeed = fileTmp.renameTo(new File(pFile));
      if (succeed == false)
      {
        throw new BeaverException("Fail to rename temporaty file to original file.");
      }
    }
    catch (final FileNotFoundException e)
    {
      throw new BeaverException("commentsLines: File not found", e);
    }
    catch (final IOException e)
    {
      throw new BeaverException("commentsLines: IOException", e);
    }
    finally
    {
      FilesFacade.deleteSilent(fileTmp);
    }

  }

  /** {@inheritDoc} */
  @Override
  public void propertiesFilesMerger(final String pOldFile, final String pNewFile,
      final List<String> pExclusionList) throws BeaverException
  {
    new PropertiesMerger(pOldFile, pNewFile, pExclusionList);
  }

  /** {@inheritDoc} */
  @Override
  public void patch(final String pPatchFile, final int pStrip, final String pDirectoryToExec,
      final boolean pKeepBackup) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Patch directory ").append(pDirectoryToExec).append(" with ").append(pPatchFile);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());
    try
    {
      final File patchFile = new File(pPatchFile);
      final File directoryToExec = new File(pDirectoryToExec);
      if ((patchFile.exists()) && (directoryToExec.isDirectory()))
      {
        AntFacade.patch(patchFile, pStrip, directoryToExec, pKeepBackup, !BeaverLogger.getFilelogger()
            .isDebugEnabled());
      }
      else if (patchFile.exists() == false)
      {
        throw new BeaverException(String.format("The patch file doesn't exist [patch=%s]", pPatchFile));
      }
      else if (directoryToExec.isDirectory() == false)
      {
        throw new BeaverException(
            String.format("The destination directory is not a directory or doesn't exist [target_dir=%s]",
                pDirectoryToExec));
      }
    }
    catch (final BeaverException e)
    {
      throw new BeaverException("Cannot execute patch command in " + pDirectoryToExec + " with file "
          + pPatchFile + ".", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMavenProfile(final String pMavenProfile)
  {
    boolean hasProfile = false;
    MavenProject project = BeaverServices.getMojoService().getMavenProject();

    for (final String profile : project.getProjectBuildingRequest().getActiveProfileIds())
    {
      if (profile.equals(pMavenProfile))
      {
        hasProfile = true;
        break;
      }
    }

    return hasProfile;
  }
}
