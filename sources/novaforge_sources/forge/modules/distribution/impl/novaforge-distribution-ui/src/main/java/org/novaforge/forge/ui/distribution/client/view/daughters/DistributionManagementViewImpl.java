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
/**
 * 
 */
package org.novaforge.forge.ui.distribution.client.view.daughters;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.distribution.client.resource.TableResources;
import org.novaforge.forge.ui.distribution.client.view.commons.dialogbox.ForgeValidateDialogBox;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeRequestDTO;

import java.util.Comparator;

/**
 * @author BILET-JC
 */
public class DistributionManagementViewImpl extends Composite implements DistributionManagementView
{

   private static DistributionManagementViewImplUiBinder uiBinder = GWT.create(DistributionManagementViewImplUiBinder.class);
   private final ForgeValidateDialogBox validateDialogBox;
   @UiField
   Label                                            distributionListTitle;
   @UiField
   Label                                            distributionRequestListTitle;
   @UiField
   Label                                            noDaughterLabel;
   @UiField(provided = true)
   CellTable<ForgeDTO>                              distributionCT;
   @UiField(provided = true)
   CellTable<ForgeRequestDTO>                       distributionRequestCT;
   @UiField(provided = true)
   SimplePager                                      distributionRequestPager;
   @UiField
   VerticalPanel                                    formPanel;
   @UiField
   VerticalPanel                                    noDaughterPanel;
   @UiField
   VerticalPanel                                    listPanel;
   private Column<ForgeDTO, SafeHtml>               labelColumn;
   private Column<ForgeDTO, String>                 levelColumn;
   private Column<ForgeDTO, String>                 dateColumn;
   private Column<ForgeDTO, SafeHtml>               urlColumn;
   private Column<ForgeRequestDTO, String>          nameSubscriptionColumn;
   private Column<ForgeRequestDTO, String>          reasonColumn;
   private Column<ForgeRequestDTO, String>          dateDemandColumn;
   private Column<ForgeRequestDTO, String>          typeColumn;
   private Column<ForgeRequestDTO, ForgeRequestDTO> validationColumn;
   private ListDataProvider<ForgeDTO>               distributionProvider;
   private ListDataProvider<ForgeRequestDTO>        distributionRequestProvider;
   public DistributionManagementViewImpl()
   {

      Common.RESOURCE.css().ensureInjected();
      this.initDistributionTable();
      this.initSubscriptionTable();

      this.initWidget(uiBinder.createAndBindUi(this));

      this.distributionListTitle.setText(Common.MESSAGES.forgeAffiliationListTitle());
      this.distributionRequestListTitle.setText(Common.MESSAGES.forgeAffiliationDemandListTitle());

      // Initialization of validation popup
      this.validateDialogBox = new ForgeValidateDialogBox(Common.MESSAGES.addValidationMessage());
   }

   private void initDistributionTable()
   {
      this.distributionCT = new CellTable<ForgeDTO>(Common.PAGE_SIZE, (Resources) GWT.create(TableResources.class),
                                                    CellKey.FORGE_AFFILIATION_KEY_PROVIDER);
      this.distributionCT.setWidth("100%", false);

      // Init empty widget
      final Label emptyForgeAffiliationLabel = new Label(Common.MESSAGES.emptyForgeAffiliationMessage());
      emptyForgeAffiliationLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
      this.distributionCT.setEmptyTableWidget(emptyForgeAffiliationLabel);

      // Initialize the columns.
      this.initDistributionTableColumns();

      // Add the CellTable to the adapter
      this.distributionProvider = new ListDataProvider<ForgeDTO>();
      this.distributionProvider.addDataDisplay(this.distributionCT);

      // Add the CellTable to the adapter
      final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(this.distributionCT);
      this.distributionCT.addColumnSortHandler(columnSortHandler);

   }

