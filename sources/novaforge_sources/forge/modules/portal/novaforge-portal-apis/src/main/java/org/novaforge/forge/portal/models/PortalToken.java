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

import org.novaforge.forge.core.organization.model.Application;

/**
 * This enum contains all token which can be used in a {@link Application} url. Those tokens will be replaced
 * a runtime by the correct value.
 * 
 * @author Guillaume Lamirand
 */
public enum PortalToken
{
  /**
   * Represents the projectId token used for portal navigation
   */
  PROJECT_ID
  {
    @Override
    public String getToken()
    {
      return "@projectId";
    }

  },
  /**
   * Represents the language token used for internationnalization
   */
  LOCALE
  {
    @Override
    public String getToken()
    {
      return "@locale";
    }

  },
  /**
   * Represents the instanceId token used for portal navigation
   */
  INSTANCE_ID
  {
    @Override
    public String getToken()
    {
      return "@instanceId";
    }

  },
  /**
   * Represents the pluginUUID token used for portal navigation
   */
  PLUGIN_UUID
  {
    @Override
    public String getToken()
    {
      return "@pluginUUID";
    }

  },
  /**
   * Represents the toolUUID token used for portal navigation
   */
  TOOL_UUID
  {
    @Override
    public String getToken()
    {
      return "@toolUUID";
    }

  },
  /**
   * Represents the pluginView token used for portal navigation
   */
  PLUGIN_VIEW
  {
    @Override
    public String getToken()
    {
      return "@pluginView";
    }

  },
  /**
   * Represents the pluginType token used for portal navigation
   */
  PLUGIN_TYPE
  {
    @Override
    public String getToken()
    {
      return "@pluginType";
    }

  },
  /**
   * Represents the userName token used for portal navigation
   */
  USER_NAME
  {
    @Override
    public String getToken()
    {
      return "@userName";
    }

  };

  /**
   * Get the concret token element.
   * 
   * @return token as a string object
   */
  public abstract String getToken();

}
