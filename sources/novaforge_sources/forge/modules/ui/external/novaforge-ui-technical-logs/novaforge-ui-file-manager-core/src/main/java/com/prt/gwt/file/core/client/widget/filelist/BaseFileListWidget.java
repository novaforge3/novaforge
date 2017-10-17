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
package com.prt.gwt.file.core.client.widget.filelist;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.prt.gwt.file.core.client.RequestUtil;
import com.prt.gwt.file.core.client.util.CssUtil;
import com.prt.gwt.file.core.client.vo.FileVO;

import java.util.List;

public abstract class BaseFileListWidget extends Composite
{
	private final ScrollPanel       fileListWidgetContainer;
	private final VerticalPanel     fileListWidgetWrapper;
	private final PopupPanel        contextMenuPanel;
	protected FileListWidgetHandler fileListWidgetHandler;
	protected FileVO                selectedFileVO;
	protected Label                 selectedFileWidget;
	protected FormPanel             downloadFileForm;

	public BaseFileListWidget(final FileListWidgetHandler fileListWidgetHandler)
	{
		this.fileListWidgetHandler = fileListWidgetHandler;
		final MenuBar contextMenu = new MenuBar(true);
		contextMenu.setAnimationEnabled(true);
		final MenuItem item = new MenuItem("Télécharger", true, new Command()
		{
			@Override
			public void execute()
			{
				downloadFileForm.setAction(RequestUtil.createBaseUrl());
				onDownload(selectedFileVO);
				contextMenuPanel.hide(false);
			}
		});
		item.setStyleName("tool-active");
		contextMenu.addItem(item);
		contextMenuPanel = new PopupPanel(true);
		contextMenuPanel.setStyleName("gwt-logger-context-menu");
		contextMenuPanel.setWidget(contextMenu);
		fileListWidgetWrapper = new VerticalPanel();
		downloadFileForm = new FormPanel("_blank");
		fileListWidgetWrapper.add(downloadFileForm);
		downloadFileForm.setMethod("GET");
		downloadFileForm.getElement().getStyle().setProperty("display", "none");

		fileListWidgetWrapper.add(new HTML("<i>Empty</i>")); // to deal with contract about 2 required elements
		                                                     // (see setFileList())!
		fileListWidgetContainer = new ScrollPanel(fileListWidgetWrapper);
		initWidget(fileListWidgetContainer);
	}

	protected abstract void onDownload(FileVO fileVO);

	public final void setFileList(final List<? extends FileVO> fileList)
	{
		fileListWidgetWrapper.remove(fileListWidgetWrapper.getWidgetCount() - 1);
		fileListWidgetWrapper.add(getListWidget(fileList, contextMenuPanel));
	}

	protected Widget getListWidget(final List<? extends FileVO> fileList, final PopupPanel contextMenuPanel)
	{
		final VerticalPanel panel = new VerticalPanel();
		panel.addStyleName("gwt-logger-file-list-table");

		boolean even = false;
		for (final FileVO fileVO : fileList)
		{

			final Label fileEntry = createLabelWithContextMenu(fileVO.getFileName(), contextMenuPanel);
			fileEntry.addMouseDownHandler(new MouseDownHandler()
			{

				@Override
				public void onMouseDown(final MouseDownEvent event)
				{
					selectedFileVO = fileVO;
					if (event.getNativeButton() == NativeEvent.BUTTON_LEFT)
					{
						if (selectedFileWidget != null)
						{
							setFileWidgetSelected(selectedFileWidget, false);
						}
						selectedFileWidget = fileEntry;
						setFileWidgetSelected(selectedFileWidget, true);
						fileListWidgetHandler.onFileSelected(fileVO);
					}
				}

			});
			fileEntry.getElement().getStyle().setProperty("height", "100%");
			fileEntry.setStyleName("gwt-logger-file-list-entry");
			fileEntry.addStyleName(even ? "entry-even" : "entry-odd");
			even = !even;
			panel.add(fileEntry);
		}
		return panel;
	}

	protected Label createLabelWithContextMenu(final String text, final PopupPanel contextMenuPanel)
	{
		final Label fileEntry = new Label(text)
		{

			@Override
			public void onBrowserEvent(final Event event)
			{
				final int type = DOM.eventGetType(event);
				if ((type == Event.ONCONTEXTMENU) || (type == Event.ONMOUSEUP))
				{
					DOM.eventPreventDefault(event);
					DOM.eventCancelBubble(event, true);
					if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT)
					{
						contextMenuPanel.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + getOffsetHeight());
						contextMenuPanel.show();
					}
				}
				else
				{
					super.onBrowserEvent(event);
				}
			}

		};
		fileEntry.sinkEvents(Event.ONCONTEXTMENU | Event.ONMOUSEUP | Event.ONCLICK);
		return fileEntry;
	}

	protected void setFileWidgetSelected(final Label w, final boolean selected)
	{
		CssUtil.setActiveState(w, selected, "entry");
	}

	public void adjustSize()
	{
		fileListWidgetContainer.setHeight((Window.getClientHeight() - fileListWidgetContainer.getAbsoluteTop()) + "px");
		fileListWidgetContainer.setWidth("100%");
	}

	public void clear()
	{

	}
}
