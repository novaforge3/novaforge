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
/**
 * SpipConnectTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.novaforge.forge.plugins.cms.spip.internal.client;

import org.junit.Test;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapClient;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapConnector;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapException;
import org.novaforge.forge.plugins.cms.spip.soap.SiteInput;
import org.novaforge.forge.plugins.cms.spip.soap.SpipConnectLocator;
import org.novaforge.forge.plugins.cms.spip.soap.UserData;
import org.novaforge.forge.plugins.cms.spip.soap.UserInfo;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import java.math.BigInteger;
import java.net.URL;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * To trigger this test, activate a java system property "spip.profile".
 * 
 * @author blachonm
 */
public class SpipClientImplTest
{

   // Warning: apache configuration :
   // - enable rewite module
   // - AllowOverride All for spip root document.


   private static final String ENDPOINT                  = "http://localhost/spip/spip1/ecrire/nf_spipconnect.php";

   private static final String USERNAME                  = "administrator";
   private static final String PASSWORD                  = "root123";

   private static final String USERDATA_NAME             = "nametest1";
   private static final String USERDATA_LOGIN            = "logintest1";
   private static final String USERDATA_EMAIL            = "";
   private static final String USERDATA_PASSWORD         = "logintest1";

   private static final String USERROLE_NF_ADMINISTRATOR = "administrator";
   private static final String USERROLE_NF_AUTHOR        = "author";
   private static final String USERROLE_NF_WEBMASTER     = "webmaster";
   private static final String USERROLE_NF_VISITOR       = "visitor";

   private static final String _SPIP_1comite             = "1comite";
   private static final String _SPIP_0minirezo           = "0minirezo";
   private static final String _SPIP_5poubelle           = "5poubelle";
   private static final String _SPIP_6forum              = "6forum";

   private static final String _SPIP_WEBMESTRE_NON       = "non";
   private static final String _SPIP_WEBMESTRE_OUI       = "oui";

   private static final String USERDATA_UPDATE_NAME      = "My User";
   private static final String USERDATA_UPDATE_EMAIL     = "";

   private static final String SITE_NAME                 = "My spip site";
   private static final String SITE_ID                   = "spip1";
   private static final String SITE_DESC                 = "my description of the site";

   private static final String SITE_UPDATE_NAME          = "My New spip site";
   private static final String SITE_UPDATE_DESC          = "my new description of the site";

   private boolean             spipProfileActivated      = false;

   public SpipClientImplTest()
   {
      String property = System.getProperty("spip.profile");
      if ("true".equals(property))
      {
         spipProfileActivated = true;
      }
   }

   @Test
   public void connectPortWSDL() throws Exception
   {
      if (isSpipProfileActivated())
      {
         final ServiceFactory serviceFactory = ServiceFactory.newInstance();
         final URL url = new java.net.URL(new SpipConnectLocator().getSpipConnectPortAddress() + "?wsdl");
         final Service service = serviceFactory.createService(url, new SpipConnectLocator().getServiceName());
         assertThat(service, notNullValue());
      }
   }

   /**
    * @return the spipProfileActivated
    */
   public boolean isSpipProfileActivated()
   {
      return spipProfileActivated;
   }

