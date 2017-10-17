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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.Common;

/**
 * @author lamirang
 */
public class TextBoxValidation extends Composite implements TextBoxBaseValidation
{

  private static ValidationResources RESOURCE = (ValidationResources) GWT.create(ValidationResources.class);
  private static TextBoxValidationUiBinder UI_BINDER = GWT.create(TextBoxValidationUiBinder.class);
  @UiField TextBox   textbox;
  @UiField Label     error;
  private  Validator validator;

  /**
   * Default constructor.
   * <p>
   * It will use {@link ValidationResources}.
   */
  public TextBoxValidation()
  {
    RESOURCE.style().ensureInjected();
    // Generate ui
    initWidget(UI_BINDER.createAndBindUi(this));
    validator = new Validator.DefaultValidator();
    bind();
  }

  private void bind()
  {

    textbox.addKeyUpHandler(new KeyUpHandler()
    {
      @Override
      public void onKeyUp(final KeyUpEvent event)
      {
        TextBoxValidation.this.isValid();
      }
    });
    textbox.addValueChangeHandler(new ValueChangeHandler<String>()
    {
      @Override
      public void onValueChange(final ValueChangeEvent<String> pEvent)
      {
        TextBoxValidation.this.isValid();

      }

    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValidator(final Validator pValidator)
  {
    validator = pValidator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid()
  {
    final boolean valid = validator.isValid(textbox.getValue());
    showError(!valid);
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
      textbox.addStyleName(RESOURCE.style().textInvalid());
      error.setText(validator.getErrorMessage());
    }
    else
    {
      textbox.removeStyleName(RESOURCE.style().textInvalid());
      error.setText(Common.EMPTY_STRING);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue()
  {
    return textbox.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String pValue)
  {
    textbox.setValue(pValue);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String value, final boolean fireEvents)
  {
    textbox.setValue(value, fireEvents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return textbox.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    textbox.setName(pName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final String pValue)
  {
    textbox.ensureDebugId(pValue);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear()
  {
    textbox.removeStyleName(RESOURCE.style().textInvalid());
    textbox.setText(Common.EMPTY_STRING);
    error.setText(Common.EMPTY_STRING);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler)
  {
    return textbox.addKeyUpHandler(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler)
  {
    return textbox.addValueChangeHandler(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnable(final boolean b)
  {
    textbox.setEnabled(b);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus(final boolean pB)
  {
    textbox.setFocus(pB);

  }

  interface TextBoxValidationUiBinder extends UiBinder<Widget, TextBoxValidation>
  {
  }

}
