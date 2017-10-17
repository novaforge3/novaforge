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
package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateInstance;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to {@link Template} and {@link TemplateInstance} data from persistence
 * 
 * @author Guillaume Lamirand
 */
public interface TemplateDAO
{

  /****************************************
   * The following methods will manage Template
   ****************************************/
  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link Template}
   */
  Template newTemplate();

  /**
   * This method will try all {@link Template} stored with status given
   * 
   * @param pTemplateStatus
   *          the status
   * @return {@link List} of {@link Template} stored with the status given, can be empty
   */
  List<Template> getTemplatesByStatus(final TemplateProjectStatus pTemplateStatus);

  /**
   * This method will try all {@link Template} stored
   * 
   * @return the whole {@link List} of {@link Template} stored
   */
  List<Template> findAll();

  /**
   * Find {@link Template} according to its id
   * 
   * @param pTemplateId
   *          represents the template id to find
   * @return {@link Template} found
   * @throws NoResultException
   *           thrown if no {@link Template} are existing for the id given
   */
  Template findByTemplateId(final String pTemplateId) throws NoResultException;

  /**
   * This method will persist the object given in parameter
   * 
   * @param pTemplate
   *          the template to persist
   * @return {@link Template} attached to persistence context
   */
  Template persist(Template pTemplate);

  /**
   * This method will update the object given in parameter
   * 
   * @param pTemplate
   *          the template to update
   * @return {@link Template} attached to persistence context
   */
  Template update(Template pTemplate);

  /**
   * This method will delete the object given in parameter
   * 
   * @param pTemplate
   *          the template to delete
   */
  void delete(final Template pTemplate);

  /****************************************
   * The following methods will manage TemplateInstance
   ****************************************/

  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link TemplateInstance}
   */
  TemplateInstance newTemplateInstance();

  /**
   * Find {@link TemplateInstance} according to a project id given
   * 
   * @param pProjectId
   *          represents the project id
   * @return {@link TemplateInstance} found
   * @throws NoResultException
   *           thrown if no {@link TemplateInstance} are existing for the id given
   */
  TemplateInstance findByProjectId(final String pProjectId) throws NoResultException;

  /**
   * This method will persist the object given in parameter
   * 
   * @param pTemplateInstance
   *          the template instance to persist
   * @return {@link TemplateInstance} attached to persistence context
   */
  TemplateInstance persist(final TemplateInstance pTemplateInstance);

  /**
   * This method will delete the object given in parameter
   * 
   * @param pTemplateInstance
   *          the template instance to delete
   */
  void delete(final TemplateInstance pTemplateInstance);

}
