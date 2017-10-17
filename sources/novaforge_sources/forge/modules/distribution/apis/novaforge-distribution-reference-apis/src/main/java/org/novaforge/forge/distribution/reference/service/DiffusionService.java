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
package org.novaforge.forge.distribution.reference.service;

import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;
import org.novaforge.forge.distribution.reference.model.DiffusionResult;
import org.novaforge.forge.distribution.reference.model.DiffusionTemplateResult;
import org.novaforge.forge.distribution.reference.model.SynchDiffered;
import org.novaforge.forge.distribution.reference.model.TargetForge;

import java.util.Set;

/**
 * Service that should be called in order to propagate the Reference Project data from the "main" forge to its
 * sub-forges.
 * 
 * @author rols-p
 */
public interface DiffusionService
{
  /**
   * Propagates the Reference Project data from this forge to the child forges. This includes the structure
   * of the project as well as the application(s) data.
   * 
   * @return a not-null result of the Diffusion
   * @throws ReferenceServiceException
   */
  DiffusionResult propagateReferenceProject() throws ReferenceServiceException;

  /**
   * Propagates all the Project templates from this forge to the child forges.
   * 
   * @return a not-null result of the Diffusion
   * @throws ReferenceServiceException
   */
  DiffusionTemplateResult propagateTemplates() throws ReferenceServiceException;

  /**
   * Propagates all the reference tools from this forge to the children.
   * 
   * @return a not-null result of the diffusion
   * @throws ReferenceServiceException
   */
  DiffusionResult propagateReferenceTools() throws ReferenceServiceException;

  /**
   * Launch the extraction on all the child forges.
   * 
   * @throws ReferenceServiceException
   */
  void launchReportingExtraction() throws ReferenceServiceException;

  /**
   * Update the scheduling on all the child forges.
   * 
   * @throws ReferenceServiceException
   */
  void configureScheduling(String hours, String minutes, String period, boolean canPropagate)
      throws ReferenceServiceException;

  /**
   * @return the forges that will be the target of the propagation.
   * @throws ReferenceServiceException
   */
  Set<TargetForge> getTargetForges() throws ReferenceServiceException;

  /**
   * stop and disable the scheduling on all the child forges.
   * 
   * @throws ReferenceServiceException
   */
  void disableScheduling(boolean canPropagate) throws ReferenceServiceException;

  /**
   * retrieve if the schudeling is active on the mother forge.
   * 
   * @throws ReferenceServiceException
   */
  SynchDiffered getSchedulingConfig();

}
