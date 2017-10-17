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
package org.novaforge.forge.ui.mailing.internal.module;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.google.common.base.Strings;
import com.vaadin.ui.UI;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.mailing.internal.client.events.CreateMailingListEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.DeleteMailingListEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowAddMailingListViewEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowMailingListDetailsViewEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowMailingListsEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowSubscriptionMailingListViewEvent;
import org.novaforge.forge.ui.mailing.internal.client.mailing.add.AddMailingListPresenter;
import org.novaforge.forge.ui.mailing.internal.client.mailing.add.AddMailingListViewImpl;
import org.novaforge.forge.ui.mailing.internal.client.mailing.details.MailingListDetailsPresenter;
import org.novaforge.forge.ui.mailing.internal.client.mailing.details.MailingListDetailsViewImpl;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.SubscriptionMailingListPresenter;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.SubscriptionMailingListViewImpl;
import org.novaforge.forge.ui.mailing.internal.client.mailing.update.MailingListsPresenter;
import org.novaforge.forge.ui.mailing.internal.client.mailing.update.MailingListsViewImpl;
import org.novaforge.forge.ui.mailing.internal.module.task.CreateMailingListCallable;
import org.novaforge.forge.ui.mailing.internal.module.task.CreateMailingListCallable.Result;
import org.novaforge.forge.ui.mailing.internal.module.task.CreateMailingListExecutor;
import org.novaforge.forge.ui.mailing.internal.module.task.CreateMailingListRefresher;
import org.novaforge.forge.ui.mailing.internal.module.task.DeleteMailingListCallable;
import org.novaforge.forge.ui.mailing.internal.module.task.DeleteMailingListCallable.DeleteResult;
import org.novaforge.forge.ui.mailing.internal.module.task.DeleteMailingListExecutor;
import org.novaforge.forge.ui.mailing.internal.module.task.DeleteMailingListRefresher;

import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalComponent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author B-Martinelli
 */
public class MainComponent extends AbstractPortalComponent
{

  private static final Log LOGGER = LogFactory.getLog(MainComponent.class);
  /**
   * Project id.
   */
  private final String                           projectId;
  /**
   * Presenter part for mailing lists.
   */
  private final MailingListsPresenter            mailingListsPresenter;
  /**
   * Presenter for Adding MailingList
   */
  private final AddMailingListPresenter          addMailingListPresenter;
  /**
   * Presenter for Details View MailingList
   */
  private final MailingListDetailsPresenter      mailingListDetailsPresenter;
  /**
   * Presenter for Subscription for MailingList
   */
  private final SubscriptionMailingListPresenter subscriptionMailingListPresenter;
  /**
   * Cache used to store the Instance UUID of mailing list application of current project.
   */
  private UUID                                   mailingListInstance = null;
  private CreateMailingListRefresher             createMailingListRefresher;
  private CreateMailingListExecutor              createMailingListExecutor;
  private DeleteMailingListRefresher             deleteMailingListRefresher;
  private DeleteMailingListExecutor              deleteMailingListExecutor;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          the initial portal context
   */
  public MainComponent(final PortalContext pPortalContext)
  {
    super(pPortalContext);
    projectId = pPortalContext.getAttributes().get(PortalContext.KEY.PROJECTID);

    mailingListsPresenter = new MailingListsPresenter(new MailingListsViewImpl(), pPortalContext);
    addMailingListPresenter = new AddMailingListPresenter(new AddMailingListViewImpl(), pPortalContext);
    mailingListDetailsPresenter = new MailingListDetailsPresenter(new MailingListDetailsViewImpl(),
        pPortalContext);
    subscriptionMailingListPresenter = new SubscriptionMailingListPresenter(
        new SubscriptionMailingListViewImpl(), pPortalContext);

  }

