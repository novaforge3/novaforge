package org.novaforge.forge.ui.novadeploy.internal.client.event;

import org.novaforge.forge.portal.events.PortalEvent;

/**
 * @author Vincent Chenal
 */

public class EditDescriptorEvent implements PortalEvent
{
	private String descriptorName;

	public EditDescriptorEvent(String descriptorName)
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
