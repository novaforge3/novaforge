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
package org.novaforge.forge.core.organization.services;

import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Template;

import java.util.List;

/**
 * @author sbenoist
 */
public interface TemplateService
{
  String ROLE_PROJECT_SEARCH_CRITERIA_KEY = "element";

  /**
   * This method allows to instanciate a project
   * 
   * @return {@link Template}
   */
  Template newTemplate();

  /**
   * This method allows to create a template
   * 
   * @param {@link Template}
   * @throws TemplateServiceException
   */
  void createTemplate(Template pTemplate) throws TemplateServiceException;

  /**
   * This method returns all the templates available in the forge
   * 
   * @return List<{@link Template}>
   * @throws TemplateServiceException
   */
  List<Template> getTemplates() throws TemplateServiceException;

  /**
   * This method returns all the enable templates in the forge
   * 
   * @return List<{@link Template}>
   * @throws TemplateServiceException
   */
  List<Template> getEnableTemplates() throws TemplateServiceException;

  /**
   * This method returns a template by its pTemplateId
   * 
   * @param pTemplateId
   * @return {@link Template}
   * @throws TemplateServiceException
   */
  Template getTemplate(String pTemplateId) throws TemplateServiceException;

  /**
   * This method allows to update a template
   * 
   * @param {@link Template}
   * @throws TemplateServiceException
   */
  void updateTemplate(Template pTemplate) throws TemplateServiceException;

  /**
   * This method allows to delete a template
   * 
   * @param pTemplateId
   * @throws TemplateServiceException
   */
  void deleteTemplate(String pTemplateId) throws TemplateServiceException;

  /**
   * Will enable a template from its given id
   * 
   * @return true if succeed otherwize false
   * @throws TemplateServiceException
   */
  boolean enableTemplate(final String pTemplateId) throws TemplateServiceException;

  /**
   * Will disable a template from its given id
   * 
   * @return true if succeed otherwize false
   * @throws TemplateServiceException
   */
  boolean disableTemplate(final String pTemplateId) throws TemplateServiceException;

  /**
   * @param pProjectId
   * @return
   * @throws TemplateServiceException
   */
  Template getTemplateForProject(String pProjectId) throws TemplateServiceException;

  /**
   * @param pProjectId
   * @throws TemplateServiceException
   */
  void deleteTemplateInstance(String pProjectId) throws TemplateServiceException;

  /**
   * This method returns true if the template exists, false otherwise
   * 
   * @param pTemplateId
   * @return boolean
   * @throws TemplateServiceException
   */
  boolean existTemplate(String pTemplateId) throws TemplateServiceException;

}
