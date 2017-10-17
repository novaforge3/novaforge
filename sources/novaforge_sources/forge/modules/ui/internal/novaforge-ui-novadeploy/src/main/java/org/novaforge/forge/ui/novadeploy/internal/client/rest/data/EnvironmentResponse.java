package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("serial")
@JsonRootName(value="response") @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class EnvironmentResponse extends ServiceResponse implements Serializable
{
	@JsonProperty
	public CustomerEnvironment environment;

	@JsonIgnore
	public CustomerEnvironment getEnvironment()
	{
		return environment;
	}

	public void setEnvironment(CustomerEnvironment environment) {
		this.environment = environment;
	}


}
