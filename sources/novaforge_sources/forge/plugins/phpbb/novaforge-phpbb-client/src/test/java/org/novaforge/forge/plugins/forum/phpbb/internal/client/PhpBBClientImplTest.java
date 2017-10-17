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
 * PhpBBConnectTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.novaforge.forge.plugins.forum.phpbb.internal.client;

import junit.framework.TestCase;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapClient;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapConnector;
import org.novaforge.forge.plugins.forum.phpbb.internal.model.PhpbbForumImpl;
import org.novaforge.forge.plugins.forum.phpbb.internal.model.PhpbbUserImpl;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbForum;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbUser;
import org.novaforge.forge.plugins.forum.phpbb.soap.ObjectRef;
import org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectLocator;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import java.math.BigInteger;
import java.net.URL;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * To trigger this test, activate a java system property "phpbb.profile".
 * 
 * @author caseryj
 */
public class PhpBBClientImplTest extends TestCase
{

   private static final String     ENDPOINT              = "http://localhost/phpBB3/api/soap/phpbbconnect.php";
   private static final String     USERNAME               = "administrator";
   private static final String     PASSWORD              = "password";

   private static final String     ACCOUNT_USERNAME      = "UserTest";
   private static final String     ACCOUNT_NAME           = "My Test User";
   private static final String     ACCOUNT_EMAIL          = "";
   private static final BigInteger ACCOUNT_STATUS         = BigInteger.valueOf(90);
   private static final String     ACCOUNT_PASSWORD       = "1234";

   private static final String     ACCOUNT_SUPERADMIN_NAME     = "MySuperAdmin";
   private static final String     ACCOUNT_SUPERADMIN_EMAIL    = "";
   private static final String     ACCOUNT_SUPERADMIN_PASSWORD = "aqzsedrf1é";

   private static final String     ACCOUNT_UPDATE_NAME    = "My User";
   private static final String     ACCOUNT_UPDATE_EMAIL   = "";

   private static final String     PROJECT_NAME           = "ProjectTest";
   private static final String     PROJECT_DES            = "My Test Project";
   private static final BigInteger PROJECT_ACCESS         = BigInteger.valueOf(50);

   private static final String     PROJECT_UPDATE_DES     = "My New Project";

   private boolean                 phpbbProfileActivated = false;

   public PhpBBClientImplTest(String name)
   {
      super(name);
      String property = System.getProperty("phpbb.profile");
      if ("true".equals(property))
      {
         phpbbProfileActivated = true;
      }
   }

