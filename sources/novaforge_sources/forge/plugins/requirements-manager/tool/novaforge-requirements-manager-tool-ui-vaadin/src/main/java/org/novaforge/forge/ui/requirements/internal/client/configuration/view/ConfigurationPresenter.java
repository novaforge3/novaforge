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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsRequest;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerSchedulingException;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.scheduling.SchedulingConfiguration;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.AbstractRequirementsPresenter;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.io.Serializable;
import java.util.Locale;

/**
 * This presenter handles configuration view.
 * 
 * @author Jeremy Casery
 */
public class ConfigurationPresenter extends AbstractRequirementsPresenter implements Serializable
{

  /**
   * Serial version uid used for serialization
   */
  private static final long       serialVersionUID = 7107034541114430211L;

  /**
   * Content of project view
   */
  private final ConfigurationView view;

  /**
   * Default constructor. It will initialize the tree component associated to
   * the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context
   * @param pInstanceId
   *          project id
   */
  public ConfigurationPresenter(final ConfigurationView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getSynchroAutoCheckBox().addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = 1838637005539920770L;

      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        view.getSynchroAutoContentLayout().setVisible(view.getSynchroAutoCheckBox().getValue());

      }
    });
    view.getSaveConfiguration().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = 8951096510947718325L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        saveConfigurations();
      }
    });
  }

  /**
   * Save the configurations
   */
  private void saveConfigurations()
  {
    if (view.getSourcePathField().isEnabled())
    {
      saveSourcesConfiguration();
    }
    if (view.getSynchroAutoCheckBox().getValue())
    {
      saveSynchronizationConfiguration();
    }
  }

  /**
   * Save the sources configurations
   */
  private void saveSourcesConfiguration()
  {
    try
    {
      final IProject project = RequirementsModule.getRequirementManagerService().findProjectByID(
          getProjectToolId());
      project.setCodeRepositoryPath(view.getSourcePathField().getValue());
      RequirementsModule.getRequirementManagerService().updateProject(project);
      final TrayNotification notification = new TrayNotification(RequirementsModule.getPortalMessages()
          .getMessage(UI.getCurrent().getLocale(), Messages.CONFIGURATION_SAVE_SOURCES_SUCCESS),
          RequirementsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
              Messages.CONFIGURATION_SAVE_SOURCES_SUCCESS_DESC), TrayNotificationType.SUCCESS);
      notification.show(Page.getCurrent());
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * Save the automatic synchronization configurations
   */
  private void saveSynchronizationConfiguration()
  {
    try
    {
      final SchedulingConfiguration configuration = RequirementsModule.getRequirementFactory()
          .buildNewSchedulingConfiguration();
      configuration.setProjectId(getProjectToolId());
      configuration.setUserId(RequirementsModule.getAuthentificationService().getCurrentUser());
      configuration.setActive(true);
      configuration.setLaunchHour(view.getSynchroLaunchTimeHour().getValue());
      configuration.setLaunchMinute(view.getSynchroLaunchTimeMinute().getValue());
      configuration.setLaunchPeriod(view.getSynchroIntervalTime().getValue());
      RequirementsModule.getRequirementManagerSchedulingService().saveSchedulingConfiguration(configuration);

      final TrayNotification notification = new TrayNotification(RequirementsModule.getPortalMessages()
          .getMessage(UI.getCurrent().getLocale(), Messages.CONFIGURATION_SAVE_SYNCHRO_SUCCESS),
          RequirementsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
              Messages.CONFIGURATION_SAVE_SOURCES_SYNCHRO_DESC), TrayNotificationType.SUCCESS);
      notification.show(Page.getCurrent());
    }
    catch (final RequirementManagerSchedulingException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }

  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Get the view element
   *
   * @return the view
   */
  public ConfigurationView getView()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    final boolean isSourceAvailable = RequirementsModule.getApplicationRequestService()
                                                        .isAssociated(getProjectForgeId(),
                                                                      RequirementsRequest.searchRequirementInSourceCode
                                                                          .name());
    view.getSourcePathField().setEnabled(isSourceAvailable);
    view.getSourceNotLinkedLabel().setVisible(!isSourceAvailable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
