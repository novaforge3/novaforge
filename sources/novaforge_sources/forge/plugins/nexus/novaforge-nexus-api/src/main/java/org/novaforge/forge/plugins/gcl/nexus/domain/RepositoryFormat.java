package org.novaforge.forge.plugins.gcl.nexus.domain;

public enum RepositoryFormat {

	BOWER("bower"),
	DOCKER("docker"),
	MAVEN2("maven2"),
	NPM("npm"),
	NUGET("nuget"),
	PYPI("pypi"),
	RAW("raw"),
	RUBYGEMS("rubygems");

	private String value;

	private RepositoryFormat(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static RepositoryFormat getFormat(String value) {

		for (RepositoryFormat repositoryFormat : values()) {

			if (value != null && repositoryFormat.getValue().equalsIgnoreCase(value)) {
				return repositoryFormat;
			}
		}
		throw new IllegalArgumentException("RepositoryFormat where value = " + value + " does not exist.");

	}
}
