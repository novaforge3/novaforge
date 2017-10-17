/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.internal.helpers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.diff.SVNDeltaProcessor;
import org.tmatesoft.svn.core.io.diff.SVNDiffWindow;

/**
 * @author sbenoist
 */
public final class SVNExportHelper
{
  public static final String WORKING_COPY_PREFIX = "working_copy";

  public static final String ZIP_EXPORT_PREFIX   = "export";

  private static final Log   log                 = LogFactory.getLog(SVNExportHelper.class);

  public static void export(final File pExportDirectory, final SVNRepository repository,
      final boolean pIsRecursive) throws SVNException
  {
    // Get latest repository revision. We will export repository contents at this very revision.
    final long latestRevision = repository.getLatestRevision();

    final ISVNReporterBaton reporterBaton = new ExportReporterBaton(latestRevision);

    final ISVNEditor exportEditor = new SVNExportHelper.ExportEditor(pExportDirectory);
    repository.update(latestRevision, null, pIsRecursive, reporterBaton, exportEditor);
  }

  /*
   * ReporterBaton implementation that always reports 'empty wc' state.
   */
  public static class ExportReporterBaton implements ISVNReporterBaton
  {
    private final long exportRevision;

    public ExportReporterBaton(final long revision)
    {
      exportRevision = revision;
    }

    @Override
    public void report(final ISVNReporter reporter) throws SVNException
    {
      try
      {
        /*
         * Here empty working copy is reported.
         * ISVNReporter includes methods that allows to report mixed-rev working copy and even let server
         * know that some files or directories are locally missing or locked.
         */
        reporter.setPath("", null, exportRevision, SVNDepth.INFINITY, true);

        /*
         * Don't forget to finish the report!
         */
        reporter.finishReport();
      }
      catch (final SVNException svne)
      {
        reporter.abortReport();
        if (log.isDebugEnabled())
        {
          log.debug("Report failed.");
        }
      }
    }
  }

  /*
   * ISVNEditor implementation that will add directories and files into the target directory accordingly to
   * update instructions sent by the server.
   */
  public static class ExportEditor implements ISVNEditor
  {

    private final File              myRootDirectory;
    private final SVNDeltaProcessor myDeltaProcessor;

    /*
     * root - the local directory where the node tree is to be exported into.
     */
    public ExportEditor(final File root)
    {
      myRootDirectory = root;
      /*
       * Utility class that will help us to transform 'deltas' sent by the server to the new file contents.
       */
      myDeltaProcessor = new SVNDeltaProcessor();
    }

    /*
     * Server reports revision to which application of the further instructions will update working copy to.
     */
    @Override
    public void targetRevision(final long revision)
    {
    }

    /*
     * Called before sending other instructions.
     */
    @Override
    public void openRoot(final long revision)
    {
    }

    /*
     * Called when a new directory has to be added.
     * For each 'addDir' call server will call 'closeDir' method after all children of the added directory
     * are added.
     * This implementation creates corresponding directory below root directory.
     */
    @Override
    public void addDir(final String path, final String copyFromPath, final long copyFromRevision)
        throws SVNException
    {
      final File newDir = new File(myRootDirectory, path);
      if (!newDir.exists())
      {
        if (!newDir.mkdirs())
        {
          final SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR,
              "error: failed to add the directory ''{0}''.", newDir);
          throw new SVNException(err);
        }
      }
      if (log.isDebugEnabled())
      {
        log.debug("dir added: " + path);
      }
    }

    /*
     * Called when there is an existing directory that has to be 'opened' either to modify this directory
     * properties or to process other files and directories inside this directory.
     * In case of export this method will never be called because we reported that our 'working copy' is
     * empty and so server knows that there are no 'existing' directories.
     */
    @Override
    public void openDir(final String path, final long revision)
    {
    }

    /*
     * Instructs to change opened or added directory property.
     * This method is called to update properties set by the user as well as those created automatically,
     * like "svn:committed-rev". See SVNProperty class for default property names.
     * When property has to be deleted value will be 'null'.
     */

