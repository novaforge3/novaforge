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
package org.novaforge.forge.ui.requirements.internal.client.requirement.list;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import org.novaforge.forge.ui.requirements.internal.client.requirement.details.DetailsView;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public interface RequirementListView extends ComponentContainer
{

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Get the requirements {@link Tree}
   * 
   * @return the tree
   */
  Tree getRequirementsTree();

  /**
   * Show or hide the details layout
   * 
   * @param pIsVisible
   *          true to show, false otherwise
   */
  void showDetailsLayout(boolean pIsVisible);

  /**
   * Get the {@link DetailsView}
   * 
   * @return the view
   */
  DetailsView getDetailsView();

  /**
   * Get the requirement filter {@link TextField}
   * 
   * @return
   */
  TextField getRequirementsFilterText();

  /**
   * Get the requirements filter {@link Button}
   * 
   * @return the button
   */
  Button getRequirementsFilterButton();

  /**
   * Get the requirements tree collapse {@link Button}
   * 
   * @return the button
   */
  Button getRequirementsTreeCollapseButton();

  /**
   * Get the requirement tree expand {@link Button}
   * 
   * @return the button
   */
  Button getRequirementsTreeExpandButton();

  /**
   * Get the requirement reset filters {@link Button}
   * 
   * @return the button
   */
  Button getRequirementsResetFilterButton();

}
