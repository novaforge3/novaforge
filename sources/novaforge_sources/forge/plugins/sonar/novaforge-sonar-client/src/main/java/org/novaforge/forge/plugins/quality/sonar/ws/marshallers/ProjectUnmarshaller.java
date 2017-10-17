package org.novaforge.forge.plugins.quality.sonar.ws.marshallers;

import org.novaforge.forge.plugins.quality.sonar.ws.models.Projects;

import com.google.gson.Gson;

public class ProjectUnmarshaller {

	public static Projects parse(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Projects.class);
	}

}
