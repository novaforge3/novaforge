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
package org.novaforge.forge.ui.memberships.internal.client.request.components;

import com.vaadin.data.Item;
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
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.RequestItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.request.inprocess.RequestPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Jeremy Casery
 */
public class RequestItemGenerator implements ItemGenerator
{

  /**
   * 
   */
  private static final long      serialVersionUID      = -5769962217840631319L;

  private static final float     USER_PICTURE_SIZE     = 108;

  private static final String    USER_ITEM_LAYOUT_SIZE = "100%";

  private static final String    ACTION_BUTTON_SIZE    = "40px";

  private final RequestPresenter presenter;

  public RequestItemGenerator(final RequestPresenter pPresenter)
  {
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(final AbstractItemLayout pSource, final Object pItemId)
  {
    final Item requestItem = pSource.getContainerDataSource().getItem(pItemId);
    final String userLogin = (String) requestItem.getItemProperty(RequestItemProperty.LOGIN.getPropertyId())
        .getValue();
    final String userFirstName = (String) requestItem.getItemProperty(
        RequestItemProperty.FIRSTNAME.getPropertyId()).getValue();
    final String userLastName = (String) requestItem.getItemProperty(
        RequestItemProperty.LASTNAME.getPropertyId()).getValue();
    final String requestMessage = (String) requestItem.getItemProperty(
        RequestItemProperty.MESSAGE.getPropertyId()).getValue();
    final Date requestDate = (Date) requestItem.getItemProperty(RequestItemProperty.DATE.getPropertyId())
        .getValue();

    HorizontalLayout userLayout = new HorizontalLayout();
    userLayout.setWidth(USER_ITEM_LAYOUT_SIZE);
    userLayout.setStyleName(NovaForge.LAYOUT_ITEM);
    userLayout.setDescription(MembershipsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.USERMANAGEMENT_ACTION_VIEW_USER));

    Embedded picture = new Embedded();

    try
    {
      UserProfile userProfile = MembershipsModule.getUserPresenter().getUserProfile(userLogin);
      if (userProfile != null && userProfile.getImage() != null)
      {
        picture.setSource(ResourceUtils.buildImageResource(userProfile.getImage().getFile(), userLogin));
      }
      else
      {
        picture.setSource(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
      }
    }
    catch (UserServiceException e)
    {
      picture.setSource(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
    }
    picture.setStyleName(NovaForge.USER_ITEM_PICTURE);
    picture.setWidth(USER_PICTURE_SIZE, Unit.PIXELS);
    picture.setHeight(USER_PICTURE_SIZE, Unit.PIXELS);

    VerticalLayout infosLayout = new VerticalLayout();
    Label name = new Label(userFirstName + " " + userLastName);
    Label login = new Label(userLogin);
    Label message = new Label(requestMessage);
    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, UI.getCurrent().getLocale());
    Label date = new Label(dateFormatter.format(requestDate));

    name.setStyleName(NovaForge.TEXT_ELLIPSIS);
    name.addStyleName(NovaForge.LABEL_GREEN);
    name.addStyleName(NovaForge.LABEL_BOLD);
    login.setStyleName(NovaForge.TEXT_ELLIPSIS);
    message.setStyleName(NovaForge.TEXT_WRAP);
    date.setStyleName(NovaForge.TEXT_ELLIPSIS);

    infosLayout.addComponent(name);
    infosLayout.addComponent(login);
    infosLayout.addComponent(message);
    infosLayout.addComponent(date);
    infosLayout.setStyleName(NovaForge.TEXT_ELLIPSIS);
    infosLayout.setHeight(100, Unit.PERCENTAGE);
    infosLayout.setComponentAlignment(date, Alignment.TOP_LEFT);
    infosLayout.setExpandRatio(message, 1);

    VerticalLayout actionsLayout = new VerticalLayout();
    actionsLayout.setHeight(80, Unit.PERCENTAGE);
    actionsLayout.setWidth(45, Unit.PIXELS);
    actionsLayout.setStyleName(NovaForge.BORDER_LEFT_GREY);
    final Embedded validateButton = new Embedded();
    validateButton.setSource(new ThemeResource(NovaForgeResources.ICON_VALIDATE_ROUND));
    validateButton.setWidth(ACTION_BUTTON_SIZE);
    validateButton.setDescription(MembershipsModule.getPortalMessages().getMessage(
        UI.getCurrent().getLocale(), Messages.MEMBERSHIPS_REQUESTS_VALID_DESCRIPTION));
    validateButton.addClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 5854439696176484439L;

      @Override
      public void click(com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.onClickActionValid(pItemId);

      }
    });
    final Embedded rejectButton = new Embedded();
    rejectButton.setSource(new ThemeResource(NovaForgeResources.ICON_BLOCKED_ROUND));
    rejectButton.setWidth(ACTION_BUTTON_SIZE);
    rejectButton.setDescription(MembershipsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.MEMBERSHIPS_REQUESTS_INVALID_DESCRIPTION));
    rejectButton.addClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -3194469072715096242L;

      @Override
      public void click(com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.onClickActionInValid(pItemId);

      }
    });
    actionsLayout.addComponent(validateButton);
    actionsLayout.addComponent(rejectButton);

    userLayout.addComponent(picture);
    userLayout.addComponent(infosLayout);
    userLayout.addComponent(actionsLayout);
    userLayout.setExpandRatio(infosLayout, 1);

    userLayout.setSpacing(true);
    userLayout.setMargin(false);

    userLayout.setComponentAlignment(actionsLayout, Alignment.MIDDLE_RIGHT);

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

    if ((RequestItemProperty.LOGIN.getPropertyId().equals(pPropertyId))
        || (RequestItemProperty.MESSAGE.getPropertyId().equals(pPropertyId))
        || (RequestItemProperty.FIRSTNAME.getPropertyId().equals(pPropertyId))
        || (RequestItemProperty.DATE.getPropertyId().equals(pPropertyId))
        || (RequestItemProperty.LASTNAME.getPropertyId().equals(pPropertyId)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object itemLogin = item.getItemProperty(RequestItemProperty.LOGIN.getPropertyId()).getValue();
      final Object itemFirstName = item.getItemProperty(RequestItemProperty.FIRSTNAME.getPropertyId())
          .getValue();
      final Object itemLastName = item.getItemProperty(RequestItemProperty.LASTNAME.getPropertyId())
          .getValue();
      final Object itemDate = item.getItemProperty(RequestItemProperty.DATE.getPropertyId()).getValue();

      isNeeded = (itemLogin != null) && (itemFirstName != null) && (itemLastName != null)
          && (itemDate != null);
    }
    return isNeeded;
  }

}
