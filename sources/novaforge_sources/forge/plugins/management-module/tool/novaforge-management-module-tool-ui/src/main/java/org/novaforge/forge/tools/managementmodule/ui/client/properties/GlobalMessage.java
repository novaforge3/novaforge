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

package org.novaforge.forge.tools.managementmodule.ui.client.properties;

import com.google.gwt.i18n.client.Messages;

public interface GlobalMessage extends Messages {

	
   String buttonAdd();
   String buttonApply();
   String buttonCancel();
   String buttonClose();
   String buttonCreate();
   String buttonDelete();
   String buttonEdit();
   String buttonExit();
   String buttonNew();
   String buttonModify();
   String buttonSave();
   String buttonShowDiagram();
   String buttonUnValidate();
   String buttonValidate();
   String buttonVisualize();
   String buttonExportCSV();

   String all();
   String date();
   String description();
   String name();
   String no();
   String phase();
   String resultTitle();
   String filterTitle();
   String status();
   String type();
   String version();
   String yes();
   String iteration();
   String lot();
   String subLot();
   String parentLot();
   String SU();
   String SUParent();
   String scopeUnit();
   String scopeUnitParent();
   String noScopeUnitParent();
   String none();
   String discipline();
   
   String loadingMessage();
   String select();
   String messageNoElements();
   String messageNotAnInteger();
   /**
    * Get the message when an user do an input of a non float value in a float field
    * @return the message
    */
   String messageNotAFloat();
   String messageRequiredField();
   String messageRequiredFieldOrDataToLong();
   String messageSaveDone();
   String messageWrongInputValue();
   String messageExportCSVError();
   
   String popupEmail();
   String popupInfo();
   String popupValidate();
   
   /** Message which is display when an user as insufficientRights to access a functionnality **/
   String insufficientRights();
   
   String homeReturn();
   String noData();
}
