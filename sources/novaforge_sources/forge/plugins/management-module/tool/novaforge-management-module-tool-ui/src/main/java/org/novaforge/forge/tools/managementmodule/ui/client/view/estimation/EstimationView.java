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
package org.novaforge.forge.tools.managementmodule.ui.client.view.estimation;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PriorizationDTO;

import java.util.Comparator;
import java.util.List;

/**
 * @author BILET-JC
 *
 */
public interface EstimationView extends IsWidget {

	Button getValidateB();
	
	ListDataProvider<EstimationDTO> getDataProvider();
	
	ListDataProvider<ComponentDTO> getFPDataProvider();
	
	void updateSortHandler();

	SingleSelectionModel<EstimationDTO> getSelectionModel();

	CellTable<EstimationDTO> getEstimationCT();

	CellTable<ComponentDTO> getfpCT();

	void displayFP();

	CellTable<EstimationDTO> getTable();
	
	ListBox getfpType();
	
	ListBox getComponentType();
	
	ListBox getLotLB();
	
	ListBox getSubLotLB();
	
	TextBox getTotalCharge();

	HasWidgets getContentPanel();

	List<EstimationDTO> getEstimations();

	Column<EstimationDTO, String> getChargeC();

	void refreshEstimationList(List<EstimationDTO> estimationList);

	HasClickHandlers getAcronymB();

	void displayfpCT(boolean pDisplayElements, boolean isSimple);

	InfoDialogBox getInfoDB();

	/**
	 * Disable cells or not depending on user rights
	 */
	void disabledCells(boolean b);

	void setNbHourByDay(Float nbHourByDay);

	void setAdjustementCoef(Float adjustementCoef);

	Button getDisciplineSharingB();

	ListDataProvider<PriorizationDTO> getPriorizationDataProvider();

	CellTable<PriorizationDTO> getPriorizationCT();

	PriorizationDTO getBenefit();

	PriorizationDTO getRisk();

	PriorizationDTO getInjury();

	Comparator<EstimationDTO> getScopeUnitComparator();

	
	


}
