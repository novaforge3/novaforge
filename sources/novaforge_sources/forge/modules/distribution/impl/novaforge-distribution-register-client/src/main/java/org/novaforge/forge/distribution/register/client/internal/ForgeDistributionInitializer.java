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
package org.novaforge.forge.distribution.register.client.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.distribution.register.client.DistributionInitializerService;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;
import org.novaforge.forge.distribution.register.services.ForgeDistributionService;
import org.novaforge.forge.reference.tool.ReferenceToolService;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 25 janv. 2012
 */
public class ForgeDistributionInitializer implements DistributionInitializerService
{

  private static final Log           LOGGER           = LogFactory.getLog(ForgeDistributionInitializer.class);

  private ForgeDistributionService   forgeDistributionService;
  private ForgeIdentificationService forgeIdentificationService;
  private ForgeConfigurationService  forgeConfigurationService;
  private ReferenceToolService       referenceToolService;

  private String                     forgeLabel       = "";
  private String                     forgeDescription = "";
  private String                     level            = "-1";

  @Override
  public void initializeDistribution() throws ForgeDistributionException
  {
    LOGGER.info("Novaforge Distribution Initialization: START.");
    addOrUpdateForge();
    LOGGER.info("Novaforge Distribution Initialization: FINISHED SUCCESSFULLY.");
  }

  private void addOrUpdateForge() throws ForgeDistributionException
  {
    final String forgeId = forgeIdentificationService.getForgeId().toString();
    final Integer forgeLevel = Integer.parseInt(level);

    final ForgeDTO myForge = forgeDistributionService.getForge(forgeId);
    if (myForge == null)
    {
      final ForgeDTO newForge = new ForgeDTO();
      newForge.setForgeId(UUID.fromString(forgeId));
      newForge.setDescription(forgeDescription);
      newForge.setLabel(forgeLabel);
      newForge.setPortalUri(forgeConfigurationService.getPortalEntryPoint());
      newForge.setForgeLevel(forgeLevel);
      newForge.setForgeUrl(forgeConfigurationService.getPublicUrl());
      newForge.setCertificatePublicKey(getCertificatePublicKey());
      LOGGER.info(String.format("Adding the forge %s to the Distribution repository.", forgeLabel));
      forgeDistributionService.addForge(newForge);
    }
    else
    {
      myForge.setDescription(forgeDescription);
      myForge.setLabel(forgeLabel);
      myForge.setPortalUri(forgeConfigurationService.getPortalEntryPoint());
      myForge.setForgeUrl(forgeConfigurationService.getPublicUrl());
      myForge.setCertificatePublicKey(getCertificatePublicKey());
      LOGGER.info(String.format("Updating the forge %s to the Distribution repository.", forgeLabel));
      forgeDistributionService.updateForgeDescription(myForge);
    }
  }

  private String getCertificatePublicKey()
  {
    final StringBuilder resultBuilder = new StringBuilder();
    try
    {
      final FileInputStream fileStream = new FileInputStream(
          referenceToolService.getCertificatePublicKeyFile());
      final DataInputStream dataInputStream = new DataInputStream(fileStream);
      final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
      String strLine;
      while ((strLine = bufferedReader.readLine()) != null)
      {
        resultBuilder.append(strLine);
      }
      dataInputStream.close();
    }
    catch (final FileNotFoundException e)
    {
      LOGGER
          .warn("No certificate public key file found, the reference tools propagation may not work correctly.");
    }
    catch (final IOException e)
    {
      LOGGER
          .error("Unknown I/O exception when reading the certificate public key file, the reference tools propagation may not work correctly.");
    }
    return resultBuilder.toString();
  }

  public void stop()
  {
    LOGGER.info("stopping DistributionInitializerService...");
  }

  public void setForgeLabel(final String forgeLabel)
  {
    this.forgeLabel = forgeLabel;
  }

  public void setForgeDescription(final String forgeDescription)
  {
    this.forgeDescription = forgeDescription;
  }

  public void setLevel(final String level)
  {
    this.level = level;
  }

  /**
   * @param pForgeDistributionService
   *          the distributionClient to set
   */
  public void setForgeDistributionService(final ForgeDistributionService pForgeDistributionService)
  {
    forgeDistributionService = pForgeDistributionService;
  }

  /**
   * @param pForgeIdentificationService
   *          the forgeIdentificationService to set
   */
  public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
  {
    forgeIdentificationService = pForgeIdentificationService;
  }

  /**
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * @param pReferenceToolService
   *          the referenceToolService to set
   */
  public void setReferenceToolService(final ReferenceToolService pReferenceToolService)
  {
    referenceToolService = pReferenceToolService;
  }
}
