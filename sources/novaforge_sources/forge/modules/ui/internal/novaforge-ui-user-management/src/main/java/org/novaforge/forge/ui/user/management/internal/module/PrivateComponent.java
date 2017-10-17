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
package org.novaforge.forge.ui.user.management.internal.module;

import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalComponent;
import org.novaforge.forge.ui.user.management.internal.client.userprofile.UserProfilePresenter;
import org.novaforge.forge.ui.user.management.internal.client.userprofile.UserProfileView;
import org.novaforge.forge.ui.user.management.internal.client.userprofile.UserProfileViewImpl;

/**
 * @author caseryj
 */
public class PrivateComponent extends AbstractPortalComponent
{

  /**
   * The user profile presenter
   */
  private final UserProfilePresenter userProfilePresenter;
  /**
   * Tue current user login
   */
  private final String               userLogin;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          {@link PortalContext}, the portal context
   */
  public PrivateComponent(final PortalContext pPortalContext)
  {
    super(pPortalContext);

    userLogin = PrivateModule.getAuthentificationService().getCurrentUser();

    final UserProfileView view = new UserProfileViewImpl();
    view.addStyleName(NovaForge.NOVAFORGE_APPLICATION_CONTENT);
    userProfilePresenter = new UserProfilePresenter(view, pPortalContext);
    setContent(view);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    userProfilePresenter.refresh(userLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return PrivateModule.getPortalModuleId();
  }

}
