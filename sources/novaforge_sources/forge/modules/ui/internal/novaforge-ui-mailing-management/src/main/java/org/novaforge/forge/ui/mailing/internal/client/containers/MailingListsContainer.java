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
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.ui.portal.data.util.StringComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author B-Martinelli
 */
public class MailingListsContainer extends IndexedContainer
{
  /** Serial uid */
  private static final long serialVersionUID = 7245265409387407802L;

  /**
   * Default constructor. Initialize mailing lists items properties
   * 
   * @see MailingListItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public MailingListsContainer()
  {
    super();
    addContainerProperty(MailingListItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(MailingListItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(MailingListItemProperty.OWNERS.getPropertyId(), List.class, new ArrayList<String>());
    addContainerProperty(MailingListItemProperty.SUBSCRIBERS.getPropertyId(), List.class,
        new ArrayList<MailingListUser>());
    addContainerProperty(MailingListItemProperty.SUBSCRIBERS_NB.getPropertyId(), Integer.class, 0);
    addContainerProperty(MailingListItemProperty.LOCKED.getPropertyId(), Boolean.class, false);
    addContainerProperty(MailingListItemProperty.DEFAULT.getPropertyId(), MailingListBean.class, 0);
    addContainerProperty(MailingListItemProperty.IS_SUBSCRIBER.getPropertyId(), Boolean.class, false);
    addContainerProperty(MailingListItemProperty.IS_GROUP.getPropertyId(), Boolean.class, false);
    addContainerProperty(MailingListItemProperty.IS_OWNER.getPropertyId(), Boolean.class, false);

    setItemSorter(new DefaultItemSorter(new StringComparator()));
  }

  /**
   * Add mailing lists into container
   * 
   * @param pMailingLists
   *          mailing lists to add
   * @param pCurrentUser
   *          current user login
   */
  public void setMailingLists(final List<MailingListBean> pMailingLists, final String pCurrentUser)
  {
    removeAllContainerFilters();
    removeAllItems();
    String name = null;
    for (final MailingListBean mailingList : pMailingLists)
    {
      name = mailingList.getName();
      if (!containsId(name))
      {
        addItem(name);
        addMailingList(name, mailingList);
        addSubscriber(name, mailingList, pCurrentUser);
        addOwner(name, mailingList, pCurrentUser);
      }
    }
    sort(new Object[] { MailingListItemProperty.NAME.getPropertyId() }, new boolean[] { true });
  }

  /**
   * Add the given mailing list to container.
   * 
   * @param pItemId
   *          item id
   * @param pMailingList
   *          mailing list to add
   */
  @SuppressWarnings("unchecked")
  private void addMailingList(final String pItemId, final MailingListBean pMailingList)
  {
    getContainerProperty(pItemId, MailingListItemProperty.NAME.getPropertyId()).setValue(
        pMailingList.getName());
    getContainerProperty(pItemId, MailingListItemProperty.DESCRIPTION.getPropertyId()).setValue(
        pMailingList.getDescription());
    getContainerProperty(pItemId, MailingListItemProperty.OWNERS.getPropertyId()).setValue(
        pMailingList.getOwners());
    getContainerProperty(pItemId, MailingListItemProperty.SUBSCRIBERS.getPropertyId()).setValue(
        pMailingList.getSubscribers());
    getContainerProperty(pItemId, MailingListItemProperty.SUBSCRIBERS_NB.getPropertyId()).setValue(
        computeSubscribersAmount(pMailingList));
    getContainerProperty(pItemId, MailingListItemProperty.LOCKED.getPropertyId()).setValue(
        pMailingList.isLocked());

    getContainerProperty(pItemId, MailingListItemProperty.DEFAULT.getPropertyId()).setValue(pMailingList);
  }

  /**
   * Add property {@link MailingListItemProperty#IS_SUBSCRIBER} if current user has subscribed the list
   *
   * @param pMailingList
   * @param pCurrentUser
   */
  @SuppressWarnings("unchecked")
  private void addSubscriber(final String pItemId, final MailingListBean pMailingList,
      final String pCurrentUser)
  {
    final List<MailingListSubscriber> subscribers = pMailingList.getSubscribers();
    for (final MailingListSubscriber subscriber : subscribers)
    {
      if (subscriber instanceof MailingListUser)
      {
        final MailingListUser mailingListUser = (MailingListUser) subscriber;
        if ((!mailingListUser.isExternal()) && (mailingListUser.getLogin().equals(pCurrentUser)))
        {
          getContainerProperty(pItemId, MailingListItemProperty.IS_SUBSCRIBER.getPropertyId()).setValue(true);
          break;
        }
      }
      else if (subscriber instanceof MailingListGroup)
      {
        getContainerProperty(pItemId, MailingListItemProperty.IS_GROUP.getPropertyId()).setValue(true);
      }
    }
  }

  /**
   * Add property {@link MailingListItemProperty#IS_OWNER} if current user has subscribed the list
   *
   * @param pMailingList
   * @param pCurrentUser
   */
  @SuppressWarnings("unchecked")
  private void addOwner(final String pItemId, final MailingListBean pMailingList, final String pCurrentUser)
  {
    for (final MailingListUser mailingListUser : pMailingList.getOwners())
    {
      if ((!mailingListUser.isExternal()) && (mailingListUser.getLogin().equals(pCurrentUser)))
      {
        getContainerProperty(pItemId, MailingListItemProperty.IS_OWNER.getPropertyId()).setValue(true);
        break;
      }

    }

  }

  /**
   * Compute how many subscribers are in the mailing list
   *
   * @param pMailingListt
   *     the mailing list
   *
   * @return how many subscribers are in the mailing list
   */
  private int computeSubscribersAmount(final MailingListBean pMailingList)
  {
    int result = -1;
    if (pMailingList.getSubscribers() != null)
    {
      result = pMailingList.getSubscribers().size();
    }
    return result;
  }
}
