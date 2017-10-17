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
package org.novaforge.forge.ui.forge.reference.client.view.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.client.view.project.resources.ProjectResources;

/**
 * @author lamirang
 */
public class ProjectDetailViewImpl extends Composite implements ProjectDetailView
{
	private static ProjectDetailViewImplUiBinder uiBinder        = GWT
	                                                                 .create(ProjectDetailViewImplUiBinder.class);
	private static ReferenceResources            ressources      = GWT.create(ReferenceResources.class);
	private static ProjectResources              projectRes      = GWT.create(ProjectResources.class);
	private final ProjectMessage projectMessages = (ProjectMessage) GWT.create(ProjectMessage.class);
	@UiField
	Button buttonUpdateProject;
	@UiField
	Button buttonAddSpace;
	@UiField
	Grid   projectGrid;
	@UiField
	Label  projectDetailTitle;
	@UiField
	Label  projectNameLabel;
	@UiField
	Label  projectName;
	@UiField
	Label  projectIdentifiantLabel;
	@UiField
	Label  projectIdentifiant;
	@UiField
	Label  projectDescLabel;
	@UiField
	Label  projectDesc;
	public ProjectDetailViewImpl()
	{
		ressources.style().ensureInjected();
		projectRes.style().ensureInjected();

		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));

		// Set buttons' text
		buttonAddSpace.setText(projectMessages.buttonAddSpace());
		buttonUpdateProject.setText(projectMessages.buttonUpdate());

		// Initialization of group detail form
		projectDetailTitle.setText(projectMessages.infoTitle());
		projectNameLabel.setText(projectMessages.projectName());
		projectIdentifiantLabel.setText(projectMessages.projectIdentifiant());
		projectDescLabel.setText(projectMessages.projectDescription());
		// Initialization row style
		for (int row = 0; row < projectGrid.getRowCount(); row++)
		{
			if ((row % 2) == 0)
			{
				projectGrid.getRowFormatter().addStyleName(row, ressources.style().gridRowPair());
			}
			projectGrid.getCellFormatter().addStyleName(row, 0, ressources.style().labelCell());
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public HasClickHandlers getAddSpace()
	{
		return buttonAddSpace;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public HasClickHandlers getUpdateButton()
	{
		return buttonUpdateProject;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public HasText getName()
	{
		return projectName;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public HasText getIdentifiant()
	{
		return projectIdentifiant;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public HasText getDescription()
	{
		return projectDesc;
	}

	interface ProjectDetailViewImplUiBinder extends UiBinder<Widget, ProjectDetailViewImpl>
	{
	}

}
