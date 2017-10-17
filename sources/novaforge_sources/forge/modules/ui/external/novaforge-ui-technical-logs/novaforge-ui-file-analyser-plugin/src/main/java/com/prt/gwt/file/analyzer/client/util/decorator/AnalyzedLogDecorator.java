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
package com.prt.gwt.file.analyzer.client.util.decorator;

import com.prt.gwt.file.core.client.util.decorator.ServerLogDecorator;

public class AnalyzedLogDecorator extends ServerLogDecorator
{
	private final static String UNDERLINE_SPAN = "<span style=\"text-decoration: underline; background-color: yellow;\">"
	                                               + DATA_MARKER + "</span>";
	private String              underligneText;
	private String              replacementString;
	private long                targetLine;

	public void setUnderligneText(final String underligneText)
	{
		this.underligneText = underligneText;
		replacementString = UNDERLINE_SPAN.replaceAll(DATA_MARKER, underligneText);
	}

	public void setTargetLine(final long line)
	{
		targetLine = line;
	}

	@Override
	public String[] decorateContentLine(final long line, final String src)
	{
		final String[] result = super.decorateContentLine(line, src);
		if (result[1].indexOf(underligneText) >= 0)
		{
			result[0] = UNDERLINE_SPAN.replaceAll(DATA_MARKER, result[0]);
			result[1] = result[1].replaceAll(underligneText, replacementString);
		}

		if (line == targetLine)
		{
			result[0] = TARGET_LINE_SPAN.replaceAll(DATA_MARKER, result[0]);
		}

		return result;
	}
}