   public void testMantisConnectPortWSDL() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         final ServiceFactory serviceFactory = ServiceFactory.newInstance();
         final URL url = new java.net.URL(new PhpBBConnectLocator().getPhpBBConnectPortAddress() + "?wsdl");
         final Service service = serviceFactory.createService(url,
               new PhpBBConnectLocator().getServiceName());
         assertThat(service, notNullValue());
      }
   }

   /**
    * @return the mantisProfileActivated
    */
   public boolean isPhpBBProfileActivated()
   {
      return phpbbProfileActivated;
   }

   public void test1_Mc_enum_access_levels() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         PhpBBSoapClient phpbbSoapClient = new PhpBBSoapClientImpl();
         // Obtain phpbb connector
         PhpBBSoapConnector connector = phpbbSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);
         ObjectRef[] accesses = phpbbSoapClient.pc_enum_access_levels(connector);

         for (ObjectRef objectRef : accesses)
         {
            System.out.println(objectRef.toString());
         }


         assertThat(accesses, notNullValue());
      }
   }

   public void test2_1_Mc_account_add() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         PhpBBSoapClient phpbbSoapClient = new PhpBBSoapClientImpl();
         // Obtain phpbb connector
         PhpBBSoapConnector connector = phpbbSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         PhpbbUser pUser = new PhpbbUserImpl();
         pUser.setUserName(ACCOUNT_USERNAME);
         pUser.setEmail(ACCOUNT_EMAIL);
         pUser.setPassword(ACCOUNT_PASSWORD);

         BigInteger userId = phpbbSoapClient.pc_account_add(connector, pUser);

         System.out.println("account id is :\n");
         System.out.println(userId.toString());

         assertTrue(!userId.equals(new BigInteger("-1")));

      }
   }

   public void test2_2_Mc_account_get() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         PhpBBSoapClient phpbbSoapClient = new PhpBBSoapClientImpl();
         // Obtain phpbb connector
         PhpBBSoapConnector connector = phpbbSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         PhpbbUser pUser = new PhpbbUserImpl();
         pUser.setUserName(ACCOUNT_USERNAME);
         pUser.setEmail(ACCOUNT_EMAIL);
         pUser.setPassword(ACCOUNT_PASSWORD);

         BigInteger userId = phpbbSoapClient.pc_account_get(connector, pUser);

         System.out.println("account id is :\n");
         System.out.println(userId.toString());

         assertTrue(!userId.equals(new BigInteger("-1")));

      }
   }

   public void test2_3_Mc_account_update() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         PhpBBSoapClient phpbbSoapClient = new PhpBBSoapClientImpl();
         // Obtain phpbb connector
         PhpBBSoapConnector connector = phpbbSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         PhpbbUser pUser = new PhpbbUserImpl();
         pUser.setUserName(ACCOUNT_USERNAME);
         pUser.setEmail(ACCOUNT_UPDATE_EMAIL);
         pUser.setPassword(ACCOUNT_PASSWORD);
         pUser.setLanguage("en");

         boolean userId = phpbbSoapClient.pc_account_update(connector, pUser);

         System.out.println("account id is :\n");
         System.out.println(userId);

         assertTrue(userId);

      }
   }

   public void test2_4_Mc_account_delete() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         PhpBBSoapClient phpbbSoapClient = new PhpBBSoapClientImpl();
         // Obtain phpbb connector
         PhpBBSoapConnector connector = phpbbSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         PhpbbUser pUser = new PhpbbUserImpl();
         pUser.setUserName(ACCOUNT_USERNAME);
         pUser.setEmail(ACCOUNT_UPDATE_EMAIL);
         pUser.setPassword(ACCOUNT_PASSWORD);

         boolean userId = phpbbSoapClient.pc_account_delete(connector, pUser);

         System.out.println("account id is :\n");
         System.out.println(userId);

         assertTrue(userId);

      }
   }

   public void test3_1_Mc_project_add() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         PhpBBSoapClient phpbbSoapClient = new PhpBBSoapClientImpl();
         // Obtain phpbb connector
         PhpBBSoapConnector connector = phpbbSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         PhpbbForum pForum = new PhpbbForumImpl();
         pForum.setCategorie("maCategorie");
         pForum.setName("monForumTest1");
         pForum.setDescription("ma description de mon forum");

         BigInteger forumId = phpbbSoapClient.pc_project_add(connector, pForum);

         assertTrue(phpbbSoapClient.pc_purge_cache(connector));
         System.out.println("forum id is :\n");
         System.out.println(forumId);

         assertTrue(!forumId.equals(new BigInteger("-1")));

      }
   }

   public void test3_2_Mc_project_add_user() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         PhpBBSoapClient phpbbSoapClient = new PhpBBSoapClientImpl();
         // Obtain phpbb connector
         PhpBBSoapConnector connector = phpbbSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         PhpbbForum pForum = new PhpbbForumImpl();
         pForum.setName("monForumTest2");
         pForum.setDescription("ma description de mon forum");

         PhpbbUser pUser = new PhpbbUserImpl();
         pUser.setUserName(ACCOUNT_USERNAME);
         pUser.setEmail(ACCOUNT_EMAIL);
         pUser.setPassword(ACCOUNT_PASSWORD);

         phpbbSoapClient.pc_account_get(connector, pUser);

         BigInteger forumId = new BigInteger("21");// phpbbSoapClient.pc_project_add(pForum);

         ObjectRef[] accesses = phpbbSoapClient.pc_enum_access_levels(connector);
         String pAccessName = "";
         for (ObjectRef objectRef : accesses)
         {
            System.out.println(objectRef.getName());
            pAccessName = objectRef.getName();
            break;
         }

         boolean result = phpbbSoapClient.pc_project_add_user(connector, forumId, pUser, pAccessName);

         assertTrue(result);

      }
   }

   public void test3_3_Mc_project_update() throws Exception
   {
      if (isPhpBBProfileActivated())
      {


      }
   }

   public void test3_4_Mc_project_delete() throws Exception
   {
      if (isPhpBBProfileActivated())
      {


      }
   }

   public void test4_1_pc_create_superadmin() throws Exception
   {
      if (isPhpBBProfileActivated())
      {
         PhpBBSoapClient phpbbSoapClient = new PhpBBSoapClientImpl();
         // Obtain phpbb connector
         PhpBBSoapConnector connector = phpbbSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         PhpbbUser pUser = new PhpbbUserImpl();
         pUser.setUserName(ACCOUNT_SUPERADMIN_NAME);
         pUser.setEmail(ACCOUNT_SUPERADMIN_EMAIL);
         pUser.setPassword(ACCOUNT_SUPERADMIN_PASSWORD);

         BigInteger userId = phpbbSoapClient.pc_account_get(connector, pUser);

         if (userId.longValue() < 0)
         {
            userId = phpbbSoapClient.pc_create_superadmin(connector, pUser);
         }

         System.out.println("account id is :\n");
         System.out.println(userId.toString());

         assertTrue(!userId.equals(new BigInteger("-1")));

      }
   }
}
