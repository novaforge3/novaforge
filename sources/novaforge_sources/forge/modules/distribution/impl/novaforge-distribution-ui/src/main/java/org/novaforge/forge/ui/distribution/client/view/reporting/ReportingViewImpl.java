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

package org.novaforge.forge.ui.distribution.client.view.reporting;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.distribution.client.resource.TableResources;
import org.novaforge.forge.ui.distribution.client.view.commons.IndexedColumn;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ForgeViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.OrganizationViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ProfilViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.UpdatedViewDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.StatusDataAccessEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.ViewTypeEnum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * @author BILET-JC
 *
 */
public class ReportingViewImpl extends Composite implements ReportingView
{

   private static ReportingViewImplUiBinder uiBinder = GWT.create(ReportingViewImplUiBinder.class);
   @UiField
   ListBox viewTypeLB;
   @UiField
   Label   viewTypeL;
   @UiField
   Label         viewTypeTitle;
   @UiField
   Panel                                         noDistributionPanel;
   @UiField
   Label                                         noDistributionLabel;
   @UiField(provided = true)
   Image                                         warningImage;
   @UiField
   Panel                                         selectViewPanel;
   @UiField
   Panel                                         reportingPanel;
   @UiField
   Panel                                         noReportingPanel;
   @UiField
   Label                                         noReportingLabel;
   @UiField(provided = true)
   CellTable<ForgeViewDTO>                       forgeViewCT;
   @UiField(provided = true)
   CellTable<OrganizationViewDTO>                organizationViewCT;
   @UiField(provided = true)
   CellTable<ProfilViewDTO>                      profilViewCT;
   @UiField(provided = true)
   SimplePager              pagerForge;
   @UiField(provided = true)
   SimplePager              pagerOrga;
   @UiField(provided = true)
   SimplePager              pagerProfile;
   /************************************
    * | Add updated |
    *************************************/

   @UiField                  Panel                     updatedPanel;
   @UiField(provided = true) CellTable<UpdatedViewDTO> updatedViewCT;
   private ListDataProvider<UpdatedViewDTO>      updatedDataProvider;
   private Column<UpdatedViewDTO, String>        updatedNameColumn;
   private Column<UpdatedViewDTO, String>        updatedDateColumn;
   private ListDataProvider<ForgeViewDTO>        forgeViewDataProvider;
   private ListDataProvider<OrganizationViewDTO> organizationViewDataProvider;
   private ListDataProvider<ProfilViewDTO>       profilViewDataProvider;
   private Column<ForgeViewDTO, String>          forgeViewNameColumn;
   private Column<ForgeViewDTO, String>          forgeViewProjectsColumn;
   private Column<ForgeViewDTO, String>          forgeViewAccountsColumn;
   private Column<OrganizationViewDTO, String>   organizationViewNameColumn;
   private Column<OrganizationViewDTO, String>   organizationViewProjectsColumn;
   private Column<ProfilViewDTO, String>         profilViewNameColumn;
   private Column<ProfilViewDTO, String>         profilViewProjectsColumn;

   public ReportingViewImpl()
   {
      Common.RESOURCE.css().ensureInjected();
      initTables();
      warningImage = new Image(Common.RESOURCE.warning2());
      initWidget(uiBinder.createAndBindUi(this));
      noReportingLabel.setText(Common.MESSAGES.noReportingText());
      noDistributionLabel.setText(Common.MESSAGES.noDistributionForReportingText());
      reportingPanel.setVisible(true);
      updatedPanel.setVisible(true);

      noReportingPanel.setVisible(false);
      noDistributionPanel.setVisible(false);
      viewTypeL.setText(Common.MESSAGES.viewType());
      viewTypeLB.addItem(ViewTypeEnum.FORGE.getLabel().toUpperCase(), ViewTypeEnum.FORGE.getLabel());
      viewTypeLB.addItem(ViewTypeEnum.ORGANIZATION.getLabel().toUpperCase(), ViewTypeEnum.ORGANIZATION.getLabel());
      viewTypeLB.addItem(ViewTypeEnum.PROFIL.getLabel().toUpperCase(), ViewTypeEnum.PROFIL.getLabel());
   }

