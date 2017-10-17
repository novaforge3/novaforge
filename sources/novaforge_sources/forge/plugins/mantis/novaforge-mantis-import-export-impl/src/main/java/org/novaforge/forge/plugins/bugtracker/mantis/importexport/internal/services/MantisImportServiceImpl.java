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
import org.novaforge.forge.plugins.bucktracker.mantis.services.MantisImportService;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueNoteData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ObjectRef;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Attachment;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Bug;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.BugRelationship;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Category;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Config;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.CustomField;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.History;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.ImportStatus;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Mantis;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Note;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Project;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.ProjectCustomField;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.User;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Version;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.internal.datamappers.MantisImportDataMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbenoist
 */
public class MantisImportServiceImpl implements MantisImportService
{
  private static final Log        LOG                  = LogFactory.getLog(MantisImportServiceImpl.class);
  private static final BigInteger ANYBODY_ACCESS_LEVEL = BigInteger.valueOf(0);
  private static final Pattern    BUG_ID_PATTERN       = Pattern.compile("#(\\d+)");
  private static final String     NOT_FOUND_VALUE      = "xxxxxxx";
  private MantisSoapClient           mantisSoapClient;
  private PluginConfigurationService pluginConfigurationService;

  /**
   * {@inheritDoc}
   *
   * @throws FileNotFoundException
   */
  @Override
  public void importMantisProject(final String pMantisDataFilePath, final String pMantisProjectName,
                                  final String pTargetMantisInstanceBaseURL) throws PluginServiceException
  {
    if (pMantisDataFilePath == null || pMantisDataFilePath.trim().length() == 0)
    {
      throw new PluginServiceException("pMantisDataFilePath is mandatory");
    }

    if (pMantisProjectName == null || pMantisProjectName.trim().length() == 0)
    {
      throw new PluginServiceException("pMantisProjectName is mandatory");
    }

    if (pTargetMantisInstanceBaseURL == null || pTargetMantisInstanceBaseURL.trim().length() == 0)
    {
      throw new PluginServiceException("pTargetMantisInstanceBaseURL is mandatory");
    }

    try
    {
      LOG.info(String.format("Import Mantis Project [with name=%s, target url=%s, file=%s]", pMantisProjectName,
                             pTargetMantisInstanceBaseURL, pMantisDataFilePath));

      final File importXML = new File(pMantisDataFilePath);
      if (!importXML.exists())
      {
        throw new FileNotFoundException(String.format("Unable to read the configuration file [file=%s]",
                                                      importXML.getPath()));
      }

      // build the mantis instance URL
      final URL mantisBaseURL = new URL(pTargetMantisInstanceBaseURL);

      // get the mantis connector
      final MantisSoapConnector mantisSoapConnector = getMantisConnector(mantisBaseURL);

      // Build JAXB component
      final JAXBContext jaxbContext = JAXBContext.newInstance(Mantis.class);
      final Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

      // Get the java root element Mantis
      final InputStream is = new FileInputStream(importXML);
      final Mantis mantisRootElement = (Mantis) unMarshaller.unmarshal(is);
      final Project project = mantisRootElement.getProject();

      // Get the existing projectId
      final BigInteger projectId = getProjectId(pMantisProjectName, mantisSoapConnector);
      if (projectId == null || Objects.equals(projectId, BigInteger.valueOf(0)))
      {
        throw new PluginServiceException(String.format("Unable to find Mantis projectId [with name=%s]",
                                                       pMantisProjectName));
      }

      // Build the users map
      final Map<String, AccountData> usersMap = buildUsersMap(mantisSoapConnector, projectId);

      // Build the custom fields map
      final Map<String, BigInteger> customFieldsMap = importCustomFields(mantisRootElement, mantisSoapConnector,
                                                                         projectId);

      // Import categories
      importCategories(project, mantisSoapConnector, projectId);

      // Import versions
      importVersions(project, mantisSoapConnector, projectId);

      // Import configs
      importConfigs(project, mantisSoapConnector, projectId);

      // Import custom fields
      importProjectCustomFields(project, mantisSoapConnector, projectId, customFieldsMap);

      // Import Bugs, Bugs objects and relationships
      importBugs(project, mantisSoapConnector, projectId, usersMap, customFieldsMap);

      // build result file with statuses
      buildResultFile(pMantisDataFilePath, mantisRootElement);

      LOG.info("Import is done !");
    }
    catch (final JAXBException e)
    {
      throw new PluginServiceException("An error occurred during unmarshalling operation", e);
    }
    catch (final MalformedURLException e)
    {
      throw new PluginServiceException("The mantis target base URL argument is incorrect", e);
    }
    catch (final MantisSoapException e)
    {
      throw new PluginServiceException("An error occurred during Mantis Soap communication", e);
    }
    catch (final Exception e)
    {
      throw new PluginServiceException("A technical error occurred during Mantis import operation", e);
    }
  }

