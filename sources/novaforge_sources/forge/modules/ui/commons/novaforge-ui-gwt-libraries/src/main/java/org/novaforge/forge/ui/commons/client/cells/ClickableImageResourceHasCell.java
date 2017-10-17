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

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Guillaume Lamirand"
 * @param <T>
 */
public class ClickableImageResourceHasCell<T> implements HasCell<T, ImageResource>
{
   private final ClickableImageResourceCell     cell;
   private final FieldUpdater<T, ImageResource> fieldUpdater;
   private final ImageResource                  image;

   /**
    * Allow to build a <code>ClickableImageResourceHasCell</code> without ImageResource. You have to overide
    * <code>getValue</code> method.
    *
    * @see ClickableImageResourceHasCell#getValue(Object)
    * @param pToolTip
    *           represents the value of the title
    * @param pUpdater
    *           represents the updater which will be executed
    */
   public ClickableImageResourceHasCell(final String pToolTip, final FieldUpdater<T, ImageResource> pUpdater)
   {
      this(null, pToolTip, pUpdater);
   }

   /**
    * Default constructor.
    *
    * @param pImage
    *           represents the image to display
    * @param pToolTip
    *           represents the value of the title
    * @param pUpdater
    *           represents the updater which will be executed
    */
   public ClickableImageResourceHasCell(final ImageResource pImage, final String pToolTip,
                                        final FieldUpdater<T, ImageResource> pUpdater)
   {
      this.image = pImage;
      this.fieldUpdater = pUpdater;
      this.cell = new ClickableImageResourceCell(pToolTip);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Cell<ImageResource> getCell()
   {
      return this.cell;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public FieldUpdater<T, ImageResource> getFieldUpdater()
   {
      return this.fieldUpdater;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ImageResource getValue(final T pObject)
   {
      return this.image;
   }

}
