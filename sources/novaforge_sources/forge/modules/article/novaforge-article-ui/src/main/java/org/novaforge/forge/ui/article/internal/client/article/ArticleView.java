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
package org.novaforge.forge.ui.article.internal.client.article;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import org.vaadin.virkki.carousel.HorizontalCarousel;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public interface ArticleView extends ComponentContainer
{

  /**
   * Refresh localized label in the view
   * 
   * @param pLocale
   *          , the locale to use
   */
  void refreshLocale(Locale pLocale);

  /**
   * Get the Admin button
   * 
   * @return the button
   */
  Button getAdminButton();

  /**
   * Get the Information Text Label
   * 
   * @return the label
   */
  Label getInformationText();

  /**
   * Get the Information Title Label
   * 
   * @return the label
   */
  Label getInformationTitle();

  /**
   * Get the News Grid
   * 
   * @return the {@link ItemGrid}
   */
  ItemGrid getNewsGrid();

  /**
   * Get the filter Text TextField
   * 
   * @return the TextField
   */
  TextField getFilterText();

  /**
   * Get the filter Category Layout
   * 
   * @return the category layout
   */
  VerticalLayout getFilterCategoryLayout();

  /**
   * Get the Filter Button
   * 
   * @return the button
   */
  Button getFilterButton();

  /**
   * Get the Filter DateFrom DateField
   * 
   * @return the DateField
   */
  DateField getFilterDateFrom();

  /**
   * Get the Filter DateTo DateFiel
   * 
   * @return the DateField
   */
  DateField getFilterDateTo();

  /**
   * Get tha Announcement Carousel
   * 
   * @return the carousel
   */
  HorizontalCarousel getAnnouncementCarousel();

  /**
   * Get the Filter Reset Button
   * 
   * @return the button
   */
  Button getFilterResetButton();

}