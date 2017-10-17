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
package org.novaforge.forge.ui.applications.internal.client.applications;

import com.google.common.base.Strings;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.PluginRealm;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.applications.internal.client.applications.components.ApplicationFormProperty;
import org.novaforge.forge.ui.applications.internal.client.events.RefreshTreeEvent;
import org.novaforge.forge.ui.applications.internal.client.events.SelectParentEvent;
import org.novaforge.forge.ui.applications.internal.module.AbstractApplicationsPresenter;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This presenter handles applications components displayed.
 * 
 * @author Guillaume Lamirand
 */
public class CreateApplicationsPresenter extends AbstractApplicationsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long            serialVersionUID = -5042299647493799344L;
  /**
   * Logger component
   */
  private static final Log             LOGGER           = LogFactory
                                                            .getLog(CreateApplicationsPresenter.class);
  /**
   * Content of project view
   */
  final private CreateApplicationsView view;

  private final String                 projectId;
  private final Map<String, String>    rolesMapping     = new HashMap<String, String>();
  private final IndexedContainer       categories       = new IndexedContainer();
  private final IndexedContainer       types            = new IndexedContainer();
  private final IndexedContainer       roles            = new IndexedContainer();
  private final List<ComboBox>         roleBoxes        = new ArrayList<ComboBox>();
  private Space parentSpace;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the init context
   * @param pProjectId
   *          the current project id
   */
  public CreateApplicationsPresenter(final CreateApplicationsView pView, final PortalContext pPortalContext,
      final String pProjectId)
  {
    super(pPortalContext);
    view = pView;
    projectId = pProjectId;

    // Init categories combobox
    categories.addContainerProperty(ApplicationFormProperty.NAME.getPropertyId(), String.class, null);
    categories.addContainerProperty(ApplicationFormProperty.ID.getPropertyId(), String.class, null);
    view.getCategoriesBox().setContainerDataSource(categories);
    view.getCategoriesBox().setItemCaptionPropertyId(ApplicationFormProperty.NAME.getPropertyId());
    view.getCategoriesBox().setItemCaptionMode(ItemCaptionMode.PROPERTY);
    // Init types combobox
    types.addContainerProperty(ApplicationFormProperty.NAME.getPropertyId(), String.class, null);
    types.addContainerProperty(ApplicationFormProperty.CATEGORY.getPropertyId(), String.class, null);
    types.addContainerProperty(ApplicationFormProperty.ICON.getPropertyId(), Resource.class, null);
    view.getTypesBox().setContainerDataSource(types);
    view.getTypesBox().setItemCaptionPropertyId(ApplicationFormProperty.NAME.getPropertyId());
    view.getTypesBox().setItemIconPropertyId(ApplicationFormProperty.ICON.getPropertyId());
    view.getTypesBox().setItemCaptionMode(ItemCaptionMode.PROPERTY);
    // Init roles combobox
    roles.addContainerProperty(ApplicationFormProperty.NAME.getPropertyId(), String.class, null);
    roles.addContainerProperty(ApplicationFormProperty.ID.getPropertyId(), String.class, null);

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getCategoriesBox().addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7964317309326923366L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        types.removeAllContainerFilters();
        final String value = (String) event.getProperty().getValue();
        if (Strings.isNullOrEmpty(value))
        {
          types.removeAllContainerFilters();
          view.getTypesBox().select(null);
        }
        else
        {
          types.addContainerFilter(ApplicationFormProperty.CATEGORY.getPropertyId(), value, true, true);
          final Collection<?> typesIds = types.getItemIds();
          if (typesIds.size() == 1)
          {
            view.getTypesBox().select(typesIds.iterator().next());
          }
        }
        view.getTypesBox().sanitizeSelection();
        for (final ComboBox roleBox : roleBoxes)
        {
          roleBox.setValue(null);
        }
      }
    });
    view.getTypesBox().addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -8727951452945379161L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        roles.removeAllContainerFilters();
        if (event.getProperty().getValue() != null)
        {
          final String uuid = (String) event.getProperty().getValue();
          final String category = (String) types.getContainerProperty(uuid,
              ApplicationFormProperty.CATEGORY.getPropertyId()).getValue();
          view.getCategoriesBox().select(category);

          roles.addContainerFilter(ApplicationFormProperty.ID.getPropertyId(), uuid, true, true);
        }
        else
        {
          for (final ComboBox roleBox : roleBoxes)
          {
            roleBox.setValue(null);
          }
          roles.addContainerFilter(ApplicationFormProperty.TYPE.getPropertyId(), " ", true, true);

        }
      }
    });
    view.getCancelButton().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = 1118059378603697848L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new SelectParentEvent(parentSpace, getUuid()));
      }
    });
    view.getApplyButton().addClickListener(new ClickListener()
    {
      /**
			 * 
			 */
      private static final long serialVersionUID = 3063292794092695535L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        try
        {
          view.getApplicationForm().commit();
          view.getRolesForm().commit();

          final String pluginUUID = (String) view.getTypesBox().getValue();
          final String appName = view.getNameField().getValue();
          final String descName = view.getDescriptionField().getValue();
          if ((pluginUUID != null) && (appName != null))
          {
            final ProjectApplication application = ApplicationsModule.getApplicationPresenter()
                .addApplication(projectId, parentSpace.getUri(), appName, descName,
                    UUID.fromString(pluginUUID), rolesMapping);

            getEventBus().publish(new RefreshTreeEvent(application, getUuid()));
          }
        }
        catch (final ApplicationServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e,
              view.getLocale());

        }
        catch (final InvalidValueException v)
        {
          LOGGER.error("Unable to create application", v);
          Notification.show(v.getLocalizedMessage(), Type.WARNING_MESSAGE);
        }
        catch (final Exception e)
        {
          LOGGER.error("Unable to create application", e);
          // Ignored, we'll let the Form handle the errors
        }

      }
    });

  }

  /**
   * Will refresh this presenter
   * 
   * @param pSpace
   *          the parent space of the new application
   */
  public void refresh(final Space pSpace)
  {
    parentSpace = pSpace;
    types.removeAllContainerFilters();
    types.removeAllItems();
    categories.removeAllItems();
    categories.removeAllContainerFilters();
    roles.removeAllItems();
    roles.removeAllContainerFilters();
    roleBoxes.clear();
    try
    {
      // Init roles
      view.getRolesForm().removeAllProperties();
      final List<ProjectRole> projectRoles = ApplicationsModule.getProjectRolePresenter().getAllRoles(
          projectId);
      for (final ProjectRole projectRole : projectRoles)
      {
        final String roleName = projectRole.getName();
        final ComboBox roleBox = new ComboBox();
        roleBox.addValueChangeListener(new ValueChangeListener()
        {

          /**
           * Serial version id
           */
          private static final long serialVersionUID = -6464863588020606524L;

          /**
           * {@inheritDoc}
           */
          @Override
          public void valueChange(final ValueChangeEvent pEvent)
          {
            final Integer roleId = (Integer) pEvent.getProperty().getValue();
            if (roleId != null)
            {
              final String appRoleName = (String) roles.getContainerProperty(roleId,
                  ApplicationFormProperty.NAME.getPropertyId()).getValue();
              rolesMapping.put(roleName, appRoleName);
            }
            else
            {
              rolesMapping.remove(roleName);
            }
          }
        });
        roleBox.setInputPrompt(ApplicationsModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.APPLICATIONS_APPLICATION_ROLES_NONE));
        roleBox.setTextInputAllowed(false);
        if (RealmType.SYSTEM.equals(projectRole.getRealmType()))
        {
        	
     
          /*"member" not required*/
		 final String memberRoleName = ApplicationsModule.getForgeConfigurationService().getForgeMemberRoleName();
         if (memberRoleName.compareToIgnoreCase(roleName) == 0) {
        	 roleBox.setRequired(false);
         } else {	 
             roleBox.setRequired(true);
         }
          roleBox.setNullSelectionAllowed(false);
          roleBox.setRequiredError(ApplicationsModule.getPortalMessages().getMessage(view.getLocale(),
              Messages.APPLICATIONS_APPLICATION_ROLES_REQUIRED));
        }
        roleBox.setCaption(roleName);
        view.getRolesForm().addField(roleName, roleBox);
        roleBox.setContainerDataSource(roles);
        roleBox.setItemCaptionPropertyId(ApplicationFormProperty.NAME.getPropertyId());
        roleBox.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        roleBoxes.add(roleBox);
      }

      final PluginsManager pluginsManager = ApplicationsModule.getPluginsManager();
      final List<String> allPluginCategories = pluginsManager.getAllPluginCategories();
      for (final String categoryId : allPluginCategories)
      {
        final CategoryDefinitionService categoryService = ApplicationsModule.getPluginsCategoryManager()
            .getCategoryService(categoryId);
        String localizedName = categoryId;
        if (categoryService != null)
        {
          localizedName = categoryService.getName(view.getLocale());
          // Don't take in account SYSTEM Categories
          if (PluginRealm.SYSTEM.equals(categoryService.getRealm()))
          {
            continue;
          }
        }

        categories.addItem(categoryId);
        categories.getContainerProperty(categoryId, ApplicationFormProperty.NAME.getPropertyId()).setValue(
            localizedName);
        categories.getContainerProperty(categoryId, ApplicationFormProperty.ID.getPropertyId()).setValue(
            categoryId);

        final List<PluginMetadata> pluginMetadataList = pluginsManager
            .getAllInstantiablePluginsMetadataByCategory(categoryId);

        if (pluginMetadataList != null)
        {
          for (final PluginMetadata pluginMetadata : pluginMetadataList)
          {
            final String pluginUUID = pluginMetadata.getUUID();
            final PluginService pluginService = pluginsManager.getPluginService(pluginUUID);
            if (pluginService != null)
            {
              types.addItem(pluginUUID);
              types.getContainerProperty(pluginUUID, ApplicationFormProperty.NAME.getPropertyId()).setValue(
                  pluginMetadata.getType());
              types.getContainerProperty(pluginUUID, ApplicationFormProperty.CATEGORY.getPropertyId())
                  .setValue(categoryId);
              final byte[] pluginIcon = pluginService.getPluginIcon();
              if (pluginIcon != null)
              {
                final StreamResource buildImageResource = ResourceUtils.buildImageResource(pluginIcon,
                    pluginUUID);
                types.getContainerProperty(pluginUUID, ApplicationFormProperty.ICON.getPropertyId())
                    .setValue(buildImageResource);
              }

              final Set<String> pluginRoles = pluginService.findRoles();

              for (final String pluginRole : pluginRoles)
              {
                final Object roleItemId = roles.addItem();
                roles.getContainerProperty(roleItemId, ApplicationFormProperty.ID.getPropertyId()).setValue(
                    pluginUUID);
                roles.getContainerProperty(roleItemId, ApplicationFormProperty.NAME.getPropertyId())
                    .setValue(pluginRole);
              }
            }
          }
        }
      }
      roles.removeAllContainerFilters();
      roles.addContainerFilter(ApplicationFormProperty.TYPE.getPropertyId(), " ", true, true);
      categories.sort(new Object[] { ApplicationFormProperty.NAME.getPropertyId() }, new boolean[] { true });
      types.sort(new Object[] { ApplicationFormProperty.NAME.getPropertyId() }, new boolean[] { true });
      roles.sort(new Object[] { ApplicationFormProperty.NAME.getPropertyId() }, new boolean[] { true });
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());

    }
    refreshLocalized(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return ApplicationsModule.getPortalModuleId();
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
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
