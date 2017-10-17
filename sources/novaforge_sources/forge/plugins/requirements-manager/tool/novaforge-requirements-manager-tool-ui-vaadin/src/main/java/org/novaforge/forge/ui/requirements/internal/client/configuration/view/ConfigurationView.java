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
package org.novaforge.forge.ui.requirements.internal.client.configuration.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.portal.client.component.PeriodField;
import org.vaadin.risto.stepper.IntStepper;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public interface ConfigurationView extends ComponentContainer
{

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Get the Synchronization intervale {@link PeriodField}
   * 
   * @return the interval periodfield
   */
  PeriodField getSynchroIntervalTime();

  /**
   * Get the Synchronization content {@link VerticalLayout}
   * 
   * @return the layout
   */
  VerticalLayout getSynchroAutoContentLayout();

  /**
   * Get the synchronization activate {@link CheckBox}
   * 
   * @return the checkbox
   */
  CheckBox getSynchroAutoCheckBox();

  /**
   * Get the source path {@link TextField}
   * 
   * @return the textfield
   */
  TextField getSourcePathField();

  /**
   * Get the source configuration {@link VerticalLayout}
   * 
   * @return the layout
   */
  VerticalLayout getSourceConfigurationLayout();

  /**
   * Get the Save configuration {@link Button}
   * 
   * @return the button
   */
  Button getSaveConfiguration();

  /**
   * Get the source not linked {@link Label}
   * 
   * @return the label
   */
  Label getSourceNotLinkedLabel();

  /**
   * Get the Synchronization launch time hour {@link IntStepper} field
   * 
   * @return the launch time hour field
   */
  IntStepper getSynchroLaunchTimeHour();

  /**
   * Get the Synchronization launch time minute {@link IntStepper} field
   * 
   * @return the launch time minute field
   */
  IntStepper getSynchroLaunchTimeMinute();

}
