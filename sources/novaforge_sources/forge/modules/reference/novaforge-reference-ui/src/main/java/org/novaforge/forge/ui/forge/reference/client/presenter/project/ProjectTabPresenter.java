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
package org.novaforge.forge.ui.forge.reference.client.presenter.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.project.CancelEditProjectEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.EditProjectEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.SaveEditProjectEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.CancelCreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.CancelEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.CreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.DeleteApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.EditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.SaveCreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.SaveEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.SelectApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.CancelCreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.CancelEditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.CreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.DeleteSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.EditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.SaveCreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.SaveEditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.SelectSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ViewEnum;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.applications.ApplicationAddPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.applications.ApplicationDetailPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.applications.ApplicationEditPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.spaces.SpaceDetailPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.spaces.SpaceManagePresenter;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.project.ProjectDetailViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.project.ProjectEditViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.project.ProjectTabView;
import org.novaforge.forge.ui.forge.reference.client.view.project.applications.ApplicationAddViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.project.applications.ApplicationDetailViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.project.applications.ApplicationEditViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.project.resources.ProjectResources;
import org.novaforge.forge.ui.forge.reference.client.view.project.spaces.SpaceDetailViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.project.spaces.SpaceManageViewImpl;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.NodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.ProjectReferenceDTO;
import org.novaforge.forge.ui.forge.reference.shared.SpaceNodeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lamirang
 */
public class ProjectTabPresenter implements Presenter
{

	private static ProjectResources          ressources = GWT.create(ProjectResources.class);
	private final ProjectMessage             messages   = (ProjectMessage) GWT.create(ProjectMessage.class);

	private final ProjectTabView             display;
	private final ProjectDetailPresenter     projectDetailPresenter;
	private final ProjectEditPresenter       projectEditPresenter;
	private final SpaceManagePresenter       spaceAddPresenter;
	private final SpaceManagePresenter       spaceEditPresenter;
	private final SpaceDetailPresenter       spaceDetailPresenter;
	private final ApplicationAddPresenter    applicationAddPresenter;
	private final ApplicationDetailPresenter applicationDetailPresenter;
	private final ApplicationEditPresenter   applicationEditPresenter;
	private final List<Integer>              openedChild;
	private ProjectReferenceDTO currentProject;
	private ViewEnum            activatedView;
	private NodeDTO             currentSelectedNode;

	public ProjectTabPresenter(final ProjectTabView display)
	{
		super();
		this.display = display;
		openedChild = new ArrayList<Integer>();

		projectDetailPresenter = new ProjectDetailPresenter(new ProjectDetailViewImpl());
		projectEditPresenter = new ProjectEditPresenter(new ProjectEditViewImpl());
		spaceAddPresenter = new SpaceManagePresenter(new SpaceManageViewImpl(ManagePresenterType.CREATE),
		    ManagePresenterType.CREATE);
		spaceEditPresenter = new SpaceManagePresenter(new SpaceManageViewImpl(ManagePresenterType.UPDATE),
		    ManagePresenterType.UPDATE);
		spaceDetailPresenter = new SpaceDetailPresenter(new SpaceDetailViewImpl());
		applicationAddPresenter = new ApplicationAddPresenter(new ApplicationAddViewImpl());
		applicationEditPresenter = new ApplicationEditPresenter(new ApplicationEditViewImpl());
		applicationDetailPresenter = new ApplicationDetailPresenter(new ApplicationDetailViewImpl());

		bind();
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
	}

