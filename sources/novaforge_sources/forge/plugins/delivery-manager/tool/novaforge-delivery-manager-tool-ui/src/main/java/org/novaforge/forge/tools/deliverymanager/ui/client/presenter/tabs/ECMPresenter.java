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

package org.novaforge.forge.tools.deliverymanager.ui.client.presenter.tabs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.ECMView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentTypeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NodeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.DeliveryManagementServiceException;
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.ExceptionCode;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;

import java.util.List;

/**
 * @author caseryj
 */
public class ECMPresenter implements TabPresenter
{

   private final ECMView display;
   private String        deliveryReference;

   public ECMPresenter(final ECMView display)
   {
      super();
      this.display = display;
      this.bind();
   }

   /**
    * This method is the interface between the presenter and the view
    */
   private void bind()
   {
      this.display.getButtonSaveDocumentsDelivery().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent event)
         {
            ECMPresenter.this.saveSelectedNodes();
         }
      });
      this.display.getCancelValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent event)
         {
            ECMPresenter.this.refreshView(ECMPresenter.this.deliveryReference);
            ECMPresenter.this.display.getCancelValidateDialogBox().hide();
         }
      });
   }

   private void saveSelectedNodes()
   {
      new AbstractRPCCall<Boolean>()
      {
         @Override
         protected void callService(final AsyncCallback<Boolean> pCb)
         {
            DeliveryManagement
                  .get()
                  .getServiceAsync()
                  .updateDocuments(DeliveryManagement.get().getProjectId(),
                        ECMPresenter.this.deliveryReference,
                        ECMPresenter.this.display.getFilesSelectionTrees().getRootSelectedNodes(), pCb);
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            DeliveryCommon.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(final Boolean pResult)
         {
            ECMPresenter.this.display.getSaveResultDialogBox().show();
         }



      }.retry(0);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void refreshView(final String pDelivery)
   {
      this.deliveryReference = pDelivery;
      this.display.getFilesSelectionTrees().clearCurrentSelection();
      this.display.getFilesSelectionTrees().setAvailableTreeLoading(true);
      this.display.getFilesSelectionTrees().setSelectedTreeLoading(true);
      this.getAvailablesDocuments();
      this.getSelectedDocuments(this.deliveryReference);
   }

   private void getAvailablesDocuments()
   {
      new AbstractRPCCall<List<NodeDTO>>()
      {
         @Override
         protected void callService(final AsyncCallback<List<NodeDTO>> pCb)
         {
            DeliveryManagement.get().getServiceAsync()
                  .getAvailablesDocuments(DeliveryManagement.get().getProjectId(), pCb);
         }

         @Override
         public void onSuccess(final List<NodeDTO> pResult)
         {
            ECMPresenter.this.display.getFilesSelectionTrees().setAvailableNodes(pResult);

            ECMPresenter.this.display.getFilesSelectionTrees().setAvailableTreeLoading(false);
            if (!ECMPresenter.this.display.getFilesSelectionTrees().checkIfSelectedNodeStillExist())
            {
               ECMPresenter.this.display.getFilesSelectionTrees().clearCurrentSelection();
               ECMPresenter.this.display.getFilesSelectionTrees().expandAllSelectedNodes();
               ECMPresenter.this.display.getNodeUnexistDialogBox().show();
            }
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            if ((caught instanceof DeliveryManagementServiceException)
                  && (((DeliveryManagementServiceException) caught).getCode() != null))
            {
               final ExceptionCode code = ((DeliveryManagementServiceException) caught).getCode();
               if (code.equals(ExceptionCode.ASSOCIATION_DO_NOT_EXIST))
               {
                  final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages()
                        .ecmTreeUnavailable(), InfoTypeEnum.ERROR);
                  info.show();
                  ECMPresenter.this.display.getFilesSelectionTrees().setAvailableErrorLoading(true);
               }
               else
               {
                  final InfoDialogBox info = new InfoDialogBox(code.getLocalizedMessage(),
                        InfoTypeEnum.WARNING);
                  info.show();
               }
            }
            else
            {
               final InfoDialogBox info = new InfoDialogBox(
                     ExceptionCode.TECHNICAL_ERROR.getLocalizedMessage(), InfoTypeEnum.WARNING);
               info.show();
            }

         }

      }.retry(0);
   }

   private void getSelectedDocuments(final String pReferenceId)
   {
      new AbstractRPCCall<ContentDTO>()
      {
         @Override
         protected void callService(final AsyncCallback<ContentDTO> pCb)
         {
            DeliveryManagement.get().getServiceAsync()
                  .getContent(DeliveryManagement.get().getProjectId(), pReferenceId, ContentTypeDTO.ECM, pCb);
         }

         @Override
         public void onSuccess(final ContentDTO result)
         {
            if (result.getRoot() != null)
            {
               ECMPresenter.this.display.getFilesSelectionTrees().setSelectedNodes(result.getRoot());
               ECMPresenter.this.display.getFilesSelectionTrees().setSelectedTreeLoading(false);
            }
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            DeliveryCommon.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }

}