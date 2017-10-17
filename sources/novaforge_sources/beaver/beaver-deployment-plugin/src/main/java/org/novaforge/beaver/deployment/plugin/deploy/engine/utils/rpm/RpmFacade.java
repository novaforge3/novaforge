/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.rpm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.ant.AntFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec.ExecFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file.DeleteDirVisitor;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author Guillaume Lamirand
 */
public class RpmFacade
{
  private static String YUM_REPO_CONF_D      = "/etc/yum.repos.d";
  private static String YUM_REPO_CONF_D_BACK = "/etc/yum.repos.d.beaver-backup";
  private static String YUM_TMP_REPO_FILE    = "Beaver-Temporary.repo";
  private static String TMP_REPO_FILE        = "/rpms/Beaver-Temporary.repo";
  private static String TMP_REPO_PATH_TOKEN  = "@REPO_PATH@";
  private static String RPM_COMMAND          = "rpm";
  private static String YUM_COMMAND          = "yum";
  private static String CREATEREPO_COMMAND   = "createrepo";

  private RpmFacade()
  {
    // Utility class should have private explicit constructor ( Sonar rule : Hide Utility Class Constructor )
  }

  public static Path createRepository(final Path pRepositoryPath, final Path pRepositoryArchive)
      throws BeaverException
  {

    // Check required RPM
    checkRequiredRPM();

    // Copy repository file to yum directory
    copyRepoFile();
    final Path repoPath = Paths.get(YUM_REPO_CONF_D, YUM_TMP_REPO_FILE);
    AntFacade.replaceToken(repoPath.toFile(), TMP_REPO_PATH_TOKEN, pRepositoryPath.toString(), "", "");

    // Extract RPMs archive
    unpackRPMs(pRepositoryPath, pRepositoryArchive);

    // Create local databases
    execCreateRepository(pRepositoryPath);

    return repoPath;

  }

  /**
   * Install or upgrade createrepo RPM if needed
   * 
   * @throws BeaverException
   */
  private static void checkRequiredRPM() throws BeaverException
  {
    final boolean installed = isInstalled(CREATEREPO_COMMAND);
    if (installed == false)
    {
      throw new BeaverException("To use createRepository feature you need to install 'createrepo' dependency");
    }
  }

  private static void copyRepoFile() throws BeaverException
  {
    final InputStream is = RpmFacade.class.getResourceAsStream(TMP_REPO_FILE);

    final Path repoPath = Paths.get(YUM_REPO_CONF_D, YUM_TMP_REPO_FILE);
    try
    {
      Files.copy(is, repoPath);
    }
    catch (final IOException e)
    {
      throw new BeaverException(String.format("Unable to copy repository file definition to [%s]", repoPath),
          e);
    }
  }

