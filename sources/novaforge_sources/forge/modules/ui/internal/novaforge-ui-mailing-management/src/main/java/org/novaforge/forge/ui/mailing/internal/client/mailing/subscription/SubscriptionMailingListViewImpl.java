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

import com.google.common.base.Strings;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.wizard.components.AddSubscribeWizard;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;

import java.util.Collection;
import java.util.Locale;

/**
 * @author sbenoist
 * @author Aimen Merkich
 */
public class SubscriptionMailingListViewImpl extends VerticalLayout implements SubscriptionMailingListView
{

  /**
   * Serial Version UID
   */
  private static final long   serialVersionUID = -4304269962588603249L;

  /** {@link Field} id */
  private static final String FILTER           = "filter";

  /** The {@link TextField} used to filter {@link SubscriptionMailingListViewImpl#subscribersTable} */
  private TextField           filterTextField;

  /** The {@link Button} to subscribe a new subscriber */
  private Button              subscribeButton;

  private Button              returnButton;

  /** The header layout */
  private HorizontalLayout    headerButtons;

  /** The {@link Table} which will display subscribers table */
  private Table               subscribersTable;

  /** The {@link Form} containing the {@link SubscriptionMailingListViewImpl#subscribersTable} */
  private Form                subscribersForm;

  /** Css for subscribers table layout. */
  private CssLayout           subscribersTableLayout;

  /** Pop-up to confirm/cancel unsubscribe action */
  private Window              unsubscribeUserWindow;

  /** Pop-up to confirm/cancel unsubscribe action */
  private Window              subscribeWindow;

  /** repository wizard */
  private AddSubscribeWizard  addSubscribeWizard;

  /** button to confirm unsubscribe user */
  private Button              unsubscribeUserConfirm;

  /** button to cancel unsubscribe user */
  private Button              unsubscribeUserCancel;

  /** checkbox to notify user */
  private CheckBox            notificationUserChbx;

  private Label               unsubscribeUserConfirmLabel;

  /** Pop-up to confirm/cancel unsubscribe group action */
  private Window              unsubscribeGroupWindow;

  /** button to confirm unsubscribe group */
  private Button              unsubscribeGroupConfirm;

  /** button to cancel unsubscribe group */
  private Button              unsubscribeGroupCancel;

  /** checkbox to notify group members */
  private CheckBox            notificationGroupChbx;

  private Label               unsubscribeGroupConfirmLabel;

  private String              currentListname;

  public SubscriptionMailingListViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component headers = initHeaders();
    final Component filter = initFilter();
    final Component content = initContent();
    addComponent(headers);
    addComponent(filter);
    addComponent(content);

