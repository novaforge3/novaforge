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
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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
public class MailingListsColumnSuscribeGenerator implements ColumnGenerator
{

  /**
   * 
   */
  private static final long           serialVersionUID = 8514764622646637677L;
  private final MailingListsPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the mailing lists
   */
  public MailingListsColumnSuscribeGenerator(final MailingListsPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
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
    actionsLayout.setSizeFull();

    final Item item = pSource.getItem(pItemId);

    final boolean isLocked = (Boolean) item.getItemProperty(MailingListItemProperty.LOCKED.getPropertyId())
        .getValue();
    if (!isLocked)
    {
      // ACTION Subcribe and unsubcribe
      final boolean isSubscriber = (Boolean) item.getItemProperty(
          MailingListItemProperty.IS_SUBSCRIBER.getPropertyId()).getValue();
      Button subscribeButton = null;
      if (isSubscriber)
      {

        subscribeButton = new Button();
        subscribeButton.setStyleName(NovaForge.BUTTON_LINK);
        subscribeButton.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_LEFT));
        subscribeButton.setCaption(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
            Messages.MAILING_LISTS_UNSUBSCRIBE_LIST));
        subscribeButton.addClickListener(new Button.ClickListener()
        {
          /** Serial version id */
          private static final long serialVersionUID = 4799439581656895309L;

          @Override
          public void buttonClick(ClickEvent event)
          {
            presenter.onClickActionUnsubscribe(pItemId);
          }
        });
      }
      else
      {
        subscribeButton = new Button();
        subscribeButton.setStyleName(NovaForge.BUTTON_PRIMARY);
        subscribeButton.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_JOIN));
        subscribeButton.setCaption(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
            Messages.MAILING_LISTS_SUBSCRIBE_LIST));
        subscribeButton.addClickListener(new Button.ClickListener()
        {
          /** Serial version id */
          private static final long serialVersionUID = 4799439581656895309L;

          @Override
          public void buttonClick(ClickEvent event)
          {
            presenter.onClickActionSubscribe(pItemId);
          }
        });
      }
      actionsLayout.addComponent(subscribeButton);
      actionsLayout.setComponentAlignment(subscribeButton, Alignment.MIDDLE_CENTER);
    }

    return actionsLayout;
  }

}
