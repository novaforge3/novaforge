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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public abstract class MonitoringAsyncCallback<T> implements AsyncCallback<T>
{

	public MonitoringAsyncCallback()
	{
		DOM.getElementById("request-monitor").getStyle().setProperty("visibility", "visible");
	}

	@Override
	public final void onFailure(final Throwable caught)
	{
		DOM.getElementById("request-monitor").getStyle().setProperty("visibility", "hidden");
		new ErrorPopup("Exception", caught.getMessage()).showAndCenter();
	}

	@Override
	public final void onSuccess(final T result)
	{
		DOM.getElementById("request-monitor").getStyle().setProperty("visibility", "hidden");
		DOM.getElementById("process-monitor").getStyle().setProperty("visibility", "visible");
		try
		{
			onSuccessInternal(result);
		}
		finally
		{
			DOM.getElementById("process-monitor").getStyle().setProperty("visibility", "hidden");
		}
	}

	public abstract void onSuccessInternal(T result);

	public static class ErrorPopup extends PopupPanel
	{
		public ErrorPopup(final String message, final String details)
		{
			super();
			setWidget(new HTML(message + "</br>" + details));
			addStyleName("gwt-logger-error-panel");
			setModal(true);
			setAutoHideEnabled(true);
		}

		public void showAndCenter()
		{
			final int maxWidth = (int) (Window.getClientWidth() * 0.8);
			final int maxHeight = (int) (Window.getClientHeight() * 0.8);
			super.show();
			if (getOffsetWidth() > maxWidth)
			{
				setWidth(maxWidth + "px");
			}
			if (getOffsetHeight() > maxHeight)
			{
				setHeight(maxHeight + "px");
			}
			super.center();
		}
	}
}
