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

import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author lamirang
 */
public interface TextBoxBaseValidation
{
   void setValidator(final Validator pValidator);

   boolean isValid();

   String getValue();

   void setValue(final String pValue);

   void setValue(final String value, boolean fireEvents);

   void setId(final String pValue);

   void clear();

   HandlerRegistration addKeyUpHandler(KeyUpHandler handler);

   HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler);

   void setFocus();
   
   void setEnabled(boolean b);
   
   /**
    * Sets the text box width. This width does not include decorations such as border, margin, and padding.
    * 
    * @param width the object's new width, in CSS units (e.g. "10px", "1em")
    */
   void setWidth(String width);
}
