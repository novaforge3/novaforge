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
package org.novaforge.forge.ui.distribution.client.view.diffusion;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.commons.client.validation.TextBoxBaseValidation;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;

public interface DiffusionDifferedTabView extends IsWidget
{
   CellTable<TargetForgeDTO> getTargetForges();

   ListDataProvider<TargetForgeDTO> getTargetForgesDataProvider();

   Label getTargetForgesTitle();

   CheckBox getActiveCheckButton();

   TextBoxBaseValidation getTime();

   TextBoxBaseValidation getPeriod();

   Button getSaveButton();

   ValidateDialogBox getPropagatedDialogBox();


   void disableAllField();

   void enableAllField();

}
