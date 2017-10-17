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
package org.novaforge.forge.ui.portal.client.util;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import de.nlh.graphics2dimages.FixedWidthGraphics2DImage;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.awt.*;

/**
 * @author Jeremy Casery
 */
public class UserIconGenerator
{

  private final static WidgetColorGenerator COLOR_GENERATOR = new WidgetColorGenerator();

  private final static String               ICON_EXTENSION  = ".png";

  private final static int                  ICON_SIZE       = 128;

  public UserIconGenerator()
  {
  }

  public static Resource getIconForUser(final String pFirstName, final String pLastName)
  {
    if (pFirstName == null && pLastName == null)
    {
      return new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW);
    }
    else
    {
      String iconText = null;
      final String iconName = pFirstName + pLastName;
      if (pFirstName == null || pLastName == null)
      {
        iconText = iconName.substring(0, 2);
      }
      else
      {
        iconText = pFirstName.toUpperCase().substring(0, 1) + pLastName.toUpperCase().substring(0, 1);
      }
      return generateIconWithText(iconText, iconName);
    }

  }

  private static StreamResource generateIconWithText(final String pText, final String pIconName)
  {
    FixedWidthGraphics2DImage icon = generateIconFileWithText(pText, pIconName);
    return icon.getResource();
  }

  private static FixedWidthGraphics2DImage generateIconFileWithText(final String pText, final String pIconName)
  {

    final String finalIconName = pIconName.concat(Long.toString(System.currentTimeMillis())).concat(
        ICON_EXTENSION);
    return new FixedWidthGraphics2DImage(finalIconName, ICON_SIZE, ICON_SIZE)
    {

      /**
       *
       */
      private static final long serialVersionUID = 7863064331225233994L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void paint(final Graphics2D g)
      {
        Color color = COLOR_GENERATOR.getRandomColor();
        g.setColor(color);
        g.fillRect(0, 0, width - 1, height - 1);
        g.setColor(color);
        g.drawRect(0, 0, width - 1, height - 1);
        g.setColor(Color.WHITE);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, ICON_SIZE / 2);
        g.setFont(font);

        FontMetrics fm = g.getFontMetrics();
        int stringWidth = fm.stringWidth(pText);
        int marginLeft = (ICON_SIZE - stringWidth) / 2;
        int marginTop = ((ICON_SIZE - fm.getHeight()) / 2) + fm.getAscent();

        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.drawString(pText, marginLeft, marginTop);
      }

    };
  }

  public static FixedWidthGraphics2DImage getIconFileForUser(final String pFirstName, final String pLastName)
  {
    FixedWidthGraphics2DImage icon = null;
    if (pFirstName != null || pLastName != null)
    {
      String iconText = null;
      final String iconName = pFirstName + pLastName;
      if (pFirstName == null || pLastName == null)
      {
        iconText = iconName.substring(0, 2);
      }
      else
      {
        iconText = pFirstName.toUpperCase().substring(0, 1) + pLastName.toUpperCase().substring(0, 1);
      }

      icon = generateIconFileWithText(iconText, iconName);
    }
    return icon;

  }

}
