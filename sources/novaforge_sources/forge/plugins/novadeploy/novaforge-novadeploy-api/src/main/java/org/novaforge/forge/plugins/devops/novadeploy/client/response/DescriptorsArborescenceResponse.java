package org.novaforge.forge.plugins.devops.novadeploy.client.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.CustomerDescriptor;


@SuppressWarnings("serial")
@JsonRootName(value="response") @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DescriptorsArborescenceResponse extends StatusResponse
{
	@JsonProperty
	public CustomerDescriptor[] descriptors;
	
	@JsonIgnore
	public CustomerDescriptor[] getArborescence()
	{
		return descriptors;
	}

	public void setDescriptors(List<CustomerDescriptor> descriptors)
	{
		this.descriptors = new CustomerDescriptor[descriptors.size()];
		descriptors.toArray(this.descriptors);
	}

}
