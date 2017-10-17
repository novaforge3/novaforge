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
package org.novaforge.forge.plugins.devops.novadeploy.internal.model;

import org.novaforge.forge.plugins.devops.novadeploy.model.NovadeployUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dekimpea
 */
public class NovadeployUserImpl implements NovadeployUser
{
  private final List<String> groups = new ArrayList<String>();
  private String             username;
//  private String             lastname;
//  private String             firstname;
//  private String             email;
  private String             password;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserName()
  {
    return username;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserName(final String pUserName)
  {
    username = pUserName;
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public String getLastName()
//  {
//
//    return lastname;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public void setLastName(final String pLastName)
//  {
//    lastname = pLastName;
//
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public String getFisrtName()
//  {
//
//    return firstname;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public void setFisrtName(final String pFisrtName)
//  {
//    firstname = pFisrtName;
//
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public void setEmail(final String pEmail)
//  {
//    email = pEmail;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public String getEmail()
//  {
//    return email;
//  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPassword()
  {
    return password;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPassword(final String pPassword)
  {
    password = pPassword;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getGroups()
  {
    return Collections.unmodifiableList(groups);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeGroup(final String pGroup)
  {
    return groups.remove(pGroup);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addGroup(final String pGroup)
  {
    return groups.add(pGroup);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    //    builder.append("NovadeployUserImpl [username=").append(username).append(", lastname=").append(lastname)
    //        .append(", firstname=").append(firstname).append(", email=").append(email).append(", password=")
    //        .append(password).append(", groups=").append(groups).append("]");
    return "NovadeployUserImpl [username=" + username + ", password=" + password + ", groups=" + groups + "]";
  }

}
