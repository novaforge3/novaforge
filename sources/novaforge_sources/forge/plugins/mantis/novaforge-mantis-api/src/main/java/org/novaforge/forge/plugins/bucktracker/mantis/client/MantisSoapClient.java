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
package org.novaforge.forge.plugins.bucktracker.mantis.client;

import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ConfigData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.CustomFieldDefinitionData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.FilterData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.HistoryData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHeaderData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHistoryStatusData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueNoteData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ObjectRef;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectStatusData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectVersionData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.RelationshipData;

import java.math.BigInteger;
import java.util.Calendar;

/**
 * @author lamirang
 */
public interface MantisSoapClient
{

  /**
   * @return Use to connect to mantis instance with specific url, username and password.
   * @param baseUrl
   *          represents the url of nexus instance
   * @param username
   *          represents username used to log in
   * @param password
   *          represents password used to log in
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built can occured if connection failed
   *           or client can be built
   * @throws
   */
  MantisSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
      throws MantisSoapException;

  /**
   * @param pArg2
   * @param pArg3
   * @return @
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_account_add
   *      (java.lang.String, java.lang.String,
   *      org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData, java.lang.String)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  BigInteger mc_account_add(final MantisSoapConnector pConnector, final AccountData pAccount,
      final String pPassword) throws MantisSoapException;

  /**
   * @param pArg2
   * @return @
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_account_delete
   *      (java.lang.String, java.lang.String, java.math.BigInteger)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  boolean mc_account_delete(final MantisSoapConnector pConnector, final BigInteger pUserId)
      throws MantisSoapException;

  /**
   * @param pUserId
   * @return user id
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_account_get
   *      (java.lang.String, java.lang.String, java.lang.String)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  BigInteger mc_account_get(final MantisSoapConnector pConnector, final String pUserId)
      throws MantisSoapException;

  /**
   * @param pUserId
   * @param pAccount
   * @param pPassword
   * @return boolean
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_account_update
   *      (java.lang.String, java.lang.String, java.math.BigInteger,
   *      org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData, java.lang.String)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  boolean mc_account_update(final MantisSoapConnector pConnector, final BigInteger pUserId,
      final AccountData pAccount, final String pPassword) throws MantisSoapException;

  /**
   * @return @
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_enum_access_levels
   *      (java.lang.String, java.lang.String)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  ObjectRef[] mc_enum_access_levels(final MantisSoapConnector pConnector) throws MantisSoapException;

  /**
   * @param pProjectData
   * @return project id
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_add
   *      (java.lang.String, java.lang.String,
   *      org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  BigInteger mc_project_add(final MantisSoapConnector pConnector, final ProjectData pProjectData)
      throws MantisSoapException;

  /**
   * @param pProjectId
   * @param pUserId
   * @param pAccessId
   * @return @
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_add_user
   *      (java.lang.String, java.lang.String, java.math.BigInteger, java.math.BigInteger,
   *      java.math.BigInteger)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  BigInteger mc_project_add_user(final MantisSoapConnector pConnector, final BigInteger pProjectId,
      final BigInteger pUserId, final BigInteger pAccessId) throws MantisSoapException;

  /**
   * @param pProjectId
   * @return @
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_delete
   *      (java.lang.String, java.lang.String, java.math.BigInteger)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  boolean mc_project_delete(final MantisSoapConnector pConnector, final BigInteger pProjectId)
      throws MantisSoapException;

  /**
   * @param pProjectName
   * @return @
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#
   *      mc_project_get_id_from_name(java.lang.String, java.lang.String, java.lang.String)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  BigInteger mc_project_get_id_from_name(final MantisSoapConnector pConnector, final String pProjectName)
      throws MantisSoapException;

  /**
   * @param pProjectId
   * @param pAccesId
   * @return @
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_get_users
   *      (java.lang.String, java.lang.String, java.math.BigInteger, java.math.BigInteger)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  AccountData[] mc_project_get_users(final MantisSoapConnector pConnector, final BigInteger pProjectId,
      final BigInteger pAccesId) throws MantisSoapException;

  /**
   * @param pProjectId
   * @param pUserId
   * @return
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_remove_user
   *      (java.lang.String, java.lang.String, java.math.BigInteger, java.math.BigInteger)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  boolean mc_project_remove_user(final MantisSoapConnector pConnector, final BigInteger pProjectId,
      final BigInteger pUserId) throws MantisSoapException;

  /**
   * @param pProjectId
   * @param pProjectData
   * @return
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_update
   *      (java.lang.String, java.lang.String, java.math.BigInteger,
   *      org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData)
   * @throws MantisSoapException
   *           can occured if mantis action failed or client can be built
   */
  boolean mc_project_update(final MantisSoapConnector pConnector, final BigInteger pProjectId,
      final ProjectData pProjectData) throws MantisSoapException;