  /** {@inheritDoc} */
  @Override
  public void init()
  {
    // Clear previous refresher and executor
    clearRefresher();

    // Create new Create refresher and executor
    createMailingListExecutor = new CreateMailingListExecutor();

    createMailingListRefresher = new CreateMailingListRefresher(UI.getCurrent());
    createMailingListRefresher.addListener(new RefreshListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7269408379224311091L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void refresh(final Refresher pSource)
      {
        for (final Future<Result> future : createMailingListExecutor.getDoneFutures())
        {
          try
          {
            if (!future.isCancelled())
            {
              final Result mailingListresult = future.get();
              if (mailingListresult.isSuccess())
              {
                TrayNotification
                    .show(
                        MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                            Messages.MAILING_LISTS_ADD_SUCCESS,
                            mailingListresult.getMailinglistName().getName()), TrayNotificationType.SUCCESS);

                // If initialization is ok, show mailing lists
                if ((mailingListInstance != null) && (mailingListsPresenter != null)
                    && (mailingListsPresenter.getComponent().isAttached()))
                {
                  mailingListsPresenter.refreshMailingList();
                }
              }
              else
              {
                TrayNotification.show(
                    MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                        Messages.MAILING_LISTS_ADD_ERROR, mailingListresult.getMailinglistName().getName()),
                    TrayNotificationType.WARNING);

              }
            }
          }
          catch (InterruptedException | CancellationException e)
          {
            LOGGER.warn("One of the widget callable task has been cancelled during refresh process", e);
          }
          catch (final ExecutionException e)
          {
            LOGGER.error("Unable to create mailing list", e);
            ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e,
                getCurrentLocale());
          }
          finally
          {
            // Remove future for next process
            createMailingListExecutor.removeFuture(future);
          }
        }

