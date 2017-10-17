package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

import java.util.ArrayList;
import java.util.List;

public class UserRoleParam {

	private String userId;
	private List<String> roleIds = new ArrayList<>();

	public UserRoleParam(String userId) {
		super();
		this.userId = userId;
	}

	public void addRoleId(String roleId) {

		if (!this.roleIds.contains(roleId)) {

			this.roleIds.add(roleId);
		}
	}

	public void removeRoleId(String roleId) {

		if (this.roleIds.contains(roleId)) {

			this.roleIds.remove(roleId);
		}
	}

	public String getUserId() {
		return userId;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	@Override
	public String toString() {
		return "UserRoleParam [userId=" + userId + ", roleIds=" + roleIds + "]";
	}
}
