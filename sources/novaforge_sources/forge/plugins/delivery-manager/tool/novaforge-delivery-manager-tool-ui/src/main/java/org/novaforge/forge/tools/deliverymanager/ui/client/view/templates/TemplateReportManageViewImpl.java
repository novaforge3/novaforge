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

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnCancelUploaderHandler;
import gwtupload.client.IUploader.UploaderConstants;
import gwtupload.client.SingleUploader;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldTypeDTO;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.commons.client.upload.ProgressUploadStatus;
import org.novaforge.forge.ui.commons.client.upload.Uploader;
import org.novaforge.forge.ui.commons.client.validation.TextAreaValidation;
import org.novaforge.forge.ui.commons.client.validation.TextBoxValidation;

/**
 * @author CASERY-J
 * @author Guillaume Lamirand
 */
public class TemplateReportManageViewImpl extends Composite implements TemplateReportManageView
{

   private static TemplateReportManageViewImplUiBinder uiBinder = GWT.create(TemplateReportManageViewImplUiBinder.class);
   private final ValidateDialogBox validateDialogBox;
   @UiField
   Button                                     returnButton;
   @UiField
   Label                                      noteTemplateDetailTitle;
   @UiField
   Label                                      noteTemplateInfoLabel;
   @UiField
   Grid                                       noteTemplateInfoGrid;
   @UiField
   Label                                      nameLabel;
   @UiField
   TextBoxValidation                          nameTB;
   @UiField
   Label                                      descriptionLabel;
   @UiField
   TextAreaValidation                         descriptionTB;
   @UiField
   Label                                      templateFileLabel;
   @UiField
   VerticalPanel                              uploadPanel;
   @UiField
   Label                                      fileNameLabel;
   IUploader                                  singleUploader;
   @UiField
   Label                                      noteTemplateFieldsTitle;
   @UiField
   Label                                      noteTemplateFieldsInfo;
   @UiField(provided = true)
   CellTable<TemplateFieldDTO>                fieldsCellTable;
   @UiField(provided = true)
   SimplePager                                fieldsPager;
   @UiField
   Button                                     buttonSave;

   private ListDataProvider<TemplateFieldDTO> dataFieldsProvider;
   private Column<TemplateFieldDTO, String>   nameColumn;
   private Column<TemplateFieldDTO, String>   descriptionColumn;
   private Column<TemplateFieldDTO, String>   typeColumn;
   public TemplateReportManageViewImpl()
   {
      DeliveryCommon.getResources().css().ensureInjected();
      // Init template list
      this.initDeliveryNotesTable();
      this.initWidget(uiBinder.createAndBindUi(this));

      // Init text
      this.returnButton.setText(DeliveryCommon.getMessages().returnTemplateList());
      this.noteTemplateDetailTitle.setText(DeliveryCommon.getMessages().createNoteTemplateTitle());
      this.noteTemplateInfoLabel.setText(DeliveryCommon.getMessages().createFormNoteTemplate());
      this.nameLabel.setText(DeliveryCommon.getMessages().templateName());
      this.descriptionLabel.setText(DeliveryCommon.getMessages().templateDescription());
      this.templateFileLabel.setText(DeliveryCommon.getMessages().templateFile());
      this.noteTemplateFieldsTitle.setText(DeliveryCommon.getMessages().noteTemplateFieldsTitle());
      this.noteTemplateFieldsInfo.setText(DeliveryCommon.getMessages().noteTemplateFieldsInfo());

      // Button
      this.buttonSave.setText(DeliveryCommon.getMessages().saveNoteTemplate());
      // Initialization row style
      for (int row = 0; row < this.noteTemplateInfoGrid.getRowCount(); row++)
      {
         if ((row % 2) == 0)
         {
            this.noteTemplateInfoGrid.getRowFormatter().addStyleName(row,
                  Common.getResources().css().gridRowPair());
         }
         this.noteTemplateInfoGrid.getCellFormatter().addStyleName(row, 0,
               Common.getResources().css().labelCell());
      }
      this.initUploader();
      // Initialization of validation popup
      this.validateDialogBox = new ValidateDialogBox(DeliveryCommon.getMessages().saveMessage());

   }

