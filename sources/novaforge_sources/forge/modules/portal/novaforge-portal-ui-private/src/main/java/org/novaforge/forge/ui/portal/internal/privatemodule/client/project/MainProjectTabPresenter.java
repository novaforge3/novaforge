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
package org.novaforge.forge.ui.portal.internal.privatemodule.client.project;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.models.PortalURI;
import org.novaforge.forge.portal.services.PortalService;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.ApplicationCloseRequestedEvent;
import org.novaforge.forge.ui.portal.event.OpenExternalAppEvent;
import org.novaforge.forge.ui.portal.event.OpenInternalAppEvent;
import org.novaforge.forge.ui.portal.event.OpenPluginViewEvent;
import org.novaforge.forge.ui.portal.event.OpenProjectProfileEvent;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.model.ApplicationTab;
import org.novaforge.forge.ui.portal.internal.privatemodule.module.PrivateModule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This presenter handles project components displayed.
 *
 * @author Guillaume Lamirand
 */
public class MainProjectTabPresenter extends ProjectTabPresenter implements Serializable
{

  /**
   * Serial version uid used for serialization
   */
  private static final long serialVersionUID = -5042299647493799344L;

  /**
   * Logger
   */
  private static final Log LOG = LogFactory.getLog(MainProjectTabPresenter.class);

