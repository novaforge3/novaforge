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

package org.novaforge.forge.tools.managementmodule.ui.client.presenter.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.novaforge.forge.tools.managementmodule.ui.client.event.scope.ScopeUnitEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.scope.SelectScopeUnitEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.service.scope.ScopeServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.ScopeListView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.ScopeListViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * @author vvigo
 */
public class ScopeListPresenter implements Presenter {

   private final ScopeServiceAsync scopeService;
   private final SimpleEventBus    eventBus;
   private final ScopeListView  display;

   private final Long              projectPlanId;
   private List<ScopeUnitDTO>      lotScopeUnitList;

	public ScopeListPresenter(final ScopeServiceAsync scopeService, final SimpleEventBus eventBus,
			final Long pProjectPlanId) {
      super();
      this.scopeService = scopeService;
      this.eventBus = eventBus;
      this.display = new ScopeListViewImpl();

      projectPlanId = pProjectPlanId;

      getManualList();
      getScopeTypeList();

      bind();
   }

  private void getManualList()
  {
    display.getScopeManualSearchTB().clear();
    display.getScopeManualSearchTB().addItem("");
    display.getScopeManualSearchTB().addItem(Common.MESSAGES_SCOPE.isManualTrue());
    display.getScopeManualSearchTB().addItem(Common.MESSAGES_SCOPE.isManualFalse());

   }

  private void getScopeTypeList()
  {
    new AbstractManagementRPCCall<List<String>>()
    {
      @Override
      protected void callService(AsyncCallback<List<String>> pCb)
      {
        ScopeListPresenter.this.scopeService.getScopeTypeList(pCb);
      }

      @Override
      public void onFailure(Throwable caught)
      {
        ErrorManagement.displayErrorMessage(caught);
      }

      @Override
      public void onSuccess(List<String> pResult)
      {
        if (pResult != null)
        {
          setScopeTypeList(pResult);
        }
      }


    }.retry(0);
  }

