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
package org.novaforge.forge.ui.mailing.internal.client.containers;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.ui.portal.data.util.StringComparator;

import java.util.List;

/**
 * @author sbenoist
 */
public class SubscribersContainer extends IndexedContainer
{

  /** Serial uid */
  private static final long serialVersionUID = -2633710916559210421L;

  /**
   * 
   */
  public SubscribersContainer()
  {
    super();
    addContainerProperty(MailingListItemProperty.SUBSCRIBER.getPropertyId(), String.class, null);
    addContainerProperty(MailingListItemProperty.IS_GROUP.getPropertyId(), Boolean.class, false);
    addContainerProperty(MailingListItemProperty.DEFAULT.getPropertyId(), MailingListSubscriber.class, 0);

    setItemSorter(new DefaultItemSorter(new StringComparator()));
  }

  /**
   * Add subscribers into container
   * 
   * @param pSubscribers
   *          subscribers to add
   */
  @SuppressWarnings("unchecked")
  public void setSubscribers(final List<MailingListSubscriber> pSubscribers)
  {
    removeAllContainerFilters();
    removeAllItems();
    for (final MailingListSubscriber subscriber : pSubscribers)
    {
      if (subscriber instanceof MailingListUser)
      {

        final MailingListUser user = (MailingListUser) subscriber;
        if (!containsId(user.getEmail()))
        {
          addItem(user.getEmail());
          addSubscriber(user.getEmail(), user);
          getContainerProperty(user.getEmail(), MailingListItemProperty.DEFAULT.getPropertyId()).setValue(
              user);
        }
      }
      else if (subscriber instanceof MailingListGroup)
      {

        final MailingListGroup group = (MailingListGroup) subscriber;
        if (!containsId(group.getName()))
        {
          addItem(group.getName());
          addSubscriber(group.getName(), group);
          getContainerProperty(group.getName(), MailingListItemProperty.DEFAULT.getPropertyId()).setValue(
              group);
        }
      }

    }
    sort(new Object[] { MailingListItemProperty.SUBSCRIBER.getPropertyId() }, new boolean[] { true });
  }

  /**
   * Add the given subscriber to container.
   *
   * @param pItemId
   *          item id
   * @param pSubscriber
   *          subscriber to add
   */
  @SuppressWarnings("unchecked")
  private void addSubscriber(final String pItemId, final MailingListUser pSubscriber)
  {
    String value = null;
    if (pSubscriber.isExternal())
    {
      value = pSubscriber.getEmail();
    }
    else
    {
      value = pSubscriber.getLogin();
    }
    getContainerProperty(pItemId, MailingListItemProperty.SUBSCRIBER.getPropertyId()).setValue(value);
    getContainerProperty(pItemId, MailingListItemProperty.IS_GROUP.getPropertyId()).setValue(false);
  }

  /**
   * @param name
   * @param group
   */
  @SuppressWarnings("unchecked")
  private void addSubscriber(final String pName, final MailingListGroup pGroup)
  {
    getContainerProperty(pName, MailingListItemProperty.SUBSCRIBER.getPropertyId()).setValue(pName);
    getContainerProperty(pName, MailingListItemProperty.IS_GROUP.getPropertyId()).setValue(true);
  }
}
