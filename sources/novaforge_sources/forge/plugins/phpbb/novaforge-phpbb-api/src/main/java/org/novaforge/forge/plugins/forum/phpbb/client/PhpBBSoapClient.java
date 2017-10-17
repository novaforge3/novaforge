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
package org.novaforge.forge.plugins.forum.phpbb.client;

import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbForum;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbUser;
import org.novaforge.forge.plugins.forum.phpbb.soap.ObjectRef;

import java.math.BigInteger;

/**
 * @author caseryj
 *
 */
public interface PhpBBSoapClient
{

	/**
	    * @return Use to connect to mantis instance with specific url, username and password.
	    * @param baseUrl
	    *           represents the url of nexus instance
	    * @param username
	    *           represents username used to log in
	    * @param password
	    *           represents password used to log in
	    * @throws MantisSoapException
	    *            can occured if mantis action failed or client can be built can occured if connection failed
	    *            or client can be built
	    * @throws
	    */
	   PhpBBSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
	   throws PhpBBSoapException ;
	

   /**
    * @return @
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_enum_access_levels
    *      (java.lang.String, java.lang.String)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   boolean pc_purge_cache(final PhpBBSoapConnector pConnector) throws PhpBBSoapException;

   /**
    * @param pArg2
    * @param pArg3
    * @return @
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_account_add
    *      (java.lang.String, java.lang.String, org.novaforge.forge.plugins.forum.phpbb.soap.AccountData,
    *      java.lang.String)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   BigInteger pc_account_add(final PhpBBSoapConnector pConnector,PhpbbUser pUser) throws PhpBBSoapException;

   /**
    * @param pArg2
    * @param pArg3
    * @return @
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_account_add
    *      (java.lang.String, java.lang.String, org.novaforge.forge.plugins.forum.phpbb.soap.AccountData,
    *      java.lang.String)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   BigInteger pc_create_superadmin(final PhpBBSoapConnector pConnector,PhpbbUser pUser) throws PhpBBSoapException;

   /**
    * @param pArg2
    * @return @
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_account_delete
    *      (java.lang.String, java.lang.String, java.math.BigInteger)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   boolean pc_account_delete(final PhpBBSoapConnector pConnector,PhpbbUser pUser) throws PhpBBSoapException;

   /**
    * @param pUserId
    * @return user id
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_account_get
    *      (java.lang.String, java.lang.String, java.lang.String)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   BigInteger pc_account_get(final PhpBBSoapConnector pConnector,PhpbbUser pUser) throws PhpBBSoapException;

   /**
    * @param pUserId
    * @param pAccount
    * @param pPassword
    * @return boolean
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_account_update
    *      (java.lang.String, java.lang.String, java.math.BigInteger,
    *      org.novaforge.forge.plugins.forum.phpbb.soap.AccountData, java.lang.String)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   boolean pc_account_update(final PhpBBSoapConnector pConnector,PhpbbUser pUser)
   throws PhpBBSoapException;

   /**
    * @return @
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_enum_access_levels
    *      (java.lang.String, java.lang.String)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   ObjectRef[] pc_enum_access_levels(final PhpBBSoapConnector pConnector) throws PhpBBSoapException;

   /**
    * @param pProjectData
    * @return project id
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_project_add
    *      (java.lang.String, java.lang.String, org.novaforge.forge.plugins.forum.phpbb.soap.ProjectData)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   BigInteger pc_project_add(final PhpBBSoapConnector pConnector,PhpbbForum pProjectData) throws PhpBBSoapException;

   /**
    * @param pProjectId
    * @param pUser
    * @param pAccessName
    * @return @
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_project_add_user
    *      (java.lang.String, java.lang.String, java.math.BigInteger, java.math.BigInteger,
    *      java.math.BigInteger)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   boolean pc_project_add_user(final PhpBBSoapConnector pConnector,BigInteger pProjectId, PhpbbUser pUser, String pAccessName)
   throws PhpBBSoapException;

   /**
    * @param pProjectId
    * @return @
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_project_delete
    *      (java.lang.String, java.lang.String, java.math.BigInteger)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   boolean pc_project_delete(final PhpBBSoapConnector pConnector,BigInteger pProjectId) throws PhpBBSoapException;

   /**
    * @param pProjectId
    * @param pUser
    * @return @
    * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_project_remove_user
    *      (java.lang.String, java.lang.String, java.math.BigInteger, java.math.BigInteger)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   boolean pc_project_remove_user(final PhpBBSoapConnector pConnector,BigInteger pProjectId, PhpbbUser pUser) throws PhpBBSoapException;

   /**
    * @param pProjectId
    * @param pProjectData
    * @return @ * @see org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub#pc_project_update
    *         (java.lang.String, java.lang.String, java.math.BigInteger,
    *         org.novaforge.forge.plugins.forum.phpbb.soap.ProjectData)
    * @throws PhpBBSoapException
    *            can occured if phpbb action failed or client can be built
    */
   boolean pc_project_update(final PhpBBSoapConnector pConnector,BigInteger pProjectId, PhpbbForum pProjectData) throws PhpBBSoapException;

}