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
package org.novaforge.forge.ui.requirements.internal.client.requirement.list;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.requirements.internal.client.containers.TreeContainer;
import org.novaforge.forge.ui.requirements.internal.client.containers.TreeItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.AbstractRequirementsPresenter;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.io.Serializable;
import java.util.Locale;

/**
 * This presenter handles requirements list view.
 * 
 * @author Jeremy Casery
 */
public class RequirementListPresenter extends AbstractRequirementsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long         serialVersionUID = -5042299647493799344L;
  /**
   * Content of project view
   */
  private final RequirementListView view;
  /**
   * The container used for tree item
   */
  private final TreeContainer       container        = new TreeContainer();

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
  public RequirementListPresenter(final RequirementListView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    addListeners();
    initRequirementsList();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getRequirementsTree().addActionHandler(new RefreshAction()
    {

      /**
       *
       */
      private static final long serialVersionUID = -3398141162733310892L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void refreshAction()
      {
        refreshContent();
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
    view.getRequirementsTree().addItemClickListener(new ItemClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = 3863282946524185869L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void itemClick(final ItemClickEvent event)
      {
        final Item itemClicked = event.getItem();
        if ((boolean) itemClicked.getItemProperty(TreeItemProperty.ISREQUIREMENT.getPropertyId()).getValue())
        {
          setRequirementDetailsValues((String) itemClicked.getItemProperty(TreeItemProperty.ID.getPropertyId())
                                                          .getValue());
          view.showDetailsLayout(true);
        }
      }
    });
    view.getDetailsView().getHeaderCloseButton().addClickListener(new ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -6811580096441018703L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.showDetailsLayout(false);
        view.getRequirementsTree().select(null);
      }
    });
    view.getRequirementsTreeExpandButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -6811580096441018703L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        for (final Object rootId : view.getRequirementsTree().rootItemIds())
        {
          view.getRequirementsTree().expandItemsRecursively(rootId);
        }

      }
    });
    view.getRequirementsTreeCollapseButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = 1620782862309494023L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        for (final Object rootId : view.getRequirementsTree().rootItemIds())
        {
          view.getRequirementsTree().collapseItemsRecursively(rootId);
        }
      }
    });
    view.getRequirementsFilterButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -3987379081678468903L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        container.removeAllContainerFilters();
        final Filter requirementsFilter = new SimpleStringFilter(TreeItemProperty.PATH.getPropertyId(),
                                                                 view.getRequirementsFilterText().getValue(), true,
                                                                 false);
        container.addContainerFilter(requirementsFilter);

      }
    });
    view.getRequirementsResetFilterButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -6710384373129438142L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        container.removeAllContainerFilters();
        view.getRequirementsFilterText().setValue("");
      }
    });
  }

  private void initRequirementsList()
  {
    view.getRequirementsTree().setContainerDataSource(container);
  }

  /**
   * Set requirement details view values for given requirement ID
   * 
   * @param pRequirementID
   *          The requirement ID to show
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
        view.getDetailsView().refreshLocale(getCurrentLocale());
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

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());

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
    try
    {
      final IProject project = RequirementsModule.getRequirementManagerService().findProjectByID(
          getProjectToolId());
      container.setRepositories(project.getRepositories());
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
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
