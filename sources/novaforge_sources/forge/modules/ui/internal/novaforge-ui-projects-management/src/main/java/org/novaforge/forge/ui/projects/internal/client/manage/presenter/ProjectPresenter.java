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
package org.novaforge.forge.ui.projects.internal.client.manage.presenter;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.OrganizationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Organization;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.ProjectCreatedEvent;
import org.novaforge.forge.ui.portal.event.ProjectUpdateEvent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.projects.internal.client.manage.view.ProjectFieldFactory;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;
import org.novaforge.forge.ui.projects.internal.module.create.CreateProjectModule;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * The presenter used edit project info
 */
public class ProjectPresenter extends AbstractPortalPresenter implements Serializable
{

  /**
   * 
   */
  private static final long  serialVersionUID = 7849007242425598955L;
  private static final Log LOGGER = LogFactory.getLog(ProjectPresenter.class);
  /**
   * Content the view
   */
  private final ProjectView  view;
  private final List<String> projectFieldOrder;
  /**
   * The current "POJO" manipulated by the form
   */
  private Project            currentProject;

  /**
   * Default constructor
   * 
   * @param pView
   *          The {@link ProjectView} to associate
   * @param pPortalContext
   *          the initial context
   */
  public ProjectPresenter(final ProjectView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();

    projectFieldOrder = new LinkedList<>();
    projectFieldOrder.add(ProjectFieldFactory.NAME_FIELD);
    projectFieldOrder.add(ProjectFieldFactory.ELEMENT_ID);
    projectFieldOrder.add(ProjectFieldFactory.DESCRIPTION_FIELD);
    projectFieldOrder.add(ProjectFieldFactory.PRIVATE_FIELD);
  }

