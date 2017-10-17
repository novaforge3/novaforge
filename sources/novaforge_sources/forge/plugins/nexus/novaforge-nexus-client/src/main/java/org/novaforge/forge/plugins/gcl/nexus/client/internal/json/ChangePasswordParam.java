package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class ChangePasswordParam {

	private String userId;
	private String newPassword;
	
	public ChangePasswordParam(String userId, String newPassword) {
		super();
		this.userId = userId;
		this.newPassword = newPassword;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ChangePasswordParam [userId=" + userId + ", newPassword=" + "*******"	+ "]";
	}

}
