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
package org.novaforge.forge.plugins.surveytool.limesurvey.internal.client;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClientExt;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyClientException;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClient;
import org.novaforge.forge.plugins.surveytool.limesurvey.internal.model.LimesurveyGroupUserImpl;
import org.novaforge.forge.plugins.surveytool.limesurvey.internal.model.LimesurveyUserImpl;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyGroupUser;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyUser;

/**
 * @author lamirang & goarzino
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LimesurveyXmlRpcClientImplTest
{

  private static final String TEST_LOGIN                 = "test_login";
  private static final String TEST_FULLNAME              = "test_fullname";
  private static final String TEST_EMAIL                 = "test.email@atos.net";
  private static final String TEST_PWD                   = "test123";
  private static final String TEST_SUPERADMIN            = "mySuperAdminTest";
  private static final String TEST_PWD_SUPERADMIN        = "pwdsuper";

  private static final String TEST_PWD_UPDATED           = "test321";
  private static final String TEST_EMAIL_UPDATED         = "test.email2@atos.net";
  private static final String TEST_FULLNAME_UPDATED      = "test_fullname2";

  private static final String TEST_GROUP                 = "testproject";
  private static final String TEST_SUMMARY               = "a test page";

  private static final String TEST_SUMMARY_UPDATED       = "a test page2";

//  private static final String LIMESURVEY_RPC_SERVER      = "http://localhost/limesurvey/index.php?r=admin/remotecontrol";
  private static final String LIMESURVEY_RPC_SERVER      = "http://localhost:8080/index.php?r=admin/remotecontrol";

  private static final String ADMIN_LOGIN                = "admin";
  private static final String ADMIN_PASSWORD             = "password";

  private boolean             limesurveyProfileActivated = false;

  public LimesurveyXmlRpcClientImplTest()
  {

    final String property = System.getProperty("limesurvey.profile");
    if ("true".equals(property))
    {
      limesurveyProfileActivated = true;
    }
  }

  @Test
  public void test0_1_establish_connection_OK() throws LimesurveyClientException
  {
    if (limesurveyProfileActivated)
    {
      System.out.println("== test0_1_establish_connection_OK()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);
      
      String sSessionKey = limesurveyXmlRpcClientExt.getSessionKey();
      System.out.println("Result :");
      if (sSessionKey!=null) 
        System.out.println("SessionKey = "+sSessionKey.toString());
      System.out.println("");
    }
    assert (true);
  }

  @Test
  public void test0_2_establish_connection_KO() throws LimesurveyClientException
  {
    if (limesurveyProfileActivated)
    {
      System.out.println("== test0_2_establish_connection_KO()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      try 
      {
        limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, "toto");      
        assert (false);
      }
      catch (final LimesurveyClientException e)
      {
        System.out.println("Result :");
        System.out.println("Exception = "+e.toString());
        assert (true);
      }   
      System.out.println("");
    }
  }

  @Test
  public void test0_3_release_connection() throws LimesurveyClientException
  {
    if (limesurveyProfileActivated)
    {
      System.out.println("== test0_3_release_connection()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);
      boolean bReturn = limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      System.out.println("Result :");
      System.out.println(bReturn);
      System.out.println("");
      assertTrue (bReturn);
    }
  }

  @Test
  public void test1_1_createUser() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test1_1_createUser()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);
      
      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_LOGIN);
      limeUser.setPassword(TEST_PWD);
      limeUser.setEmail(TEST_EMAIL);
      limeUser.setFullName(TEST_FULLNAME);
      limeUser.setLanguage("fr");

      final Integer iUserID = limesurveyXmlRpcClient.createUser(limesurveyXmlRpcClientExt, limeUser);

      System.out.println("Result :");
      System.out.println("UserID = "+iUserID.toString());
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      System.out.println("");
      
      assert (true);
    }
  }

  /**
   * @return the limesurveyProfileActivated
   */
  public boolean isLimesurveyProfileActivated()
  {
    return limesurveyProfileActivated;
  }

  @Test
  public void test1_2_isUserExist() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test1_2_isUserExist()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_LOGIN);

      final Integer iUserID = limesurveyXmlRpcClient.isUserExist(limesurveyXmlRpcClientExt, limeUser);

      System.out.println("Result :");
      System.out.println(iUserID.toString());
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      System.out.println("");

      assert (true);
    }
  }

  @Test
  public void test1_3_createSuperAdmin() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test1_3_createSuperAdmin()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_SUPERADMIN);
      limeUser.setPassword(TEST_PWD_SUPERADMIN);
      limeUser.setEmail(TEST_EMAIL);
      limeUser.setFullName(TEST_FULLNAME);
      limeUser.setLanguage("en");

      final Integer iUserID = limesurveyXmlRpcClient.isUserExist(limesurveyXmlRpcClientExt, limeUser);

      if ((iUserID!=null) && (iUserID.intValue()==-1))
      {
        final Integer iSuperAdminID = limesurveyXmlRpcClient.createSuperAdmin(limesurveyXmlRpcClientExt, limeUser);
        System.out.println("Result :");
        System.out.println(iSuperAdminID.toString());
      }
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      System.out.println("");

      assert (true);
    }
  }

  @Test
  public void test1_4_updateUser() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test1_4_updateUser()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_LOGIN);
      limeUser.setPassword(TEST_PWD_UPDATED);
      limeUser.setEmail(TEST_EMAIL_UPDATED);
      limeUser.setFullName(TEST_FULLNAME_UPDATED);
      limeUser.setLanguage("en");

      final boolean result = limesurveyXmlRpcClient.updateUser(limesurveyXmlRpcClientExt, limeUser);
      System.out.println("Result :");
      System.out.println(result);
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      System.out.println("");

      assertTrue (result);
    }
  }

  @Test
  public void test1_5_setUserPermission() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test1_5_setUserPermission()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_LOGIN);
      limeUser.setPassword(TEST_PWD);
      limeUser.setEmail(TEST_EMAIL);
      limeUser.setFullName(TEST_FULLNAME);

      final boolean result = limesurveyXmlRpcClient.setCreateSurveyPermission(limesurveyXmlRpcClientExt, limeUser, true);
      System.out.println("Result :");
      System.out.println(result);
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      assertTrue (result);
      System.out.println("");
    }
  }

  @Test
  public void test2_1_createGroupUser() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test2_1_createGroupUser()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyGroupUser limeGroup = new LimesurveyGroupUserImpl();
      limeGroup.setName(TEST_GROUP);
      limeGroup.setDescription(TEST_SUMMARY);

      limesurveyXmlRpcClient.createGroupUser(limesurveyXmlRpcClientExt, limeGroup);

      final Integer iGroupID = limesurveyXmlRpcClient.isGroupUserExist(limesurveyXmlRpcClientExt, limeGroup);

      System.out.println("Result :");
      System.out.println(iGroupID.toString());
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      System.out.println("");

      assert (true);
    }
  }

