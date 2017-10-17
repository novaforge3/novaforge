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
package org.novaforge.forge.plugins.forum.phpbb.internal.client;

import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapClient;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapConnector;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapException;
import org.novaforge.forge.plugins.forum.phpbb.internal.PhpBBConstant;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbForum;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbUser;
import org.novaforge.forge.plugins.forum.phpbb.soap.ObjectRef;
import org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectBindingStub;
import org.novaforge.forge.plugins.forum.phpbb.soap.PhpBBConnectLocator;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * This class is used in order to instantiate new connector to PhpBB Web-service.
 *
 * @author vvigo
 */
public class PhpBBSoapClientImpl implements PhpBBSoapClient
{

  /**
   * {@inheritDoc}
   */
  @Override
  public PhpBBSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
      throws PhpBBSoapException
  {
    PhpBBSoapConnector connector = null;
    URL                url       = null;
    try
    {
      url = new URL(pBaseUrl);
      PhpBBConnectBindingStub connectBindingStub = (PhpBBConnectBindingStub) new PhpBBConnectLocator()
                                                                                 .getPhpBBConnectPort(url);
      connectBindingStub.setTimeout(PhpBBConstant.SOAP_TIMEOUT);
      connector = new PhpBBSoapConnectorImpl(pUsername, pPassword, connectBindingStub);
    }
    catch (MalformedURLException e)
    {
      throw new PhpBBSoapException(String.format("Unable to build URL object with [value=%s]", pBaseUrl), e);
    }
    catch (ServiceException e)
    {
      throw new PhpBBSoapException(String.format("Unable to get the PhpBB Connector Binding with [URL=%s]",
                                                 url.toString()), e);
    }
    return connector;
  }

  @Override
  public boolean pc_purge_cache(final PhpBBSoapConnector pConnector) throws PhpBBSoapException
  {
    boolean returnSucess = false;
    try
    {
      returnSucess = pConnector.getConnectBindingStub().pc_purge_cache(pConnector.getUsername(),
                                                                       pConnector.getPassword());
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException("Unable to purge server cache", e);
    }
    return returnSucess;
  }

