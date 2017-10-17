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
package org.novaforge.forge.plugins.gcl.nexus.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This enumeration declares default roles which will be created in nexus for a
 * new repository. Defines the type of the role and the associated privilege
 * list.
 * 
 * Provides a template to create new roles.
 * 
 * @author lamirang
 */
public enum NexusRolePrivilege {

	/**
	 * ANONYMOUS role : not to be displayed in Role mapping
	 * Only used to offer read access to release repositories for anonymous users
	 * repository.
	 */
  
	/**
	 * Represents administrator role which can browse, read, create, update and
	 * delete artifacts in a specific repository.
	 */
	ADMINISTRATOR {
		@Override
		public String getLabel() {
			return NexusConstant.ADMINISTRATOR_LABEL;
		}

		@Override
		public String getId() {
			return NexusConstant.ADMINISTRATOR_ID;
		}

		@Override
		public PrivilegeActionCategoryType getActionCategory() {
			return PrivilegeActionCategoryType.ADMIN;
		}

		@Override
		public Set<PrivilegeActionType> getPrivileges() {
			Set<PrivilegeActionType> privileges = new HashSet<PrivilegeActionType>();
			privileges.add(PrivilegeActionType.ACTION_WILDCARD);

			return privileges;
		}
		
		@Override
		public Set<String> getSubRoles() {

			return new HashSet<>();
		}
	},
	/**
	 * Represents observer role. It can browse and read artifacts in a specific
	 * repository.
	 */
	OBSERVER {
		@Override
		public String getLabel() {
			return NexusConstant.OBSERVER_LABEL;
		}

		@Override
		public String getId() {
			return NexusConstant.OBSERVER_ID;
		}

		@Override
		public PrivilegeActionCategoryType getActionCategory() {
			return PrivilegeActionCategoryType.VIEW;
		}

		@Override
		public Set<PrivilegeActionType> getPrivileges() {
			Set<PrivilegeActionType> privileges = new HashSet<PrivilegeActionType>();
			privileges.add(PrivilegeActionType.ACTION_BROWSE);
			privileges.add(PrivilegeActionType.ACTION_READ);
			return privileges;
		}
		
		@Override
		public Set<String> getSubRoles() {

			return new HashSet<>();
		}
	},
	/**
	 * Represents developer senior role. It can browse, read, create and update
	 * artifacts in a specific repository.
	 */
	DEVELOPER_SENIOR {
		@Override
		public String getLabel() {
			return NexusConstant.DEVELOPER_SENIOR_LABEL;
		}

		@Override
		public String getId() {
			return NexusConstant.DEVELOPER_SENIOR_ID;
		}

		@Override
		public PrivilegeActionCategoryType getActionCategory() {
			return PrivilegeActionCategoryType.VIEW;
		}

		@Override
		public Set<PrivilegeActionType> getPrivileges() {
			Set<PrivilegeActionType> privileges = new HashSet<PrivilegeActionType>();
			privileges.add(PrivilegeActionType.ACTION_WILDCARD);

			return privileges;
		}
		
		@Override
		public Set<String> getSubRoles() {

			return new HashSet<>();
		}
	},
	/**
	 * Represents developer senior role. It can browse, read and create
	 * artifacts in a specific repository.
	 */
	DEVELOPER {
		@Override
		public String getLabel() {
			return NexusConstant.DEVELOPER_LABEL;
		}

		@Override
		public String getId() {
			return NexusConstant.DEVELOPER_ID;
		}

		@Override
		public PrivilegeActionCategoryType getActionCategory() {
			return PrivilegeActionCategoryType.VIEW;
		}

		@Override
		public Set<PrivilegeActionType> getPrivileges() {
			Set<PrivilegeActionType> privileges = new HashSet<PrivilegeActionType>();

			privileges.add(PrivilegeActionType.ACTION_ADD);
			privileges.add(PrivilegeActionType.ACTION_BROWSE);
			privileges.add(PrivilegeActionType.ACTION_EDIT);
			privileges.add(PrivilegeActionType.ACTION_READ);

			return privileges;
		}
		
		@Override
		public Set<String> getSubRoles() {

			return new HashSet<>();
		}
	};

	/**
	 * Contains map which link a label to a specific role element
	 */
	private static final Map<String, NexusRolePrivilege> roles = new HashMap<String, NexusRolePrivilege>();

	static {
		for (NexusRolePrivilege role : values()) {
			roles.put(role.getLabel(), role);
		}
	}

	/**
	 * Return element from enumeration regarding a specific label.
	 *
	 * @param pLabel
	 *            represents the label used to search a element in the
	 *            enumeration
	 * @return specific element
	 */
	public static NexusRolePrivilege fromLabel(String pLabel) {
		return roles.get(pLabel);
	}

	/**
	 * Return the label of a role
	 *
	 * @return label of a role
	 */
	public abstract String getLabel();

	/**
	 * Return the id of a role
	 *
	 * @return id of a role
	 */
	public abstract String getId();

	/**
	 * Return the id of a role
	 *
	 * @return id of a role
	 */
	public abstract PrivilegeActionCategoryType getActionCategory();

	/**
	 * Return list of privileges associated to a role
	 *
	 * @return list of privileges
	 */
	public abstract Set<PrivilegeActionType> getPrivileges();
	
	/**
	 * Return list of sub-roles of the roles
	 */
	public abstract Set<String> getSubRoles();
}
