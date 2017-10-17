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
package org.novaforge.forge.plugins.requirements.requirementmanager.internal.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.DirectoriesBean;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.DirectoryBean;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsManagementServiceException;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsRequest;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsSend;
import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultListBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.CoveredRequirementBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.CoveredRequirementsBean;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.delegates.PluginMessageDelegate;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.ApplicationRequestException;
import org.novaforge.forge.core.plugins.services.ApplicationRequestService;
import org.novaforge.forge.plugins.categories.beans.DirectoriesBeanImpl;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementManagerFunctionalService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class RequirementManagerFunctionalServiceImpl implements RequirementManagerFunctionalService
{
   private PluginMessageDelegate		 pluginMessageDelegate;
   private ApplicationRequestService applicationRequestService;
   private InstanceConfigurationDAO	instanceConfigurationDAO;

   /**
    * {@inheritDoc}
    */
   @Override
   public String getProjectId(final String pInstanceId) throws RequirementsManagementServiceException
   {
      String result = null;

      final InstanceConfiguration inst = instanceConfigurationDAO.findByInstanceId(pInstanceId);
      if ((inst != null) && (inst.getToolProjectId() != null))
      {
         result = inst.getToolProjectId();
      }
      else
      {
         throw new RequirementsManagementServiceException(String.format(
               "Unable to find any project for the instance given [instance_id=%s", pInstanceId));
      }

      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean updateRequirements(final String pProjectId, final List<DirectoryBean> pDirectories,
         final String pUserName) throws RequirementsManagementServiceException
         {

      final DirectoriesBean directories = new DirectoriesBeanImpl();
      for (final DirectoryBean directoryBean : pDirectories)
      {
         directories.addDirectoryBean(directoryBean);
      }

      final InstanceConfiguration instance = this.getInstance(pProjectId);
      try
      {
         if ((pUserName == null) || ("".equals(pUserName)))
         {
            throw new RequirementsManagementServiceException("UserName parameter should'nt be null or empty");
         }
         this.pluginMessageDelegate.sendDataMessage(instance.getForgeId(), instance.getForgeProjectId(),
               instance.getInstanceId(), RequirementsSend.updateRequirements.toString(), pUserName, directories);
      }
      catch (final MessageServiceException e)
      {
         throw new RequirementsManagementServiceException(String.format(
               "Unable to send the notification to forge with [send_event=%s, bean=%s]",
               RequirementsSend.updateRequirements.toString(), directories.toString()), e);
      }
      return true;
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<CoveredRequirementBean> getRequirementsWithTestCoverage(final String pProjectId,
         final String pUserName) throws RequirementsManagementServiceException
         {
      List<CoveredRequirementBean> returnList = new ArrayList<CoveredRequirementBean>();
      final InstanceConfiguration instance = this.getInstance(pProjectId);

      if ((pUserName == null) || ("".equals(pUserName)))
      {
         throw new RequirementsManagementServiceException("UserName parameter should'nt be null or empty");
      }
      try
      {
         final List<CoveredRequirementsBean> nodes = this.applicationRequestService.notifyForRequest(
               instance.getInstanceId(), RequirementsRequest.getRequirementsWithTestCoverage.name(), pUserName);

        if ((nodes != null) && (!nodes.isEmpty()))
         {
            for (CoveredRequirementsBean node : nodes)
            {
              if (node != null && !node.getCoveredRequirementBean().isEmpty())
               {
                  returnList.addAll(node.getCoveredRequirementBean());
               }
            }
         }
      }
      catch (final ApplicationRequestException e)
      {
         throw new RequirementsManagementServiceException(String.format(
               "Unable to get the covered requirement available for the curent project with [project_id=%s]",
               pProjectId), e);
      }
      return returnList;

         }

   @Override
  public List<SCMSearchResultBean> searchRequirementInSourceCode(final String pProjectId,
      final String pRegex, final String pFileRegex, final String pCodeRepositoryPath, final String pUserName, final String... pFileExtentions)
      throws RequirementsManagementServiceException
  {
      List<SCMSearchResultBean> result = new ArrayList<SCMSearchResultBean>();
      final InstanceConfiguration instance = this.getInstance(pProjectId);

      if (pProjectId == null || pProjectId.trim().length() == 0)
      {
         throw new RequirementsManagementServiceException("pProjectId parameter should'nt be null or empty");
      }

      if (pRegex == null || pRegex.trim().length() == 0)
      {
         throw new RequirementsManagementServiceException("pRegex parameter should'nt be null or empty");
      }

      if (pUserName == null || pUserName.trim().length() == 0)
      {
         throw new RequirementsManagementServiceException("pUserName parameter should'nt be null or empty");
      }

      try
      {
         // build a JSON object to set the regex
         final JSONObject json = new JSONObject();
         json.put("regex", pRegex);
         json.put("fileRegex", pFileRegex);
         json.put("repositoryPath", pCodeRepositoryPath);
         json.put("fileExtensions", pFileExtentions);

         final List<SCMSearchResultListBean> resultList = this.applicationRequestService.notifyForRequest(
               instance.getInstanceId(), RequirementsRequest.searchRequirementInSourceCode.name(), pUserName,
               json.toString());

        if (resultList != null && !resultList.isEmpty())
         {
            for (SCMSearchResultListBean scmSearchResultListBean : resultList)
            {
               if (scmSearchResultListBean != null && !scmSearchResultListBean.getSCMSearchResultBeans().isEmpty())
               {
                  result.addAll(scmSearchResultListBean.getSCMSearchResultBeans());
               }
            }

         }
      }
      catch (final ApplicationRequestException e)
      {
         throw new RequirementsManagementServiceException(String.format(
               "Unable to get the covered requirement available for the curent project with [project_id=%s]",
               pProjectId), e);
      }
      catch (final JSONException e)
      {
         throw new RequirementsManagementServiceException(String.format(
               "Unable to build json object with [regex=%s]", pRegex), e);
      }
      return result;
   }

  /**
   * @param pProjectId
   *     represents tool project id used in requirement side
   *
   * @return return {@link InstanceConfiguration} according to the tool project id given
   *
   * @throws RequirementsManagementServiceException
   *     thrown because of persistence errors
   */
  private InstanceConfiguration getInstance(final String pProjectId) throws RequirementsManagementServiceException
  {
    return instanceConfigurationDAO.findByToolProject(pProjectId);
  }

   public void setPluginMessageDelegate(final PluginMessageDelegate pPluginMessageDelegate)
   {
      pluginMessageDelegate = pPluginMessageDelegate;
   }

   public void setApplicationRequestService(final ApplicationRequestService pApplicationRequestService)
   {
      applicationRequestService = pApplicationRequestService;
   }

   public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
   {
      instanceConfigurationDAO = pInstanceConfigurationDAO;
   }

}
