package org.novaforge.forge.plugins.gcl.nexus.domain;

public enum PublicRepository {

  // New Nexus 3 public repositories
  MAVENCENTRAL("maven-central"),
  MAVENPUBLIC("maven-public"),
  MAVENNUGETGROUP("nuget-group"),
  MAVENNUGETORGPROXY("nuget.org-proxy");

	private String value;

	private PublicRepository(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static PublicRepository getRepository(String value) {

		for (PublicRepository PublicRepository : values()) {

			if (value != null && PublicRepository.getValue().equalsIgnoreCase(value)) {
				return PublicRepository;
			}
		}
		throw new IllegalArgumentException("PublicRepository where value = " + value + " does not exist.");

	}
}
