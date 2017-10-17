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

package org.novaforge.forge.plugins.scm.svn.agent.internal.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.search.JavaSearchResult;
import org.novaforge.forge.commons.technical.search.SearchResult;
import org.novaforge.forge.commons.technical.search.SearchService;
import org.novaforge.forge.core.plugins.categories.scm.SCMServiceException;
import org.novaforge.forge.plugins.scm.svn.agent.dto.AffectedPathDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNNodeEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNSearchResultDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.ScmLogEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.internal.helpers.SVNExportDirectory;
import org.novaforge.forge.plugins.scm.svn.agent.internal.helpers.SVNExportHelper;
import org.novaforge.forge.plugins.scm.svn.agent.internal.helpers.ZipCreatorHelper;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNRepositoryException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNRepositoryService;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @author rols-p
 */
public class SVNRepositoryServiceImpl implements SVNRepositoryService
{
  private static final Log LOGGER = LogFactory.getLog(SVNFacadeServiceImpl.class);

  private SearchService    searchService;

  @Override
  public void createRepository(final String pRepositoriesPath, final String pRepositoryName)
      throws IOException, SVNRepositoryException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : pRepositoriesPath = " + pRepositoriesPath);
      LOGGER.debug("Input : pRepositoryName = " + pRepositoryName);
    }

    // Initializes the library (it must be done before ever using the
    // library itself)
    FSRepositoryFactory.setup();

    final File repoDirectory = new File(getRepositoryPath(pRepositoriesPath, pRepositoryName));

    if (repoDirectory.exists() == true)
    {
      throw new IllegalArgumentException(MessageFormat.format("the SVN repository \"{0}\" already exists.",
          pRepositoryName));
    }

    SVNRepository tgtRepository;
    try
    {
      final SVNURL tgtURL = SVNRepositoryFactory.createLocalRepository(repoDirectory, true, false);
      tgtRepository = SVNRepositoryFactory.create(tgtURL);
    }
    catch (final SVNException e)
    {
      throw new SVNRepositoryException("unable to create repository" + pRepositoryName);
    }
    tgtRepository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager());

    changePermissions(repoDirectory);
  }

  @Override
  public void deleteRepository(final String pRepositoriesPath, final String pRepositoryName)
      throws IOException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : pRepositoriesPath = " + pRepositoriesPath);
      LOGGER.debug("Input : pRepositoryName = " + pRepositoryName);
    }

    // just delete the directory
    final File directory = new File(getRepositoryPath(pRepositoriesPath, pRepositoryName));
    FileUtils.deleteDirectory(directory);
  }

  /**
   * @param pRepositoryDir
   * @throws IOException
   * @throws SCMServiceException
   */
  private static void changePermissions(final File pRepositoryDir) throws IOException
  {

    final String os = System.getProperty("os.name");
    if ((os != null) && (os.toLowerCase().startsWith("windows") == false))
    {
      new ProcessBuilder("chmod", "-R", "g+w", pRepositoryDir.getAbsolutePath()).start();
    }

  }

  private static String getRepositoryPath(final String pRepositoriesPath, final String pRepositoryName)
  {
    return new StringBuilder(pRepositoriesPath).append(File.separatorChar).append(pRepositoryName).toString();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ScmLogEntryDTO> getLastCommit(final String pRepositoriesPath, final String pUserName,
      final int pNbComit) throws SVNAgentException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : pRepositoriesPath = " + pRepositoriesPath);
      LOGGER.debug("Input : pUserName = " + pUserName);
    }

    final List<ScmLogEntryDTO> scmLogEntries = new ArrayList<ScmLogEntryDTO>();

    SVNRepository repository = null;

    try
    {
      @SuppressWarnings("deprecation")
      final SVNURL svnUrl = SVNURL.parseURIDecoded(pRepositoriesPath);
      LOGGER.info("svnUrl : " + svnUrl.getPath());
      DAVRepositoryFactory.setup();
      SVNRepositoryFactoryImpl.setup();
      FSRepositoryFactory.setup();

      repository = SVNRepositoryFactory.create(svnUrl);
      final ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(pUserName,
          "");
      repository.setAuthenticationManager(authManager);

      repository.testConnection();

      long startRevision = 0;
      long endRevision = -1; // HEAD (the latest) revision

      if (repository.getLatestRevision() > 0)
      {
        endRevision = repository.getLatestRevision();
        if (((repository.getLatestRevision() - pNbComit) + 1) > 0)
        {
          startRevision = (repository.getLatestRevision() - pNbComit) + 1;
        }
      }

      final Collection<SVNLogEntry> logEntries = repository.log(new String[] { "" }, null, startRevision,
          endRevision, true, true);

      for (final SVNLogEntry svnLogEntry : logEntries)
      {
        final Date comitDate = new Date();
        comitDate.setTime(svnLogEntry.getDate().getTime());
        final ScmLogEntryDTO logEntry = new ScmLogEntryDTO(svnLogEntry.getRevision(),
            svnLogEntry.getAuthor(), svnLogEntry.getMessage(), comitDate);

        final List<AffectedPathDTO> affectedPathList = new ArrayList<AffectedPathDTO>();

        if (svnLogEntry.getChangedPaths().size() > 0)
        {

          final Set<String> changedPathsSet = svnLogEntry.getChangedPaths().keySet();
          for (final Iterator<String> changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();)
          {

            final SVNLogEntryPath entryPath = svnLogEntry.getChangedPaths().get(changedPaths.next());

            final AffectedPathDTO affectedPath = new AffectedPathDTO(String.valueOf(entryPath.getType()),
                entryPath.getPath());

            affectedPathList.add(affectedPath);
          }
        }
        logEntry.setAffectedPathList(affectedPathList);

        scmLogEntries.add(logEntry);

      }
    }
    catch (final SVNException e)
    {
      throw new SVNAgentException(e.getMessage(), e);

    }
    return scmLogEntries;

  }

  private SVNRepository getSVNRepository(final String pRepositoryPath, final String pUserName)
      throws SVNAgentException
  {
    try
    {
      // this is used for SVNRepository driver creation
      DAVRepositoryFactory.setup();
      SVNRepositoryFactoryImpl.setup();
      FSRepositoryFactory.setup();

      @SuppressWarnings("deprecation")
      final SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(pRepositoryPath));

      final ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(pUserName,
          "");
      repository.setAuthenticationManager(authManager);
      return repository;
    }
    catch (final SVNException e)
    {
      throw new SVNAgentException(String.format(
          "the user with login=%s is not authorized to access to the repository with path:%s", pUserName,
          pRepositoryPath), e);
    }
  }

  private String getParentDirectory(final String pFilePath)
  {
    if (pFilePath == null)
    {
      throw new IllegalArgumentException(String.format("the file path : %s is not correctly defined.",
          pFilePath));
    }
    else if ((pFilePath != null) && (pFilePath.contains("/") == false))
    {
      return "/";
    }
    else
    {
      return pFilePath.substring(0, pFilePath.lastIndexOf("/"));
    }
  }

  private String getFileName(final String pFilePath)
  {
    if (pFilePath == null)
    {
      throw new IllegalArgumentException(String.format("the file path : %s is not correctly defined.",
          pFilePath));
    }
    else if ((pFilePath != null) && (pFilePath.contains("/") == false))
    {
      return pFilePath;
    }
    else
    {
      return pFilePath.substring(pFilePath.lastIndexOf("/") + 1);
    }
  }

  private Set<SVNExportDirectory> getExportDirectories(final List<String> pRepositoryNodes,
      final SVNRepository pRepository) throws SVNException
  {
    final Set<SVNExportDirectory> directories = new HashSet<SVNExportDirectory>();

    // If the node is a file, we have to export the parent directory not recursively
    // else we export the directory recursively
    String path = null;
    boolean recursive = false;
    for (final String node : pRepositoryNodes)
    {
      SVNExportDirectory directory = null;
      final SVNNodeKind nodeKind = pRepository.checkPath(node, -1);
      if (nodeKind == SVNNodeKind.FILE)
      {
        path = getParentDirectory(node);
        recursive = false;
      }
      else
      {
        path = node;
        recursive = true;
      }

      final SVNExportDirectory dirToUpdate = getDirectory(directories, path);
      final List<SVNExportDirectory> dirsToRemove = getSubDirectories(directories, path);
      // update the directory if it already exists
      if (dirToUpdate != null)
      {
        // we keep a recursive export by default
        if (dirToUpdate.isRecursive() != recursive)
        {
          dirToUpdate.setRecursive(true);
        }

        // if the directory is not exported recursively, we add the new file
        if (dirToUpdate.isRecursive() == false)
        {
          dirToUpdate.addFile(getFileName(node));
        }
      }
      // if a parent directory already exists and export recursively the subdirectories, we ignore it
      else if (getParentDirectory(directories, path) != null)
      {
        continue;
      }
      // remove the subdirectories if any exist and if we are in recursive way
      else if (recursive && (dirsToRemove.isEmpty() == false))
      {
        for (final SVNExportDirectory dirToRemove : dirsToRemove)
        {
          directories.remove(dirToRemove);
        }

        directory = new SVNExportDirectory(path, recursive);
        directories.add(directory);
      }
      else
      {
        directory = new SVNExportDirectory(path, recursive);
        if (recursive == false)
        {
          directory.addFile(getFileName(node));
        }
        directories.add(directory);
      }
    }
    return directories;
  }

  private SVNExportDirectory getParentDirectory(final Set<SVNExportDirectory> pDirectories, final String pPath)
  {
    SVNExportDirectory result = null;
    for (final SVNExportDirectory directory : pDirectories)
    {
      if ((directory.isRecursive() == true) && (pPath.indexOf(directory.getPath()) == 0)
          && (directory.getPath().equals(pPath) == false))
      {
        result = directory;
        break;
      }
    }

    return result;
  }

  private List<SVNExportDirectory> getSubDirectories(final Set<SVNExportDirectory> pDirectories,
      final String pPath)
  {
    final List<SVNExportDirectory> result = new ArrayList<SVNExportDirectory>();
    for (final SVNExportDirectory directory : pDirectories)
    {
      if ((directory.getPath().indexOf(pPath) == 0) && (directory.getPath().equals(pPath) == false))
      {
        result.add(directory);
      }
    }
    return result;
  }

  private SVNExportDirectory getDirectory(final Set<SVNExportDirectory> pDirectories, final String pPath)
  {
    SVNExportDirectory result = null;
    for (final SVNExportDirectory directory : pDirectories)
    {
      if (directory.getPath().equals(pPath))
      {
        result = directory;
        break;
      }
    }

    return result;
  }

  @Override
  public DataHandler exportRepositoryNodes(final List<String> pRepositoryNodes, final String pRepositoryPath,
      final String pUserName) throws SVNAgentException
  {
    // Get the root repository
    final SVNRepository root = getSVNRepository(pRepositoryPath, pUserName);

    // Get the horodated export key
    final String key = String.valueOf(new Date().getTime());

    // Create the working copy directory
    File workingCopyDir = null;
    try
    {
      workingCopyDir = Files.createTempDirectory(SVNExportHelper.WORKING_COPY_PREFIX).toFile();
    }
    catch (final IOException e)
    {
      throw new SVNAgentException("unable to create temp directory", e);
    }

    // Loop on the directories to make the export
    SVNRepository repo = null;
    final Set<String> requestedFiles = new HashSet<String>();
    try
    {
      final Set<SVNExportDirectory> dirsToExport = getExportDirectories(pRepositoryNodes, root);

      for (final SVNExportDirectory dirToExport : dirsToExport)
      {
        // export the svn content directory
        repo = getSVNRepository(pRepositoryPath + "/" + dirToExport.getPath(), pUserName);
        SVNExportHelper.export(workingCopyDir, repo, dirToExport.isRecursive());

        if (dirToExport.getFiles() != null)
        {
          requestedFiles.addAll(dirToExport.getFiles());
        }
      }
      // remove the files which have been exported by default
      for (final File file : workingCopyDir.listFiles())
      {
        if (requestedFiles.contains(file.getName()) == false)
        {
          file.delete();
        }
      }

      // build the archive
      final File zip = Files.createTempFile(SVNExportHelper.ZIP_EXPORT_PREFIX, ".zip").toFile();
      zip.deleteOnExit();
      ZipCreatorHelper.createZipFile(workingCopyDir.getAbsolutePath(), zip.getAbsolutePath());
      final DataHandler handler = new DataHandler(new FileDataSource(zip.getAbsolutePath()));

      return handler;
    }
    catch (final SVNException e)
    {
      throw new SVNAgentException("unable to export files from svn", e);
    }
    catch (final IOException e)
    {
      throw new SVNAgentException("unable to create zip file from svn export", e);
    }
    finally
    {
      try
      {
        if (workingCopyDir != null && workingCopyDir.exists())
        {
          FileUtils.deleteDirectory(workingCopyDir);
        }
      }
      catch (final IOException e)
      {
        throw new SVNAgentException(
            String.format("unable to delete the working copy temp directory with path=%s",
                workingCopyDir.getAbsolutePath()), e);
      }
    }
  }

  @Override
  public SVNNodeEntryDTO getRepositoryTree(final String pRepositoryPath, final String pUserName)
      throws SVNAgentException
  {
    final SVNNodeEntryDTO rootEntry = new SVNNodeEntryDTO();

    final SVNRepository repository = getSVNRepository(pRepositoryPath, pUserName);
    try
    {
      final SVNNodeKind nodeKind = repository.checkPath("", -1);
      if ((nodeKind == SVNNodeKind.NONE) || (nodeKind == SVNNodeKind.UNKNOWN))
      {
        throw new SVNAgentException(String.format("there is no or unknown entry at the following path : %s",
            pRepositoryPath));
      }

      rootEntry.setPath("");
      rootEntry.setDirectory(nodeKind == SVNNodeKind.DIR);

      final String path = "";
      final Collection<?> entries = repository.getDir(path, -1, null, (Collection<?>) null);
      final Iterator<?> iterator = entries.iterator();
      while (iterator.hasNext())
      {
        final SVNDirEntry SVNentry = (SVNDirEntry) iterator.next();
        final SVNNodeEntryDTO nodeEntry = toScmNodeEntry(path, SVNentry);
        rootEntry.addChild(nodeEntry);
        if (SVNentry.getKind() == SVNNodeKind.DIR)
        {
          getChildren(nodeEntry, repository);
        }
      }

      return rootEntry;
    }
    catch (final SVNException e)
    {
      throw new SVNAgentException(e);
    }
  }

  private SVNNodeEntryDTO toScmNodeEntry(final String pPath, final SVNDirEntry pSVNDirEntry)
  {
    final SVNNodeEntryDTO scmNodeEntry = new SVNNodeEntryDTO();
    scmNodeEntry.setAuthor(pSVNDirEntry.getAuthor());
    scmNodeEntry.setDate(pSVNDirEntry.getDate());
    scmNodeEntry.setPath(getPath(pPath, pSVNDirEntry));
    scmNodeEntry.setRevision(String.valueOf(pSVNDirEntry.getRevision()));
    scmNodeEntry.setDirectory(pSVNDirEntry.getKind() == SVNNodeKind.DIR);
    return scmNodeEntry;
  }

  private String getPath(final String pPath, final SVNDirEntry pSVNDirEntry)
  {
    String result = "";
    if ("".equals(pPath))
    {
      result = pSVNDirEntry.getName();
    }
    else
    {
      result = pPath + "/" + pSVNDirEntry.getName();
    }
    return result;
  }

  private void getChildren(final SVNNodeEntryDTO pScmNodeEntry, final SVNRepository pRepository)
      throws SVNException
  {
    final String path = pScmNodeEntry.getPath();

    final Collection<?> entries = pRepository.getDir(path, -1, null, (Collection<?>) null);
    final Iterator<?> iterator = entries.iterator();
    while (iterator.hasNext())
    {
      final SVNDirEntry SVNentry = (SVNDirEntry) iterator.next();
      final SVNNodeEntryDTO nodeEntry = toScmNodeEntry(path, SVNentry);
      pScmNodeEntry.addChild(nodeEntry);
      if (SVNentry.getKind() == SVNNodeKind.DIR)
      {
        getChildren(nodeEntry, pRepository);
      }
    }
  }

  @Override
  public List<SVNSearchResultDTO> searchInSourceCode(final String pRegex, final String pFileRegex,
      final String pRepositoryPath, final String pUserName, final String... pFileExtensions)
      throws SVNAgentException
  {
    File workingCopyDir = null;
    try
    {
      final List<SVNSearchResultDTO> svnResults = new ArrayList<SVNSearchResultDTO>();

      // Get the SVNRepository mapped to the full repository
      final SVNRepository repository = getSVNRepository(pRepositoryPath, pUserName);

      workingCopyDir = Files.createTempDirectory(SVNExportHelper.WORKING_COPY_PREFIX).toFile();

      // export the repository recursively into the working copy
      SVNExportHelper.export(workingCopyDir, repository, true);

      // make the search on the directory
      List<? extends SearchResult> results = null;
      if (pFileRegex != null && "javaOnly".equals(pFileRegex))
      {
        results = searchService.searchInJavaSource(workingCopyDir.getAbsolutePath(), pRegex);
      }
      else
      {
        results = searchService.search(workingCopyDir.getAbsolutePath(), pRegex, pFileExtensions);
      }

      if ((results != null) && (results.isEmpty() == false))
      {
        SVNSearchResultDTO svnResult = null;
        for (final SearchResult result : results)
        {
          svnResult = new SVNSearchResultDTO();

          // get the path of the file without the working copy reference
          svnResult.setPath(pRepositoryPath
              + result.getFilePath().substring(workingCopyDir.getAbsolutePath().length()));
          svnResult.setLineNumber(result.getLineNumber());
          svnResult.setSnippet(result.getSnippet());
          svnResult.setOccurrence(result.getOccurrence());
          svnResult.setFilename(result.getFileName());
          if (result instanceof JavaSearchResult)
          {
            final JavaSearchResult javaSearchResult = (JavaSearchResult) result;
            svnResult.setClassName(javaSearchResult.getClassName());
            svnResult.setPackageName(javaSearchResult.getPackageName());
          }
          else
          {
            svnResult.setClassName(result.getFileName());
          }
          svnResults.add(svnResult);
        }
      }

      return svnResults;
    }
    catch (final SVNException e)
    {
      throw new SVNAgentException("unable to export working copy from svn", e);
    }
    catch (final Exception e)
    {
      throw new SVNAgentException(String.format(
          "an error ocurred during searching regex into svn repository with regex=%s, repository pah=%s",
          pRegex, pRepositoryPath), e);
    }
    finally
    {
      try
      {
        if (workingCopyDir != null && workingCopyDir.exists())
        {
          FileUtils.deleteDirectory(workingCopyDir);
        }
      }
      catch (final IOException e)
      {
        throw new SVNAgentException(
            String.format("unable to delete the working copy temp directory with path=%s",
                workingCopyDir.getAbsolutePath()), e);
      }
    }
  }

  /**
   * @param pSearchService
   *          the searchService to set
   */
  public void setSearchService(final SearchService pSearchService)
  {
    searchService = pSearchService;
  }
}
