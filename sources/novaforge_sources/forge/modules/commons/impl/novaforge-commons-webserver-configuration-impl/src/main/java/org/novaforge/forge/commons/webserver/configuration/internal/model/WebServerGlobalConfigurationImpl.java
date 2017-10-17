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
package org.novaforge.forge.commons.webserver.configuration.internal.model;

import org.novaforge.forge.commons.webserver.configuration.model.WebServerAdministrator;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerGlobalConfiguration;

import java.util.ArrayList;
import java.util.List;

public class WebServerGlobalConfigurationImpl implements WebServerGlobalConfiguration
{

  private final String                       name;
  private final List<String>                 publicAliases;
  private final List<WebServerAdministrator> admins;

  public WebServerGlobalConfigurationImpl(final String pName)
  {
    this(pName, new ArrayList<String>());
  }

  public WebServerGlobalConfigurationImpl(final String pName, final List<String> pAlias)
  {
    this(pName, pAlias, new ArrayList<WebServerAdministrator>());
  }

  public WebServerGlobalConfigurationImpl(final String pName, final List<String> pAlias,
      final List<WebServerAdministrator> pAdmins)
  {
    name = pName;
    publicAliases = pAlias;
    admins = pAdmins;
  }

  /**
   * @return the admins
   */
  @Override
  public List<WebServerAdministrator> getAdmins()
  {
    return admins;
  }

  /**
   * @return the name
   */
  @Override
  public List<String> getPublicAliases()
  {
    return publicAliases;
  }

  /**
   * @return the name
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "WebServerConfigurationImpl [name=" + name + ", publicAliases=" + publicAliases + ", admins=" + admins + "]";
  }

}
