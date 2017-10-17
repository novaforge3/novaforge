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
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyClientException;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClient;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClientExt;
import org.novaforge.forge.plugins.surveytool.limesurvey.internal.LimesurveyXmlRpcConstant;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyGroupUser;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * This class is used in order to instantiate new connector to limesurvey xml-rpc server.
 * 
 * @author lamirang & goarzino
 */
public class LimesurveyXmlRpcClientImpl implements LimesurveyXmlRpcClient
{

  @Override
  public LimesurveyXmlRpcClientExt getConnector(final String pBaseUrl, final String pUsername,
      final String pPassword) throws LimesurveyClientException
  {
    final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt;
    try
    {
      config.setServerURL(new URL(pBaseUrl));
      config.setBasicUserName(pUsername);
      config.setBasicPassword(pPassword);
      config.setBasicEncoding(XmlRpcStreamConfig.UTF8_ENCODING);
    }
    catch (final MalformedURLException e)
    {
      throw new LimesurveyClientException(String.format("Unable to build server url with [baseurl=%s]",
          pBaseUrl));
    }

    limesurveyXmlRpcClientExt = new LimesurveyXmlRpcClientExt();
    limesurveyXmlRpcClientExt.setConfig(config);
    String sSessionKey = null;
    try
    {
      final List<String> user = new ArrayList<String>();
      user.add(pUsername);
      user.add(pPassword);
      XmlRpcClient xmlRpcClient = (XmlRpcClient)limesurveyXmlRpcClientExt;
      Object oReturn = xmlRpcClient.execute(LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_LOGIN, user);
      if (oReturn instanceof String)
        sSessionKey = (String) oReturn;
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to authentificate on server with [baseurl=%s, user=%s, message=%s]", pBaseUrl, pUsername, String.valueOf(oReturn)));        
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to authentificate on server - Unexpected class [%s]", oReturn.getClass().getName()));          
      
      if (!sSessionKey.equals("")) 
        limesurveyXmlRpcClientExt.setSessionKey(sSessionKey);
      else
        throw new LimesurveyClientException(String.format(
            "Authentification has failed on server with [baseurl=%s, user=%s]", pBaseUrl, pUsername));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to authentificate on server with [baseurl=%s, user=%s]", pBaseUrl, pUsername), e);
    }   
    return limesurveyXmlRpcClientExt;
  }

