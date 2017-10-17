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
package org.novaforge.forge.ui.commons.client.dialogbox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.novaforge.forge.ui.commons.client.Common;

/**
 * @author caseryj
 */
public class ContentDialogBox extends AbstractCustomDialogBox
{
   private final VerticalPanel content;

   public ContentDialogBox(final String pTitle)
   {
      super(pTitle);
      // Create the content panel
      final VerticalPanel dialogContents = new VerticalPanel();
      // Add content text to dialog box
      final HorizontalPanel horizontal = new HorizontalPanel();
      this.content = new VerticalPanel();
      horizontal.add(this.content);
      horizontal.setCellVerticalAlignment(this.content, HasVerticalAlignment.ALIGN_MIDDLE);

      // Add validation button
      final Button closeButton = new Button(Common.getMessages().close(), new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent pEvent)
         {
            ContentDialogBox.this.getDialogBox().hide();
         }
      });

      // Set panel and dialogbox content
      dialogContents.add(horizontal);
      dialogContents.setCellHorizontalAlignment(horizontal, HasHorizontalAlignment.ALIGN_CENTER);
      dialogContents.add(closeButton);
      dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
      this.getDialogBox().add(dialogContents);

   }

   public VerticalPanel getContentPanel()
   {
      return this.content;
   }

}
