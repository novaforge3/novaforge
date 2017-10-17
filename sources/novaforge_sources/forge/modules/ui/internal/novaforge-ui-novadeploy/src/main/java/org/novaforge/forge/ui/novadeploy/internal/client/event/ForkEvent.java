package org.novaforge.forge.ui.novadeploy.internal.client.event;

import org.novaforge.forge.portal.events.PortalEvent;

/**
 * @author Vincent Chenal
 */

public class ForkEvent implements PortalEvent
{
	private String descriptorName;
	private int descriptorVersion;

	public ForkEvent(String descriptorName, int descriptorVersion)
	{
		super();
		this.descriptorName = descriptorName;
		this.descriptorVersion = descriptorVersion;
	}

	public String getDescriptorName()
	{
		return descriptorName;
	}

	public void setDescriptorName(String descriptorName)
	{
		this.descriptorName = descriptorName;
	}

	public int getDescriptorVersion()
	{
		return descriptorVersion;
	}

	public void setDescriptorVersion(int descriptorVersion)
	{
		this.descriptorVersion = descriptorVersion;
	}
}
