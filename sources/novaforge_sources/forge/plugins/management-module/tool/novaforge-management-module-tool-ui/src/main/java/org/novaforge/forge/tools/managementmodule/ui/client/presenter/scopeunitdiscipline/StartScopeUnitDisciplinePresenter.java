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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.scopeunitdiscipline;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowScopeUnitDisciplineViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scopeunitdiscipline.StartScopeUnitDisciplineView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scopeunitdiscipline.StartScopeUnitDisciplineViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineStatusDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitLightDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Bilet-jc
 */
public class StartScopeUnitDisciplinePresenter implements Presenter
{

  private final StartScopeUnitDisciplineView display;
  /* local bus event */
  private final SimpleEventBus               localEventBus;
  private       List<ScopeUnitDisciplineDTO> scopeUnitDisciplines;
  private       Set<DisciplineDTO>           disciplines;
  AbstractManagementRPCCall<Set<DisciplineDTO>> getDisciplines = new AbstractManagementRPCCall<Set<DisciplineDTO>>()
  {
    @Override
    protected void callService(final AsyncCallback<Set<DisciplineDTO>> asyncCallback)
    {
      Common.COMMON_SERVICE.getDisciplinesOfConnectedUser(SessionData.projectId, asyncCallback);
    }

    @Override
    public void onFailure(final Throwable caught)
    {
      ErrorManagement.displayErrorMessage(caught);
    }

    @Override
    public void onSuccess(Set<DisciplineDTO> pResult)
    {
      if (pResult != null)
      {
        disciplines = pResult;
        refreshDisciplineList();
      }
    }

  };
  private Map<String, Set<DisciplineDTO>> isTerminatedScopeUnit;
  AbstractManagementRPCCall<List<ScopeUnitLightDTO>> getScopeUnits = new AbstractManagementRPCCall<List<ScopeUnitLightDTO>>()
  {

    @Override
    protected void callService(AsyncCallback<List<ScopeUnitLightDTO>> pCb)
    {
      Common.SCOPE_SERVICE.getScopeUnitLight(SessionData.currentValidatedProjectPlanId, pCb);
    }

    @Override
    public void onSuccess(List<ScopeUnitLightDTO> pResult)
    {
      if (pResult != null)
      {
        List<ScopeUnitLightDTO> notFinishedScopeUnits = new ArrayList<ScopeUnitLightDTO>();
        createTerminatedScopeUnitMap();
        for (ScopeUnitLightDTO scopeUnitLight : pResult)
        {
          if (!scopeUnitLight.isFinished())
          {
            if (isTerminatedScopeUnit.containsKey(scopeUnitLight.getUnitId()))
            {
              if (isTerminatedScopeUnit.get(scopeUnitLight.getUnitId()) != null
                      && isTerminatedScopeUnit.get(scopeUnitLight.getUnitId()).size() > 0)
              {
                notFinishedScopeUnits.add(scopeUnitLight);
              }
            }
            else
            {
              notFinishedScopeUnits.add(scopeUnitLight);
              isTerminatedScopeUnit.put(scopeUnitLight.getUnitId(), disciplines);
            }
          }
        }
        refreshScopeUnitList(notFinishedScopeUnits);
      }
    }

    @Override
    public void onFailure(Throwable pCaught)
    {
      ErrorManagement.displayErrorMessage(pCaught);
    }
  };

  /**
   *
   */
  public StartScopeUnitDisciplinePresenter(SimpleEventBus localEventBus)
  {
    super();
    this.localEventBus = localEventBus;
    display = new StartScopeUnitDisciplineViewImpl();
    bind();
  }