   private void initTables()
   {
      initUpdatedViewTable();
      initForgeViewTable();
      initOrganizationViewTable();
      initProfilViewTable();

      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      pagerForge = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      pagerOrga = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      pagerProfile = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      pagerForge.setDisplay(forgeViewCT);
      pagerOrga.setDisplay(organizationViewCT);
      pagerProfile.setDisplay(profilViewCT);
   }

   private void initUpdatedViewTable()
   {
      updatedViewCT = new CellTable<UpdatedViewDTO>(Common.PAGE_SIZE, (Resources) GWT.create(TableResources.class),
                                                    CellKey.UPDATED_VIEW_TYPE_KEY_PROVIDER);

      updatedViewCT.setWidth("100%", false);

      // Init empty widget
      Label emptyL = new Label(Common.MESSAGES.noUpdateFound());
      emptyL.setStyleName(Common.RESOURCE.css().emptyLabel());
      updatedViewCT.setEmptyTableWidget(emptyL);

      // Initialize the columns.
      initUpdatedColumns();

      // Add the CellTable to the adapter
      updatedDataProvider = new ListDataProvider<UpdatedViewDTO>();
      updatedDataProvider.addDataDisplay(updatedViewCT);
   }

   private void initForgeViewTable()
   {
      forgeViewCT = new CellTable<ForgeViewDTO>(Common.PAGE_SIZE, (Resources) GWT.create(TableResources.class),
                                                CellKey.FORGE_VIEW_TYPE_KEY_PROVIDER);
      forgeViewCT.setWidth("100%", false);

      // Init empty widget
      Label emptyL = new Label(Common.MESSAGES.emptyForgeViewType());
      emptyL.setStyleName(Common.RESOURCE.css().emptyLabel());
      forgeViewCT.setEmptyTableWidget(emptyL);

      // Initialize the columns.
      initForgeViewColumns();

      // Add the CellTable to the adapter
      forgeViewDataProvider = new ListDataProvider<ForgeViewDTO>();
      forgeViewDataProvider.addDataDisplay(forgeViewCT);

   }

   private void initOrganizationViewTable()
   {
      organizationViewCT = new CellTable<OrganizationViewDTO>(Common.PAGE_SIZE,
                                                              (Resources) GWT.create(TableResources.class),
                                                              CellKey.ORGANIZATION_VIEW_TYPE_KEY_PROVIDER);
      organizationViewCT.setWidth("100%", false);

      // Init empty widget
      Label emptyL = new Label(Common.MESSAGES.emptyOrganizationViewType());
      emptyL.setStyleName(Common.RESOURCE.css().emptyLabel());
      organizationViewCT.setEmptyTableWidget(emptyL);

      // Initialize the columns.
      initOrganizationViewColumns();

      // Add the CellTable to the adapter
      organizationViewDataProvider = new ListDataProvider<OrganizationViewDTO>();
      organizationViewDataProvider.addDataDisplay(organizationViewCT);
   }

   private void initProfilViewTable()
   {
      profilViewCT = new CellTable<ProfilViewDTO>(Common.PAGE_SIZE, (Resources) GWT.create(TableResources.class),
                                                  CellKey.PROFIL_VIEW_TYPE_KEY_PROVIDER);
      profilViewCT.setWidth("100%", false);

      // Init empty widget
      Label emptyL = new Label(Common.MESSAGES.emptyProfilViewType());
      emptyL.setStyleName(Common.RESOURCE.css().emptyLabel());
      profilViewCT.setEmptyTableWidget(emptyL);

      // Initialize the columns.
      initProfilViewColumns();

      // Add the CellTable to the adapter
      profilViewDataProvider = new ListDataProvider<ProfilViewDTO>();
      profilViewDataProvider.addDataDisplay(profilViewCT);

   }
   
   private void initUpdatedColumns()
   {
      // Updated Forge Name column
      updatedNameColumn = new Column<UpdatedViewDTO, String>(new TextCell())
      {
         @Override
         public String getValue(UpdatedViewDTO object)
         {
            return object.getForgeName();
         }
      };
      updatedNameColumn.setSortable(true);
      updatedViewCT.addColumn(updatedNameColumn, Common.MESSAGES.updatedDistributionForgeNameColumn());

      // Date Forge column
      updatedDateColumn = new Column<UpdatedViewDTO, String>(new TextCell())
      {
         @Override
         public String getValue(UpdatedViewDTO object)
         {
            return object.getLastUpdated();
         }
      };
      updatedDateColumn.setSortable(true);
      updatedViewCT.addColumn(updatedDateColumn, Common.MESSAGES.updatedDistributionForgeDateColumn());

   }

