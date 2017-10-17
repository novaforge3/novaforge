/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.commands.datasource.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class describes a element used to display result of command executed
 * 
 * @author Guillaume Lamirand
 */
public class ShellTable
{
	/**
	 * Contains the headers
	 */
	private final List<String>				header			= new ArrayList<String>();
	/**
	 * Contains the headers
	 */
	private final List<List<String>>	content			= new ArrayList<List<String>>();
	/**
	 * Defines the max column size
	 */
	private static int								maxColSize	= 65;

	/**
	 * Allows to add a new empty row to contents
	 * 
	 * @return empty list already added to content
	 */
	public List<String> addRow()
	{
		List<String> row = new ArrayList<String>();
		content.add(row);
		return row;
	}

	/**
	 * Will print the all table
	 */
	public void print()
	{
		// Defines size of each column, depending on the final content
		int[] sizes = new int[header.size()];
		updateSizes(sizes, header);
		for (List<String> row : content)
		{
			updateSizes(sizes, row);
		}
		// Draw table
		String headerLine = getRow(sizes, header, " | ");
		System.out.println(headerLine);
		System.out.println(underline(headerLine.length()));
		for (List<String> row : content)
		{
			System.out.println(getRow(sizes, row, " | "));
		}
	}

	private String underline(final int length)
	{
		char[] exmarks = new char[length];
		Arrays.fill(exmarks, '-');
		return new String(exmarks);
	}

	private String getRow(final int[] pSizes, final List<String> pRow, final String pSeparator)
	{
		StringBuilder line = new StringBuilder();
		int c = 0;
		for (String cell : pRow)
		{
			if (cell == null)
			{
				cell = "";
			}
			if (cell.length() > maxColSize)
			{
				cell = cell.substring(0, maxColSize - 1);
			}
			cell = cell.replaceAll("\n", "");
			line.append(String.format("%-" + pSizes[c] + "s", cell));
			if (c + 1 < pRow.size())
			{
				line.append(pSeparator);
			}
			c++;
		}
		return line.toString();
	}

	/**
	 * This method will update the size of each column according the max column size define
	 * 
	 * @param pSizes
	 *          previous size defined
	 * @param pRow
	 *          current row
	 */
	private void updateSizes(final int[] pSizes, final List<String> pRow)
	{
		int c = 0;
		for (String cellContent : pRow)
		{
			int cellSize = cellContent != null ? cellContent.length() : 0;
			cellSize = Math.min(cellSize, maxColSize);
			if (cellSize > pSizes[c])
			{
				pSizes[c] = cellSize;
			}
			c++;
		}
	}

	/**
	 * @return the header list
	 */
	public List<String> getHeader()
	{
		return header;
	}

	/**
	 * @return the content list containing each column
	 */
	public List<List<String>> getContent()
	{
		return content;
	}
}
