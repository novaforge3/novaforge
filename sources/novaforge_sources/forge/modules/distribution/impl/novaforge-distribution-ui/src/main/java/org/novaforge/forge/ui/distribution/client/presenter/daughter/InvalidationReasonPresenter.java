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

package org.novaforge.forge.ui.distribution.client.presenter.daughter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.validation.Validator;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.event.CloseInvalidationZoneEvent;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.service.DistributionServiceAsync;
import org.novaforge.forge.ui.distribution.client.view.daughters.InvalidationReasonView;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeRequestDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;

/**
 * @author BILET-JC
 */
public class InvalidationReasonPresenter implements Presenter
{
   private final DistributionServiceAsync rpcService;
   private final SimpleEventBus           eventBus;
   private final InvalidationReasonView   display;
   private final ForgeRequestDTO          forgeRequest;

   /**
    * @param pRpcService
    * @param pEventBus
    * @param pDisplay
    */
   public InvalidationReasonPresenter(DistributionServiceAsync pRpcService, SimpleEventBus pEventBus,
         InvalidationReasonView pDisplay, ForgeRequestDTO pForgeRequestDTO)
   {
      super();
      this.rpcService = pRpcService;
      this.eventBus = pEventBus;
      this.display = pDisplay;
      this.forgeRequest = pForgeRequestDTO;
      this.display.getInvalidationReasonTA().ensureDebugId(
            "invalidationReasonId-" + forgeRequest.getForgeRequestId());
      bind();
   }

   public void bind()
   {
      // cancel invalidation button
      display.getCancelInvalidationReasonB().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            eventBus.fireEvent(new CloseInvalidationZoneEvent());
         }
      });
      // create invalidation button
      display.getCreateInvalidationReasonB().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(ClickEvent event)
         {
            disapprouve(forgeRequest.getType(), forgeRequest.getForgeRequestId(),
                  display
                  .getInvalidationReasonTA().getValue());

         }
      });
      // reason conditions
      display.getInvalidationReasonTA().setValidator(new Validator()
      {

         @Override
         public boolean isValid(String pValue)
         {
            boolean ret = false;
            if (pValue.length() < 256)
            {
               ret = true;
            }
            return ret;
         }

         @Override
         public String getErrorMessage()
         {
            return Common.MESSAGES.eMaxLenght();
         }
      });
   }

   /**
    * Invalidate a subscription or unsubscription demand
    * 
    * @param pType
    * @param pForgeRequestId
    * @param pReason
    */
   private void disapprouve(final TypeDistributionRequestEnum pType, final String pForgeRequestId,
         final String pReason)
   {
      new AbstractDistributionRPCCall<Void>()
      {

         @Override
         protected void callService(AsyncCallback<Void> cb)
         {
            rpcService.disapproveDistributionRequest(pType, pForgeRequestId, pReason, cb);
         }

         @Override
         public void onFailure(Throwable pCaught)
         {
            eventBus.fireEvent(new CloseInvalidationZoneEvent());
            Common.displayErrorMessage(pCaught);
         }

         @Override
         public void onSuccess(Void arg0)
         {
            InfoDialogBox info = new InfoDialogBox(Common.MESSAGES.invalidateSuccessful(), InfoTypeEnum.SUCCESS);
            info.show();
            eventBus.fireEvent(new CloseInvalidationZoneEvent());
         }
      }.retry(0);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void go(HasWidgets container)
   {
      container.clear();
      container.add(display.asWidget());

   }
   public HasClickHandlers getCreateInvalidationReasonB()
   {
      return display.getCreateInvalidationReasonB();

   }
   private Validator getValidReasonTextValidator()
   {
      // Validator for needed fields
      return new Validator()
      {
         @Override
         public boolean isValid(String pValue)
         {
            boolean ret = false;
            if (pValue.length() < 256)
            {
               ret = true;
            }
            return ret;
         }

         @Override
         public String getErrorMessage()
         {
            return Common.MESSAGES.eMaxLenght();
         }
      };}
}
