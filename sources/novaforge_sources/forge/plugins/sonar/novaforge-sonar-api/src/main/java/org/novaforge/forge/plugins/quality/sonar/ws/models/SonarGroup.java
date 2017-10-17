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
package org.novaforge.forge.plugins.quality.sonar.ws.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration declares default roles which are used on Sonar
 * 
 * @author lequere-g
 */
public enum SonarGroup
{
	
	ANYONE
	{
		@Override
		public String getDescription()
		{
			return "Anybody (authenticated or not) who browses the application belongs to this group";
		}

		@Override
		public String getId()
		{
			return "Anyone";
		}
	},
	/**
	 * Represents user role.
	 */
	USERS
	{
		@Override
		public String getDescription()
		{
			return "Any new users created will automatically join this group";
		}

		@Override
		public String getId()
		{
			return "sonar-users";
		}
	},
	ADMINISTRATORS
	{
		
		@Override
		public String getDescription()
		{
			return "System administrators";
		}

		@Override
		public String getId()
		{
			return "sonar-administrators";
		}
	};
	/**
	 * Contains map which link an id to a specific group element
	 */
	private static final Map<String, SonarGroup> groups = new HashMap<String, SonarGroup>();

	static
	{
		for (final SonarGroup group : values())
		{
			groups.put(group.getId(), group);
		}
	}



	/**
	 * Return the description of a group
	 *
	 * @return description of a groupe
	 */
	public abstract String getDescription();

	/**
	 * Return the id of a group
	 *
	 * @return id of a group
	 */
	public abstract String getId();
}