   @Test
   public void create_site() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {

         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         SiteInput siteInput = new SiteInput();
         siteInput.setNom_site(SITE_NAME);
         siteInput.setSite_id(SITE_ID);
         siteInput.setDescriptif_site(SITE_DESC);
         String site = spipSoapClient.create_site(connector, siteInput);
         assertThat(site, notNullValue());
         assertEquals(SITE_ID, site);
      }
   }

   @Test
   public void update_site() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {

         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         SiteInput siteInput = new SiteInput();
         siteInput.setNom_site(SITE_UPDATE_NAME);
         siteInput.setSite_id(SITE_ID);
         siteInput.setDescriptif_site(SITE_UPDATE_DESC);
         boolean success = spipSoapClient.update_site(connector, siteInput);
         assertTrue(success);
      }
   }

   @Test
   public void get_roles() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {
         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);
         final String[] roles = spipSoapClient.get_roles(connector);

         assertThat(roles, notNullValue());
         assertEquals("administratorwebmasterauthorvisitor", roles[0] + roles[1] + roles[2] + roles[3]);
      }
   }

   @Test
   public void add_user() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {

         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         UserData userData = new UserData();
         userData.setNom(USERDATA_NAME);
         userData.setLogin(USERDATA_LOGIN);
         userData.setEmail(USERDATA_EMAIL);
         userData.setPass(USERDATA_PASSWORD);
         spipSoapClient.add_user(connector, userData);
      }
   }

   @Test
   public void add_user_site() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {

         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         UserData userData = new UserData();
         userData.setNom(USERDATA_NAME);
         userData.setLogin(USERDATA_LOGIN);
         userData.setEmail(USERDATA_EMAIL);
         userData.setPass(USERDATA_PASSWORD);

         spipSoapClient.add_user_site(connector, SITE_ID, USERDATA_LOGIN, USERROLE_NF_ADMINISTRATOR);
         UserInfo userInfo = spipSoapClient.get_user_info(connector, USERDATA_LOGIN);
         assertEquals(userInfo.getWebmestre(), SpipClientImplTest._SPIP_WEBMESTRE_NON);
         assertEquals(userInfo.getStatut(), SpipClientImplTest._SPIP_0minirezo);

         spipSoapClient.add_user_site(connector, SITE_ID, USERDATA_LOGIN, USERROLE_NF_WEBMASTER);
         userInfo = spipSoapClient.get_user_info(connector, USERDATA_LOGIN);
         assertEquals(userInfo.getWebmestre(), SpipClientImplTest._SPIP_WEBMESTRE_OUI);
         assertEquals(userInfo.getStatut(), SpipClientImplTest._SPIP_0minirezo);

         spipSoapClient.add_user_site(connector, SITE_ID, USERDATA_LOGIN, USERROLE_NF_VISITOR);
         userInfo = spipSoapClient.get_user_info(connector, USERDATA_LOGIN);
         assertEquals(userInfo.getWebmestre(), SpipClientImplTest._SPIP_WEBMESTRE_NON);
         assertEquals(userInfo.getStatut(), SpipClientImplTest._SPIP_6forum);

         spipSoapClient.add_user_site(connector, SITE_ID, USERDATA_LOGIN, USERROLE_NF_AUTHOR);
         userInfo = spipSoapClient.get_user_info(connector, USERDATA_LOGIN);
         assertEquals(userInfo.getWebmestre(), SpipClientImplTest._SPIP_WEBMESTRE_NON);
         assertEquals(userInfo.getStatut(), SpipClientImplTest._SPIP_1comite);
      }
   }

   @Test
   public void get_user_id() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {
         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);
         BigInteger userId = spipSoapClient.get_user_id(connector, USERDATA_LOGIN);
         assertThat(userId, notNullValue());
      }
   }

   @Test
   public void update_user() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {

         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         UserData userData = new UserData();
         userData.setLogin(USERDATA_LOGIN);
         userData.setPass(USERDATA_PASSWORD);
         userData.setNom(USERDATA_UPDATE_NAME);
         userData.setEmail(USERDATA_UPDATE_EMAIL);
         final boolean success = spipSoapClient.update_user(connector, USERDATA_LOGIN, userData);
         assertThat(success, notNullValue());
         assertTrue(success);
      }
   }

   @Test
   public void delete_user() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {

         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         final boolean success = spipSoapClient.delete_user(connector, USERDATA_LOGIN);
         assertTrue(success);
         UserInfo userInfo = spipSoapClient.get_user_info(connector, USERDATA_LOGIN);
         assertEquals(userInfo.getStatut(), SpipClientImplTest._SPIP_5poubelle);
      }
   }

   @Test
   public void delete_site() throws SpipSoapException
   {
      if (isSpipProfileActivated())
      {

         SpipSoapClient spipSoapClient = new SpipSoapClientImpl();
         SpipSoapConnector connector = spipSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);
         boolean success = spipSoapClient.delete_site(connector, SITE_ID);
         assertTrue(success);

      }
   }
}
