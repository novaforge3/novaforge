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
package org.novaforge.forge.ui.user.management.internal.client.admin.blacklist;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.user.management.internal.client.admin.UserManagementView;
import org.novaforge.forge.ui.user.management.internal.client.components.CustomUsersContainer;
import org.novaforge.forge.ui.user.management.internal.client.userpublicprofile.UserPublicProfilePresenter;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * @author caseryj
 */
public class BlackListUsersPresenter extends AbstractPortalPresenter implements Serializable
{

  /**
   * Default serial version UID
   */
  private static final long        serialVersionUID = -4185129710494779450L;
  /**
   * The view
   */
  private final BlackListUsersView view;
  /**
   * Log element
   */
  private final Log logger = LogFactory.getLog(UserPublicProfilePresenter.class);
  /**
   * The users container
   */
  private CustomUsersContainer usersContainer;
  /**
   * The users container filter
   */
  private Filter               usersFilter;

  /**
   * Default constructor
   *
   * @param pView
   *          the {@link UserManagementView} to associate
   * @param pPortalContext
   *          the initial context
   */
  public BlackListUsersPresenter(final BlackListUsersView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
    // Initialize users list
    initUsersList();
  }

  /**
   * Add listener to the view elements
   */
  private void addListeners()
  {
    view.getFilterTextField().addTextChangeListener(new TextChangeListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 8606396016268448812L;

      @Override
      public void textChange(final TextChangeEvent event)
      {
        final Filterable f = usersContainer;

        // Remove old filter
        if (usersFilter != null)
        {
          f.removeContainerFilter(usersFilter);
        }
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          usersFilter = new Or(new SimpleStringFilter(UserItemProperty.LOGIN.getPropertyId(), event.getText(), true,
                                                      false), new SimpleStringFilter(UserItemProperty.EMAIL
                                                                                         .getPropertyId(),
                                                                                     event.getText(), true, false));
          f.addContainerFilter(usersFilter);
        }
      }
    });
  }

  /**
   * Initialize users list
   */
  private void initUsersList()
  {
    usersContainer = new CustomUsersContainer();
    view.getUsersTable().setContainerDataSource(usersContainer);

    // Define visibles columns
    view.getUsersTable().setVisibleColumns(UserItemProperty.LOGIN.getPropertyId(),
                                           UserItemProperty.EMAIL.getPropertyId(),
                                           UserItemProperty.CREATED_DATE.getPropertyId());
  }

  /**
   * Will refresh users information
   */
  public void refresh()
  {
    view.getFilterTextField().setValue("");
    usersContainer.removeAllContainerFilters();
    refreshContent();
    refreshLocalized(getCurrentLocale());
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
    try
    {
      final List<BlacklistedUser> users = AdminModule.getUserPresenter().getAllBlacklistedUsers();
      usersContainer.setBlacklistedUsers(users);
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, view.getLocale());
    }

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
