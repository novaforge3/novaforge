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
package org.novaforge.forge.tools.deliverymanager.ui.client.view.commons.celltree.nodesselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.novaforge.forge.ui.commons.client.Common;

/**
 * @author caseryj
 */
public class EditFolderDialogBox extends Composite
{

   private static NodesSelectionMessage messages = GWT.create(NodesSelectionMessage.class);
   private final Button                 closeButton;
   private final Button                 validateButton;
   private final DialogBox              dialogBox;
   private final TextBox                textBox;
   private final Label                  folderPath;
   private boolean                      isCreateMode;

   public EditFolderDialogBox(final String pTitle)
   {
      this.dialogBox = new DialogBox();
      this.dialogBox.setText(pTitle);
      this.dialogBox.setGlassEnabled(true);
      this.dialogBox.setAnimationEnabled(true);
      // Create the content panel
      final VerticalPanel dialogContents = new VerticalPanel();
      dialogContents.setSpacing(4);

      // Add content text to dialog box
      final Label details = new Label(messages.folderNameLabel());

      this.folderPath = new Label();

      // Add text box to get value
      final HorizontalPanel messagePanel = new HorizontalPanel();
      messagePanel.setSpacing(5);
      this.textBox = new TextBox();
      messagePanel.add(this.folderPath);
      messagePanel.add(this.textBox);

      // Add validation button
      final HorizontalPanel buttonPanel = new HorizontalPanel();
      buttonPanel.setSpacing(5);

      this.validateButton = new Button(Common.getMessages().confirm());
      buttonPanel.add(this.validateButton);

      this.closeButton = new Button(Common.getMessages().cancel(), new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent pEvent)
         {
            EditFolderDialogBox.this.dialogBox.hide();
            EditFolderDialogBox.this.getTextBox().setText(null);
         }
      });
      buttonPanel.add(this.closeButton);

      // Set panel and dialogbox content
      dialogContents.add(details);
      dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);
      dialogContents.add(messagePanel);
      dialogContents.setCellHorizontalAlignment(messagePanel, HasHorizontalAlignment.ALIGN_CENTER);
      dialogContents.add(buttonPanel);
      dialogContents.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
      this.dialogBox.add(dialogContents);

   }

   public TextBox getTextBox()
   {
      return this.textBox;
   }

   public HasClickHandlers getValidate()
   {
      return this.validateButton;
   }

   public Button getValidateButton()
   {
      return this.validateButton;
   }

   public HasClickHandlers getClose()
   {
      return this.closeButton;
   }

   public Label getFolderPath()
   {
      return this.folderPath;
   }

   public PopupPanel getDialogPanel()
   {
      return this.dialogBox;
   }

   public DialogBox getDialogBox()
   {
      return this.dialogBox;
   }

   /**
    * @return the isCreateMode
    */
   public boolean isCreateMode()
   {
      return this.isCreateMode;
   }

   /**
    * @param pIsCreateMode
    *           the isCreateMode to set
    */
   public void setCreateMode(final boolean pIsCreateMode)
   {
      this.isCreateMode = pIsCreateMode;
   }

}
