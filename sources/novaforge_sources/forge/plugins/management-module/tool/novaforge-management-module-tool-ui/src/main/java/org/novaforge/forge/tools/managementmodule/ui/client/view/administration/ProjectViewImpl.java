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

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.ProjectRepositorySettingsMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.ressources.ManagementModuleRessources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.ClickableImageResourceCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableEditTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.CDOParametersDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectViewImpl extends Composite implements ProjectView
{
   private static ManagementModuleRessources ressources = GWT.create(ManagementModuleRessources.class);
   private static ProjectRepositorySettingsViewImplUiBinder uiBinder = GWT
         .create(ProjectRepositorySettingsViewImplUiBinder.class);
	private static int pageSizeDisciplines = 20;
	private static int pageSizeCategories = 30;
  private final ProjectRepositorySettingsMessage projectRepositorySettingsMessages = (ProjectRepositorySettingsMessage) GWT.create(ProjectRepositorySettingsMessage.class);
   @UiField
   Button buttonSaveReferentielProjet;
   @UiField
   Button buttonCancelReferentielProjet;
	@UiField
	Button buttonHomeReturn;
	@UiField
	Button buttonImportRefScopeUnit;
	@UiField
	Label projectRepositorySettingsTitle;
	@UiField
	Grid mainGrid;
	@UiField
	Label transformationLabel;
	@UiField
	TextBox inputNbrDeJourParAnBox;
	@UiField
	Label inputNbrDeJourParAnLabel;
	@UiField
	TextBox inputNbrDeJourParMoisBox;
	@UiField
	Label inputNbrDeJourParMoisLabel;
	@UiField
	TextBox inputNbrDeJourParSemaineBox;
	@UiField
	Label inputNbrDeJourParSemaineLabel;
	@UiField
	TextBox inputNbrHeuresParJourBox;
	@UiField
	Label inputNbrHeuresParJourLabel;
	@UiField
	Label inputNbJoursNonTravailLabel;
	@UiField
	TextBox inputNbJoursNonTravailBox;
	@UiField
	Label parametrageCDOLabel;
    @UiField
    Label cdoParameterBLabel;
	@UiField
   Label pointFonctionSimpleLabel;
   @UiField(provided = true)
   CellTable<ComponentDTO> componentCellTable;
	@UiField
	Label tauxRepartitionLabel;
	@UiField(provided = true)
	CellTable<ProjectDisciplineDTO> disciplinesList;
	@UiField
	Label categorieLabel;
	@UiField(provided = true)
	CellTable<TaskCategoryDTO> categoriesList;
  @UiField(provided = true)
   CellTable<CDOParametersDTO> cdoCellTable;
	@UiField
	Label ajouterCategorieLabel;
	@UiField
	TextBox inputAjouterCategorieBox;
	@UiField
	HorizontalPanel ajouterCategoriePanel;
	@UiField
   VerticalPanel ajouterCDOPanel;
	@UiField
	Label uniteTempsIterationLabel;
	@UiField
	Label uniteTempsSemaineLabel;
	@UiField
	RadioButton uniteTempsSemaineRadioButton;
	@UiField
	Label uniteTempsMoisLabel;
	@UiField
	RadioButton uniteTempsMoisRadioButton;
  private ListDataProvider<ComponentDTO>         componentProvider;
  private Column<ComponentDTO, String>           componentLabelColumn;
  private EditTextCell                           componentPercentTextInputCell;
  private Column<ComponentDTO, String>           repartitionComponentColumn;
  private ListDataProvider<ProjectDisciplineDTO> dataDisciplinesProvider;
  private Column<ProjectDisciplineDTO, String>   libelleDisciplineColumn;
  private EditTextCell                           disciplinePercentTextInputCell;
  private Column<ProjectDisciplineDTO, String>   tauxRepartitionColumn;
  private ListDataProvider<TaskCategoryDTO>      dataCategoriesProvider;
  private Column<TaskCategoryDTO, String>        libelleCategorieColumn;
  private ListDataProvider<CDOParametersDTO>     cdoDataProvider;
  private Column<CDOParametersDTO, String>       hostCDOColumn;
  private Column<CDOParametersDTO, String>       portCDOColumn;
  private Column<CDOParametersDTO, String>       projectCDOColumn;
  private Column<CDOParametersDTO, String>       repositoryCDOColumn;
  private Column<CDOParametersDTO, String>       systemGraalCDOColumn;
  private Column<CDOParametersDTO, String>       cronExpressionCDOColumn;
  private Image                                  ajouterCategorieBouton;
  private Image                                  ajouterCDOBouton;

  public ProjectViewImpl() {
		ressources.css().ensureInjected();
		initCDOCellTable();
		initDisciplinesTable();
		initCategoriesTable();
		initComponentCellTable();
		initWidget(uiBinder.createAndBindUi(this));
		buttonSaveReferentielProjet.setText(Common.getGlobal().buttonSave());
		buttonCancelReferentielProjet.setText(projectRepositorySettingsMessages.buttonCancelModifications());
		buttonHomeReturn.setText(projectRepositorySettingsMessages.buttonHomeReturn());
		projectRepositorySettingsTitle.setText(projectRepositorySettingsMessages.projectRepositorySettingsTitle());
		transformationLabel.setText(projectRepositorySettingsMessages.transformationLabel());
		inputNbrDeJourParAnLabel.setText(projectRepositorySettingsMessages.inputNbrDeJourParAnLabel());
		inputNbrDeJourParMoisLabel.setText(projectRepositorySettingsMessages.inputNbrDeJourParMoisLabel());
		inputNbrDeJourParSemaineLabel.setText(projectRepositorySettingsMessages.inputNbrDeJourParSemaineLabel());
		inputNbrHeuresParJourLabel.setText(projectRepositorySettingsMessages.inputNbrHeuresParJourLabel());
		inputNbJoursNonTravailLabel.setText(projectRepositorySettingsMessages.inputNbJoursNonTravailLabel());
		uniteTempsMoisLabel.setText(projectRepositorySettingsMessages.uniteTempsMoisLabel());
		uniteTempsSemaineLabel.setText(projectRepositorySettingsMessages.uniteTempsSemaineLabel());
		uniteTempsIterationLabel.setText(projectRepositorySettingsMessages.uniteTempsIterationLabel());
		pointFonctionSimpleLabel.setText(projectRepositorySettingsMessages.pointFonctionSimpleLabel());
      tauxRepartitionLabel.setText(projectRepositorySettingsMessages.tauxRepartitionLabel());
      categorieLabel.setText(projectRepositorySettingsMessages.categoryListLabel());
      ajouterCategorieLabel.setText(projectRepositorySettingsMessages.ajouterCategorieLabel());
      mainGrid.setWidth("100%");
      mainGrid.setCellSpacing(5);
      mainGrid.getCellFormatter().setWidth(0, 0, "500px");
      ajouterCategorieBouton = new Image(ressources.addButton());
      ajouterCDOBouton = new Image(ressources.addButton());
      ajouterCategoriePanel.add(ajouterCategorieBouton);
      ajouterCDOPanel.add(ajouterCDOBouton);
      buttonImportRefScopeUnit.setText(projectRepositorySettingsMessages.buttonImportRefScopeUnit());
      parametrageCDOLabel.setText(projectRepositorySettingsMessages.parametrageCDOLabel());
      cdoParameterBLabel.setText(projectRepositorySettingsMessages.inputCdoParameterBLabel());
	}

  private void initCDOCellTable()
  {
    cdoCellTable = new CellTable<CDOParametersDTO>(pageSizeCategories, (Resources) GWT.create(TableResources.class),
                                                   CellKey.CDO_KEY_PROVIDER);
    //      cdoCellTable.setWidth("350px", false);
    initCDOTableColumns();
    //      cdoCellTable.setColumnWidth(libelleCDOColumn, 250, Unit.PX);

    // Add the CellTable to the adapter
    cdoDataProvider = new ListDataProvider<CDOParametersDTO>();
    cdoDataProvider.addDataDisplay(cdoCellTable);

    // Suppression CDOParameters Column
    Column<CDOParametersDTO, ImageResource> suppressionCDOParametersColumn = new Column<CDOParametersDTO, ImageResource>(new ClickableImageResourceCell())
    {
      @Override
      public ImageResource getValue(CDOParametersDTO object)
      {
        return ressources.deleteButton();
      }
    };
    suppressionCDOParametersColumn.setFieldUpdater(new FieldUpdater<CDOParametersDTO, ImageResource>()
    {
      @Override
      public void update(int index, CDOParametersDTO object, ImageResource value)
      {
        cdoDataProvider.getList().remove(object);
      }
    });
    suppressionCDOParametersColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    cdoCellTable.addColumn(suppressionCDOParametersColumn,
                           projectRepositorySettingsMessages.suppressionCategorieColumnLabel());
  }

  private void initDisciplinesTable(){
      disciplinesList = new CellTable<ProjectDisciplineDTO>(pageSizeDisciplines,
            (Resources) GWT.create(TableResources.class),
            CellKey.PROJECT_DISCIPLINE_KEY_PROVIDER);
      disciplinesList.setWidth("100%", false);

      initDisciplinesTableColumns();

      // Add the CellTable to the adapter
      dataDisciplinesProvider = new ListDataProvider<ProjectDisciplineDTO>();
      dataDisciplinesProvider.addDataDisplay(disciplinesList);
   }
	
	private void initCategoriesTable(){
		categoriesList = new CellTable<TaskCategoryDTO>(pageSizeCategories,
				(Resources) GWT.create(TableResources.class),
				CellKey.CATEGORIE_KEY_PROVIDER);
		categoriesList.setWidth("350px", false);
		initCategoriesTableColumns();
		categoriesList.setColumnWidth(libelleCategorieColumn, 250, Unit.PX);

		// Add the CellTable to the adapter
		dataCategoriesProvider = new ListDataProvider<TaskCategoryDTO>();
		dataCategoriesProvider.addDataDisplay(categoriesList);

		// Suppression Categorie Column
      Column<TaskCategoryDTO, ImageResource> suppressionCategorieColumn = new Column<TaskCategoryDTO, ImageResource>(
              new ClickableImageResourceCell())
              {
               @Override
               public ImageResource getValue(TaskCategoryDTO object)
               {
                  return ressources.deleteButton();
               }
              };
      suppressionCategorieColumn.setFieldUpdater(new FieldUpdater<TaskCategoryDTO, ImageResource>()
              {
               @Override
               public void update(int index, TaskCategoryDTO object, ImageResource value)
               {
                  dataCategoriesProvider.getList().remove(object);
               }
              });
    suppressionCategorieColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      categoriesList.addColumn(suppressionCategorieColumn, projectRepositorySettingsMessages.suppressionCategorieColumnLabel());
	}

  private void initComponentCellTable()
  {
    componentCellTable = new CellTable<ComponentDTO>(pageSizeDisciplines, (Resources) GWT.create(TableResources.class),
                                                     CellKey.COMPOSANT_KEY_PROVIDER);
    componentCellTable.setWidth("100%", false);

    // Label Column
    componentLabelColumn = new Column<ComponentDTO, String>(new TextCell())
    {
      @Override
      public String getValue(ComponentDTO object)
      {
        return object.getComposantLibelle();
      }
    };

    componentLabelColumn.setSortable(false);
    componentLabelColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    componentCellTable.addColumn(componentLabelColumn, "", projectRepositorySettingsMessages.libelledisciplineTotal());
    componentCellTable.setColumnWidth(componentLabelColumn, 250, Style.Unit.PX);

    final Map<String, String> attributesMap = new HashMap<String, String>();
      attributesMap.put("size", "5");
    attributesMap.put("maxLength", "5");
    componentPercentTextInputCell = new StylableEditTextCell(attributesMap);
    final Header<String> headerRepartitionComponentColumn = new TextHeader(projectRepositorySettingsMessages
                                                                               .tauxRepartitionColumnLabel());
    final Header<String> footerRepartitionComponentColumn = new TextHeader("0")
    {
         @Override
         public String getValue() {
           float totalPartComponent = getTotalValueComponentPercent();
           return String.valueOf(totalPartComponent);
         }
      };
    repartitionComponentColumn = new Column<ComponentDTO, String>(componentPercentTextInputCell)
    {
         @Override
         public String getValue(ComponentDTO object)
         {
           return String.valueOf(Common.floatFormat(object.getSimplifiedPercentForEstimation(), 2));
         }
      };
    componentCellTable.setColumnWidth(repartitionComponentColumn, 125, Style.Unit.PX);
    repartitionComponentColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    componentCellTable.addColumn(repartitionComponentColumn, headerRepartitionComponentColumn,
                                 footerRepartitionComponentColumn);

    // Add the CellTable to the adapter
    componentProvider = new ListDataProvider<ComponentDTO>();
    componentProvider.addDataDisplay(componentCellTable);
  }
	
	/**
	 * initialize columns for the CDOCellTable
	 */
	private void initCDOTableColumns(){

	   final Map<String, String> hostAttributesMap = new HashMap<String, String>();
	   hostAttributesMap.put("size", "18");

	   hostCDOColumn = new Column<CDOParametersDTO, String>(new StylableEditTextCell(hostAttributesMap)) {
         @Override
         public String getValue(CDOParametersDTO object) {
            if (object.getHost() == null){
               return "";
            }else{
               return object.getHost();
            }
         }
      };
      hostCDOColumn.setSortable(false);

      final Map<String, String> portAttributesMap = new HashMap<String, String>();
      portAttributesMap.put("size", "5");
      portAttributesMap.put("maxLength", "4");

    portCDOColumn = new Column<CDOParametersDTO, String>(new StylableEditTextCell(portAttributesMap)) {
         @Override
         public String getValue(CDOParametersDTO object) {
            if (object.getPort() == null){
               return "";
            }else{
               return String.valueOf(object.getPort());
            }

         }
      };
      portCDOColumn.setSortable(false);

    final Map<String, String> projectAttributesMap = new HashMap<String, String>();
      projectAttributesMap.put("size", "18");

      projectCDOColumn = new Column<CDOParametersDTO, String>(new StylableEditTextCell(projectAttributesMap)) {
         @Override
         public String getValue(CDOParametersDTO object) {
            if (object.getProjetCdo() == null){
               return "";
            }else{
               return object.getProjetCdo();
            }
         }
      };
      projectCDOColumn.setSortable(false);

      final Map<String, String> repositoryAttributesMap = new HashMap<String, String>();
      repositoryAttributesMap.put("size", "18");

      repositoryCDOColumn = new Column<CDOParametersDTO, String>(new StylableEditTextCell(repositoryAttributesMap)) {
         @Override
         public String getValue(CDOParametersDTO object) {
            if (object.getRepository() == null){
               return "";
            }else{
               return object.getRepository();
            }
         }
      };
      repositoryCDOColumn.setSortable(false);

      final Map<String, String> systemGraalAttributesMap = new HashMap<String, String>();
      systemGraalAttributesMap.put("size", "18");

      systemGraalCDOColumn = new Column<CDOParametersDTO, String>(new StylableEditTextCell(systemGraalAttributesMap)) {
         @Override
         public String getValue(CDOParametersDTO object) {
            if (object.getSystemGraal() == null){
               return "";
            }else{
               return object.getSystemGraal();
            }
         }
      };
      systemGraalCDOColumn.setSortable(false);

      final Map<String, String> cronExpressionAttributesMap = new HashMap<String, String>();
      cronExpressionAttributesMap.put("size", "18");

      cronExpressionCDOColumn = new Column<CDOParametersDTO, String>(new StylableEditTextCell(cronExpressionAttributesMap)) {
         @Override
         public String getValue(CDOParametersDTO object) {
            if (object.getCronExpression() == null){
               return "";
            }else{
               return object.getCronExpression();
            }
         }
      };
      cronExpressionCDOColumn.setSortable(false);

      cdoCellTable.addColumn(hostCDOColumn, projectRepositorySettingsMessages.inputHostCDOLabel());
      cdoCellTable.addColumn(portCDOColumn, projectRepositorySettingsMessages.inputPortCDOLabel());
      cdoCellTable.addColumn(repositoryCDOColumn, projectRepositorySettingsMessages.inputRepositoryCDOLabel());
      cdoCellTable.addColumn(projectCDOColumn, projectRepositorySettingsMessages.inputProjetCDOLabel());
      cdoCellTable.addColumn(systemGraalCDOColumn, projectRepositorySettingsMessages.inputSystemGraalCDOLabel());
      cdoCellTable.addColumn(cronExpressionCDOColumn, projectRepositorySettingsMessages.inputCronExpressionLabel());
   }

  /**
   * Initialize Disciplines' column
   */
  private void initDisciplinesTableColumns()
  {
    // Libelle Column
    libelleDisciplineColumn = new Column<ProjectDisciplineDTO, String>(new TextCell())
    {
      @Override
      public String getValue(ProjectDisciplineDTO object)
      {
        return object.getDisciplineDTO().getLibelle();
      }
    };

    libelleDisciplineColumn.setSortable(false);
    libelleDisciplineColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    disciplinesList.addColumn(libelleDisciplineColumn, "", projectRepositorySettingsMessages.libelledisciplineTotal());
    disciplinesList.setColumnWidth(libelleDisciplineColumn, 250, Style.Unit.PX);

    final Map<String, String> attributesMap = new HashMap<String, String>();
    attributesMap.put("size", "5");
    attributesMap.put("maxLength", "3");
    disciplinePercentTextInputCell = new StylableEditTextCell(attributesMap);
    final Header<String> headerTauxRepartitionColumn = new TextHeader(projectRepositorySettingsMessages
                                                                          .tauxRepartitionColumnLabel());
    final Header<String> footerTauxRepartitionColumn = new TextHeader("0")
    {
      @Override
      public String getValue()
      {
        int totalPartDiscplines = getTotalValueDisciplines();
        return String.valueOf(totalPartDiscplines);
      }
    };
    tauxRepartitionColumn = new Column<ProjectDisciplineDTO, String>(disciplinePercentTextInputCell)
    {
      @Override
      public String getValue(ProjectDisciplineDTO object)
      {
        return String.valueOf(object.getDisciplinePourcentage());
      }
    };
    disciplinesList.setColumnWidth(tauxRepartitionColumn, 125, Style.Unit.PX);
    tauxRepartitionColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    disciplinesList.addColumn(tauxRepartitionColumn, headerTauxRepartitionColumn, footerTauxRepartitionColumn);
  }

  private void initCategoriesTableColumns(){
		// Libelle Column
		libelleCategorieColumn = new Column<TaskCategoryDTO, String>(new TextCell()) {
			@Override
			public String getValue(TaskCategoryDTO object) {
				return object.getName();
			}
		};

		libelleCategorieColumn.setSortable(false);

		categoriesList.addColumn(libelleCategorieColumn, projectRepositorySettingsMessages.libelleCategorieColumnLabel());
	}
	
	@Override
	public ListDataProvider<ProjectDisciplineDTO> getDataDisciplinesProvider() {
		return dataDisciplinesProvider;
	}
	
	@Override
	public CellTable<ProjectDisciplineDTO> getDisciplinesList() {
		return disciplinesList;
	}
	
	@Override
	public ListDataProvider<TaskCategoryDTO> getDataCategoriesProvider() {
		return dataCategoriesProvider;
	}
	
	@Override
	public CellTable<TaskCategoryDTO> getCategoriesList() {
		return categoriesList;
	}
	
	@Override
	public HorizontalPanel getAjouterCategoriePanel() {
		return ajouterCategoriePanel;
	}
	
	@Override
	public TextBox getInputAjouterCategorieBox() {
		return inputAjouterCategorieBox;
  }

  @Override
	public CellTable<ProjectDisciplineDTO> getProjectDisciplineList() {
		return disciplinesList;
	}
	
	/**
	 * @return the buttonSaveReferentielProjet
	 */
	@Override
	public Button getButtonSaveReferentielProjet() {
		return buttonSaveReferentielProjet;
  }

  /**
	 * @return the buttonCancelReferentielProjet
	 */
	@Override
	public Button getButtonCancelReferentielProjet() {
		return buttonCancelReferentielProjet;
	}

	/**
	 * @return the inputNbrDeJourParAnBox
	 */
	@Override
	public TextBox getInputNbrDeJourParAnBox() {
		return inputNbrDeJourParAnBox;
  }

  /**
	 * @return the inputNbrDeJourParAnLabel
	 */
	@Override
	public Label getInputNbrDeJourParAnLabel() {
		return inputNbrDeJourParAnLabel;
	}

	/**
	 * @return the inputNbrDeJourParMoisBox
	 */
	@Override
	public TextBox getInputNbrDeJourParMoisBox() {
		return inputNbrDeJourParMoisBox;
	}

	/**
	 * @return the inputNbrDeJourParMoisLabel
	 */
	@Override
	public Label getInputNbrDeJourParMoisLabel() {
		return inputNbrDeJourParMoisLabel;
	}

	/**
	 * @return the inputNbrDeJourParSemaineBox
	 */
	@Override
	public TextBox getInputNbrDeJourParSemaineBox() {
		return inputNbrDeJourParSemaineBox;
	}

	/**
	 * @return the inputNbrDeJourParSemaineLabel
	 */
	@Override
	public Label getInputNbrDeJourParSemaineLabel() {
		return inputNbrDeJourParSemaineLabel;
	}

	/**
	 * @return the inputNbrHeuresParJourBox
	 */
	@Override
	public TextBox getInputNbrHeuresParJourBox() {
		return inputNbrHeuresParJourBox;
	}

	/**
	 * @return the inputNbrHeuresParJourLabel
	 */
	@Override
	public Label getInputNbrHeuresParJourLabel() {
		return inputNbrHeuresParJourLabel;
	}

	/**
	 * @return the inputNbJoursNonTravailLabel
	 */
	@Override
	public Label getInputNbJoursNonTravailLabel() {
		return inputNbJoursNonTravailLabel;
	}

	/**
	 * @return the inputNbJoursNonTravailBox
	 */
	@Override
	public TextBox getInputNbJoursNonTravailBox() {
		return inputNbJoursNonTravailBox;
	}

	/**
	 * @return the uniteTempsSemaineRadioButton
	 */
	@Override
	public RadioButton getUniteTempsSemaineRadioButton() {
		return uniteTempsSemaineRadioButton;
	}

	/**
	 * @return the uniteTempsMoisRadioButton
	 */
	@Override
	public RadioButton getUniteTempsMoisRadioButton() {
		return uniteTempsMoisRadioButton;
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
    * Get the tauxRepartitionColumn
    * @return the tauxRepartitionColumn
    */
	@Override
   public Column<ProjectDisciplineDTO, String> getTauxRepartitionColumn() {
      return tauxRepartitionColumn;
   }

   /**
    * Get the total value on discplines
    * @return the value
    */
   @Override
   public int getTotalValueDisciplines()
   {
     List<ProjectDisciplineDTO> listDisciplineDTO   = disciplinesList.getVisibleItems();
     int                        totalPartDiscplines = 0;
     for (ProjectDisciplineDTO dDTO : listDisciplineDTO)
     {
       totalPartDiscplines = totalPartDiscplines + dDTO.getDisciplinePourcentage();
     }
     return totalPartDiscplines;
   }

  /**
    * Get the disciplinePercentTextInputCell
    * @return the disciplinePercentTextInputCell
    */
	@Override
   public EditTextCell getDisciplinePercentTextInputCell() {
      return disciplinePercentTextInputCell;
   }

	@Override
   public Image getAjouterCategorieBouton() {
      return ajouterCategorieBouton;
  }

   @Override
   public Image getAjouterCDOBouton() {
      return ajouterCDOBouton;
   }

	@Override
	public Button getButtonImportRefScopeUnit() {
		return buttonImportRefScopeUnit;
  }

  /**
    * Get the componentProvider
    * @return the componentProvider
    */
	@Override
   public ListDataProvider<ComponentDTO> getComponentProvider() {
      return componentProvider;
   }

   /**
    * Get the repartitionComponentColumn
    * @return the repartitionComponentColumn
    */
	@Override
   public Column<ComponentDTO, String> getComponentRepartitionColumn() {
      return repartitionComponentColumn;
   }

   /**
    * Get the componentPercentTextInputCell
    * @return the componentPercentTextInputCell
    */
	@Override
   public EditTextCell getComponentPercentTextInputCell() {
      return componentPercentTextInputCell;
   }

   /**
    * Get the componentCellTable
    * @return the componentCellTable
    */
	@Override
   public CellTable<ComponentDTO> getComponentCellTable() {
      return componentCellTable;
  }

   @Override
   public CellTable<CDOParametersDTO> getCDOCellTable() {
      return cdoCellTable;
   }

   @Override
   public ListDataProvider<CDOParametersDTO> getCDODataProvider() {
      return cdoDataProvider;
   }

  /**
   * Get the total value of the components
   *
   * @return the total
   */
  @Override
  public float getTotalValueComponentPercent()
  {
    final List<ComponentDTO> listComponentDTO = componentCellTable.getVisibleItems();
    float                    total            = 0;
    for (final ComponentDTO dto : listComponentDTO)
    {
      total = total + Common.floatFormat(dto.getSimplifiedPercentForEstimation(), 3);
    }
    return Common.floatFormat(total, 3);
  }

  @Override
  public Column<CDOParametersDTO, String> getCronExpressionCDOColumn()
  {
    return cronExpressionCDOColumn;
   }

  @Override
  public Column<CDOParametersDTO, String> getSystemGraalCDOColumn()
  {
    return systemGraalCDOColumn;
   }

  @Override
  public Column<CDOParametersDTO, String> getRepositoryCDOColumn()
  {
    return repositoryCDOColumn;
  }

  @Override
   public Column<CDOParametersDTO, String> getProjectCDOColumn(){
      return projectCDOColumn;
   }

  @Override
  public Column<CDOParametersDTO, String> getPortCDOColumn()
  {
    return portCDOColumn;
   }

  @Override
  public Column<CDOParametersDTO, String> getHostCDOColumn()
  {
    return hostCDOColumn;
  }

  interface ProjectRepositorySettingsViewImplUiBinder extends UiBinder<Widget, ProjectViewImpl> {
   }
}
