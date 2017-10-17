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

import com.vaadin.ui.Component;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.unavailable.internal.module.UnavailableModule;

import java.io.Serializable;
import java.util.Locale;

/**
 * Presenter used to manage dashboard component.
 * 
 * @author Guillaume Lamirand
 */
public class UnavailablePresenter extends AbstractPortalPresenter implements Serializable, PortalComponent
{
  /**
   * Serial version id used for serialization
   */
  private static final long     serialVersionUID = 1158368564534063818L;

  /**
   * View associated to this presenter
   */
  private final UnavailableView view;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          the portalContext used to initialize this module
   * @param pView
   *          the view associated to this presenter
   */
  public UnavailablePresenter(final PortalContext pPortalContext, final UnavailableView pView)
  {
    super(pPortalContext);
    // Init the view
    view = pView;

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
    view.refreshLocale(view.getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    refreshContent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return UnavailableModule.getPortalModuleId();
  }

}
