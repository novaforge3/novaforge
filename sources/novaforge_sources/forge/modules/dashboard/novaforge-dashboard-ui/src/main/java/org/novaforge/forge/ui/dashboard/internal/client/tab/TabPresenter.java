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
package org.novaforge.forge.ui.dashboard.internal.client.tab;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.dashboard.internal.client.AbstractDashBoardPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.event.CloseSettingsEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.ShowSettingsEvent;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.ContentPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.ContentViewImpl;
import org.novaforge.forge.ui.dashboard.internal.client.tab.header.HeaderPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.tab.header.HeaderViewImpl;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.SettingsPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.SettingsViewImpl;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class TabPresenter extends AbstractDashBoardPresenter implements Serializable
{
  /**
   * Serial version id
   */
  private static final long       serialVersionUID = -3067819403919493755L;

  private static final Log        LOGGER           = LogFactory.getLog(TabPresenter.class);
  private final TabView           view;
  private final HeaderPresenter   headerPresenter;
  private final ContentPresenter  contentPresenter;
  private final SettingsPresenter settingsPresenter;
  private final UUID              tabUUID;
  private boolean isHeaderShow = false;
  private boolean                 hasAdminRights;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          the portalContext used to initialize this module
   * @param pTabUUID
   *          the associated tab uuid
   * @param pView
   *          the view associated to this presenter
   * @param pContent
   *          the content of the tab
   */
  public TabPresenter(final PortalContext pPortalContext, final UUID pTabUUID, final TabView pView)
  {
    super(pPortalContext);
    tabUUID = pTabUUID;
    // Init the view
    view = pView;
    headerPresenter = new HeaderPresenter(pPortalContext, pTabUUID, new HeaderViewImpl());
    contentPresenter = new ContentPresenter(pPortalContext, pTabUUID, new ContentViewImpl());
    settingsPresenter = new SettingsPresenter(pPortalContext, pTabUUID, new SettingsViewImpl());
    view.initialize(headerPresenter.getComponent(), contentPresenter.getComponent());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterReferences()
  {
    super.unregisterReferences();
    headerPresenter.unregisterReferences();
    contentPresenter.unregisterReferences();
    settingsPresenter.unregisterReferences();
  }

  /**
   * Refreshes the header component
   */
  public void refreshTabHeader()
  {
    headerPresenter.refreshContent();
    headerPresenter.refreshLocalized(UI.getCurrent().getLocale());
  }

  /**
   * Refreshes the content component
   */
  public void refreshTabContent()
  {
    contentPresenter.refreshContent();
    contentPresenter.refreshLocalized(UI.getCurrent().getLocale());
  }

  /**
   * Shows or hides the header depending on the boolean given
   * 
   * @param pShow
   *          true to show it, false to hide
   */
  public void showHeader(final boolean pShow)
  {
    hasAdminRights = hasAdminRights();
    if (hasAdminRights)
    {
      isHeaderShow = pShow;
      headerPresenter.getComponent().setVisible(isHeaderShow);
      if (isHeaderShow)
      {
        contentPresenter.getComponent().addStyleName(NovaForge.DASHBOARD_SHOW_AREA);
      }
      else
      {
        contentPresenter.getComponent().removeStyleName(NovaForge.DASHBOARD_SHOW_AREA);
      }
    }
  }

  /**
   * Initialize header to a hidden state
   */
  public void initHiddenHeader()
  {
    isHeaderShow = false;
    headerPresenter.getComponent().setVisible(false);

  }

  /**
   * Method call when a {@link ShowSettingsEvent} is received
   *
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onShowSettingsEvent(final ShowSettingsEvent pEvent)
  {
    if ((tabUUID != null) && (tabUUID.equals(pEvent.getTabUUID())))
    {
      try
      {
        view.setContent(settingsPresenter.getComponent());
        settingsPresenter.refresh(pEvent.getWidgetUUID(), pEvent.getWidgetName(), pEvent.getIconResource());
      }
      catch (final IllegalAccessException e)
      {
        LOGGER.error("Unable to set new content to the tab view.", e);
      }
    }
  }

  /**
   * Method call when a {@link CloseSettingsEvent} is received
   *
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onCloseSettingsEvent(final CloseSettingsEvent pEvent)
  {
    if ((tabUUID != null) && (tabUUID.equals(pEvent.getTabUUID())))
    {
      try
      {
        view.setContent(contentPresenter.getComponent());
        contentPresenter.refreshContent();
      }
      catch (final IllegalAccessException e)
      {
        LOGGER.error("Unable to set new content to the tab view.", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    hasAdminRights = hasAdminRights();
    if ((isHeaderShown()) && hasAdminRights)
    {
      headerPresenter.refreshContent();
    }
    if (contentPresenter.getComponent().getParent() != null)
    {
      contentPresenter.refreshContent();
    }
    if (settingsPresenter.getComponent().getParent() != null)
    {
      settingsPresenter.refreshContent();
    }
  }

  /**
   * Returns visibility of the header component
   *
   * @return visibility of the header component
   */
  public boolean isHeaderShown()
  {
    return headerPresenter.getComponent().isVisible();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    headerPresenter.refreshLocalized(pLocale);
    contentPresenter.refreshLocalized(pLocale);

  }

}
