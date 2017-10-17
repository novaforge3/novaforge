package org.novaforge.forge.plugins.gcl.nexus.domain;

public enum PrivilegeActionCategoryType {
	
	ADMIN("admin"),
	VIEW("view");

	private String value;
	
	private PrivilegeActionCategoryType(String value){
		
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
