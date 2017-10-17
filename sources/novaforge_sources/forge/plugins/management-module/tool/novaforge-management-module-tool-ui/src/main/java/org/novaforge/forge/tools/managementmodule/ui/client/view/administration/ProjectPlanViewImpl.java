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

package org.novaforge.forge.tools.managementmodule.ui.client.view.administration;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.ProjectRepositorySettingsMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.ressources.ManagementModuleRessources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableEditTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorJointureDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qsivan
 */
public class ProjectPlanViewImpl extends Composite implements ProjectPlanView {
	private static ManagementModuleRessources ressources = GWT
			.create(ManagementModuleRessources.class);
	private static ProjectPlanViewImplUiBinder uiBinder = GWT
	.create(ProjectPlanViewImplUiBinder.class);
	private static int pageSizeComposants = 20;
	private static int pageSizeFacteursAjustement = 20;
	private final ProjectRepositorySettingsMessage projectRepositorySettingsMessages = (ProjectRepositorySettingsMessage) GWT.create(ProjectRepositorySettingsMessage.class);
	@UiField
	Label projectPlanTitle;
	@UiField
	Grid mainGrid;
	@UiField
   Button buttonSaveSettings;
	@UiField
   Button buttonCancelModifications;
	@UiField
   Button buttonHomeReturn;
	@UiField
	Label pointFonctionDetailLabel;
	@UiField
	VerticalPanel leftPanel;
	CellTable<ComponentDTO> composantsList;
	@UiField
	Label pointFonctionLabel;
	@UiField
	TextBox inputAbaqueChargeHJBox;
	@UiField
	Label inputAbaqueChargeHJLabel;
	@UiField
	Label coefficientAjustementLabel;
	@UiField
   VerticalPanel rightPanel;
	CellTable<AdjustFactorJointureDTO> facteursAjustementList;
	private ListDataProvider<ComponentDTO> dataComposantsProvider;
	private Column<ComponentDTO, String>   libelleComposantColumn;
	private Column<ComponentDTO, String>   simpleColumn;
	private Column<ComponentDTO, String>   moyenColumn;
	private Column<ComponentDTO, String>   complexeColumn;
	private ListDataProvider<AdjustFactorJointureDTO> dataFacteursAjustementProvider;
	private Column<AdjustFactorJointureDTO, String> libelleFacteurAjustementColumn;
	//the textCells for composant celltable
   private StylableEditTextCell simpleStylableEditTextCell;
   private StylableEditTextCell mediumStylableEditTextCell;
   private StylableEditTextCell complexStylableEditTextCell;
	public ProjectPlanViewImpl() {
		ressources.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		initComposantsTable();
		initFacteursAjustementTable();
		mainGrid.getCellFormatter().setWidth(0, 0, "500px");
		projectPlanTitle.setText(projectRepositorySettingsMessages.projectPlanTitle());
		pointFonctionDetailLabel.setText(projectRepositorySettingsMessages.pointFonctionDetailLabel());
		pointFonctionLabel.setText(projectRepositorySettingsMessages.pointFonctionLabel());
		inputAbaqueChargeHJLabel.setText(projectRepositorySettingsMessages.inputAbaqueChargeHJLabel());
		coefficientAjustementLabel.setText(projectRepositorySettingsMessages.coefficientAjustementLabel());
		buttonSaveSettings.setText(Common.getGlobal().buttonSave());
		buttonCancelModifications.setText(projectRepositorySettingsMessages.buttonCancelModifications());
		buttonHomeReturn.setText(projectRepositorySettingsMessages.buttonHomeReturn());
	}
	
	private void initComposantsTable(){
      composantsList = new CellTable<ComponentDTO>(pageSizeComposants,
            (Resources) GWT.create(TableResources.class), CellKey.COMPOSANT_KEY_PROVIDER);
      composantsList.setWidth("325px");
		initComposantsTableColumns();
		leftPanel.add(composantsList);
		// Add the CellTable to the adapter
		dataComposantsProvider = new ListDataProvider<ComponentDTO>();
		dataComposantsProvider.addDataDisplay(composantsList);
	}
	
