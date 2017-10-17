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
package org.novaforge.forge.plugins.scm.svn.agent.dto;

/**
 * @author sbenoist
 */
public class SVNSearchResultDTO
{
	private String path;

	private String filename;

	private String className;

	private String packageName;

	private String snippet;

	private long   lineNumber;

	private String occurrence;

	/**
	 * {@inheritDoc}
	 */

	public String getPath()
	{
		return path;
	}

	public void setPath(final String pPath)
	{
		path = pPath;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(final String pFilename)
	{
		filename = pFilename;
	}

	/**
	 * {@inheritDoc}
	 */

	public String getClassName()
	{
		return className;
	}

	public void setClassName(final String pClassName)
	{
		className = pClassName;
	}

	/**
	 * {@inheritDoc}
	 */

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(final String pPackageName)
	{
		packageName = pPackageName;
	}

	/**
	 * {@inheritDoc}
	 */

	public String getSnippet()
	{
		return snippet;
	}

	public void setSnippet(final String pSnippet)
	{
		snippet = pSnippet;
	}

	/**
	 * {@inheritDoc}
	 */

	public long getLineNumber()
	{
		return lineNumber;
	}

	public void setLineNumber(final long pLineNumber)
	{
		lineNumber = pLineNumber;
	}

	public String getOccurrence()
	{
		return occurrence;
	}

	public void setOccurrence(final String occurrence)
	{
		this.occurrence = occurrence;
	}

	@Override
	public String toString()
	{
		return "SVNSearchResultDTO [path=" + path + ", className=" + className + ", packageName=" + packageName
							 + ", snippet=" + snippet + ", lineNumber=" + lineNumber + ", occurrence=" + occurrence + "]";
	}

}