  @Override
  public boolean releaseSession(LimesurveyXmlRpcClientExt connector) throws LimesurveyClientException
  {
    String sReturnCall = null;
    boolean bReturn = false;
    try
    {
      final List<String> sessKey = new ArrayList<String>();
      sessKey.add(connector.getSessionKey());
      sReturnCall = (String) getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_LOGOUT, sessKey);
      if (sReturnCall.equals("OK"))
      {
        bReturn=true;
      }
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to perfom release of session with [session_key=%s]",
          connector.getSessionKey()), e);
    }
    return bReturn;   
  }
  
  @Override
  public Integer isUserExist(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers) throws LimesurveyClientException
  {
    Integer iReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pSurveyUsers.getUserName());
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_IS_USER_EXIST, params);
      if (oReturn instanceof Integer)
        iReturn = (Integer) oReturn;
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to determine if user account exists with [login=%s, message=%s]", pSurveyUsers.getUserName(),
            String.valueOf(oReturn)));        
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to determine if user account exists - Unexpected class [%s]", oReturn.getClass().getName()));          
   }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to determine if user account exists with [login=%s]",
          pSurveyUsers.getUserName()), e);
    }
    return iReturn;
  }

  @Override
  public Integer createUser(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers) throws LimesurveyClientException
  {
    Integer iReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pSurveyUsers.getUserName());
      params.add(pSurveyUsers.getPassword());
      params.add(pSurveyUsers.getEmail());
      params.add(pSurveyUsers.getFullName());
      params.add(pSurveyUsers.getLanguage().toLowerCase());    
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_CREATE_USER, params);
      if (oReturn instanceof Integer)
        iReturn = (Integer) oReturn;
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to add user account with [login=%s, email=%s, real_name=%s, message=%s]", pSurveyUsers.getUserName(),
            pSurveyUsers.getEmail(), pSurveyUsers.getFullName(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to add user account - Unexpected class [%s]", oReturn.getClass().getName()));          
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to add user account with [login=%s, email=%s, real_name=%s]", pSurveyUsers.getUserName(),
          pSurveyUsers.getEmail(), pSurveyUsers.getFullName()), e);
    }
    return iReturn;
  }

  @Override
  public Integer createSuperAdmin(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers) throws LimesurveyClientException
  {
    Integer iReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pSurveyUsers.getUserName());
      params.add(pSurveyUsers.getPassword());
      params.add(pSurveyUsers.getEmail());
      params.add(pSurveyUsers.getFullName());
      params.add(pSurveyUsers.getLanguage().toLowerCase());    
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_CREATE_SUPER_ADMIN, params);
      if (oReturn instanceof Integer)
        iReturn = (Integer) oReturn;
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to add super-admin account with [login=%s, email=%s, real_name=%s, message=%s]", pSurveyUsers.getUserName(),
            pSurveyUsers.getEmail(), pSurveyUsers.getFullName(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to add super-admin account - Unexpected class [%s]", oReturn.getClass().getName()));          
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to add super-admin account with [login=%s, email=%s, real_name=%s]",
          pSurveyUsers.getUserName(), pSurveyUsers.getEmail(), pSurveyUsers.getFullName()), e);
    }
    return iReturn;
  }

  @Override
  public boolean updateUser(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers) throws LimesurveyClientException
  {
    String sReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pSurveyUsers.getUserName());
      params.add(pSurveyUsers.getPassword());
      params.add(pSurveyUsers.getEmail());
      params.add(pSurveyUsers.getFullName());
      params.add(pSurveyUsers.getLanguage().toLowerCase());    
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_UPDATE_USER, params);
      if (oReturn instanceof String)
      {
        sReturn = (String) oReturn;
        if ((sReturn!=null) && (sReturn.equals("true")))
          return true;
        else
          return false;
      }
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to update user account with [login=%s, email=%s, real_name=%s, message=%s]", pSurveyUsers.getUserName(),
            pSurveyUsers.getEmail(), pSurveyUsers.getFullName(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to update user account - Unexpected class [%s]", oReturn.getClass().getName()));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to update user account with [login=%s, email=%s, real_name=%s]",
          pSurveyUsers.getUserName(), pSurveyUsers.getEmail(), pSurveyUsers.getFullName()), e);
    }
  }

  @Override
  public boolean setCreateSurveyPermission(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers, final boolean pCreateRight) throws LimesurveyClientException
  {
    String sReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pSurveyUsers.getUserName());
      params.add(Boolean.toString(pCreateRight));
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_UPDATE_USER_PERMISSION, params);
      if (oReturn instanceof String)
      {
        sReturn = (String) oReturn;
        if ((sReturn!=null) && (sReturn.equals("true")))
          return true;
        else
          return false;
      }
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to update user permission with [login=%s, email=%s, real_name=%s, message=%s]", pSurveyUsers.getUserName(),
            pSurveyUsers.getEmail(), pSurveyUsers.getFullName(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to update user permission - Unexpected class [%s]", oReturn.getClass().getName()));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to update user permission with [login=%s, email=%s, real_name=%s]",
          pSurveyUsers.getUserName(), pSurveyUsers.getEmail(), pSurveyUsers.getFullName()), e);
    }
  }

  @Override
  public boolean deleteUser(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers) throws LimesurveyClientException
  {
    String sReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pSurveyUsers.getUserName());
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_DELETE_USER, params);
      if (oReturn instanceof String)
      {
        sReturn = (String) oReturn;
        if ((sReturn!=null) && (sReturn.equals("true")))
          return true;
        else
          return false;
      }
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to delete user account with [login=%s, message=%s]", pSurveyUsers.getUserName(), 
            String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to delete user account - Unexpected class [%s]", oReturn.getClass().getName()));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to delete user account with [login=%s]",
          pSurveyUsers.getUserName()), e);
    }
  }

  @Override
  public Integer isGroupUserExist(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyGroupUser pSurveyGroupUsers) throws LimesurveyClientException
  {
    Integer iReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pSurveyGroupUsers.getName());
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_IS_USER_GROUP_EXIST, params);
      if (oReturn instanceof Integer)
        iReturn = (Integer) oReturn;
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to determine if user group exists with [name=%s, message=%s]", pSurveyGroupUsers.getName(),
            String.valueOf(oReturn)));        
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to determine if user group exists - Unexpected class [%s]", oReturn.getClass().getName()));          
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to determine if user group exists with [name=%s]",
          pSurveyGroupUsers.getName()), e);
    }
    return iReturn;
  }

  @Override
  public Integer isGroupUserExistById(final LimesurveyXmlRpcClientExt connector,
      final Integer pGroupId) throws LimesurveyClientException
  {
    Integer iReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pGroupId.toString());
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_IS_USER_GROUP_EXIST_BY_ID, params);
      if (oReturn instanceof Integer)
        iReturn = (Integer) oReturn;
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to determine if user group exists with [id=%s, message=%s]", pGroupId.toString(), 
            String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to determine if user group exists - Unexpected class [%s]", oReturn.getClass().getName()));          
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to determine if user group exists with [id=%s]", pGroupId.toString()), e);
    }
    return iReturn;
  }

  @Override
  public Integer createGroupUser(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyGroupUser pSurveyGroupUsers) throws LimesurveyClientException
  {
    Integer iReturn = null;
    try
    {
      final XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl)connector.getConfig();
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(config.getBasicUserName());
      params.add(pSurveyGroupUsers.getName());
      params.add(pSurveyGroupUsers.getDescription());
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_CREATE_USER_GROUP, params);
      if (oReturn instanceof Integer)
        iReturn = (Integer) oReturn;
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to add user group with [lname=%s, description=%s, message=%s]", pSurveyGroupUsers.getName(),
            pSurveyGroupUsers.getDescription(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to add user account - Unexpected class [%s]", oReturn.getClass().getName()));             
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to add user group with [name=%s, description=%s]", pSurveyGroupUsers.getName(),
          pSurveyGroupUsers.getDescription()), e);
    }
    return iReturn;
  }

  @Override
  public boolean updateGroupUser(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyGroupUser pSurveyGroupUsers, final Integer pGroupId)
      throws LimesurveyClientException
  {
    String sReturn = null;
    try
    {
      final XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl)connector.getConfig();
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(config.getBasicUserName());
      params.add(pGroupId.toString());
      params.add(pSurveyGroupUsers.getName());
      params.add(pSurveyGroupUsers.getDescription());
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_UPDATE_USER_GROUP, params);
      if (oReturn instanceof String)
      {
        sReturn = (String) oReturn;
        if ((sReturn!=null) && (sReturn.equals("true")))
          return true;
        else
          return false;
      }
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to update user group with [name=%s, description=%s, message=%s]", pSurveyGroupUsers.getName(),
            pSurveyGroupUsers.getDescription(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to update user account - Unexpected class [%s]", oReturn.getClass().getName()));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to update user group with [name=%s, description=%s]", pSurveyGroupUsers.getName(),
          pSurveyGroupUsers.getDescription()), e);
    }
  }

  @Override
  public boolean deleteGroupUser(final LimesurveyXmlRpcClientExt connector,
      final Integer pGroupId) throws LimesurveyClientException
  {
    String sReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pGroupId.toString());
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_DELETE_USER_GROUP, params);
      if (oReturn instanceof String)
      {
        sReturn = (String) oReturn;
        if ((sReturn!=null) && (sReturn.equals("true")))
          return true;
        else
          return false;
      }
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to delete user group with [group id=%s, message=%s]", pGroupId.toString(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to update user account - Unexpected class [%s]", oReturn.getClass().getName()));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format("Unable to delete user group with [group id=%s]",
          pGroupId.toString()), e);
    }
  }

  @Override
  public boolean addUserInGroup(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers, final LimesurveyGroupUser pSurveyGroupUsers)
      throws LimesurveyClientException
  {
    String sReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pSurveyGroupUsers.getName());
      params.add(pSurveyUsers.getUserName());     
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_ADD_USER_IN_GROUP, params);
      if (oReturn instanceof String)
      {
        sReturn = (String) oReturn;
        if ((sReturn!=null) && (sReturn.equals("true")))
          return true;
        else
          return false;
      }
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable add user to group with [username=%s, groupname=%s, message=%s]", pSurveyUsers.getUserName(),
            pSurveyGroupUsers.getName(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to update user account - Unexpected class [%s]", oReturn.getClass().getName()));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to add user to group with [username=%s, groupname=%s]", pSurveyUsers.getUserName(),
          pSurveyGroupUsers.getName()), e);
    }
  }

  @Override
  public boolean addUserInGroupById(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers, final Integer pGroupId) throws LimesurveyClientException
  {
    String sReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(pGroupId.toString());
      params.add(pSurveyUsers.getUserName());     
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_ADD_USER_IN_GROUP_BY_ID, params);
      if (oReturn instanceof String)
      {
        sReturn = (String) oReturn;
        if ((sReturn!=null) && (sReturn.equals("true")))
          return true;
        else
          return false;
      }
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable add user to group with [username=%s, groupid=%s, message=%s]", pSurveyUsers.getUserName(),
            pGroupId.toString(), String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to update user account - Unexpected class [%s]", oReturn.getClass().getName()));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to add user to group with [username=%s, groupid=%s]", pSurveyUsers.getUserName(),
          pGroupId.toString()), e);
    }
  }

  @Override
  public boolean removeUserFromGroup(final LimesurveyXmlRpcClientExt connector,
      final LimesurveyUser pSurveyUsers, final String sGroupId) throws LimesurveyClientException
  {
    String sReturn = null;
    try
    {
      final List<String> params = new ArrayList<String>();
      params.add(connector.getSessionKey());
      params.add(sGroupId);
      params.add(pSurveyUsers.getUserName());     
      Object oReturn = getXmlRpcClient(connector).execute(
          LimesurveyXmlRpcConstant.LIMESURVEY_METHOD_REMOVE_USER_FROM_GROUP, params);
      if (oReturn instanceof String)
      {
        sReturn = (String) oReturn;
        if ((sReturn!=null) && (sReturn.equals("true")))
          return true;
        else
          return false;
      }
      else if (oReturn instanceof HashMap)
        throw new LimesurveyClientException(String.format(
            "Unable to remove user from group with [username=%s, groupid=%s, message=%s]", pSurveyUsers.getUserName(),
            sGroupId, String.valueOf(oReturn)));      
      else 
        throw new LimesurveyClientException(String.format(
            "Unable to update user account - Unexpected class [%s]", oReturn.getClass().getName()));
    }
    catch (final XmlRpcException e)
    {
      throw new LimesurveyClientException(String.format(
          "Unable to remove user from group with [username=%s, groupid=%s]", pSurveyUsers.getUserName(),
          sGroupId), e);
    }
  }

  /**
   * Check if a connection is enable otherwise return an exception.
   * 
   * @return XmlRpcClient instance
   * @throws LimesurveyClientException
   *           occured if any connection was active.
   */
  private XmlRpcClient getXmlRpcClient(LimesurveyXmlRpcClientExt limesurveyXmlRpcClientExt) throws LimesurveyClientException
  {
    if (limesurveyXmlRpcClientExt != null)
    {
      return (XmlRpcClient)limesurveyXmlRpcClientExt;
    }
    else
    {
      throw new LimesurveyClientException("Not connected to a Limesurvey instance.");
    }
  }
}