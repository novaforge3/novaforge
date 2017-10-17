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


package org.novaforge.studio.ui.editor;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioRepositoryType;
import org.novaforge.studio.ui.editor.project.ProjectEditorPart;
import org.novaforge.studio.ui.editor.task.TaskEditorPart;

/**
 * Novastudio editor page (builds the editor depending on the task repository)
 *
 */
public class StudioTaskEditorPage extends AbstractTaskEditorPage {
	
	public static final String ID_PART_PROJECT = "org.novaforge.studio.ui.editors.part.project";
	
	public static final String ID_PART_TASK = "org.novaforge.studio.ui.editors.part.task";
	
	public StudioTaskEditorPage(TaskEditor editor) {
		super(editor, StudioCorePlugin.CONNECTOR_KIND);
		
		setNeedsPrivateSection(false);
		setNeedsSubmit(false);
		setNeedsSubmitButton(false);
	}

	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {

		Set<TaskEditorPartDescriptor> descriptors = new HashSet<TaskEditorPartDescriptor>();

		String repositoryTypeProperty = getModel().getTaskRepository().getProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE);
		
		if (StringUtils.isNotEmpty(repositoryTypeProperty))
		{
			StudioRepositoryType repositoryType = StudioRepositoryType.valueOf(repositoryTypeProperty);
			switch(repositoryType)
			{
				case PROJECT:
					descriptors.add(new TaskEditorPartDescriptor(ID_PART_PROJECT) {
						@Override
						public AbstractTaskEditorPart createPart() {
							return new ProjectEditorPart(Messages.ProjectEditorPart_Name);
						}
					}.setPath(PATH_HEADER));
					break;
				case TASK:
					descriptors.add(new TaskEditorPartDescriptor(ID_PART_TASK) {
						@Override
						public AbstractTaskEditorPart createPart() {
							return new TaskEditorPart(Messages.TaskEditorPart_Name);
						}
					}.setPath(PATH_HEADER));
					break;
				default:
					break;
			}
		}
		return descriptors;
	}

	@Override
	protected void createParts() {
		super.createParts();
	}
}
