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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.iterationmonitoring;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowBacklogEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.iterationmonitoring.IterationMonitoringView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.iterationmonitoring.IterationMonitoringViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitIterationMonitoringDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The presenter of the view which display the monitoring of an iteration
 */
public class IterationMonitoringPresenter implements TabPresenter {

   public static final String ITERATION_MONITORING_CSV_NAME = "IterationMonitoring_";
   private final IterationMonitoringView view = new IterationMonitoringViewImpl();
   private IterationDTO iteration;
   /**
    * Get the scopeUnits
    */
   private AbstractManagementRPCCall<List<ScopeUnitIterationMonitoringDTO>> getScopeUnits           = new AbstractManagementRPCCall<List<ScopeUnitIterationMonitoringDTO>>()
   {

      @Override
      protected void callService(AsyncCallback<List<ScopeUnitIterationMonitoringDTO>> callback)
      {
         view.getIterationMonitoringCellTable().setVisibleRangeAndClearData(view.getIterationMonitoringCellTable()
                                                                                .getVisibleRange(), true);
         view.getIterationMonitoringCellTable().fireEvent(new LoadingStateChangeEvent(LoadingState.LOADING));
         Common.SCOPE_SERVICE.getScopeUnitMonitoringByIteration(iteration.getIterationId(), getDisciplineFilter(),
                                                                callback);
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }

      @Override
      public void onSuccess(List<ScopeUnitIterationMonitoringDTO> pResult)
      {
         if (pResult != null)
         {
            view.getIterationMonitoringCellTable().fireEvent(new LoadingStateChangeEvent(LoadingState.LOADED));
            resetSelectionModel();
            view.setFullDataList(pResult);
            view.updateSortHandler();
            view.getDataProvider().updateRowData(0, pResult);
            view.getDataProvider().updateRowCount(pResult.size(), true);
         }
      }

   };
   /**
    * Get And display the indicators
    */
   private AbstractManagementRPCCall<MonitoringIndicatorsDTO>               getAndDisplayIndicators = new AbstractManagementRPCCall<MonitoringIndicatorsDTO>()
   {

      @Override
      protected void callService(AsyncCallback<MonitoringIndicatorsDTO> callback)
      {
         Common.TASK_SERVICE.getMonitoringIndicators(iteration.getIterationId(), getDisciplineFilter(), callback);
      }

      @Override
      public void onSuccess(MonitoringIndicatorsDTO monitoringIndicatorsDTO)
      {
         displayIndicators(monitoringIndicatorsDTO);
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }
   };
   AbstractManagementRPCCall<IterationDTO> getDefaultIterationAndDoDisplay = new AbstractManagementRPCCall<IterationDTO>()
   {

      @Override
      protected void callService(AsyncCallback<IterationDTO> pCb)
      {
         Common.ITERATION_SERVICE.getCurrentOrLastFinishedIteration(SessionData.currentValidatedProjectPlanId, false,
                                                                    pCb);
      }

      @Override
      public void onSuccess(IterationDTO pResult)
      {
         if (pResult != null)
         {
            iteration = pResult;
            displayIterationDatas();
            getScopeUnits.retry(0);
            getAndDisplayIndicators.retry(0);
         }
         else
         {
            new InfoDialogBox(Common.MESSAGES_MONITORING.noIterationAvailable(), InfoTypeEnum.KO).getDialogPanel()
                                                                                                 .center();
         }
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }
   };

   /**
    * Constructor
    */
   public IterationMonitoringPresenter()
   {
      bind();
      fillDisciplinesListBox();
   }

