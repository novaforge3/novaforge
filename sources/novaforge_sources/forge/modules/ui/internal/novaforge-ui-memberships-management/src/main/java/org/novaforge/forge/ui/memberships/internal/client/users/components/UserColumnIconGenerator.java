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
package org.novaforge.forge.ui.memberships.internal.client.users.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

/**
 * @author Jeremy Casery
 */
public class UserColumnIconGenerator implements ColumnGenerator
{

  /**
   * 
   */
  private static final long serialVersionUID = 8039663899477142391L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {

    final Embedded userIcon = new Embedded();
    userIcon.setWidth(NovaForge.ACTION_ICON_SIZE);

    String userLogin = (String) pSource.getItem(pItemId)
        .getItemProperty(UserItemProperty.LOGIN.getPropertyId()).getValue();
    try
    {
      UserProfile userProfile = MembershipsModule.getUserPresenter().getUserProfile(userLogin);
      if (userProfile != null && userProfile.getImage() != null)
      {
        userIcon.setSource(ResourceUtils.buildImageResource(userProfile.getImage().getFile(), userLogin));
      }
      else
      {
        userIcon.setSource(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
      }
    }
    catch (UserServiceException e)
    {
      // Do nothing
    }
    finally
    {
      userIcon.setSource(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
    }

    return userIcon;
  }
}
