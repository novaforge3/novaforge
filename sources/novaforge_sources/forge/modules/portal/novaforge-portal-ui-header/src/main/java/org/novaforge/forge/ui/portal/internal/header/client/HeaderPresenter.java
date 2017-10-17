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
package org.novaforge.forge.ui.portal.internal.header.client;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.UI;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.services.PortalService;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.AuthentificationChangedEvent;
import org.novaforge.forge.ui.portal.event.AuthentificationChangedEvent.Status;
import org.novaforge.forge.ui.portal.event.OpenInternalAppEvent;
import org.novaforge.forge.ui.portal.event.OpenProjectEvent;
import org.novaforge.forge.ui.portal.event.ProjectUpdateEvent;
import org.novaforge.forge.ui.portal.event.ProjectValidatedEvent;
import org.novaforge.forge.ui.portal.event.UserUpdateEvent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.portal.i18n.LocaleChangedEvent;
import org.novaforge.forge.ui.portal.internal.header.client.components.OpenExternalAppCommand;
import org.novaforge.forge.ui.portal.internal.header.client.components.OpenInternalAppCommand;
import org.novaforge.forge.ui.portal.internal.header.client.components.ProjectItemProperty;
import org.novaforge.forge.ui.portal.internal.header.client.components.ProjectsContainer;
import org.novaforge.forge.ui.portal.internal.header.module.HeaderModule;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class HeaderPresenter extends AbstractPortalPresenter implements Serializable, PortalComponent
{
  /**
   * Serial version id used for serialization
   */
  private static final long       serialVersionUID = -7391151601368829434L;
  /**
   * Logger
   */
  private static final Log LOG = LogFactory.getLog(HeaderPresenter.class);
  /**
   * View associated to this presenter
   */
  private final HeaderView        view;
  /**
   * Contains projects list
   */
  private final ProjectsContainer projects;
  /**
   * Refere to the request project application
   */
  private       PortalApplication linkFromId;
  private       MenuBar.MenuItem  accountItem;
  private Status status = Status.UNAUTHENTIFICATED;

  /**
   * Default constructor
   *
   * @param pView
   *          the view associated to this presenter
   * @param pPortalContext
   *          initial context
   */
  public HeaderPresenter(final HeaderView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // Init the view
    view = pView;
    status = Status.valueOf(pPortalContext.getAttributes().get(PortalContext.KEY.AUTH_STATUS));

    // init container
    projects = new ProjectsContainer();
    view.getProjectCombo().setContainerDataSource(projects);

    // Sets the combobox to show a certain property as the item caption
    view.getProjectCombo().setItemCaptionPropertyId(ProjectItemProperty.NAME.getPropertyId());
    view.getProjectCombo().setItemCaptionMode(ItemCaptionMode.PROPERTY);

    // Sets the icon to use with the items
    view.getProjectCombo().setItemIconPropertyId(ProjectItemProperty.ICON.getPropertyId());

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {

    view.getProjectCombo().addShortcutListener(new ComboBox.FocusShortcut(view.getProjectCombo(), KeyCode.P,
                                                                          ModifierKey.ALT, ModifierKey.SHIFT));
    view.getProjectCombo().addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id used for serilization
       */
      private static final long serialVersionUID = -6989264226334537075L;

      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        final Property<?> selected = projects.getContainerProperty(pEvent.getProperty().getValue(),
                                                                   ProjectItemProperty.ID.getPropertyId());
        if ((selected != null) && (selected.getValue() != null))
        {
          getEventBus().publish(new OpenProjectEvent(selected.getValue().toString()));
          view.getProjectCombo().setValue(null);
        }
      }
    });
    view.getOtherProject().addClickListener(new Button.ClickListener()
    {

      /**
       * Serial version id for serialization
       */
      private static final long serialVersionUID = -6755017750747746429L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        if ((view.getOtherProject().isEnabled()) && (view.getOtherProject().isVisible()) && (linkFromId != null))
        {
          getEventBus().publish(new OpenInternalAppEvent(linkFromId));
        }

      }
    });
    view.getEnButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id for serialization
       */
      private static final long serialVersionUID = -5470256602969236003L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publishAsync(new LocaleChangedEvent(Locale.ENGLISH));

      }
    });
    view.getFrButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id for serialization
       */
      private static final long serialVersionUID = -5470256602969236003L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publishAsync(new LocaleChangedEvent(Locale.FRENCH));

      }
    });

  }

  /**
   * @return the view
   */
  @Override
  public HeaderView getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    refreshLocalized(getCurrentLocale());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);
    refreshAuthenticated(pLocale);

  }

  /**
   * Method call when a {@link AuthentificationChangedEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void onAuthenticationChange(final AuthentificationChangedEvent pEvent)
  {
    if (!status.equals(pEvent.getStatus()))
    {
      status = pEvent.getStatus();

      refreshAuthenticated(getCurrentLocale());
    }

  }

  /**
   * Refresh contents which need to be authentificated
   */
  private void refreshAuthenticated(final Locale pLocale)
  {
    final boolean visible = AuthentificationChangedEvent.Status.AUTHENTIFICATED.equals(status);

    view.getProjectsLayout().setVisible(visible);
    view.getProjectCombo().setVisible(visible);
    view.getOtherProject().setVisible(visible);
    view.getAccountMenu().setVisible(visible);
    view.getAccountIcon().setVisible(visible);
    view.getLanguagesLayout().setVisible(!visible);
    if (visible)
    {
      try
      {
        initProjects(pLocale);
        initAccountMenu(pLocale);
      }
      catch (final Exception e)
      {
        LOG.error("Unable to refresh authentification status", e);
      }
    }
  }

  /**
   * This method will init projects elements
   *
   * @throws PortalException
   */
  private void initProjects(final Locale pLocale) throws PortalException
  {
    final List<Project> userProjects = HeaderModule.getPortalService().getUserProjects();
    projects.setProjects(userProjects);

    linkFromId = HeaderModule.getPortalService().getLinkFromId(PortalModuleId.PUBLIC_PROJECT, pLocale);
    if (linkFromId != null)
    {
      view.getOtherProject().setCaption(linkFromId.getName());
      view.getOtherProject().setDescription(linkFromId.getDescription());
      view.getOtherProject().setIcon(linkFromId.getIcon());
    }

  }

  /**
   * will initialize the account menu
   *
   * @throws PortalException
   *           if the application can not be got from {@link PortalService} service
   * @throws UserServiceException
   *           if current user profile cannot be retrieved
   */
  private void initAccountMenu(final Locale pLocale) throws PortalException, UserServiceException
  {
    view.getAccountMenu().removeItems();
    final PortalSpace accountSpaces = HeaderModule.getPortalService().getAccountSpaces(pLocale);
    if (accountSpaces != null)
    {
      final UserProfile currentUserProfile = HeaderModule.getUserPresenter().getUserProfile(
          HeaderModule.getAuthentificationService().getCurrentUser());
      if (currentUserProfile.getImage() != null)
      {
        view.getAccountIcon().setSource(
            ResourceUtils.buildImageResource(currentUserProfile.getImage().getFile(), currentUserProfile
                .getImage().getName()));
      }
      else
      {
        view.getAccountIcon().setSource(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
      }
      accountItem = view.getAccountMenu().addItem(accountSpaces.getName(),
          new ThemeResource(NovaForgeResources.ICON_ARROW_BOTTOM3), null);
      accountItem.setStyleName(NovaForge.MENUBAR_LIGHT);
      for (final PortalApplication app : accountSpaces.getApplications())
      {
        if (app.getId().equals(PortalModuleId.LOGOUT.getId()))
        {
          accountItem.addSeparator();
        }
        Command command;

        if (app.getPortalURI().isInternalModule())
        {
          command = new OpenInternalAppCommand(getEventBus(), app);
        }
        else
        {
          command = new OpenExternalAppCommand(getEventBus(), app);
        }
        accountItem.addItem(app.getName(), app.getIcon(), command);
      }
    }
  }

  /**
   * Method call when a {@link ProjectRefreshEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void onRefreshProject(final ProjectUpdateEvent pEvent)
  {
    final String projectIdEvent = pEvent.getProject().getProjectId();
    if (projects.containsId(projectIdEvent))
    {
      projects.getContainerProperty(projectIdEvent, ProjectItemProperty.NAME.getPropertyId()).setValue(pEvent
                                                                                                           .getProject()
                                                                                                           .getName());

      if ((pEvent.getProject().getImage() != null) && (pEvent.getProject().getImage().getFile() != null))
      {
        final StreamResource buildImageResource = ResourceUtils.buildImageResource(pEvent.getProject().getImage()
                                                                                         .getFile(),
                                                                                   pEvent.getProject().getName());
        buildImageResource.setMIMEType(pEvent.getProject().getImage().getMimeType());
        projects.getContainerProperty(projectIdEvent, ProjectItemProperty.ICON.getPropertyId())
                .setValue(buildImageResource);
      }
    }
  }

  /**
   * Method call when a {@link ProjectValidatedEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void onValidateProject(final ProjectValidatedEvent pEvent)
  {
    if (!projects.containsId(pEvent.getProjectId()))
    {
      try
      {
        initProjects(getCurrentLocale());
      }
      catch (final PortalException e)
      {
        LOG.error("Unable to header with new projects available.", e);

      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    refreshContent();
  }

  /**
   * Method call when a {@link UserUpdateEvent} is received
   * 
   * @param pEvent
   *          the source event
   */
  @Handler
  public void onUpdateUser(final UserUpdateEvent pEvent)
  {
    final Locale eventLocale = pEvent.getLocale();
    if (!eventLocale.getLanguage().equals(view.getLocale().getLanguage()))
    {
      UI.getCurrent().setLocale(eventLocale);
      getEventBus().publishAsync(new LocaleChangedEvent(eventLocale));

      TrayNotification notification = new TrayNotification(HeaderModule.getPortalMessages().getMessage(
          eventLocale, Messages.WARNING_TITLE), HeaderModule.getPortalMessages().getMessage(eventLocale,
          Messages.LOCALE_REFRESH), TrayNotificationType.WARNING);
      notification.show(Page.getCurrent());
    }
    try
    {
      if (accountItem != null)
      {
        accountItem.setText(buildItemCaption(pEvent.getName(), pEvent.getFirstName()));
        if (pEvent.getPicture())
        {
          final UserProfile userProfile = HeaderModule.getUserPresenter().getUserProfile(pEvent.getLogin());
          if (userProfile.getImage() != null)
          {
            view.getAccountIcon().setSource(
                ResourceUtils.buildImageResource(userProfile.getImage().getFile(), userProfile.getImage()
                    .getName()));
          }
          else
          {
            view.getAccountIcon().setSource(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
          }
        }
      }
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(HeaderModule.getPortalMessages(), e, getCurrentLocale());
    }

  }

  /**
   * Build the account menu item caption from user name and firstname
   * 
   * @param pName
   *          user name
   * @param pFirstName
   *          user first name
   * @return item menu caption
   */
  private String buildItemCaption(final String pName, final String pFirstName)
  {
    return pFirstName + " " + pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return HeaderModule.getPortalModuleId();
  }

}
