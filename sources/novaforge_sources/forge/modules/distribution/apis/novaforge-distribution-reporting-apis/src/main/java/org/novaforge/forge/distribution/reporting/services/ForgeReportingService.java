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
package org.novaforge.forge.distribution.reporting.services;

import org.novaforge.forge.distribution.reporting.domain.ProjectDTO;
import org.novaforge.forge.distribution.reporting.domain.UserDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.ForgeViewDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.OrganizationViewDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.ProfilViewDTO;
import org.novaforge.forge.distribution.reporting.exceptions.ForgeReportingException;

import javax.jws.WebService;
import java.util.List;
import java.util.UUID;

/**
 * @author Bilet-jc
 */
@WebService
public interface ForgeReportingService
{

  /*
   * Method to get all forgeView to show indicators
   */
  List<ForgeViewDTO> getForgeView(UUID pForgeId) throws ForgeReportingException;

  /*
   * Method to get all profilView to show indicators
   */
  List<ProfilViewDTO> getProfilView(UUID pForgeId) throws ForgeReportingException;

  /*
   * Method to get all organizationView to show indicators
   */
  List<OrganizationViewDTO> getOrganizationView(UUID pForgeId) throws ForgeReportingException;

  void storeForgeData(UUID forgeId, List<ProjectDTO> projectData, final List<UserDTO> usersData)
      throws ForgeReportingException;

}
