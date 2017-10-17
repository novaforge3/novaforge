/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */


package org.novaforge.studio.ui.editor.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.novaforge.studio.core.project.ProjectAttributeMapper;
import org.novaforge.studio.ui.AbstractEditorPart;
import org.novaforge.studio.ui.StudioUiPlugin;
import org.novaforge.studio.ui.editor.Messages;

/**
 * Project editor (listing project information and list of its spaces and applications.)
 *
 */
public class ProjectEditorPart extends AbstractEditorPart 
{
	private static final String DEFAULT_MANTIS_TOOL_URL = "https://www.myforge.com/mantis";

	private ScrolledForm form;
	
	private final static String MANAGEMENT_APPLICATION = "management";
	private final static String MANTIS_APPLICATION = "Mantis";

	public ProjectEditorPart(String spaceName) 
	{
		setPartName(spaceName);
	}
	
	@Override
	public void createControl(Composite parent, FormToolkit toolkit) 
	{	
		form = toolkit.createScrolledForm(parent);
		TableWrapLayout layout = new TableWrapLayout();
		layout.verticalSpacing = 15;
		form.getBody().setLayout(layout);
		String projectName = getAttributeValue(getTaskData().getRoot(), ProjectAttributeMapper.NAME);
		form.setText(projectName);
		
		String projectDescription = getAttributeValue(getTaskData().getRoot(), ProjectAttributeMapper.DESCRIPTION);
		FormText description = toolkit.createFormText(form.getBody(), false);
		description.setText(projectDescription, false, false);
		TableWrapData td = new TableWrapData();
		td.maxWidth = 600;
		description.setLayoutData(td);
		
		List<TaskAttribute> spaces = filterAttributes(getTaskData().getRoot(), ProjectAttributeMapper.SPACE_PREFIX);
		for (TaskAttribute space : spaces) {
			showSpace(form.getBody(), toolkit, space);
		}
	}
	
