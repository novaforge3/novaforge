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

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Jeremy Casery
 */
public class WidgetColorGenerator
{

  private static final Color COLOR_GREEN    = new Color(119, 170, 84);
  private static final Color COLOR_YELLOW   = new Color(255, 188, 65);
  private static final Color COLOR_RED      = new Color(194, 71, 88);

  private final static List<Color> FIXED_COLOR = Collections.unmodifiableList(new ArrayList<Color>()
                                            {
                                              {
                                                add(new Color(74, 112, 147));
                                                add(new Color(73, 207, 229));
                                                add(COLOR_YELLOW);
                                                add(COLOR_RED);
                                                add(COLOR_GREEN);
                                                add(new Color(112, 113, 200));
                                                add(new Color(197, 143, 186));
                                                add(new Color(181, 185, 198));
                                              }
                                            });

  private final static List<Color> EXCLUDE_COLOR = Collections.unmodifiableList(new ArrayList<Color>()
                                            {
                                              {
                                                add(new Color(131, 178, 87)); // #83B257 NovaForge Green
                                              }
                                            });

  private final Random       colorGenerator = new Random();
  private float              saturation;
  private float              brightness;

  private Set<Color>         usedColors     = new HashSet<Color>();

  /**
   * Create a new default WidgetColorGenerator, will generate colors after 8 fixed
   * You will never have same color twice, but you can get colors many close
   * Default values are :
   * - saturation : 1.0f
   * - brightness : 0.7f
   * - Generate always same colors in same order : true
   */
  public WidgetColorGenerator()
  {
    this(1.0f, 0.7f, true);
  }

  /**
   * Create a new WidgetColorGenerator
   * You will never have same color twice, but you can get colors many close
   *
   * @param pColorStaturation
   *          , The saturation for generated colors
   * @param pColorBrightness
   *          , The brightness for generated colors
   * @param pAlwaysSameColorInSameOrder
   *          , define if generator must generate always the same colors in same order
   */
  public WidgetColorGenerator(final float pColorStaturation, final float pColorBrightness,
      final boolean pAlwaysSameColorInSameOrder)
  {
    saturation = pColorStaturation;
    brightness = pColorBrightness;
    if (pAlwaysSameColorInSameOrder)
    {
      colorGenerator.setSeed(24);
    }
    usedColors.addAll(EXCLUDE_COLOR);
  }

  /**
   * Create a new WidgetColorGenerator, will generate colors after 8 fixed and will always returns generate
   * colors in same order
   * You will never have same color twice, but you can get colors many close
   *
   * @param pColorStaturation
   *          , The saturation for generated colors
   * @param pColorBrightness
   *          , The brightness for generated colors
   */
  public WidgetColorGenerator(final float pColorStaturation, final float pColorBrightness)
  {
    this(pColorStaturation, pColorBrightness, true);
  }

  /**
   * Get the Red color
   *
   * @return the color
   */
  public static Color getRed()
  {
    return COLOR_RED;
  }

  /**
   * Get the Yellow color
   *
   * @return the color
   */
  public static Color getYellow()
  {
    return COLOR_YELLOW;
  }

  /**
   * Get the Green color
   *
   * @return the color
   */
  public static Color getGreen()
  {
    return COLOR_GREEN;
  }

  /**
   * Get the next available color
   *
   * @return {@link Color}
   */
  public Color getNextColor()
  {
    Color color = null;
    Iterator<Color> fixedColorsIterator = FIXED_COLOR.iterator();
    do
    {
      color = fixedColorsIterator.next();
    }
    while (!usedColors.add(color) && fixedColorsIterator.hasNext());
    if (usedColors.size() >= FIXED_COLOR.size())
    {
      color = getNextRandomColor();
    }
    return color;
  }

  /**
   * Get next random color
   *
   * @return {@link Color}
   */
  private Color getNextRandomColor()
  {
    Color color = null;
    do
    {
      final float hue = colorGenerator.nextFloat();
      final int rgb = Color.HSBtoRGB(hue, saturation, brightness);
      color = new Color(rgb);
    }
    while (!usedColors.add(color));

    return color;
  }

  /**
   * Generate a random color, and don't check if color has been previously used
   *
   * @return a random {@link Color}
   */
  public Color getRandomColor()
  {
    Random generator = new Random();
    int randomIndex = generator.nextInt((FIXED_COLOR.size() * 2) - 1);
    if (randomIndex < FIXED_COLOR.size())
    {
      return FIXED_COLOR.get(randomIndex);
    }
    else
    {
      return generateRandomColor();
    }
  }

  /**
   * Generate a random color
   *
   * @return {@link Color}
   */
  private Color generateRandomColor()
  {
    final float hue = colorGenerator.nextFloat();
    final int rgb = Color.HSBtoRGB(hue, saturation, brightness);

    return new Color(rgb);
  }

  /**
   * Reset the current used colors, to get again from first
   */
  public void resetUsedColors()
  {
    usedColors.clear();
  }

}
