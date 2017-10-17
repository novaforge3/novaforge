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
package org.novaforge.forge.ui.applications.internal.client.spaces;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.applications.internal.client.spaces.components.SpaceFieldFactory;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class SpacesViewImpl extends VerticalLayout implements SpacesView
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID            = -5650066717157280916L;
  /**
   * Min lenght for the field name.
   */
  public static int SPACE_NAME_FIELD_MIN_LENGHT = 3;
  /**
   * Max lenght for the field name.
   */
  public static int SPACE_NAME_FIELD_MAX_LENGHT = 50;
  private Form              spaceForm;
  private SpaceFieldFactory fieldFactory;
  private Button            applyButton;
  private Button            editButton;
  private Button            delButton;
  private HorizontalLayout  headerButtons;
  private Button            cancelButton;
  private Button            addAppButton;

  /**
   * Default constructor.
   */
  public SpacesViewImpl()
  {
    setMargin(true);
    initHeaders();
    initForm();

  }

  private void initHeaders()
  {
    headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    addAppButton = new Button();
    addAppButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    addAppButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));

    editButton = new Button();
    editButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    editButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));

    delButton = new Button();
    delButton.setIcon(new ThemeResource(NovaForgeResources.ICON_TRASH_RED));

    headerButtons.addComponent(getAddAppButton());
    headerButtons.addComponent(editButton);
    headerButtons.addComponent(delButton);
    addComponent(headerButtons);
  }

  private void initForm()
  {
    spaceForm = new Form();
    // FIXME
    // spaceForm.setWriteThrough(false);
    spaceForm.setImmediate(true);
    spaceForm.setInvalidCommitted(false);

    fieldFactory = new SpaceFieldFactory();
    spaceForm.setFormFieldFactory(fieldFactory);

    // The cancel / apply buttons
    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    applyButton = new Button();
    applyButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    cancelButton = new Button();
    cancelButton.setStyleName(NovaForge.BUTTON_LINK);
    buttons.addComponent(cancelButton);
    buttons.addComponent(applyButton);
    buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(applyButton, Alignment.MIDDLE_CENTER);
    spaceForm.getFooter().addComponent(buttons);
    addComponent(spaceForm);
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
  public Form getSpaceForm()
  {
    return spaceForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SpaceFieldFactory getFieldFactory()
  {
    return fieldFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCancelButton()
  {
    return cancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getApplyButton()
  {
    return applyButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditButton()
  {
    return editButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDelButton()
  {
    return delButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalLayout getHeaderLayout()
  {
    return headerButtons;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    spaceForm.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.APPLICATIONS_SPACES_TITLE));
    if (fieldFactory.getName() != null)
    {
      fieldFactory.getName().setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                          Messages.APPLICATIONS_SPACES_NAME));
      fieldFactory.getName().removeAllValidators();
      final StringLengthValidator nameValidator = new StringLengthValidator(ApplicationsModule.getPortalMessages()
                                                                                              .getMessage(pLocale,
                                                                                                          Messages.APPLICATIONS_SPACES_NAME_REQUIRED,
                                                                                                          SPACE_NAME_FIELD_MIN_LENGHT,
                                                                                                          SPACE_NAME_FIELD_MAX_LENGHT),
                                                                            SPACE_NAME_FIELD_MIN_LENGHT,
                                                                            SPACE_NAME_FIELD_MAX_LENGHT, false);
      fieldFactory.getName().addValidator(nameValidator);
      fieldFactory.getName().setRequiredError(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                                Messages.APPLICATIONS_SPACES_NAME_REQUIRED,
                                                                                                nameValidator
                                                                                                    .getMinLength(),
                                                                                                nameValidator
                                                                                                    .getMaxLength()));

    }
    if (fieldFactory.getDescription() != null)
    {
      fieldFactory.getDescription().setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                                 Messages.APPLICATIONS_SPACES_DESCRIPTION));

    }
    addAppButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.APPLICATIONS_SPACES_APPLICATION_CREATE));
    editButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                            Messages.APPLICATIONS_SPACES_EDIT));
    delButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.APPLICATIONS_SPACES_DELETE));
    cancelButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale, Messages.ACTIONS_CANCEL));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAddAppButton()
  {
    return addAppButton;
  }

}
