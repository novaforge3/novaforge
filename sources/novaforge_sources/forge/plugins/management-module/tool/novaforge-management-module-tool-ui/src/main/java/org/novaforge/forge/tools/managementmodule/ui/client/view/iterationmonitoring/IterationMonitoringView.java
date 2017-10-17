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
package org.novaforge.forge.tools.managementmodule.ui.client.view.iterationmonitoring;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitIterationMonitoringDTO;

import java.util.List;

/**
 * Interface of the view 
 */
public interface IterationMonitoringView extends IsWidget {

   /**
    * Get the data provider
    */
   AsyncDataProvider<ScopeUnitIterationMonitoringDTO> getDataProvider();

   /**
    * Update the sort handler of the cell table
    */
   void updateSortHandler();

   /**
    * Get the homeReturnButton
    * @return the homeReturnButton
    */
   Button getHomeReturnButton();

   /**
    * Get the iterationDetailButton
    * @return the iterationDetailButton
    */
   Button getIterationDetailButton();

   /**
    * Get the iterationValueLabel
    * @return the iterationValueLabel
    */
   Label getIterationValueLabel();

   /**
    * Get the lotValueLabel
    * @return the lotValueLabel
    */
   Label getLotValueLabel();

   /**
    * Get the subLotValueLabel
    * @return the subLotValueLabel
    */
   Label getParentLotValueLabel();

   /**
    * Get the startDatevalueLabel
    * @return the startDatevalueLabel
    */
   Label getStartDateValueLabel();

   /**
    * Get the endDatevalueLabel
    * @return the endDatevalueLabel
    */
   Label getEndDateValueLabel();

   /**
    * Get the disciplinesListBox
    * @return the disciplinesListBox
    */
   CustomListBox<DisciplineDTO> getDisciplinesListBox();

   /**
    * Get the nbActorsTB
    * @return the nbActorsTB
    */
   TextBox getNbActorsTB();

   /**
    * Get the consumedTB
    * @return the consumedTB
    */
   TextBox getConsumedTB();

   /**
    * Get the focalisationTB
    * @return the focalisationTB
    */
   TextBox getFocalisationTB();

   /**
    * Get the velocityTB
    * @return the velocityTB
    */
   TextBox getVelocityTB();

   /**
    * Get the errorTB
    * @return the errorTB
    */
   TextBox getErrorTB();

   /**
    * Get the advancementTB
    * @return the advancementTB
    */
   TextBox getAdvancementTB();

   /**
    * Get the iterationMonitoringCT
    * @return the iterationMonitoringCT
    */
   CellTable<ScopeUnitIterationMonitoringDTO> getIterationMonitoringCellTable();

   /**
    * Get the selection model
    * @return the selection Model
    */
   SingleSelectionModel<ScopeUnitIterationMonitoringDTO> getSelectionModel();

   /**
    * Get the fullDataList
    * @return the fullDataList
    */
   List<ScopeUnitIterationMonitoringDTO> getFullDataList();

   /**
    * Set the complet data list in the view
    *
    * @param pResult
    */
   void setFullDataList(List<ScopeUnitIterationMonitoringDTO> list);
   
   /**
    * Get the csvExportButton
    * @return the csvExportButton
    */
   Button getCsvExportButton();

   
   
}
