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
package org.novaforge.forge.ui.user.management.internal.client.admin.blacklist;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.util.Collection;
import java.util.Locale;

/**
 * @author caseryj
 */
public class BlackListUsersViewImpl extends VerticalLayout implements BlackListUsersView
{

  /**
   * Default serial version UID
   */
  private static final long   serialVersionUID = 3222623093278417870L;

  /**
   * Filter constant for field
   */
  private static final String FILTER_FIELD     = "filter";

  /**
   * {@link Field} used to filter the table
   */
  private TextField           filterTextField;
  /**
   * {@link Form} containing filter and its table
   */
  private Form                usersForm;
  /**
   * The {@link Table} containing the users list
   */
  private Table               usersTable;

  /**
   * Default constructor
   */
  public BlackListUsersViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component filter = initFilter();
    final Component content = initContent();
    addComponent(filter);
    addComponent(content);
  }

  /**
   * Initialize the filter component
   * 
   * @return {@link Component} containing the filter component
   */
  private Component initFilter()
  {
    usersForm = new Form();
    usersForm.setImmediate(true);
    usersForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    filterTextField.setImmediate(true);
    usersForm.addField(FILTER_FIELD, filterTextField);
    return usersForm;
  }

  /**
   * Initialize the content components
   * 
   * @return {@link Component} containing the content components
   */
  private Component initContent()
  {
    usersTable = new Table();
    usersTable.setSelectable(true);
    usersTable.setPageLength(10);
    usersTable.setWidth(100, Unit.PERCENTAGE);
    usersTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3559455760887782118L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(final LayoutClickEvent event)
      {

        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if (child == null || !child.equals(usersTable))
        {
          final Collection<?> itemIds = usersTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (usersTable.isSelected(itemId))
            {
              usersTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    return usersTable;
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
    usersForm.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
        Messages.USERMANAGEMENT_ADMIN_TITLE_BLACKLIST));
    filterTextField.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
        Messages.USERMANAGEMENT_FILTER_FIELDS));
    usersTable.setColumnHeader(UserItemProperty.LOGIN.getPropertyId(), AdminModule.getPortalMessages()
        .getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_LOGIN));
    usersTable.setColumnHeader(UserItemProperty.EMAIL.getPropertyId(), AdminModule.getPortalMessages()
        .getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_EMAIL));
    usersTable.setColumnHeader(UserItemProperty.CREATED_DATE.getPropertyId(), AdminModule.getPortalMessages()
        .getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_CREATED_DATE));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getUsersTable()
  {
    return usersTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

}
