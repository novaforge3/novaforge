package org.novaforge.forge.plugins.gcl.nexus.domain;

public enum RepositoryType {

	PROXY("proxy"), HOSTED("hosted"), GROUP("group");

	private RepositoryType(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public static RepositoryType getType(String value) {

		for (RepositoryType repositoryType : values()) {

			if (value != null && repositoryType.getValue().equalsIgnoreCase(value)) {
				return repositoryType;
			}
		}
		throw new IllegalArgumentException("RepositoryType where value = " + value + " does not exist.");

	}
}
