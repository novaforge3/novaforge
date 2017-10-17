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

import org.novaforge.forge.portal.models.PortalStringTokenized;
import org.novaforge.forge.portal.models.PortalToken;
import org.novaforge.forge.portal.services.navigation.NavigationToken;

/**
 * Service implementation of {@link NavigationToken}
 * 
 * @author Guillaume Lamirand
 * @see NavigationToken
 */
public class NavigationTokenImpl implements NavigationToken
{

  /**
   * {@inheritDoc}
   */
  @Override
  public String resolved(final PortalStringTokenized pTokenized)
  {
    String source = pTokenized.getSource();
    if ((pTokenized.getProjectId() != null) && (!"".equals(pTokenized.getProjectId())))
    {
      source = source.replace(PortalToken.PROJECT_ID.getToken(), pTokenized.getProjectId());
    }
    if ((pTokenized.getLocale() != null) && (!"".equals(pTokenized.getLocale())))
    {
      source = source.replace(PortalToken.LOCALE.getToken(), pTokenized.getLocale());
    }
    if ((pTokenized.getInstanceId() != null) && (!"".equals(pTokenized.getInstanceId())))
    {
      source = source.replace(PortalToken.INSTANCE_ID.getToken(), pTokenized.getInstanceId());
    }
    if ((pTokenized.getPluginUuid() != null) && (!"".equals(pTokenized.getPluginUuid())))
    {
      source = source.replace(PortalToken.PLUGIN_UUID.getToken(), pTokenized.getPluginUuid());
    }
    if ((pTokenized.getToolUuid() != null) && (!"".equals(pTokenized.getToolUuid())))
    {
      source = source.replace(PortalToken.TOOL_UUID.getToken(), pTokenized.getToolUuid());
    }
    if ((pTokenized.getPluginType() != null) && (!"".equals(pTokenized.getPluginType())))
    {
      source = source.replace(PortalToken.PLUGIN_TYPE.getToken(), pTokenized.getPluginType());
    }
    if ((pTokenized.getPluginViewId() != null) && (!"".equals(pTokenized.getPluginViewId())))
    {
      source = source.replace(PortalToken.PLUGIN_VIEW.getToken(), pTokenized.getPluginViewId());
    }
    if ((pTokenized.getUserName() != null) && (!"".equals(pTokenized.getUserName())))
    {
      source = source.replace(PortalToken.USER_NAME.getToken(), pTokenized.getUserName());
    }
    return source;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean containsToken(final String pTokenized, final PortalToken pToken)
  {
    return pTokenized.contains(pToken.getToken());
  }

}