    @Override
    public void changeDirProperty(final String name, final SVNPropertyValue property)
    {
    }

    /*
     * Called when a new file has to be created.
     * For each 'addFile' call server will call 'closeFile' method after sending file properties and
     * contents.
     * This implementation creates empty file below root directory, file contents will be updated later, and
     * for empty files may not be sent at all.
     */
    @Override
    public void addFile(final String path, final String copyFromPath, final long copyFromRevision)
        throws SVNException
    {
      final File file = new File(myRootDirectory, path);
      if (file.exists() == false)
      {
        try
        {
          file.createNewFile();
        }
        catch (final IOException e)
        {
          final SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR,
              "error: cannot create new  file ''{0}''", file);
          throw new SVNException(err);
        }
      }
    }

    /*
     * Called when there is an existing files that has to be 'opened' either to modify file contents or
     * properties.
     * In case of export this method will never be called because we reported that our 'working copy' is
     * empty and so server knows that there are no 'existing' files.
     */
    @Override
    public void openFile(final String path, final long revision)
    {
    }

    /*
     * Instructs to add, modify or delete file property. In this example we skip this instruction, but
     * 'real' export operation may inspect 'svn:eol-style' or 'svn:mime-type' property values to transfor
     * file contents propertly after receiving.
     */

    @Override
    public void changeFileProperty(final String path, final String name, final SVNPropertyValue property)
    {
    }

    /*
     * Called before sending 'delta' for a file. Delta may include instructions on how to create a file or
     * how to modify existing file. In this example delta will always contain instructions on how to create
     * a new file and so we set up deltaProcessor with 'null' base file and target file to which we would
     * like to store the result of delta application.
     */
    @Override
    public void applyTextDelta(final String path, final String baseChecksum) throws SVNException
    {
      myDeltaProcessor.applyTextDelta((File) null, new File(myRootDirectory, path), false);
    }

    /*
     * Server sends deltas in form of 'diff windows'. Depending on the file size there may be several diff
     * windows. Utility class SVNDeltaProcessor processes these windows for us.
     */
    @Override
    public OutputStream textDeltaChunk(final String path, final SVNDiffWindow diffWindow) throws SVNException
    {
      return myDeltaProcessor.textDeltaChunk(diffWindow);
    }

    /*
     * Called when all diff windows (delta) is transferred.
     */
    @Override
    public void textDeltaEnd(final String path)
    {
      myDeltaProcessor.textDeltaEnd();
    }

    /*
     * Called when file update is completed. This call always matches addFile or openFile call.
     */
    @Override
    public void closeFile(final String path, final String textChecksum)
    {
      if (log.isDebugEnabled())
      {
        log.debug("file added: " + path);
      }
    }

    /*
     * Called when all child files and directories are processed. This call always matches addDir, openDir
     * or openRoot call.
     */
    @Override
    public void closeDir()
    {
    }

    /*
     * Insturcts to delete an entry in the 'working copy'. Of course will not be called during export
     * operation.
     */
    @Override
    public void deleteEntry(final String path, final long revision)
    {
    }

    /*
     * Called when directory at 'path' should be somehow processed, but authenticated user (or anonymous
     * user) doesn't have enough access rights to get information on this directory (properties, children).
     */
    @Override
    public void absentDir(final String path)
    {
    }

    /*
     * Called when file at 'path' should be somehow processed, but authenticated user (or anonymous user)
     * doesn't have enough access rights to get information on this file (contents, properties).
     */
    @Override
    public void absentFile(final String path)
    {
    }

    /*
     * Called when update is completed.
     */
    @Override
    public SVNCommitInfo closeEdit()
    {
      return null;
    }

    /*
     * Called when update is completed with an error or server requests client to abort update operation.
     */
    @Override
    public void abortEdit()
    {
    }

  }
}