	private void initFacteursAjustementTable(){
      facteursAjustementList = new CellTable<AdjustFactorJointureDTO>(pageSizeFacteursAjustement,
            (Resources) GWT.create(TableResources.class), CellKey.FACTEUR_AJUSTEMENT_KEY_PROVIDER);
      facteursAjustementList.setWidth("100%", false);

		initFacteursAjustementTableColumns();
		rightPanel.add(facteursAjustementList);
		// Add the CellTable to the adapter
		dataFacteursAjustementProvider = new ListDataProvider<AdjustFactorJointureDTO>();
		dataFacteursAjustementProvider.addDataDisplay(facteursAjustementList);
	}
	
	private void initComposantsTableColumns(){
		// Libelle Column
		libelleComposantColumn = new Column<ComponentDTO, String>(new TextCell()) {
			@Override
			public String getValue(ComponentDTO object) {
				return object.getComposantLibelle();
			}
		};
		libelleComposantColumn.setSortable(false);
		composantsList.setColumnWidth(libelleComposantColumn, 125, Unit.PX);

		final Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("size", "5");
		attributesMap.put("maxLength", "3");

		simpleStylableEditTextCell = new StylableEditTextCell(attributesMap);
		FieldUpdater<ComponentDTO, String> simpleFieldUpdater = new FieldUpdater<ComponentDTO, String>() {
         @Override
         public void update(int index, ComponentDTO object, String value) {
            try {
               int intValue = Integer.valueOf(value);
               dataComposantsProvider.getList().get(index).setComposantValeurSimple(intValue);
            } catch (NumberFormatException e) {
               showNotAnIntegerMessage();
               //pass out GWT edit text cell  cache
               simpleStylableEditTextCell.clearViewData(object.getComposantLibelle());
               composantsList.redraw();
            }
         }
      };
      simpleColumn = new Column<ComponentDTO, String>(simpleStylableEditTextCell) {
			@Override
			public String getValue(ComponentDTO object) {
				return String.valueOf(object.getComposantValeurSimple());
			}
		};
		setCommonColumnComposantProperties(simpleColumn);
		simpleColumn.setFieldUpdater(simpleFieldUpdater);

		mediumStylableEditTextCell = new StylableEditTextCell(attributesMap);
      FieldUpdater<ComponentDTO, String> mediumFieldUpdater = new FieldUpdater<ComponentDTO, String>() {
         @Override
         public void update(int index, ComponentDTO object, String value) {
            try {
               int intValue = Integer.valueOf(value);
               dataComposantsProvider.getList().get(index).setComposantValeurMoyen(intValue);
            } catch (NumberFormatException e) {
               showNotAnIntegerMessage();
               //pass out GWT edit text cell  cache
               mediumStylableEditTextCell.clearViewData(object.getComposantLibelle());
               composantsList.redraw();
            }
         }
      };
		moyenColumn = new Column<ComponentDTO, String>(mediumStylableEditTextCell) {
			@Override
			public String getValue(ComponentDTO object) {
				return String.valueOf(object.getComposantValeurMoyen());
			}
		};
		composantsList.setColumnWidth(moyenColumn, 75, Unit.PX);
		setCommonColumnComposantProperties(moyenColumn);
		moyenColumn.setFieldUpdater(mediumFieldUpdater);

		complexStylableEditTextCell = new StylableEditTextCell(attributesMap);
      FieldUpdater<ComponentDTO, String> complexFieldUpdater = new FieldUpdater<ComponentDTO, String>() {
         @Override
         public void update(int index, ComponentDTO object, String value) {
            try {
               int intValue = Integer.valueOf(value);
               dataComposantsProvider.getList().get(index).setComposantValeurComplexe(intValue);
            } catch (NumberFormatException e) {
               showNotAnIntegerMessage();
               //pass out GWT edit text cell  cache
               complexStylableEditTextCell.clearViewData(object.getComposantLibelle());
               composantsList.redraw();
            }
         }
      };
		complexeColumn = new Column<ComponentDTO, String>(complexStylableEditTextCell) {
			@Override
			public String getValue(ComponentDTO object) {
				return String.valueOf(object.getComposantValeurComplexe());
			}
		};
		setCommonColumnComposantProperties(complexeColumn);
		complexeColumn.setFieldUpdater(complexFieldUpdater);

		composantsList.addColumn(libelleComposantColumn, "");
		composantsList.addColumn(simpleColumn, projectRepositorySettingsMessages.simpleLabel());
		composantsList.addColumn(moyenColumn, projectRepositorySettingsMessages.moyenLabel());
		composantsList.addColumn(complexeColumn, projectRepositorySettingsMessages.complexeLabel());
	}
	
