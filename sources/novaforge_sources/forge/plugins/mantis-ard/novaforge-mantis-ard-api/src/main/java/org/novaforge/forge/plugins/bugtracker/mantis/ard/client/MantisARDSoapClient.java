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
package org.novaforge.forge.plugins.bugtracker.mantis.ard.client;

import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.AccountData;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.ObjectRef;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.ProjectData;

import java.math.BigInteger;

/**
 * @author lamirang
 * @author BILET-JC
 */
public interface MantisARDSoapClient
{

   /**
    * @return Use to connect to mantis instance with specific url, username and password.
    * @param baseUrl
    *           represents the url of nexus instance
    * @param username
    *           represents username used to log in
    * @param password
    *           represents password used to log in
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built can occured if connection failed
    *            or client can be built
    * @throws
    */
   MantisARDSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
   throws MantisARDSoapException;

   /**
    * @param pArg2
    * @param pArg3
    * @return @
    * @see org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.MantisConnectBindingStub#mc_account_add
    *      (java.lang.String, java.lang.String,
    *      org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData, java.lang.String)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   BigInteger mc_account_add(final MantisARDSoapConnector pConnector, final AccountData pAccount,
         final String pPassword) throws MantisARDSoapException;

/**
 * 
 * @param pConnector
 * @param pUserId
 * @return
 * @throws MantisARDSoapException
 */
   boolean mc_account_delete(final MantisARDSoapConnector pConnector, final BigInteger pUserId)
         throws MantisARDSoapException;

   /**
    * @param pUserId
    * @return user id
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_account_get
    *      (java.lang.String, java.lang.String, java.lang.String)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   BigInteger mc_account_get(final MantisARDSoapConnector pConnector, final String pUserId)
         throws MantisARDSoapException;

   /**
    * @param pUserId
    * @param pAccount
    * @param pPassword
    * @return boolean
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_account_update
    *      (java.lang.String, java.lang.String, java.math.BigInteger,
    *      org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData, java.lang.String)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   boolean mc_account_update(final MantisARDSoapConnector pConnector, final BigInteger pUserId,
         final AccountData pAccount, final String pPassword)
   throws MantisARDSoapException;

   /**
    * @return @
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_enum_access_levels
    *      (java.lang.String, java.lang.String)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   ObjectRef[] mc_enum_access_levels(final MantisARDSoapConnector pConnector) throws MantisARDSoapException;

    /**
    * 
    * @param pConnector
    * @return boolean
    * @throws MantisARDSoapException
    */
   boolean mc_configure_management(final MantisARDSoapConnector pConnector, final String pProjectName) throws MantisARDSoapException;

   /**
    * @param pProjectId
    * @param pUserId
    * @param pAccessId
    * @return @
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_add_user
    *      (java.lang.String, java.lang.String, java.math.BigInteger, java.math.BigInteger,
    *      java.math.BigInteger)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   BigInteger mc_management_add_user(final MantisARDSoapConnector pConnector, final BigInteger pProjectId,
         final BigInteger pUserId,
         final BigInteger pAccessId) throws MantisARDSoapException;

   /**
    * @param pProjectId
    * @return boolean
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_management_delete
    *      (java.lang.String, java.lang.String, java.math.BigInteger)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   boolean mc_management_delete(final MantisARDSoapConnector pConnector, final BigInteger pProjectId)
         throws MantisARDSoapException;

   /**
    * @param pProjectName
    * @return @
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#
    *      mc_project_get_id_from_name(java.lang.String, java.lang.String, java.lang.String)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   BigInteger mc_project_get_id_from_name(final MantisARDSoapConnector pConnector, final String pProjectName)
         throws MantisARDSoapException;

   /**
    * @param pProjectId
    * @param pAccesId
    * @return @
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_get_users
    *      (java.lang.String, java.lang.String, java.math.BigInteger, java.math.BigInteger)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   AccountData[] mc_project_get_users(final MantisARDSoapConnector pConnector, final BigInteger pProjectId,
         final BigInteger pAccesId)
   throws MantisARDSoapException;

   /**
    * @param pProjectId
    * @param pUserId
    * @return
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_project_remove_user
    *      (java.lang.String, java.lang.String, java.math.BigInteger, java.math.BigInteger)
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   boolean mc_management_remove_user(final MantisARDSoapConnector pConnector, final BigInteger pProjectId,
         final BigInteger pUserId)
   throws MantisARDSoapException;


   /**
    * @param pConnector
    * @param pProjectId
    * @param pProjectData
    * @return boolean
    * @throws MantisARDSoapException
    *            can occured if mantis action failed or client can be built
    */
   boolean mc_management_update(final MantisARDSoapConnector pConnector, final BigInteger pProjectId,
         final ProjectData pProjectData)
   throws MantisARDSoapException;

   /**
    * @see org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectBindingStub#mc_issue_add(java.lang.String
    *      username, java.lang.String password, org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData
    *      issue)
    * @param pIssue
    * @return id of issue created
    * @throws MantisARDSoapException
    */
//   BigInteger mc_issue_add(final MantisARDSoapConnector pConnector, final IssueData pIssue)
//         throws MantisARDSoapException;
   
}