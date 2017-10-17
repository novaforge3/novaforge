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
package org.novaforge.forge.ui.projects.internal.client.admin.containers;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;

import java.util.Date;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class ProjectsContainer extends IndexedContainer
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 7245265409387407802L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public ProjectsContainer()
  {
    super();
    addContainerProperty(ProjectItemProperty.ICON.getPropertyId(), BinaryFile.class, null);
    addContainerProperty(ProjectItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.AUTHOR.getPropertyId(), UserProfile.class, null);
    addContainerProperty(ProjectItemProperty.AUTHOR_LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.DATE.getPropertyId(), Date.class, null);
    addContainerProperty(ProjectItemProperty.STATUS.getPropertyId(), ProjectStatus.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add {@link Project} into container
   * 
   * @param pProjects
   *          {@link Project} to add
   * @throws UserServiceException
   */
  public void setProjects(final List<Project> pProjects) throws UserServiceException
  {
    removeAllContainerFilters();
    removeAllItems();
    for (final Project project : pProjects)
    {
      final String itemId = project.getElementId();
      if (!ProjectServices.getProjectPresenter().isForgeProject(itemId))
      {
        addItem(itemId);
        getContainerProperty(itemId, ProjectItemProperty.ICON.getPropertyId()).setValue(project.getImage());
        getContainerProperty(itemId, ProjectItemProperty.ID.getPropertyId()).setValue(project.getElementId());
        getContainerProperty(itemId, ProjectItemProperty.NAME.getPropertyId()).setValue(project.getName());
        getContainerProperty(itemId, ProjectItemProperty.DESCRIPTION.getPropertyId()).setValue(
            project.getDescription());
        final UserProfile userProfile = ProjectServices.getUserPresenter()
            .getUserProfile(project.getAuthor());
        getContainerProperty(itemId, ProjectItemProperty.AUTHOR.getPropertyId()).setValue(userProfile);
        getContainerProperty(itemId, ProjectItemProperty.AUTHOR_LOGIN.getPropertyId()).setValue(
            userProfile.getUser().getLogin());
        getContainerProperty(itemId, ProjectItemProperty.DATE.getPropertyId()).setValue(project.getCreated());
        getContainerProperty(itemId, ProjectItemProperty.STATUS.getPropertyId())
            .setValue(project.getStatus());
      }
    }

    sort(new Object[] { ProjectItemProperty.NAME.getPropertyId() }, new boolean[] { true });
  }

}
