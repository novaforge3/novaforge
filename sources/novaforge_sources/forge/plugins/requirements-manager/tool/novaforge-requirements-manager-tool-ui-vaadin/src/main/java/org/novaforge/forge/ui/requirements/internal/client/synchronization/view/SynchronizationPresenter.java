/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.ui.requirements.internal.client.synchronization.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsRequest;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerSchedulingException;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.AbstractRequirementsPresenter;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * This presenter handles configuration view.
 * 
 * @author Jeremy Casery
 */
public class SynchronizationPresenter extends AbstractRequirementsPresenter implements Serializable
{
  /** Serial version uid used for serialization. */
  private static final long         serialVersionUID = 7107034541114430211L;

  /** Content of project view */
  private final SynchronizationView view;

  /** Constant for OBEO */
  public final static String        DATA_REPO        = "repositories";

  /** Constant for TEST */
  public final static String        DATA_TEST        = "testlink";

  /** Constant for CODE */
  public final static String        DATA_CODE        = "code";

  /** Logger component */
  private static final Log          LOGGER           = LogFactory.getLog(SynchronizationPresenter.class);

  /**
   * Default constructor. It will initialize the tree component associated to
   * the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context
   * @param pProjectId
   *          project id
   */
  public SynchronizationPresenter(final SynchronizationView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    addListeners();
  }

  /** It will add listeners to view components */
  public void addListeners()
  {
    view.getSynchroButton().addClickListener(new Button.ClickListener()
    {
      /** Default serial UID */
      private static final long serialVersionUID = -962390990210061962L;

      /** {@inheritDoc} */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        // Check which option has been selected and if it's available
        if (SynchronizationViewImpl.RADIO_OPT_REQUIREMENTS.equals(view.getSynchroGroup().getValue()))
        {
          if (isRequirementsAvailable())
          {
            launchSynchronization(new ArrayList<String>(Arrays.asList(DATA_REPO)));
          }
          else
          {
            new TrayNotification(RequirementsModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.SYNCHRO_REQUIREMENTS_NOTLINKED), TrayNotificationType.WARNING).show(Page
                .getCurrent());
          }
        }
        else if (SynchronizationViewImpl.RADIO_OPT_TESTS.equals(view.getSynchroGroup().getValue()))
        {
          if (isTestsAvailable())
          {
            launchSynchronization(new ArrayList<String>(Arrays.asList(DATA_TEST)));
          }
          else
          {
            new TrayNotification(RequirementsModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.SYNCHRO_TESTS_NOTLINKED), TrayNotificationType.WARNING).show(Page.getCurrent());
          }
        }
        else if (SynchronizationViewImpl.RADIO_OPT_CODE.equals(view.getSynchroGroup().getValue()))
        {
          if (isSourcesAvailable())
          {
            launchSynchronization(new ArrayList<String>(Arrays.asList(DATA_CODE)));
          }
          else
          {
            new TrayNotification(RequirementsModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.SYNCHRO_CODES_NOTLINKED), TrayNotificationType.WARNING).show(Page.getCurrent());
          }
        }
      }

      /**
       * Execute the synchronization with the asked data.
       * 
       * @param dataToSynchronize
       *          the data type to synchronize
       */
      private void launchSynchronization(final List<String> dataToSynchronize)
      {
        try
        {
          final boolean isOngoing = RequirementsModule.getRequirementManagerSchedulingService()
              .isSynchronizationOngoing(getProjectToolId());
          if (isOngoing)
          {
            new TrayNotification(RequirementsModule.getPortalMessages().getMessage(
                UI.getCurrent().getLocale(), Messages.SYNCHRONIZATION_ALREADY_RUNNING_TITLE),
                RequirementsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                    Messages.SYNCHRONIZATION_ALREADY_RUNNING_DESC), TrayNotificationType.INFO).show(Page
                .getCurrent());
          }
          else
          {
            RequirementsModule.getRequirementManagerSchedulingService().launchSynchronization(
                getProjectToolId(), RequirementsModule.getAuthentificationService().getCurrentUser(),
                dataToSynchronize);
            new TrayNotification(RequirementsModule.getPortalMessages().getMessage(
                UI.getCurrent().getLocale(), Messages.SYNCHRONIZATION_SUCCESS_TITLE), RequirementsModule
                .getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                    Messages.SYNCHRONIZATION_SUCCESS_DESC), TrayNotificationType.SUCCESS).show(Page
                .getCurrent());
          }
        }
        catch (final RequirementManagerSchedulingException e)
        {
          ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI
              .getCurrent().getLocale());
        }
        catch (final UnsupportedOperationException e)
        {
          LOGGER.warn(e.getMessage());
        }
      }
    });
  }

  /**
   * Check if there is requirement repositories
   * 
   * @return true if there is at least one repository, false otherwise
   */
  private boolean isRequirementsAvailable()
  {
    boolean isAvailable = false;
    try
    {
      IProject project = RequirementsModule.getRequirementManagerService()
          .findProjectByID(getProjectToolId());
      isAvailable = !project.getRepositories().isEmpty();
    }
    catch (final RequirementManagerServiceException e)
    {
      LOGGER.debug(e);
    }
    return isAvailable;
  }

  /**
   * Check if requirement is linked with Test Management
   * 
   * @return true if linked, false otherwise
   */
  private boolean isTestsAvailable()
  {
    return RequirementsModule.getApplicationRequestService().isAssociated(getProjectForgeId(),
        RequirementsRequest.getRequirementsWithTestCoverage.name());
  }

  /**
   * Check if requirement is linked with Code management
   * 
   * @return true if linked, false otherwise
   */
  private boolean isSourcesAvailable()
  {
    return RequirementsModule.getApplicationRequestService().isAssociated(getProjectForgeId(),
        RequirementsRequest.searchRequirementInSourceCode.name());
  }

  /** Will refresh the project information. */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /** {@inheritDoc} */
  @Override
  protected void refreshContent()
  {
  }

  /** {@inheritDoc} */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);
  }

  /**
   * Get the view element
   * 
   * @return the view
   */
  public SynchronizationView getView()
  {
    return view;
  }

  /** {@inheritDoc} */
  @Override
  public Component getComponent()
  {
    return view;
  }

}
