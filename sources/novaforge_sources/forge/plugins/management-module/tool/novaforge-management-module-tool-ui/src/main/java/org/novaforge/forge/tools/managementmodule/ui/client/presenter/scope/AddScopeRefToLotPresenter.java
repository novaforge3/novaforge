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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.event.scope.ScopeUnitEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.service.scope.ScopeServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.AddScopeRefToLotView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.AddScopeRefToLotViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vvigo
 */
public class AddScopeRefToLotPresenter implements Presenter {

   private final ScopeServiceAsync scopeService;
   private final SimpleEventBus eventBus;
   private final AddScopeRefToLotView display;

   private final Long projectPlanId;
   private final ScopeUnitDTO curentScope;
   private Map<String, Long> lotMap;

   public AddScopeRefToLotPresenter(final ScopeServiceAsync scopeService,
         final SimpleEventBus eventBus, final Long pProjectPlanId,
         final ScopeUnitDTO curentScope) {
      super();
      this.scopeService = scopeService;
      this.eventBus = eventBus;
      this.display = new AddScopeRefToLotViewImpl();
      this.curentScope = curentScope;

      projectPlanId = pProjectPlanId;

      bind();
   }

   public void bind() {
      AddScopeRefToLotPresenter.this.display.getButtonSave().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            if (display.getScopeLotLB().getSelectedIndex() > 0) {
               getLot();
               importScopeFromRefScope();
            }
         }
      });
      AddScopeRefToLotPresenter.this.display.getButtonCancel().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            cancelCreateScope();
         }
      });
   }

   private void getLot() {
      String completeLotName = display.getScopeLotLB().getItemText(
            display.getScopeLotLB().getSelectedIndex());
      curentScope.setLotName(completeLotName);
      if (lotMap.get(completeLotName) != null) {
         curentScope.setLotId(lotMap.get(completeLotName).toString());
      }
   }

   /**
    * Generate
    */
   private void importScopeFromRefScope() {

      new AbstractManagementRPCCall<ScopeUnitDTO>()
      {
         @Override
         protected void callService(AsyncCallback<ScopeUnitDTO> pCb) {
            AddScopeRefToLotPresenter.this.scopeService.createScopeUnitFromRef(curentScope,
                  Long.valueOf(curentScope.getLotId()), pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(ScopeUnitDTO pResult) {
            if (pResult != null) {
               eventBus.fireEvent(new ScopeUnitEvent());
               AddScopeRefToLotPresenter.this.display.closeWidget();
            }
         }


      }.retry(0);

   }

   private void cancelCreateScope() {
      AddScopeRefToLotPresenter.this.display.closeWidget();
   }

   @Override
   public void go(final HasWidgets container)
   {
      refreshLots(projectPlanId);
      AddScopeRefToLotPresenter.this.display.showWidget();
   }

   @Override
   public IsWidget getDisplay() {
      return this.display.asWidget();
   }

   private void refreshLots(final Long projectPlanId) {
      new AbstractManagementRPCCall<List<LotDTO>>()
      {
         @Override
         protected void callService(AsyncCallback<List<LotDTO>> pCb) {
            AddScopeRefToLotPresenter.this.scopeService.getLotList(projectPlanId, pCb);
         }

         @Override
         public void onSuccess(List<LotDTO> pResult) {
            if (pResult != null) {
               Date current = new Date();
               Date last = null;
               lotMap = new HashMap<String, Long>();
               display.getScopeLotLB().addItem("");

               int selected = 0;
               int increment = 0;
               for (LotDTO itLotDTO : pResult) {
                  String lotName = "";
                  if (itLotDTO.getParentLotName() != null) {
                     lotName = itLotDTO.getParentLotName() + " - ";
                  }
                  lotName = lotName + itLotDTO.getName();
                  display.getScopeLotLB().addItem(lotName);
                  
                  if (last == null 
                        || (current.before(itLotDTO.getEndDate()) 
                              && current.after(itLotDTO.getStartDate()) 
                              && last.before(itLotDTO.getStartDate())))
                  {
                     last = itLotDTO.getStartDate();
                     selected = increment;
                  }
                  increment++;
                  lotMap.put(lotName, itLotDTO.getLotId());
               }
               display.getScopeLotLB().setSelectedIndex(selected);
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

}
