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
package org.novaforge.forge.widgets.scm.lastcommit.internal.client.data;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Table;
import org.vaadin.addon.itemlayout.horizontal.ItemHorizontal;

import java.util.Locale;

/**
 * Define default view for {@link DataPresenter}
 * 
 * @author Guillaume Lamirand
 */
public interface DataView extends ComponentContainer
{

  /**
   * Refresh text view
   * 
   * @param pLocale
   *          the locale used to refresh labels
   */
  void refreshLocale(final Locale pLocale);

  /**
   * Returns the {@link Table} used to store commits
   * 
   * @return the commitsTable
   */
  Table getCommitsTable();

  /**
   * Clear the existing instance
   */
  void clearInstances();

  /**
   * Returns the {@link ItemHorizontal} used to store commits
   * 
   * @return the {@link ItemHorizontal}
   */
  ItemHorizontal getRepositoryLayout();

}
