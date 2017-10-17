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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.PreviewGenerateDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ReloadDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ShowDeliveriesEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.TabPresenterSource;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.tabs.BinariesPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.tabs.BugsPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.tabs.DeliveryDetailsPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.tabs.ECMPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.tabs.NotePresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.tabs.SCMPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.DeliveryTabView;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.BinariesViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.BugsViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.DeliveryDetailsViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.ECMViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.NoteViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.SCMViewImpl;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentTypeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * presenter of the DeliveryTabView
 * 
 * @author FALSQUELLE-E
 */
public class DeliveryTabPresenter implements Presenter
{

   private final DeliveryTabView          display;
   private final DeliveryDetailsPresenter deliveryDetailsPresenter;
   private final ECMPresenter             gedPresenter;
   private final SCMPresenter             scmPresenter;
   private final BinariesPresenter        binariesPresenter;
   private final BugsPresenter            bugsPresenter;
   private final NotePresenter            notePresenter;
   private final List<ContentTypeDTO>     tabContents = new LinkedList<ContentTypeDTO>();
   private String             deliveryReference;
   private TabPresenterSource source;

   public DeliveryTabPresenter(final DeliveryTabView display)
   {
      super();

      this.display = display;

      this.deliveryDetailsPresenter = new DeliveryDetailsPresenter(new DeliveryDetailsViewImpl());
      this.gedPresenter = new ECMPresenter(new ECMViewImpl());
      this.scmPresenter = new SCMPresenter(new SCMViewImpl());
      this.binariesPresenter = new BinariesPresenter(new BinariesViewImpl());
      this.bugsPresenter = new BugsPresenter(new BugsViewImpl());
      this.notePresenter = new NotePresenter(new NoteViewImpl());
      this.bind();
   }

   public void bind()
   {
      DeliveryManagement.get().getEventBus()
      .addHandler(ReloadDeliveryEvent.TYPE, new ReloadDeliveryEvent.Handler()
      {

         @Override
         public void onReloadDelivery(final ReloadDeliveryEvent event)
         {
            DeliveryTabPresenter.this.refreshView(event.getDeliveryId(),
                  DeliveryTabPresenter.this.source);
            DeliveryTabPresenter.this.go(RootLayoutPanel.get());
            DeliveryTabPresenter.this.display.getTabPanel().selectTab(0);
         }
      });
      this.display.getReturnValidation().getValidate().addClickHandler(new ClickHandler()
      {
         public void onClick(ClickEvent pEvent)
         {

            switch (DeliveryTabPresenter.this.source)
            {
            case GENERATE:
               DeliveryManagement
               .get()
               .getEventBus()
               .fireEvent(new PreviewGenerateDeliveryEvent(DeliveryTabPresenter.this.deliveryReference));
               break;
            case LIST:
               DeliveryManagement.get().getEventBus().fireEvent(new ShowDeliveriesEvent());
               break;
            default:
               DeliveryManagement.get().getEventBus().fireEvent(new ShowDeliveriesEvent());
               break;
            }
            DeliveryTabPresenter.this.display.getReturnValidation().hide();
         }
      });
      this.display.getReturnButton().addClickHandler(new ClickHandler()
      {
         public void onClick(ClickEvent pEvent)
         {
            DeliveryTabPresenter.this.display.getReturnValidation().show();
         }
      });
      this.display.getTabPanel().addSelectionHandler(new SelectionHandler<Integer>()
            {

         @Override
         public void onSelection(final SelectionEvent<Integer> pEvent)
         {
            final Integer selectedItem = pEvent.getSelectedItem();
            if (selectedItem == 0)
            {

               DeliveryTabPresenter.this.deliveryDetailsPresenter
               .refreshView(DeliveryTabPresenter.this.deliveryReference);
            }
            else
            {
               final ContentTypeDTO contentTypeDTO = DeliveryTabPresenter.this.tabContents
                     .get(selectedItem - 1);
               switch (contentTypeDTO)
               {
               case ECM:
                  DeliveryTabPresenter.this.gedPresenter
                  .refreshView(DeliveryTabPresenter.this.deliveryReference);
                  break;
               case SCM:
                  DeliveryTabPresenter.this.scmPresenter
                  .refreshView(DeliveryTabPresenter.this.deliveryReference);
                  break;
               case FILE:
                  DeliveryTabPresenter.this.binariesPresenter
                  .refreshView(DeliveryTabPresenter.this.deliveryReference);
                  break;
               case BUG:
                  DeliveryTabPresenter.this.bugsPresenter
                  .refreshView(DeliveryTabPresenter.this.deliveryReference);
                  break;
               case NOTE:
                  DeliveryTabPresenter.this.notePresenter
                  .refreshView(DeliveryTabPresenter.this.deliveryReference);
                  break;
               default:
                  DeliveryTabPresenter.this.display.getTabPanel().selectTab(0);
                  break;

               }
            }
         }
            });
   }

