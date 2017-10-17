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
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ListBox;

import java.util.HashMap;
import java.util.Map;

/**
 * A custom list box is a listBox where it is possible to set a full object at item insert and to recover it
 * after
 */
public class CustomListBox<V> extends ListBox {

   /** The map which stocks the associated object by value */
   final Map<String, V> associatedObjectByValue = new HashMap<String, V>();
   
   /**
    * Adds an item to the list box, specifying an initial value for the item, an associating a full object to
    * tHis element
    * 
    * @param item the text of the item to be added
    * @param value the item's value, to be submitted if it is part of a {@link FormPanel}; cannot be
    *           <code>null</code>
    * @param objectToAssociate the object to associate to this item
    */
   public void addItem(String item, String value, V objectToAssociate) {
     super.addItem(item, value);
     associatedObjectByValue.put(value, objectToAssociate);
   }
   
   /**
    * Get the selected associated object
    * @return the associated object at the selected index
    */
   public V getSelectedAssociatedObject()
   {
      return getAssociatedObject(super.getSelectedIndex());
   }

   /**
    * Get the associated object for the the index param
    * @param index the index to use
    * @return the object or null if none
    */
   public V getAssociatedObject(int index){
      final String value = super.getValue(index);
      return getAssociatedObject(value);
   }
   
   /**
    * Get the associated object for a option value
    * @param value the value to use
    * @return the object or null if none
    */
   public V getAssociatedObject(final String value) {
      return this.associatedObjectByValue.get(value);
   }
   
   /**
    * Get the selected value
    * @return the value at the selected index
    */
   public String getValue() {
      return super.getValue(super.getSelectedIndex());
   }
   
}
