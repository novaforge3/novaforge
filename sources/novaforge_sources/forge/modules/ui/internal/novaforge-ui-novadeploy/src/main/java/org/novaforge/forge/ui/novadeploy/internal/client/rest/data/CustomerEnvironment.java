package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonRootName(value = "environment")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CustomerEnvironment
{

	public long id;

	public EnvironmentState environmentState;

	public String descriptorName;

	@JsonProperty
	public long timeReferenceID;

	@JsonProperty
	public List<CustomerNode> customerNodeList;
	
	@JsonProperty
	public String user;

	public String getUser() {
		return user;
	}

	public CustomerEnvironment()
	{
	}

	public long getTimeReferenceID()
	{
		return timeReferenceID;
	}

	public EnvironmentState getEnvironmentState()
	{
		return environmentState;
	}

	public void setEnvironmentState(EnvironmentState environmentState)
	{
		this.environmentState = environmentState;
	}

	public void addNode(CustomerNode node)
	{
		if (customerNodeList == null)
		{
			customerNodeList = new ArrayList<CustomerNode>();
		}
		// node.setCustomerEnvironment(this);
		customerNodeList.add(node);
	}

	public String getDescriptorName()
	{
		return descriptorName;
	}

	public void setDescriptorName(String descriptorName)
	{
		this.descriptorName = descriptorName;
	}

	@SuppressWarnings("unchecked")
	public List<CustomerNode> getNodeList()
	{
		return Collections.unmodifiableList(customerNodeList);
	}

	public String getCanonicalName()
	{
		return descriptorName + "." + timeReferenceID;
	}

	public CustomerNode getNodeByName(String name)
	{
		CustomerNode returnedNode = null;
		for (CustomerNode node : customerNodeList)
		{
			if (node.getNodeName().equals(name))
			{
				returnedNode = node;
			}
		}
		return returnedNode;
	}

	public long getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return "CustomerEnvironment : [" + getId() + ", " + getTimeReferenceID() + ", " + getCanonicalName() + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (timeReferenceID ^ (timeReferenceID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerEnvironment other = (CustomerEnvironment) obj;
		if (timeReferenceID != other.timeReferenceID)
			return false;
		return true;
	}
}
