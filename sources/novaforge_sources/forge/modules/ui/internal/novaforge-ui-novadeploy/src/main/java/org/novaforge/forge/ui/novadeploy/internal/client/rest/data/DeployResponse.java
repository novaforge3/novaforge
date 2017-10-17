package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("serial")
@JsonRootName(value = "response")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DeployResponse extends ServiceResponse implements Serializable
{

	@JsonProperty
	public long timeReferenceID;

	public void setTimeReferenceID(long timeReferenceID)
	{
		this.timeReferenceID = timeReferenceID;
	}

	public long getTimeReferenceID()
	{
		return timeReferenceID;
	}

}