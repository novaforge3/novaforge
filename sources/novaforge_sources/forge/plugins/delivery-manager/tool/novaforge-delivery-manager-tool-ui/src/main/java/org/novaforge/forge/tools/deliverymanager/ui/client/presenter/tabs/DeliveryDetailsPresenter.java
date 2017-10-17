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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ReloadDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.DeliveryDetailsView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentTypeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryDTO;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.commons.client.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This presenter manage the view which allow to create or edit a delivery
 * 
 * @author Guillaume Lamirand
 * @author FALSQUELLE-E
 */
public class DeliveryDetailsPresenter implements TabPresenter
{

  private static final String       SEPARATOR = "_";
  private final DeliveryDetailsView display;
  private DeliveryDTO               deliveryDTO;
  private String                    currentReference;

  /**
   * Default presenter
   * 
   * @param display
   */
  public DeliveryDetailsPresenter(final DeliveryDetailsView display)
  {
    super();
    this.display = display;
    bind();
  }

  /**
   * This method is the interface between the presenter and the view
   */
  private void bind()
  {
    display.getButtonSaveDelivery().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        if ((display.getName().isValid()) && (display.getVersion().isValid()) && (display.getReference().isValid())
                && (display.getType().isValid()))
        {
          final ValidateDialogBox box = new ValidateDialogBox(DeliveryCommon.getMessages().saveMessage());
          box.getValidate().addClickHandler(new ClickHandler()
          {

            @Override
            public void onClick(final ClickEvent pEvent)
            {
              box.hide();
              DeliveryDetailsPresenter.this.saveDelivery();

            }
          });
          box.show();

        }
        else
        {
          display.getName().isValid();
          display.getVersion().isValid();
          display.getReference().isValid();
          display.getType().isValid();
          final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages().formErrorValidation(),
              InfoTypeEnum.ERROR);
          info.show();
        }

      }
    });

    display.getName().addValueChangeHandler(new ValueChangeHandler<String>()
    {

      @Override
      public void onValueChange(final ValueChangeEvent<String> event)
      {
        DeliveryDetailsPresenter.this.generateReference();

      }
    });

    display.getName().addKeyUpHandler(new KeyUpHandler()
    {
      @Override
      public void onKeyUp(final KeyUpEvent event)
      {
        DeliveryDetailsPresenter.this.generateReference();
      }
    });
    display.getName().setValidator(new Validator.DefaultValidator()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getErrorMessage()
      {
        return DeliveryCommon.getMessages().nameValidation();
      }
    });
    display.getVersion().addValueChangeHandler(new ValueChangeHandler<String>()
    {

      @Override
      public void onValueChange(final ValueChangeEvent<String> event)
      {
        DeliveryDetailsPresenter.this.generateReference();

      }
    });
    display.getVersion().addKeyUpHandler(new KeyUpHandler()
    {
      @Override
      public void onKeyUp(final KeyUpEvent event)
      {
        DeliveryDetailsPresenter.this.generateReference();
      }
    });
    display.getVersion().setValidator(new Validator.DefaultValidator()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getErrorMessage()
      {
        return DeliveryCommon.getMessages().versionValidation();
      }
    });
    display.getReference().setValidator(new Validator.DefaultValidator()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getErrorMessage()
      {
        return DeliveryCommon.getMessages().referenceValidation();
      }
    });
    display.getBugContent().addValueChangeHandler(new ValueChangeHandler<Boolean>()
    {
      @Override
      public void onValueChange(final ValueChangeEvent<Boolean> event)
      {
        if (event.getValue())
        {
          display.getNoteContent().setValue(true);
        }
      }
    });

    display.getNoteContent().addValueChangeHandler(new ValueChangeHandler<Boolean>()
    {
      @Override
      public void onValueChange(final ValueChangeEvent<Boolean> event)
      {
        if (!event.getValue() && display.getBugContent().getValue())
        {
          final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages()
              .selectBugContentWithoutNote(), InfoTypeEnum.WARNING);
          info.show();
          display.getNoteContent().setValue(true);
        }
      }
    });

  }

  private void saveDelivery()
  {
    DeliveryDetailsPresenter.this.majDeliveryDTO();
    new AbstractRPCCall<String>()
    {
      @Override
      protected void callService(final AsyncCallback<String> pCb)
      {
        DeliveryManagement.get().getServiceAsync()
            .saveDelivery(DeliveryManagement.get().getProjectId(), currentReference, deliveryDTO, pCb);
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        display.getReference().showError(true);
        DeliveryCommon.displayErrorMessage(caught);
      }

      @Override
      public void onSuccess(final String pResult)
      {
        if (pResult != null)
        {
          final InfoDialogBox box = new InfoDialogBox(DeliveryCommon.getMessages().saveDelivery(),
              InfoTypeEnum.SUCCESS);

          currentReference = pResult;
          box.getCloseButton().addClickHandler(new ClickHandler()
          {

            @Override
            public void onClick(final ClickEvent event)
            {
              DeliveryManagement.get().getEventBus().fireEvent(new ReloadDeliveryEvent(currentReference));
              box.hide();
            }
          });
          box.show();
        }
      }


    }.retry(0);
  }

  /**
   *
    */
  private void generateReference()
  {
    if (!Common.validateStringNotNull(currentReference))
    {
      final StringBuilder sb = new StringBuilder();
      if (Common.validateStringNotNull(display.getName().getValue()))
      {
        sb.append(display.getName().getValue());
      }
      if (Common.validateStringNotNull(display.getVersion().getValue()))
      {
        sb.append(SEPARATOR);
        sb.append(display.getVersion().getValue());
      }
      display.getReference().setValue(normalize(sb.toString()));
    }

  }

  private void majDeliveryDTO()
  {
    deliveryDTO.setName(display.getName().getValue());
    deliveryDTO.setVersion(display.getVersion().getValue());
    deliveryDTO.setReference(display.getReference().getValue());
    deliveryDTO.setType(display.getType().getValue());

    final Map<ContentTypeDTO, String> contents = new HashMap<ContentTypeDTO, String>();
    if (display.getEcmContent().getValue())
    {
      contents.put(ContentTypeDTO.ECM, display.getEcmDirectory().getValue());
    }
    if (display.getScmContent().getValue())
    {
      contents.put(ContentTypeDTO.SCM, display.getScmDirectory().getValue());
    }
    if (display.getBinaryContent().getValue())
    {
      contents.put(ContentTypeDTO.FILE, display.getBinaryDirectory().getValue());
    }
    if (display.getBugContent().getValue())
    {
      contents.put(ContentTypeDTO.BUG, ContentTypeDTO.BUG.name());
    }
    if (display.getNoteContent().getValue())
    {
      contents.put(ContentTypeDTO.NOTE, display.getNoteDirectory().getValue());
    }
    deliveryDTO.setContents(contents);

  }

  private final native String normalize(String pValue)
  /*-{
  	return $wnd.normalize(pValue);
  }-*/;

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshView(final String pReference)
  {
    if ((pReference == null) || ("".equals(pReference)))
    {
      updateView(new DeliveryDTO());
    }
    else
    {
      getDelivery(pReference);
    }
  }

  private void updateView(final DeliveryDTO pResult)
  {
    deliveryDTO = pResult;
    currentReference = deliveryDTO.getReference();
    initTypesList();
    initFields();
    initContents();
  }

  private void getDelivery(final String pReference)
  {
    new AbstractRPCCall<DeliveryDTO>()
    {
      @Override
      protected void callService(final AsyncCallback<DeliveryDTO> pCb)
      {
        DeliveryManagement.get().getServiceAsync().getDelivery(DeliveryManagement.get().getProjectId(), pReference,
                                                               pCb);
      }

      @Override
      public void onFailure(final Throwable caught)
      {

        DeliveryCommon.displayErrorMessage(caught);
      }

      @Override
      public void onSuccess(final DeliveryDTO pResult)
      {
        if (pResult != null)
        {
          DeliveryDetailsPresenter.this.updateView(pResult);
        }
      }

    }.retry(0);
  }

  /**
   *
   */
  private void initTypesList()
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
        display.setTypes(pResult);

      }

      @Override
      public void onFailure(final Throwable caught)
      {
        DeliveryCommon.displayErrorMessage(caught);
      }
    }.retry(0);
  }

  private void initFields()
  {
    display.getReference().setValue(deliveryDTO.getReference());
    display.getName().setValue(deliveryDTO.getName());
    display.getVersion().setValue(deliveryDTO.getVersion());
    display.getType().setValue(deliveryDTO.getType());
  }

  private void initContents()
  {
    // Clear content
    display.cleanContent();

    // Check available content
    new AbstractRPCCall<List<ContentTypeDTO>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<ContentTypeDTO>> pCb)
      {
        DeliveryManagement.get().getServiceAsync().getAvailableContent(DeliveryManagement.get().getProjectId(), pCb);
      }

      @Override
      public void onSuccess(final List<ContentTypeDTO> pResult)
      {
        // Update content availabity
        for (final ContentTypeDTO content : pResult)
        {
          switch (content)
          {
            case ECM:
              display.setEcmAvailable(true);
              break;
            case SCM:
              display.setScmAvailable(true);
              break;
            case FILE:
              display.setBinaryAvailable(true);
              break;
            case BUG:
              display.setBugAvailable(true);
              break;
            case NOTE:
              display.setNoteAvailable(true);
              break;

          }
        }
        // Update content according to the delivery
        final Set<Entry<ContentTypeDTO, String>> entrySet = deliveryDTO.getContents().entrySet();
        for (final Entry<ContentTypeDTO, String> entry : entrySet)
        {
          switch (entry.getKey())
          {
            case ECM:
              display.getEcmContent().setValue(true, true);
              display.getEcmDirectory().setValue(entry.getValue());
              break;
            case SCM:
              display.getScmContent().setValue(true, true);
              display.getScmDirectory().setValue(entry.getValue());
              break;
            case FILE:
              display.getBinaryContent().setValue(true, true);
              display.getBinaryDirectory().setValue(entry.getValue());
              break;
            case BUG:
              display.getBugContent().setValue(true, true);
              break;
            case NOTE:
              display.getNoteContent().setValue(true, true);
              display.getNoteDirectory().setValue(entry.getValue());
              break;

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

  public IsWidget getDisplay()
  {
    return display.asWidget();
  }

}
