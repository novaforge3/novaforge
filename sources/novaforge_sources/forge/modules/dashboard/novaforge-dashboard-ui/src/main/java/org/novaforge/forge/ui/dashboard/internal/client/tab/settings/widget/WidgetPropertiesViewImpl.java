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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.widget;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
@SuppressWarnings("deprecation")
public class WidgetPropertiesViewImpl extends VerticalLayout implements WidgetPropertiesView
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 3529814710722435066L;
  private final Button      saveButton;
  private Form widgetForm;
  private TextField         widgetName;
  private Form              infoForm;

  /**
   * Default constructor
   */
  public WidgetPropertiesViewImpl()
  {
    setMargin(true);

    final Component infoForm = initInfoForm();
    final Component widgetForm = initWidget();
    saveButton = new Button();
    saveButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    saveButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));

    addComponent(infoForm);
    addComponent(widgetForm);
    addComponent(saveButton);
    setComponentAlignment(saveButton, Alignment.MIDDLE_CENTER);
  }

  private Component initInfoForm()
  {
    infoForm = new Form();
    infoForm.setImmediate(true);
    infoForm.setInvalidCommitted(false);
    infoForm.setFooter(null);

    widgetName = new TextField();
    widgetName.setImmediate(true);

    infoForm.addField("widgetName", widgetName);
    return infoForm;
  }

  private Component initWidget()
  {
    widgetForm = new Form();
    widgetForm.setImmediate(true);
    widgetForm.setInvalidCommitted(false);
    widgetForm.setFooter(null);

    return widgetForm;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    infoForm.setCaption(portalMessages.getMessage(pLocale, Messages.DASHBOARD_SETTINGS_WIDGET_INFO_TITLE));
    widgetName.setCaption(portalMessages.getMessage(pLocale, Messages.DASHBOARD_SETTINGS_WIDGET_INFO_NAME));
    widgetName.setDescription(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_SETTINGS_WIDGET_INFO_NAME_DESCRIPTION));
    widgetName.removeAllValidators();
    widgetName.addValidator(new StringLengthValidator(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_SETTINGS_WIDGET_INFO_NAME_VALIDATOR), 1, 26, false));

    widgetForm.setCaption(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_SETTINGS_WIDGET_PROPERTIES_TITLE));

    saveButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_SAVE));

  }

  /**
   * @return the infoForm
   */
  @Override
  public TextField getWidgetNameField()
  {
    return widgetName;
  }

  /**
   * @return the widgetForm
   */
  @Override
  public Form getWidgetForm()
  {
    return widgetForm;
  }

  /**
   * @return the widgetForm
   */
  @Override
  public Button getSaveButton()
  {
    return saveButton;
  }

}
