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
package org.novaforge.forge.tools.deliverymanager.services;

import org.novaforge.forge.tools.deliverymanager.exceptions.TemplateReportServiceException;
import org.novaforge.forge.tools.deliverymanager.model.TemplateCustomField;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;

import java.util.List;

public interface TemplateReportPresenter
{
	/**
	 * This method will return you an new instance of {@link TemplateReport}. It will be an empty one.
	 * 
	 * @return an new instance
	 * @see TemplateReport
	 */
	TemplateReport newTemplate();

	/**
	 * This method will return you an instance of {@link TemplateCustomField}. It will be an empty one.
	 * 
	 * @return an new instance
	 * @see TemplateCustomField
	 */
	TemplateCustomField newCustomField();

	/**
	 * This method will :
	 * <ul>
	 * <li>Check if the data given in parameter</li>
	 * <li>Call the persistence service</li>
	 * </ul>
	 * 
	 * @param pReport
	 *          represents the templatereport to persist
	 * @return template returned by the persistence service
	 * @throws TemplateReportServiceException
	 * @see TemplateReport
	 */
	TemplateReport createTemplateReport(final TemplateReport pReport) throws TemplateReportServiceException;

	/**
	 * This method will :
	 * <ul>
	 * <li>Check the data given in parameter</li>
	 * <li>Call the persistence service</li>
	 * </ul>
	 * 
	 * @param pPreviousName
	 *          represents the older template report name
	 * @param pReport
	 *          represents the template report to update
	 * @return template returned by the persistence service
	 * @throws TemplateReportServiceException
	 * @see TemplateReport
	 */
	TemplateReport updateTemplateReport(final String pPreviousName, final TemplateReport pReport)
	    throws TemplateReportServiceException;

	/**
	 * This method will delete the report according to the parameters
	 * 
	 * @param pProjectId
	 *          represents the project id of the template report
	 * @param pName
	 *          represents the template name
	 * @return true if succeed otherwise an exception is thrown
	 * @throws TemplateReportServiceException
	 */
	boolean deleteTemplateReport(final String pProjectId, final String pName)
	    throws TemplateReportServiceException;

	/**
	 * This method will get the report object according to the parameters
	 * 
	 * @param pProjectId
	 *          represents the project id of the template report
	 * @param pName
	 *          represents the template name
	 * @return persist object found
	 * @throws TemplateReportServiceException
	 *           if no object are found
	 */
	TemplateReport getTemplateReport(final String pProjectId, final String pName)
	    throws TemplateReportServiceException;

	/**
	 * This method will get the reports object according to the parameter
	 * 
	 * @param pProjectId
	 *          represents the project id of the templates
	 * @return a list of template found, can be empty but not null
	 * @throws TemplateReportServiceException
	 */
	List<TemplateReport> getTemplateReport(final String pProjectId) throws TemplateReportServiceException;

	/**
	 * This method allows to check if a template file is already existing
	 * 
	 * @param pProjectId
	 *          represents the project if
	 * @param pPreivousName
	 *          represents the current name of the template file
	 * @param pNewName
	 *          represents the new name
	 * @return true if existing otherwise false
	 * @throws TemplateReportServiceException
	 */
	boolean exitTemplateFile(final String pProjectId, final String pPreivousName, final String pNewName)
	    throws TemplateReportServiceException;

	/**
	 * Create default template for project
	 * 
	 * @param pProjectId
	 * @throws TemplateReportServiceException
	 */
	void createDefaultTemplate(String pProjectId) throws TemplateReportServiceException;

	/**
	 * Get the default template name
	 * 
	 * @return the template name
	 */
	String getTemplateDefaultName();

	/**
	 * Get the default template descripition
	 * 
	 * @return the template description
	 */
	String getTemplateDefaultDescription();

}