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
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author sbenoist
 */
public class MantisExportDataMapper
{
  public static XMLGregorianCalendar toXMLGregorianCalendar(final BigInteger pTime)
      throws DatatypeConfigurationException
  {
    XMLGregorianCalendar xc = null;
    if (pTime != null)
    {
      final GregorianCalendar gc = new GregorianCalendar();
      gc.setTimeInMillis((pTime.longValue()));
      xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    }
    return xc;
  }

  public static Project toProject(final ProjectData pProjectData)
  {
    final Project project = new Project();
    project.setAccessMin(toReference(pProjectData.getAccess_min()));
    project.setDescription(pProjectData.getDescription());
    project.setEnabled(pProjectData.getEnabled());
    project.setFilePath(pProjectData.getFile_path());
    project.setName(pProjectData.getName());
    project.setStatus(toReference(pProjectData.getStatus()));
    project.setViewState(toReference(pProjectData.getView_state()));
    return project;
  }

  public static Reference toReference(final ObjectRef pObjectref)
  {
    final Reference reference = new Reference();
    reference.setId(pObjectref.getId());
    reference.setName(pObjectref.getName());
    return reference;
  }

  public static Version toVersion(final ProjectVersionData pProjectVersionData)
      throws DatatypeConfigurationException
  {
    final Version version = new Version();
    version.setDateOrder(toXMLGregorianCalendar(pProjectVersionData.getDate_order()));
    version.setDescription(pProjectVersionData.getDescription());
    version.setName(pProjectVersionData.getName());
    version.setObsolete(pProjectVersionData.getObsolete());
    version.setReleased(pProjectVersionData.getReleased());
    return version;
  }

  public static XMLGregorianCalendar toXMLGregorianCalendar(final Calendar c) throws DatatypeConfigurationException
  {
    XMLGregorianCalendar xc = null;
    if (c != null)
    {
      final GregorianCalendar gc = new GregorianCalendar();
      gc.setTimeInMillis(c.getTimeInMillis());
      xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    }
    return xc;
  }

  public static Config toConfig(final ConfigData pConfigData)
  {
    final Config config = new Config();
    config.setConfigId(pConfigData.getConfig_id());
    config.setConfigOption(pConfigData.getConfig_option());
    return config;
  }

  public static CustomField toCustomField(final CustomFieldDefinitionData pCustomFieldData)
  {
    final CustomField customField = new CustomField();
    customField.setAccessLevelR(pCustomFieldData.getAccess_level_r());
    customField.setAccessLevelRw(pCustomFieldData.getAccess_level_rw());
    customField.setDefaultValue(pCustomFieldData.getDefault_value());
    customField.setDisplayClosed(pCustomFieldData.getDisplay_closed());
    customField.setDisplayReport(pCustomFieldData.getDisplay_report());
    customField.setDisplayResolved(pCustomFieldData.getDisplay_resolved());
    customField.setDisplayUpdate(pCustomFieldData.getDisplay_update());
    customField.setName(pCustomFieldData.getField().getName());
    customField.setLengthMax(pCustomFieldData.getLength_max());
    customField.setLengthMin(pCustomFieldData.getLength_min());
    customField.setPossibleValues(pCustomFieldData.getPossible_values());
    customField.setRequireClosed(pCustomFieldData.getRequire_closed());
    customField.setRequireReport(pCustomFieldData.getRequire_report());
    customField.setRequireResolved(pCustomFieldData.getRequire_resolved());
    customField.setRequireUpdate(pCustomFieldData.getRequire_update());
    customField.setType(pCustomFieldData.getType());
    customField.setValidRegexp(pCustomFieldData.getValid_regexp());
    return customField;
  }

  public static Attachment toAttachment(final AttachmentData pAttachmentData, final String pUsername,
      final String pExportFilePath) throws DatatypeConfigurationException
  {
    final Attachment attachment = new Attachment();
    attachment.setContentType(pAttachmentData.getContent_type());
    attachment.setDateSubmitted(toXMLGregorianCalendar(pAttachmentData.getDate_submitted()));
    attachment.setFilename(pAttachmentData.getFilename());
    attachment.setDownloadUrl(pAttachmentData.getDownload_url().toString());
    attachment.setSize(pAttachmentData.getSize());
    attachment.setUsername(pUsername);
    attachment.setExportCopyPath(pExportFilePath);
    return attachment;
  }

