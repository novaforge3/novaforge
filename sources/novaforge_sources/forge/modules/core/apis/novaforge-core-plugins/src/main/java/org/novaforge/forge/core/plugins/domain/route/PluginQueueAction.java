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
package org.novaforge.forge.core.plugins.domain.route;

import java.util.HashMap;
import java.util.Map;

/**
 * This class describes three action headers which are used to send message to JMS queue.
 * 
 * @author Guillaume Lamirand
 */
public enum PluginQueueAction
{
  /**
   * Used on create event
   */
  CREATE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return "create";
    }
  },
  /**
   * Used on update event
   */
  UPDATE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return "update";
    }
  },
  /**
   * Used on delete event
   */
  DELETE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return "delete";
    }
  };

  private static final Map<String, PluginQueueAction> actions = new HashMap<String, PluginQueueAction>();

  static
  {
    for (final PluginQueueAction action : values())
    {
      actions.put(action.getLabel(), action);
    }
  }

  /**
   * Return the {@link PluginQueueAction} from its label
   *
   * @param pLabel
   *          the action's label
   * @return {@link PluginQueueAction} according its label
   */
  public static PluginQueueAction fromLabel(final String pLabel)
  {
    return actions.get(pLabel);
  }

  /**
   * Return the action label
   *
   * @return action's label as {@link String}
   */
  public abstract String getLabel();
}
