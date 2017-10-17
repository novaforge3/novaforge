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
package org.novaforge.forge.plugins.bucktracker.mantis.internal.client;

import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapClient;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapConnector;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapException;
import org.novaforge.forge.plugins.bucktracker.mantis.internal.MantisConstant;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ConfigData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.CustomFieldDefinitionData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.FilterData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.HistoryData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHeaderData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHistoryStatusData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueNoteData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectLocator;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ObjectRef;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectStatusData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectVersionData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.RelationshipData;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * This class is used in order to instantiate new connector to mantis Web-service.
 *
 * @author lamirang
 */
public class MantisSoapClientImpl implements MantisSoapClient
{
  /**
   * {@inheritDoc}
   */
  @Override
  public MantisSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
      throws MantisSoapException
  {
    MantisSoapConnector connector = null;
    URL url = null;
    try
    {
      url = new URL(pBaseUrl);
      final MantisConnectBindingStub connectBindingStub = (MantisConnectBindingStub) new MantisConnectLocator()
                                                                                         .getMantisConnectPort(url);
      connectBindingStub.setTimeout(MantisConstant.SOAP_TIMEOUT);
      connector = new MantisSoapConnectorImpl(pUsername, pPassword, connectBindingStub);
    }
    catch (final MalformedURLException e)
    {
      throw new MantisSoapException(String.format("Unable to build URL object with [value=%s]", pBaseUrl), e);
    }
    catch (final ServiceException e)
    {
      throw new MantisSoapException(String.format("Unable to get the Mantis Connector Binding with [URL=%s]",
                                                  url.toString()), e);
    }
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_account_add(final MantisSoapConnector pConnector, final AccountData pAccount,
                                   final String pPassword) throws MantisSoapException
  {
    BigInteger returnId = null;
    try
    {
      returnId = pConnector.getConnectBindingStub().mc_account_add(pConnector.getUsername(), pConnector.getPassword(),
                                                                   pAccount, pPassword);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to add user account with [login=%s, email=%s, real_name=%s]",
                                                  pAccount.getName(), pAccount.getEmail(), pAccount.getReal_name()), e);
    }
    return returnId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_account_delete(final MantisSoapConnector pConnector, final BigInteger pUserId)
      throws MantisSoapException
  {
    boolean returnSucess = false;
    try
    {
      returnSucess = pConnector.getConnectBindingStub().mc_account_delete(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pUserId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to delete user account with [id=%s]", pUserId), e);
    }
    return returnSucess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_account_get(final MantisSoapConnector pConnector, final String pUserName)
      throws MantisSoapException
  {
    BigInteger returnId = null;
    try
    {
      returnId = pConnector.getConnectBindingStub().mc_account_get(pConnector.getUsername(), pConnector.getPassword(),
                                                                   pUserName);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get user account with [id=%s]", pUserName), e);
    }
    return returnId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_account_update(final MantisSoapConnector pConnector, final BigInteger pUserId,
                                   final AccountData pAccount, final String pPassword) throws MantisSoapException
  {
    boolean returnSucces = false;
    try
    {
      returnSucces = pConnector.getConnectBindingStub().mc_account_update(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pUserId, pAccount,
                                                                          pPassword);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to add user account with [id=%s, login=%s, email=%s, real_name=%s]",
                                                pUserId, pAccount.getName(), pAccount.getEmail(),
                                                pAccount.getReal_name()), e);
    }
    return returnSucces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ObjectRef[] mc_enum_access_levels(final MantisSoapConnector pConnector) throws MantisSoapException
  {
    ObjectRef[] returnAcces = null;
    try
    {
      returnAcces = pConnector.getConnectBindingStub().mc_enum_access_levels(pConnector.getUsername(),
                                                                             pConnector.getPassword());
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException("Unable to get the list of acces level available.", e);
    }
    return returnAcces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_project_add(final MantisSoapConnector pConnector, final ProjectData pProjectData)
      throws MantisSoapException
  {
    BigInteger returnId = null;
    try
    {
      returnId = pConnector.getConnectBindingStub().mc_project_add(pConnector.getUsername(), pConnector.getPassword(),
                                                                   pProjectData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to add project with [name=%s, data=%s]",
                                                  pProjectData.getName(), pProjectData.toString()), e);
    }
    return returnId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_project_add_user(final MantisSoapConnector pConnector, final BigInteger pProjectId,
                                        final BigInteger pUserId, final BigInteger pAccessId) throws MantisSoapException
  {
    BigInteger returnAccess = null;
    try
    {
      returnAccess = pConnector.getConnectBindingStub().mc_project_add_user(pConnector.getUsername(),
                                                                            pConnector.getPassword(), pProjectId,
                                                                            pUserId, pAccessId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to add user to project with [project_id=%s, user_id=%s, acces_id=%s]",
                                                pProjectId, pUserId, pAccessId), e);
    }
    return returnAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_project_delete(final MantisSoapConnector pConnector, final BigInteger pProjectId)
      throws MantisSoapException
  {
    boolean returnSuccess = false;
    try
    {
      returnSuccess = pConnector.getConnectBindingStub().mc_project_delete(pConnector.getUsername(),
                                                                           pConnector.getPassword(), pProjectId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to delete project with [project_id=%s]", pProjectId), e);
    }
    return returnSuccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_project_get_id_from_name(final MantisSoapConnector pConnector, final String pProjectName)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_id_from_name(pConnector.getUsername(),
                                                                            pConnector.getPassword(), pProjectName);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project id from project name with [name=%s]",
                                                  pProjectName), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AccountData[] mc_project_get_users(final MantisSoapConnector pConnector, final BigInteger pProjectId,
                                            final BigInteger pAccesId) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_users(pConnector.getUsername(), pConnector.getPassword(),
                                                                     pProjectId, pAccesId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project's users  with [project_id=%s, acces_id=%s]",
                                                  pProjectId, pAccesId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_project_remove_user(final MantisSoapConnector pConnector, final BigInteger pProjectId,
                                        final BigInteger pUserId) throws MantisSoapException
  {
    boolean returnSucess = false;
    try
    {
      returnSucess = pConnector.getConnectBindingStub().mc_project_remove_user(pConnector.getUsername(),
                                                                               pConnector.getPassword(), pProjectId,
                                                                               pUserId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to delete user account with [project_id=%s, user_id=%s]",
                                                  pProjectId, pUserId), e);
    }
    return returnSucess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_project_update(final MantisSoapConnector pConnector, final BigInteger pProjectId,
                                   final ProjectData pProjectData) throws MantisSoapException
  {
    boolean returnSucess = false;
    try
    {
      returnSucess = pConnector.getConnectBindingStub().mc_project_update(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pProjectId,
                                                                          pProjectData);
    }
    catch (final RemoteException e)
    {

      throw new MantisSoapException(String.format("Unable to update project with [id=%s]", pProjectId), e);
    }
    return returnSucess;
  }

  /**
   * {@inheritDoc}
   *
   * @throws MantisSoapException
   */
  @Override
  public BigInteger mc_issue_add(final MantisSoapConnector pConnector, final IssueData pIssue)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_add(pConnector.getUsername(), pConnector.getPassword(),
                                                             pIssue);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to create issue with [issue_summary=%s,description=%s,issue_additional_info=%s]",
                                                pIssue.getSummary(), pIssue.getDescription(),
                                                pIssue.getAdditional_information()), e);
    }

  }

  @Override
  public ProjectVersionData[] mc_project_get_versions(final MantisSoapConnector pConnector, final BigInteger pProjectId)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_versions(pConnector.getUsername(),
                                                                        pConnector.getPassword(), pProjectId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project versions with [project_id=%s]", pProjectId),
                                    e);
    }
  }

  @Override
  public IssueData[] mc_project_get_issues(final MantisSoapConnector pConnector, final BigInteger pProjectId,
                                           final BigInteger pPageNumber, final BigInteger pPerPage)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_issues(pConnector.getUsername(),
                                                                      pConnector.getPassword(), pProjectId, pPageNumber,
                                                                      pPerPage);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project issues with [project_id=%s]", pProjectId), e);
    }
  }

  @Override
  public IssueData mc_issue_get(final MantisSoapConnector pConnector, final BigInteger pBugTrackerId)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_get(pConnector.getUsername(), pConnector.getPassword(),
                                                             pBugTrackerId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get issue with [id=%s]", pBugTrackerId), e);
    }
  }

  @Override
  public ProjectStatusData[] mc_project_get_statuses(final MantisSoapConnector pConnector, final BigInteger pProjectId,
                                                     final String pLocale) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_statuses(pConnector.getUsername(),
                                                                        pConnector.getPassword(), pProjectId, pLocale);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project status with [project id=%s]", pProjectId), e);
    }
  }

  @Override
  public IssueHistoryStatusData[] mc_project_get_issues_history_by_status(final MantisSoapConnector pConnector,
                                                                          final BigInteger pProjectId,
                                                                          final Calendar pStart, final Calendar pEnd,
                                                                          final BigInteger pIncrement,
                                                                          final FilterData[] pFilter)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_issues_history_by_status(pConnector.getUsername(),
                                                                                        pConnector.getPassword(),
                                                                                        pProjectId, pStart, pEnd,
                                                                                        pIncrement, pFilter);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to get project issues history with [project_id=%s, start=%s, end=%s, increment=%s]",
                                                pProjectId, pStart, pEnd, pIncrement), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_issue_attachment_uploader_add(final MantisSoapConnector pConnector, final BigInteger pIssueId,
                                                     final AccountData pUploader, final String pAttachmentName,
                                                     final String pAttachmentType, final byte[] pAttachmentContent)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_attachment_uploader_add(pConnector.getUsername(),
                                                                                 pConnector.getPassword(), pIssueId,
                                                                                 pUploader, pAttachmentName,
                                                                                 pAttachmentType, pAttachmentContent);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to add attachment to issue with [issue_id=%s,attachment_name=%s,attachment_type=%s]",
                                                pIssueId, pAttachmentName, pAttachmentType), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectData[] mc_projects_get_all_projects(final MantisSoapConnector pConnector) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_projects_get_user_accessible(pConnector.getUsername(),
                                                                                pConnector.getPassword());
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException("Unable to get all mantis projects", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectData mc_projects_get_project_by_name(final MantisSoapConnector pConnector, final String pProjectName)
      throws MantisSoapException
  {
    try
    {
      ProjectData projectReturned = null;

      if (pProjectName != null && pProjectName.trim().length() > 0)
      {
        final ProjectData[] projects = pConnector.getConnectBindingStub().mc_projects_get_user_accessible(pConnector
                                                                                                              .getUsername(),
                                                                                                          pConnector
                                                                                                              .getPassword());
        for (final ProjectData projectData : projects)
        {
          if (pProjectName.equals(projectData.getName()))
          {
            projectReturned = projectData;
            break;
          }
        }
      }
      return projectReturned;
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project with [project name=%s]", pProjectName), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CustomFieldDefinitionData[] mc_project_get_custom_fields(final MantisSoapConnector pConnector,
                                                                  final BigInteger pProjectId)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_custom_fields(pConnector.getUsername(),
                                                                             pConnector.getPassword(), pProjectId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project custom fields with [project id=%s]",
                                                  pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] mc_project_get_categories(final MantisSoapConnector pConnector, final BigInteger pProjectId)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_categories(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pProjectId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project categories with [project id=%s]", pProjectId),
                                    e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConfigData[] mc_config_get_all_project(final MantisSoapConnector pConnector, final BigInteger pProjectId)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_config_get_all_project(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pProjectId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get project configs with [project id=%s]", pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IssueHeaderData[] mc_project_get_all_issue_headers(final MantisSoapConnector pConnector,
                                                            final BigInteger pProjectId) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_get_issue_headers(pConnector.getUsername(),
                                                                             pConnector.getPassword(), pProjectId,
                                                                             BigInteger.valueOf(1),
                                                                             BigInteger.valueOf(-1));
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get all project issues with [project id=%s]", pProjectId),
                                    e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HistoryData[] mc_issue_get_history(final MantisSoapConnector pConnector, final BigInteger pIssueId)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_get_history(pConnector.getUsername(), pConnector.getPassword(),
                                                                     pIssueId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to get all issue histories with [issue id=%s]", pIssueId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_project_add_category(final MantisSoapConnector pConnector, final BigInteger pProjectId,
                                            final String pCategoryName) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_add_category(pConnector.getUsername(),
                                                                        pConnector.getPassword(), pProjectId,
                                                                        pCategoryName);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to add project category with [category name=%s, project id=%s]",
                                                pCategoryName, pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_project_version_add(final MantisSoapConnector pConnector,
                                           final ProjectVersionData pProjectVersionData) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_project_version_add(pConnector.getUsername(),
                                                                       pConnector.getPassword(), pProjectVersionData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to add project version with [version name=%s, project id=%s]",
                                                  pProjectVersionData.getName(), pProjectVersionData.getProject_id()),
                                    e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_project_add_custom_field(final MantisSoapConnector pConnector,
                                             final CustomFieldDefinitionData pCustomFieldDefinitionData,
                                             final BigInteger pProjectId) throws MantisSoapException
  {
    try
    {
      final BigInteger customFieldId = pConnector.getConnectBindingStub().mc_add_custom_field(pConnector.getUsername(),
                                                                                              pConnector.getPassword(),
                                                                                              pCustomFieldDefinitionData);

      return pConnector.getConnectBindingStub().mc_link_custom_field_to_project(pConnector.getUsername(),
                                                                                pConnector.getPassword(), pProjectId,
                                                                                customFieldId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to add project custom field with [field name=%s, project id=%s]",
                                                pCustomFieldDefinitionData.getField().getName(), pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_config_set_project(final MantisSoapConnector pConnector, final ConfigData pConfigData,
                                       final BigInteger pProjectId) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_config_set_project(pConnector.getUsername(),
                                                                      pConnector.getPassword(), pProjectId,
                                                                      pConfigData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to add project config with [config id=%s, project id=%s]",
                                                  pConfigData.getConfig_id(), pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_issue_relationship_add(final MantisSoapConnector pConnector, final BigInteger pIssueId,
                                              final RelationshipData pRelationshipData) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_relationship_add(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pIssueId,
                                                                          pRelationshipData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to add issue relationship with [relationship type id=%s, issue id=%s]",
                                                pRelationshipData.getType().getName(), pIssueId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_issue_note_add(final MantisSoapConnector pConnector, final BigInteger pIssueId,
                                      final IssueNoteData pIssueNoteData) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_note_add(pConnector.getUsername(), pConnector.getPassword(),
                                                                  pIssueId, pIssueNoteData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to add issue note with [issue note text=%s, issue id=%s]",
                                                  pIssueNoteData.getText(), pIssueId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_issue_add_history(final MantisSoapConnector pConnector, final BigInteger pIssueId,
                                      final HistoryData pHistoryData) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_add_history(pConnector.getUsername(), pConnector.getPassword(),
                                                                     pIssueId, pHistoryData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to add issue history with [issue history date=%s, issue id=%s]",
                                                pHistoryData.getDate().toString(), pIssueId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_issue_note_update_reporter(final MantisSoapConnector pConnector, final IssueNoteData pIssueNoteData)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_note_update_reporter(pConnector.getUsername(),
                                                                              pConnector.getPassword(), pIssueNoteData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to update issue note reporter with [issue history date=%s, issue id=%s]",
                                                pIssueNoteData.getText(), e));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger mc_add_custom_field(final MantisSoapConnector pConnector,
                                        final CustomFieldDefinitionData pCustomFieldDefinitionData)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_add_custom_field(pConnector.getUsername(), pConnector.getPassword(),
                                                                    pCustomFieldDefinitionData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to add custom field with [field name=%s]",
                                                  pCustomFieldDefinitionData.getField().getName()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_link_custom_field_to_project(final MantisSoapConnector pConnector, final BigInteger pProjectId,
                                                 final BigInteger pCustomFieldId) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_link_custom_field_to_project(pConnector.getUsername(),
                                                                                pConnector.getPassword(), pProjectId,
                                                                                pCustomFieldId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String
                                        .format("Unable to link custom field to project with [custom field id=%s, project id=%s]",
                                                pCustomFieldId, pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ObjectRef[] mc_enum_custom_field_types(final MantisSoapConnector pConnector) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_enum_custom_field_types(pConnector.getUsername(),
                                                                           pConnector.getPassword());
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException("Unable to get enum custom field types", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_issue_update_last_updated(final MantisSoapConnector pConnector, final BigInteger pIssueId,
                                              final Calendar pLastUpdated) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_update_last_updated(pConnector.getUsername(),
                                                                             pConnector.getPassword(), pIssueId,
                                                                             pLastUpdated);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to update last modified date [with issue id=%s]", pIssueId),
                                    e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_issue_update_date_submitted(final MantisSoapConnector pConnector, final BigInteger pIssueId,
                                                final Calendar pDateSubmitted) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_update_date_submitted(pConnector.getUsername(),
                                                                               pConnector.getPassword(), pIssueId,
                                                                               pDateSubmitted);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to update date submitted [with issue id=%s]", pIssueId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_issue_delete_last_history(final MantisSoapConnector pConnector, final BigInteger pIssueId)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_delete_last_history(pConnector.getUsername(),
                                                                             pConnector.getPassword(), pIssueId);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to delete last issue history for id=%s", pIssueId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_issue_note_update_dates(final MantisSoapConnector pConnector, final IssueNoteData pIssueNoteData)
      throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_note_update_dates(pConnector.getUsername(),
                                                                           pConnector.getPassword(), pIssueNoteData);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to update note issue date for id=%s", pIssueNoteData.getId()),
                                    e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_issue_update_description(final MantisSoapConnector pConnector, final BigInteger pIssueId,
                                             final String pDescription) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_update_description(pConnector.getUsername(),
                                                                            pConnector.getPassword(), pIssueId,
                                                                            pDescription);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to update description [with issue id=%s]", pIssueId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mc_issue_note_update_text(final MantisSoapConnector pConnector, final BigInteger pNoteIssueId,
                                           final String pText) throws MantisSoapException
  {
    try
    {
      return pConnector.getConnectBindingStub().mc_issue_note_update_text(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pNoteIssueId,
                                                                          pText);
    }
    catch (final RemoteException e)
    {
      throw new MantisSoapException(String.format("Unable to update note text [with note issue id=%s]", pNoteIssueId),
                                    e);
    }
  }
}
