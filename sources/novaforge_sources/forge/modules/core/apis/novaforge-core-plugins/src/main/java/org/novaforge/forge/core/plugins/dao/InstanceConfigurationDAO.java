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
package org.novaforge.forge.core.plugins.dao;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to instance data from database
 * 
 * @author lamirang
 */
public interface InstanceConfigurationDAO
{

  /**
   * This method allows to find an plugin instance from specific forge id
   * 
   * @param pForge
   *          represents forge id
   * @return list of instance found regarding forge id
   */
  List<InstanceConfiguration> findByForgeId(final String pForge);

  /**
   * This method allows to find an plugin instance from specific instance id
   * 
   * @param pInstanceID
   *          represents instance id
   * @return an instance found regarding isntance id
   * @throws NoResultException
   */
  InstanceConfiguration findByInstanceId(final String pInstanceID) throws NoResultException;

  /**
   * This method allows to find an plugin instance from specific forge project id
   * 
   * @param pForgeProject
   *          represents project id
   * @return list of instance found regarding project id
   */
  List<InstanceConfiguration> findByProject(final String pForgeProject);

  /**
   * This method allows to find an plugin instance from specific tool project id
   * 
   * @param pToolProject
   * @return instance found regarding project id
   * @throws NoResultException
   */
  InstanceConfiguration findByToolProject(final String pToolProject) throws NoResultException;

  /**
   * This method will persist the object given in parameter
   * 
   * @param pInstanceConfiguration
   *          the instance to persist
   * @return {@link pInstanceConfiguration} attached to persistence context
   */
  InstanceConfiguration persist(InstanceConfiguration pInstanceConfiguration);

  /**
   * This method will update the object given in parameter
   * 
   * @param pInstanceConfiguration
   *          the instance to persist
   * @return {@link pInstanceConfiguration} attached to persistence context
   */
  InstanceConfiguration update(InstanceConfiguration pInstanceConfiguration);

  /**
   * Will delete the {@link InstanceConfiguration} given from persistence context
   * 
   * @param pInstanceConfiguration
   *          the instance to delete
   */
  void delete(final InstanceConfiguration pInstanceConfiguration);

}
