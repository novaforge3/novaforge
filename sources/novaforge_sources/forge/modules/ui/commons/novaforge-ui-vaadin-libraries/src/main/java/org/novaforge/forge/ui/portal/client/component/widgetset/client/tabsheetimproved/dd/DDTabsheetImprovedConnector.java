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

package org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.dd;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.shared.ui.Connect;
import fi.jasoft.dragdroplayouts.client.VDragFilter;
import fi.jasoft.dragdroplayouts.client.ui.VDragDropUtil;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.VHasDragFilter;
import fi.jasoft.dragdroplayouts.client.ui.util.HTML5Support;
import org.novaforge.forge.ui.portal.client.component.DDTabsheetImproved;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.TabsheetImprovedConnector;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.dd.shared.DDTabsheetImprovedState;

@Connect(DDTabsheetImproved.class)
public class DDTabsheetImprovedConnector extends TabsheetImprovedConnector implements Paintable, VHasDragFilter
{

  private HTML5Support html5Support;

  @Override
  public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
  {
    VDDTabsheetImprovedDropHandler dropHandler = new VDDTabsheetImprovedDropHandler(this);
    VDragDropUtil.updateDropHandlerFromUIDL(uidl, this, dropHandler);
    if (html5Support != null)
    {
      html5Support.disable();
    }
    html5Support = HTML5Support.enable(this, dropHandler);
  }

  @Override
  public void onUnregister()
  {
    if (html5Support != null)
    {
      html5Support.disable();
      html5Support = null;
    }
    super.onUnregister();
  }

  @Override
  protected void init()
  {
    super.init();
    VDragDropUtil.listenToStateChangeEvents(this, getWidget());
  }

  @Override
  public VDragFilter getDragFilter()
  {
    return getWidget().getDragFilter();
  }

  @Override
  public void setDragFilter(VDragFilter filter)
  {
    getWidget().setDragFilter(filter);
  }

  @Override
  public VDDTabsheetImproved getWidget()
  {
    return (VDDTabsheetImproved) super.getWidget();
  }

  @Override
  public DDTabsheetImprovedState getState()
  {
    return (DDTabsheetImprovedState) super.getState();
  }

}
