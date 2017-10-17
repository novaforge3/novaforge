package org.novaforge.forge.plugins.gcl.nexus.client.internal;

public enum ScriptOperation {
	
	DELETE_REPOSITORY("repository.delete"),
	EXISTS_REPOSITORY("repository.exists"),
	GET_REPOSITORY("repository.get"),
	CREATE_BOWER_HOSTED("repository.createBowerHosted"),
	CREATE_DOCKER_HOSTED("repository.createDockerHosted"),
	CREATE_MAVEN_HOSTED("repository.createMavenHosted"),
	CREATE_NPM_HOSTED("repository.createNpmHosted"),
	CREATE_NUGGET_HOSTED("repository.createNugetHosted"),
	CREATE_PYPI_HOSTED("repository.createPyPiHosted"),
	CREATE_RAW_HOSTED("repository.createRawHosted"),
	CREATE_RUBYGEMS_HOSTED("repository.createRubygemsHosted"),
	ADD_ROLE("security.addRole"),
	ADD_USER("security.addUser"),
	LIST_ROLES("security.listRoles"),
	UPDATE_REPOSITORY("repository.updateRepository"),
	UPDATE_USER("security.updateUser"),
	EXIST_USER("security.existsUser"),
	GET_USER("security.getUser"),
	DELETE_ROLE("security.deleteRole"),
	DELETE_USER("security.deleteUser"),
	CHANGE_PASSWORD("security.changePassword"),
	SET_ANONYMOUS_ACCESS("security.setAnonymousAccess"),
	SET_USER_ROLES("security.setUserRoles"),
	DUMMY("dummy");

	private String scriptName = null;

	private ScriptOperation(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getScriptName() {
		return scriptName;
	}
	
	public static ScriptOperation getScriptOperation(String scriptName) {

	
		for (ScriptOperation value : values()) {

			if (scriptName != null && value.getScriptName().equalsIgnoreCase(scriptName)) {
				return value;
			}
		}
		throw new IllegalArgumentException("ScriptOperation where scriptName = " + scriptName + " does not exist." );
	}
}
