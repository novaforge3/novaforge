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
package org.novaforge.forge.ui.portal.internal.footer.client;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

/**
 * This view is used as footer element in the {@link PortalLayout}.
 * 
 * @author Guillaume Lamirand
 */
public class FooterViewImpl extends HorizontalLayout implements FooterView
{

  /**
   * Serial version id for serialization
   */
  private static final long   serialVersionUID = -6392072839075232867L;

  /**
   * Define the target window for a link component
   */
  private static final String TARGET_WINDOW    = "_blank";
  /**
   * Contains the version of NovaForge
   */
  private final Label         novaforgeLabel;

  /**
   * Contains a link to Bull website
   */
  private final Link          bullLink;

  /**
   * Default constructor.
   */
  public FooterViewImpl()
  {
    // Init NovaForge Label
    novaforgeLabel = new Label();
    // Link separator
    final Label sep = new Label(" | ");
    sep.setSizeUndefined();
    sep.setStyleName(Reindeer.LABEL_SMALL);
    bullLink = new Link();
    bullLink.setTargetName(TARGET_WINDOW);
    bullLink.setStyleName(NovaForge.BUTTON_IMAGE);
    bullLink.setIcon(new ThemeResource(NovaForgeResources.LOGO_COMPANY));

    // Add these components
    addComponent(novaforgeLabel);
    addComponent(sep);
    addComponent(bullLink);
    setSpacing(true);
    setComponentAlignment(novaforgeLabel, Alignment.MIDDLE_CENTER);
    setComponentAlignment(sep, Alignment.MIDDLE_CENTER);
    setComponentAlignment(bullLink, Alignment.MIDDLE_CENTER);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getNovaForgeLabel()
  {
    return novaforgeLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Link getBullLink()
  {
    return bullLink;
  }
}
