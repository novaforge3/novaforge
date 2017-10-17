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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.DownloadFrame;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.CreateDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.EditDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ManageTemplateReportEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.PreviewGenerateDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ShowDeliveriesEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.TabPresenterSource;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.DeliveryListView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryStatusDTO;
import org.novaforge.forge.ui.commons.client.cells.ClickableImageResourceHasCell;
import org.novaforge.forge.ui.commons.client.loading.LoadingPanel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author BILET-JC
 */
public class DeliveryListPresenter implements Presenter
{

  private final DeliveryListView display;
  private List<DeliveryDTO>      deliveryList;
  private DeliveryDTO            deliverySelected;

  /**
   * @param display
   */
  public DeliveryListPresenter(final DeliveryListView display)
  {
    super();
    this.display = display;
    this.deliverySelected = null;

    this.initActionPanel();
    this.initActionColumn();

    this.bind();
  }

  private void initActionPanel()
  {
    display.getActionsPanel().setVisible(DeliveryManagement.get().isCanEdit());
  }

  private void initActionColumn()
  {
    final List<HasCell<DeliveryDTO, ?>> cells = new LinkedList<HasCell<DeliveryDTO, ?>>();
    final boolean canEdit = DeliveryManagement.get().isCanEdit();
    if (canEdit)
    {
      cells.add(new ClickableImageResourceHasCell<DeliveryDTO>(DeliveryCommon.getResources().edit(),
          DeliveryCommon.getMessages().edit(), new FieldUpdater<DeliveryDTO, ImageResource>()
          {

            @Override
            public void update(final int pIndex, final DeliveryDTO pObject, final ImageResource pValue)
            {
              if (!DeliveryStatusDTO.DELIVERED.equals(pObject.getStatus()))
              {
                DeliveryManagement.get().getEventBus()
                    .fireEvent(new EditDeliveryEvent(pObject.getReference(), TabPresenterSource.LIST));
              }
            }
          })
      {
        @Override
        public final ImageResource getValue(final DeliveryDTO pObject)
        {
          if (DeliveryStatusDTO.GENERATING.equals(pObject.getStatus())
              || DeliveryStatusDTO.DELIVERED.equals(pObject.getStatus()))
          {
            return null;
          }
          else
          {
            return DeliveryCommon.getResources().edit();
          }
        }
      });
      cells.add(new ClickableImageResourceHasCell<DeliveryDTO>(DeliveryCommon.getResources().generate(),
          DeliveryCommon.getMessages().generate(), new FieldUpdater<DeliveryDTO, ImageResource>()
          {

            @Override
            public void update(final int pIndex, final DeliveryDTO pObject, final ImageResource pValue)
            {
              if (!DeliveryStatusDTO.DELIVERED.equals(pObject.getStatus()))
              {
                DeliveryManagement.get().getEventBus()
                    .fireEvent(new PreviewGenerateDeliveryEvent(pObject.getReference()));
              }
            }
          })
      {
        @Override
        public final ImageResource getValue(final DeliveryDTO pObject)
        {
          if (DeliveryStatusDTO.GENERATING.equals(pObject.getStatus())
              || DeliveryStatusDTO.DELIVERED.equals(pObject.getStatus()))
          {
            return null;
          }
          else
          {
            return DeliveryCommon.getResources().generate();
          }
        }
      });
    }

    // no edit permission is needed for downloading delivery
    cells.add(new ClickableImageResourceHasCell<DeliveryDTO>(DeliveryCommon.getMessages().download(),
        new FieldUpdater<DeliveryDTO, ImageResource>()
        {

          @Override
          public void update(final int pIndex, final DeliveryDTO pObject, final ImageResource pValue)
          {
            if (!DeliveryStatusDTO.CREATED.equals(pObject.getStatus()) && !DeliveryStatusDTO.MODIFIED.equals(pObject
                                                                                                                 .getStatus()))
            {
              // Building servlet url
              final String downloadUrl = GWT.getModuleBaseURL() + "download?project="
                  + DeliveryManagement.get().getProjectId() + "&reference=" + pObject.getReference();

              // Open download frame
              new DownloadFrame(downloadUrl);

            }

          }
        })
    {
      @Override
      public final ImageResource getValue(final DeliveryDTO pObject)
      {
        if (DeliveryStatusDTO.CREATED.equals(pObject.getStatus())
            || DeliveryStatusDTO.MODIFIED.equals(pObject.getStatus()))
        {
          // return DeliveryCommon.getResources().downloadGray();
          return null;
        }
        else if (DeliveryStatusDTO.GENERATING.equals(pObject.getStatus()))
        {
          return null;
        }
        else
        {
          return DeliveryCommon.getResources().download();
        }
      }
    });

    if (canEdit)
    {
      cells.add(new ClickableImageResourceHasCell<DeliveryDTO>(DeliveryCommon.getResources().lockDisabled(),
          DeliveryCommon.getMessages().lockDelivery(), new FieldUpdater<DeliveryDTO, ImageResource>()
          {

            @Override
            public void update(final int pIndex, final DeliveryDTO pObject, final ImageResource pValue)
            {
              if (DeliveryStatusDTO.GENERATED.equals(pObject.getStatus()) && (!DeliveryStatusDTO.DELIVERED
                                                                                   .equals(pObject.getStatus())))
              {
                DeliveryListPresenter.this.deliverySelected = pObject;
                DeliveryListPresenter.this.display.getLockDeliveryPopup().setModal(true);
                DeliveryListPresenter.this.display.getLockDeliveryPopup().show();
              }
            }
          })
      {
        @Override
        public final ImageResource getValue(final DeliveryDTO pObject)
        {
          if (DeliveryStatusDTO.GENERATED.equals(pObject.getStatus()))
          {
            return DeliveryCommon.getResources().lock();

          }
          else if (DeliveryStatusDTO.GENERATING.equals(pObject.getStatus())
              || DeliveryStatusDTO.DELIVERED.equals(pObject.getStatus()))
          {
            return null;
          }
          else
          {
            // return DeliveryCommon.getResources().lockDisabled();
            return null;
          }
        }
      });
      cells.add(new ClickableImageResourceHasCell<DeliveryDTO>(DeliveryCommon.getResources().koSmall(),
          DeliveryCommon.getMessages().delete(), new FieldUpdater<DeliveryDTO, ImageResource>()
          {

            @Override
            public void update(final int pIndex, final DeliveryDTO pObject, final ImageResource pValue)
            {
              if (!DeliveryStatusDTO.GENERATING.equals(pObject.getStatus()))
              {
                DeliveryListPresenter.this.deliverySelected = pObject;
                DeliveryListPresenter.this.display.getDeleteDeliveryPopup().setModal(true);
                DeliveryListPresenter.this.display.getDeleteDeliveryPopup().show();
              }

            }
          })
      {
        @Override
        public final ImageResource getValue(final DeliveryDTO pObject)
        {
          if (DeliveryStatusDTO.GENERATING.equals(pObject.getStatus()))
          {
            return LoadingPanel.getRessources().loaderBarMEDIUM();

          }
          else
          {
            return DeliveryCommon.getResources().koSmall();

          }
        }
      });
    }
    final CompositeCell<DeliveryDTO> actions = new CompositeCell<DeliveryDTO>(cells);
    final Column<DeliveryDTO, DeliveryDTO> actionsColumn = new Column<DeliveryDTO, DeliveryDTO>(actions)
    {
      @Override
      public DeliveryDTO getValue(final DeliveryDTO object)
      {
        return object;
      }
    };
    this.display.getDeliveryTable().addColumn(actionsColumn, DeliveryCommon.getMessages().actions());
    this.display.getDeliveryTable().setColumnWidth(actionsColumn, 150, Unit.PX);
  }

