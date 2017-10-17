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
import java.util.Map;

public class GetFileDataVO extends JsonableBaseVO
{
	/**
    * 
    */
	private static final long serialVersionUID = 314397693936871752L;
	private BaseFileVO        file;
	private long              startWith;
	private long              lineCount;
	private boolean           tail;

	public GetFileDataVO()
	{
		super();
	}

	public GetFileDataVO(final BaseFileVO file, final long startWith, final long lineCount, final boolean tail)
	{
		super();
		this.file = file;
		this.startWith = startWith;
		this.lineCount = lineCount;
		this.tail = tail;
	}

	public BaseFileVO getFile()
	{
		return file;
	}

	public void setFile(final BaseFileVO file)
	{
		this.file = file;
	}

	public long getStartWith()
	{
		return startWith;
	}

	public void setStartWith(final long startWith)
	{
		this.startWith = startWith;
	}

	public long getLineCount()
	{
		return lineCount;
	}

	public void setLineCount(final long lineCount)
	{
		this.lineCount = lineCount;
	}

	public boolean isTail()
	{
		return tail;
	}

	public void setTail(final boolean tail)
	{
		this.tail = tail;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object getData()
	{
		final HashMap data = new HashMap();
		data.put("file", file.getData());
		data.put("startWith", startWith);
		data.put("lineCount", lineCount);
		data.put("tail", tail);
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setData(final Map d)
	{
		file = new BaseFileVO();
		file.setData((Map) d.get("file"));
		startWith = JsonUtils.getLong(d, "startWith");
		lineCount = JsonUtils.getLong(d, "lineCount");
		tail = JsonUtils.getBoolean(d, "tail");
	}

}
