package org.novaforge.forge.plugins.quality.sonar.ws.models;

public class Project {
	

	private String uuid;
	private String key;
	private String name;
	private String creationDate;
	
	public Project(String uuid, String key, String name, String creationDate) {
		super();
		this.uuid = uuid;
		this.key = key;
		this.name = name;
		this.creationDate = creationDate;
	}
	
	public String getUuid() {
		return uuid;
	}
	public String getKey() {
		return key;
	}
	public String getName() {
		return name;
	}
	public String getCreationDate() {
		return creationDate;
	}	

}
