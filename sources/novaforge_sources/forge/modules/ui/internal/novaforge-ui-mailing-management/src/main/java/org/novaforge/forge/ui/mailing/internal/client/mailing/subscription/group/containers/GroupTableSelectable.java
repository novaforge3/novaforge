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
package org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.group.containers;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.plugins.categories.beans.MailingListGroupImpl;
import org.novaforge.forge.plugins.categories.beans.MailingListUserImpl;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.group.components.GroupColumnNameGenerator;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.group.components.GroupColumnSelectGenerator;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.group.components.GroupColumnUsersGenerator;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * This component should be used to display a filter and table for selecting group. All selecting user are
 * displyed on the right of table.
 * 
 * @author Guillaume Lamirand
 * @author Aimen Merkich
 */
public class GroupTableSelectable extends VerticalLayout
{
  /**
   * Serial version id
   */
  private static final long      serialVersionUID    = -1444548144654821860L;
  /**
   * Id for filter field
   */
  private static final String    FIELD_FILTER        = "filter";
  /**
   * Contains the {@link TextField} used to filter table
   */
  private TextField              filterTextField;
  /**
   * Contains the {@link Button} used to clean table selection
   */
  private Button                 clearButton;
  /**
   * Contains the {@link Form} used to format filter {@link TextField} and {@link Button}
   */
  private Form                   filterForm;
  /**
   * Contains the parent layout of user tables
   */
  private CssLayout              groupsTableLayout;
  /**
   * Contains {@link Table} which will displayed {@link User}
   */
  private Table                  groupsTable;
  /**
   * Represents {@link GroupContainer} used to store {@link User}
   */
  private GroupContainer         groupsContainer;
  /**
   * Will contains the selected groups
   */
  private List<MailingListGroup> selectedGroupsUsers = new LinkedList<MailingListGroup>();
  private VerticalLayout         selectedLayout;

  /**
   * Default constructor.
   */
  public GroupTableSelectable()
  {
    // Init contents
    final Component filter = initFilter();
    final Component content = initContent();
    addComponent(filter);
    addComponent(content);

    // Bind listener
    initGroupsList();
    addListeners();
  }

  private Component initFilter()
  {
    filterForm = new Form();
    filterForm.setImmediate(true);
    filterForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    clearButton = new Button();
    clearButton.setStyleName(NovaForge.BUTTON_LINK);
    filterForm.addField(FIELD_FILTER, filterTextField);
    filterForm.getFooter().addComponent(clearButton);
    return filterForm;
  }

  private Component initContent()
  {
    final HorizontalLayout layout = new HorizontalLayout();
    layout.setSpacing(true);
    layout.setWidth(100, Unit.PERCENTAGE);
    layout.setMargin(new MarginInfo(true, false, false, false));
    final Component tableContent    = initTable();
    final Component selectedContent = initSelected();
    layout.addComponent(tableContent);
    layout.addComponent(selectedContent);
    layout.setExpandRatio(tableContent, 0.8f);
    layout.setExpandRatio(selectedContent, 0.2f);
    return layout;
  }

