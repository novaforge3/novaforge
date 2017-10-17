package org.novaforge.forge.plugins.gcl.nexus.client.internal;

public class AbstractNexusManager {
	
	protected final static String DEFAULT_SOURCE = "default";

	// URL base of the REST url
	protected String urlBase;

	protected String adminUser;

	protected String adminPassword;
	
	protected void createScriptIfDoesNotExist(ScriptOperation scriptOperation) {

		ScriptManager.getInstance(this.urlBase).createScriptIfDoesNotExist(adminUser, adminPassword, scriptOperation);
	}
}
