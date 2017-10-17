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
package com.prt.gwt.file.core.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.UIObject;

public class CssUtil
{
	private static final String ACTIVE_STATE_PROPERTY = "active_state";
	private static final String DEFAULT_TOOL_PREFIX   = "tool";
	private static final String TOOL_ACTIVE_CSS       = "-active";
	private static final String TOOL_PASSIVE_CSS      = "-passive";

	public static String getUserAgentBasedCss(final String className)
	{
		return GWT.<CssUtilImplCommon> create(CssUtilImplCommon.class).transformCssClassName(className);
	}

	public static void invertActiveState(final UIObject o)
	{
		setActiveState(o, !getActiveState(o));
	}

	public static void setActiveState(final UIObject o, final boolean active)
	{
		setActiveState(o, active, DEFAULT_TOOL_PREFIX);
	}

	public static boolean getActiveState(final UIObject o)
	{
		return o.getElement().getPropertyBoolean(ACTIVE_STATE_PROPERTY);
	}

	public static void setActiveState(final UIObject o, final boolean active, final String cssPrefix)
	{
		o.removeStyleName(active ? cssPrefix + TOOL_PASSIVE_CSS : cssPrefix + TOOL_ACTIVE_CSS);
		o.addStyleName(active ? cssPrefix + TOOL_ACTIVE_CSS : cssPrefix + TOOL_PASSIVE_CSS);
		o.getElement().setPropertyBoolean(ACTIVE_STATE_PROPERTY, active);
	}

	public static void invertActiveState(final UIObject o, final String cssPrefix)
	{
		setActiveState(o, !getActiveState(o), cssPrefix);
	}

	static class CssUtilImplCommon
	{
		public String transformCssClassName(final String className)
		{
			return className;
		}
	}

	static class CssUtilImplGeckoWebkit extends CssUtilImplCommon
	{
		@Override
		public String transformCssClassName(final String className)
		{
			return className + "-GeckoWebkit";
		}
	}

}
