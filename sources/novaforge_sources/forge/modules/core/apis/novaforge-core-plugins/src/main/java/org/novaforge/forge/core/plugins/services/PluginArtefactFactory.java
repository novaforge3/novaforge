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

import org.novaforge.forge.core.plugins.exceptions.PluginArtefactFactoryException;

import java.util.Map;

/**
 * This interface defines a service which will be used to build an artefact from another one
 * 
 * @author lamirang
 */
public interface PluginArtefactFactory
{

  /**
   * This method allows to build an object from the given source category, notification and object using the
   * given target category and action.
   * 
   * @param pSourceCategory
   * @param pSourceNotification
   * @param pSource
   * @param pTargetCategory
   * @param pTargetAction
   * @param pTemplate
   * @return
   * @throws PluginArtefactFactoryException
   */
  Object buildTargetArtefact(String pSourceCategory, String pSourceNotification, Object pSource,
      String pTargetCategory, String pTargetAction, String pTemplate) throws PluginArtefactFactoryException;

  /**
   * This method will build a string template for an artefact with the given map.
   * 
   * @param pTemplate
   * @return template as a string
   * @throws PluginArtefactFactoryException
   */
  String buildArtefactTemplate(Map<String, String> pTemplate) throws PluginArtefactFactoryException;

}
