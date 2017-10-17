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
package org.novaforge.forge.ui.portal.client.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * This class is used to define the main layout for the portal content.
 * 
 * @author Guillaume Lamirand
 */
public class PortalLayout extends VerticalLayout
{
  /**
   * Default serial version id
   */
  private static final long    serialVersionUID = -7158844150197589196L;
  /**
   * Contains the header view
   */
  private final CssLayout      header;
  /**
   * Contains the main content view
   */
  private final CssLayout      content;
  /**
   * Contains the footer view
   */
  private final VerticalLayout footer;

  /**
   * Default constructor. Only set full size
   */
  public PortalLayout()
  {
    super();

    // Init header
    header = new CssLayout();
    header.setHeight(55, Unit.PIXELS);
    header.setWidth(100, Unit.PERCENTAGE);
    header.setStyleName(NovaForge.PORTAL_HEADER);
    addComponent(header);
    setExpandRatio(header, 0);

    // Init content
    content = new CssLayout();
    content.setStyleName(NovaForge.PORTAL_CONTENT);
    content.setSizeFull();
    addComponent(content);
    setExpandRatio(content, 1);

    // Init footer
    footer = new VerticalLayout();
    footer.setWidth(100, Unit.PERCENTAGE);
    footer.setHeight(20, Unit.PIXELS);
    footer.setStyleName(NovaForge.PORTAL_FOOTER);
    addComponent(footer);
    setExpandRatio(footer, 0);

    setSizeFull();
  }

  /**
   * Clean and add a component as header element
   * 
   * @param pComponent
   *          the {@link Component} to add
   */
  public void addHeader(final Component pComponent)
  {
    header.removeAllComponents();
    header.addComponent(pComponent);
  }

  /**
   * Clean and add a component as content element
   * 
   * @param pComponent
   *          the {@link Component} to add
   */
  public void addContent(final Component pComponent)
  {
    content.removeAllComponents();
    content.addComponent(pComponent);
  }

  /**
   * Clean and add a component as footer element
   * 
   * @param pComponent
   *          the {@link Component} to add
   */
  public void addFooter(final Component pComponent)
  {
    footer.removeAllComponents();
    footer.addComponent(pComponent);
    footer.setComponentAlignment(pComponent, Alignment.MIDDLE_CENTER);
  }
}
