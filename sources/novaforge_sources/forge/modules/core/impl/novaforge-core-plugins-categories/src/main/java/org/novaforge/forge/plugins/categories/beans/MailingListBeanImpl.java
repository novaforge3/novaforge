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
package org.novaforge.forge.plugins.categories.beans;

import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListType;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sbenoist
 */
public class MailingListBeanImpl implements MailingListBean
{

  private static final long           serialVersionUID = -357085344524686617L;

  private String                      name;

  private String                      description;

  private String                      subject;

  private MailingListType             type;

  private boolean                     locked           = false;

  private List<MailingListSubscriber> subscribers      = new ArrayList<MailingListSubscriber>();

  private List<MailingListUser>       owners           = new ArrayList<MailingListUser>();

  /**
   * {@inheritDoc}
   */
  @Override
  public MailingListType getType()
  {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setType(final MailingListType pType)
  {
    type = pType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSubject()
  {
    return subject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSubject(final String pSubject)
  {
    subject = pSubject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLocked()
  {
    return locked;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLocked(final boolean pLocked)
  {
    locked = pLocked;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MailingListSubscriber> getSubscribers()
  {
    return subscribers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSubscribers(final List<MailingListSubscriber> pSubscribers)
  {
    subscribers = pSubscribers;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MailingListUser> getOwners()
  {
    return owners;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOwners(final List<MailingListUser> pOwners)
  {
    owners = pOwners;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSubscriber(final MailingListSubscriber pUser)
  {
    subscribers.add(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addGroupsSubscribers(final List<MailingListGroup> pGroupsSubscribers)
  {
    subscribers.addAll(pGroupsSubscribers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSubscriber(final MailingListSubscriber pUser)
  {
    subscribers.remove(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addOwner(final MailingListUser pUser)
  {
    owners.add(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeOwner(final MailingListUser pUser)
  {
    owners.remove(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "MailingListBeanImpl [name=" + name + ", description=" + description + ", subject=" + subject + ", type="
               + type + ", locked=" + locked + ", subscribers=" + subscribers + ", owners=" + owners + "]";
  }

}
