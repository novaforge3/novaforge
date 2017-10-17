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
package org.novaforge.forge.ui.unavailable.internal.client;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.unavailable.internal.module.UnavailableModule;

import java.util.Locale;

/**
 * This view define to display dashboard component
 * 
 * @author Guillaume Lamirand
 */
public class UnavailableViewImpl extends VerticalLayout implements UnavailableView
{

  /**
   * Serial version id used for serialization
   */
  private static final long serialVersionUID = -6571228909763527384L;
  /**
   * Contains the main information
   */
  private Label             title;
  /**
   * Contains the second label used as description
   */
  private Label             desc;

  /**
   * Default constructor
   * 
   * @param pSmall
   *          defines if view should be small or large
   */
  public UnavailableViewImpl(final boolean pSmall)
  {
    super();
    if (pSmall)
    {
      initSmall();
    }
    else
    {
      initLarge();
    }

  }

  /**
   * Will init this view with a small label
   */
  private void initSmall()
  {
    // Add text information
    desc = new Label();

    // Add all components to the layout
    addComponent(desc);
    setComponentAlignment(desc, Alignment.MIDDLE_CENTER);
  }

  /**
   * Will init this view with a label and a description associated
   */
  private void initLarge()
  {
    // Init vertical content
    setMargin(true);

    // Add text information
    title = new Label();
    title.setStyleName(Reindeer.LABEL_H1);
    desc = new Label();

    // Add all components to the layout
    addComponent(title);
    setComponentAlignment(title, Alignment.TOP_CENTER);
    addComponent(desc);
    setComponentAlignment(desc, Alignment.TOP_CENTER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    final PortalMessages portalMessages = UnavailableModule.getPortalMessages();
    if (title != null)
    {
      title.setValue(portalMessages.getMessage(pLocale, Messages.UNAVAILABLE_TITLE));
    }
    if (desc != null)
    {
      desc.setValue(portalMessages.getMessage(pLocale, Messages.UNAVAILABLE_DESCRIPTION));
    }
  }
}
