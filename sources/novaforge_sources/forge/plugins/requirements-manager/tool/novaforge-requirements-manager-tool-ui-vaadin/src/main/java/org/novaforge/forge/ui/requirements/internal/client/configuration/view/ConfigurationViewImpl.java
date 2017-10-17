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

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.PeriodField;
import org.novaforge.forge.ui.portal.client.component.PeriodRange;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;
import org.vaadin.risto.stepper.IntStepper;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class ConfigurationViewImpl extends VerticalLayout implements ConfigurationView
{

  /**
   * Field source path id
   */
  public static final  String         FIELD_SOURCE_PATH            = "confSourcePath";
  /**
   * Serialization id
   */
  private static final long           serialVersionUID             = -4420222948069676402L;
  /**
   * Default field time width
   */
  private final static float          FIELD_TIME_WIDTH             = 40;
  /**
   * Configuration title label
   */
  private final        Label          configurationTitle           = new Label();
  /**
   * Source configuration Layout
   */
  private final        VerticalLayout sourceConfigurationLayout    = new VerticalLayout();
  /**
   * Source configuration path field
   */
  private final        TextField      sourcePathField              = new TextField();
  /**
   * Source not linked label
   */
  private final        Label          sourceNotLinkedLabel         = new Label();
  /**
   * Source configuration form
   */
  private final        Form           sourceConfigurationForm      = new Form();
  /**
   * Synchronization configuration form
   */
  private final        Form           synchroConfigurationForm     = new Form();
  /**
   * Synchronization automatic checkbox
   */
  private final        CheckBox       synchroAutoCheckBox          = new CheckBox();
  /**
   * Synchronization automatic content layout
   */
  private final        VerticalLayout synchroAutoContentLayout     = new VerticalLayout();
  /**
   * Synchronization automatic interval time
   */
  private final        PeriodField    synchroIntervalTime          = new PeriodField();
  /**
   * Synchronization automatic launch time hour label
   */
  private final        Label          synchroLaunchTimeHourLabel   = new Label();
  /**
   * Synchronization automatic launch time hour field
   */
  private final        IntStepper     synchroLaunchTimeHour        = new IntStepper();
  /**
   * Synchronization automatic launch time minute label
   */
  private final        Label          synchroLaunchTimeMinuteLabel = new Label();
  /**
   * Synchronization automatic launch time hour field
   */
  private final        IntStepper     synchroLaunchTimeMinute      = new IntStepper();
  /**
   * Save configuration button
   */
  private final        Button         saveConfiguration            = new Button();

  /**
   * Default constructor.
   */
  public ConfigurationViewImpl()
  {
    setMargin(true);
    setSpacing(true);

    configurationTitle.setStyleName(NovaForge.LABEL_H2);
    sourceNotLinkedLabel.setStyleName(NovaForge.LABEL_ORANGE);
    sourcePathField.setWidth(NovaForge.FORM_FIELD_URL_WIDTH);
    sourceConfigurationForm.getLayout().addComponent(sourcePathField);
    sourceConfigurationForm.getLayout().addComponent(sourceNotLinkedLabel);
    sourceConfigurationLayout.addComponent(sourceConfigurationForm);

    synchroIntervalTime.setRange(PeriodRange.HOUR);
    synchroLaunchTimeHour.setMinValue(0);
    synchroLaunchTimeHour.setMaxValue(23);
    synchroLaunchTimeHour.setManualInputAllowed(false);
    synchroLaunchTimeHour.setMouseWheelEnabled(true);
    synchroLaunchTimeHour.setValue(8);
    synchroLaunchTimeHour.setWidth(FIELD_TIME_WIDTH, Unit.PIXELS);
    synchroLaunchTimeMinute.setMinValue(0);
    synchroLaunchTimeMinute.setMaxValue(59);
    synchroLaunchTimeMinute.setManualInputAllowed(false);
    synchroLaunchTimeMinute.setMouseWheelEnabled(true);
    synchroLaunchTimeMinute.setValue(00);
    synchroLaunchTimeMinute.setWidth(FIELD_TIME_WIDTH, Unit.PIXELS);
    HorizontalLayout launchTimeLayout = new HorizontalLayout();
    launchTimeLayout.setMargin(false);
    launchTimeLayout.setSpacing(true);
    launchTimeLayout.addComponent(synchroLaunchTimeHourLabel);
    launchTimeLayout.addComponent(synchroLaunchTimeHour);
    launchTimeLayout.addComponent(synchroLaunchTimeMinuteLabel);
    launchTimeLayout.addComponent(synchroLaunchTimeMinute);
    synchroAutoContentLayout.addComponent(launchTimeLayout);
    synchroAutoContentLayout.addComponent(synchroIntervalTime);
    synchroAutoContentLayout.setMargin(true);

    saveConfiguration.setStyleName(NovaForge.BUTTON_PRIMARY);
    saveConfiguration.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));

    final HorizontalLayout footerLayout = new HorizontalLayout();
    footerLayout.setMargin(true);
    footerLayout.setSpacing(true);
    footerLayout.addComponent(saveConfiguration);

    addComponent(configurationTitle);
    addComponent(sourceConfigurationLayout);
    addComponent(synchroConfigurationForm);
    addComponent(synchroAutoCheckBox);
    addComponent(synchroAutoContentLayout);
    addComponent(footerLayout);

    synchroAutoContentLayout.setVisible(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    configurationTitle.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_CONFIGURATION));
    sourceNotLinkedLabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.SYNCHRO_CODES_NOTLINKED));
    sourceConfigurationForm.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.SYNCHRO_CODES));
    sourcePathField.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.CONFIGURATION_CODE_PATH));

    synchroConfigurationForm.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_SYNCHRONIZATION));
    synchroAutoCheckBox.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.CONFIGURATION_SYNCHROAUTO_ACTIVATE));
    synchroIntervalTime.setCaptionBefore(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.CONFIGURATION_SYNCHROAUTO_REPEATTIME));
    synchroLaunchTimeHourLabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.CONFIGURATION_SYNCHROAUTO_LAUNCHTIME));
    synchroLaunchTimeMinuteLabel.setValue(":");

    saveConfiguration.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.CONFIGURATION_SAVE));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PeriodField getSynchroIntervalTime()
  {
    return synchroIntervalTime;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VerticalLayout getSynchroAutoContentLayout()
  {
    return synchroAutoContentLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getSynchroAutoCheckBox()
  {
    return synchroAutoCheckBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getSourcePathField()
  {
    return sourcePathField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VerticalLayout getSourceConfigurationLayout()
  {
    return sourceConfigurationLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getSaveConfiguration()
  {
    return saveConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getSourceNotLinkedLabel()
  {
    return sourceNotLinkedLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IntStepper getSynchroLaunchTimeHour()
  {
    return synchroLaunchTimeHour;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IntStepper getSynchroLaunchTimeMinute()
  {
    return synchroLaunchTimeMinute;
  }

}
