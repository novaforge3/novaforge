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
package org.novaforge.forge.tools.managementmodule.ui.client.view.marker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import org.novaforge.forge.tools.managementmodule.ui.client.ressources.ManagementModuleRessources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.DateBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextAreaValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

public class MarkerEditViewImpl extends Composite implements MarkerEditView {

   private static MarkerEditViewImplUiBinder uiBinder = GWT
         .create(MarkerEditViewImplUiBinder.class);
   private static ManagementModuleRessources ressources = GWT
         .create(ManagementModuleRessources.class);
   private final ValidateDialogBox validateDialogBox;
   @UiField
   Button buttonAddMarker;
   @UiField
   Button buttonCancelMarker;
   @UiField
   Button buttonSaveMarker;
   @UiField
   Label markerNameLabel;
   @UiField
   Label markerDateLabel;
   @UiField
   ListBox markerTypeList;
   @UiField
   Label markerDescLabel;
   @UiField
   Label markerCreationTitle;
   @UiField
   Label markerTypeListLabel;
   @UiField
   Grid markerGrid;
   @UiField
   DateBoxValidation markerDatePicker;
   @UiField
   TextBoxValidation markerNameValidator;
   @UiField
   TextAreaValidation markerDescValidator;

   public MarkerEditViewImpl() {
      // Generate ui
      initWidget(uiBinder.createAndBindUi(this));
      markerCreationTitle.setText(Common.getProjectPlanMessages().markerCreationTitle());

      buttonAddMarker.setText(Common.getGlobal().buttonAdd());
      buttonSaveMarker.setText(Common.getGlobal().buttonSave());
      buttonCancelMarker.setText(Common.getGlobal().buttonCancel());

      markerNameLabel.setText(Common.getGlobal().name());
      markerDateLabel.setText(Common.getGlobal().date());
      markerTypeListLabel.setText(Common.getGlobal().type());
      markerDescLabel.setText(Common.getGlobal().description());


      markerDescValidator.getTextArea().setCharacterWidth(60);
      markerDescValidator.getElement().setAttribute("maxlength", "2000");

      markerDatePicker.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy")));

      for (int row = 0; row < markerGrid.getRowCount(); row++) {
         if (row % 2 == 0) {
            markerGrid.getRowFormatter().addStyleName(row, ressources.css().gridRowPair());
         }
         markerGrid.getCellFormatter().addStyleName(row, 0, ressources.css().labelCell());
      }

      validateDialogBox = new ValidateDialogBox(Common.getProjectPlanMessages()
            .addValidationCreateMarkerMessage());
   }

   @Override
   public Label getMarkerNameLabel()
   {
      return markerNameLabel;
   }

   @Override
   public Label getMarkerDateLabel()
   {
      return markerDateLabel;
   }

   @Override
   public ListBox getMarkerTypeList()
   {
      return markerTypeList;
   }

   @Override
   public Button getButtonAddMarker()
   {
      return buttonAddMarker;
   }

   @Override
   public Button getButtonCancelMarker()
   {
      return buttonCancelMarker;
   }

   @Override
   public Button getButtonSaveMarker()
   {
      return buttonSaveMarker;
   }

   @Override
   public DateBoxValidation getMarkerDatePicker() {
      return markerDatePicker;
   }

   @Override
   public TextBoxValidation getMarkerNameValidator() {
      return markerNameValidator;
   }

   @Override
   public TextAreaValidation getMarkerDescValidator() {
      return markerDescValidator;
   }

   @Override
   public ValidateDialogBox getValidateDialogBox() {
      return validateDialogBox;
   }

   interface MarkerEditViewImplUiBinder extends UiBinder<Widget, MarkerEditViewImpl>
   {
   }
}
