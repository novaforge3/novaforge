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
package org.novaforge.forge.ui.mailing.internal.client.mailing.update;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;

import java.util.Locale;

/**
 * @author B-Martinelli
 */
public interface MailingListsView extends ComponentContainer
{

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Return {@link Button} used to add a new mailing list
   * 
   * @return {@link Button} used to add a new mailing list
   */
  Button getCreateButton();

  /**
   * Return {@link TextField} used to filter mailing list's table
   * 
   * @return {@link TextField} used to filter mailing lists's table
   */
  TextField getFilterTextField();

  /**
   * Return {@link Table} containing mailing lists
   * 
   * @return {@link Table} containing mailing lists
   */
  Table getMailingListsTable();

  /**
   * This will attach or detach visuel composant from its parent
   * 
   * @param pAttach
   *          if true so table will be attached otherwise detached
   */
  void attachMailinglistsTable(boolean pAttach);

  /**
   * This method returns the window which allow to close a mailing list
   * 
   * @return Window
   */
  DeleteConfirmWindow getCloseListWindow();

  /**
   * Allows to display or hid element according admin rights
   * 
   * @param pIsAdmin
   */
  void setAdminVisibility(boolean pIsAdmin);

  /**
   * This button allows to cancel the unsubscription
   * 
   * @return {@link Button}
   */
  Button getUnsubscribeCancel();

  /**
   * This button allows to confirm the unsubscription
   * 
   * @return {@link Button}
   */
  Button getUnsubscribeConfirm();

  /**
   * This label which displayed confirm message from the unsubscription
   * 
   * @return {@link Label}
   */
  Label getUnsubscribeConfirmLabel();

  /**
   * This button allows to get the unsubscription popup
   * 
   * @return {@link Window}
   */
  Window getUnsubscribeWindow();
}
