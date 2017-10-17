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

package org.novaforge.forge.tools.managementmodule.ui.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author FALSQUELLE-E
 */
public class ShowListEvent extends GwtEvent<ShowListEventHandler> {

	public final static Type<ShowListEventHandler> TYPE = new Type<ShowListEventHandler>();

	private EnumList listToShow;

	public ShowListEvent(final EnumList list)
	{
		super();
		this.listToShow = list;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ShowListEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final ShowListEventHandler handler) {
		handler.onShowListEvent(this);
	}

   public EnumList getListToShow() {
      return listToShow;
   }

   public void setListToShow(final EnumList enumList){
      this.listToShow = enumList;
   }

	public enum EnumList
	{
		LOT_LIST, ITERATION_LIST, MARKER_LIST, ESTIMATION_LIST, CHARGEPLAN_LIST
	}
}
