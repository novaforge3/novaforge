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
package org.novaforge.forge.tools.deliverymanager.facades;

import org.novaforge.forge.tools.deliverymanager.exceptions.TemplateReportServiceException;
import org.novaforge.forge.tools.deliverymanager.model.TemplateCustomField;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;

import java.util.List;

/**
 * This interface describes a service which will deal with persistence template information.
 * 
 * @author Guillaume Lamirand
 */
public interface TemplateReportService
{

	/**
	 * This method will return you an instance of the persistence object. It will be an empty one.
	 * 
	 * @return an new instance
	 * @see TemplateReport
	 */
	TemplateReport newTemplate();

	/**
	 * This method will return you an instance of the persistence object. It will be an empty one.
	 * 
	 * @return an new instance
	 * @see TemplateCustomField
	 */
	TemplateCustomField newCustomField();

	/**
	 * This method will only persist the report given
	 * 
	 * @param pReport
	 *          represents the templatereport to persist
	 * @return template persisted
	 * @throws TemplateReportServiceException
	 * @see TemplateReport
	 */
	TemplateReport creeteTemplateReport(final TemplateReport pReport) throws TemplateReportServiceException;

	/**
	 * This method will only update the report given
	 * 
	 * @param pPreviousName
	 *          represents the ole template name
	 * @param pReport
	 *          represents the template report to update
	 * @return template persisted
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
	 * This method will get the report object persisted according to the parameters
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
	 * This method will get the reports object persisted according to the parameter
	 * 
	 * @param pProjectId
	 *          represents the project id of the templates
	 * @return a list of template found, can be empty but not null
	 * @throws TemplateReportServiceException
	 */
	List<TemplateReport> getTemplateReport(final String pProjectId) throws TemplateReportServiceException;

	/**
	 * This method will check if the name given is existing or not in the templates persisted
	 * 
	 * @param pProjectId
	 *          represents the project id of the templates
	 * @param pTemplateName
	 *          represents the name of the template
	 * @return true if existing otherwise false
	 * @throws TemplateReportServiceException
	 */
	boolean existTemplateName(String pProjectId, String pTemplateName) throws TemplateReportServiceException;

	/**
	 * This method will check if the file name given is already used or not by a templates persisted
	 * 
	 * @param pProjectId
	 *          represents the project id of the templates
	 * @param pFileName
	 *          represents the file name of the template report
	 * @return true if existing otherwise false
	 * @throws TemplateReportServiceException
	 */
	boolean existTemplateFileName(String pProjectId, String pFileName) throws TemplateReportServiceException;

}