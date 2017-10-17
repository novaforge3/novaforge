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
package org.novaforge.forge.ui.memberships.internal.client.users.components;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.UsersContainer;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * This component should be used to display a filter and table for selecting user. All selecting user are
 * displyed on the right of table.
 * 
 * @author Guillaume Lamirand
 */
public class UserTableSelectable extends VerticalLayout
{
  /**
   * Serial version id
   */
  private static final long   serialVersionUID = -1444548144654821860L;
  /**
   * Id for filter field
   */
  private static final String FIELD_FILTER     = "filter";
  /**
   * Contains the {@link TextField} used to filter table
   */
  private TextField           filterTextField;
  /**
   * Contains the {@link Button} used to clean table selection
   */
  private Button              clearButton;
  /**
   * Contains the {@link Form} used to format filter {@link TextField} and {@link Button}
   */
  private Form                filterForm;
  /**
   * Contains the parent layout of user tables
   */
  private CssLayout           usersTableLayout;
  /**
   * Contains {@link Table} which will displayed {@link User}
   */
  private Table               usersTable;
  /**
   * Represents {@link UsersContainer} used to store {@link User}
   */
  private UsersContainer      usersContainer;
  /**
   * Will contains the selected user
   */
  private Set<UUID>           selectedUsers    = new HashSet<UUID>();
  private VerticalLayout      selectedLayout;

  /**
   * Default constructor.
   */
  public UserTableSelectable()
  {
    // Init contents
    final Component filter = initFilter();
    final Component content = initContent();
    addComponent(filter);
    addComponent(content);

    // Bind listener
    initUsersList();
    addListeners();
  }

  private Component initFilter()
  {
    filterForm = new Form();
    filterForm.setImmediate(true);
    filterForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    clearButton = new Button();
    clearButton.setStyleName(NovaForge.BUTTON_LINK);
    filterForm.addField(FIELD_FILTER, filterTextField);
    filterForm.getFooter().addComponent(clearButton);
    return filterForm;
  }

  private Component initContent()
  {
    final HorizontalLayout layout = new HorizontalLayout();
    layout.setSpacing(true);
    layout.setWidth(100, Unit.PERCENTAGE);
    layout.setMargin(new MarginInfo(true, false, false, false));
    final Component tableContent    = initTable();
    final Component selectedContent = initSelected();
    layout.addComponent(tableContent);
    layout.addComponent(selectedContent);
    layout.setExpandRatio(tableContent, 0.8f);
    layout.setExpandRatio(selectedContent, 0.2f);
    return layout;
  }

