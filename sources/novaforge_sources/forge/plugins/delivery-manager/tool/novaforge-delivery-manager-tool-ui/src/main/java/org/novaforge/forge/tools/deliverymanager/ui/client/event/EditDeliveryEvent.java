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
package org.novaforge.forge.tools.deliverymanager.ui.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.TabPresenterSource;

/**
 * @author Guillaume Lamirand
 */
public class EditDeliveryEvent extends GwtEvent<EditDeliveryEvent.Handler>
{
   public static Type<EditDeliveryEvent.Handler> TYPE = new Type<EditDeliveryEvent.Handler>();
   private final String                          deliveryReference;
   private final TabPresenterSource              source;
   public EditDeliveryEvent(final String pDeliveryReference, final TabPresenterSource pSource)
   {
      this.deliveryReference = pDeliveryReference;
      this.source = pSource;
   }

   /*
    * (non-Javadoc)
    *
    * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
    */
   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<EditDeliveryEvent.Handler> getAssociatedType()
   {
      return TYPE;
   }

   public TabPresenterSource getSource()
   {
      return source;
   }

   /*
    * (non-Javadoc)
    *
    * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
    */
   @Override
   protected void dispatch(final EditDeliveryEvent.Handler pHandler)
   {
      pHandler.editDelivery(this);

   }

   /**
    * @return the delivery
    */
   public String getDeliveryReference()
   {
      return this.deliveryReference;
   }

   /**
    * Handler interface for {@link EditDeliveryEvent} events.
    */
   public interface Handler extends EventHandler
   {
      /**
       * @param event
       */
      void editDelivery(EditDeliveryEvent event);

   }

}
