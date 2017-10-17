package org.novaforge.forge.plugins.quality.sonar.ws.marshallers;

import org.novaforge.forge.plugins.quality.sonar.ws.models.TimeMachine;

import com.google.gson.Gson;

public class TimeMachineUnmarshaller {

	
	public static TimeMachine parse(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, TimeMachine.class);
	}
}
