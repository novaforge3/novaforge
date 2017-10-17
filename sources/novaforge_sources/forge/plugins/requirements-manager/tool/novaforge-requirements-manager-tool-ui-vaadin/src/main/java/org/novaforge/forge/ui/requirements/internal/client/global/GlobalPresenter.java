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

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsRequest;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementPermissionServiceException;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.requirements.internal.client.codeview.view.CodeViewPresenter;
import org.novaforge.forge.ui.requirements.internal.client.codeview.view.CodeViewViewImpl;
import org.novaforge.forge.ui.requirements.internal.client.configuration.view.ConfigurationPresenter;
import org.novaforge.forge.ui.requirements.internal.client.configuration.view.ConfigurationViewImpl;
import org.novaforge.forge.ui.requirements.internal.client.events.ShowRequirementListEvent;
import org.novaforge.forge.ui.requirements.internal.client.repository.view.RepositoryPresenter;
import org.novaforge.forge.ui.requirements.internal.client.repository.view.RepositoryViewImpl;
import org.novaforge.forge.ui.requirements.internal.client.requirement.list.RequirementListPresenter;
import org.novaforge.forge.ui.requirements.internal.client.requirement.list.RequirementListViewImpl;
import org.novaforge.forge.ui.requirements.internal.client.synchronization.view.SynchronizationPresenter;
import org.novaforge.forge.ui.requirements.internal.client.synchronization.view.SynchronizationViewImpl;
import org.novaforge.forge.ui.requirements.internal.client.testview.view.TestViewPresenter;
import org.novaforge.forge.ui.requirements.internal.client.testview.view.TestViewViewImpl;
import org.novaforge.forge.ui.requirements.internal.module.AbstractRequirementsPresenter;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.io.Serializable;
import java.util.Locale;

/**
 * This presenter handles main view.
 * 
 * @author Jeremy Casery
 */
public class GlobalPresenter extends AbstractRequirementsPresenter implements Serializable
{

  /**
   * Serial version uid used for serialization
   */
  private static final long              serialVersionUID = -509915124366295717L;

  /**
   * Content of project view
   */
  private final GlobalView               view;

  /**
   * The {@link RequirementListPresenter} used for requirement list view
   */
  private final RequirementListPresenter requirementListPresenter;
  /**
   * The {@link CodeViewPresenter} used for requirement codeview view
   */
  private final CodeViewPresenter        codeViewPresenter;
  /**
   * The {@link TestViewPresenter} used for requirement testview view
   */
  private final TestViewPresenter        testViewPresenter;
  /**
   * The {@link SynchronizationPresenter} used for requirement synchronization view
   */
  private final SynchronizationPresenter synchronizationPresenter;
  /**
   * The {@link RepositoryPresenter} used for requirement repository view
   */
  private final RepositoryPresenter      repositoryPresenter;
  /**
   * The {@link ConfigurationPresenter} used for requirement configuration view
   */
  private final ConfigurationPresenter   configurationPresenter;

  /**
   * Default constructor. It will initialize the tree component associated to
   * the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the Portal Context
   */
  public GlobalPresenter(final GlobalView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    requirementListPresenter = new RequirementListPresenter(new RequirementListViewImpl(), pPortalContext);
    codeViewPresenter = new CodeViewPresenter(new CodeViewViewImpl(), pPortalContext);
    testViewPresenter = new TestViewPresenter(new TestViewViewImpl(), pPortalContext);
    synchronizationPresenter = new SynchronizationPresenter(new SynchronizationViewImpl(), pPortalContext);
    repositoryPresenter = new RepositoryPresenter(new RepositoryViewImpl(), pPortalContext);
    configurationPresenter = new ConfigurationPresenter(new ConfigurationViewImpl(), pPortalContext);

    addListeners();
    checkUserRights();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {

    view.getRequirementList().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSplitPanel().setSecondComponent(requirementListPresenter.getComponent());
        requirementListPresenter.refresh();
      }
    });
    view.getRequirementCodeViewButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSplitPanel().setSecondComponent(codeViewPresenter.getComponent());
        codeViewPresenter.refresh();
      }
    });
    view.getRequirementTestViewButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSplitPanel().setSecondComponent(testViewPresenter.getComponent());
        testViewPresenter.refresh();
      }
    });
    view.getRequirementSynchronizationButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSplitPanel().setSecondComponent(synchronizationPresenter.getComponent());
        synchronizationPresenter.refresh();
      }
    });
    view.getRequirementRepositoryButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSplitPanel().setSecondComponent(repositoryPresenter.getComponent());
        repositoryPresenter.refresh();
      }
    });
    view.getRequirementConfigurationButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSplitPanel().setSecondComponent(configurationPresenter.getComponent());
        configurationPresenter.refresh();
      }
    });
  }

  private void checkUserRights()
  {
    final String currentUser = RequirementsModule.getAuthentificationService().getCurrentUser();
    try
    {
      final boolean canExecute = RequirementsModule.getRequirementPermissionService().canExecute(currentUser,
                                                                                                 getProjectToolId());
      view.showAdministration(canExecute);
    }
    catch (final RequirementPermissionServiceException e)
    {
      view.showAdministration(false);
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e,
                                                 UI.getCurrent().getLocale());
    }

  }

  /**
   * Will refresh the requirement list information.
   */
  public void refresh()
  {
    view.getRequirementList().addStyleName(NovaForge.SELECTED);
    view.getSplitPanel().setSecondComponent(requirementListPresenter.getComponent());
    requirementListPresenter.refresh();
    checkLinkedApps();
  }

  /**
   * Check which apps are linked, to allow or not their view
   */
  private void checkLinkedApps()
  {
    checkCodeApp();
    checkTestApp();
  }

  private void checkCodeApp()
  {
    view.setCodeAvailable(RequirementsModule.getApplicationRequestService().isAssociated(getProjectForgeId(),
        RequirementsRequest.searchRequirementInSourceCode.name()));
  }

  private void checkTestApp()
  {
    view.setTestAvailable(RequirementsModule.getApplicationRequestService().isAssociated(getProjectForgeId(),
        RequirementsRequest.getRequirementsWithTestCoverage.name()));
  }

  /**
   * Callback on {@link ShowAddUserMemberViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onShowRequirementListEvent(final ShowRequirementListEvent pEvent)
  {
    view.getSplitPanel().setSecondComponent(requirementListPresenter.getComponent());
    requirementListPresenter.refresh();
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
    // Doesn't handle it by default
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
