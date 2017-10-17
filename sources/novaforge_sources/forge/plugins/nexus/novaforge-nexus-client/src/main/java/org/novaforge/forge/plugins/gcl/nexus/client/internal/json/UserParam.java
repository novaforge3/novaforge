package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

import java.util.ArrayList;
import java.util.List;

public class UserParam {



	private String id;
	private String sourceId;
	private String firstName;
	private String lastName;
	private String email;
	private boolean active;
	private String password;
	private List<String> roleIds = new ArrayList<>();

	public UserParam() {
		super();
	}
	
	/**
	 * 
	 * @param id
	 * @param sourceId
	 */
	public UserParam(String id, String sourceId) {
		super();
		this.id = id;
		this.sourceId = sourceId;
	}
	/**
	 * 
	 * @param id	the user ID
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param active
	 * @param password
	 */
	public UserParam(String id, String firstName, String lastName, String email, boolean active, String password) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.active = active;
		this.password = password;
	}

	public void addRoleId(String roleId){
		
		if(!this.roleIds.contains(roleId)){
			
			this.roleIds.add(roleId);
		}
	}
	
	public void removeRoleId(String roleId){
		
		if(this.roleIds.contains(roleId)){
			
			this.roleIds.remove(roleId);
		}
	}
	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public boolean isActive() {
		return active;
	}

	public String getPassword() {
		return password;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public String toString() {
		return "UserParam [id=" + id + ", sourceId=" + sourceId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", active=" + active + ", password=" + "********" + ", roleIds=" + roleIds + "]";
	}

}
