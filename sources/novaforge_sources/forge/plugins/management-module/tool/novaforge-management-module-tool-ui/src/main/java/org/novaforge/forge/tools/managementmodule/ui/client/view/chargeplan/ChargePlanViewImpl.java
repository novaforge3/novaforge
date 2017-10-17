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

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TextInputCustomCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanLineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanMainDataDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanToolTipDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FALSQUELLE-E
 */
public class ChargePlanViewImpl extends Composite implements ChargePlanView {

   private static ChargePlanViewImplUiBinder uiBinder = GWT
         .create(ChargePlanViewImplUiBinder.class);
   final Map<Column<ChargePlanLineDTO, String>, Date> dateByColumn         = new HashMap<Column<ChargePlanLineDTO, String>, Date>();
   final InfoDialogBox                                popupWrongValue      = new InfoDialogBox(Common.getGlobal()
                                                                                                     .messageWrongInputValue(),
                                                                                               InfoTypeEnum.KO);
   final InfoDialogBox                                popupChargePlanSaved = new InfoDialogBox(Common.getGlobal()
                                                                                                     .messageSaveDone(),
                                                                                               InfoTypeEnum.OK);
   final ValidateDialogBox                            validateDialogBox    = new ValidateDialogBox(Common
                                                                                                       .getChargePlanMessages()
                                                                                                       .chargePlanSaveValidationMessage());
   final ChargePlanTooltip                            tooltip              = new ChargePlanTooltip();
   @UiField
   Button buttonSave;
   @UiField
   Button buttonShowDiagram;
   @UiField(provided = true)
   CellTable<ChargePlanLineDTO> chargePlanLeftCellTable;
   @UiField(provided = true)
   CellTable<ChargePlanLineDTO> chargePlanRightCellTable;
   @UiField
   VerticalPanel mainPanel;
   private ChargePlanMainDataDTO chargePlanMainDataDTO;
   private ListDataProvider<ChargePlanLineDTO> chargePlanLeftDataProvider;
   private ListDataProvider<ChargePlanLineDTO> chargePlanRightDataProvider;
   private boolean initiated = false;
   
   public ChargePlanViewImpl() {
      Common.getResource().css().ensureInjected();

      chargePlanLeftCellTable = new CellTable<ChargePlanLineDTO>(20,
            Common.CHANGE_PLAN_TABLE_RESOURCES);
      chargePlanRightCellTable = new CellTable<ChargePlanLineDTO>(20,
            Common.CHANGE_PLAN_TABLE_RESOURCES);

      chargePlanLeftDataProvider = new ListDataProvider<ChargePlanLineDTO>();
      chargePlanRightDataProvider = new ListDataProvider<ChargePlanLineDTO>();

      // Add the CellTables to the adapters
      chargePlanLeftDataProvider.addDataDisplay(chargePlanLeftCellTable);
      chargePlanRightDataProvider.addDataDisplay(chargePlanRightCellTable);

      initWidget(uiBinder.createAndBindUi(this));

      // chargePlanTitle.setText(Common.getProjectPlanMessages().chargePlanTitle());
      buttonSave.setText(Common.getGlobal().buttonSave());
      buttonShowDiagram.setText(Common.getGlobal().buttonShowDiagram());
   }

   @Override
   public Button getButtonSave()
   {
      return buttonSave;
   }

   @Override
   public Button getButtonShowDiagram()
   {
      return buttonShowDiagram;
   }

   @Override
   public ChargePlanMainDataDTO getChargePlanMainDataDTO()
   {
      return this.chargePlanMainDataDTO;
   }

   @Override
   public void setChargePlanMainDataDTO(ChargePlanMainDataDTO chargePlanMainDataDTO, boolean isReadonly)
   {
      this.chargePlanMainDataDTO = chargePlanMainDataDTO;

      if (!initiated)
      {
         initChargePlanLeftCellTable();
         initChargePlanRightCellTable( isReadonly );
         initiated = true;
      }

      chargePlanLeftDataProvider.setList(chargePlanMainDataDTO.getListLines());
      chargePlanRightDataProvider.setList(chargePlanMainDataDTO.getListLines());
   }

