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
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An edit text cell which has his input text element stylable with class and
 * html attributes (of <input>)
 */
public class StylableEditTextCell extends EditTextCell {

	Map<String, String> attributesMap = new HashMap<String, String>();

	String className;

	/**
	 * StylableEditTextCell Constructor,
	 * 
	 * @param attributesMap
	 *            the attributes map
	 * @param className
	 *            the className to set in input element
	 */
	public StylableEditTextCell(final Map<String, String> attributesMap, final String className) {
		super();
		this.attributesMap.putAll(attributesMap);
		this.className = className;
	}

	/**
	 * StylableEditTextCell Constructor,
	 * 
	 * @param attributesMap
	 *            the attributes map
	 */
	public StylableEditTextCell(final Map<String, String> attributesMap) {
		super();
		this.attributesMap.putAll(attributesMap);
	}

	/**
	 * StylableEditTextCell Constructor,
	 * 
	 * @param className
	 *            the className to set in input element
	 */
	public StylableEditTextCell(final String className) {
		super();
		this.className = className;
	}

	/**
	 * Set the class name
	 * 
	 * @param className
	 *            the className to set in input element
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * Add an attribute to the StylableEditTextCell
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void addAttribute(final String key, final String value) {
		attributesMap.put(key, value);
	}

	/**
	 * Convert the cell to edit mode.
	 *
	 * @param context
	 *            the {@link Context} of the cell
	 * @param parent
	 *            the parent element
	 * @param value
	 *            the current value
	 */
	@Override
	protected void edit(Context context, Element parent, String value) {
		setValue(context, parent, value);
		InputElement input = getInputElement(parent);
		for (final Entry<String, String> entry : attributesMap.entrySet()) {
			input.setAttribute(entry.getKey(), entry.getValue());
		}
		if (className != null) {
			input.addClassName(className);
		}
		input.focus();
		input.select();
	}

	/**
	 * Get the input element in edit mode.
	 */
	private InputElement getInputElement(Element parent)
	{
		return parent.getFirstChild().cast();
	}
}