	private void showSpace(Composite parent, FormToolkit toolkit, TaskAttribute spaceAttribute) 
	{
		String spaceText = getAttributeValue(spaceAttribute);
		
		Section space = toolkit.createSection(parent, Section.TREE_NODE | Section.CLIENT_INDENT);
		TableWrapData td = new TableWrapData();
		td.maxWidth = 600;
		td.align = TableWrapData.FILL_GRAB;
		space.setLayoutData(td);
		space.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				getTaskEditorPage().reflow();
			}
		});
		space.setText(spaceText);
		
		Composite spaceClient = toolkit.createComposite(space);
		TableWrapLayout clientLayout = new TableWrapLayout();
		spaceClient.setLayout(clientLayout);
		td = new TableWrapData();
		td.align = TableWrapData.FILL_GRAB;
		spaceClient.setLayoutData(td);
		
		List<TaskAttribute> applications = filterAttributes(spaceAttribute, ProjectAttributeMapper.APPLICATION_PREFIX);
		
		for (TaskAttribute application : applications) {
			showApplication(spaceClient, toolkit, application);
		}
		space.setClient(spaceClient);
	}
	
	private void showApplication(Composite parent, FormToolkit toolkit, final TaskAttribute applicationAttribute) 
	{
		Section application = toolkit.createSection(parent, Section.TREE_NODE | Section.CLIENT_INDENT);
		TableWrapData td = new TableWrapData();
		td.align = TableWrapData.FILL_GRAB;
		application.setLayoutData(td);
		application.setText(applicationAttribute.getValue());
		application.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				getTaskEditorPage().reflow();
			}
		});
		
		String applicationCategory = getAttributeValue(applicationAttribute, ProjectAttributeMapper.APPLICATION_CATEGORY);
		String applicationType = getAttributeValue(applicationAttribute, ProjectAttributeMapper.APPLICATION_TYPE);
		
		Composite applicationClient = toolkit.createComposite(application);
		TableWrapLayout applicationClientLayout = new TableWrapLayout();
		applicationClientLayout.numColumns = 2;
		applicationClient.setLayout(applicationClientLayout);
		
		String applicationAccessInfo = getAttributeValue(applicationAttribute, ProjectAttributeMapper.APPLICATION_ACCESS_INFO);
		FormText accessInfoLabel = toolkit.createFormText(applicationClient, false);
		accessInfoLabel.setText(applicationAccessInfo, false, false);
		TableWrapData accessInfoData = new TableWrapData();
		accessInfoData.maxWidth = 400;
		
		if (!MANAGEMENT_APPLICATION.equalsIgnoreCase(applicationCategory) && 
			!MANTIS_APPLICATION.equalsIgnoreCase(applicationType))
		{
			accessInfoData.colspan = 2;
		}
		
		accessInfoLabel.setLayoutData(accessInfoData);
		
		if (MANAGEMENT_APPLICATION.equalsIgnoreCase(applicationCategory))
		{
			addManagementButtons(toolkit, applicationClient, applicationAttribute);
		}
		
		if (isMantisConnectorInstalled() && MANTIS_APPLICATION.equalsIgnoreCase(applicationType))
		{
			addMantisButton(toolkit, applicationClient, applicationAttribute);
		}

		application.setClient(applicationClient);
	}
	
	private void addManagementButtons(FormToolkit toolkit, Composite parent, final TaskAttribute applicationAttribute)
	{
		Composite buttonsComposite = toolkit.createComposite(parent);
		TableWrapData compositeData = new TableWrapData();
		compositeData.align = TableWrapData.RIGHT;
		compositeData.valign = TableWrapData.TOP;
		compositeData.maxWidth = 120;
		buttonsComposite.setLayoutData(compositeData);
		RowLayout buttonsLayout = new RowLayout();
		buttonsLayout.type = SWT.VERTICAL;
		buttonsLayout.pack = false;
		buttonsComposite.setLayout(buttonsLayout);
		
		Button projectTasksButton = toolkit.createButton(buttonsComposite, Messages.ProjectEditorPart_ProjectTasksButton, SWT.PUSH);
		String projectName = getAttributeValue(getModel().getTaskData().getRoot().getAttribute(ProjectAttributeMapper.NAME));
		ProjectTasksButtonListener projectListener = new ProjectTasksButtonListener(applicationAttribute, projectName);
		projectTasksButton.addSelectionListener(projectListener);
		
		Button iterationTasksButton = toolkit.createButton(buttonsComposite, Messages.ProjectEditorPart_IterationTasksButton, SWT.PUSH);
		IterationTasksButtonListener iterationListener = new IterationTasksButtonListener(applicationAttribute, form.getShell(), form.getStyle());
		iterationTasksButton.addSelectionListener(iterationListener);
	}
	
	private void addMantisButton(FormToolkit toolkit, Composite parent, final TaskAttribute applicationAttribute)
	{
		Button mantisButton = toolkit.createButton(parent, Messages.ProjectEditorPart_MantisButton, SWT.PUSH);
		TableWrapData mantisData = new TableWrapData();
		mantisData.align = TableWrapData.RIGHT;
		mantisData.valign = TableWrapData.TOP;
		mantisData.maxWidth = 120;
		mantisButton.setLayoutData(mantisData);
		String applicationName = applicationAttribute.getValue();
		String applicationUrl = getAttributeValue(applicationAttribute, ProjectAttributeMapper.APPLICATION_TOOL_URL);
		if (applicationUrl==null || applicationUrl.isEmpty()){
			applicationUrl = DEFAULT_MANTIS_TOOL_URL;
		}
		MantisButtonListener mantisListener = new MantisButtonListener(applicationName, applicationUrl);
		mantisButton.addSelectionListener(mantisListener);
	}
	
	private List<TaskAttribute> filterAttributes(TaskAttribute parent, String filter) 
	{
		List<TaskAttribute> result = new ArrayList<TaskAttribute>();
		
		Map<String, TaskAttribute> attributes = parent.getAttributes();
		for (Map.Entry<String, TaskAttribute> entry : attributes.entrySet()) {
			
			if (entry.getKey().startsWith(filter)) {
				result.add(entry.getValue());
			}
		}
		return result;
	}
	
	private boolean isMantisConnectorInstalled()
	{
		AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(StudioUiPlugin.MANTIS_CONNECTOR_KIND);
		return connector != null;
	}
}
