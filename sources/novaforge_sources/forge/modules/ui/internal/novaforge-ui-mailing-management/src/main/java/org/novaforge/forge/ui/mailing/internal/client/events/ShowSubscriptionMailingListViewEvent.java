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
package org.novaforge.forge.ui.mailing.internal.client.events;

import java.util.UUID;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListsContainer;

/**
 * @author sbenoist
 */
public class ShowSubscriptionMailingListViewEvent implements PortalEvent
{
  private final String                listname;
  
  private final Object               item;
  
  private final MailingListsContainer  mailingListsContainer;
  
  /** 
  * Unique identifier of module instance 
  */ 
  private final UUID                  moduleUUID; 

  /**
   * @param mailingListId
   */
  public ShowSubscriptionMailingListViewEvent(final String pListname, final Object pItem, final MailingListsContainer pMailingListsContainer, final UUID pModuleUUID)
  {
    super();
    listname = pListname;
    item = pItem;
    mailingListsContainer = pMailingListsContainer;
    moduleUUID = pModuleUUID; 
  }

  /**
   * @return the item
   */
  public Object getItem()
  {
    return item;
  }

  /**
   * @return the mailingListsContainer
   */
  public MailingListsContainer getMailingListsContainer()
  {
    return mailingListsContainer;
  }

  /**
   * @return the listname
   */
  public String getListname()
  {
    return listname;
  }
  
  /**
   * Return the unique identifier of module instance
   * 
   * @return the unique identifier of module instance
   */
  public UUID getUuid()
  {
    return moduleUUID;
  }

}