  private MantisSoapConnector getMantisConnector(final URL pMantisBaseURL)
      throws MantisSoapException, PluginServiceException
  {
    return mantisSoapClient.getConnector(pluginConfigurationService.getClientURL(pMantisBaseURL),
                                         pluginConfigurationService.getClientAdmin(),
                                         pluginConfigurationService.getClientPwd());
  }

  private BigInteger getProjectId(final String pProjectName, final MantisSoapConnector pConnector)
      throws MantisSoapException
  {
    return mantisSoapClient.mc_project_get_id_from_name(pConnector, pProjectName);
  }

  private Map<String, AccountData> buildUsersMap(final MantisSoapConnector pConnector,
                                                 final BigInteger pMantisProjectId) throws MantisSoapException
  {
    LOG.info("Build Users Map...");
    final Map<String, AccountData> usersMap = new HashMap<String, AccountData>();
    final AccountData[] accountDatas = mantisSoapClient.mc_project_get_users(pConnector, pMantisProjectId,
                                                                             ANYBODY_ACCESS_LEVEL);
    if (accountDatas != null)
    {
      for (final AccountData accountData : accountDatas)
      {
        LOG.info(String.format("Add user to map [with name=%s]", accountData.getName()));
        usersMap.put(accountData.getName(), accountData);
      }
    }

    return usersMap;
  }