	public void bind() {
      bindSearchEvent();

		this.eventBus.addHandler(ScopeUnitEvent.TYPE, new ScopeUnitEvent.Handler() {

         @Override
			public void editScopeUnit(final ScopeUnitEvent pEvent) {
            refreshScopeLots(projectPlanId);
         }

      });

		this.display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

         @Override
			public void onSelectionChange(final SelectionChangeEvent pEvent) {
            ScopeUnitDTO currentScope = ScopeListPresenter.this.display.getSelectionModel()
            .getSelectedObject();
            eventBus.fireEvent(new SelectScopeUnitEvent(currentScope));
         }

      });
   }

  public void setScopeTypeList(List<String> types)
  {
    display.getScopeTypeSearchTB().clear();
    display.getScopeTypeSearchTB().addItem("");
    for (String itType : types)
    {
      display.getScopeTypeSearchTB().addItem(itType);
    }
  }

	protected void bindSearchEvent() {
		display.getScopeLotSearchTB().addKeyUpHandler(new KeyUpHandler() {
         @Override
			public void onKeyUp(KeyUpEvent event) {
				filteredList(
						display.getScopeLotSearchTB().getValue(),
                  display.getScopeNameSearchTB().getValue(),
						display.getScopeTypeSearchTB().getItemText(
								display.getScopeTypeSearchTB().getSelectedIndex()),
						display.getScopeManualSearchTB().getItemText(
								display.getScopeManualSearchTB().getSelectedIndex()));
         }
      });
		display.getScopeLotSearchTB().addValueChangeHandler(new ValueChangeHandler<String>() {
         @Override
			public void onValueChange(ValueChangeEvent<String> pEvent) {
            filteredList(
                  display.getScopeLotSearchTB().getValue(),
                  display.getScopeNameSearchTB().getValue(),
                  display.getScopeTypeSearchTB().getItemText(
                        display.getScopeTypeSearchTB().getSelectedIndex()),
						display.getScopeManualSearchTB().getItemText(
								display.getScopeManualSearchTB().getSelectedIndex()));

         }
            });

		display.getScopeNameSearchTB().addKeyUpHandler(new KeyUpHandler() {
         @Override
			public void onKeyUp(KeyUpEvent event) {
            filteredList(
                  display.getScopeLotSearchTB().getValue(),
                  display.getScopeNameSearchTB().getValue(),
                  display.getScopeTypeSearchTB().getItemText(
                        display.getScopeTypeSearchTB().getSelectedIndex()),
						display.getScopeManualSearchTB().getItemText(
                              display.getScopeManualSearchTB().getSelectedIndex()));
         }
      });
		display.getScopeNameSearchTB().addValueChangeHandler(new ValueChangeHandler<String>() {
         @Override
			public void onValueChange(ValueChangeEvent<String> pEvent) {
            filteredList(
                  display.getScopeLotSearchTB().getValue(),
                  display.getScopeNameSearchTB().getValue(),
                  display.getScopeTypeSearchTB().getItemText(
                        display.getScopeTypeSearchTB().getSelectedIndex()),
                        display.getScopeManualSearchTB().getItemText(
                              display.getScopeManualSearchTB().getSelectedIndex()));

         }
            });

		display.getScopeTypeSearchTB().addKeyUpHandler(new KeyUpHandler() {
         @Override
			public void onKeyUp(KeyUpEvent event) {
            filteredList(
                  display.getScopeLotSearchTB().getValue(),
                  display.getScopeNameSearchTB().getValue(),
                  display.getScopeTypeSearchTB().getItemText(
                        display.getScopeTypeSearchTB().getSelectedIndex()),
                        display.getScopeManualSearchTB().getItemText(
                              display.getScopeManualSearchTB().getSelectedIndex()));
         }
      });
		display.getScopeTypeSearchTB().addChangeHandler(new ChangeHandler() {
         @Override
			public void onChange(ChangeEvent event) {
            filteredList(
                  display.getScopeLotSearchTB().getValue(),
                  display.getScopeNameSearchTB().getValue(),
                  display.getScopeTypeSearchTB().getItemText(
                        display.getScopeTypeSearchTB().getSelectedIndex()),
                        display.getScopeManualSearchTB().getItemText(
                              display.getScopeManualSearchTB().getSelectedIndex()));
         }

      });
      display.getScopeManualSearchTB().addChangeHandler(new ChangeHandler() {

         @Override
         public void onChange(ChangeEvent event) {
            filteredList(
                  display.getScopeLotSearchTB().getValue(),
                  display.getScopeNameSearchTB().getValue(),
                  display.getScopeTypeSearchTB().getItemText(
                        display.getScopeTypeSearchTB().getSelectedIndex()),
                        display.getScopeManualSearchTB().getItemText(
                              display.getScopeManualSearchTB().getSelectedIndex()));
         }
      });
   }

   /**
    * reload list elements
    *
    * @param projectPlanId
    */
	public void refreshScopeLots(final Long projectPlanId) {
      lotScopeUnitList = new ArrayList<ScopeUnitDTO>();
    new AbstractManagementRPCCall<List<ScopeUnitDTO>>()
    {
         @Override
			protected void callService(AsyncCallback<List<ScopeUnitDTO>> pCb) {
				ScopeListPresenter.this.scopeService.getProjectPlanScope(projectPlanId, null, true, true,
						pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
           ErrorManagement.displayErrorMessage(caught);
         }

      @Override
      public void onSuccess(List<ScopeUnitDTO> pResult) {
				if (pResult != null) {
               lotScopeUnitList.addAll(pResult);
               refreshScopeList(lotScopeUnitList);
               
             if (display.getScopeNameSearchTB() != null && display.getScopeNameSearchTB().getValue() != null || 
               display.getScopeLotSearchTB() != null && display.getScopeLotSearchTB().getValue()  != null || 
               display.getScopeTypeSearchTB() != null && display.getScopeTypeSearchTB().getSelectedValue()  != null || 
               display.getScopeManualSearchTB() != null && display.getScopeManualSearchTB().getSelectedValue()  != null )
             { 
                 display.applyFiltering();
             }
         }
      }


      }.retry(0);
   }

  public void filteredList(final String pLotName, final String pUPName, final String pType, final String pIsManual)
  {
    Boolean isManual = null;

    if (Common.MESSAGES_SCOPE.isManualTrue().equalsIgnoreCase(pIsManual))
    {
      isManual = Boolean.TRUE;
    }
    else if (Common.MESSAGES_SCOPE.isManualFalse().equalsIgnoreCase(pIsManual))
    {
      isManual = Boolean.FALSE;
    }

    final Collection<ScopeUnitDTO> filter = filteredOnScope(lotScopeUnitList, pLotName, pUPName, pType, isManual);
    refreshScopeList(new ArrayList<ScopeUnitDTO>(filter));
   }

  private void refreshScopeList(List<ScopeUnitDTO> pList)
  {
    // default sort
    Collections.sort(pList, display.getScopeUnitComparator());

    display.getLotScopeDataProvider().setList(pList);
    display.scopeLotListSortHandler();
   }

   private Collection<ScopeUnitDTO> filteredOnScope(final List<ScopeUnitDTO> pScopeLotList,
			final String pLotName, final String pUPName, final String pType, final Boolean pIsManual) {
     return Collections2.filter(pScopeLotList, new Predicate<ScopeUnitDTO>()
     {
       @Override
       public boolean apply(ScopeUnitDTO pInput)
       {
         if (!pInput.getLotName().contains(pLotName))
         {
           return false;
         }
         if (!pInput.getName().contains(pUPName))
         {
           return false;
         }
         if (!pInput.getType().contains(pType))
         {
           return false;
         }
         if (pIsManual != null)
         {
           if (!(pIsManual.equals(pInput.isManual())))
           {
             return false;
           }
         }
         return true;
       }
     });
         }

  @Override
  public void go(final HasWidgets container)
  {
    container.clear();
    container.add(this.display.asWidget());
    refreshScopeLots(projectPlanId); 
  }

  @Override
  public IsWidget getDisplay()
  {
    return this.display.asWidget();
   }
}