@Test
  public void test2_2_updateGroupUser() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test2_2_updateGroupUser()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyGroupUser limeGroup = new LimesurveyGroupUserImpl();
      limeGroup.setName(TEST_GROUP);

      final LimesurveyGroupUser limeGroupNew = new LimesurveyGroupUserImpl();
      limeGroupNew.setName(TEST_GROUP);
      limeGroupNew.setDescription(TEST_SUMMARY_UPDATED);

      final Integer iGroupID = limesurveyXmlRpcClient.isGroupUserExist(limesurveyXmlRpcClientExt, limeGroup);
      final boolean result = limesurveyXmlRpcClient.updateGroupUser(limesurveyXmlRpcClientExt, limeGroupNew, iGroupID);
      System.out.println("Result :");
      System.out.println(result);
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      assertTrue (result);
      System.out.println("");
    }
  }

  @Test
  public void test2_3_addUserInGroup() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test2_3_addUserInGroup()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_LOGIN);
      limeUser.setPassword(TEST_PWD);
      limeUser.setEmail(TEST_EMAIL);
      limeUser.setFullName(TEST_FULLNAME);

      final LimesurveyGroupUser limeGroup = new LimesurveyGroupUserImpl();
      limeGroup.setName(TEST_GROUP);
      limeGroup.setDescription(TEST_SUMMARY);

      final boolean result = limesurveyXmlRpcClient.addUserInGroup(limesurveyXmlRpcClientExt, limeUser, limeGroup);
      System.out.println("Result :");
      System.out.println(result);
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      assertTrue (result);
      System.out.println("");
    }
  }

  @Test
  public void test2_4_removeUserFromGroup() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test2_4_removeUserFromGroup()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_LOGIN);
      limeUser.setPassword(TEST_PWD);
      limeUser.setEmail(TEST_EMAIL);
      limeUser.setFullName(TEST_FULLNAME);

      final LimesurveyGroupUser limeGroup = new LimesurveyGroupUserImpl();
      limeGroup.setName(TEST_GROUP);
      limeGroup.setDescription(TEST_SUMMARY);

      final Integer iGroupId = limesurveyXmlRpcClient.isGroupUserExist(limesurveyXmlRpcClientExt, limeGroup);
      final boolean result = limesurveyXmlRpcClient.removeUserFromGroup(limesurveyXmlRpcClientExt, limeUser, iGroupId.toString());
      System.out.println("Result :");
      System.out.println(result);
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      assertTrue (result);
      System.out.println("");
    }
  }

  @Test
  public void test2_5_addUserInGroupById() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test2_5_addUserInGroupById()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_LOGIN);
      limeUser.setPassword(TEST_PWD);
      limeUser.setEmail(TEST_EMAIL);
      limeUser.setFullName(TEST_FULLNAME);

      final LimesurveyGroupUser limeGroup = new LimesurveyGroupUserImpl();
      limeGroup.setName(TEST_GROUP);
      limeGroup.setDescription(TEST_SUMMARY);

      final Integer iGroupId = limesurveyXmlRpcClient.isGroupUserExist(limesurveyXmlRpcClientExt, limeGroup);

      final boolean result = limesurveyXmlRpcClient.addUserInGroupById(limesurveyXmlRpcClientExt, limeUser, iGroupId);
      System.out.println("Result :");
      System.out.println(result);
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      assertTrue (result);
      System.out.println("");
    }
  }

  @Test
  public void test3_deleteGroup() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test3_deleteGroup()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyGroupUser limeGroup = new LimesurveyGroupUserImpl();
      limeGroup.setName(TEST_GROUP);
      limeGroup.setDescription(TEST_SUMMARY);
      final Integer iGroupId = limesurveyXmlRpcClient.isGroupUserExist(limesurveyXmlRpcClientExt, limeGroup);
      System.out.println("GroupId :");
      System.out.println(iGroupId.toString());

      final boolean result = limesurveyXmlRpcClient.deleteGroupUser(limesurveyXmlRpcClientExt, iGroupId);
      System.out.println("Result :");
      System.out.println(result);
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      assertTrue (result);
      System.out.println("");
    }
  }

  @Test
  public void test4_deleteUser() throws LimesurveyClientException
  {
    if (isLimesurveyProfileActivated())
    {
      System.out.println("== test4_deleteUser()");
      final LimesurveyXmlRpcClient limesurveyXmlRpcClient = new LimesurveyXmlRpcClientImpl();
      LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt = null;
      limesurveyXmlRpcClientExt = limesurveyXmlRpcClient.getConnector(LIMESURVEY_RPC_SERVER, ADMIN_LOGIN, ADMIN_PASSWORD);

      final LimesurveyUser limeUser = new LimesurveyUserImpl();
      limeUser.setUserName(TEST_LOGIN);
      limeUser.setPassword(TEST_PWD);
      limeUser.setEmail(TEST_EMAIL);
      limeUser.setFullName(TEST_FULLNAME);

      final boolean result = limesurveyXmlRpcClient.deleteUser(limesurveyXmlRpcClientExt, limeUser);
      System.out.println("Result :");
      System.out.println(result);
      limesurveyXmlRpcClient.releaseSession(limesurveyXmlRpcClientExt);
      assertTrue (result);
      System.out.println("");
    }
  }
}