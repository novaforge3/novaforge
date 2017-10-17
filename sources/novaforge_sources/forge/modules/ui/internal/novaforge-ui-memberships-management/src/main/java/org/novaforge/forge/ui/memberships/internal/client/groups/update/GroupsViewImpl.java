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
package org.novaforge.forge.ui.memberships.internal.client.groups.update;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.GroupItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.groups.components.GroupFieldFactory;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RolesHandlerComponent;
import org.novaforge.forge.ui.memberships.internal.client.users.components.UserTableSelectable;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;

import java.util.Collection;
import java.util.Locale;

/**
 * This view is used to display groups table
 * 
 * @author Guillaume Lamirand
 */
public class GroupsViewImpl extends VerticalLayout implements GroupsView
{
  /**
   * Serialization id
   */
  private static final long     serialVersionUID = -4420222948069676402L;

  /**
   * {@link Field} id
   */
  private static final String   FILTER           = "filter";
  /**
   * The {@link Button} to create a new group
   */
  private Button                createButton;
  /**
   * The {@link TextField} used to filter {@link GroupsViewImpl#groupsTable}
   */
  private TextField             filterTextField;
  /**
   * The {@link Form} containing the {@link GroupsViewImpl#groupsTable}
   */
  private Form                  groupsForm;
  /**
   * The {@link Table} which will display group list
   */
  private Table                 groupsTable;
  /**
   * {@link Window} for editing group details
   */
  private Window                editGroupWindow;
  /**
   * {@link Form} containing group detail field
   */
  private Form                  editGroupForm;
  /**
   * {@link Button} to cancel edit group
   */
  private Button                editGroupCancelButton;
  /**
   * {@link Button} to confirm edit group
   */
  private Button                editGroupConfirmButton;
  private Window                editUsersWindow;
  private Button                editUsersCancelButton;
  private Button                editUsersConfirmButton;
  private UserTableSelectable   userTableSelectable;
  private Window                deleteGroupWindow;
  private Label                 deleteGroupConfirmLabel;
  private CheckBox              deleteGroupNotifCheck;
  private Button                deleteGroupCancelButton;
  private Button                deleteGroupConfirmButton;
  private Window                editRolesWindow;
  private RolesHandlerComponent editRolesComponent;

  private Window                viewUsersWindow;
  private Button                viewUserCloseButton;
  private Table                 viewUsersTable;
  private Label                 viewUsersFilterLabel;
  private TextField             viewUsersFiltersText;

  private CssLayout             groupsTableLayout;

  private GroupFieldFactory     groupFieldFactory;

  /**
   * Default constructor.
   */
  public GroupsViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component headers = initHeaders();
    final Component filter = initFilter();
    final Component content = initContent();
    addComponent(headers);
    addComponent(filter);
    addComponent(content);

