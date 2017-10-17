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
package org.novaforge.forge.core.plugins.domain.plugin;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;
import org.novaforge.forge.core.plugins.domain.route.PluginQueues;

import java.io.Serializable;
import java.util.List;

/**
 * This class defines model to describe plugin service metadata
 * 
 * @author lamirang
 */
public interface PluginServiceMetadata extends Serializable
{

  /**
   * This method allows to get plugin's UUID
   * 
   * @return plugin's UUID
   */
  @Historizable(label = "uuid")
  String getUUID();

  /**
   * This method allows to get plugin's description
   * 
   * @return plugin's description
   */
  String getDescription();

  /**
   * This method allows to get plugin's type
   * 
   * @return plugin's type
   */
  @Historizable(label = "type")
  String getType();

  /**
   * This method allows to get plugin's category
   * 
   * @return plugin's category
   */
  @Historizable(label = "category")
  String getCategory();

  /**
   * This method returns JMS queues used to propage information to plugin
   * 
   * @return plugin jms queues
   */
  PluginQueues getJMSQueues();

  /**
   * This method returns avalaible views
   * 
   * @return plugin views
   */
  List<PluginView> getPluginViews();

  /**
   * This method returns the plugin version
   * 
   * @return version
   */
  String getVersion();
}
