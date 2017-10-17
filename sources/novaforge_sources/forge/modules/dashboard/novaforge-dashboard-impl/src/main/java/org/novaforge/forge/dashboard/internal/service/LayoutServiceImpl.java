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
package org.novaforge.forge.dashboard.internal.service;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.dashboard.exception.DashBoardException;
import org.novaforge.forge.dashboard.service.LayoutService;
import org.novaforge.forge.dashboard.xml.DashboardConfig;
import org.novaforge.forge.dashboard.xml.Layout;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Implementation of {@link LayoutService}
 * 
 * @author Guillaume Lamirand
 */
public class LayoutServiceImpl implements LayoutService
{

  /**
   * Configuration directory
   */
  private static final String       SEPARATOR            = "/";
  /**
   * Configuration directory
   */
  private static final String       DASHBOARD_DIRECTORY  = "dashboard";
  /**
   * Configuration file
   */
  private static final String       DASHBOARD_CONFIG_XML = "dashboard-config.xml";
  /**
   * Reference to {@link ForgeCfgService} service injected by the container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * Current {@link PortalConfig}
   */
  private DashboardConfig           dashboardConfig;

  /**
   * Callback method call when the component become valid.
   * This will read the xml file and build {@link DashboardConfig} associated
   * 
   * @throws DashBoardException
   */
  public void init() throws DashBoardException
  {
    refresh();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refresh() throws DashBoardException
  {
    // Get portal-config file
    final File portalXml = new File(forgeConfigurationService.getForgeConfDirectory() + SEPARATOR + DASHBOARD_DIRECTORY,
                                    DASHBOARD_CONFIG_XML);
    readFile(portalXml);

  }

  /**
   * Read the given file and unmarshal if to {@link #dashboardConfig}
   * 
   * @param pDashBoardXml
   *          file given
   * @throws DashBoardException
   *           occured if the file cannot be read
   */
  private void readFile(final File pDashBoardXml) throws DashBoardException
  {
    InputStream is = null;
    try
    {
      // Build JAXB component
      final JAXBContext jaxbContext = JAXBContext.newInstance(DashboardConfig.class);
      final Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

      if (pDashBoardXml.exists())
      {
        is = new FileInputStream(pDashBoardXml);
      }
      else
      {
        is = this.getClass().getResourceAsStream(SEPARATOR + DASHBOARD_CONFIG_XML);
      }
      dashboardConfig = (DashboardConfig) unMarshaller.unmarshal(is);
    }
    catch (final JAXBException e)
    {
      throw new DashBoardException(String.format("Unable to read the configuration file [file=%s]",
          pDashBoardXml), e);
    }
    catch (final FileNotFoundException e)
    {
      throw new DashBoardException(String.format("The configuration file given is not found [file=%s]",
          pDashBoardXml), e);
    }
    finally
    {
      IOUtils.closeQuietly(is);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Layout> getLayouts()
  {
    return dashboardConfig.getLayouts().getLayout();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Layout getLayout(final String pKey)
  {
    Layout returnLayout = null;
    if (!Strings.isNullOrEmpty(pKey))
    {
      for (final Layout layout : dashboardConfig.getLayouts().getLayout())
      {
        if (layout.getKey().equals(pKey))
        {
          returnLayout = layout;
          break;
        }
      }
    }
    return returnLayout;
  }

  /**
   * Bind method used by the container to inject {@link ForgeConfigurationManager} service.
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationManager to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }
}
