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
package org.novaforge.forge.ui.memberships.internal.client.groups.add;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.groups.components.GroupFieldFactory;
import org.novaforge.forge.ui.memberships.internal.client.users.components.UserTableSelectable;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class AddGroupViewImpl extends VerticalLayout implements AddGroupView
{

  /**
   * Serialization id
   */
  private static final long   serialVersionUID = 6746998286381149587L;

  private Button              returnButton;
  private Form                groupForm;
  private UserTableSelectable userTableSelectable;
  private Button              addButton;

  private GroupFieldFactory   fieldFactory;

  /**
   * Default constructor.
   */
  public AddGroupViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component headers = initHeaders();
    final Component groupContent = initGroupContent();
    final Component userContent = initUserContent();
    final Component footer = initFooter();
    addComponent(headers);
    addComponent(groupContent);
    addComponent(userContent);
    addComponent(footer);

  }

  private Component initHeaders()
  {
    final HorizontalLayout headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    returnButton = new Button();
    returnButton.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));

    headerButtons.addComponent(returnButton);
    return headerButtons;
  }

  private Component initGroupContent()
  {
    groupForm = new Form();
    // FIXME
    // groupForm.setWriteThrough(false);
    groupForm.setImmediate(true);
    groupForm.setInvalidCommitted(false);
    fieldFactory = new GroupFieldFactory();
    groupForm.setFormFieldFactory(fieldFactory);

    return groupForm;
  }

  private Component initUserContent()
  {
    userTableSelectable = new UserTableSelectable();
    userTableSelectable.getUsersTable().setPageLength(5);
    return userTableSelectable;
  }

  private Component initFooter()
  {
    final HorizontalLayout footerButtons = new HorizontalLayout();
    footerButtons.setSpacing(true);
    footerButtons.setMargin(new MarginInfo(true, false, true, true));
    addButton = new Button();
    addButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    addButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));

    footerButtons.addComponent(addButton);
    return footerButtons;
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
        Messages.MEMBERSHIPS_GROUPS_ADD_BACK));
    groupForm.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_INFO));
    if (fieldFactory.getName() != null)
    {
      fieldFactory.getName().setCaption(
          MembershipsModule.getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_NAME));
      fieldFactory.getName().setRequiredError(
          MembershipsModule.getPortalMessages()
              .getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_NAME_REQUIRED));
      fieldFactory.getName().removeAllValidators();
      fieldFactory.getName().addValidator(
          new StringLengthValidator(MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_GROUPS_NAME_REQUIRED), 3, 40, false));
    }
    if (fieldFactory.getDescription() != null)
    {
      fieldFactory.getDescription().setCaption(
          MembershipsModule.getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GROUPS_DESCRIPTION));
      fieldFactory.getDescription().setRequiredError(
          MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_GROUPS_DESCRIPTION_REQUIRED));
      fieldFactory.getDescription().removeAllValidators();
      fieldFactory.getDescription().addValidator(
          new StringLengthValidator(MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_GROUPS_DESCRIPTION_REQUIRED), 3, 250, false));
    }
    if (fieldFactory.getVisibility() != null)
    {
      fieldFactory.getVisibility().setCaption(
          MembershipsModule.getPortalMessages().getMessage(pLocale,
              Messages.MEMBERSHIPS_GROUPS_EDIT_MAKEPUBLIC));
    }

    userTableSelectable.refreshLocale(pLocale);
    addButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GROUPS_ADD_CREATE));

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
  public Button getAddButton()
  {
    return addButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getGroupForm()
  {
    return groupForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserTableSelectable getUserTableSelectable()
  {
    return userTableSelectable;
  }

}
