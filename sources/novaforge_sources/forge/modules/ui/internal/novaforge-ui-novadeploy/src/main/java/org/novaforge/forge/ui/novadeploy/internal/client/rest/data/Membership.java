/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * 
 * This file is free software: you may redistribute and/or modify it
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the
 * License.
 * 
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have
 * received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses.
 * 
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section
 * 7.
 * 
 * If you modify this Program, or any covered work, by linking or
 * combining it with libraries listed in COPYRIGHT file at the
 * top-level directory of this distribution (or a modified version of
 * that libraries), containing parts covered by the terms of licenses
 * cited in the COPYRIGHT file, the licensors of this Program grant
 * you additional permission to convey the resulting work.
 */

package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;

public class Membership
{

	@Column(name = "USER", nullable = false)
	public String user;
	@Column(name = "PROJECT", nullable = false)
	public String project;
	@Enumerated(EnumType.STRING)
	@Column(name = "ROLE")
	public Role userRole;
	@Column(name = "ID")
	private long id;

	public Membership()
	{

	}

	public Role getUserRole()
	{
		return userRole;
	}

	public void setUserRole(Role userRole)
	{
		this.userRole = userRole;
	}

	@XmlElement(type = String.class)
	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getProject()
	{
		return project;
	}

	public void setProject(String project)
	{
		this.project = project;
	}

	public String toString()
	{

		return "Membership of user " + this.user + " linked to project :" + project + " with role:" + this.userRole;

	}

}
