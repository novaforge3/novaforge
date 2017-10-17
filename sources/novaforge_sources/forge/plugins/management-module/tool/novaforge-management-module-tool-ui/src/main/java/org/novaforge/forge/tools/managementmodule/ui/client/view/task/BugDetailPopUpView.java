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
package org.novaforge.forge.tools.managementmodule.ui.client.view.task;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Interface of the view which display the details of a bug
 */
public interface BugDetailPopUpView extends IsWidget {
   
   /**
    * Get the titleTB
    * @return the titleTB
    */
   TextBox getTitleTB();

   /**
    * Get the assignedToTB
    * @return the assignedToTB
    */
   TextBox getAssignedToTB();

   /**
    * Get the statusTB
    * @return the statusTB
    */
   TextBox getStatusTB();

   /**
    * Get the severityTB
    * @return the severityTB
    */
   TextBox getSeverityTB();

   /**
    * Get the reporterTB
    * @return the reporterTB
    */
   TextBox getReporterTB();

   /**
    * Get the priorityTB
    * @return the priorityTB
    */
   TextBox getPriorityTB();

   /**
    * Get the categoryTB
    * @return the categoryTB
    */
   TextBox getCategoryTB();

   /**
    * Get the closeButton
    * @return the closeButton
    */
   Button getCloseButton();

   /**
    * Get the bugTrackerIdTB
    * @return the bugTrackerIdTB
    */
   TextBox getBugTrackerIdTB();

   /**
    * Hide the pop up
    */
   void hide();

   /**
    * Center the pop up
    */
   void center();
   
}
