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

public class ProfileVO extends JsonableBaseVO
{

	/**
    * 
    */
	private static final long serialVersionUID = -4944905221799063778L;
	private String            appName;
	private String            appUrl;
	private long              linesToShow;
	private String            defaultFileMask;

	public ProfileVO()
	{
	}

	public String getAppName()
	{
		return appName;
	}

	public void setAppName(final String appName)
	{
		this.appName = appName;
	}

	public String getAppUrl()
	{
		return appUrl;
	}

	public void setAppUrl(final String appUrl)
	{
		this.appUrl = appUrl;
	}

	public long getLinesToShow()
	{
		return linesToShow;
	}

	public void setLinesToShow(final long linesToShow)
	{
		this.linesToShow = linesToShow;
	}

	public String getDefaultFileMask()
	{
		return defaultFileMask;
	}

	public void setDefaultFileMask(final String defaultFileMask)
	{
		this.defaultFileMask = defaultFileMask;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object getData()
	{
		final Map d = new HashMap();
		d.put("appName", appName);
		d.put("appUrl", appUrl);
		d.put("linesToShow", linesToShow);
		d.put("defaultFileMask", defaultFileMask);
		return d;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void setData(final Map d)
	{
		appName = (String) d.get("appName");
		appUrl = (String) d.get("appUrl");
		linesToShow = JsonUtils.getLong(d, "linesToShow");
		defaultFileMask = (String) d.get("defaultFileMask");

	}
}
