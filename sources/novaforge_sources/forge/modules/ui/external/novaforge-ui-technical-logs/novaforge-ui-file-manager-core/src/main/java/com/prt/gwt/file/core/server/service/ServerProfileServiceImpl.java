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
package com.prt.gwt.file.core.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.prt.gwt.file.core.client.service.ServerProfileService;
import com.prt.gwt.file.core.client.vo.ProfileVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.log.exceptions.LogTechnicalException;
import org.novaforge.forge.commons.log.models.ServerConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ServerProfileServiceImpl extends RemoteServiceServlet implements ServerProfileService
{
	/**
    * 
    */
	private static final long serialVersionUID = 1157954293363438544L;

	private static final Log  LOGGER           = LogFactory.getLog(ServerProfileServiceImpl.class);

	@Override
	public List<ProfileVO> getProfiles()
	{
		final List<ProfileVO> returnList = new ArrayList<ProfileVO>();
		try
		{
			final List<ServerConfiguration> serverConfigurations = OSGiHelper.getLogTechnicalService()
			    .getServerConfiguration();
			for (final ServerConfiguration serverConfiguration : serverConfigurations)
			{
				final ProfileVO profile = new ProfileVO();
				profile.setAppName(serverConfiguration.getName());
				profile.setAppUrl(serverConfiguration.getUrl());
				profile.setDefaultFileMask(serverConfiguration.getDefaultMask());
				if ((serverConfiguration.getMaxDisplayLine() != null) && (!"".equals(serverConfiguration.getMaxDisplayLine())))
				{
					profile.setLinesToShow(Long.parseLong(serverConfiguration.getMaxDisplayLine()));
				}
				returnList.add(profile);
			}
		}
		catch (final LogTechnicalException e)
		{
			LOGGER.error("Unable to get the servers configured", e);
			throw new SecurityException("Unable to get the servers configured", e);
		}
		return returnList;
	}

}
