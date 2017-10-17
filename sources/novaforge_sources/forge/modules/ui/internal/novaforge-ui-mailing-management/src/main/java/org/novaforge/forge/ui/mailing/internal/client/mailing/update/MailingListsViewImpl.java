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

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;

import java.util.Collection;
import java.util.Locale;

/**
 * This view is used to display mailing lists table
 * 
 * @author B-Martinelli
 */
public class MailingListsViewImpl extends VerticalLayout implements MailingListsView
{
  /** Serialization id */
  private static final long   serialVersionUID = -4420222948069676402L;

  /** {@link Field} id */
  private static final String FILTER           = "filter";

  /** The {@link Button} to create a new mailing list */
  private Button              createButton;

  /** The header layout */
  private HorizontalLayout    headerButtons;

  /** The {@link TextField} used to filter {@link MailingListsViewImpl#mailingListsTable} */
  private TextField           filterTextField;

  /** The {@link Table} which will display mailing lists */
  private Table               mailingListsTable;

  /** The {@link Form} containing the {@link MailingListsViewImpl#mailingListsTable} */
  private Form                mailingListsForm;

  /** Css for mailing lists table layout. */
  private CssLayout           mailingListsTableLayout;

  /** The {@link DeleteConfirmWindow} which will allows to delete a mailinglist */
  private DeleteConfirmWindow closeListWindow;
  /** Pop-up to confirm/cancel unsubscribe action */
  private Window              unsubscribeWindow;
  /** button to confirm unsubscribe user */
  private Button              unsubscribeConfirm;

  /** button to cancel unsubscribe user */
  private Button              unsubscribeCancel;
  private Label               unsubscribeConfirmLabel;

  /** Default constructor. */
  public MailingListsViewImpl()
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

    // Init subwindows
    initCloseListWindow();
    initUnsubscribeWindow();
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
    createButton = new Button();
    createButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));

    headerButtons.addComponent(createButton);
    return headerButtons;
  }

  /**
   * Init the filter form
   * 
   * @return {@link Component} created
   */
  private Component initFilter()
  {
    mailingListsForm = new Form();
    mailingListsForm.setImmediate(true);
    mailingListsForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    mailingListsForm.addField(FILTER, filterTextField);
    return mailingListsForm;
  }

  /**
   * Init the main content
   * 
   * @return {@link Component} created
   */
  private Component initContent()
  {
    mailingListsTableLayout = new CssLayout();
    mailingListsTableLayout.setWidth(100, Unit.PERCENTAGE);
    mailingListsTable = new Table();
    mailingListsTable.setPageLength(15);
    mailingListsTable.setWidth(100, Unit.PERCENTAGE);
    mailingListsTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      /** Serial version id */
      private static final long serialVersionUID = -3559455760887782118L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {
        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if ((child != null) && (!child.equals(mailingListsTable)) && (!child.equals(mailingListsTableLayout)))
        {
          final Collection<?> itemIds = mailingListsTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (mailingListsTable.isSelected(itemId))
            {
              mailingListsTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    mailingListsTableLayout.addComponent(mailingListsTable);
    return mailingListsTableLayout;
  }

  /**
   * Init the window used to delete group
   */
  private void initCloseListWindow()
  {
    closeListWindow = new DeleteConfirmWindow(Messages.MAILING_LISTS_CLOSE_CONFIRM);
    closeListWindow.setWidth(450, Unit.PIXELS);
  }

  private void initUnsubscribeWindow()
  {
    unsubscribeWindow = new Window();
    unsubscribeWindow.setModal(true);
    unsubscribeWindow.setResizable(false);

    unsubscribeConfirmLabel = new Label();
    unsubscribeConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(400, Unit.PIXELS);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    unsubscribeConfirm = new Button();
    unsubscribeCancel = new Button();
    unsubscribeCancel.setStyleName(NovaForge.BUTTON_LINK);
    unsubscribeConfirm.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_DELETE));
    unsubscribeConfirm.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(unsubscribeCancel);
    buttons.addComponent(unsubscribeConfirm);
    buttons.setComponentAlignment(unsubscribeCancel, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(unsubscribeConfirm, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(unsubscribeConfirmLabel);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(unsubscribeConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    unsubscribeWindow.setContent(windowLayout);
  }

  /** {@inheritDoc} */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  /** {@inheritDoc} */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    createButton.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_CREATE));
    mailingListsForm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_SHOW_LISTS));
    filterTextField
        .setCaption(MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_FILTER));
    mailingListsTable.setColumnHeader(MailingListItemProperty.NAME.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_NAME));
    mailingListsTable.setColumnHeader(MailingListItemProperty.DESCRIPTION.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_DESCRIPTION));
    mailingListsTable.setColumnHeader(MailingListItemProperty.OWNERS.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_OWNERS));
    mailingListsTable.setColumnHeader(MailingListItemProperty.IS_SUBSCRIBER.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_SUBSCRIBTION));
    mailingListsTable.setColumnHeader(MailingListItemProperty.SUBSCRIBERS_NB.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_SUBSCRIBERS));

    mailingListsTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    unsubscribeWindow.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_LIST_PRIVATE_TITLE));
    unsubscribeCancel.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    unsubscribeConfirm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_UNSUBSCRIBE_LIST_PRIVATE_TITLE));

    closeListWindow.refreshLocale(pLocale);
  }

  /** {@inheritDoc} */
  @Override
  public Button getCreateButton()
  {
    return createButton;
  }

  /** {@inheritDoc} */
  @Override
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /** {@inheritDoc} */
  @Override
  public Table getMailingListsTable()
  {
    return mailingListsTable;
  }

  /** {@inheritDoc} */
  @Override
  public void attachMailinglistsTable(final boolean pAttach)
  {
    if (pAttach)
    {
      mailingListsTable.refreshRowCache();
      mailingListsTableLayout.addComponent(mailingListsTable);
    }
    else
    {
      mailingListsTableLayout.removeAllComponents();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getCloseListWindow()
  {
    return closeListWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAdminVisibility(final boolean pIsAdmin)
  {
    headerButtons.setVisible(pIsAdmin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUnsubscribeCancel()
  {
    return unsubscribeCancel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUnsubscribeConfirm()
  {
    return unsubscribeConfirm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUnsubscribeConfirmLabel()
  {
    return unsubscribeConfirmLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getUnsubscribeWindow()
  {
    return unsubscribeWindow;
  }

}
