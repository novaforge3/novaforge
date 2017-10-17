/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.widgets.quality.client.admin;

import java.util.Locale;

import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.widgets.quality.module.AbstractQualityModule;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;

/**
 * @author Cart Gauthier
 */
public class AdminViewImpl extends FormLayout implements AdminView
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -6029422061181687481L;

  private ComboBox          resourceBox;
  private Label             datasourceError;

  public AdminViewImpl()
  {
    super();

    final Component content = initContent();
    final Component error = initError();
    error.setVisible(false);
    addComponent(error);
    addComponent(content);

  }

  private Component initContent()
  {
    resourceBox = new ComboBox();
    resourceBox.setNullSelectionAllowed(false);
    return resourceBox;
  }

  private Component initError()
  {
    datasourceError = new Label();
    datasourceError.setStyleName(NovaForge.LABEL_BOLD);
    datasourceError.addStyleName(NovaForge.LABEL_ORANGE);
    return datasourceError;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOnError(final boolean pState)
  {
    datasourceError.setVisible(pState);
    resourceBox.setVisible(!pState);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {

    // Init error text
    datasourceError.setValue(AbstractQualityModule.getPortalMessages().getMessage(pLocale,
        Messages.QUALITYLIBRARIES_ADMIN_DATASOURCE));

    // Init resource combox text
    resourceBox.setTextInputAllowed(true);
    resourceBox.setInputPrompt(AbstractQualityModule.getPortalMessages().getMessage(pLocale,
        Messages.QUALITYLIBRARIES_ADMIN_RESOURCE_INPUT));
    resourceBox.setCaption(AbstractQualityModule.getPortalMessages().getMessage(pLocale,
        Messages.QUALITYLIBRARIES_ADMIN_RESOURCE));
    resourceBox.removeAllValidators();
    resourceBox.setRequired(true);
    resourceBox.setRequiredError(AbstractQualityModule.getPortalMessages().getMessage(pLocale,
        Messages.QUALITYLIBRARIES_ADMIN_RESOURCE_REQUIRE));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getResourceComboBox()
  {
    return resourceBox;
  }

}
