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
package org.novaforge.forge.tools.deliverymanager.ui.client.presenter.templates;

import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.DownloadFrame;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.CreateTemplateReportEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.EditTemplateReportEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ShowDeliveriesEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.templates.TemplateReportListView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateDTO;
import org.novaforge.forge.ui.commons.client.cells.ClickableImageResourceHasCell;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class TemplateReportListPresenter implements Presenter
{

   private final TemplateReportListView display;
   private List<TemplateDTO>            deliveryTemplateNotesList;

   /**
    * @param display
    */
   public TemplateReportListPresenter(final TemplateReportListView display)
   {
      super();
      this.display = display;

      this.initActionColumn();

      this.bind();
   }

   private void initActionColumn()
   {
      final List<HasCell<TemplateDTO, ?>> cells = new LinkedList<HasCell<TemplateDTO, ?>>();
      cells.add(new ClickableImageResourceHasCell<TemplateDTO>(DeliveryCommon.getResources().edit(),
            DeliveryCommon.getMessages().editNoteTemplate(), new FieldUpdater<TemplateDTO, ImageResource>()
            {

         @Override
         public void update(final int pIndex, final TemplateDTO pObject, final ImageResource pValue)
         {
            DeliveryManagement.get().getEventBus().fireEvent(new EditTemplateReportEvent(pObject));

         }
            }));
      cells.add(new ClickableImageResourceHasCell<TemplateDTO>(DeliveryCommon.getResources().koSmall(),
            DeliveryCommon.getMessages().deleteNoteTemplate(), new FieldUpdater<TemplateDTO, ImageResource>()
            {

         @Override
         public void update(final int pIndex, final TemplateDTO pObject, final ImageResource pValue)
         {
            TemplateReportListPresenter.this.deleteTemplate(pObject);

         }
            }));
      final CompositeCell<TemplateDTO> actions = new CompositeCell<TemplateDTO>(cells);
      final Column<TemplateDTO, TemplateDTO> actionsColumn = new Column<TemplateDTO, TemplateDTO>(actions)
            {
         @Override
         public TemplateDTO getValue(final TemplateDTO object)
         {
            return object;
         }
            };
            this.display.getDeliveryTemplateNotesTable().addColumn(actionsColumn,
                  DeliveryCommon.getMessages().actions());
            this.display.getDeliveryTemplateNotesTable().setColumnWidth(actionsColumn, 110, Unit.PX);
   }

   /**
    * This method is the interface between the presenter and the view
    */
   private void bind()
   {
      this.display.getButtonCreateDeliveryTemplateNote().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent event)
         {
            DeliveryManagement.get().getEventBus().fireEvent(new CreateTemplateReportEvent());
         }
      });
      this.display.getDownloadNoteSampleFile().addClickHandler(new ClickHandler()
      {
         public void onClick(ClickEvent event)
         {
            String downloadUrl = GWT.getModuleBaseURL() + "download?templatesample=true";

            new DownloadFrame(downloadUrl);
         }
      });
      this.display.getButtonManageDelivery().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent event)
         {
            DeliveryManagement.get().getEventBus().fireEvent(new ShowDeliveriesEvent());
         }
      });

   }

   private void deleteTemplate(final TemplateDTO pTemplate)
   {
      new AbstractRPCCall<Boolean>()
      {
         @Override
         protected void callService(final AsyncCallback<Boolean> pCb)
         {
            DeliveryManagement.get().getServiceAsync().deleteTemplateReport(DeliveryManagement.get().getProjectId(),
                                                                            pTemplate.getName(), pCb);
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            DeliveryCommon.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(final Boolean pResult)
         {
            if (pResult)
            {
               TemplateReportListPresenter.this.fillTemplateReportList();
            }
         }

      }.retry(0);

   }

   private void fillTemplateReportList()
   {
      this.deliveryTemplateNotesList = new ArrayList<TemplateDTO>();
      new AbstractRPCCall<List<TemplateDTO>>()
      {
         @Override
         protected void callService(final AsyncCallback<List<TemplateDTO>> pCb)
         {
            DeliveryManagement.get().getServiceAsync().getTemplateReportList(DeliveryManagement.get().getProjectId(),
                                                                             pCb);
         }

         @Override
         public void onFailure(final Throwable caught)
         {
         }

         @Override
         public void onSuccess(final List<TemplateDTO> pResult)
         {
            if (pResult != null)
            {
               TemplateReportListPresenter.this.deliveryTemplateNotesList.clear();
               TemplateReportListPresenter.this.deliveryTemplateNotesList.addAll(pResult);
               TemplateReportListPresenter.this
                   .refreshDeliveryNotesList(TemplateReportListPresenter.this.deliveryTemplateNotesList);
            }
         }


      }.retry(0);
   }

   private void refreshDeliveryNotesList(final List<TemplateDTO> pList)
   {
      this.display.getDeliveryTemplateNotesDataProvider().setList(pList);
      this.display.getDeliveryTemplateNotesTable().redraw();
   }

   /**
    * @inheritDoc
    */
   @Override
   public void go(final HasWidgets container)
   {
      container.clear();
      container.add(this.display.asWidget());
      this.fillTemplateReportList();
   }
}
