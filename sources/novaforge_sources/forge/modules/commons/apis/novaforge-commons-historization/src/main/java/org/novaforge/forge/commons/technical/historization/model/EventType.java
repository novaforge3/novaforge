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
package org.novaforge.forge.commons.technical.historization.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author sbenoist
 */
public enum EventType
{
  CREATE_PROJECT,

  VALIDATE_PROJECT,

  UPDATE_PROJECT,

  DELETE_PROJECT,

  CREATE_USER,

  UPDATE_USER,

  DELETE_USER,

  SEARCH_USER,

  GET_ALL_USERS,

  GET_ALL_USERPROFILES,

  RECOVER_PASSWORD,

  GET_ALL_ROLES,

  DELETE_ROLE,

  CREATE_ROLE,

  UPDATE_ROLE,

  CREATE_GROUP,

  DELETE_GROUP,

  UPDATE_GROUP,

  ADD_GROUP_MEMBERSHIP,

  ADD_USER_MEMBERSHIP,

  GET_ALL_MEMBERSHIPS,

  REMOVE_GROUP_MEMBERSHIP,

  REMOVE_USER_MEMBERSHIP,

  UPDATE_GROUP_MEMBERSHIP,

  UPDATE_USER_MEMBERSHIP,

  ADD_APPLICATION,

  REMOVE_APPLICATION,

  UPDATE_APPLICATION,

  UPDATE_APPLICATION_DATA,

  GET_ALL_APPLICATIONS,

  LOGIN,

  LOGOUT,

  CHANGE_PLUGIN_STATUS,

  PROPAGATE_REFERENCE_PROJECT_ALL_CHILD_FORGES,

  PROPAGATE_REFERENCE_TOOLS_ALL_CHILD_FORGES,

  PROPAGATE_TEMPLATES_ALL_CHILD_FORGES,

  GET_REFERENCE_PROJECT,

  UPDATE_REFERENCE_PROJECT,

  UPDATE_TEMPLATES,

  LAUNCH_EXTRACTION_ALL_CHILD_FORGES,

  LAUNCH_EXTRACTION,

  START_EXTRACTION,

  CONFIGURE_EXTRACTION,

  CONFIGURE_SCHEDULING,

  DISABLE_SCHEDULING,

  DISABLE_EXTRACTION,

  SEND_REFERENCE_TOOLS_REPORT,

  SYNCHRONIZATION_REQUIREMENTS_AND_SOURCES,

  SYNCHRONIZATION_REQUIREMENTS_AND_TESTS,

  SYNCHRONIZATION_EXTERNAL_REQUIREMENTS,

  PROPAGATION_DATA,

  PROPAGATION_FOR_USER,

  PROPAGATION_FOR_PROJECT,

  PROPAGATION_FOR_ROLES_MAPPING,

  PROPAGATION_FOR_MEMBERSHIP,

  MAILING;

  private static final Map<String, EventType> events = new HashMap<String, EventType>();

  static
  {
    for (final EventType type : values())
    {
      events.put(type.getLabel(), type);
    }
  }

  public static EventType fromLabel(final String pLabel)
  {
    return events.get(pLabel);
  }

  public String getLabel()
  {
    return getLabel(EventTypeResourceBundle.DEFAULT_LOCALE);
  }

  public String getLabel(final Locale pLocale)
  {
    final ResourceBundle resourceBundle = EventTypeResourceBundle.getBundle(pLocale);
    return resourceBundle.getString(toString() + "." + EventTypeResourceBundle.LABEL);
  }

  private static final class EventTypeResourceBundle
  {
    private static final Locale DEFAULT_LOCALE = new Locale("fr");

    private static final String PROPERTY_FILE  = "eventtype.type";

    private static final String LABEL          = "label";

    public static ResourceBundle getBundle(final Locale pLocale)
    {
      return ResourceBundle.getBundle(PROPERTY_FILE, pLocale);
    }

  }
}
