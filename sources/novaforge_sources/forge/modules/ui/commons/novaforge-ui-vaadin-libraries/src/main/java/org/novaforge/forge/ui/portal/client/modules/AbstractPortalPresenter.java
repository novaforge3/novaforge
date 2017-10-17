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
import com.vaadin.ui.UI;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.ui.portal.event.UIRefreshedEvent;
import org.novaforge.forge.ui.portal.i18n.LocaleChangedEvent;
import org.novaforge.forge.ui.portal.i18n.LocaleChangedListener;

import java.util.Locale;

/**
 * Default behaviour for presenter.
 * Should be used by each presenter in a {@link PortalModule}
 * 
 * @author Guillaume Lamirand
 */
public abstract class AbstractPortalPresenter extends AbstractPortalEventHandler implements
    LocaleChangedListener
{

  /**
   * The source locale used to init the presenter
   */
  private Locale currentLocale;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          .getEventBus()
   *          the eventbus
   */
  public AbstractPortalPresenter(final PortalContext pPortalContext)
  {
    super(pPortalContext);
    currentLocale = pPortalContext.getInitialLocale();
  }

  /**
   * Method call when a {@link UIRefreshedEvent} is received
   * 
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onUIRefreshedEvent(final UIRefreshedEvent pEvent)
  {
    if (getComponent().isAttached())
    {
      refreshContent();
    }
  }

  /**
   * Reeturn the {@link Component} attached to this presenter
   *
   * @return the {@link Component} manipulared by this presenter
   */
  public abstract Component getComponent();

  /**
   * Called to refresh content when {@link UI} has been refreshed
   */
  protected abstract void refreshContent();

  /**
   * /** {@inheritDoc}
   */
  @Override
  @Handler
  public void onLocaleChanged(final LocaleChangedEvent pEvent)
  {
    if (getComponent().isAttached())
    {
      refreshLocalized(pEvent.getLocale());
    }
  }

  /**
   * Called to refresh messsages with given locale
   *
   * @param locale
   *          new locae
   */
  protected abstract void refreshLocalized(final Locale locale);

  /**
   * Will return either UI Locale or view locale
   *
   * @return {@link Locale}
   */
  protected Locale getCurrentLocale()
  {
    if (UI.getCurrent() != null)
    {
      currentLocale = UI.getCurrent().getLocale();
    }
    else if ((getComponent() != null) && (getComponent().getLocale() != null))
    {
      currentLocale = getComponent().getLocale();
    }
    return currentLocale;

  }

}
