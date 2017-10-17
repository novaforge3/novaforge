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
package org.novaforge.forge.core.plugins.domain.core;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class defines differents status available for a plugin
 * 
 * @author lamirang @date Mar 22, 2011
 */
public enum PluginStatus
{

  /**
   * the plugin is installed and must be activated by administrators to be used by users.
   */
  INSTALLED,
  /**
   * the plugin can be used by all users.
   */
  ACTIVATED,
  /**
   * the plugin is desactivated except for administrators.
   */
  DESACTIVATED,
  /**
   * the plugin is still available for existing applications but not for new ones.
   */
  DEPRECATED,
  /**
   * the plugin is stopped. So it can't be used by anyone.
   */
  STOPPED,
  /**
   * the plugin is uninstalled
   */
  UNINSTALLED;

  private static final Map<String, PluginStatus> statuss = new HashMap<String, PluginStatus>();

  static
  {
    for (final PluginStatus status : values())
    {
      statuss.put(status.getLabel(), status);
    }
  }

  public static PluginStatus fromLabel(final String pLabel)
  {
    return statuss.get(pLabel);
  }

  public String getDescription()
  {
    return getDescription(PluginStatusResourceBundle.DEFAULT_LOCALE);
  }

  public String getDescription(final Locale pLocale)
  {
    final ResourceBundle resourceBundle = PluginStatusResourceBundle.getBundle(pLocale);
    return resourceBundle.getString(toString() + "." + PluginStatusResourceBundle.DESCRIPTION);
  }

  @Historizable(label = "label")
  public String getLabel()
  {
    return getLabel(PluginStatusResourceBundle.DEFAULT_LOCALE);
  }

  public String getLabel(final Locale pLocale)
  {
    final ResourceBundle resourceBundle = PluginStatusResourceBundle.getBundle(pLocale);
    return resourceBundle.getString(toString() + "." + PluginStatusResourceBundle.LABEL);
  }

  private static final class PluginStatusResourceBundle
  {
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private static final String PROPERTY_FILE  = "pluginstatus.status";

    private static final String LABEL          = "label";

    private static final String DESCRIPTION    = "description";

    public static ResourceBundle getBundle(final Locale pLocale)
    {
      return ResourceBundle.getBundle(PROPERTY_FILE, pLocale);
    }

  }

}
