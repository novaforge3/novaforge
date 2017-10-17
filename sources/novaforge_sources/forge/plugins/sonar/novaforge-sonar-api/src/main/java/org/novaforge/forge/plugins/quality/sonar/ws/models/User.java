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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {

	private static final String SONAR_ADMIN = "sonar-administrators";
	private static final String GROUP_ADMIN_SUFFIX = "admin";
	
	private String login;
	private String password;
	private String name;
	private String email;
	private List<String> groups = new ArrayList<>();
	private List<String> scmAccounts  = new ArrayList<>();
	private boolean active;
	private boolean local;
	private int tokensCount;

	public User() {
		super();
	}
	
	public User(String login, String name, String email, List<String> groups, List<String> scmAccounts, boolean active,
			boolean local, int tokensCount) {
		super();
		this.login = login;
		this.name = name;
		this.email = email;
		this.groups = groups;
		this.scmAccounts = scmAccounts;
		this.active = active;
		this.local = local;
		this.tokensCount = tokensCount;
	}

	public void addGroup(String groupName){
		
		if(this.groups != null && !this.groups.contains(groupName)){
			
			this.groups.add(groupName);
		}
	}
	public int getTokensCount() {
		return tokensCount;
	}

	public void setTokensCount(int tokensCount) {
		this.tokensCount = tokensCount;
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public List<String> getGroups() {
		return groups;
	}

	public List<String> getGroups(String forgeProjectId) {
		
		List<String> projectGroups = new ArrayList<String>();
		
		Iterator<String> iterator = this.groups.iterator();
		
		while (iterator.hasNext()){
			
			String groupName = iterator.next();
			
			if(groupName.startsWith(forgeProjectId)){
			
				projectGroups.add(groupName);
			}
		}
		
		return projectGroups;
	}
	public List<String> getScmAccounts() {
		return scmAccounts;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isLocal() {
		return local;
	}

	public String getPassword() {
		return password;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public void setScmAccounts(List<String> scmAccounts) {
		this.scmAccounts = scmAccounts;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}
	
	public boolean isSonarAdmin() {
		
		boolean ret = false;
		
		for (final String group : groups) {
			
			if (SONAR_ADMIN.equals(group)) {
				
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean isProjectAdmin(String forgeProjectID) {
		
		boolean ret = false;
		
		for (final String group : groups) {
			
			if (group.startsWith(forgeProjectID) && group.endsWith(GROUP_ADMIN_SUFFIX)) {
				
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean isProjectUser(String forgeProjectID) {
		
		boolean ret = false;
		
		for (final String group : groups) {
			
			if (group.startsWith(forgeProjectID)) {
				
				ret = true;
				break;
			}
		}
		return ret;
	}
}