	public void bind()
	{
		display.getProjectTitle().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent pEvent)
			{
				switch (activatedView)
				{
					case ADD:
					case EDIT:
						displayEmptyMessage();
						break;

					case READ:
					default:
						activatedView = ViewEnum.READ;
						ProjectTabPresenter.this.selectProjectTitle();
						break;
				}

			}

		});
		/*
		 * Node selection
		 */
		display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{

			@Override
			public void onSelectionChange(final SelectionChangeEvent pEvent)
			{
				currentSelectedNode = display.getSelectionModel().getSelectedObject();
				if (currentSelectedNode != null)
				{
					switch (activatedView)
					{
						case ADD:
						case EDIT:
							displayEmptyMessage();
							break;

						case READ:
							if (currentSelectedNode instanceof SpaceNodeDTO)
							{
								ForgeReferenceEntryPoint.getEventBus().fireEvent(
								    new SelectSpaceEvent((SpaceNodeDTO) currentSelectedNode));
							}
							else if (currentSelectedNode instanceof ApplicationNodeDTO)
							{
								ForgeReferenceEntryPoint.getEventBus().fireEvent(
								    new SelectApplicationEvent((ApplicationNodeDTO) currentSelectedNode));

							}
							break;
					}
				}

			}

		});
		display.getProjectTree().addOpenHandler(new OpenHandler<TreeNode>()
		{

			@Override
			public void onOpen(final OpenEvent<TreeNode> pEvent)
			{
				openedChild.add(pEvent.getTarget().getIndex());
			}
		});
		display.getProjectTree().addCloseHandler(new CloseHandler<TreeNode>()
		{

			@Override
			public void onClose(final CloseEvent<TreeNode> pEvent)
			{
				openedChild.remove(pEvent.getTarget().getIndex());
			}
		});
		/*
		 * Project Event
		 */
		addProjectHandler();
		/*
		 * Space Event
		 */
		addSpaceHandler();
		/*
		 * Application Event
		 */
		addApplicationHandler();
	}

	/**
	 * 
	 */
	private void addApplicationHandler()
	{
		ForgeReferenceEntryPoint.getEventBus().addHandler(SelectApplicationEvent.TYPE,
		    new SelectApplicationEvent.Handler()
		    {

			    @Override
			    public void displayApplication(final SelectApplicationEvent pEvent)
			    {
				    display.getProjectTitle().setStyleName(ressources.style().projectTitle());
				    activatedView = ViewEnum.READ;
				    applicationDetailPresenter.go(display.getContentPanel());
				    applicationDetailPresenter.updateApplicationDetail(pEvent.getApplication());
			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(CreateApplicationEvent.TYPE,
		    new CreateApplicationEvent.Handler()
		    {

			    @Override
			    public void displayCreateApplication(final CreateApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.ADD;
				    applicationAddPresenter.go(display.getContentPanel());
				    applicationAddPresenter.updateApplication(

				    (SpaceNodeDTO) currentSelectedNode);

			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(SaveCreateApplicationEvent.TYPE,
		    new SaveCreateApplicationEvent.Handler()
		    {
			    @Override
			    public void saveCreateApplication(final SaveCreateApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.createApplication(pEvent.getApplication(), pEvent.getRolesMapping());

			    }

		    });

		ForgeReferenceEntryPoint.getEventBus().addHandler(CancelCreateApplicationEvent.TYPE,
		    new CancelCreateApplicationEvent.Handler()
		    {
			    @Override
			    public void cancelCreateApplication(final CancelCreateApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.selectNode(currentSelectedNode.getName(), true);
				    ForgeReferenceEntryPoint.getEventBus().fireEvent(
				        new SelectSpaceEvent((SpaceNodeDTO) currentSelectedNode));

			    }

		    });

		ForgeReferenceEntryPoint.getEventBus().addHandler(EditApplicationEvent.TYPE,
		    new EditApplicationEvent.Handler()
		    {
			    @Override
			    public void editApplication(final EditApplicationEvent pEvent)
			    {

				    activatedView = ViewEnum.EDIT;
				    applicationEditPresenter.go(display.getContentPanel());
				    applicationEditPresenter.updateApplication(

				    (ApplicationNodeDTO) currentSelectedNode);

			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(CancelEditApplicationEvent.TYPE,
		    new CancelEditApplicationEvent.Handler()
		    {

			    @Override
			    public void cancelEditSpace(final CancelEditApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.selectNode(currentSelectedNode.getName(), true);
				    ForgeReferenceEntryPoint.getEventBus().fireEvent(
				        new SelectApplicationEvent((ApplicationNodeDTO) currentSelectedNode));

			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(SaveEditApplicationEvent.TYPE,
		    new SaveEditApplicationEvent.Handler()
		    {

			    @Override
			    public void saveEditApplication(final SaveEditApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.updateApplication(pEvent.getApplication(), pEvent.getRolesMapping());

			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(DeleteApplicationEvent.TYPE,
		    new DeleteApplicationEvent.Handler()
		    {

			    @Override
			    public void deleteApplication(final DeleteApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.deleteApplication(pEvent.getApplication());
			    }

		    });
	}

	/**
	 * 
	 */
	private void addSpaceHandler()
	{
		/*
		 * Space event
		 */
		ForgeReferenceEntryPoint.getEventBus().addHandler(SelectSpaceEvent.TYPE, new SelectSpaceEvent.Handler()
		{
			@Override
			public void displaySpace(final SelectSpaceEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				display.getProjectTitle().setStyleName(ressources.style().projectTitle());
				spaceDetailPresenter.go(display.getContentPanel());
				spaceDetailPresenter.updateSpaceDetails(pEvent.getSpace());
			}

		});
		ForgeReferenceEntryPoint.getEventBus().addHandler(EditSpaceEvent.TYPE, new EditSpaceEvent.Handler()
		{
			@Override
			public void editSpace(final EditSpaceEvent pEvent)
			{
				activatedView = ViewEnum.EDIT;
				spaceEditPresenter.go(display.getContentPanel());
				spaceEditPresenter.updateSpace(pEvent.getSpace());

			}

		});
		ForgeReferenceEntryPoint.getEventBus().addHandler(SaveEditSpaceEvent.TYPE,
		    new SaveEditSpaceEvent.Handler()
		    {
			    @Override
			    public void saveEditSpace(final SaveEditSpaceEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.updateSpace(pEvent.getSpace());

			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(CancelEditSpaceEvent.TYPE,
		    new CancelEditSpaceEvent.Handler()
		    {
			    @Override
			    public void cancelEditSpace(final CancelEditSpaceEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.selectNode(currentSelectedNode.getName(), true);
				    ForgeReferenceEntryPoint.getEventBus().fireEvent(
				        new SelectSpaceEvent((SpaceNodeDTO) currentSelectedNode));

			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(DeleteSpaceEvent.TYPE, new DeleteSpaceEvent.Handler()
		{
			@Override
			public void deleteSpace(final DeleteSpaceEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				ProjectTabPresenter.this.deleteSpace(pEvent.getSpace());

			}

		});
		ForgeReferenceEntryPoint.getEventBus().addHandler(CreateSpaceEvent.TYPE, new CreateSpaceEvent.Handler()
		{
			@Override
			public void displayCreateSpace(final CreateSpaceEvent pEvent)
			{
				activatedView = ViewEnum.ADD;
				spaceAddPresenter.go(display.getContentPanel());
				spaceAddPresenter.updateRootNode(currentProject.getRootNode());
			}

		});
		ForgeReferenceEntryPoint.getEventBus().addHandler(SaveCreateSpaceEvent.TYPE,
		    new SaveCreateSpaceEvent.Handler()
		    {
			    @Override
			    public void saveCreateSpace(final SaveCreateSpaceEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.createSpace(pEvent.getSpace());
			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(CancelCreateSpaceEvent.TYPE,
		    new CancelCreateSpaceEvent.Handler()
		    {
			    @Override
			    public void cancelCreateSpace(final CancelCreateSpaceEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    ProjectTabPresenter.this.selectProjectTitle();
			    }

		    });
	}

	/**
	 * 
	 */
	private void addProjectHandler()
	{
		ForgeReferenceEntryPoint.getEventBus().addHandler(EditProjectEvent.TYPE, new EditProjectEvent.Handler()
		{

			@Override
			public void editProject(final EditProjectEvent pEvent)
			{
				activatedView = ViewEnum.EDIT;
				projectEditPresenter.go(display.getContentPanel());
				projectEditPresenter.updateProjectDetails(pEvent.getProjectDTO());
			}

		});
		ForgeReferenceEntryPoint.getEventBus().addHandler(SaveEditProjectEvent.TYPE,
		    new SaveEditProjectEvent.Handler()
		    {

			    @Override
			    public void saveUpdateProject(final SaveEditProjectEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    currentProject = pEvent.getProjectDTO();
				    ProjectTabPresenter.this.updateProject(currentProject);
				    ProjectTabPresenter.this.selectProjectTitle();

			    }

		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(CancelEditProjectEvent.TYPE,
		    new CancelEditProjectEvent.Handler()
		    {

			    @Override
			    public void cancelEditProject(final CancelEditProjectEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    display.getProjectTitle().setStyleName(ressources.style().selectedTitle());
				    display.getSelectionModel().setSelected(display.getSelectionModel().getSelectedObject(), false);
				    projectDetailPresenter.go(display.getContentPanel());
				    projectDetailPresenter.updateProjectDetails(currentProject);

			    }
		    });
	}

	private void displayEmptyMessage()
	{
		if (currentSelectedNode != null)
		{
			if (currentSelectedNode instanceof SpaceNodeDTO)
			{
				final InfoDialogBox info = new InfoDialogBox(messages.editionSpaceActive());
				info.show();
			}
			else if (currentSelectedNode instanceof ApplicationNodeDTO)
			{
				final InfoDialogBox info = new InfoDialogBox(messages.editionAppActive());
				info.show();
			}
		}
		else
		{
			final InfoDialogBox info = new InfoDialogBox(messages.editionProjectActive());
			info.show();

		}
	}

	private void updateProject(final ProjectReferenceDTO pProjectDTO)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().updateProjectReference(pProjectDTO, pCb);
			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				if (pResult)
				{
					refreshProjectTree(null, false, true, true);
				}

			}



		}.retry(0);

	}

	private void createSpace(final SpaceNodeDTO pSpace)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().createSpace(pSpace, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{

				refreshProjectTree(pSpace.getName(), true, false, true);

			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void updateSpace(final SpaceNodeDTO pSpace)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().updateSpace(pSpace, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				refreshProjectTree(pSpace.getName(), true, true, true);
			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void deleteSpace(final SpaceNodeDTO pSpace)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().deleteSpace(pSpace, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				refreshProjectTree(null, false, false, true);
			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void createApplication(final ApplicationNodeDTO pApp, final Map<String, String> pRolesMapping)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().createApplication(pApp, pRolesMapping, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				refreshProjectTree(pApp.getName(), false, true, true);

			}

			@Override
			public void onFailure(final Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void updateApplication(final ApplicationNodeDTO pApp, final Map<String, String> pRolesMapping)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().updateApplication(pApp, pRolesMapping, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				refreshProjectTree(pApp.getName(), false, true, true);

			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void deleteApplication(final ApplicationNodeDTO pApp)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().deleteApplication(pApp, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				refreshProjectTree(pApp.getSpaceParent().getUri(), true, true, true);

			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void selectNode(final String pNameOrUri, final boolean iSpace)
	{
		final List<SpaceNodeDTO> list = display.getSpacesDataProvider().getList();
		for (final SpaceNodeDTO space : list)
		{
			if (iSpace)
			{
				if ((space.getName().equals(pNameOrUri)) || (space.getUri().equals(pNameOrUri)))
				{
					display.getSelectionModel().setSelected(space, true);
					break;
				}
			}
			else
			{
				for (final ApplicationNodeDTO app : space.getApplications())
				{
					if ((app.getName().equals(pNameOrUri)) || (app.getUri().equals(pNameOrUri)))
					{
						display.getSelectionModel().setSelected(app, true);
						break;
					}

				}
			}

		}
		currentSelectedNode = display.getSelectionModel().getSelectedObject();
	}

	private void selectProjectTitle()
	{
		activatedView = ViewEnum.READ;
		display.getProjectTitle().setStyleName(ressources.style().selectedTitle());
		display.getSelectionModel().setSelected(display.getSelectionModel().getSelectedObject(), false);
		projectDetailPresenter.go(display.getContentPanel());
		projectDetailPresenter.updateProjectDetails(currentProject);
	}

	public void refreshProjectTree(final String pSelectedNode, final boolean pIsSpace, final boolean pReOpened,
	    final boolean pTreeEvent)
	{
		new AbstractReferenceRPCCall<ProjectReferenceDTO>()
		{
			@Override
			protected void callService(final AsyncCallback<ProjectReferenceDTO> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().getProjectReference(pCb);
			}

			@Override
			public void onSuccess(final ProjectReferenceDTO pResult)
			{
				if (pResult != null)
				{
					currentProject = pResult;
					display.getProjectTitle().setText(currentProject.getName());
					updateSpaceList();
					openPreviousSpaces(pReOpened);
					selection(pSelectedNode, pIsSpace);

				}
			}

			/**
          * 
          */
			private void updateSpaceList()
			{
				final List<SpaceNodeDTO> spaces = currentProject.getRootNode().getSpaces();
				if (!spaces.isEmpty())
				{
					display.getSpacesDataProvider().setList(spaces);
				}
				else
				{
					display.getSpacesDataProvider().getList().clear();
				}
			}

			/**
          * 
          */
			private void openPreviousSpaces(final boolean pReOpened)
			{
				if (pReOpened)
				{
					// Used to open space which was previously opened
					final TreeNode rootNode = display.getProjectTree().getRootTreeNode();
					for (final Integer integer : openedChild)
					{
						rootNode.setChildOpen(integer, true, false);
					}
				}
			}

			/**
			 * @param pSelectedNode
			 * @param pIsSpace
			 */
			private void selection(final String pSelectedNode, final boolean pIsSpace)
			{
				if (pSelectedNode == null)
				{
					selectProjectTitle();
				}
				else if (pSelectedNode != null)
				{
					selectNode(pSelectedNode, pIsSpace);
					if (currentSelectedNode instanceof SpaceNodeDTO)
					{
						ForgeReferenceEntryPoint.getEventBus().fireEvent(
						    new SelectSpaceEvent((SpaceNodeDTO) currentSelectedNode));
					}
					else if (currentSelectedNode instanceof ApplicationNodeDTO)
					{
						ForgeReferenceEntryPoint.getEventBus().fireEvent(
						    new SelectApplicationEvent((ApplicationNodeDTO) currentSelectedNode));

					}
				}
			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}

	/**
	 * @return the activatedView
	 */
	public ViewEnum getActivatedView()
	{
		return activatedView;
	}

}
