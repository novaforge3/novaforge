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
package com.prt.gwt.file.core.client.util.decorator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ServerLogDecorator implements HtmlDecorator
{
	private final static String       TAB_SYMBOL = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	private final static String       RED_SPAN   = "<span style=\"color: red;\">" + DATA_MARKER + "</span>";
	private final static String       BLUE_SPAN  = "<span style=\"color: blue;\">" + DATA_MARKER + "</span>";
	private final static String       LINE_SPAN  = "<span id=\"line_" + DATA_MARKER + "\">" + DATA_MARKER
	                                                 + "</span>";
	private final Map<String, String> wrapRules;
	private final Map<String, String> replaceRules;

	public ServerLogDecorator()
	{
		wrapRules = new LinkedHashMap<String, String>();
		replaceRules = new LinkedHashMap<String, String>();
		wrapRules.put("\tat", RED_SPAN);
		wrapRules.put(TAB_SYMBOL + "at", RED_SPAN);
		wrapRules.put("Exception", RED_SPAN);
		wrapRules.put("exception", RED_SPAN);
		wrapRules.put("ERROR", RED_SPAN);
		wrapRules.put("SEVERE", RED_SPAN);
		wrapRules.put("ORA-", RED_SPAN);
		wrapRules.put("WARN", BLUE_SPAN);
		replaceRules.put("\t", TAB_SYMBOL);
	}

	@Override
	public String[] decorateContentLine(final long line, final String src)
	{
		String lineString = quoteReplacement(src);
		for (final Entry<String, String> entry : replaceRules.entrySet())
		{
			lineString = lineString.replaceAll(entry.getKey(), entry.getValue());
		}

		for (final Entry<String, String> entry : wrapRules.entrySet())
		{
			if (lineString.indexOf(entry.getKey()) >= 0)
			{
				lineString = entry.getValue().replaceAll(DATA_MARKER, quoteReplacement(lineString));
			}
		}
		return new String[] { LINE_SPAN.replaceAll(DATA_MARKER, String.valueOf(line)), lineString };
	}

	/**
	 * Copy-pasted from Matcher!
	 * 
	 * @param s
	 *          The string to be literalized
	 * @return A literal string replacement
	 */
	public static String quoteReplacement(final String s)
	{
		if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1))
		{
			return s;
		}
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++)
		{
			final char c = s.charAt(i);
			if (c == '\\')
			{
				sb.append('\\');
				sb.append('\\');
			}
			else if (c == '$')
			{
				sb.append('\\');
				sb.append('$');
			}
			else
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