   /**
    * initialize the left part of the ChargePlan Table
    */
   public void initChargePlanLeftCellTable() {

      TextHeader header = new TextHeader("");
      // No header.
      chargePlanLeftCellTable.redraw();
      // Name column.
      chargePlanLeftCellTable.addColumn(new TextColumn<ChargePlanLineDTO>() {
         @Override
         public String getValue(ChargePlanLineDTO object) {
            return object.getDisciplineName();
         }
      }, header);
      // chargePlanLeftCellTable.redraw();

      // Total column.
      chargePlanLeftCellTable.addColumn(new TextColumn<ChargePlanLineDTO>() {
         @Override
         public String getValue(ChargePlanLineDTO object) {
            if (object.getIdDiscipline().equalsIgnoreCase(Constants.CHARGE_PLAN_FIRST_LINE_ID)) {
               return Common.MESSAGES_CHARGE_PLAN.headerColumnTotalLoad();
            } else {
               float val = object.getTotalLoad();
               val = val * 100;
               return "" + Math.round(val)/100f;
            }
         }
      }, header);

      // Verified column.
      chargePlanLeftCellTable.addColumn(new TextColumn<ChargePlanLineDTO>() {
         @Override
         public String getValue(ChargePlanLineDTO object) {
            if (object.getIdDiscipline().equalsIgnoreCase(Constants.CHARGE_PLAN_FIRST_LINE_ID)) {
               return Common.MESSAGES_CHARGE_PLAN.headerColumnVerifiedLoad();
            } else {
               float val = object.getVerifiedLoad();
               val = val * 100;
               return "" + Math.round(val)/100f;
            }
         }
      }, header);

      // Remaining column.
      chargePlanLeftCellTable.addColumn(new TextColumn<ChargePlanLineDTO>() {
         @Override
         public String getValue(ChargePlanLineDTO object) {
            if (object.getIdDiscipline().equalsIgnoreCase(Constants.CHARGE_PLAN_FIRST_LINE_ID)) {
               return Common.MESSAGES_CHARGE_PLAN.headerColumnRemainingLoad();
            } else {
               float val = object.getRemainnigLoad();
               val = val * 100;
               return "" + Math.round(val)/100f;
            }
         }
      }, header);

      chargePlanLeftCellTable.redraw();
   }

