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
package org.novaforge.forge.core.plugins.categories.mailinglist;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sbenoist
 */
public enum MailingListType
{
  PRIVATE_LIST
  {
    @Override
    public String getLabel()
    {
      return "private list";
    }

    @Override
    public boolean isInstanciable()
    {
      return true;
    }

    @Override
    public boolean isVisible()
    {
      return false;
    }

    @Override
    public boolean isCustom()
    {
      return false;
    }
  },

  PUBLIC_LIST
  {
    @Override
    public String getLabel()
    {
      return "public list";
    }

    @Override
    public boolean isInstanciable()
    {
      return false;
    }

    @Override
    public boolean isVisible()
    {
      return false;
    }

    @Override
    public boolean isCustom()
    {
      return true;
    }
  },

  NEWSLETTER_LIST
  {
    @Override
    public String getLabel()
    {
      return "newsletter list";
    }

    @Override
    public boolean isInstanciable()
    {
      return false;
    }

    @Override
    public boolean isVisible()
    {
      return false;
    }

    @Override
    public boolean isCustom()
    {
      return true;
    }
  },

  HOTLINE_LIST
  {
    @Override
    public String getLabel()
    {
      return "hotline list";
    }

    @Override
    public boolean isInstanciable()
    {
      return true;
    }

    @Override
    public boolean isVisible()
    {
      return true;
    }

    @Override
    public boolean isCustom()
    {
      return false;
    }
  },

  INTRANET_LIST
  {
    @Override
    public String getLabel()
    {
      return "intranet list";
    }

    @Override
    public boolean isInstanciable()
    {
      return false;
    }

    @Override
    public boolean isVisible()
    {
      return false;
    }

    @Override
    public boolean isCustom()
    {
      return true;
    }
  },

  FORUM_LIST
  {
    @Override
    public String getLabel()
    {
      return "forum list";
    }

    @Override
    public boolean isInstanciable()
    {
      return false;
    }

    @Override
    public boolean isVisible()
    {
      return false;
    }

    @Override
    public boolean isCustom()
    {
      return true;
    }
  };

  private static final Map<String, MailingListType> types = new HashMap<String, MailingListType>();

  static
  {
    for (final MailingListType type : values())
    {
      types.put(type.getLabel(), type);
    }
  }

  /**
   * Return the {@link MailingListType} from its label
   *
   * @param pLabel
   *          the type's label
   * @return {@link MailingListType} according its label
   */
  public static MailingListType fromLabel(final String pLabel)
  {
    return types.get(pLabel);
  }

  /**
   * This method returns the label of the type
   *
   * @return the label
   */
  public abstract String getLabel();

  /**
   * This method returns true if a mailinglist of this type can be instanciated
   *
   * @return boolean
   */
  public abstract boolean isInstanciable();

  /**
   * This method returns true if a mailinglist of this type can be visible for others than subscribers
   *
   * @return boolean
   */
  public abstract boolean isVisible();

  /**
   * This method returns true if this type is considered like custom for new UI
   *
   * @return boolean
   */
  public abstract boolean isCustom();
}
