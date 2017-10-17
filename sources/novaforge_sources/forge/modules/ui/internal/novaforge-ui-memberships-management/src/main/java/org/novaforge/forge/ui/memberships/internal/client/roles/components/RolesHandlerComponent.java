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
package org.novaforge.forge.ui.memberships.internal.client.roles.components;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.RolesContainer;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * This component is used to display a way to update role for a membership
 * 
 * @author Guillaume Lamirand
 */
public class RolesHandlerComponent extends VerticalLayout
{

  /**
   * Serialization id
   */
  private static final long serialVersionUID = -4420222948069676402L;
  private final boolean nullAllowed;
  // Component
  private Table             rolesTable;
  private CssLayout         rolesTableLayout;
  private Label             descriptionLabel;
  private Button            cancelButton;
  private Button            confirmButton;
  private RolesContainer    rolesContainer;
  private Set<String>       currentRoles     = new HashSet<String>();
  private String            defaultRole;

  /**
   * Default constructor.
   */
  public RolesHandlerComponent()
  {
    this(false);
  }

  /**
   * Constructor which allow to set if its possible to validate with no role selected
   * 
   * @param pNullAllowed
   */
  public RolesHandlerComponent(final boolean pNullAllowed)
  {
    nullAllowed = pNullAllowed;
    initLayout();
    initRolesList();

  }

