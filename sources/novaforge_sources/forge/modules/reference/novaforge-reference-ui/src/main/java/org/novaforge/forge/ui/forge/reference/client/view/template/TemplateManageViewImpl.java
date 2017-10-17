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
package org.novaforge.forge.ui.forge.reference.client.view.template;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.properties.TemplateMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;

/**
 * @author lamirang
 */
public class TemplateManageViewImpl extends Composite implements TemplateManageView
{
  private static TemplateManageViewImplUiBinder uiBinder         = GWT
                                                                     .create(TemplateManageViewImplUiBinder.class);
  private static ReferenceResources             ressources       = GWT.create(ReferenceResources.class);
  private final TemplateMessage templateMessages = (TemplateMessage) GWT.create(TemplateMessage.class);
  private final ValidateDialogBox validateDialogBox;
  @UiField
  Label                           templateManageTitle;

  @UiField
  Button                          buttonPrevious;
  @UiField
  Button                          buttonNext;
  @UiField
  Button                          buttonSave;
  @UiField
  Button                          buttonCancel;
  @UiField
  Panel                           centerPanel;
  @UiField
  VerticalPanel                   verticalPanel;
  @UiField
  Label                           detail;
  @UiField
  Label                           roles;
  @UiField
  Label                           applications;
  @UiField
  Label                           summary;

  public TemplateManageViewImpl(final ManagePresenterType pType)
  {

    ressources.style().ensureInjected();

    // Generate ui
    initWidget(uiBinder.createAndBindUi(this));

    // Set text label
    templateManageTitle.setText(templateMessages.infoTitle());
    detail.setText(templateMessages.detail());
    roles.setText(templateMessages.roles());
    applications.setText(templateMessages.applications());
    summary.setText(templateMessages.summary());

    buttonPrevious.setText(templateMessages.buttonPrevious());
    buttonNext.setText(templateMessages.buttonNext());
    buttonSave.setText(templateMessages.buttonSave());
    buttonCancel.setText(templateMessages.buttonCancel());

    final Widget widget = verticalPanel.getWidget(1);
    verticalPanel.setCellHeight(widget, "15px");

    // Initialization of validation popup
    if (ManagePresenterType.CREATE.equals(pType))
    {
      validateDialogBox = new ValidateDialogBox(templateMessages.addValidationMessage());

    }
    else
    {
      validateDialogBox = new ValidateDialogBox(templateMessages.editValidationMessage());

    }
  }

  @Override
  public Button getPreviousButton()
  {
    return buttonPrevious;
  }

  @Override
  public Button getNextButton()
  {
    return buttonNext;
  }

  @Override
  public Button getSaveButton()
  {
    return buttonSave;
  }

  @Override
  public Button getCancelButton()
  {
    return buttonCancel;
  }

  @Override
  public Label getTemplateManageTitle()
  {
    return templateManageTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ValidateDialogBox getValidateDialogBox()
  {
    return validateDialogBox;
  }

  @Override
  public Panel getCenterPanel()
  {
    return centerPanel;
  }

  @Override
  public Label getDetail()
  {
    return detail;
  }

  @Override
  public Label getRoles()
  {
    return roles;
  }

  @Override
  public Label getSpaces()
  {
    return applications;
  }

  @Override
  public Label getSummary()
  {
    return summary;
  }

  interface TemplateManageViewImplUiBinder extends UiBinder<Widget, TemplateManageViewImpl>
  {
  }
}
