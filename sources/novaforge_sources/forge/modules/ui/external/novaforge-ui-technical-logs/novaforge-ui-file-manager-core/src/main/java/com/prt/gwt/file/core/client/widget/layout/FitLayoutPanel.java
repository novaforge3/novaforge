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
package com.prt.gwt.file.core.client.widget.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class FitLayoutPanel extends SimplePanel
{
	private final ResizeListenerImpl impl = GWT.create(ResizeListenerImpl.class);

	public FitLayoutPanel()
	{
		super();
		setSize("100%", "100%");
	}

	public FitLayoutPanel(final Element elem)
	{
		throw new RuntimeException(" Not supported operation!");
	}

	public FitLayoutPanel(final Widget elem)
	{
		super();
		setWidget(elem);
		setSize("100%", "100%");
	}

	@Override
	public void addStyleDependentName(final String styleSuffix)
	{
		throw new RuntimeException(" Not supported operation!");
	}

	@Override
	public void addStyleName(final String style)
	{
		throw new RuntimeException(" Not supported operation!");
	}

	@Override
	public void setSize(final String width, final String height)
	{
		super.setSize(width, height);
		super.setHeight(getOffsetHeight() + "px");
		super.setWidth(getOffsetWidth() + "px");
	}

	@Override
	protected void onAttach()
	{
		super.onAttach();
		impl.onAttach(this);
	}

	@Override
	protected void onDetach()
	{
		impl.onDetach(this);
		super.onDetach();
	}

	public static class ResizeListenerImpl
	{
		protected final int wDelta        = 4;
		protected final int hDelta        = 9;
		protected int       currentHeight = 0;
		protected int       currentWidth  = 0;

		public void onAttach(final SimplePanel widget)
		{
		}

		public void onDetach(final SimplePanel widget)
		{
		}

		protected void resize(final SimplePanel widget)
		{
			if (widget.isAttached() && (widget.getWidget() != null))
			{
				final int clientHeight = DOM.getElementPropertyInt(widget.getElement(), "clientHeight");
				final int clientWidth = DOM.getElementPropertyInt(widget.getElement(), "clientWidth");
				if (((currentHeight != clientHeight) && (clientHeight > hDelta))
				    || ((currentWidth != clientWidth) && (clientWidth > wDelta)))
				{
					widget.getWidget().setSize("1px", "1px"); // force calculation of ALOWABLE size

					currentHeight = clientHeight > hDelta ? clientHeight : hDelta + 1;
					currentWidth = clientWidth > wDelta ? clientWidth : wDelta + 1;
					widget.getWidget().setHeight((currentHeight - hDelta) + "px");
					widget.getWidget().setWidth((currentWidth - wDelta) + "px");
				}
			}
		}
	}

	public static class ResizeListenerImplIE extends ResizeListenerImpl
	{
		// private Timer resizeTimer;

		@Override
		public void onAttach(final SimplePanel widget)
		{
			initResizeListener(widget, widget.getElement());
		}

		private native void initResizeListener(SimplePanel widget, Element element) /*-{
																																								var self = this;
		                                                                            element.onresize = function() {
		                                                                            if (.gwt.file.core.client.widget.layout.FitLayoutPanel.ResizeListenerImpl::currentHeight != element.offsetHeight ||
		                                                                            .gwt.file.core.client.widget.layout.FitLayoutPanel.ResizeListenerImpl::currentWidth != element.offsetWidth){
		                                                                            .gwt.file.core.client.widget.layout.FitLayoutPanel.ResizeListenerImpl::resize(Lcom/google/gwt/user/client/ui/SimplePanel;)(widget);
		                                                                            }
		                                                                            };
		                                                                            }-*/;

		@Override
		public void onDetach(final SimplePanel widget)
		{
			DOM.setElementAttribute(widget.getElement(), "onresize", null);
		}



	}

	public static class ResizeListenerImplMozilla extends ResizeListenerImpl
	{
		private static final int RESIZE_TIMEOUT = 1000;
		private Timer            resizeTimer;

		@Override
		public void onAttach(final SimplePanel widget)
		{
			resizeTimer = new Timer()
			{
				@Override
				public void run()
				{
					ResizeListenerImplMozilla.this.resize(widget);
				}
			};
			resizeTimer.scheduleRepeating(RESIZE_TIMEOUT);
			// resizeTimer.schedule(RESIZE_TIMEOUT);
		}

		@Override
		public void onDetach(final SimplePanel widget)
		{
			if (resizeTimer != null)
			{
				resizeTimer.cancel();
				resizeTimer = null;
			}
		}
	}
}
