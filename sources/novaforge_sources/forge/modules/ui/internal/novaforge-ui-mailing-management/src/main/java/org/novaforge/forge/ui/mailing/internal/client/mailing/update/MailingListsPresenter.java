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
package org.novaforge.forge.ui.mailing.internal.client.mailing.update;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.plugins.categories.beans.MailingListUserImpl;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListsContainer;
import org.novaforge.forge.ui.mailing.internal.client.events.DeleteMailingListEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowAddMailingListViewEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowMailingListDetailsViewEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowSubscriptionMailingListViewEvent;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.MailingListsColumnActionsGenerator;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.MailingListsColumnSuscribeGenerator;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.OwnersColumnNameGenerator;
import org.novaforge.forge.ui.mailing.internal.module.AbstractMailingListsPresenter;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.data.generator.EmailColumnGenerator;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * This presenter handles mailing lists view.
 * 
 * @author B-Martinelli
 */
public class MailingListsPresenter extends AbstractMailingListsPresenter implements Serializable
{
  /** Serial version uid used for serialization */
  private static final long      serialVersionUID    = -5042299647493799344L;
  private static final Log LOGGER = LogFactory.getLog(MailingListsPresenter.class);
  /** Content of project view */
  private final MailingListsView view;
  /** Mailing list vaadin container. */
  private MailingListsContainer  mailingListsContainer;
  /** Set if any list has been selected in the table **/
  private String                 selectedList;
  private boolean                hasAdminPermissions = false;

  /**
   * Default constructor. Initialize the tree component associated to the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context
   * @param pMailingListsAppUUID
   */
  public MailingListsPresenter(final MailingListsView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;

    initAdminPermissions();
    initMailingListsTable();
    addListeners();
  }

  private void initAdminPermissions()
  {
    hasAdminPermissions = MailingModule.getProjectPresenter().hasAdminAuthorizations(getProjectId());

    // Display or Hide Creation Button if the current user hasn or hasn't any admin permissions
    view.setAdminVisibility(hasAdminPermissions);
  }

  /** Initialize the container of Mailing Lists. */
  private void initMailingListsTable()
  {
    mailingListsContainer = new MailingListsContainer();
    view.getMailingListsTable().setContainerDataSource(mailingListsContainer);

    // Define specific columns dislay
    view.getMailingListsTable().addGeneratedColumn(MailingListItemProperty.NAME.getPropertyId(),
        new EmailColumnGenerator(MailingListItemProperty.NAME.getPropertyId()));
    view.getMailingListsTable().addGeneratedColumn(MailingListItemProperty.OWNERS.getPropertyId(),
        new OwnersColumnNameGenerator(this));
    view.getMailingListsTable().addGeneratedColumn(MailingListItemProperty.IS_SUBSCRIBER.getPropertyId(),
        new MailingListsColumnSuscribeGenerator(this));
    view.getMailingListsTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
        new MailingListsColumnActionsGenerator(this, hasAdminPermissions));

    // Define visibles columns
    view.getMailingListsTable().setVisibleColumns(MailingListItemProperty.NAME.getPropertyId(),
        MailingListItemProperty.DESCRIPTION.getPropertyId(), MailingListItemProperty.OWNERS.getPropertyId(),
        MailingListItemProperty.SUBSCRIBERS_NB.getPropertyId(),
        MailingListItemProperty.IS_SUBSCRIBER.getPropertyId(), CommonItemProperty.ACTIONS.getPropertyId());

