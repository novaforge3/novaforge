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
package com.prt.gwt.file.viewer.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.prt.gwt.file.core.client.DataProvider;
import com.prt.gwt.file.core.client.util.CssUtil;
import com.prt.gwt.file.core.client.util.callback.MonitoringAsyncCallback;
import com.prt.gwt.file.core.client.util.callback.ProcessAsyncCallback;
import com.prt.gwt.file.core.client.vo.BaseFileDataVO;
import com.prt.gwt.file.core.client.vo.BaseFileVO;
import com.prt.gwt.file.core.client.vo.FileVO;
import com.prt.gwt.file.core.client.vo.GetFileListCallbackVO;
import com.prt.gwt.file.core.client.widget.BaseCompositeForm;
import com.prt.gwt.file.core.client.widget.configuration.ConfigurationManager;
import com.prt.gwt.file.core.client.widget.filelist.FileListWidgetHandler;
import com.prt.gwt.file.viewer.client.vo.FileViewConfigurationVO;
import com.prt.gwt.file.viewer.client.widget.FileDataWidget;
import com.prt.gwt.file.viewer.client.widget.FileListWidget;

public class FileViewerWidget extends BaseCompositeForm {
    private static final int TAIL_TIMEOUT = 1000;
    private FileViewerDataProvider dataProvider;
    private FileListWidget fileListWidget;
    private FileDataWidget fileDataPanel;
    private HorizontalPanel viewMainPanel;
    private DockPanel toobarPanel;
    private Label fileNameTool;
    private FocusPanel refreshListTool;
    private FocusPanel tailTool;
    private FocusPanel collapseTool;
    private Timer tailTimer;
    private final ProcessAsyncCallback<BaseFileDataVO> processCallback = new ProcessAsyncCallback<BaseFileDataVO>("Rafraichissement...") {
        public void onSuccessInternal(BaseFileDataVO baseFileDataVO) {
            tailTimer.cancel();
            if (baseFileDataVO.isChanged() && !baseFileDataVO.getFileData().isEmpty()) {
                showData(baseFileDataVO, true);
            }
            if (getCurrentFileDataWidget().getConfiguration().isTail()) { // stop tail requesting
                showFileData(baseFileDataVO.getFileVO(), getCurrentFileDataWidget().getConfiguration());
            }

        }
    };

    private final Command refreshCommand = new Command() {
        public void execute() {
            showFileData(getCurrentFileDataWidget().getFileDataVO().getFileVO(), getCurrentFileDataWidget().getConfiguration());
        }
    };

    private final FileListWidgetHandler listWidgetHandler = new FileListWidgetHandler() {
        public void onFileSelected(FileVO fileVO) {
            if (fileVO.isDirectory()) {
                FileViewerWidget.this.onDirectorySelected(fileVO);
            } else {
                setSelectedFileName(fileVO.getFileName());
                FileViewerWidget.this.onFileSelected(fileVO);
            }
        }

        public void refreshFileList()
        {
            fillFileList(null);
        }
    };

    private void onDirectorySelected(final FileVO fileVO)
    {
        fillFileList(fileVO);
    }

    private void fillFileList(final FileVO fileVO)
    {
        dataProvider.getFileList(new MonitoringAsyncCallback<GetFileListCallbackVO>()
        {

            @Override
            public void onSuccessInternal(GetFileListCallbackVO result)
            {
                fileListWidget.setFileList(result.getFilesData());
                adjustSize();
            }
        });
    }

    public void adjustSize()
    {
        viewMainPanel.setCellWidth(toobarPanel, "20px");
        fileListWidget.setWidth("200px");
        viewMainPanel.setCellWidth(fileListWidget, "200px");
        viewMainPanel.setCellWidth(fileDataPanel, "100%");
        viewMainPanel.setHeight((Window.getClientHeight() - viewMainPanel.getAbsoluteTop() - 5) + "px");

        fileListWidget.adjustSize();
        fileDataPanel.adjustSize();
    }

    public void applyData(DataProvider dataProvider)
    {
        this.dataProvider = (FileViewerDataProvider) dataProvider;
        fillFileList(null);
    }

    private void onFileSelected(final FileVO fileVO)
    {
        clearData();
        getCurrentFileDataWidget().getConfiguration().setStartWith(1);
        showFileData(fileVO, getCurrentFileDataWidget().getConfiguration());
    }

    private void clearData()
    {
        fileDataPanel.clear();
    }

    private FileDataWidget getCurrentFileDataWidget()
    {
        return fileDataPanel;
    }

    private void showFileData(final FileVO fileVO, final FileViewConfigurationVO configuration)
    {
        if (configuration.isTail())
        {
            processCallback.activate();
            if (tailTimer != null)
            {
                tailTimer.cancel();
            }

            tailTimer = new Timer()
            {
                public void run()
                {
                    final FileViewConfigurationVO currentConfiguration = getCurrentFileDataWidget().getConfiguration();
                    dataProvider.getFileData(new BaseFileVO(fileVO), currentConfiguration.getStartWith(),
                                             ConfigurationManager.getCurrentProfile().getLinesToShow(), true,
                                             processCallback.start());

                }
            };
            tailTimer.schedule(TAIL_TIMEOUT);
        }
        else
        {
            processCallback.passivate();
            fetchFileData(fileVO, getCurrentFileDataWidget().getConfiguration());
        }
    }

