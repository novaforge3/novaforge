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
package org.novaforge.forge.ui.forge.reference.client.presenter.template.apps;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateCancelCreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateCancelEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateCreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateDeleteApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateSaveCreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateSaveEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateSelectApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateCancelCreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateCancelEditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateCreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateDeleteSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateEditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateSaveCreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateSaveEditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateSelectSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ViewEnum;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.apps.applications.TemplateApplicationAddPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.apps.applications.TemplateApplicationDetailPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.apps.applications.TemplateApplicationEditPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.apps.spaces.TemplateSpaceDetailPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.apps.spaces.TemplateSpaceManagePresenter;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.TemplateEditSpacesView;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.TemplateSpaceStartViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.applications.TemplateApplicationAddViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.applications.TemplateApplicationDetailViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.applications.TemplateApplicationEditViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.spaces.TemplateSpaceDetailViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.spaces.TemplateSpaceManageViewImpl;
import org.novaforge.forge.ui.forge.reference.shared.NodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.ui.forge.reference.shared.exceptions.ReferenceServiceException;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateRootNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateSpaceNodeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lamirang
 */
public class TemplateManageSpacesPresenter implements Presenter
{

	private final ProjectMessage                     messages = (ProjectMessage) GWT
	                                                              .create(ProjectMessage.class);

	private final SimpleEventBus                     eventBus;
	private final TemplateEditSpacesView             display;
	private final TemplateSpaceManagePresenter       spaceAddPresenter;
	private final TemplateSpaceManagePresenter       spaceEditPresenter;
	private final TemplateSpaceDetailPresenter       spaceDetailPresenter;
	private final TemplateApplicationAddPresenter    applicationAddPresenter;
	private final TemplateApplicationDetailPresenter applicationDetailPresenter;
	private final TemplateApplicationEditPresenter   applicationEditPresenter;
	private final TemplateSpaceStartPresenter        templateSpaceStartPresenter;
	private final List<Integer>                      openedChild;
	private TemplateDTO currentTemplate;
	private ViewEnum    activatedView;
	private NodeDTO     currentSelectedNode;

	public TemplateManageSpacesPresenter(final SimpleEventBus eventBus, final TemplateEditSpacesView display)
	{
		super();
		this.eventBus = eventBus;
		this.display = display;
		openedChild = new ArrayList<Integer>();

		templateSpaceStartPresenter = new TemplateSpaceStartPresenter(eventBus, new TemplateSpaceStartViewImpl());
		spaceAddPresenter = new TemplateSpaceManagePresenter(eventBus, new TemplateSpaceManageViewImpl(
		    ManagePresenterType.CREATE), ManagePresenterType.CREATE);
		spaceEditPresenter = new TemplateSpaceManagePresenter(eventBus, new TemplateSpaceManageViewImpl(
		    ManagePresenterType.UPDATE), ManagePresenterType.UPDATE);
		spaceDetailPresenter = new TemplateSpaceDetailPresenter(eventBus, new TemplateSpaceDetailViewImpl());
		applicationAddPresenter = new TemplateApplicationAddPresenter(eventBus,
		    new TemplateApplicationAddViewImpl());
		applicationEditPresenter = new TemplateApplicationEditPresenter(eventBus,
		    new TemplateApplicationEditViewImpl());
		applicationDetailPresenter = new TemplateApplicationDetailPresenter(eventBus,
		    new TemplateApplicationDetailViewImpl());

		bind();
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
		templateSpaceStartPresenter.go(display.getContentPanel());
	}

