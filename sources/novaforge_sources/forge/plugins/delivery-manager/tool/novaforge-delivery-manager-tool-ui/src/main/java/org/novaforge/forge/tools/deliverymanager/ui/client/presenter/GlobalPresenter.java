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
package org.novaforge.forge.tools.deliverymanager.ui.client.presenter;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.CreateDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.CreateTemplateReportEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.EditDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.EditTemplateReportEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.GenerateDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ManageTemplateReportEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.PreviewGenerateDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ShowDeliveriesEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.TabPresenterSource;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.templates.TemplateReportListPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.templates.TemplateReportManagePresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.DeliveryGenerateViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.DeliveryListViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.DeliveryTabViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.templates.TemplateReportListViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.templates.TemplateReportManageViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateDTO;

public class GlobalPresenter
{
   private final DeliveryListPresenter         deliveryListPresenter;
   private final DeliveryTabPresenter          deliveryTabPresenter;
   private final TemplateReportListPresenter   templateReportListPresenter;
   private final TemplateReportManagePresenter createTemplateReportPresenter;
   private final TemplateReportManagePresenter editTemplateReportPresenter;
   private final DeliveryGeneratePresenter     deliveryGeneratePresenter;

   public GlobalPresenter()
   {
      super();

      this.deliveryListPresenter = new DeliveryListPresenter(new DeliveryListViewImpl());
      this.deliveryTabPresenter = new DeliveryTabPresenter(new DeliveryTabViewImpl());
      this.templateReportListPresenter = new TemplateReportListPresenter(new TemplateReportListViewImpl());
      this.createTemplateReportPresenter = new TemplateReportManagePresenter(
            new TemplateReportManageViewImpl(), ManagePresenterType.CREATE);
      this.editTemplateReportPresenter = new TemplateReportManagePresenter(
            new TemplateReportManageViewImpl(), ManagePresenterType.EDIT);
      this.deliveryGeneratePresenter = new DeliveryGeneratePresenter(new DeliveryGenerateViewImpl());

      this.bind();
   }

   public void bind()
   {
      DeliveryManagement.get().getEventBus()
            .addHandler(CreateDeliveryEvent.TYPE, new CreateDeliveryEvent.Handler()
            {

               @Override
               public void displayCreateDelivery(final CreateDeliveryEvent pEvent)
               {
                  GlobalPresenter.this.deliveryTabPresenter.refreshView(null, TabPresenterSource.LIST);
                  GlobalPresenter.this.deliveryTabPresenter.go(RootLayoutPanel.get());

               }

            });
      DeliveryManagement.get().getEventBus()
            .addHandler(EditDeliveryEvent.TYPE, new EditDeliveryEvent.Handler()
            {

               @Override
               public void editDelivery(final EditDeliveryEvent pEvent)
               {
                  GlobalPresenter.this.deliveryTabPresenter.refreshView(pEvent.getDeliveryReference(),
                        pEvent.getSource());
                  GlobalPresenter.this.deliveryTabPresenter.go(RootLayoutPanel.get());
               }
            });
      DeliveryManagement.get().getEventBus()
            .addHandler(ShowDeliveriesEvent.TYPE, new ShowDeliveriesEvent.Handler()
            {

               @Override
               public void showDeliveries(final ShowDeliveriesEvent pEvent)
               {
                  GlobalPresenter.this.deliveryListPresenter.go(RootLayoutPanel.get());

               }

            });
      DeliveryManagement.get().getEventBus()
            .addHandler(PreviewGenerateDeliveryEvent.TYPE, new PreviewGenerateDeliveryEvent.Handler()
            {

               @Override
               public void onPreviewGenerateDelivery(final PreviewGenerateDeliveryEvent pEvent)
               {

                  GlobalPresenter.this.deliveryGeneratePresenter.refreshView(pEvent.getReference());
                  GlobalPresenter.this.deliveryGeneratePresenter.go(RootLayoutPanel.get());

               }
            });
      DeliveryManagement.get().getEventBus()
            .addHandler(GenerateDeliveryEvent.TYPE, new GenerateDeliveryEvent.Handler()
            {

               @Override
               public void onGenerateDeliveryEvent(final GenerateDeliveryEvent pEvent)
               {
                  GlobalPresenter.this.deliveryListPresenter.showDeliveryGeneratedPopup(pEvent.getSuccess());
                  GlobalPresenter.this.deliveryListPresenter.refreshView();
               }
            });
      DeliveryManagement.get().getEventBus()
            .addHandler(ManageTemplateReportEvent.TYPE, new ManageTemplateReportEvent.Handler()
            {

               @Override
               public void displayManageTemplateReport(final ManageTemplateReportEvent pEvent)
               {
                  GlobalPresenter.this.templateReportListPresenter.go(RootLayoutPanel.get());

               }

            });
      DeliveryManagement.get().getEventBus()
            .addHandler(CreateTemplateReportEvent.TYPE, new CreateTemplateReportEvent.Handler()
            {

               @Override
               public void displayCreateTemplateReport(final CreateTemplateReportEvent pEvent)
               {
                  GlobalPresenter.this.createTemplateReportPresenter.refreshView(new TemplateDTO());
                  GlobalPresenter.this.createTemplateReportPresenter.go(RootLayoutPanel.get());

               }

            });
      DeliveryManagement.get().getEventBus()
            .addHandler(EditTemplateReportEvent.TYPE, new EditTemplateReportEvent.Handler()
            {

               @Override
               public void displayEditTemplateReport(final EditTemplateReportEvent pEvent)
               {
                  GlobalPresenter.this.editTemplateReportPresenter.refreshView(pEvent.getTemplate());
                  GlobalPresenter.this.editTemplateReportPresenter.go(RootLayoutPanel.get());

               }

            });
   }

   public void go()
   {
      this.deliveryListPresenter.go(RootLayoutPanel.get());
   }
}
