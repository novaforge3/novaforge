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
package org.novaforge.forge.portal.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.portal.services.PortalMessages;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Implementation of {@link PortalMessages}
 *
 * @author Guillaume Lamirand
 */
public class PortalMessagesImpl implements PortalMessages
{
  /**
   * Logger
   */
  private static final Log    LOG                        = LogFactory.getLog(PortalMessagesImpl.class);
  /**
   * The navigation messages folder
   */
  private final static String NAVIGATION_MESSAGES_FOLDER = "i18n";                                     //$NON-NLS-1$
  /**
   * The navigation messages file
   */
  private final static String NAVIGATION_MESSAGES_FILE   = "messages";                                 //$NON-NLS-1$
  /**
   * {@link ForgeCfgService} implementation injected by container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * {@link ClassLoader} used to retrieve {@link ResourceBundle}
   */
  private ClassLoader               loader;

  /**
   * Init method
   * This will build a {@link ClassLoader} in order to retrieve {@link ResourceBundle}
   */
  public void init()
  {
    final String portalBundlePath =
        forgeConfigurationService.getPortalConfDirectory() + File.separatorChar + NAVIGATION_MESSAGES_FOLDER;
    final File file = new File(portalBundlePath);
    try
    {
      final URL[] urls = { file.toURI().toURL() };
      loader = new URLClassLoader(urls);
    }
    catch (final MalformedURLException e)
    {
      LOG.error("Unable to build classloader from file");
      loader = this.getClass().getClassLoader();
    }
  }

  public void destroy()
  {
    ResourceBundle.clearCache(loader);
    loader = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage(final Locale pLocale, final String pKey)
  {
    String value = "";
    try
    {
      final ResourceBundle resourceBundle = getResourceBundle(pLocale);
      if (resourceBundle != null)
      {
        value = resourceBundle.getString(pKey);
      }
      else
      {
        value = pKey;
      }
    }
    catch (final MissingResourceException e)
    {
      value = pKey;
    }
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage(final Locale pLocale, final String pKey, final Object... pParams)
  {
    String value = "";
    try
    {
      value = MessageFormat.format(getResourceBundle(pLocale).getString(pKey), pParams);
    }
    catch (final MissingResourceException e)
    {
      value = pKey;
    }
    return value;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This will clean cache associated to {@link ResourceBundle}
   * </p>
   */
  @Override
  public void refresh()
  {
    ResourceBundle.clearCache(loader);

  }

  /**
   * Allows to get the current {@link ResourceBundle}
   *
   * @return the resourceBundle
   *
   * @see ResourceBundle
   */
  private ResourceBundle getResourceBundle(final Locale pLocale)
  {
    return ResourceBundle.getBundle(NAVIGATION_MESSAGES_FILE, pLocale, loader);
  }

  /**
   * Use by container to inject {@link ForgeCfgService}
   *
   * @param pForgeConfigurationService
   *     the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

}
