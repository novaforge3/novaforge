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
package org.novaforge.forge.core.plugins.services;

import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;

import java.util.List;

/**
 * This class defines a factory to build plugin metadata object
 * 
 * @author lamirang
 */
public interface PluginMetadataFactory
{
  /**
   * This method allows to create plugin metadata from plugin service metadata
   * 
   * @param pPluginServiceMetadata
   *          represents plugin service metadata used to create plugin metadata
   * @return plugin metadata object
   */
  PluginMetadata createPluginMetadata(final PluginServiceMetadata pPluginServiceMetadata);

  /**
   * This method allows to create plugins metadata from plugin persistence metadata
   * 
   * @param pCompletedPluginMetadata
   * @return PluginPersistenceMetadata object
   */
  PluginMetadata createPluginMetadata(final PluginPersistenceMetadata pCompletedPluginMetadata);

  /**
   * This method allows to create plugin metada from plugin persistence metadata
   * 
   * @param pCompletedPluginMetadaList
   *          <PluginPersistenceMetadata> pPluginMetadaList
   * @return List<PluginMetadata> a list of plugins
   */
  List<PluginMetadata> createPluginMetadataList(
      final List<PluginPersistenceMetadata> pCompletedPluginMetadaList);

  /**
   * This method allows to update persisted plugin metadata from plugin metadata. Only information store in
   * {@link PluginMetadata} can be updated. So there aren't any status or availability
   * 
   * @param pPluginPersistenceMetadata
   *          the PluginPersistenceMetadata to update
   * @param pPluginServiceMetadata
   *          the PluginServiceMetadata to use
   * @return {@link PluginPersistenceMetadata} updated
   */
  PluginPersistenceMetadata updatePluginPersistenceMetadata(
      final PluginPersistenceMetadata pPluginPersistenceMetadata,
      final PluginServiceMetadata pPluginServiceMetadata);
}
