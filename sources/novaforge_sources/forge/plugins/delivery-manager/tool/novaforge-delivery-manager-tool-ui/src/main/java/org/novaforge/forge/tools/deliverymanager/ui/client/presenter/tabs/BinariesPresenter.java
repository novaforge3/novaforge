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

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnChangeUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.Utils;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.BinariesView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.*;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.cells.ClickableImageResourceCell;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;

import java.util.ArrayList;
import java.util.List;

/**
 * This presenter manage a tabulation about file content
 *
 * @author Guillaume Lamirand
 */
public class BinariesPresenter implements TabPresenter {

  private final BinariesView display;
  private final String servletPath;
  private String reference;

  public BinariesPresenter(final BinariesView display) {
    super();
    this.display = display;

    initDeleteColumn();

    // Add a selection model only on edit column
    final SelectionModel<NodeDTO> selectionModel = new MultiSelectionModel<NodeDTO>(
            CellKey.NODE_KEY_PROVIDER);
    display.getBinariesTable().setSelectionModel(selectionModel,
            DefaultSelectionEventManager.<ArtefactNode>createWhitelistManager(3));

    bind();
    servletPath = display.getUploader().getServletPath();

  }

  private void initDeleteColumn() {
    // Edit Column
    final Column<ArtefactNode, ImageResource> editColumn = new Column<ArtefactNode, ImageResource>(
            new ClickableImageResourceCell(DeliveryCommon.getMessages().deleteBinary())) {
      @Override
      public ImageResource getValue(final ArtefactNode object) {
        return DeliveryCommon.getResources().koSmall();
      }
    };
    editColumn.setFieldUpdater(new FieldUpdater<ArtefactNode, ImageResource>() {
      @Override
      public void update(final int index, final ArtefactNode object, final ImageResource value) {
        final ValidateDialogBox box = new ValidateDialogBox(DeliveryCommon.getMessages()
                .deleteFileMessage());
        box.getValidate().addClickHandler(new ClickHandler() {

          @Override
          public void onClick(final ClickEvent pEvent) {
            box.hide();
            deleteFile(object);

          }
        });
        box.show();
      }

      private void deleteFile(final ArtefactNode object) {
        new AbstractRPCCall<Boolean>() {
          @Override
          protected void callService(final AsyncCallback<Boolean> pCb) {
            DeliveryManagement
                    .get()
                    .getServiceAsync()
                    .deleteFileArtefact(DeliveryManagement.get().getProjectId(), reference,
                            object.getName(), pCb);
          }

          @Override
          public void onFailure(final Throwable caught) {
            DeliveryCommon.displayErrorMessage(caught);
          }

          @Override
          public void onSuccess(final Boolean pResult) {
            if (pResult) {
              BinariesPresenter.this.refreshBinaries();
            }
          }


        }.retry(0);
      }
    });
    editColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    display.getBinariesTable().addColumn(editColumn, DeliveryCommon.getMessages().actions());
    display.getBinariesTable().setColumnWidth(editColumn, 30, Unit.PX);
  }

