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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author lamirang
 */
public class TextAreaValidation extends Composite implements TextBoxBaseValidation
{

  private static final String EMPTY_TEXT = "";
  private static ValidationResources        RESSOURCE  = null;
  private static TextAreaValidationUiBinder UI_BINDER  = GWT.create(TextAreaValidationUiBinder.class);
  @UiField
  TextArea textarea;
  @UiField
  Label    error;
  private Validator validator;
  /**
   * Default constructur.
   * <p>
   * It will use {@link ValidationResources}.
   */
  public TextAreaValidation()
  {
    this((ValidationResources) GWT.create(ValidationResources.class));
  }

  /**
   * @param pResources
   *          the resource to use
   */
  public TextAreaValidation(final ValidationResources pResources)
  {
    RESSOURCE = pResources;
    RESSOURCE.style().ensureInjected();
    // Generate ui
    initWidget(UI_BINDER.createAndBindUi(this));
    validator = new Validator.LongTextValidator();
    bind();
  }

  private void bind()
  {

    textarea.addKeyUpHandler(new KeyUpHandler()
    {
      @Override
      public void onKeyUp(final KeyUpEvent event)
      {
        TextAreaValidation.this.isValid();
      }
    });
    textarea.addValueChangeHandler(new ValueChangeHandler<String>()
    {
      @Override
      public void onValueChange(final ValueChangeEvent<String> pEvent)
      {
        TextAreaValidation.this.isValid();

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
    final boolean valid = validator.isValid(textarea.getValue());
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
      textarea.addStyleName(RESSOURCE.style().textInvalid());
      error.setText(validator.getErrorMessage());
    }
    else
    {
      textarea.removeStyleName(RESSOURCE.style().textInvalid());
      error.setText(EMPTY_TEXT);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue()
  {
    return textarea.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String pValue)
  {
    textarea.setValue(pValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String value, final boolean fireEvents)
  {
    textarea.setValue(value, fireEvents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return textarea.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    textarea.setName(pName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final String pValue)
  {
    textarea.ensureDebugId(pValue);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear()
  {
    textarea.removeStyleName(RESSOURCE.style().textInvalid());
    textarea.setText(EMPTY_TEXT);
    error.setText(EMPTY_TEXT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler)
  {
    return textarea.addKeyUpHandler(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler)
  {
    return textarea.addValueChangeHandler(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnable(final boolean b)
  {
    textarea.setEnabled(b);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus(final boolean pB)
  {
    textarea.setFocus(pB);

  }

  /**
   * @param textAreaWidth
   * @see TextArea#setCharacterWidth(int)
   */
  public void setCharacterWidth(final int textAreaWidth)
  {
    textarea.setCharacterWidth(textAreaWidth);
  }

  /**
   * @param textAreaHeight
   * @see TextArea#setVisibleLines(int)
   */
  public void setVisibleLines(final int textAreaHeight)
  {
    textarea.setVisibleLines(textAreaHeight);

  }

  interface TextAreaValidationUiBinder extends UiBinder<Widget, TextAreaValidation>
  {
  }

}
