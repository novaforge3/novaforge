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
package org.novaforge.forge.plugins.management.managementmodule.internal.services;

import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssuesBean;
import org.novaforge.forge.core.plugins.categories.management.ManagementRequest;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.ApplicationRequestException;
import org.novaforge.forge.core.plugins.services.ApplicationRequestService;
import org.novaforge.forge.plugins.management.managementmodule.services.ManagementModulePluginService;
import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.exceptions.ExceptionCode;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the service dedicated to communication between tool and plugin
 */
public class ManagementModulePluginServiceImpl implements ManagementModulePluginService
{

   /** The DAO which permits to access plugin_instance table */
   private InstanceConfigurationDAO	instanceConfigurationDAO;
   /** The service to use interplugin */
   private ApplicationRequestService requestService;

   private BusinessObjectFactory		 businessObjectFactory;

   @Override
   public String getProjectId(final String pInstanceId) throws ManagementModuleException
   {
      final InstanceConfiguration instanceConfiguration = instanceConfigurationDAO.findByInstanceId(pInstanceId);
      if (instanceConfiguration != null)
      {
         return instanceConfiguration.getToolProjectId();
      }
      throw new ManagementModuleException(String.format("Unable to find any projectId for the instance given [pInstanceId=%s]",
                                                        pInstanceId));
   }

   @Override
   public String getInstanceId(final String pProjectId) throws ManagementModuleException
   {
      final InstanceConfiguration instanceConfiguration = instanceConfigurationDAO.findByToolProject(pProjectId);
      if (instanceConfiguration != null)
      {
         return instanceConfiguration.getInstanceId();
      }
      throw new ManagementModuleException(String.format("Unable to find any instance for the project given [project_id=%s]",
                                                        pProjectId));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<Bug> getAllIssues(final String pProjectId, final String pUser) throws ManagementModuleException
   {
      final List<Bug> issues = new ArrayList<Bug>();
      // Getting instance id for the current project
      final String instanceId = this.getInstanceId(pProjectId);
      try
      {

         final List<BugTrackerIssuesBean> beans = this.requestService.notifyForRequest(instanceId,
               ManagementRequest.getAllIssues.name(), pUser);
         if ((beans != null) && (!beans.isEmpty()))
         {
            for (BugTrackerIssuesBean bean : beans)
            {
               if (bean != null && !bean.getIssues().isEmpty())
               {
                  for (final BugTrackerIssueBean issue : bean.getIssues())
                  {
                     final Bug bug = businessObjectFactory.getInstanceBug();
                     fillBugWithBugTrackerIssue(bug, issue);
                     issues.add(bug);
                  }
               }
            }
         }
      }
      catch (final ApplicationRequestException arex)
      {
         throw new ManagementModuleException(
               this.getExceptionCodeForMantisInterplugin(arex),
               String
               .format(
                     "Unable to get the list of bugs available for your project on your bugtracker with [project_id=%s]",
                     pProjectId), arex);
      }
      return issues;
   }

   /**
    * This method fill a bug with the bug info of a mantis bug tracker
    *
    * @param pIssue
    *          the source
    * @return the created bug
    */
   private void fillBugWithBugTrackerIssue(final Bug bug, final BugTrackerIssueBean pIssue)
   {
      bug.setAdditionalInfo(pIssue.getAdditionalInfo());
      bug.setAssignedTo(pIssue.getAssignedTo());
      bug.setBugTrackerId(pIssue.getId());
      bug.setCategory(pIssue.getCategory());
      bug.setDescription(pIssue.getDescription());
      bug.setFixedInVersion(pIssue.getFixedInVersion());
      bug.setPriority(pIssue.getPriority());
      bug.setProductVersion(pIssue.getProductVersion());
      bug.setReporter(pIssue.getReporter());
      bug.setReproducibility(pIssue.getReproducibility());
      bug.setResolution(pIssue.getResolution());
      bug.setSeverity(pIssue.getSeverity());
      bug.setStatus(pIssue.getStatus());
      bug.setTargetVersion(pIssue.getTargetVersion());
      bug.setTitle(pIssue.getTitle());
   }

   private ExceptionCode getExceptionCodeForMantisInterplugin(final ApplicationRequestException e)
   {
      ExceptionCode code = ExceptionCode.BUG_TRACKER_COMMUNICATION_PROBLEM;
      if (e.getCode() != null && e.getCode()
                                  .equals(org.novaforge.forge.core.plugins.exceptions.ExceptionCode.ERR_REQUEST_DO_HAVE_EXISTING_ASSOCIATION))
      {
         code = ExceptionCode.BUG_TRACKER_ASSOCIATION_DOES_NOT_EXIST;
      }
      return code;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean hasBugtrackerAvailable(final String pProjectId) throws ManagementModuleException
   {

      final String instanceId = this.getInstanceId(pProjectId);
      return this.requestService.isAssociated(instanceId, ManagementRequest.getAllIssues.name())
            && this.requestService.isAssociated(instanceId, ManagementRequest.getIssue.name());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void fillBugWithBugTrackerInformations(final String pProjectId, final String pUser, final Bug bug)
       throws ManagementModuleException
   {
      // Getting instance id for the current project
      final String instanceId = this.getInstanceId(pProjectId);
      try
      {
         final List<BugTrackerIssueBean> beans = this.requestService.notifyForRequest(instanceId,
                                                                                      ManagementRequest.getIssue.name(),
                                                                                      pUser, bug.getBugTrackerId());
         // we are expecting only one bean
         if (beans != null && beans.size() == 1)
         {
            fillBugWithBugTrackerIssue(bug, beans.get(0));
         }
      }
      catch (final ApplicationRequestException e)
      {
         throw new ManagementModuleException(this.getExceptionCodeForMantisInterplugin(e), String
                                                                                               .format("Unable to get the additional informations on [bugTrackerId=%s]",
                                                                                                       bug.getBugTrackerId()),
                                             e);
      }
   }

   public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
   {
      instanceConfigurationDAO = pInstanceConfigurationDAO;
   }

   public void setRequestService(final ApplicationRequestService pRequestService)
   {
      requestService = pRequestService;
   }

   public void setBusinessObjectFactory(final BusinessObjectFactory pBusinessObjectFactory)
   {
      businessObjectFactory = pBusinessObjectFactory;
   }

}