  private void initLayout()
  {
    setMargin(true);
    setSpacing(true);
    setWidth(500, Unit.PIXELS);

    descriptionLabel = new Label();

    rolesTableLayout = new CssLayout();
    rolesTable = new Table();
    rolesTable.setPageLength(6);
    rolesTable.setWidth(450, Unit.PIXELS);
    rolesTable.setStyleName(Reindeer.TABLE_STRONG);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    confirmButton = new Button();
    cancelButton = new Button();
    cancelButton.setStyleName(NovaForge.BUTTON_LINK);
    confirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_VALIDATE_ROUND));
    confirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(cancelButton);
    buttons.addComponent(confirmButton);
    buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(confirmButton, Alignment.MIDDLE_CENTER);
    // Set window content
    addComponent(descriptionLabel);
    addComponent(rolesTableLayout);
    addComponent(buttons);
    setComponentAlignment(descriptionLabel, Alignment.MIDDLE_CENTER);
    setComponentAlignment(rolesTableLayout, Alignment.MIDDLE_CENTER);
    setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
  }

  private void initRolesList()
  {
    rolesContainer = new RolesContainer();
    rolesTable.setContainerDataSource(rolesContainer);

    rolesTable.addGeneratedColumn(RoleItemProperty.SELECT.getPropertyId(),
        new RoleColumnSelectGenerator(this));
    rolesTable.addGeneratedColumn(RoleItemProperty.DEFAULT.getPropertyId(), new RoleColumnDefaultGenerator(
        this));

    // Define visibles columns
    rolesTable.setVisibleColumns(RoleItemProperty.ORDER.getPropertyId(),
        RoleItemProperty.NAME.getPropertyId(), RoleItemProperty.SELECT.getPropertyId(),
        RoleItemProperty.DEFAULT.getPropertyId());

    rolesTable.setColumnAlignments(Align.CENTER, Align.LEFT, Align.CENTER, Align.CENTER);
    // Define special column width
    rolesTable.setColumnExpandRatio(RoleItemProperty.ORDER.getPropertyId(), 0);
    rolesTable.setColumnWidth(RoleItemProperty.ORDER.getPropertyId(), 35);
    rolesTable.setColumnAlignment(RoleItemProperty.ORDER.getPropertyId(), Align.CENTER);
    rolesTable.setColumnExpandRatio(RoleItemProperty.NAME.getPropertyId(), 0.5f);
    rolesTable.setColumnAlignment(RoleItemProperty.NAME.getPropertyId(), Align.CENTER);
    rolesTable.setColumnWidth(RoleItemProperty.SELECT.getPropertyId(), 50);
    rolesTable.setColumnAlignment(RoleItemProperty.SELECT.getPropertyId(), Align.CENTER);
    rolesTable.setColumnExpandRatio(RoleItemProperty.SELECT.getPropertyId(), 0);
    rolesTable.setColumnWidth(RoleItemProperty.DEFAULT.getPropertyId(), 60);
    rolesTable.setColumnAlignment(RoleItemProperty.DEFAULT.getPropertyId(), Align.CENTER);
    rolesTable.setColumnExpandRatio(RoleItemProperty.DEFAULT.getPropertyId(), 0);
  }

  /**
   * Clear content of this component and sub-component
   */
  public void clearComponent()
  {
    currentRoles = new HashSet<String>();
    defaultRole = null;
    if (rolesContainer != null)
    {
      rolesContainer.removeAllItems();
    }

  }

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  public void refreshLocale(final Locale pLocale)
  {
    descriptionLabel.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_MEMBER_TITLE));
    cancelButton.setCaption(MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.ACTIONS_CANCEL));
    confirmButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_MEMBER_CONFIRM));
    rolesTable.setColumnHeader(RoleItemProperty.ORDER.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_ORDER));
    rolesTable.setColumnHeader(RoleItemProperty.NAME.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_NAME));
    rolesTable.setColumnHeader(RoleItemProperty.SELECT.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_MEMBER_SELECT));
    rolesTable.setColumnHeader(RoleItemProperty.DEFAULT.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_MEMBER_DEFAULT));
  }

  /**
   * Call when a click is received in select column
   * 
   * @param pItemId
   *          id of item selected
   */
  public void onClickSelectRole(final String pItemId)
  {
    attachRolesTable(false);
    if ((nullAllowed || ((!nullAllowed) && (currentRoles.size() > 1)))
        && (currentRoles.contains(pItemId)))
    {
      currentRoles.remove(pItemId);
      rolesContainer.getContainerProperty(pItemId, RoleItemProperty.DEFAULT.getPropertyId()).setValue(false);
      rolesContainer.getContainerProperty(pItemId, RoleItemProperty.SELECT.getPropertyId()).setValue(false);
    }
    else if (!currentRoles.contains(pItemId))
    {
      currentRoles.add(pItemId);
      rolesContainer.getContainerProperty(pItemId, RoleItemProperty.SELECT.getPropertyId()).setValue(true);

    }
    attachRolesTable(true);

  }

  /**
   * Used to attach and detach table from its parent
   *
   * @param pAttach
   *          true to attach, false to detach
   */
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
   * Call when a click is received in default column
   *
   * @param pItemId
   *          id of item selected
   */
  public void onClickDefaultRole(final String pItemId)
  {
    attachRolesTable(false);
    if ((defaultRole != null) && (defaultRole.equals(pItemId)))
    {
      defaultRole = null;
      rolesContainer.getContainerProperty(pItemId, RoleItemProperty.DEFAULT.getPropertyId()).setValue(false);
    }
    else if (defaultRole != null)
    {
      rolesContainer.getContainerProperty(defaultRole, RoleItemProperty.DEFAULT.getPropertyId()).setValue(
          false);
      rolesContainer.getContainerProperty(pItemId, RoleItemProperty.DEFAULT.getPropertyId()).setValue(true);
      rolesContainer.getContainerProperty(pItemId, RoleItemProperty.SELECT.getPropertyId()).setValue(true);
      defaultRole = pItemId;
      currentRoles.add(pItemId);
    }
    else
    {
      rolesContainer.getContainerProperty(pItemId, RoleItemProperty.DEFAULT.getPropertyId()).setValue(true);
      rolesContainer.getContainerProperty(pItemId, RoleItemProperty.SELECT.getPropertyId()).setValue(true);
      defaultRole = pItemId;
      currentRoles.add(pItemId);

    }
    attachRolesTable(true);

  }

  /**
   * Return {@link Label} containing description
   * 
   * @return {@link Label} containing description
   */
  public Label getDescriptionLabel()
  {
    return descriptionLabel;
  }

  /**
   * Return {@link Button} used to confirm the edition
   * 
   * @return {@link Button} used to confirm the edition
   */
  public Button getConfirmButton()
  {
    return confirmButton;
  }

  /**
   * Return {@link Button} used to cancel action
   * 
   * @return {@link Button} used to cancel action
   */
  public Button getCancelButton()
  {
    return cancelButton;
  }

  /**
   * Return {@link Table} used to display roles
   * 
   * @return {@link Table} used to display roles
   */
  public Table getRolesTable()
  {
    return rolesTable;
  }

  /**
   * Return {@link RolesContainer} used to store roles
   * 
   * @return {@link RolesContainer} used to store roles
   */
  public RolesContainer getRolesContainer()
  {
    return rolesContainer;
  }

  /**
   * Return a {@link Set} containing all role selected
   * 
   * @return a {@link Set} containing all role selected
   */
  public Set<String> getSelectedRoles()
  {
    final Set<String> selectedRole = new HashSet<String>();
    for (final Object itemId : rolesContainer.getItemIds())
    {
      final Item item = rolesContainer.getItem(itemId);
      final Property<?> selectProperty = item.getItemProperty(RoleItemProperty.SELECT.getPropertyId());
      final boolean isEnable = (selectProperty != null) && (selectProperty.getValue() != null)
          && ((Boolean) selectProperty.getValue());
      if (isEnable)
      {
        final String roleName = (String) item.getItemProperty(RoleItemProperty.NAME.getPropertyId())
            .getValue();
        selectedRole.add(roleName);
      }
    }
    return selectedRole;
  }

  /**
   * Return a {@link Set} containing all role
   * 
   * @return a {@link Set} containing all role
   */
  public Set<String> getCurrentRoles()
  {
    return currentRoles;
  }

  /**
   * Return the role defined as default
   * 
   * @return the role defined as default
   */
  public String getDefaultRole()
  {
    return defaultRole;
  }

  /**
   * Set the role defined as default
   * 
   * @param pDefaultRole
   *          the role name
   */
  public void setDefaultRole(final String pDefaultRole)
  {
    defaultRole = pDefaultRole;
  }

  /**
   * Return true if norole can be selected
   * 
   * @return
   *         true if if norole can be selected, false if at least one role has to be selected
   */
  public boolean isNullAllowed()
  {
    return nullAllowed;
  }

}
