package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("serial")
@JsonRootName(value = "response")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrgResourcesResponse extends ServiceResponse implements Serializable
{
	@JsonProperty
	private OrgResources orgResources;

	@JsonIgnore
	public OrgResources getOrgResources()
	{
		return orgResources;
	}

	public void setOrgResources(OrgResources orgResources)
	{
		this.orgResources = orgResources;
	}
}
