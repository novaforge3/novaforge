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
package org.novaforge.forge.ui.portal.client.modules;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;

/**
 * This class defines default behaviour for a {@link PortalComponent}
 * 
 * @author Guillaume Lamirand
 */
public abstract class AbstractPortalComponent extends AbstractPortalEventHandler implements PortalComponent
{

  /**
   * The {@link Panel} used as content
   */
  private final Panel content;

  /**
   * @param pPortalContext
   *          the initial context
   */
  public AbstractPortalComponent(final PortalContext pPortalContext)
  {
    super(pPortalContext);
    content = new Panel();
    content.setSizeFull();
    content.setStyleName(Reindeer.PANEL_LIGHT);

  }

  /**
   * Add the given component as main content
   * 
   * @param pContent
   *          the component to add
   */
  public void setContent(final Component pContent)
  {
    content.setContent(pContent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return content;
  }

}
