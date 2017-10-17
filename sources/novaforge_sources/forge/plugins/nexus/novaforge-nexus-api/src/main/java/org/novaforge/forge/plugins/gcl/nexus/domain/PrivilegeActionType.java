package org.novaforge.forge.plugins.gcl.nexus.domain;

/**
 * 
 * @author s241664
 *
 */
public enum PrivilegeActionType {
	
	ACTION_WILDCARD("*"),
	ACTION_ADD("add"),
	ACTION_BROWSE("browse"),
	ACTION_DELETE("delete"),
	ACTION_EDIT("edit"),
	ACTION_READ("read");

	private String value;
	
	private PrivilegeActionType(String value) {
		
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
