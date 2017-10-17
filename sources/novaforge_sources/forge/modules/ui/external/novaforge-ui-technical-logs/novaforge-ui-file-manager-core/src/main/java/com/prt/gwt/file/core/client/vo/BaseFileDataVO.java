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

import com.prt.gwt.file.core.client.util.json.JsonUtils;
import com.prt.gwt.file.core.client.util.json.JsonableBaseVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseFileDataVO extends JsonableBaseVO
{
	/**
    * 
    */
	private static final long serialVersionUID = -6367982235889434075L;
	private BaseFileVO        baseFileVO;
	private List<String>      fileData;
	private boolean           changed;
	private long              activePage       = 0;
	private long              startLine;

	public BaseFileDataVO()
	{
	}

	public BaseFileDataVO(final BaseFileVO baseFileVO)
	{
		this.baseFileVO = baseFileVO;
	}

	public BaseFileVO getFileVO()
	{
		return baseFileVO;
	}

	public void setFileVO(final BaseFileVO baseFileVO)
	{
		this.baseFileVO = baseFileVO;
	}

	public List<String> getFileData()
	{
		return fileData;
	}

	public void setFileData(final List<String> data)
	{
		fileData = data;
	}

	public boolean isChanged()
	{
		return changed;
	}

	public void setChanged(final boolean changed)
	{
		this.changed = changed;
	}

	public long getActivePage()
	{
		return activePage;
	}

	public void setActivePage(final long activePage)
	{
		this.activePage = activePage;
	}

	public long getStartLine()
	{
		return startLine;
	}

	public void setStartLine(final long startLine)
	{
		this.startLine = startLine;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setData(final Map d)
	{
		baseFileVO = new BaseFileVO();
		baseFileVO.setData((Map) d.get("baseFileVO"));
		fileData = (List<String>) d.get("fileData");
		changed = JsonUtils.getBoolean(d, "changed");
		activePage = JsonUtils.getLong(d, "activePage");
		startLine = JsonUtils.getLong(d, "startLine");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object getData()
	{
		final HashMap result = new HashMap();
		result.put("baseFileVO", baseFileVO.getData());
		result.put("fileData", fileData);
		result.put("changed", changed);
		result.put("activePage", activePage);
		result.put("startLine", startLine);
		return result;
	}
}
