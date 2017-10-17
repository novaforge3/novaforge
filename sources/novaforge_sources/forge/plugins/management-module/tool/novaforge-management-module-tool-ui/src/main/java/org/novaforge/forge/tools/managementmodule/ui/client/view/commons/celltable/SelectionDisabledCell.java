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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author BILET-JC
 *
 *This class describes a selectionCell which can be disable
 */
public class SelectionDisabledCell extends AbstractInputCell<String, String> {
	  private static Template template;
	  private final List<String> options;
	private HashMap<String, Integer> indexForOption = new HashMap<String, Integer>();
	private boolean disabled;

	  /**
	   * Construct a new {@link SelectionCell} with the specified options.
	   *
	   * @param options the options in the cell
	   */
	  public SelectionDisabledCell(List<String> options) {
	    super("change");
		disabled = false;
	    if (template == null) {
	      template = GWT.create(Template.class);
	    }
	    this.options = new ArrayList<String>(options);
	    int index = 0;
	    for (String option : options) {
	      indexForOption.put(option, index++);
	    }
	  }

	  @Override
	  public void onBrowserEvent(Context context, Element parent, String value,
	      NativeEvent event, ValueUpdater<String> valueUpdater) {
	    super.onBrowserEvent(context, parent, value, event, valueUpdater);
	    String type = event.getType();
	    if ("change".equals(type)) {
	      Object key = context.getKey();
	      SelectElement select = parent.getFirstChild().cast();
	      String newValue = options.get(select.getSelectedIndex());
	      setViewData(key, newValue);
	      finishEditing(parent, newValue, key, valueUpdater);
	      if (valueUpdater != null) {
	        valueUpdater.update(newValue);
	      }
	    }
	  }

	  @Override
	  public void render(Context context, String value, SafeHtmlBuilder sb) {
	    // Get the view data.
	    Object key = context.getKey();
	    String viewData = getViewData(key);
	    if (viewData != null && viewData.equals(value)) {
	      clearViewData(key);
	      viewData = null;
	    }

	    int selectedIndex = getSelectedIndex(viewData == null ? value : viewData);

	    StringBuilder html = new StringBuilder();
	    html.append("<select ");
		if (disabled) {
			html.append("disabled");
		}
		html.append(" tabindex=\"-1\">");
		sb.appendHtmlConstant(html.toString());
	    int index = 0;
	    for (String option : options) {
	      if (index++ == selectedIndex) {
	        sb.append(template.selected(option));
	      } else {
	        sb.append(template.deselected(option));
	      }
	    }
	    sb.appendHtmlConstant("</select>");

	  }

	  private int getSelectedIndex(String value) {
	    Integer index = indexForOption.get(value);
	    if (index == null) {
	      return -1;
	    }
	    return index.intValue();
	  }

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	interface Template extends SafeHtmlTemplates
	{
		@Template("<option value=\"{0}\">{0}</option>")
		SafeHtml deselected(String option);

		@Template("<option value=\"{0}\" selected=\"selected\">{0}</option>")
		SafeHtml selected(String option);
	}
}
