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
package org.novaforge.forge.commons.webserver.configuration.model;

/**
 * This enum contains all token which can be used in a WebService Configuration template. Those tokens will be
 * replaced a runtime by the instance value.
 * 
 * @author Guillaume Lamirand
 */
public enum WebServerProxyToken
{
  BASE_URL
  {
    @Override
    public String getToken()
    {
      return "@BASE_URL@";
    }

  },
  BASE_SCHEME
  {
    @Override
    public String getToken()
    {
      return "@BASE_SCHEME@";
    }

  },
  BASE_HOST
  {
    @Override
    public String getToken()
    {
      return "@BASE_HOST@";
    }

  },
  BASE_ALIAS
  {
    @Override
    public String getToken()
    {
      return "@BASE_ALIAS@";
    }

  },
  BASE_PORT
  {
    @Override
    public String getToken()
    {
      return "@BASE_PORT@";
    }

  },
  ALIAS
  {
    @Override
    public String getToken()
    {
      return "@ALIAS@";
    }

  };

  /**
   * Get the concret token element.
   * 
   * @return token as a string object
   */
  public abstract String getToken();

}
