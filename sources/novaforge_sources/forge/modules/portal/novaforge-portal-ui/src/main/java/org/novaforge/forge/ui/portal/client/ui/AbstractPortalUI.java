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
package org.novaforge.forge.ui.portal.client.ui;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.common.Properties;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalContext.KEY;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalModuleListener;
import org.novaforge.forge.portal.services.PortalModuleService;
import org.novaforge.forge.ui.portal.client.view.PortalLayout;
import org.novaforge.forge.ui.portal.event.ApplicationCloseRequestedEvent;
import org.novaforge.forge.ui.portal.event.AuthentificationChangedEvent;
import org.novaforge.forge.ui.portal.event.AuthentificationChangedEvent.Status;
import org.novaforge.forge.ui.portal.event.LoginRequestedEvent;
import org.novaforge.forge.ui.portal.event.ModuleRefreshRequestedEvent;
import org.novaforge.forge.ui.portal.event.PortalModuleRegisteredEvent;
import org.novaforge.forge.ui.portal.event.PortalModuleUnRegisteredEvent;
import org.novaforge.forge.ui.portal.event.UIClosedEvent;
import org.novaforge.forge.ui.portal.event.UIRefreshedEvent;
import org.novaforge.forge.ui.portal.i18n.LocaleChangedEvent;
import org.novaforge.forge.ui.portal.i18n.LocaleChangedListener;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;
import org.novaforge.forge.ui.portal.shared.PortalContentUri;
import org.novaforge.forge.ui.portal.shared.PortalUI;
import org.vaadin.risto.formsender.FormSenderBuilder;
import org.vaadin.risto.formsender.widgetset.client.shared.Method;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This abstract class for {@link UI} contains default behaviour for a Portal UI
 *
 * @author Guillaume Lamirand
 */
