package org.novaforge.forge.plugins.devops.novadeploy.client.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.novaforge.forge.plugins.devops.novadeploy.client.data.CustomerDescriptor;

@SuppressWarnings("serial")
@XmlRootElement(name = "response")
public class DescriptorsResponse extends StatusResponse
{
	@XmlElement
	public CustomerDescriptor[] descriptors;

	public CustomerDescriptor[] getEnvironments()
	{
		return descriptors;
	}

	public void setDescriptors(List<CustomerDescriptor> descriptors)
	{
		this.descriptors = new CustomerDescriptor[descriptors.size()];
		descriptors.toArray(this.descriptors);
	}

}
