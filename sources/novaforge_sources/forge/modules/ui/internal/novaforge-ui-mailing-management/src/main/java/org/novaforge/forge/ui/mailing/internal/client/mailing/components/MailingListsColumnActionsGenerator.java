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
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.mailing.update.MailingListsPresenter;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;

/**
 * @author Guillaume Lamirand
 */
public class MailingListsColumnActionsGenerator implements ColumnGenerator
{
  /** Serial version id */
  private static final long           serialVersionUID    = 8736106711161577296L;

  private final MailingListsPresenter presenter;

  private boolean                     hasAdminPermissions = false;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the mailing lists
   */
  public MailingListsColumnActionsGenerator(final MailingListsPresenter pPresenter,
      final boolean pHasAdminPermissions)
  {
    super();
    presenter = pPresenter;
    hasAdminPermissions = pHasAdminPermissions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setSpacing(true);
    actionsLayout.setSizeUndefined();

    final Item item = pSource.getItem(pItemId);
    // ACTION View List
    final Embedded viewProfileIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_MAIL_INFO));
    viewProfileIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    viewProfileIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    viewProfileIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    viewProfileIcon.setDescription(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MAILING_LISTS_VIEW_LIST));
    viewProfileIcon.addClickListener(new MouseEvents.ClickListener()
    {
      /** Serial version id */
      private static final long serialVersionUID = 4799439581656895309L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.onClickActionViewDetails(pItemId);
      }
    });
    actionsLayout.addComponent(viewProfileIcon);
    actionsLayout.setComponentAlignment(viewProfileIcon, Alignment.MIDDLE_CENTER);

    final boolean isLocked = (Boolean) item.getItemProperty(MailingListItemProperty.LOCKED.getPropertyId())
        .getValue();
    if (!isLocked)
    {
      // ACTION Subcribe and unsubcribe
      // final boolean isSubscriber = (Boolean) item.getItemProperty(
      // MailingListItemProperty.IS_SUBSCRIBER.getPropertyId()).getValue();
      // Embedded subscribeIcon = null;
      // if (isSubscriber)
      // {
      //
      // subscribeIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_USER_LEFT));
      // subscribeIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      // subscribeIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      // subscribeIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      // subscribeIcon.setDescription(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
      // Messages.MAILING_LISTS_UNSUBSCRIBE_LIST));
      // subscribeIcon.addClickListener(new MouseEvents.ClickListener()
      // {
      // /** Serial version id */
      // private static final long serialVersionUID = 4799439581656895309L;
      //
      // @Override
      // public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      // {
      // presenter.onClickActionUnsubscribe(pItemId);
      // }
      // });
      // }
      // else
      // {
      // subscribeIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_USER_JOIN));
      // subscribeIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      // subscribeIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      // subscribeIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      // subscribeIcon.setDescription(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
      // Messages.MAILING_LISTS_SUBSCRIBE_LIST));
      // subscribeIcon.addClickListener(new MouseEvents.ClickListener()
      // {
      // /** Serial version id */
      // private static final long serialVersionUID = 4799439581656895309L;
      //
      // @Override
      // public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      // {
      // presenter.onClickActionSubscribe(pItemId);
      // }
      // });
      // }
      // actionsLayout.addComponent(subscribeIcon);
      // actionsLayout.setComponentAlignment(subscribeIcon, Alignment.MIDDLE_CENTER);

      Embedded subscriptionIcon = null;
      Embedded deleteMemberIcon = null;
      if (hasAdminPermissions)
      {
        // ACTION subscription
        subscriptionIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_MAIL_USERS));
        subscriptionIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
        subscriptionIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
        subscriptionIcon.setStyleName(NovaForge.BUTTON_IMAGE);
        subscriptionIcon.setDescription(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
            Messages.MAILING_LISTS_SUBSCRIBERS_ACTION));
        subscriptionIcon.addClickListener(new MouseEvents.ClickListener()
        {

          /**
           * Serial Version UID
           */
          private static final long serialVersionUID = 7122212615787164L;

          @Override
          public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
          {
            presenter.onClickActionSubscription(pItemId);
          }
        });

        // ACTION delete list
        deleteMemberIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_MAIL_TRASH));
        deleteMemberIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
        deleteMemberIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
        deleteMemberIcon.setStyleName(NovaForge.BUTTON_IMAGE);
        deleteMemberIcon.setDescription(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
            Messages.MAILING_LISTS_DELETE_LIST));
        deleteMemberIcon.addClickListener(new MouseEvents.ClickListener()
        {
          /** Serial version id */
          private static final long serialVersionUID = 671779180324183911L;

          @Override
          public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
          {
            presenter.onClickActionDelete(pItemId);
          }
        });
        actionsLayout.addComponent(subscriptionIcon);
        actionsLayout.addComponent(deleteMemberIcon);
        actionsLayout.setComponentAlignment(subscriptionIcon, Alignment.MIDDLE_CENTER);
        actionsLayout.setComponentAlignment(deleteMemberIcon, Alignment.MIDDLE_CENTER);
      }
    }

    return actionsLayout;
  }

}
