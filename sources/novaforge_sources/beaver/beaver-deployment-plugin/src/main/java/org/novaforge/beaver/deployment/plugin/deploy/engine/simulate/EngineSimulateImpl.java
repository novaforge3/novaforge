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
package org.novaforge.beaver.deployment.plugin.deploy.engine.simulate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.deploy.engine.AbstractEngine;
import org.novaforge.beaver.deployment.plugin.deploy.engine.HTTPService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.RepositoryService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SSLService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec.ExecResult;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources.ResourceImpl;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.MavenPropertyMode;
import org.novaforge.beaver.resource.Resource;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * EngineImpl is simulation implementation of API. It will just display some messages.
 * 
 * @see org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
 * @see org.novaforge.beaver.deployment.plugin.deploy.engine.AbstractEngine
 * @author Guillaume Lamirand
 * @version 2.0
 */
public class EngineSimulateImpl extends AbstractEngine
{
  private static final String                     DEFAULT_VALUE   = "default";
  private final Map<String, List<DependencyNode>> useRessources   = new HashMap<String, List<DependencyNode>>();
  private final Map<String, String>               ressourcesValue = new HashMap<String, String>();

  private final UserService                       userService;
  private final RepositoryService                 repositoryService;
  private final SSLService                        sslService;
  private final SystemdService                    systemDService;
  private final HTTPService                       httpService;

