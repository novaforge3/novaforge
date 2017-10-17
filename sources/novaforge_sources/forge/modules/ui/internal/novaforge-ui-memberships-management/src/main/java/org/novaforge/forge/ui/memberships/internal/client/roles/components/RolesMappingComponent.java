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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.memberships.internal.client.containers.RolesMappingContainer;
import org.novaforge.forge.ui.memberships.internal.client.containers.RolesMappingItemProperty;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * This component is used to update role mapping by application
 * 
 * @author Guillaume Lamirand
 */
public class RolesMappingComponent extends VerticalLayout
{
  /**
   * Serial version id
   */
  private static final long     serialVersionUID = -1040886202786291667L;
  /**
   * The {@link Form} containing roles mapping configuration
   */
  private Form                  roleMappingForm;
  /**
   * The {@link TreeTable} containing roles mapping association
   */
  private TreeTable             treeTable;
  /**
   * The {@link RolesMappingContainer} containing application details
   */
  private RolesMappingContainer rolesMappingContainer;
  /**
   * The project role selected
   */
  private Role                  selectedRole;

  /**
   * Default constructor.
   */
  public RolesMappingComponent()
  {
    setMargin(true);
    setSpacing(true);

    final Component content = initForm();
    final Component table = initTable();
    addComponent(content);
    addComponent(table);
    setComponentAlignment(content, Alignment.MIDDLE_CENTER);
    setComponentAlignment(table, Alignment.MIDDLE_CENTER);
    setExpandRatio(content, 0);
    setExpandRatio(content, 1);

    initTableContainer();
  }

  /**
   * Init the main content
   *
   * @return {@link Component} created
   */
  private Component initForm()
  {
    roleMappingForm = new Form();
    roleMappingForm.setImmediate(true);
    roleMappingForm.setInvalidCommitted(false);
    roleMappingForm.setFooter(null);
    return roleMappingForm;
  }

  /**
   * Init the table which will contain application detaiols
   *
   * @return {@link Component} created
   */
  private Component initTable()
  {
    treeTable = new TreeTable();
    treeTable.setHeight(250, Unit.PERCENTAGE);
    treeTable.setWidth(450, Unit.PIXELS);
    treeTable.setStyleName(Reindeer.TABLE_STRONG);
    treeTable.addStyleName(NovaForge.TREETABLE_UNCOLLAPSE);
    return treeTable;
  }