  /**
   * Add listeners to the view elements
   */
  private void addListeners()
  {
    view.getApplyButton().addClickListener(new ClickListener()
    {

      /**
       * Serial id
       */
      private static final long serialVersionUID = -4726048120711658597L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        final String oldName = currentProject.getName();
        try
        {
          view.getProjectForm().commit();
          // Setting license
          final String license = (String) view.getLicenses().getValue();
          currentProject.setLicenceType(license);

          // Setting organism
          final Organization organization = (Organization) view.getOrganism().getValue();
          if (organization != null)
          {
            currentProject.setOrganization(organization);
          }
          // Get image if one has been chooseen
          if (view.getImageUpload().getValue() != null)
          {
            final byte[] value = (byte[]) view.getImageUpload().getValue();
            if (value.length > 0)
            {
              currentProject.getImage().setFile(value);
              currentProject.getImage().setMimeType(view.getImageUpload().getMimeType());
            }
          }
          if (view.isCreateMode())
          {
            // Get template information
            final Template template = (Template) view.getTemplates().getValue();
            String templateId = null;
            if (template != null)
            {
              templateId = template.getElementId();
            }
            ProjectServices.getProjectPresenter().createProjectFromTemplate(currentProject, templateId);

            getEventBus().publish(new ProjectCreatedEvent(currentProject, template));
          }
          else
          {
            ProjectServices.getProjectPresenter().updateProject(oldName, currentProject);
            getEventBus().publish(new ProjectUpdateEvent(currentProject));
          }
        }
        catch (final InvalidValueException v)
        {
          // Ignored, we'll let the Form handle the errors
          currentProject.setName(oldName);
          TrayNotification.show(v.getLocalizedMessage(), TrayNotificationType.WARNING);
        }
        catch (final Exception e)
        {
          ExceptionCodeHandler.showNotificationError(ProjectServices.getPortalMessages(), e, view.getLocale());
          currentProject.setName(oldName);
        }
      }
    });
    view.getIconConfirmButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 4972032507461947475L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        final byte[] value = (byte[]) view.getImageUpload().getValue();
        if ((value != null) && (value.length > 0))
        {
          updateProjectIcon(value);
          view.getImageUpload().discard();
          UI.getCurrent().removeWindow(view.getUpdatePictureWindow());
        }
        else
        {
          final Locale locale = getCurrentLocale();
          Notification.show(
              ProjectServices.getPortalMessages().getMessage(locale, Messages.COMPONENT_IMAGE_ERROR_NULL),
              Type.WARNING_MESSAGE);
        }
      }

    });
    view.getIconDeleteButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -7861649120755681975L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        setDefaultIcon();
        if (!view.isCreateMode())
        {
          final byte[] defaultIcon = ProjectServices.getForgeConfigurationService().getDefaultIcon();
          updateProjectIcon(defaultIcon);
        }
      }
    });
  }

  /**
   * Will update the current project icon from the value given
   *
   * @param value
   */
  private void updateProjectIcon(final byte[] value)
  {
    view.getProjectIcon().setSource(ResourceUtils.buildImageResource(value, UUID.randomUUID().toString()));

    currentProject.getImage().setFile(value);
    currentProject.getImage().setMimeType(view.getImageUpload().getMimeType());
    if (!view.isCreateMode())
    {
      try
      {
        ProjectServices.getProjectPresenter().updateProject(currentProject.getName(), currentProject);
        getEventBus().publish(new ProjectUpdateEvent(currentProject));
      }
      catch (final ProjectServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(ProjectServices.getPortalMessages(), e, view.getLocale());
      }
    }
  }

  private void setDefaultIcon()
  {
    final byte[] defaultIcon = ProjectServices.getForgeConfigurationService().getDefaultIcon();
    if (defaultIcon != null)
    {
      view.getProjectIcon().setSource(ResourceUtils.buildImageResource(defaultIcon, "default"));

    }
  }

  /**
   * Refresh the presenter with the given {@link Project}
   *
   * @param pProject
   *          the {@link Project} to display
   */
  public void refresh(final Project pProject)
  {
    if (view.getParent() != null)
    {
      currentProject = pProject;

      if (currentProject != null)
      {
        final BeanItem<Project> projectItem = new BeanItem<Project>(currentProject);
        view.getProjectForm().setItemDataSource(projectItem, projectFieldOrder);

        // Initialize external component
        initDefaultIcon();
        initLicenses();
        initOrganism();
        initTemplates();
        addDynamicListener();
      }
      refreshLocalized(view.getLocale());
    }
  }

  private void initDefaultIcon()
  {
    if (currentProject.getImage().getFile() == null)
    {
      setDefaultIcon();
    }
    else
    {
      final byte[] projectIcon = currentProject.getImage().getFile();
      if (projectIcon != null)
      {
        view.getProjectIcon().setSource(
            ResourceUtils.buildImageResource(projectIcon, currentProject.getElementId()));

      }

    }
  }

  /**
   * Initialization licenses data
   */
  private void initLicenses()
  {
    // TODO Got the followings from a specific service.
    view.getLicenses().addItem("Sans licence");
    view.getLicenses().setValue("Sans licence");
    view.getLicenses().addItem("GPL");
    view.getLicenses().addItem("LGPL");

    if (currentProject.getLicenceType() != null)
    {
      view.getLicenses().setValue(currentProject.getLicenceType());

    }
  }

  /**
   * Initialization organism data
   */
  private void initOrganism()
  {
    try
    {
      final List<Organization> allOrganizations = ProjectServices.getOrganizationPresenter()
          .getAllOrganizations();
      for (final Organization organization : allOrganizations)
      {
        view.getOrganism().addItem(organization);
        view.getOrganism().setItemCaption(organization, organization.getName());
      }
    }
    catch (final OrganizationServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ProjectServices.getPortalMessages(), e, view.getLocale());
    }

    if (currentProject.getOrganization() != null)
    {
      view.getOrganism().setValue(currentProject.getOrganization());

    }

  }

  /**
   * Initialization templates
   */
  private void initTemplates()
  {
    if (view.isCreateMode())
    {
      try
      {
        final List<Template> templates = ProjectServices.getTemplatePresenter().getEnableTemplates();
        for (final Template template : templates)
        {
          view.getTemplates().addItem(template);
          view.getTemplates().setItemCaption(template, template.getName());
        }
      }
      catch (final TemplateServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(ProjectServices.getPortalMessages(), e, view.getLocale());
      }
    }
  }

  /**
   * Will init listener for dyanmic field
   */
  private void addDynamicListener()
  {
    if (view.isCreateMode())
    {
      view.getProjectName().addTextChangeListener(new TextChangeListener()
      {
        /**
         * Serialization id
         */
        private static final long serialVersionUID = 1758320425399929758L;

        @Override
        public void textChange(final TextChangeEvent pEvent)
        {
          final String normalized = Normalizer.normalize(pEvent.getText(), Normalizer.Form.NFD);
          String id = normalized.replaceAll("[^\\w]", "");
          if (id.length() > 26)
          {
            id = id.substring(0, 26);
          }
          view.getProjectId().setValue(id.toLowerCase());
        }
      });
    }
    view.getOrganism().setNewItemHandler(new NewItemHandler()
    {
      /**
       * Serialization id
       */
      private static final long serialVersionUID = -1427611550531908722L;

      @Override
      public void addNewItem(final String pNewItemCaption)
      {
        final String newOrgaName = pNewItemCaption.toUpperCase();
        @SuppressWarnings("unchecked")
        final Collection<Organization> itemIds = (Collection<Organization>) view.getOrganism().getItemIds();
        final List<Organization> orgas = new ArrayList<Organization>(itemIds);
        Organization orgaFound = null;
        for (final Organization organization : orgas)
        {
          if (organization.getName().equals(newOrgaName))
          {
            orgaFound = organization;
            break;
          }
        }
        if (orgaFound == null)
        {
          final Organization newOrga = ProjectServices.getOrganizationPresenter().newOrganization();
          newOrga.setName(newOrgaName);
          view.getOrganism().addItem(newOrga);
          view.getOrganism().setItemCaption(newOrga, newOrga.getName());
          view.getOrganism().setValue(newOrga);
        }
        else
        {
          view.getOrganism().setValue(orgaFound);

        }

      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return CreateProjectModule.getPortalModuleId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return getView();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    // Nothing to do here. It is handled by Component associated
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale locale)
  {
    view.refreshLocale(locale);

  }

  /**
   * @return {@link ProjectView}
   */
  public ProjectView getView()
  {
    return view;
  }

}
