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
package org.novaforge.forge.ui.forge.reference.client.presenter.template;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.template.CreateTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.SelectTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ViewEnum;
import org.novaforge.forge.ui.forge.reference.client.properties.TemplateMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateDetailViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplatesListView;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateLightDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TemplatesListPresenter implements Presenter
{

  private final TemplateMessage         messages = (TemplateMessage) GWT.create(TemplateMessage.class);
  private final TemplateDetailPresenter templateDetailPresenter;
  private final TemplatesListView       display;
  private ViewEnum activatedView;
  private List<TemplateLightDTO>        templatesList;
  private String                        currentSelectedKey;

  public TemplatesListPresenter(final TemplatesListView display)
  {
    super();
    this.display = display;
    templateDetailPresenter = new TemplateDetailPresenter(new TemplateDetailViewImpl());

    bind();
  }

  public void bind()
  {
    display.getReloadImage().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        refreshTemplateList(true, null);

      }
    });
    display.getAddButton().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        ForgeReferenceEntryPoint.getEventBus().fireEvent(new CreateTemplateEvent());
      }
    });
    display.getPreloadButton().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        preloadTemplates();
      }
    });
    display.getListSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
    {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event)
      {
        switch (activatedView)
        {
          case ADD:
          case EDIT:
            if ((currentSelectedKey != null) && (!currentSelectedKey.equals(getTemplateSelected().getName())))
            {
              final InfoDialogBox info = new InfoDialogBox(messages.editionActive());
              info.show();
            }
            break;
          case READ:
            currentSelectedKey = display.getListSelectionModel().getSelectedObject();
            ForgeReferenceEntryPoint.getEventBus().fireEvent(
                new SelectTemplateEvent(getTemplateFromKey(currentSelectedKey)));
            break;
        }
      }
    });

    ForgeReferenceEntryPoint.getEventBus().addHandler(SelectTemplateEvent.TYPE,
        new SelectTemplateEvent.Handler()
        {
          @Override
          public void refreshView(final SelectTemplateEvent pEvent)
          {
            new AbstractReferenceRPCCall<TemplateDTO>()
            {
              @Override
              protected void callService(final AsyncCallback<TemplateDTO> pCb)
              {
                ForgeReferenceEntryPoint.getServiceAsync().getTemplate(pEvent.getTemplateLightDTO().getId(),
                    pCb);
              }

              @Override
              public void onFailure(final Throwable caught)
              {
                ErrorManagement.displayErrorMessage(caught);
              }

              @Override
              public void onSuccess(final TemplateDTO pResult)
              {
                if (pResult != null)
                {
                  activatedView = ViewEnum.READ;
                  display.getAddButton().setEnabled(true);
                  templateDetailPresenter.go(display.getContentPanel());
                  templateDetailPresenter.updateTemplateDetails(pResult);
                }
              }



            }.retry(0);
          }

        });

  }

  public void refreshTemplateList(final boolean pNew, final String pSelectKey)
  {
    if (pNew)
    {
      currentSelectedKey = pSelectKey;
    }

    templatesList = new ArrayList<TemplateLightDTO>();
    new AbstractReferenceRPCCall<List<TemplateLightDTO>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<TemplateLightDTO>> pCb)
      {
        ForgeReferenceEntryPoint.getServiceAsync().getTemplates(pCb);
      }

      @Override
      public void onSuccess(final List<TemplateLightDTO> pResult)
      {
        if (pResult != null)
        {
          display.getAddButton().setEnabled(true);
          display.getPreloadButton().setEnabled(true);
          templatesList.addAll(pResult);
          display.getListDataProvider().setList(getTemplatesList());
          display.getCellList().setPageSize(getTemplatesList().size());
          activatedView = ViewEnum.READ;
          if ((currentSelectedKey != null) && (!"".equals(currentSelectedKey)))
          {
            display.getListSelectionModel().setSelected(currentSelectedKey, true);
            ForgeReferenceEntryPoint.getEventBus().fireEvent(
                new SelectTemplateEvent(getTemplateFromKey(currentSelectedKey)));
          }
          else if (!templatesList.isEmpty())
          {
            currentSelectedKey = getTemplatesList().get(0);
            display.getListSelectionModel().setSelected(currentSelectedKey, true);
            ForgeReferenceEntryPoint.getEventBus().fireEvent(
                new SelectTemplateEvent(getTemplateFromKey(currentSelectedKey)));
          }
          else
          {
            display.getContentPanel().clear();
          }
        }
      }

      @Override
      public void onFailure(final Throwable caught)
      {

        ErrorManagement.displayErrorMessage(caught);
      }

    }.retry(0);

  }

  private void preloadTemplates()
  {
    new AbstractReferenceRPCCall<Void>()
    {
      @Override
      protected void callService(final AsyncCallback<Void> pCb)
      {
        ForgeReferenceEntryPoint.getServiceAsync().preloadTemplates(pCb);
      }

      @Override
      public void onSuccess(final Void pResult)
      {
        refreshTemplateList(true, null);
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        ErrorManagement.displayErrorMessage(caught);
      }

    }.retry(0);
  }

  private TemplateLightDTO getTemplateSelected()
  {
    final String selectedTemplate = display.getListSelectionModel().getSelectedObject();
    return getTemplateFromKey(selectedTemplate);
  }

  private TemplateLightDTO getTemplateFromKey(final String pKey)
  {
    TemplateLightDTO returnTemplate = null;
    for (final TemplateLightDTO templateDTO : templatesList)
    {
      if (templateDTO.getName().equals(pKey))
      {
        returnTemplate = templateDTO;
        break;
      }
    }
    return returnTemplate;
  }

  private List<String> getTemplatesList()
  {
    final List<String> list = new LinkedList<String>();
    for (final TemplateLightDTO template : templatesList)
    {
      list.add(template.getName());
    }
    Collections.sort(list);
    return list;
  }

  @Override
  public void go(final HasWidgets container)
  {
    container.clear();
    display.getAddButton().setEnabled(true);
    display.getPreloadButton().setEnabled(true);
    container.add(display.asWidget());
  }

  public IsWidget getDisplay()
  {
    return display.asWidget();
  }

  public TemplateDetailPresenter getTemplateDetailPresenter()
  {
    return templateDetailPresenter;
  }
}
