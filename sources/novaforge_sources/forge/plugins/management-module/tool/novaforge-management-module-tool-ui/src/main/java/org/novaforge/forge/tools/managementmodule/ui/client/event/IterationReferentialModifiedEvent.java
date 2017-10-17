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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowIterationMonitoringEvent;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;

/**
 * Event which is fired when showing an iteration monitoring is needed
 */
public class IterationReferentialModifiedEvent extends GwtEvent<IterationReferentialModifiedEvent.Handler>
{
   public final static Type<IterationReferentialModifiedEvent.Handler> TYPE = new Type<IterationReferentialModifiedEvent.Handler>();
   /** The iteration modified - (facultative)*/
   private IterationDTO iteration;
   
   /**
    * Constructor with an iteration (to indicate on which iteration is the modification)
    * @param iterationDTO the iteration
    */
   public IterationReferentialModifiedEvent(final IterationDTO iterationDTO)
   {
      super();
      iteration = iterationDTO;
   }

   /**
    * Constructor without iteration - indicates something has been updates on iteration referential
    */
   public IterationReferentialModifiedEvent() {
      super();
   }

   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<IterationReferentialModifiedEvent.Handler> getAssociatedType()
   {
      return TYPE;
   }
   
   @Override
   protected void dispatch(final IterationReferentialModifiedEvent.Handler pHandler)
   {
      pHandler.onModifyIterationReferential(this);

   }
   
   /**
    * Get the iteration
    * @return the iteration
    */
   public IterationDTO getIteration() {
      return iteration;
   }

   public void setIteration(final IterationDTO iteration)
   {
      this.iteration = iteration;
   }

   /**
    * Indicate if event has iteration
    * @return true if iteration has iteration false otherwise
    */
   public boolean hasIteration(){
      return iteration != null;
   }

   /**
    * Handler interface for {@link ShowIterationMonitoringEvent} events.
    */
   public interface Handler extends EventHandler
   {
      /**
       * @param event
       */
      void onModifyIterationReferential(IterationReferentialModifiedEvent event);

   }
}
