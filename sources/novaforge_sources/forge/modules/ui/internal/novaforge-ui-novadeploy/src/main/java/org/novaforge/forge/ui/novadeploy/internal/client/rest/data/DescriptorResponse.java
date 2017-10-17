package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;




@SuppressWarnings("serial")
@JsonRootName(value="response") @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DescriptorResponse extends StatusResponse
{
	@JsonProperty
	public CustomerDescriptor descriptor;
	@JsonProperty
	public String content;

	public CustomerDescriptor getDescriptor()
	{
		return descriptor;
	}

	public void setDescriptor(CustomerDescriptor descriptor)
	{
		this.descriptor = descriptor;
	}


	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

}
