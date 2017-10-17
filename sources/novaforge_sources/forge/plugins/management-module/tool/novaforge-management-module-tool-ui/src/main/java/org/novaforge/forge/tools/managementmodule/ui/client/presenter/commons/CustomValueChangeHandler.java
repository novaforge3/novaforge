/**
 * Copyright ( c ) 2011-2016, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * used to manage value change events, allow to retrieve old value
 * @author s267533
 *
 */
public abstract class CustomValueChangeHandler<T> implements ValueChangeHandler<T> {

  /**
   * previous value
   */
  private T prevValue = null;
  
  /**
   * current  value
   */
  private T value = null;
  
  public CustomValueChangeHandler(final ValueBoxBase<T> widget) {
      widget.addFocusHandler(new FocusHandler() {
          public void onFocus(FocusEvent event) {
              prevValue = widget.getValue();
          }
      });
  }
  
  @Override
  public void onValueChange(ValueChangeEvent<T> event) {
      value = event.getValue();
      onValueChange(value, prevValue);
  
      // or
      // onValueChange(event, prevValue);
  
      prevValue = value;
  }
  
  public abstract void onValueChange(T value, T prevValue);

}