        if ((!createMailingListExecutor.hasFutures()) && (UI.getCurrent() != null))
        {
          createMailingListRefresher.disable();
        }

      }

    });
    
    
    // Create new Delete refresher and executor
    deleteMailingListExecutor = new DeleteMailingListExecutor();

    deleteMailingListRefresher = new DeleteMailingListRefresher(UI.getCurrent());
    deleteMailingListRefresher.addListener(new RefreshListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7269408379224311091L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void refresh(final Refresher pSource)
      {
        for (final Future<DeleteResult> future : deleteMailingListExecutor.getDoneFutures())
        {
          try
          {
            if (!future.isCancelled())
            {
              final DeleteResult mailingListresult = future.get();
              if (mailingListresult.isSuccess())
              {
                TrayNotification
                    .show(
                        MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                            Messages.MAILING_LISTS_DELETE_LIST_SUCCESS,
                            mailingListresult.getMailinglistName()), TrayNotificationType.SUCCESS);

                // If initialization is ok, show mailing lists
                if ((mailingListInstance != null) && (mailingListsPresenter != null)
                    && (mailingListsPresenter.getComponent().isAttached()))
                {
                  mailingListsPresenter.refreshMailingList();
                }
              }
              else
              {
                TrayNotification.show(
                    MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                        Messages.MAILING_LISTS_DELETE_LIST_ERROR, mailingListresult.getMailinglistName()),
                    TrayNotificationType.WARNING);

              }
            }
          }
          catch (InterruptedException | CancellationException e)
          {
            LOGGER.warn("One of the widget callable task has been cancelled during refresh process", e);
          }
          catch (final ExecutionException e)
          {
            LOGGER.error("Unable to delete mailing list", e);
            ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e,
                getCurrentLocale());
          }
          finally
          {
            // Remove future for next process
            deleteMailingListExecutor.removeFuture(future);
          }
        }

        if ((!deleteMailingListExecutor.hasFutures()) && (UI.getCurrent() != null))
        {
          deleteMailingListRefresher.disable();
        }

      }

    });
    
    // First init mailing list instance
    if (mailingListInstance == null)
    {
      final boolean initSucessfully = initializeMailingListInstance();
      if (initSucessfully)
      {
        mailingListsPresenter.setMailingListInstance(mailingListInstance);
        addMailingListPresenter.setMailingListInstance(mailingListInstance);
        mailingListDetailsPresenter.setMailingListInstance(mailingListInstance);
        subscriptionMailingListPresenter.setMailingListInstance(mailingListInstance);
      }
    }
    // If initialization is ok, show mailing lists
    if (mailingListInstance != null)
    {
      displayMainView();
    }
  }

  /**
   * Search for Instance UUID of the application of a project which plugin category is Mailing List
   */
  private boolean initializeMailingListInstance()
  {
    boolean success = false;
    final String currentUser = MailingModule.getAuthentificationService().getCurrentUser();
    if (!Strings.isNullOrEmpty(currentUser))
    {
      try
      {
        // Getting Plugin UUID for Category = Mailing list
        final List<PluginMetadata> mailingListPlugins = MailingModule.getPluginsManager()
            .getPluginsMetadataByCategory(Category.MAILINGLIST.getId());
        if ((mailingListPlugins != null) && (mailingListPlugins.size() > 0))
        {
          final Map<String, PluginStatus> pluginsUUID = getPluginsUUID(mailingListPlugins);
          if (pluginsUUID != null)
          {
            // Getting all applications in current projet for mailing list plugins
            final Collection<String> uuidsCollection = pluginsUUID.keySet();
            final List<ProjectApplication> listMailingApp = MailingModule.getApplicationPresenter()
                .getAllProjectApplications(projectId,
                    uuidsCollection.toArray(new String[uuidsCollection.size()]));
            if ((listMailingApp != null) && (listMailingApp.size() == 1))
            {
              mailingListInstance = listMailingApp.get(0).getPluginInstanceUUID();
              success = true;
            }
            else if ((listMailingApp != null) && (listMailingApp.size() == 0))
            {
              final boolean hasAdminPermissions = MailingModule.getProjectPresenter().hasAdminAuthorizations(
                  projectId);
              if (hasAdminPermissions)
              {
                final Set<Entry<String, PluginStatus>> entrySet = pluginsUUID.entrySet();
                for (final Entry<String, PluginStatus> entry : entrySet)
                {
                  if (PluginStatus.ACTIVATED.equals(entry.getValue()))
                  {
                    final Project project = MailingModule.getProjectPresenter().getProject(projectId, false);
                    MailingModule.getSysApplicationHandler().createSysApplication(project, entry.getKey());
                    success = initializeMailingListInstance();
                    break;
                  }
                }
              }
              else
              {
                // TODO Display message

              }
            }
            else if ((listMailingApp != null) && (listMailingApp.size() > 1))
            {
              // TODO Display message
            }

          }
        }
        else
        {
          // TODO Display message

        }

      }
      catch (final Exception e)
      {
        LOGGER.error(e.getLocalizedMessage(), e);
      }
    }
    if (mailingListInstance == null)
    {
      success = false;
    }
    return success;
  }

  private Map<String, PluginStatus> getPluginsUUID(final List<PluginMetadata> pMailingListPlugins)
      throws IllegalArgumentException
  {
    final Map<String, PluginStatus> mailingUUIDs = new HashMap<String, PluginStatus>();
    for (final PluginMetadata pluginMetadata : pMailingListPlugins)
    {
      if ((pluginMetadata.isAvailable())
          && ((PluginStatus.ACTIVATED.equals(pluginMetadata.getStatus())) || (PluginStatus.DEPRECATED
              .equals(pluginMetadata.getStatus()))))
      {
        final boolean alreadyPluginActivated = mailingUUIDs.values().contains(PluginStatus.ACTIVATED);
        if ((alreadyPluginActivated) && (PluginStatus.ACTIVATED.equals(pluginMetadata.getStatus())))
        {
          throw new IllegalArgumentException("There are more than one mailing list application active");
        }
        mailingUUIDs.put(pluginMetadata.getUUID(), pluginMetadata.getStatus());
      }
    }
    return mailingUUIDs;
  }

  /**
   * Callback on {@link onAddMailingListEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void onShowMailingListsEvent(final ShowMailingListsEvent pEvent)
  {
    if (getUuid().equals(pEvent.getUuid()))
    {
      displayMainView();
    }
  }

  private void displayMainView()
  {
    if (mailingListsPresenter != null)
    {
      setContent(mailingListsPresenter.getComponent());
      mailingListsPresenter.refresh();
    }
  }

  /**
   * Callback on {@link CreateMailingListEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void onCreateMailingListEvent(final CreateMailingListEvent pEvent)
  {
    if (getUuid().equals(pEvent.getUuid()))
    {
      displayMainView();
      if ((createMailingListExecutor != null) && (createMailingListRefresher != null))
      {
        final MailingListBean mailinglist = pEvent.getMailinglist();
        if (mailinglist != null)
        {
          TrayNotification.show(
              MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.MAILING_LISTS_ADD_INPROGRESS, mailinglist.getName()), TrayNotificationType.INFO);
          createMailingListRefresher.enable();
  
          createMailingListExecutor.addCallable(new CreateMailingListCallable(mailingListInstance, mailinglist,
              MailingModule.getAuthentificationService().getCurrentUser()));
          createMailingListExecutor.submitAll();
        }
      }
    } 
  }
  
  /**
   * Callback on {@link DeleteMailingListEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void onDeleteMailingListEvent(final DeleteMailingListEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      displayMainView();
      if ((deleteMailingListExecutor != null) && (deleteMailingListRefresher != null))
      {
        final String mailinglist = pEvent.getMailinglist();
        if (mailinglist != null)
        {
          TrayNotification.show(
              MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.MAILING_LISTS_DELETE_LIST_INPROGRESS, mailinglist), TrayNotificationType.INFO);
          deleteMailingListRefresher.enable();
  
          deleteMailingListExecutor.addCallable(new DeleteMailingListCallable(mailingListInstance, mailinglist,
              MailingModule.getAuthentificationService().getCurrentUser()));
          deleteMailingListExecutor.submitAll();
        }
      }
    }
  }

  /**
   * Will return either UI Locale or view locale
   *
   * @return {@link Locale}
   */
  private Locale getCurrentLocale()
  {
    Locale currentLocale = Locale.ENGLISH;
    if (UI.getCurrent() != null)
    {
      currentLocale = UI.getCurrent().getLocale();
    }
    else if ((getComponent() != null) && (getComponent().getLocale() != null))
    {
      currentLocale = getComponent().getLocale();
    }
    return currentLocale;

  }

  /**
   * Callback on {@link onAddMailingListEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onAddMailingListEvent(final ShowAddMailingListViewEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
    if (addMailingListPresenter != null)
      {
        setContent(addMailingListPresenter.getComponent());
        addMailingListPresenter.refresh();
      }
    }
  }

  /**
   * Callback on {@link onDetailsMailingListEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onDetailsMailingListEvent(final ShowMailingListDetailsViewEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      if (mailingListDetailsPresenter != null)
      {
        setContent(mailingListDetailsPresenter.getComponent());
        mailingListDetailsPresenter.refresh(pEvent.getMailinglist());
      }
    }
  }

  /**
   * Callback on {@link onAddMailingListEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onSubscriptionMailingListEvent(final ShowSubscriptionMailingListViewEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      if (subscriptionMailingListPresenter != null)
      {
        setContent(subscriptionMailingListPresenter.getComponent());
        subscriptionMailingListPresenter.refresh(pEvent.getListname(), pEvent.getItem(),
            pEvent.getMailingListsContainer());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterReferences()
  {
    clearRefresher();

    super.unregisterReferences();
  }

  /** {@inheritDoc} */
  @Override
  protected PortalModuleId getModuleId()
  {
    return MailingModule.getPortalModuleId();
  }

  private void clearRefresher()
  {
    if ((createMailingListRefresher != null) && (createMailingListRefresher.isAttached()))
    {
      createMailingListRefresher.remove();
    }
    if (createMailingListExecutor != null)
    {
      createMailingListExecutor.cancelAll();
      createMailingListExecutor.shutdown();
    }
    
    if ((deleteMailingListRefresher != null) && (deleteMailingListRefresher.isAttached()))
    {
      deleteMailingListRefresher.remove();
    }
    if (deleteMailingListExecutor != null)
    {
      deleteMailingListExecutor.cancelAll();
      deleteMailingListExecutor.shutdown();
    }
  }
}