  private void initGroupsList()
  {
    groupsContainer = new GroupContainer();
    groupsTable.setContainerDataSource(groupsContainer);

    groupsTable
        .addGeneratedColumn(GroupItemProperty.SELECT.getPropertyId(), new GroupColumnSelectGenerator());
    groupsTable.addGeneratedColumn(GroupItemProperty.NAME.getPropertyId(), new GroupColumnNameGenerator());
    groupsTable.addGeneratedColumn(GroupItemProperty.USERS.getPropertyId(), new GroupColumnUsersGenerator());
    // Define visibles columns
    groupsTable.setVisibleColumns(GroupItemProperty.SELECT.getPropertyId(),
        GroupItemProperty.NAME.getPropertyId(), GroupItemProperty.DESCRIPTION.getPropertyId(),
        GroupItemProperty.USERS.getPropertyId());
    // Define special column width
    groupsTable.setColumnExpandRatio(GroupItemProperty.NAME.getPropertyId(), 0.4f);
    groupsTable.setColumnExpandRatio(GroupItemProperty.DESCRIPTION.getPropertyId(), 0.6f);
    // groupsTable.setColumnExpandRatio(GroupItemProperty.USERS.getPropertyId(), 0.1f);
    groupsTable.setColumnWidth(GroupItemProperty.USERS.getPropertyId(), 90);
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    groupsTable.addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 6798250830949164100L;

      /**
       * {@inheritDoc}
       */
      @SuppressWarnings("unchecked")
      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {

        selectedLayout.removeAllComponents();

        for (final Object itemId : groupsContainer.getItemIds())
        {
          if (groupsTable.isSelected(itemId))
          {
            final Item item = groupsTable.getItem(itemId);
            final String groupName = (String) item.getItemProperty(GroupItemProperty.NAME.getPropertyId())
                .getValue();
            final Label label = new Label(groupName);
            final Group group = (Group) item.getItemProperty(GroupItemProperty.GROUP.getPropertyId())
                .getValue();
            final MailingListGroup mailingListGroup = new MailingListGroupImpl();
            final List<MailingListUser> subscribersToAdd = new LinkedList<MailingListUser>();
            mailingListGroup.setName(groupName);
            mailingListGroup.setUUID(group.getUuid());

            for (final User user : group.getUsers())
            {
              final MailingListUser mailingListUser = new MailingListUserImpl(user.getLogin(), user
                  .getEmail());
              subscribersToAdd.add(mailingListUser);
            }
            mailingListGroup.setMembers(subscribersToAdd);
            final Boolean isselected = contains(mailingListGroup);
            if (!isselected)
            {
              selectedGroupsUsers.add(mailingListGroup);
            }
            selectedLayout.addComponent(label);
          }
        }
      }
    });
    filterTextField.addTextChangeListener(new TextChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -1728083457370048943L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void textChange(final TextChangeEvent event)
      {
        // Clean filter
        groupsContainer.removeAllContainerFilters();
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter usersFilter = new Or(new SimpleStringFilter(GroupItemProperty.NAME.getPropertyId(),
              event.getText(), true, false), new SimpleStringFilter(GroupItemProperty.DESCRIPTION
              .getPropertyId(), event.getText(), true, false));
          groupsContainer.addContainerFilter(usersFilter);
        }
      }
    });
    clearButton.addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 1908099716804282434L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        attachGroupsTable(false);
        selectedGroupsUsers = new LinkedList<MailingListGroup>();
        for (final Object itemId : groupsContainer.getItemIds())
        {
          if (groupsTable.isSelected(itemId))
          {
            groupsTable.unselect(itemId);
          }
        }
        attachGroupsTable(true);

      }
    });

  }

  private Component initTable()
  {
    groupsTableLayout = new CssLayout();
    groupsTableLayout.setWidth(100, Unit.PERCENTAGE);
    groupsTable = new Table();
    groupsTable.setSelectable(false);
    groupsTable.setMultiSelect(true);
    groupsTable.setPageLength(8);
    groupsTable.setWidth(100, Unit.PERCENTAGE);
    groupsTable.setStyleName(Reindeer.TABLE_STRONG);
    groupsTableLayout.addComponent(groupsTable);
    return groupsTableLayout;
  }

  /**
   * @return
   */
  private Component initSelected()
  {
    selectedLayout = new VerticalLayout();
    selectedLayout.setMargin(new MarginInfo(false, false, false, true));
    return selectedLayout;
  }

  /**
   * @param pMailingListGroup
   * @return true if element is found in the list
   */
  public boolean contains(final MailingListGroup pMailingListGroup)
  {

    for (final MailingListGroup mailingListGroup : getSelectedGroup())
    {
      if (mailingListGroup.getUUID().toString().equalsIgnoreCase(pMailingListGroup.getUUID().toString()))
      {
        return (true);
      }
    }

    return (false);
  }

  /**
   * Attach or detach table to its parent
   *
   * @param pAttach
   *          true to attache, false to detach
   */
  public void attachGroupsTable(final boolean pAttach)
  {
    if (pAttach)
    {
      groupsTable.refreshRowCache();
      groupsTableLayout.addComponent(groupsTable);
    }
    else
    {
      groupsTableLayout.removeAllComponents();
    }

  }

  /**
   * Return {@link List} of group selecting
   *
   * @return {@link List} of group selecting
   */
  public List<MailingListGroup> getSelectedGroup()
  {
    return selectedGroupsUsers;
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
   * Should be called to refresh view according to the {@link Locale} given
   *
   * @param pLocale
   *          the new locale
   */
  public void refreshLocale(final Locale pLocale)
  {
    // TODO
    filterForm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_GROUP_LIST_TITLE));
    filterForm.setDescription(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_GROUP_LIST_DESCRIPTION));
    filterTextField.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_FILTER));
    clearButton.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_USERS_LIST_CANCEL));
    groupsTable.setColumnHeader(GroupItemProperty.NAME.getPropertyId(), MailingModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_NAME));
    groupsTable.setColumnHeader(GroupItemProperty.DESCRIPTION.getPropertyId(), MailingModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_DESCRIPTION));
    groupsTable.setColumnHeader(GroupItemProperty.USERS.getPropertyId(), MailingModule.getPortalMessages()
        .getMessage(pLocale, Messages.MAILING_LISTS_GROUP_GLOBAL_USERS));
    selectedLayout.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_GROUP_LIST_SELECTED));
  }

  /**
   * Should be called to clear container and field
   */
  public void clear()
  {
    selectedGroupsUsers = new LinkedList<MailingListGroup>();
    filterTextField.setValue("");
    groupsContainer.removeAllContainerFilters();
    selectedLayout.removeAllComponents();
    for (final Object itemId : groupsContainer.getItemIds())
    {
      if (groupsTable.isSelected(itemId))
      {
        groupsTable.unselect(itemId);
      }
    }

  }

  /**
   * Return {@link GroupContainer} used to store {@link MailingListGroup}
   *
   * @return {@link GroupContainer} used to store {@link MailingListGroup}
   */
  public GroupContainer getGroupsContainer()
  {
    return groupsContainer;
  }

  /**
   * Return {@link TextField} used to filter user's table
   *
   * @return {@link TextField} used to filter user's table
   */
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /**
   * Return {@link Button} used to clear table selection
   *
   * @return {@link Button} used to clear table selection
   */
  public Button getClearButton()
  {
    return clearButton;
  }

  /**
   * Return {@link Table} containing groups
   *
   * @return {@link Table} containing groups
   */
  public Table getGroupsTable()
  {
    return groupsTable;
  }

}
