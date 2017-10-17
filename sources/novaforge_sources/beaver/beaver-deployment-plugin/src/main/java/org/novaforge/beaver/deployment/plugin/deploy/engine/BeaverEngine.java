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
package org.novaforge.beaver.deployment.plugin.deploy.engine;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec.ExecResult;
import org.novaforge.beaver.exception.BeaverException;

/**
 * {@link BeaverEngine} represents contract between engine and groovy process.
 * 
 * @see org.novaforge.beaver.deployment.plugin.deploy.engine.AbstractEngine
 * @see org.novaforge.beaver.deployment.plugin.deploy.engine.impl.EngineImpl
 * @see org.novaforge.beaver.deployment.plugin.deploy.api.impl.EngineSimulateImpl
 * @author Guillaume Lamirand
 * @version 2.0
 */
/**
 * @author Guillaume Lamirand
 */
public interface BeaverEngine
{

  /**
   * Return <code>true</code> if current engine is simulate mode
   * 
   * @return <code>true</code> if current engine is simulate mode, default is <code>false</code>
   */
  boolean isSimulateMode();

  /**
   * Return an implementation of {@link UserService} used to manage user
   * 
   * @return {@link UserService} implementation
   */
  UserService getUserService();

  /**
   * Return an implementation of {@link RepositoryService} used to manage repository
   * 
   * @return {@link RepositoryService} implementation
   */
  RepositoryService getRepositoryService();

  /**
   * Return an implementation of {@link SSLService} used to manage repository
   * 
   * @return {@link SSLService} implementation
   */
  SSLService getSSLService();

  /**
   * Return an implementation of {@link SystemdService} used to manage user
   * 
   * @return {@link SystemdService} implementation
   */
  SystemdService getSystemdService();

  /**
   * Return an implementation of {@link HTTPService} used to manage HTTP requests
   * 
   * @return {@link HTTPService} implementation
   */
  HTTPService getHTTPService();

  /**
   * It will look for your property inside the global process.
   * <p>
   * The product id and server id will be retrieve from current process calling this method.
   * </p>
   * 
   * @param pRessourceKey
   *          represents the name of the ressource needed.
   * @return the ressource found after the search
   * @throws BeaverException
   */
  String getResource(final String pRessourceKey) throws BeaverException;

  /**
   * It will look for your property inside the global process.
   * <p>
   * The server id will be retrieve from current process calling this method.
   * </p>
   * 
   * @param pProductId
   *          represents the product id declaring the resource id
   * @param pRessourceKey
   *          represents the name of the ressource needed.
   * @return the ressource found after the search
   * @throws BeaverException
   */
  String getResource(final String pProductId, final String pRessourceKey) throws BeaverException;

  /**
   * It will look for your property inside the global process.
   * 
   * @param pServerId
   *          represents the seerver id where the product is located
   * @param pProductId
   *          represents the product id declaring the resource id
   * @param pRessourceKey
   *          represents the name of the ressource needed.
   * @return the ressource found after the search
   * @throws BeaverException
   */
  String getResource(final String pServerId, final String pProductId, final String pRessourceKey)
      throws BeaverException;

  /**
   * It will look for the previous value of the property inside the deployment persistence file.
   * <p>
   * The product id and server id will be retrieve from current process calling this method.
   * </p>
   * 
   * @param pRessourceKey
   *          represents the name of the ressource needed.
   * @return the ressource found after the search
   * @throws BeaverException
   */
  String getPreviousResource(final String pRessourceKey) throws BeaverException;

  /**
   * It will look for the previous value of the property inside the deployment persistence file.
   * <p>
   * The server id will be retrieve from current process calling this method.
   * </p>
   * 
   * @param pProductId
   *          represents the product id declaring the resource id
   * @param pRessourceKey
   *          represents the name of the ressource needed.
   * @return the ressource found after the search
   * @throws BeaverException
   */
  String getPreviousResource(final String pProductId, final String pRessourceKey) throws BeaverException;

  /**
   * It will look for the previous value of the property inside the deployment persistence file.
   * 
   * @param pServerId
   *          represents the seerver id where the product is located
   * @param pProductId
   *          represents the product id declaring the resource id
   * @param pRessourceKey
   *          represents the name of the ressource needed.
   * @return the ressource found after the search
   * @throws BeaverException
   */
  String getPreviousResource(final String pServerId, final String pProductId, final String pRessourceKey)
      throws BeaverException;

  /**
   * Does the actual copy of the file to a dir.
   * 
   * @param pSrc
   *          represents the file or the directory to copy.
   * @param pDes
   *          directory name of destination file.
   * @throws BeaverException
   */
  void copy(final String pSrc, final String pDes) throws BeaverException;

