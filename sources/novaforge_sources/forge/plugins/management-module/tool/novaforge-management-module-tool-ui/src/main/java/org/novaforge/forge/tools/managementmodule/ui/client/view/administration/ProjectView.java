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

package org.novaforge.forge.tools.managementmodule.ui.client.view.administration;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.managementmodule.ui.shared.CDOParametersDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;

/**
 * @author qsivan
 */
public interface ProjectView extends IsWidget {
	
	ListDataProvider<ProjectDisciplineDTO> getDataDisciplinesProvider();
	
	CellTable<ProjectDisciplineDTO> getDisciplinesList();
	
	ListDataProvider<TaskCategoryDTO> getDataCategoriesProvider();
	
	CellTable<TaskCategoryDTO> getCategoriesList();
	
	HorizontalPanel getAjouterCategoriePanel();
	
	TextBox getInputAjouterCategorieBox();

	CellTable<ProjectDisciplineDTO> getProjectDisciplineList();

	Button getButtonSaveReferentielProjet();

	Button getButtonCancelReferentielProjet();

	TextBox getInputNbrDeJourParAnBox();

	Label getInputNbrDeJourParAnLabel();

	TextBox getInputNbrDeJourParMoisBox();

	Label getInputNbrDeJourParMoisLabel();

	TextBox getInputNbrDeJourParSemaineBox();

	Label getInputNbrDeJourParSemaineLabel();

	TextBox getInputNbrHeuresParJourBox();

	Label getInputNbrHeuresParJourLabel();

	Label getInputNbJoursNonTravailLabel();

	TextBox getInputNbJoursNonTravailBox();

	RadioButton getUniteTempsSemaineRadioButton();

	RadioButton getUniteTempsMoisRadioButton();

	/**
    * Get the buttonHomeReturn
    * @return the buttonHomeReturn
    */
   Button getButtonHomeReturn();

   /**
    * Get the tauxRepartitionColumn
    * @return the tauxRepartitionColumn
    */
   Column<ProjectDisciplineDTO, String> getTauxRepartitionColumn();

   /**
    * Get the total value on discplines
    * @return the value
    */
   int getTotalValueDisciplines();

   /**
    * Get the disciplinePercentTextInputCell
    * @return the disciplinePercentTextInputCell
    */
   EditTextCell getDisciplinePercentTextInputCell();

   /**
    * Get the ajouterCategorieBouton
    * @return the ajouterCategorieBouton
    */
   Image getAjouterCategorieBouton();

   /**
    * Get the ajouterCDOBouton
    * @return the ajouterCDOBouton
    */
   Image getAjouterCDOBouton();
   
   /**
    * Get the ButtonImportRefScopeUnit
    * @return the ButtonImportRefScopeUnit
    */
   Button getButtonImportRefScopeUnit();

	/**
    * Get the componentProvider
    * @return the componentProvider
    */
   ListDataProvider<ComponentDTO> getComponentProvider();

   /**
    * Get the repartitionComponentColumn
    * @return the repartitionComponentColumn
    */
   Column<ComponentDTO, String> getComponentRepartitionColumn();

   /**
    * Get the componentPercentTextInputCell
    * @return the componentPercentTextInputCell
    */
   EditTextCell getComponentPercentTextInputCell();

   /**
    * Get the componentCellTable
    * @return the componentCellTable
    */
   CellTable<ComponentDTO> getComponentCellTable();

   /**
    * Get the CDOCellTable
    * @return the CDOCellTable
    */
   CellTable<CDOParametersDTO> getCDOCellTable();
   
   /**
    * Get the DataProvider for CDOCellTable
    * @return
    */
   ListDataProvider<CDOParametersDTO> getCDODataProvider();
   
   /**
    * Get the total value of the components
    * @return the total
    */
   float getTotalValueComponentPercent();

   Column<CDOParametersDTO, String> getCronExpressionCDOColumn();

   Column<CDOParametersDTO, String> getSystemGraalCDOColumn();

   Column<CDOParametersDTO, String> getRepositoryCDOColumn();

   Column<CDOParametersDTO, String> getProjectCDOColumn();

   Column<CDOParametersDTO, String> getPortCDOColumn();

   Column<CDOParametersDTO, String> getHostCDOColumn();
   
}
