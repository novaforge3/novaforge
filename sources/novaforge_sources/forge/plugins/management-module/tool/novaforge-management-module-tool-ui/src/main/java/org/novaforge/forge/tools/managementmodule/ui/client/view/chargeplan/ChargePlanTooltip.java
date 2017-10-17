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

package org.novaforge.forge.tools.managementmodule.ui.client.view.chargeplan;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanToolTipDTO;

/**
 * @author falsquelle-e
 */
public class ChargePlanTooltip extends PopupPanel {

   private static ChargePlanTooltipUiBinder uiBinder = GWT.create(ChargePlanTooltipUiBinder.class);
   @UiField
   Label lotNamesLabel;
   @UiField
   Label lotNames;
   @UiField
   Label subLotNamesLabel;
   @UiField
   Label subLotNames;
   @UiField
   Label phaseNamesLabel;
   @UiField
   Label phaseNames;
   @UiField
   Label iterationNamesLabel;
   @UiField
   Label iterationNames;
   public ChargePlanTooltip() {
      Common.getResource().css().ensureInjected();
      add(uiBinder.createAndBindUi(this));

      lotNamesLabel.setText(defineLabelField(Common.getGlobal().lot()));
      subLotNamesLabel.setText(defineLabelField(Common.getGlobal().subLot()));
      phaseNamesLabel.setText(defineLabelField(Common.getGlobal().phase()));
      iterationNamesLabel.setText(defineLabelField(Common.getGlobal().iteration()));

      setModal(false);
      setGlassEnabled(false);
   }

   /**
    * Add colon to the label
    *
    * @param label
    * @return
    */
   private String defineLabelField(String label) {
      return label + " :";
   }

   /**
    * set Values in the tooltip for a ChargePlanToolTipDTO
    *
    * @param dto
    *           the based ChargePlanToolTipDTO
    */
   public void initFields(ChargePlanToolTipDTO dto) {

      lotNames.setText(null);
      subLotNames.setText(null);
      phaseNames.setText(null);
      iterationNames.setText(null);
      
      if (dto != null) {

         // LotNames
         StringBuilder lotSB = new StringBuilder();
         if (dto.getLotNames() != null) {
           lotSB.append(dto.getLotNames());
         }
         if (lotSB.toString() != null) {
            lotNames.setText(lotSB.toString());
         }

         // subLotNames
         StringBuilder subLotSB = new StringBuilder();
         if (dto.getChildLotNames() != null){
           subLotSB.append(dto.getChildLotNames());
         }
         if (subLotSB.toString() != null) {
            subLotNames.setText(subLotSB.toString());
         }

         // PhaseNames
         StringBuilder phaseSB = new StringBuilder();
         if (dto.getPhaseNames() != null){
           phaseSB.append(dto.getPhaseNames());
         }
         if (phaseSB.toString() != null) {
            phaseNames.setText(phaseSB.toString());
         }

         // IterationNames
         StringBuilder iterationSB = new StringBuilder();
         if (dto.getIterationNames() != null){
           iterationSB.append(dto.getIterationNames());
         }
         if (iterationSB.toString() != null) {
            iterationNames.setText(iterationSB.toString());
         }
      }

      if (lotNames.getText() != null) {
         lotNamesLabel.setVisible(true);
         lotNames.setVisible(true);
      } else {
         lotNamesLabel.setVisible(false);
         lotNames.setVisible(false);
      }
      if (subLotNames.getText() != null) {
         subLotNamesLabel.setVisible(true);
         subLotNames.setVisible(true);
      } else {
         subLotNamesLabel.setVisible(false);
         subLotNames.setVisible(false);
      }
      if (phaseNames.getText() != null) {
         phaseNamesLabel.setVisible(true);
         phaseNames.setVisible(true);
      } else {
         phaseNamesLabel.setVisible(false);
         phaseNames.setVisible(false);
      }
      if (iterationNames.getText() != null) {
         iterationNamesLabel.setVisible(true);
         iterationNames.setVisible(true);
      } else {
         iterationNamesLabel.setVisible(false);
         iterationNames.setVisible(false);
      }

   }

   /**
    * Sets the popup's position relative to the browser's client area and Show
    *
    * @param left
    *           the left position, in pixels
    * @param top
    *           the top position, in pixels
    */
   public void showWidget(int left, int top) {
      setPopupPosition(left, top);
      show();
   }

   /**
    * hide tooltip
    */
   public void closeWidget() {
      hide();
   }

   interface ChargePlanTooltipUiBinder extends UiBinder<Widget, ChargePlanTooltip>
   {
   }
}
