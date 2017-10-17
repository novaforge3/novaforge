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

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnChangeUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.Utils;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ManageTemplateReportEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.templates.TemplateReportManageView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryProperties;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.ExceptionCode;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.cells.ClickableImageResourceCell;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.commons.client.validation.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CASERY-J
 */
public class TemplateReportManagePresenter implements Presenter {
  private final TemplateReportManageView display;
  private final String servletPath;
  private final ManagePresenterType type;
  private TemplateDTO noteTemplate;

  /**
   * @param display
   */
  public TemplateReportManagePresenter(final TemplateReportManageView display,
                                       final ManagePresenterType pType) {
    super();
    type = pType;

    this.display = display;

    servletPath = display.getUploader().getServletPath();

    bind();
    initEditColumn();
  }

  /**
   * This method is the interface between the presenter and the view
   */
  private void bind() {
    display.getButtonSave().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        if (display.getNameTB().isValid()
                && display.getDescriptionTB().isValid()
                && (((ManagePresenterType.CREATE.equals(type)) && (Common.validateStringNotNull(Utils.basename(display
                .getUploader().getServerMessage().getUploadedFileNames().get(0)))))) || (ManagePresenterType.EDIT.equals(type))) {
          display.getValidateDialogBox().show();

        } else { // Has to be done here in order to display error message associated to validation box
          display.getNameTB().isValid();
          display.getDescriptionTB().isValid();
          if (ManagePresenterType.CREATE.equals(type)) {

            display.getUploader().getFileInput().getWidget()
                    .addStyleName(Common.getResources().css().importantRed());
          }
          final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages()
                  .formErrorValidation(), InfoTypeEnum.ERROR);
          info.show();

        }
      }
    });

    display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        new AbstractRPCCall<Boolean>() {
          @Override
          protected void callService(final AsyncCallback<Boolean> pCb) {
            DeliveryManagement
                    .get()
                    .getServiceAsync()
                    .existTemplateFile(DeliveryManagement.get().getProjectId(),
                            noteTemplate.getFileName(), Utils.basename(display.getUploader().getServerMessage().getUploadedFileNames().get(0)), pCb);
          }

          @Override
          public void onFailure(final Throwable caught) {
            TemplateReportManagePresenter.this.updateForm(true);
            DeliveryCommon.displayErrorMessage(caught);
          }

          @Override
          public void onSuccess(final Boolean pResult) {
            if (pResult) {
              final InfoDialogBox info = new InfoDialogBox(ExceptionCode.TEMPLATE_FILE_ALREADY_EXISTS
                      .getLocalizedMessage(), InfoTypeEnum.WARNING);
              info.show();
              display.getUploader().getFileInput().getWidget()
                      .addStyleName(Common.getResources().css().importantRed());
            } else {
              display.getUploader().getFileInput().getWidget()
                      .removeStyleName(Common.getResources().css().importantRed());
              TemplateReportManagePresenter.this.updateForm(false);
              display.getValidateDialogBox().hide();
              if (Common.validateStringNotNull(Utils.basename(display.getUploader().getServerMessage().getUploadedFileNames().get(0)))) {

                final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages()
                        .uploadTemplateFile(), InfoTypeEnum.INFO, 2000);
                info.show();
                display.getUploader().submit();
              } else {
                final TemplateDTO template = TemplateReportManagePresenter.this.buildTemplateDTO();
                TemplateReportManagePresenter.this.createTemplate(template);
              }

            }
          }


        }.retry(0);
      }
    });

    display.getButtonReturn().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        final ValidateDialogBox box = new ValidateDialogBox(DeliveryCommon.getMessages()
                .returnListMessage());
        box.getValidate().addClickHandler(new ClickHandler() {

          @Override
          public void onClick(final ClickEvent pEvent) {
            box.hide();
            DeliveryManagement.get().getEventBus().fireEvent(new ManageTemplateReportEvent());

          }
        });
        box.show();
      }
    });

    display.getNameTB().setValidator(new Validator() {
      @Override
      public boolean isValid(final String pValue) {
        return Common.validateStringNotNull(pValue);
      }

      @Override
      public String getErrorMessage() {
        return DeliveryCommon.getMessages().templateNameValidation();
      }
    });
    display.getDescriptionTB().setValidator(new Validator() {
      @Override
      public boolean isValid(final String pValue) {
        return (Common.validateStringNotNull(pValue)) && (pValue.length() < 250);
      }

      @Override
      public String getErrorMessage() {
        return DeliveryCommon.getMessages().templateDescriptionValidation();
      }
    });
    display.getUploader().addOnChangeUploadHandler(new OnChangeUploaderHandler() {

      @Override
      public void onChange(final IUploader pUploader) {
        if (Status.CHANGED.equals(pUploader.getStatus())) {
          display.getUploader().getFileInput().getWidget()
                  .removeStyleName(Common.getResources().css().importantRed());

        } else if (Status.CANCELED.equals(pUploader.getStatus())) {

          TemplateReportManagePresenter.this.updateForm(true);
        }
      }
    });
    display.getUploader().addOnFinishUploadHandler(new OnFinishUploaderHandler() {

      @Override
      public void onFinish(final IUploader pUploader) {
        if (Status.SUCCESS.equals(pUploader.getStatus())) {
          final TemplateDTO template = TemplateReportManagePresenter.this.buildTemplateDTO();
          TemplateReportManagePresenter.this.createTemplate(template);
        } else {
          TemplateReportManagePresenter.this.updateForm(true);
          display.getUploader().getFileInput().getWidget()
                  .addStyleName(Common.getResources().css().importantRed());
        }

      }
    });

  }

  /**
   * @param display
   */
  protected void initEditColumn() {
    // Edit Column
    final Column<TemplateFieldDTO, ImageResource> editColumn = new Column<TemplateFieldDTO, ImageResource>(
            new ClickableImageResourceCell()) {
      @Override
      public ImageResource getValue(final TemplateFieldDTO object) {
        if (object.isNew()) {
          return Common.getResources().add();
        } else {
          return Common.getResources().delete();
        }
      }
    };
    editColumn.setFieldUpdater(new FieldUpdater<TemplateFieldDTO, ImageResource>() {
      @Override
      public void update(final int index, final TemplateFieldDTO object, final ImageResource value) {
        if ((!Common.validateStringNotNull(object.getName())) || (!Common.validateStringNotNull(object
                .getDescription()))) {
          final InfoDialogBox box = new InfoDialogBox(DeliveryCommon.getMessages().emptyForm(),
                  InfoTypeEnum.WARNING);
          box.show();
        } else if (object.isNew()) {
          object.setNew(false);
          final List<TemplateFieldDTO> list = new ArrayList<TemplateFieldDTO>(display.getDataProvider()
                  .getList());

          list.add(new TemplateFieldDTO());
          display.getDataProvider().setList(list);

        } else {
          final List<TemplateFieldDTO> list = new ArrayList<TemplateFieldDTO>(display.getDataProvider()
                  .getList());
          list.remove(object);
          display.getDataProvider().setList(list);
        }
      }
    });
    display.getFieldsTable().addColumn(editColumn, DeliveryCommon.getMessages().actions());
    display.getFieldsTable().setColumnWidth(editColumn, 20, Unit.PX);
  }

  private void updateForm(final boolean pStatus) {
    TemplateReportManagePresenter.this.display.getButtonReturn().setEnabled(pStatus);
    TemplateReportManagePresenter.this.display.getButtonSave().setEnabled(pStatus);
    TemplateReportManagePresenter.this.display.getDescriptionTB().setEnable(pStatus);
    TemplateReportManagePresenter.this.display.getNameTB().setEnable(pStatus);
  }

  private TemplateDTO buildTemplateDTO() {
    final TemplateDTO template = new TemplateDTO();
    template.setName(TemplateReportManagePresenter.this.display.getNameTB().getValue());
    template.setDescription(TemplateReportManagePresenter.this.display.getDescriptionTB().getValue());
    if (Common
            .validateStringNotNull(Utils.basename(TemplateReportManagePresenter.this.display.getUploader().getServerMessage().getUploadedFileNames().get(0)))) {
      template.setFileName(Utils.basename(TemplateReportManagePresenter.this.display.getUploader().getServerMessage().getUploadedFileNames().get(0)));

    } else if (ManagePresenterType.EDIT.equals(TemplateReportManagePresenter.this.type)) {
      template.setFileName(noteTemplate.getFileName());
    }
    final List<TemplateFieldDTO> list = TemplateReportManagePresenter.this.display.getDataProvider()
            .getList();
    final List<TemplateFieldDTO> fields = new ArrayList<TemplateFieldDTO>();
    for (final TemplateFieldDTO templateFieldDTO : list) {
      if (!templateFieldDTO.isNew()) {
        fields.add(templateFieldDTO);
      }
    }
    template.setFields(fields);
    return template;
  }

  private void createTemplate(final TemplateDTO pTemplate) {
    new AbstractRPCCall<Boolean>() {
      @Override
      protected void callService(final AsyncCallback<Boolean> pCb) {
        DeliveryManagement
                .get()
                .getServiceAsync()
                .saveTemplateReport(DeliveryManagement.get().getProjectId(), noteTemplate.getName(),
                        pTemplate, pCb);
      }

      @Override
      public void onSuccess(final Boolean pResult) {
        if (pResult) {
          DeliveryManagement.get().getEventBus().fireEvent(new ManageTemplateReportEvent());

        }
      }

      @Override
      public void onFailure(final Throwable caught) {
        TemplateReportManagePresenter.this.updateForm(true);
        DeliveryCommon.displayErrorMessage(caught);
      }
    }.retry(0);

  }

  /**
   * @inheritDoc
   */
  @Override
  public void go(final HasWidgets container) {
    container.clear();
    container.add(display.asWidget());
  }

  public void refreshView(final TemplateDTO pTemplate) {
    // Update current template
    noteTemplate = pTemplate;

    // Update field list
    final List<TemplateFieldDTO> list = new ArrayList<TemplateFieldDTO>();
    if (pTemplate.getFields() != null) {
      list.addAll(pTemplate.getFields());
    }
    list.add(new TemplateFieldDTO());
    display.getDataProvider().setList(list);

    // Update template form
    updateForm(true);
    display.getNameTB().clear();
    display.getDescriptionTB().clear();
    display.getNameTB().setValue(noteTemplate.getName());
    display.getDescriptionTB().setValue(noteTemplate.getDescription());
    display.getFileName().setText(noteTemplate.getFileName());

    // Update uploader
    if (ManagePresenterType.CREATE.equals(type)) {
      display.getFileName().setVisible(false);
    } else {
      display.getFileName().setVisible(true);

    }
    display.getUploader().reset();
    display.getUploader().setServletPath(getUploadRequest());
  }

  private String getUploadRequest() {
    return servletPath + "?" + DeliveryProperties.PROJECT_PARAMETER + "=" + DeliveryManagement.get().getProjectId();
  }
}
