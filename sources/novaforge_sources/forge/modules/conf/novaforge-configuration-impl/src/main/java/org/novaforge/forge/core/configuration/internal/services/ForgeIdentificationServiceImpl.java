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
package org.novaforge.forge.core.configuration.internal.services;

import org.novaforge.forge.core.configuration.dao.ForgeIdentificationDAO;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.model.ForgeIdentification;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;

import java.util.UUID;

/**
 * Service implementation of {@link ForgeIdentificationService}
 * 
 * @author Florent Trolat
 * @author Guillaume Lamirand
 */
public class ForgeIdentificationServiceImpl implements ForgeIdentificationService
{
  /**
   * Reference to {@link DataSourceSelect} bean
   */
  private ForgeIdentificationDAO forgeIdentificationDAO;

  /**
   * Contains the forge id
   */
  private UUID                   forgeId;

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized UUID getForgeId()
  {
    if (forgeId == null)
    {
      initializeIdentification();
    }
    return forgeId;

  }

  /**
   * It will initialize a new UUID if needed
   * 
   * @throws ForgeConfigurationException
   *           thrown on persistence error
   */
  private void initializeIdentification()
  {
    if (!forgeIdentificationDAO.existIdentifiant())
    {
      final ForgeIdentification id = forgeIdentificationDAO.create(UUID.randomUUID());
      forgeId = id.getIdentifiant();
    }
    else
    {
      forgeId = forgeIdentificationDAO.get().getIdentifiant();
    }
  }

  /**
   * Method used by the container to inject {@link ForgeIdentificationDAO} service.
   * 
   * @param pForgeIdentificationDAO
   *          the forgeIdentificationDAO to set
   */
  public void setForgeIdentificationDAO(final ForgeIdentificationDAO pForgeIdentificationDAO)
  {
    forgeIdentificationDAO = pForgeIdentificationDAO;
  }

}
