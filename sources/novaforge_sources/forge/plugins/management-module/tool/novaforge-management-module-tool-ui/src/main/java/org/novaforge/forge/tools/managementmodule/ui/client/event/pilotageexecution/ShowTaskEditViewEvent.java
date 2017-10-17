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
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.task.TaskEditPresenter.Mode;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;

/**
 * Event produced on making an action to show a task
 */
public class ShowTaskEditViewEvent extends GwtEvent<ShowTaskEditViewEventHandler> {

   public final static Type<ShowTaskEditViewEventHandler> TYPE = new Type<ShowTaskEditViewEventHandler>();
   /** The task to edit */
   private TaskDTO task;
   /** The mode in which edit the task */
   private Mode mode;
   /** The source presenter, useful to go back */
   private TabPresenter tabPresenterSrc;
   
   /**
    * Get the task
    * @return the task
    */
   public TaskDTO getTask() {
      return task;
   }

   /**
    * Set the task
    * @param task the task to set
    */
   public void setTask(final TaskDTO task) {
      this.task = task;
   }
   
	/**
    * Get the mode
    * @return the mode
    */
   public Mode getMode() {
      return mode;
   }

   /**
    * Set the mode
    * @param mode the mode to set
    */
   public void setMode(final Mode mode) {
      this.mode = mode;
   }

   /**
    * Get the tabPresenterSrc
    * @return the tabPresenterSrc
    */
   public TabPresenter getTabPresenterSrc() {
      return tabPresenterSrc;
   }

   

   /**
    * Set the tabPresenterSrc
    * @param tabPresenterSrc the tabPresenterSrc to set
    */
   public void setTabPresenterSrc(final TabPresenter tabPresenterSrc) {
      this.tabPresenterSrc = tabPresenterSrc;
   }



   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<ShowTaskEditViewEventHandler> getAssociatedType() {
      return TYPE;
   }

   @Override
   protected void dispatch(final ShowTaskEditViewEventHandler handler) {
      handler.onShowTaskEditView(this);
   }

}