   private void initForgeViewColumns()
   {
      // Forge Name Column
      forgeViewNameColumn = new Column<ForgeViewDTO, String>(new TextCell())
      {
         @Override
         public String getValue(ForgeViewDTO object)
         {
            return object.getForgeName();
         }
      };
      forgeViewNameColumn.setSortable(true);
      forgeViewCT.addColumn(forgeViewNameColumn, Common.MESSAGES.forgeColumn());

      // number of projects Column
      forgeViewProjectsColumn = new Column<ForgeViewDTO, String>(new TextCell())
      {
         @Override
         public String getValue(ForgeViewDTO object)
         {
            return object.getNumberProject().toString();
         }
      };
      forgeViewProjectsColumn.setSortable(true);
      forgeViewCT.addColumn(forgeViewProjectsColumn, Common.MESSAGES.nbProjectsColumn());

      // number of accounts Column
      forgeViewAccountsColumn = new Column<ForgeViewDTO, String>(new TextCell())
      {
         @Override
         public String getValue(ForgeViewDTO object)
         {
            return object.getNumberAccount().toString();
         }
      };
      forgeViewAccountsColumn.setSortable(true);
      forgeViewCT.addColumn(forgeViewAccountsColumn, Common.MESSAGES.nbAccountsColumn());

   }

   private void initOrganizationViewColumns()
   {
      // Organization Name Column
      organizationViewNameColumn = new Column<OrganizationViewDTO, String>(new TextCell())
      {
         @Override
         public String getValue(OrganizationViewDTO object)
         {
            return object.getOrganizationName();
         }
      };
      organizationViewNameColumn.setSortable(true);
      organizationViewCT.addColumn(organizationViewNameColumn, Common.MESSAGES.organizationColumn());

      // number of projects Column
      organizationViewProjectsColumn = new Column<OrganizationViewDTO, String>(new TextCell())
      {
         @Override
         public String getValue(OrganizationViewDTO object)
         {
            return object.getNumberProject().toString();
         }
      };
      organizationViewProjectsColumn.setSortable(true);
      organizationViewCT
      .addColumn(organizationViewProjectsColumn, Common.MESSAGES.nbProjectsColumn());

   }

   private void initProfilViewColumns()
   {
      // Forge Name Column
      profilViewNameColumn = new Column<ProfilViewDTO, String>(new TextCell())
      {
         @Override
         public String getValue(ProfilViewDTO object)
         {
            return object.getForgeName();
         }
      };
      profilViewNameColumn.setSortable(true);
      profilViewCT.addColumn(profilViewNameColumn, Common.MESSAGES.forgeColumn());

      // Project Name Column
      profilViewProjectsColumn = new Column<ProfilViewDTO, String>(new TextCell())
      {

         @Override
         public String getValue(ProfilViewDTO object)
         {
            return object.getProjectName();
         }
      };
      profilViewCT.addColumn(profilViewProjectsColumn, Common.MESSAGES.projectColumn());

   }

   @Override
   public ListBox getViewType()
   {
      return viewTypeLB;
   }

   @Override
   public Label getViewTypeTitle()
   {
      return viewTypeTitle;
   }

   @Override
   public void noReportingPanel()
   {
      noDistributionPanel.setVisible(false);
      reportingPanel.setVisible(false);
      selectViewPanel.setVisible(false);
      noReportingPanel.setVisible(true);
   }

   @Override
   public void noDistributionPanel()
   {
      noDistributionPanel.setVisible(true);
      reportingPanel.setVisible(true);
      selectViewPanel.setVisible(true);
      noReportingPanel.setVisible(false);

   }

   @Override
   public ListDataProvider<ForgeViewDTO> getForgeViewDataProvider()
   {
      return forgeViewDataProvider;
   }

   @Override
   public ListDataProvider<OrganizationViewDTO> getOrganizationViewDataProvider()
   {
      return organizationViewDataProvider;
   }

   @Override
   public ListDataProvider<ProfilViewDTO> getProfilViewDataProvider()
   {
      return profilViewDataProvider;
   }
   