  /**
   * Default constructor.
   *
   * @param pView
   *     the view attached to this presenter
   * @param pPortalContext
   *     the initial context
   */
  public MainProjectTabPresenter(final ProjectTabView pView, final PortalContext pPortalContext)
  {
    super(pView, pPortalContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<PortalSpace> getProjectNavigation() throws PortalException
  {
    return PrivateModule.getPortalService().getMainNavigation(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refresh(final String pProjectId, final boolean pOpenDefault)
  {
    refresh(pOpenDefault);
  }

  /**
   * Will refresh the main project information.
   *
   * @param pOpenDefault
   *     if true the default tab is added and selected
   *
   * @throws PortalException
   */
  public void refresh(final boolean pOpenDefault)
  {
    final PortalService portalFacade = PrivateModule.getPortalService();
    try
    {

      // Getting project
      final Project forgeProject = portalFacade.getMainProject();
      setProjectId(forgeProject.getElementId());

      // Update navigation
      updateProjectNavigation();

      // Update details
      updateProjectInformation(forgeProject);

      // Open first page tab
      openDefaultApplication(forgeProject.getElementId(), pOpenDefault);
    }
    catch (final PortalException e)
    {
      LOG.error("Unable to update main project information", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalApplication getDefaultApplication(final String pProjectId) throws PortalException
  {
    return PrivateModule.getPortalService().getMainDefaultApplication(getCurrentLocale());

  }

  /**
   * Method call when a {@link OpenExternalAppEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void openExternalApplication(final OpenExternalAppEvent pEvent)
  {
    final PortalApplication application = pEvent.getApplication();
    if (application != null)
    {
      final PortalURI portalURI = application.getPortalURI();
      addEmbeddedTab(application.getUniqueId().toString(), application.getName(), application.getIcon(),
                     portalURI.getAbsoluteURL().toExternalForm());
    }
  }

  /**
   * Method call when a {@link OpenInternalAppEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void openInternalAppEvent(final OpenInternalAppEvent pEvent)
  {

    if ((pEvent != null) && (pEvent.getApplication() != null))
    {
      final PortalApplication application = pEvent.getApplication();
      final PortalModuleId portalModuleId = PortalModuleId.getFromId(application.getId());
      if (portalModuleId != null)
      {
        if (portalModuleId.equals(PortalModuleId.LOGOUT))
        {
          getEventBus().publish(new ApplicationCloseRequestedEvent());
        }
        else
        {
          final PortalURI portalURI = application.getPortalURI();
          addInternalTab(portalURI.getModuleId(), application.getUniqueId().toString(), application.getName(),
                         application.getIcon(), true);
        }
      }
    }
  }

  /**
   * Method call when a {@link OpenPluginViewEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void onOpenPluginViewEvent(final OpenPluginViewEvent pEvent)
  {
    try
    {
      final PortalApplication application = PrivateModule.getPortalService().getPortalApplication(pEvent
                                                                                                      .getPluginuuid(),
                                                                                                  pEvent.getToolUUID(),
                                                                                                  null, null,
                                                                                                  pEvent.getView());
      addApplicationTab(application, application.getName(), true);
    }
    catch (final PortalException e)
    {
      LOG.error(String.format("Unable to get application informations with [plugin_id=%s,tool_id=%s,view=%s]",
                              pEvent.getPluginuuid(), pEvent.getToolUUID(), pEvent.getView()), e);

      ExceptionCodeHandler.showNotificationError(PrivateModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Method call when a {@link OpenProjectProfileEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void onOpenProjectProfileEvent(final OpenProjectProfileEvent pEvent)
  {
    final Project project = pEvent.getProject();
    if (project != null)
    {
      final String moduleId = PortalModuleId.PUBLIC_PROJECT.getId();
      final PortalModule module = PrivateModule.getPortalModuleService().getModule(moduleId);
      final Map<PortalContext.KEY, String> attributes = new HashMap<PortalContext.KEY, String>();
      attributes.put(PortalContext.KEY.PROJECTID, project.getProjectId());
      final PortalContext buildContext = PrivateModule.getPortalModuleService().buildContext(getEventBus(),
                                                                                             getCurrentLocale(),
                                                                                             attributes);
      if (module != null)
      {
        final PortalComponent createComponent = module.createComponent(buildContext);
        final Component component = createComponent.getComponent();
        final String title = PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                          Messages.PUBLIC_PROJECT_PROFILE_TAB_TITLE,
                                                                          project.getName());
        final Resource icon = ResourceUtils.buildImageResource(project.getImage().getFile(), project.getName());

        // create only the tab if it doesn't already exists
        if (!getTabMapping().containsKey(project.getProjectId()))
        {
          final ApplicationTab portalTab = new ApplicationTab(buildContext.getUuid(), moduleId, createComponent);
          getTabMapping().put(project.getProjectId(), portalTab);
        }

        final boolean addTab = addTab(project.getProjectId(), title, icon, component, true);
        if (addTab)
        {
          createComponent.init();
        }
      }

    }
  }

  /**
   * Method call when a {@link OpenUserProfileEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void onOpenUserProfileEvent(final OpenUserProfileEvent pEvent)
  {
    final UserProfile userProfile = pEvent.getUserProfile();
    if (userProfile != null)
    {
      final User user = userProfile.getUser();
      final String moduleId = PortalModuleId.USERMANAGEMENT_PUBLIC.getId();
      final PortalModule module = PrivateModule.getPortalModuleService().getModule(moduleId);
      final Map<PortalContext.KEY, String> attributes = new HashMap<PortalContext.KEY, String>();
      attributes.put(PortalContext.KEY.LOGIN, user.getLogin());
      final PortalContext buildContext = PrivateModule.getPortalModuleService().buildContext(getEventBus(),
                                                                                             getView().getLocale(),
                                                                                             attributes);
      if (module != null)
      {
        final PortalComponent createComponent = module.createComponent(buildContext);
        final Component component = createComponent.getComponent();
        final String title = PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                          Messages.USERMANAGEMENT_PROFILE_TAB_TITLE,
                                                                          user.getFirstName(), user.getName());
        Resource icon;
        if (userProfile.getImage() != null)
        {
          icon = ResourceUtils.buildImageResource(userProfile.getImage().getFile(), userProfile.getImage().getName());
        }
        else
        {
          icon = new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW);
        }

        // create only the tab if it doesn't already exists
        if (!getTabMapping().containsKey(user.getLogin()))
        {
          final ApplicationTab portalTab = new ApplicationTab(buildContext.getUuid(), moduleId, createComponent);
          getTabMapping().put(user.getLogin(), portalTab);
        }

        final boolean addTab = addTab(user.getLogin(), title, icon, component, true);
        if (addTab)
        {
          createComponent.init();
        }
      }

    }
  }
}
