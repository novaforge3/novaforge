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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;
import org.novaforge.forge.ui.distribution.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.distribution.client.properties.DiffusionMessage;
import org.novaforge.forge.ui.distribution.client.resource.DistributionResources;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchonizationResultDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.ComponentToSyncEnum;

/**
 * @author rols-p
 */
public class DiffusionTabViewImpl extends Composite implements DiffusionTabView
{

   private static DistributionResources         ressources        = GWT.create(DistributionResources.class);
   private static DiffusionTabViewImpllUiBinder uiBinder = GWT.create(DiffusionTabViewImpllUiBinder.class);
   private final DiffusionMessage               diffusionMessages = (DiffusionMessage) GWT
         .create(DiffusionMessage.class);
   @UiField
   Label                                             syncReferentielTitle;
   @UiField
   Label                                             targetForgesTitle;
   @UiField
   Label                                             diffusionTitle;
   @UiField(provided = true)
   CellTable<TargetForgeDTO>                         targetForgesCellTable;
   @UiField(provided = true)
   SimplePager                                       targetForgesPager;
   @UiField
   Button                                            diffuseButton;
   @UiField
   CheckBox                                          projetRefCheck;
   @UiField
   Label                                             projetRefLabel;
   @UiField
   CheckBox                                          templateCheck;
   @UiField
   Label                                             templateLabel;
   @UiField
   CheckBox                                          toolsCheck;
   @UiField
   Label                                             toolsLabel;
   @UiField
   CheckBox                                          indicatorCheck;
   @UiField
   Label                                             indicatorLabel;
   private ListDataProvider<TargetForgeDTO>          dataTargetProvider;
   private Column<TargetForgeDTO, String>            labelColumn;
   private Column<TargetForgeDTO, String>            descriptionColumn;
   private ListDataProvider<SynchonizationResultDTO> dataResultProvider;
   private ListDataProvider<SynchonizationResultDTO> dataResultTemplateProvider;

   /**
    *
    */
   public DiffusionTabViewImpl()
   {
      ressources.css().ensureInjected();
      initTargetForgesTable();

      initWidget(uiBinder.createAndBindUi(this));
      syncReferentielTitle.setText(diffusionMessages.syncReferentielTitle());
      projetRefLabel.setText(diffusionMessages.projetRefLabel());
      projetRefCheck.setFormValue(ComponentToSyncEnum.PROJ_REF.toString());
      templateLabel.setText(diffusionMessages.templateLabel());
      templateCheck.setFormValue(ComponentToSyncEnum.TEMPLATE.toString());
      toolsLabel.setText(diffusionMessages.toolsLabel());
      toolsCheck.setFormValue(ComponentToSyncEnum.TOOLS.toString());
      indicatorLabel.setText(diffusionMessages.indicatorLabel());
      indicatorCheck.setFormValue(ComponentToSyncEnum.INDICATOR.toString());

      diffusionTitle.setText(diffusionMessages.diffusionTitle());
      targetForgesTitle.setText(diffusionMessages.targetForges());
      diffuseButton.setText(diffusionMessages.diffuseButton());
      diffuseButton.setEnabled(false);
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

   /**
    * {@inheritDoc}
    */
   @Override
   public Button getDiffuseButton()
   {
      return diffuseButton;
   }

   public CheckBox getProjetRefCheckButton()
   {
      return projetRefCheck;
   }

   public CheckBox getTemplateCheckButton()
   {
      return templateCheck;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ListDataProvider<SynchonizationResultDTO> getResultDataProvider()
   {
      return dataResultProvider;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setEnabled(boolean pBoolean)
   {
      projetRefCheck.setEnabled(pBoolean);
      templateCheck.setEnabled(pBoolean);
      toolsCheck.setEnabled(pBoolean);
      indicatorCheck.setEnabled(pBoolean);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ListDataProvider<SynchonizationResultDTO> getTemplateResultDataProvider()
   {
      return dataResultTemplateProvider;
   }

   public CheckBox getToolsCheckButton()
   {
      return toolsCheck;
   }

   public CheckBox getIndicatorCheckButton()
   {
      return indicatorCheck;
   }

   interface DiffusionTabViewImpllUiBinder extends UiBinder<Widget, DiffusionTabViewImpl>
   {
   }

}