    // Define special column width
    view.getMailingListsTable().setColumnExpandRatio(MailingListItemProperty.NAME.getPropertyId(), 0.3f);
    view.getMailingListsTable().setColumnExpandRatio(MailingListItemProperty.DESCRIPTION.getPropertyId(),
        0.3f);
    view.getMailingListsTable().setColumnExpandRatio(MailingListItemProperty.OWNERS.getPropertyId(), 0.3f);
    view.getMailingListsTable().setColumnExpandRatio(MailingListItemProperty.SUBSCRIBERS_NB.getPropertyId(),
        0.1f);
    view.getMailingListsTable().setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 100);
  }

  /** Add listeners to view components */
  public void addListeners()
  {
    view.getCreateButton().addClickListener(new ClickListener()
    {
      /** Serial version id */
      private static final long serialVersionUID = -966341600260989898L;

      /** {@inheritDoc} */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        getEventBus().publish(new ShowAddMailingListViewEvent(getUuid()));
      }
    });
    view.getMailingListsTable().addActionHandler(new RefreshAction()
    {
      /** Serial version id */
      private static final long serialVersionUID = 3657198735744388874L;

      /** {@inheritDoc} */
      @Override
      public void refreshAction()
      {
        refresh();
      }

      /** {@inheritDoc} */
      @Override
      public PortalMessages getPortalMessages()
      {
        return MailingModule.getPortalMessages();
      }

    });
    view.getFilterTextField().addTextChangeListener(new TextChangeListener()
    {
      /** Serial UID. */
      private static final long serialVersionUID = 1978421308920978868L;

      @Override
      public void textChange(final TextChangeEvent pEvent)
      {
        // Clean filter
        mailingListsContainer.removeAllContainerFilters();
        if ((pEvent.getText() != null) && !pEvent.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter mailingListsFilter = new Or(new SimpleStringFilter(MailingListItemProperty.NAME
              .getPropertyId(), pEvent.getText(), true, false), new SimpleStringFilter(
              MailingListItemProperty.DESCRIPTION.getPropertyId(), pEvent.getText(), true, false));
          mailingListsContainer.addContainerFilter(mailingListsFilter);
        }
      }
    });

    view.getCloseListWindow().getYesButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 2756645480332651962L;
      
      /** {@inheritDoc} */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        if (selectedList != null)
        {
          getEventBus().publish(new DeleteMailingListEvent(selectedList, getUuid()));
        }
      }
    });
    view.getUnsubscribeCancel().addClickListener(new ClickListener()
    {
      private static final long serialVersionUID = -3450026734075164574L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        selectedList = null;
        UI.getCurrent().removeWindow(view.getUnsubscribeWindow());
      }
    });
    view.getUnsubscribeConfirm().addClickListener(new Button.ClickListener()
    {

      private static final long serialVersionUID = -7903091556206579706L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (selectedList != null)
        {
          unSubscribeFromSelectedMailinglist();
          UI.getCurrent().removeWindow(view.getUnsubscribeWindow());
        }
      }
    });
  }

  /** Refresh the project information. */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /** {@inheritDoc} */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    selectedList = null;
    view.getFilterTextField().setValue(""); //$NON-NLS-1$
    mailingListsContainer.removeAllContainerFilters();
    refreshMailingList();

  }

  /** {@inheritDoc} */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

  /**
   * @param pItemId
   */
  public void onClickActionSubscription(final Object pItemId)
  {
    selectedList = (String) pItemId;
    final String listname = (String) mailingListsContainer.getContainerProperty(pItemId,
        MailingListItemProperty.NAME.getPropertyId()).getValue();
    getEventBus().publish(new ShowSubscriptionMailingListViewEvent(listname, pItemId, mailingListsContainer, getUuid()));
  }

  /**
   * @param pItemId
   */
  public void onClickActionViewDetails(final Object pItemId)
  {
    selectedList = (String) pItemId;

    final MailingListBean mailinglist = (MailingListBean) mailingListsContainer.getContainerProperty(pItemId,
        MailingListItemProperty.DEFAULT.getPropertyId()).getValue();

    getEventBus().publish(new ShowMailingListDetailsViewEvent(mailinglist, getUuid()));
  }

  /**
   * @param pItemId
   */
  public void onClickActionDelete(final Object pItemId)
  {
    selectedList = (String) pItemId;
    
    view.getCloseListWindow().setParameterMessage(selectedList);
    UI.getCurrent().addWindow(view.getCloseListWindow());

  }

  /**
   * @param pItemId
   */
  public void onClickActionUnsubscribe(final Object pItemId)
  {

    final MailingListBean mailingListBean = (MailingListBean) mailingListsContainer.getContainerProperty(
        pItemId, MailingListItemProperty.DEFAULT.getPropertyId()).getValue();
    final boolean isOwner = (Boolean) mailingListsContainer.getContainerProperty(pItemId,
        MailingListItemProperty.IS_OWNER.getPropertyId()).getValue();
    if (mailingListBean != null)
    {
      selectedList = (String) pItemId;
      if ((mailingListBean.getType().isVisible()) || (((!mailingListBean.getType().isVisible()) && (isOwner))))
      {
        unSubscribeFromSelectedMailinglist();
      }
      else
      {
        view.getUnsubscribeConfirmLabel().setValue(
            MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.MAILING_LISTS_UNSUBSCRIBE_LIST_PRIVATE_CONFIRM, pItemId));
        UI.getCurrent().addWindow(view.getUnsubscribeWindow());
      }
    }
  }

  private void unSubscribeFromSelectedMailinglist()
  {

    try
    {
      final MailingListUser subscriberToRemove = getMailingListUser(selectedList);
      if (subscriberToRemove != null)
      {
        MailingModule.getMailingListCategoryService().removeSubscriber(null,
            getMailingListInstance().toString(), getCurrentUser(), selectedList, subscriberToRemove, false);
        TrayNotification.show(MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
            Messages.MAILING_LISTS_UNSUBSCRIBE_LIST_SUCCESS, selectedList));
        refreshContent();
      }

    }
    catch (final Exception e)
    {
      LOGGER.error(e.getLocalizedMessage(), e);
      ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e, view.getLocale());
    }

  }

  @SuppressWarnings("unchecked")
  private MailingListUser getMailingListUser(final Object pItemId)
  {
    final Item item = mailingListsContainer.getItem(pItemId);
    final List<MailingListUser> subscribers = (List<MailingListUser>) item.getItemProperty(MailingListItemProperty.SUBSCRIBERS
                                                                                               .getPropertyId())
                                                                          .getValue();
    MailingListUser returned = null;
    if (subscribers != null)
    {
      for (final MailingListUser subscriber : subscribers)
      {
        if ((!subscriber.isExternal()) && (subscriber.getLogin().equals(getCurrentUser())))
        {
          returned = subscriber;
          break;
        }
      }
    }
    return returned;
  }

  /**
   * This method will only refresh mailing list container
   */
  public void refreshMailingList()
  {
    view.attachMailinglistsTable(false);

    try
    {
      // Search for current User
      final String currentUser = MailingModule.getAuthentificationService().getCurrentUser();

      // Searching for instanceUUID for mailing list applications of current projet
      final List<MailingListBean> mailingLists = MailingModule.getMailingListCategoryService()
                                                              .getVisibleMailingLists(getProjectId(),
                                                                                      getMailingListInstance()
                                                                                          .toString(), currentUser);
      mailingListsContainer.setMailingLists(mailingLists, currentUser);

    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e, view.getLocale());
    }
    view.attachMailinglistsTable(true);
  }

  /**
   * @param pItemId
   */
  public void onClickActionSubscribe(final Object pItemId)
  {
    try
    {

      final UserProfile userProfile = MailingModule.getUserPresenter().getUserProfile(getCurrentUser());
      final User user = userProfile.getUser();
      final MailingListUser mailingListUser = new MailingListUserImpl(user.getLogin(), user.getEmail());
      MailingModule.getMailingListCategoryService().addSubscriber(null, getMailingListInstance().toString(),
          getCurrentUser(), (String) pItemId, mailingListUser);

      TrayNotification.show(MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
          Messages.MAILING_LISTS_SUBSCRIBE_LIST_SUCCESS, pItemId));
      // remove the subscriber from the list
      refreshContent();
    }
    catch (final Exception e)
    {
      LOGGER.error(e.getLocalizedMessage(), e);
      ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e, view.getLocale());
    }
  }

}
