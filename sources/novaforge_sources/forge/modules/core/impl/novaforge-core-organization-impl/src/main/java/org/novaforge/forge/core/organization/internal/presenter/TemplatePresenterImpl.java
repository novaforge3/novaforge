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
package org.novaforge.forge.core.organization.internal.presenter;

import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.presenters.TemplatePresenter;
import org.novaforge.forge.core.organization.services.TemplateService;

import java.util.List;

/**
 * Implementation of {@link TemplatePresenter}
 * 
 * @author Guillaume Lamirand
 * @see TemplatePresenter
 */
public class TemplatePresenterImpl implements TemplatePresenter
{

  // FIXME We should manage some authorizations
  /**
   * Reference to {@link TemplateService} service injected by the container
   */
  private TemplateService templateService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Template newTemplate()
  {
    return templateService.newTemplate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createTemplate(final Template pTemplate) throws TemplateServiceException
  {
    templateService.createTemplate(pTemplate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Template> getEnableTemplates() throws TemplateServiceException
  {
    return templateService.getEnableTemplates();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Template> getTemplates() throws TemplateServiceException
  {
    return templateService.getTemplates();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Template getTemplate(final String pTemplateId) throws TemplateServiceException
  {
    return templateService.getTemplate(pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateTemplate(final Template pTemplate) throws TemplateServiceException
  {
    templateService.updateTemplate(pTemplate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean enableTemplate(final String pTemplateId) throws TemplateServiceException
  {
    return templateService.enableTemplate(pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean disableTemplate(final String pTemplateId) throws TemplateServiceException
  {
    return templateService.disableTemplate(pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteTemplate(final String pTemplateId) throws TemplateServiceException
  {
    templateService.deleteTemplate(pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existTemplate(final String pTemplateId) throws TemplateServiceException
  {
    return templateService.existTemplate(pTemplateId);
  }

  /**
   * Use by container to inject {@link TemplateService} implementation
   * 
   * @param pTemplateService
   *          the templateService to set
   */
  public void setTemplateService(final TemplateService pTemplateService)
  {
    templateService = pTemplateService;
  }

}
