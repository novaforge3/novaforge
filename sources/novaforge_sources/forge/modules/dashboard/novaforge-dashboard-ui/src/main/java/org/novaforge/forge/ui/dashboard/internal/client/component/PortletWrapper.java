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
package org.novaforge.forge.ui.dashboard.internal.client.component;

import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * This object wraps a component into portlet design
 * 
 * @author Guillaume Lamirand
 */
public class PortletWrapper extends VerticalLayout
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 7297890272656507757L;
  /**
   * Contains the component wrappred
   */
  private final CssLayout   content;
  /**
   * The {@link Label} used to display caption text
   */
  private Label             captionTxt;
  /**
   * The {@link Image} used to display caption icon
   */
  private Image             captionIcon;
  /**
   * The {@link Button} used as back button
   */
  private Button            back;

  /**
   * Default constructor
   */
  public PortletWrapper()
  {
    super();

    // Init portlet slot
    setMargin(true);
    setSizeFull();
    final CssLayout portletSlot = new CssLayout();
    portletSlot.setStyleName(NovaForge.DASHBOARD_PORTLET_WRAP_SLOT);
    portletSlot.setSizeFull();

    final VerticalLayout portlet = new VerticalLayout();
    portlet.setSizeFull();
    portlet.setStyleName(NovaForge.DASHBOARD_PORTLET_WRAP);

    // Init portlet header
    final Component header = initHeader();

    // Init portlet content
    content = new CssLayout();
    content.setStyleName(NovaForge.DASHBOARD_PORTLET_WRAP_CONTENT);

    // Add components
    portlet.addComponent(header);
    portlet.setExpandRatio(header, 0);
    portlet.addComponent(content);
    portlet.setExpandRatio(content, 1);

    portletSlot.addComponent(portlet);
    addComponent(portletSlot);

  }

  private Component initHeader()
  {
    // Init the header layout
    final HorizontalLayout header = new HorizontalLayout();
    header.setStyleName(NovaForge.DASHBOARD_PORTLET_WRAP_HEADER);
    header.setWidth(100, Unit.PERCENTAGE);
    header.setHeight(41, Unit.PIXELS);

    // Init the caption side
    final HorizontalLayout caption = new HorizontalLayout();
    caption.setStyleName(NovaForge.DASHBOARD_PORTLET_WRAP_HEADER_CAPTION);
    captionTxt = new Label();
    captionTxt.setStyleName(NovaForge.DASHBOARD_PORTLET_WRAP_HEADER_CAPTIONTEXT);
    captionIcon = new Image();
    captionIcon.setStyleName(NovaForge.DASHBOARD_PORTLET_WRAP_HEADER_CAPTIONICON);
    caption.addComponent(captionIcon);
    caption.addComponent(captionTxt);

    // Init the button side
    back = new NativeButton();
    back.setStyleName(NovaForge.DASHBOARD_PORTLET_WRAP_HEADER_BUTTON);

    // Add components
    header.addComponent(caption);
    header.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
    header.addComponent(back);
    header.setComponentAlignment(back, Alignment.MIDDLE_RIGHT);

    return header;
  }

  /**
   * Wraps the given component
   * 
   * @param pComponent
   *          the component to wrap
   */
  public void wrap(final Component pComponent)
  {
    content.removeAllComponents();
    content.addComponent(pComponent);
  }

  /**
   * Add listener on back button
   * 
   * @param listener
   *          the listener to add
   */
  public void addBackClickListener(final ClickListener listener)
  {
    back.addClickListener(listener);
  }

  /**
   * Sets the caption text of portlet
   * 
   * @param pCaption
   *          the text to set
   */
  public void setHeaderCaption(final String pCaption)
  {
    captionTxt.setValue(pCaption);
  }

  /**
   * Sets the caption icon of portlet
   * 
   * @param pSource
   *          the icon to set
   */
  public void setHeaderIcon(final Resource pSource)
  {
    captionIcon.setSource(pSource);
  }
}
