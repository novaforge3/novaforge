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
package org.novaforge.forge.ui.mailing.internal.client.containers;

import com.vaadin.ui.ComboBox;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.MailingListsFieldFactory;

/**
 * @author B-Martinelli
 */
public enum MailingListItemProperty
{
  /** Mailing list name. */
  NAME
  {
    @Override
    public String getPropertyId()
    {
      return "name";
    }
  },
  /** Mailing list description. */
  DESCRIPTION
  {
    @Override
    public String getPropertyId()
    {
      return "description";
    }
  },
  /** Mailing list type. */
  TYPE
  {
    @Override
    public String getPropertyId()
    {
      return "type";
    }
  },
  /** Mailing list locked. */
  LOCKED
  {
    @Override
    public String getPropertyId()
    {
      return "locked";
    }
  },
  /** Mailing list subject. */
  SUBJECT
  {
    @Override
    public String getPropertyId()
    {
      return "subject";
    }
  },
  /** Mailing list owners. */
  OWNERS
  {
    @Override
    public String getPropertyId()
    {
      return "owners";
    }
  },
  /** Mailing list subscribers */
  SUBSCRIBERS
  {
    @Override
    public String getPropertyId()
    {
      return "subscribers";
    }
  },
  /** Mailing list subscribers number */
  SUBSCRIBERS_NB
  {
    @Override
    public String getPropertyId()
    {
      return "subscribers_nb";
    }
  },
  /** Mailing list subscriber. */
  SUBSCRIBER
  {
    @Override
    public String getPropertyId()
    {
      return "subscriber";
    }
  },
  /** Define if current user has subcribed this list */
  IS_SUBSCRIBER
  {
    @Override
    public String getPropertyId()
    {
      return "is_subscriber";
    }
  },
  IS_GROUP
  {
    @Override
    public String getPropertyId()
    {
      return "is_group";
    }
  },
  /** Mailing list default column. */
  DEFAULT
  {
    @Override
    public String getPropertyId()
    {
      return "default";
    }
  },
  /** Mailing list prop setting if editable. */
  EDITABLE
  {
    @Override
    public String getPropertyId()
    {
      return "editable";
    }
  },
  IS_OWNER
  {
    @Override
    public String getPropertyId()
    {
      return "is_owner";
    }
  };

  /** Contains the array of fields to display in the form if the project is forge one. */
  public static final String[] MAILING_LISTS_FORGE_FIELDS = new String[] {
      MailingListsFieldFactory.NAME_FIELD, MailingListsFieldFactory.DESCRIPTION_FIELD,
      MailingListsFieldFactory.SUBSCRIBERS_FIELD, MailingListsFieldFactory.OWNERS_FIELD };
  /** Contains the array of fields to display in the form. */
  public static final String[] MAILING_LISTS_FIELDS       = new String[] {
      MailingListsFieldFactory.NAME_FIELD, MailingListsFieldFactory.DESCRIPTION_FIELD,
      MailingListsFieldFactory.SUBSCRIBERS_FIELD, MailingListsFieldFactory.OWNERS_FIELD };

  /**
   * Get ItemPropertyId used by {@link ComboBox}
   *
   * @return property id
   */
  public abstract String getPropertyId();
}
