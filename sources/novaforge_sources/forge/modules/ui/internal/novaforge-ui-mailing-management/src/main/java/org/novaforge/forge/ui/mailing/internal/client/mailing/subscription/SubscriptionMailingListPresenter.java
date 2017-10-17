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
package org.novaforge.forge.ui.mailing.internal.client.mailing.subscription;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListServiceException;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.plugins.categories.beans.MailingListUserImpl;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListsContainer;
import org.novaforge.forge.ui.mailing.internal.client.containers.SubscribersContainer;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowMailingListsEvent;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.SubscribersColumnActionsGenerator;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.SubscribersColumnNameGenerator;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.SubscribersColumnTypeGenerator;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.wizard.components.EmailCheck;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.wizard.components.SubscribeType;
import org.novaforge.forge.ui.mailing.internal.module.AbstractMailingListsPresenter;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 * @author Aimen Merkich
 */
public class SubscriptionMailingListPresenter extends AbstractMailingListsPresenter implements Serializable
{

  protected static final String STEP_TWO      = "STEP_TWO";
  protected static final String FINAL_STEP    = "FINAL_STEP";
  /**
   * Serial Version UID
   */
  private static final long                 serialVersionUID              = 6216291602385034719L;
  private static final String               EMAIL_SEPARATOR               = "@";
  /**
   * Logger
   */
  private static final   Log    LOGGER        = LogFactory.getLog(SubscriptionMailingListPresenter.class);
  private static final   String FORGE_PROJECT = "FORGE_PROJECT";
  private static final   String PROJECT       = "PROJECT";
  private final SubscriptionMailingListView view;
  /**
   * The list of mailing to add
   */
  private final List<MailingListUser> subscribersToAdd = new LinkedList<MailingListUser>();
  /** Subscribers vaadin container. */
  private SubscribersContainer subscribersContainer;
  /** The current mailing listname */
  private String                      currentListname               = null;
  /** The current currentSubscribers */
  private List<MailingListSubscriber> currentSubscribers            = null;
  /** The list of mailing group to add */
  private List<MailingListGroup>      selectedGroupsUsers           = new LinkedList<MailingListGroup>();
  /** The returned inner mails in error list */
  private List<MailingListUser>       InnerMailInErrror             = null;
  /** The Subscribe type */
  private SubscribeType               subscribeType                 = null;
  /** The Subscriber to add */
  private MailingListUser             subscriberToAdd               = null;
  /** Notification */
  private TrayNotification            notification                  = null;
  /** The Return of external mail validation */
  private String                      externalEmailValidationReturn = null;
  /** The Email Check class */
  private EmailCheck                  emailCheck                    = null;
  /** The Externe list of rejected mail */
  private String                      mailExternList                = null;
  /** The selected subscriber */
  private String                      selectedSubscriberId          = null;
  /** The selected item */
  private Object                item;
  /** The Mailing list container */
  private MailingListsContainer mailingListsContainer;
  private boolean               isForgeProject;

  /**
   * @param pView
   * @param pPortalContext
   */
  public SubscriptionMailingListPresenter(final SubscriptionMailingListView pView, final PortalContext pPortalContext)

  {
    super(pPortalContext);
    view = pView;

    initSubscribersTable();
    addListeners();
  }

  /** Initialize the container of Mailing Lists. */
  private void initSubscribersTable()
  {
    isForgeProject = MailingModule.getProjectPresenter().isForgeProject(getProjectId());
    subscribersContainer = new SubscribersContainer();
    view.getSubscribersTable().setContainerDataSource(subscribersContainer);

    // Define specific columns dislay
    view.getSubscribersTable().addGeneratedColumn(MailingListItemProperty.SUBSCRIBER.getPropertyId(),
                                                  new SubscribersColumnNameGenerator(this));
    view.getSubscribersTable().addGeneratedColumn(MailingListItemProperty.TYPE.getPropertyId(),
                                                  new SubscribersColumnTypeGenerator(this));
    view.getSubscribersTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
                                                  new SubscribersColumnActionsGenerator(this));

    // Define visibles columns
    view.getSubscribersTable().setVisibleColumns(MailingListItemProperty.SUBSCRIBER.getPropertyId(),
                                                 MailingListItemProperty.TYPE.getPropertyId(),
                                                 CommonItemProperty.ACTIONS.getPropertyId());

