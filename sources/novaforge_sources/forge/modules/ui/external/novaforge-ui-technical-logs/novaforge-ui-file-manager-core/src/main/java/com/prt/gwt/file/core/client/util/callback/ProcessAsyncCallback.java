/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package com.prt.gwt.file.core.client.util.callback;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.prt.gwt.file.core.client.util.callback.MonitoringAsyncCallback.ErrorPopup;

public abstract class ProcessAsyncCallback<T> implements AsyncCallback<T>
{

	private final String text;

	public ProcessAsyncCallback(final String text)
	{
		this.text = text;
	}

	public AsyncCallback<T> start()
	{
		DOM.getElementById("tail-monitor").setClassName("gwt-logger-plugin-tool tool-active");
		return this;
	}

	public void activate()
	{
		final Element monitorElement = DOM.getElementById("tail-monitor");
		monitorElement.setInnerText(text);
		monitorElement.setClassName("gwt-logger-plugin-tool tool-passive");
		monitorElement.getStyle().setProperty("visibility", "visible");
	}

	@Override
	public final void onFailure(final Throwable caught)
	{
		passivate();
		new ErrorPopup("Exception", caught.getMessage()).showAndCenter();
	}

	public void passivate()
	{
		DOM.setStyleAttribute(DOM.getElementById("tail-monitor"), "visibility", "hidden");
	}

	@Override
	public final void onSuccess(final T result)
	{
		try
		{
			onSuccessInternal(result);
			stop();
		}
		catch (final Exception e)
		{
			passivate();
			new ErrorPopup("Exception", e.getMessage()).showAndCenter();
		}
	}

	public abstract void onSuccessInternal(T result);

	public AsyncCallback<T> stop()
	{
		DOM.getElementById("tail-monitor").setClassName("gwt-logger-plugin-tool tool-passive");
		return this;
	}
}
