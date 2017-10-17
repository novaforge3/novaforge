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
package org.novaforge.forge.widgets.bugtracker.issuedivision.internal.client.admin;

import java.util.Locale;

import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.widgets.bugtracker.issuedivision.internal.module.IssueDivisionModule;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

/**
 * @author bruno
 */
@SuppressWarnings("serial")
public class AdminViewImpl extends FormLayout implements AdminView
{

  private ComboBox comboBox;

  public AdminViewImpl()
  {
    super();

    final Component content = initContent();

    addComponent(content);

  }

  private Component initContent()
  {
    comboBox = new ComboBox();
    comboBox.setNullSelectionAllowed(true);
    return comboBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    final String caption = IssueDivisionModule.getPortalMessages().getMessage(pLocale,
        Messages.WIDGET_BUGTRACKER_VERSION);
    comboBox.setCaption(caption);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getVersionComboBox()
  {
    return comboBox;
  }

}
