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
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;

/**
 * @author BILET-JC
 *
 *This class describes a checkboxCell which can be disable
 */
public class CheckboxDisabledCell extends AbstractInputCell<Boolean, Boolean> {
	 /**
	   * An html string representation of a checked input box.
	   */
	  private static final SafeHtml INPUT_CHECKED = SafeHtmlUtils.fromSafeConstant("<input TYPE=\"checkbox\" tabindex=\"-1\" checked/>");

	  /**
	   * An html string representation of an unchecked input box.
	   */
	  private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant("<input TYPE=\"checkbox\" tabindex=\"-1\"/>");
		 
	  /**
	   * An html string representation of a disabled checked input box.
	   */
	  private static final SafeHtml INPUT_CHECKED_DISABLED = SafeHtmlUtils.fromSafeConstant("<input TYPE=\"checkbox\" disabled tabindex=\"-1\" checked/>");

	  /**
	   * An html string representation of a disabled unchecked input box.
	   */
	  private static final SafeHtml INPUT_UNCHECKED_DISABLED = SafeHtmlUtils.fromSafeConstant("<input TYPE=\"checkbox\" disabled tabindex=\"-1\"/>");

	  private final boolean dependsOnSelection;
	  private final boolean handlesSelection;

	  private boolean disabled;

	  /**
	   * Construct a new {@link CheckboxCell}.
	   */
	  public CheckboxDisabledCell() {
	    this(false);
	  }

	  /**
	   * Construct a new {@link CheckboxCell} that optionally controls selection.
	   *
	   * @param isSelectBox true if the cell controls the selection state
	   * @deprecated use {@link #CheckboxCell(boolean, boolean)} instead
	   */
	  @Deprecated
	  public CheckboxDisabledCell(boolean isSelectBox) {
	    this(isSelectBox, isSelectBox);
	  }

	  /**
	   * Construct a new {@link CheckboxCell} that optionally controls selection.
	   *
	   * @param dependsOnSelection true if the cell depends on the selection state
	   * @param handlesSelection true if the cell modifies the selection state
	   */
	  public CheckboxDisabledCell(boolean dependsOnSelection, boolean handlesSelection) {
	    super("change", "keydown");
	    this.dependsOnSelection = dependsOnSelection;
	    this.handlesSelection = handlesSelection;
	    disabled = false;
	  }

	  @Override
	  public boolean isEditing(Context context, Element parent, Boolean value) {
	    // A checkbox is never in "edit mode". There is no intermediate state
	    // between checked and unchecked.
	    return false;
	  }

	  @Override
		public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
	    String type = event.getType();

	    boolean enterPressed = "keydown".equals(type)
	        && event.getKeyCode() == KeyCodes.KEY_ENTER;
	    if ("change".equals(type) || enterPressed) {
	      InputElement input = parent.getFirstChild().cast();
	      Boolean isChecked = input.isChecked();

	      /*
	       * Toggle the value if the enter key was pressed and the cell handles
	       * selection or doesn't depend on selection. If the cell depends on
	       * selection but doesn't handle selection, then ignore the enter key and
	       * let the SelectionEventManager determine which keys will trigger a
	       * change.
	       */
	      if (enterPressed && (handlesSelection() || !dependsOnSelection())) {
	        isChecked = !isChecked;
	        input.setChecked(isChecked);
	      }

	      /*
	       * Save the new value. However, if the cell depends on the selection, then
	       * do not save the value because we can get into an inconsistent state.
	       */
	      if (value != isChecked && !dependsOnSelection()) {
	        setViewData(context.getKey(), isChecked);
	      } else {
	        clearViewData(context.getKey());
	      }

	      if (valueUpdater != null) {
	        valueUpdater.update(isChecked);
	      }
	    }
		}

	@Override
	public boolean dependsOnSelection()
	{
		return dependsOnSelection;
	}

	@Override
	public boolean handlesSelection()
	{
		return handlesSelection;
	}

	  @Override
	  public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
	    // Get the view data.
	    Object key = context.getKey();
	    Boolean viewData = getViewData(key);
	    if (viewData != null && viewData.equals(value)) {
	      clearViewData(key);
	      viewData = null;
	    }
	    if (!((EstimationDTO) key).getScopeUnit().hasChild()) {
		    if (value != null && ((viewData != null) ? viewData : value)) {
		    	if (disabled) {
		  	      sb.append(INPUT_CHECKED_DISABLED);
				}
		    	else {
		  	      sb.append(INPUT_CHECKED);
		    	}
		    } else {
		    	if (disabled) {
		  	      sb.append(INPUT_UNCHECKED_DISABLED);				
				}
		    	else {
		  	      sb.append(INPUT_UNCHECKED);	    		
		    	}
		    }
		}
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
}
