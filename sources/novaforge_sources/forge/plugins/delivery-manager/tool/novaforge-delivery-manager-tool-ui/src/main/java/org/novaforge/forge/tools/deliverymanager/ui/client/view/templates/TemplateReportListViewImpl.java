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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateDTO;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;

/**
 * @author CASERY-J
 */
public class TemplateReportListViewImpl extends Composite implements TemplateReportListView
{
   private static TemplateReportListViewImplUiBinder uiBinder = GWT.create(TemplateReportListViewImplUiBinder.class);
   @UiField
   Button                                buttonManageDelivery;
   @UiField
   Button                                buttonCreateDeliveryNote;
   @UiField
   Label                                 deliveryManagementNotesTitle;
   @UiField
   Label                                 deliveryNotesTitle;
   @UiField(provided = true)
   CellTable<TemplateDTO>                deliveryNotesCellTable;
   @UiField(provided = true)
   SimplePager                           deliveryNotesPager;
   @UiField
   Label                                 deliveryNoteSampleTitle;
   @UiField
   Label                                 deliveryNoteSample;
   @UiField
   Button                                downloadNoteSampleFile;
   private ListDataProvider<TemplateDTO> dataDeliveryNotesProvider;
   private Column<TemplateDTO, String>   nameColumn;
   private Column<TemplateDTO, String>   descriptionColumn;
   public TemplateReportListViewImpl()
   {
      DeliveryCommon.getResources().css().ensureInjected();
      this.initDeliveryNotesTable();
      this.initWidget(uiBinder.createAndBindUi(this));
      this.buttonManageDelivery.setText(DeliveryCommon.getMessages().manageLivraisonButton());
      this.buttonCreateDeliveryNote.setText(DeliveryCommon.getMessages().createDeliveryNoteTemplateButton());
      this.deliveryManagementNotesTitle.setText(DeliveryCommon.getMessages().deliveryManagementNotesTitle());
      this.deliveryNotesTitle.setText(DeliveryCommon.getMessages().deliveryNotesTitle());
      this.downloadNoteSampleFile.setText(DeliveryCommon.getMessages().buttonDownloadDeliveryNoteSample());
      this.deliveryNoteSampleTitle.setText(DeliveryCommon.getMessages().deliveryNoteSampleTitle());
      this.deliveryNoteSample.setText(DeliveryCommon.getMessages().deliveryNoteSample());
   }

   /**
    * Create and initialize delivery Notes cells and table
    */
   private void initDeliveryNotesTable()
   {
      this.deliveryNotesCellTable = new CellTable<TemplateDTO>(8,
            (Resources) GWT.create(TableResources.class));
      this.deliveryNotesCellTable.setWidth("100%", false);

      // Init empty widget
      final Label emptyDeliveryLabel = new Label(DeliveryCommon.getMessages()
            .emptyDeliveryNoteTemplateMessage());
      emptyDeliveryLabel.setStyleName(Common.getResources().css().emptyLabel());
      this.deliveryNotesCellTable.setEmptyTableWidget(emptyDeliveryLabel);

      // Create a Pager to control the CellTable
      final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      this.deliveryNotesPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      this.deliveryNotesPager.setDisplay(this.deliveryNotesCellTable);

      // Initialize the columns.
      this.initDeliveryNotesTableColumns();

      // Add the CellTable to the adapter
      this.dataDeliveryNotesProvider = new ListDataProvider<TemplateDTO>();
      this.dataDeliveryNotesProvider.addDataDisplay(this.deliveryNotesCellTable);

      // Add the CellTable to the adapter
      final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
            this.deliveryNotesCellTable);
      this.deliveryNotesCellTable.addColumnSortHandler(columnSortHandler);

   }

   /**
    * Initialize delivery Notes table columns
    */
   private void initDeliveryNotesTableColumns()
   {
      // Name Column
      this.nameColumn = new Column<TemplateDTO, String>(new TextCell())
            {
         @Override
         public String getValue(final TemplateDTO object)
         {
            return object.getName();
         }
            };
            this.nameColumn.setSortable(true);
            this.deliveryNotesCellTable.addColumn(this.nameColumn, DeliveryCommon.getMessages().name());
            this.deliveryNotesCellTable.setColumnWidth(this.nameColumn, 150, Unit.PX);

            // Version column
            this.descriptionColumn = new Column<TemplateDTO, String>(new TextCell())
                  {
               @Override
               public String getValue(final TemplateDTO object)
               {
                  return object.getDescription();
               }
                  };
                  this.descriptionColumn.setSortable(true);
                  this.deliveryNotesCellTable.addColumn(this.descriptionColumn, DeliveryCommon.getMessages()
                        .description());
                  this.deliveryNotesCellTable.setColumnWidth(this.descriptionColumn, 150, Unit.PX);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public CellTable<TemplateDTO> getDeliveryTemplateNotesTable()
   {
      return this.deliveryNotesCellTable;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ListDataProvider<TemplateDTO> getDeliveryTemplateNotesDataProvider()
   {
      return this.dataDeliveryNotesProvider;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HasClickHandlers getButtonCreateDeliveryTemplateNote()
   {
      return this.buttonCreateDeliveryNote;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HasClickHandlers getButtonManageDelivery()
   {
      return this.buttonManageDelivery;
   }

   @Override
   public Button getDownloadNoteSampleFile()
   {
      return this.downloadNoteSampleFile;
   }

   @Override
   public Label getDeliveryNoteSample()
   {
      return this.deliveryNoteSample;
   }

   @Override
   public Label getDeliveryNoteSampleTitle()
   {
      return this.deliveryNoteSampleTitle;
   }

   interface TemplateReportListViewImplUiBinder extends UiBinder<Widget, TemplateReportListViewImpl>
   {
   }

}