  /**
   * Init table container details
   */
  private void initTableContainer()
  {
    rolesMappingContainer = new RolesMappingContainer();
    treeTable.setContainerDataSource(rolesMappingContainer);
    treeTable.setItemDescriptionGenerator(new RolesMappingDescriptionGenerator());
    treeTable.setCellStyleGenerator(new RolesMappingStyleGenerator());
    treeTable.addGeneratedColumn(RolesMappingItemProperty.ROLES.getPropertyId(),
        new RolesMappingRolesGenerator(this));
    treeTable.setItemIconPropertyId(RolesMappingItemProperty.ICON.getPropertyId());
    treeTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
    treeTable.setVisibleColumns(RolesMappingItemProperty.CAPTION.getPropertyId(),
        RolesMappingItemProperty.ROLES.getPropertyId());
    treeTable.setColumnHeaders(RolesMappingItemProperty.CAPTION.getPropertyId(),
                               RolesMappingItemProperty.ROLES.getPropertyId());
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
    roleMappingForm.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_MAPPING_TITLE));
    setFormDesc(pLocale);
    treeTable.setColumnHeader(RolesMappingItemProperty.CAPTION.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_NAME));
    treeTable.setColumnHeader(RolesMappingItemProperty.ROLES.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_MAPPING_ROLECOLUMN));
  }

  /**
   * Set the correct description according the current state
   *
   * @param pLocale
   *          the locale
   */
  private void setFormDesc(final Locale pLocale)
  {
    String description = MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_ROLES_MAPPING_ADD_DESCRIPTION);
    if (selectedRole != null)
    {
      description = MessageFormat.format(
          MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_ROLES_MAPPING_EDIT_DESCRIPTION), selectedRole.getName());
    }
    roleMappingForm.setDescription(description);
  }

  /**
   * Return {@link Form} used by this component
   *
   * @return {@link Form}
   */
  public Form getForm()
  {
    return roleMappingForm;
  }

  /**
   * Return {@link TreeTable} used to display {@link ProjectApplication} and their roles
   *
   * @return {@link TreeTable}
   */
  public TreeTable getTable()
  {
    return treeTable;
  }

  /**
   * Return {@link RolesMappingContainer} which contains {@link ProjectApplication} and their roles
   *
   * @return {@link RolesMappingContainer}
   */
  public RolesMappingContainer getContainer()
  {
    return rolesMappingContainer;
  }

  /**
   * Defined the current selected {@link ProjectRole#getName()}
   *
   * @param pSelectedRole
   *          role name
   */
  public void setSelectedRole(final Role pSelectedRole)
  {
    selectedRole = pSelectedRole;
    if (getParent() != null)
    {
      setFormDesc(getLocale());
    }

  }

  /**
   * Check if role name is either a admin or super admin role
   *
   * @return true if the current role is an admin or super admin type
   */
  public boolean isAdminRole()
  {
    boolean isAdminRole = false;
    final String administratorRoleName = MembershipsModule.getForgeConfigurationService()
        .getForgeAdministratorRoleName();
    final String superAdministratorRoleName = MembershipsModule.getForgeConfigurationService()
        .getForgeSuperAdministratorRoleName();
    if ((selectedRole != null)
        && (administratorRoleName.equals(selectedRole.getName()) || superAdministratorRoleName
            .equals(selectedRole.getName())))
    {
      isAdminRole = true;
    }
    return isAdminRole;

  }

  /**
   * This method will update the mapping with container information
   *
   * @param pProjectId
   *          the project id
   * @param pRoleName
   *          the role used to update
   * @throws RolesMappingException
   *           contains others exceptions
   */
  public void updateRoleMapping(final String pProjectId, final String pRoleName) throws RolesMappingException
  {
    final Collection<?> itemIds = rolesMappingContainer.getItemIds();
    for (final Object itemId : itemIds)
    {
      final Item item = rolesMappingContainer.getItem(itemId);
      final Property<?> nodeProperty = item.getItemProperty(RolesMappingItemProperty.NODE.getPropertyId());
      if ((nodeProperty != null) && (nodeProperty.getValue() != null)
          && (nodeProperty.getValue() instanceof ProjectApplication))
      {
        final ProjectApplication projectApplication = (ProjectApplication) nodeProperty.getValue();
        final Boolean isAvailable = (Boolean) item.getItemProperty(
            RolesMappingItemProperty.AVAILABILITY.getPropertyId()).getValue();
        if (isAvailable)
        {

          try
          {
            final PluginService pluginService = MembershipsModule.getPluginsManager().getPluginService(
                projectApplication.getPluginUUID().toString());
            final Map<String, String> rolesMapping = pluginService.getRolesMapping(projectApplication
                .getPluginInstanceUUID().toString());
            final Property<?> roleMappedProperty = item.getItemProperty(RolesMappingItemProperty.ROLE_MAPPED
                .getPropertyId());
            if ((roleMappedProperty != null) && (roleMappedProperty.getValue() != null))
            {
              final String roleName = (String) item.getItemProperty(
                  RolesMappingItemProperty.ROLE_MAPPED.getPropertyId()).getValue();

              rolesMapping.put(pRoleName, roleName);
            }
            else
            {
              rolesMapping.remove(pRoleName);
            }
            MembershipsModule.getApplicationPresenter().updateRoleMapping(pProjectId,
                projectApplication.getUri(), rolesMapping);
          }
          catch (final PluginManagerException e)
          {
            throw new RolesMappingException(e);
          }
          catch (final PluginServiceException e)
          {
            throw new RolesMappingException(e);
          }
          catch (final ApplicationServiceException e)
          {
            throw new RolesMappingException(e);
          }

        }
      }
    }
  }

  /**
   * This exception is used by {@link RolesMappingComponent} to encapsule many core exception
   *
   * @author Guillaume Lamirand
   */
  public class RolesMappingException extends Exception
  {

    /**
     * Serial version id
     */
    private static final long serialVersionUID = 746807957532811517L;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     *
     * @see {@link Exception#Exception()}
     */
    public RolesMappingException()
    {
      super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param pMessage
     *     the detail message.
     *
     * @see {@link Exception#Exception(String)}
     */
    public RolesMappingException(final String pMessage)
    {
      super(pMessage);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param pMessage
     *     the detail message
     * @param pCause
     *     the cause
     *
     * @see {@link Exception#Exception(String, Throwable)}
     */
    public RolesMappingException(final String pMessage, final Throwable pCause)
    {
      super(pMessage, pCause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt>
     *
     * @param pCause
     *     the cause
     *
     * @see {@link Exception#Exception(Throwable)}
     */
    public RolesMappingException(final Throwable pCause)
    {
      super(pCause);
    }

  }
}
