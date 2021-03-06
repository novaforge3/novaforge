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
package org.novaforge.forge.ui.user.management.internal.client.admin.components;

import com.vaadin.data.Item;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.data.container.UserProfileItemProperty;
import org.novaforge.forge.ui.user.management.internal.client.admin.UserManagementPresenter;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;
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
  private static final long             serialVersionUID      = -3806115815154444932L;

  private static final float            USER_PICTURE_SIZE     = 54;

  private static final String           USER_ITEM_LAYOUT_SIZE = "220px";

  private static final String           ACTION_BUTTON_SIZE    = "22px";

  private final UserManagementPresenter presenter;

  public UserItemGenerator(final UserManagementPresenter pPresenter)
  {
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(final AbstractItemLayout pSource, final Object pItemId)
  {
    final Item userItem = pSource.getContainerDataSource().getItem(pItemId);
    final String userLogin = (String) userItem.getItemProperty(UserProfileItemProperty.LOGIN.getPropertyId())
        .getValue();
    final String userFirstName = (String) userItem.getItemProperty(
        UserProfileItemProperty.FIRSTNAME.getPropertyId()).getValue();
    final String userLastName = (String) userItem.getItemProperty(
        UserProfileItemProperty.LASTNAME.getPropertyId()).getValue();
    final String userEmail = (String) userItem.getItemProperty(UserProfileItemProperty.EMAIL.getPropertyId())
        .getValue();
    final BinaryFile userPicture = (BinaryFile) userItem.getItemProperty(
        UserProfileItemProperty.PICTURE.getPropertyId()).getValue();

    HorizontalLayout userLayout = new HorizontalLayout();
    userLayout.setWidth(USER_ITEM_LAYOUT_SIZE);
    userLayout.setStyleName(NovaForge.LAYOUT_ITEM);
    userLayout.setDescription(AdminModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.USERMANAGEMENT_ACTION_VIEW_USER));

    Embedded picture = new Embedded();

    if (userPicture != null)
    {
      picture.setSource(ResourceUtils.buildImageResource(userPicture.getFile(), userLogin));
    }
    else
    {
      picture.setSource(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
    }
    picture.setStyleName(NovaForge.USER_ITEM_PICTURE);
    picture.setWidth(USER_PICTURE_SIZE, Unit.PIXELS);
    picture.setHeight(USER_PICTURE_SIZE, Unit.PIXELS);

    VerticalLayout infosLayout = new VerticalLayout();
    Label name = new Label(userFirstName + " " + userLastName);
    Label login = new Label(userLogin);
    Label mail = new Label(userEmail);

    name.setStyleName(NovaForge.TEXT_ELLIPSIS);
    name.addStyleName(NovaForge.LABEL_GREEN);
    name.addStyleName(NovaForge.LABEL_BOLD);
    login.setStyleName(NovaForge.TEXT_ELLIPSIS);
    mail.setStyleName(NovaForge.TEXT_ELLIPSIS);

    infosLayout.addComponent(name);
    infosLayout.addComponent(login);
    infosLayout.addComponent(mail);
    infosLayout.setStyleName(NovaForge.TEXT_ELLIPSIS);

    VerticalLayout actionsLayout = new VerticalLayout();
    actionsLayout.setHeight(80, Unit.PERCENTAGE);
    actionsLayout.setWidth(27, Unit.PIXELS);
    actionsLayout.setStyleName(NovaForge.BORDER_LEFT_GREY);
    final Embedded editButton = new Embedded();
    editButton.setSource(new ThemeResource(NovaForgeResources.ICON_USER_EDIT));
    editButton.setWidth(ACTION_BUTTON_SIZE);
    editButton.setDescription(AdminModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.USERMANAGEMENT_ACTION_EDIT_USER));
    editButton.addClickListener(new ClickListener()
    {

      /**
     *
     */
      private static final long serialVersionUID = -1334165982785147263L;

      @Override
      public void click(com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.actionEditProfileClicked(userLogin);

      }
    });
    final Embedded deleteButton = new Embedded();
    deleteButton.setSource(new ThemeResource(NovaForgeResources.ICON_USER_TRASH));
    deleteButton.setWidth(ACTION_BUTTON_SIZE);
    deleteButton.setDescription(AdminModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.USERMANAGEMENT_ACTION_DELETE_USER));
    deleteButton.addClickListener(new ClickListener()
    {

      /**
     *
     */
      private static final long serialVersionUID = -4872611181027713354L;

      @Override
      public void click(com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.actionDeleteUserClicked(userLogin);

      }
    });
    actionsLayout.addComponent(editButton);
    actionsLayout.addComponent(deleteButton);

    userLayout.addComponent(picture);
    userLayout.addComponent(infosLayout);
    userLayout.addComponent(actionsLayout);

    userLayout.setSpacing(true);
    userLayout.setMargin(false);

    userLayout.setComponentAlignment(picture, Alignment.MIDDLE_LEFT);
    userLayout.setComponentAlignment(actionsLayout, Alignment.MIDDLE_RIGHT);
    userLayout.setExpandRatio(infosLayout, 1);

    userLayout.addLayoutClickListener(new LayoutClickListener()
    {

      /**
     *
     */
      private static final long serialVersionUID = 3184755498989979152L;

      @Override
      public void layoutClick(LayoutClickEvent event)
      {
        if (!editButton.equals(event.getClickedComponent()) && !deleteButton.equals(event.getClickedComponent()))
        {
          presenter.actionViewProfileClicked(userLogin);
        }

      }
    });
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

    if ((UserProfileItemProperty.LOGIN.getPropertyId().equals(pPropertyId))
        || (UserProfileItemProperty.EMAIL.getPropertyId().equals(pPropertyId))
        || (UserProfileItemProperty.FIRSTNAME.getPropertyId().equals(pPropertyId))
        || (UserProfileItemProperty.LASTNAME.getPropertyId().equals(pPropertyId))
        || (UserProfileItemProperty.PICTURE.getPropertyId().equals(pPropertyId)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object itemLogin = item.getItemProperty(UserProfileItemProperty.LOGIN.getPropertyId()).getValue();
      final Object itemFirstName = item.getItemProperty(UserProfileItemProperty.FIRSTNAME.getPropertyId())
          .getValue();
      final Object itemLastName = item.getItemProperty(UserProfileItemProperty.LASTNAME.getPropertyId())
          .getValue();
      final Object itemEmail = item.getItemProperty(UserProfileItemProperty.EMAIL.getPropertyId()).getValue();
      final Object itemPicture = item.getItemProperty(UserProfileItemProperty.PICTURE.getPropertyId())
          .getValue();

      isNeeded = (itemLogin != null) && (itemFirstName != null) && (itemLastName != null)
          && (itemEmail != null) && (itemPicture != null);
    }
    return isNeeded;
  }
}
