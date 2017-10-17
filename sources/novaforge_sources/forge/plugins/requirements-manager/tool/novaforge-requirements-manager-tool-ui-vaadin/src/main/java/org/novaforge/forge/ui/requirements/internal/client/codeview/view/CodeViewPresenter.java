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
package org.novaforge.forge.ui.requirements.internal.client.codeview.view;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsRequest;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.ECodeStatus;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.requirements.internal.client.codeview.components.ColumnClassGenerator;
import org.novaforge.forge.ui.requirements.internal.client.codeview.components.ColumnRequirementGenerator;
import org.novaforge.forge.ui.requirements.internal.client.codeview.components.ColumnStatusGenerator;
import org.novaforge.forge.ui.requirements.internal.client.containers.CodeViewContainer;
import org.novaforge.forge.ui.requirements.internal.client.containers.CodeViewItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.containers.FilterItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.containers.RepositoryContainer;
import org.novaforge.forge.ui.requirements.internal.client.containers.RepositoryItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.containers.StatusContainer;
import org.novaforge.forge.ui.requirements.internal.client.containers.TypeContainer;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.AbstractRequirementsPresenter;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * This presenter handles codeview view.
 * 
 * @author Jeremy Casery
 */
public class CodeViewPresenter extends AbstractRequirementsPresenter implements Serializable
{

  /**
   * Serial version uid used for serialization
   */
  private static final long         serialVersionUID    = 7107034541114430211L;

  /**
   * Content of project view
   */
  private final CodeViewView        view;
  /**
   * Container for Requirement Status
   */
  private final StatusContainer     statusContainer     = new StatusContainer();
  /**
   * Container for Requirement Repository
   */
  private final RepositoryContainer repositoryContainer = new RepositoryContainer();
  /**
   * Container for Requirement Type
   */
  private final TypeContainer       typeContainer       = new TypeContainer();
  /**
   * Container for CodeView requirements
   */
  private CodeViewContainer codeViewContainer = new CodeViewContainer();

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
  public CodeViewPresenter(final CodeViewView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    initFilters();
    initCodeViewList();
    addListeners();
  }

  /**
   * Initialize the filters values
   */
  private void initFilters()
  {
    view.getFilterStatusComboBox().setContainerDataSource(statusContainer);
    view.getFilterStatusComboBox().setItemCaptionPropertyId(FilterItemProperty.LABEL.getPropertyId());
    view.getFilterStatusComboBox().setItemIconPropertyId(FilterItemProperty.ICON.getPropertyId());
    view.getFilterStatusComboBox().setNewItemsAllowed(false);

    view.getFilterRepositoryComboBox().setContainerDataSource(repositoryContainer);
    view.getFilterRepositoryComboBox().setItemCaptionPropertyId(RepositoryItemProperty.URI.getPropertyId());
    view.getFilterRepositoryComboBox().setNewItemsAllowed(false);
    view.getFilterRepositoryComboBox().setNullSelectionAllowed(true);

    view.getFilterTypeComboBox().setContainerDataSource(typeContainer);
    view.getFilterTypeComboBox().setItemCaptionPropertyId(FilterItemProperty.LABEL.getPropertyId());
    view.getFilterTypeComboBox().setNewItemsAllowed(false);
    view.getFilterTypeComboBox().setNullSelectionAllowed(true);
  }

