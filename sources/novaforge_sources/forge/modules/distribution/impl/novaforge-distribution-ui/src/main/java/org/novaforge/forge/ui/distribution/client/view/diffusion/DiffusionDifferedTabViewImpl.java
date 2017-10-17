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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.commons.client.validation.TextBoxBaseValidation;
import org.novaforge.forge.ui.commons.client.validation.TextBoxValidation;
import org.novaforge.forge.ui.distribution.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.distribution.client.properties.DiffusionMessage;
import org.novaforge.forge.ui.distribution.client.resource.DistributionResources;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchonizationResultDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;

/**
 * @author rols-p
 */
public class DiffusionDifferedTabViewImpl extends Composite implements DiffusionDifferedTabView
{

   private static DistributionResources                 ressources        = GWT.create(DistributionResources.class);
   private static DiffusionDifferedTabViewImpllUiBinder uiBinder = GWT.create(DiffusionDifferedTabViewImpllUiBinder.class);
   private final DiffusionMessage                       diffusionMessages = (DiffusionMessage) GWT
         .create(DiffusionMessage.class);
   private final ValidateDialogBox propagatedDialogBox;
   @UiField
   Label                                             syncDifferedReferentielTitle;

   @UiField
   Label                                             targetForgesTitle;

   @UiField
   Label                                             diffusionTitle;

   @UiField(provided = true)
   CellTable<TargetForgeDTO>                         targetForgesCellTable;

   @UiField(provided = true)
   SimplePager                                       targetForgesPager;

   @UiField
   Grid                                              syncDifferedGrid;

   @UiField
   Button                                            saveButton;
   @UiField
   Label                                             activeLabel;
   @UiField
   CheckBox                                          activeCheck;

   @UiField
   TextBoxValidation                                 timeValidator;
   @UiField
   Label                                             timeLabel;
   @UiField
   TextBoxValidation                                 periodValidator;
   @UiField
   Label                                             periodLabel;

   private ListDataProvider<TargetForgeDTO>          dataTargetProvider;
   private Column<TargetForgeDTO, String>            labelColumn;
   private Column<TargetForgeDTO, String>            descriptionColumn;

   private ListDataProvider<SynchonizationResultDTO> dataResultProvider;

   private ListDataProvider<SynchonizationResultDTO> dataResultTemplateProvider;
   /**
    *
    */
   public DiffusionDifferedTabViewImpl()
   {
      ressources.css().ensureInjected();
      initTargetForgesTable();
      // Initialization of validation popup
      propagatedDialogBox = new ValidateDialogBox(diffusionMessages.propagationValidationMessage(), true);

      initWidget(uiBinder.createAndBindUi(this));
      syncDifferedReferentielTitle.setText(diffusionMessages.syncReferentielTitle());
      activeLabel.setText(diffusionMessages.activeLabel());
      activeCheck.setFormValue(diffusionMessages.activeLabel());
      activeCheck.setValue(false);

      timeLabel.setText(diffusionMessages.timeLabel());
      timeValidator.setVisible(true);
      timeValidator.setEnable(false);

      periodLabel.setText(diffusionMessages.periodeLabel());
      periodValidator.setVisible(true);
      periodValidator.setEnable(false);

      diffusionTitle.setText(diffusionMessages.diffusionTitle());
      targetForgesTitle.setText(diffusionMessages.targetForges());
      saveButton.setText(diffusionMessages.saveLabel());

      // Initialization of group detail form
      activeCheck.ensureDebugId("activeCheck");
      timeValidator.ensureDebugId("timeValidator");
      periodValidator.ensureDebugId("periodValidator");
      saveButton.ensureDebugId("parameterButton");

      // Initialization row style
      for (int row = 0; row < this.syncDifferedGrid.getRowCount(); row++)
      {
         if ((row % 2) == 0)
         {
            this.syncDifferedGrid.getRowFormatter().addStyleName(row, ressources.css().gridRowPair());
         }
         this.syncDifferedGrid.getCellFormatter().addStyleName(row, 0, ressources.css().labelCell());
      }
   }

