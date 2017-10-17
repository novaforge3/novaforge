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
package org.novaforge.forge.plugins.bugtracker.mantis.importexport.internal.datamappers;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AttachmentData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ConfigData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.CustomFieldDefinitionData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.CustomFieldValueForIssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.HistoryData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueNoteData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ObjectRef;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectVersionData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.RelationshipData;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Attachment;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Bug;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.BugCustomField;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.BugRelationship;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Config;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.CustomField;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.History;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Note;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Project;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Reference;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.User;
import org.novaforge.forge.plugins.bugtracker.mantis.importexport.datas.model.Version;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public class MantisImportDataMapper
{
  public static ProjectData toProject(final Project pProject)
  {
    final ProjectData projectData = new ProjectData();
    projectData.setAccess_min(toObjectRef(pProject.getAccessMin()));
    projectData.setDescription(pProject.getDescription());
    projectData.setEnabled(pProject.isEnabled());
    projectData.setFile_path(pProject.getFilePath());
    projectData.setName(pProject.getName());
    projectData.setStatus(toObjectRef(pProject.getStatus()));
    projectData.setView_state(toObjectRef(pProject.getViewState()));
    return projectData;
  }

  public static ObjectRef toObjectRef(final Reference pReference)
  {
    final ObjectRef objectRef = new ObjectRef();
    objectRef.setId(pReference.getId());
    objectRef.setName(pReference.getName());
    return objectRef;
  }

  public static ProjectVersionData toProjectVersionData(final Version pVersion, final BigInteger pProjectId)
      throws DatatypeConfigurationException
  {
    final ProjectVersionData projectVersionData = new ProjectVersionData();
    projectVersionData.setDate_order(toCalendar(pVersion.getDateOrder()));
    projectVersionData.setDescription(pVersion.getDescription());
    projectVersionData.setName(pVersion.getName());
    projectVersionData.setObsolete(pVersion.isObsolete());
    projectVersionData.setReleased(pVersion.isReleased());
    projectVersionData.setProject_id(pProjectId);
    return projectVersionData;
  }

  public static Calendar toCalendar(final XMLGregorianCalendar xc) throws DatatypeConfigurationException
  {
    Calendar calendar = null;
    if (xc != null)
    {
      calendar = xc.toGregorianCalendar();
    }
    return calendar;
  }

  public static ConfigData toConfigData(final Config pConfig)
  {
    final ConfigData configData = new ConfigData();
    configData.setConfig_id(pConfig.getConfigId());
    configData.setConfig_option(pConfig.getConfigOption());
    return configData;
  }

  public static CustomFieldDefinitionData toCustomFieldDefinitionData(final CustomField pCustomField)
  {
    final CustomFieldDefinitionData customFieldDefinitionData = new CustomFieldDefinitionData();
    customFieldDefinitionData.setAccess_level_r(pCustomField.getAccessLevelR());
    customFieldDefinitionData.setAccess_level_rw(pCustomField.getAccessLevelRw());
    customFieldDefinitionData.setDefault_value(pCustomField.getDefaultValue());
    customFieldDefinitionData.setDisplay_closed(pCustomField.isDisplayClosed());
    customFieldDefinitionData.setDisplay_report(pCustomField.isDisplayReport());
    customFieldDefinitionData.setDisplay_resolved(pCustomField.isDisplayResolved());
    customFieldDefinitionData.setDisplay_update(pCustomField.isDisplayUpdate());
    customFieldDefinitionData.setLength_max(pCustomField.getLengthMax());
    customFieldDefinitionData.setLength_min(pCustomField.getLengthMin());
    customFieldDefinitionData.setPossible_values(pCustomField.getPossibleValues());
    customFieldDefinitionData.setRequire_closed(pCustomField.isRequireClosed());
    customFieldDefinitionData.setRequire_report(pCustomField.isRequireReport());
    customFieldDefinitionData.setRequire_resolved(pCustomField.isRequireResolved());
    customFieldDefinitionData.setRequire_update(pCustomField.isRequireUpdate());
    customFieldDefinitionData.setType(pCustomField.getType());
    customFieldDefinitionData.setValid_regexp(pCustomField.getValidRegexp());

    // Set the field with only the name
    final ObjectRef objectRef = new ObjectRef();
    objectRef.setName(pCustomField.getName());
    customFieldDefinitionData.setField(objectRef);

    return customFieldDefinitionData;
  }

  public static AttachmentData toAttachmentData(final Attachment pAttachment, final BigInteger pUserId)
      throws DatatypeConfigurationException, MalformedURIException
  {
    final AttachmentData attachmentData = new AttachmentData();
    attachmentData.setContent_type(pAttachment.getContentType());
    attachmentData.setDate_submitted(toCalendar(pAttachment.getDateSubmitted()));
    attachmentData.setFilename(pAttachment.getFilename());
    attachmentData.setDownload_url(new URI(pAttachment.getDownloadUrl()));
    attachmentData.setSize(pAttachment.getSize());
    attachmentData.setUser_id(pUserId);
    return attachmentData;
  }

  public static HistoryData toHistoryData(final History pHistory, final AccountData pAccountData)
      throws DatatypeConfigurationException
  {
    final HistoryData historyData = new HistoryData();
    historyData.setDate(pHistory.getDate());
    historyData.setField(pHistory.getField());
    historyData.setNew_value(pHistory.getNewValue());
    historyData.setOld_value(pHistory.getOldValue());
    historyData.setType(pHistory.getType());
    historyData.setUsername(pHistory.getUsername());
    historyData.setUserid(pAccountData.getId());
    return historyData;
  }

  public static RelationshipData toRelationshipData(final BugRelationship pBugRelationship,
      final BigInteger pNewIssueTargetId)
  {
    final RelationshipData relationshipData = new RelationshipData();
    relationshipData.setType(toObjectRef(pBugRelationship.getType()));
    relationshipData.setTarget_id(pNewIssueTargetId);
    return relationshipData;
  }

  public static IssueNoteData toIssueNoteData(final Note pNote, final AccountData pReporter)
      throws DatatypeConfigurationException
  {
    final IssueNoteData issueNoteData = new IssueNoteData();
    issueNoteData.setDate_submitted(toCalendar(pNote.getDateSubmitted()));
    issueNoteData.setLast_modified(toCalendar(pNote.getLastModified()));
    issueNoteData.setNote_attr(pNote.getNoteAttr());
    issueNoteData.setNote_type(pNote.getNoteType());
    if (pReporter != null)
    {
      issueNoteData.setReporter(pReporter);
    }
    issueNoteData.setText(pNote.getText());
    issueNoteData.setTime_tracking(pNote.getTimeTracking());
    issueNoteData.setView_state(toObjectRef(pNote.getViewState()));
    issueNoteData.setId(pNote.getTargetId());
    return issueNoteData;
  }

  public static AccountData toAccountData(final User pUser)
  {
    final AccountData accountData = new AccountData();
    accountData.setAccess(pUser.getAccess());
    accountData.setEmail(pUser.getEmail());
    accountData.setId(pUser.getId());
    accountData.setLanguage(pUser.getLanguage());
    accountData.setName(pUser.getUsername());
    accountData.setReal_name(pUser.getRealName());
    return accountData;
  }

  public static IssueData toIssueData(final Bug pBug, final AccountData pHandler,
      final AccountData pReporter, final BigInteger pProjectId, final String pProjectName,
      final Map<String, BigInteger> pCustomFieldsMap) throws DatatypeConfigurationException
  {
    final IssueData issueData = new IssueData();
    // issueData.setId(pBug.getId());
    final ObjectRef projectRef = new ObjectRef();
    projectRef.setId(pProjectId);
    projectRef.setName(pProjectName);
    issueData.setProject(projectRef);
    issueData.setAdditional_information(pBug.getAdditionalInformation());
    issueData.setBuild(pBug.getBuild());
    issueData.setCategory(pBug.getCategory());
    issueData.setDate_submitted(toCalendar(pBug.getDateSubmitted()));
    issueData.setDescription(pBug.getDescription());
    issueData.setDue_date(toCalendar(pBug.getDueDate()));
    issueData.setEta(toObjectRef(pBug.getEta()));
    issueData.setFixed_in_version(pBug.getFixedInVersion());
    if (pHandler != null)
    {
      issueData.setHandler(pHandler);
    }
    issueData.setLast_updated(toCalendar(pBug.getLastUpdated()));
    issueData.setOs(pBug.getOs());
    issueData.setOs_build(pBug.getOsBuild());
    issueData.setPlatform(pBug.getPlatform());
    issueData.setPriority(toObjectRef(pBug.getPriority()));
    issueData.setProjection(toObjectRef(pBug.getProjection()));
    if (pReporter != null)
    {
      issueData.setReporter(pReporter);
    }
    issueData.setReproducibility(toObjectRef(pBug.getReproducibility()));
    issueData.setResolution(toObjectRef(pBug.getResolution()));
    issueData.setSeverity(toObjectRef(pBug.getSeverity()));
    issueData.setSponsorship_total(pBug.getSponsorshipTotal());
    issueData.setStatus(toObjectRef(pBug.getStatus()));
    issueData.setSteps_to_reproduce(pBug.getStepsToReproduce());
    issueData.setSticky(pBug.isSticky());
    issueData.setSummary(pBug.getSummary());
    issueData.setTarget_version(pBug.getTargetVersion());
    issueData.setVersion(pBug.getVersion());
    issueData.setView_state(toObjectRef(pBug.getViewState()));

    // Get the bug customfields
    final List<BugCustomField> bugCustomFields = pBug.getBugCustomFieldSection().getBugCustomField();
    if (!bugCustomFields.isEmpty())
    {
      final CustomFieldValueForIssueData[] customFieldsArray = new CustomFieldValueForIssueData[bugCustomFields
          .size()];
      for (int i = 0; i < bugCustomFields.size(); i++)
      {
        customFieldsArray[i] = toCustomFieldValueForIssueData(bugCustomFields.get(i),
            pCustomFieldsMap.get(bugCustomFields.get(i).getFieldName()));
      }
      issueData.setCustom_fields(customFieldsArray);
    }

    return issueData;
  }

  public static CustomFieldValueForIssueData toCustomFieldValueForIssueData(
      final BugCustomField pBugCustomField, final BigInteger pCustomFieldId)
  {
    final CustomFieldValueForIssueData customFieldValueForIssueData = new CustomFieldValueForIssueData();
    final ObjectRef objectRef = new ObjectRef();
    objectRef.setName(pBugCustomField.getFieldName());
    objectRef.setId(pCustomFieldId);
    customFieldValueForIssueData.setField(objectRef);
    customFieldValueForIssueData.setValue(pBugCustomField.getValue());
    return customFieldValueForIssueData;

  }
}
