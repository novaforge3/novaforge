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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.DateBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.UserDTO;

/**
 * The view of task edition
 */
public interface TaskEditView extends IsWidget {

   /**
    * Get the buttonSave
    * @return the buttonSave
    */
   Button getButtonSave();

   /**
    * Get the buttonCancel
    * @return the buttonCancel
    */
   Button getButtonCancel();

   /**
    * Get the labelTitle
    * @return the labelTitle
    */
   Label getLabelTitle();

   /**
    * Get the iterationNameTB
    * @return the iterationNameTB
    */
   TextBox getIterationNameTB();

   /**
    * Get the parentLotNameTB
    * @return the parentLotNameTB
    */
   TextBox getParentLotNameTB();

   /**
    * Get the typeNameTB
    * @return the typeNameTB
    */
   TextBox getTypeNameTB();

   /**
    * Get the uPNameTB
    * @return the uPNameTB
    */
   TextBox getUPNameTB();

   /**
    * Get the parentUpNameTB
    * @return the parentUpNameTB
    */
   TextBox getParentUpNameTB();

   /**
    * Get the startDateDBV
    * @return the startDateDBV
    */
   DateBoxValidation getStartDateDBV();

   /**
    * Get the endDateDBV
    * @return the endDateDBV
    */
   DateBoxValidation getEndDateDBV();

   /**
    * Get the stateLB
    * @return the stateLB
    */
   CustomListBox<TaskStatusDTO> getStatusLB();

   /**
    * Get the disciplineLB
    * @return the disciplineLB
    */
   CustomListBox<DisciplineDTO> getDisciplineLB();

   /**
    * Get the userLB
    * @return the userLB
    */
   CustomListBox<UserDTO> getUserLB();

   /**
    * Get the categoryLB
    * @return the categoryLB
    */
   CustomListBox<TaskCategoryDTO> getCategoryLB();

   /**
    * Get the initialEstimationTBV
    * @return the initialEstimationTBV
    */
   TextBoxValidation getInitialEstimationTBV();

   /**
    * Get the suiviGrid
    * @return the suiviGrid
    */
   Grid getSuiviGrid();

   /**
    * Get the suiviFieldSetTitle
    * @return the suiviFieldSetTitle
    */
   Label getSuiviFieldSetTitle();

   /**
    * Get the remainingTimeTBV
    * @return the remainingTimeTBV
    */
   TextBoxValidation getRemainingTimeTBV();

   /**
    * Get the consumedTimeTBV
    * @return the consumedTimeTBV
    */
   TextBoxValidation getConsumedTimeTBV();

   /**
    * Get the reEstimatedTBV
    * @return the reEstimatedTBV
    */
   TextBoxValidation getReEstimatedTBV();

   /**
    * Get the descriptionTA
    * @return the descriptionTA
    */
   TextArea getDescriptionTA();

   /**
    * Get the commentTA
    * @return the commentTA
    */
   TextArea getCommentTA();

   /**
    * Get the lotNameTB
    * @return the lotNameTB
    */
   TextBox getLotNameTB();

   /**
    * Get the nameTBV
    * @return the nameTBV
    */
   TextBoxValidation getNameTBV();

   /**
    * Get the buttonReOpen
    * @return the buttonReOpen
    */
   Button getButtonReOpen();

   /**
    * Get the buttonInfos
    * @return the buttonInfos
    */
   Button getButtonInfos();

   /**
    * Get the buttonChooseBug
    * @return the buttonChooseBug
    */
   Button getButtonChooseBug();

   /**
    * Get the bugTitleTB
    * @return the bugTitleTB
    */
   TextBox getBugTitleTB();

   /**
    * Get the bugPanel
    * @return the bugPanel
    */
   Panel getBugPanel();
}
