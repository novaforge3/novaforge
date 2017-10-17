package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

import java.util.ArrayList;
import java.util.List;

public class RoleParam {


	private String id;
	private String sourceId;
	private String name;
	private String description;
	private List<String> privilegeIds = new ArrayList<>();
	private List<String> roleIds = new ArrayList<>();

	public RoleParam() {
		super();
	}
	
	public RoleParam(String id, String sourceId) {
		super();
		this.id = id;
		this.sourceId = sourceId;
	}
	
	public RoleParam(String id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public void addRole(String roleId) {

		if (!this.roleIds.contains(roleId)) {

			this.roleIds.add(roleId);
		}
	}

	public void addPrivilege(String privilege) {

		if (!this.privilegeIds.contains(privilege)) {

			this.privilegeIds.add(privilege);
		}
	}

	public void removeRole(String roleId) {

		if (this.roleIds.contains(roleId)) {

			this.roleIds.remove(roleId);
		}
	}

	public void removePrivilege(String privilege) {

		if (this.privilegeIds.contains(privilege)) {

			this.privilegeIds.remove(privilege);
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getPrivilegeIds() {
		return privilegeIds;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrivilegeIds(List<String> privileges) {
		this.privilegeIds = privileges;
	}

	public void setRoleIds(List<String> roles) {
		this.roleIds = roles;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public String toString() {
		return "RoleParam [id=" + id + ", sourceId=" + sourceId + ", name=" + name + ", description=" + description
				+ ", privilegeIds=" + privilegeIds + ", roleIds=" + roleIds + "]";
	}
}