   private void initTargetForgesTable()
   {
      targetForgesCellTable = new CellTable<TargetForgeDTO>(10, (Resources) GWT.create(TableResources.class),
            CellKey.TARGET_FORGE_KEY_PROVIDER);
      targetForgesCellTable.setWidth("100%", false);
      // Init empty widget
      Label emptyUsersLabel = new Label(diffusionMessages.emptyTargetForgesMessage());
      emptyUsersLabel.setStyleName(ressources.css().emptyLabel());
      targetForgesCellTable.setEmptyTableWidget(emptyUsersLabel);

      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      targetForgesPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      targetForgesPager.setDisplay(targetForgesCellTable);
      // Initialize the columns.
      initTargetForgesTableColumns();

      // Add the CellTable to the adapter
      dataTargetProvider = new ListDataProvider<TargetForgeDTO>();

      dataTargetProvider.addDataDisplay(targetForgesCellTable);
      // Add the CellTable to the adapter
      ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(targetForgesCellTable);
      targetForgesCellTable.addColumnSortHandler(columnSortHandler);
   }

   /**
    * Add the columns to the table.
    */
   private void initTargetForgesTableColumns()
   {
      // LabelColumn
      labelColumn = new Column<TargetForgeDTO, String>(new TextCell())
            {
         @Override
         public String getValue(TargetForgeDTO object)
         {
            return object.getLabel();
         }
            };
            labelColumn.setSortable(false); // colone triable
            targetForgesCellTable.addColumn(labelColumn, diffusionMessages.targetForgeLabel());

            // descriptionName Column
            descriptionColumn = new Column<TargetForgeDTO, String>(new TextCell())
                  {
               @Override
               public String getValue(TargetForgeDTO object)
               {
                  return object.getDescription();
               }
                  };
                  descriptionColumn.setSortable(false);
                  targetForgesCellTable.addColumn(descriptionColumn, diffusionMessages.targetForgeDescription());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public CellTable<TargetForgeDTO> getTargetForges()
   {
      return targetForgesCellTable;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ListDataProvider<TargetForgeDTO> getTargetForgesDataProvider()
   {
      return dataTargetProvider;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getTargetForgesTitle()
   {
      return targetForgesTitle;
   }

   public CheckBox getActiveCheckButton()
   {
      return activeCheck;
   }

   public TextBoxBaseValidation getTime()
   {
      return timeValidator;
   }

   public TextBoxBaseValidation getPeriod()
   {
      return periodValidator;
   }

   /**
    * {@inheritDoc}
    */
   public Button getSaveButton()
   {
      return saveButton;
   }

   @Override
   public ValidateDialogBox getPropagatedDialogBox()
   {
      return propagatedDialogBox;
   }

   @Override
   public void disableAllField()
   {
      activeCheck.setEnabled(false);
      timeValidator.setEnable(false);
      periodValidator.setEnable(false);
      saveButton.setEnabled(false);
   }

   @Override
   public void enableAllField()
   {
      activeCheck.setEnabled(true);
      timeValidator.setEnable(false);
      periodValidator.setEnable(false);
      saveButton.setEnabled(true);
   }

   /**
    * {@inheritDoc}
    */
   public void disableDiffusion()
   {
      saveButton.setEnabled(false);
   }

   /**
    * {@inheritDoc}
    */
   public void enableDiffusion()
   {
      saveButton.setEnabled(true);
   }

   /**
    * {@inheritDoc}
    */
   public ListDataProvider<SynchonizationResultDTO> getResultDataProvider()
   {
      return dataResultProvider;
   }

   /**
    * {@inheritDoc}
    */
   public ListDataProvider<SynchonizationResultDTO> getTemplateResultDataProvider()
   {
      return dataResultTemplateProvider;
   }

   interface DiffusionDifferedTabViewImpllUiBinder extends UiBinder<Widget, DiffusionDifferedTabViewImpl>
   {
   }

}
