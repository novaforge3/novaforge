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
package org.novaforge.forge.ui.forge.reference.shared;

import java.io.Serializable;
import java.util.Set;

/**
 * @author lamirang
 */
public class ApplicationRoleMappingDTO implements Serializable
{

	/**
    * 
    */
	private static final long serialVersionUID = -8101045771709997643L;
	private String            applicationName;
	private String            uri;
	private boolean           state;
	private String            roleName;
	private Set<String>       roleList;

	public ApplicationRoleMappingDTO()
	{
		// For serialization
	}

	/**
	 * @param pUri
	 * @param pState
	 */
	public ApplicationRoleMappingDTO(final String pApplicationName, final String pUri, final boolean pState)
	{
		applicationName = pApplicationName;
		uri = pUri;
		state = pState;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName()
	{
		return applicationName;
	}

	/**
	 * @param pApplicationName
	 *          the applicationName to set
	 */
	public void setApplicationName(final String pApplicationName)
	{
		applicationName = pApplicationName;
	}

	/**
	 * @return the uri
	 */
	public String getUri()
	{
		return uri;
	}

	/**
	 * @param pUri
	 *          the uri to set
	 */
	public void setUri(final String pUri)
	{
		uri = pUri;
	}

	/**
	 * @return the state
	 */
	public boolean isState()
	{
		return state;
	}

	/**
	 * @param pState
	 *          the state to set
	 */
	public void setState(final boolean pState)
	{
		state = pState;
	}

	/**
	 * @return the rolename
	 */
	public String getRoleName()
	{
		return roleName;
	}

	/**
	 * @param rolename
	 *          the rolename to set
	 */
	public void setRoleName(final String rolename)
	{
		roleName = rolename;
	}

	/**
	 * @return the rolelist
	 */
	public Set<String> getRolelist()
	{
		return roleList;
	}

	/**
	 * @param pRoleList
	 *          the rolelist to set
	 */
	public void setRolelist(final Set<String> pRoleList)
	{
		roleList = pRoleList;
	}

}
