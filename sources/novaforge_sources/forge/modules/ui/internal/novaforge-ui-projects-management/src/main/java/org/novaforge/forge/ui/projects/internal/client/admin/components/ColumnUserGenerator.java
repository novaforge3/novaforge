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
package org.novaforge.forge.ui.projects.internal.client.admin.components;

import com.vaadin.data.Item;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import net.engio.mbassy.bus.MBassador;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectItemProperty;

/**
 * @author Guillaume Lamirand
 */
public class ColumnUserGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long            serialVersionUID = -2377060170281716413L;
  /**
   * the event bus used to send profile event
   */
  private final MBassador<PortalEvent> eventBus;

  /**
   * Default constructor
   * 
   * @param pEventBus
   *          the event bus used to send profile event
   */
  public ColumnUserGenerator(final MBassador<PortalEvent> pEventBus)
  {
    eventBus = pEventBus;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    final Button button = new Button();
    button.setStyleName(NovaForge.BUTTON_LINK);
    button.addStyleName(NovaForge.CAPITALIZE_TEXT);
    button.addStyleName(NovaForge.BUTTON_LARGE);
    final Item item = pSource.getItem(pItemId);
    final UserProfile userProfile = (UserProfile) item.getItemProperty(
        ProjectItemProperty.AUTHOR.getPropertyId()).getValue();
    if (userProfile != null)
    {
      button.setCaption(userProfile.getUser().getFirstName() + " " + userProfile.getUser().getName());
      Resource icon;
      if (userProfile.getImage() != null)
      {
        icon = ResourceUtils.buildImageResource(userProfile.getImage().getFile(), userProfile.getImage()
            .getName());
      }
      else
      {
        icon = new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW);
      }
      button.setIcon(icon);
      button.addClickListener(new ClickListener()
      {

        /**
         * Serial version id
         */
        private static final long serialVersionUID = 6659224424619360428L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void buttonClick(final ClickEvent pEvent)
        {
          eventBus.publish(new OpenUserProfileEvent(userProfile));

        }
      });
    }
    return button;
  }
}