  private Map<String, BigInteger> importCustomFields(final Mantis pMantisRootElement,
                                                     final MantisSoapConnector pConnector, final BigInteger pProjectId)
  {
    final Map<String, BigInteger> customFieldsMap = new HashMap<String, BigInteger>();

    // Get existing customfields to avoid duplicated trials
    ObjectRef[] existingCustomFields = null;
    try
    {
      existingCustomFields = mantisSoapClient.mc_enum_custom_field_types(pConnector);
    }
    catch (final Exception e)
    {
      LOG.error("An error occurred during getting existing custom fields types", e);
    }

    // Create customFields
    final List<CustomField> customFields = pMantisRootElement.getCustomField();
    for (final CustomField customField : customFields)
    {
      if (ImportStatus.DONE.equals(customField.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        if (!existCustomField(customField.getName(), existingCustomFields))
        {
          try
          {
            LOG.info(String.format("Import custom field [with field name=%s]", customField.getName()));
            final BigInteger customFieldId = mantisSoapClient.mc_add_custom_field(pConnector, MantisImportDataMapper
                                                                                                  .toCustomFieldDefinitionData(customField));
            customField.setId(customFieldId);
            status = ImportStatus.DONE;
          }
          catch (final Exception e)
          {
            LOG.error(String.format("An error occurred during import of custom field [with field name=%s]",
                                    customField.getName()), e);
            status = ImportStatus.ON_ERROR;
          }
        }
        else
        {
          customField.setId(getExistingCustomField(customField.getName(), existingCustomFields));
          status = ImportStatus.DONE;
        }

        // Set the new status
        customField.setImportStatus(status);
      }

      // in all cases, add the custom field to the corresponding map
      customFieldsMap.put(customField.getName(), customField.getId());
    }

    return customFieldsMap;
  }

  private void importCategories(final Project pProject, final MantisSoapConnector pConnector,
                                final BigInteger pProjectId)
  {
    // Create categories
    final List<Category> categories = pProject.getCategorySection().getCategory();
    for (final Category category : categories)
    {
      if (ImportStatus.DONE.equals(category.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        try
        {
          LOG.info(String.format("Import category [with name=%s]", category.getName()));
          mantisSoapClient.mc_project_add_category(pConnector, pProjectId, category.getName());
          status = ImportStatus.DONE;
        }
        catch (final Exception e)
        {
          LOG.error(String.format("An error occurred during import of category [with name=%s]", category.getName()), e);
          status = ImportStatus.ON_ERROR;
        }

        // Set the new status
        category.setImportStatus(status);
      }
    }
  }

  private void importVersions(final Project pProject, final MantisSoapConnector pConnector, final BigInteger pProjectId)
  {
    // Create versions
    final List<Version> versions = pProject.getVersionSection().getVersion();
    for (final Version version : versions)
    {
      if (ImportStatus.DONE.equals(version.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        try
        {
          LOG.info(String.format("Import version [with version name=%s]", version.getName()));
          mantisSoapClient.mc_project_version_add(pConnector, MantisImportDataMapper.toProjectVersionData(version,
                                                                                                          pProjectId));
          status = ImportStatus.DONE;
        }
        catch (final Exception e)
        {
          LOG.error(String.format("An error occurred during import of version [with name=%s]", version.getName()), e);
          status = ImportStatus.ON_ERROR;
        }

        // Set the new status
        version.setImportStatus(status);
      }
    }
  }

  private void importConfigs(final Project pProject, final MantisSoapConnector pConnector, final BigInteger pProjectId)
  {
    // Create configs
    final List<Config> configs = pProject.getConfigSection().getConfig();
    for (final Config config : configs)
    {
      if (ImportStatus.DONE.equals(config.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        try
        {
          LOG.info(String.format("Import config [with config id=%s]", config.getConfigId()));
          mantisSoapClient.mc_config_set_project(pConnector, MantisImportDataMapper.toConfigData(config), pProjectId);
          status = ImportStatus.DONE;
        }
        catch (final Exception e)
        {
          LOG.error(String.format("An error occurred during import of config [with id=%s]", config.getConfigId()), e);
          status = ImportStatus.ON_ERROR;
        }

        // Set the new status
        config.setImportStatus(status);
      }
    }
  }

  private void importProjectCustomFields(final Project pProject, final MantisSoapConnector pConnector,
                                         final BigInteger pProjectId, final Map<String, BigInteger> pCustomFieldsMap)
  {
    // Create project customFields
    final List<ProjectCustomField> projectCustomFields = pProject.getProjectCustomFieldSection().getCustomField();
    for (final ProjectCustomField projectCustomField : projectCustomFields)
    {
      if (ImportStatus.DONE.equals(projectCustomField.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        if (pCustomFieldsMap.containsKey(projectCustomField.getFieldName()))
        {
          try
          {
            LOG.info(String.format("Import project custom field [with field name=%s]",
                                   projectCustomField.getFieldName()));

            final boolean link = mantisSoapClient.mc_link_custom_field_to_project(pConnector, pProjectId,
                                                                                  pCustomFieldsMap
                                                                                      .get(projectCustomField
                                                                                               .getFieldName()));

            if (!link)
            {
              status = ImportStatus.ON_ERROR;
            }
            else
            {
              status = ImportStatus.DONE;
            }
          }
          catch (final Exception e)
          {
            LOG.error(String.format("An error occurred during import of project custom field [with field name=%s]",
                                    projectCustomField.getFieldName()), e);
            status = ImportStatus.ON_ERROR;
          }
        }
        else
        {
          LOG.error(String.format("Unable to find custom field id in custom fields map [with field name=%s]",
                                  projectCustomField.getFieldName()));
          status = ImportStatus.ON_ERROR;
        }
        // Set the new status
        projectCustomField.setImportStatus(status);
      }
    }
  }

  private void importBugs(final Project pProject, final MantisSoapConnector pConnector, final BigInteger pProjectId,
                          final Map<String, AccountData> pUsersMap, final Map<String, BigInteger> pCustomFieldsMap)
      throws IOException
  {
    // build a map with old and new bugs ids
    final Map<BigInteger, BigInteger> bugsIdMapping = new HashMap<BigInteger, BigInteger>();
    // build a map with old and new bugs notes ids
    final Map<BigInteger, BigInteger> bugsNotesIdMapping = new HashMap<BigInteger, BigInteger>();

    // Create bugs objects and set mapping between old and mew bug's ids
    final List<Bug> bugs = pProject.getBugSection().getBug();
    for (final Bug bug : bugs)
    {
      BigInteger newBugId = null;
      if (ImportStatus.DONE.equals(bug.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        try
        {
          LOG.info(String.format("Import bug [with id=%s]", bug.getSourceId()));
          newBugId = mantisSoapClient.mc_issue_add(pConnector, MantisImportDataMapper.toIssueData(bug, pUsersMap
                                                                                                           .get(bug.getHandlerUsername()),
                                                                                                  pUsersMap
                                                                                                      .get(bug.getReporterUsername()),
                                                                                                  pProjectId,
                                                                                                  pProject.getName(),
                                                                                                  pCustomFieldsMap));
          bug.setTargetId(newBugId);
          status = ImportStatus.DONE;

          // add the ids to the bugs mapping
          bugsIdMapping.put(bug.getSourceId(), bug.getTargetId());

          // remove import history
          removeImportHistory(pConnector, bug.getTargetId());
        }
        catch (final Exception e)
        {
          LOG.error(String.format("An error occurred during import of bug [with id=%s]", bug.getSourceId()), e);
          status = ImportStatus.ON_ERROR;
        }

        // Set the new status
        bug.setImportStatus(status);
      }
      else
      {
        // add the ids to the bugs mapping even if the bug is already imported : it can be useful for
        // relationships
        bugsIdMapping.put(bug.getSourceId(), bug.getTargetId());
      }

      // import bugs objects
      if (ImportStatus.DONE.equals(bug.getImportStatus()))
      {
        // Create notes
        importNotes(pProject, bug, pConnector, pUsersMap, bugsNotesIdMapping);

        // Create attachments
        importAttachments(pProject, bug, pConnector, pUsersMap);

        // Create histories
        importHistories(pProject, bug, pConnector, pUsersMap, bugsNotesIdMapping);
      }
    }

    // Import Bugs links
    importBugLinks(pConnector, bugs, bugsIdMapping);

    // Import Bug relationships
    importBugRelationships(pProject, pConnector, pProjectId, bugsIdMapping);

    // loop again on the bugs to update the dates at the end of the process
    for (final Bug bug : bugs)
    {
      if (ImportStatus.DONE.equals(bug.getImportStatus()))
      {
        // import bug dates
        importBugDates(pConnector, bug);
      }
    }
  }

  private void buildResultFile(final String pMantisDataFilePath, final Mantis pMantisRootElement) throws JAXBException
  {
    // Create the result file
    final File resultXML = new File(pMantisDataFilePath);

    final JAXBContext jaxbContext = JAXBContext.newInstance(Mantis.class);
    final Marshaller  marshaller  = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    marshaller.marshal(pMantisRootElement, resultXML);
  }

  private boolean existCustomField(final String pCustomFieldName, final ObjectRef[] pCustomFields)
  {
    boolean exist = false;
    for (final ObjectRef customField : pCustomFields)
    {
      if (customField.getName().equals(pCustomFieldName))
      {
        exist = true;
        break;
      }
    }
    return exist;
  }

  private BigInteger getExistingCustomField(final String pCustomFieldName, final ObjectRef[] pCustomFields)
  {
    BigInteger id = null;
    for (final ObjectRef customField : pCustomFields)
    {
      if (customField.getName().equals(pCustomFieldName))
      {
        id = customField.getId();
        break;
      }
    }
    return id;
  }

  private void removeImportHistory(final MantisSoapConnector pConnector, final BigInteger pBugId)
  {
    try
    {
      mantisSoapClient.mc_issue_delete_last_history(pConnector, pBugId);
    }
    catch (final Exception e)
    {
      LOG.error(String.format("An error occurred during deleting last issue history of bug [with id=%s]", pBugId), e);
    }
  }

  private void importNotes(final Project pProject, final Bug pBug, final MantisSoapConnector pConnector,
                           final Map<String, AccountData> pUsersMap, final Map<BigInteger, BigInteger> pNotesIdMap)
  {
    // Create notes
    final List<Note> notes = pBug.getNoteSection().getNote();
    for (final Note note : notes)
    {
      if (ImportStatus.DONE.equals(note.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        try
        {
          LOG.info(String.format("Import note [with date submitted=%s]", note.getDateSubmitted().toString()));
          final BigInteger issueNoteId = mantisSoapClient.mc_issue_note_add(pConnector, pBug.getTargetId(),
                                                                            MantisImportDataMapper.toIssueNoteData(note,
                                                                                                                   pUsersMap
                                                                                                                       .get(note.getReporterUsername())));
          note.setTargetId(issueNoteId);
          status = ImportStatus.DONE;

          // fill the notes ids map
          pNotesIdMap.put(note.getSourceId(), note.getTargetId());

          // remove import history
          removeImportHistory(pConnector, pBug.getTargetId());
        }
        catch (final Exception e)
        {
          LOG.error(String.format("An error occurred during import of bug note [with id=%s]", note.getSourceId()), e);
          status = ImportStatus.ON_ERROR;
        }

        // Set the new status
        note.setImportStatus(status);
      }
      else
      {
        // fill the notes'ids map
        pNotesIdMap.put(note.getSourceId(), note.getTargetId());
      }

      // import note objects
      if (ImportStatus.DONE.equals(note.getImportStatus()))
      {
        // update the note in order to set the reporter (bug Mantis)
        importNoteReporter(pConnector, note, pBug, pUsersMap);

        // update the note in order to set the dates (bug Mantis)
        importNoteDates(pConnector, note, pBug, pUsersMap);
      }
    }
  }

  private void importAttachments(final Project pProject, final Bug pBug, final MantisSoapConnector pConnector,
                                 final Map<String, AccountData> pUsersMap) throws IOException
  {
    // Create attachments
    final List<Attachment> attachments = pBug.getAttachmentSection().getAttachment();
    for (final Attachment attachment : attachments)
    {
      if (ImportStatus.DONE.equals(attachment.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        try
        {
          LOG.info(String.format("Import attachment [with filename=%s]", attachment.getFilename()));
          if (pUsersMap.containsKey(attachment.getUsername()))
          {
            mantisSoapClient.mc_issue_attachment_uploader_add(pConnector, pBug.getTargetId(), pUsersMap.get(attachment
                                                                                                                .getUsername()),
                                                              attachment.getFilename(), attachment.getContentType(),
                                                              getFileContent(attachment.getExportCopyPath()));
            status = ImportStatus.DONE;

            // remove import history
            removeImportHistory(pConnector, pBug.getTargetId());
          }
          else
          {
            LOG.error(String.format("Unable to find username in users map [with username=%s]",
                                    attachment.getUsername()));
          }
        }
        catch (final Exception e)
        {
          LOG.error(String.format("An error occurred during import of bug attachment [with file name=%s]",
                                  attachment.getFilename()), e);
          status = ImportStatus.ON_ERROR;
        }

        // Set the new status
        attachment.setImportStatus(status);
      }
    }
  }

  private void importHistories(final Project pProject, final Bug pBug, final MantisSoapConnector pConnector,
                               final Map<String, AccountData> pUsersMap, final Map<BigInteger, BigInteger> pNotesIdMap)
  {
    // Create histories
    final List<History> histories = pBug.getHistorySection().getHistory();
    for (final History history : histories)
    {
      if (ImportStatus.DONE.equals(history.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        try
        {
          LOG.info(String.format("Import history [with date=%s]", history.getDate().toString()));
          if (pUsersMap.containsKey(history.getUsername()))
          {
            // if history is related to a bug note
            if (history.getType().equals(BigInteger.valueOf(2)) || history.getType().equals(BigInteger.valueOf(3))
                    || history.getType().equals(BigInteger.valueOf(4)))
            {
              if (history.getOldValue() != null && history.getOldValue().trim().length() > 0)
              {
                if (pNotesIdMap.containsKey(history.getOldValue()))
                {
                  history.setOldValue(String.valueOf(pNotesIdMap.get(history.getOldValue())));
                }
                else
                {
                  history.setOldValue(NOT_FOUND_VALUE);
                }
              }
            }
            // If the history is a monitoring : link it to the new user
            else if (history.getType().equals(BigInteger.valueOf(13)) || history.getType().equals(BigInteger
                                                                                                      .valueOf(12)))
            {
              final String username = getUsernameByOldUserId(new BigInteger(history.getOldValue()), pProject);
              if (username != null && pUsersMap.containsKey(username))
              {
                history.setOldValue(String.valueOf(pUsersMap.get(username).getId()));
              }
            }

            // If the field type is handler_id
            if (history.getField().equals("handler_id"))
            {
              if (history.getOldValue().equals("0") == false)
              {
                final String username = getUsernameByOldUserId(new BigInteger(history.getOldValue()), pProject);
                if (username != null && pUsersMap.containsKey(username))
                {
                  history.setOldValue(String.valueOf(pUsersMap.get(username).getId()));
                }
              }

              if (history.getNewValue().equals("0") == false)
              {
                final String username = getUsernameByOldUserId(new BigInteger(history.getNewValue()), pProject);
                if (username != null && pUsersMap.containsKey(username))
                {
                  history.setNewValue(String.valueOf(pUsersMap.get(username).getId()));
                }
              }
            }

            mantisSoapClient.mc_issue_add_history(pConnector, pBug.getTargetId(),
                                                  MantisImportDataMapper.toHistoryData(history, pUsersMap.get(history
                                                                                                                  .getUsername())));
            status = ImportStatus.DONE;
          }
          else
          {
            LOG.error(String.format("Unable to find username in users map [with username=%s]", history.getUsername()));
          }
        }
        catch (final Exception e)
        {
          LOG.error(String.format("An error occurred during import of bug history [with date=%s]",
                                  history.getDate().toString()), e);
          status = ImportStatus.ON_ERROR;
        }

        // Set the new status
        history.setImportStatus(status);
      }
    }
  }

  private void importBugLinks(final MantisSoapConnector pConnector, final List<Bug> pBugs,
                              final Map<BigInteger, BigInteger> pBugsIdMapping)
  {
    for (final Bug bug : pBugs)
    {
      if (ImportStatus.DONE.equals(bug.getImportStatus()))
      {
        if (matchLinks(bug.getDescription()))
        {
          try
          {
            final String newDescription = replaceLinks(bug.getDescription(), pBugsIdMapping);
            mantisSoapClient.mc_issue_update_description(pConnector, bug.getTargetId(), newDescription);

            // remove import history
            removeImportHistory(pConnector, bug.getTargetId());
          }
          catch (final Exception e)
          {
            LOG.error(String.format("An error occurred during updating bug description [with id=%s]",
                                    bug.getSourceId()), e);
          }
        }

        final List<Note> notes = bug.getNoteSection().getNote();
        for (final Note note : notes)
        {
          if (ImportStatus.DONE.equals(note.getImportStatus()))
          {
            if (matchLinks(note.getText()))
            {
              try
              {
                final String newText = replaceLinks(note.getText(), pBugsIdMapping);
                mantisSoapClient.mc_issue_note_update_text(pConnector, note.getTargetId(), newText);

                // remove import history
                removeImportHistory(pConnector, bug.getTargetId());
              }
              catch (final Exception e)
              {
                LOG.error(String.format("An error occurred during updating bug note text [with note id=%s]",
                                        note.getSourceId()), e);
              }
            }
          }
        }
      }
    }
  }

  private void importBugRelationships(final Project pProject, final MantisSoapConnector pConnector,
                                      final BigInteger pProjectId, final Map<BigInteger, BigInteger> pBugIdsMapping)
  {
    // Create bug relationships
    final List<BugRelationship> bugRelationships = pProject.getBugRelationshipSection().getBugRelationship();
    for (final BugRelationship bugRelationship : bugRelationships)
    {
      if (ImportStatus.DONE.equals(bugRelationship.getImportStatus()) == false)
      {
        ImportStatus status = ImportStatus.TO_DO;
        try
        {
          LOG.info(String.format("Import bug relationship [with bug source id=%s, bug target id=%s]",
                                 bugRelationship.getSourceId(), bugRelationship.getTargetId()));
          // Update the bugs ids with bugsIdMapping
          final BigInteger newTargetBugId = pBugIdsMapping.get(bugRelationship.getTargetId());
          final BigInteger newSourceBugId = pBugIdsMapping.get(bugRelationship.getSourceId());

          if (newTargetBugId != null && newSourceBugId != null)
          {
            mantisSoapClient.mc_issue_relationship_add(pConnector, newSourceBugId,
                                                       MantisImportDataMapper.toRelationshipData(bugRelationship,
                                                                                                 newTargetBugId));
            status = ImportStatus.DONE;

            // remove import history
            removeImportHistory(pConnector, newSourceBugId);
          }
          else
          {
            LOG.error(String
                          .format("Unable to find new bugs ids mapping for bug relationship [with original source id=%s, original target id=%s]",
                                  bugRelationship.getSourceId(), bugRelationship.getSourceId()));
            status = ImportStatus.ON_ERROR;
          }
        }
        catch (final Exception e)
        {
          LOG.error(String
                        .format("An error occurred during import of bug relationship [with original source id=%s, original target id=%s]",
                                bugRelationship.getSourceId(), bugRelationship.getTargetId()), e);
          status = ImportStatus.ON_ERROR;
        }

        // Set the new status
        bugRelationship.setImportStatus(status);
      }
    }
  }

  private void importBugDates(final MantisSoapConnector pConnector, final Bug pBug)
  {
    if (ImportStatus.DONE.equals(pBug.getImportDatesStatus()) == false)
    {
      ImportStatus status = ImportStatus.TO_DO;
      try
      {
        LOG.info(String.format("Update bug dates [with id=%s]", pBug.getSourceId()));
        final boolean updateOne = mantisSoapClient.mc_issue_update_last_updated(pConnector, pBug.getTargetId(),
                                                                                MantisImportDataMapper
                                                                                    .toCalendar(pBug.getLastUpdated()));

        // remove import history
        removeImportHistory(pConnector, pBug.getTargetId());

        final boolean updateTwo = mantisSoapClient.mc_issue_update_date_submitted(pConnector, pBug.getTargetId(),
                                                                                  MantisImportDataMapper
                                                                                      .toCalendar(pBug.getDateSubmitted()));

        // remove import history
        removeImportHistory(pConnector, pBug.getTargetId());

        if (updateOne && updateTwo)
        {
          status = ImportStatus.DONE;
        }
        else
        {
          LOG.error(String.format("Unable to update dates issue of bug [with id=%s]", pBug.getSourceId()));
          status = ImportStatus.ON_ERROR;
        }
      }
      catch (final Exception e)
      {
        LOG.error(String.format("An error occurred during updating dates issue of bug [with id=%s]",
                                pBug.getSourceId()), e);
        status = ImportStatus.ON_ERROR;
      }

      // Set the new status
      pBug.setImportDatesStatus(status);
    }
  }

  private void importNoteReporter(final MantisSoapConnector pConnector, final Note pNote, final Bug pBug,
                                  final Map<String, AccountData> pUsersMap)
  {
    if (ImportStatus.DONE.equals(pNote.getImportReporterStatus()) == false)
    {
      ImportStatus status = ImportStatus.TO_DO;
      try
      {
        LOG.info(String.format("Update Note reporter [with issue note id=%s]", pNote.getSourceId()));
        final IssueNoteData issueNoteData = MantisImportDataMapper.toIssueNoteData(pNote, pUsersMap.get(pNote
                                                                                                            .getReporterUsername()));
        final boolean updatedReporter = mantisSoapClient.mc_issue_note_update_reporter(pConnector, issueNoteData);
        if (!updatedReporter)
        {
          LOG.error(String.format("Unable to update reporter issue note [with issue note id=%s]",
                                  issueNoteData.getId()));
          status = ImportStatus.ON_ERROR;
        }
        else
        {
          status = ImportStatus.DONE;
        }

        // remove import history
        removeImportHistory(pConnector, pBug.getTargetId());
      }
      catch (final Exception e)
      {
        LOG.error(String.format("Unable to update reporter issue note [with issue note id=%s]", pNote.getSourceId()),
                  e);
        status = ImportStatus.ON_ERROR;
      }

      // Set the new status
      pNote.setImportReporterStatus(status);
    }
  }

  private void importNoteDates(final MantisSoapConnector pConnector, final Note pNote, final Bug pBug,
                               final Map<String, AccountData> pUsersMap)
  {
    if (ImportStatus.DONE.equals(pNote.getImportDatesStatus()) == false)
    {
      ImportStatus status = ImportStatus.TO_DO;
      try
      {
        LOG.info(String.format("Update Note dates [with issue note id=%s]", pNote.getSourceId()));
        final IssueNoteData issueNoteData = MantisImportDataMapper.toIssueNoteData(pNote, pUsersMap.get(pNote
                                                                                                            .getReporterUsername()));
        final boolean updateDates = mantisSoapClient.mc_issue_note_update_dates(pConnector, issueNoteData);
        if (!updateDates)
        {
          LOG.error(String.format("Unable to update dates issue note [with issue note id=%s]", issueNoteData.getId()));
          status = ImportStatus.ON_ERROR;
        }
        else
        {
          status = ImportStatus.DONE;
        }

        // remove import history
        removeImportHistory(pConnector, pBug.getTargetId());
      }
      catch (final Exception e)
      {
        LOG.error(String.format("Unable to update dates issue note [with issue note id=%s]", pNote.getSourceId()), e);
        status = ImportStatus.ON_ERROR;
      }

      // Set the new status
      pNote.setImportDatesStatus(status);
    }
  }

  private byte[] getFileContent(final String pFilePath) throws IOException
  {
    final File file = new File(pFilePath);
    return FileUtils.readFileToByteArray(file);
  }

  private String getUsernameByOldUserId(final BigInteger pOldUserId, final Project pProject)
  {
    String           username = null;
    final List<User> users    = pProject.getUserSection().getUser();
    for (final User user : users)
    {
      if (user.getId().equals(pOldUserId))
      {
        username = user.getUsername();
        break;
      }
    }

    return username;
  }

  private boolean matchLinks(final String pText)
  {
    return BUG_ID_PATTERN.matcher(pText).find();
  }

  private String replaceLinks(final String pText, final Map<BigInteger, BigInteger> pBugsIdMapping)
  {
    String        result = pText;
    final Matcher m      = BUG_ID_PATTERN.matcher(pText);
    while (m.find())
    {
      final String id = m.group().replace("#", "");
      final BigInteger oldId = new BigInteger(id);
      if (pBugsIdMapping.containsKey(oldId))
      {
        result = result.replaceAll(String.valueOf(oldId), String.valueOf(pBugsIdMapping.get(oldId)));
      }
      else
      {
        result = result.replaceAll(String.valueOf(oldId), NOT_FOUND_VALUE);
      }
    }

    return result;
  }

  /**
   * @param mantisSoapClient
   *     the mantisSoapClient to set
   */
  public void setMantisSoapClient(final MantisSoapClient mantisSoapClient)
  {
    this.mantisSoapClient = mantisSoapClient;
  }

  /**
   * @param pluginConfigurationService
   *     the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pluginConfigurationService)
  {
    this.pluginConfigurationService = pluginConfigurationService;
  }

}
