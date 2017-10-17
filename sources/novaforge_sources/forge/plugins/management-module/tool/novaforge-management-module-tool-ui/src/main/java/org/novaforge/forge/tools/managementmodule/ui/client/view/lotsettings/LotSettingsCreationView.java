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
package org.novaforge.forge.tools.managementmodule.ui.client.view.lotsettings;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.DateBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextAreaValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextBoxValidation;

public interface LotSettingsCreationView extends IsWidget {

   /**
    * return the label for the lotName field
    * @return
    */
	Label getLotNameLabel();

	/**
	 * return the label for the lotStartDate field
	 * @return
	 */
	Label getLotStartDateLabel();

	/**
	 * return the label for the lotEndDate field
	 * @return
	 */
	Label getLotEndDateLabel();

	/**
	 * return the listBox for LotParent elements
	 * @return
	 */
	ListBox getLotParentList();

	/**
	 * return the AddLot button
	 * @return
	 */
	Button getButtonAddLot();

	/**
    * return the CancelLot button
    * @return
    */
	Button getButtonCancelLot();
	
	/**
    * return the SaveLot button
    * @return
    */
	Button getButtonSaveLot();

	/**
	 * return the startDate field
	 * @return
	 */
	DateBoxValidation getLotStartDatePicker();

	/**
	 * return the endDate field
	 * @return
	 */
	DateBoxValidation getLotEndDatePicker();

	/**
	 * return the name field
	 * @return
	 */
	TextBoxValidation getLotNameValidator();

	/**
	 * return the description field
	 * @return
	 */
	TextAreaValidation getLotDescValidator();

	/**
	 * return the validateDialogBox
	 * @return
	 */
	ValidateDialogBox getValidateDialogBox();

	/**
	 * set the formTitle for Creation
	 */
   void setLotFormTitleCreation();

   /**
    * set the formTitle for Modification
    */
   void setLotFormTitleModification();

}