  private void initUsersList()
  {
    usersContainer = new UsersContainer();
    usersTable.setContainerDataSource(usersContainer);

    usersTable.addGeneratedColumn(UserItemProperty.SELECT.getPropertyId(), new UserColumnSelectGenerator());
    // Define visibles columns
    usersTable.setVisibleColumns(UserItemProperty.SELECT.getPropertyId(),
        UserItemProperty.LOGIN.getPropertyId(), UserItemProperty.FIRSTNAME.getPropertyId(),
        UserItemProperty.LASTNAME.getPropertyId(), UserItemProperty.EMAIL.getPropertyId());
    // Define special column width
    usersTable.setColumnExpandRatio(UserItemProperty.LOGIN.getPropertyId(), 0);
    usersTable.setColumnExpandRatio(UserItemProperty.FIRSTNAME.getPropertyId(), 0.1f);
    usersTable.setColumnExpandRatio(UserItemProperty.LASTNAME.getPropertyId(), 0.1f);
    usersTable.setColumnExpandRatio(UserItemProperty.EMAIL.getPropertyId(), 0.3f);
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    usersTable.addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 6798250830949164100L;

      /**
       * {@inheritDoc}
       */
      @SuppressWarnings("unchecked")
      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        selectedUsers = new HashSet<UUID>((Set<UUID>) usersTable.getValue());
        selectedLayout.removeAllComponents();
        for (final UUID uuid : selectedUsers)
        {
          final String login = usersContainer.getLogin(uuid);
          if (login != null)
          {
            final Label label = new Label(login);
            selectedLayout.addComponent(label);
          }
        }
      }
    });
    filterTextField.addTextChangeListener(new TextChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -1728083457370048943L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void textChange(final TextChangeEvent event)
      {
        // Clean filter
        usersContainer.removeAllContainerFilters();
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter usersFilter = new Or(new SimpleStringFilter(UserItemProperty.LOGIN.getPropertyId(),
              event.getText(), true, false), new SimpleStringFilter(UserItemProperty.FIRSTNAME
              .getPropertyId(), event.getText(), true, false), new SimpleStringFilter(
              UserItemProperty.LASTNAME.getPropertyId(), event.getText(), true, false),
              new SimpleStringFilter(UserItemProperty.EMAIL.getPropertyId(), event.getText(), true, false));
          usersContainer.addContainerFilter(usersFilter);
        }
      }
    });
    clearButton.addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 1908099716804282434L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        attachUsersTable(false);
        for (final Object itemId : usersContainer.getItemIds())
        {
          if (usersTable.isSelected(itemId))
          {
            usersTable.unselect(itemId);
          }
        }
        attachUsersTable(true);

      }
    });

  }

  private Component initTable()
  {
    usersTableLayout = new CssLayout();
    usersTableLayout.setWidth(100, Unit.PERCENTAGE);
    usersTable = new Table();
    usersTable.setSelectable(false);
    usersTable.setMultiSelect(true);
    usersTable.setPageLength(10);
    usersTable.setWidth(100, Unit.PERCENTAGE);
    usersTable.setStyleName(Reindeer.TABLE_STRONG);
    usersTableLayout.addComponent(usersTable);
    return usersTableLayout;
  }

  /**
   * @return
   */
  private Component initSelected()
  {
    selectedLayout = new VerticalLayout();
    selectedLayout.setMargin(new MarginInfo(false, false, false, true));
    return selectedLayout;
  }

  /**
   * Attach or detach table to its parent
   *
   * @param pAttach
   *          true to attache, false to detach
   */
  public void attachUsersTable(final boolean pAttach)
  {
    if (pAttach)
    {
      usersTable.refreshRowCache();
      usersTableLayout.addComponent(usersTable);
    }
    else
    {
      usersTableLayout.removeAllComponents();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());

  }

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  public void refreshLocale(final Locale pLocale)
  {
    filterForm.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_LIST_TITLE));
    filterForm.setDescription(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_LIST_DESCRIPTION));
    filterTextField.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_FILTER));
    clearButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_LIST_CANCEL));
    usersTable.setColumnHeader(UserItemProperty.LOGIN.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_USERS_LOGIN));
    usersTable.setColumnHeader(UserItemProperty.FIRSTNAME.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_USERS_FIRSTNAME));
    usersTable.setColumnHeader(UserItemProperty.LASTNAME.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_USERS_NAME));
    usersTable.setColumnHeader(UserItemProperty.EMAIL.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_USERS_EMAIL));
    selectedLayout.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_LIST_SELECTED));
  }

  /**
   * Should be called to clear container and field
   */
  public void clear()
  {
    selectedUsers = new HashSet<UUID>();
    filterTextField.setValue("");
    usersContainer.removeAllContainerFilters();
    selectedLayout.removeAllComponents();
    for (final Object itemId : usersContainer.getItemIds())
    {
      if (usersTable.isSelected(itemId))
      {
        usersTable.unselect(itemId);
      }
    }

  }

  /**
   * Return {@link Set} of user login selecting
   * 
   * @return {@link Set} of user login selecting
   */
  public Set<UUID> getSelectedUser()
  {
    return selectedUsers;
  }

  /**
   * Return {@link UsersContainer} used to store {@link User}
   * 
   * @return {@link UsersContainer} used to store {@link User}
   */
  public UsersContainer getUsersContainer()
  {
    return usersContainer;
  }

  /**
   * Return {@link TextField} used to filter user's table
   * 
   * @return {@link TextField} used to filter user's table
   */
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /**
   * Return {@link Button} used to clear table selection
   * 
   * @return {@link Button} used to clear table selection
   */
  public Button getClearButton()
  {
    return clearButton;
  }

  /**
   * Return {@link Table} containing users
   * 
   * @return {@link Table} containing users
   */
  public Table getUsersTable()
  {
    return usersTable;
  }

}
