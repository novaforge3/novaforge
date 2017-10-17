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
package org.novaforge.forge.ui.projects.internal.client.manage.view;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.UploadFieldCustom;
import org.novaforge.forge.ui.portal.client.component.UploadImageComponent;
import org.novaforge.forge.ui.portal.client.util.DebugIdGenerator;
import org.novaforge.forge.ui.portal.data.validator.IdValidator;
import org.novaforge.forge.ui.projects.internal.client.manage.presenter.ProjectView;
import org.novaforge.forge.ui.projects.internal.client.manage.presenter.components.OrganizationValidator;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;
import org.novaforge.forge.ui.projects.internal.module.create.CreateProjectModule;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class ProjectViewImpl extends VerticalLayout implements ProjectView
{
  /** Default serial version UID */
  private static final long    serialVersionUID = -5231197448997190414L;
  /**
   * Max size for project image : 512Ko.
   */
  private final static int IMAGE_MAX_SIZE = 524288;
  /**
   * Define if the view is used to create or edit project
   */
  private final boolean              isCreateMode;
  /** Global form */
  private       Form                 mainForm;
  /** The project detail form */
  private       Form                 projectForm;
  /** the field factory used to generate form */
  private       ProjectFieldFactory  mainFieldFactory;
  /** The project additional form */
  private       Form                 additionalForm;
  /** The {@link ComboBox} which display licenses */
  private       ComboBox             licenses;
  /** The {@link ComboBox} which display organism */
  private       ComboBox             organism;
  private       Button               applyButton;
  /** The project template form */
  private       Form                 templateForm;
  /** Contains the templates data */
  private       ComboBox             templates;
  /** Component used to upload the user image. */
  private       UploadImageComponent imageComponent;

  /**
   * Default constructor
   *
   * @param pIsCreated
   *          defined the ui mode
   */
  public ProjectViewImpl(final boolean pIsCreated)
  {
    isCreateMode = pIsCreated;
    setMargin(true);
    setSpacing(true);
    setWidth(100, Unit.PERCENTAGE);
    final Component mainComponent       = initMain();
    final Component additionalComponent = initAdditional();
    final Component templateComponent   = initTemplate();
    final Component footer              = initFooter();
    addComponent(mainComponent);
    addComponent(additionalComponent);
    addComponent(templateComponent);
    addComponent(footer);
    setExpandRatio(mainComponent, 0);
    setExpandRatio(additionalComponent, 0);
    setExpandRatio(templateComponent, 0);
    setExpandRatio(footer, 1);
  }

  /**
   * Initialize main section
   */
  private Component initMain()
  {
    final HorizontalLayout mainLayout = new HorizontalLayout();
    mainLayout.setWidth(100, Unit.PERCENTAGE);
    mainForm = new Form();
    mainForm.setWidth(100, Unit.PERCENTAGE);

    final Component iconLayout    = initIconLayout();
    final Component projectLayout = initProjectLayout();

    final GridLayout mainGridLayout = new GridLayout(2, 1);
    mainGridLayout.setSpacing(true);
    mainGridLayout.addComponent(iconLayout, 0, 0);
    mainGridLayout.addComponent(projectLayout, 1, 0);
    final FormLayout mainFormLayout = (FormLayout) mainForm.getLayout();
    mainFormLayout.setWidth(100, Unit.PERCENTAGE);
    mainFormLayout.addComponent(mainGridLayout);
    mainFormLayout.setComponentAlignment(mainGridLayout, Alignment.MIDDLE_CENTER);
    mainLayout.addComponent(mainForm);
    mainLayout.setComponentAlignment(mainForm, Alignment.MIDDLE_CENTER);
    return mainLayout;
  }

  /**
   * Initialize additional form
   */
  private Component initAdditional()
  {
    additionalForm = new Form();
    additionalForm.setImmediate(true);

    licenses = new ComboBox();
    licenses.setNullSelectionAllowed(false);
    licenses.setImmediate(true);
    licenses.setFilteringMode(FilteringMode.CONTAINS);
    licenses.setId(DebugIdGenerator.getDynamicComponentId(CreateProjectModule.getPortalModuleId(),
                                                          ProjectFieldFactory.LICENSE_FIELD));
    organism = new ComboBox();
    organism.setNullSelectionAllowed(true);
    organism.setNewItemsAllowed(true);
    organism.setImmediate(true);
    organism.setFilteringMode(FilteringMode.CONTAINS);
    organism.setId(DebugIdGenerator.getDynamicComponentId(CreateProjectModule.getPortalModuleId(),
                                                          ProjectFieldFactory.ORGANISM_FIELD));

    additionalForm.addField(ProjectFieldFactory.LICENSE_FIELD, licenses);
    additionalForm.addField(ProjectFieldFactory.ORGANISM_FIELD, organism);
    return additionalForm;
  }

  /**
   * Initialize template form
   */
  private Component initTemplate()
  {
    templateForm = new Form();
    templateForm.setVisible(isCreateMode);
    templateForm.setImmediate(true);

    templates = new ComboBox();
    templates.setNullSelectionAllowed(true);
    templates.setImmediate(true);
    templates.setFilteringMode(FilteringMode.CONTAINS);
    templates.setNullSelectionAllowed(true);
    templates.setId(DebugIdGenerator.getDynamicComponentId(CreateProjectModule.getPortalModuleId(),
        ProjectFieldFactory.TEMPLATE_FIELD));
    templateForm.addField(ProjectFieldFactory.TEMPLATE_FIELD, templates);
    return templateForm;
  }

  private Component initFooter()
  {
    final HorizontalLayout footerButtons = new HorizontalLayout();
    footerButtons.setSpacing(true);
    footerButtons.setWidth(100, Unit.PERCENTAGE);
    applyButton = new Button();
    applyButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    applyButton.setId(DebugIdGenerator.getDynamicComponentId(CreateProjectModule.getPortalModuleId(),
        "applyButton"));

    footerButtons.addComponent(applyButton);
    footerButtons.setComponentAlignment(applyButton, Alignment.MIDDLE_CENTER);
    return footerButtons;
  }

  private Component initIconLayout()
  {
    imageComponent = new UploadImageComponent(UploadFieldCustom.ICON_SIZE, IMAGE_MAX_SIZE);
    imageComponent.setWindowTitleKeyMessage(Messages.PROJECT_MAIN_ICON_TITLE);
    imageComponent.setHelpCaptionKeyMessage(Messages.PROJECT_MAIN_ICON_HELP);
    imageComponent.setUploadButtonDescriptionKeyMessage(Messages.PROJECT_MAIN_ICON_UPLOAD_DESCRIPTION);
    imageComponent.setUploadCaptionKeyMessage(Messages.PROJECT_MAIN_ICON_UPLOAD_CAPTION);
    imageComponent.setConfirmButtonCaptionKeyMessage(Messages.PROJECT_MAIN_ICON_CONFIRM);
    return imageComponent;
  }

  private Component initProjectLayout()
  {
    // Create the Form
    projectForm = new Form();
    projectForm.setImmediate(true);

    mainFieldFactory = new ProjectFieldFactory();
    projectForm.setFormFieldFactory(mainFieldFactory);
    return projectForm;
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
    // Main form
    final PortalMessages portalMessages = ProjectServices.getPortalMessages();
    mainForm.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_MAIN_TITLE));
    final TextField name = mainFieldFactory.getName();
    if (name != null)
    {
      name.removeAllValidators();
      name.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_MAIN_NAME_CAPTION));
      name.setRequiredError(portalMessages.getMessage(pLocale, Messages.PROJECT_MAIN_NAME_REQUIRE));
      name.addValidator(new StringLengthValidator(portalMessages.getMessage(pLocale,
          Messages.PROJECT_MAIN_NAME_VALIDATION), 3, 35, false));
    }
    final TextField id = mainFieldFactory.getId();
    if (id != null)
    {
      id.setReadOnly(!isCreateMode);
      id.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_MAIN_ID_CAPTION));
      id.removeAllValidators();
      id.setRequiredError(portalMessages.getMessage(pLocale, Messages.PROJECT_MAIN_ID_REQUIRE));
      id.addValidator(new IdValidator(
          portalMessages.getMessage(pLocale, Messages.PROJECT_MAIN_ID_VALIDATION), 3, 26));
    }
    final TextArea description = mainFieldFactory.getDescription();

    if (description != null)
    {
      description.removeAllValidators();
      description.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_MAIN_DESCRIPTION_CAPTION));
      description.setRequiredError(portalMessages.getMessage(pLocale,
          Messages.PROJECT_MAIN_DESCRIPTION_REQUIRE));
      description.addValidator(new StringLengthValidator(portalMessages.getMessage(pLocale,
          Messages.PROJECT_MAIN_DESCRIPTION_VALIDATION), 3, 250, false));
    }
    final CheckBox privateField = mainFieldFactory.getPrivate();
    if (privateField != null)
    {
      privateField.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_MAIN_VISIBILITY_CAPTION));

    }
    // Update image component locale
    imageComponent.refreshLocale(portalMessages, pLocale);

    // Additional form
    additionalForm.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_ADD_TITLE));

    if (licenses != null)
    {
      licenses.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_ADD_LICENSE_CAPTION));
    }
    if (organism != null)
    {
      organism.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_ADD_ORGANISM_CAPTION));
      organism.setInputPrompt(portalMessages.getMessage(pLocale, Messages.PROJECT_ADD_ORGANISM_PROMPT));
      organism.setDescription(portalMessages.getMessage(pLocale, Messages.PROJECT_ADD_ORGANISM_TITLE));
      organism.removeAllValidators();
      organism.addValidator(new OrganizationValidator(portalMessages.getMessage(pLocale,
          Messages.PROJECT_ADD_ORGANISM_VALIDATION)));

    }
    // Template form
    templateForm.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_TEMPLATE_TITLE));
    templates.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_TEMPLATE_CAPTION));
    templates.addItem(portalMessages.getMessage(pLocale, Messages.PROJECT_TEMPLATE_NULL));
    templates.setNullSelectionItemId(portalMessages.getMessage(pLocale, Messages.PROJECT_TEMPLATE_NULL));

    // Footer
    if (isCreateMode)
    {
      applyButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
      applyButton.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_CREATE_APPLY));
    }
    else
    {
      applyButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
      applyButton.setCaption(portalMessages.getMessage(pLocale, Messages.PROJECT_UPDATE_APPLY));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getProjectForm()
  {
    return projectForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getAdditionalForm()
  {
    return additionalForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getProjectIcon()
  {
    return imageComponent.getImage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getUpdatePictureWindow()
  {
    return imageComponent.getUpdateImageWindow();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getIconDeleteButton()
  {
    return imageComponent.getDeleteImageButton();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getIconConfirmButton()
  {
    return imageComponent.getUpdateImageConfirmButton();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UploadFieldCustom getImageUpload()
  {
    return imageComponent.getUploadImageField();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getProjectId()
  {
    return mainFieldFactory.getId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getProjectName()
  {
    return mainFieldFactory.getName();
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
  public ComboBox getLicenses()
  {
    return licenses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getOrganism()
  {
    return organism;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getTemplates()
  {
    return templates;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCreateMode()
  {
    return isCreateMode;
  }

}
