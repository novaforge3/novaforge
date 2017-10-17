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
package org.novaforge.forge.ui.portal.client.component;

import com.google.common.base.Strings;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;

import java.util.Locale;

/**
 * @author caseryj
 */
public class DeleteConfirmWindow extends Window
{

  /**
   * 
   */
  private static final long serialVersionUID = 3687361217729769710L;
  private final static int  WINDOW_WIDTH     = 450;
  private final Label  textLabel = new Label();
  private final Button yesButton = new Button();
  private final Button noButton  = new Button();
  private String            textMessageId;
  private String            parameterMessage;
  private String            yesMessageId;

  public DeleteConfirmWindow()
  {
    this(null);
  }

  public DeleteConfirmWindow(final String pI18NMessageId)
  {
    super();
    textMessageId = pI18NMessageId;
    yesMessageId = Messages.DELETE_WINDOW_YES;
    setModal(true);
    setResizable(false);
    setIcon(new ThemeResource(NovaForgeResources.ICON_TRASH_WHITE));
    setWidth(WINDOW_WIDTH, Unit.PIXELS);
    initWindowContent();

  }

  private void initWindowContent()
  {
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setSpacing(true);
    windowLayout.setMargin(true);
    textLabel.setContentMode(ContentMode.HTML);

    final HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.setWidth(100, Unit.PERCENTAGE);
    buttonsLayout.setSpacing(true);
    buttonsLayout.setMargin(new MarginInfo(true, true, false, true));
    yesButton.setStyleName(NovaForge.BUTTON_DELETE);
    yesButton.setIcon(new ThemeResource(NovaForgeResources.ICON_TRASH_WHITE));
    yesButton.addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 814101120621605015L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(DeleteConfirmWindow.this);
      }
    });
    noButton.setIcon(new ThemeResource(NovaForgeResources.ICON_BLOCKED_GREY));
    noButton.addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 814101120621605015L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(DeleteConfirmWindow.this);
      }
    });
    buttonsLayout.addComponent(noButton);
    buttonsLayout.addComponent(yesButton);
    buttonsLayout.setComponentAlignment(noButton, Alignment.MIDDLE_LEFT);
    buttonsLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_RIGHT);
    windowLayout.addComponent(textLabel);
    windowLayout.addComponent(buttonsLayout);
    windowLayout.setComponentAlignment(textLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
    windowLayout.setExpandRatio(textLabel, 1);
    windowLayout.setExpandRatio(buttonsLayout, 0);
    setContent(windowLayout);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());

  }

  public void refreshLocale(final Locale pLocale)
  {
    setCaption(OSGiHelper.getPortalMessages().getMessage(UI.getCurrent().getLocale(), Messages.DELETE_WINDOW_TITLE));
    if (!Strings.isNullOrEmpty(textMessageId))
    {
      if (!Strings.isNullOrEmpty(parameterMessage))
      {
        textLabel.setValue(OSGiHelper.getPortalMessages().getMessage(UI.getCurrent().getLocale(), textMessageId,
                                                                     parameterMessage));
      }
      else
      {
        textLabel.setValue(OSGiHelper.getPortalMessages().getMessage(UI.getCurrent().getLocale(), textMessageId));

      }
    }
    else
    {
      textLabel.setValue(OSGiHelper.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                                   Messages.DELETE_WINDOW_DEFAULT_TEXT));
    }
    yesButton.setCaption(OSGiHelper.getPortalMessages().getMessage(UI.getCurrent().getLocale(), yesMessageId));
    noButton.setCaption(OSGiHelper.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                                  Messages.DELETE_WINDOW_NO));

  }

  public void setParameterMessage(final String pParameter)
  {
    parameterMessage = pParameter;
    if (!Strings.isNullOrEmpty(textMessageId))
    {
      textLabel.setValue(OSGiHelper.getPortalMessages().getMessage(UI.getCurrent().getLocale(), textMessageId,
                                                                   parameterMessage));
    }
  }

  public void setWindowMessageTextId(final String pI18NMessageId)
  {
    textMessageId = pI18NMessageId;
  }

  public void setYesButtonMessageId(final String pI18NMessageId)
  {
    yesMessageId = pI18NMessageId;
  }

  public Button getYesButton()
  {
    return yesButton;
  }

  public Button getNoButton()
  {
    return noButton;
  }

}
