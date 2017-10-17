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

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author BILET-JC
 * 
 */
public class CustomCellTable<T> extends CellTable<T> {

	SingleSelectionModel<T> selectionModel;

	/**
	 * 
	 */
	public CustomCellTable() {
		super();

	}

	/**
	 * @param pageSize
	 * @param keyProvider
	 */
	public CustomCellTable(final int pageSize, final ProvidesKey<T> keyProvider) {
		super(pageSize, keyProvider);
	}

	/**
	 * @param pageSize
	 * @param resources
	 * @param keyProvider
	 * @param loadingIndicator
	 */
	public CustomCellTable(final int pageSize, final com.google.gwt.user.cellview.client.CellTable.Resources resources,
			final ProvidesKey<T> keyProvider, Widget loadingIndicator) {
		super(pageSize, resources, keyProvider, loadingIndicator);

		sinkEvents(Event.ONKEYDOWN);
		addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				final int key = event.getNativeKeyCode();
				//if selectionModel exist and used
				if (selectionModel != null && selectionModel.getSelectedObject() != null) {
					int pos;
					T object;
					
					if (KeyCodes.KEY_UP == key){
					   pos = getVisibleItems().indexOf(selectionModel.getSelectedObject());
                  if (pos > 0) {
                     object = getVisibleItems().get(pos - 1);
                     selectionModel.setSelected(object, true);
                     event.stopPropagation();
                     event.preventDefault();
                  }
					}else if (KeyCodes.KEY_DOWN == key){
					   pos = getVisibleItems().indexOf(selectionModel.getSelectedObject());
                  if (pos != getVisibleItems().size() - 1) {
                     object = getVisibleItems().get(pos + 1);
                     selectionModel.setSelected(object, true);
                     event.stopPropagation();
                     event.preventDefault();
                  }
					}
				}					
			}
		}, KeyDownEvent.getType());
	}

	/**
	 * @param pageSize
	 * @param resources
	 * @param keyProvider
	 */
	public CustomCellTable(int pageSize, com.google.gwt.user.cellview.client.CellTable.Resources resources,
			ProvidesKey<T> keyProvider) {
		super(pageSize, resources, keyProvider);
	}

	/**
	 * @param pageSize
	 * @param resources
	 */
	public CustomCellTable(int pageSize, com.google.gwt.user.cellview.client.CellTable.Resources resources) {
		super(pageSize, resources);
	}

	/**
	 * @param pageSize
	 */
	public CustomCellTable(int pageSize) {
		super(pageSize);
	}

	/**
	 * @param keyProvider
	 */
	public CustomCellTable(ProvidesKey<T> keyProvider) {
		super(keyProvider);
	}

	public Element getKeyboardSelectedElement(int columnIndex, int rowIndex) {
	   Element retour = null;
		final NodeList<TableRowElement> rows = ((TableSectionElement) getChildContainer()).getRows();
		if (rowIndex >= 0 && rowIndex < rows.getLength() && getColumnCount() > 0) {
			final TableRowElement tr = rows.getItem(rowIndex);
			final TableCellElement td = tr.getCells().getItem(columnIndex);
			retour =  getCellParent(td);
		}
		return retour;
	}

	/**
	 * Get the parent element that is passed to the {@link Cell} from the table
	 * cell element.
	 *
	 * @param td
	 *            the table cell
	 * @return the parent of the {@link Cell}
	 */
	private Element getCellParent(TableCellElement td) {
		return td.getFirstChildElement();
	}

	/**
	 * Get the row index of the keyboard selected row.
	 *
	 * @return the row index
	 */
	@Override
	public int getKeyboardSelectedRow()
	{
		return super.getKeyboardSelectedRow();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSelectionModel(final SelectionModel<? super T> selecionModel) {
		super.setSelectionModel(selecionModel);
		if (this.getSelectionModel() instanceof SingleSelectionModel) {
			selectionModel = (SingleSelectionModel<T>) this.getSelectionModel();
		}
	}
}
