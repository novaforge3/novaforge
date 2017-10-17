package org.novaforge.forge.ui.novadeploy.internal.client.event;

import org.novaforge.forge.portal.events.PortalEvent;

/**
 * @author Vincent Chenal
 */

public class CreateDescriptorEvent implements PortalEvent
{
	private String descriptorName;

	public CreateDescriptorEvent(String descriptorName)
	{
		this.descriptorName = descriptorName;
	}

	public String getDescriptorName()
	{
		return descriptorName;
	}

	public void setDescriptorName(String descriptorName)
	{
		this.descriptorName = descriptorName;
	}
}
