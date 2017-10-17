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
package org.novaforge.commons.route.util;

import org.apache.camel.Message;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;

/**
 * @author sbenoist
 */
public final class RouteUtil
{
  /**
   * This is the queue used for errors
   */
  public final static String FORGE_ERRORS_QUEUE                      = "jms:queue:forgeErrorsQueue";

  /**
   * These are the queues used for messaging from plugins to core
   */
  public final static String FORGE_CALLBACK_DATA_QUEUE               = "jms:queue:forgeCallbackDataQueue";

  public final static String FORGE_CALLBACK_PROJECT_QUEUE            = "jms:queue:forgeCallbackProjectQueue";

  public final static String FORGE_CALLBACK_ROLES_MAPPING_QUEUE      = "jms:queue:forgeCallbackRolesMappingQueue";

  public final static String FORGE_CALLBACK_USER_QUEUE               = "jms:queue:forgeCallbackUserQueue";

  public final static String FORGE_CALLBACK_MEMBERSHIP_QUEUE         = "jms:queue:forgeCallbackMembershipQueue";

  public final static String FORGE_CALLBACK_GROUP_QUEUE              = "jms:queue:forgeCallbackGroupQueue";
  /**
   * These are the queues used for messaging from core to plugins
   */
  public final static String FORGE_USER_QUEUE                        = "jms:queue:forgeUserQueue";

  public final static String FORGE_ROLES_MAPPING_QUEUE               = "jms:queue:forgeRolesMappingQueue";

  public final static String FORGE_PROJECT_QUEUE                     = "jms:queue:forgeProjectQueue";

  public final static String FORGE_MEMBERSHIP_QUEUE                  = "jms:queue:forgeMembershipQueue";

  public final static String FORGE_COMPOSITION_QUEUE                 = "jms:queue:forgeCompositionQueue";

  public final static String FORGE_GROUP_QUEUE                       = "jms:queue:forgeGroupQueue";
  /**
   * These are the routes's name
   */
  public static final String FORGE_MEMBERSHIP_ROUTE_NAME             = "pluginMembershipRoute";

  public static final String FORGE_PROJECT_ROUTE_NAME                = "pluginProjectRoute";

  public static final String FORGE_USER_ROUTE_NAME                   = "pluginUserRoute";

  public static final String FORGE_GROUP_ROUTE_NAME                  = "pluginGroupRoute";

  public static final String FORGE_COMPOSITION_ROUTE_NAME            = "pluginCompositionRoute";

  public static final String FORGE_ROLES_MAPPING_ROUTE_NAME          = "pluginRolesMappingRoute";

  public static final String FORGE_CALLBACK_MEMBERSHIP_ROUTE_NAME    = "callbackPluginMembershipRoute";

  public static final String FORGE_CALLBACK_PROJECT_ROUTE_NAME       = "callbackPluginProjectRoute";

  public static final String FORGE_CALLBACK_USER_ROUTE_NAME          = "callbackPluginUserRoute";

  public static final String FORGE_CALLBACK_DATA_ROUTE_NAME          = "callbackPluginDataRoute";

  public static final String FORGE_CALLBACK_ROLES_MAPPING_ROUTE_NAME = "callbackPluginRolesMappingRoute";

  public static final String FORGE_CALLBACK_GROUP_ROUTE_NAME         = "callbackPluginGroupRoute";

  public static String formatInputMessage(final Message message)
  {
    final StringBuilder sb = new StringBuilder();
    final String messageID = (String) message.getHeader("JMSMessageID");
    final String action = (String) message.getHeader(PluginQueueHeader.ACTION_HEADER);
    final String pluginUUID = (String) message.getHeader(PluginQueueHeader.PLUGIN_UUID_HEADER);

    sb.append("Core - Get a message ID=").append(messageID);
    sb.append(" - Action = ").append(action);
    sb.append(" - Plugin = ").append(pluginUUID);
    return sb.toString();
  }
}
