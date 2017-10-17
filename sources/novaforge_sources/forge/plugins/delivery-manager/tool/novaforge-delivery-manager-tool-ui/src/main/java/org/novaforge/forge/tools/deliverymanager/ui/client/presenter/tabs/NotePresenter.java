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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.NoteView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ArtefactNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NoteContentDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateDTO;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class NotePresenter implements TabPresenter
{

   private final NoteView    display;
   private String            reference;
   private List<TemplateDTO> templates;
   private ContentDTO        content;

   public NotePresenter(final NoteView display)
   {
      super();
      this.display = display;
      this.templates = new ArrayList<TemplateDTO>();
      this.bind();

   }

   public void bind()
   {
      NotePresenter.this.display.getSaveButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent pEvent)
         {
            boolean isValid = true;
            // Check template name
            final int selectedIndex = NotePresenter.this.display.getTemplatesList().getSelectedIndex();
            if (selectedIndex == 0)
            {
               isValid = false;
            }
            // Check specific field
            final Map<String, String> fields = NotePresenter.this.display.getFields();
            final Set<Entry<String, String>> entrySet = fields.entrySet();
            for (final Entry<String, String> entry : entrySet)
            {
               if (!Common.validateStringNotNull(entry.getValue()))
               {
                  isValid = false;
                  break;
               }
            }
            if (isValid)
            {
               final String templateName = NotePresenter.this.display.getTemplatesList().getItemText(
                     selectedIndex);
               NotePresenter.this.saveNoteTemplate(templateName, fields);
            }
            else
            {
               final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages()
                     .noteTemplateValidation(), InfoTypeEnum.ERROR);
               info.show();
            }
         }

      });
      NotePresenter.this.display.getTemplatesList().addChangeHandler(new ChangeHandler()
      {

         @Override
         public void onChange(final ChangeEvent pEvent)
         {
            NotePresenter.this.display.getFieldsPanel().setVisible(false);
            if (NotePresenter.this.display.getTemplatesList().getSelectedIndex() != 0)
            {
               NotePresenter.this.display.getSaveButton().setEnabled(true);

               final int selectedIndex = NotePresenter.this.display.getTemplatesList().getSelectedIndex();

               NotePresenter.this.refreshFields(NotePresenter.this.display.getTemplatesList().getItemText(
                     selectedIndex));
            }
            else
            {
               NotePresenter.this.display.getSaveButton().setEnabled(false);

            }

         }

      });
   }

   public void saveNoteTemplate(final String pTemplateName, final Map<String, String> pFields)
   {
      new AbstractRPCCall<Boolean>()
      {
         @Override
         protected void callService(final AsyncCallback<Boolean> pCb)
         {
            DeliveryManagement.get().getServiceAsync().updateNoteTemplate(DeliveryManagement.get().getProjectId(),
                                                                          NotePresenter.this.reference, pTemplateName,
                                                                          pFields, pCb);
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
               final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages().saveSuccess(),
                                                            InfoTypeEnum.SUCCESS, 2500);
               info.show();
            }
         }



      }.retry(0);
   }

   private void refreshFields(final String pItemText)
   {
      final TemplateDTO template = this.getTemplate(pItemText);
      if (template != null && template.getFields() != null && !template.getFields().isEmpty())
      {
         this.display.getTemplateInfoLabel().setText(DeliveryCommon.getMessages().templateInfo());
         this.display.setFields(template.getFields(), this.getFieldsValue(this.content));
         this.display.getFieldsGrid().setVisible(true);
      }
      else
      {
         this.display.getFieldsGrid().setVisible(false);
         this.display.getTemplateInfoLabel().setText(DeliveryCommon.getMessages().noteTemplateEmptyCustomField());
      }
      this.display.getFieldsPanel().setVisible(true);

   }

   private TemplateDTO getTemplate(final String pItemText)
   {
      TemplateDTO returnTemplateDTO = null;
      if ((pItemText != null) && (this.templates != null))
      {
         for (final TemplateDTO template : this.templates)
         {
            if (pItemText.equals(template.getName()))
            {
               returnTemplateDTO = template;
               break;
            }
         }
      }
      return returnTemplateDTO;
   }

   private Map<String, String> getFieldsValue(final ContentDTO pResult)
   {
      if ((pResult.getRoot().getChildren() != null) && (!pResult.getRoot().getChildren().isEmpty()) && (pResult
                                                                                                                    .getRoot()
                                                                                                                    .getChildren()
                                                                                                                    .get(0) instanceof ArtefactNode))
      {
         final ArtefactNode artefact = (ArtefactNode) pResult.getRoot().getChildren().get(0);
         return artefact.getFields();
      }
      else
      {
         return new HashMap<String, String>();
      }
   }

   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }

   @Override
   public void refreshView(final String pReference)
   {
      this.reference = pReference;
      this.display.getSaveButton().setEnabled(false);
      this.display.getTemplatesList().clear();
      this.display.getFieldsPanel().setVisible(false);
      this.refreshNoteContent();
   }

   private void refreshNoteContent()
   {
      this.display.getTemplatesList().clear();
      this.templates = new ArrayList<TemplateDTO>();
      new AbstractRPCCall<NoteContentDTO>()
      {
         @Override
         protected void callService(final AsyncCallback<NoteContentDTO> pCb)
         {
            DeliveryManagement.get().getServiceAsync().getNoteContent(DeliveryManagement.get().getProjectId(),
                                                                      NotePresenter.this.reference, pCb);
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            DeliveryCommon.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(final NoteContentDTO pResult)
         {
            if (pResult != null)
            {
               NotePresenter.this.content = pResult.getContent();
               NotePresenter.this.templates.clear();
               NotePresenter.this.templates.addAll(pResult.getTemplates());
               NotePresenter.this.display.getTemplatesList().addItem(DeliveryCommon.getMessages()
                                                                                   .noteTemplateNameBox());

               if ((NotePresenter.this.templates != null) && (!NotePresenter.this.templates.isEmpty()))
               {

                  for (int i = 0; i < NotePresenter.this.templates.size(); i++)
                  {
                     final TemplateDTO template = NotePresenter.this.templates.get(i);
                     NotePresenter.this.display.getTemplatesList().addItem(template.getName());
                     if (((pResult.getContent().getRoot().getChildren() != null) & (pResult.getContent().getRoot()
                                                                                           .getChildren().size() == 1))
                             && (template.getName().equals(pResult.getContent().getRoot().getChildren().get(0)
                                                                  .getName())))
                     {
                        NotePresenter.this.display.getTemplatesList().setSelectedIndex(i + 1);
                        refreshFields(template.getName());
                        NotePresenter.this.display.getSaveButton().setEnabled(true);
                     }
                  }
               }
            }
         }


      }.retry(0);

   }
}
