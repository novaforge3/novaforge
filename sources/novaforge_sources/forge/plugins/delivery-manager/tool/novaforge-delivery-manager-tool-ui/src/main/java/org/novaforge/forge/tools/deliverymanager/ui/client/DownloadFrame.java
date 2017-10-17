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
package org.novaforge.forge.tools.deliverymanager.ui.client;

import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This allow to open an IFRAME on specific url in order to download its content.
 * 
 * @author Guillaume Lamirand
 */
public class DownloadFrame extends Frame implements LoadHandler, HasLoadHandlers
{
   private static final String DOWNLOAD_FRAME = "__gwt_downloadFrame";

   /**
    * @param url
    *           the url
    */
   public DownloadFrame(final String url)
   {
      super();
      this.setSize("0px", "0px");
      this.setVisible(false);
      final RootPanel rp = RootPanel.get(DownloadFrame.DOWNLOAD_FRAME);
      if (rp != null)
      {
         this.addLoadHandler(this);
         rp.add(this);
         this.setUrl(url);
      }
      else
      {
         this.openURLInNewWindow(url);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HandlerRegistration addLoadHandler(final LoadHandler handler)
   {
      return this.addHandler(handler, LoadEvent.getType());
   }

   native void openURLInNewWindow(String url) /*-{
    $wnd.open(url);
   }-*/;

   /**
    * {@inheritDoc}
    */
   @Override
   public void onLoad(final LoadEvent event)
   {
   }
}
