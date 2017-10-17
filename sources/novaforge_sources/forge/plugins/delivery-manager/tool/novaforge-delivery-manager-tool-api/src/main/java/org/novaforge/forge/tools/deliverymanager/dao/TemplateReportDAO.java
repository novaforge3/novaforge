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
package org.novaforge.forge.tools.deliverymanager.dao;

import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to template report object from database
 * 
 * @see TemplateReport
 * @author Guillaume Lamirand
 */
public interface TemplateReportDAO
{

	/**
	 * This method allows to get template available for a project id and a name given. It will throw a
	 * exception if there is no result
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @param pName
	 *          represents the template name we want to fin
	 * @return the template report entity regarding project id and name given
	 * @throws NoResultException
	 *           is thrown if no result
	 */
	TemplateReport findByProjectAndName(final String pProjectId, final String pName) throws NoResultException;

	/**
	 * This method allows to get template list available for a project id given. It will return null i there is
	 * no result
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @return a list of template report found, if no result are found the list is null
	 * @throws DataAccessException
	 */
	List<TemplateReport> findByProject(final String pProjectId);

	/**
	 * This method allows to get template available for a project id and a filename given. It will throw a
	 * exception if there is no result
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @param pFileName
	 *          represents the template name we want to fin
	 * @return the template report entity regarding project id and name given
	 * @throws NoResultException
	 *           is thrown if no result
	 */
	TemplateReport findByProjectAndFileName(final String pProjectId, final String pFileName)
	    throws NoResultException;

	/**
	 * Persist {@link TemplateReport} into persistence context
	 * 
	 * @param pReport
	 * @return {@link TemplateReport} attached
	 */
	TemplateReport persist(TemplateReport pReport);

	/**
	 * Update {@link TemplateReport} into persistence context
	 * 
	 * @param pReport
	 */
	void remove(TemplateReport pReport);

}