  public void bind() {
    display.getExtBox().addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(final ValueChangeEvent<String> event) {
        BinariesPresenter.this.updateButton();
        BinariesPresenter.this.updateFileName();
      }

    });

    display.getExtBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(final KeyUpEvent event) {
        BinariesPresenter.this.updateButton();
        BinariesPresenter.this.updateFileName();
      }
    });

    display.getExtFileNameBox().addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(final ValueChangeEvent<String> event) {
        display.getExtFileNameBox().removeStyleName(Common.getResources().css().importantRed());
        BinariesPresenter.this.updateButton();
      }

    });

    display.getExtFileNameBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(final KeyUpEvent event) {
        display.getExtFileNameBox().removeStyleName(Common.getResources().css().importantRed());
        BinariesPresenter.this.updateButton();
      }
    });

    display.getExtButton().addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent pEvent) {
        new AbstractRPCCall<Boolean>() {
          @Override
          protected void callService(final AsyncCallback<Boolean> pCb) {
            DeliveryManagement
                    .get()
                    .getServiceAsync()
                    .existFile(DeliveryManagement.get().getProjectId(), reference,
                            display.getExtFileNameBox().getValue(), pCb);
          }

          @Override
          public void onSuccess(final Boolean pResult) {
            if (!pResult) {
              getExtFile();
            } else {
              display.getExtButton().setEnabled(false);
              display.getExtButton().removeStyleName("changed");
              display.getExtButton().setEnabled(false);
              display.getExtFileNameBox().addStyleName(Common.getResources().css().importantRed());
              final InfoDialogBox box = new InfoDialogBox(
                      DeliveryCommon.getMessages().fileNameExist(), InfoTypeEnum.ERROR);
              box.show();

            }
          }

          @Override
          public void onFailure(final Throwable caught) {

            DeliveryCommon.displayErrorMessage(caught);
          }
        }.retry(0);
      }

      private void getExtFile() {
        new AbstractRPCCall<Boolean>() {
          @Override
          protected void callService(final AsyncCallback<Boolean> pCb) {
            DeliveryManagement
                    .get()
                    .getServiceAsync()
                    .getExternalFile(DeliveryManagement.get().getProjectId(), reference,
                            display.getExtBox().getValue(), display.getExtFileNameBox().getValue(), pCb);
          }

          @Override
          public void onSuccess(final Boolean pResult) {
            if (pResult) {
              BinariesPresenter.this.refreshBinaries();
              final InfoDialogBox box = new InfoDialogBox(DeliveryCommon.getMessages().receiveValid(),
                      InfoTypeEnum.SUCCESS, 2500);
              box.addContentStyleName(Common.getResources().css().important());
              box.show();
            }
          }

          @Override
          public void onFailure(final Throwable caught) {

            DeliveryCommon.displayErrorMessage(caught);
          }
        }.retry(0);
      }

    });
    display.getUploader().addOnChangeUploadHandler(new OnChangeUploaderHandler() {

      @Override
      public void onChange(final IUploader pUploader) {
        if (Status.CHANGED.equals(pUploader.getStatus())) {
          new AbstractRPCCall<Boolean>() {
            @Override
            protected void callService(final AsyncCallback<Boolean> pCb) {
              DeliveryManagement
                      .get()
                      .getServiceAsync()
                      .existFile(DeliveryManagement.get().getProjectId(), reference,
                              Utils.basename(pUploader.getServerMessage().getUploadedFileNames().get(0)), pCb);
            }

            @Override
            public void onSuccess(final Boolean pResult) {
              if (pResult) {
                display.getUploaderButton().setEnabled(false);
                pUploader.getFileInput().getWidget()
                        .addStyleName(Common.getResources().css().importantRed());
                final InfoDialogBox box = new InfoDialogBox(DeliveryCommon.getMessages()
                        .fileNameExist(), InfoTypeEnum.ERROR);
                box.show();

              } else {
                display.getUploaderButton().setEnabled(true);
                pUploader.getFileInput().getWidget()
                        .removeStyleName(Common.getResources().css().importantRed());
              }
            }

            @Override
            public void onFailure(final Throwable caught) {

              DeliveryCommon.displayErrorMessage(caught);
            }
          }.retry(0);
        }
      }
    });
    display.getUploader().addOnFinishUploadHandler(new OnFinishUploaderHandler() {

      @Override
      public void onFinish(final IUploader pUploader) {
        BinariesPresenter.this.refreshBinaries();
        if (!Status.CANCELED.equals(pUploader.getStatus())) {
          final InfoDialogBox box = new InfoDialogBox(DeliveryCommon.getMessages().receiveValid(),
                  InfoTypeEnum.SUCCESS, 2500);
          box.addContentStyleName(Common.getResources().css().important());
          box.show();
        }
      }
    });
    display.getFileNameColumn().setFieldUpdater(new FieldUpdater<ArtefactNode, String>() {

      @Override
      public void update(final int pIndex, final ArtefactNode pObject, final String pValue) {
        BinariesPresenter.this.updateFileArtefact(pObject, pValue);
      }
    });

  }

  private void updateFileName() {
    final String url = BinariesPresenter.this.display.getExtBox().getValue();
    final String[] split = url.split("/");
    BinariesPresenter.this.display.getExtFileNameBox().setText(split[split.length - 1]);
  }

  public IsWidget getDisplay() {
    return display.asWidget();
  }

  @Override
  public void refreshView(final String pReference) {
    reference = pReference;
    display.getExtBox().setText(Common.EMPTY_STRING);
    display.getExtFileNameBox().setText(Common.EMPTY_STRING);
    display.getExtFileNameBox().removeStyleName(Common.getResources().css().importantRed());
    display.getUploader().reset();
    display.getUploader().getFileInput().getWidget()
            .removeStyleName(Common.getResources().css().importantRed());
    display.getUploader().setServletPath(getUploadRequest());
    refreshBinaries();
  }

  private String getUploadRequest() {
    return servletPath + "?" + DeliveryProperties.PROJECT_PARAMETER + "=" + DeliveryManagement.get().getProjectId()
            + "&" + DeliveryProperties.REFERENCE_PARAMETER + "=" + reference;
  }

  private void refreshBinaries() {
    display.getListDataProvider().getList().clear();
    new AbstractRPCCall<ContentDTO>() {
      @Override
      protected void callService(final AsyncCallback<ContentDTO> pCb) {
        DeliveryManagement.get().getServiceAsync()
                .getContent(DeliveryManagement.get().getProjectId(), reference, ContentTypeDTO.FILE, pCb);
      }

      @Override
      public void onSuccess(final ContentDTO pResult) {
        display.getListDataProvider().getList().clear();
        if (pResult != null) {
          if (pResult.getRoot() != null) {
            final List<ArtefactNode> artefacts = new ArrayList<ArtefactNode>();
            for (final NodeDTO node : pResult.getRoot().getChildren()) {
              if (node instanceof ArtefactNode) {
                artefacts.add((ArtefactNode) node);
              }

            }
            display.getListDataProvider().setList(artefacts);

          }
          display.getExtFileNameBox().setText(Common.EMPTY_STRING);
          display.getExtBox().setText(Common.EMPTY_STRING);
          BinariesPresenter.this.updateButton();
        }
      }

      @Override
      public void onFailure(final Throwable caught) {

        DeliveryCommon.displayErrorMessage(caught);
      }
    }.retry(0);
  }

  /**
   *
   */
  private void updateButton() {
    final String value = display.getExtBox().getValue();
    if (Common.validateStringNotNull(value)) {
      display.getExtButton().setEnabled(true);
      display.getExtButton().addStyleName("changed");
    } else {
      display.getExtButton().setEnabled(false);
      display.getExtButton().removeStyleName("changed");

    }
  }

  private void updateFileArtefact(final ArtefactNode pObject, final String pNewName) {
    if (!pObject.getName().equals(pNewName)) {
      new AbstractRPCCall<Boolean>() {
        @Override
        protected void callService(final AsyncCallback<Boolean> pCb) {
          DeliveryManagement.get().getServiceAsync().updateFileArtefact(DeliveryManagement.get().getProjectId(),
                  reference, pObject.getName(), pNewName, pCb);
        }

        @Override
        public void onSuccess(final Boolean pResult) {
          if (pResult) {
            pObject.setName(pNewName);
            pObject.setID(pNewName);
          }
        }

        @Override
        public void onFailure(final Throwable caught) {
          pObject.setName(pObject.getName());
          pObject.setID(pObject.getName());
          DeliveryCommon.displayErrorMessage(caught);
        }
      }.retry(0);
    }
  }
}