  /**
   * @param pRepositoryPath
   * @param pRepositoryArchive
   * @throws BeaverException
   */
  private static void unpackRPMs(final Path pRepositoryPath, final Path pRepositoryArchive)
      throws BeaverException
  {
    try
    {
      if (Files.exists(pRepositoryPath) == false)
      {
        Files.createDirectories(pRepositoryPath);
      }
    }
    catch (final IOException e)
    {
      throw new BeaverException(String.format(
          "Target repository path doesn't exist but unable to create it [path=%s]", pRepositoryPath), e);
    }
    if (Files.exists(pRepositoryArchive))
    {
      final File archiveFile = pRepositoryArchive.toFile();
      final Archiver archiver = ArchiverFactory.createArchiver(archiveFile);

      try
      {
        archiver.extract(archiveFile, pRepositoryPath.toFile());
      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format(
            "An error occured when unpacking the YUM repository archive [%s] to destination [%s]",
            pRepositoryArchive, pRepositoryPath), e);
      }
    }
    else
    {
      throw new BeaverException(String.format(
          "Unable to unpack the YUM repository archive [%s] because it doesn't exist", pRepositoryArchive));

    }

  }

  /**
   * @param pRepositoryPath
   * @throws BeaverException
   */
  private static boolean execCreateRepository(final Path pRepositoryPath) throws BeaverException
  {
    final int returnValue = ExecFacade.execCommandWithParams(CREATEREPO_COMMAND, "-q",
        pRepositoryPath.toString());
    return ExecFacade.isSuccess(returnValue);

  }

  /**
   * @throws BeaverException
   */
  public static void backupYUMConf() throws BeaverException
  {
    final Path repoPath = Paths.get(YUM_REPO_CONF_D);
    final Path backRepoPah = Paths.get(YUM_REPO_CONF_D_BACK);

    try
    {
      Files.move(repoPath, backRepoPah, StandardCopyOption.REPLACE_EXISTING);
      Files.createDirectories(repoPath);
    }
    catch (final IOException e)
    {
      throw new BeaverException("Unable to backup yum repository configuration.", e);
    }

  }

  /**
   * @throws BeaverException
   */
  public static void restaureYUMConf() throws BeaverException
  {
    final Path repoPath = Paths.get(YUM_REPO_CONF_D);
    final Path backRepoPah = Paths.get(YUM_REPO_CONF_D_BACK);
    if (Files.exists(backRepoPah))
    {
      try
      {
        Files.walkFileTree(repoPath, new DeleteDirVisitor());
        Files.move(backRepoPah, repoPath);
      }
      catch (final IOException e)
      {
        throw new BeaverException("Unable to restaure yum repository configuration.", e);
      }
    }

  }

  /**
   * Install a single RPM package from its absolute path (/home/user/myrpm.rpm)
   * 
   * @param pPackagePath
   *          should be the absolute path to the rpm to install
   * @return true if exit value equals 0
   * @throws BeaverException
   */
  public static boolean rpmInstall(final String... pPackagePath) throws BeaverException
  {
    boolean isOk = false;
    for (final String packageName : pPackagePath)
    {
      final int returnValue = ExecFacade.execCommandWithParams(RPM_COMMAND, "-Ivh", "--quiet", packageName);
      isOk = ExecFacade.isSuccess(returnValue);
      if (isOk == false)
      {
        BeaverLogger.getOutlogger().error(
            String.format("Unable to install RPM package with [path=%s]", packageName));
        break;
      }
    }
    return isOk;
  }

  /**
   * Upgrade a single RPM package from its absolute path (/home/user/myrpm.rpm)
   * 
   * @param pPackagePath
   *          should be the absolute path to the rpm to install
   * @return true if exit value equals 0
   * @throws BeaverException
   */
  public static boolean rpmUpgrade(final String... pPackagePath) throws BeaverException
  {
    boolean isOk = false;
    for (final String packageName : pPackagePath)
    {
      final int returnValue = ExecFacade.execCommandWithParams(RPM_COMMAND, "-Uvh", "--quiet", packageName);
      isOk = ExecFacade.isSuccess(returnValue);
      if (isOk == false)
      {
        BeaverLogger.getOutlogger().error(
            String.format("Unable to install RPM package with [path=%s]", packageName));
        break;
      }
    }
    return isOk;
  }

  /**
   * Remove a single RPM package from its absolute path (/home/user/myrpm.rpm)
   * 
   * @param pPackagePath
   *          should be the absolute path to the rpm to install
   * @return true if exit value equals 0
   * @throws BeaverException
   */
  public static boolean rpmRemove(final String... pPackagePath) throws BeaverException
  {
    boolean isOk = false;
    for (final String packageName : pPackagePath)
    {
      final int returnValue = ExecFacade.execCommandWithParams(RPM_COMMAND, "-e", "--quiet", packageName);
      isOk = ExecFacade.isSuccess(returnValue);
      if (isOk == false)
      {
        BeaverLogger.getOutlogger().error(
            String.format("Unable to install RPM package with [path=%s]", packageName));
        break;
      }
    }
    return isOk;
  }

  /**
   * Install RPMs package using YUM command
   * 
   * @param pPackageName
   *          list of package name to install
   * @return true if exit value equals 0
   * @throws BeaverException
   */
  public static boolean yumInstall(final String... pPackageName) throws BeaverException
  {
    final List<String> params = new ArrayList<>();
    params.add("install");
    params.add("-y");
    params.add("-q");
    Collections.addAll(params, pPackageName);
    final int returnValue = ExecFacade.execCommandWithParams(YUM_COMMAND, params);
    return ExecFacade.isSuccess(returnValue);
  }

  /**
   * Remove RPMs package using YUM command
   * 
   * @param pPackageName
   *          list of package name to install
   * @return true if exit value equals 0
   * @throws BeaverException
   */
  public static boolean yumRemove(final String... pPackageName) throws BeaverException
  {
    final List<String> params = new ArrayList<>();
    params.add("remove");
    params.add("-y");
    params.add("-q");
    Collections.addAll(params, pPackageName);
    final int returnValue = ExecFacade.execCommandWithParams(YUM_COMMAND, params);
    return ExecFacade.isSuccess(returnValue);
  }

  /**
   * Upgrade RPMs package using YUM command
   * 
   * @param pPackageName
   *          list of package name to install
   * @return true if exit value equals 0
   * @throws BeaverException
   */
  public static boolean yumUpgrade(final String... pPackageName) throws BeaverException
  {
    final List<String> params = new ArrayList<>();
    params.add("upgrade");
    params.add("-y");
    params.add("-q");
    Collections.addAll(params, pPackageName);
    final int returnValue = ExecFacade.execCommandWithParams(YUM_COMMAND, params);
    return ExecFacade.isSuccess(returnValue);
  }

  /**
   * Check if the package name is installed
   * 
   * @param pPackageName
   *          name of RPM package
   * @return true is installed false otherwise
   * @throws BeaverException
   */
  public static boolean isInstalled(final String pPackageName) throws BeaverException
  {

    final int returnValue = ExecFacade.execCommandWithParams(RPM_COMMAND, "-q", "--quiet", pPackageName);
    return ExecFacade.isSuccess(returnValue);
  }
}
