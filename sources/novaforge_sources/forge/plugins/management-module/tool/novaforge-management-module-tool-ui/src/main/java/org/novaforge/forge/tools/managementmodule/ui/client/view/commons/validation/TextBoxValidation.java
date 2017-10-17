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
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation;

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

/**
 * @author lamirang
 */
public class TextBoxValidation extends Composite implements TextBoxBaseValidation
{

   private static final String EMPTY_TEXT = "";
   private static ValidationResources       ressource  = null;
   private static TextBoxValidationUiBinder uiBinder   = GWT.create(TextBoxValidationUiBinder.class);
   @UiField
   TextBox textbox;
   @UiField
   Label   error;
   private Validator validator;
   public TextBoxValidation()
   {
      this((ValidationResources) GWT.create(ValidationResources.class));
   }

   public TextBoxValidation(final ValidationResources pResources)
   {
      ressource = pResources;
      ressource.style().ensureInjected();
      // Generate ui
      initWidget(uiBinder.createAndBindUi(this));
      validator = new Validator()
      {
         @Override
         public boolean isValid(String pValue)
         {
            return pValue != null || !EMPTY_TEXT.equals(pValue);
         }

         @Override
         public String getErrorMessage()
         {
            return "Unknown validation error";
         }
      };
      bind();
   }

   private void bind()
   {

      textbox.addKeyUpHandler(new KeyUpHandler()
      {
         @Override
         public void onKeyUp(KeyUpEvent event)
         {
            setStateStyle();
         }
      });
      textbox.addValueChangeHandler(new ValueChangeHandler<String>()
            {
         @Override
         public void onValueChange(ValueChangeEvent<String> pEvent)
         {
            setStateStyle();

         }

            });
   }

   private void setStateStyle()
   {
      if (!isValid())
      {
         textbox.setStyleName(ressource.style().textInvalid());
         error.setText(validator.getErrorMessage());
      }
      else
      {
         textbox.setStyleName(ressource.style().text());
         error.setText(EMPTY_TEXT);
      }
   }

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
      boolean valid = validator.isValid(textbox.getValue().trim());
      if (!valid)
      {
         textbox.setStyleName(ressource.style().textInvalid());
         error.setText(validator.getErrorMessage());
      }
      return valid;
   }

   @Override
   public String getValue()
   {
      return textbox.getValue();
   }

   @Override
   public void setValue(final String pValue)
   {
      textbox.setValue(pValue);
   }

   @Override
   public void setValue(final String value, boolean fireEvents)
   {
      textbox.setValue(value, fireEvents);
   }

   @Override
   public void setId(String pValue)
   {
      textbox.ensureDebugId(pValue);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void clear()
   {
      textbox.setStyleName(ressource.style().text());
      textbox.setText(EMPTY_TEXT);
      error.setText(EMPTY_TEXT);
   }

   @Override
   public HandlerRegistration addKeyUpHandler(KeyUpHandler handler)
   {
      return textbox.addKeyUpHandler(handler);
   }

   @Override
   public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler)
   {
      return textbox.addValueChangeHandler(handler);
   }

   @Override
   public void setFocus()
   {
      textbox.setFocus(true);

   }

   @Override
   public void setEnabled(boolean b)
   {
      textbox.setEnabled(b);
   }

	@Override
	public void setWidth(String width) {
	   textbox.setWidth(width);
	}

   interface TextBoxValidationUiBinder extends UiBinder<Widget, TextBoxValidation>
   {
   }
}