  public static History toHistory(final HistoryData pHistoryData) throws DatatypeConfigurationException
  {
    final History history = new History();
    history.setDate(pHistoryData.getDate());
    history.setField(pHistoryData.getField());
    history.setNewValue(pHistoryData.getNew_value());
    history.setOldValue(pHistoryData.getOld_value());
    history.setType(pHistoryData.getType());
    history.setUsername(pHistoryData.getUsername());
    return history;
  }

  public static BugRelationship toRelationship(final RelationshipData pRelationshipData,
      final BigInteger pSourceIssueId)
  {
    final BugRelationship relationship = new BugRelationship();
    relationship.setType(toReference(pRelationshipData.getType()));
    relationship.setTargetId(pRelationshipData.getTarget_id());
    relationship.setSourceId(pSourceIssueId);
    return relationship;
  }

  public static Note toNote(final IssueNoteData pIssueNoteData) throws DatatypeConfigurationException
  {
    final Note note = new Note();
    note.setSourceId(pIssueNoteData.getId());
    note.setDateSubmitted(toXMLGregorianCalendar(pIssueNoteData.getDate_submitted()));
    note.setLastModified(toXMLGregorianCalendar(pIssueNoteData.getLast_modified()));
    note.setNoteAttr(pIssueNoteData.getNote_attr());
    note.setNoteType(pIssueNoteData.getNote_type());
    note.setReporterUsername(pIssueNoteData.getReporter().getName());
    note.setText(pIssueNoteData.getText());
    note.setTimeTracking(pIssueNoteData.getTime_tracking());
    note.setViewState(toReference(pIssueNoteData.getView_state()));
    return note;
  }

  public static User toUser(final AccountData pAccountData)
  {
    final User user = new User();
    user.setAccess(pAccountData.getAccess());
    user.setEmail(pAccountData.getEmail());
    user.setId(pAccountData.getId());
    user.setLanguage(pAccountData.getLanguage());
    user.setUsername(pAccountData.getName());
    user.setRealName(pAccountData.getReal_name());
    return user;
  }

  public static Bug toBug(final IssueData pIssueData) throws DatatypeConfigurationException
  {
    final Bug bug = new Bug();
    bug.setSourceId(pIssueData.getId());
    bug.setAdditionalInformation(pIssueData.getAdditional_information());
    bug.setBuild(pIssueData.getBuild());
    bug.setCategory(pIssueData.getCategory());
    bug.setDateSubmitted(toXMLGregorianCalendar(pIssueData.getDate_submitted()));
    bug.setDescription(pIssueData.getDescription());
    bug.setDueDate(toXMLGregorianCalendar(pIssueData.getDue_date()));
    bug.setEta(toReference(pIssueData.getEta()));
    bug.setFixedInVersion(pIssueData.getFixed_in_version());
    if (pIssueData.getHandler() != null)
    {
      bug.setHandlerUsername(pIssueData.getHandler().getName());
    }
    bug.setLastUpdated(toXMLGregorianCalendar(pIssueData.getLast_updated()));
    bug.setOs(pIssueData.getOs());
    bug.setOsBuild(pIssueData.getOs_build());
    bug.setPlatform(pIssueData.getPlatform());
    bug.setPriority(toReference(pIssueData.getPriority()));
    bug.setProjection(toReference(pIssueData.getProjection()));
    if (pIssueData.getReporter() != null)
    {
      bug.setReporterUsername(pIssueData.getReporter().getName());
    }
    bug.setReproducibility(toReference(pIssueData.getReproducibility()));
    bug.setResolution(toReference(pIssueData.getResolution()));
    bug.setSeverity(toReference(pIssueData.getSeverity()));
    bug.setSponsorshipTotal(pIssueData.getSponsorship_total());
    bug.setStatus(toReference(pIssueData.getStatus()));
    bug.setStepsToReproduce(pIssueData.getSteps_to_reproduce());
    bug.setSticky(pIssueData.getSticky());
    bug.setSummary(pIssueData.getSummary());
    bug.setTargetVersion(pIssueData.getTarget_version());
    bug.setVersion(pIssueData.getVersion());
    bug.setViewState(toReference(pIssueData.getView_state()));
    return bug;
  }

  public static BugCustomField toIssueCustomField(
      final CustomFieldValueForIssueData pCustomFieldValueForIssueData)
  {
    final BugCustomField issueCustomField = new BugCustomField();
    issueCustomField.setFieldName(pCustomFieldValueForIssueData.getField().getName());
    issueCustomField.setValue(pCustomFieldValueForIssueData.getValue());
    return issueCustomField;

  }
}
