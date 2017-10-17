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
package org.novaforge.forge.ui.memberships.internal.client.roles.update;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RoleFieldFactory;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RolesMappingComponent;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;

import java.util.Collection;
import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class RolesViewImpl extends VerticalLayout implements RolesView
{

  /**
	 * 
	 */
  private static final String   FIELD_FILTER     = "filter";

  /**
   * Serialization id
   */
  private static final long     serialVersionUID = -4420222948069676402L;

  private Button                createButton;

  private HorizontalLayout      headerButtons;
  private TextField             filterTextField;
  private Form                  rolesForm;
  private CssLayout             rolesTableLayout;
  private Table                 rolesTable;
  private Window                editRoleWindow;
  private Form                  editRoleForm;
  private Button                editRoleCancelButton;
  private Button                editRoleConfirmButton;
  private DeleteConfirmWindow   deleteRoleWindow;

  private RoleFieldFactory      roleFieldFactory;

  private Window                editRolesMappingWindow;
  private RolesMappingComponent editRolesMappingComponent;
  private Button                editRolesMappingConfirmButton;
  private Button                editRolesMappingCancelButton;

  /**
   * Default constructor.
   */
  public RolesViewImpl()
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
    initEditRoleWindow();
    initEditRoleMappingWindow();
    initDeleteRoleWindow();
  }

  private Component initHeaders()
  {
    headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    createButton = new Button();
    createButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));

    headerButtons.addComponent(createButton);
    return headerButtons;
  }

  private Component initFilter()
  {
    rolesForm = new Form();
    rolesForm.setImmediate(true);
    rolesForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    rolesForm.addField(FIELD_FILTER, filterTextField);
    return rolesForm;
  }

  private Component initContent()
  {
    rolesTableLayout = new CssLayout();
    rolesTableLayout.setWidth(100, Unit.PERCENTAGE);
    rolesTable = new Table();
    rolesTable.setSelectable(true);
    rolesTable.setPageLength(10);
    rolesTable.setWidth(100, Unit.PERCENTAGE);
    rolesTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3559455760887782118L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(final LayoutClickEvent event)
      {
        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if ((child != null) && (!child.equals(rolesTable)) && (!child.equals(rolesTableLayout)))
        {
          final Collection<?> itemIds = rolesTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (rolesTable.isSelected(itemId))
            {
              rolesTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    rolesTableLayout.addComponent(rolesTable);
    return rolesTableLayout;
  }

  private void initEditRoleWindow()
  {
    editRoleWindow = new Window();
    editRoleWindow.setModal(true);
    editRoleWindow.setResizable(false);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(new MarginInfo(false, true, true, true));
    windowLayout.setSpacing(true);
    windowLayout.setWidth(320, Unit.PIXELS);

    editRoleForm = new Form();
    editRoleForm.setImmediate(true);
    editRoleForm.setInvalidCommitted(false);
    editRoleForm.setFooter(null);
    roleFieldFactory = new RoleFieldFactory();
    editRoleForm.setFormFieldFactory(roleFieldFactory);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    editRoleConfirmButton = new Button();
    editRoleCancelButton = new Button();
    editRoleCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    editRoleConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
    editRoleConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(editRoleCancelButton);
    buttons.addComponent(editRoleConfirmButton);

    // Set window content
    windowLayout.addComponent(editRoleForm);
    windowLayout.setComponentAlignment(editRoleForm, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    editRoleWindow.setContent(windowLayout);

  }

  private void initEditRoleMappingWindow()
  {
    editRolesMappingWindow = new Window();
    editRolesMappingWindow.setModal(true);
    editRolesMappingWindow.setResizable(false);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(new MarginInfo(false, true, true, true));
    windowLayout.setSpacing(true);
    windowLayout.setWidth(550, Unit.PIXELS);

    editRolesMappingComponent = new RolesMappingComponent();

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    editRolesMappingConfirmButton = new Button();
    editRolesMappingCancelButton = new Button();
    editRolesMappingCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    editRolesMappingConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
    editRolesMappingConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(editRolesMappingCancelButton);
    buttons.setComponentAlignment(editRolesMappingCancelButton, Alignment.MIDDLE_CENTER);
    buttons.addComponent(editRolesMappingConfirmButton);
    buttons.setComponentAlignment(editRolesMappingConfirmButton, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(editRolesMappingComponent);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(editRolesMappingComponent, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    editRolesMappingWindow.setContent(windowLayout);

  }

  private void initDeleteRoleWindow()
  {
    deleteRoleWindow = new DeleteConfirmWindow(Messages.MEMBERSHIPS_ROLES_EDIT_DELETE_CONFIRMLABEL);
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
        Messages.MEMBERSHIPS_ROLES_EDIT_ADD));
    rolesForm.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_EDIT_TITLE));
    filterTextField.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_FILTER));
    rolesTable.setColumnHeader(RoleItemProperty.NAME.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_NAME));
    rolesTable.setColumnHeader(RoleItemProperty.DESCRIPTION.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_DESCRIPTION));
    rolesTable.setColumnHeader(RoleItemProperty.ORDER.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_ORDER));
    rolesTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    editRoleWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_EDIT_INFO_TITLE));
    if (roleFieldFactory.getName() != null)
    {
      roleFieldFactory.getName().setCaption(
          MembershipsModule.getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_NAME));
      roleFieldFactory.getName()
          .setRequiredError(
              MembershipsModule.getPortalMessages().getMessage(pLocale,
                  Messages.MEMBERSHIPS_ROLES_NAME_REQUIRED));
      roleFieldFactory.getName().removeAllValidators();
      roleFieldFactory.getName().addValidator(
          new StringLengthValidator(MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_ROLES_NAME_REQUIRED), 3, 40, false));
    }
    if (roleFieldFactory.getDescription() != null)
    {
      roleFieldFactory.getDescription().setCaption(
          MembershipsModule.getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_DESCRIPTION));
      roleFieldFactory.getDescription().setRequiredError(
          MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_ROLES_DESCRIPTION_REQUIRED));
      roleFieldFactory.getDescription().removeAllValidators();
      roleFieldFactory.getDescription().addValidator(
          new StringLengthValidator(MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_ROLES_DESCRIPTION_REQUIRED), 3, 250, false));
    }
    editRoleCancelButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    editRoleConfirmButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_EDIT_INFO_CONFIRM));
    editRolesMappingWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_EDIT_MAPPING_TITLE));
    editRolesMappingComponent.refreshLocale(pLocale);
    editRolesMappingCancelButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    editRolesMappingConfirmButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_EDIT_MAPPING_CONFIRM));
    deleteRoleWindow.refreshLocale(pLocale);

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
  public Table getRolesTable()
  {
    return rolesTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getDeleteRoleWindow()
  {
    return deleteRoleWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attachRolesTable(final boolean pAttach)
  {
    if (pAttach)
    {
      rolesTable.refreshRowCache();
      rolesTableLayout.addComponent(rolesTable);
    }
    else
    {
      rolesTableLayout.removeAllComponents();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditRoleCancelButton()
  {
    return editRoleCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditRoleConfirmButton()
  {
    return editRoleConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getEditRoleForm()
  {
    return editRoleForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getEditRoleWindow()
  {
    return editRoleWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditRolesMappingCancelButton()
  {
    return editRolesMappingCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditRolesMappingConfirmButton()
  {
    return editRolesMappingConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RolesMappingComponent getEditRolesMappingComponent()
  {
    return editRolesMappingComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getEditRolesMappingWindow()
  {
    return editRolesMappingWindow;
  }

}
