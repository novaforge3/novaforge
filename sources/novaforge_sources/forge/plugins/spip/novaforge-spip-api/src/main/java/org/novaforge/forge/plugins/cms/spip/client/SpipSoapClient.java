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
package org.novaforge.forge.plugins.cms.spip.client;

import org.novaforge.forge.plugins.cms.spip.soap.SiteInput;
import org.novaforge.forge.plugins.cms.spip.soap.UserData;
import org.novaforge.forge.plugins.cms.spip.soap.UserInfo;

import java.math.BigInteger;

/**
 * @author blachonm
 */
public interface SpipSoapClient
{

   /**
    * @return Use to connect to spip instance with specific url, username and password.
    * @param baseUrl
    *           represents the url of spip instance
    * @param username
    *           represents username used to log in
    * @param password
    *           represents password used to log in
    * @throws SpipSoapException
    *            can occured if spip action failed or client can be built can occured if connection failed or
    *            client can be built
    * @throws
    */

   SpipSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
         throws SpipSoapException;

   /**
    * @param pConnector
    * @param userData
    * @return the user id
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#add_user(String, String, UserData)
    * @throws SpipSoapException
    */
   BigInteger add_user(final SpipSoapConnector pConnector, final UserData userData) throws SpipSoapException;

   /**
    * @param pConnector
    * @param login
    *           of the user
    * @return boolean
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#delete_user(String, String,
    *      String)
    * @throws SpipSoapException
    */
   boolean delete_user(final SpipSoapConnector pConnector, final String login) throws SpipSoapException;

   /**
    * @param pConnector
    * @param login
    *           of the user
    * @return user id
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#get_user_id(String, String,
    *      String)
    * @throws SpipSoapException
    */
   BigInteger get_user_id(final SpipSoapConnector pConnector, final String login) throws SpipSoapException;

   /**
    * @param pConnector
    * @param login
    *           of the user
    * @return the user_info data
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#get_user_info(String, String,
    *      String)
    * @throws SpipSoapException
    */
   UserInfo get_user_info(final SpipSoapConnector pConnector, final String login) throws SpipSoapException;

   /**
    * @param pConnector
    * @param pLogin
    * @param userData
    * @return boolean
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#update_user(String, String,
    *      String, UserData)String, String, String, UserData)
    * @throws SpipSoapException
    */
   boolean update_user(final SpipSoapConnector pConnector, final String pLogin, final UserData userData)
         throws SpipSoapException;

   /**
    * @param pConnector
    * @param pSiteInput
    * @return site id
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#create_site(String, String,
    *      SiteInput)
    * @throws SpipSoapException
    */
   String create_site(final SpipSoapConnector pConnector, final SiteInput pSiteInput)
         throws SpipSoapException;

   /**
    * @param pConnector
    * @param pSiteId
    * @param pLogin
    * @param pRole
    * @return user id
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#add_user_site(String, String,
    *      String, String, String)
    * @throws SpipSoapException
    */
   BigInteger add_user_site(final SpipSoapConnector pConnector, final String pSiteId, final String pLogin,
         final String pRole) throws SpipSoapException;

   /**
    * @param pConnector
    * @param pSiteId
    * @return boolean
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#delete_site(String, String,
    *      String)
    * @throws SpipSoapException
    */
   boolean delete_site(final SpipSoapConnector pConnector, final String pSiteId) throws SpipSoapException;


   /**
    * @param pConnector
    * @param pSiteInput
    * @return boolean
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#update_site(String, String,
    *      SiteInput)
    * @throws SpipSoapException
    */
   boolean update_site(final SpipSoapConnector pConnector, final SiteInput pSiteInput)
         throws SpipSoapException;

   /**
    * @param pConnector
    * @return Array of String roles
    * @see org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub#get_roles()
    * @throws SpipSoapException
    */
   String[] get_roles(final SpipSoapConnector pConnector) throws SpipSoapException;

}