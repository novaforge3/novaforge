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
package org.novaforge.forge.plugins.testmanager.testlink.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.core.plugins.categories.commons.Attachment;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.DirectoryBean;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.CoveredRequirementBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestManagementNotification;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestReferenceBean;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.delegates.PluginMessageDelegate;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.categories.beans.CoveredRequirementBeanImpl;
import org.novaforge.forge.plugins.categories.beans.TestBeanImpl;
import org.novaforge.forge.plugins.categories.beans.TestReferenceBeanImpl;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcClient;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcException;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.RequirementDataMapper;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestCaseDatamapper;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkParameter;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkRPCStatus;
import org.novaforge.forge.plugins.testmanager.testlink.services.TestlinkFunctionalService;
import org.novaforge.forge.plugins.testmanager.testlink.services.TestlinkFunctionalWebService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class TestlinkFunctionalServiceImpl implements TestlinkFunctionalService, TestlinkFunctionalWebService
{
  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(TestlinkFunctionalServiceImpl.class);
  /**
   * Reference to service implementation of {@link TestlinkXmlRpcClient}
   */
  private TestlinkXmlRpcClient       testlinkXmlRpcClient;
  /**
   * Reference to service implementation of {@link InstanceConfigurationDAO}
   */
  private InstanceConfigurationDAO   instanceConfigurationDAO;
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginMessageDelegate      pluginMessageDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean testFailed(final String pUserName, final String pProjectId, final String pId, final String pTitle,
                            final String pStatus, final String pTestPlan, final String pBuild, final String pPlateform,
                            final String pSummary, final String pNotes, final List<Attachment> pAttachments)
      throws PluginServiceException
  {

    final TestBean test = new TestBeanImpl();
    test.setId(pId);
    test.setTitle(pTitle);
    test.setStatus(pStatus);
    test.setTestPlan(pTestPlan);
    test.setBuild(pBuild);
    test.setPlateform(pPlateform);
    test.setSummary(pSummary);
    test.setNotes(pNotes);
    test.setAttachments(pAttachments);

    final InstanceConfiguration instance = instanceConfigurationDAO.findByToolProject(pProjectId);

    try
    {
      if ((pUserName == null) || ("".equals(pUserName)))
      {
        throw new PluginServiceException("UserName parameter should'nt be null or empty");
      }
      pluginMessageDelegate.sendNotificationMessage(instance.getForgeId(), instance.getForgeProjectId(),
                                                    instance.getInstanceId(),
                                                    TestManagementNotification.testFailed.toString(), pUserName, test);
    }
    catch (final MessageServiceException e)
    {
      throw new PluginServiceException(String
                                           .format("Unable to send the notification to forge with [notification=%s, bean=%s]",
                                                   TestManagementNotification.testFailed.toString(), test.toString()),
                                       e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean synchronizeRequirements(final String pUserName, final String pInstanceId,
                                         final Set<DirectoryBean> directories) throws PluginServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("------------ Begin SynchronizeRequirements --------------");
    }

    InstanceConfiguration instance = null;
    try
    {
      if ((directories == null) || (directories.size() == 0))
      {
        if (LOGGER.isDebugEnabled())
        {
          LOGGER.debug("------------ Directories null !!! --------------");
        }
        return false;
      }
      else
      {
        instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
        final BigInteger toolProjectId = new BigInteger(instance.getToolProjectId());

        final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(pluginConfigurationService
                                                                             .getClientURL(instance.getToolInstance()
                                                                                                   .getBaseURL()),
                                                                         pluginConfigurationService.getClientAdmin(),
                                                                         pluginConfigurationService.getClientPwd());
        performUpdateDirectories(connector, pUserName, toolProjectId.toString(), toolProjectId.toString(), directories);
        if (LOGGER.isDebugEnabled())
        {
          LOGGER.debug("------------ End SynchronizeRequirement --------------");
        }
        return true;
      }
    }
    catch (final TestlinkXmlRpcException e)
    {
      throw new PluginServiceException(String.format("Unable to get client connector with [instance_id=%s]",
                                                     pInstanceId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateRequirementsInXML(final String pUserName, final String pInstanceId,
                                         final Set<DirectoryBean> directories) throws PluginServiceException
  {
    InstanceConfiguration instance = null;
    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
      final BigInteger toolProjectId = new BigInteger(instance.getToolProjectId());

      final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(pluginConfigurationService.getClientURL(instance
                                                                                                                   .getToolInstance()
                                                                                                                   .getBaseURL()),
                                                                       pluginConfigurationService.getClientAdmin(),
                                                                       pluginConfigurationService.getClientPwd());

      final HashMap<String, Object> requirementsData = new HashMap<String, Object>();
      // TODO the xml
      final String xml = "";
      requirementsData.put(TestLinkParameter.REQUIREMENTS_XML.toString(), xml);
      return testlinkXmlRpcClient.updateRequirements(connector, requirementsData, toolProjectId.toString(), pUserName)
                                 .isSucces();
    }
    catch (final TestlinkXmlRpcException e)
    {
      throw new PluginServiceException(String
                                           .format("Unable to update requirements to Testlink Instance with [toolProjectId=%s]",
                                                   instance.getToolProjectId()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CoveredRequirementBean> getRequirementsCoverageByTest(final String pInstanceId)
      throws PluginServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("------------ Begin getRequirementsCoverageByTest --------------");
    }
    InstanceConfiguration              instance     = null;
    final List<CoveredRequirementBean> requirements = new ArrayList<CoveredRequirementBean>();

    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
      final BigInteger toolProjectId = new BigInteger(instance.getToolProjectId());

      final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(pluginConfigurationService.getClientURL(instance
                                                                                                                   .getToolInstance()
                                                                                                                   .getBaseURL()),
                                                                       pluginConfigurationService.getClientAdmin(),
                                                                       pluginConfigurationService.getClientPwd());

      final HashMap<String, Object> requirementsData = new HashMap<String, Object>();
      final RequirementDataMapper[] requirementsDataMapper = testlinkXmlRpcClient
                                                                 .getRequirementsCoverageByTest(connector,
                                                                                                requirementsData,
                                                                                                toolProjectId
                                                                                                    .toString());

      for (final RequirementDataMapper requirementDataMapper : requirementsDataMapper)
      {
        final CoveredRequirementBean requirement = new CoveredRequirementBeanImpl(requirementDataMapper.getReqDocId(),
                                                                                  requirementDataMapper.getName(),
                                                                                  requirementDataMapper.getVersion()
                                                                                                       .toString());
        final Set<TestCaseDatamapper> testsCaseDatamapper = requirementDataMapper.getTestCaseList();
        for (final TestCaseDatamapper testCaseDatamapper : testsCaseDatamapper)
        {
          final TestReferenceBean testBean = new TestReferenceBeanImpl(testCaseDatamapper.getExternalName() + " - "
                                                                           + testCaseDatamapper.getName(),
                                                                       String.valueOf(testCaseDatamapper.getVersion()));
          requirement.addTest(testBean);
        }
        requirements.add(requirement);
      }
    }
    catch (final TestlinkXmlRpcException e)
    {
      throw new PluginServiceException(String
                                           .format("Unable to get TestCase from Testlink toolProjectId with [toolProjectId=%s]",
                                                   instance.getToolProjectId()), e);
    }
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("------------ End getRequirementsCoverageByTest --------------");
    }

    return requirements;
  }

  private void performUpdateDirectories(final XmlRpcClient pXmlRpcClient, final String pUserName,
      final String pToolProjectId, final String pParentId, final Set<DirectoryBean> pDirectories)
      throws PluginServiceException
  {
    try
    {
      String toolDirectoryId = null;
      for (final DirectoryBean directory : pDirectories)
      {
        toolDirectoryId = updateDirectory(pXmlRpcClient, pUserName, pToolProjectId, directory, pParentId);
        final Set<RequirementBean> requirements = directory.findRequirements();
        if ((requirements != null) && (!requirements.isEmpty()))
        {
          updateRequirements(pXmlRpcClient, pUserName, pToolProjectId, toolDirectoryId, requirements);
        }

        final Set<DirectoryBean> subDirectories = directory.findSubDirectories();
        if ((subDirectories != null) && (!subDirectories.isEmpty()))
        {
          performUpdateDirectories(pXmlRpcClient, pUserName, pToolProjectId, toolDirectoryId, subDirectories);
        }
      }
    }
    catch (final Exception e)
    {
      throw new PluginServiceException(String.format(
          "Unable to update requirements to Testlink Instance with [pToolProjectId=%s]", pToolProjectId), e);
    }
  }

  private String updateDirectory(final XmlRpcClient pXmlRpcClient, final String pUserName,
      final String pToolProjectId, final DirectoryBean pDirectory, final String pParentId)
      throws PluginServiceException
  {
    try
    {
      String toolDirectoryId = null;
      String name = pDirectory.getName();
      if ((name == null) || name.equals(""))
      {
        final UUID id = UUID.randomUUID();
        name = "NAME" + id;
      }
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("Directory : " + name);
      }

      final String description = pDirectory.getDescription();
      // We don't have the scope
      final String scope = pDirectory.getDescription();

      final Map<String, Object> directoryData = new HashMap<String, Object>();
      directoryData.put(TestLinkParameter.PROJECT_ID.toString(), pToolProjectId);
      directoryData.put(TestLinkParameter.USER_NAME.toString(), pUserName);
      directoryData.put(TestLinkParameter.DIRECTORY_NAME.toString(), name);
      if (description != null)
      {
        directoryData.put(TestLinkParameter.DIRECTORY_DESCRIPTION.toString(), description);
        directoryData.put(TestLinkParameter.DIRECTORY_SCOPE.toString(), scope);
      }
      else
      {
        directoryData.put(TestLinkParameter.DIRECTORY_DESCRIPTION.toString(), "TITRE");
        directoryData.put(TestLinkParameter.DIRECTORY_SCOPE.toString(), "SCOPE");
      }
      directoryData.put(TestLinkParameter.DIRECTORY_PARENT_ID.toString(), pParentId);
      final TestLinkRPCStatus<?> result = testlinkXmlRpcClient.updateDirectory(pXmlRpcClient, directoryData);
      if (result.getMessage().contains("Original message->true "))
      {
        final String[] str = result.getMessage().split("Original message->true ");
        if (str.length == 2)
        {
          toolDirectoryId = str[1].trim();
        }

      }
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("toolDirectoryId : " + toolDirectoryId);
      }

      return toolDirectoryId;
    }
    catch (final TestlinkXmlRpcException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to update requirements to Testlink Instance with [pToolProjectId=%s]", pToolProjectId), e);
    }
  }

  private void updateRequirements(final XmlRpcClient pXmlRpcClient, final String pUserName,
      final String pToolProjectId, final String pToolDirectoryId, final Set<RequirementBean> pRequirements)
      throws PluginServiceException
  {
    try
    {
      for (final RequirementBean requirementBean : pRequirements)
      {
        updateRequirement(pXmlRpcClient, pUserName, pToolProjectId, pToolDirectoryId, requirementBean);
      }
    }
    catch (final PluginServiceException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to update requirements to Testlink Instance with [pToolProjectId=%s]", pToolProjectId), e);
    }
  }

  private boolean updateRequirement(final XmlRpcClient pXmlRpcClient, final String pUserName,
      final String pToolProjectId, final String pToolDirectoryId, final RequirementBean pRequirement)
      throws PluginServiceException
  {
    try
    {
      final String reqId = pRequirement.getReference();
      String name = pRequirement.getName();
      if ((name == null) || name.equals(""))
      {
        final UUID id = UUID.randomUUID();
        name = "NAME" + id;
      }
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("name : " + name);
      }
      final String description = pRequirement.getDescription();
      final int requirementVersionId = pRequirement.getLastRequirementVersionId();

      final HashMap<String, Object> requirementData = new HashMap<String, Object>();
      requirementData.put(TestLinkParameter.PROJECT_ID.toString(), pToolProjectId);
      requirementData.put(TestLinkParameter.USER_NAME.toString(), pUserName);
      requirementData.put(TestLinkParameter.REQUIREMENT_ID.toString(), reqId);
      requirementData.put(TestLinkParameter.REQUIREMENT_NAME.toString(), name);
      if (description != null)
      {
        requirementData.put(TestLinkParameter.REQUIREMENT_DESCRIPTION.toString(), description);
      }
      else
      {
        requirementData.put(TestLinkParameter.REQUIREMENT_DESCRIPTION.toString(), "DESCRIPTION");
      }
      requirementData.put(TestLinkParameter.REQUIREMENT_PARENT_ID.toString(), pToolDirectoryId);
      requirementData.put(TestLinkParameter.REQUIREMENT_VERSION.toString(), requirementVersionId);

      return testlinkXmlRpcClient.updateRequirement(pXmlRpcClient, requirementData).isSucces();
    }
    catch (final TestlinkXmlRpcException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to update requirements to Testlink Instance with [pToolProjectId=%s]", pToolProjectId), e);
    }
  }

  private void performDeleteRequirements(final XmlRpcClient pXmlRpcClient, final String pToolProjectId,
      final Set<DirectoryBean> pDirectories) throws PluginServiceException
  {
    try
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("--- performDeleteRequirements");
      }
      final HashMap<String, Object> requirementsData = new HashMap<String, Object>();
      final RequirementDataMapper[] requirementsDataMapper = testlinkXmlRpcClient
                                                                 .getRequirementsCoverageByTest(pXmlRpcClient,
                                                                                                requirementsData,
                                                                                                pToolProjectId);
      for (final RequirementDataMapper requirementDataMapper : requirementsDataMapper)
      {
        final String requirementDocId = requirementDataMapper.getReqDocId();
        if (LOGGER.isDebugEnabled())
        {
          LOGGER.debug("requirementDocId : " + requirementDocId);
        }
        final boolean toDelete = requirementToDelete(requirementDocId, pDirectories);
        if (toDelete)
        {
          final Map<String, Object> requirementData = new HashMap<String, Object>();
          requirementData.put(TestLinkParameter.REQUIREMENT_ID.toString(), requirementDocId);
          testlinkXmlRpcClient.deleteRequirement(pXmlRpcClient, requirementData);
        }
      }
    }
    catch (final Exception e)
    {
      throw new PluginServiceException(String.format(
          "Unable to delete requirement to Testlink Instance with [pToolProjectId=%s]", pToolProjectId), e);
    }
  }

  private boolean requirementToDelete(final String pRequirementDocId, final Set<DirectoryBean> pDirectories)
      throws PluginServiceException
  {
    boolean toDelete = true;
    try
    {
      for (final DirectoryBean directory : pDirectories)
      {
        final Set<RequirementBean> requirements = directory.findRequirements();
        if ((requirements != null) && (!requirements.isEmpty()))
        {
          for (final RequirementBean requirementBean : requirements)
          {
            if (LOGGER.isDebugEnabled())
            {
              LOGGER.debug("test delete requirementBean : " + requirementBean.getReference() + " = "
                  + pRequirementDocId);
            }
            if (requirementBean.getReference().equals(pRequirementDocId))
            {
              if (LOGGER.isDebugEnabled())
              {
                LOGGER.debug("Trouve !!!!!!!!!!!!!!");
              }
              toDelete = false;
              return toDelete;
            }
          }
        }
        final Set<DirectoryBean> subDirectories = directory.findSubDirectories();
        if (toDelete && (subDirectories != null) && (!subDirectories.isEmpty()))
        {
          requirementToDelete(pRequirementDocId, subDirectories);
        }
      }
    }
    catch (final Exception e)
    {
      throw new PluginServiceException(
          String.format("Unable to update requirements to Testlink Instance with [pRequirementDocId=%s]",
              pRequirementDocId), e);
    }
    return toDelete;
  }

  /**
   * Use by container to inject {@link TestlinkXmlRpcClient}
   * 
   * @param pTestlinkXmlRpcClient
   *          the testlinkXmlRpcClient to set
   */
  public void setTestlinkXmlRpcClient(final TestlinkXmlRpcClient pTestlinkXmlRpcClient)
  {
    testlinkXmlRpcClient = pTestlinkXmlRpcClient;
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
   * Use by container to inject {@link PluginMessageDelegate}
   * 
   * @param pPluginMessageDelegate
   *          the pluginMessageDelegate to set
   */
  public void setPluginMessageDelegate(final PluginMessageDelegate pPluginMessageDelegate)
  {
    pluginMessageDelegate = pPluginMessageDelegate;
  }

}
