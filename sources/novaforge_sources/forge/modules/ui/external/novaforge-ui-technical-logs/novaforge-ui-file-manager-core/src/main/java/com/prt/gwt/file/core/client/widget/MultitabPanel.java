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
package com.prt.gwt.file.core.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;
import java.util.List;

public class MultitabPanel<T extends Widget> extends TabPanel
{
	private static final String       EMPTY_TAB_TITLE = "[empty tab]";
	private final HTML                ADD_HTML        = new HTML("add");
	@SuppressWarnings("unchecked")
	private final PanelWidgetProvider panelWidgetProvider;
	private final List<RemovableTab>  tabWidgets      = new LinkedList<RemovableTab>();

	public MultitabPanel(final PanelWidgetProvider<T> panelWidgetProvider)
	{
		final FocusPanel addImage = new FocusPanel();
		addImage.setStyleName("add-tab");
		this.panelWidgetProvider = panelWidgetProvider;

		addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>()
		{
			@Override
			public void onBeforeSelection(final BeforeSelectionEvent<Integer> integerBeforeSelectionEvent)
			{
				if (getWidget(integerBeforeSelectionEvent.getItem()) == ADD_HTML)
				{
					addTab(createTabWidget());
					integerBeforeSelectionEvent.cancel();
				}
			}
		});
		insert(ADD_HTML, addImage, 0);
		checkTabCount();
	}

	public void addTab(final T w)
	{
		final MultitabPanel<T>.RemovableTab tab = new RemovableTab();
		tab.addRemoveAction(new Command()
		{
			@Override
			public void execute()
			{
				selectTab(tabWidgets.indexOf(tab) + 1);
				removeTab();
			}
		});
		tabWidgets.add(0, tab);
		insert(w, tab, 1);
		selectTab(1);
	}

	@SuppressWarnings("unchecked")
	private T createTabWidget()
	{
		final Widget widget = panelWidgetProvider.getPanelWidget();
		if (isAttached())
		{
			widget.setPixelSize(getOffsetWidth(), getOffsetHeight());
		}
		return (T) widget;
	}

	public void checkTabCount()
	{
		if (getWidgetCount() < 3)
		{
			addTab(createTabWidget());
		}
	}

	public void removeTab()
	{
		tabWidgets.remove(getTabBar().getSelectedTab() - 1);
		remove(getTabBar().getSelectedTab());
		if (getTabBar().getTabCount() > 1)
		{
			selectTab(1);
		}
	}

	public void setActiveTabText(final String text)
	{
		tabWidgets.get(getTabBar().getSelectedTab() - 1).setText(text);
	}

	@SuppressWarnings("unchecked")
	public T getActiveTabWidget()
	{
		if (getTabBar().getSelectedTab() < 1)
		{
			addTab(createTabWidget());
		}
		return (T) getWidget(getTabBar().getSelectedTab());
	}

	class RemovableTab extends Composite
	{
		private final HorizontalPanel tabWidget = new HorizontalPanel();

		RemovableTab()
		{
			tabWidget.add(new Label(EMPTY_TAB_TITLE));
			initWidget(tabWidget);
		}

		public void addRemoveAction(final Command removeCommand)
		{
			final FocusPanel img = new FocusPanel();
			img.setStyleName("close-tab");
			img.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(final ClickEvent event)
				{
					removeCommand.execute();
				}
			});
			tabWidget.add(img);
		}

		public void setText(final String text)
		{
			((Label) tabWidget.getWidget(0)).setText(text);
		}
	}
}
