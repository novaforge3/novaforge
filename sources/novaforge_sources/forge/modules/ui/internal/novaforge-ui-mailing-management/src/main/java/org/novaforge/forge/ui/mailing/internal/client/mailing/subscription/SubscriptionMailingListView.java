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
package org.novaforge.forge.ui.mailing.internal.client.mailing.subscription;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.wizard.components.AddSubscribeWizard;

import java.util.Locale;

/**
 * @author sbenoist
 * @author Aimen Merkich
 */
public interface SubscriptionMailingListView extends ComponentContainer
{
  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Return {@link Button} used to subscribe a new user
   * 
   * @return {@link Button} used to subscribe a new user
   */
  Button getSubscribeButton();

  /**
   * Return {@link TextField} used to filter mailing list subscribers's table
   * 
   * @return {@link TextField} used to filter mailing list subscribers's table
   */
  TextField getFilterTextField();

  /**
   * Return {@link Table} containing subscribers
   * 
   * @return {@link Table} containing subscribers
   */
  Table getSubscribersTable();

  /**
   * This will attach or detach visuel composant from its parent
   * 
   * @param pAttach
   *          if true so table will be attached otherwise detached
   */
  void attachSubscribersTable(boolean pAttach);

  /**
   * This button allows to return to the precedent view
   * 
   * @return {@link Button}
   */
  Button getReturnButton();

  /**
   * This button allows to cancel the user unsubscription
   * 
   * @return {@link Button}
   */
  Button getUnsubscribeUserCancel();

  /**
   * This button allows to confirm the user unsubscription
   * 
   * @return {@link Button}
   */
  Button getUnsubscribeUserConfirm();

  /**
   * This button allows to get the unsubscription user popup
   * 
   * @return {@link Window}
   */
  Window getUnsubscribeUserWindow();

  /**
   * This button allows to get the unsubscription group popup
   * 
   * @return {@link Window}
   */
  Window getUnsubscribeGroupWindow();

  /**
   * This button allows to cancel the group unsubscription
   * 
   * @return {@link Button}
   */
  Button getUnsubscribeGroupCancel();

  /**
   * This button allows to confirm the group unsubscription
   * 
   * @return {@link Button}
   */
  Button getUnsubscribeGroupConfirm();

  /**
   * This button allows to get the subscription popup
   * 
   * @return {@link Window}
   */
  Window getSubscribeWindow();

  /**
   * This button allows to get notification user checkbox
   * 
   * @return {@link CheckBox}
   */
  CheckBox getNotificationUserChbx();

  /**
   * This button allows to get notification group checkbox
   * 
   * @return {@link CheckBox}
   */
  CheckBox getNotificationGroupChbx();

  /**
   * Get the {@link AddSubscribeWizard}
   * 
   * @return the wizard
   */
  AddSubscribeWizard getAddSubscribeWizard();

  /**
   * @param pLocale
   * @param pCurrentListname
   */
  void setListName(final Locale pLocale, final String pCurrentListname);

}
