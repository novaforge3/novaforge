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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.components;

import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;

import java.util.Date;
import java.util.List;

/**
 * @author Jeremy Casery
 */
public class RequestsContainer extends IndexedContainer
{

  /**
   * SerialUID
   */
  private static final long serialVersionUID = 6954030599683680120L;

  /**
   * Default constructor, initialize the container properties
   */
  public RequestsContainer()
  {
    super();
    addContainerProperty(RequestsItemProperty.APPLICATION.getPropertyId(), String.class, null);
    addContainerProperty(RequestsItemProperty.CREATEDDATE.getPropertyId(), Date.class, null);
    addContainerProperty(RequestsItemProperty.LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(RequestsItemProperty.PROJECT.getPropertyId(), String.class, null);
  }

  /**
   * Add requests into container
   * 
   * @param pRequests
   *          requests to add
   */
  public void setRequests(final List<ProjectApplicationRequest> pRequests)
  {
    removeAllItems();
    for (final ProjectApplicationRequest request : pRequests)
    {
      final Object itemId = request.getProject().getName();
      addItem(itemId);
      getContainerProperty(itemId, RequestsItemProperty.APPLICATION.getPropertyId()).setValue(
          request.getApplication().getName());
      getContainerProperty(itemId, RequestsItemProperty.CREATEDDATE.getPropertyId()).setValue(
          request.getCreated());
      getContainerProperty(itemId, RequestsItemProperty.LOGIN.getPropertyId()).setValue(request.getLogin());
      getContainerProperty(itemId, RequestsItemProperty.PROJECT.getPropertyId()).setValue(
          request.getProject().getName());
    }
    sort(new Object[] { RequestsItemProperty.CREATEDDATE.getPropertyId() }, new boolean[] { true });
  }

}
