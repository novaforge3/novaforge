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

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableEditTextCell;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorJointureDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;

/**
 * @author qsivan
 */
public interface ProjectPlanView extends IsWidget {
	
	ListDataProvider<ComponentDTO> getDataComposantsProvider();
	
	ListDataProvider<AdjustFactorJointureDTO> getDataFacteursAjustementProvider();
	
	CellTable<AdjustFactorJointureDTO> getFacteursAjustementList();
	

	/**
    * Get the buttonSaveSettings
    * @return the buttonSaveSettings
    */
   Button getButtonSaveSettings();
   
   /**
    * Get the buttonCancelModifications
    * @return the buttonCancelModifications
    */
   Button getButtonCancelModifications();

   /**
    * Get the buttonHomeReturn
    * @return the buttonHomeReturn
    */
   Button getButtonHomeReturn();

   /**
    * Get the inputAbaqueChargeHJBox
    * @return the inputAbaqueChargeHJBox
    */
   TextBox getInputAbaqueChargeHJBox();

   /**
    * Get the composantsList
    * @return the composantsList
    */
   CellTable<ComponentDTO> getComposantsList();

   /**
    * Get the simpleStylableEditTextCell
    * @return the simpleStylableEditTextCell
    */
   StylableEditTextCell getSimpleStylableEditTextCell();

   /**
    * Get the mediumStylableEditTextCell
    * @return the mediumStylableEditTextCell
    */
   StylableEditTextCell getMediumStylableEditTextCell();

   /**
    * Get the complexStylableEditTextCell
    * @return the complexStylableEditTextCell
    */
   StylableEditTextCell getComplexStylableEditTextCell();

}
