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
package org.novaforge.forge.ui.commons.client.cells;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;

public class CustomButtonCell extends AbstractSafeHtmlCell<String>
{

   private String  disabledString = "";
   private boolean disabled       = false;

   public CustomButtonCell()
   {
      this(SimpleSafeHtmlRenderer.getInstance());
   }

   public CustomButtonCell(final SafeHtmlRenderer<String> renderer)
   {
      super(renderer, "click", "keydown");
   }

   @Override
   public void render(final Context context, final SafeHtml data, final SafeHtmlBuilder sb)
   {
      sb.appendHtmlConstant("<button type=\"button\" tabindex=\"-1\"" + this.disabledString + ">");
      if (data != null)
      {
         sb.append(data);
      }
      sb.appendHtmlConstant("</button>");
   }

   @Override
   public void onBrowserEvent(final Context context, final Element parent, final String value,
         final NativeEvent event, final ValueUpdater<String> valueUpdater)
   {
      super.onBrowserEvent(context, parent, value, event, valueUpdater);
      if ("click".equals(event.getType()))
      {
         final EventTarget eventTarget = event.getEventTarget();
         if (!Element.is(eventTarget))
         {
            return;
         }
         if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget)))
         {
            this.onEnterKeyDown(context, parent, value, event, valueUpdater);
         }
      }
   }

   @Override
   protected void onEnterKeyDown(final Context context, final Element parent, final String value,
         final NativeEvent event, final ValueUpdater<String> valueUpdater)
   {
      if (valueUpdater != null)
      {
         valueUpdater.update(value);
      }
   }

   public boolean isDisabled()
   {
      return this.disabled;
   }

   public void setDisabled(final boolean disabled)
   {
      this.disabled = disabled;
      if (disabled)
      {
         this.disabledString = "disabled=\"disabled\"";
      }
      else
      {
         this.disabledString = "";
      }
   }

}
