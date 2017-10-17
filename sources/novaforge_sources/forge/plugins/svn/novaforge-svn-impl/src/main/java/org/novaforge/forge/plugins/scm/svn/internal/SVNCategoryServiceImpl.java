/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */

package org.novaforge.forge.plugins.scm.svn.internal;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.novaforge.forge.core.plugins.categories.ecm.ECMServiceException;
import org.novaforge.forge.core.plugins.categories.scm.SCMCategoryService;
import org.novaforge.forge.core.plugins.categories.scm.SCMCommitBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMNodeEntryBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultListBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMServiceException;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.categories.beans.SCMCommitBeanImpl;
import org.novaforge.forge.plugins.categories.beans.SCMNodeEntryBeanImpl;
import org.novaforge.forge.plugins.categories.beans.SCMSearchResultBeanImpl;
import org.novaforge.forge.plugins.categories.beans.SCMSearchResultListBeanImpl;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNNodeEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNSearchResultDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.ScmLogEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNClientFacadeService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNFacadeService;
import org.novaforge.forge.plugins.scm.svn.domain.ScmLogEntry;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author rols-p
 * @author Guillaume Lamirand
 */
public class SVNCategoryServiceImpl extends AbstractPluginCategoryService implements SCMCategoryService
{

  private static final String        PROPERTY_FILE = "svn";

  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * Reference to service implementation of {@link SVNClientFacadeService}
   */
  private SVNClientFacadeService     svnClientFacadeService;

  /**
   * Reference to service implementation of {@link InstanceConfigurationDAO}
   */
  private InstanceConfigurationDAO   instanceConfigurationDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
      throws PluginServiceException
  {
    String message = null;
    try
    {
      message = getMessage(KEY_ACCESS_INFO, pLocale, getRepositoryUrlFromInstance(pInstanceId));
    }
    catch (final SVNAgentException e)
    {
      throw new PluginServiceException("Unable to get the SVN Repository Url", e);
    }
    return message;
  }