  /**
   * Does the actual copy of the file to another file.
   * 
   * @param pSrc
   *          represents the file or the directory to copy.
   * @param pDes
   *          directory name of destination file.
   * @throws BeaverException
   */
  void copyToFile(final String pSrc, final String pDes) throws BeaverException;

  /**
   * Create a symlink for the resource given to the target link
   * 
   * @param pResource
   *          it has to be an existing file or an existing directory
   * @param pTargetLink
   *          represents the target link to create
   * @throws BeaverException
   */
  void createSymlink(final String pResource, final String pTargetLink) throws BeaverException;

  /**
   * unpack file into destination directory.
   * 
   * @param pFile
   *          represents the path of the archive
   * @param pDes
   *          represents the directory name of destination.
   * @throws BeaverException
   */
  void unpackFile(final String pFile, final String pDes) throws BeaverException;

  /**
   * unpack file into destination directory.
   * 
   * @param pFile
   *          represents the file of the archive
   * @param pDes
   *          represents the directory file of destination.
   * @throws BeaverException
   */
  void unpackFile(final Path pFile, final Path pDes) throws BeaverException;

  /**
   * archive a directory in a file.
   * 
   * @param pSrc
   *          represents the path to archive
   * @param pDes
   *          represents the directory name of destination.
   * @throws BeaverException
   */
  void pack(final String pSrc, final String pDes) throws BeaverException;

  /**
   * archive a directory in a file.
   * 
   * @param pSrc
   *          represents the path to archive
   * @param pDes
   *          represents the directory name of destination.
   * @throws BeaverException
   */
  void pack(final File pSrc, final File pDes) throws BeaverException;

  /**
   * Set a permission to an element including all element directories and files
   * 
   * @param pRecursive
   *          Boolean about rescursivity of permission (chmod -R)
   * @param pRight
   *          represents right to set
   * @param pElement
   *          represents the file or directory to setup
   * @throws BeaverException
   */
  void setPermissions(final boolean pRecursive, final String pRight, final String pElement)
      throws BeaverException;

  /**
   * Set a permission to an element including only files
   * 
   * @param pRecursive
   *          Boolean about rescursivity of permission (chmod -R)
   * @param pRight
   *          represents right to set
   * @param pElement
   *          represents the file or directory to setup
   * @throws BeaverException
   */
  void setPermissionsOnFiles(final boolean pRecursive, final String pRight, final String pElement)
      throws BeaverException;

  /**
   * Set a permission to an element including only directories
   * 
   * @param pRecursive
   *          Boolean about rescursivity of permission (chmod -R)
   * @param pRight
   *          represents right to set
   * @param pElement
   *          represents the file or directory to setup
   * @throws BeaverException
   */
  void setPermissionsOnDirectories(final boolean pRecursive, final String pRight, final String pElement)
      throws BeaverException;

  /**
   * Set the owner of directory TODO: Change the group/user order
   * 
   * @param pRecursive
   *          Boolean about rescursivity of permission (chown -R)
   * @param pGroup
   *          represents group of the user
   * @param pUser
   *          represents the user
   * @param pDirectory
   *          represents the file or directory to setup
   * @throws BeaverException
   */
  void setOwner(final boolean pRecursive, final String pGroup, final String pUser, final String pDirectory)
      throws BeaverException;

  /**
   * Set the owner of directory and fail, or not, on error TODO: Change the group/user order
   * 
   * @param pRecursive
   *          Boolean about rescursivity of permission (chown -R)
   * @param pGroup
   *          represents group of the user
   * @param pUser
   *          represents the user
   * @param pDirectory
   *          represents the file or directory to setup
   * @param throwsOnError
   *          throw an exception on error, else hide the error
   * @throws BeaverException
   */
  void setOwner(final boolean pRecursive, final String pGroup, final String pUser, final String pDirectory,
      final boolean throwsOnError) throws BeaverException;

  /**
   * Change the group owner of the directory
   * 
   * @param pRecursive
   *          Boolean about rescursivity of permission (chown -R)
   * @param pGroup
   *          represents group of the user
   * @param pDirectory
   *          represents the file or directory to setup
   * @throws BeaverException
   */
  void changeGroup(final boolean pRecursive, final String pGroup, final String pDirectory)
      throws BeaverException;

  /**
   * Get value of the group find with given regular expression
   * 
   * @param pFile
   *          represent the file where we want to get something
   * @param pPatern
   *          represents the regular expression to match
   * @return value found
   * @throws BeaverException
   */
  String getValueFromRegex(final String pFile, final String pPatern) throws BeaverException;

