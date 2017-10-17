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

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import fi.jasoft.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;
import fi.jasoft.dragdroplayouts.events.HorizontalLocationIs;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import org.novaforge.forge.ui.portal.client.component.DDTabsheetImproved.TabSheetTargetDetails;
import org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab;

/**
 * @author Guillaume Lamirand
 */
public class DefaultTabSheetImprovedDropHandler extends AbstractDefaultLayoutDropHandler
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -7261868866601731608L;

  @Override
  protected void handleComponentReordering(final DragAndDropEvent event)
  {
    final LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    final TabSheetTargetDetails details = (TabSheetTargetDetails) event.getTargetDetails();
    final DDTabsheetImproved tabSheet = (DDTabsheetImproved) details.getTarget();
    final Component c = transferable.getComponent();
    final Tab tab = tabSheet.getTab(c);
    final HorizontalDropLocation location = details.getDropLocation();
    final int idx = details.getOverIndex();

    if (location == HorizontalDropLocation.LEFT)
    {
      // Left of previous tab
      final int originalIndex = tabSheet.getTabPosition(tab);
      if (originalIndex > idx)
      {
        tabSheet.setTabPosition(tab, idx);
      }
      else if ((idx - 1) >= 0)
      {
        tabSheet.setTabPosition(tab, idx - 1);
      }

    }
    else if (location == HorizontalDropLocation.RIGHT)
    {
      // Right of previous tab
      final int originalIndex = tabSheet.getTabPosition(tab);
      if (originalIndex > idx)
      {
        tabSheet.setTabPosition(tab, idx + 1);
      }
      else
      {
        tabSheet.setTabPosition(tab, idx);
      }
    }

  }

  @Override
  protected void handleDropFromLayout(final DragAndDropEvent event)
  {
    final LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    final TabSheetTargetDetails details = (TabSheetTargetDetails) event.getTargetDetails();
    final DDTabsheetImproved tabSheet = (DDTabsheetImproved) details.getTarget();
    final Component c = transferable.getComponent();
    final HorizontalDropLocation location = details.getDropLocation();
    final int idx = details.getOverIndex();
    final ComponentContainer source = (ComponentContainer) transferable.getSourceComponent();

    source.removeComponent(c);
    if (location == HorizontalDropLocation.LEFT)
    {
      tabSheet.addTab(c, idx);
    }
    else if (location == HorizontalDropLocation.RIGHT)
    {
      tabSheet.addTab(c, idx + 1);
    }
  }

  @Override
  public AcceptCriterion getAcceptCriterion()
  {
    // Only allow drops between tabs
    return new Not(HorizontalLocationIs.CENTER);
  }
}
