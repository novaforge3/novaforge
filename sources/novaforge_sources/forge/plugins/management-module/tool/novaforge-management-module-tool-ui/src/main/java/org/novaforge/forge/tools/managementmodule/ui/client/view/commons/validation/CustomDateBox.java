/**
 * Copyright ( c ) 2011-2017, BULL SAS, NovaForge Version 3 and above.
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
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation;

import java.util.Date;

import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * DateBox which handles default month selection correctly 
 *
 */
public class CustomDateBox extends DateBox {

  /**
   * defaultMonth to show in datePicker
   */
  private Date defaultMonth = null;

  /**
   * default constructor
   */
  public CustomDateBox()
  {
    super(); 
  }
  
  /**
   * constructor
   * @param picker picker
   * @param date date to set
   * @param format display format
   */
  public CustomDateBox(final DatePicker picker, final Date date, final Format format) {
      super(picker, date, format);
  }

  /**
   * set default month to set in date picker
   * @param m
   */
  void setDefaultMonth(final Date month) {
      this.getDatePicker().setCurrentMonth(month);
      defaultMonth = month;
  }

  /**
   * override native method to make current month selection works in date picker
   * not clean... but it works
   * {@inheritDoc}
   */
  @Override
  public void showDatePicker() {
    Date prevDate = getValue();
    if(defaultMonth != null && prevDate == null)
    {
      getDatePicker().setCurrentMonth(defaultMonth);
      setValue(defaultMonth);
    }
    super.showDatePicker();
    if(defaultMonth != null && prevDate == null)
    {
      setValue(null);
    }    
  }
}
