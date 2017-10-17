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
package org.novaforge.forge.plugins.surveytool.limesurvey.internal.model;

import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyUser;

/**
 * @author lamirang
 */
public class LimesurveyUserImpl implements LimesurveyUser
{
  private String username;
  private String fullname;
  private String email;
  private String password;
  private String language;

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
  public void setUserName(String pUserName)
  {
    username = pUserName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFullName()
  {

    return fullname;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFullName(String pFullName)
  {
    fullname = pFullName;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEmail()
  {
    return email;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEmail(String pEmail)
  {
    email = pEmail;
  }

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
  public void setPassword(String pPassword)
  {
    password = pPassword;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLanguage()
  {
    return language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLanguage(String pLanguage)
  {
    this.language = pLanguage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "LimesurveyUserImpl [username=" + username + ", fullname=" + fullname + ", email=" + email
        + ", password=" + password + ", language=" + language + " ]";
  }

}
