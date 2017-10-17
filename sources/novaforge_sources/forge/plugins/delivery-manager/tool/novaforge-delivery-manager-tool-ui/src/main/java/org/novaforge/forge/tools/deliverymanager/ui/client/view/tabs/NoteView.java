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

package org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldDTO;

import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public interface NoteView extends IsWidget
{

   /**
    * Get the save button
    * 
    * @return {@link Button} the save button
    */
   Button getSaveButton();

   /**
    * Get the template listbox
    * 
    * @return {@link ListBox} the template listbox
    */
   ListBox getTemplatesList();

   /**
    * Get the fields panel
    * 
    * @return {@link Panel} the fields panel
    */
   Panel getFieldsPanel();

   /**
    * Get the fields
    * 
    * @return {@link Map} a map of fields
    */
   Map<String, String> getFields();

   /**
    * Set the template fields
    * 
    * @param pFields
    *           a list of {@link TemplateFieldDTO} fields
    * @param pValue
    *           a map of value
    */
   void setFields(List<TemplateFieldDTO> pFields, Map<String, String> pValue);

   /**
    * Get the fields grid
    * 
    * @return the {@link Grid} of fields
    */
   Grid getFieldsGrid();

   /**
    * Get the template info label
    * 
    * @return the {@link Label} template info
    */
   Label getTemplateInfoLabel();

}
