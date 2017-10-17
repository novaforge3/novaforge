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
package org.novaforge.forge.ui.portal.internal.header.client.components;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.util.List;

/**
 * This component describes a specific {@link IndexedContainer} used to build projects combobox.
 * 
 * @author Guillaume Lamirand
 */
public class ProjectsContainer extends IndexedContainer
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 4344536170873384315L;

  /**
   * Default constructor. It will initialize project item property
   * 
   * @see ProjectItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public ProjectsContainer()
  {
    super();
    addContainerProperty(ProjectItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.ICON.getPropertyId(), Resource.class, null);
  }

  /**
   * Add projects into container
   * 
   * @param pProject
   *          the project to add
   */
  public void setProjects(final List<Project> pProjects)
  {
    removeAllItems();
    for (final Project project : pProjects)
    {
      final String elementId = project.getElementId();
      addItem(elementId);
      getContainerProperty(elementId, ProjectItemProperty.ID.getPropertyId()).setValue(elementId);
      getContainerProperty(elementId, ProjectItemProperty.NAME.getPropertyId()).setValue(project.getName());
      getContainerProperty(elementId, ProjectItemProperty.DESCRIPTION.getPropertyId()).setValue(
          project.getDescription());

      if ((project.getImage() != null) && (project.getImage().getFile() != null))
      {
        final StreamResource buildImageResource = ResourceUtils.buildImageResource(project.getImage()
            .getFile(), project.getName());
        buildImageResource.setMIMEType(project.getImage().getMimeType());
        getContainerProperty(elementId, ProjectItemProperty.ICON.getPropertyId())
            .setValue(buildImageResource);
      }

    }
    sort(new Object[] { ProjectItemProperty.NAME.getPropertyId() }, new boolean[] { true });
  }
}
