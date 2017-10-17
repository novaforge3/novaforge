package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

public enum EnvironmentState
{
	CHECK, PROVISIONING, CONFIGURATION, LIFECYCLE, DEPLOYED, FAILED;

	public static EnvironmentState fromString(String phase)
	{
		switch (phase)
		{
		case "CHECK":
			return EnvironmentState.CHECK;
		case "PROVISIONING":
			return EnvironmentState.PROVISIONING;
		case "CONFIGURATION":
			return EnvironmentState.CONFIGURATION;
		case "LIFECYCLE":
			return EnvironmentState.LIFECYCLE;
		case "DEPLOYED":
			return EnvironmentState.DEPLOYED;
		case "FAILED":
			return EnvironmentState.FAILED;
		}

		return null;
	}
}