   private void initSubscriptionTable()
   {
      this.distributionRequestCT = new CellTable<ForgeRequestDTO>(Common.PAGE_SIZE,
            (Resources) GWT.create(TableResources.class), CellKey.FORGE_AFFILIATION_DEMAND_KEY_PROVIDER);
      this.distributionRequestCT.setWidth("100%", false);

      // Init empty widget
      final Label emptyForgeAffiliationDemandLabel = new Label(
            Common.MESSAGES.emptyForgeAffiliationDemandMessage());
      emptyForgeAffiliationDemandLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
      this.distributionRequestCT.setEmptyTableWidget(emptyForgeAffiliationDemandLabel);

      // Create a Pager to control the CellTable
      final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      this.distributionRequestPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      this.distributionRequestPager.setDisplay(this.distributionRequestCT);

      // Initialize the columns.
      this.initSubscriptionTableColumns();

      // Add the CellTable to the adapter
      this.distributionRequestProvider = new ListDataProvider<ForgeRequestDTO>();
      this.distributionRequestProvider.addDataDisplay(this.distributionRequestCT);

      // Add the CellTable to the adapter
      final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
            this.distributionRequestCT);
      this.distributionRequestCT.addColumnSortHandler(columnSortHandler);
   }

   private void initDistributionTableColumns()
   {
      // Label Column
      this.labelColumn = new Column<ForgeDTO, SafeHtml>(new SafeHtmlCell())
      {
         @Override
         public SafeHtml getValue(final ForgeDTO object)
         {
            final SafeHtmlBuilder retSHB = new SafeHtmlBuilder();
            if (object.getParent().getParent() != null)
            {
               retSHB.appendHtmlConstant("<span style='margin-left:10px;color:#696969;'>").appendEscaped(object
                                                                                                             .getLabel())
                     .appendHtmlConstant("</span>");
            }
            else
            {
               retSHB.appendEscaped(object.getLabel());
            }
            return retSHB.toSafeHtml();
         }
      };
      this.distributionCT.setColumnWidth(this.labelColumn, 100, Unit.PX);
      this.distributionCT.addColumn(this.labelColumn, Common.MESSAGES.nameColumn());

      // Level Column
      this.levelColumn = new Column<ForgeDTO, String>(new TextCell())
      {
         @Override
         public String getValue(final ForgeDTO object)
         {
            return Common.getLevelLabel(object.getForgeLevel());
         }
      };
      this.distributionCT.setColumnWidth(this.levelColumn, 100, Unit.PX);
      this.distributionCT.addColumn(this.levelColumn, Common.MESSAGES.levelColumn());

      // Date Column
      this.dateColumn = new Column<ForgeDTO, String>(new TextCell())
      {
         @Override
         public String getValue(final ForgeDTO object)
         {
            if (object.getAffiliationDate() != null)
            {
               return Common.DATE_FORMAT.format(object.getAffiliationDate());
            }
            else
            {
               return Common.MESSAGES.eDateDisplay();
            }
         }
      };
      this.distributionCT.setColumnWidth(this.dateColumn, 100, Unit.PX);
      this.distributionCT.addColumn(this.dateColumn, Common.MESSAGES.dateColumn());

      // url Column
      this.urlColumn = new Column<ForgeDTO, SafeHtml>(new SafeHtmlCell())
      {
         @Override
         public SafeHtml getValue(final ForgeDTO object)
         {
            final SafeHtmlBuilder retSHB = new SafeHtmlBuilder();
            retSHB.appendHtmlConstant("<a href='" + object.getUrl() + "' alt='lien administration' target='_blank'>"
                                          + object.getUrl() + "</a>");
            return retSHB.toSafeHtml();
         }
      };
      this.distributionCT.setColumnWidth(this.urlColumn, 100, Unit.PX);
      this.distributionCT.addColumn(this.urlColumn, Common.MESSAGES.linkColumn());
   }

   private void initSubscriptionTableColumns()
   {
      // Name Column
      this.nameSubscriptionColumn = new Column<ForgeRequestDTO, String>(new TextCell())
      {
         @Override
         public String getValue(final ForgeRequestDTO object)
         {
            return object.getSourceForgeLabel();
         }
      };
      this.nameSubscriptionColumn.setSortable(true);
      this.distributionRequestCT.setColumnWidth(this.nameSubscriptionColumn, 100, Unit.PX);
      this.distributionRequestCT.addColumn(this.nameSubscriptionColumn, Common.MESSAGES.nameColumn());

      // Type Column
      this.typeColumn = new Column<ForgeRequestDTO, String>(new TextCell())
      {
         @Override
         public String getValue(final ForgeRequestDTO object)
         {
            return object.getType().getLabel();
         }
      };
      this.typeColumn.setSortable(true);
      this.distributionRequestCT.setColumnWidth(this.typeColumn, 100, Unit.PX);
      this.distributionRequestCT.addColumn(this.typeColumn, Common.MESSAGES.typeColumn());

      // Reason Column
      this.reasonColumn = new Column<ForgeRequestDTO, String>(new TextCell())
      {
         @Override
         public String getValue(final ForgeRequestDTO object)
         {
            return object.getReason();
         }
      };
      this.reasonColumn.setSortable(true);
      this.distributionRequestCT.setColumnWidth(this.reasonColumn, 100, Unit.PX);
      this.distributionRequestCT.addColumn(this.reasonColumn, Common.MESSAGES.reasonColumn());

      // Date Column
      this.dateDemandColumn = new Column<ForgeRequestDTO, String>(new TextCell())
      {
         @Override
         public String getValue(final ForgeRequestDTO object)
         {
            return Common.DATE_FORMAT.format(object.getDate());
         }
      };
      this.dateDemandColumn.setSortable(true);
      this.distributionRequestCT.addColumn(this.dateDemandColumn, Common.MESSAGES.dateDemandColumn());
      this.distributionRequestCT.setColumnWidth(this.dateDemandColumn, 100, Unit.PX);

      // Validation column
      final SafeHtmlBuilder validationSHB = new SafeHtmlBuilder();
      validationSHB.appendHtmlConstant("<img src='" + Common.RESOURCE.ok().getURL() + "' width='"
                                           + Common.IMG_BUTTON_SIZE.toString() + "px' height='" + Common.IMG_BUTTON_SIZE
                                                                                                      .toString()
                                           + "px' />");
      final Cell<ForgeRequestDTO> validationCell = new ActionCell<ForgeRequestDTO>(
            validationSHB.toSafeHtml(), new ActionCell.Delegate<ForgeRequestDTO>()
            {
               @Override
               public void execute(final ForgeRequestDTO pObject)
               {
                  DistributionManagementViewImpl.this.getFormZone().clear();
                  DistributionManagementViewImpl.this.ensureDebugId("validationForgeAffiliationDemandId-"
                        + pObject.getSourceForgeLabel());

                  DistributionManagementViewImpl.this.validateDialogBox.setType(pObject.getType());
                  DistributionManagementViewImpl.this.validateDialogBox.setObjectId(pObject
                        .getForgeRequestId());
                  DistributionManagementViewImpl.this.validateDialogBox.show();
               }
            });
      this.validationColumn = new Column<ForgeRequestDTO, ForgeRequestDTO>(validationCell)
      {
         @Override
         public ForgeRequestDTO getValue(final ForgeRequestDTO object)
         {
            return object;
         }
      };
      this.distributionRequestCT.addColumn(this.validationColumn, Common.MESSAGES.validationColumn());
      this.distributionRequestCT.setColumnWidth(this.validationColumn, 100, Unit.PX);

   }

   @Override
   public ListDataProvider<ForgeDTO> getDistributionProvider()
   {
      return this.distributionProvider;
   }

   @Override
   public ListDataProvider<ForgeRequestDTO> getSubscriptionProvider()
   {
      return this.distributionRequestProvider;
   }

   @Override
   public void updateDistributionSortHandler()
   {
      // Add the Sort Handler to the CellTable
      final ListHandler<ForgeDTO> sortHandler = new ListHandler<ForgeDTO>(this.distributionProvider.getList());
      this.distributionCT.addColumnSortHandler(sortHandler);

      // label sort
      sortHandler.setComparator(this.labelColumn, new Comparator<ForgeDTO>()
      {
         @Override
         public int compare(final ForgeDTO o1, final ForgeDTO o2)
         {
            return o1.getLabel().compareTo(o2.getLabel());
         }
      });
      // level sort
      sortHandler.setComparator(this.levelColumn, new Comparator<ForgeDTO>()
      {
         @Override
         public int compare(final ForgeDTO o1, final ForgeDTO o2)
         {
            return Common.getLevelLabel(o1.getForgeLevel()).compareTo(
                  Common.getLevelLabel(o2.getForgeLevel()));
         }
      });
      // date sort
      sortHandler.setComparator(this.dateColumn, new Comparator<ForgeDTO>()
      {
         @Override
         public int compare(final ForgeDTO o1, final ForgeDTO o2)
         {
            return o1.getAffiliationDate().toString().compareTo(o2.getAffiliationDate().toString());
         }
      });
      // link sort
      sortHandler.setComparator(this.urlColumn, new Comparator<ForgeDTO>()
      {
         @Override
         public int compare(final ForgeDTO o1, final ForgeDTO o2)
         {
            return o1.getUrl().compareTo(o2.getUrl());
         }
      });

   }

   @Override
   public void updateSubscriptionSortHandler()
   {

      // Add the Sort Handler to the CellTable
      final ListHandler<ForgeRequestDTO> sortHandler = new ListHandler<ForgeRequestDTO>(
            this.distributionRequestProvider.getList());
      this.distributionRequestCT.addColumnSortHandler(sortHandler);

      // name sort
      sortHandler.setComparator(this.nameSubscriptionColumn, new Comparator<ForgeRequestDTO>()
      {
         @Override
         public int compare(final ForgeRequestDTO o1, final ForgeRequestDTO o2)
         {
            return o1.getSourceForgeLabel().compareTo(o2.getSourceForgeLabel());
         }
      });
      // type sort
      sortHandler.setComparator(this.typeColumn, new Comparator<ForgeRequestDTO>()
      {
         @Override
         public int compare(final ForgeRequestDTO o1, final ForgeRequestDTO o2)
         {
            return o1.getType().toString().compareTo(o2.getType().toString());
         }
      });
      // reason sort
      sortHandler.setComparator(this.reasonColumn, new Comparator<ForgeRequestDTO>()
      {
         @Override
         public int compare(final ForgeRequestDTO o1, final ForgeRequestDTO o2)
         {
            return o1.getReason().compareTo(o2.getReason());
         }
      });
      // date sort
      sortHandler.setComparator(this.dateDemandColumn, new Comparator<ForgeRequestDTO>()
      {
         @Override
         public int compare(final ForgeRequestDTO o1, final ForgeRequestDTO o2)
         {
            return o1.getDate().toString().compareTo(o2.getDate().toString());
         }
      });
   }

   @Override
   public HasWidgets getFormZone()
   {
      return this.formPanel;
   }

   @Override
   public ForgeValidateDialogBox getValidateDialogBox()
   {
      return this.validateDialogBox;
   }

   @Override
   public void createInvalidationColumn(final Column<ForgeRequestDTO, ForgeRequestDTO> pInvalidationColumn)
   {
      this.distributionRequestCT.addColumn(pInvalidationColumn, Common.MESSAGES.invalidationColumn());
      this.distributionRequestCT.setColumnWidth(pInvalidationColumn, 100, Unit.PX);
   }

   @Override
   public Column<ForgeRequestDTO, String> getNameSubscriptionColumn()
   {
      return this.nameSubscriptionColumn;
   }

   @Override
   public void noDaughter(final boolean pReferenceList)
   {
      // if actual forge is local
      if (pReferenceList)
      {
         this.noDaughterLabel.setText(Common.MESSAGES.noDaughterText());
      }
      // if actual forge is not in the forge list
      else
      {

         this.noDaughterLabel.setText(Common.MESSAGES.noReferenceText());
      }
      this.listPanel.setVisible(false);
      this.formPanel.setVisible(false);
      this.noDaughterPanel.setVisible(true);
      this.noDaughterLabel.setVisible(true);

   }

   @Override
   public void daughter()
   {
      this.listPanel.setVisible(true);
      this.formPanel.setVisible(true);
      this.noDaughterLabel.setVisible(false);
      this.noDaughterPanel.setVisible(false);
   }

   @Override
   public void noDistributionPanel()
   {
      this.noDaughterLabel.setText(Common.MESSAGES.noDistributionText());
      this.listPanel.setVisible(false);
      this.formPanel.setVisible(false);
      this.noDaughterPanel.setVisible(true);
      this.noDaughterLabel.setVisible(true);

   }

   interface DistributionManagementViewImplUiBinder extends UiBinder<Widget, DistributionManagementViewImpl>
   {
   }

}
