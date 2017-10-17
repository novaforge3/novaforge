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
package com.prt.gwt.file.analyzer.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.prt.gwt.file.analyzer.client.util.decorator.AnalyzedLogDecorator;
import com.prt.gwt.file.analyzer.client.vo.AnalyzedFileVO;
import com.prt.gwt.file.analyzer.client.vo.GetFileListCallbackVO;
import com.prt.gwt.file.analyzer.client.vo.SearchVO;
import com.prt.gwt.file.analyzer.client.widget.FileListWidget;
import com.prt.gwt.file.core.client.DataProvider;
import com.prt.gwt.file.core.client.util.CssUtil;
import com.prt.gwt.file.core.client.util.callback.MonitoringAsyncCallback;
import com.prt.gwt.file.core.client.vo.BaseFileDataVO;
import com.prt.gwt.file.core.client.vo.BaseFileVO;
import com.prt.gwt.file.core.client.vo.FileVO;
import com.prt.gwt.file.core.client.widget.BaseCompositeForm;
import com.prt.gwt.file.core.client.widget.configuration.ConfigurationManager;
import com.prt.gwt.file.core.client.widget.filedata.BaseFileDataWidget;
import com.prt.gwt.file.core.client.widget.filelist.FileListWidgetHandler;
import com.prt.gwt.file.core.client.widget.pager.PagerHandler;
import com.prt.gwt.file.core.client.widget.pager.PagerWidget;

public class FileAnalyzerWidget extends BaseCompositeForm
{
	private final AnalyzedLogDecorator decorator = new AnalyzedLogDecorator();
	private FileAnalyzerDataProvider   dataProvider;
	private FileListWidget             fileListWidget;
	private BaseFileDataWidget         fileDataWidget;
	private HorizontalPanel            analyzeMainPanel;
	private StackPanel                 fileSearchPanel;
	private DockPanel                  toobarPanel;
	private Label                      fileNameTool;
	private FocusPanel                 collapseTool;
	private ConfigPanel                configPanel;
	private final PagerWidget          pagerWidget           = new PagerWidget(new PagerHandler()
	                                                         {
		                                                         @Override
		                                                         public void onPageSelect(final int page)
		                                                         {
			                                                         final long linesToShow = ConfigurationManager
			                                                             .getCurrentProfile().getLinesToShow();
			                                                         fetchFileData(getCurrentFileDataWidget()
			                                                             .getFileDataVO().getFileVO(),
			                                                             (page * linesToShow) + 1,
			                                                             (page * linesToShow) + 1, false);
		                                                         }
	                                                         });

	private final Command              searchCommand         = new Command()
	                                                         {
		                                                         @Override
		                                                         public void execute()
		                                                         {
			                                                         clear();
			                                                         dataProvider
			                                                             .getFileList(
			                                                                 configPanel.getSearchVO(),
			                                                                 new MonitoringAsyncCallback<GetFileListCallbackVO>()
			                                                                 {
				                                                                 @Override
				                                                                 public void onSuccessInternal(
				                                                                     final GetFileListCallbackVO result)
				                                                                 {
					                                                                 fileSearchPanel.showStack(1);
					                                                                 fileListWidget.setFileList(result
					                                                                     .getFilesData());
					                                                                 adjustSize();
				                                                                 }
			                                                                 });
		                                                         }
	                                                         };

	final FileListWidgetHandler        fileListWidgetHandler = new FileListWidgetHandler()
	                                                         {

		                                                         @Override
		                                                         public void onFileSelected(final FileVO fileVO)
		                                                         {
			                                                         final long targetLine = ((AnalyzedFileVO) fileVO)
			                                                             .getSelectedEntry();
			                                                         fetchFileData(new BaseFileVO(fileVO),
			                                                             Math.max(targetLine
			                                                                 - (ConfigurationManager
			                                                                     .getCurrentProfile()
			                                                                     .getLinesToShow() / 2), 1),
			                                                             targetLine, targetLine >= 0);
			                                                         FileAnalyzerWidget.this
			                                                             .setSelectedFileName(fileVO.getFileName()
			                                                                 + " (" + targetLine + ")");
		                                                         }

		                                                         @Override
																														 public void refreshFileList()
																														 {
																															 searchCommand.execute();
																														 }
	};

	@Override
	protected void buildUIInternal()
	{
		initCollapseTool();

		analyzeMainPanel = new HorizontalPanel();
		analyzeMainPanel.setStyleName("gla-main-panel");

		toobarPanel = new DockPanel();
		toobarPanel.setStyleName("gla-toolbar-panel");

		toobarPanel.add(collapseTool, DockPanel.NORTH);
		toobarPanel.setCellHeight(collapseTool, "100%");

		fileListWidget = new FileListWidget(fileListWidgetHandler);
		fileListWidget.setStyleName("gla-file-list-panel");

		fileDataWidget = new BaseFileDataWidget();
		fileDataWidget.setStyleName("gla-file-data-panel");
		fileDataWidget.initializeWidget(pagerWidget);

		fileSearchPanel = new StackPanel();
		fileSearchPanel.setStyleName("gla-search-stack");
		configPanel = new ConfigPanel(searchCommand);
		fileSearchPanel.add(configPanel, "<div class=\"gla-search-stack-head\">Configuration</div>", true);
		fileSearchPanel.add(fileListWidget, "<div class=\"gla-search-stack-head\">Results</div>", true);

		analyzeMainPanel.add(toobarPanel);
		analyzeMainPanel.add(fileSearchPanel);
		analyzeMainPanel.add(fileDataWidget);
		analyzeMainPanel.setCellWidth(fileSearchPanel, "100px");
		analyzeMainPanel.setCellHeight(fileSearchPanel, "1px");
		add(analyzeMainPanel);
	}

