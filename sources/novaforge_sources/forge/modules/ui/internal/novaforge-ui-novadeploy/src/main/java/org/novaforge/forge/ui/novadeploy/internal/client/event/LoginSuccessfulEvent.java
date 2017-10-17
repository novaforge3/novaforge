/**
 * NovaForge(TM) is a web-based forge offering a Collaborative
 * Development and Project Management Environment.
 *
 * Copyright (C) 2007-2012 BULL SAS
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.ui.novadeploy.internal.client.event;

import org.novaforge.forge.portal.events.PortalEvent;

/**
* @author Vincent Chenal
*/
public class LoginSuccessfulEvent implements PortalEvent
{
	private String[] vdcs;
	private String[] networks;

	public LoginSuccessfulEvent(String[] cloudName, String[] login)
	{
		super();
		this.vdcs = cloudName;
		this.networks = login;
	}

	public String[] getVdcs()
	{
		return vdcs;
	}

	public void setVdcs(String[] vdcs)
	{
		this.vdcs = vdcs;
	}

	public String[] getNetworks()
	{
		return networks;
	}

	public void setNetworks(String[] networks)
	{
		this.networks = networks;
	}
}