   /**
    * initialize the Right part of the ChargePlan Table
    * @param isReadonly true if table has to be shown in readonly mode
    */
   public void initChargePlanRightCellTable( boolean isReadonly) {
     
      // No header.
      final TextHeader header = new TextHeader("");

      for (final Map.Entry<Date, ChargePlanToolTipDTO> entry : chargePlanMainDataDTO
            .getTooltipByDate().entrySet()) {
         final Date date = entry.getKey();

         final TextInputCustomCell valueCell = new TextInputCustomCell(isReadonly, 6, 4, null);         
         
         Column<ChargePlanLineDTO, String> column = new Column<ChargePlanLineDTO, String>(valueCell) {
            @Override
            public String getValue(final ChargePlanLineDTO object) {
               if (object.getIdDiscipline().equalsIgnoreCase(Constants.CHARGE_PLAN_FIRST_LINE_ID)) {
                  return Common.FR_DATE_FORMAT_ONLY_DAY_SHORT.format(date);
               } else if (object.getLoadsByDate().get(date) != null) {
                  float val = object.getLoadsByDate().get(date);
                  val = val * 100;
                  return "" + Math.round(val)/100f;
               } else {
                  return "" + 0;
               }
            }
         };
         column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
         column.setSortable(false);

         column.setFieldUpdater(new FieldUpdater<ChargePlanLineDTO, String>() {

            @Override
            public void update(int index, ChargePlanLineDTO object, String value) {
               Float newValue = null;
               if (value == null || value.equalsIgnoreCase(Common.EMPTY_TEXT)) {
                  newValue = 0f;
               } else {
                  try {
                     newValue = Float.parseFloat(value);
                  } catch (NumberFormatException nfe) {
                     showPopupWrongValue();
                  }
               }

               if (newValue != null) {

                  ChargePlanLineDTO totalLine = ChargePlanViewImpl.this.chargePlanMainDataDTO
                        .getListLines()
                        .get(ChargePlanViewImpl.this.chargePlanMainDataDTO.getListLines().size() - 1);

                  // removing all old values
                  if (object.getLoadsByDate().get(date) != null) {

                     // Update totalLine, verified column
                     totalLine.setVerifiedLoad(totalLine.getVerifiedLoad()
                           - object.getVerifiedLoad());
                     // Update Verified Column
                     object.setVerifiedLoad(object.getVerifiedLoad()
                           - object.getLoadsByDate().get(date));
                     // Update totalLine, total for this column
                     totalLine.getLoadsByDate()
                           .put(date,
                                 totalLine.getLoadsByDate().get(date)
                                       - object.getLoadsByDate().get(date));
                  } else {
							// Update totalLine, verified column
                     totalLine.setVerifiedLoad(totalLine.getVerifiedLoad()
                           - object.getVerifiedLoad());

                     if (totalLine.getLoadsByDate().get(date) == null) {
                        totalLine.getLoadsByDate().put(date, 0.0f);
                     }
                  }
                  // Updating with newValue
                  object.getLoadsByDate().put(date, newValue);

                  object.setVerifiedLoad(object.getVerifiedLoad() + newValue);
                  object.setRemainingLoad(object.getTotalLoad() - object.getVerifiedLoad());

                  totalLine.setVerifiedLoad(totalLine.getVerifiedLoad() + object.getVerifiedLoad());
                  totalLine.setRemainingLoad(totalLine.getTotalLoad() - totalLine.getVerifiedLoad());
                  totalLine.getLoadsByDate().put(date,
                        totalLine.getLoadsByDate().get(date) + newValue);

                  chargePlanLeftCellTable.redraw();
                  chargePlanRightCellTable.redraw();
               }
            }
         });

         dateByColumn.put(column, date);

         chargePlanRightCellTable.addCellPreviewHandler(new Handler<ChargePlanLineDTO>() {

            @Override
            public void onCellPreview(CellPreviewEvent<ChargePlanLineDTO> event) {
               if ("mouseover".equals(event.getNativeEvent().getType())) {
                  tooltip.initFields(chargePlanMainDataDTO.getTooltipByDate().get(
                        dateByColumn.get(chargePlanRightCellTable.getColumn(event.getColumn()))));
                  tooltip.showWidget(event.getNativeEvent().getClientX() + 5, 50);
               } else if ("mouseout".equals(event.getNativeEvent().getType())) {
                  tooltip.hide();
               }
            }
         });

         chargePlanRightCellTable.addColumn(column, header);
      }

      chargePlanRightCellTable.redraw();

   }
   
   /**
    * Show a popup when wrong value was enter in a Cell
    */
   private void showPopupWrongValue() {
      popupWrongValue.getDialogPanel().center();
      popupWrongValue.getDialogPanel().show();
   }

   @Override
   public void showChargePlanSaved() {
      popupChargePlanSaved.getDialogPanel().center();
      popupChargePlanSaved.getDialogPanel().show();
   }

   @Override
   public VerticalPanel getMainPanel()
   {
      return mainPanel;
   }

   @Override
   public ValidateDialogBox getChargePlanSaveValidateDialogBox(){
      return validateDialogBox;
   }

   interface ChargePlanViewImplUiBinder extends UiBinder<Widget, ChargePlanViewImpl>
   {
   }
}