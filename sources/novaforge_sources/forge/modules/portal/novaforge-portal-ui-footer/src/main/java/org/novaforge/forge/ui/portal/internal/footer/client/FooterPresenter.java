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

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Component;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.internal.footer.module.FooterModule;

import java.io.Serializable;
import java.util.Locale;

/**
 * This presenter will handle the footer view.
 * 
 * @author Guillaume Lamirand
 */
public class FooterPresenter extends AbstractPortalPresenter implements Serializable, PortalComponent
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = 1185512328038526159L;
  /**
   * {@link FooterView} associated to this presenter
   */
  private final FooterView  view;

  /**
   * Default constructor.
   * 
   * @param pView
   *          represents the view to use
   * @param pPortalContext
   *          the initial context
   */
  public FooterPresenter(final FooterView pView, final PortalContext pPortalContext)
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
    init();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    refreshLocalized(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    final String footerWebSite = FooterModule.getForgeConfigurationService().getPortalFooterWebSite();
    if (!Strings.isNullOrEmpty(footerWebSite))
    {
      view.getBullLink().setResource(new ExternalResource(footerWebSite));
    }
    final String footer = FooterModule.getForgeConfigurationService().getPortalFooter();
    if (!Strings.isNullOrEmpty(footer))
    {
      view.getNovaForgeLabel().setValue(footer);
    }
    else
    {
      final PortalMessages portalMessages = FooterModule.getPortalMessages();
      view.getNovaForgeLabel().setCaption(portalMessages.getMessage(pLocale, Messages.FOOTER_NOVAFORGE));

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return FooterModule.getPortalModuleId();
  }

}
