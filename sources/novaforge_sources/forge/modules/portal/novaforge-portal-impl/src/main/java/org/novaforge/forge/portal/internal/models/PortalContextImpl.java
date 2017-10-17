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
package org.novaforge.forge.portal.internal.models;

import net.engio.mbassy.bus.MBassador;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.models.PortalContext;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class PortalContextImpl implements PortalContext
{
  /**
   * The {@link UUID}
   */
  private final UUID                           uuid;
  /**
   * The {@link MBassador} event bus
   */
  private final MBassador<PortalEvent>         eventBus;
  /**
   * The {@link Locale}
   */
  private final Locale                         locale;
  /**
   * The current project id
   */
  private final Map<PortalContext.KEY, String> attributes;

  /**
   * Default constructor
   * 
   * @param pEventBus
   *          the parent event bus
   * @param pLocale
   *          the current locale
   * @param pAttributes
   *          the attributes used to configure the module
   */
  public PortalContextImpl(final MBassador<PortalEvent> pEventBus, final Locale pLocale,
      final Map<PortalContext.KEY, String> pAttributes)
  {
    uuid = UUID.randomUUID();
    eventBus = pEventBus;
    locale = pLocale;
    attributes = pAttributes;
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
  public Locale getInitialLocale()
  {
    return locale;
  }

  @Override
  public Map<KEY, String> getAttributes()
  {
    return attributes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUuid()
  {
    return uuid;
  }
}