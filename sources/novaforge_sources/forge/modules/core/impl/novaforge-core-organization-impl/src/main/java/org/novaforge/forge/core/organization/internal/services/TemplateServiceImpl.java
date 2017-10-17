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
package org.novaforge.forge.core.organization.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;
import org.novaforge.forge.core.organization.dao.ProjectElementDAO;
import org.novaforge.forge.core.organization.dao.TemplateDAO;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateInstance;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;
import org.novaforge.forge.core.organization.services.TemplateService;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Implementation of {@link TemplateService}
 *
 * @author Guillaume Lamirand
 * @author sbenoist
 * @see TemplateService
 */
public class TemplateServiceImpl implements TemplateService
{

  private static final Log LOGGER = LogFactory.getLog(TemplateServiceImpl.class);
  /**
   * Reference to {@link TemplateDAO} service injected by the container
   */
  private TemplateDAO       templateDAO;
  /**
   * Reference to {@link ProjectElementDAO} service injected by the container
   */
  private ProjectElementDAO projectElementDAO;
  /**
   * Reference to {@link ValidationService} service injected by the container
   */
  private ValidationService validationService;

  @Override
  public Template newTemplate()
  {
    return templateDAO.newTemplate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createTemplate(final Template pTemplate) throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input - template_id : " + pTemplate.getElementId());
      LOGGER.debug("Input - template_name : " + pTemplate.getName());
      LOGGER.debug("Input - template_description : " + pTemplate.getDescription());
    }

    try
    {
      checkTemplate(pTemplate);

      // set template's status
      pTemplate.setStatus(TemplateProjectStatus.IN_PROGRESS);

      templateDAO.persist(pTemplate);
    }
    catch (final Exception e)
    {
      if (e instanceof TemplateServiceException)
      {
        throw (TemplateServiceException) e;
      }
      else
      {
        throw new TemplateServiceException("a technical error occured", e);
      }
    }

  }

  private void checkTemplate(final Template pTemplate) throws TemplateServiceException
  {

    // validate the bean
    final ValidatorResponse response = validationService.validate(Template.class, pTemplate);
    if (!response.isValid())
    {
      LOGGER.error(ExceptionCode.ERR_VALIDATION_BEAN.toString() + " : " + response.getMessage());
      throw new TemplateServiceException(ExceptionCode.ERR_VALIDATION_BEAN, response.getMessage());
    }

    // check another project with the same project id doesn't already exist
    if (projectElementDAO.existId(pTemplate.getTemplateId()))
    {
      LOGGER.error(ExceptionCode.ERR_CREATE_TEMPLATE_ID_ALREADY_EXIST.toString());
      throw new TemplateServiceException(ExceptionCode.ERR_CREATE_TEMPLATE_ID_ALREADY_EXIST, String.format(
          "The template id=%s already exists", pTemplate.getTemplateId()));
    }

    // check another project with the same project name doesn't already exist
    if (projectElementDAO.existId(pTemplate.getName()))
    {
      LOGGER.error(ExceptionCode.ERR_CREATE_TEMPLATE_NAME_ALREADY_EXIST.toString());
      throw new TemplateServiceException(ExceptionCode.ERR_CREATE_TEMPLATE_NAME_ALREADY_EXIST, String.format(
          "The template name=%s already exists", pTemplate.getName()));
    }

  }

  /**
   * {@inheritDoc}
   *
   * @throws TemplateServiceException
   */
  @Override
  public List<Template> getTemplates() throws TemplateServiceException
  {
    try
    {
      return templateDAO.findAll();
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  @Override
  public List<Template> getEnableTemplates() throws TemplateServiceException
  {
    try
    {
      return templateDAO.getTemplatesByStatus(TemplateProjectStatus.ENABLE);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Template getTemplate(final String pTemplateId) throws TemplateServiceException
  {
    try
    {
      return templateDAO.findByTemplateId(pTemplateId);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException(String.format("Problem while finding the template with [id=%s]",
          pTemplateId), e);
    }
  }

  @Override
  public void updateTemplate(final Template pTemplate) throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input - template_id : " + pTemplate.getElementId());
      LOGGER.debug("Input - template_name : " + pTemplate.getName());
      LOGGER.debug("Input - template_description : " + pTemplate.getDescription());
    }

    try
    {
      templateDAO.update(pTemplate);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteTemplate(final String pTemplateId) throws TemplateServiceException
  {
    try
    {
      // Retrieve template entity
      final Template found = templateDAO.findByTemplateId(pTemplateId);
      // delete the template
      templateDAO.delete(found);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException(String.format("Problem while deleting the template with [id=%s]", pTemplateId),
                                         e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean enableTemplate(final String pTemplateId) throws TemplateServiceException
  {
    try
    {
      final Template template = templateDAO.findByTemplateId(pTemplateId);
      template.setStatus(TemplateProjectStatus.ENABLE);
      templateDAO.update(template);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException(String.format(
          "Problem while finding the template with [id=%s], so it cannot be enabled.", pTemplateId), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean disableTemplate(final String pTemplateId) throws TemplateServiceException
  {
    try
    {
      final Template template = templateDAO.findByTemplateId(pTemplateId);
      template.setStatus(TemplateProjectStatus.IN_PROGRESS);
      templateDAO.update(template);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException(String.format(
          "Problem while finding the template with [id=%s], so it can be disabled.", pTemplateId), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   *
   * @throws TemplateServiceException
   */
  @Override
  public Template getTemplateForProject(final String pProjectId) throws TemplateServiceException
  {
    Template returnTemplate = null;
    try
    {
      final TemplateInstance instance = templateDAO.findByProjectId(pProjectId);
      if (instance != null)
      {
        returnTemplate = instance.getTemplate();
      }
    }
    catch (final NoResultException e)
    {
      // Just return <code>null</code> in this case
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
    return returnTemplate;
  }

  /**
   * {@inheritDoc}
   */

  /**
   * {@inheritDoc}
   *
   * @throws TemplateServiceException
   */
  @Override
  public void deleteTemplateInstance(final String pProjectId) throws TemplateServiceException
  {
    try
    {
      final TemplateInstance templateInstance = templateDAO.findByProjectId(pProjectId);

      if (templateInstance != null)
      {
        // delete the template instance
        templateDAO.delete(templateInstance);
      }
    }
    catch (final NoResultException e)
    {
      // Nothing to do in this case
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existTemplate(final String pTemplateId) throws TemplateServiceException
  {
    try
    {
      return projectElementDAO.existId(pTemplateId);
    }
    catch (Exception e)
    {
      throw new TemplateServiceException("a technical erro occurred", e);
    }
  }

  /**
   * Use by container to inject {@link TemplateDAO} implementation
   *
   * @param pTemplateDAO
   *          the templateDAO to set
   */
  public void setTemplateDAO(final TemplateDAO pTemplateDAO)
  {
    templateDAO = pTemplateDAO;
  }

  /**
   * Use by container to inject {@link ProjectElementDAO} implementation
   *
   * @param pProjectElementDAO
   *          the projectElementDAO to set
   */
  public void setProjectElementDAO(final ProjectElementDAO pProjectElementDAO)
  {
    projectElementDAO = pProjectElementDAO;
  }

  /**
   * Use by container to inject {@link ValidationService} implementation
   *
   * @param pValidationService
   *          the validationService to set
   */
  public void setValidationService(final ValidationService pValidationService)
  {
    validationService = pValidationService;
  }
}
