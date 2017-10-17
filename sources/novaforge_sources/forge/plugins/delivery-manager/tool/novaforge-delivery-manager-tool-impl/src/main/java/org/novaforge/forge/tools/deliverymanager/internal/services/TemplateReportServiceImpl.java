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
package org.novaforge.forge.tools.deliverymanager.internal.services;

import org.novaforge.forge.tools.deliverymanager.dao.TemplateReportDAO;
import org.novaforge.forge.tools.deliverymanager.entity.TemplateCustomFieldEntity;
import org.novaforge.forge.tools.deliverymanager.entity.TemplateReportEntity;
import org.novaforge.forge.tools.deliverymanager.exceptions.TemplateReportServiceException;
import org.novaforge.forge.tools.deliverymanager.facades.DeliveryService;
import org.novaforge.forge.tools.deliverymanager.facades.TemplateReportService;
import org.novaforge.forge.tools.deliverymanager.model.TemplateCustomField;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This is a concret implement of {@link DeliveryService}
 * 
 * @see TemplateReportService
 * @author Guillaume Lamirand
 */
public class TemplateReportServiceImpl implements TemplateReportService
{
	private TemplateReportDAO templateReportDAO;

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity
	 */
	@Override
	public TemplateReport newTemplate()
	{
		return new TemplateReportEntity();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity
	 */
	@Override
	public TemplateCustomField newCustomField()
	{
		return new TemplateCustomFieldEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateReport creeteTemplateReport(final TemplateReport pReport)
	    throws TemplateReportServiceException
	{
		return templateReportDAO.persist(pReport);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateReport updateTemplateReport(final String pPreviousName, final TemplateReport pReport)
	    throws TemplateReportServiceException
	{
		// Delete previous template
		final TemplateReport previousTemplate = templateReportDAO.findByProjectAndName(pReport.getProjectId(),
		    pPreviousName);
		templateReportDAO.remove(previousTemplate);

		// Save new one
		return templateReportDAO.persist(pReport);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteTemplateReport(final String pProjectId, final String pName)
	    throws TemplateReportServiceException
	{
		final TemplateReport templateReport = this.getTemplateReport(pProjectId, pName);
		templateReportDAO.remove(templateReport);

		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportDAO#findByProjectAndName(String, String)
	 */
	@Override
	public TemplateReport getTemplateReport(final String pProjectId, final String pName)
	    throws TemplateReportServiceException
	{
		try
		{
			return templateReportDAO.findByProjectAndName(pProjectId, pName);
		}
		catch (final NoResultException e)
		{
			throw new TemplateReportServiceException(String.format(
			    "Unable to get template report with [project_id=%s, name=%s]", pProjectId, pName), e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportDAO#findByProject(String)
	 */
	@Override
	public List<TemplateReport> getTemplateReport(final String pProjectId)
	    throws TemplateReportServiceException
	{
		return templateReportDAO.findByProject(pProjectId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existTemplateName(final String pProjectId, final String pTemplateName)
	    throws TemplateReportServiceException
	{
		boolean returnValue = false;
		try
		{
			final TemplateReport findByProjectAndName = templateReportDAO.findByProjectAndName(pProjectId,
			    pTemplateName);
			if (findByProjectAndName != null)
			{
				returnValue = true;
			}
		}
		catch (final NoResultException e)
		{
			returnValue = false;

		}
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existTemplateFileName(final String pProjectId, final String pFileName)
	    throws TemplateReportServiceException
	{
		boolean returnValue = false;
		try
		{
			final TemplateReport templateReport = templateReportDAO.findByProjectAndFileName(pProjectId, pFileName);
			if (templateReport != null)
			{
				returnValue = true;
			}
		}
		catch (final NoResultException e)
		{
			returnValue = false;
		}
		return returnValue;
	}

	/**
	 * @param pTemplateReportDAO
	 *          the templateReportDAO to set
	 */
	public void setTemplateReportDAO(final TemplateReportDAO pTemplateReportDAO)
	{
		templateReportDAO = pTemplateReportDAO;
	}
}
