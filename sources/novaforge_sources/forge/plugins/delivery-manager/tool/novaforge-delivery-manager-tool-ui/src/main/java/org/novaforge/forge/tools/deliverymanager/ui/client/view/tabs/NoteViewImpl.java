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

package org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldTypeDTO;
import org.novaforge.forge.ui.commons.client.Common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class NoteViewImpl extends Composite implements NoteView
{

   private static NoteViewImplUiBinder uiBinder = GWT.create(NoteViewImplUiBinder.class);
   @UiField
   Label   noteTitle;
   @UiField
   Label   noteInfo;
   @UiField
   Label   templateLabel;
   @UiField
   ListBox templateListBox;
   @UiField
   Panel   fieldsTemplate;
   @UiField
   Label   templateTitle;
   @UiField
   Label   templateInfo;
   @UiField
   Grid    fieldGrid;
   @UiField
   Button  buttonSave;

   public NoteViewImpl()
   {
      DeliveryCommon.getResources().css().ensureInjected();

      this.initWidget(uiBinder.createAndBindUi(this));

      // Set fields element
      this.fieldsTemplate.setVisible(false);
      this.fieldGrid.resizeColumns(3);
      this.fieldGrid.setWidth("80%");
      this.fieldGrid.addStyleName(DeliveryCommon.getResources().css().templateGrid());
      this.templateInfo.addStyleName(DeliveryCommon.getResources().css().infoAction());

      // Set txt
      this.noteTitle.setText(DeliveryCommon.getMessages().noteTitle());
      this.noteInfo.setText(DeliveryCommon.getMessages().noteInfo());
      this.templateLabel.setText(DeliveryCommon.getMessages().noteTemplate());
      this.templateTitle.setText(DeliveryCommon.getMessages().templateTitle());
      this.templateInfo.setText(DeliveryCommon.getMessages().templateInfo());

      this.buttonSave.setText(Common.getMessages().save());

   }

   @Override
   public Button getSaveButton()
   {
      return this.buttonSave;
   }

   @Override
   public ListBox getTemplatesList()
   {
      return this.templateListBox;
   }

   @Override
   public Panel getFieldsPanel()
   {
      return this.fieldsTemplate;
   }

   @Override
   public Map<String, String> getFields()
   {
      final Map<String, String> returnMap = new HashMap<String, String>();
      final int rowCount = this.fieldGrid.getRowCount();
      for (int rowIndex = 1; rowIndex < rowCount; rowIndex++)
      {
         final String name = this.fieldGrid.getText(rowIndex, 0);
         final Widget value = this.fieldGrid.getWidget(rowIndex, 2);
         returnMap.put(name, ((TextBoxBase) value).getValue());

      }
      return returnMap;
   }

   @Override
   public void setFields(final List<TemplateFieldDTO> pFields, final Map<String, String> pValue)
   {
      // Clear current grid
      final int rowCount = this.fieldGrid.getRowCount() - 1;
      for (int rowRemovedIndex = rowCount; rowRemovedIndex >= 0; rowRemovedIndex--)
      {
         this.fieldGrid.removeRow(rowRemovedIndex);
      }

      // Fill grid
      if (pFields != null)
      {
         int rowIndex = 0;
         // Add first row which define titles
         this.fieldGrid.insertRow(rowIndex);
         this.fieldGrid.setWidget(rowIndex, 0, new Label(DeliveryCommon.getMessages().noteTemplateName()));
         this.fieldGrid.setWidget(rowIndex, 1, new Label(DeliveryCommon.getMessages()
               .noteTemplateDescription()));
         this.fieldGrid.setWidget(rowIndex, 2, new Label(DeliveryCommon.getMessages().noteTemplateValue()));

         for (final TemplateFieldDTO templateFieldDTO : pFields)
         {
            rowIndex++;
            this.fieldGrid.insertRow(rowIndex);
            this.fieldGrid.setWidget(rowIndex, 0, new Label(templateFieldDTO.getName()));
            this.fieldGrid.setWidget(rowIndex, 1, new Label(templateFieldDTO.getDescription()));
            final String value = pValue.get(templateFieldDTO.getName());
            if (TemplateFieldTypeDTO.TEXTAREA.equals(templateFieldDTO.getType()))
            {
               final TextArea textArea = new TextArea();
               textArea.setValue(value);
               this.fieldGrid.setWidget(rowIndex, 2, textArea);

            }
            else
            {
               final TextBox textBox = new TextBox();
               textBox.setValue(value);
               this.fieldGrid.setWidget(rowIndex, 2, textBox);

            }
         }
         this.setGridStyle();
      }
   }

   @Override
   public Grid getFieldsGrid()
   {
      return this.fieldGrid;
   }

   @Override
   public Label getTemplateInfoLabel()
   {
      return this.templateInfo;
   }

   /**
    *
    */
   private void setGridStyle()
   {
      // Column style
      this.fieldGrid.getColumnFormatter().setWidth(0, "10%");
      this.fieldGrid.getColumnFormatter().setWidth(1, "25%");
      this.fieldGrid.getColumnFormatter().setWidth(2, "35%");

      // Row style
      this.fieldGrid.getRowFormatter().addStyleName(0, Common.getResources().css().important());
      this.fieldGrid.getRowFormatter().addStyleName(0,
            DeliveryCommon.getResources().css().templateGridTitle());
      for (int row = 0; row < this.fieldGrid.getRowCount(); row++)
      {
         this.fieldGrid.getRowFormatter().addStyleName(row, Common.getResources().css().textCenter());

         if ((row % 2) == 1)
         {
            this.fieldGrid.getRowFormatter().addStyleName(row, Common.getResources().css().gridRowPair());

         }
      }
   }

   interface NoteViewImplUiBinder extends UiBinder<Widget, NoteViewImpl>
   {
   }
}
