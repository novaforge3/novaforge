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
package com.prt.gwt.file.analyzer.client.vo;

import com.prt.gwt.file.core.client.util.json.JsonableBaseVO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchVO extends JsonableBaseVO implements Serializable
{
	/**
    * 
    */
	private static final long serialVersionUID = 5669317571081211686L;
	private String            searchString;
	private String            fileMask;

	public SearchVO()
	{
	}

	public String getSearchString()
	{
		return searchString;
	}

	public void setSearchString(final String searchString)
	{
		this.searchString = searchString;
	}

	public String getFileMask()
	{
		return fileMask;
	}

	public void setFileMask(final String fileMask)
	{
		this.fileMask = fileMask;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Map getData()
	{
		final Map data = new HashMap();
		data.put("searchString", searchString);
		data.put("fileMask", fileMask);
		return data;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void setData(final Map data)
	{
		searchString = (String) data.get("searchString");
		fileMask = (String) data.get("fileMask");

	}

}