	public void bind()
	{
		display.getSpaceTitle().addClickHandler(new ClickHandler()
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
						templateSpaceStartPresenter.go(display.getContentPanel());
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
							TemplateManageSpacesPresenter.this.displayEmptyMessage();
							break;

						case READ:
							if (currentSelectedNode instanceof TemplateSpaceNodeDTO)
							{
								eventBus.fireEvent(new TemplateSelectSpaceEvent((TemplateSpaceNodeDTO) currentSelectedNode));
							}
							else if (currentSelectedNode instanceof TemplateApplicationNodeDTO)
							{
								eventBus.fireEvent(new TemplateSelectApplicationEvent(
								    (TemplateApplicationNodeDTO) currentSelectedNode));

							}
							break;
					}
				}

			}

		});
		display.getTemplateTree().addOpenHandler(new OpenHandler<TreeNode>()
		{

			@Override
			public void onOpen(final OpenEvent<TreeNode> pEvent)
			{
				openedChild.add(pEvent.getTarget().getIndex());
			}
		});
		display.getTemplateTree().addCloseHandler(new CloseHandler<TreeNode>()
		{

			@Override
			public void onClose(final CloseEvent<TreeNode> pEvent)
			{
				openedChild.remove(pEvent.getTarget().getIndex());
			}
		});
		/*
		 * Space event
		 */
		eventBus.addHandler(TemplateSelectSpaceEvent.TYPE, new TemplateSelectSpaceEvent.Handler()
		{
			@Override
			public void displaySpace(final TemplateSelectSpaceEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				spaceDetailPresenter.go(display.getContentPanel());
				spaceDetailPresenter.updateSpaceDetails(pEvent.getSpace());
			}

		});
		eventBus.addHandler(TemplateEditSpaceEvent.TYPE, new TemplateEditSpaceEvent.Handler()
		{
			@Override
			public void editSpace(final TemplateEditSpaceEvent pEvent)
			{
				activatedView = ViewEnum.EDIT;
				spaceEditPresenter.go(display.getContentPanel());
				spaceEditPresenter.setRootNode(pEvent.getSpace().getRootNode());
				spaceEditPresenter.updateSpace(pEvent.getSpace());

			}

		});
		eventBus.addHandler(TemplateSaveEditSpaceEvent.TYPE, new TemplateSaveEditSpaceEvent.Handler()
		{
			@Override
			public void saveEditSpace(final TemplateSaveEditSpaceEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				updateSpace(pEvent.getOldName(), pEvent.getSpace());

			}

		});
		eventBus.addHandler(TemplateCancelEditSpaceEvent.TYPE, new TemplateCancelEditSpaceEvent.Handler()
		{
			@Override
			public void cancelEditSpace(final TemplateCancelEditSpaceEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				TemplateManageSpacesPresenter.this.selectNode(currentSelectedNode.getName(), true);
				eventBus.fireEvent(new TemplateSelectSpaceEvent((TemplateSpaceNodeDTO) currentSelectedNode));

			}

		});
		eventBus.addHandler(TemplateDeleteSpaceEvent.TYPE, new TemplateDeleteSpaceEvent.Handler()
		{
			@Override
			public void deleteSpace(final TemplateDeleteSpaceEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				TemplateManageSpacesPresenter.this.deleteSpace(pEvent.getSpace());

			}

		});
		eventBus.addHandler(TemplateCreateSpaceEvent.TYPE, new TemplateCreateSpaceEvent.Handler()
		{
			@Override
			public void displayCreateSpace(final TemplateCreateSpaceEvent pEvent)
			{
				activatedView = ViewEnum.ADD;
				spaceAddPresenter.go(display.getContentPanel());
				spaceAddPresenter.setRootNode(currentTemplate.getRootNode());
			}

		});
		eventBus.addHandler(TemplateSaveCreateSpaceEvent.TYPE, new TemplateSaveCreateSpaceEvent.Handler()
		{
			@Override
			public void saveCreateSpace(final TemplateSaveCreateSpaceEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				TemplateManageSpacesPresenter.this.createSpace(pEvent.getSpace());
			}

		});
		eventBus.addHandler(TemplateCancelCreateSpaceEvent.TYPE, new TemplateCancelCreateSpaceEvent.Handler()
		{
			@Override
			public void cancelCreateSpace(final TemplateCancelCreateSpaceEvent pEvent)
			{
				activatedView = ViewEnum.READ;
			}

		});
		/*
		 * Application Event
		 */
		eventBus.addHandler(TemplateSelectApplicationEvent.TYPE, new TemplateSelectApplicationEvent.Handler()
		{

			@Override
			public void displayApplication(final TemplateSelectApplicationEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				applicationDetailPresenter.go(display.getContentPanel());
				applicationDetailPresenter.updateApplicationDetail(pEvent.getApplication(),
				    currentTemplate.getRoles());
			}

		});
		eventBus.addHandler(TemplateCreateApplicationEvent.TYPE, new TemplateCreateApplicationEvent.Handler()
		{

			@Override
			public void displayCreateApplication(final TemplateCreateApplicationEvent pEvent)
			{
				activatedView = ViewEnum.ADD;
				applicationAddPresenter.go(display.getContentPanel());
				applicationAddPresenter.updateApplication((TemplateSpaceNodeDTO) currentSelectedNode,
				    currentTemplate.getRoles());

			}

		});
		eventBus.addHandler(TemplateSaveCreateApplicationEvent.TYPE,
		    new TemplateSaveCreateApplicationEvent.Handler()
		    {
			    @Override
			    public void saveCreateApplication(final TemplateSaveCreateApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    TemplateManageSpacesPresenter.this.createApplication(pEvent.getApplication());

			    }

		    });

		eventBus.addHandler(TemplateCancelCreateApplicationEvent.TYPE,
		    new TemplateCancelCreateApplicationEvent.Handler()
		    {
			    @Override
			    public void cancelCreateApplication(final TemplateCancelCreateApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    TemplateManageSpacesPresenter.this.selectNode(currentSelectedNode.getName(), true);
				    eventBus.fireEvent(new TemplateSelectSpaceEvent((TemplateSpaceNodeDTO) currentSelectedNode));

			    }

		    });

		eventBus.addHandler(TemplateEditApplicationEvent.TYPE, new TemplateEditApplicationEvent.Handler()
		{
			@Override
			public void editApplication(final TemplateEditApplicationEvent pEvent)
			{

				activatedView = ViewEnum.EDIT;
				applicationEditPresenter.go(display.getContentPanel());
				applicationEditPresenter.updateApplication((TemplateApplicationNodeDTO) currentSelectedNode,
				    currentTemplate.getRoles());

			}

		});
		eventBus.addHandler(TemplateCancelEditApplicationEvent.TYPE,
		    new TemplateCancelEditApplicationEvent.Handler()
		    {

			    @Override
			    public void cancelEditSpace(final TemplateCancelEditApplicationEvent pEvent)
			    {
				    activatedView = ViewEnum.READ;
				    TemplateManageSpacesPresenter.this.selectNode(currentSelectedNode.getName(), true);
				    eventBus.fireEvent(new TemplateSelectApplicationEvent(
				        (TemplateApplicationNodeDTO) currentSelectedNode));

			    }

		    });
		eventBus.addHandler(TemplateSaveEditApplicationEvent.TYPE, new TemplateSaveEditApplicationEvent.Handler()
		{

			@Override
			public void saveEditApplication(final TemplateSaveEditApplicationEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				TemplateManageSpacesPresenter.this.updateApplication(pEvent.getApplication());

			}

		});
		eventBus.addHandler(TemplateDeleteApplicationEvent.TYPE, new TemplateDeleteApplicationEvent.Handler()
		{

			@Override
			public void deleteApplication(final TemplateDeleteApplicationEvent pEvent)
			{
				activatedView = ViewEnum.READ;
				TemplateManageSpacesPresenter.this.deleteApplication(pEvent.getApplication());
			}

		});
	}

	private void displayEmptyMessage()
	{
		if (currentSelectedNode != null)
		{
			if (currentSelectedNode instanceof TemplateSpaceNodeDTO)
			{
				final InfoDialogBox info = new InfoDialogBox(messages.editionSpaceActive());
				info.show();
			}
			else if (currentSelectedNode instanceof TemplateApplicationNodeDTO)
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

	private void createSpace(final TemplateSpaceNodeDTO pSpace)
	{
		isExistingSpace(pSpace.getName());

		currentTemplate.getRootNode().getSpaces().add(pSpace);
		refreshTree(pSpace.getName(), true, false);

	}

	private void updateSpace(final String pOldName, final TemplateSpaceNodeDTO pSpace)
	{
		if (!pOldName.equals(pSpace.getName()))
		{
			isExistingSpace(pSpace.getName());
			final List<TemplateSpaceNodeDTO> spaces = currentTemplate.getRootNode().getSpaces();
			for (final TemplateSpaceNodeDTO space : spaces)
			{
				if (space.getName().equals(pOldName))
				{
					space.setUri(pSpace.getUri());
					space.setName(pSpace.getName());
					for (final TemplateApplicationNodeDTO app : space.getApplications())
					{
						app.setSpaceParent(space);
					}
					break;
				}
			}
			refreshTree(pSpace.getName(), true, true);
		}
	}

	/**
	 * @param pSpace
	 * @return
	 */
	private void isExistingSpace(final String pSpaceName)
	{
		boolean onError = false;
		for (final TemplateSpaceNodeDTO space : currentTemplate.getRootNode().getSpaces())
		{
			if (space.getName().equals(pSpaceName))
			{
				onError = true;
				break;
			}
		}
		if (onError)
		{
			ErrorManagement.displayErrorMessage(new ReferenceServiceException(
			    ErrorEnumeration.ERR_CREATE_SPACE_NAME_ALREADY_EXIST));
		}
	}

	private void deleteSpace(final TemplateSpaceNodeDTO pSpace)
	{
		currentTemplate.getRootNode().getSpaces().remove(pSpace);
		refreshTree(null, false, false);

	}

	private void createApplication(final TemplateApplicationNodeDTO pApp)
	{
		final List<TemplateSpaceNodeDTO> spaces = currentTemplate.getRootNode().getSpaces();
		for (final TemplateSpaceNodeDTO templateSpaceNodeDTO : spaces)
		{
			if (templateSpaceNodeDTO.getName().equals(pApp.getSpaceParent().getName()))
			{
				templateSpaceNodeDTO.getApplications().add(pApp);
				break;
			}
		}
		refreshTree(pApp.getName(), false, true);
	}

	private void updateApplication(final TemplateApplicationNodeDTO pApp)
	{
		final List<TemplateSpaceNodeDTO> spaces = currentTemplate.getRootNode().getSpaces();
		for (final TemplateSpaceNodeDTO templateSpaceNodeDTO : spaces)
		{
			if (templateSpaceNodeDTO.getName().equals(pApp.getSpaceParent().getName()))
			{
				for (final TemplateApplicationNodeDTO application : templateSpaceNodeDTO.getApplications())
				{
					if (application.getName().equals(pApp.getName()))
					{
						application.setRolesMapping(pApp.getRolesMapping());
						break;
					}
				}
			}
		}
		refreshTree(pApp.getName(), false, true);
	}

	private void deleteApplication(final TemplateApplicationNodeDTO pApp)
	{
		final List<TemplateSpaceNodeDTO> spaces = currentTemplate.getRootNode().getSpaces();
		for (final TemplateSpaceNodeDTO templateSpaceNodeDTO : spaces)
		{
			if (templateSpaceNodeDTO.getName().equals(pApp.getSpaceParent().getName()))
			{
				final List<TemplateApplicationNodeDTO> temp = new ArrayList<TemplateApplicationNodeDTO>(
				    templateSpaceNodeDTO.getApplications());
				for (final TemplateApplicationNodeDTO application : temp)
				{
					if (application.getName().equals(pApp.getName()))
					{
						templateSpaceNodeDTO.getApplications().remove(application);
						break;
					}
				}
			}
		}
		refreshTree(pApp.getSpaceParent().getName(), true, true);
	}

	/**
	 * @return the currentTemplateDTO
	 */
	public TemplateDTO getCurrentTemplateDTO()
	{
		return currentTemplate;
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}

	/**
	 * @param pCurrentTemplate
	 */
	public void refreshTemplateTree(final TemplateDTO pCurrentTemplate)
	{
		currentTemplate = pCurrentTemplate;
		refreshTree(null, false, false);

	}

	private void refreshTree(final String pSelectedNode, final boolean pIsSpace, final boolean pReOpened)
	{
		updateSpaceList();
		openPreviousSpaces(pReOpened);
		selection(pSelectedNode, pIsSpace);

	}

	/**
	 *
	 */
	private void updateSpaceList()
	{
		if (currentTemplate.getRootNode() == null)
		{
			currentTemplate.setRootNode(new TemplateRootNodeDTO());
		}
		final List<TemplateSpaceNodeDTO> spaces = currentTemplate.getRootNode().getSpaces();
		if (!spaces.isEmpty())
		{
			display.getSpacesDataProvider().setList(spaces);
		}
		else if (Common.isDebugMode())
		{
			final List<TemplateSpaceNodeDTO> newSpace = new ArrayList<TemplateSpaceNodeDTO>();
			newSpace.add(new TemplateSpaceNodeDTO(null, "debugging space"));
			display.getSpacesDataProvider().setList(newSpace);
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
			final TreeNode rootNode = display.getTemplateTree().getRootTreeNode();
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
			activatedView = ViewEnum.READ;
			display.getSelectionModel().setSelected(display.getSelectionModel().getSelectedObject(), false);
			templateSpaceStartPresenter.go(display.getContentPanel());
		}
		else if (pSelectedNode != null)
		{
			selectNode(pSelectedNode, pIsSpace);
			if (currentSelectedNode instanceof TemplateSpaceNodeDTO)
			{
				eventBus.fireEvent(new TemplateSelectSpaceEvent((TemplateSpaceNodeDTO) currentSelectedNode));
			}
			else if (currentSelectedNode instanceof TemplateApplicationNodeDTO)
			{
				eventBus.fireEvent(new TemplateSelectApplicationEvent(
				    (TemplateApplicationNodeDTO) currentSelectedNode));

			}
		}
	}

	private void selectNode(final String pNameOrUri, final boolean iSpace)
	{
		final List<TemplateSpaceNodeDTO> list = display.getSpacesDataProvider().getList();
		for (final TemplateSpaceNodeDTO space : list)
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
				for (final TemplateApplicationNodeDTO app : space.getApplications())
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
}
