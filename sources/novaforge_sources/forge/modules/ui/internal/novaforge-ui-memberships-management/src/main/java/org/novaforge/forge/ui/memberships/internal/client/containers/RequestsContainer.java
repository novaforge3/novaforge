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
package org.novaforge.forge.ui.memberships.internal.client.containers;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.enumerations.MembershipRequestStatus;
import org.novaforge.forge.ui.portal.data.util.StringComparator;

import java.util.Date;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class RequestsContainer extends IndexedContainer
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 7245265409387407802L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public RequestsContainer()
  {
    super();
    addContainerProperty(RequestItemProperty.LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(RequestItemProperty.FIRSTNAME.getPropertyId(), String.class, null);
    addContainerProperty(RequestItemProperty.LASTNAME.getPropertyId(), String.class, null);
    addContainerProperty(RequestItemProperty.DATE.getPropertyId(), Date.class, null);
    addContainerProperty(RequestItemProperty.MESSAGE.getPropertyId(), String.class, null);
    addContainerProperty(RequestItemProperty.STATUS.getPropertyId(), MembershipRequestStatus.class, null);

    setItemSorter(new DefaultItemSorter(new StringComparator()));
  }

  /**
   * Add {@link MembershipRequest} into container
   * 
   * @param pRequests
   *          {@link MembershipRequest} to add
   */
  public void setRequests(final List<MembershipRequest> pRequests)
  {
    removeAllContainerFilters();
    removeAllItems();
    for (final MembershipRequest request : pRequests)
    {
      final String itemId = request.getUser().getLogin();
      addItem(itemId);
      getContainerProperty(itemId, RequestItemProperty.LOGIN.getPropertyId()).setValue(
          request.getUser().getLogin());
      getContainerProperty(itemId, RequestItemProperty.FIRSTNAME.getPropertyId()).setValue(
          request.getUser().getFirstName());
      getContainerProperty(itemId, RequestItemProperty.LASTNAME.getPropertyId()).setValue(
          request.getUser().getName());
      getContainerProperty(itemId, RequestItemProperty.DATE.getPropertyId()).setValue(
          request.getCreatedDate());
      getContainerProperty(itemId, RequestItemProperty.MESSAGE.getPropertyId())
          .setValue(request.getMessage());
      getContainerProperty(itemId, RequestItemProperty.STATUS.getPropertyId()).setValue(request.getStatus());

    }

    sort(new Object[] { UserItemProperty.LOGIN.getPropertyId() }, new boolean[] { true });
  }

}
