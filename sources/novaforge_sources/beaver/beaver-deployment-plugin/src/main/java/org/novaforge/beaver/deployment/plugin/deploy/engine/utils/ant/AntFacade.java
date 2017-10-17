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
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.ant;

import java.io.File;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.Chmod;
import org.apache.tools.ant.taskdefs.Concat;
import org.apache.tools.ant.taskdefs.ExecuteOn;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Patch;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.taskdefs.War;
import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.novaforge.beaver.exception.BeaverException;

/**
 * {@link AntFacade} is a facade to use {@link Ant} object.
 * 
 * @author Guillaume Lamirand
 * @version 2.0
 */
public class AntFacade
{

  private AntFacade()
  {
    // Utility class should have private explicit constructor ( Sonar rule : Hide Utility Class Constructor )
  }

  /**
   * Pack to war archive a source directory.
   * 
   * @param pDataSrc
   *          represents the source to war.
   * @param pDesFile
   *          represents the destination war file.
   * @throws BeaverException
   */
  public static void packToWar(final File pDataSrc, final File pDesFile) throws BeaverException
  {
    final War warTask = new War();

    warTask.setBasedir(pDataSrc);
    warTask.setDestFile(pDesFile);
    warTask.setNeedxmlfile(false);
    warTask.setManifest(new File(pDataSrc, "META-INF/MANIFEST.MF"));
    warTask.setProject(new AntProject());
    warTask.setTaskName("War");

    warTask.execute();
    checkReturnCode(warTask);
  }

  /**
   * Pack to jar archive a source directory.
   * 
   * @param pDataSrc
   *          represents the source to jar.
   * @param pDesFile
   *          represents the destination jar file.
   * @throws BeaverException
   */
  public static void packToJar(final File pDataSrc, final File pDesFile) throws BeaverException
  {
    final Jar jarTask = new Jar();

    jarTask.setBasedir(pDataSrc);
    jarTask.setDestFile(pDesFile);
    jarTask.setManifest(new File(pDataSrc, "META-INF/MANIFEST.MF"));
    jarTask.setProject(new AntProject());
    jarTask.setTaskName("Jar");

    jarTask.execute();
    checkReturnCode(jarTask);
  }

  /**
   * Setup permission to a element
   * 
   * @param pRecursive
   *          Boolean about rescursivity of persmission (chmod -R).
   * @param pRight
   *          represents right to set.
   * @param pElement
   *          represents the file or directory to setup.
   * @param pTypeElement
   *          represents type of element which will be set
   * @throws BeaverException
   */
  public static void setPermission(final boolean pRecursive, final String pRight, final File pElement)
      throws BeaverException
  {

    final Chmod chmodTask = new Chmod();
    if (pElement.isDirectory() == true)
    {
      chmodTask.setDir(pElement);
    }
    else if (pElement.isFile() == true)
    {
      chmodTask.setFile(pElement);
    }
    if (pRecursive == true)
    {
      chmodTask.setPerm("-R");
    }
    chmodTask.setVerbose(true);
    chmodTask.setParallel(true);

    chmodTask.setPerm(pRight);

    chmodTask.setProject(new AntProject());
    chmodTask.setTaskName("Permissions");

    chmodTask.execute();
    checkReturnCode(chmodTask);

  }

  /**
   * Replace token into a file by a value
   * 
   * @param pFile
   *          represent the file where we want to change something.
   * @param pToken
   *          represents token to change.
   * @param pValue
   *          represents the value of changing.
   * @param pIncludes
   *          represents the including file.
   * @throws BeaverException
   */
  public static void replaceToken(final File pFile, final String pToken, final String pValue,
      final String pIncludes, final String pExcludes) throws BeaverException
  {

    final Replace replaceTask = new Replace();

    if (pFile.isDirectory() == true)
    {
      replaceTask.setDir(pFile);
    }
    if (pFile.isFile() == true)
    {
      replaceTask.setFile(pFile);
    }
    replaceTask.setToken(pToken);
    replaceTask.setValue(pValue);
    if ("".equals(pIncludes) == false)
    {
      replaceTask.setIncludes(pIncludes);
    }
    if ("".equals(pExcludes) == false)
    {
      replaceTask.setExcludes(pExcludes);
    }

    // Verbose mode
    replaceTask.setSummary(true);

    replaceTask.setProject(new AntProject());
    replaceTask.setTaskName("Replace");
    replaceTask.execute();
    checkReturnCode(replaceTask);

  }

