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

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.dd.VAcceptCallback;
import com.vaadin.client.ui.dd.VDragEvent;
import fi.jasoft.dragdroplayouts.client.ui.VDDAbstractDropHandler;

public class VDDTabsheetImprovedDropHandler extends VDDAbstractDropHandler<VDDTabsheetImproved>
{

  public VDDTabsheetImprovedDropHandler(ComponentConnector connector)
  {
    super(connector);
  }

  @Override
  protected void dragAccepted(VDragEvent drag)
  {
    dragOver(drag);
  }

  @Override
  public void dragOver(VDragEvent drag)
  {

    getLayout().deEmphasis();

    getLayout().updateDragDetails(drag);

    getLayout().postOverHook(drag);

    // Validate the drop
    validate(new VAcceptCallback()
    {
      public void accepted(VDragEvent event)
      {
        getLayout().emphasis(event.getElementOver(), event);
      }
    }, drag);
  }

  @Override
  public void dragLeave(VDragEvent drag)
  {
    getLayout().deEmphasis();
    getLayout().updateDragDetails(drag);
    getLayout().postLeaveHook(drag);
  }

  @Override
  public boolean drop(VDragEvent drag)
  {

    getLayout().deEmphasis();

    // Update the details
    getLayout().updateDragDetails(drag);
    return getLayout().postDropHook(drag) && super.drop(drag);
  }

}
