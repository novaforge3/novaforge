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

import org.novaforge.forge.plugins.scm.svn.domain.ScmLogEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author rols-p
 */
public class ScmLogEntryDTO implements ScmLogEntry
{

	private long                  revision;
	private String                author;
	private String                coment;
	private Date                  comitDate;
	private List<AffectedPathDTO> affectedPathList;

	public ScmLogEntryDTO()
	{
		super();
	}

	public ScmLogEntryDTO(final long revision, final String author, final String coment, final Date comitDate)
	{
		this.revision = revision;
		this.author = author;
		this.coment = coment;
		this.comitDate = comitDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getRevision()
	{
		return revision;
	}

	/**
	 * @param revision
	 *          the revision to set
	 */
	public void setRevision(final long revision)
	{
		this.revision = revision;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor()
	{
		return author;
	}

	/**
	 * @param author
	 *          the author to set
	 */
	public void setAuthor(final String author)
	{
		this.author = author;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getComent()
	{
		return coment;
	}

	/**
	 * @param coment
	 *          the coment to set
	 */
	public void setComent(final String coment)
	{
		this.coment = coment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getComitDate()
	{
		return comitDate;
	}

	/**
	 * @param comitDate
	 *          the comitDate to set
	 */
	public void setComitDate(final Date comitDate)
	{
		this.comitDate = comitDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AffectedPathDTO> getAffectedPathList()
	{
		if (affectedPathList == null)
		{

			affectedPathList = new ArrayList<AffectedPathDTO>();
		}
		return affectedPathList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAffectedPathList(final List<AffectedPathDTO> affectedPathList)
	{
		this.affectedPathList = affectedPathList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ScmLogEntryDTO [revision=" + revision + ", author=" + author + ", coment=" + coment + ", comitDate="
							 + comitDate + ", affectedPathList=" + affectedPathList + "]";
	}

}
