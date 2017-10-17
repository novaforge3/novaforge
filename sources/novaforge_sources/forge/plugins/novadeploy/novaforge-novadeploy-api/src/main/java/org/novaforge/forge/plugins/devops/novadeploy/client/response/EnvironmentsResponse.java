package org.novaforge.forge.plugins.devops.novadeploy.client.response;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.CustomerEnvironment;

@SuppressWarnings("serial")
@JsonRootName(value = "response")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EnvironmentsResponse extends ServiceResponse implements Serializable
{
	@JsonProperty
	private CustomerEnvironment[] environments;

	@JsonIgnore
	public CustomerEnvironment[] getEnvironments()
	{
		return environments;
	}

	public void setEnvironments(List<CustomerEnvironment> environments)
	{
		this.environments = new CustomerEnvironment[environments.size()];
		environments.toArray(this.environments);
	}
}
