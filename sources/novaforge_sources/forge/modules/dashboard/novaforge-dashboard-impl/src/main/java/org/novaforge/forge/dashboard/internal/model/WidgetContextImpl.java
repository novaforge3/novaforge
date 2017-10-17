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
package org.novaforge.forge.dashboard.internal.model;

import net.engio.mbassy.bus.MBassador;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.portal.events.PortalEvent;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class WidgetContextImpl implements WidgetContext
{
  /**
   * The {@link MBassador} event bus
   */
  private final MBassador<PortalEvent>    eventBus;
  /**
   * The {@link Locale}
   */
  private final Locale                    locale;
  /**
   * The {@link Locale}
   */
  private final UUID                      uuid;
  /**
   * The applications by applications
   */
  private final Map<String, List<String>> applications;
  /**
   * the configuration of the widget
   */
  private final String                    properties;

  /**
   * Default constructor
   * 
   * @param pEventBus
   *          the source event bus
   * @param pLocale
   *          the locale
   * @param pUUID
   *          the widget identifiant
   * @param pApplications
   *          the applications by applications defined for this widget
   * @param pProperties
   *          the configuration
   */
  public WidgetContextImpl(final MBassador<PortalEvent> pEventBus, final Locale pLocale, final UUID pUUID,
      final Map<String, List<String>> pApplications, final String pProperties)
  {
    eventBus = pEventBus;
    locale = pLocale;
    uuid = pUUID;
    applications = pApplications;
    properties = pProperties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MBassador<PortalEvent> getEventBus()
  {
    return eventBus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getLocale()
  {
    return locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getId()
  {
    return uuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, List<String>> getApplicationsByProject()
  {
    return applications;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProperties()
  {
    return properties;
  }
}