package org.novaforge.forge.ui.novadeploy.internal.client.event;

import org.novaforge.forge.portal.events.PortalEvent;

/**
 * @author Vincent Chenal
 */

public class ShowLogEvent implements PortalEvent
{
	private long timeRefId;

	public ShowLogEvent(long timeRefId)
	{
		super();
		this.timeRefId = timeRefId;
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
