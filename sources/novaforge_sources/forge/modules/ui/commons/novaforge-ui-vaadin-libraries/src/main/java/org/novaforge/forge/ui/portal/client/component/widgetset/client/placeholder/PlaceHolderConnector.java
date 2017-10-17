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

package org.novaforge.forge.ui.portal.client.component.widgetset.client.placeholder;

import com.vaadin.client.extensions.AbstractExtensionConnector;
import org.novaforge.forge.ui.portal.client.component.PlaceHolder;

@com.vaadin.shared.ui.Connect(PlaceHolder.class)
public class PlaceHolderConnector extends AbstractExtensionConnector
    implements com.vaadin.client.communication.StateChangeEvent.StateChangeHandler
{

  /**
   *
   */
  private static final long serialVersionUID = -8639149032444707332L;
  private com.vaadin.client.ui.VTextField textField;

  @Override
  protected void extend(com.vaadin.client.ServerConnector target)
  {
    target.addStateChangeHandler(new com.vaadin.client.communication.StateChangeEvent.StateChangeHandler()
    {

      /**
       *Serial version Id
       */
      private static final long serialVersionUID = -6695821855850841782L;

      @Override
      public void onStateChanged(com.vaadin.client.communication.StateChangeEvent stateChangeEvent)
      {
        com.google.gwt.core.client.Scheduler.get()
                                            .scheduleDeferred(new com.google.gwt.core.client.Scheduler.ScheduledCommand()
                                            {
                                              @Override
                                              public void execute()
                                              {
                                                updatePlaceHolder();
                                              }
                                            });
      }
    });
    textField = (com.vaadin.client.ui.VTextField) ((com.vaadin.client.ComponentConnector) target).getWidget();
  }

  private void updatePlaceHolder()
  {
    textField.getElement().setAttribute("placeholder", getState().getPlaceholder());
  }

  @Override
  public org.novaforge.forge.ui.portal.client.component.widgetset.client.placeholder.shared.PlaceHolderState getState()
  {
    return (org.novaforge.forge.ui.portal.client.component.widgetset.client.placeholder.shared.PlaceHolderState) super
                                                                                                                     .getState();
  }

}