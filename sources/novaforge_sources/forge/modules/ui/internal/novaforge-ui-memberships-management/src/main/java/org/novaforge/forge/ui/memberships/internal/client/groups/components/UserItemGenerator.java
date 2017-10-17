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
package org.novaforge.forge.ui.memberships.internal.client.groups.components;

import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.client.util.UserIconGenerator;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;

/**
 * @author Jeremy Casery
 */
public class UserItemGenerator implements ItemGenerator
{

  /**
   * 
   */
  private static final long   serialVersionUID      = -1446984490775349585L;

  private static final String USER_PICTURE_SIZE     = "54px";

  private static final String USER_ITEM_LAYOUT_SIZE = "200px";

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(final AbstractItemLayout pSource, final Object pItemId)
  {
    final Item userItem = pSource.getContainerDataSource().getItem(pItemId);
    final String userLogin = (String) userItem.getItemProperty(UserItemProperty.LOGIN.getPropertyId())
        .getValue();
    final String userFirstName = (String) userItem
        .getItemProperty(UserItemProperty.FIRSTNAME.getPropertyId()).getValue();
    final String userLastName = (String) userItem.getItemProperty(UserItemProperty.LASTNAME.getPropertyId())
        .getValue();
    final String userEmail = (String) userItem.getItemProperty(UserItemProperty.EMAIL.getPropertyId())
        .getValue();

    final HorizontalLayout userLayout = new HorizontalLayout();
    userLayout.setWidth(USER_ITEM_LAYOUT_SIZE);
    userLayout.setStyleName(NovaForge.LAYOUT_ITEM);
    userLayout.setDescription(MembershipsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.MEMBERSHIPS_USERS_EDIT_PROFILE));

    final HorizontalLayout pictureLayout = new HorizontalLayout();
    pictureLayout.setSpacing(true);
    final Embedded picture = new Embedded();

    try
    {
      final UserProfile userProfile = MembershipsModule.getUserPresenter().getUserProfile(userLogin);
      if ((userProfile != null) && (userProfile.getImage() != null))
      {
        picture.setSource(ResourceUtils.buildImageResource(userProfile.getImage().getFile(), userLogin));
      }
      else
      {
        picture.setSource(UserIconGenerator.getIconForUser(userFirstName, userLastName));
      }
    }
    catch (final UserServiceException e)
    {
      picture.setSource(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
    }
    picture.setStyleName(NovaForge.USER_ITEM_PICTURE);
    pictureLayout.addComponent(picture);

    final VerticalLayout infosLayout = new VerticalLayout();
    final Label name = new Label(userFirstName + " " + userLastName);

    final Label login = new Label(userLogin);
    final Label mail = new Label(userEmail);

    infosLayout.addComponent(name);
    infosLayout.addComponent(login);
    infosLayout.addComponent(mail);

    name.setStyleName(NovaForge.TEXT_ELLIPSIS);
    login.setStyleName(NovaForge.TEXT_ELLIPSIS);
    mail.setStyleName(NovaForge.TEXT_ELLIPSIS);

    userLayout.addComponent(pictureLayout);
    userLayout.addComponent(infosLayout);

    userLayout.setSpacing(true);
    userLayout.setMargin(new MarginInfo(false, true, false, false));

    picture.setWidth(USER_PICTURE_SIZE);
    picture.setHeight(USER_PICTURE_SIZE);

    name.setStyleName(NovaForge.LABEL_GREEN);
    name.addStyleName(NovaForge.LABEL_BOLD);

    userLayout.setComponentAlignment(pictureLayout, Alignment.MIDDLE_CENTER);
    userLayout.setExpandRatio(infosLayout, 1);
    return userLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeGenerated(final AbstractItemLayout pSource, final Object pItemId,
      final Object pPropertyId)
  {
    boolean isNeeded = false;

    if ((UserItemProperty.LOGIN.getPropertyId().equals(pPropertyId))
        || (UserItemProperty.EMAIL.getPropertyId().equals(pPropertyId))
        || (UserItemProperty.FIRSTNAME.getPropertyId().equals(pPropertyId))
        || (UserItemProperty.LASTNAME.getPropertyId().equals(pPropertyId)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object itemLogin = item.getItemProperty(UserItemProperty.LOGIN.getPropertyId()).getValue();
      final Object itemFirstName = item.getItemProperty(UserItemProperty.FIRSTNAME.getPropertyId())
          .getValue();
      final Object itemLastName = item.getItemProperty(UserItemProperty.LASTNAME.getPropertyId()).getValue();
      final Object itemEmail = item.getItemProperty(UserItemProperty.EMAIL.getPropertyId()).getValue();
      isNeeded = (itemLogin != null) && (itemFirstName != null) && (itemLastName != null)
          && (itemEmail != null);
    }
    return isNeeded;
  }
}
