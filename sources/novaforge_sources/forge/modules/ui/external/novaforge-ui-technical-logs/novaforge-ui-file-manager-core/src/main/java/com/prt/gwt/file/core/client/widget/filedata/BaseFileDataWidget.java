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
package com.prt.gwt.file.core.client.widget.filedata;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.prt.gwt.file.core.client.util.decorator.HtmlDecorator;
import com.prt.gwt.file.core.client.util.decorator.ServerLogDecorator;
import com.prt.gwt.file.core.client.vo.BaseFileDataVO;
import com.prt.gwt.file.core.client.widget.configuration.ConfigurationManager;
import com.prt.gwt.file.core.client.widget.pager.BasePagerWidget;

import java.util.List;

public class BaseFileDataWidget extends Composite
{
	private final VerticalPanel mainPanel;
	private final HtmlDecorator defaultDecorator;
	private HTML                fileDataWidget;
	private HTML                fileLinesWidget;
	private HtmlDecorator       decorator;
	private BasePagerWidget     pagerPanel;

	private BaseFileDataVO      baseFileDataVO;

	private ScrollPanel         fileDataScrollPanel;

	public BaseFileDataWidget()
	{
		defaultDecorator = new ServerLogDecorator();
		mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "100%");
		mainPanel.setStyleName("gwt-logger-file-data");
		initWidget(mainPanel);
	}

	public void initializeWidget(final BasePagerWidget pagerPanel)
	{
		this.pagerPanel = pagerPanel;
		final HorizontalPanel content = new HorizontalPanel();
		fileLinesWidget = new HTML("", false);
		content.add(fileLinesWidget);
		fileDataWidget = new HTML("", false);
		content.add(fileDataWidget);
		fileDataScrollPanel = new ScrollPanel(content);

		fileLinesWidget.setStyleName("gwt-logger-file-lines");
		fileDataWidget.setStyleName("gwt-logger-file-content");
		fileDataScrollPanel.setStyleName("gwt-logger-file-content-wrapper");

		mainPanel.add(pagerPanel);
		mainPanel.add(fileDataScrollPanel);

		mainPanel.setCellHeight(pagerPanel, "25px");
		mainPanel.setCellWidth(pagerPanel, "1px");
	}

	public void clear()
	{
		pagerPanel.clear();
		fileDataWidget.setHTML("");
		fileLinesWidget.setHTML("");
	}

	public void setFileData(final BaseFileDataVO baseFileDataVO, final boolean full,
	    final HtmlDecorator decorator)
	{
		this.decorator = decorator;
		this.baseFileDataVO = baseFileDataVO;
		applyFileData(full);
	}

	private void applyFileData(final boolean full)
	{
		if (!full)
		{
			if (baseFileDataVO.isChanged())
			{
				pagerPanel.clear();
				pagerPanel.setFileSize(
				    (int) Math.ceil((double) baseFileDataVO.getFileVO().getLinesInFile()
				        / (double) ConfigurationManager.getCurrentProfile().getLinesToShow()),
				    (int) baseFileDataVO.getActivePage(), ConfigurationManager.getCurrentProfile().getLinesToShow());
			}
			else
			{
				pagerPanel.setActivePage((int) baseFileDataVO.getActivePage());
			}
		}
		else
		{
			pagerPanel.clear();
		}

		baseFileDataVO.getFileData();
		String dataString = "";
		String linesString = "";
		long lineN = baseFileDataVO.getStartLine();
		final List<String> fileData = baseFileDataVO.getFileData();
		for (final String line : fileData)
		{
			final String[] result = decorator.decorateContentLine(lineN++, line);
			linesString += result[0] + "</br>";
			dataString += result[1] + "</br>";
		}
		fileLinesWidget.setHTML(linesString);
		fileDataWidget.setHTML(dataString);

		// Adjust size to fit client
		adjustSize();

		if (DOM.getElementById(HtmlDecorator.TARGET_MARKER) != null)
		{
			DOM.scrollIntoView(DOM.getElementById(HtmlDecorator.TARGET_MARKER));
		}
	}

	public void adjustSize()
	{
		pagerPanel.adjustSize();
		mainPanel.setCellHeight(pagerPanel, (pagerPanel.getOffsetHeight() + 1) + "px");
		fileDataScrollPanel
		    .setWidth((Window.getClientWidth() - fileDataScrollPanel.getAbsoluteLeft() - 5) + "px");
		fileDataScrollPanel.setHeight((Window.getClientHeight() - fileDataScrollPanel.getAbsoluteTop() - 5)
		    + "px");
	}

	public void setFileData(final BaseFileDataVO baseFileDataVO, final boolean full)
	{
		decorator = defaultDecorator;
		this.baseFileDataVO = baseFileDataVO;
		applyFileData(full);
	}

	public BaseFileDataVO getFileDataVO()
	{
		return baseFileDataVO;
	}
}
