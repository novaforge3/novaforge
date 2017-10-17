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
package org.novaforge.forge.plugins.surveytool.limesurvey.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClientExt;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyGroupUser;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyUser;

/**
 * @author lamirang & goarzino
 */
public interface LimesurveyXmlRpcClient
{

  /**
   * Get connector to limesurvey instance with specific url, username and password.
   * 
   * @param pBaseUrl
   *          represents the url of nexus instance
   * @param pUsername
   *          represents username used to log in
   * @param pPassword
   *          represents password used to log in
   * @return a {@link XmlRpcClient } built from the parameter given
   * @throws LimesurveyClientException
   *           can occured if limesurvey action failed or client can be built can occured if connection
   *           failed or client can be built
   */
  LimesurveyXmlRpcClientExt getConnector(String pBaseUrl, String pUsername, String pPassword)
      throws LimesurveyClientException;
  
  /**
   * @return true if session released
   * @throws LimesurveyClientException
   */
  boolean releaseSession(LimesurveyXmlRpcClientExt connector)
      throws LimesurveyClientException;
 
  /**
   * @param pSurveyUsers
   * @return true if user exist
   * @throws LimesurveyClientException
   */
  Integer isUserExist(final LimesurveyXmlRpcClientExt connector, final LimesurveyUser pSurveyUsers)
      throws LimesurveyClientException;

  /**
   * @param pSurveyUsers
   * @return user id
   * @throws LimesurveyClientException
   */
  Integer createUser(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers) throws LimesurveyClientException;

  /**
   * @param pSurveyUsers
   * @return user id
   * @throws LimesurveyClientException
   */
  Integer createSuperAdmin(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers) throws LimesurveyClientException;

  /**
   * @param pSurveyUsers
   * @throws LimesurveyClientException
   */
  boolean updateUser(final LimesurveyXmlRpcClientExt connector, final LimesurveyUser pSurveyUsers)
      throws LimesurveyClientException;

  /**
   * @param pSurveyUsers
   * @param pCreateRight
   * @throws LimesurveyClientException
   */
  boolean setCreateSurveyPermission(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers, boolean pCreateRight) throws LimesurveyClientException;

  /**
   * @param pSurveyUsers
   * @throws LimesurveyClientException
   */
  boolean deleteUser(final LimesurveyXmlRpcClientExt connector, final LimesurveyUser pSurveyUsers)
      throws LimesurveyClientException;

  /**
   * @param pSurveyGroupUsers
   * @return group ID (-1 -> group does not exist)
   * @throws LimesurveyClientException
   */
  Integer isGroupUserExist(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyGroupUser pSurveyGroupUsers) throws LimesurveyClientException;

  /**
   * @param pGroupId
   * @return group ID (-1 -> group does not exist)
   * @throws LimesurveyClientException
   */
  Integer isGroupUserExistById(final LimesurveyXmlRpcClientExt connector,
      final Integer pGroupId) throws LimesurveyClientException;

  /**
   * @param pSurveyGroupUsers
   * @return user group id
   * @throws LimesurveyClientException
   */
  Integer createGroupUser(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyGroupUser pSurveyGroupUsers) throws LimesurveyClientException;

  /**
   * @param pSurveyGroupUsers
   * @param groupId
   * @throws LimesurveyClientException
   */
  boolean updateGroupUser(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyGroupUser pSurveyGroupUsers, final Integer pGroupId)
      throws LimesurveyClientException;

  /**
   * @param groupId
   * @throws LimesurveyClientException
   */
  boolean deleteGroupUser(final LimesurveyXmlRpcClientExt connector, final Integer pGroupId)
      throws LimesurveyClientException;

  /**
   * @param pSurveyUsers
   * @param pSurveyGroupUsers
   * @throws LimesurveyClientException
   */
  boolean addUserInGroup(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers, final LimesurveyGroupUser pSurveyGroupUsers)
      throws LimesurveyClientException;

  /**
   * @param pSurveyUsers
   * @param pGroupId
   * @throws LimesurveyClientException
   */
  boolean addUserInGroupById(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers, final Integer pGroupId) throws LimesurveyClientException;

  /**
   * @param pSurveyUsers
   * @param pGroupId
   * @throws LimesurveyClientException
   */
  boolean removeUserFromGroup(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers, final String sGroupId) throws LimesurveyClientException;

}
