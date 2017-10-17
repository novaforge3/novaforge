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

import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * This interface defines a validation component.
 * 
 * @author lamirang
 */
public interface TextBoxBaseValidation
{
   /**
    * @param pValidator
    *           the validator to set
    */
   void setValidator(final Validator pValidator);

   /**
    * @return true if the value is valid.
    */
   boolean isValid();

   /**
    * It will show the error message and set the css
    * 
    * @param pShow
    *           true will show the error message, false will hidde it
    */
   void showError(boolean pShow);

   /**
    * @return strign value
    * @see ValueBoxBase#getValue()
    */
   String getValue();

   /**
    * Sets this object's value without firing any events.
    * 
    * @param pValue
    *           the value to set
    * @see ValueBoxBase#setValue(Object)
    */
   void setValue(final String pValue);

   /**
    * Sets this object's value with firing any events.
    * 
    * @param value
    *           the value to set
    * @param fireEvents
    * @see ValueBoxBase#setValue(Object, boolean)
    */
   void setValue(final String value, boolean fireEvents);

   /**
    * @return widget's name
    * @see ValueBoxBase#getName()
    */
   String getName();

   /**
    * @param pName
    *
    * @see ValueBoxBase#setName(String)
    */
   void setName(final String pName);

   /**
    * @param pValue
    *           the ensure id to set
    */
   void setId(final String pValue);

   /**
    * It will clear the component.
    * <ul>
    * <li>Back to default css</li>
    * <li>Delete all value</li>
    * </ul>
    */
   void clear();

   /**
    * @param handler
    *           the handler to add
    * @return {@link HandlerRegistration} used to remove this handler
    * @see ValueBoxBase#addKeyUpHandler(KeyUpHandler)
    */
   HandlerRegistration addKeyUpHandler(KeyUpHandler handler);

   /**
    * @param handler
    *           the handler to add
    * @return {@link HandlerRegistration} used to remove this handler
    * @see ValueBoxBase#addValueChangeHandler(ValueChangeHandler)
    */
   HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler);

   /**
    * @param b
    *           true will enable the widget, false to disable
    * @see ValueBoxBase#setEnabled(boolean)
    */
   void setEnable(boolean b);

   /**
    * @param b
    *           true will focus the widget, false to unfocus it
    * @see ValueBoxBase#setFocus(boolean)
    */
   void setFocus(boolean b);

}
