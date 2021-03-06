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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * @author caseryj
 */
public class ContentDialogBox extends Composite
{

   private final DialogBox     dialogBox;
   private final VerticalPanel                 content;


   public ContentDialogBox(String pTitle)
   {
      dialogBox = new DialogBox();
      dialogBox.setText(pTitle);
      dialogBox.setTitle(pTitle);
      dialogBox.setGlassEnabled(true);
      dialogBox.setAnimationEnabled(true);
      // Create the content panel
      VerticalPanel dialogContents = new VerticalPanel();
      // Add content text to dialog box
      HorizontalPanel horizontal = new HorizontalPanel();
      content = new VerticalPanel();
      horizontal.add(content);
      horizontal.setCellVerticalAlignment(content, HasVerticalAlignment.ALIGN_MIDDLE);

      // Add validation button
      Button closeButton = new Button(Common.getGlobal().buttonClose(), new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent pEvent)
         {
            dialogBox.hide();
         }
      });

      // Set panel and dialogbox content
      dialogContents.add(horizontal);
      dialogContents.setCellHorizontalAlignment(horizontal, HasHorizontalAlignment.ALIGN_CENTER);
      dialogContents.add(closeButton);
      dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
      dialogBox.add(dialogContents);

   }

   public VerticalPanel getContentPanel()
   {
      return content;
   }

   public PopupPanel getDialogPanel()
   {
      return dialogBox;
   }

}
