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
package org.novaforge.forge.ui.user.management.internal.client.global;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.user.management.internal.client.admin.UserManagementPresenter;
import org.novaforge.forge.ui.user.management.internal.client.admin.UserManagementViewImpl;
import org.novaforge.forge.ui.user.management.internal.client.admin.blacklist.BlackListUsersPresenter;
import org.novaforge.forge.ui.user.management.internal.client.admin.blacklist.BlackListUsersViewImpl;
import org.novaforge.forge.ui.user.management.internal.client.event.OpenEditUserProfileEvent;
import org.novaforge.forge.ui.user.management.internal.client.event.OpenUserAdminEvent;
import org.novaforge.forge.ui.user.management.internal.client.event.OpenUserPublicProfileEvent;
import org.novaforge.forge.ui.user.management.internal.client.securityrules.SecurityRulesPresenter;
import org.novaforge.forge.ui.user.management.internal.client.securityrules.SecurityRulesViewImpl;
import org.novaforge.forge.ui.user.management.internal.client.userprofile.UserProfilePresenter;
import org.novaforge.forge.ui.user.management.internal.client.userprofile.UserProfileViewImpl;
import org.novaforge.forge.ui.user.management.internal.client.userpublicprofile.UserPublicProfilePresenter;
import org.novaforge.forge.ui.user.management.internal.client.userpublicprofile.UserPublicProfileViewImpl;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.io.Serializable;
import java.util.Locale;

/**
 * This presenter handles main view.
 * 
 * @author Guillaume Lamirand
 */
public class GlobalPresenter extends AbstractPortalPresenter implements Serializable
{
  /**
   * Default serial version UID
   */
  private static final long                serialVersionUID = 3111732866599804993L;

  /**
   * Content of project view
   */
  private final GlobalView                 view;
  /**
   * The admin presenter
   */
  private final UserManagementPresenter    adminPresenter;
  /**
   * The blacklist presenter
   */
  private final BlackListUsersPresenter    blackListPresenter;
  /**
   * The user profile presenter
   */
  private final UserProfilePresenter       editProfilePresenter;
  /**
   * The user public profile presenter
   */
  private final UserPublicProfilePresenter userProfilePresenter;
  /**
   * The security rules presenter
   */
  private final SecurityRulesPresenter     securityPresenter;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context
   */
  public GlobalPresenter(final GlobalView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    adminPresenter = new UserManagementPresenter(new UserManagementViewImpl(), pPortalContext);
    blackListPresenter = new BlackListUsersPresenter(new BlackListUsersViewImpl(), pPortalContext);
    userProfilePresenter = new UserPublicProfilePresenter(new UserPublicProfileViewImpl(), pPortalContext,
        true);
    editProfilePresenter = new UserProfilePresenter(new UserProfileViewImpl(), pPortalContext, true);
    securityPresenter = new SecurityRulesPresenter(new SecurityRulesViewImpl(), pPortalContext);

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getAdminUsers().addClickListener(new ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 264010147793126838L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(adminPresenter.getComponent());
        adminPresenter.refresh();
      }
    });
    view.getBlackListUsers().addClickListener(new ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 8574986302598484202L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(blackListPresenter.getComponent());
        blackListPresenter.refresh();
      }
    });
    view.getManageSecurityRules().addClickListener(new ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 7424587579529493535L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(securityPresenter.getComponent());
        securityPresenter.refresh();
      }
    });
  }

  /**
   * Will refresh the projects list
   */
  public void refresh()
  {
    view.getAdminUsers().addStyleName(NovaForge.SELECTED);
    view.setSecondComponent(adminPresenter.getComponent());
    adminPresenter.refresh();
  }

  /**
   * Callback on {@link OpenUserAdminEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showAdminUsersViewEvent(final OpenUserAdminEvent pEvent)
  {
    view.setSecondComponent(adminPresenter.getComponent());
    adminPresenter.refresh();
  }

  /**
   * Callback on {@link OpenUserPublicProfileEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showUserPublicProfileEvent(final OpenUserPublicProfileEvent pEvent)
  {
    view.setSecondComponent(userProfilePresenter.getComponent());
    userProfilePresenter.refresh(pEvent.getUserLogin());
  }

  /**
   * Callback on {@link OpenEditUserProfileEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showEditUserProfileEvent(final OpenEditUserProfileEvent pEvent)
  {
    view.setSecondComponent(editProfilePresenter.getComponent());
    editProfilePresenter.refresh(pEvent.getUserLogin());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return AdminModule.getPortalModuleId();
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
    // Handle by each sub presenter
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
