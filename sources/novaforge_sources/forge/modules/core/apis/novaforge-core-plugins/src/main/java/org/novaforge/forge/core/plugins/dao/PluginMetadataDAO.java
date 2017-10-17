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

import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

/**
 * This class defines methods to access to plugin metadata from database
 * 
 * @author lamirang
 */
public interface PluginMetadataDAO
{
  /**
   * This method allows to get a plugin metadata from its specific uuid
   * 
   * @param pUUID
   *          the plugin uuid
   * @return {@link PluginPersistenceMetadata} found
   * @throws NoResultException
   *           thrown if no result are found
   */
  PluginPersistenceMetadata findByUUID(UUID pUUID) throws NoResultException;

  /**
   * This method allows to get a list of completed plugin metadata from specific category's id
   * 
   * @param pCategoryId
   *          the categories' id
   * @return {@link List} of {@link PluginPersistenceMetadata}
   */
  List<PluginPersistenceMetadata> findByCategory(final String... pCategoryId);

  /**
   * This method allows to get a list of completed plugin metadata from category's id, plugin's status and
   * plugin's availability
   * 
   * @param pId
   *          the category id
   * @param pStatus
   *          the plugin status
   * @param pAvailability
   *          the plugin availability
   * @return {@link List} of {@link PluginPersistenceMetadata}
   */
  List<PluginPersistenceMetadata> findByCategoryAndStatusAndAvailability(final String pId,
      final PluginStatus pStatus, final boolean pAvailability);

  /**
   * This method allows to get a list of completed plugin metadata from specific type's label
   * 
   * @param pLabel
   * @return {@link List} of {@link PluginPersistenceMetadata}
   */
  List<PluginPersistenceMetadata> findByType(final String pLabel);

  /**
   * This method allows to get a list of completed plugin metadata from specific status
   * 
   * @param pStatus
   * @return {@link List} of {@link PluginPersistenceMetadata}
   */
  List<PluginPersistenceMetadata> findByStatus(final PluginStatus pStatus);

  /**
   * This method allows to get a list of completed plugin metadata from specific availability
   * 
   * @param pStatus
   * @param pAvailability
   * @return {@link List} of {@link PluginPersistenceMetadata}
   */
  List<PluginPersistenceMetadata> findByStatusAndAvailabitly(final PluginStatus pStatus,
      final boolean pAvailability);

  /**
   * This method allows to get the plugin's view according its uuid
   * 
   * @param pUUID
   *          plugin uuid
   * @return {@link List} of {@link PluginViewPersistence}
   */
  List<PluginViewEnum> findViewsByUUID(final UUID pUUID);

  /**
   * This method allows to get the list of plugin which expose the view
   * 
   * @param pViewId
   *          view id from {@link PluginViewEnum}
   * @return {@link List} of {@link PluginViewPersistence} which expose the view id
   */
  List<PluginPersistenceMetadata> findByViewId(final PluginViewEnum pViewId);

  /**
   * This method allows to check if a plugin metadata is existing
   * 
   * @param pId
   *          plugin uuid to check
   * @return true if the uuid given is existing
   */
  boolean exist(UUID pId);

  /**
   * This method allows to get the whole list of plugin
   * 
   * @return {@link List} of {@link PluginViewPersistence}
   */
  List<PluginPersistenceMetadata> findAll();

  /**
   * This method allows to get the whole list of category
   * 
   * @return {@link List} of {@link String}
   */
  List<String> findCategories();

  /**
   * @param pPluginPersistenceMetadata
   * @return
   */
  PluginPersistenceMetadata create(PluginPersistenceMetadata pPluginPersistenceMetadata);

  /**
   * @param pMetadata
   * @return
   */
  PluginPersistenceMetadata update(PluginPersistenceMetadata pMetadata);

  /**
   * @return
   */
  PluginPersistenceMetadata newEntity();

}
