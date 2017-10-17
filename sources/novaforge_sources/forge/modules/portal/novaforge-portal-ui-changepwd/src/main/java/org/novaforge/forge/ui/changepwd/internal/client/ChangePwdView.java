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
package org.novaforge.forge.ui.changepwd.internal.client;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.novaforge.forge.ui.portal.data.validator.PasswordValidator;

/**
 * This content is shown on the change pwd view
 *
 * @author Guillaume Lamirand
 */
public interface ChangePwdView extends ComponentContainer
{

  /**
   * Get the apply button to valid the form
   *
   * @return {@link Button} to valid the form
   */
  Button getApply();

  /**
   * Get link element displayed
   *
   * @return {@link Link}
   */
  Button getHomeLink();

  /**
   * @param pIsEmpty
   * @param pIsValid
   */
  void setNewPasswdValid(boolean pIsEmpty, boolean pIsValid);

  /**
   * @param pOneEmpty
   * @param pIsMatching
   */
  void setNewPasswdMatching(boolean pOneEmpty, boolean pIsMatching);

  /**
   * @return
   */
  TextField getLogin();

  /**
   * @return
   */
  PasswordField getCurrentPasswd();

  /**
   * @return
   */
  PasswordField getNewPasswd();

  /**
   * @return
   */
  PasswordField getNewAgainPasswd();

  /**
   * @return
   */
  PasswordValidator getPasswordRegexValidator();
  
  /**
   * @return
   */ 
  Label getPasswdSecurityRuleLabel();
  
  
}
