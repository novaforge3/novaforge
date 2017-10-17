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
package org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.client.admin;

import java.util.Locale;

import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.portal.client.component.DateRangeComponent;
import org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.module.IssuesTimelineModule;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

/**
 * @author Jeremy Casery
 */
@SuppressWarnings("serial")
public class AdminViewImpl extends FormLayout implements AdminView
{
  private ComboBox           versionComboBox;
  private DateRangeComponent dateRange;

  public AdminViewImpl()
  {
    super();

    final Component version = initVersionContent();
    final Component dateRange = initDateRangeContent();

    addComponent(version);
    addComponent(dateRange);

  }

  private Component initVersionContent()
  {
    versionComboBox = new ComboBox();
    versionComboBox.setNullSelectionAllowed(true);
    return versionComboBox;
  }

  private Component initDateRangeContent()
  {
    dateRange = new DateRangeComponent();
    return dateRange;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    final PortalMessages portalMessages = IssuesTimelineModule.getPortalMessages();
    versionComboBox.setCaption(portalMessages.getMessage(pLocale, Messages.WIDGET_BUGTRACKER_VERSION));
    dateRange.refreshLocale(portalMessages, pLocale);
    dateRange.setCaption(portalMessages.getMessage(pLocale, Messages.BUGTRACKERISSUETIMELINE_ADMIN_PERIOD));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getVersionComboBox()
  {
    return versionComboBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DateRangeComponent getDateRangeComponent()
  {
    return dateRange;
  }

}