   public void refreshView(final String pDelivery, final TabPresenterSource pSource)
   {
      this.deliveryReference = pDelivery;
      this.source = pSource;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void go(final HasWidgets container)
   {
      container.clear();
      container.add(this.display.asWidget());

      switch (this.source)
      {

         case GENERATE:
            this.display.getReturnButton().setText(DeliveryCommon.getMessages().returnGeneration());
            break;
         case LIST:
            this.display.getReturnButton().setText(DeliveryCommon.getMessages().returnList());
            break;
         default:
            this.display.getReturnButton().setText(DeliveryCommon.getMessages().returnList());
            break;
      }
      this.display.getTabPanel().clear();
      this.display.getTabPanel().insert(this.deliveryDetailsPresenter.getDisplay(),
                                        DeliveryCommon.getMessages().headerGeneralTab(), 0);
      if ((this.deliveryReference != null) && (!"".equals(this.deliveryReference)))
      {
         this.refreshTabs();

      }

   }

   private void refreshTabs()
   {
      new AbstractRPCCall<DeliveryDTO>()
      {
         @Override
         protected void callService(final AsyncCallback<DeliveryDTO> pCb)
         {
            DeliveryManagement
            .get()
            .getServiceAsync()
            .getDelivery(DeliveryManagement.get().getProjectId(),
                  DeliveryTabPresenter.this.deliveryReference, pCb);
         }

         @Override
         public void onFailure(final Throwable caught)
         {

            DeliveryCommon.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(final DeliveryDTO pResult)
         {
            DeliveryTabPresenter.this.tabContents.clear();
            if ((pResult != null) && (pResult.getContents() != null))
            {
               final Set<Entry<ContentTypeDTO, String>> entrySet = pResult.getContents().entrySet();
               for (final Entry<ContentTypeDTO, String> entry : entrySet)
               {
                  int pos = DeliveryTabPresenter.this.display.getTabPanel().getWidgetCount();
                  switch (entry.getKey())
                  {
                  case ECM:
                     if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.SCM))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.SCM) + 1;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.FILE))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.FILE) + 1;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.NOTE))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.NOTE) + 1;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.BUG))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.BUG) + 1;
                     }
                     DeliveryTabPresenter.this.display.getTabPanel().insert(
                           DeliveryTabPresenter.this.gedPresenter.getDisplay(),
                           DeliveryCommon.getMessages().headerGEDTab(), pos);
                     break;
                  case SCM:
                     if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.ECM))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.ECM) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.FILE))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.FILE) + 1;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.NOTE))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.NOTE) + 1;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.BUG))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.BUG) + 1;
                     }
                     DeliveryTabPresenter.this.display.getTabPanel().insert(
                           DeliveryTabPresenter.this.scmPresenter.getDisplay(),
                           DeliveryCommon.getMessages().headerGCLTab(), pos);
                     break;
                  case FILE:
                     if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.SCM))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.SCM) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.ECM))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.ECM) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.NOTE))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.NOTE) + 1;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.BUG))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.BUG) + 1;
                     }
                     DeliveryTabPresenter.this.display.getTabPanel().insert(
                           DeliveryTabPresenter.this.binariesPresenter.getDisplay(),
                           DeliveryCommon.getMessages().headerBinariesTab(), pos);
                     break;
                  case NOTE:
                     if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.FILE))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.FILE) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.SCM))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.SCM) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.ECM))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.ECM) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.BUG))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.BUG) + 1;
                     }
                     DeliveryTabPresenter.this.display.getTabPanel().insert(
                           DeliveryTabPresenter.this.notePresenter.getDisplay(),
                           DeliveryCommon.getMessages().headerNoteTab(), pos);
                     break;
                  case BUG:

                     if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.NOTE))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.NOTE) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.FILE))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.FILE) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.SCM))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.SCM) + 2;
                     }
                     else if (DeliveryTabPresenter.this.tabContents.contains(ContentTypeDTO.ECM))
                     {
                        pos = DeliveryTabPresenter.this.tabContents.indexOf(ContentTypeDTO.ECM) + 2;
                     }

                     DeliveryTabPresenter.this.display.getTabPanel().insert(
                           DeliveryTabPresenter.this.bugsPresenter.getDisplay(),
                           DeliveryCommon.getMessages().headerBugsTab(), pos);
                     break;
                  default:
                     // Unsupported type element
                     break;

                  }
                  DeliveryTabPresenter.this.tabContents.add(pos - 1, entry.getKey());
               }

            }
         }
      }.retry(0);
   }

}