  private String getRepositoryUrlFromInstance(final String pInstanceId) throws SVNAgentException
  {
    String url = "";
    final InstanceConfiguration configuration = instanceConfigurationDAO.findByInstanceId(pInstanceId);
    final String repository = configuration.getToolProjectId();
    try
    {
      final String clientURL = pluginConfigurationService.getClientURL(configuration.getToolInstance()
          .getBaseURL());
      final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
      url = svnClientFacadeService.getRepositoryUrl(svnFacadeService, repository);
    }
    catch (final PluginServiceException e)
    {
      throw new SVNAgentException(String.format("Unable to build reposistory url from instance [%s]",
          pInstanceId), e);
    }
    return url;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getPropertyFileName()
  {
    return PROPERTY_FILE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SCMNodeEntryBean getSourcesTree(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws SCMServiceException

  {
    SCMNodeEntryBean requestBean = null;

    // Get instance object
    final InstanceConfiguration instance = getInstance(pInstanceId);

    // Repository id
    final String repositoryId = instance.getToolProjectId();

    // Check if instance got is mapped to the correct forge id
    checkForgeId(pForgeId, instance);

    try
    {
      final String clientURL = pluginConfigurationService.getClientURL(instance.getToolInstance()
          .getBaseURL());
      final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
      final SVNNodeEntryDTO nodeEntry = svnClientFacadeService.getSCMRepositoryTree(svnFacadeService,
          repositoryId, pCurrentUser);
      requestBean = toSCMNodeEntry(nodeEntry);
    }
    catch (final SVNAgentException e)
    {
      throw new SCMServiceException(String.format(
          "Unable to get SVN node entry with [repository id=%s, username=%s]", repositoryId, pCurrentUser), e);
    }
    catch (final PluginServiceException e)
    {
      throw new SCMServiceException(String.format("Unable to build client url with [instance=%s]", instance),
          e);
    }
    return requestBean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copySources(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pJSONParameter) throws SCMServiceException
  {
    // Get instance object
    final InstanceConfiguration instance = getInstance(pInstanceId);

    // Repository id
    final String repositoryId = instance.getToolProjectId();

    // Check if instance got is mapped to the correct forge id
    checkForgeId(pForgeId, instance);

    try
    {
      // Get JSON Object
      final JSONObject json = JSONObject.fromObject(pJSONParameter);

      // Get the path where the sources will be copied
      final String path = json.getString("path");

      // Get the list of sources
      final JSONArray array = json.getJSONArray("sources");
      final List<String> sources = new ArrayList<String>();
      for (int i = 0; i < array.size(); i++)
      {
        sources.add(array.getString(i));
      }

      final String clientURL = pluginConfigurationService.getClientURL(instance.getToolInstance()
          .getBaseURL());
      final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
      final DataHandler handler = svnClientFacadeService.exportSCMRepositoryNodes(svnFacadeService, sources,
          repositoryId, pCurrentUser);

      final DataSource dataSource = handler.getDataSource();
      final ZipInputStream zipInputStream = new ZipInputStream(dataSource.getInputStream());

      // unzip the ZipInputStream object into the argued path
      unzip(zipInputStream, path);
    }
    catch (final SVNAgentException e)
    {
      throw new SCMServiceException(String.format(
          "Unable to export SVN repository nodes with [repository_id=%s, username=%s]", repositoryId,
          pCurrentUser), e);
    }
    catch (final PluginServiceException e)
    {
      throw new SCMServiceException(String.format("Unable to build client url with [instance=%s]", instance),
          e);
    }
    catch (final IOException e)
    {
      throw new SCMServiceException(String.format(
          "Unable to unzip the sources from SVN repository nodes with [repository_id=%s, username=%s]",
          repositoryId, pCurrentUser), e);
    }
    catch (final JSONException e)
    {
      throw new SCMServiceException(String.format(
          "Invalid JSON parameters to copy sources with [repository_id=%s, username=%s, JSON parameters=%s]",
          repositoryId, pCurrentUser, pJSONParameter), e);
    }
  }

  /**
   * @param pForgeId
   * @param pInstanceId
   * @return
   * @throws ECMServiceException
   */
  private InstanceConfiguration getInstance(final String pInstanceId) throws SCMServiceException
  {
    return instanceConfigurationDAO.findByInstanceId(pInstanceId);
  }

  /**
   * @param pForgeId
   * @param instance
   *
   * @throws ECMServiceException
   */
  private void checkForgeId(final String pForgeId, final InstanceConfiguration instance) throws SCMServiceException
  {
    if (instance != null)
    {
      if (!instance.getForgeId().equals(pForgeId))
      {
        throw new SCMServiceException("The forge id given as parameter doesn''t match with the instance id");
      }
    }
  }

  /**
   * Unzip a ZipInputStream object
   *
   * @param zipFile
   *          input zip file
   * @param output
   *          zip file output folder
   * @throws IOException
   */
  private void unzip(final ZipInputStream pZipInputStream, final String pDestinationFolder)
      throws IOException
  {
    final byte[] buffer = new byte[1024];

    // create output directory is not exists
    final File folder = new File(pDestinationFolder);
    if (!folder.exists())
    {
      folder.mkdir();
    }

    // get the zipped file list entry
    ZipEntry ze = pZipInputStream.getNextEntry();

    while (ze != null)
    {
      final String fileName = ze.getName();
      final File newFile = new File(folder + File.separator + fileName);

      // create all non exists folders
      // else you will hit FileNotFoundException for compressed folder
      new File(newFile.getParent()).mkdirs();

      final FileOutputStream fos = new FileOutputStream(newFile);

      int len;
      while ((len = pZipInputStream.read(buffer)) > 0)
      {
        fos.write(buffer, 0, len);
      }

      fos.close();
      ze = pZipInputStream.getNextEntry();
    }

    pZipInputStream.closeEntry();
    pZipInputStream.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SCMSearchResultListBean searchInSourceCode(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pJSONParameter) throws SCMServiceException
  {
    final SCMSearchResultListBean result = new SCMSearchResultListBeanImpl();

    // Get instance object
    final InstanceConfiguration instance = getInstance(pInstanceId);

    // Repository id
    final String repositoryId = instance.getToolProjectId();

    // Check if instance got is mapped to the correct forge id
    checkForgeId(pForgeId, instance);

    String regex = null;
    String fileRegex = null;
    String[] fileExtensions = null;
    String repositoryPath = null;
    try
    {
      // Get JSON Object
      final JSONObject json = JSONObject.fromObject(pJSONParameter);

      // Get the regex to be searched (mandatory)
      regex = json.getString("regex");

      // Get the file regex to be searched (optional)
      if (json.has("fileRegex"))
      {
        fileRegex = json.getString("fileRegex");
      }

      // Get the file extensions to be searched (optional)
      if (json.has("fileExtensions"))
      {
        try {
          JSONArray jsonArray = json.getJSONArray("fileExtensions");
          if(jsonArray!=null)
          {
            ArrayList<String> al = new ArrayList<>();
            for (Object object : jsonArray) {
              al.add(object.toString());
            }
           fileExtensions = al.toArray(new String[jsonArray.size()]);
          }
        } catch (Exception e) {
          fileExtensions = new String [] { json.getString("fileExtensions") };
        }
      }

      // Get the repository path where the search will proceed (optional)
      if (json.has("repositoryPath"))
      {
        repositoryPath = json.getString("repositoryPath");
      }

      final String clientURL = pluginConfigurationService.getClientURL(instance.getToolInstance()
          .getBaseURL());
      final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
      final List<SVNSearchResultDTO> svnResults = svnClientFacadeService.searchInSourceCode(svnFacadeService,
          regex, fileRegex, repositoryId, repositoryPath, pCurrentUser, fileExtensions);
      if (svnResults != null)
      {
        for (final SVNSearchResultDTO svnResult : svnResults)
        {
          result.addSCMSearchResultBean(toSCMSearchResultBean(svnResult));
        }
      }
    }
    catch (final SVNAgentException e)
    {
      throw new SCMServiceException(
          String.format(
              "Unable to search regex into SVN repository source code with [regex=%s, repository_id=%s, username=%s]",
              regex, repositoryId, pCurrentUser), e);
    }
    catch (final PluginServiceException e)
    {
      throw new SCMServiceException(String.format("Unable to build client url with [instance=%s]", instance),
          e);
    }
    catch (final JSONException e)
    {
      throw new SCMServiceException(
          String.format(
              "Invalid JSON parameters to search regex into SVN repository source code with [regex=%s,repository_id=%s, username=%s, JSON parameters=%s]",
              regex, repositoryId, pCurrentUser, pJSONParameter), e);
    }
    return result;
  }

  private SCMSearchResultBean toSCMSearchResultBean(final SVNSearchResultDTO pSVNResult)
  {
    final SCMSearchResultBean bean = new SCMSearchResultBeanImpl();
    bean.setClassName(pSVNResult.getClassName());
    bean.setLineNumber(pSVNResult.getLineNumber());
    bean.setPackageName(pSVNResult.getPackageName());
    bean.setPath(pSVNResult.getPath());
    bean.setSnippet(pSVNResult.getSnippet());
    bean.setOccurrence(pSVNResult.getOccurrence());
    return bean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRepositoryId(final String pForgeId, final String pInstanceId) throws SCMServiceException
  {
    // Get instance object
    final InstanceConfiguration instance = getInstance(pInstanceId);

    // Check if instance got is mapped to the correct forge id
    checkForgeId(pForgeId, instance);

    // Repository id
    return instance.getToolProjectId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SCMCommitBean> getCommits(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final int pNumberOfCommit) throws SCMServiceException
  {
    // Get instance object
    final InstanceConfiguration instance = getInstance(pInstanceId);

    // Repository id
    final String repositoryId = instance.getToolProjectId();

    // Check if instance got is mapped to the correct forge id
    checkForgeId(pForgeId, instance);

    final List<SCMCommitBean> lastCommitList = new ArrayList<SCMCommitBean>();
    try
    {
      final String clientURL = pluginConfigurationService.getClientURL(instance.getToolInstance()
          .getBaseURL());
      final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);

      final List<ScmLogEntryDTO> entries = svnClientFacadeService.getLastCommit(svnFacadeService,
          repositoryId, pCurrentUser, pNumberOfCommit);
      lastCommitList.addAll(toSCMCommitBean(entries));

    }
    catch (final Exception e)
    {
      throw new SCMServiceException(String.format(
          "Unable to find commits with [number=%s, repository_id=%s, username=%s]", pNumberOfCommit,
          repositoryId, pCurrentUser), e);
    }
    return lastCommitList;
  }

  private List<SCMCommitBean> toSCMCommitBean(final List<ScmLogEntryDTO> pEntries)
  {
    final List<SCMCommitBean> resultComitList = new ArrayList<SCMCommitBean>();
    for (final ScmLogEntry entry : pEntries)
    {
      final SCMCommitBean comit = new SCMCommitBeanImpl();
      comit.setRevision(String.valueOf(entry.getRevision()));
      comit.setAuthor(entry.getAuthor());
      comit.setComment(entry.getComent());
      comit.setDate(entry.getComitDate());
      comit.setChanges(entry.getAffectedPathList().size());
      resultComitList.add(comit);
    }
    return resultComitList;
  }

  private SCMNodeEntryBean toSCMNodeEntry(final SVNNodeEntryDTO pSVNNodeEntry)
  {
    final SCMNodeEntryBean scmNodeEntry = new SCMNodeEntryBeanImpl();
    scmNodeEntry.setAuthor(pSVNNodeEntry.getAuthor());
    scmNodeEntry.setDate(pSVNNodeEntry.getDate());
    scmNodeEntry.setDirectory(pSVNNodeEntry.isDirectory());
    scmNodeEntry.setPath(pSVNNodeEntry.getPath());
    scmNodeEntry.setRevision(pSVNNodeEntry.getRevision());

    final List<SCMNodeEntryBean> children = new ArrayList<SCMNodeEntryBean>();
    for (final SVNNodeEntryDTO svnNodeEntry : pSVNNodeEntry.getChildren())
    {
      children.add(toSCMNodeEntry(svnNodeEntry));
    }

    scmNodeEntry.setChildren(children);
    return scmNodeEntry;
  }

  /**
   * Use by container to inject {@link SVNClientFacadeService}
   *
   * @param pSvnClientFacadeService
   *          the svnClientFacadeService to set
   */
  public void setSvnClientFacadeService(final SVNClientFacadeService pSvnClientFacadeService)
  {
    svnClientFacadeService = pSvnClientFacadeService;
  }

  /**
   * Use by container to inject {@link InstanceConfigurationDAO}
   *
   * @param pInstanceConfigurationDAO
   *          the instanceConfigurationDAO to set
   */
  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

  /**
   * Use by container to inject {@link PluginConfigurationService}
   *
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

}
