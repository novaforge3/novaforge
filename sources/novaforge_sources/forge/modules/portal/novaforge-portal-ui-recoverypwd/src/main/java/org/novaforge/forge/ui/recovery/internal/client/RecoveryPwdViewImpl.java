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
package org.novaforge.forge.ui.recovery.internal.client;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.recovery.internal.module.RecoveryModule;

/**
 * This view describes the recovery pwd view
 * 
 * @author Guillaume Lamirand
 */
public class RecoveryPwdViewImpl extends VerticalLayout implements RecoveryPwdView
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 5432786190734792037L;
  /**
   * Represents the {@link Form} used to get user's e-mail
   */
  private final Form        emailForm;
  /**
   * Represents the {@link Button} used on apply action
   */
  private final Button      applyButton;
  /**
   * Represents the {@link Label} used on display a description
   */
  private final Label       description;
  /**
   * Represents the {@link Field} email
   */
  private final TextField   email;
  /**
   * Contains true if the current view is opened in a sub-window
   */
  private final boolean     inSub;

  /**
   * Default constructor
   * 
   * @param pInSub
   *          true means this view is containing in a sub-window
   */
  public RecoveryPwdViewImpl(final boolean pInSub)
  {
    inSub = pInSub;
    // Init
    setMargin(true);

    description = new Label();
    addComponent(description);
    setComponentAlignment(description, Alignment.MIDDLE_CENTER);

    // Create the Form
    emailForm = new Form();
    emailForm.setImmediate(true);
    emailForm.setInvalidCommitted(false);
    ((FormLayout) emailForm.getLayout()).setMargin(true);
    emailForm.setFooter(null);

    // Add fields
    email = new TextField();
    email.setRequired(true);
    email.setWidth(250, Unit.PIXELS);
    emailForm.addField(RecoveryPwdView.EMAIL, email);
    // Add form to layout
    addComponent(emailForm);
    setComponentAlignment(emailForm, Alignment.TOP_CENTER);

    // The apply buttons
    applyButton = new Button();
    applyButton.setIcon(new ThemeResource(NovaForgeResources.ICON_MAIL));
    applyButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    addComponent(applyButton);
    setComponentAlignment(applyButton, Alignment.MIDDLE_CENTER);

    setSizeUndefined();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    description.setValue(RecoveryModule.getPortalMessages().getMessage(getLocale(), Messages.RECOVERY_DESC));
    if (!inSub)
    {
      emailForm.setCaption(RecoveryModule.getPortalMessages()
          .getMessage(getLocale(), Messages.RECOVERY_TITLE));
    }
    email.setCaption(RecoveryModule.getPortalMessages().getMessage(getLocale(),
        Messages.PUBLIC_REGISTER_FORM_EMAIL));
    email.setRequiredError(RecoveryModule.getPortalMessages().getMessage(getLocale(),
        Messages.PUBLIC_REGISTER_FORM_EMAIL_TOOLTIP));
    email.addValidator(new EmailValidator(RecoveryModule.getPortalMessages().getMessage(getLocale(),
        Messages.PUBLIC_REGISTER_FORM_EMAIL_TOOLTIP)));
    applyButton.setCaption(RecoveryModule.getPortalMessages()
        .getMessage(getLocale(), Messages.RECOVERY_APPLY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detach()
  {
    super.detach();
    email.removeAllValidators();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getApply()
  {
    return applyButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getForm()
  {
    return emailForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getDescriptionLabel()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Field getEmailField()
  {
    return email;
  }
}
