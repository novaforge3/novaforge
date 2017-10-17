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
package org.novaforge.forge.ui.portal.client.component;

import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

/**
 * @author Jeremy Casery
 */
public enum TrayNotificationType
{

  SUCCESS
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
      return "succes";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleName()
    {
      return "nf-traynotification-success";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon()
    {
      return NovaForgeResources.ICON_VALIDATE_ROUND;
    }
  },
  INFO
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
      return "info";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleName()
    {
      return "nf-traynotification-info";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon()
    {
      // TODO Auto-generated method stub
      return NovaForgeResources.ICON_INFORMATION;
    }
  },
  WARNING
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
      return "warning";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleName()
    {
      return "nf-traynotification-warning";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon()
    {
      return NovaForgeResources.ICON_WARNING_ROUND;
    }
  };

  /**
   * Get the TrayNotificationType id
   * 
   * @return the type id
   */
  public abstract String getId();

  /**
   * Get the TrayNotification stylename
   * 
   * @return the style name
   */
  public abstract String getStyleName();

  /**
   * Get the TrayNotification icon
   * 
   * @return the icon
   */
  public abstract String getIcon();
}
