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
package org.novaforge.forge.tools.deliverymanager.ui.client.view.templates;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import gwtupload.client.IUploader;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldDTO;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.commons.client.validation.TextAreaValidation;
import org.novaforge.forge.ui.commons.client.validation.TextBoxValidation;

/**
 * @author CASERY-J
 * @author Guillaume Lamirand
 */
public interface TemplateReportManageView extends IsWidget
{

   /**
    * Get the save button
    * 
    * @return {@link Button} the save button
    */
   Button getButtonSave();

   /**
    * Get the description textarea
    * 
    * @return {@link TextArea} the description textarea
    */
   TextAreaValidation getDescriptionTB();

   /**
    * Get the name textbox
    * 
    * @return {@link TextBoxValidation} the name textboxvalidation
    */
   TextBoxValidation getNameTB();

   /**
    * Get the fields table
    * 
    * @return a {@link CellTable} of {@link TemplateFieldDTO}
    */
   CellTable<TemplateFieldDTO> getFieldsTable();

   /**
    * Get the data provider
    * 
    * @return a {@link ListDataProvider} of {@link TemplateFieldDTO}
    */
   ListDataProvider<TemplateFieldDTO> getDataProvider();

   /**
    * Get the return button
    * 
    * @return the {@link Button} return
    */
   Button getButtonReturn();

   /**
    * Get the Validate DialogBox
    * 
    * @return the {@link ValidateDialogBox}
    */
   ValidateDialogBox getValidateDialogBox();

   /**
    * Get the Uploader
    * 
    * @return the {@link IUploader}
    */
   IUploader getUploader();

   /**
    * Get the file name
    * 
    * @return the {@link Label} filename
    */
   Label getFileName();

}
