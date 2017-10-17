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
package org.novaforge.forge.portal.internal.services.navigation;

import org.apache.commons.io.IOUtils;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.services.PortalNavigation;
import org.novaforge.forge.portal.services.navigation.NavigationXML;
import org.novaforge.forge.portal.xml.App;
import org.novaforge.forge.portal.xml.DefaultApp;
import org.novaforge.forge.portal.xml.ExternalApp;
import org.novaforge.forge.portal.xml.Link;
import org.novaforge.forge.portal.xml.PortalConfig;
import org.novaforge.forge.portal.xml.Space;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service implementation of {@link PortalNavigation}
 * 
 * @author Guillaume Lamirand
 */
public class NavigationXMLImpl implements NavigationXML
{

  /**
   * Configuration file
   */
  private static final String PORTAL_CONFIG_XML = "portal-config.xml";
  /**
   * Current {@link PortalConfig}
   */
  private PortalConfig        portalConfig;
  /**
   * Map all uri found with their unique id, used in order to set the same id to the same uri
   */
  private Map<String, String> uriToUuid;

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalConfig unmarshal(final File pPortalXml) throws PortalException
  {
    // Init variables
    uriToUuid = new HashMap<String, String>();
    portalConfig = null;

    if (pPortalXml != null)
    {
      // Read the given file
      readFile(pPortalXml);
      // Set unique identifier for each applications
      setUniqueId();
    }
    // Return the portalConfid built from file read
    return portalConfig;
  }

  /**
   * Read the given file and unmarshal if to {@link #portalConfig}
   * 
   * @param pPortalXml
   *          file given
   * @throws PortalException
   *           occured if the file cannot be read
   */
  private void readFile(final File pPortalXml) throws PortalException
  {
    InputStream is = null;
    try
    {
      // Build JAXB component
      final JAXBContext jaxbContext = JAXBContext.newInstance(PortalConfig.class);
      final Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

      if (pPortalXml.exists())
      {
        is = new FileInputStream(pPortalXml);
      }
      else
      {
        is = this.getClass().getResourceAsStream("/" + PORTAL_CONFIG_XML);
      }
      portalConfig = (PortalConfig) unMarshaller.unmarshal(is);
    }
    catch (final JAXBException e)
    {
      throw new PortalException(String.format("Unable to read the configuration file [file=%s]",
          PORTAL_CONFIG_XML), e);
    }
    catch (final FileNotFoundException e)
    {
      throw new PortalException(String.format("Unable to read the configuration file [file=%s]",
          pPortalXml.getPath()), e);
    }
    finally
    {
      IOUtils.closeQuietly(is);
    }
  }

  /**
   * Will browse all application and set them an unique id
   */
  private void setUniqueId()
  {
    final List<Space> forgeSpaces = portalConfig.getNavigation().getForge().getSpace();
    if (forgeSpaces != null)
    {
      for (final Space forgeSpace : forgeSpaces)
      {
        if (forgeSpace.getApps() != null)
        {
          buildAppsIdentifier(forgeSpace.getApps().getExternalApp(), forgeSpace.getApps().getInternalApp());
        }
      }
    }
    final DefaultApp mainDefaultApp = portalConfig.getNavigation().getForge().getDefaultApp();
    if (mainDefaultApp != null)
    {
      if (mainDefaultApp.getExternalApp() != null)
      {
        setIdentifier(mainDefaultApp.getExternalApp());
      }
      if (mainDefaultApp.getInternalApp() != null)
      {
        setIdentifier(mainDefaultApp.getInternalApp());
      }
    }
    final List<Space> projectSpaces = portalConfig.getNavigation().getProject().getSpace();
    if (projectSpaces != null)
    {
      for (final Space projectSpace : projectSpaces)
      {
        if (projectSpace.getApps() != null)
        {
          buildAppsIdentifier(projectSpace.getApps().getExternalApp(), projectSpace.getApps()
              .getInternalApp());
        }
      }
    }
    final DefaultApp projectDefaultApp = portalConfig.getNavigation().getProject().getDefaultApp();
    if (projectDefaultApp != null)
    {
      if (projectDefaultApp.getExternalApp() != null)
      {
        setIdentifier(projectDefaultApp.getExternalApp());
      }
      if (projectDefaultApp.getInternalApp() != null)
      {
        setIdentifier(projectDefaultApp.getInternalApp());
      }
    }
    final List<Space> accountSpaces = portalConfig.getNavigation().getAccount().getSpace();
    if (accountSpaces != null)
    {
      for (final Space accSpace : accountSpaces)
      {
        if (accSpace.getApps() != null)
        {
          buildAppsIdentifier(accSpace.getApps().getExternalApp(), accSpace.getApps().getInternalApp());
        }
      }
    }
    final Link link = portalConfig.getNavigation().getLink();
    if (link != null)
    {
      buildAppsIdentifier(link.getExternalApp(), link.getInternalApp());
    }
  }

  /**
   * Will set a {@link UUID} to each applications
   * 
   * @param pExternalApp
   * @param pInternalApp
   */
  private void buildAppsIdentifier(final List<ExternalApp> pExternalApp, final List<App> pInternalApp)
  {
    if (pExternalApp != null)
    {
      for (final ExternalApp extApp : pExternalApp)
      {
        setIdentifier(extApp);
      }
    }
    if (pInternalApp != null)
    {
      for (final App intApp : pInternalApp)
      {
        setIdentifier(intApp);
      }
    }

  }

  private void setIdentifier(final App extApp)
  {
    final String uniqueID = getUniqueID(extApp.getId());
    extApp.setUuid(uniqueID);
  }

  /**
   * Will set a {@link UUID} to each applications
   * 
   * @param pId
   *          application id
   */
  private String getUniqueID(final String pId)
  {
    String uuid = "";
    if (uriToUuid.containsKey(pId))
    {
      uuid = uriToUuid.get(pId);
    }
    else
    {
      uuid = UUID.randomUUID().toString();
      uriToUuid.put(pId, uuid);
    }
    return uuid;
  }

}
