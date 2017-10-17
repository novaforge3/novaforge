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

import com.prt.gwt.file.core.client.util.json.JsonUtils;
import com.prt.gwt.file.core.client.vo.BaseFileVO;
import com.prt.gwt.file.core.client.vo.FileVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AnalyzedFileVO extends BaseFileVO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8908421701493260293L;
	private Map<Long, String> entries;
	private Long              selectedEntry;

	public AnalyzedFileVO()
	{
	}

	public AnalyzedFileVO(final FileVO fileVO)
	{
		setDirectory(fileVO.isDirectory());
		setFileLength(fileVO.getFileLength());
		setFileName(fileVO.getFileName());
		setFilePath(fileVO.getFilePath());
		setParent(fileVO.getParent());
		setLastModified(fileVO.getLastModified());
	}

	public Map<Long, String> getEntries()
	{
		return entries;
	}

	public void setEntries(final Map<Long, String> entries)
	{
		this.entries = entries;
	}

	public Long getSelectedEntry()
	{
		return selectedEntry;
	}

	public void setSelectedEntry(final Long selectedEntry)
	{
		this.selectedEntry = selectedEntry;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map getData()
	{
		final Map data = super.getData();
		data.put("entries", entries);
		data.put("selectedEntry", selectedEntry);
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setData(final Map jsonObject)
	{
		super.setData(jsonObject);
		// KEY WILL ALWAYS BE STRING!!
		final Map<String, String> map = (Map<String, String>) jsonObject.get("entries");
		entries = new HashMap<Long, String>();
		for (final Entry<String, String> entry : map.entrySet())
		{
			entries.put(Long.valueOf(entry.getKey()), entry.getValue());
		}
		selectedEntry = JsonUtils.getLong(jsonObject, "selectedEntry");
	}

}
