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
package org.novaforge.forge.plugins.cms.spip.internal.client;

import org.novaforge.forge.plugins.cms.spip.client.SpipSoapClient;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapConnector;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapException;
import org.novaforge.forge.plugins.cms.spip.internal.SpipConstant;
import org.novaforge.forge.plugins.cms.spip.soap.SiteInput;
import org.novaforge.forge.plugins.cms.spip.soap.SpipConnectBindingStub;
import org.novaforge.forge.plugins.cms.spip.soap.SpipConnectLocator;
import org.novaforge.forge.plugins.cms.spip.soap.UserData;
import org.novaforge.forge.plugins.cms.spip.soap.UserInfo;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * This class is used in order to instantiate new connector to spip Web-service.
 * 
 * @author blachonm
 */
public class SpipSoapClientImpl implements SpipSoapClient
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpipSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
	    throws SpipSoapException
	{
		SpipSoapConnector connector = null;
		URL url = null;
		try
		{
			url = new URL(pBaseUrl);
			SpipConnectBindingStub connectBindingStub = (SpipConnectBindingStub) new SpipConnectLocator()
			    .getSpipConnectPort(url);
			connectBindingStub.setTimeout(SpipConstant.SOAP_TIMEOUT);
			connector = new SpipSoapConnectorImpl(pUsername, pPassword, connectBindingStub);
		}
		catch (MalformedURLException e)
		{
			throw new SpipSoapException(String.format("Unable to build URL object with [value=%s]", pBaseUrl), e);
		}
		catch (ServiceException e)
		{
			throw new SpipSoapException(String.format("Unable to get the Spip Connector Binding with [URL=%s]",
			    url.toString()), e);
		}
		return connector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger add_user(final SpipSoapConnector pConnector, final UserData userData)
	    throws SpipSoapException
	{
		BigInteger returnId = null;
		try
		{
			returnId = pConnector.getConnectBindingStub().add_user(pConnector.getUsername(),
			    pConnector.getPassword(), userData);

		}
		catch (RemoteException e)
		{
			throw new SpipSoapException(String.format(
			    "Unable to add user account with [login=%s, email=%s, nom=%s]", userData.getLogin(),
			    userData.getEmail(), userData.getNom()), e);
		}
		return returnId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete_user(final SpipSoapConnector pConnector, final String pLogin)
	    throws SpipSoapException
	{
		boolean returnSucess = false;
		try
		{
			returnSucess = pConnector.getConnectBindingStub().delete_user(pConnector.getUsername(),
			    pConnector.getPassword(), pLogin);
		}
		catch (RemoteException e)
		{
			throw new SpipSoapException(String.format("Unable to delete user account with [login=%s]", pLogin), e);
		}
		return returnSucess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger get_user_id(final SpipSoapConnector pConnector, final String pLogin)
	    throws SpipSoapException
	{
		BigInteger returnId = null;
		try
		{
			returnId = pConnector.getConnectBindingStub().get_user_id(pConnector.getUsername(),
			    pConnector.getPassword(), pLogin);
		}
		catch (RemoteException e)
		{
			throw new SpipSoapException(String.format("Unable to get user account with [login=%s]", pLogin), e);
		}
		return returnId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserInfo get_user_info(final SpipSoapConnector pConnector, final String pLogin)
	    throws SpipSoapException
	{
		UserInfo userInfo = null;
		try
		{
			userInfo = pConnector.getConnectBindingStub().get_user_info(pConnector.getUsername(),
			    pConnector.getPassword(), pLogin);
		}
		catch (RemoteException e)
		{
			throw new SpipSoapException(String.format("Unable to get user account with [login=%s]", pLogin), e);
		}
		return userInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update_user(final SpipSoapConnector pConnector, final String pLogin, final UserData userData)
	    throws SpipSoapException
	{
		boolean returnSucces = false;
		try
		{
			returnSucces = pConnector.getConnectBindingStub().update_user(pConnector.getUsername(),
			    pConnector.getPassword(), pLogin, userData);
		}
		catch (RemoteException e)
		{
			throw new SpipSoapException(String.format(
			    "Unable to update user data with [login=%s, email=%s, real_name=%s]", userData.getLogin(),
			    userData.getEmail(), userData.getNom()), e);
		}
		return returnSucces;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String create_site(final SpipSoapConnector pConnector, final SiteInput pSiteInput)
	    throws SpipSoapException
	{
		String returnId = null;
		try
		{
			returnId = pConnector.getConnectBindingStub().create_site(pConnector.getUsername(),
			    pConnector.getPassword(), pSiteInput);
		}
		catch (RemoteException e)
		{
			throw new SpipSoapException(String.format("Unable to create Spip site with [site_id=%s, site name=%s]",
			    pSiteInput.getSite_id(), pSiteInput.getNom_site()), e);
		}

		return returnId;
	}

	@Override
	public BigInteger add_user_site(final SpipSoapConnector pConnector, final String pSiteId,
	    final String pLogin, final String role) throws SpipSoapException
	{
		BigInteger returnId = null;
		try
		{
			returnId = pConnector.getConnectBindingStub().add_user_site(pConnector.getUsername(),
			    pConnector.getPassword(), pLogin, pSiteId, role);

		}
		catch (RemoteException e)
		{
			throw new SpipSoapException(String.format(
			    "Unable to add user account to spip site with [login=%s, pSiteId=%s, role=%s]", pLogin, pSiteId,
			    role), e);
		}
		return returnId;
	}

	@Override
	public boolean delete_site(final SpipSoapConnector pConnector, final String siteId)
	    throws SpipSoapException
	{

		boolean returnSucces = false;
		try
		{
			returnSucces = pConnector.getConnectBindingStub().delete_site(pConnector.getUsername(),
			    pConnector.getPassword(), siteId);
		}
		catch (RemoteException e)
		{
			throw new SpipSoapException("Unable to delete Spip site for siteId= " + siteId, e);
		}
		return returnSucces;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update_site(final SpipSoapConnector pConnector, final SiteInput pSiteInput)
	    throws SpipSoapException
	{
		boolean returnSucces = false;
		try
		{
			returnSucces = pConnector.getConnectBindingStub().update_site(pConnector.getUsername(),
			    pConnector.getPassword(), pSiteInput);
		}
		catch (RemoteException e)
		{
			throw new SpipSoapException(String.format("Unable to create Spip site with [site_id=%s, host_name=%s]",
			    pSiteInput.getSite_id(), pSiteInput.getNom_site()), e);
		}
		return returnSucces;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] get_roles(final SpipSoapConnector pConnector) throws SpipSoapException
	{
		String[] roles = null;
		try
		{
			roles = pConnector.getConnectBindingStub().get_roles();
		}
		catch (RemoteException e)
		{
			throw new SpipSoapException("Unable to get the list of available roles.", e);
		}
		return roles;
	}

}
