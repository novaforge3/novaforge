package org.novaforge.forge.ui.novadeploy.internal.client.event;

import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.EnvironmentState;

/**
 * @author Vincent Chenal
 */

public class DeleteEnvironmentEvent implements PortalEvent
{

	private String descriptorName;
	private long timeRefId;
	private EnvironmentState environmentState;

	public DeleteEnvironmentEvent(String descriptorName, long timeRefId)
	{
		super();
		this.descriptorName = descriptorName;
		this.timeRefId = timeRefId;
		this.environmentState = environmentState;
	}

	public String getDescriptorName()
	{
		return descriptorName;
	}

	public void setDescriptorName(String descriptorName)
	{
		this.descriptorName = descriptorName;
	}

	public long getTimeRefId()
	{
		return timeRefId;
	}

	public void setTimeRefId(long timeRefId)
	{
		this.timeRefId = timeRefId;
	}

	public EnvironmentState getEnvironmentState()
	{
		return environmentState;
	}

	public void setEnvironmentState(EnvironmentState environmentState)
	{
		this.environmentState = environmentState;
	}
}
