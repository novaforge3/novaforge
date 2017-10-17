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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.applications.internal.client.events.LinkApplicationEvent;
import org.novaforge.forge.ui.applications.internal.client.events.RefreshTreeEvent;
import org.novaforge.forge.ui.applications.internal.module.AbstractApplicationsPresenter;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This presenter handles applications components displayed.
 * 
 * @author Guillaume Lamirand
 */
public class ApplicationsPresenter extends AbstractApplicationsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long         serialVersionUID = -5042299647493799344L;
  /**
   * Logger
   */
  private static final Log          LOG              = LogFactory.getLog(ApplicationsPresenter.class);
  /**
   * Content of project view
   */
  final private ApplicationsView view;
  private final String              projectId;
  private final Map<String, String> roleMapping      = new HashMap<String, String>();
  private ProjectApplication currentApp;
  private Space                     parentSpace;

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
  public ApplicationsPresenter(final ApplicationsView pView, final PortalContext pPortalContext,
      final String pProjectId)
  {
    super(pPortalContext);
    view = pView;
    projectId = pProjectId;

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getLinkButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 2085114095342001132L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new LinkApplicationEvent(currentApp, getUuid()));

      }

    });
    view.getEditButton().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4007391546749871601L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getDescriptionField().setReadOnly(false);
        view.getRolesForm().setReadOnly(false);
        view.getHeaderLayout().setVisible(false);
        view.getFooter().setVisible(true);
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
        view.getRolesForm().discard();
        final Set<Entry<String, String>> entrySet = roleMapping.entrySet();
        for (final Entry<String, String> entry : entrySet)
        {
          final Field field = view.getRolesForm().getField(entry.getKey());
          field.setValue(entry.getValue());
        }
        view.getDescriptionField().setReadOnly(true);
        view.getRolesForm().setReadOnly(true);
        view.getHeaderLayout().setVisible(true);
        view.getFooter().setVisible(false);
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
          view.getRolesForm().commit();
          final String description = view.getDescriptionField().getValue();
          if ((description != null) && (!description.equals(currentApp.getDescription())))
          {
            ApplicationsModule.getApplicationPresenter().updateDescription(projectId, currentApp.getUri(),
                description);
          }
          ApplicationsModule.getApplicationPresenter().updateRoleMapping(projectId, currentApp.getUri(),
              roleMapping);

          view.getDescriptionField().setReadOnly(true);
          view.getRolesForm().setReadOnly(true);
          view.getHeaderLayout().setVisible(true);
          view.getFooter().setVisible(false);

        }
        catch (final ApplicationServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e,
              view.getLocale());
        }
        catch (final InvalidValueException v)
        {
          Notification.show(v.getLocalizedMessage(), Type.WARNING_MESSAGE);
        }
        catch (final Exception e)
        {
          // Ignored, we'll let the Form handle the errors
        }

      }
    });
    view.getDeleteWindow().getYesButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 2756645480332651962L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {

        try
        {
          ApplicationsModule.getApplicationPresenter().removeApplication(projectId, currentApp.getUri());
          getEventBus().publish(new RefreshTreeEvent(parentSpace, getUuid()));

        }
        catch (final ApplicationServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e,
              view.getLocale());

        }

      }
    });
    view.getDelButton().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3893425584628753368L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().addWindow(view.getDeleteWindow());
      }
    });

  }

  /**
   * This method will refresh the {@link ApplicationsView} associated to this presenter.
   * 
   * @param pSpace
   *          the {@link Space} parent
   * @param pApp
   *          the {@link ProjectApplication} to display
   * @param readOnly
   *          the mode used to display info
   */
  public void refresh(final Space pSpace, final ProjectApplication pApp, final boolean readOnly)
  {
    parentSpace = pSpace;
    currentApp = pApp;
    view.getRolesForm().setReadOnly(false);
    view.getApplicationForm().setReadOnly(false);
    try
    {
      final String pluginUUID = currentApp.getPluginUUID().toString();
      final PluginMetadata metadata = ApplicationsModule.getPluginsManager().getPluginMetadataByUUID(
          pluginUUID);

      final CategoryDefinitionService categoryService = ApplicationsModule.getPluginsCategoryManager()
          .getCategoryService(metadata.getCategory());
      if (categoryService != null)
      {
        final String localizedName = categoryService.getName(view.getLocale());
        view.getCategoryField().setValue(localizedName);
      }
      view.getNameField().setValue(currentApp.getName());
      view.getDescriptionField().setValue(currentApp.getDescription());
      view.getTypeField().setValue(metadata.getType());

      final boolean availablePlugin = ApplicationsModule.getPluginsManager().isAvailablePlugin(pluginUUID);
      final boolean active = ApplicationStatus.ACTIVE.equals(pApp.getStatus());
      final boolean onDelete = ApplicationStatus.DELETE_IN_PROGRESS.equals(pApp.getStatus())
          || ApplicationStatus.DELETE_ON_ERROR.equals(pApp.getStatus());
      final boolean isSuperAdmin = ApplicationsModule.getMembershipPresenter().isCurrentSuperAdmin();
      setInformation(availablePlugin, view.getLocale());
      view.getDelButton().setEnabled(availablePlugin && (isSuperAdmin || (!onDelete)));
      view.getRolesForm().setVisible(availablePlugin && active);
      view.getInformationLabel().setVisible((!active) || (!availablePlugin));
      view.getEditButton().setEnabled(availablePlugin && active);
      view.getLinkButton().setEnabled(!onDelete);

      if (availablePlugin && active)
      {
        initRoleMappingForm(pluginUUID);
      }
    }
    catch (final PluginManagerException | ProjectServiceException | PluginServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());
    }
    view.refreshLocale(view.getLocale());
    view.getRolesForm().setReadOnly(readOnly);
    view.getApplicationForm().setReadOnly(true);
    view.getDescriptionField().setReadOnly(readOnly);
    view.getHeaderLayout().setVisible(readOnly);
    view.getFooter().setVisible(!readOnly);
  }

  private void setInformation(final boolean pActive, final Locale pLocale)
  {
    if (!pActive)
    {
      view.getInformationLabel().setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                            Messages.APPLICATIONS_APPLICATION_UNAVAILABLE));
      view.getInformationLabel().setStyleName(NovaForge.LABEL_RED);
      view.getInformationLabel().addStyleName(NovaForge.LABEL_BOLD);
    }
    else
    {
      if (ApplicationStatus.CREATE_IN_PROGRESS.equals(currentApp.getStatus()))
      {
        view.getInformationLabel().setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                              Messages.APPLICATIONS_APPLICATION_CREATE_IN_PROGRESS));
        view.getInformationLabel().setStyleName(NovaForge.LABEL_ORANGE);
        view.getInformationLabel().addStyleName(NovaForge.LABEL_BOLD);

      }
      else if (ApplicationStatus.CREATE_ON_ERROR.equals(currentApp.getStatus()))
      {
        view.getInformationLabel().setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                              Messages.APPLICATIONS_APPLICATION_CREATE_ERROR));
        view.getInformationLabel().setStyleName(NovaForge.LABEL_RED);
        view.getInformationLabel().addStyleName(NovaForge.LABEL_BOLD);

      }
      else if (ApplicationStatus.DELETE_IN_PROGRESS.equals(currentApp.getStatus()))
      {
        view.getInformationLabel().setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                              Messages.APPLICATIONS_APPLICATION_DELETE_IN_PROGRESS));
        view.getInformationLabel().setStyleName(NovaForge.LABEL_GREY);
        view.getInformationLabel().addStyleName(NovaForge.LABEL_BOLD);

      }
      else if (ApplicationStatus.DELETE_ON_ERROR.equals(currentApp.getStatus()))
      {
        view.getInformationLabel().setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                              Messages.APPLICATIONS_APPLICATION_DELETE_ERROR));
        view.getInformationLabel().setStyleName(NovaForge.LABEL_RED);
        view.getInformationLabel().addStyleName(NovaForge.LABEL_BOLD);

      }
      else if (ApplicationStatus.PROVIDING_PENDING.equals(currentApp.getStatus()))
      {
        view.getInformationLabel().setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                              Messages.APPLICATIONS_APPLICATION_PENDING));
        view.getInformationLabel().setStyleName(NovaForge.LABEL_ORANGE);
        view.getInformationLabel().addStyleName(NovaForge.LABEL_BOLD);
      }
    }
  }

  private void initRoleMappingForm(final String pluginUUID) throws ProjectServiceException,
      PluginManagerException, PluginServiceException
  {
    final List<ProjectRole> allRoles = ApplicationsModule.getProjectRolePresenter().getAllRoles(projectId);
    final PluginService pluginService = ApplicationsModule.getPluginsManager().getPluginService(pluginUUID);
    final Map<String, String> rolesMapping = pluginService.getRolesMapping(currentApp.getPluginInstanceUUID()
        .toString());
    final Set<String> pluginRole = pluginService.findRoles();
    view.getRolesForm().removeAllProperties();
    for (final ProjectRole projectRole : allRoles)
    {
      final String projectRoleName = projectRole.getName();
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
          final String roleName = (String) pEvent.getProperty().getValue();
          if (roleName != null)
          {
            roleMapping.put(projectRoleName, roleName);
          }
          else
          {
            roleMapping.remove(projectRoleName);
          }
        }
      });
      roleBox.setInputPrompt(ApplicationsModule.getPortalMessages().getMessage(view.getLocale(),
          Messages.APPLICATIONS_APPLICATION_ROLES_NONE));
      final boolean isAdminRole = isAdminRole(projectRole.getName());
      if (isAdminRole)
      {
        roleBox.setRequired(true);
        roleBox.setRequiredError(ApplicationsModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.APPLICATIONS_APPLICATION_ROLES_REQUIRED));
      }
      roleBox.setTextInputAllowed(!isAdminRole);
      roleBox.setNullSelectionAllowed(!isAdminRole);
      roleBox.setCaption(projectRoleName);
      for (final String pluginRoleName : pluginRole)
      {
        roleBox.addItem(pluginRoleName);
      }
      final String roleMapped = rolesMapping.get(projectRoleName);
      roleBox.setValue(roleMapped);
      view.getRolesForm().addField(projectRoleName, roleBox);
      if (roleMapped != null)
      {
        roleMapping.put(projectRoleName, roleMapped);
      }
    }
  }

  /**
   * Check if role name is either a admin or super admin role
   */
  private boolean isAdminRole(final String pRoleName)
  {
    final String administratorRoleName = ApplicationsModule.getForgeConfigurationService()
        .getForgeAdministratorRoleName();
    final String superAdministratorRoleName = ApplicationsModule.getForgeConfigurationService()
        .getForgeSuperAdministratorRoleName();
    return administratorRoleName.equals(pRoleName) || superAdministratorRoleName.equals(pRoleName);

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
    if (currentApp != null)
    {
      final boolean availablePlugin = ApplicationsModule.getPluginsManager().isAvailablePlugin(
          currentApp.getPluginUUID().toString());
      setInformation(availablePlugin, pLocale);
      view.refreshLocale(pLocale);
    }
  }

  /**
   * @return the currentApp
   */
  public ProjectApplication getCurrentApp()
  {
    return currentApp;
  }

  /**
   * @return the parentSpace
   */
  public Space getParentSpace()
  {
    return parentSpace;
  }

}
