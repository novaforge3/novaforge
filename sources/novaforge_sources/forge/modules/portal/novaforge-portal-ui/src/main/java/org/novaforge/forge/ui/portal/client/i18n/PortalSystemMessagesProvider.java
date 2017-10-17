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
package org.novaforge.forge.ui.portal.client.i18n;

import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;

/**
 * This provider will retrieve system message from properties service
 * 
 * @author Guillaume Lamirand
 */
public class PortalSystemMessagesProvider implements SystemMessagesProvider
{

  /**
   * Serial version id
   */
  private static final long                         serialVersionUID = 2531815889881719011L;
  /**
   * Default instance
   */
  private static final PortalSystemMessagesProvider instance         = new PortalSystemMessagesProvider();

  /**
   * This is private because of singleton pattern
   */
  private PortalSystemMessagesProvider()
  {
    // Singleton
  }

  /**
   * Get the instance.
   *
   * @return the default system messages provider.
   */
  public static SystemMessagesProvider get()
  {
    return instance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SystemMessages getSystemMessages(final SystemMessagesInfo pSystemMessagesInfo)
  {
    return new LocalizedSystemMessages(pSystemMessagesInfo);
  }

}
