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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sbenoist
 */
public class SVNNodeEntryDTO
{
	private String                author;

	private String                revision;

	private String                path;

	private Date                  date;

	private boolean               isDirectory = false;

	private List<SVNNodeEntryDTO> children    = new ArrayList<SVNNodeEntryDTO>();

	/**
	 * {@inheritDoc}
	 */
	public String getAuthor()
	{
		return author;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAuthor(final String pAuthor)
	{
		author = pAuthor;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getRevision()
	{
		return revision;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRevision(final String pRevision)
	{
		revision = pRevision;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPath(final String pPath)
	{
		path = pPath;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDate(final Date pDate)
	{
		date = pDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDirectory()
	{
		return isDirectory;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDirectory(final boolean pIsDirectory)
	{
		isDirectory = pIsDirectory;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SVNNodeEntryDTO> getChildren()
	{
		return children;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setChildren(final List<SVNNodeEntryDTO> pChildren)
	{
		children = pChildren;
	}

	public void addChild(final SVNNodeEntryDTO pNodeEntry)
	{
		children.add(pNodeEntry);
	}

	public void removeChild(final SVNNodeEntryDTO pNodeEntry)
	{
		children.remove(pNodeEntry);
	}

	@Override
	public String toString()
	{
		return "ScmNodeEntryDTO [author=" + author + ", children=" + children + ", date=" + date + ", isDirectory="
							 + isDirectory + ", path=" + path + ", revision=" + revision + "]";
	}

}
