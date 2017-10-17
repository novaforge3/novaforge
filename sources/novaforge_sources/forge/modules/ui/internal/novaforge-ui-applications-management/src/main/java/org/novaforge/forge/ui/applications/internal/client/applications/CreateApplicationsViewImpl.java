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
package org.novaforge.forge.ui.applications.internal.client.applications;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.applications.internal.client.applications.components.ApplicationFormProperty;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.data.validator.NameValidator;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
@SuppressWarnings("deprecation")
public class CreateApplicationsViewImpl extends VerticalLayout implements CreateApplicationsView
{

  /**
   * Serial version id
   */
  private static final long   serialVersionUID          = -5650066717157280916L;

  /**
   * TODO used globale constant
   * Default representation for <code>null</code> value.
   */
  private static final String NULL_REPRESENTATION       = "";
  /**
   * Min lenght for the field name.
   */
  public static int APP_NAME_FIELD_MIN_LENGHT = 3;
  /**
   * Max lenght for the field name.
   */
  public static int APP_NAME_FIELD_MAX_LENGHT = 25;
  private Form             appForm;
  private Button           applyButton;
  private Button           cancelButton;
  private TextField        nameField;
  private TextArea         descriptionField;
  private ComboBox         categoriesBox;
  private ComboBox         typesBox;
  private Form             rolesForm;
  private HorizontalLayout footer;

  /**
   * Default constructor.
   */
  public CreateApplicationsViewImpl()
  {
    setMargin(true);

    final Component application = initApplication();
    final Component roles       = initRolesForm();
    final Component footer      = initFooter();

    addComponent(application);
    addComponent(roles);
    addComponent(footer);
    setComponentAlignment(roles, Alignment.TOP_LEFT);
    setComponentAlignment(footer, Alignment.TOP_LEFT);
  }

  private Component initApplication()
  {
    appForm = new Form();
    appForm.setImmediate(true);
    appForm.setInvalidCommitted(false);
    appForm.setFooter(null);

    // init fields
    categoriesBox = new ComboBox();
    categoriesBox.setWidth(250, Unit.PIXELS);
    categoriesBox.setRequired(true);
    categoriesBox.setNullSelectionAllowed(true);
    categoriesBox.setTextInputAllowed(true);
    categoriesBox.setWidth(NovaForge.FORM_FIELD_WIDTH);

    typesBox = new ComboBox();
    typesBox.setWidth(NovaForge.FORM_FIELD_WIDTH);
    typesBox.setRequired(true);
    typesBox.setNullSelectionAllowed(true);
    typesBox.setTextInputAllowed(true);

    nameField = new TextField();
    nameField.setNullRepresentation(NULL_REPRESENTATION);
    nameField.setRequired(true);
    nameField.setWidth(NovaForge.FORM_FIELD_WIDTH);

    descriptionField = new TextArea();
    descriptionField.setNullRepresentation(NULL_REPRESENTATION);
    descriptionField.setWidth(NovaForge.FORM_FIELD_WIDTH);

    appForm.addField(ApplicationFormProperty.CATEGORY.getPropertyId(), categoriesBox);
    appForm.addField(ApplicationFormProperty.TYPE.getPropertyId(), typesBox);
    appForm.addField(ApplicationFormProperty.NAME.getPropertyId(), nameField);
    appForm.addField(ApplicationFormProperty.DESCRIPTION.getPropertyId(), descriptionField);

    return appForm;
  }

  private Component initRolesForm()
  {
    rolesForm = new Form();
    rolesForm.setImmediate(true);
    rolesForm.setInvalidCommitted(false);
    rolesForm.setFooter(null);
    return rolesForm;
  }

  private Component initFooter()
  {
    footer = new HorizontalLayout();
    footer.setSpacing(true);
    footer.setMargin(new MarginInfo(true, false, false, false));
    applyButton = new Button();
    applyButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    applyButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    cancelButton = new Button();
    cancelButton.setStyleName(NovaForge.BUTTON_LINK);
    footer.addComponent(cancelButton);
    footer.addComponent(applyButton);
    footer.setComponentAlignment(cancelButton, Alignment.MIDDLE_LEFT);
    footer.setComponentAlignment(applyButton, Alignment.MIDDLE_LEFT);
    return footer;
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
  public Form getApplicationForm()
  {
    return appForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getNameField()
  {
    return nameField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextArea getDescriptionField()
  {
    return descriptionField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getCategoriesBox()
  {
    return categoriesBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getTypesBox()
  {
    return typesBox;
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
  public Form getRolesForm()
  {
    return rolesForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    appForm.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                         Messages.APPLICATIONS_APPLICATION_TITLE));
    if (categoriesBox != null)
    {
      categoriesBox.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                 Messages.APPLICATIONS_APPLICATION_CATEGORY));
      categoriesBox.setInputPrompt(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                     Messages.APPLICATIONS_APPLICATION_CATEGORY_INPUTPROMPT));
      categoriesBox.setRequiredError(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                       Messages.APPLICATIONS_APPLICATION_CATEGORY_REQUIRED));

    }
    if (typesBox != null)
    {
      typesBox.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                            Messages.APPLICATIONS_APPLICATION_TYPE));
      typesBox.setInputPrompt(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.APPLICATIONS_APPLICATION_TYPE_INPUTPROMPT));
      typesBox.setRequiredError(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                  Messages.APPLICATIONS_APPLICATION_TYPE_REQUIRED));
    }
    if (nameField != null)
    {
      nameField.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.APPLICATIONS_APPLICATION_NAME));
      nameField.removeAllValidators();
      nameField.addValidator(new NameValidator(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                                 Messages.APPLICATIONS_APPLICATION_NAME_REQUIRED,
                                                                                                 APP_NAME_FIELD_MIN_LENGHT,
                                                                                                 APP_NAME_FIELD_MAX_LENGHT),
                                               APP_NAME_FIELD_MIN_LENGHT, APP_NAME_FIELD_MAX_LENGHT));
      nameField.setRequiredError(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                   Messages.APPLICATIONS_APPLICATION_NAME_REQUIRED,
                                                                                   APP_NAME_FIELD_MIN_LENGHT,
                                                                                   APP_NAME_FIELD_MAX_LENGHT));
    }
    if (descriptionField != null)
    {
      descriptionField.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                    Messages.APPLICATIONS_APPLICATION_DESCRIPTION));
    }
    rolesForm.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.APPLICATIONS_APPLICATION_ROLES));
    rolesForm.setDescription(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.APPLICATIONS_APPLICATION_ROLES_DESCRIPTION));
    applyButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.APPLICATIONS_APPLICATION_CREATE));
    cancelButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale, Messages.ACTIONS_CANCEL));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalLayout getFooter()
  {
    return footer;
  }

}
