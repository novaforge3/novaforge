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

import org.novaforge.forge.plugins.scm.svn.domain.AffectedPath;

/**
 * @author rols-p
 */
public class AffectedPathDTO implements AffectedPath
{

	private String action;
	private String affectedPath;
	private String description;

	public AffectedPathDTO()
	{
	}

	public AffectedPathDTO(final String action, final String affectedPath)
	{
		this.action = action;
		this.affectedPath = affectedPath;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAction()
	{
		return action;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAction(final String action)
	{
		this.action = action;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAffectedPath()
	{
		return affectedPath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAffectedPath(final String affectedPath)
	{
		this.affectedPath = affectedPath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "AffectedPathDTO [action=" + action + ", affectedPath=" + affectedPath + ", description=" + description
							 + "]";
	}

}
