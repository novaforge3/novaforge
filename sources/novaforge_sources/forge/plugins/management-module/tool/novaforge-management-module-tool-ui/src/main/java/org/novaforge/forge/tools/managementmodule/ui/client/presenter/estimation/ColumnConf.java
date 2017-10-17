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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.estimation;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;

/**
 * @author BILET-JC
 *
 */
public class ColumnConf {
	private int index;
	private SafeHtml header;
	private Column<EstimationDTO, String> column;
	/**
	 * @param index, -1 if column just have to be add at the end of the table
	 * @param safeHtml
	 * @param column
	 */
	public ColumnConf(int index, SafeHtml safeHtml, Column<EstimationDTO, String> column) {
		super();
		this.index = index;
		this.header = safeHtml;
		this.column = column;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the header
	 */
	public SafeHtml getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(SafeHtml header) {
		this.header = header;
	}
	/**
	 * @return the column
	 */
	public Column<EstimationDTO, String> getColumn() {
		return column;
	}
	/**
	 * @param column the column to set
	 */
	public void setColumn(Column<EstimationDTO, String> column) {
		this.column = column;
	}
	
}
