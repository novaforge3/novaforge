package org.novaforge.forge.ui.novadeploy.internal.client.event;

import org.novaforge.forge.portal.events.PortalEvent;

/**
 * @author Vincent Chenal
 */

public class ReleaseEnvironmentEvent implements PortalEvent
{
	private String descriptorName;
	private long timeRefId;

	public ReleaseEnvironmentEvent(String descriptorName, long timeRefId)
	{
		super();
		this.descriptorName = descriptorName;
		this.timeRefId = timeRefId;
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

}
