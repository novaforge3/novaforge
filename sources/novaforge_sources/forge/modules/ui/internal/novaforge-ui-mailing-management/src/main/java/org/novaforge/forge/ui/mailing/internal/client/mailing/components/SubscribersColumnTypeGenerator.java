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
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.SubscriptionMailingListPresenter;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;

/**
 * @author sbenoist
 */
public class SubscribersColumnTypeGenerator extends AbstractColumnNameGenerator
{

  private static final long                      serialVersionUID = 868210873709975257L;

  private final SubscriptionMailingListPresenter presenter;

  public SubscribersColumnTypeGenerator(final SubscriptionMailingListPresenter pPresenter)
  {
    super();
    this.presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final Table pSource, final Object pItemId, final Object pColumnId)
  {
    final VerticalLayout layout = new VerticalLayout();
    final Item item = pSource.getItem(pItemId);
    final boolean isGroup = (Boolean) item.getItemProperty(MailingListItemProperty.IS_GROUP.getPropertyId())
        .getValue();

    final AbstractComponent typeComponent = new Label();
    if (isGroup)
    {
      typeComponent.setCaption(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MAILING_LISTS_SUBSCRIBER_TYPE_GROUP));
    }
    else
    {
      typeComponent.setCaption(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MAILING_LISTS_SUBSCRIBER_TYPE_USER));
    }
    layout.addComponent(typeComponent);
    return layout;
  }
}
