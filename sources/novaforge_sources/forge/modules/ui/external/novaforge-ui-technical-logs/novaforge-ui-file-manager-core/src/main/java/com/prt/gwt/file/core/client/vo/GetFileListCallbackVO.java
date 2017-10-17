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
package com.prt.gwt.file.core.client.vo;

import com.prt.gwt.file.core.client.util.json.JsonableBaseVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetFileListCallbackVO extends JsonableBaseVO
{
	/**
    * 
    */
	private static final long serialVersionUID = -3658251134675566534L;
	private List<BaseFileVO>  filesData;

	public GetFileListCallbackVO()
	{
		super();
	}

	public GetFileListCallbackVO(final List<BaseFileVO> filesData)
	{
		super();
		this.filesData = filesData;
	}

	public List<BaseFileVO> getFilesData()
	{
		return filesData;
	}

	public void setFilesData(final List<BaseFileVO> filesData)
	{
		this.filesData = filesData;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object getData()
	{
		final Map result = new HashMap();
		final List<Map> resultData = new ArrayList<Map>();
		if (filesData != null)
		{
			for (final BaseFileVO elem : filesData)
			{
				resultData.add(elem.getData());
			}
			result.put("filesData", resultData);
		}
		else
		{
			result.put("filesData", null);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setData(final Map readJSONObject)
	{
		filesData = new ArrayList<BaseFileVO>();
		final List<Map> obj = (List<Map>) readJSONObject.get("filesData");
		for (final Object element : obj)
		{
			final Map map = (Map) element;
			final BaseFileVO o = new BaseFileVO();
			o.setData(map);
			filesData.add(o);
		}
	}

}
