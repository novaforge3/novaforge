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
import java.util.Iterator;
import java.util.Map;

/**
 * This enumeration declares default roles which are used on Sonar
 * 
 * @author Guillaume Lamirand
 */
public enum SonarRole
{

	/**
	 * Represents Code viewers role.
	 */
	CODE_VIEWERS
	{
		@Override
		public String getLabel()
		{
			return "Code viewers";
		}

		@Override
		public String getId()
		{
			return "codeviewer";
		}
	},
	/**
	 * Represents user role.
	 */
	USERS
	{
		@Override
		public String getLabel()
		{
			return "Users";
		}

		@Override
		public String getId()
		{
			return "user";
		}
	},
	/**
	 * Represents administrator role.
	 */
	ADMINISTRATORS
	{
		@Override
		public String getLabel()
		{
			return "Administrators";
		}

		@Override
		public String getId()
		{
			return "admin";
		}
	};

//	/**
//	 * Represents issue administrator role.
//	 */
//	ISSUE_ADMINISTRATORS
//	{
//		@Override
//		public String getLabel()
//		{
//			return "Issue administrators";
//		}
//
//		@Override
//		public String getId()
//		{
//			return "issueadmin";
//		}
//	},
//	/**
//	 * Represents issue administrator role.
//	 */
//	ANALYSIS_EXECUTORS
//	{
//		@Override
//		public String getLabel()
//		{
//			return "Analysis Executors";
//		}
//
//		@Override
//		public String getId()
//		{
//			return "scan";
//		}
//	};
	/**
	 * Contains map which link a label to a specific role element
	 */
	private static final Map<String, SonarRole>	roles	= new HashMap<String, SonarRole>();

	static
	{
		for (final SonarRole role : values())
		{
			roles.put(role.getLabel(), role);
		}
	}

	/**
	 * Return element from enumeration regarding a specific label.
	 *
	 * @param pLabel
	 *           represents the label used to search a element in the enumeration
	 * @return specific element
	 */
	public static SonarRole fromLabel(final String pLabel)
	{
		return roles.get(pLabel);
	}

	/**
	 * Return element from enumeration regarding a specific id.
	 *
	 * @param id
	 *           represents the id used to search a element in the enumeration
	 * @return specific element
	 */
	public static SonarRole fromId(final String id)
	{
		SonarRole ret = null;
		Iterator<SonarRole> iterator = roles.values().iterator();
		
		while (iterator.hasNext()){
			
			SonarRole role = iterator.next();
			
			if(role.getId().equals(id)){
				
				ret = role;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * Return the label of a role
	 *
	 * @return label of a role
	 */
	public abstract String getLabel();

	/**
	 * Return the id of a privilege
	 *
	 * @return id of a role
	 */
	public abstract String getId();
}
