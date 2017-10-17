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

import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.portal.services.navigation.NavigationMessage;

import java.util.Locale;

/**
 * Service implementation of {@link NavigationMessage}
 * 
 * @author Guillaume Lamirand
 */
public class NavigationMessageImpl implements NavigationMessage
{
  /**
   * The navigation key prefix
   */
  private final static String NAVIGATION_PREFIX = "${"; //$NON-NLS-1$
  /**
   * {@link PortalMessages} implementation injected by container
   */
  private PortalMessages portalMessages;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getNavigationMessage(final String pKey, final Locale pLocale)
  {
    String keyClean = pKey;
    String value    = keyClean;
    if (pKey.startsWith(NAVIGATION_PREFIX))
    {
      // Remove ${}
      keyClean = pKey.substring(NAVIGATION_PREFIX.length(), pKey.length() - 1);
      value = portalMessages.getMessage(pLocale, keyClean);
    }
    return value;
  }

  /**
   * Use by container to inject {@link PortalMesages}
   *
   * @param pPortalMessages
   *          the portalMessages to set
   */
  public void setPortalMessages(final PortalMessages pPortalMessages)
  {
    portalMessages = pPortalMessages;
  }
}
