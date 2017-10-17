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
package com.prt.gwt.file.analyzer.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.prt.gwt.file.analyzer.client.vo.GetFileListCallbackVO;
import com.prt.gwt.file.analyzer.client.vo.SearchVO;
import com.prt.gwt.file.core.client.RequestUtil;
import com.prt.gwt.file.core.client.util.json.Jsonable;

import java.util.HashMap;
import java.util.Map;

public class RPCCallUtil implements FileAnalyzerServiceAsync
{
	@Override
	public void getFileList(final SearchVO pArg0, final AsyncCallback<GetFileListCallbackVO> pArg2)
	{
		final String argLocal0 = pArg0.toJson();
		final Jsonable callbackType = new GetFileListCallbackVO();

		final Map<String, String> data = new HashMap<String, String>();
		data.put("methodName", "getFileList");
		data.put("serviceName", "FileAnalyzerServiceAsync");
		data.put("_argLocal0", argLocal0);
		data.put("_argLocalType0", "com.prt.gwt.file.analyzer.client.vo.SearchVO");

		RequestUtil.makeACall(data, RequestUtil.getProxyAsyncCallback(callbackType, pArg2));
	}
}