  @Override
  public BigInteger pc_account_add(final PhpBBSoapConnector pConnector, PhpbbUser pUser) throws PhpBBSoapException
  {
    BigInteger returnId = null;
    try
    {
      returnId = pConnector.getConnectBindingStub().pc_account_add(pConnector.getUsername(), pConnector.getPassword(),
                                                                   pUser.getUserName(), pUser.getPassword(),
                                                                   pUser.getEmail(), pUser.getLanguage());
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String.format("Unable to add user account with [login=%s, email=%s]",
                                                 pUser.getUserName(), pUser.getEmail()), e);
    }
    return returnId;
  }

  @Override
  public BigInteger pc_create_superadmin(final PhpBBSoapConnector pConnector, PhpbbUser pUser) throws PhpBBSoapException
  {
    BigInteger returnId = null;
    try
    {
      returnId = pConnector.getConnectBindingStub().pc_create_superadmin(pConnector.getUsername(),
                                                                         pConnector.getPassword(), pUser.getUserName(),
                                                                         pUser.getPassword(), pUser.getEmail(),
                                                                         pUser.getLanguage());
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String.format("Unable to add user account with [login=%s, email=%s]",
                                                 pUser.getUserName(), pUser.getEmail()), e);
    }
    return returnId;
  }

  @Override
  public boolean pc_account_delete(final PhpBBSoapConnector pConnector, PhpbbUser pUser) throws PhpBBSoapException
  {
    boolean returnSucess = false;
    try
    {
      returnSucess = pConnector.getConnectBindingStub().pc_account_delete(pConnector.getUsername(),
                                                                          pConnector.getPassword(),
                                                                          pUser.getUserName());
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String.format("Unable to delete user account with [login=%s]", pUser.getUserName()),
                                   e);
    }
    return returnSucess;
  }

  @Override
  public BigInteger pc_account_get(final PhpBBSoapConnector pConnector, PhpbbUser pUser) throws PhpBBSoapException
  {
    BigInteger returnId = null;
    try
    {
      returnId = pConnector.getConnectBindingStub().pc_account_get(pConnector.getUsername(), pConnector.getPassword(),
                                                                   pUser.getUserName());
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String.format("Unable to get user account with [login=%s]", pUser.getUserName()), e);
    }
    return returnId;
  }

  @Override
  public boolean pc_account_update(final PhpBBSoapConnector pConnector, PhpbbUser pUser) throws PhpBBSoapException
  {
    boolean returnSucces = false;
    try
    {
      returnSucces = pConnector.getConnectBindingStub().pc_account_update(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pUser.getUserName(),
                                                                          pUser.getPassword(), pUser.getEmail(),
                                                                          pUser.getLanguage());
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String.format("Unable to add user account with [login=%s, email=%s]",
                                                 pUser.getUserName(), pUser.getEmail()), e);
    }
    return returnSucces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ObjectRef[] pc_enum_access_levels(final PhpBBSoapConnector pConnector) throws PhpBBSoapException
  {
    ObjectRef[] returnAcces = null;
    try
    {
      returnAcces = pConnector.getConnectBindingStub().pc_enum_access_levels(pConnector.getUsername(),
                                                                             pConnector.getPassword());

    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException("Unable to get the list of acces level available.", e);
    }
    return returnAcces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger pc_project_add(final PhpBBSoapConnector pConnector, PhpbbForum pProjectData)
      throws PhpBBSoapException
  {
    BigInteger returnId = null;
    try
    {
      returnId = pConnector.getConnectBindingStub().pc_project_add(pConnector.getUsername(), pConnector.getPassword(),
                                                                   pProjectData.getCategorie(), pProjectData.getName(),
                                                                   pProjectData.getDescription());
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String.format("Unable to add project with [name=%s, data=%s]",
                                                 pProjectData.getName(), pProjectData.toString()), e);
    }
    return returnId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean pc_project_add_user(final PhpBBSoapConnector pConnector, BigInteger pProjectId, PhpbbUser pUser,
                                     String pAccessName) throws PhpBBSoapException
  {
    boolean returnAccess = false;
    try
    {
      returnAccess = pConnector.getConnectBindingStub().pc_project_add_user_to_forum(pConnector.getUsername(),
                                                                                     pConnector.getPassword(),
                                                                                     pProjectId, pUser.getUserName(),
                                                                                     pAccessName);
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String
                                       .format("Unable to add user to project with [project_id=%s, user_login=%s, acces_name=%s]",
                                               pProjectId, pUser.getUserName(), pAccessName), e);
    }
    return returnAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean pc_project_delete(final PhpBBSoapConnector pConnector, BigInteger pProjectId) throws PhpBBSoapException
  {
    boolean returnSuccess = false;
    try
    {
      returnSuccess = pConnector.getConnectBindingStub().pc_project_delete(pConnector.getUsername(),
                                                                           pConnector.getPassword(), pProjectId);
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String.format("Unable to delete project with [project_id=%s]", pProjectId), e);
    }
    return returnSuccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean pc_project_remove_user(final PhpBBSoapConnector pConnector, BigInteger pProjectId, PhpbbUser pUser)
      throws PhpBBSoapException
  {
    boolean returnSucess = false;
    try
    {
      returnSucess = pConnector.getConnectBindingStub().pc_project_remove_user_from_forum(pConnector.getUsername(),
                                                                                          pConnector.getPassword(),
                                                                                          pProjectId,
                                                                                          pUser.getUserName());
    }
    catch (RemoteException e)
    {
      throw new PhpBBSoapException(String.format("Unable to delete user account with [project_id=%s, user_login=%s]",
                                                 pProjectId, pUser.getUserName()), e);
    }
    return returnSucess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean pc_project_update(final PhpBBSoapConnector pConnector, BigInteger pProjectId, PhpbbForum pProjectData)
      throws PhpBBSoapException
  {
    boolean returnSucess = false;
    try
    {
      returnSucess = pConnector.getConnectBindingStub().pc_project_update(pConnector.getUsername(),
                                                                          pConnector.getPassword(), pProjectId,
                                                                          pProjectData.getName(),
                                                                          pProjectData.getDescription());
    }
    catch (RemoteException e)
    {

      throw new PhpBBSoapException(String.format("Unable to update project with [id=%s]", pProjectId), e);
    }
    return returnSucess;
  }

}
