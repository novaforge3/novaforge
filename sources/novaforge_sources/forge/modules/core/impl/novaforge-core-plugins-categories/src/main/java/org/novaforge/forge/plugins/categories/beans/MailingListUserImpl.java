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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;

/**
 * @author sbenoist
 */
public class MailingListUserImpl implements MailingListUser
{

  private static final long serialVersionUID = 559220446501852712L;

  private boolean           external;

  private String            login;

  private String            email;

  /**
   * This method allows to build an external (to the forge) mailing list user
   *
   * @param pEmail
   */
  public MailingListUserImpl(final String pEmail)
  {
    this(null, pEmail);
    external = true;
  }

  /**
   * This method allows to build an internal (to the forge) mailing list user
   *
   * @param pLogin
   * @param pEmail
   */
  public MailingListUserImpl(final String pLogin, final String pEmail)
  {
    this.login = pLogin;
    this.email = pEmail;
    external = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isExternal()
  {
    return external;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setExternal(final boolean pExternal)
  {
    external = pExternal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLogin()
  {
    return login;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLogin(final String pLogin)
  {
    login = pLogin;
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
  public void setEmail(final String pEmail)
  {
    email = pEmail;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getEmail()).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof MailingListUserImpl))
    {
      return false;
    }
    final MailingListUserImpl castOther = (MailingListUserImpl) other;
    return new EqualsBuilder().append(getEmail(), castOther.getEmail()).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "MailingListUserImpl [external=" + external + ", login=" + login + ", email=" + email + "]";
  }

}