   /**
    * Link with the view / event management
    */
   private void bind()
   {
      view.getHomeReturnButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            ManagementModuleEntryPoint.getHomePresenter().go(RootLayoutPanel.get());
         }
      });
      view.getCsvExportButton().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(ClickEvent event)
         {
            doCsvExport(view.getFullDataList());

         }
      });
      // discpline changement management
      view.getDisciplinesListBox().addChangeHandler(new ChangeHandler()
      {

         @Override
         public void onChange(ChangeEvent event)
         {
            getScopeUnits.retry(0);
            getAndDisplayIndicators.retry(0);
         }
      });
      view.getIterationDetailButton().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(ClickEvent event)
         {
            Common.GLOBAL_EVENT_BUS.fireEvent(new ShowBacklogEvent(iteration));
         }
      });
   }

   /**
    * Fill the disciplines list box
    */
   private void fillDisciplinesListBox()
   {
      view.getDisciplinesListBox().addItem(Common.EMPTY_TEXT, Common.EMPTY_TEXT, null);
      final List<DisciplineDTO> disciplines = new ArrayList<DisciplineDTO>(SessionData.disciplinesOfConnectedUser);
      Collections.sort(disciplines, new Comparator<DisciplineDTO>()
      {
         @Override
         public int compare(DisciplineDTO o1, DisciplineDTO o2)
         {
            return o1.getLibelle().compareTo(o2.getLibelle());
         }
      });
      for (final DisciplineDTO discipline : disciplines)
      {
         view.getDisciplinesListBox().addItem(discipline.getLibelle(), discipline.getFunctionalId(), discipline);
      }
   }

   /**
    * Export the datas into CSV file
    * @param listToExport the list to export
    */
   private void doCsvExport(final List<ScopeUnitIterationMonitoringDTO> listToExport)
   {
      new AbstractManagementRPCCall<Void>()
      {

         @Override
         protected void callService(AsyncCallback<Void> callBack)
         {
            Common.SCOPE_SERVICE.createCSVFromScopeUnitIterationMonitoringDTOList(listToExport, callBack);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught.getMessage());

         }

         @Override
         public void onSuccess(Void v)
         {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(Constants.EXPORT_CSV_NAME_PARAMETER, ITERATION_MONITORING_CSV_NAME + iteration.getLabel());
            Common.exportCSV(GWT.getModuleBaseURL() + Constants.EXPORT_CSV_SERVLET_NAME, parameters);
         }

      }.retry(0);
   }

   /**
    * Set the displayedIteration
    *
    * @param iteration
    *     the displayedIteration to set
    */
   public void setIteration(IterationDTO iteration)
   {
      this.iteration = iteration;
   }

   @Override
   public void go(HasWidgets container)
   {
      container.clear();
      container.add(view.asWidget());
   }

   @Override
   public void loadDataOnSelectionTab()
   {
      //direct click without choosing iteration -> we display default one
      if (iteration == null)
      {
         getDefaultIterationAndDoDisplay.retry(0);
      }
      else
      {
         displayIterationDatas();
         getScopeUnits.retry(0);
         getAndDisplayIndicators.retry(0);
      }
   }

   @Override
   public IsWidget getDisplay()
   {
      return view.asWidget();
   }

   /**
    * Display the selected iteration
    */
   private void displayIterationDatas()
   {
      fillContextTextBoxes();
   }

   /**
    * Fill the text boxes of the context
    */
   private void fillContextTextBoxes()
   {
      view.getIterationValueLabel().setText(iteration.getLabel());
      final LotDTO lot = iteration.getLot();
      view.getLotValueLabel().setText(iteration.getLot().getName());
      if (lot.getParentLotId() == null)
      {
         view.getParentLotValueLabel().setText(Common.EMPTY_TEXT);
      }
      else
      {
         view.getParentLotValueLabel().setText(iteration.getLot().getParentLotName());
      }
      view.getStartDateValueLabel().setText(Common.FR_DATE_FORMAT_ONLY_DAY.format(iteration.getStartDate()));
      view.getEndDateValueLabel().setText(Common.FR_DATE_FORMAT_ONLY_DAY.format(iteration.getEndDate()));

   }

   /**
    * Display the indicators
    * @param monitoringIndicatorsDTO the object which contains the indicators
    */
   private void displayIndicators(MonitoringIndicatorsDTO monitoringIndicatorsDTO)
   {
      view.getAdvancementTB().setText(String.valueOf(Common.floatFormat(monitoringIndicatorsDTO.getAdvancement(), 2)));
      view.getConsumedTB().setText(String.valueOf(Common.floatFormat(monitoringIndicatorsDTO.getConsumed(), 2)));
      view.getErrorTB().setText(String.valueOf(Common.floatFormat(monitoringIndicatorsDTO.getAverageEstimationError(),
                                                                  2)));
      view.getFocalisationTB().setText(String.valueOf(Common.floatFormat(monitoringIndicatorsDTO.getFocalisation(),
                                                                         2)));
      view.getNbActorsTB().setText(String.valueOf(monitoringIndicatorsDTO.getActiveUsersNumber()));
      view.getVelocityTB().setText(String.valueOf(Common.floatFormat(monitoringIndicatorsDTO.getVelocity(), 2)));
   }

   /**
    * Reset the selectionModel
    */
   private void resetSelectionModel()
   {
      if (view.getSelectionModel().getSelectedObject() != null)
      {
         view.getSelectionModel().setSelected(view.getSelectionModel().getSelectedObject(), false);
      }
   }

   /**
    * Get the discipline filter using the list box
    * @return the functionalId of the selected discipline or null
    */
   private String getDisciplineFilter()
   {
      String filter = null;
      if (view.getDisciplinesListBox().getSelectedAssociatedObject() != null)
      {
         filter = view.getDisciplinesListBox().getSelectedAssociatedObject().getFunctionalId();
      }
      return filter;
   }
   
}