  /**
   * Replace a token into a file by a value
   * 
   * @param pFile
   *          represent the file where we want to change something
   * @param pToken
   *          represents token to change
   * @param pValue
   *          represents the value of changing
   * @throws BeaverException
   */
  void replaceElement(final String pFile, final String pToken, final String pValue) throws BeaverException;

  /**
   * Replace a token into a file by a value. You can specify some including of excluding files.
   * 
   * @param pFile
   *          represent the directory where we want to change something
   * @param pToken
   *          represents token to change
   * @param pValue
   *          represents the value of changing
   * @param pIncludes
   *          represents the including files, such as "*.sh"
   * @param pExcludes
   *          represents the exluding files
   * @throws BeaverException
   */
  void replaceElementFilter(final String pFile, final String pToken, final String pValue,
      final String pIncludes, final String pExcludes) throws BeaverException;

  /**
   * Replace regular expression by a substitute.
   * 
   * @param pFile
   *          represent the file where we want to change something
   * @param pPatern
   *          represents the regular expression to match
   * @param pSubstitution
   *          represents the substitute
   * @throws BeaverException
   */
  void replaceExpression(final String pFile, final String pPatern, final String pSubstitution)
      throws BeaverException;

  /**
   * Concat two files into the second
   * 
   * @param pFileSrc
   *          represents the first file
   * @param pFileDes
   *          represents second file to concat and the destination
   * @throws BeaverException
   */
  void concatFiles(final String pFileSrc, final String pFileDes) throws BeaverException;

  /**
   * Allow to execute a unix script with sh commande
   * 
   * @param pFile
   *          represents the script to run
   * @throws BeaverException
   */
  void executeScript(final String pFile) throws BeaverException;

  /**
   * Allow to execute a mysql command
   * 
   * @param pMysqlBin
   *          represents the mysql binary
   * @param pMysqlPort
   *          represents the port of mysql
   * @param pUser
   *          the mysql user used to login
   * @param pPwd
   *          the password
   * @param pCommand
   *          represents the command to execute
   * @throws BeaverException
   */
  void executeMysql(final String pMysqlBin, final String pMysqlPort, final String pUser, final String pPwd,
      final String pCommand) throws BeaverException;

  /**
   * Test is the database exist.
   * 
   * @param pMysqlBin
   *          represents the mysql binary
   * @param pMysqlPort
   *          represents the port of mysql
   * @param pUser
   *          the mysql user used to login
   * @param pPwd
   *          the password
   * @param pDatabase
   *          represents the name of the database to test
   * @return <code>true</code> if the database exist, <code>false</code> otherwise.
   */
  boolean isDatabaseExists(final String pMysqlBin, final String pMysqlPort, final String pUser,
      final String pPwd, final String pDatabase);

  /**
   * Allow to execute a mysql script
   * 
   * @param pMysqlBin
   *          represents the mysql binary
   * @param pMysqlPort
   *          represents the port of mysql
   * @param pUser
   *          the mysql user used to login
   * @param pPwd
   *          the password
   * @param pScript
   *          represents the file where are stored mysql commands
   * @throws BeaverException
   */
  void executeMysqlScript(String pMysqlBin, String pMysqlPort, String pUser, String pPwd, String pScript)
      throws BeaverException;

  /**
   * return current environment linux or windows
   * 
   * @return the environment
   * @throws BeaverException
   */
  String getEnvironment();

  /**
   * return boolean true if your environment is windows, otherwise false will be returned.
   * 
   * @return boolean
   * @throws BeaverException
   */
  boolean isWindows();

  /**
   * return boolean true if your environment is linux, otherwise false will be returned.
   * 
   * @return booleanNovaForgeEngineAPI
   * @throws BeaverException
   */
  boolean isLinux();

  /**
   * Create the directory if not exits
   * 
   * @param pDirectory
   *          represents the directory to create
   * @throws BeaverException
   *           thrown if directory cannot be created
   */
  void createDirectory(final String pDirectory) throws BeaverException;

  /**
   * Create the file if not exits
   * 
   * @param pFile
   *          represents the path of the file to create
   * @throws BeaverException
   *           thrown if file cannot be created
   */
  void createNewFile(final String pFile) throws BeaverException;

  /**
   * Delete the file/directory given. If it is a directory, all tree will be deleted too.
   * 
   * @param pElementToDelete
   *          represents the directory or regular file to delete
   * @throws BeaverException
   */
  void delete(final String pElementToDelete) throws BeaverException;

  /**
   * Return the version of the current product
   * 
   * @return the version
   * @throws BeaverException
   */
  String getMyVersion();

  /**
   * Return the previous version of the current product
   * 
   * @return the version
   * @throws BeaverException
   */
  String getMyPreviousVersion();

