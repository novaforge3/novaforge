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
package org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container;

/**
 * @author Guillaume Lamirand
 */
public enum CommitItemProperty
{
  ICON
  {
    @Override
    public String getPropertyId()
    {
      return "icon";
    }
  },
  REPOSITORY
  {
    @Override
    public String getPropertyId()
    {
      return "repository";
    }
  },
  REVISION
  {
    @Override
    public String getPropertyId()
    {
      return "revision";
    }
  },
  COMMENT
  {
    @Override
    public String getPropertyId()
    {
      return "comment";
    }
  },
  DATE
  {
    @Override
    public String getPropertyId()
    {
      return "date";
    }
  },
  AUTHOR_LOGIN
  {
    @Override
    public String getPropertyId()
    {
      return "author_login";
    }
  },
  AUTHOR_EMAIL
  {
    @Override
    public String getPropertyId()
    {
      return "author_email";
    }
  },
  AUTHOR_PROFILE
  {
    @Override
    public String getPropertyId()
    {
      return "author_profile";
    }
  },
  CHANGES
  {
    @Override
    public String getPropertyId()
    {
      return "changes";
    }
  };
  /**
   * Get ItemPropertyId used by {@link Table}
   * 
   * @return property id
   */
  public abstract String getPropertyId();

}