  /**
   * This method is the interface between the presenter and the view
   */
  private void bind()
  {
    this.display.getButtonCreateDelivery().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        DeliveryManagement.get().getEventBus().fireEvent(new CreateDeliveryEvent());
      }
    });

    this.display.getButtonManageDeliveryNote().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        DeliveryManagement.get().getEventBus().fireEvent(new ManageTemplateReportEvent());
      }
    });

    this.display.getStatusSearch().addChangeHandler(new ChangeHandler()
    {
      @Override
      public void onChange(final ChangeEvent event)
      {
        DeliveryListPresenter.this.filteredList();
      }
    });

    this.display.getTypeSearch().addChangeHandler(new ChangeHandler()
    {
      @Override
      public void onChange(final ChangeEvent event)
      {
        DeliveryListPresenter.this.filteredList();
      }
    });

    this.display.getLockDeliveryPopup().getValidate().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        DeliveryListPresenter.this.lockDelivery();
        DeliveryListPresenter.this.display.getLockDeliveryPopup().hide();
      }
    });

    this.display.getDeleteDeliveryPopup().getValidate().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        DeliveryListPresenter.this.deleteDelivery(DeliveryListPresenter.this.deliverySelected);
        DeliveryListPresenter.this.display.getDeleteDeliveryPopup().hide();
      }
    });

  }

  private void filteredList()
  {
    final Collection<DeliveryDTO> filter = this.filteredOnDeliver(this.deliveryList);
    this.refreshDeliveryList(new ArrayList<DeliveryDTO>(filter));
  }

  private void lockDelivery()
  {
    new AbstractRPCCall<Boolean>()
    {
      @Override
      protected void callService(final AsyncCallback<Boolean> pCb)
      {
        DeliveryManagement.get().getServiceAsync().lockDelivery(DeliveryManagement.get().getProjectId(),
                                                                DeliveryListPresenter.this.deliverySelected
                                                                    .getReference(), pCb);
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
          DeliveryManagement.get().getEventBus().fireEvent(new ShowDeliveriesEvent());
        }
      }


    }.retry(0);

  }

  private void deleteDelivery(final DeliveryDTO pDelivery)
  {
    new AbstractRPCCall<Boolean>()
    {
      @Override
      protected void callService(final AsyncCallback<Boolean> pCb)
      {
        DeliveryManagement.get().getServiceAsync().deleteDelivery(DeliveryManagement.get().getProjectId(),
                                                                  pDelivery.getReference(), pCb);
      }

      @Override
      public void onSuccess(final Boolean pResult)
      {
        if (pResult)
        {
          DeliveryListPresenter.this.fillDeliveryList();
        }
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        DeliveryCommon.displayErrorMessage(caught);
      }
    }.retry(0);

  }

  private Collection<DeliveryDTO> filteredOnDeliver(final List<DeliveryDTO> pDelivery)
  {
    return Collections2.filter(pDelivery, new Predicate<DeliveryDTO>()
    {
      @Override
      public boolean apply(final DeliveryDTO pInput)
      {
        boolean returnValue = true;
        if (DeliveryListPresenter.this.display.getTypeSearch().getSelectedIndex() != 0)
        {
          if (DeliveryListPresenter.this.display.getStatusSearch().getSelectedIndex() != 0)
          {
            returnValue = this.isStatus(pInput.getStatus().getLabel()) && this.isType(pInput.getType());
          }
          else
          {
            returnValue = this.isType(pInput.getType());
          }
        }
        else
        {
          if (DeliveryListPresenter.this.display.getStatusSearch().getSelectedIndex() != 0)
          {
            returnValue = this.isStatus(pInput.getStatus().getLabel());
          }

        }
        return returnValue;
      }

      private boolean isStatus(final String pStatusLabel)
      {
        boolean returnValue = false;

        final String statusText = DeliveryListPresenter.this.display.getStatusSearch().getItemText(
            DeliveryListPresenter.this.display.getStatusSearch().getSelectedIndex());

        if ((pStatusLabel).equals(statusText))
        {
          returnValue = true;
        }
        return returnValue;
      }

      private boolean isType(final String pType)
      {
        boolean returnValue = false;
        final String typeText = DeliveryListPresenter.this.display.getTypeSearch().getItemText(
            DeliveryListPresenter.this.display.getTypeSearch().getSelectedIndex());

        if ((pType).equals(typeText))
        {
          returnValue = true;
        }
        return returnValue;
      }
    });
  }

  private void refreshDeliveryList(final List<DeliveryDTO> pList)
  {
    this.display.getDeliveryDataProvider().setList(pList);
    this.display.getDeliveryTable().redraw();
    this.display.deliveryListSortHandler();
  }

  private void fillDeliveryList()
  {
    this.deliveryList = new ArrayList<DeliveryDTO>();
    new AbstractRPCCall<List<DeliveryDTO>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<DeliveryDTO>> pCb)
      {
        DeliveryManagement.get().getServiceAsync()
            .getDeliveryList(DeliveryManagement.get().getProjectId(), pCb);
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        DeliveryCommon.displayErrorMessage(caught);
      }

      @Override
      public void onSuccess(final List<DeliveryDTO> pResult)
      {
        if (pResult != null)
        {
          DeliveryListPresenter.this.deliveryList.clear();
          DeliveryListPresenter.this.deliveryList.addAll(pResult);
          DeliveryListPresenter.this.refreshDeliveryList(DeliveryListPresenter.this.deliveryList);
        }
      }


    }.retry(0);
  }

  /**
   * @inheritDoc
   */
  @Override
  public void go(final HasWidgets container)
  {
    container.clear();
    container.add(this.display.asWidget());
    this.fillDeliveryList();
    this.initListBox();
  }

  private void initListBox()
  {
    this.getTypes();
    this.getStatus();
  }

  private void getTypes()
  {
    new AbstractRPCCall<List<String>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<String>> pCb)
      {
        DeliveryManagement.get().getServiceAsync().getAllTypes(DeliveryManagement.get().getProjectId(), pCb);
      }

      @Override
      public void onSuccess(final List<String> pResult)
      {
        if (pResult != null)
        {
          DeliveryListPresenter.this.display.getTypeSearch().clear();
          DeliveryListPresenter.this.display.getTypeSearch().addItem(DeliveryCommon.getMessages().typeBox());
          for (final String type : pResult)
          {
            DeliveryListPresenter.this.display.getTypeSearch().addItem(type);

          }
        }
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        DeliveryCommon.displayErrorMessage(caught);
      }
    }.retry(0);
  }

  private void getStatus()
  {
    new AbstractRPCCall<List<DeliveryStatusDTO>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<DeliveryStatusDTO>> pCb)
      {
        DeliveryManagement.get().getServiceAsync().getStatus(pCb);
      }

      @Override
      public void onSuccess(final List<DeliveryStatusDTO> pResult)
      {
        if (pResult != null)
        {
          DeliveryListPresenter.this.display.getStatusSearch().clear();
          DeliveryListPresenter.this.display.getStatusSearch().addItem(
              DeliveryCommon.getMessages().statusBox());
          for (final DeliveryStatusDTO status : pResult)
          {
            DeliveryListPresenter.this.display.getStatusSearch().addItem(status.getLabel());

          }
        }
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        DeliveryCommon.displayErrorMessage(caught);
      }
    }.retry(0);
  }

  /**
   * Refresh the delivery list view
   */
  public void refreshView()
  {
    this.fillDeliveryList();
    this.initListBox();
  }

  public void showDeliveryGeneratedPopup(final boolean isSuccess)
  {
    if (isSuccess)
    {
      this.display.getSuccessGeneratedDialogBox().show();
    }
    else
    {
      this.display.getFailedGeneratedDialogBox().show();
    }

  }
}
