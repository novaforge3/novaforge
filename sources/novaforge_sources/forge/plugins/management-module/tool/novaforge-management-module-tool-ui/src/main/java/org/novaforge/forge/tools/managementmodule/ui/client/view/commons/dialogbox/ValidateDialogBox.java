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
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * @author lamirang
 */
public class ValidateDialogBox extends Composite
{
   private final Button        closeButton;
   private final Button        validateButton;
   private final DialogBox     dialogBox;
   private final Label		   details;

   public ValidateDialogBox(final String pContent)
   {
      this.dialogBox = new DialogBox();
      this.dialogBox.setText(Common.getGlobal().popupValidate());
      this.dialogBox.setGlassEnabled(true);
      this.dialogBox.setAnimationEnabled(true);
      // Create the content panel
      VerticalPanel dialogContents = new VerticalPanel();
      dialogContents.setSpacing(4);

      // Add content text to dialog box
      details = new Label(pContent);

      // Add validation button
      HorizontalPanel buttonPanel = new HorizontalPanel();
      buttonPanel.setSpacing(5);

      this.validateButton = new Button(Common.getGlobal().yes());
      buttonPanel.add(this.validateButton);

      this.closeButton = new Button(Common.getGlobal().no(), new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent pEvent)
         {
            ValidateDialogBox.this.dialogBox.hide();
         }
      });
      buttonPanel.add(this.closeButton);


      // Set panel and dialogbox content
      dialogContents.add(details);
      dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);
      dialogContents.add(buttonPanel);
      dialogContents.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
      this.dialogBox.add(dialogContents);

   }

   public HasClickHandlers getValidate()
   {
      return this.validateButton;
   }

   public HasClickHandlers getClose()
   {
      return this.closeButton;
   }

   public PopupPanel getDialogPanel()
   {
      return this.dialogBox;
   }
	
	public void setDetails(String pContent){
		this.details.setText(pContent);
	}

}

