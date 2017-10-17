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
package org.novaforge.forge.ui.portal.servlet.jmetter;

import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;

/**
 * @author Guillaume Lamirand
 */
public class JMeterSession extends VaadinSession
{
  private static final long serialVersionUID = 4596901275146146127L;

  public JMeterSession(final VaadinService service)
  {
    super(service);
  }

  @Override
  public String createConnectorId(final ClientConnector connector)
  {
    if (connector instanceof Component)
    {
      final Component component = (Component) connector;
      return component.getId() == null ? super.createConnectorId(connector) : component.getId();
    }
    return super.createConnectorId(connector);
  }
}
