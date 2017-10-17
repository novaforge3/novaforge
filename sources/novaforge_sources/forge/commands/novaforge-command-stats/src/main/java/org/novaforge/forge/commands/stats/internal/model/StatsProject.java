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
package org.novaforge.forge.commands.stats.internal.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class StatsProject
{
  private final String                  name;
  private final Date                    created;
  private final String                  contact;
  private final Map<String, StatsUsers> applications = new HashMap<>();
  private StatsUsers users;

  public StatsProject(final String pName, final Date pCreatedDate, final String pContact)
  {
    name = pName;
    created = pCreatedDate;
    contact = pContact;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @return the users
   */
  public StatsUsers getUsers()
  {
    return users;
  }

  /**
   * @param pUsers
   *          the users to set
   */
  public void setUsers(final StatsUsers pUsers)
  {
    users = pUsers;
  }

  /**
   * @return the created
   */
  public Date getCreated()
  {
    return created;
  }

  /**
   * @return the contact
   */
  public String getContact()
  {
    return contact;
  }

  /**
   * @return the applications
   */
  public Map<String, StatsUsers> getApplications()
  {
    return applications;
  }

}