  /**
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_issue_add(java.lang.String
   *      username, java.lang.String password, org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData
   *      issue)
   * @param pIssue
   * @return id of issue created
   * @throws MantisSoapException
   */
  BigInteger mc_issue_add(final MantisSoapConnector pConnector, final IssueData pIssue)
      throws MantisSoapException;

  /**
   * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectVersionData[]
   *      mc_project_get_versions(java.lang.String username, java.lang.String password, java.math.BigInteger
   *      project_id)
   * @param pProjectId
   * @return array of ProjectVersionData
   * @throws MantisSoapException
   */
  ProjectVersionData[] mc_project_get_versions(MantisSoapConnector pConnector, BigInteger pProjectId)
      throws MantisSoapException;

  /**
   * @see public org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData[]
   *      mc_project_get_issues(java.lang.String username, java.lang.String password, java.math.BigInteger
   *      project_id, java.math.BigInteger page_number, java.math.BigInteger per_page)
   * @param pConnector
   *          the connector
   * @param pProjectId
   *          the project id
   * @param pPageNumber
   *          the page number
   * @param pPerPage
   *          the number of issues per page
   * @return
   * @throws MantisSoapException
   */
  IssueData[] mc_project_get_issues(MantisSoapConnector pConnector, BigInteger pProjectId,
      BigInteger pPageNumber, BigInteger pPerPage) throws MantisSoapException;

  /**
   * @see public org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData mc_issue_get(java.lang.String
   *      username, java.lang.String password, java.math.BigInteger bug_id)
   * @param pConnector
   * @param pBugTrackerId
   * @return
   * @throws MantisSoapException
   */
  IssueData mc_issue_get(final MantisSoapConnector pConnector, final BigInteger pBugTrackerId)
      throws MantisSoapException;

  /**
   * @see public org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectStatusData
   *      mc_project_get_statuses(java.lang.String
   *      username, java.lang.String password, java.math.BigInteger pProjectId, java.lang.String pLocale)
   * @param pConnector
   * @param BigInteger
   * @return
   * @throws MantisSoapException
   */
  ProjectStatusData[] mc_project_get_statuses(final MantisSoapConnector pConnector,
      final BigInteger pProjectId, final String pLocale) throws MantisSoapException;

  /**
   * @see public org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHistoryStatusData
   *      mc_project_get_issues_history_by_status(MantisSoapConnector pConnector,
   *      BigInteger pProjectId, Calendar pStart, Calendar pEnd, BigInteger pIncrement)
   * @param pConnector
   * @param pProjectId
   * @param pStart
   * @param pEnd
   * @param pIncrement
   * @param pFilter
   * @return
   * @throws MantisSoapException
   */
  IssueHistoryStatusData[] mc_project_get_issues_history_by_status(final MantisSoapConnector pConnector,
      final BigInteger pProjectId, final Calendar pStart, final Calendar pEnd, final BigInteger pIncrement,
      FilterData[] pFilter) throws MantisSoapException;

