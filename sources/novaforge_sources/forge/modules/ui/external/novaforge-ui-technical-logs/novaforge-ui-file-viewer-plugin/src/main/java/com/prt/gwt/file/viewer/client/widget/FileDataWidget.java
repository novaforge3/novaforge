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
package com.prt.gwt.file.viewer.client.widget;

import com.google.gwt.user.client.Command;
import com.prt.gwt.file.core.client.util.decorator.HtmlDecorator;
import com.prt.gwt.file.core.client.vo.BaseFileDataVO;
import com.prt.gwt.file.core.client.widget.configuration.ConfigurationManager;
import com.prt.gwt.file.core.client.widget.filedata.BaseFileDataWidget;
import com.prt.gwt.file.core.client.widget.pager.PagerHandler;
import com.prt.gwt.file.core.client.widget.pager.PagerWidget;
import com.prt.gwt.file.viewer.client.vo.FileViewConfigurationVO;

import java.util.LinkedList;
import java.util.List;

public class FileDataWidget extends BaseFileDataWidget {

    private List<String> cachedData = new LinkedList<String>();
    private String realLastLine;
    private FileViewConfigurationVO fileViewConfigurationVO;

    public FileDataWidget(final Command refreshCommand) {
        super();
        fileViewConfigurationVO = new FileViewConfigurationVO();
        initializeWidget(new PagerWidget(new PagerHandler() {
                    public void onPageSelect(int page) {
                        getConfiguration().setStartWith(page * ConfigurationManager.getCurrentProfile().getLinesToShow() + 1);
                        refreshCommand.execute();
                    }
                }));
    }

    public FileViewConfigurationVO getConfiguration()
    {
        return (FileViewConfigurationVO) fileViewConfigurationVO;
    }

    public void setConfiguration(FileViewConfigurationVO configuration)
    {
        fileViewConfigurationVO = configuration;
    }

    @Override
    public void setFileData(BaseFileDataVO baseFileDataVO, boolean full) {

        if (!getConfiguration().isTail()) {
            realLastLine = null;
            cachedData.clear();
        }

        // remove previous scroll target
        if (!cachedData.isEmpty() && realLastLine != null) {
            cachedData.remove(cachedData.size() - 1);
            cachedData.add(realLastLine);
        }

        normalizeDataSize(baseFileDataVO.getFileData());

        // set scroll target
        if (getConfiguration().isTail() && !cachedData.isEmpty()) {
            realLastLine = cachedData.remove(cachedData.size() - 1);
            cachedData.add(HtmlDecorator.TARGET_LINE_SPAN.replaceAll(HtmlDecorator.DATA_MARKER, realLastLine));

        }
        baseFileDataVO.setFileData(cachedData);

        super.setFileData(baseFileDataVO, full);
    }

    private void normalizeDataSize(List<String> inValues) {

        long linesToShow = ConfigurationManager.getCurrentProfile().getLinesToShow();
        if (inValues.size() >= linesToShow) {
            cachedData.clear();
            // Do not skip any lines!!
            cachedData.addAll(inValues);
        } else {
            cachedData.addAll(inValues);
            if (cachedData.size() > linesToShow) {
                long removeCount = cachedData.size() - linesToShow;
                for (int i = 0; i < removeCount; i++) {
                    cachedData.remove(0);
                }
            }
        }
    }

}
