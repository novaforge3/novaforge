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
/**
 *
 */
package org.novaforge.forge.ui.commons.client.validation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author BILET-JC
 */
public class PasswordTextBoxValidation extends Composite implements TextBoxBaseValidation
{

  private static final String                            EMPTY_TEXT = "";
  private static final ValidationResources               resource   = (ValidationResources) GWT.create(ValidationResources.class);
  private static       PasswordTextBoxValidationUiBinder UI_BINDER  = GWT.create(PasswordTextBoxValidationUiBinder.class);
  @UiField PasswordTextBox passwordtextbox;
  @UiField Label           error;
  private  Validator       validator;

  /**
   * Default constructur.
   * <p>
   * It will use {@link ValidationResources}.
   */
  public PasswordTextBoxValidation()
  {
    resource.style().ensureInjected();
    // Generate ui
    this.initWidget(UI_BINDER.createAndBindUi(this));
    this.validator = new Validator.DefaultValidator();
    this.bind();
  }

  private void bind()
  {

    this.passwordtextbox.addKeyUpHandler(new KeyUpHandler()
    {
      @Override
      public void onKeyUp(final KeyUpEvent event)
      {
        PasswordTextBoxValidation.this.isValid();
      }
    });
    this.passwordtextbox.addValueChangeHandler(new ValueChangeHandler<String>()
    {
      @Override
      public void onValueChange(final ValueChangeEvent<String> pEvent)
      {
        PasswordTextBoxValidation.this.isValid();

      }

    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValidator(final Validator pValidator)
  {
    this.validator = pValidator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid()
  {
    final boolean valid = this.validator.isValid(this.passwordtextbox.getValue().trim());
    this.showError(!valid);
    return valid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showError(final boolean pShow)
  {
    if (pShow)
    {
      this.passwordtextbox.addStyleName(resource.style().textInvalid());
      this.error.setText(this.validator.getErrorMessage());
    }
    else
    {
      this.passwordtextbox.removeStyleName(resource.style().textInvalid());
      this.error.setText(EMPTY_TEXT);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue()
  {
    return this.passwordtextbox.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String pValue)
  {
    this.passwordtextbox.setValue(pValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String value, final boolean fireEvents)
  {
    this.passwordtextbox.setValue(value, fireEvents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return this.passwordtextbox.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    this.passwordtextbox.setText(pName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final String pValue)
  {
    this.passwordtextbox.ensureDebugId(pValue);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear()
  {
    this.passwordtextbox.removeStyleName(resource.style().textInvalid());
    this.passwordtextbox.setText(EMPTY_TEXT);
    this.error.setText(EMPTY_TEXT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler)
  {
    return this.passwordtextbox.addKeyUpHandler(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler)
  {
    return this.passwordtextbox.addValueChangeHandler(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnable(final boolean pEnable)
  {
    this.passwordtextbox.setEnabled(pEnable);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus(final boolean b)
  {
    this.passwordtextbox.setFocus(b);
  }

  /**
   * @param value
   * @param fireEvents
   */
  public void setalue(final String value, final boolean fireEvents)
  {
    this.passwordtextbox.setValue(value, fireEvents);
  }

  /**
   *
   */
  public void setFocus()
  {
    this.passwordtextbox.setFocus(true);

  }

  interface PasswordTextBoxValidationUiBinder extends UiBinder<Widget, PasswordTextBoxValidation>
  {
  }

}
