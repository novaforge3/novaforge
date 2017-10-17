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
package org.novaforge.forge.portal.models;

/**
 * This enum describes the setting id used in portal-config.xml
 *
 * @author Guillaume Lamirand
 */
public enum PortalSettingId
{
  /**
   * Id for theme settings
   */
  THEME
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "theme";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDefaultValue()
        {
          return "novaforge";
        }

      };

  /**
   * Check if the given id is an known internal application.
   *
   * @param pId
   *     the id to check
   *
   * @return true or false
   */
  public static boolean isExist(final String pId)
  {
    boolean                 returnValue = false;
    final PortalSettingId[] values      = PortalSettingId.values();
    if (pId != null)
    {
      for (final PortalSettingId intApp : values)
      {
        if (intApp.getId().equals(pId))
        {
          returnValue = true;
          break;
        }
      }
    }
    return returnValue;

  }

  /**
   * Get the setting id.
   *
   * @return id as a string object
   */
  public abstract String getId();

  /**
   * Get a {@link PortalSettingId} from the id given.
   *
   * @param pId
   *     the original application id
   *
   * @return {@link PortalSettingId} found or <code>null</code>
   */
  public static PortalSettingId getFromId(final String pId)
  {
    PortalSettingId app = null;

    if (pId != null)
    {
      final PortalSettingId[] values = PortalSettingId.values();
      for (final PortalSettingId privateApplication : values)
      {
        if (privateApplication.getId().equals(pId))
        {
          app = privateApplication;
        }
      }
    }
    return app;

  }

  /**
   * Get the default value for the current setting
   *
   * @return default value as a string object
   */
  public abstract String getDefaultValue();
}