   /**
    * Create and initialize delivery Notes cells and table
    */
   private void initDeliveryNotesTable()
   {
      this.fieldsCellTable = new CellTable<TemplateFieldDTO>(15, (Resources) GWT.create(TableResources.class));
      this.fieldsCellTable.setWidth("60%", false);

      // Init empty widget
      final Label emptyDeliveryLabel = new Label(DeliveryCommon.getMessages().emptyFieldMessage());
      emptyDeliveryLabel.setStyleName(Common.getResources().css().emptyLabel());
      this.fieldsCellTable.setEmptyTableWidget(emptyDeliveryLabel);

      // Create a Pager to control the CellTable
      final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      this.fieldsPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      this.fieldsPager.setDisplay(this.fieldsCellTable);

      // Initialize the columns.
      this.initDeliveryNotesTableColumns();

      // Add the CellTable to the adapter
      this.dataFieldsProvider = new ListDataProvider<TemplateFieldDTO>();
      this.dataFieldsProvider.addDataDisplay(this.fieldsCellTable);

      // Add the CellTable to the adapter
      final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
            this.fieldsCellTable);
      this.fieldsCellTable.addColumnSortHandler(columnSortHandler);

   }

   private void initUploader()
   {
      this.singleUploader = new SingleUploader(new ProgressUploadStatus(), null);
      this.singleUploader.getFileInput().setSize("100%", "25px");
      this.singleUploader.setI18Constants((UploaderConstants) GWT.create(Uploader.class));
      this.singleUploader.addOnCancelUploadHandler(new OnCancelUploaderHandler()
      {
         @Override
         public void onCancel(final IUploader pUploader)
         {
            pUploader.cancel();
         }
      });

      this.uploadPanel.add((SingleUploader) this.singleUploader);
   }

   /**
    * Initialize delivery Notes table columns
    */
   private void initDeliveryNotesTableColumns()
   {
      // Name Column
      this.nameColumn = new Column<TemplateFieldDTO, String>(new TextInputCell())
            {
         @Override
         public String getValue(final TemplateFieldDTO object)
         {
            return object.getName();
         }
            };
            this.nameColumn.setFieldUpdater(new FieldUpdater<TemplateFieldDTO, String>()
                  {
               @Override
               public void update(final int index, final TemplateFieldDTO object, final String value)
               {
                  if ((!object.isNew()) && (!Common.validateStringNotNull(value)))
                  {
                     final InfoDialogBox box = new InfoDialogBox(DeliveryCommon.getMessages().emptyForm(),
                           InfoTypeEnum.WARNING);
                     box.show();
                     box.getCloseButton().addClickHandler(new ClickHandler()
                     {
                        @Override
                        public void onClick(final ClickEvent pEvent)
                        {
                           box.hide();

                           // Set back input value
                           final TableCellElement item = TemplateReportManageViewImpl.this.fieldsCellTable
                                 .getRowElement(index).getCells().getItem(0);
                           final Element div = item.getFirstChildElement();
                           final InputElement input = div.getFirstChildElement().cast();
                           input.setValue(object.getName());
                        }
                     });
                  }
                  else
                  {
                     object.setName(value);
                  }
               }
                  });
            this.nameColumn.setSortable(true);
            this.fieldsCellTable.addColumn(this.nameColumn, DeliveryCommon.getMessages().name());
            this.fieldsCellTable.setColumnWidth(this.nameColumn, 100, Unit.PX);

            // Type column
            this.typeColumn = new Column<TemplateFieldDTO, String>(new SelectionCell(
                  TemplateFieldTypeDTO.listLabel()))
                  {
               @Override
               public String getValue(final TemplateFieldDTO object)
               {
                  return object.getType().getLabel();
               }
                  };
                  this.typeColumn.setFieldUpdater(new FieldUpdater<TemplateFieldDTO, String>()
                        {
                     @Override
                     public void update(final int index, final TemplateFieldDTO object, final String value)
                     {
                        object.setType(TemplateFieldTypeDTO.getByLabel(value));
                     }
                        });
                  this.typeColumn.setSortable(true);
                  this.fieldsCellTable.addColumn(this.typeColumn, DeliveryCommon.getMessages().type());
                  this.fieldsCellTable.setColumnWidth(this.typeColumn, 40, Unit.PX);

                  // Description column
                  this.descriptionColumn = new Column<TemplateFieldDTO, String>(new TextInputCell())
                        {
                     @Override
                     public String getValue(final TemplateFieldDTO object)
                     {
                        return object.getDescription();
                     }
                        };
                        this.descriptionColumn.setFieldUpdater(new FieldUpdater<TemplateFieldDTO, String>()
                              {
                           @Override
                           public void update(final int index, final TemplateFieldDTO object, final String value)
                           {
                              if ((!object.isNew()) && (!Common.validateStringNotNull(value)))
                              {
                                 final InfoDialogBox box = new InfoDialogBox(DeliveryCommon.getMessages().emptyForm(),
                                       InfoTypeEnum.WARNING);
                                 box.show();
                                 box.getCloseButton().addClickHandler(new ClickHandler()
                                 {
                                    @Override
                                    public void onClick(final ClickEvent pEvent)
                                    {
                                       box.hide();

                                       // Set back input value
                                       final TableCellElement item = TemplateReportManageViewImpl.this.fieldsCellTable
                                             .getRowElement(index).getCells().getItem(2);
                                       final Element div = item.getFirstChildElement();
                                       final InputElement input = div.getFirstChildElement().cast();
                                       input.setValue(object.getDescription());
                                    }
                                 });
                              }
                              else
                              {
                                 object.setDescription(value);
                              }
                           }
                              });
                        this.descriptionColumn.setSortable(true);
                        this.fieldsCellTable.addColumn(this.descriptionColumn, DeliveryCommon.getMessages().description());
                        this.fieldsCellTable.setColumnWidth(this.descriptionColumn, 150, Unit.PX);

   }

   /**
    * @return the buttonSaveNoteTemplate
    */
   @Override
   public Button getButtonSave()
   {
      return this.buttonSave;
   }

   @Override
   public TextAreaValidation getDescriptionTB()
   {
      return this.descriptionTB;
   }

   @Override
   public TextBoxValidation getNameTB()
   {
      return this.nameTB;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public CellTable<TemplateFieldDTO> getFieldsTable()
   {
      return this.fieldsCellTable;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ListDataProvider<TemplateFieldDTO> getDataProvider()
   {
      return this.dataFieldsProvider;
   }

   @Override
   public Button getButtonReturn()
   {
      return this.returnButton;
   }

   @Override
   public ValidateDialogBox getValidateDialogBox()
   {
      return this.validateDialogBox;
   }

   @Override
   public IUploader getUploader()
   {
      return this.singleUploader;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getFileName()
   {
      return this.fileNameLabel;
   }

   interface TemplateReportManageViewImplUiBinder extends UiBinder<Widget, TemplateReportManageViewImpl>
   {
   }
}