    // Pop-up
    initSubscribeWindow();
    initUnsubscribeUserWindow();
    initUnsubscribeGroupWindow();
  }

  /**
   * Init the headers component
   *
   * @return {@link Component} created
   */
  private Component initHeaders()
  {
    headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));

    // Add return button
    returnButton = new Button();
    returnButton.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));

    // Add subscribe button
    subscribeButton = new Button();
    subscribeButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    subscribeButton.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_ADD));

    headerButtons.addComponent(returnButton);
    headerButtons.addComponent(subscribeButton);
    return headerButtons;
  }

  /**
   * Init the filter form
   *
   * @return {@link Component} created
   */
  private Component initFilter()
  {
    subscribersForm = new Form();
    subscribersForm.setImmediate(true);
    subscribersForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    subscribersForm.addField(FILTER, filterTextField);
    return subscribersForm;
  }

  /**
   * Init the main content
   *
   * @return {@link Component} created
   */
  private Component initContent()
  {
    subscribersTableLayout = new CssLayout();
    subscribersTableLayout.setWidth(100, Unit.PERCENTAGE);
    subscribersTable = new Table();
    subscribersTable.setPageLength(10);
    subscribersTable.setWidth(100, Unit.PERCENTAGE);
    subscribersTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      private static final long serialVersionUID = 1L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {
        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if ((child != null) && (!child.equals(subscribersTable)) && (!child.equals(subscribersTableLayout)))
        {
          final Collection<?> itemIds = subscribersTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (subscribersTable.isSelected(itemId))
            {
              subscribersTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    subscribersTableLayout.addComponent(subscribersTable);
    return subscribersTableLayout;
  }

  private void initSubscribeWindow()
  {
    subscribeWindow = new Window();
    addSubscribeWizard = new AddSubscribeWizard();
    subscribeWindow.setModal(true);
    subscribeWindow.setResizable(false);
    subscribeWindow.center();
    subscribeWindow.setWidth(800, Unit.PIXELS);
    subscribeWindow.setHeight(600, Unit.PIXELS);
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.addComponent(addSubscribeWizard);
    windowLayout.setSizeFull();
    subscribeWindow.setContent(windowLayout);

  }

  private void initUnsubscribeUserWindow()
  {
    unsubscribeUserWindow = new Window();
    unsubscribeUserWindow.setModal(true);
    unsubscribeUserWindow.setResizable(false);

    unsubscribeUserConfirmLabel = new Label();
    unsubscribeUserConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(400, Unit.PIXELS);

    // Init notification bos
    notificationUserChbx = new CheckBox();
    notificationUserChbx.setValue(true);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    unsubscribeUserConfirm = new Button();
    unsubscribeUserCancel = new Button();
    unsubscribeUserCancel.setStyleName(NovaForge.BUTTON_LINK);
    unsubscribeUserConfirm.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_DELETE));
    unsubscribeUserConfirm.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(unsubscribeUserCancel);
    buttons.addComponent(unsubscribeUserConfirm);
    buttons.setComponentAlignment(unsubscribeUserCancel, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(unsubscribeUserConfirm, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(unsubscribeUserConfirmLabel);
    windowLayout.addComponent(notificationUserChbx);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(unsubscribeUserConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(notificationUserChbx, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    unsubscribeUserWindow.setContent(windowLayout);
  }

  private void initUnsubscribeGroupWindow()
  {
    unsubscribeGroupWindow = new Window();
    unsubscribeGroupWindow.setModal(true);
    unsubscribeGroupWindow.setResizable(false);

    unsubscribeGroupConfirmLabel = new Label();
    unsubscribeGroupConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(400, Unit.PIXELS);

    // Init notification bos
    notificationGroupChbx = new CheckBox();
    notificationGroupChbx.setValue(true);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    unsubscribeGroupConfirm = new Button();
    unsubscribeGroupCancel = new Button();
    unsubscribeGroupCancel.setStyleName(NovaForge.BUTTON_LINK);
    unsubscribeGroupConfirm.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_DELETE));
    unsubscribeGroupConfirm.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(unsubscribeGroupCancel);
    buttons.addComponent(unsubscribeGroupConfirm);
    buttons.setComponentAlignment(unsubscribeGroupCancel, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(unsubscribeGroupConfirm, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(unsubscribeGroupConfirmLabel);
    windowLayout.addComponent(notificationGroupChbx);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(unsubscribeGroupConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(notificationGroupChbx, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    unsubscribeGroupWindow.setContent(windowLayout);
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
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    subscribeButton.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_SUBSCRIBERS_ADD));
    returnButton.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_BACK));

    if (!Strings.isNullOrEmpty(currentListname))
    {
      subscribersForm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
          Messages.MAILING_LISTS_SUBSCRIBERS_LIST, currentListname));
    }
    filterTextField.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_SUBSCRIBERS_FILTER));
    subscribersTable.setColumnHeader(MailingListItemProperty.SUBSCRIBER.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_SUBSCRIBERS));

    subscribersTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    unsubscribeUserWindow.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_TITLE));
    unsubscribeUserConfirmLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_CONFIRM));
    unsubscribeUserCancel.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    notificationUserChbx.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_NOTIFY));
    unsubscribeUserConfirm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_CONFIRM_TITLE));

    unsubscribeGroupWindow.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_GROUP_TITLE));
    unsubscribeGroupConfirmLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_GROUP_CONFIRM));
    unsubscribeGroupCancel.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    notificationGroupChbx.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_GROUP_NOTIFY));
    unsubscribeGroupConfirm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_GROUP_CONFIRM_TITLE));

    subscribeWindow.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_SUBSCRIBE_TITLE));
    addSubscribeWizard.refreshLocale(pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getSubscribeButton()
  {
    return subscribeButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getSubscribersTable()
  {
    return subscribersTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attachSubscribersTable(final boolean pAttach)
  {
    if (pAttach)
    {
      subscribersTable.refreshRowCache();
      subscribersTableLayout.addComponent(subscribersTable);
    }
    else
    {
      subscribersTableLayout.removeAllComponents();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getReturnButton()
  {
    return returnButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUnsubscribeUserCancel()
  {
    return unsubscribeUserCancel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUnsubscribeUserConfirm()
  {
    return unsubscribeUserConfirm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getUnsubscribeUserWindow()
  {
    return unsubscribeUserWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getUnsubscribeGroupWindow()
  {
    return unsubscribeGroupWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUnsubscribeGroupCancel()
  {
    return unsubscribeGroupCancel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUnsubscribeGroupConfirm()
  {
    return unsubscribeGroupConfirm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getSubscribeWindow()
  {
    return subscribeWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getNotificationUserChbx()
  {
    return notificationUserChbx;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getNotificationGroupChbx()
  {
    return notificationGroupChbx;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AddSubscribeWizard getAddSubscribeWizard()
  {
    return addSubscribeWizard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setListName(final Locale pLocale, final String pCurrentListname)
  {
    currentListname = pCurrentListname;
    if (!Strings.isNullOrEmpty(currentListname))
    {
      subscribersForm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.MAILING_LISTS_SUBSCRIBERS_LIST,
                                                                              currentListname));
    }
  }

}