    // Define special column width
    view.getSubscribersTable().setColumnExpandRatio(MailingListItemProperty.SUBSCRIBER.getPropertyId(), 0.4f);
    view.getSubscribersTable().setColumnExpandRatio(MailingListItemProperty.TYPE.getPropertyId(), 0.4f);
    view.getSubscribersTable().setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 80);
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {

    view.getReturnButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -5090013824553391627L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new ShowMailingListsEvent(getUuid()));

      }
    });

    view.getSubscribeButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4716744769773705933L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().addWindow(view.getSubscribeWindow());
        view.getAddSubscribeWizard().restartWizard();
        view.getAddSubscribeWizard().getStepOne().getProjectUserLogo().setSource(getProjectImageRessource(PROJECT));
        view.getAddSubscribeWizard().getStepOne().getForgeUserLogo().setSource(getProjectImageRessource(FORGE_PROJECT));
      }
    });
    view.getUnsubscribeUserCancel().addClickListener(new ClickListener()
    {
      private static final long serialVersionUID = -3450026734075164574L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSubscribersTable().unselect(selectedSubscriberId);
        selectedSubscriberId = null;
        UI.getCurrent().removeWindow(view.getUnsubscribeUserWindow());
      }
    });
    view.getUnsubscribeUserConfirm().addClickListener(new Button.ClickListener()
    {

      private static final long serialVersionUID = -7903091556206579706L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (selectedSubscriberId != null)
        {
          try
          {
            final MailingListUser subscriberToRemove = (MailingListUser) findById(selectedSubscriberId);
            MailingModule.getMailingListCategoryService().removeSubscriber(null, getMailingListInstance().toString(),
                                                                           getCurrentUser(), currentListname,
                                                                           subscriberToRemove,
                                                                           !view.getNotificationUserChbx().getValue());

            // remove the subscriber from the list
            currentSubscribers.remove(subscriberToRemove);
            TrayNotification.show(MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                               Messages.MAILING_LISTS_UNSUBSCRIBE_CONFIRM_SUCCESS,
                                                                               subscriberToRemove.getEmail()));
            refreshContent();
          }
          catch (final Exception e)
          {
            LOGGER.error(e.getLocalizedMessage(), e);
            ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e, view.getLocale());
          }
          finally
          {
            UI.getCurrent().removeWindow(view.getUnsubscribeUserWindow());
            view.getSubscribersTable().unselect(selectedSubscriberId);
            selectedSubscriberId = null;
          }
        }
      }
    });
    view.getUnsubscribeGroupCancel().addClickListener(new ClickListener()
    {
      private static final long serialVersionUID = -7556038428307785917L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSubscribersTable().unselect(selectedSubscriberId);
        selectedSubscriberId = null;
        UI.getCurrent().removeWindow(view.getUnsubscribeGroupWindow());
      }
    });
    view.getUnsubscribeGroupConfirm().addClickListener(new Button.ClickListener()
    {
      private static final long serialVersionUID = -7329882433516187290L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (selectedSubscriberId != null)
        {
          try
          {
            final MailingListGroup subscriberToRemove = (MailingListGroup) findById(selectedSubscriberId);
            MailingModule.getMailingListCategoryService().removeGroupSubscription(null,
                getMailingListInstance().toString(), getCurrentUser(), currentListname, subscriberToRemove,
                !view.getNotificationGroupChbx().getValue());

            // remove the subscriber from the list
            currentSubscribers.remove(subscriberToRemove);
            TrayNotification.show(MailingModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.MAILING_LISTS_UNSUBSCRIBE_GROUP_CONFIRM_SUCCESS, subscriberToRemove.getName()));
            refreshContent();
          }
          catch (final Exception e)
          {
            LOGGER.error(e.getLocalizedMessage(), e);
            ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e, view.getLocale());
          }
          finally
          {
            UI.getCurrent().removeWindow(view.getUnsubscribeGroupWindow());
            view.getSubscribersTable().unselect(selectedSubscriberId);
            selectedSubscriberId = null;
          }
        }
      }
    });
    view.getFilterTextField().addTextChangeListener(new TextChangeListener()
    {
      private static final long serialVersionUID = -316111102687994586L;

      @Override
      public void textChange(final TextChangeEvent pEvent)
      {
        // Clean filter
        subscribersContainer.removeAllContainerFilters();
        if ((pEvent.getText() != null) && !pEvent.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter mailingListsFilter = new Or(new SimpleStringFilter(MailingListItemProperty.SUBSCRIBER
              .getPropertyId(), pEvent.getText(), true, false));
          subscribersContainer.addContainerFilter(mailingListsFilter);
        }
      }
    });

    view.getAddSubscribeWizard().getCancelButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -4585177870324828008L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(view.getSubscribeWindow());
        view.getAddSubscribeWizard().restartWizard();
      }
    });

    view.getAddSubscribeWizard().getFinishButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = 8626858636136202517L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(view.getSubscribeWindow());
        view.getAddSubscribeWizard().restartWizard();
        refresh(currentListname, item, mailingListsContainer);
      }
    });

    view.getAddSubscribeWizard().getNextButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = 8626858636136202517L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        subscribeType = view.getAddSubscribeWizard().getSubscribeType();
        emailCheck = new EmailCheck(view.getAddSubscribeWizard().getExternalUserMail().getValue());
        externalEmailValidationReturn = emailCheck.checkEmail();
        view.getAddSubscribeWizard().getStepThree().setRejectedEmailsComponentVisible(true);
        view.getAddSubscribeWizard().getStepThree().setAddedEmailsComponentVisible(true);

        if (view.getAddSubscribeWizard().getStep().equals(STEP_TWO))
        {
          if (subscribeType.equals(SubscribeType.GROUP))
          {

            refreshGroupsTableSelectableContent();

          }
          else if (!subscribeType.equals(SubscribeType.EXTERNAL)
              && !subscribeType.equals(SubscribeType.GROUP))
          {
            refreshUserTableSelectableContent(subscribeType);

          }
        }
        else if (view.getAddSubscribeWizard().getStep().equals(FINAL_STEP))
        {

          if (view.getAddSubscribeWizard().getSubscribeType().equals(SubscribeType.GROUP))
          {

            if (!view.getAddSubscribeWizard().getAddGroupView().getGroupTableSelectable().getSelectedGroup().isEmpty())
            {
              final List<String> groupName = new LinkedList<String>();
              selectedGroupsUsers.clear();
              selectedGroupsUsers = view.getAddSubscribeWizard().getAddGroupView().getGroupTableSelectable()
                  .getSelectedGroup();
              for (final MailingListGroup pGroupSubscriber : selectedGroupsUsers)
              {

                groupName.add(pGroupSubscriber.getName());
              }

              try
              {
                MailingModule.getMailingListCategoryService().addGroupsSubscriptions(null,
                    getMailingListInstance().toString(), getCurrentUser(), currentListname,
                    selectedGroupsUsers, false);

                view.getAddSubscribeWizard().getStepThree().setMailComponent(groupName, true);
                view.getAddSubscribeWizard().getStepThree().setRejectedEmailsComponentVisible(false);
              }
              catch (final MailingListServiceException e)
              {

                view.getAddSubscribeWizard().getStepThree().setMailComponent(groupName, false);
                view.getAddSubscribeWizard().getStepThree().setAddedEmailsComponentVisible(false);
                LOGGER.error(e.getLocalizedMessage(), e);
                ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e,
                    view.getLocale());
              }
              finally
              {
                view.getAddSubscribeWizard().getAddGroupView().getGroupTableSelectable().clear();
              }

            }
            else
            {
              view.getAddSubscribeWizard().setStepActive(view.getAddSubscribeWizard().getStepTwo());
              notification = new TrayNotification((MailingModule.getPortalMessages().getMessage(
                  getCurrentLocale(), Messages.MAILING_LISTS_ADD_GROUP_SUBSCRIBERS_WARNING)),
                  TrayNotificationType.WARNING);
              notification.show(Page.getCurrent());
            }

          }
          else if (!view.getAddSubscribeWizard().getSubscribeType().equals(SubscribeType.EXTERNAL)
              && !view.getAddSubscribeWizard().getSubscribeType().equals(SubscribeType.GROUP))
          {
            if (!view.getAddSubscribeWizard().getAddUserView().getUserTableSelectable().getSelectedUser().isEmpty())
            {
              subscribersToAdd.clear();
              for (final String userLogin : view.getAddSubscribeWizard().getAddUserView()
                  .getUserTableSelectable().getSelectedUser())
              {

                final String email = (String) view.getAddSubscribeWizard().getAddUserView()
                    .getUserTableSelectable().getUsersContainer()
                    .getContainerProperty(userLogin, UserItemProperty.EMAIL.getPropertyId()).getValue();
                subscriberToAdd = new MailingListUserImpl(userLogin, email);
                subscribersToAdd.add(subscriberToAdd);

              }
              try
              {
                InnerMailInErrror = MailingModule.getMailingListCategoryService().addSubscribers(null,
                    getMailingListInstance().toString(), getCurrentUser(), currentListname, subscribersToAdd,
                    false);
                if (InnerMailInErrror != null)
                {
                  if (!InnerMailInErrror.isEmpty() && (InnerMailInErrror.size() == subscribersToAdd.size()))
                  {
                    view.getAddSubscribeWizard().getStepThree()
                        .setMailComponent(getMailStringList(InnerMailInErrror), false);
                    view.getAddSubscribeWizard().getStepThree().setAddedEmailsComponentVisible(false);
                  }
                  else
                  {
                    subscribersToAdd.removeAll(InnerMailInErrror);
                    view.getAddSubscribeWizard().getStepThree()
                        .setMailComponent(getMailStringList(InnerMailInErrror), false);
                    view.getAddSubscribeWizard().getStepThree()
                        .setMailComponent(getMailStringList(subscribersToAdd), true);
                  }
                }
                else
                {
                  view.getAddSubscribeWizard().getStepThree()
                      .setMailComponent(getMailStringList(subscribersToAdd), true);
                  view.getAddSubscribeWizard().getStepThree().setRejectedEmailsComponentVisible(false);
                }
              }
              catch (final MailingListServiceException e)
              {
                LOGGER.error(e.getLocalizedMessage(), e);
                ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e,
                    view.getLocale());
              }
              finally
              {
                view.getAddSubscribeWizard().getAddUserView().getUserTableSelectable().clear();
              }
            }
            else
            {
              view.getAddSubscribeWizard().setStepActive(view.getAddSubscribeWizard().getStepTwo());
              notification = new TrayNotification((MailingModule.getPortalMessages().getMessage(
                  getCurrentLocale(), Messages.MAILING_LISTS_ADD_SUBSCRIBERS_WARNING)),
                  TrayNotificationType.WARNING);
              notification.show(Page.getCurrent());
            }

          }
          else if (externalEmailValidationReturn.equals(""))

          {
            List<String> ExternMailOnError = new LinkedList<String>();
            final String emailList = view.getAddSubscribeWizard().getExternalUserMail().getValue();

            mailExternList = removeDuplicateEmails(emailList);
            final String[] emailsArrayToAdd = mailExternList.split(";");
            final List<String> emailListTolist = new LinkedList<String>(Arrays.asList(emailsArrayToAdd));
            try
            {

              ExternMailOnError = MailingModule.getMailingListCategoryService().addExternalSubscribers(null,
                  getMailingListInstance().toString(), getCurrentUser(), currentListname, mailExternList,
                  false);

              if (ExternMailOnError != null)
              {
                if (ExternMailOnError.isEmpty())
                {

                  view.getAddSubscribeWizard().getStepThree().setMailComponent(emailListTolist, true);
                  view.getAddSubscribeWizard().getStepThree().setRejectedEmailsComponentVisible(false);
                }
                else if (ExternMailOnError.size() == emailListTolist.size())
                {

                  view.getAddSubscribeWizard().getStepThree().setMailComponent(ExternMailOnError, false);
                  view.getAddSubscribeWizard().getStepThree().setAddedEmailsComponentVisible(false);
                }
                else
                {
                  emailListTolist.removeAll(ExternMailOnError);
                  view.getAddSubscribeWizard().getStepThree().setMailComponent(ExternMailOnError, false);
                  view.getAddSubscribeWizard().getStepThree().setMailComponent(emailListTolist, true);
                }

              }

            }
            catch (final MailingListServiceException e)
            {
              LOGGER.error(e.getLocalizedMessage(), e);
              ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e,
                  view.getLocale());

            }

          }
          else
          {
            notification = new TrayNotification((MailingModule.getPortalMessages().getMessage(
                getCurrentLocale(), externalEmailValidationReturn)), TrayNotificationType.WARNING);
            notification.show(Page.getCurrent());
            view.getAddSubscribeWizard().setStepActive(view.getAddSubscribeWizard().getStepTwo());

          }
        }

      }

    });
  }

  /**
   * @param ProjectType
   * @return StreamResource
   */
  public StreamResource getProjectImageRessource(final String ProjectType)
  {
    StreamResource streamResource = null;
    try
    {
      final String projectId;
      if (ProjectType.equals(FORGE_PROJECT))
      {
        projectId = MailingModule.getForgeConfigurationService().getForgeProjectId();
      }
      else
      {
        projectId = getProjectId();
      }

      final ProjectOptions pProjectOptions = MailingModule.getProjectPresenter().newProjectOptions(true, true, true);
      final Project project = MailingModule.getProjectPresenter().getProject(projectId, pProjectOptions);
      streamResource = ResourceUtils.buildImageResource(project.getImage().getFile(), projectId);
    }
    catch (final ProjectServiceException e)
    {
      LOGGER.error(e.getLocalizedMessage(), e);
    }
    return streamResource;

  }

  private MailingListSubscriber findById(final String pId)
  {
    MailingListSubscriber returned = null;
    final boolean         isUser   = pId.contains(EMAIL_SEPARATOR);
    for (final MailingListSubscriber subscriber : currentSubscribers)
    {
      if (isUser && subscriber instanceof MailingListUser)
      {
        final MailingListUser user = (MailingListUser) subscriber;
        if (pId.equals(user.getEmail()))
        {
          returned = user;
          break;
        }
      }
      else if (!isUser && subscriber instanceof MailingListGroup)
      {
        final MailingListGroup group = (MailingListGroup) subscriber;
        if (pId.equals(group.getName()))
        {
          returned = group;
          break;
        }
      }
    }
    return returned;
  }

  /**
   * Will refresh the project information.
   */
  @SuppressWarnings("unchecked")
  public void refresh(final String pListname, final Object pItem,
      final MailingListsContainer pMailingListsContainer)
  {
    currentListname = pListname;
    item = pItem;
    mailingListsContainer = pMailingListsContainer;

    List<MailingListBean> mailingLists = null;
    try
    {
      mailingLists = MailingModule.getMailingListCategoryService().getVisibleMailingLists(getProjectId(),
          getMailingListInstance().toString(), getCurrentUser());
    }
    catch (final MailingListServiceException e)
    {
      LOGGER.error(e.getLocalizedMessage(), e);
    }
    mailingListsContainer.setMailingLists(mailingLists, getCurrentUser());
    currentSubscribers = (List<MailingListSubscriber>) mailingListsContainer.getContainerProperty(item,
        MailingListItemProperty.SUBSCRIBERS.getPropertyId()).getValue();
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /**
   *
   */
  private void refreshGroupsTableSelectableContent()
  {
    view.getAddSubscribeWizard().getAddGroupView().getGroupTableSelectable().attachGroupsTable(false);
    view.getAddSubscribeWizard().getAddGroupView().getGroupTableSelectable().clear();
    view.getFilterTextField().setValue(""); //$NON-NLS-1$
    try
    {
      final List<Group> groups = MailingModule.getGroupPresenter().getAllGroups(getProjectId(), true);
      final List<Group> filtredGroup = new LinkedList<Group>();

      for (final Group group : groups)
      {
        filtredGroup.add(group);
      }

      // delete all previous subscribed group
      final List<Group> groupToRemove = new LinkedList<Group>();
      for (final Group group : filtredGroup)
      {
        for (final MailingListSubscriber memberSubscribed : currentSubscribers)
        {
          if (memberSubscribed instanceof MailingListGroup)
          {
            final String groupUUIDToString = group.getUuid().toString();
            final String memberSubscribedUUIDToString = ((MailingListGroup) memberSubscribed).getUUID().toString();

            if (groupUUIDToString.equalsIgnoreCase(memberSubscribedUUIDToString))
            {
              groupToRemove.add(group);
            }
          }
        }
      }

      filtredGroup.removeAll(groupToRemove);
      final List<MembershipInfo> memberships = MailingModule.getMembershipPresenter()
                                                            .getAllGroupMemberships(getProjectId());
      view.getAddSubscribeWizard().getAddGroupView().getGroupTableSelectable().getGroupsContainer()
          .setGroups(filtredGroup, isForgeProject);
      view.getAddSubscribeWizard().getAddGroupView().getGroupTableSelectable().getGroupsContainer()
          .setGroupsMember(memberships);
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e, view.getLocale());

    }

    view.getAddSubscribeWizard().getAddGroupView().getGroupTableSelectable().attachGroupsTable(true);

  }

  /**
   * @param subscribeType
   */
  private void refreshUserTableSelectableContent(final SubscribeType subscribeType)
  {
    view.getAddSubscribeWizard().getAddUserView().getUserTableSelectable().clear();
    try
    {
      final List<MembershipInfo> memberships = MailingModule.getMembershipPresenter().getAllUserMemberships(
          getProjectId(), true);
      final List<User> users = MailingModule.getUserPresenter().getAllUsers(true);
      final Set<User> selectableUsersFilterOne = new HashSet<User>();

      for (final User user : users)
      {
        boolean isMember = false;
        for (final MembershipInfo member : memberships)
        {
          if (member.getActor().getUuid().equals(user.getUuid()))
          {
            isMember = true;
            if (subscribeType.equals(SubscribeType.PROJECT))
            {
              selectableUsersFilterOne.add(user);
            }
            break;
          }
        }

        if (!isMember && subscribeType.equals(SubscribeType.FORGE))
        {
          selectableUsersFilterOne.add(user);
        }
      }

      final List<User> selectableUsersFilterTwo = new ArrayList<User>();
      if (selectableUsersFilterOne != null)
      {
        for (final User user : selectableUsersFilterOne)
        {
          boolean isSubscriber = false;
          for (final MailingListSubscriber subscriber : currentSubscribers)
          {
            if (subscriber instanceof MailingListUser)
            {
              final MailingListUser userSubscriber = (MailingListUser) subscriber;
              if (user.getEmail().equals(userSubscriber.getEmail()))
              {
                isSubscriber = true;
                break;
              }
            }
          }

          if (!isSubscriber)
          {
            selectableUsersFilterTwo.add(user);
          }
        }
      }

      view.getAddSubscribeWizard().getAddUserView().getUserTableSelectable().getUsersContainer()
          .setUsers(selectableUsersFilterTwo);
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e, view.getLocale());
    }
  }

  /**
   * Get a list contain only emails
   *
   * @param pMailingListUser
   *          the mailing list user
   * @return List<String>
   *         list of mails
   */
  private List<String> getMailStringList(final List<MailingListUser> pMailingListUser)
  {
    List<String> returned = null;
    if ((pMailingListUser != null) && (pMailingListUser.size() > 0))
    {
      returned = new ArrayList<String>();
      for (final MailingListUser result : pMailingListUser)
      {
        returned.add(result.getEmail());
      }
    }

    return returned;
  }

  /**
   * @param String
   *          pMailExternList
   * @return list of mail to add
   */
  private String removeDuplicateEmails(final String pMailExternList)
  {
    final String[] emailsArrayToAdd = pMailExternList.split(";");
    final List<String> emailListTolist = new LinkedList<String>(Arrays.asList(emailsArrayToAdd));

    final Set<String> set = new HashSet<String>();
    set.addAll(emailListTolist);
    final List<String> distinctList = new ArrayList<String>(set);
    String listString = "";

    for (final String string : distinctList)
    {
      listString += string.toLowerCase() + ";";
    }
    return listString;
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
  protected void refreshContent()
  {
    view.setListName(getCurrentLocale(), currentListname);

    view.attachSubscribersTable(false);
    view.getFilterTextField().setValue(""); //$NON-NLS-1$
    subscribersContainer.removeAllContainerFilters();

    subscribersContainer.setSubscribers(currentSubscribers);
    view.attachSubscribersTable(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);
  }

  /**
   * @param pItemId
   */
  public void onClickActionUnsubscribe(final Object pItemId)
  {
    selectedSubscriberId = (String) pItemId;
    if (isUserSelected())
    {
      UI.getCurrent().addWindow(view.getUnsubscribeUserWindow());
    }
    else
    {
      UI.getCurrent().addWindow(view.getUnsubscribeGroupWindow());
    }

  }

  private boolean isUserSelected()
  {
    return selectedSubscriberId.contains(EMAIL_SEPARATOR);
  }

  public void onClickActionSubscribe()
  {
    UI.getCurrent().addWindow(view.getSubscribeWindow());
    view.getAddSubscribeWizard().restartWizard();
  }

  /**
   * @return the subscribersContainer
   */
  public SubscribersContainer getSubscribersContainer()
  {
    return subscribersContainer;
  }

  public List<MailingListUser> getGroupMembers(final UUID pGroupUUID) throws MailingListServiceException
  {
    return MailingModule
        .getMailingListCategoryService()
        .getGroupSubscriber(null, getMailingListInstance().toString(), getCurrentUser(), currentListname,
            pGroupUUID).getMembers();
  }
}