@PreserveOnRefresh
public abstract class AbstractPortalUI extends UI implements PortalModuleListener, LocaleChangedListener, PortalUI
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -7764280046700991233L;

  /**
   * Logger component
   */
  private static final Log                   LOGGER       = LogFactory.getLog(AbstractPortalUI.class);
  /**
   * Contains list of {@link PortalComponent} which need to be refresh on the next request. This is needed to
   * get session
   */
  protected final      List<PortalComponent> modulesQueue = new ArrayList<>();
  /**
   * Represents the portal layout
   */
  protected PortalLayout           portalLayout;
  /**
   * Contains {@link MBassador} event bus shared between modules
   */
  protected MBassador<PortalEvent> eventBus;
  protected Status authStatus = Status.UNAUTHENTIFICATED;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void init(final VaadinRequest request)
  {
    // Set event bus
    eventBus = new MBassador<>(new BusConfiguration().addFeature(Feature.SyncPubSub.Default())
                                                     .addFeature(Feature.AsynchronousHandlerInvocation.Default())
                                                     .addFeature(Feature.AsynchronousMessageDispatch.Default())
                                                     .setProperty(Properties.Common.Id, "PortalEventBus")
                                                     .setProperty(Properties.Handler.PublicationError,
                                                                  new IPublicationErrorHandler.ConsoleLogger()));
    eventBus.subscribe(this);

    // Set user's locale
    Locale locale = OSGiHelper.getPortalService().getCurrentUserLocale();
    if (locale == null)
    {
      final WebBrowser context = Page.getCurrent().getWebBrowser();
      locale = context.getLocale();
    }
    setLocale(locale);

    // Set current authentication status
    final boolean checkLogin = OSGiHelper.getAuthentificationService().checkLogin();
    if ((checkLogin) && (Status.UNAUTHENTIFICATED.equals(authStatus)))
    {
      authStatus = Status.AUTHENTIFICATED;
    }
    else if ((!checkLogin) && (Status.AUTHENTIFICATED.equals(authStatus)))
    {
      authStatus = Status.UNAUTHENTIFICATED;
    }

    // Register a PortalModuleListener if needed or close unregister module
    if (!org.novaforge.forge.ui.portal.osgi.OSGiHelper.getPortalModuleService().existListener(this))
    {
      OSGiHelper.getPortalModuleService().addListener(this);
    }

    // Set page title
    Page.getCurrent().setTitle(OSGiHelper.getPortalMessages().getMessage(locale, Messages.PORTAL_TITLE));

    // Init tooltip configuration
    getTooltipConfiguration().setOpenDelay(900);
    getTooltipConfiguration().setQuickOpenDelay(400);
    getTooltipConfiguration().setQuickOpenTimeout(1500);

    // Initialize the portal layout
    initializePortalLayout();

    // Set the main content
    setPortalContent();
  }

  /**
   * Will initialize all presenter and their associated views
   */
  private void initializePortalLayout()
  {
    portalLayout = new PortalLayout();

    // Set the portal layout
    setContent(portalLayout);

    // Build module context
    initializePortalModules();

  }

  /**
   * This is used to set content of the {@link PortalLayout}
   */
  protected abstract void setPortalContent();

  private void initializePortalModules()
  {
    final Map<PortalContext.KEY, String> map = new HashMap<>();
    map.put(PortalContext.KEY.AUTH_STATUS, authStatus.name());

    // Initialize and add the header content
    final PortalContext   headerContext = OSGiHelper.getPortalModuleService().buildContext(eventBus, getLocale(), map);
    final PortalModule    headerModule  = OSGiHelper.getPortalModuleService().getModule(PortalModuleId.HEADER.getId());
    final PortalComponent header        = headerModule.createComponent(headerContext);
    portalLayout.addHeader(header.getComponent());
    header.init();

    // Initialize and add the footer content
    map.put(PortalContext.KEY.SMALL, Boolean.TRUE.toString());
    final PortalContext   footerContext = OSGiHelper.getPortalModuleService().buildContext(eventBus, getLocale(), map);
    final PortalModule    footerModule  = OSGiHelper.getPortalModuleService().getModule(PortalModuleId.FOOTER.getId());
    final PortalComponent footer        = footerModule.createComponent(footerContext);
    portalLayout.addFooter(footer.getComponent());
    footer.init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refresh(final VaadinRequest request)
  {
    // Register a PortalModuleListener if needed or close unregistered module
    if (OSGiHelper.getPortalModuleService().existListener(this))
    {
      eventBus.publishAsync(new UIRefreshedEvent());
    }
    else
    {
      OSGiHelper.getPortalModuleService().addListener(this);
    }
    final boolean checkLogin = OSGiHelper.getAuthentificationService().checkLogin();
    if ((checkLogin) && (Status.UNAUTHENTIFICATED.equals(authStatus)))
    {
      authStatus = Status.AUTHENTIFICATED;
      eventBus.publishAsync(new AuthentificationChangedEvent(authStatus));

      final Locale locale = OSGiHelper.getPortalService().getCurrentUserLocale();
      if (!getLocale().getDisplayLanguage().equals(locale.getDisplayLanguage()))
      {
        setLocale(locale);
        eventBus.publishAsync(new LocaleChangedEvent(locale));
      }
    }
    else if ((!checkLogin) && (Status.AUTHENTIFICATED.equals(authStatus)))
    {
      authStatus = Status.UNAUTHENTIFICATED;
      eventBus.publishAsync(new AuthentificationChangedEvent(authStatus));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detach()
  {
    final PortalModuleService portalModuleService = OSGiHelper.getPortalModuleService();
    if ((portalModuleService != null) && (portalModuleService.existListener(this)))
    {
      portalModuleService.removeListener(this);
    }
    if (eventBus != null)
    {
      eventBus.publish(new UIClosedEvent());
      eventBus.shutdown();
    }

    super.detach();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleModulesQueue()
  {
    if (modulesQueue != null)
    {
      for (final PortalComponent component : modulesQueue)
      {
        if (component.getComponent().isConnectorEnabled())
        {
          component.init();
        }
        else
        {
          try
          {
            eventBus.unsubscribe(component);
          }
          catch (final IllegalArgumentException e)
          {
            LOGGER.warn("An internal module has been refreshed but it cannot be unregistered from current eventBus");
          }
        }
      }
      modulesQueue.clear();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Handler
  public void onLocaleChanged(final LocaleChangedEvent pEvent)
  {
    setLocale(pEvent.getLocale());
  }

  /**
   * Execute the login stuff on {@link LoginRequestedEvent}
   *
   * @param pLoginRequestedEvent
   *     source event
   */
  @Handler
  public void onLoginRequested(final LoginRequestedEvent pLoginRequestedEvent)
  {
    final URL forgeUrl = OSGiHelper.getForgeConfigurationService().getPublicUrl();
    final String servletContext = OSGiHelper.getForgeConfigurationService().getPortalEntryPoint();
    final String formAction = OSGiHelper.getCasSecurityUrl().getCasLogin(forgeUrl.toString() + servletContext
                                                                             + PortalContentUri.SHIRO_CAS.getUri());
    final String formTarget = "_self";
    final Map<String, String> params = new HashMap<>();
    params.put("login", pLoginRequestedEvent.getLogin());
    params.put("pwd", pLoginRequestedEvent.getPwd());
    params.put("auto", Boolean.TRUE.toString());
    params.put("renew", Boolean.TRUE.toString());

    FormSenderBuilder.create().withUI(getUI()).withAction(formAction).withTarget(formTarget).withMethod(Method.POST)
                     .withValues(params).submit();
  }

  /**
   * Execute when {@link ApplicationCloseRequestedEvent} is received
   *
   * @param pEvent
   *     source event
   */
  @Handler
  public void onCloseRequested(final ApplicationCloseRequestedEvent pEvent)
  {
    close();
  }

  /**
   * Method call when a {@link ModuleRefreshRequestedEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void onModuleRefreshRequestedEvent(final ModuleRefreshRequestedEvent pEvent)
  {
    if (!PortalModuleId.UNAVAILABLE.getId().equals(pEvent.getModuleId()))
    {
      modulesQueue.add(pEvent.getPortalComponent());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moduleRegistered(final PortalModule pModule)
  {
    final boolean isMain = refreshMainModules(pModule.getId());
    if (!isMain)
    {
      eventBus.publishAsync(new PortalModuleRegisteredEvent(pModule.getId()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moduleUnregistered(final String pModuleId)
  {
    final boolean isMain = refreshMainModules(pModuleId);
    if (!isMain)
    {
      eventBus.publishAsync(new PortalModuleUnRegisteredEvent(pModuleId));
    }
  }

  /**
   * @param pModuleId
   *
   * @return true is param was a main module id
   */
  private boolean refreshMainModules(final String pModuleId)
  {
    boolean isMain = false;
    if (StringUtils.isNotBlank(pModuleId))
    {
      // Build module context
      final PortalModuleService portalModuleService = OSGiHelper.getPortalModuleService();
      if (portalModuleService != null)
      {
        final Map<PortalContext.KEY, String> map = new HashMap<>();
        map.put(PortalContext.KEY.AUTH_STATUS, authStatus.name());
        if (PortalModuleId.HEADER.getId().equals(pModuleId))
        {
          final PortalContext buildContext = portalModuleService.buildContext(eventBus, getLocale(), map);
          // Initialize and add the header content
          final PortalModule headerModule = portalModuleService.getModule(PortalModuleId.HEADER.getId());
          final PortalComponent header = headerModule.createComponent(buildContext);
          portalLayout.addHeader(header.getComponent());
          if (!PortalModuleId.UNAVAILABLE.getId().equals(headerModule.getId()))
          {
            modulesQueue.add(header);
          }
          isMain = true;

        }
        else if (PortalModuleId.FOOTER.getId().equals(pModuleId))
        {
          map.put(PortalContext.KEY.SMALL, Boolean.TRUE.toString());
          final PortalContext buildContext = portalModuleService.buildContext(eventBus, getLocale(), map);
          // Initialize and add the footer content
          final PortalModule footerModule = portalModuleService.getModule(PortalModuleId.FOOTER.getId());
          final PortalComponent footer = footerModule.createComponent(buildContext);
          portalLayout.addFooter(footer.getComponent());
          if (!PortalModuleId.UNAVAILABLE.getId().equals(footerModule.getId()))
          {
            modulesQueue.add(footer);
          }
          isMain = true;
        }
        else
        {
          isMain = refreshPortalContent(map, pModuleId);

        }
      }
    }
    return isMain;
  }

  /**
   * Refresh the portal content according to the module
   *
   * @param pMap
   *     the source context
   * @param pModuleId
   *     the source module id
   *
   * @return true if the module correspond to portal content
   */
  protected abstract boolean refreshPortalContent(Map<KEY, String> pMap, String pModuleId);

}