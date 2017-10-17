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
package org.novaforge.forge.commons.log.internal;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.log.exceptions.LogTechnicalException;
import org.novaforge.forge.commons.log.models.ServerConfiguration;
import org.novaforge.forge.commons.log.services.LogTechnicalService;

import java.util.ArrayList;
import java.util.List;

public class LogTechnicalServiceImpl implements LogTechnicalService
{
	/**
	 * Logger instanciation
	 */
	private static final Log	log				= LogFactory.getLog(LogTechnicalServiceImpl.class);
	private static String			JSON_NAME	= "name";
	private static String			JSON_URL	= "url";
	private static String			JSON_LINE	= "linesToShow";
	private static String			JSON_MASK	= "defaultFileMask";
	private String						logDirectory;
	private String						serverProfile;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLogDirectory()
	{
		return this.logDirectory;
	}

	/**
	 * @param pLogDirectory
	 *          the logPath set by container
	 */
	public void setLogDirectory(final String pLogDirectory)
	{
		log.debug(String.format("Log directory '%s' has been changed from '%s' to '%s'.", this.getClass()
				.getName(), this.serverProfile, pLogDirectory));
		this.logDirectory = pLogDirectory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ServerConfiguration> getServerConfiguration() throws LogTechnicalException
	{
		final List<ServerConfiguration> returnList = new ArrayList<ServerConfiguration>();
		try
		{
			final JSONArray json = JSONArray.fromObject(this.serverProfile);
			for (int index = 0; index < json.size(); index++)
			{
				final JSONObject objMapping = json.getJSONObject(index);

				returnList.add(new ServerConfigurationImpl(objMapping.getString(JSON_NAME), objMapping
						.optString(JSON_URL), objMapping.optString(JSON_LINE), objMapping.optString(JSON_MASK)));

			}
		}
		catch (final JSONException e)
		{
			throw new LogTechnicalException("Can't parse JSON mapping template", e);
		}
		return returnList;
	}

	/**
	 * @param pServerProfile
	 *     the serverProfile set by container
	 */
	public void setServerProfile(final String pServerProfile)
	{
		log.debug(String.format("Server configurations '%s' has been changed from '%s' to '%s'.", this.getClass().getName(),
														this.serverProfile, pServerProfile));
		this.serverProfile = pServerProfile;

	}
}
