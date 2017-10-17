package org.novaforge.forge.plugins.quality.sonar.ws.models;

import java.util.List;

public class Projects {
	
	private List<Project> projects;

	public Projects(List<Project> projects) {
		super();
		this.projects = projects;
	}

	public List<Project> getProjects() {
		return projects;
	}

}
