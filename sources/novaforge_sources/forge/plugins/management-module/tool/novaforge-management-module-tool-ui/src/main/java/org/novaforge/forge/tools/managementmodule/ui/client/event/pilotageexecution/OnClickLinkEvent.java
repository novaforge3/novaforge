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

package org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution;

import com.google.gwt.event.shared.GwtEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.PresenterEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.AnchorsEnum;

/**
 * Event produced when clicking on a link
 */
public class OnClickLinkEvent extends GwtEvent<LinkEventHandler> {

   /** The TYPE */
   public final static Type<LinkEventHandler> TYPE = new Type<LinkEventHandler>();
   
   /** The target presenter */
   private PresenterEnum presenter;
   
   /** An additional parameter for the link*/
   private AnchorsEnum anchor;
   
   /**
    * Get the presenter
    * @return the presenter
    */
   public PresenterEnum getPresenter() {
      return presenter;
   }

   /**
    * Set the presenter
    * @param presenter the presenter to set
    */
   public void setPresenter(final PresenterEnum presenter) {
      this.presenter = presenter;
   }

   /**
    * Get the anchor
    * @return the anchor
    */
   public AnchorsEnum getAnchor() {
      return anchor;
   }

   /**
    * Set the anchor
    * @param anchor the anchor to set
    */
   public void setAnchor(final AnchorsEnum anchor) {
      this.anchor = anchor;
   }

   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<LinkEventHandler> getAssociatedType() {
      return TYPE;
   }

   @Override
   protected void dispatch(final LinkEventHandler handler) {
      handler.onClickLinkEvent(this);
   }
}