    private void fetchFileData(final FileVO fileVO, final FileViewConfigurationVO configuration)
    {
        dataProvider.getFileData(new BaseFileVO(fileVO), configuration.getStartWith(),
                                 ConfigurationManager.getCurrentProfile().getLinesToShow(), false,
                                 new MonitoringAsyncCallback<BaseFileDataVO>()
                                 {
                                     public void onSuccessInternal(BaseFileDataVO baseFileDataVO)
                                     {
                                         showData(baseFileDataVO, false);
                                     }
                                 });
    }

    private void showData(final BaseFileDataVO baseFileDataVO, boolean full)
    {
        fileDataPanel.setFileData(baseFileDataVO, full);
    }

    protected void buildUIInternal()
    {
        initCollapseTool();
        initAutoRefreshTool();
        initRefreshListTool();

        viewMainPanel = new HorizontalPanel();
        viewMainPanel.setStyleName("glv-main-panel");

        toobarPanel = new DockPanel();
        toobarPanel.setStyleName("glv-toolbar-panel");

        toobarPanel.add(collapseTool, DockPanel.NORTH);
        toobarPanel.setCellHeight(collapseTool, "1%");
        toobarPanel.add(refreshListTool, DockPanel.NORTH);
        toobarPanel.setCellHeight(refreshListTool, "99%");

        fileListWidget = new FileListWidget(listWidgetHandler);
        fileListWidget.setStyleName("glv-file-list-panel");

        fileDataPanel = new FileDataWidget(refreshCommand);
        fileDataPanel.setStyleName("glv-file-data-panel");

        viewMainPanel.add(toobarPanel);
        viewMainPanel.add(fileListWidget);
        viewMainPanel.add(fileDataPanel);

        add(viewMainPanel);
    }

    private void initCollapseTool()
    {
        final HTML child = new HTML("&lt;");
        child.setStyleName("glv-toolbar-tool");
        CssUtil.setActiveState(child, true, "glv-toolbar-tool-collapse");
        collapseTool = new FocusPanel(child);
        collapseTool.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                CssUtil.invertActiveState(child, "glv-toolbar-tool-collapse");
                boolean activeState = CssUtil.getActiveState(child);
                child.setHTML(activeState ? "&lt;" : "&gt;");
                fileListWidget.getElement().getStyle().setProperty("display", activeState ? "block" : "none");

                if (!activeState) {
                    refreshListTool.getElement().getStyle().setProperty("display", "none");
                    if (fileNameTool != null) {
                        toobarPanel.add(fileNameTool, DockPanel.NORTH);
                    }
                } else {

                    refreshListTool.getElement().getStyle().setProperty("display", "block");
                    if (fileNameTool != null && toobarPanel.getWidgetIndex(fileNameTool) >= 0) {
                        toobarPanel.remove(fileNameTool);
                    }
                }
                fileDataPanel.adjustSize();
            }
        });
    }

    private void initAutoRefreshTool() {
        Label label = new Label("Auto-Rafraichir");
        label.setStyleName("glv-toolbar-tool");
        tailTool = new FocusPanel(label);
        CssUtil.setActiveState(tailTool.getWidget(), false);
        label.addStyleName(CssUtil.getUserAgentBasedCss("glv-toolbar-tool-vertical")); // Vertical text!!
        tailTool.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                CssUtil.invertActiveState(tailTool.getWidget());
                getCurrentFileDataWidget().getConfiguration().setTail(CssUtil.getActiveState(tailTool.getWidget()));
                refreshCommand.execute();
            }
        });
    }

    private void initRefreshListTool()
    {
        final Label child = new Label("Rafraichir la liste");
        child.setStyleName("glv-toolbar-tool");
        child.addStyleName(CssUtil.getUserAgentBasedCss("glv-toolbar-tool-vertical")); // Vertical text!!
        refreshListTool = new FocusPanel(child);
        CssUtil.setActiveState(refreshListTool.getWidget(), true);

        refreshListTool.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {

                listWidgetHandler.refreshFileList();
            }
        });
    }

    private void setSelectedFileName(String name) {
        if (fileNameTool != null && toobarPanel.getWidgetIndex(fileNameTool) >= 0) {
            toobarPanel.remove(fileNameTool);
        }

        resetAutoRefreshTool();

        fileNameTool = new Label(name);
        fileNameTool.setStyleName("glv-toolbar-tool tool-active");
        fileNameTool.addStyleName(CssUtil.getUserAgentBasedCss("glv-toolbar-tool-vertical")); // Vertical text!!
        fileNameTool.getElement().getStyle().setPropertyPx("top", tailTool.getWidget().getAbsoluteTop() + tailTool.getWidget().getOffsetWidth() + 1); // getOffsetWidth - because of mozilla transfromation!!
    }

    private void resetAutoRefreshTool()
    {
        CssUtil.setActiveState(tailTool.getWidget(), false);
        getCurrentFileDataWidget().getConfiguration().setTail(CssUtil.getActiveState(tailTool.getWidget()));

        if (toobarPanel.getWidgetIndex(tailTool) < 0)
        {
            tailTool.getWidget().getElement().getStyle().setPropertyPx("top",
                                                                       refreshListTool.getWidget().getAbsoluteTop()
                                                                           + refreshListTool.getWidget()
                                                                                            .getOffsetWidth()
                                                                           + 1); // getOffsetWidth - because of mozilla transfromation!!
            toobarPanel.add(tailTool, DockPanel.NORTH);
        }
    }

    public void clear() {
        clearData();
        fileListWidget.clear();
    }
}
