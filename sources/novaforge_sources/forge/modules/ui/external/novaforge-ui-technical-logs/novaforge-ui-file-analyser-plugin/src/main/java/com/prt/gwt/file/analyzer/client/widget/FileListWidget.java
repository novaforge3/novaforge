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
package com.prt.gwt.file.analyzer.client.widget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.prt.gwt.file.analyzer.client.vo.AnalyzedFileVO;
import com.prt.gwt.file.core.client.RequestUtil;
import com.prt.gwt.file.core.client.util.Constants;
import com.prt.gwt.file.core.client.vo.FileVO;
import com.prt.gwt.file.core.client.widget.configuration.ConfigurationManager;
import com.prt.gwt.file.core.client.widget.filelist.BaseFileListWidget;
import com.prt.gwt.file.core.client.widget.filelist.FileListWidgetHandler;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileListWidget extends BaseFileListWidget
{

	public FileListWidget(final FileListWidgetHandler fileListWidgetHandler)
	{
		super(fileListWidgetHandler);
	}

	@Override
	protected void onDownload(final FileVO fileVO)
	{
		final AnalyzedFileVO file = (AnalyzedFileVO) fileVO;
		downloadFileForm.clear();
		final VerticalPanel params = new VerticalPanel();
		params.add(new Hidden("serviceName", "FileDownloadServlet"));
		params.add(new Hidden(Constants.FILE_PATH_PARAM, file.getFilePath()));
		params.add(new Hidden(RequestUtil.REDIRECT_CALL_PARAM, "true"));
		if (file.getSelectedEntry() >= 0)
		{
			params.add(new Hidden(Constants.FILE_START_LINE, String.valueOf(Math.max(file.getSelectedEntry()
			    - (ConfigurationManager.getCurrentProfile().getLinesToShow() / 2), 0))));
			params.add(new Hidden(Constants.FILE_LINES, String.valueOf(ConfigurationManager.getCurrentProfile()
			    .getLinesToShow())));
		}
		downloadFileForm.setWidget(params);
		downloadFileForm.submit();
	}

	@Override
	protected Widget getListWidget(final List<? extends FileVO> fileList, final PopupPanel contextMenuPanel)
	{
		final VerticalPanel panel = new VerticalPanel();
		panel.addStyleName("gwt-logger-file-list-table");

		boolean even = false;
		for (final FileVO fileVO : fileList)
		{
			final AnalyzedFileVO file = (AnalyzedFileVO) fileVO;

			final Label fileEntry = createLabelWithContextMenu(fileVO.getFileName(), contextMenuPanel);
			fileEntry.addMouseDownHandler(new MouseDownHandler()
			{

				@Override
				public void onMouseDown(final MouseDownEvent event)
				{
					selectedFileVO = fileVO;
					if (selectedFileWidget != null)
					{
						setFileWidgetSelected(selectedFileWidget, false);
					}
					selectedFileWidget = fileEntry;
					setFileWidgetSelected(selectedFileWidget, true);
					fileListWidgetHandler.onFileSelected(fileVO);
				}
			});
			fileEntry.getElement().getStyle().setProperty("width", "100%");
			fileEntry.getElement().getStyle().setProperty("fontWeight", "bold");
			fileEntry.setStyleName("gwt-logger-file-list-entry");
			fileEntry.addStyleName(even ? "entry-even" : "entry-odd");
			even = !even;
			panel.add(fileEntry);

			final Map<Long, String> entries = file.getEntries();
			for (final Entry<Long, String> entry : entries.entrySet())
			{
				final Label fileChildEntry = createLabelWithContextMenu(
				    "(" + entry.getKey() + ").." + entry.getValue() + "..", contextMenuPanel);
				fileChildEntry.addMouseDownHandler(new MouseDownHandler()
				{
					@Override
					public void onMouseDown(final MouseDownEvent event)
					{
						final Object key = entry.getKey();
						file.setSelectedEntry((Long) key);
						selectedFileVO = fileVO;
						if (selectedFileWidget != null)
						{
							setFileWidgetSelected(selectedFileWidget, false);
						}
						selectedFileWidget = fileEntry;
						setFileWidgetSelected(selectedFileWidget, true);
						fileListWidgetHandler.onFileSelected(fileVO);
					}
				});
				fileChildEntry.getElement().getStyle().setPropertyPx("paddingLeft", 10);
				fileChildEntry.setStyleName("gwt-logger-file-list-entry");
				fileChildEntry.addStyleName(even ? "entry-even" : "entry-odd");
				even = !even;
				panel.add(fileChildEntry);
			}

		}
		return panel;
	}

}
