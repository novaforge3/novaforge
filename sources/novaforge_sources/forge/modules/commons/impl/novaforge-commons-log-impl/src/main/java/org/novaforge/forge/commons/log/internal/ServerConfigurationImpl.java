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

import org.novaforge.forge.commons.log.models.ServerConfiguration;

/**
 * The class describes a configuration server used by {@link LogTechnicalServiceImpl}
 * 
 * @author Guillaume Lamirand
 */
public class ServerConfigurationImpl implements ServerConfiguration
{
	private String	name;
	private String	url;
	private String	maxLine;
	private String	mask;

	/**
	 * Default constructor
	 * 
	 * @param pName
	 *          server name
	 * @param pUrl
	 *          server url
	 * @param pMaxLine
	 *          server max line
	 * @param pMask
	 *          server mask
	 */
	public ServerConfigurationImpl(final String pName, final String pUrl, final String pMaxLine,
			final String pMask)
	{
		name = pName;
		url = pUrl;
		maxLine = pMaxLine;
		mask = pMask;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(final String pName)
	{
		name = pName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl()
	{
		return url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUrl(final String pUrl)
	{
		url = pUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMaxDisplayLine()
	{
		return maxLine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMaxDisplayLine(final String pNumber)
	{
		maxLine = pNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultMask()
	{
		return mask;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultMask(String pDefaultMask)
	{
		mask = pDefaultMask;
	}

}
