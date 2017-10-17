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
package org.novaforge.forge.ui.memberships.internal.client.request.history;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.RequestItemProperty;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.util.Collection;
import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class RequestHistoryViewImpl extends VerticalLayout implements RequestHistoryView
{

  /**
   * Filter constant for field
   */
  private static final String FILTER_FIELD     = "filter";

  /**
   * Serialization id
   */
  private static final long   serialVersionUID = -4420222948069676402L;

  private HorizontalLayout    headerButtons;
  private Button              returnButton;
  private TextField           filterTextField;
  private Form                requestForm;
  private Table               requestsTable;

  /**
   * Default constructor.
   */
  public RequestHistoryViewImpl()
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
  }

  private Component initHeaders()
  {
    headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    returnButton = new Button();
    returnButton.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));

    headerButtons.addComponent(returnButton);
    return headerButtons;
  }

  private Component initFilter()
  {
    requestForm = new Form();
    requestForm.setImmediate(true);
    requestForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    requestForm.addField(FILTER_FIELD, filterTextField);
    return requestForm;
  }

  private Component initContent()
  {
    requestsTable = new Table();
    requestsTable.setPageLength(10);
    requestsTable.setWidth(100, Unit.PERCENTAGE);
    requestsTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3559455760887782118L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {

        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if ((child == null) || (!child.equals(requestsTable)))
        {
          final Collection<?> itemIds = requestsTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (requestsTable.isSelected(itemId))
            {
              requestsTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    return requestsTable;
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
    returnButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_REQUESTS_HISTORY_BACK));
    requestForm.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_REQUESTS_HISTORY_TITLE));
    requestForm.setDescription(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_REQUESTS_HISTORY_DESCRIPTION));
    filterTextField.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_FILTER));
    requestsTable.setColumnHeader(RequestItemProperty.LOGIN.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_USERS_LOGIN));
    requestsTable.setColumnHeader(RequestItemProperty.FIRSTNAME.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_USERS_FIRSTNAME));
    requestsTable.setColumnHeader(RequestItemProperty.LASTNAME.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_USERS_NAME));
    requestsTable.setColumnHeader(RequestItemProperty.DATE.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_REQUESTS_HISTORY_DATE));
    requestsTable.setColumnHeader(RequestItemProperty.MESSAGE.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_REQUESTS_MESSAGE));

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
  public Table getRequestsTable()
  {
    return requestsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getReturnButton()
  {
    return returnButton;
  }

}
