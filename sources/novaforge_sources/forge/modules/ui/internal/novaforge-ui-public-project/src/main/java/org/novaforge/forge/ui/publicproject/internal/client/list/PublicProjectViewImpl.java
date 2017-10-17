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
package org.novaforge.forge.ui.publicproject.internal.client.list;

import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.publicproject.internal.client.components.ProjectItemProperty;
import org.novaforge.forge.ui.publicproject.internal.module.PublicProjectModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class PublicProjectViewImpl extends VerticalSplitPanel implements PublicProjectView
{
  /**
   * Serial version id used for serialization
   */
  private static final long      serialVersionUID              = -2220073303146264084L;
  /**
   * Define the view constants
   */
  private final static int       SPLIT_POSITION                = 72;
  private final static int       REQUEST_WINDOW_SIZE           = 500;
  private final static String    JOINING_REQUEST_MESSAGE_FIELD = "requestMessage";
  private final static int       PROJECT_ICON_SIZE             = 100;
  private final Label            projectsComboboxLabel         = new Label();
  private final ComboBox         projectsCombobox              = new ComboBox();
  private final Label            projectNameLabel              = new Label();
  private final Label            projectDescriptionLabel       = new Label();
  private final Embedded         projectIcon                   = new Embedded();
  private final GridLayout       projectDetailsGrid            = new GridLayout(2, 4);
  private final Label            fieldOrganizationLabel        = new Label();
  private final Label            fieldOrganizationValue        = new Label();
  private final Label            fieldLicenseLabel             = new Label();
  private final Label            fieldLicenseValue             = new Label();
  private final Label            fieldDateLabel                = new Label();
  private final Label            fieldDatenValue               = new Label();
  private final Label            fieldAuthorLabel              = new Label();
  private final Button           fieldAuthorValue              = new Button();
  private final VerticalLayout   bottomLayout                  = new VerticalLayout();
  private final VerticalLayout   bottomEmptyLayout             = new VerticalLayout();
  private final VerticalLayout   bottomProjectLayout           = new VerticalLayout();
  private final HorizontalLayout buttonLayout                  = new HorizontalLayout();
  private final Label            requestAlreadyDoneLabel       = new Label();
  private final Button           requestJoiningButton          = new Button();
  private final Window           joiningRequestWindow          = new Window();
  private final Label            joiningRequestHelpProject     = new Label();
  private final Label            joiningRequestHelpOwner       = new Label();
  private final Form             joiningRequestForm            = new Form();
  private final TextArea         joiningRequestTextArea        = new TextArea();
  private final Button           sendJoiningRequestButton      = new Button();
  private Item selectedProject;

  public PublicProjectViewImpl()
  {
    setStyleName(Reindeer.SPLITPANEL_SMALL);
    setSplitPosition(SPLIT_POSITION, Unit.PIXELS);
    setLocked(true);
    initLayout();
  }

  private void initLayout()
  {
    initTopLayout();
    initBottomLayout();
    initJoiningRequestWindow();
    requestAlreadyDoneLabel.setContentMode(ContentMode.HTML);
    requestAlreadyDoneLabel.setSizeUndefined();
    requestAlreadyDoneLabel.setStyleName(NovaForge.LABEL_BOLD);
    requestAlreadyDoneLabel.addStyleName(NovaForge.LABEL_GREEN);
  }

  private void initTopLayout()
  {
    final HorizontalLayout topLayout = new HorizontalLayout();
    topLayout.setWidth(100, Unit.PERCENTAGE);
    // projectsCombobox.setWidth(PROJECT_COMBOBOX_SIZE, Unit.PIXELS);
    projectsCombobox.setImmediate(true);
    final GridLayout topGrid = new GridLayout(2, 1);
    topGrid.setMargin(true);
    topGrid.setSpacing(true);
    topGrid.addComponent(projectsComboboxLabel, 0, 0);
    topGrid.addComponent(projectsCombobox, 1, 0);
    topLayout.addComponent(topGrid);
    topLayout.setComponentAlignment(topGrid, Alignment.MIDDLE_CENTER);
    addComponent(topLayout);
  }

  private void initBottomLayout()
  {
    initBottomEmptyLayout();
    initBottomProjectLayout();
    setEmptyBottom();
    addComponent(bottomLayout);
  }

  private void initJoiningRequestWindow()
  {
    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(REQUEST_WINDOW_SIZE, Unit.PIXELS);
    joiningRequestWindow.setModal(true);
    joiningRequestWindow.setResizable(false);
    joiningRequestForm.setSizeFull();
    joiningRequestTextArea.setWidth(100, Unit.PERCENTAGE);
    joiningRequestForm.addField(JOINING_REQUEST_MESSAGE_FIELD, joiningRequestTextArea);
    sendJoiningRequestButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    sendJoiningRequestButton.setIcon(new ThemeResource(NovaForgeResources.ICON_MAIL));
    final HorizontalLayout formButtonLayout = new HorizontalLayout();
    formButtonLayout.setWidth(100, Unit.PERCENTAGE);
    formButtonLayout.addComponent(sendJoiningRequestButton);
    formButtonLayout.setComponentAlignment(sendJoiningRequestButton, Alignment.MIDDLE_CENTER);
    joiningRequestForm.setFooter(formButtonLayout);
    ((FormLayout) joiningRequestForm.getLayout()).setMargin(true);
    joiningRequestHelpProject.setStyleName(NovaForge.LABEL_BOLD);
    joiningRequestHelpOwner.setStyleName(NovaForge.LABEL_BOLD);
    // Add Components
    final VerticalLayout helpLayout = new VerticalLayout();
    helpLayout.addComponent(joiningRequestHelpProject);
    helpLayout.addComponent(joiningRequestHelpOwner);
    helpLayout.setWidth(100, Unit.PERCENTAGE);
    windowLayout.addComponent(helpLayout);
    windowLayout.addComponent(joiningRequestForm);
    // ALign
    windowLayout.setComponentAlignment(joiningRequestForm, Alignment.MIDDLE_CENTER);
    joiningRequestWindow.setContent(windowLayout);

  }

  private void initBottomEmptyLayout()
  {
    bottomEmptyLayout.setSizeFull();
  }

  private void initBottomProjectLayout()
  {
    bottomProjectLayout.setSpacing(true);
    bottomProjectLayout.setMargin(true);
    projectNameLabel.setStyleName(Reindeer.LABEL_H1);
    projectIcon.setWidth(PROJECT_ICON_SIZE, Unit.PIXELS);
    requestJoiningButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    requestJoiningButton.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_JOIN));
    final GridLayout headerGrid = new GridLayout(2, 1);
    headerGrid.setSpacing(true);
    headerGrid.addComponent(projectIcon, 0, 0);
    headerGrid.addComponent(projectNameLabel, 1, 0);
    headerGrid.setComponentAlignment(projectIcon, Alignment.MIDDLE_RIGHT);
    headerGrid.setComponentAlignment(projectNameLabel, Alignment.MIDDLE_LEFT);
    bottomProjectLayout.addComponent(headerGrid);
    bottomProjectLayout.setComponentAlignment(headerGrid, Alignment.MIDDLE_CENTER);
    projectDescriptionLabel.setStyleName(NovaForge.LABEL_BOLD);
    final HorizontalLayout projectDescriptionLayout = new HorizontalLayout();
    projectDescriptionLayout.setMargin(true);
    projectDescriptionLayout.addComponent(projectDescriptionLabel);
    bottomProjectLayout.addComponent(projectDescriptionLayout);
    bottomProjectLayout.setComponentAlignment(projectDescriptionLayout, Alignment.MIDDLE_CENTER);
    fieldAuthorValue.setStyleName(NovaForge.BUTTON_LINK);
    fieldAuthorValue.addStyleName(NovaForge.CAPITALIZE_TEXT);
    fieldAuthorValue.addStyleName(NovaForge.BUTTON_LARGE);
    projectDetailsGrid.addComponent(fieldOrganizationLabel, 0, 0);
    projectDetailsGrid.addComponent(fieldOrganizationValue, 1, 0);
    projectDetailsGrid.addComponent(fieldLicenseLabel, 0, 1);
    projectDetailsGrid.addComponent(fieldLicenseValue, 1, 1);
    projectDetailsGrid.addComponent(fieldDateLabel, 0, 2);
    projectDetailsGrid.addComponent(fieldDatenValue, 1, 2);
    projectDetailsGrid.addComponent(fieldAuthorLabel, 0, 3);
    projectDetailsGrid.addComponent(fieldAuthorValue, 1, 3);
    projectDetailsGrid.setMargin(true);
    projectDetailsGrid.setSpacing(true);
    bottomProjectLayout.addComponent(projectDetailsGrid);
    bottomProjectLayout.setComponentAlignment(projectDetailsGrid, Alignment.MIDDLE_CENTER);
    buttonLayout.setWidth(100, Unit.PERCENTAGE);
    bottomProjectLayout.addComponent(buttonLayout);
  }

  private void setEmptyBottom()
  {
    bottomLayout.removeAllComponents();
    projectsCombobox.setValue(null);
    bottomLayout.addComponent(bottomEmptyLayout);
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
    projectsComboboxLabel.setValue(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_COMBOBOX_TITLE));
    projectsCombobox.setInputPrompt(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_COMBOBOX_EMPTY));
    requestJoiningButton.setCaption(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_REQUEST_ACTION));
    joiningRequestWindow.setCaption(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_REQUEST_ACTION));
    sendJoiningRequestButton.setCaption(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_SEND_ACTION));
    joiningRequestForm.setCaption(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_REQUEST_SEND_MESSAGE_TITLE));
    projectDetailsGrid.setCaption(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_DETAILS_TITLE));
    fieldOrganizationLabel.setValue(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_FIELD_ORGANIZATION));
    fieldLicenseLabel.setValue(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_FIELD_LICENCE));
    fieldDateLabel.setValue(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_FIELD_DATE));
    fieldAuthorLabel.setValue(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_FIELD_AUTHOR));
    requestAlreadyDoneLabel.setValue(PublicProjectModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_PROJECT_REQUEST_EXIST));

    joiningRequestTextArea.addValidator(new StringLengthValidator(PublicProjectModule.getPortalMessages()
        .getMessage(pLocale, Messages.PUBLIC_PROJECT_REQUEST_MESSAGE_LENGTH), 0, 255, true));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedProject(final String pProjectContainerId)
  {
    if (pProjectContainerId == null)
    {
      setEmptyBottom();
    }
    else
    {
      // Get Project
      selectedProject = getProjectItem(pProjectContainerId);
      if (selectedProject != null)
      {
        // Update view with project elements
        projectNameLabel.setValue(selectedProject.getItemProperty(ProjectItemProperty.NAME.getPropertyId())
            .getValue().toString());
        projectIcon.setSource((Resource) selectedProject.getItemProperty(
            ProjectItemProperty.ICON.getPropertyId()).getValue());
        projectIcon.setAlternateText(selectedProject
            .getItemProperty(ProjectItemProperty.NAME.getPropertyId()).getValue().toString());
        projectDescriptionLabel.setValue(selectedProject
            .getItemProperty(ProjectItemProperty.DESCRIPTION.getPropertyId()).getValue().toString());
        buttonLayout.removeAllComponents();
        if ((Boolean) selectedProject.getItemProperty(ProjectItemProperty.REQUESTINPROGRESS.getPropertyId())
            .getValue())
        {
          buttonLayout.addComponent(requestAlreadyDoneLabel);
          buttonLayout.setComponentAlignment(requestAlreadyDoneLabel, Alignment.MIDDLE_CENTER);
        }
        else
        {
          buttonLayout.addComponent(requestJoiningButton);
          buttonLayout.setComponentAlignment(requestJoiningButton, Alignment.MIDDLE_CENTER);
        }
        fieldOrganizationValue.setValue(selectedProject
            .getItemProperty(ProjectItemProperty.ORGANIZATION.getPropertyId()).getValue().toString());
        fieldLicenseValue.setValue(selectedProject
            .getItemProperty(ProjectItemProperty.LICENCE.getPropertyId()).getValue().toString());
        final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", getLocale());
        fieldDatenValue.setValue(dateFormat.format(selectedProject.getItemProperty(
            ProjectItemProperty.CREATEDDATE.getPropertyId()).getValue()));
        final UserProfile author = (UserProfile) selectedProject.getItemProperty(
            ProjectItemProperty.AUTHOR.getPropertyId()).getValue();
        fieldAuthorValue.setCaption(author.getUser().getFirstName() + " " + author.getUser().getName());
        Resource icon = null;
        if (author.getImage() != null)
        {
          icon = ResourceUtils.buildImageResource(author.getImage().getFile(), author.getImage().getName());
        }
        else
        {
          icon = new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW);
        }
        fieldAuthorValue.setIcon(icon);
        setProjectBottom();
      }
      else
      {
        // display the top panel with the projects combo
        setSplitPosition(SPLIT_POSITION, Unit.PIXELS);

        setEmptyBottom();
        final Notification notification = new Notification(PublicProjectModule.getPortalMessages()
            .getMessage(getLocale(), Messages.PUBLIC_PROJECT_ERROR_PROJECTSELECTED_TITLE),
            PublicProjectModule.getPortalMessages().getMessage(getLocale(),
                Messages.PUBLIC_PROJECT_ERROR_PROJECTSELECTED_DESC), Type.ERROR_MESSAGE, true);
        notification.setHtmlContentAllowed(true);
        notification.show(Page.getCurrent());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showRequestWindowForProject()
  {
    // Empty TextArea
    joiningRequestTextArea.setValue("");
    joiningRequestHelpProject.setValue(PublicProjectModule.getPortalMessages()
        .getMessage(
            getLocale(), Messages.PUBLIC_PROJECT_REQUEST_SEND_HELP1,
            selectedProject.getItemProperty(ProjectItemProperty.NAME.getPropertyId()).getValue()));
    final UserProfile author = (UserProfile) selectedProject.getItemProperty(
        ProjectItemProperty.AUTHOR.getPropertyId()).getValue();
    final String name = author.getUser().getFirstName() + " " + author.getUser().getName();
    joiningRequestHelpOwner.setValue(PublicProjectModule.getPortalMessages().getMessage(getLocale(),
                                                                                        Messages.PUBLIC_PROJECT_REQUEST_SEND_HELP2,
                                                                                        name));
    UI.getCurrent().addWindow(joiningRequestWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getProjectsCombobox()
  {
    return projectsCombobox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequestJoiningButton()
  {
    return requestJoiningButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getJoiningRequestWindow()
  {
    return joiningRequestWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getSendJoiningRequestButton()
  {
    return sendJoiningRequestButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSelectedProjectId()
  {
    return selectedProject.getItemProperty(ProjectItemProperty.ID.getPropertyId()).getValue().toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextArea getJoiningRequestTextArea()
  {
    return joiningRequestTextArea;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getFieldAuthorValue()
  {
    return fieldAuthorValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile getSelectedProjectAuthor()
  {
    return (UserProfile) selectedProject.getItemProperty(ProjectItemProperty.AUTHOR.getPropertyId()).getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void hideHeader()
  {
    setSplitPosition(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getJoiningRequestForm()
  {
    return joiningRequestForm;
  }

  private Item getProjectItem(final String pProjectContainerId)
  {
    return projectsCombobox.getContainerDataSource().getItem(pProjectContainerId);
  }

  private void setProjectBottom()
  {
    bottomLayout.removeAllComponents();
    bottomLayout.addComponent(bottomProjectLayout);
  }

}
