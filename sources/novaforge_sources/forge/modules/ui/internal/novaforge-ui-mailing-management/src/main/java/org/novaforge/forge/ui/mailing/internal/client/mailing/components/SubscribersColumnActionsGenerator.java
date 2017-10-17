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
package org.novaforge.forge.ui.mailing.internal.client.mailing.components;

import com.vaadin.data.Item;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListServiceException;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.SubscriptionMailingListPresenter;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * @author sbenoist
 */
public class SubscribersColumnActionsGenerator extends AbstractColumnNameGenerator
{

  /**
   * serialization UID
   */
  private static final long                      serialVersionUID = 8408189342189534318L;
  private static final Log                       LOGGER           = LogFactory
                                                                      .getLog(SubscribersColumnActionsGenerator.class);
  private final SubscriptionMailingListPresenter presenter;

  /**
   * @param presenter
   */
  public SubscribersColumnActionsGenerator(final SubscriptionMailingListPresenter presenter)
  {
    super();
    this.presenter = presenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final Table pSource, final Object pItemId, final Object pColumnId)
  {
    final Item item = pSource.getItem(pItemId);
    final Locale locale = presenter.getComponent().getLocale();
    final String subscriberInfo = (String) item.getItemProperty(
        MailingListItemProperty.SUBSCRIBER.getPropertyId()).getValue();

    // ACTION unsubscribe
    final Embedded unsubscribeIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_CLOSE_RED));
    unsubscribeIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    unsubscribeIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    unsubscribeIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    unsubscribeIcon.setDescription(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MAILING_LISTS_UNSUBSCRIBE_ACTION));
    unsubscribeIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Serialization UID
       */
      private static final long serialVersionUID = 8152076640358331097L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.onClickActionUnsubscribe(pItemId);
      }
    });

    // Action Information
    final boolean isGroup = (Boolean) item.getItemProperty(MailingListItemProperty.IS_GROUP.getPropertyId())
        .getValue();
    Embedded informationIcon = null;
    if (isGroup)
    {
      informationIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_GROUP));
      informationIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      informationIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      informationIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      informationIcon.setDescription(MailingModule.getPortalMessages().getMessage(locale,
          Messages.MAILING_LISTS_GROUP_INFO_DESCRIPTION));
      informationIcon.addClickListener(new MouseEvents.ClickListener()
      {

        private static final long serialVersionUID = 7631321491721266476L;

        @Override
        public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
        {
          UI.getCurrent().addWindow(getGroupMembersWindow(pItemId));
        }
      });
    }
    else if (!isAnEmail(subscriberInfo))
    {
      informationIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_USER_INFO));
      informationIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      informationIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      informationIcon.setStyleName(NovaForge.BUTTON_IMAGE);

      informationIcon.setDescription(MailingModule.getPortalMessages().getMessage(locale,
          Messages.MAILING_LISTS_USER_INFO_DESCRIPTION));

      informationIcon.addClickListener(new MouseEvents.ClickListener()
      {

        private static final long serialVersionUID = -3054544617951605018L;

        @Override
        public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
        {
          try
          {
            final UserProfile userProfile = MailingModule.getUserPresenter().getUserProfile(subscriberInfo);
            presenter.getPortalContext().getEventBus().publish(new OpenUserProfileEvent(userProfile));
          }
          catch (final UserServiceException e)
          {
            LOGGER.error(e.getLocalizedMessage(), e);
          }
        }
      });
    }

    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setSpacing(true);
    actionsLayout.addComponent(unsubscribeIcon);
    actionsLayout.setComponentAlignment(unsubscribeIcon, Alignment.MIDDLE_CENTER);
    if (informationIcon != null)
    {
      actionsLayout.addComponent(informationIcon);
      actionsLayout.setComponentAlignment(informationIcon, Alignment.MIDDLE_CENTER);
    }
    actionsLayout.setSizeUndefined();

    return actionsLayout;
  }

  private Window getGroupMembersWindow(final Object pItemId)
  {
    final MailingListGroup group = (MailingListGroup) presenter.getSubscribersContainer()
        .getContainerProperty(pItemId, MailingListItemProperty.DEFAULT.getPropertyId()).getValue();
    final Locale locale = presenter.getComponent().getLocale();

    final Window groupMembersWindow = new Window();
    groupMembersWindow.setModal(true);
    groupMembersWindow.setResizable(true);
    groupMembersWindow.setWidth(400, Unit.PIXELS);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(100, Unit.PERCENTAGE);

    try
    {
      // refresh members information if possible
      final List<MailingListUser> members = presenter.getGroupMembers(group.getUUID());
      group.setMembers(members);
    }
    catch (final MailingListServiceException e)
    {
      LOGGER.error("Error during getting group members", e);
    }

    for (final MailingListUser user : group.getMembers())
    {
      windowLayout.addComponent(getUserProfileComponent(user.getLogin()));
    }

    groupMembersWindow.setCaption(MailingModule.getPortalMessages().getMessage(locale,
        Messages.MAILING_LISTS_GROUP_MEMBERS_TITLE));

    groupMembersWindow.setContent(windowLayout);
    return groupMembersWindow;
  }

  private boolean isAnEmail(final String pValue)
  {
    boolean returned = false;
    if (pValue.contains("@"))
    {
      returned = true;
    }
    return returned;
  }

  private AbstractComponent getUserProfileComponent(final String pLogin)
  {
    AbstractComponent subscriberComponent = null;
    try
    {
      final UserProfile userProfile = MailingModule.getUserPresenter().getUserProfile(pLogin);

      final User user = userProfile.getUser();
      subscriberComponent = new Button(getSubscriberInfo(user));
      subscriberComponent.setStyleName(Reindeer.BUTTON_LINK);
      final Button ownerLink = (Button) subscriberComponent;
      ownerLink.addClickListener(new ClickListener()
      {
        private static final long serialVersionUID = -7163642583590448808L;

        @Override
        public void buttonClick(final ClickEvent event)
        {
          presenter.getPortalContext().getEventBus().publish(new OpenUserProfileEvent(userProfile));

          final Collection<Window> windows = UI.getCurrent().getWindows();
          if (windows != null && windows.size() == 1)
          {
            UI.getCurrent().removeWindow(windows.iterator().next());
          }

        }
      });
    }
    catch (final UserServiceException e)
    {
      LOGGER.error(e.getLocalizedMessage(), e);
      subscriberComponent = new Label(pLogin);
    }

    return subscriberComponent;
  }

}
