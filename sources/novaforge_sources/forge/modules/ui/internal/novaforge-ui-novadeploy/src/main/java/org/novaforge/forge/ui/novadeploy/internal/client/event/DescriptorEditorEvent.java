package org.novaforge.forge.ui.novadeploy.internal.client.event;

import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentFrom;

/**
 * @author Vincent Chenal
 */

public class DescriptorEditorEvent implements PortalEvent
{
	private String descriptorWindowMode;
	private String descriptorName;
	private int descriptorVersion;
	private EnvironmentFrom environmentFrom;

	public DescriptorEditorEvent(String descriptorWindowMode, String descriptorName, int descriptorVersion,
			EnvironmentFrom environmentFrom)
	{
		this.descriptorWindowMode = descriptorWindowMode;
		this.descriptorName = descriptorName;
		this.descriptorVersion = descriptorVersion;
		this.environmentFrom = environmentFrom;
	}

	public String getDescriptorWindowMode()
	{
		return descriptorWindowMode;
	}

	public void setDescriptorWindowMode(String descriptorWindowMode)
	{
		this.descriptorWindowMode = descriptorWindowMode;
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

	public EnvironmentFrom getEnvironmentFrom()
	{
		return environmentFrom;
	}

	public void setEnvironmentFrom(EnvironmentFrom environmentFrom)
	{
		this.environmentFrom = environmentFrom;
	}

}