   @Override
   public ListDataProvider<UpdatedViewDTO> getUpdatedViewDataProvider() 
   {
	   return updatedDataProvider; 
   }
   
   @Override
   public void updateForgeViewSortHandler()
   {
      // Add the Sort Handler to the CellTable
      ListHandler<ForgeViewDTO> sortHandler = new ListHandler<ForgeViewDTO>(
            forgeViewDataProvider.getList());
      forgeViewCT.addColumnSortHandler(sortHandler);

      // name sort
      sortHandler.setComparator(forgeViewNameColumn, new Comparator<ForgeViewDTO>()
            {
         @Override
         public int compare(ForgeViewDTO o1, ForgeViewDTO o2)
         {
            return o1.getForgeName().compareTo(o2.getForgeName());
         }
            });

   }

   @Override
   public void updateOrganizationViewSortHandler()
   {
      // Add the Sort Handler to the CellTable
      ListHandler<OrganizationViewDTO> sortHandler = new ListHandler<OrganizationViewDTO>(
            organizationViewDataProvider.getList());
      organizationViewCT.addColumnSortHandler(sortHandler);

      // name sort
      sortHandler.setComparator(organizationViewNameColumn, new Comparator<OrganizationViewDTO>()
            {
         @Override
         public int compare(OrganizationViewDTO o1, OrganizationViewDTO o2)
         {
            return o1.getOrganizationName().compareTo(o2.getOrganizationName());
         }
            });

      // nb account sort
      sortHandler.setComparator(organizationViewProjectsColumn, new Comparator<OrganizationViewDTO>()
            {
         @Override
         public int compare(OrganizationViewDTO o1, OrganizationViewDTO o2)
         {
            return o1.getNumberProject().compareTo(o2.getNumberProject());
         }
            });

   }

   @Override
   public void updateProfilViewSortHandler()
   {
      // Add the Sort Handler to the CellTable
      ListHandler<ProfilViewDTO> sortHandler = new ListHandler<ProfilViewDTO>(
            profilViewDataProvider.getList());
      profilViewCT.addColumnSortHandler(sortHandler);

      // name sort
      sortHandler.setComparator(profilViewNameColumn, new Comparator<ProfilViewDTO>()
            {
         @Override
         public int compare(ProfilViewDTO o1, ProfilViewDTO o2)
         {
            return o1.getForgeName().compareTo(o2.getForgeName());
         }
            });

   }

   @Override
   public void selectView(ViewTypeEnum pViewType)
   {
      switch (pViewType)
      {
      case ORGANIZATION:
         forgeViewCT.setVisible(false);
         pagerForge.setVisible(false);
         
         organizationViewCT.setVisible(true);
         pagerOrga.setVisible(true);
         
         profilViewCT.setVisible(false);
         pagerProfile.setVisible(false);
         break;
      case PROFIL:
         forgeViewCT.setVisible(false);
         pagerForge.setVisible(false);
         
         organizationViewCT.setVisible(false);
         pagerOrga.setVisible(false);
         
         profilViewCT.setVisible(true);
         pagerProfile.setVisible(true);
         break;
      case FORGE:
      default:
         forgeViewCT.setVisible(true);
         pagerForge.setVisible(true);
         
         organizationViewCT.setVisible(false);
         pagerOrga.setVisible(false);
         
         profilViewCT.setVisible(false);
         pagerProfile.setVisible(false);
         break;
      }

   }

   @Override
   public void createRolesColumn(List<ProfilViewDTO> pProfilViews)
   {

      boolean      noDistribution = false;
      List<String> roles          = new ArrayList<String>();
      for (ProfilViewDTO profilViewDTO : pProfilViews)
      {
         if (StatusDataAccessEnum.DISTRIBUTION_DOWN.equals(profilViewDTO.getStatus()))
         {
            noDistribution = true;
         }
         Set<String> key = profilViewDTO.getRoles().keySet();
         Iterator<String> it = key.iterator();
         while (it.hasNext())
         {
            String role = it.next();
            // if role has not already been added, create a new column for its
            if (!roles.contains(role))
            {
               roles.add(role);
               profilViewCT.addColumn(new IndexedColumn(role), role);
            }
         }
      }

      if (noDistribution)
      {
         noDistributionPanel();
      }

   }

   interface ReportingViewImplUiBinder extends UiBinder<Widget, ReportingViewImpl>
   {
   }


}