  private void bind()
  {
    display.getButtonValidate().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        List<ScopeUnitDisciplineDTO> scopeUnitDisciplines = new ArrayList<ScopeUnitDisciplineDTO>();
        /* status processing */
        ScopeUnitDisciplineStatusDTO status = new ScopeUnitDisciplineStatusDTO();
        status.setFunctionalId(ScopeUnitDisciplineStatusEnum.IN_PROGRESS.getFunctionnalId());
        for (ScopeUnitLightDTO scopeUnit : display.getSelectionModelScopeUnit().getSelectedSet())
        {
        /* scope unit processing */
          ScopeUnitDTO scopeUnitDTO = new ScopeUnitDTO();
          scopeUnitDTO.setUnitId(scopeUnit.getUnitId());
          for (DisciplineDTO discipline : display.getSelectionModelDiscipline().getSelectedSet())
          {
            ScopeUnitDisciplineDTO scopeUnitDisciplineDTO = new ScopeUnitDisciplineDTO();
        /* discipline processing */
            DisciplineDTO disciplineDTO = new DisciplineDTO();
            disciplineDTO.setFunctionalId(discipline.getFunctionalId());
            // scopeUnitDiscipline creation
            scopeUnitDisciplineDTO.setDiscipline(disciplineDTO);
            scopeUnitDisciplineDTO.setScopeUnit(scopeUnitDTO);
            scopeUnitDisciplineDTO.setStatus(status);
            scopeUnitDisciplines.add(scopeUnitDisciplineDTO);
          }
        }
        createScopeUnitDiscipline(scopeUnitDisciplines);
      }
    });
    display.getSelectionModelScopeUnit().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
    {

      @Override
      public void onSelectionChange(SelectionChangeEvent event)
      {
        boolean enableValidateButton = false;
        /* check the selectionModel has selected objects */
        Set<ScopeUnitLightDTO> selectedScopeUnits = display.getSelectionModelScopeUnit().getSelectedSet();
        if (selectedScopeUnits != null && selectedScopeUnits.size() > 0)
        {
          disciplines = getCommonDisciplines(selectedScopeUnits);
          refreshDisciplineList();
          // check the selected disciplines to enable or not the
          // validate button
          Set<DisciplineDTO> selectedDisciplines = display.getSelectionModelDiscipline().getSelectedSet();
          if (selectedDisciplines != null && selectedDisciplines.size() > 0)
          {
            enableValidateButton = true;
          }
        }
        display.getButtonValidate().setEnabled(enableValidateButton);
      }
    });
    display.getSelectionModelDiscipline().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
    {

      @Override
      public void onSelectionChange(SelectionChangeEvent event)
      {
        // check the selected disciplines and scopeUnit to enable or not
        // the validate button
        boolean                enableValidateButton = false;
        Set<DisciplineDTO>     selectedDisciplines  = display.getSelectionModelDiscipline().getSelectedSet();
        Set<ScopeUnitLightDTO> selectedScopeUnits   = display.getSelectionModelScopeUnit().getSelectedSet();
        if (selectedDisciplines != null && selectedDisciplines.size() > 0 && selectedScopeUnits != null
                && selectedScopeUnits.size() > 0)
        {
          enableValidateButton = true;
        }
        display.getButtonValidate().setEnabled(enableValidateButton);
      }

    });
  }

  /**
   * Create a couple scope unit - discipline
   */
  private void createScopeUnitDiscipline(final List<ScopeUnitDisciplineDTO> scopeUnitDisciplineDTO)
  {
    new AbstractManagementRPCCall<List<ScopeUnitDisciplineDTO>>()
    {

      @Override
      protected void callService(AsyncCallback<List<ScopeUnitDisciplineDTO>> pCb)
      {
        Common.TASK_SERVICE.createScopeUnitDiscipline(scopeUnitDisciplineDTO, pCb);
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
        ErrorManagement.displayErrorMessage(pCaught);
      }

      @Override
      public void onSuccess(List<ScopeUnitDisciplineDTO> pResult)
      {
        localEventBus.fireEvent(new ShowScopeUnitDisciplineViewEvent());
        display.hideWidget();
        display.getSuccessCreationBox().getDialogPanel().center();
        display.getSuccessCreationBox().getDialogPanel().show();
      }

    }.retry(0);
  }

  /**
   * Return the intersection of Disciplines of selected ScopeUnits
   *
   * @param selected
   *
   * @return a set of Disciplines
   */
  private Set<DisciplineDTO> getCommonDisciplines(Set<ScopeUnitLightDTO> selected)
  {
    final Set<DisciplineDTO>       ret   = new HashSet<DisciplineDTO>();
    final List<Set<DisciplineDTO>> lists = new ArrayList<Set<DisciplineDTO>>();
    // size list comparator to begin the list comparaison with the smallest
    // list
    final Comparator<Set<DisciplineDTO>> LISTS_SIZE_COMPARATOR = new Comparator<Set<DisciplineDTO>>()
    {
      @Override
      public int compare(Set<DisciplineDTO> o1, Set<DisciplineDTO> o2)
      {
        return String.valueOf(o1.size()).compareTo(String.valueOf(o2.size()));
      }
    };
    // add each allowed disciplines list in a list
    for (ScopeUnitLightDTO scopeUnitLight : selected)
    {
      Set<DisciplineDTO> disciplines = isTerminatedScopeUnit.get(scopeUnitLight.getUnitId());
      if (disciplines == null)
      {
        break;
      }
      lists.add(disciplines);
    }
    // if more than 1 scopeUnit selected, compare each set of disciplines to
    // obtain the commons ones
    if (lists.size() > 1)
    {
      Collections.sort(lists, LISTS_SIZE_COMPARATOR);
      Set<DisciplineDTO> initSet = lists.get(0);
      for (int i = 1; i < lists.size(); i++)
      {
        if (initSet.size() == 0)
        {
          break;
        }
        initSet.retainAll(lists.get(i));
      }
      ret.addAll(initSet);
    }
    // if only 1 scopeUnit selected, only 1 set of disciplines
    else if (lists.size() == 1)
    {
      ret.addAll(lists.get(0));
    }
    return ret;

  }

  public void refreshDisciplineList()
  {
    display.getSelectionModelDiscipline().clear();
    Collections.sort(new ArrayList<DisciplineDTO>(disciplines), display.getComparatorDiscipline());
    display.getDataProviderDiscipline().setList(new ArrayList<DisciplineDTO>(disciplines));

  }

  @Override
  public void go(HasWidgets container)
  {
    getScopeUnits.retry(0);
    getDisciplines.retry(0);
    display.showWidget();
  }

  @Override
  public IsWidget getDisplay()
  {
    return display;
  }

  public void refreshScopeUnitList(List<ScopeUnitLightDTO> list)
  {
    display.getSelectionModelScopeUnit().clear();
    //default sort
    Collections.sort(list, display.getComparatorScopeUnit());
    display.getDataProviderScopeUnit().setList(list);
    display.updateSortHandler();
  }

  public void setScopeUnitDisciplineList(List<ScopeUnitDisciplineDTO> list)
  {
    getScopeUnits.retry(0);
    getDisciplines.retry(0);
    scopeUnitDisciplines = list;

  }

  /**
   * Create a list containing scope unit which still have not finished
   * discipline
   */
  private void createTerminatedScopeUnitMap()
  {
    isTerminatedScopeUnit = new HashMap<String, Set<DisciplineDTO>>();
    for (ScopeUnitDisciplineDTO scopeUnitDiscipline : scopeUnitDisciplines)
    {
      if (!ScopeUnitDisciplineStatusEnum.CLOSED.equals(scopeUnitDiscipline.getStatus()))
      {
        Set<DisciplineDTO> set;
        /* scope unit already referenced */
        if (isTerminatedScopeUnit.containsKey(scopeUnitDiscipline.getScopeUnit().getUnitId()))
        {
          set = isTerminatedScopeUnit.get(scopeUnitDiscipline.getScopeUnit().getUnitId());
        }
        else
        {
          set = new HashSet<DisciplineDTO>(disciplines);
        }
        set.remove(scopeUnitDiscipline.getDiscipline());
        isTerminatedScopeUnit.put(scopeUnitDiscipline.getScopeUnit().getUnitId(), set);
      }
      /* scope unit closed and discipline not referenced */
      else if (!isTerminatedScopeUnit.containsKey(scopeUnitDiscipline.getScopeUnit().getUnitId()))
      {
        isTerminatedScopeUnit.put(scopeUnitDiscipline.getScopeUnit().getUnitId(), null);
      }
    }
  }

}