  /**
   * return the version of product specified by the parameter
   * 
   * @param pProductId
   *          represents the product's id
   * @return the version
   */
  String getProductVersion(String pProductId);

  /**
   * Return the version of the current data
   * 
   * @return the version
   * @throws BeaverException
   */
  String getDataVersion() throws BeaverException;

  /**
   * return the status of the current product
   * 
   * @return status
   */
  String getMyStatus();

  /**
   * return the update version of the current product
   * 
   * @return status
   */
  String getMyUpdateVersion();

  /**
   * return the productId of the current product
   * 
   * @return productId
   */
  String getMyProductId();

  /**
   * Get current server id
   * 
   * @return the id of the current server
   */
  String getServerId();

  /**
   * Add a property to a properties file
   * 
   * @param pFileName
   *          The path of the properties file
   * @param pKey
   *          the key of the property
   * @param pValue
   *          the value of the property
   * @throws BeaverException
   *           thrown if property cannot be added
   */
  void addPropertyFile(final String pFileName, final String pKey, final String pValue) throws BeaverException;

  /**
   * Allow to add a line after another into a file
   * 
   * @param pFile
   *          the path of the file
   * @param pBefore
   *          represents the line before this you want to add
   * @param pLine
   *          represents the line you want to add
   * @return <code>true</code> if a line was added, <code>false</code> otherwise
   * @throws BeaverException
   */
  int addLineToFile(final String pFile, final String pBefore, final String pLine) throws BeaverException;

  /**
   * Move source directory to destination
   * 
   * @param pSource
   *          the path of the source to move
   * @param pDestination
   *          represents the path of the destination of source
   * @throws BeaverException
   */
  void moveDir(final String pSource, final String pDestination) throws BeaverException;

  /**
   * Move elements from regular expression to destination
   * 
   * @param pRegEx
   *          the regular expression use to set up the source elements
   * @param pDestination
   *          represents the path of the destination of source
   * @throws BeaverException
   */
  void move(final String pRegEx, final String pDestination) throws BeaverException;

  /**
   * Comment lines between two lines included themself. It will use the parameter pType to comment the lines
   * (#;//;..;)
   * 
   * @param pFile
   *          the path of the file
   * @param pStart
   *          represents the first line
   * @param pEnd
   *          represents the last line
   * @param pType
   *          represents the token used to comment a line
   * @throws BeaverException
   */
  void commentLines(final String pFile, final String pStart, final String pEnd, final String pType)
      throws BeaverException;

  /**
   * Delete lines between two lines included themself.
   * 
   * @param pFile
   *          the path of the file
   * @param pStart
   *          represents the first line
   * @param pEnd
   *          represents the last line
   * @throws BeaverException
   */
  void deleteLines(final String pFile, final String pStart, final String pEnd) throws BeaverException;

  /**
   * update new file with old file if matching key exist and no exclusions are fixed.
   * 
   * @param pNewFile
   *          New .properties file to update
   * @param pOldFile
   *          Old .properties file to keep
   * @param pExclusionList
   *          list of exclusion key to ignore when updating new file
   * @throws BeaverException
   */
  void propertiesFilesMerger(final String pOldFile, final String pNewFile, final List<String> pExclusionList)
      throws BeaverException;

  /**
   * Patches a directory by applying a 'diff' file to it; requires "patch" to be on the execution path.
   * 
   * @param pPatchFile
   *          represents the patch file to apply
   * @param pDirectoryDes
   *          represents the destination directory to execute patch command
   * @param pKeepBackup
   *          keep backups of the unpatched files
   * @throws BeaverException
   */
  void patch(final String pPatchFile, final int pStrip, final String pDirectoryDes, final boolean pKeepBackup)
      throws BeaverException;

  /**
   * Returns an object represented by pJson
   * 
   * @param pJson
   *          The json string to parse
   * @return An object represented by pJson
   */
  Object parseJson(String pJson);

  /**
   * Execute a shell command for linux systems
   * 
   * @param pCommand
   *          The command to execute
   * @param pArgs
   *          The arguments to add to the command
   * @throws BeaverException
   */
  void executeCommand(final String pCommand, final String... pArgs) throws BeaverException;

  /**
   * Execute a shell command for linux systems
   * 
   * @param pCommand
   *          The command to execute
   * @param pArgs
   *          The arguments to add to the command
   * @return The Command Result
   * @throws BeaverException
   */
  ExecResult executeCommandAndGetResult(final String pCommand, final String... pArgs) throws BeaverException;

  /**
   * Execute a shell command for linux systems
   * 
   * @param pMavenProfile
   *          The profile to test
   * @throws BeaverException
   */
  boolean hasMavenProfile(final String pMavenProfile) throws BeaverException;

}
