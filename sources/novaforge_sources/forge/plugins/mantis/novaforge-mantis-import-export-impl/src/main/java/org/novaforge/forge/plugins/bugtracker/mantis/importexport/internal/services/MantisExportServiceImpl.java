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
package org.novaforge.forge.plugins.bugtracker.mantis.importexport.internal.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapClient;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapConnector;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapException;
import org.novaforge.forge.plugins.bucktracker.mantis.services.MantisExportService;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AttachmentData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ConfigData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.CustomFieldDefinitionData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.CustomFieldValueForIssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.HistoryData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHeaderData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueNoteData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectVersionData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.RelationshipData;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.AttachmentSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Bug;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.BugCustomField;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.BugCustomFieldSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.BugRelationship;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.BugRelationshipSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.BugSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Category;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.CategorySection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.ConfigSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.CustomField;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.HistorySection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Mantis;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.NoteSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Project;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.ProjectCustomField;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.ProjectCustomFieldSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.User;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.UserSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.VersionSection;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.internal.datamappers.MantisExportDataMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sbenoist
 */
public class MantisExportServiceImpl implements MantisExportService
{
  private static final BigInteger    ANYBODY_ACCESS_LEVEL         = BigInteger.valueOf(0);
  private static final Log           LOG                          = LogFactory
                                                                      .getLog(MantisExportServiceImpl.class);
  private static final String        MANTIS_EXPORT_FILE_DIRECTORY = "/tmp/mantis_export/";
  private static final String        OLD_ISSUE_ID_FIELD           = "Ancien Identifiant";
  private MantisSoapClient           mantisSoapClient;
  private PluginConfigurationService pluginConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void exportMantisProject(final String pMantisDataFilePath, final String pMantisProjectName,
      final String pSourceMantisInstanceBaseURL) throws PluginServiceException
  {
    if (pMantisDataFilePath == null || pMantisDataFilePath.trim().length() == 0)
    {
      throw new PluginServiceException("pMantisDataFilePath is mandatory");
    }

    if (pMantisProjectName == null || pMantisProjectName.trim().length() == 0)
    {
      throw new PluginServiceException("pMantisProjectName is mandatory");
    }

    if (pSourceMantisInstanceBaseURL == null || pSourceMantisInstanceBaseURL.trim().length() == 0)
    {
      throw new PluginServiceException("pSourceMantisInstanceBaseURL is mandatory");
    }

    try
    {
      LOG.info(String.format("Export Mantis Project [with name=%s, source url=%s] in file=%s",
          pMantisProjectName, pSourceMantisInstanceBaseURL, pMantisDataFilePath));

      final File exportXML = new File(pMantisDataFilePath);

      // build the mantis instance URL
      final URL mantisBaseURL = new URL(pSourceMantisInstanceBaseURL);

      // get the mantis connector
      final MantisSoapConnector mantisSoapConnector = getMantisConnector(mantisBaseURL);

      // Get the current datetime
      final GregorianCalendar gregorianCalendar = new GregorianCalendar();
      final DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
      final XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);

      // build the root element
      final Mantis mantisRootElement = new Mantis();
      mantisRootElement.setExportDate(now);

      // build the project object
      final BigInteger projectId = buildProject(mantisRootElement, pMantisProjectName, mantisSoapConnector);

      // build the custom fields elements
      buildCustomFields(mantisRootElement, projectId, mantisSoapConnector);

      // link custom fields to project
      linkCustomFieldsToProject(mantisRootElement);

      final JAXBContext jaxbContext = JAXBContext.newInstance(Mantis.class);
      final Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      marshaller.marshal(mantisRootElement, exportXML);

      LOG.info("Export is done !");
    }
    catch (final JAXBException e)
    {
      throw new PluginServiceException("An error occurred during marshalling operation", e);
    }
    catch (final MalformedURLException e)
    {
      throw new PluginServiceException("The mantis source base URL argument is incorrect", e);
    }
    catch (final MantisSoapException e)
    {
      throw new PluginServiceException("An error occurred during Mantis Soap communication", e);
    }
    catch (final Exception e)
    {
      throw new PluginServiceException("A technical error occurred during Mantis export operation", e);
    }
  }

  private MantisSoapConnector getMantisConnector(final URL pMantisBaseURL) throws MantisSoapException,
      PluginServiceException
  {
    return mantisSoapClient.getConnector(pluginConfigurationService.getClientURL(pMantisBaseURL),
        pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
  }

  private BigInteger buildProject(final Mantis pMantisRootElement, final String pProjectName,
      final MantisSoapConnector pConnector) throws MantisSoapException, DatatypeConfigurationException,
      IOException, PluginServiceException
  {
    LOG.info(String.format("Build the project object [with name=%s]", pProjectName));
    final ProjectData projectData = mantisSoapClient
        .mc_projects_get_project_by_name(pConnector, pProjectName);

    // Create temp directory to save attachments
    final File tempDir = new File(MANTIS_EXPORT_FILE_DIRECTORY);
    if (tempDir.exists())
    {
      FileUtils.deleteDirectory(tempDir);
    }

    tempDir.mkdir();

    // get the very useful mantis project id
    final BigInteger mantisProjectId = projectData.getId();

    // instanciate the project element
    final Project project = MantisExportDataMapper.toProject(projectData);

    // Add Users
    final UserSection userSection = new UserSection();
    project.setUserSection(userSection);
    addUsers(pConnector, userSection, mantisProjectId);

    // Add versions
    final VersionSection versionSection = new VersionSection();
    project.setVersionSection(versionSection);
    addVersions(pConnector, versionSection, mantisProjectId);

    // Add categories
    final CategorySection categorySection = new CategorySection();
    project.setCategorySection(categorySection);
    addCategories(pConnector, categorySection, mantisProjectId);

    // Add configs
    final ConfigSection configSection = new ConfigSection();
    project.setConfigSection(configSection);
    addConfigs(pConnector, configSection, mantisProjectId);

    // Build bugs and bugs relationships
    final BugRelationshipSection bugRelationshipSection = new BugRelationshipSection();
    project.setBugRelationshipSection(bugRelationshipSection);
    buildBugs(pConnector, project, mantisProjectId);

    pMantisRootElement.setProject(project);
    return mantisProjectId;
  }

  private void buildCustomFields(final Mantis pMantisRootElement, final BigInteger pMantisProjectId,
      final MantisSoapConnector pConnector) throws MantisSoapException
  {
    final CustomFieldDefinitionData[] customFieldDefinitionDatas = mantisSoapClient
        .mc_project_get_custom_fields(pConnector, pMantisProjectId);
    if (customFieldDefinitionDatas != null)
    {
      for (final CustomFieldDefinitionData customFieldDefinitionData : customFieldDefinitionDatas)
      {
        LOG.info(String.format("Add Custom field [with name=%s]", customFieldDefinitionData.getField()
            .getName()));
        pMantisRootElement.getCustomField().add(
            MantisExportDataMapper.toCustomField(customFieldDefinitionData));
      }
    }

    LOG.info("Add Old Id Custom field");
    createOldIdCustomField(pMantisRootElement);
  }

  private void linkCustomFieldsToProject(final Mantis pMantisRootElement)
  {
    final ProjectCustomFieldSection projectCustomFieldSection = new ProjectCustomFieldSection();
    pMantisRootElement.getProject().setProjectCustomFieldSection(projectCustomFieldSection);
    final List<CustomField> customFields = pMantisRootElement.getCustomField();
    for (final CustomField customField : customFields)
    {
      final ProjectCustomField projectCustomField = new ProjectCustomField();
      projectCustomField.setFieldName(customField.getName());
      projectCustomFieldSection.getCustomField().add(projectCustomField);
    }
  }

  private void addUsers(final MantisSoapConnector pConnector, final UserSection pUserSection,
      final BigInteger pMantisProjectId) throws MantisSoapException
  {
    final AccountData[] accountDatas = mantisSoapClient.mc_project_get_users(pConnector, pMantisProjectId,
        ANYBODY_ACCESS_LEVEL);
    if (accountDatas != null)
    {
      for (final AccountData accountData : accountDatas)
      {
        LOG.info(String.format("Add User [with username=%s]", accountData.getName()));
        pUserSection.getUser().add(MantisExportDataMapper.toUser(accountData));
      }
    }
  }

  private void addVersions(final MantisSoapConnector pConnector, final VersionSection pVersionSection,
      final BigInteger pMantisProjectId) throws MantisSoapException, DatatypeConfigurationException
  {
    final ProjectVersionData[] projectVersionDatas = mantisSoapClient.mc_project_get_versions(pConnector,
        pMantisProjectId);
    if (projectVersionDatas != null)
    {
      for (final ProjectVersionData projectVersionData : projectVersionDatas)
      {
        LOG.info(String.format("Add Version [with name=%s]", projectVersionData.getName()));
        pVersionSection.getVersion().add(MantisExportDataMapper.toVersion(projectVersionData));
      }
    }
  }

  private void addCategories(final MantisSoapConnector pConnector, final CategorySection pCategorySection,
      final BigInteger pMantisProjectId) throws MantisSoapException, DatatypeConfigurationException
  {
    final String[] categories = mantisSoapClient.mc_project_get_categories(pConnector, pMantisProjectId);
    if (categories != null)
    {
      // remove duplicated categories
      final Set<String> categorySet = new HashSet<String>(Arrays.asList(categories));
      for (final String categoryOne : categorySet)
      {
        LOG.info(String.format("Add category [with name=%s]", categoryOne));
        final Category category = new Category();
        category.setName(categoryOne);
        pCategorySection.getCategory().add(category);
      }
    }
  }

  private void addConfigs(final MantisSoapConnector pConnector, final ConfigSection pConfigSection,
      final BigInteger pMantisProjectId) throws MantisSoapException, DatatypeConfigurationException
  {
    final ConfigData[] configs = mantisSoapClient.mc_config_get_all_project(pConnector, pMantisProjectId);
    if (configs != null)
    {
      for (final ConfigData config : configs)
      {
        LOG.info(String.format("Add config [with id=%s]", config.getConfig_id()));
        pConfigSection.getConfig().add(MantisExportDataMapper.toConfig(config));
      }
    }
  }

  private void buildBugs(final MantisSoapConnector pConnector, final Project pProject,
      final BigInteger pMantisProjectId) throws MantisSoapException, DatatypeConfigurationException,
      IOException, PluginServiceException
  {
    final IssueHeaderData[] issueHeaders = mantisSoapClient.mc_project_get_all_issue_headers(pConnector,
        pMantisProjectId);

    // Build bug section
    final BugSection bugSection = new BugSection();
    pProject.setBugSection(bugSection);
    for (final IssueHeaderData issueHeader : issueHeaders)
    {
      final IssueData issueData = mantisSoapClient.mc_issue_get(pConnector, issueHeader.getId());
      final Bug bug = MantisExportDataMapper.toBug(issueData);
      bugSection.getBug().add(bug);
      LOG.info(String.format("Add bug [with id=%s]", issueData.getId()));

      // Add Notes
      final NoteSection noteSection = new NoteSection();
      bug.setNoteSection(noteSection);
      final IssueNoteData[] notes = issueData.getNotes();
      if (notes != null)
      {
        for (final IssueNoteData issueNoteData : notes)
        {
          noteSection.getNote().add(MantisExportDataMapper.toNote(issueNoteData));
        }
      }
      // Add Histories
      final HistorySection historySection = new HistorySection();
      bug.setHistorySection(historySection);
      final HistoryData[] historyDatas = mantisSoapClient.mc_issue_get_history(pConnector, issueData.getId());
      if (historyDatas != null)
      {
        for (final HistoryData historyData : historyDatas)
        {
          historySection.getHistory().add(MantisExportDataMapper.toHistory(historyData));
        }
      }
      // Add Attachments
      final AttachmentSection attachmentSection = new AttachmentSection();
      bug.setAttachmentSection(attachmentSection);
      final AttachmentData[] attachments = issueData.getAttachments();
      if (attachments != null)
      {
        for (final AttachmentData attachmentData : attachments)
        {
          // Copy the file in specific directory
          final String exportFilePath = MANTIS_EXPORT_FILE_DIRECTORY + attachmentData.getFilename();

          copyFile(new URL(escapeXMLCharacters(attachmentData.getDownload_url().toString())), exportFilePath);
          attachmentSection.getAttachment().add(
              MantisExportDataMapper.toAttachment(attachmentData,
                  getUsernameById(pProject, attachmentData.getUser_id()), exportFilePath));
        }
      }
      // Add Custom Fields
      final CustomFieldValueForIssueData[] customFieldValues = issueData.getCustom_fields();
      final BugCustomFieldSection bugCustomFieldSection = new BugCustomFieldSection();
      bug.setBugCustomFieldSection(bugCustomFieldSection);
      if (customFieldValues != null)
      {
        for (final CustomFieldValueForIssueData customFieldValueForIssueData : customFieldValues)
        {
          bugCustomFieldSection.getBugCustomField().add(
              MantisExportDataMapper.toIssueCustomField(customFieldValueForIssueData));
        }
      }

      // Add OldId Custom Field
      final BugCustomField bugCustomField = getOrCreateOldIdBugCustomField(bugCustomFieldSection);
      bugCustomField.setValue(String.valueOf(bug.getSourceId()));

      // Add Relationships
      final RelationshipData[] relationships = issueData.getRelationships();
      if (relationships != null)
      {
        for (final RelationshipData relationshipData : relationships)
        {
          final BugRelationship relationship = MantisExportDataMapper.toRelationship(relationshipData,
              issueData.getId());

          if (!existReversedRelationship(pProject.getBugRelationshipSection().getBugRelationship(), relationship))
          {
            pProject.getBugRelationshipSection().getBugRelationship().add(relationship);
          }
        }
      }
    }
  }

  private void createOldIdCustomField(final Mantis pMantisRootElement)
  {
    boolean exist = false;
    CustomField oldIdCustomField = null;
    final List<CustomField> customFields = pMantisRootElement.getCustomField();
    for (final CustomField customField : customFields)
    {
      if (customField.getName().equals(OLD_ISSUE_ID_FIELD))
      {
        oldIdCustomField = customField;
        exist = true;
        break;
      }
    }

    if (!exist)
    {
      oldIdCustomField = new CustomField();
      oldIdCustomField.setAccessLevelR(BigInteger.valueOf(10));
      oldIdCustomField.setAccessLevelRw(BigInteger.valueOf(90));
      oldIdCustomField.setRequireClosed(false);
      oldIdCustomField.setRequireReport(false);
      oldIdCustomField.setDisplayClosed(false);
      oldIdCustomField.setDisplayReport(true);
      oldIdCustomField.setRequireResolved(false);
      oldIdCustomField.setRequireUpdate(false);
      oldIdCustomField.setDisplayResolved(false);
      oldIdCustomField.setDisplayUpdate(true);
      oldIdCustomField.setName(OLD_ISSUE_ID_FIELD);
      pMantisRootElement.getCustomField().add(oldIdCustomField);
    }
  }

  private void copyFile(final URL pDownloadURL, final String pExportFilePath) throws IOException
  {
    FileUtils.copyURLToFile(pDownloadURL, new File(pExportFilePath));
  }

  private String escapeXMLCharacters(final String pXMLData)
  {
    return pXMLData.replaceAll("&amp;", "&");
  }

  private String getUsernameById(final Project pProject, final BigInteger pId)
  {
    String username = null;
    for (final User user : pProject.getUserSection().getUser())
    {
      if (user.getId().equals(pId))
      {
        username = user.getUsername();
        break;
      }
    }
    return username;
  }

  private BugCustomField getOrCreateOldIdBugCustomField(final BugCustomFieldSection pBugCustomFieldSection)
  {
    BugCustomField oldIdCustomField = null;

    final List<BugCustomField> bugCustomFields = pBugCustomFieldSection.getBugCustomField();
    for (final BugCustomField bugCustomField : bugCustomFields)
    {
      if (bugCustomField.getFieldName().equals(OLD_ISSUE_ID_FIELD))
      {
        oldIdCustomField = bugCustomField;
        break;
      }
    }

    if (oldIdCustomField == null)
    {
      final BugCustomField bugCustomField = new BugCustomField();
      bugCustomField.setFieldName(OLD_ISSUE_ID_FIELD);
      pBugCustomFieldSection.getBugCustomField().add(bugCustomField);
    }

    return oldIdCustomField;
  }

  private boolean existReversedRelationship(final List<BugRelationship> pRelationShips,
                                            final BugRelationship pBugRelationship)
  {
    boolean exist = false;
    for (final BugRelationship bugRelationship : pRelationShips)
    {
      if (bugRelationship.getSourceId().equals(pBugRelationship.getTargetId()) && bugRelationship.getTargetId()
                                                                                                 .equals(pBugRelationship
                                                                                                             .getSourceId()))
      {
        exist = true;
        break;
      }
    }

    return exist;
  }

  /**
   * @param mantisSoapClient
   *          the mantisSoapClient to set
   */
  public void setMantisSoapClient(final MantisSoapClient mantisSoapClient)
  {
    this.mantisSoapClient = mantisSoapClient;
  }

  /**
   * @param pluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pluginConfigurationService)
  {
    this.pluginConfigurationService = pluginConfigurationService;
  }
}