	private void initFacteursAjustementTableColumns(){
		// Libelle Column
		libelleFacteurAjustementColumn = new Column<AdjustFactorJointureDTO, String>(new TextCell()) {
			@Override
			public String getValue(AdjustFactorJointureDTO object) {
				return object.getAdjustFactor().getName();
			}
		};

		libelleFacteurAjustementColumn.setSortable(false);

		facteursAjustementList.addColumn(libelleFacteurAjustementColumn, "");
	}

	/**
	 * Show the error pop up with not an inter message
	 */
	private void showNotAnIntegerMessage()
	{
		InfoDialogBox info = new InfoDialogBox(Common.GLOBAL.messageNotAnInteger(), InfoTypeEnum.KO);
		info.getDialogPanel().center();
		info.getDialogPanel().show();
	}

	/**
	 * Set the common properties of the composant's column
	 *
	 * @param column
	 *     the column to modify
	 */
	private void setCommonColumnComposantProperties(Column<ComponentDTO, String> column)
	{
		column.setSortable(false);
		column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		composantsList.setColumnWidth(column, 75, Unit.PX);
	}

	@Override
	public ListDataProvider<ComponentDTO> getDataComposantsProvider() {
		return dataComposantsProvider;
	}
	
	@Override
	public ListDataProvider<AdjustFactorJointureDTO> getDataFacteursAjustementProvider() {
		return dataFacteursAjustementProvider;
	}
	
	@Override
	public CellTable<AdjustFactorJointureDTO> getFacteursAjustementList() {
		return facteursAjustementList;
	}

   /**
    * Get the buttonSaveSettings
    * @return the buttonSaveSettings
    */
	@Override
   public Button getButtonSaveSettings() {
      return buttonSaveSettings;
   }

   /**
    * Get the buttonCancelModifications
    * @return the buttonCancelModifications
    */
	@Override
   public Button getButtonCancelModifications() {
      return buttonCancelModifications;
   }

   /**
    * Get the buttonHomeReturn
    * @return the buttonHomeReturn
    */
	@Override
   public Button getButtonHomeReturn() {
      return buttonHomeReturn;
   }

   /**
    * Get the inputAbaqueChargeHJBox
    * @return the inputAbaqueChargeHJBox
    */
	@Override
   public TextBox getInputAbaqueChargeHJBox() {
      return inputAbaqueChargeHJBox;
   }

   /**
    * Get the composantsList
    * @return the composantsList
    */
	@Override
   public CellTable<ComponentDTO> getComposantsList() {
      return composantsList;
   }

   /**
    * Get the simpleStylableEditTextCell
    * @return the simpleStylableEditTextCell
    */
   @Override
   public StylableEditTextCell getSimpleStylableEditTextCell() {
      return simpleStylableEditTextCell;
	 }

	/**
    * Get the mediumStylableEditTextCell
    * @return the mediumStylableEditTextCell
    */
   @Override
   public StylableEditTextCell getMediumStylableEditTextCell() {
      return mediumStylableEditTextCell;
   }

   /**
    * Get the complexStylableEditTextCell
    * @return the complexStylableEditTextCell
    */
   @Override
   public StylableEditTextCell getComplexStylableEditTextCell() {
      return complexStylableEditTextCell;
	 }

	interface ProjectPlanViewImplUiBinder extends UiBinder<Widget, ProjectPlanViewImpl>
	{
	}
}
