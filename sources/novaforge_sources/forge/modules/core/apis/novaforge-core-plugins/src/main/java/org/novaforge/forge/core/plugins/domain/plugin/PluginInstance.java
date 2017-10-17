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

/**
 * This class describe object which will be used during communication between forge core and a specific plugin
 * 
 * @author lamirang
 */
public interface PluginInstance
{
  /**
   * Return forge id
   * 
   * @return the ForgeId
   */
  String getForgeId();

  /**
   * That allows to set the forge id
   * 
   * @param pForgeId
   *          the pForgeId to set
   */
  void setForgeId(final String pForgeId);

  /**
   * Return the instance id
   * 
   * @return the pInstanceId
   */
  String getInstanceId();

  /**
   * That allows to set the instance id
   * 
   * @param pInstanceId
   *          the pInstanceId to set
   */
  void setInstanceId(final String pInstanceId);

  /**
   * Return the project id used by the forge
   * 
   * @return the pForgeProjectId
   */
  String getForgeProjectId();

  /**
   * That allows to set project id used by the forge
   * 
   * @param pForgeProjectId
   *          the pForgeProjectId to set
   */
  void setForgeProjectId(final String pForgeProjectId);

  /**
   * Return the instance's label
   * 
   * @return the instanceLabel
   */
  String getInstanceLabel();

  /**
   * That allows to set instance's label
   * 
   * @param pInstanceLabel
   *          the instanceLabel to set
   */
  void setInstanceLabel(final String pInstanceLabel);

  /**
   * @return the tool instance id associated
   */
  String getToolInstanceId();

  /**
   * That allows to set tool instance id associated
   * 
   * @param pToolInstanceId
   */
  void setToolInstanceId(String pToolInstanceId);
}
