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
package org.novaforge.forge.ui.applications.internal.client.associations;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.Window;

import java.util.Locale;

/**
 * This describes a view used to manage Composition stuff
 * 
 * @author Guillaume Lamirand
 */
public interface AssociationsView extends ComponentContainer
{

  /**
   * Get the {@link Form} used to display associations
   * 
   * @return the association form
   */
  Form getAssociationsForm();

  /**
   * Get the field containing project applications table
   * 
   * @return the {@link TreeTable}
   */
  TreeTable getApplicationsTable();

  /**
   * Should be call to refresh view when locale has changed
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Return button used to get back to application details view
   * 
   * @return {@link Button}
   */
  Button getReturnButton();

  /**
   * This will attach or detach visuel composant from its parent
   * 
   * @param pAttach
   *          if true so table will be attached otherwise detached
   */
  void attachTable(boolean pAttach);

  /**
   * Get the table containing composition notification
   * 
   * @return the {@link Table}
   */
  Table getNotificationsTable();

  /**
   * Return the window used to configure notificationl ink
   * 
   * @return {@link Window}
   */
  Window getNotificationsWindow();

  /**
   * This will attach or detach visuel composant from its parent
   * 
   * @param pAttach
   *          if true so table will be attached otherwise detached
   */
  void attachNotificationTable(boolean pAttach);

  /**
   * Return the form used to build action object
   * 
   * @return {@link Form}
   */
  Form getConfigurationActionsForm();

  /**
   * Return table containing source event details
   * 
   * @return {@link Table}
   */
  Table getConfigurationParametersTable();

  /**
   * Return button to display preview
   * 
   * @return {@link Button}
   */
  Button getConfigurationPreviewButton();

  /**
   * Return button to cancel setup association template
   * 
   * @return {@link Button}
   */
  Button getConfigurationCancelButton();

  /**
   * Return button to confirm setup association template
   * 
   * @return {@link Button}
   */
  Button getConfigurationConfirmButton();

  /**
   * Return window used to setup association template
   * 
   * @return {@link Window}
   */
  Window getConfigurationWindow();

  /**
   * Return table containing source event example
   * 
   * @return {@link Table}
   */
  Table getPreviewParametersTable();

  /**
   * Return the window used to preview action content
   * 
   * @return {@link Window}
   */
  Window getPreviewWindow();

  /**
   * Return form containing action details
   * 
   * @return {@link Form}
   */
  Form getPreviewActionsForm();

  /**
   * Return the window used to delete link
   * 
   * @return {@link Window}
   */
  Window getDeleteLinkWindow();

  /**
   * Return the button used to cancel deleting link
   * 
   * @return {@link Button}
   */
  Button getDeleteLinkCancelButton();

  /**
   * Return the button used to confirm deleting link
   * 
   * @return {@link Button}
   */
  Button getDeleteLinkConfirmButton();

}
