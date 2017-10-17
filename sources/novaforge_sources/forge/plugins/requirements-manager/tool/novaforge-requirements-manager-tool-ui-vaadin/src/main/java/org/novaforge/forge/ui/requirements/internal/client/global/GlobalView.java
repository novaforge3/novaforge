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
package org.novaforge.forge.ui.requirements.internal.client.global;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalSplitPanel;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public interface GlobalView extends ComponentContainer
{

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * This method allows to return the slip panel
   * 
   * @return {@link HorizontalSplitPanel}
   */
  HorizontalSplitPanel getSplitPanel();

  /**
   * This method return the split default position in pixel
   * 
   * @return the split default position in pixel
   */
  int getSplitPositionDefault();

  /**
   * This method allows to return the {@link Button} used for requirement list
   * menu
   * 
   * @return {@link Button} used for requirement list menu
   */
  Button getRequirementList();

  /**
   * This method allows to return the {@link Button} used for requirement configuration
   * menu
   * 
   * @return {@link Button} used for requirement configuration menu
   */
  Button getRequirementConfigurationButton();

  /**
   * This method allows to return the {@link Button} used for requirement repository
   * menu
   * 
   * @return {@link Button} used for requirement repository menu
   */
  Button getRequirementRepositoryButton();

  /**
   * This method allows to return the {@link Button} used for requirement synchronization
   * menu
   * 
   * @return {@link Button} used for requirement synchronization menu
   */
  Button getRequirementSynchronizationButton();

  /**
   * This method allows to return the {@link Button} used for requirement testview
   * menu
   * 
   * @return {@link Button} used for requirement testview menu
   */
  Button getRequirementTestViewButton();

  /**
   * This method allows to return the {@link Button} used for requirement codeview
   * menu
   * 
   * @return {@link Button} used for requirement codeview menu
   */
  Button getRequirementCodeViewButton();

  /**
   * Define if UI show administration link or not
   * 
   * @param pVisible
   *          to show administration, false otherwise
   */
  void showAdministration(boolean pVisible);

  /**
   * Set Code view available or not
   * 
   * @param pIsAvailable
   *          true if available, false otherwise
   */
  void setCodeAvailable(boolean pIsAvailable);

  /**
   * Set Test view available or not
   * 
   * @param pIsAvailable
   *          true if available, false otherwise
   */
  void setTestAvailable(boolean pIsAvailable);

}
