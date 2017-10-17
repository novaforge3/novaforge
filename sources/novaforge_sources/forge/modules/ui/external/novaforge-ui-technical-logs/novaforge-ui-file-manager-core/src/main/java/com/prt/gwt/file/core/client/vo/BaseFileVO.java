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

public class BaseFileVO extends JsonableBaseVO implements FileVO
{
	/**
    * 
    */
	private static final long serialVersionUID = 804558129409143862L;
	private boolean           isDirectory      = false;
	private String            fileName;
	private String            filePath;
	private long              fileLength;
	private String            parent;
	private long              lastModified;
	private long              linesInFile;

	public BaseFileVO()
	{
	}

	public BaseFileVO(final boolean directory, final String filePath)
	{
		isDirectory = directory;
		this.filePath = filePath;
	}

	public BaseFileVO(final FileVO fileVO)
	{
		setDirectory(fileVO.isDirectory());
		setFileLength(fileVO.getFileLength());
		setFileName(fileVO.getFileName());
		setFilePath(fileVO.getFilePath());
		setParent(fileVO.getParent());
		setLastModified(fileVO.getLastModified());
		setLinesInFile(fileVO.getLinesInFile());
	}

	@Override
	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	@Override
	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(final String filePath)
	{
		this.filePath = filePath;
	}

	@Override
	public boolean isDirectory()
	{
		return isDirectory;
	}

	public void setDirectory(final boolean directory)
	{
		isDirectory = directory;
	}

	@Override
	public long getFileLength()
	{
		return fileLength;
	}

	public void setFileLength(final long fileLength)
	{
		this.fileLength = fileLength;
	}

	@Override
	public String getParent()
	{
		return parent;
	}

	public void setParent(final String parent)
	{
		this.parent = parent;
	}

	@Override
	public long getLastModified()
	{
		return lastModified;
	}

	public void setLastModified(final long lastModified)
	{
		this.lastModified = lastModified;
	}

	@Override
	public long getLinesInFile()
	{
		return linesInFile;
	}

	public void setLinesInFile(final long linesInFile)
	{
		this.linesInFile = linesInFile;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Map getData()
	{
		final Map data = new HashMap();
		data.put("isDirectory", isDirectory);
		data.put("fileName", fileName);
		data.put("filePath", filePath);
		data.put("fileLength", fileLength);
		data.put("parent", parent);
		data.put("lastModified", lastModified);
		data.put("linesInFile", linesInFile);
		return data;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void setData(final Map d)
	{
		isDirectory = (Boolean) d.get("isDirectory");
		fileName = (String) d.get("fileName");
		filePath = (String) d.get("filePath");
		fileLength = JsonUtils.getLong(d, "fileLength");
		parent = (String) d.get("parent");
		lastModified = JsonUtils.getLong(d, "lastModified");
		linesInFile = JsonUtils.getLong(d, "linesInFile");
	}
}
