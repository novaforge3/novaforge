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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author lamirang
 */
public class ClickableImageResourceCell extends AbstractCell<ImageResource>
{
   private static final String KEYDOWN_ACTION = "keydown";
   private static final String CLICK_ACTION   = "click";
   private final String        toolTip;
   private boolean             addingToolTip  = false;

   /**
    * Construct a new ClickableImageResourceCell
    */
   public ClickableImageResourceCell()
   {
      this(null);
   }

   /**
    * Construct a new ClickableImageResourceCell with a title value
    * 
    * @param pToolTip
    *           represents the title value
    */
   public ClickableImageResourceCell(final String pToolTip)
   {
      super(CLICK_ACTION, KEYDOWN_ACTION);
      this.toolTip = pToolTip;

      if ((this.toolTip != null) && (!"".equals(this.toolTip)))
      {
         this.addingToolTip = true;
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void onBrowserEvent(final Context context, final Element parent, final ImageResource resource,
                              final NativeEvent event, final ValueUpdater<ImageResource> valueUpdater)
   {
      super.onBrowserEvent(context, parent, resource, event, valueUpdater);
      if (CLICK_ACTION.equals(event.getType()))
      {
         if (valueUpdater != null)
         {
            valueUpdater.update(resource);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void render(final Context context, final ImageResource value, final SafeHtmlBuilder sb)
   {
      if (value != null)
      {
         final StringBuilder span = new StringBuilder();
         span.append("<span style=\"cursor: pointer;\" ");
         // Adding tooltip to a div
         if (this.addingToolTip)
         {
            span.append("title=\"").append(this.toolTip).append("\"");
         }
         span.append(">");
         sb.appendHtmlConstant(span.toString());
         final AbstractImagePrototype create = AbstractImagePrototype.create(value);
         final SafeHtml html = SafeHtmlUtils.fromTrustedString(create.getHTML());
         sb.append(html);
         sb.appendHtmlConstant("</span>");
      }
   }

}
