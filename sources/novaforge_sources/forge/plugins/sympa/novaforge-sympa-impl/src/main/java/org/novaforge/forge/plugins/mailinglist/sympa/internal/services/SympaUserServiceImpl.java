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
package org.novaforge.forge.plugins.mailinglist.sympa.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapClient;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapException;
import org.novaforge.forge.plugins.mailinglist.sympa.dao.UserDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.UserEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.model.User;
import org.novaforge.forge.plugins.mailinglist.sympa.services.SympaConfigurationService;
import org.novaforge.forge.plugins.mailinglist.sympa.utils.Utils;

import java.net.URL;

/**
 * @author sbenoist
 */
public class SympaUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{

	private SympaSoapClient					 sympaSoapClient;

	private UserDAO									 userDAO;

	private SympaConfigurationService sympaConfigurationService;

	/**
	 * @inheritDoc
	 */
	@Override
	public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pPluginUser)
			throws PluginServiceException
	{
		try
		{
			SympaSoapConnector connector = getConnector(pInstance);

			// we check that the superadmin exists in sympa application
			if (sympaSoapClient.isUser(connector, pPluginUser.getEmail()))
			{
				sympaSoapClient.updateUser(connector, pPluginUser.getEmail(),
						Utils.buildGecos(pPluginUser.getFirstName(), pPluginUser.getName()), pPluginUser.getPassword(),
						Utils.toSympaLanguage(pPluginUser.getLanguage()));
			}
			else
			{
				sympaSoapClient.createUser(connector, pPluginUser.getEmail(),
						Utils.buildGecos(pPluginUser.getFirstName(), pPluginUser.getName()), pPluginUser.getPassword(),
						Utils.toSympaLanguage(pPluginUser.getLanguage()));
			}

			if (!userDAO.existUser(pPluginUser.getLogin()))
			{
				userDAO.persist(new UserEntity(pPluginUser.getLogin(), pPluginUser.getEmail()));
			}
		}
		catch (SympaSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to add sympa administrator with login=%s",
					pPluginUser.getLogin()), e);
		}
	}

	private SympaSoapConnector getConnector(final ToolInstance pToolInstance)
			throws SympaSoapException, PluginServiceException
	{
		// get the baseURL for the connector
		URL baseURL = pToolInstance.getBaseURL();

		// get the connector

		return sympaSoapClient.getConnector(sympaConfigurationService.getClientURL(baseURL),
																																sympaConfigurationService.getClientAdmin(),
																																sympaConfigurationService.getClientPwd(),
																																baseURL.getHost(),
																																sympaConfigurationService.getListmaster());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
			throws PluginServiceException
	{
		boolean success = false;
		try
		{
			if (userDAO.existUser(pUser.getLogin()))
			{
				SympaSoapConnector connector = getConnector(pInstance.getToolInstance());
				success = sympaSoapClient.deleteUser(connector, pUser.getEmail());

				// delete the stored user in plugin side
				User persistentUser = userDAO.findByLogin(pUser.getLogin());
				userDAO.delete(persistentUser);
			}
		}
		catch (SympaSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
																										 pInstance.toString(), pUser.toString()), e);
		}
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
			final PluginUser pPluginUser) throws PluginServiceException
	{
		boolean success = false;
		try
		{
			if (userDAO.existUser(pPluginUser.getLogin()))
			{
				SympaSoapConnector connector = getConnector(pInstance.getToolInstance());

				// first, chek if the email has been updated
				checkEmail(connector, pPluginUser.getLogin(), pPluginUser.getEmail());
				success = sympaSoapClient.updateUser(connector, pPluginUser.getEmail(),
						Utils.buildGecos(pPluginUser.getFirstName(), pPluginUser.getName()), pPluginUser.getPassword(),
						Utils.toSympaLanguage(pPluginUser.getLanguage()));
			}
		}
		catch (SympaSoapException e)
		{
			throw new PluginServiceException(String.format(
					"Unable to update user with instance=%s, username=%s and User=%s", pInstance.toString(), pUserName,
					pPluginUser.toString()), e);

		}
		return success;
	}

	/**
	 * This method checks if the email has been updated and change it in the tool application
	 *
	 * @param pLogin
	 * @param pEmail
	 * @throws PluginServiceException
	 */
	private void checkEmail(final SympaSoapConnector connector, final String pLogin, final String pEmail)
			throws PluginServiceException
	{
		String storedEmail = null;
		try
		{
			User user = userDAO.findByLogin(pLogin);
			storedEmail = user.getEmail();
			if (!storedEmail.equals(pEmail))
			{
				sympaSoapClient.updateUserEmail(connector, storedEmail, pEmail);

				// save the new email
				user.setEmail(pEmail);
				userDAO.update(user);
			}
		}
		catch (SympaSoapException e)
		{
			throw new PluginServiceException(String.format(
					"Unable to update tool user email with login=%s, old email=%s, new email=%s", pLogin, storedEmail,
					pEmail), e);
		}
	}

	public void setSympaSoapClient(final SympaSoapClient pSympaSoapClient)
	{
		sympaSoapClient = pSympaSoapClient;
	}

	public void setUserDAO(final UserDAO pUserDAO)
	{
		userDAO = pUserDAO;
	}

	public void setSympaConfigurationService(final SympaConfigurationService pSympaConfigurationService)
	{
		sympaConfigurationService = pSympaConfigurationService;
	}

}
