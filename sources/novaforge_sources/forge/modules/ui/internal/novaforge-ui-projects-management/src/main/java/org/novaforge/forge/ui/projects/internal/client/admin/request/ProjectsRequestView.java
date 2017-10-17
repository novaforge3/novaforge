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
package org.novaforge.forge.ui.projects.internal.client.admin.request;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import java.util.Locale;

/**
 * This interface describes the view used to list project which request a validation
 * 
 * @author Guillaume Lamirand
 */
public interface ProjectsRequestView extends ComponentContainer
{

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Return {@link TextField} used to filter projects' table
   * 
   * @return {@link TextField} used to filter projects' table
   */
  TextField getFilterTextField();

  /**
   * Return {@link Table} containing projects
   * 
   * @return {@link Table} containing projects
   */
  Table getProjectsTable();

  /**
   * Return sub window used to validate project
   * 
   * @return {@link Window}
   */
  Window getValidProjectWindow();

  /**
   * Return button used to confirm project validation
   * 
   * @return {@link Button}
   */
  Button getValidProjectConfirmButton();

  /**
   * Return button used to cancel project validation
   * 
   * @return {@link Button}
   */
  Button getValidProjectCancelButton();

  /**
   * Return sub window used to reject project
   * 
   * @return {@link Window}
   */
  Window getInValidProjectWindow();

  /**
   * Return button used to confirm rejecting project
   * 
   * @return {@link Button}
   */
  Button getInValidProjectConfirmButton();

  /**
   * Return button used to cancel rejecting project
   * 
   * @return {@link Button}
   */
  Button getInValidProjectCancelButton();

  /**
   * Return button used to cancel user deletion
   * 
   * @return {@link Button}
   */
  TextArea getInValidProjectText();

}
