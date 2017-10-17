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
package org.novaforge.forge.plugins.bugtracker.mantis.ard.internal.client;

import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapClient;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapConnector;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapException;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.internal.MantisARDConstant;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.AccountData;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.MantisConnectBindingStub;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.MantisConnectLocator;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.ObjectRef;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.ProjectData;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * This class is used in order to instantiate new connector to mantis ARD
 * Web-service.
 * 
 * @author BILET-JC
 */
public class MantisARDSoapClientImpl implements MantisARDSoapClient {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MantisARDSoapConnector getConnector(final String pBaseUrl, final String pUsername,
			final String pPassword) throws MantisARDSoapException {
		MantisARDSoapConnector connector = null;
		URL url = null;
		try {
			url = new URL(pBaseUrl);
			MantisConnectBindingStub connectBindingStub = (MantisConnectBindingStub) new MantisConnectLocator()
					.getMantisConnectPort(url);
			connectBindingStub.setTimeout(MantisARDConstant.SOAP_TIMEOUT);
			connector = new MantisARDSoapConnectorImpl(pUsername, pPassword, connectBindingStub);
		} catch (MalformedURLException e) {
			throw new MantisARDSoapException(String.format("Unable to build URL object with [value=%s]",
					pBaseUrl), e);
		} catch (ServiceException e) {
			throw new MantisARDSoapException(String.format(
					"Unable to get the Mantis Connector Binding with [URL=%s]", url.toString()), e);
		}
		return connector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger mc_account_add(final MantisARDSoapConnector pConnector, final AccountData pAccount,
			final String pPassword) throws MantisARDSoapException {
		BigInteger returnId = null;
		try {
			returnId = pConnector.getConnectBindingStub().mc_account_add(pConnector.getUsername(),
					pConnector.getPassword(), pAccount, pPassword);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(String.format(
					"Unable to add user account with [login=%s, email=%s, real_name=%s]", pAccount.getName(),
					pAccount.getEmail(), pAccount.getReal_name()), e);
		}
		return returnId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean mc_account_delete(final MantisARDSoapConnector pConnector, final BigInteger pUserId)
			throws MantisARDSoapException {
		boolean returnSucess = false;
		try {
			returnSucess = pConnector.getConnectBindingStub().mc_account_delete(pConnector.getUsername(),
					pConnector.getPassword(), pUserId);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(String.format("Unable to delete user account with [id=%s]",
					pUserId), e);
		}
		return returnSucess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger mc_account_get(final MantisARDSoapConnector pConnector, final String pUserName)
			throws MantisARDSoapException {
		BigInteger returnId = null;
		try {
			returnId = pConnector.getConnectBindingStub().mc_account_get(pConnector.getUsername(),
					pConnector.getPassword(), pUserName);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(String.format("Unable to get user account with [id=%s]",
					pUserName), e);
		}
		return returnId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean mc_account_update(final MantisARDSoapConnector pConnector, final BigInteger pUserId,
			final AccountData pAccount, final String pPassword) throws MantisARDSoapException {
		boolean returnSucces = false;
		try {
			returnSucces = pConnector.getConnectBindingStub().mc_account_update(pConnector.getUsername(),
					pConnector.getPassword(), pUserId, pAccount, pPassword);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(String.format(
					"Unable to add user account with [id=%s, login=%s, email=%s, real_name=%s]", pUserId,
					pAccount.getName(), pAccount.getEmail(), pAccount.getReal_name()), e);
		}
		return returnSucces;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObjectRef[] mc_enum_access_levels(final MantisARDSoapConnector pConnector)
			throws MantisARDSoapException {
		ObjectRef[] returnAcces = null;
		try {
			returnAcces = pConnector.getConnectBindingStub().mc_enum_access_levels(pConnector.getUsername(),
					pConnector.getPassword());
		} catch (RemoteException e) {
			throw new MantisARDSoapException("Unable to get the list of acces level available.", e);
		}
		return returnAcces;
	}

	@Override
	public boolean mc_configure_management(MantisARDSoapConnector pConnector, String pProjectName)
			throws MantisARDSoapException
	{
		boolean returnSuccess = false;
		try
		{
			returnSuccess = pConnector.getConnectBindingStub().mc_configure_management(pConnector.getUsername(),
																																								 pConnector.getPassword(),
																																								 pProjectName);
		}
		catch (RemoteException e)
		{
			throw new MantisARDSoapException("Unable to configure mantis ARD", e);
		}
		return returnSuccess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger mc_management_add_user(final MantisARDSoapConnector pConnector,
			final BigInteger pProjectId, final BigInteger pUserId, final BigInteger pAccessId)
			throws MantisARDSoapException {
		BigInteger returnAccess = null;
		try {
			returnAccess = pConnector.getConnectBindingStub().mc_management_add_user(
					pConnector.getUsername(), pConnector.getPassword(), pProjectId, pUserId, pAccessId);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(String.format(
					"Unable to add user to project with [project_id=%s, user_id=%s, acces_id=%s]",
					pProjectId, pUserId, pAccessId), e);
		}
		return returnAccess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean mc_management_delete(final MantisARDSoapConnector pConnector, final BigInteger pProjectId)
			throws MantisARDSoapException {
		boolean returnSuccess = false;
		try {
			returnSuccess = pConnector.getConnectBindingStub().mc_management_delete(pConnector.getUsername(),
					pConnector.getPassword(), pProjectId);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(String.format("Unable to delete project with [project_id=%s]",
					pProjectId), e);
		}
		return returnSuccess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger mc_project_get_id_from_name(final MantisARDSoapConnector pConnector,
			final String pProjectName) throws MantisARDSoapException {
		try {
			return pConnector.getConnectBindingStub().mc_project_get_id_from_name(pConnector.getUsername(),
					pConnector.getPassword(), pProjectName);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(String.format(
					"Unable to get project id from project name with [name=%s]", pProjectName), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountData[] mc_project_get_users(final MantisARDSoapConnector pConnector,
			final BigInteger pProjectId, final BigInteger pAccesId) throws MantisARDSoapException {
		try {
			return pConnector.getConnectBindingStub().mc_project_get_users(pConnector.getUsername(),
					pConnector.getPassword(), pProjectId, pAccesId);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(
					String.format("Unable to get project's users  with [project_id=%s, acces_id=%s]",
							pProjectId, pAccesId), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean mc_management_remove_user(final MantisARDSoapConnector pConnector,
			final BigInteger pProjectId, final BigInteger pUserId) throws MantisARDSoapException {
		boolean returnSucess = false;
		try {
			returnSucess = pConnector.getConnectBindingStub().mc_management_remove_user(
					pConnector.getUsername(), pConnector.getPassword(), pProjectId, pUserId);
		} catch (RemoteException e) {
			throw new MantisARDSoapException(String.format(
					"Unable to delete user account with [project_id=%s, user_id=%s]", pProjectId, pUserId), e);
		}
		return returnSucess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean mc_management_update(final MantisARDSoapConnector pConnector, final BigInteger pProjectId,
			final ProjectData pProjectData) throws MantisARDSoapException {
		boolean returnSucess = false;
		try {
			returnSucess = pConnector.getConnectBindingStub().mc_management_update(pConnector.getUsername(),
					pConnector.getPassword(), pProjectId, pProjectData);
		} catch (RemoteException e) {

			throw new MantisARDSoapException(String.format("Unable to update project with [id=%s]", pProjectId), e);
		}
		return returnSucess;
	}

}
