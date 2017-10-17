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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A text cell which has his input text element stylable and html attributes
 * 
 * @author BILET-JC
 */
public class StylableTextCell extends TextCell {

	Map<String, String> attributesMap = new HashMap<String, String>();

	/**
	 * StylableEditTextCell Constructor,
	 * 
	 * @param attributesMap
	 *            the attributes map
	 */
	public StylableTextCell(final Map<String, String> attributesMap) {
		super();
		this.attributesMap.putAll(attributesMap);
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

	@Override
	public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		StringBuilder html = new StringBuilder();
		html.append("<div ");
		for (final Entry<String, String> entry : attributesMap.entrySet()) {
			html.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
		}
		html.append(">");
		if (value != null) {
			html.append(value.asString());
		}
		html.append("</div>");
		sb.appendHtmlConstant(html.toString());

	}

}
