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
package com.prt.gwt.file.core.client.widget.pager;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.prt.gwt.file.core.client.util.CssUtil;

public class PagerWidget extends BasePagerWidget
{
	private static final int      myHeight   = 22;
	private final PagerHandler    pagerHandler;
	private final HorizontalPanel pagesPanel;
	private int                   activePage = -1;

	public PagerWidget(final PagerHandler pagerHandler)
	{
		pagesPanel = new HorizontalPanel();
		pagesPanel.addStyleName("gwt-logger-file-data-pager");
		this.pagerHandler = pagerHandler;
		add(pagesPanel);
		setVisible(true);
	}

	@Override
	public void setVisible(final boolean visible)
	{
		getElement().getStyle().setProperty("display", visible ? "block" : "none");
	}

	@Override
	public void clear()
	{
		pagesPanel.clear();
		activePage = -1;
	}

	@Override
	public void setActivePage(final int page)
	{
		final int widgetCount = pagesPanel.getWidgetCount();

		// Activate previously selected page
		if ((widgetCount >= activePage) && (activePage > 0))
		{
			final Label pageLink = (Label) pagesPanel.getWidget(activePage - 1);
			CssUtil.setActiveState(pageLink, false);
		}
		activePage = -1;
		if ((widgetCount >= page) && (page > 0))
		{
			final Label pageLink = (Label) pagesPanel.getWidget(page - 1);
			CssUtil.setActiveState(pageLink, true);
			activePage = page;
		}
	}

	@Override
	public void setFileSize(final int pages, final int selectedPage, final long pageSize)
	{
		clear();
		if (pages > 1)
		{
			for (int i = 0; i < pages; i++)
			{
				final String text = String.valueOf(i + 1);
				final Label page = new Label(text);
				page.addClickHandler(getPageClickHandler(i));
				CssUtil.setActiveState(page, false);
				pagesPanel.add(page);
			}
			setActivePage(selectedPage);
		}
	}

	@Override
	public void adjustSize()
	{
		if (!isNeedToShow())
		{
			pagesPanel.setHeight("0px");
			setHeight("0px");
		}
		else
		{
			pagesPanel.setHeight(myHeight + "px");
			if (pagesPanel.getOffsetWidth() > (Window.getClientWidth() - pagesPanel.getAbsoluteLeft() - 10))
			{
				setHeight((myHeight + 13) + "px");
			}
			else
			{
				setHeight((myHeight) + "px");
			}
		}

		setWidth((Window.getClientWidth() - getAbsoluteLeft() - 5) + "px");
	}

	private ClickHandler getPageClickHandler(final int pageN)
	{
		return new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				pagerHandler.onPageSelect(pageN);
				setActivePage(pageN + 1);
			}
		};
	}

	@Override
	public boolean isNeedToShow()
	{
		return pagesPanel.getWidgetCount() > 0;
	}

}
