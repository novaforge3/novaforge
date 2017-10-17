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
package org.novaforge.forge.ui.portal.internal.privatemodule.client.model;

import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.ui.portal.client.component.TabsheetWithState.Tab;

import java.util.UUID;

/**
 * This class is used to map an tab element to its references such as :
 * <ol>
 * <li>its {@link Tab} object</li>
 * <li>its identifier</li>
 * <li>its portal module id</li>
 * <li>its {@link PortalComponent}</li>
 * <li>its type (internal or external)</li>
 * </ol>
 * If {@link ApplicationTab#internal} is true, so identifier,module id and {@link PortalComponent} are
 * <code>null</code>
 * 
 * @author Guillaume Lamirand
 */
public class ApplicationTab
{
  private final String  moduleId;
  private final boolean internal;
  private UUID            identifier;
  private PortalComponent portalComponent;
  private Tab             tab;

  /**
   * Default constructor for internal tabulation
   * 
   * @param pIdentifier
   * @param pModuleId
   * @param pPortalComponent
   */
  public ApplicationTab(final UUID pIdentifier, final String pModuleId, final PortalComponent pPortalComponent)
  {
    super();
    identifier = pIdentifier;
    moduleId = pModuleId;
    portalComponent = pPortalComponent;
    internal = true;
  }

  /**
   * Default constructor for external tabulation
   */
  public ApplicationTab()
  {
    super();
    identifier = null;
    moduleId = null;
    portalComponent = null;
    internal = false;
  }

  /**
   * @return the tab
   */
  public Tab getTab()
  {
    return tab;
  }

  /**
   * @param pTab
   *          the tab to set
   */
  public void setTab(final Tab pTab)
  {
    tab = pTab;
  }

  /**
   * @return the identifier
   */
  public UUID getIdentifier()
  {
    return identifier;
  }

  /**
   * @param pIdentifier
   *          the identifier to set
   */
  public void setIdentifier(final UUID pIdentifier)
  {
    identifier = pIdentifier;
  }

  /**
   * @return the moduleId
   */
  public String getModuleId()
  {
    return moduleId;
  }

  /**
   * @return the portalComponent
   */
  public PortalComponent getPortalComponent()
  {
    return portalComponent;
  }

  /**
   * @param pPortalComponent
   *          the portalComponent to set
   */
  public void setPortalComponent(final PortalComponent pPortalComponent)
  {
    portalComponent = pPortalComponent;
  }

  /**
   * @return the internal
   */
  public boolean isInternal()
  {
    return internal;
  }

}