  /**
   * Initialize the CodeView list
   */
  private void initCodeViewList()
  {
    codeViewContainer = new CodeViewContainer();
    view.getCodeViewTable().setContainerDataSource(codeViewContainer);

    view.getCodeViewTable().addGeneratedColumn(CodeViewItemProperty.CLASS.getPropertyId(),
        new ColumnClassGenerator());
    view.getCodeViewTable().addGeneratedColumn(CodeViewItemProperty.STATUS.getPropertyId(),
        new ColumnStatusGenerator());
    view.getCodeViewTable().addGeneratedColumn(CodeViewItemProperty.REQUIREMENT_NAME.getPropertyId(),
        new ColumnRequirementGenerator(this));

    // Define visibles columns
    view.getCodeViewTable().setVisibleColumns(CodeViewItemProperty.REQUIREMENT_NAME.getPropertyId(),
        CodeViewItemProperty.CLASS.getPropertyId(),
        CodeViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId(),
        CodeViewItemProperty.VERSION_IN_CODE.getPropertyId(), CodeViewItemProperty.STATUS.getPropertyId());

    // Define columns expand ratio
    view.getCodeViewTable()
        .setColumnExpandRatio(CodeViewItemProperty.REQUIREMENT_NAME.getPropertyId(), 0.60F);
    view.getCodeViewTable().setColumnExpandRatio(CodeViewItemProperty.CLASS.getPropertyId(), 0.20F);
    view.getCodeViewTable().setColumnExpandRatio(CodeViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId(),
        0.05F);
    view.getCodeViewTable().setColumnExpandRatio(CodeViewItemProperty.VERSION_IN_CODE.getPropertyId(), 0.10F);
    view.getCodeViewTable().setColumnExpandRatio(CodeViewItemProperty.STATUS.getPropertyId(), 0.05F);
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getFilterApplyButton().addClickListener(new Button.ClickListener()
    {
      /**
       * 
       */
      private static final long serialVersionUID = 5005136944255694232L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        final Filterable requirementFilter = codeViewContainer;
        requirementFilter.removeAllContainerFilters();
        if (!view.getFilterRequirementField().getValue().isEmpty())
        {
          requirementFilter.addContainerFilter(new Or(new SimpleStringFilter(
              CodeViewItemProperty.REQUIREMENT_NAME.getPropertyId(), view.getFilterRequirementField()
                  .getValue(), true, false), new SimpleStringFilter(
              CodeViewItemProperty.REQUIREMENT_DESCRIPTION.getPropertyId(), view.getFilterRequirementField()
                  .getValue(), true, false)));
        }
        if (view.getFilterRepositoryComboBox().getValue() != null)
        {
          requirementFilter.addContainerFilter(new SimpleStringFilter(CodeViewItemProperty.REPOSITORY
              .getPropertyId(), view.getFilterRepositoryComboBox().getValue().toString(), true, false));
        }
        if (view.getFilterStatusComboBox().getValue() != null)
        {
          requirementFilter.addContainerFilter(new Compare.Equal(CodeViewItemProperty.STATUSID
              .getPropertyId(), view.getFilterStatusComboBox().getValue()));
        }
        if (view.getFilterTypeComboBox().getValue() != null)
        {
          requirementFilter.addContainerFilter(new Compare.Equal(CodeViewItemProperty.REQUIREMENT_TYPE
              .getPropertyId(), view.getFilterTypeComboBox().getValue()));
        }
      }
    });
    view.getFilterResetButton().addClickListener(new ClickListener()
    {

      /**
       * {@inheritDoc}
       */
      private static final long serialVersionUID = 8241498087594670893L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        resetFilters();
      }
    });
    view.getCodeViewTable().addActionHandler(new RefreshAction()
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
        refreshFilters();
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
   * Reset filters selected value, and remove any container filters
   */
  private void resetFilters()
  {
    view.getFilterRequirementField().setValue("");
    view.getFilterStatusComboBox().setValue(null);
    view.getFilterRepositoryComboBox().setValue(null);
    view.getFilterTypeComboBox().setValue(null);
    codeViewContainer.removeAllContainerFilters();
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
   * Refresh requirements datas
   */
  protected void refreshRequirements()
  {

    codeViewContainer.removeAllItems();
    final Set<IRequirement> requirements = new HashSet<IRequirement>();
    try
    {
      final IProject findProjectByID = RequirementsModule.getRequirementManagerService().findProjectByID(
          getProjectToolId());
      for (final IRepository repository : findProjectByID.getRepositories())
      {
        final Set<org.novaforge.forge.tools.requirements.common.model.IRequirement> currentRequirements = RequirementsModule
            .getRequirementCodeService().findRequirementsCodeByStatus(getProjectToolId(),
                repository.getURI(), ECodeStatus.ALL);
        requirements.addAll(currentRequirements);
      }
      codeViewContainer.setRequirements(requirements);
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * Refresh the status filter
   */
  private void refreshStatusFilter()
  {
    statusContainer.refreshCodeView(getCurrentLocale());
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
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e,
                                                 UI.getCurrent().getLocale());
    }
  }

  /**
   * Refresh the type filter
   */
  private void refreshTypeFilter()
  {
    final Set<String> types = new HashSet<String>();
    for (final Object itemId : codeViewContainer.getItemIds())
    {
      final String itemType = (String) codeViewContainer.getItem(itemId)
          .getItemProperty(CodeViewItemProperty.REQUIREMENT_TYPE.getPropertyId()).getValue();
      if (itemType != null)
      {
        types.add(itemType);
      }
    }
    typeContainer.setTypes(types);
  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    if (RequirementsModule.getApplicationRequestService().isAssociated(getProjectForgeId(),
                                                                       RequirementsRequest.searchRequirementInSourceCode
                                                                           .name()))
    {
      refreshContent();
    }
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Get the view element
   *
   * @return the view
   */
  public CodeViewView getView()
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
    statusContainer.refreshCodeView(getCurrentLocale());

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

  /**
   * Show the requirement details window
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
                RequirementsModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.DETAILS_REQUIREMENT_NOTFOUND));
      }
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }

  }

}
