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
package org.novaforge.forge.tools.deliverymanager.internal.services;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssuesBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerProjectVersionsBean;
import org.novaforge.forge.core.plugins.categories.delivery.DeliveryRequest;
import org.novaforge.forge.core.plugins.categories.ecm.DocumentNodeBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMNodeEntryBean;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.ApplicationRequestException;
import org.novaforge.forge.core.plugins.services.ApplicationRequestService;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.exceptions.ExceptionCode;
import org.novaforge.forge.tools.deliverymanager.internal.model.BugTrackerIssueImpl;
import org.novaforge.forge.tools.deliverymanager.internal.model.ECMNodeImpl;
import org.novaforge.forge.tools.deliverymanager.internal.model.SCMNodeImpl;
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.BugTrackerIssue;
import org.novaforge.forge.tools.deliverymanager.model.ECMNode;
import org.novaforge.forge.tools.deliverymanager.model.SCMNode;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPluginService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link DeliveryPluginService}
 * 
 * @author Guillaume Lamirand
 */
public class DeliveryPluginServiceImpl implements DeliveryPluginService
{
  private InstanceConfigurationDAO  instanceConfigurationDAO;
  private ApplicationRequestService applicationRequestService;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectId(final String pInstanceId) throws DeliveryServiceException
  {
    String result;

    final InstanceConfiguration inst = instanceConfigurationDAO.findByInstanceId(pInstanceId);
    if ((inst != null) && (inst.getToolProjectId() != null))
    {
      result = inst.getToolProjectId();
    }
    else
    {
      throw new DeliveryServiceException(String.format(
          "Unable to find any project for the instance given [instance_id=%s", pInstanceId));
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInstanceId(final String pProjectId) throws DeliveryServiceException
  {
    String result;

    final InstanceConfiguration inst = instanceConfigurationDAO.findByToolProject(pProjectId);
    if ((inst != null) && (inst.getInstanceId() != null))
    {
      result = inst.getInstanceId();
    }
    else
    {
      throw new DeliveryServiceException(String.format(
          "Unable to find any instance for the project given [project_id=%s", pProjectId));
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getIssuesVersion(final String pProjectId, final String pUser)
      throws DeliveryServiceException
  {
    // Use a set to have unique versions
    final Set<String> versions = new HashSet<String>();
    // Getting instance id for the current project
    final String instanceId = getInstanceId(pProjectId);
    try
    {
      final List<BugTrackerProjectVersionsBean> beans = applicationRequestService.notifyForRequest(
          instanceId, DeliveryRequest.getBugsVersions.name(), pUser);
      if ((beans != null) && (!beans.isEmpty()))
      {
        for (BugTrackerProjectVersionsBean bugTrackerProjectVersionsBean : beans)
        {
          if (bugTrackerProjectVersionsBean != null && !bugTrackerProjectVersionsBean.getVersions().isEmpty())
          {
            versions.addAll(bugTrackerProjectVersionsBean.getVersions());
          }
        }
      }
    }
    catch (final ApplicationRequestException e)
    {
      throw new DeliveryServiceException(
          String.format(
              "Unable to get the list of versions avaialble for your project on your bugtracker with [project_id=%s]",
              pProjectId), e, getExceptionCode(e));
    }

    return new ArrayList<String>(versions);
  }

  private ExceptionCode getExceptionCode(final ApplicationRequestException e)
  {
    ExceptionCode code = ExceptionCode.TECHNICAL_ERROR;

    if (org.novaforge.forge.core.plugins.exceptions.ExceptionCode.ERR_REQUEST_DO_HAVE_EXISTING_ASSOCIATION
            .equals(e.getCode()))
    {
      code = ExceptionCode.ASSOCIATION_DO_NOT_EXIST;
    }
    return code;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BugTrackerIssue> getIssues(final String pProjectId, final String pVersion, final String pUser)
      throws DeliveryServiceException
  {
    final List<BugTrackerIssue> issues = new ArrayList<BugTrackerIssue>();
    // Getting instance id for the current project
    final String instanceId = getInstanceId(pProjectId);
    try
    {

      final List<BugTrackerIssuesBean> beans = applicationRequestService.notifyForRequest(instanceId,
          DeliveryRequest.findBugsByVersion.name(), pUser, getJsonVersion(pVersion).toString());
      if ((beans != null) && (!beans.isEmpty()))
      {
        for (BugTrackerIssuesBean bugTrackerIssuesBean : beans)
        {
          if (bugTrackerIssuesBean != null && !bugTrackerIssuesBean.getIssues().isEmpty())
          {
            for (final BugTrackerIssueBean issue : bugTrackerIssuesBean.getIssues())
            {
              issues.add(buildIssue(issue));
            }
          }
        }
      }
    }
    catch (final ApplicationRequestException e)
    {
      throw new DeliveryServiceException(
          String.format(
              "Unable to get the list of versions avaialble for your project on your bugtracker with [project_id=%s]",
              pProjectId), e, getExceptionCode(e));
    }

    return issues;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ECMNode getECMNode(final String pProjectId, final String pUser) throws DeliveryServiceException
  {
    ECMNode returnNode = null;
    // // Getting instance id for the current project
    final String instanceId = getInstanceId(pProjectId);
    try
    {
      final List<DocumentNodeBean> nodes = applicationRequestService.notifyForRequest(instanceId,
                                                                                      DeliveryRequest.getDocumentsList
                                                                                          .name(), pUser);

      // Building GedNode from object got
      if (nodes != null && !nodes.isEmpty())
      {
        // we are expecting only one root node here : so we take the first one...
        returnNode = buildGedNode(nodes.get(0));
      }
    }
    catch (final ApplicationRequestException e)
    {
      throw new DeliveryServiceException(String
                                             .format("Unable to get the ecm node available for the curent project with [project_id=%s]",
                                                     pProjectId), e, getExceptionCode(e));
    }

    return returnNode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SCMNode getSCMNode(final String pProjectId, final String pUser) throws DeliveryServiceException
  {
    SCMNode returnNode = null;
    // // Getting instance id for the current project
    final String instanceId = getInstanceId(pProjectId);
    try
    {
      final List<SCMNodeEntryBean> nodes = applicationRequestService.notifyForRequest(instanceId,
                                                                                      DeliveryRequest.getSourcesList
                                                                                          .name(), pUser);

      // Building SCMNode from object got
      if (nodes != null && !nodes.isEmpty())
      {
        // we are expecting only one root node here : so we take the first one...
        returnNode = buildSCMNode(nodes.get(0));
      }
    }
    catch (final ApplicationRequestException e)
    {
      throw new DeliveryServiceException(String
                                             .format("Unable to get the scm node available for the curent project with [project_id=%s]",
                                                     pProjectId), e, getExceptionCode(e));
    }

    return returnNode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSCMAvailable(final String pProjectId) throws DeliveryServiceException
  {

    final String instanceId = getInstanceId(pProjectId);
    return applicationRequestService.isAssociated(instanceId, DeliveryRequest.getSourcesList.name())
        && applicationRequestService.isAssociated(instanceId, DeliveryRequest.getSourcesContent.name());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasECMAvailable(final String pProjectId) throws DeliveryServiceException
  {

    final String instanceId = getInstanceId(pProjectId);
    return applicationRequestService.isAssociated(instanceId, DeliveryRequest.getDocumentsList.name())
        && applicationRequestService.isAssociated(instanceId, DeliveryRequest.getDocumentsContent.name());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBugtrackerAvailable(final String pProjectId) throws DeliveryServiceException
  {

    final String instanceId = getInstanceId(pProjectId);
    return applicationRequestService.isAssociated(instanceId, DeliveryRequest.findBugsByVersion.name())
        && applicationRequestService.isAssociated(instanceId, DeliveryRequest.getBugsVersions.name());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getSCMDocumentToDelivery(final String pProjectId, final String pReference,
      final String pDestinationPath, final List<Artefact> pDocuments, final String pUser)
      throws DeliveryServiceException
  {
    try
    {
      JSONObject json = new JSONObject();

      json.put("path", pDestinationPath);
      JSONArray array = new JSONArray();
      if ((pDocuments != null) && !pDocuments.isEmpty())
      {
        for (Artefact document : pDocuments)
        {
          array.add(document.getIdentifiant());
        }
      }
      json.put("sources", array);

      // call WS
      applicationRequestService.notifyForRequest(getInstanceId(pProjectId), DeliveryRequest.getSourcesContent.name(),
                                                 pUser, json.toString());
    }
    catch (ApplicationRequestException e)
    {
      throw new DeliveryServiceException(e);
    }
    catch (JSONException e)
    {
      throw new DeliveryServiceException(e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getECMDocumentToDelivery(final String pProjectId, final String pReference,
      final String pDestinationPath, final List<Artefact> pDocuments, final String pUser)
      throws DeliveryServiceException
  {
    try
    {
      JSONObject json = new JSONObject();

      json.put("path", pDestinationPath);
      JSONArray array = new JSONArray();
      if ((pDocuments != null) && !pDocuments.isEmpty())
      {
        for (Artefact document : pDocuments)
        {
          array.add(document.getIdentifiant());
        }
      }
      json.put("documents", array);

      // call WS
      applicationRequestService.notifyForRequest(getInstanceId(pProjectId), DeliveryRequest.getDocumentsContent.name(),
                                                 pUser, json.toString());
    }
    catch (ApplicationRequestException e)
    {
      throw new DeliveryServiceException(e);
    }
    catch (JSONException e)
    {
      throw new DeliveryServiceException(e);
    }
    return true;
  }

  private JSONObject getJsonVersion(final String pVersion) throws DeliveryServiceException
  {
    final JSONObject json = new JSONObject();
    try
    {
      json.put("fixed_in_version", pVersion);
    }
    catch (final JSONException e)
    {
      throw new DeliveryServiceException(String.format("Unable to build json version with [version=%s]", pVersion), e);
    }
    return json;
  }

  private BugTrackerIssue buildIssue(final BugTrackerIssueBean pIssue)
  {

    final BugTrackerIssueImpl issue = new BugTrackerIssueImpl();
    issue.setId(pIssue.getId());
    issue.setTitle(pIssue.getTitle());
    issue.setDescription(pIssue.getDescription());
    issue.setCategory(pIssue.getCategory());
    issue.setReporter(pIssue.getReporter());
    issue.setSeverity(pIssue.getSeverity());
    return issue;
  }

  private ECMNode buildGedNode(final DocumentNodeBean pNode)
  {
    final ECMNode ecmNode = new ECMNodeImpl(pNode);
    for (final DocumentNodeBean node : pNode.getChildren())
    {
      ecmNode.addChild(buildGedNode(node));
    }
    return ecmNode;
  }

  private SCMNode buildSCMNode(final SCMNodeEntryBean pNode)
  {
    final SCMNode scmNode = new SCMNodeImpl(pNode);
    for (final SCMNodeEntryBean node : pNode.getChildren())
    {
      scmNode.addChild(buildSCMNode(node));
    }
    return scmNode;
  }

  /**
   * @param pInstanceConfigurationDAO
   *          the instanceConfigurationDAO to set
   */
  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

  /**
   * @param pApplicationRequestService
   *          the applicationRequestService to set
   */
  public void setApplicationRequestService(final ApplicationRequestService pApplicationRequestService)
  {
    applicationRequestService = pApplicationRequestService;
  }

}
