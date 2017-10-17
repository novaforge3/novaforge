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
package org.novaforge.forge.distribution.reference.client;

import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;
import org.novaforge.forge.distribution.reference.model.ApplicationItem;
import org.novaforge.forge.distribution.reference.model.ReferenceProject;
import org.novaforge.forge.distribution.reference.model.ReferenceTemplates;
import org.novaforge.forge.distribution.reference.model.SynchronizationResult;
import org.novaforge.forge.distribution.reference.model.TemplateSynchroResult;
import org.novaforge.forge.distribution.reference.model.ToolsSynchronizationReport;

/**
 * Gets a proxy webservices exposed by the the forge identified bu its url in order to Synchronize the forge
 * reference space.
 * 
 * @author rols-p
 */
public interface ReferentielSynchroClient
{

  /**
   * Updates the Reference Project data inside the current forge using the incoming ReferenceProjectDTO. Also
   * propagates this updates to all its child forges if exist.
   * 
   * @param refProjectDTO
   * @return a Synchronization result for this forge.
   * @throws ReferenceServiceException
   */
  SynchronizationResult updateReferenceProject(ReferenceProject refProjectDTO, String forgeUrl)
      throws ReferenceServiceException;

  /**
   * Updates Project templates inside the current forge using the incoming DiffusionTemplate object. Also
   * propagates this updates to all its child forges if exist.
   * 
   * @param diffusionTemplate
   * @return
   * @throws ReferenceServiceException
   */
  TemplateSynchroResult updateTemplates(ReferenceTemplates refTemplate, String forgeUrl)
      throws ReferenceServiceException;

  /**
   * Gets the reference project information (also containing the reference items of all the
   * synchronizable plugins part of the project).
   * 
   * @param forgeUrl
   * @return the reference project of the forge.
   * @throws ReferenceServiceException
   */
  ReferenceProject getReferenceProject(String forgeUrl) throws ReferenceServiceException;

  /**
   * Synchronizes the items of the given application.
   * 
   * @param applicationItem
   * @param forgeUrl
   * @return true if the update has been done, false otherwise.
   */
  boolean updateApplicationItem(ApplicationItem applicationItem, String forgeUrl)
      throws ReferenceServiceException;

  /**
   * Send the reference tools synchronization report to the forge administrator.
   * 
   * @param report
   * @param forgeUrl
   * @throws ReferenceServiceException
   */
  void sendReferenceToolsUpdateReport(ToolsSynchronizationReport report, String forgeUrl)
      throws ReferenceServiceException;
}