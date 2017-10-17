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
package com.prt.gwt.file.core.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.prt.gwt.file.analyzer.client.FileAnalyzerPluginEntryPoint;
import com.prt.gwt.file.core.client.service.ServerProfileService;
import com.prt.gwt.file.core.client.service.ServerProfileServiceAsync;
import com.prt.gwt.file.core.client.util.callback.ProcessAsyncCallback;
import com.prt.gwt.file.core.client.vo.ProfileVO;
import com.prt.gwt.file.core.client.widget.configuration.ConfigurationManager;
import com.prt.gwt.file.viewer.client.FileViewerPluginEntryPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GWTFileManagerApp implements EntryPoint
{
	private final ServerProfileServiceAsync         profileServiceAsync = GWT
	                                                                        .create(ServerProfileService.class);

	private final List<PluginEntryPoint>            plugins             = new ArrayList<PluginEntryPoint>();
	private final Map<PluginEntryPoint, FocusPanel> pluginTools         = new HashMap<PluginEntryPoint, FocusPanel>();
	private PluginEntryPoint                        activePlugin;
	private FocusPanel                              profilesBar;
	private PopupPanel                              profilesList;
	private DockPanel                               toolBar;

	@Override
	public void onModuleLoad()
	{
		profileServiceAsync.getProfiles(new ProcessAsyncCallback<List<ProfileVO>>("Loading...")
		{

			@Override
			public void onSuccessInternal(final List<ProfileVO> pResult)
			{
				ConfigurationManager.setProfiles(pResult);
				GWTFileManagerApp.this.buildUI();
				Window.addResizeHandler(new ResizeHandler()
				{
					@Override
					public void onResize(final ResizeEvent event)
					{
						activePlugin.adjustSize();
					}
				});

			}
		});

	}

	private void buildUI()
	{
		initializePlugins();
		initProfileSelectorTool();

		toolBar = new DockPanel();
		toolBar.setStyleName("gwt-logger-toolbar");
		toolBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		toolBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		final SimplePanel container = new SimplePanel();
		container.setStyleName("gwt-logger-container");

		toolBar.add(profilesBar, DockPanel.WEST);

		addSeparatorPanel("15%", DockPanel.WEST);

		// Build plugins tab
		for (final PluginEntryPoint entryPoint : plugins)
		{
			toolBar.add(createPluginTool(entryPoint, container), DockPanel.WEST);
		}

		addSeparatorPanel("100%", DockPanel.WEST);

		toolBar.add(createMonitorTool("Requesting data..", "request-monitor"), DockPanel.EAST);
		toolBar.add(createMonitorTool("Processing data..", "process-monitor"), DockPanel.EAST);
		toolBar.add(createMonitorTool("[tail-monitor]", "tail-monitor"), DockPanel.EAST);

		final RootPanel rootPanel = RootPanel.get("gwt-logger-main-panel");
		rootPanel.add(toolBar);
		rootPanel.add(container);

		pluginTools.get(plugins.get(0)).fireEvent(new ClickEvent()
		{
		});
	}

	private void initializePlugins()
	{
		plugins.clear();
		plugins.add(new FileViewerPluginEntryPoint());
		plugins.add(new FileAnalyzerPluginEntryPoint());
	}

	private void initProfileSelectorTool()
	{
		profilesBar = new FocusPanel(new Label(ConfigurationManager.getCurrentProfile().getAppName()));
		profilesBar.getWidget().setStyleName("gwt-logger-plugin-tool tool-active");

		final List<ProfileVO> applications = ConfigurationManager.getProfiles();
		if (applications.size() > 1)
		{
			final MenuBar contextMenu = new MenuBar(true);
			profilesList = new PopupPanel(true);
			profilesList.setStyleName("gwt-logger-context-menu");
			profilesList.setWidget(contextMenu);

			for (final ProfileVO application : applications)
			{
				final MenuItem item = new MenuItem(application.getAppName(), new Command()
				{
					@Override
					public void execute()
					{
						ConfigurationManager.setCurrentProfile(application);
						profilesBar.setWidget(new Label(application.getAppName()));
						profilesBar.getWidget().setStyleName("gwt-logger-plugin-tool tool-active");
						profilesList.hide();
						pluginTools.get(plugins.get(0)).fireEvent(new ClickEvent()
						{
						});
					}
				});
				item.setStyleName("tool-active");
				contextMenu.addItem(item);
			}

			profilesBar.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(final ClickEvent event)
				{
					profilesList.showRelativeTo(profilesBar);
				}
			});
		}

	}

	private void addSeparatorPanel(final String size, final DockLayoutConstant direction)
	{
		final SimplePanel fillPanel = new SimplePanel();
		fillPanel.setWidth(size);
		toolBar.add(fillPanel, direction);
		toolBar.setCellWidth(fillPanel, size);

	}

	private Widget createPluginTool(final PluginEntryPoint entryPoint, final SimplePanel pluginContainer)
	{
		final Label pluginTool = new Label(entryPoint.getPluginTitle());
		pluginTool.setStyleName("gwt-logger-plugin-tool");
		pluginTool.addStyleName("tool-passive");
		final FocusPanel result = new FocusPanel(pluginTool);
		result.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (activePlugin != null)
				{
					activePlugin.passivate();
					GWTFileManagerApp.this.setPluginToolState(pluginTools.get(activePlugin), false);
				}
				activePlugin = entryPoint;
				activePlugin.activate();
				GWTFileManagerApp.this.setPluginToolState(pluginTools.get(activePlugin), true);
				pluginContainer.setWidget(activePlugin.getPluginWidget());
				DeferredCommand.addCommand(new Command()
				{
					@Override
					public void execute()
					{
						activePlugin.adjustSize();
					}

				});
			}
		});

		result.addMouseOverHandler(new MouseOverHandler()
		{
			@Override
			public void onMouseOver(final MouseOverEvent event)
			{
				GWTFileManagerApp.this.setPluginToolState(result, true);
			}
		});

		result.addMouseOutHandler(new MouseOutHandler()
		{
			@Override
			public void onMouseOut(final MouseOutEvent event)
			{
				GWTFileManagerApp.this.setPluginToolState(result, activePlugin == entryPoint);
			}
		});

		pluginTools.put(entryPoint, result);
		return result;
	}

	private Label createMonitorTool(final String text, final String id)
	{
		final Label monitor = new Label(text);
		monitor.setStyleName("gwt-logger-plugin-tool tool-active");
		monitor.getElement().setId(id);
		monitor.getElement().getStyle().setProperty("visibility", "hidden");
		return monitor;
	}

	private void setPluginToolState(final FocusPanel pluginToolWrapper, final boolean active)
	{
		final Widget pluginTool = pluginToolWrapper.getWidget();
		if (active)
		{
			pluginTool.removeStyleName("tool-passive");
			pluginTool.addStyleName("tool-active");
		}
		else
		{
			pluginTool.addStyleName("tool-passive");
			pluginTool.removeStyleName("tool-active");
		}
	}

	// TEST JSON

}
