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

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.novaforge.forge.tools.managementmodule.ui.shared.Identifiable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterizableSelectionCell<T extends Identifiable> extends AbstractInputCell<T, String> {

   private static Template template;
   private final Map<Identifiable, String> options;
   private final Map<String, Identifiable> valueByKey;
  private Map<Identifiable, Integer> indexForOption = new HashMap<Identifiable, Integer>();

   /**
    * Construct a new {@link ParameterizableSelectionCell} with the specified options.
    *
    * @param options the options in the cell
    */
   public ParameterizableSelectionCell(Map<Identifiable, String> options) {
     super("change");
     if (template == null) {
       template = GWT.create(Template.class);
     }
     this.options = new LinkedHashMap<Identifiable, String>(options);
     this.valueByKey = new HashMap<String, Identifiable>();
     int index = 0;
     for (Identifiable option : options.keySet()) {
       indexForOption.put(option, index++);
       valueByKey.put(option.getFunctionalId(), option);
     }
   }

  @Override
   public void onBrowserEvent(Context context, Element parent, T value, NativeEvent event,
         ValueUpdater<T> valueUpdater) {
      super.onBrowserEvent(context, parent, value, event, valueUpdater);
      String type = event.getType();
      if ("change".equals(type)) {
         Object key = context.getKey();
         SelectElement select = parent.getFirstChild().cast();

         @SuppressWarnings("unchecked")
         T newValue = (T) valueByKey.get(select.getValue());

         setViewData(key, ((Identifiable)newValue).getFunctionalId());
         finishEditing(parent, newValue, key, valueUpdater);
         if (valueUpdater != null) {
            valueUpdater.update(newValue);
         }
      }
   }

   @Override
   public void render(Context context, T value, SafeHtmlBuilder sb) {
     // Get the view data.
     Object key = context.getKey();
     //we set the value to the render value
     Identifiable instantValue = value;
     //actual value
     String viewData = getViewData(key);
     //if render on a cell where we have value, we clear the viewData
     if (viewData != null && viewData.equals(((Identifiable)value).getFunctionalId())) {
       clearViewData(key);
       viewData = null;
     }
     //if value exist and hasnt be erased
     if(viewData != null) {
        instantValue = valueByKey.get(viewData);
     }

     int selectedIndex = getSelectedIndex(instantValue);
     sb.appendHtmlConstant("<select tabindex=\"-1\">");
     int index = 0;
     for (Map.Entry<Identifiable, String> option : options.entrySet()) {
       if (index++ == selectedIndex) {
         sb.append(template.selected(option.getKey().getFunctionalId(), option.getValue()));
       } else {
         sb.append(template.deselected(option.getKey().getFunctionalId(), option.getValue()));
       }
     }
     sb.appendHtmlConstant("</select>");
   }

   private int getSelectedIndex(Identifiable value) {
     Integer index = indexForOption.get(value);
     if (index == null) {
       return -1;
     }
     return index.intValue();
   }

  interface Template extends SafeHtmlTemplates
  {
    @Template("<option value=\"{0}\">{1}</option>")
    SafeHtml deselected(String value, String option);

    @Template("<option value=\"{0}\" selected=\"selected\">{1}</option>")
    SafeHtml selected(String value, String option);
   }
 }