    // Init subwindows
    initEditGroupWindow();
    initEditUsersWindow();
    initViewUsersWindow();
    initEditRolesWindow();
    initDeleteGroupWindow();
  }

  /**
   * Init the headers component
   * 
   * @return {@link Component} created
   */
  private Component initHeaders()
  {
    /*
    The header layout
   */
    final HorizontalLayout headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    createButton = new Button();
    createButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));

    headerButtons.addComponent(createButton);
    return headerButtons;
  }

  /**
   * Init the filter form
   * 
   * @return {@link Component} created
   */
  private Component initFilter()
  {
    groupsForm = new Form();
    groupsForm.setImmediate(true);
    groupsForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    groupsForm.addField(FILTER, filterTextField);
    return groupsForm;
  }

  /**
   * Init the main content
   * 
   * @return {@link Component} created
   */
  private Component initContent()
  {
    groupsTableLayout = new CssLayout();
    groupsTableLayout.setWidth(100, Unit.PERCENTAGE);
    groupsTable = new Table();
    groupsTable.setSelectable(true);
    groupsTable.setPageLength(10);
    groupsTable.setWidth(100, Unit.PERCENTAGE);
    groupsTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3559455760887782118L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {

        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if ((child != null) && (!child.equals(groupsTable)) && (!child.equals(groupsTableLayout)))
        {
          final Collection<?> itemIds = groupsTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (groupsTable.isSelected(itemId))
            {
              groupsTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    groupsTableLayout.addComponent(groupsTable);
    return groupsTableLayout;
  }

  /**
   * Init the window used to edit group details
   */
  private void initEditGroupWindow()
  {
    editGroupWindow = new Window();
    editGroupWindow.setModal(true);
    editGroupWindow.setResizable(false);
    editGroupWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_GROUP_EDIT));

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(new MarginInfo(false, true, true, true));
    windowLayout.setSpacing(true);
    windowLayout.setWidth(320, Unit.PIXELS);

    editGroupForm = new Form();
    editGroupForm.setSizeUndefined();
    groupFieldFactory = new GroupFieldFactory();
    editGroupForm.setFormFieldFactory(groupFieldFactory);
    editGroupForm.setFooter(null);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    // buttons.setWidth(100, Unit.PERCENTAGE);
    editGroupConfirmButton = new Button();
    editGroupCancelButton = new Button();
    editGroupCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    editGroupConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    editGroupConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(editGroupCancelButton);
    buttons.addComponent(editGroupConfirmButton);
    // Set window content
    windowLayout.addComponent(editGroupForm);
    windowLayout.setComponentAlignment(editGroupForm, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    editGroupWindow.setContent(windowLayout);

  }

  /**
   * Init the window used to edit group users
   */
  private void initEditUsersWindow()
  {
    editUsersWindow = new Window();
    editUsersWindow.setModal(true);
    editUsersWindow.setResizable(false);
    editUsersWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_GROUP_USER));

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setWidth(935, Unit.PIXELS);
    windowLayout.setMargin(new MarginInfo(false, true, true, true));
    windowLayout.setSpacing(true);

    userTableSelectable = new UserTableSelectable();
    userTableSelectable.setMargin(new MarginInfo(true, false, true, false));
    userTableSelectable.getUsersTable().setPageLength(5);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    editUsersConfirmButton = new Button();
    editUsersCancelButton = new Button();
    editUsersCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    editUsersConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    editUsersConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(editUsersCancelButton);
    buttons.addComponent(editUsersConfirmButton);
    // Set window content
    windowLayout.addComponent(userTableSelectable);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(userTableSelectable, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    editUsersWindow.setContent(windowLayout);
  }

  /**
   * Init the window used to edit group users
   */
  private void initViewUsersWindow()
  {
    viewUsersWindow = new Window();
    viewUsersWindow.setModal(true);
    viewUsersWindow.setResizable(false);
    viewUsersWindow.setWidth(950, Unit.PIXELS);
    viewUsersWindow.setHeight(450, Unit.PIXELS);

    viewUsersWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_GROUP_USER));

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(new MarginInfo(false, true, true, true));
    windowLayout.setSpacing(true);
    windowLayout.setSizeFull();
    windowLayout.setStyleName(NovaForge.LAYOUT_LIGHTGREY);

    HorizontalLayout filtersLayout = new HorizontalLayout();
    filtersLayout.setSpacing(true);
    filtersLayout.setMargin(true);
    viewUsersFilterLabel = new Label();
    viewUsersFiltersText = new TextField();
    filtersLayout.addComponent(viewUsersFilterLabel);
    filtersLayout.addComponent(viewUsersFiltersText);
    filtersLayout.setComponentAlignment(viewUsersFilterLabel, Alignment.MIDDLE_LEFT);
    filtersLayout.setComponentAlignment(viewUsersFiltersText, Alignment.MIDDLE_LEFT);

    HorizontalLayout usersListLayout = new HorizontalLayout();
    usersListLayout.setStyleName(NovaForge.LAYOUT_SCROLLABLE);
    usersListLayout.setWidth(100, Unit.PERCENTAGE);
    usersListLayout.setHeight(300, Unit.PIXELS);
    usersListLayout.setMargin(false);
    usersListLayout.setSpacing(false);
    viewUsersTable = new Table();
    viewUsersTable.setSelectable(true);
    viewUsersTable.setPageLength(10);
    viewUsersTable.setWidth(100, Unit.PERCENTAGE);
    viewUsersTable.setStyleName(Reindeer.TABLE_BORDERLESS);
    usersListLayout.addComponent(viewUsersTable);

    final HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.setMargin(new MarginInfo(true, false, false, false));
    buttonsLayout.setSpacing(true);
    viewUserCloseButton = new Button();
    viewUserCloseButton.setStyleName(NovaForge.BUTTON_LINK);
    buttonsLayout.addComponent(viewUserCloseButton);
    // Set window content
    windowLayout.addComponent(filtersLayout);
    windowLayout.addComponent(usersListLayout);
    windowLayout.addComponent(buttonsLayout);
    windowLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
    windowLayout.setExpandRatio(usersListLayout, 1);
    viewUsersWindow.setContent(windowLayout);
  }

  /**
   * * Init the window used to edit group roles
   */
  private void initEditRolesWindow()
  {
    editRolesWindow = new Window();
    editRolesWindow.setModal(true);
    editRolesWindow.setResizable(false);
    editRolesWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_GROUP_RIGHTS));

    editRolesComponent = new RolesHandlerComponent(true);
    editRolesWindow.setContent(editRolesComponent);

  }

  /**
   * Init the window used to delete group
   */
  private void initDeleteGroupWindow()
  {
    deleteGroupWindow = new Window();
    deleteGroupWindow.setModal(true);
    deleteGroupWindow.setResizable(false);
    deleteGroupWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_GROUP_TRASH));

    deleteGroupConfirmLabel = new Label();
    deleteGroupConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(400, Unit.PIXELS);

    // Init notification bos
    deleteGroupNotifCheck = new CheckBox();
    deleteGroupNotifCheck.setValue(true);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    deleteGroupConfirmButton = new Button();
    deleteGroupCancelButton = new Button();
    deleteGroupCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    deleteGroupConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_TRASH_RED));
    deleteGroupConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(deleteGroupCancelButton);
    buttons.addComponent(deleteGroupConfirmButton);
    buttons.setComponentAlignment(deleteGroupCancelButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(deleteGroupConfirmButton, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(deleteGroupConfirmLabel);
    windowLayout.addComponent(deleteGroupNotifCheck);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(deleteGroupConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(deleteGroupNotifCheck, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    deleteGroupWindow.setContent(windowLayout);

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
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    createButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_ADD));
    groupsForm.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_LIST));
    filterTextField.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_FILTER));
    groupsTable.setColumnHeader(GroupItemProperty.NAME.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_NAME));
    groupsTable.setColumnHeader(GroupItemProperty.DESCRIPTION.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_DESCRIPTION));
    groupsTable.setColumnHeader(GroupItemProperty.ROLES.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_EDIT_ROLES));
    groupsTable.setColumnHeader(GroupItemProperty.USERS.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_EDIT_USERS));
    if (groupsTable.getColumnHeader(GroupItemProperty.VISIBILITY.getPropertyId()) != null)
    {
      groupsTable.setColumnHeader(GroupItemProperty.VISIBILITY.getPropertyId(), MembershipsModule
          .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_PUBLIC));
    }
    groupsTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    editGroupWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_TITLE));
    if (groupFieldFactory.getName() != null)
    {
      groupFieldFactory.getName().setCaption(
          MembershipsModule.getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_NAME));
      groupFieldFactory.getName().setRequiredError(
          MembershipsModule.getPortalMessages()
              .getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_NAME_REQUIRED));
      groupFieldFactory.getName().removeAllValidators();
      groupFieldFactory.getName().addValidator(
          new StringLengthValidator(MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_GROUPS_NAME_REQUIRED, 3, 40, false)));
    }
    if (groupFieldFactory.getDescription() != null)
    {
      groupFieldFactory.getDescription().setCaption(
          MembershipsModule.getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_DESCRIPTION));
      groupFieldFactory.getDescription().setRequiredError(
          MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_GROUPS_DESCRIPTION_REQUIRED));
      groupFieldFactory.getDescription().removeAllValidators();
      groupFieldFactory.getDescription().addValidator(
          new StringLengthValidator(MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_GROUPS_DESCRIPTION_REQUIRED), 3, 250, false));
    }
    if (groupFieldFactory.getVisibility() != null)
    {
      groupFieldFactory.getVisibility().setCaption(
          MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_GROUPS_EDIT_MAKEPUBLIC));
    }
    editGroupCancelButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    editGroupConfirmButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_CONFIRM));
    editUsersWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_USERS_TITLE));
    editUsersCancelButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    editUsersConfirmButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_USERS_CONFIRM));
    userTableSelectable.refreshLocale(pLocale);
    editRolesComponent.refreshLocale(pLocale);
    editRolesWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_ROLES_TITLE));
    editRolesComponent.getConfirmButton().setCaption(
        MembershipsModule.getPortalMessages().getMessage(pLocale,
            Messages.MEMBERSHIPS_GROUPS_EDIT_ROLES_CONFIRM));
    deleteGroupWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_DELETE_TITLE));
    deleteGroupConfirmLabel.setValue(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_DELETE_CONFIRMLABEL));
    deleteGroupNotifCheck.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_DELETE_NOTIFICATION));
    deleteGroupCancelButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    deleteGroupConfirmButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_EDIT_DELETE_CONFIRM));

    viewUsersWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_VIEW_USERS_DESCRIPTION));
    viewUserCloseButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CLOSE));
    viewUsersFilterLabel.setValue(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.USERMANAGEMENT_FILTER_FIELDS));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCreateButton()
  {
    return createButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getGroupsTable()
  {
    return groupsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getEditRolesWindow()
  {
    return editRolesWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RolesHandlerComponent getEditRolesComponent()
  {
    return editRolesComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getDeleteGroupWindow()
  {
    return deleteGroupWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDeleteGroupConfirmButton()
  {
    return deleteGroupConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDeleteGroupCancelButton()
  {
    return deleteGroupCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getDeleteGroupNotifCheck()
  {
    return deleteGroupNotifCheck;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attachGroupsTable(final boolean pAttach)
  {
    if (pAttach)
    {
      groupsTable.refreshRowCache();
      groupsTableLayout.addComponent(groupsTable);
    }
    else
    {
      groupsTableLayout.removeAllComponents();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditGroupCancelButton()
  {
    return editGroupCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditGroupConfirmButton()
  {
    return editGroupConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getEditGroupForm()
  {
    return editGroupForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getEditGroupWindow()
  {
    return editGroupWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditUsersCancelButton()
  {
    return editUsersCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditUsersConfirmButton()
  {
    return editUsersConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getEditUsersWindow()
  {
    return editUsersWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserTableSelectable getEditUsersTable()
  {
    return userTableSelectable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getViewUsersWindow()
  {
    return viewUsersWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getViewUserCloseButton()
  {
    return viewUserCloseButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getViewUsersTable()
  {
    return viewUsersTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getViewUsersFiltersText()
  {
    return viewUsersFiltersText;
  }

}