  /**
   * Replace regular expression by substitute.
   * 
   * @param pFile
   *          represent the file where we want to change something
   * @param pToken
   *          represents token to change
   * @param pValue
   *          represents the value of changing
   * @throws BeaverException
   */
  public static void replaceRegExp(final File pFile, final String pPatern, final String pSubstitution)
      throws BeaverException
  {

    final ReplaceRegExp replaceregexpTask = new ReplaceRegExp();

    replaceregexpTask.setFile(pFile);

    replaceregexpTask.createRegexp().setPattern(pPatern);
    replaceregexpTask.createSubstitution().setExpression(pSubstitution);

    replaceregexpTask.setProject(new AntProject());
    replaceregexpTask.setTaskName("ReplaceRegExp");
    replaceregexpTask.setFlags("g");
    replaceregexpTask.execute();

    checkReturnCode(replaceregexpTask);

  }

  /**
   * Replace token into a file by a value
   * 
   * @param pFile
   *          represent the file where we want to change something
   * @param pToken
   *          represents token to change
   * @param pValue
   *          represents the value of changing
   * @throws BeaverException
   */
  public static void concatFiles(final String pFileSrc, final String pFileDes) throws BeaverException
  {

    final Concat concatTask = new Concat();
    concatTask.setAppend(true);
    concatTask.setFixLastLine(true);

    final FileList filelist = new FileList();
    filelist.setFiles(pFileSrc);
    concatTask.addFilelist(filelist);

    final File fileDes = new File(pFileDes);
    concatTask.setDestfile(fileDes);
    concatTask.setProject(new AntProject());
    concatTask.setTaskName("Concat");
    concatTask.execute();

    checkReturnCode(concatTask);

  }

  /**
   * Execute a bash script
   * 
   * @param pScript
   *          represent the script to execute
   * @throws BeaverException
   */
  public static void executeBashScript(final File pScript) throws BeaverException
  {
    final ExecuteOn execTask = new ExecuteOn();
    execTask.setExecutable("sh");
    execTask.setParallel(true);
    execTask.setSkipEmptyFilesets(true);
    execTask.setAppend(true);

    final FileSet fileset = new FileSet();
    fileset.setFile(pScript);
    execTask.addFileset(fileset);

    execTask.setProject(new AntProject());
    execTask.setTaskName("Execute");
    execTask.execute();

    checkReturnCode(execTask);
  }

  /**
   * Execute a bat script
   * 
   * @param pScript
   *          represent the script to execute
   * @throws BeaverException
   */
  public static void executeBatScript(final File pScript) throws BeaverException
  {
    final ExecuteOn execTask = new ExecuteOn();
    execTask.setExecutable(pScript.getAbsolutePath());
    execTask.setParallel(true);
    execTask.setSkipEmptyFilesets(true);
    execTask.setVerbose(true);

    final FileSet fileset = new FileSet();
    fileset.setFile(pScript);
    execTask.addFileset(fileset);

    execTask.setProject(new AntProject());
    execTask.setTaskName("Execute");

    execTask.execute();
    checkReturnCode(execTask);

  }

  /**
   * Patches a directory by applying a 'diff' file to it; requires "patch" to be on the execution path.
   * 
   * @param pPatchFile
   *          represent the patch file to apply
   * @param pDirDes
   *          represents target directory to execute patch command
   * @param pKeepBackup
   *          keep backups of the unpatched files
   * @param pQuietMode
   *          work silently unless an error occurs
   * @throws BeaverException
   */
  public static void patch(final File pPatchFile, final int pStrip, final File pDirDes,
      final boolean pKeepBackup, final boolean pQuietMode) throws BeaverException
  {

    final Patch patchTask = new Patch();
    patchTask.setDir(pDirDes);
    patchTask.setPatchfile(pPatchFile);
    patchTask.setStrip(pStrip);
    patchTask.setBackups(pKeepBackup);
    patchTask.setQuiet(pQuietMode);

    patchTask.setProject(new AntProject());
    patchTask.setTaskName("Patch");
    patchTask.execute();

    checkReturnCode(patchTask);

  }

  private static void checkReturnCode(final Task pTask) throws BeaverException
  {

    final String taskname = pTask.getProject().getProperty("ErrorTask");
    if (null != taskname)
    {

      final StringBuilder errorBuilder = new StringBuilder();
      errorBuilder.append("Error during *");
      errorBuilder.append(taskname);
      errorBuilder.append("* process \n ");
      errorBuilder.append(pTask.getProject().getProperty("ErrorMsg"));

      final String executeTaskError = pTask.getProject().getProperty("ErrorExecute");
      if (null != executeTaskError)
      {
        errorBuilder.append("\n ");
        errorBuilder.append(executeTaskError);
      }
      throw new BeaverException(errorBuilder.toString());
    }
  }

}
