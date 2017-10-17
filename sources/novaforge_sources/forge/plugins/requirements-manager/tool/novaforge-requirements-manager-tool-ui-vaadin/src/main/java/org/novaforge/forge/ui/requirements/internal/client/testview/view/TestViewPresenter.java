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
package org.novaforge.forge.ui.requirements.internal.client.testview.view;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsRequest;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.ETestStatus;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.requirements.internal.client.containers.FilterItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.containers.RepositoryContainer;
import org.novaforge.forge.ui.requirements.internal.client.containers.RepositoryItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.containers.StatusContainer;
import org.novaforge.forge.ui.requirements.internal.client.containers.TestViewContainer;
import org.novaforge.forge.ui.requirements.internal.client.containers.TestViewItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.containers.TypeContainer;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.client.testview.components.ColumnRequirementGenerator;
import org.novaforge.forge.ui.requirements.internal.client.testview.components.ColumnStatusGenerator;
import org.novaforge.forge.ui.requirements.internal.client.testview.components.DataMapper;
import org.novaforge.forge.ui.requirements.internal.module.AbstractRequirementsPresenter;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * This presenter handles configuration view.
 * 
 * @author Jeremy Casery
 */
public class TestViewPresenter extends AbstractRequirementsPresenter implements Serializable
{

  /**
   * Serial version uid used for serialization
   */
  private static final long         serialVersionUID    = 7107034541114430211L;

  /**
   * Content of project view
   */
  private final TestViewView        view;
  /**
   * Requirement test view container
   */
  private final TestViewContainer   container           = new TestViewContainer();
  /**
   * Requirement status container
   */
  private final StatusContainer     statusContainer     = new StatusContainer();
  /**
   * Requirement repository container
   */
  private final RepositoryContainer repositoryContainer = new RepositoryContainer();
  /**
   * Requirement type container
   */
  private final TypeContainer       typeContainer       = new TypeContainer();

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
  public TestViewPresenter(final TestViewView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    initFiltersValues();
    initTestViewList();
    addListeners();
  }

  /**
   * Initialize filters values
   */
  private void initFiltersValues()
  {
    initFilterStatus();
    initFilterRepository();
    initFilterType();
  }

