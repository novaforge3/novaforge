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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.prt.gwt.file.analyzer.client.service.FileAnalyzerServiceAsync;
import com.prt.gwt.file.analyzer.client.vo.GetFileListCallbackVO;
import com.prt.gwt.file.analyzer.client.vo.SearchVO;
import com.prt.gwt.file.core.client.DataProvider;
import com.prt.gwt.file.core.client.service.FileManagerServiceAsync;
import com.prt.gwt.file.core.client.vo.BaseFileDataVO;
import com.prt.gwt.file.core.client.vo.BaseFileVO;
import com.prt.gwt.file.core.client.vo.GetFileDataVO;

public class FileAnalyzerDataProvider implements DataProvider
{
	private final FileAnalyzerServiceAsync analyzerServiceAsync = GWT.create(FileAnalyzerServiceAsync.class);
	private final FileManagerServiceAsync  managerServiceAsync  = GWT.create(FileManagerServiceAsync.class);

	public void getFileList(final SearchVO vo, final AsyncCallback<GetFileListCallbackVO> callback)
	{
		analyzerServiceAsync.getFileList(vo, callback);
	}

	public void getFileData(final BaseFileVO file, final long startWith, final long lines, final boolean tail,
	    final AsyncCallback<BaseFileDataVO> callback)
	{
		managerServiceAsync.getFileData(new GetFileDataVO(file, startWith, lines, tail), callback);
	}
}
