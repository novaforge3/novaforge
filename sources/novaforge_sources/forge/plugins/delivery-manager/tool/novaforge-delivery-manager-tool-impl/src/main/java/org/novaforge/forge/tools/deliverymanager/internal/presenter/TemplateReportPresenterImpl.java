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
package org.novaforge.forge.tools.deliverymanager.internal.presenter;

import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.exceptions.ExceptionCode;
import org.novaforge.forge.tools.deliverymanager.exceptions.TemplateReportServiceException;
import org.novaforge.forge.tools.deliverymanager.facades.TemplateReportService;
import org.novaforge.forge.tools.deliverymanager.model.TemplateCustomField;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryRepositoryService;
import org.novaforge.forge.tools.deliverymanager.services.TemplateReportPresenter;

import java.io.File;
import java.util.List;

public class TemplateReportPresenterImpl implements TemplateReportPresenter
{
	private final String default_template_name = "default";
	private final String default_template_desc = "default template";
	private TemplateReportService     templateReportService;
	private ValidationService         validationService;
	private DeliveryRepositoryService deliveryRepositoryService;

	/**
	 * @param pTemplateReportService
	 *          the templateReportService to set
	 */
	public void setTemplateReportService(final TemplateReportService pTemplateReportService)
	{
		templateReportService = pTemplateReportService;
	}

	/**
	 * @param pValidationService
	 *          the validationService to set
	 */
	public void setValidationService(final ValidationService pValidationService)
	{
		validationService = pValidationService;
	}

	/**
	 * Check if a template object is valid to be created or updated
	 * 
	 * @throws DeliveryServiceException
	 */
	private void checkTemplateReport(final String pPreviousName, final TemplateReport pTemplateReport)
	    throws TemplateReportServiceException
	{
		// validate the bean
		final ValidatorResponse templateResponse = validationService.validate(TemplateReport.class,
		    pTemplateReport);
		if (!templateResponse.isValid())
		{
			throw new TemplateReportServiceException(templateResponse.getMessage(),
			    ExceptionCode.ERR_VALIDATION_BEAN);
		}
		for (final TemplateCustomField field : pTemplateReport.getFields())
		{
			final ValidatorResponse fieldResponse = validationService.validate(TemplateCustomField.class, field);
			if (!fieldResponse.isValid())
			{
				throw new TemplateReportServiceException(fieldResponse.getMessage(),
				    ExceptionCode.ERR_VALIDATION_BEAN);
			}

		}

		// Check the template name
		if ((pPreviousName == null) || (!pPreviousName.equals(pTemplateReport.getName())))
		{
			final boolean existName = templateReportService.existTemplateName(pTemplateReport.getProjectId(),
			    pTemplateReport.getName());
			if (existName)
			{
				throw new TemplateReportServiceException("The name given is already used",
				    ExceptionCode.TEMPLATE_NAME_ALREADY_EXITS);
			}
		}
	}

	/**
	 * @param pDeliveryRepositoryService
	 *          the repositoryService to set
	 */
	public void setDeliveryRepositoryService(final DeliveryRepositoryService pDeliveryRepositoryService)
	{
		deliveryRepositoryService = pDeliveryRepositoryService;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportService#newTemplate()
	 */
	@Override
	public TemplateReport newTemplate()
	{
		return templateReportService.newTemplate();
	}



	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportService#newCustomField()
	 */
	@Override
	public TemplateCustomField newCustomField()
	{
		return templateReportService.newCustomField();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportService#creeteTemplateReport(TemplateReport)
	 */
	@Override
	public TemplateReport createTemplateReport(final TemplateReport pReport)
	    throws TemplateReportServiceException
	{
		// Checking data
		checkTemplateReport(null, pReport);

		// Call session facade
		return templateReportService.creeteTemplateReport(pReport);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createDefaultTemplate(final String pProjectId) throws TemplateReportServiceException
	{
		TemplateReport defaultTemplate = newTemplate();
		defaultTemplate.setProjectId(pProjectId);
		defaultTemplate.setFileName(deliveryRepositoryService.getTemplateDefaultFilename());
		defaultTemplate.setName(default_template_name);
		defaultTemplate.setDescription(default_template_desc);
		createTemplateReport(defaultTemplate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportService#updateTemplateReport(TemplateReport)
	 */
	@Override
	public TemplateReport updateTemplateReport(final String pPreviousName, final TemplateReport pReport)
	    throws TemplateReportServiceException
	{
		// Checking data
		checkTemplateReport(pPreviousName, pReport);

		// Call session facade
		return templateReportService.updateTemplateReport(pPreviousName, pReport);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportService#deleteTemplateReport(String, String)
	 */
	@Override
	public boolean deleteTemplateReport(final String pProjectId, final String pName)
	    throws TemplateReportServiceException
	{
		String templateName = pName;
		if(templateName == null || templateName.isEmpty())
		{
			templateName = default_template_name;
		}
		return templateReportService.deleteTemplateReport(pProjectId, templateName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exitTemplateFile(final String pProjectId, final String pPreviousName, final String pFileName)
	    throws TemplateReportServiceException
	{
		boolean returnValue = false;
		if ((pFileName != null) && (!pFileName.equals(pPreviousName)))
		{
			returnValue = templateReportService.existTemplateFileName(pProjectId, pFileName);

			if (returnValue)
			{
				final String path = deliveryRepositoryService.getTemplateDirectory(pProjectId);
				final File file = new File(path, pFileName);
				returnValue = file.exists();
			}
		}
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportService#getTemplateReport(String, String)
	 */
	@Override
	public TemplateReport getTemplateReport(final String pProjectId, final String pName)
	    throws TemplateReportServiceException
	{
		return templateReportService.getTemplateReport(pProjectId, pName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportService#getTemplateReport(String)
	 */
	@Override
	public List<TemplateReport> getTemplateReport(final String pProjectId)
	    throws TemplateReportServiceException
	{
		return templateReportService.getTemplateReport(pProjectId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTemplateDefaultName()
	{
		return default_template_name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTemplateDefaultDescription()
	{
		return default_template_desc;
	}

}
