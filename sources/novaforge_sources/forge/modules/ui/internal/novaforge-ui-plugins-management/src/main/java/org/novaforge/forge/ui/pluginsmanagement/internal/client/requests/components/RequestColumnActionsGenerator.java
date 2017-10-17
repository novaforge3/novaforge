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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.RequestsListPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class RequestColumnActionsGenerator implements ColumnGenerator
{

  /**
   * SerialUID
   */
  private static final long           serialVersionUID = 6365451958428976558L;
  private final RequestsListPresenter presenter;

  /**
   * Default constructor
   * 
   * @param pPresenter
   *          the presenter of the requests table view
   */
  public RequestColumnActionsGenerator(final RequestsListPresenter pPresenter)
  {
    super();
    presenter = pPresenter;

  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("serial")
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    final Locale locale = UI.getCurrent().getLocale();

    final Embedded associateRequest = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_INSTANCE_LINK));
    associateRequest.setWidth(NovaForge.ACTION_ICON_SIZE);
    associateRequest.setStyleName(NovaForge.BUTTON_IMAGE);
    associateRequest.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_REQUESTS_ACTIONS_LINK));
    associateRequest.addClickListener(new MouseEvents.ClickListener()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.showLinkRequestWindow((String) pItemId);
      }
    });

    final Embedded deleteRequest = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_TRASH_RED));
    deleteRequest.setWidth(NovaForge.ACTION_ICON_SIZE);
    deleteRequest.setStyleName(NovaForge.BUTTON_IMAGE);
    deleteRequest.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_REQUESTS_ACTIONS_DELETE));
    deleteRequest.addClickListener(new MouseEvents.ClickListener()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.showDeleteRequestWindow((String) pItemId);
      }
    });

    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.addComponent(associateRequest);
    actionsLayout.addComponent(deleteRequest);
    actionsLayout.setComponentAlignment(associateRequest, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(deleteRequest, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();
    return actionsLayout;
  }

}