  /**
   * Initialize requirement testview list
   */
  private void initTestViewList()
  {
    view.getDataTable().setContainerDataSource(container);
    view.getDataTable().addGeneratedColumn(TestViewItemProperty.REQUIREMENT_NAME.getPropertyId(),
        new ColumnRequirementGenerator(this));
    view.getDataTable().addGeneratedColumn(TestViewItemProperty.STATUS.getPropertyId(), new ColumnStatusGenerator());

    // Define visibles columns
    view.getDataTable().setVisibleColumns(TestViewItemProperty.REQUIREMENT_NAME.getPropertyId(),
        TestViewItemProperty.TEST_NAME.getPropertyId(), TestViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId(),
        TestViewItemProperty.VERSION_IN_TEST.getPropertyId(), TestViewItemProperty.STATUS.getPropertyId());

    // Define columns expand ratio
    view.getDataTable().setColumnExpandRatio(TestViewItemProperty.REQUIREMENT_NAME.getPropertyId(), 0.45F);
    view.getDataTable().setColumnExpandRatio(TestViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId(), 0.05F);
    view.getDataTable().setColumnExpandRatio(TestViewItemProperty.TEST_NAME.getPropertyId(), 0.35F);
    view.getDataTable().setColumnExpandRatio(TestViewItemProperty.VERSION_IN_TEST.getPropertyId(), 0.10F);
    view.getDataTable().setColumnExpandRatio(TestViewItemProperty.STATUS.getPropertyId(), 0.05F);
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getFilterApplyButton().addClickListener(new ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = 2360950428352863679L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        final Filterable requirementFilter = container;
        requirementFilter.removeAllContainerFilters();
        if (!view.getFilterRequirementField().getValue().isEmpty())
        {
          requirementFilter.addContainerFilter(new Or(new SimpleStringFilter(TestViewItemProperty.REQUIREMENT_NAME
              .getPropertyId(), view.getFilterRequirementField().getValue(), true, false), new SimpleStringFilter(
              TestViewItemProperty.REQUIREMENT_DESCRIPTION.getPropertyId(), view.getFilterRequirementField().getValue(), true,
              false)));
        }
        if (view.getFilterRepositoryComboBox().getValue() != null)
        {
          requirementFilter.addContainerFilter(new SimpleStringFilter(TestViewItemProperty.REPOSITORY.getPropertyId(), view
              .getFilterRepositoryComboBox().getValue().toString(), true, false));
        }
        if (view.getFilterStatusComboBox().getValue() != null)
        {
          requirementFilter.addContainerFilter(new Compare.Equal(TestViewItemProperty.STATUSID.getPropertyId(), view
              .getFilterStatusComboBox().getValue()));
        }
        if (view.getFilterTypeComboBox().getValue() != null)
        {
          requirementFilter.addContainerFilter(new Compare.Equal(TestViewItemProperty.REQUIREMENT_TYPE.getPropertyId(), view
              .getFilterTypeComboBox().getValue()));
        }
      }
    });
    view.getFilterResetButton().addClickListener(new ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = 8241498087594670893L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        resetFilters();
      }
    });
    view.getDataTable().addActionHandler(new RefreshAction()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -1978024187077030364L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void refreshAction()
      {
        refreshRequirements();

      }

      /**
       * {@inheritDoc}
       */
      @Override
      public PortalMessages getPortalMessages()
      {
        return RequirementsModule.getPortalMessages();
      }
    });
  }

  /**
   * Initialize status filter
   */
  private void initFilterStatus()
  {
    view.getFilterStatusComboBox().setContainerDataSource(statusContainer);
    view.getFilterStatusComboBox().setItemCaptionPropertyId(FilterItemProperty.LABEL.getPropertyId());
    view.getFilterStatusComboBox().setItemIconPropertyId(FilterItemProperty.ICON.getPropertyId());
    view.getFilterStatusComboBox().setNewItemsAllowed(false);
  }

  /**
   * Initialize repository filter
   */
  private void initFilterRepository()
  {
    view.getFilterRepositoryComboBox().setContainerDataSource(repositoryContainer);
    view.getFilterRepositoryComboBox().setItemCaptionPropertyId(RepositoryItemProperty.URI.getPropertyId());
    view.getFilterRepositoryComboBox().setNewItemsAllowed(false);
  }

  /**
   * Initialize type filter
   */
  private void initFilterType()
  {
    view.getFilterTypeComboBox().setContainerDataSource(typeContainer);
    view.getFilterTypeComboBox().setItemCaptionPropertyId(FilterItemProperty.LABEL.getPropertyId());
    view.getFilterTypeComboBox().setNewItemsAllowed(false);
  }

  /**
   * Reset filters selected value, and remove any container filters
   */
  private void resetFilters()
  {
    view.getFilterRequirementField().setValue("");
    view.getFilterStatusComboBox().setValue(null);
    view.getFilterRepositoryComboBox().setValue(null);
    view.getFilterTypeComboBox().setValue(null);
    container.removeAllContainerFilters();
  }

  /** Refresh content */
  private void refreshRequirements()
  {
    container.removeAllItems();
    try
    {
      final String projectToolId = getProjectToolId();
      final IProject project = RequirementsModule.getRequirementManagerService().findProjectByID(projectToolId);
      for (final IRepository repository : project.getRepositories())
      {
        final Set<IRequirement> currentRequirements = RequirementsModule.getRequirementFunctionalTestService()
            .findRequirementsTestByStatus(projectToolId, repository.getURI(), ETestStatus.ALL);
        if (currentRequirements != null)
        {
          container.addAllRequirements(repository, DataMapper.convertRequirementToTestRequirement(currentRequirements));
        }
      }
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Show the requirement Details view
   *
   * @param pRequirementId
   *          the requirement id to show
   */
  public void showRequirementDetails(final String pRequirementId)
  {
    setRequirementDetailsValues(pRequirementId);
    view.getRequirementDetailsWindow().setCaption(RequirementsModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                                                    Messages.DETAILS_TITLE,
                                                                                                    pRequirementId));
    UI.getCurrent().addWindow(view.getRequirementDetailsWindow());
  }

  /**
   * Set the requirement details view values
   *
   * @param pRequirementID
   *          the requirement id to show
   */
  private void setRequirementDetailsValues(final String pRequirementID)
  {
    IRequirement requirement;
    try
    {
      requirement = RequirementsModule.getRequirementManagerService().loadRequirementTreeByID(pRequirementID);
      if (requirement != null)
      {
        view.getDetailsView().setRequirementID(requirement.getName());
        view.getDetailsView().getTypeValue().setValue(requirement.getType());
        view.getDetailsView().getVersionValue()
            .setValue(Integer.toString(requirement.findLastRequirementVersion().getCurrentVersion()));
        view.getDetailsView().getObjRefValue().setValue(requirement.getReference());
        view.getDetailsView().getStatusValue().setValue(requirement.getStatus());
        view.getDetailsView().getNameValue().setValue(requirement.getName());
        view.getDetailsView().getDescValue().setValue(requirement.getDescription());
        view.getDetailsView().getAccepCritValue().setValue(requirement.getAcceptanceCriteria());
      }
      else
      {
        view.getDetailsView().setRequirementID(pRequirementID);
        view.getDetailsView()
            .getNameValue()
            .setValue(
                RequirementsModule.getPortalMessages().getMessage(getCurrentLocale(), Messages.DETAILS_REQUIREMENT_NOTFOUND));
      }
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }

  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    if (RequirementsModule.getApplicationRequestService().isAssociated(getProjectForgeId(),
                                                                       RequirementsRequest.getRequirementsWithTestCoverage
                                                                           .name()))
    {
      refreshContent();
    }
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Refresh the filters
   */
  private void refreshFilters()
  {
    resetFilters();
    refreshStatusFilter();
    refreshRepositoryFilter();
    refreshTypeFilter();
  }

  /**
   * Refresh the status filter
   */
  private void refreshStatusFilter()
  {
    statusContainer.refreshTestView(getCurrentLocale());
  }

  /**
   * Refresh the repository filter
   */
  private void refreshRepositoryFilter()
  {
    try
    {
      final IProject project = RequirementsModule.getRequirementManagerService().findProjectByID(getProjectToolId());
      repositoryContainer.setRepositories(project.getRepositories());
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Refresh the type filter
   */
  private void refreshTypeFilter()
  {
    final Set<String> types = new HashSet<String>();
    for (final Object itemId : container.getItemIds())
    {
      final String itemType = (String) container.getItem(itemId).getItemProperty(TestViewItemProperty.REQUIREMENT_TYPE
                                                                                     .getPropertyId()).getValue();
      if (itemType != null)
      {
        types.add(itemType);
      }
    }
    typeContainer.setTypes(types);
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
    refreshRequirements();
    refreshFilters();
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
