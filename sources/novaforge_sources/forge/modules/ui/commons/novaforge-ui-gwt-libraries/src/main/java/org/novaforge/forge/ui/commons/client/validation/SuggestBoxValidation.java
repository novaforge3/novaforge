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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.Common;

/**
 * This will create a suggest box with a validation behavior. It will also used default CSS.
 *
 * @author Guillaume Lamirand
 */
public class SuggestBoxValidation extends Composite implements TextBoxBaseValidation
{

  private static ValidationResources RESOURCE = (ValidationResources) GWT.create(ValidationResources.class);
  private static SuggestBoxValidationUiBinder UI_BINDER = GWT.create(SuggestBoxValidationUiBinder.class);
  private final             String     defaultText;
  @UiField(provided = true) SuggestBox suggestbox;
  @UiField                  Label      error;
  private                   Validator  validator;

  /**
   * @param pDefaultText
   *     the default text displayed in the text box
   */
  public SuggestBoxValidation(final String pDefaultText)
  {
    defaultText = pDefaultText;
    RESOURCE.style().ensureInjected();
    // Init suggest box
    final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
    final DefaultSuggestionDisplay defaultSuggestionDisplay = new DefaultSuggestionDisplay();
    defaultSuggestionDisplay.setPopupStyleName("gwt-SuggestBoxPopup-common");
    defaultSuggestionDisplay.setAnimationEnabled(true);
    suggestbox = new SuggestBox(oracle, new TextBox(), defaultSuggestionDisplay);
    suggestbox.addStyleDependentName("common");

    // Generate ui
    initWidget(UI_BINDER.createAndBindUi(this));
    validator = new Validator.DefaultValidator();
    bind();
    clear();
  }

  private void bind()
  {

    suggestbox.getTextBox().addKeyUpHandler(new KeyUpHandler()
    {
      @Override
      public void onKeyUp(final KeyUpEvent event)
      {
        SuggestBoxValidation.this.isValid();
      }
    });
    suggestbox.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>()
    {
      @Override
      public void onValueChange(final ValueChangeEvent<String> pEvent)
      {
        SuggestBoxValidation.this.isValid();

      }

    });
    suggestbox.getTextBox().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent pEvent)
      {
        if ((suggestbox.getValue() != null) && (suggestbox.getValue().equals(defaultText)))
        {
          suggestbox.setValue(Common.EMPTY_STRING);
        }
        suggestbox.showSuggestionList();

      }
    });
    suggestbox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>()
    {

      @Override
      public void onSelection(final SelectionEvent<Suggestion> pEvent)
      {
        SuggestBoxValidation.this.isValid();

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
    final boolean valid = validator.isValid(suggestbox.getValue());
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
      suggestbox.getTextBox().removeStyleName(Common.getResources().css().emptyLabel());
      suggestbox.getTextBox().addStyleName(RESOURCE.style().textInvalid());
      error.setText(validator.getErrorMessage());
    }
    else
    {
      suggestbox.getTextBox().removeStyleName(Common.getResources().css().emptyLabel());
      suggestbox.getTextBox().removeStyleName(RESOURCE.style().textInvalid());
      error.setText(Common.EMPTY_STRING);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue()
  {
    return suggestbox.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String pValue)
  {
    suggestbox.getTextBox().removeStyleName(Common.getResources().css().emptyLabel());
    suggestbox.setValue(pValue);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String value, final boolean fireEvents)
  {
    suggestbox.setValue(value, fireEvents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return suggestbox.getTextBox().getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    suggestbox.getTextBox().setName(pName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final String pValue)
  {
    suggestbox.ensureDebugId(pValue);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear()
  {
    suggestbox.getTextBox().removeStyleName(RESOURCE.style().textInvalid());
    suggestbox.getTextBox().addStyleName(Common.getResources().css().emptyLabel());
    suggestbox.setValue(defaultText);
    error.setText(Common.EMPTY_STRING);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler)
  {
    return suggestbox.getTextBox().addKeyUpHandler(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler)
  {
    return suggestbox.getTextBox().addValueChangeHandler(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnable(final boolean b)
  {
    suggestbox.getTextBox().setEnabled(b);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus(final boolean pB)
  {
    suggestbox.setFocus(pB);

  }

  /**
   * @return suggext box
   */
  public SuggestBox getSuggextBox()
  {
    return suggestbox;

  }

  interface SuggestBoxValidationUiBinder extends UiBinder<Widget, SuggestBoxValidation>
  {
  }

}