	private void initCollapseTool()
	{
		final HTML child = new HTML("&lt;");
		child.setStyleName("gla-toolbar-tool");
		CssUtil.setActiveState(child, true, "gla-toolbar-tool-collapse");
		collapseTool = new FocusPanel(child);
		collapseTool.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				CssUtil.invertActiveState(child, "gla-toolbar-tool-collapse");
				final boolean activeState = CssUtil.getActiveState(child);
				child.setHTML(activeState ? "&lt;" : "&gt;");
				fileSearchPanel.getElement().getStyle().setProperty("display", activeState ? "block" : "none");
				if (!activeState)
				{
					if (fileNameTool != null)
					{
						toobarPanel.add(fileNameTool, DockPanel.NORTH);
						toobarPanel.setCellHeight(collapseTool, "1%");
						toobarPanel.setCellHeight(fileNameTool, "99%");
					}
				}
				else
				{
					if ((fileNameTool != null) && (toobarPanel.getWidgetIndex(fileNameTool) >= 0))
					{
						toobarPanel.remove(fileNameTool);
					}
					toobarPanel.setCellHeight(collapseTool, "100%");
				}
				DeferredCommand.addCommand(new Command()
				{
					@Override
					public void execute()
					{
						adjustSize();
					}
				});
			}
		});
	}

	@Override
	public void adjustSize()
	{
		fileSearchPanel.setHeight((Window.getClientHeight() - fileSearchPanel.getAbsoluteTop()) + "px");
		setCellWidth(analyzeMainPanel, "200px");
		analyzeMainPanel.setCellWidth(fileDataWidget, "100%");
		fileListWidget.adjustSize();
		fileDataWidget.adjustSize();
	}

	@Override
	public void applyData(final DataProvider dataProvider)
	{
		this.dataProvider = (FileAnalyzerDataProvider) dataProvider;
	}

	private void setSelectedFileName(final String name)
	{
		if ((fileNameTool != null) && (toobarPanel.getWidgetIndex(fileNameTool) >= 0))
		{
			toobarPanel.remove(fileNameTool);
		}

		fileNameTool = new Label(name);
		fileNameTool.setStyleName("gla-toolbar-tool tool-active");
		fileNameTool.addStyleName(CssUtil.getUserAgentBasedCss("gla-toolbar-tool-vertical")); // Vertical text!!
	}

	@Override
	public void clear()
	{
		fileSearchPanel.showStack(0);

		if (!CssUtil.getActiveState(collapseTool.getWidget()))
		{
			collapseTool.fireEvent(new ClickEvent()
			{
			});
		}

		fileDataWidget.clear();
		fileListWidget.clear();
	}

	private void fetchFileData(final BaseFileVO fileVO, final long startWith, final long targetLine, final boolean full)
	{
		fileDataWidget.clear();
		dataProvider.getFileData(fileVO, startWith, ConfigurationManager.getCurrentProfile().getLinesToShow(), false,
														 new MonitoringAsyncCallback<BaseFileDataVO>()
														 {
															 @Override
															 public void onSuccessInternal(final BaseFileDataVO baseFileDataVO)
															 {
																 decorator.setUnderligneText(configPanel.getSearchVO().getSearchString());
																 decorator.setTargetLine(targetLine);
																 getCurrentFileDataWidget().setFileData(baseFileDataVO, full, decorator);
															 }
														 });
	}

	private BaseFileDataWidget getCurrentFileDataWidget()
	{
		return fileDataWidget;
	}

	class ConfigPanel extends VerticalPanel
	{
		private final TextBox  fileMaskWidget;
		private final TextBox  searchWidget;
		private       SearchVO searchVO;

		ConfigPanel(final Command searchCommand)
		{
			final Label fileMaskLabel = new Label("Nom du fichier : ");
			add(fileMaskLabel);
			fileMaskWidget = new TextBox();
			add(fileMaskWidget);
			final Label searchForLabel = new Label("Mot-clé : ");
			add(searchForLabel);
			searchWidget = new TextBox();
			add(searchWidget);
			final Button button = new Button("Rechercher", new ClickHandler()
			{
				@Override
				public void onClick(final ClickEvent event)
				{
					if (validate())
					{
						searchCommand.execute();
					}
				}
			});

			fileMaskLabel.setStyleName("gla-search-param-label");
			searchForLabel.setStyleName("gla-search-param-label");
			button.setStyleName("gla-toolbar-tool tool-active");
			add(button);

			final SearchVO vo = new SearchVO();
			vo.setFileMask(ConfigurationManager.getCurrentProfile().getDefaultFileMask());
			setSearchVO(vo);
		}

		private boolean validate()
		{
			boolean result = true;

			final String fileMaskValue = fileMaskWidget.getText();
			if ((fileMaskValue == null) || "".equals(fileMaskValue))
			{
				fileMaskWidget.addStyleName("gla-invalid-field-value");
				result = false;
			}
			else
			{
				fileMaskWidget.removeStyleName("gla-invalid-field-value");
			}

			final String searchString = searchWidget.getText();
			if ((searchString == null) || "".equals(searchString))
			{
				searchWidget.addStyleName("gla-invalid-field-value");
				result = false;
			}
			else
			{
				searchWidget.removeStyleName("gla-invalid-field-value");
			}

			return result;
		}

		public SearchVO getSearchVO()
		{
			searchVO.setFileMask(fileMaskWidget.getText());
			searchVO.setSearchString(searchWidget.getText());
			return searchVO;
		}

		public void setSearchVO(final SearchVO vo)
		{
			searchVO = vo;
			fileMaskWidget.setText(vo.getFileMask());
			searchWidget.setText(vo.getSearchString());
		}
	}
}