  /**
   * mc_issue_attachment_uploader_add(MantisSoapConnector pConnector, BigInteger pIssueId,
   * AccountData pUploader, String pAttachmentName, String pAttachmentType, byte[] pAttachmentContent)
   * 
   * @param pConnector
   * @param pIssueId
   * @param pUploader
   * @param pAttachmentName
   * @param pAttachmentType
   * @param pAttachmentContent
   * @return
   * @throws MantisSoapException
   */
  BigInteger mc_issue_attachment_uploader_add(MantisSoapConnector pConnector, BigInteger pIssueId,
      AccountData pUploader, String pAttachmentName, String pAttachmentType, byte[] pAttachmentContent)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @return ProjectData[] Array of ProjectData Objects
   * @throws MantisSoapException
   */
  ProjectData[] mc_projects_get_all_projects(MantisSoapConnector pConnector) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pProjectName
   *          the project name
   * @return ProjectData the project found
   * @throws MantisSoapException
   */
  ProjectData mc_projects_get_project_by_name(MantisSoapConnector pConnector, String pProjectName)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pProjectId
   * @return CustomFieldDefinitionData[] array
   * @throws MantisSoapException
   */
  CustomFieldDefinitionData[] mc_project_get_custom_fields(MantisSoapConnector pConnector,
      BigInteger pProjectId) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pProjectId
   * @return
   * @throws MantisSoapException
   */
  String[] mc_project_get_categories(MantisSoapConnector pConnector, BigInteger pProjectId)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pProjectId
   * @return
   * @throws MantisSoapException
   */
  ConfigData[] mc_config_get_all_project(MantisSoapConnector pConnector, BigInteger pProjectId)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pProjectId
   * @return
   * @throws MantisSoapException
   */
  IssueHeaderData[] mc_project_get_all_issue_headers(MantisSoapConnector pConnector,
      final BigInteger pProjectId) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueId
   * @return
   * @throws MantisSoapException
   */
  HistoryData[] mc_issue_get_history(MantisSoapConnector pConnector, BigInteger pIssueId)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pProjectId
   * @param pCategoryName
   * @return
   * @throws MantisSoapException
   */
  BigInteger mc_project_add_category(MantisSoapConnector pConnector, BigInteger pProjectId,
      String pCategoryName) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pProjectVersionData
   * @return
   * @throws MantisSoapException
   */
  BigInteger mc_project_version_add(MantisSoapConnector pConnector, ProjectVersionData pProjectVersionData)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pCustomFieldDefinitionData
   * @param pProjectId
   * @return
   * @throws MantisSoapException
   */
  boolean mc_project_add_custom_field(MantisSoapConnector pConnector,
      CustomFieldDefinitionData pCustomFieldDefinitionData, BigInteger pProjectId) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pConfigData
   * @param pProjectId
   * @return
   * @throws MantisSoapException
   */
  boolean mc_config_set_project(MantisSoapConnector pConnector, ConfigData pConfigData, BigInteger pProjectId)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueId
   * @param pRelationshipData
   * @return
   * @throws MantisSoapException
   */
  BigInteger mc_issue_relationship_add(MantisSoapConnector pConnector, BigInteger pIssueId,
      RelationshipData pRelationshipData) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueId
   * @param pIssueNoteData
   * @return
   * @throws MantisSoapException
   */
  BigInteger mc_issue_note_add(MantisSoapConnector pConnector, BigInteger pIssueId,
      IssueNoteData pIssueNoteData) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueId
   * @param pHistoryData
   * @return
   * @throws MantisSoapException
   */
  boolean mc_issue_add_history(MantisSoapConnector pConnector, BigInteger pIssueId, HistoryData pHistoryData)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueNoteData
   * @return
   * @throws MantisSoapException
   */
  boolean mc_issue_note_update_reporter(MantisSoapConnector pConnector, IssueNoteData pIssueNoteData)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pCustomFieldDefinitionData
   * @return
   * @throws MantisSoapException
   */
  BigInteger mc_add_custom_field(MantisSoapConnector pConnector,
      CustomFieldDefinitionData pCustomFieldDefinitionData) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pProjectId
   * @param pCustomFieldId
   * @return
   * @throws MantisSoapException
   */
  boolean mc_link_custom_field_to_project(MantisSoapConnector pConnector, BigInteger pProjectId,
      BigInteger pCustomFieldId) throws MantisSoapException;

  /**
   * @param pConnector
   * @return
   * @throws MantisSoapException
   */
  ObjectRef[] mc_enum_custom_field_types(MantisSoapConnector pConnector) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueId
   * @param pLastUpdated
   * @return
   * @throws MantisSoapException
   */
  boolean mc_issue_update_last_updated(MantisSoapConnector pConnector, BigInteger pIssueId,
      Calendar pLastUpdated) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueId
   * @param pDateSubmitted
   * @return
   * @throws MantisSoapException
   */
  boolean mc_issue_update_date_submitted(MantisSoapConnector pConnector, BigInteger pIssueId,
      Calendar pDateSubmitted) throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueId
   * @return
   * @throws MantisSoapException
   */
  boolean mc_issue_delete_last_history(MantisSoapConnector pConnector, BigInteger pIssueId)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueNoteData
   * @return
   * @throws MantisSoapException
   */
  boolean mc_issue_note_update_dates(MantisSoapConnector pConnector, IssueNoteData pIssueNoteData)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pIssueId
   * @param pDescription
   * @return
   * @throws MantisSoapException
   */
  boolean mc_issue_update_description(MantisSoapConnector pConnector, BigInteger pIssueId, String pDescription)
      throws MantisSoapException;

  /**
   * @param pConnector
   * @param pNoteIssueId
   * @param pText
   * @return
   * @throws MantisSoapException
   */
  boolean mc_issue_note_update_text(MantisSoapConnector pConnector, BigInteger pNoteIssueId, String pText)
      throws MantisSoapException;

}