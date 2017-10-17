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
 * MantisConnectTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.novaforge.forge.plugins.bugtracker.mantis.internal.client;

import junit.framework.TestCase;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapClient;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapConnector;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.internal.client.MantisARDSoapClientImpl;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.AccountData;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.MantisConnectLocator;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.ObjectRef;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import java.math.BigInteger;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * To trigger this test, activate a java system property "mantis.profile".
 * 
 * @author lamirang
 */
public class MantisARDClientImplTest extends TestCase
{

   private static final String     ENDPOINT               = "http://127.0.0.1/mats/api/soap/mantisconnect.php";
   private static final String     USERNAME               = "administrator";
   private static final String     PASSWORD               = "root";

   private static final String     ACCOUNT_USERNAME       = "Test";
   private static final String     ACCOUNT_NAME           = "My Test User";
   private static final String     ACCOUNT_EMAIL          = "";
   private static final BigInteger ACCOUNT_STATUS         = BigInteger.valueOf(90);
   private static final String     ACCOUNT_PASSWORD       = "1234";

   private static final String     ACCOUNT_UPDATE_NAME    = "My User";
   private static final String     ACCOUNT_UPDATE_EMAIL   = "";

   private static final String     PROJECT_NAME           = "ProjectTest";
   private boolean                 mantisProfileActivated = false;

   public MantisARDClientImplTest(String name)
   {
      super(name);
      String property = System.getProperty("mantis.profile");
      if ("true".equals(property))
      {
         mantisProfileActivated = true;
      }
   }

   public void testMantisConnectPortWSDL() throws Exception
   {
      if (isMantisProfileActivated())
      {
         final ServiceFactory serviceFactory = ServiceFactory.newInstance();
         final URL url = new java.net.URL(new MantisConnectLocator().getMantisConnectPortAddress() + "?wsdl");
         final Service service = serviceFactory.createService(url,
               new MantisConnectLocator().getServiceName());
         assertThat(service, notNullValue());
      }
   }

   /**
    * @return the mantisProfileActivated
    */
   public boolean isMantisProfileActivated()
   {
      return mantisProfileActivated;
   }

   public void testMantisConnectPortMc_enum_access_levels() throws Exception
   {
      if (isMantisProfileActivated())
      {
         MantisARDSoapClient mantisARDSoapClient = new MantisARDSoapClientImpl();
         MantisARDSoapConnector connector = mantisARDSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);
         final ObjectRef[] accesses = mantisARDSoapClient.mc_enum_access_levels(connector);
         assertThat(accesses, notNullValue());
      }
   }

   public void testMantisConnectPortMc_account_add() throws Exception
   {
      if (isMantisProfileActivated())
      {

         MantisARDSoapClient MantisARDSoapClient = new MantisARDSoapClientImpl();
         MantisARDSoapConnector connector = MantisARDSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         AccountData accountData = new AccountData();
         accountData.setName(ACCOUNT_USERNAME);
         accountData.setReal_name(ACCOUNT_NAME);
         accountData.setEmail(ACCOUNT_EMAIL);
         accountData.setAccess(ACCOUNT_STATUS);
         MantisARDSoapClient.mc_account_add(connector, accountData, ACCOUNT_PASSWORD);
      }
   }

   public void testMantisConnectPortMc_account_get() throws Exception
   {
      if (isMantisProfileActivated())
      {
         MantisARDSoapClient MantisARDSoapClient = new MantisARDSoapClientImpl();
         MantisARDSoapConnector connector = MantisARDSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         BigInteger userId = MantisARDSoapClient.mc_account_get(connector, ACCOUNT_USERNAME);
         assertThat(userId, notNullValue());
      }
   }

   public void testMantisConnectPortMc_account_update() throws Exception
   {
      if (isMantisProfileActivated())
      {

         MantisARDSoapClient MantisARDSoapClient = new MantisARDSoapClientImpl();
         MantisARDSoapConnector connector = MantisARDSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         BigInteger userId = MantisARDSoapClient.mc_account_get(connector, ACCOUNT_USERNAME);
         AccountData accountData = new AccountData();
         accountData.setReal_name(ACCOUNT_UPDATE_NAME);
         accountData.setEmail(ACCOUNT_UPDATE_EMAIL);

         accountData.getId();

         final boolean success = MantisARDSoapClient.mc_account_update(connector, userId, accountData, null);
         assertThat(success, notNullValue());
         assertThat(success, is(true));
      }
   }

   public void testMantisConnectPortMc_account_delete() throws Exception
   {
      if (isMantisProfileActivated())
      {

         MantisARDSoapClient MantisARDSoapClient = new MantisARDSoapClientImpl();
         MantisARDSoapConnector connector = MantisARDSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         BigInteger userId = MantisARDSoapClient.mc_account_get(connector, ACCOUNT_USERNAME);
         MantisARDSoapClient.mc_account_delete(connector, userId);
      }
   }

   public void testMantisConnectPortMc_project_get_id_from_name() throws Exception
   {
      if (isMantisProfileActivated())
      {

         MantisARDSoapClient MantisARDSoapClient = new MantisARDSoapClientImpl();
         MantisARDSoapConnector connector = MantisARDSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

         BigInteger id = MantisARDSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);
         assertThat(id, notNullValue());
      }
   }
}
