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
package org.novaforge.forge.ui.article.internal.client.admin.announcement.create;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import org.novaforge.forge.article.model.Article;

import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public interface AdminAnnouncementCreateView extends ComponentContainer
{

  /**
   * Bind the view to the given article
   * 
   * @param pArticle
   *          the article to bind
   */
  void bindToArticle(Article pArticle);

  /**
   * Get the back to announcement list button
   * 
   * @return the button
   */
  Button getBackToListButton();

  /**
   * Get the title label
   * 
   * @return the label
   */
  Label getTitle();

  /**
   * Get the form binders
   * 
   * @return the form binder list
   */
  List<FieldGroup> getFormBinders();

  /**
   * Get the form layout
   * 
   * @return the layout
   */
  FormLayout getFormLayout();

  /**
   * Get the create button
   * 
   * @return the button
   */
  Button getCreateButton();

  /**
   * Refresh the internationnalized label with the given label
   * 
   * @param pLocale
   *          the locale to use
   */
  void refreshLocale(Locale pLocale);

  /**
   * Get the category field
   * 
   * @return the combobox
   */
  ComboBox getCategoryField();

  /**
   * Set if view is in edit mode or in create mode
   * 
   * @param pIsUpdateMode
   *          , true to set edit mode, false to create mode
   */
  void setUpdateMode(boolean pIsUpdateMode);

}
