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
package org.novaforge.forge.ui.historization.client.view.commons.dialogbox;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import org.novaforge.forge.ui.historization.client.properties.LoggingMessage;

/**
 * @author qsivan
 */
public class ValidatePurgeDialogBox extends Composite
{
  private final LoggingMessage loggingMessages = (LoggingMessage) GWT.create(LoggingMessage.class);

  private final Button         closeButton;
  private final Button         validateButton;
  private final DialogBox      dialogBox;
  private final Label          details;

  public ValidatePurgeDialogBox(final String pContent)
  {
    dialogBox = new DialogBox();
    dialogBox.setText(loggingMessages.popupPurgeValidateTitle());
    dialogBox.setGlassEnabled(true);
    dialogBox.setAnimationEnabled(true);
    // Create the content panel
    final VerticalPanel dialogContents = new VerticalPanel();
    dialogContents.setSpacing(4);

    // Add content text to dialog box
    details = new Label(pContent);

    // Add validation button
    final HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.setSpacing(5);

    validateButton = new Button(loggingMessages.buttonConfirm());
    buttonPanel.add(validateButton);

    closeButton = new Button(loggingMessages.buttonCancel(), new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent pEvent)
      {
        dialogBox.hide();
      }
    });
    buttonPanel.add(closeButton);

    // Set panel and dialogbox content
    dialogContents.add(details);
    dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);
    dialogContents.add(buttonPanel);
    dialogContents.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
    dialogBox.add(dialogContents);

  }

  public Label getDetails()
  {
    return details;
  }

  public HasClickHandlers getValidate()
  {
    return validateButton;
  }

  public HasClickHandlers getClose()
  {
    return closeButton;
  }

  public PopupPanel getDialogPanel()
  {
    return dialogBox;
  }
}
