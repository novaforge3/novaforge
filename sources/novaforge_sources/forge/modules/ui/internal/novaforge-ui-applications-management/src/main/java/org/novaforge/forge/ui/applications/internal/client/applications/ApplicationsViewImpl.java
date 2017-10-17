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
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.applications.internal.client.applications.components.ApplicationFormProperty;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
@SuppressWarnings("deprecation")
public class ApplicationsViewImpl extends VerticalLayout implements ApplicationsView
{

  /**
   * Serial version id
   */
  private static final long   serialVersionUID    = -5650066717157280916L;

  /**
   * Default representation for <code>null</code> value.
   */
  private static final String NULL_REPRESENTATION = "";
  private Form                appForm;
  private Button              applyButton;

  private Button              editButton;

  private Button              delButton;

  private HorizontalLayout    headerButtons;

  private Button              cancelButton;

  private TextField           nameField;
  private TextArea            descriptionField;

  private TextField           categoryField;

  private TextField           typefield;

  private Form                rolesForm;
  private Label               informationLabel;

  private HorizontalLayout    footer;

  private Button              linkButton;
  private DeleteConfirmWindow deleteWindow;

  /**
   * Default constructor.
   */
  public ApplicationsViewImpl()
  {
    setMargin(true);
    initHeaders();
    initUnavailableLabel();
    initApplication();
    initRolesForm();
    initFooter();
    initDeleteWindow();

  }

  private void initHeaders()
  {
    headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    linkButton = new Button();
    linkButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    linkButton.setIcon(new ThemeResource(NovaForgeResources.ICON_LINK));
    editButton = new Button();
    editButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    editButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));

    delButton = new Button();
    delButton.setIcon(new ThemeResource(NovaForgeResources.ICON_TRASH_RED));

    headerButtons.addComponent(linkButton);
    headerButtons.addComponent(editButton);
    headerButtons.addComponent(delButton);
    addComponent(headerButtons);
  }

  private void initUnavailableLabel()
  {
    informationLabel = new Label();
    addComponent(informationLabel);
    setComponentAlignment(informationLabel, Alignment.TOP_LEFT);
  }

  private void initApplication()
  {
    appForm = new Form();
    appForm.setImmediate(true);
    appForm.setInvalidCommitted(false);
    appForm.setFooter(null);

    // init fields
    categoryField = new TextField();
    categoryField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    categoryField.setWidth(250, Unit.PIXELS);

    typefield = new TextField();
    typefield.setWidth(NovaForge.FORM_FIELD_WIDTH);

    nameField = new TextField();
    nameField.setNullRepresentation(NULL_REPRESENTATION);
    nameField.setWidth(NovaForge.FORM_FIELD_WIDTH);

    descriptionField = new TextArea();
    descriptionField.setNullRepresentation(NULL_REPRESENTATION);
    descriptionField.setWidth(NovaForge.FORM_FIELD_WIDTH);

    appForm.addField(ApplicationFormProperty.CATEGORY.getPropertyId(), categoryField);
    appForm.addField(ApplicationFormProperty.TYPE.getPropertyId(), typefield);
    appForm.addField(ApplicationFormProperty.NAME.getPropertyId(), nameField);
    appForm.addField(ApplicationFormProperty.DESCRIPTION.getPropertyId(), descriptionField);

    addComponent(appForm);
  }

  private void initRolesForm()
  {
    rolesForm = new Form();
    rolesForm.setImmediate(true);
    rolesForm.setInvalidCommitted(false);
    rolesForm.setFooter(null);
    addComponent(rolesForm);
    setComponentAlignment(rolesForm, Alignment.TOP_LEFT);
  }

  private void initFooter()
  {

    footer = new HorizontalLayout();
    footer.setSpacing(true);
    applyButton = new Button();
    applyButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    applyButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
    cancelButton = new Button();
    cancelButton.setStyleName(NovaForge.BUTTON_LINK);
    footer.addComponent(cancelButton);
    footer.addComponent(applyButton);
    footer.setComponentAlignment(cancelButton, Alignment.MIDDLE_LEFT);
    footer.setComponentAlignment(applyButton, Alignment.MIDDLE_LEFT);
    addComponent(footer);
    setComponentAlignment(footer, Alignment.TOP_LEFT);
  }

  /**
   * Init the window used to delete group
   */
  private void initDeleteWindow()
  {
    deleteWindow = new DeleteConfirmWindow(Messages.APPLICATIONS_APPLICATION_DELETE_CONFIRMLABEL);
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
  public TextField getCategoryField()
  {
    return categoryField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getTypeField()
  {
    return typefield;
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
    if (categoryField != null)
    {
      categoryField.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                                 Messages.APPLICATIONS_APPLICATION_CATEGORY));
    }
    if (typefield != null)
    {
      typefield.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.APPLICATIONS_APPLICATION_TYPE));
    }
    if (nameField != null)
    {
      nameField.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.APPLICATIONS_APPLICATION_NAME));
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
    linkButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                            Messages.APPLICATIONS_APPLICATION_LINK));
    editButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                            Messages.APPLICATIONS_APPLICATION_EDIT));
    delButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.APPLICATIONS_APPLICATION_DELETE));
    applyButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale, Messages.ACTIONS_SAVE));
    cancelButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale, Messages.ACTIONS_CANCEL));

    deleteWindow.refreshLocale(pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalLayout getFooter()
  {
    return footer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getLinkButton()
  {
    return linkButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getInformationLabel()
  {
    return informationLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getDeleteWindow()
  {
    return deleteWindow;
  }

}