  public EngineSimulateImpl()
  {
    userService = new UserServiceSimulateImpl();
    repositoryService = new RepositoryServiceSimulateImpl();
    sslService = new SSLServiceSimulateImpl();
    systemDService = new SystemDServiceSimulateImpl();
    httpService = new HTTPServiceSimulateImpl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSimulateMode()
  {
    return true;
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
  public HTTPService getHTTPService()
  {
    return httpService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SystemdService getSystemdService()
  {
    return systemDService;
  }

  public Map<String, List<DependencyNode>> getUseRessources()
  {
    return useRessources;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyFile(final String pFileName, final String pKey, final String pValue)
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Add property to propertiles file");
    BeaverLogger.getFilelogger().info("File: " + pFileName);
    BeaverLogger.getFilelogger().info("Key: " + pKey);
    BeaverLogger.getFilelogger().info("Value: " + pValue);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int addLineToFile(final String pFile, final String pAfter, final String pLine)
      throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Add line to file");
    BeaverLogger.getFilelogger().info("Line: " + pLine);
    BeaverLogger.getFilelogger().info("After: " + pAfter);
    BeaverLogger.getFilelogger().info("File: " + pFile);
    return 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeGroup(final boolean pRecursive, final String pGroup, final String pDirectory)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Change group owner of ").append(pDirectory).append(" by ").append(pGroup);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void concatFiles(final String pFileSrc, final String pFileDes)
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Concat ");
    stringBuilder.append(pFileSrc);
    stringBuilder.append(" and ");
    stringBuilder.append(pFileDes);
    stringBuilder.append("into");
    stringBuilder.append(pFileDes);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void patch(final String pPatchFile, final int pStrip, final String pDirectoryToExec,
      final boolean pKeepBackup) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Patch directory :").append(pDirectoryToExec).append(" with ").append(pPatchFile);
    BeaverLogger.getFilelogger().info(stringBuilder.toString());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copy(final String pSrc, final String pDes)
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Copy file to directory");
    BeaverLogger.getFilelogger().info("File/Directory: " + pSrc);
    BeaverLogger.getFilelogger().info("Directory: " + pDes);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copyToFile(final String pSrc, final String pDes)
  {

    BeaverLogger.getFilelogger().info("Copy file to file");
    BeaverLogger.getFilelogger().info("File: " + pSrc);
    BeaverLogger.getFilelogger().info("To File: " + pDes);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createDirectory(final String pDirectory) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Create the directory : " + pDirectory);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createNewFile(final String pFile) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Create the file : " + pFile);

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

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final String pElementToDelete) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(String.format("Delete the element [%s].", pElementToDelete));

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

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeMysqlScript(final String pMysqlBin, final String pMysqlPort, final String pUser,
      final String pPwd, final String pScript) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Execute mysql");
    BeaverLogger.getFilelogger().info("Mysql: " + pMysqlBin);
    BeaverLogger.getFilelogger().info("Port: " + pMysqlPort);
    BeaverLogger.getFilelogger().info("User: " + pUser);
    BeaverLogger.getFilelogger().info("Command line: " + pScript);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeMysql(final String pMysqlBin, final String pMysqlPort, final String pUser,
      final String pPwd, final String pCommand) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Execute mysql");
    BeaverLogger.getFilelogger().info("Mysql: " + pMysqlBin);
    BeaverLogger.getFilelogger().info("Port: " + pMysqlPort);
    BeaverLogger.getFilelogger().info("User: " + pUser);
    BeaverLogger.getFilelogger().info("Commande file: " + pCommand);
    final File script = FileUtils.getFile(pCommand);
    if (script.exists() == false)
    {
      BeaverLogger.getFilelogger().error("Mysql script: " + pCommand + " doesn't exist.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeScript(final String pFile) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Execute " + pFile);
    final File script = FileUtils.getFile(pFile);
    if (script.exists() == false)
    {
      BeaverLogger.getFilelogger().error("Execute script: " + pFile + " doesn't exist.");
    }
  }

  /**
   * {@inheritDoc}
   */
  /**
   * {@inheritDoc}
   */
  @Override
  public String getResource(final String pRessourceNeeded) throws BeaverException
  {
    String resourceValue = DEFAULT_VALUE;
    try
    {
      resourceValue = super.getResource(pRessourceNeeded);
    }
    catch (final BeaverException e)
    {
      BeaverLogger.getFilelogger().warn(
          "No resource was found so default value is returned in simulation mode");

    }
    storeSimulateAcces(null, null, pRessourceNeeded, resourceValue);
    return resourceValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResource(final String pProductId, final String pRessourceNeeded) throws BeaverException
  {
    String resourceValue = DEFAULT_VALUE;
    try
    {
      resourceValue = super.getResource(pProductId, pRessourceNeeded);
    }
    catch (final BeaverException e)
    {
      BeaverLogger.getFilelogger().warn(
          "No resource was found so default value is returned in simulation mode");

    }
    storeSimulateAcces(null, pProductId, pRessourceNeeded, resourceValue);
    return resourceValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResource(final String pServerId, final String pProductId, final String pRessourceNeeded)
      throws BeaverException
  {
    String resourceValue = DEFAULT_VALUE;
    try
    {
      resourceValue = super.getResource(pServerId, pProductId, pRessourceNeeded);
    }
    catch (final BeaverException e)
    {
      BeaverLogger.getFilelogger().warn(
          "No resource was found so default value is returned in simulation mode");
    }
    storeSimulateAcces(pServerId, pProductId, pRessourceNeeded, resourceValue);
    return resourceValue;
  }

  private void storeSimulateAcces(final String pServerId, final String pProductId,
      final String pRessourceNeeded, final String resourceValue) throws BeaverException
  {
    if (StringUtils.isNotEmpty(resourceValue))
    {
      Resource property = new ResourceImpl(pServerId, pProductId, pRessourceNeeded);
      if (property.isCurrentProductRelated() == true)
      {
        property = new ResourceImpl(pServerId, BeaverServices.getCurrentProductProcess().getProductId(),
            pRessourceNeeded);
      }
      addRessourceUser(property);
      addRessourceValue(property, resourceValue);
    }
  }

  private void addRessourceValue(final Resource pResource, final String pValue) throws BeaverException
  {
    final String mavenProperty = pResource.getMavenProperty(MavenPropertyMode.PRODUCT_ID);
    if (ressourcesValue.containsKey(mavenProperty) == false)
    {
      ressourcesValue.put(mavenProperty, pValue);
    }
  }

  private void addRessourceUser(final Resource pResource) throws BeaverException
  {
    final String mavenProperty = pResource.getMavenProperty(MavenPropertyMode.PRODUCT_ID);
    if (BeaverServices.getCurrentProductProcess() != null)
    {
      if (useRessources.containsKey(mavenProperty) == false)
      {
        final List<DependencyNode> newUsers = new ArrayList<DependencyNode>();
        newUsers.add(BeaverServices.getCurrentProductProcess().getNode());
        useRessources.put(mavenProperty, newUsers);
      }
      else
      {
        final List<DependencyNode> users = useRessources.get(mavenProperty);
        users.add(BeaverServices.getCurrentProductProcess().getNode());
        useRessources.put(mavenProperty, users);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLinux()
  {
    final boolean islinux = super.isLinux();
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Ask if on Linux");
    if (islinux)
    {
      BeaverLogger.getFilelogger().info("Yes, you use Linux");
    }
    else
    {
      BeaverLogger.getFilelogger().info("No, you DON'T use on Linux");
    }

    return islinux;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWindows()
  {
    final boolean iswindows = super.isWindows();

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Ask if on Windows");
    if (iswindows)
    {
      BeaverLogger.getFilelogger().info("Yes, you use Windows");
    }
    else
    {
      BeaverLogger.getFilelogger().info("No, you DON'T use on Windows");
    }
    return iswindows;
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
    BeaverLogger.getFilelogger().info("File " + pFile);
    final File script = FileUtils.getFile(pFile);
    if (script.exists() == false)
    {
      BeaverLogger.getFilelogger().error("Replace element: " + pFile + " doesn't exist.");
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void replaceElementFilter(final String pFile, final String pToken, final String pValue,
      final String pIncludes, final String pExcludes) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Replace " + pToken + " by " + pValue);
    BeaverLogger.getFilelogger().info("File " + pFile);
    BeaverLogger.getFilelogger().info("Including : '" + pIncludes);
    BeaverLogger.getFilelogger().info("Excluding : '" + pExcludes + "'");
    final File script = FileUtils.getFile(pFile);
    if (script.exists() == false)
    {
      BeaverLogger.getFilelogger().error("Replace element: " + pFile + " doesn't exist.");
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
    BeaverLogger.getFilelogger().info("Replace regular expression " + pPatern + " by " + pSubstitution);
    BeaverLogger.getFilelogger().info("File " + pFile);
    final File script = FileUtils.getFile(pFile);
    if (script.exists() == false)
    {
      BeaverLogger.getFilelogger().error("Replace element: " + pFile + " doesn't exist.");
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValueFromRegex(final String pFile, final String pPatern) throws BeaverException
  {
    String returnValue = "";
    try
    {
      returnValue = super.getValueFromRegex(pFile, pPatern);
    }
    catch (final BeaverException e)
    {
      BeaverLogger.getFilelogger().error("Unable to retrieve value from regex given", e);
    }
    return returnValue;
  }

  /** {@inheritDoc} */
  @Override
  public void setOwner(final boolean pRecursive, final String pGroup, final String pUser,
      final String pDirectory)
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Define owner of " + pDirectory);
  }

  /** {@inheritDoc} */
  @Override
  public void setOwner(final boolean pRecursive, final String pGroup, final String pUser,
      final String pDirectory, final boolean throwsOnError) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        "Define owner of " + pDirectory + " and throwsOnError is " + throwsOnError);
  }

  /** {@inheritDoc} */
  @Override
  public void setPermissions(final boolean pRecursive, final String pRight, final String pDirectory)
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Define permission : " + pRight + " on " + pDirectory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPermissionsOnFiles(final boolean pRecursive, final String pRight, final String pDirectory)
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger()
        .info("Define permission : " + pRight + " on " + pDirectory + "only on files");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPermissionsOnDirectories(final boolean pRecursive, final String pRight,
      final String pDirectory)
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        "Define permission : " + pRight + " on " + pDirectory + "only on directories");

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unpackFile(final String pFile, final String pDes)
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Unpack file");
    BeaverLogger.getFilelogger().info("File: " + pFile);
    BeaverLogger.getFilelogger().info("Destination: " + pDes);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void pack(final String pSrc, final String pDes)
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Pack into file");
    BeaverLogger.getFilelogger().info("Source: " + pSrc);
    BeaverLogger.getFilelogger().info("Destination: " + pDes);
  }

  public void writeSimulationReport()
  {
    BeaverLogger.getSimulationlogger().info(
        "*******************************************************************");
    BeaverLogger.getSimulationlogger().info("SIMULATION REPORT");

    if (ressourcesValue != null)
    {
      BeaverLogger.getSimulationlogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getSimulationlogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getSimulationlogger().info("List of properties used by the process : ");
      final Iterator<String> iValue = ressourcesValue.keySet().iterator();
      while (iValue.hasNext())
      {
        final String valueKey = iValue.next();
        BeaverLogger.getSimulationlogger().info(
            "        - " + valueKey + " : " + ressourcesValue.get(valueKey));
      }
      BeaverLogger.getSimulationlogger().info(BeaverLogger.SEPARATOR_TIRET);
    }

    if (useRessources != null)
    {
      BeaverLogger.getSimulationlogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getSimulationlogger().info("List of product using a specific property - Key : Value");
      final Iterator<String> iRessources = useRessources.keySet().iterator();
      while (iRessources.hasNext())
      {
        final String ressourceKey = iRessources.next();
        final List<DependencyNode> usersList = useRessources.get(ressourceKey);
        BeaverLogger.getSimulationlogger().info(BeaverLogger.SEPARATOR_TIRET);
        BeaverLogger.getSimulationlogger().info(
            " The ressource : *" + ressourceKey + "* is used by the followings :");
        final Iterator<DependencyNode> iUsersList = usersList.iterator();
        while (iUsersList.hasNext())
        {
          final DependencyNode user = iUsersList.next();
          BeaverLogger.getSimulationlogger().info(
              "        - " + user.getArtifact().getGroupId() + " / " + user.getArtifact().getArtifactId()
                  + " / " + user.getArtifact().getVersion());
        }
      }
      BeaverLogger.getSimulationlogger().info(BeaverLogger.SEPARATOR_TIRET);
    }
    BeaverLogger.getSimulationlogger().info(
        "*******************************************************************");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void move(final String pRegEx, final String pDestination) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Move element using regular expression to Destination");
    BeaverLogger.getFilelogger().info("RegEx: " + pRegEx);
    BeaverLogger.getFilelogger().info("Destination: " + pDestination);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moveDir(final String pSource, final String pDestination) throws BeaverException
  {

    // TODO Add log information
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commentLines(final String pFile, final String pStart, final String pEnd, final String pType)
      throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Comment paragraph ");
    BeaverLogger.getFilelogger().info("Starting with: " + pStart);
    BeaverLogger.getFilelogger().info("Ending with: " + pEnd);
    BeaverLogger.getFilelogger().info("Using : " + pType);
    BeaverLogger.getFilelogger().info("File: " + pFile);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteLines(final String pFile, final String pStart, final String pEnd) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Delete paragraph ");
    BeaverLogger.getFilelogger().info("Starting with: " + pStart);
    BeaverLogger.getFilelogger().info("Ending with: " + pEnd);
    BeaverLogger.getFilelogger().info("File: " + pFile);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void propertiesFilesMerger(final String pNewFile, final String pOldFile,
      final List<String> pExclusionList) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("New properties file to update: " + pNewFile);
    BeaverLogger.getFilelogger().info("Old properties file to conserve: " + pOldFile);
    if ((pExclusionList != null) && !pExclusionList.isEmpty())
    {
      BeaverLogger.getFilelogger().info(
          "List of exclusion file: " + Arrays.toString(pExclusionList.toArray()));
    }
    else
    {
      BeaverLogger.getFilelogger().info("List of exclusion file is null or empty");
    }

  }

  /** {@inheritDoc} */
  @Override
  public void executeCommand(final String pCommand, final String... pParams) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Execute Command:" + pCommand);
    for (final String arg : pParams)
    {
      BeaverLogger.getFilelogger().info("With argument: " + arg);
    }
  }

  /** {@inheritDoc} */
  @Override
  public ExecResult executeCommandAndGetResult(final String pCommand, final String... pParams)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Execute Command:" + pCommand);
    for (final String arg : pParams)
    {
      BeaverLogger.getFilelogger().info("With argument: " + arg);
    }
    return new ExecResult(1, "Result");
  }

  public boolean hasMavenProfile(final String pMavenProfile)
  {
    boolean hasProfile = true;

    BeaverLogger.getFilelogger().info("Test if this profile exists:" + pMavenProfile);

    return hasProfile;
  }

}
