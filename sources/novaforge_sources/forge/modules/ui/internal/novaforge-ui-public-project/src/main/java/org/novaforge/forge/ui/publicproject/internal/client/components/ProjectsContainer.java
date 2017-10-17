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
package org.novaforge.forge.ui.publicproject.internal.client.components;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.publicproject.internal.module.PublicProjectModule;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This component describes a specific {@link IndexedContainer} used to build projects combobox.
 * 
 * @author Guillaume Lamirand
 */
public class ProjectsContainer extends IndexedContainer
{

  /**
	 * 
	 */
  private static final long serialVersionUID = -981255362730667240L;

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
    addContainerProperty(ProjectItemProperty.LICENCE.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.AUTHOR.getPropertyId(), UserProfile.class, null);
    addContainerProperty(ProjectItemProperty.ORGANIZATION.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.CREATEDDATE.getPropertyId(), Date.class, null);
    addContainerProperty(ProjectItemProperty.ICON.getPropertyId(), Resource.class, null);
    addContainerProperty(ProjectItemProperty.REQUESTINPROGRESS.getPropertyId(), Boolean.class, false);

    setItemSorter(new DefaultItemSorter(new Comparator<Object>()
    {

      @Override
      public int compare(final Object o1, final Object o2)
      {
        if ((o1 instanceof String) && (o2 instanceof String))
        {
          return ((String) o1).toLowerCase().compareTo(((String) o2).toLowerCase());
        }
        return 0;
      }
    }));
  }

  /**
   * Add projects into container
   * 
   * @param pProjects
   *          the public projects to add
   * @param pProjectsRequested
   *          the request for the current user
   * @param pLocale
   *          the user locale
   * @throws UserServiceException
   */
  public void setProjects(final List<Project> pProjects, final List<Project> pProjectsRequested,
      final Locale pLocale) throws UserServiceException
  {
    removeAllItems();
    for (final Project project : pProjects)
    {
      addItem(project.getElementId());
      getContainerProperty(project.getElementId(), ProjectItemProperty.ID.getPropertyId()).setValue(
          project.getElementId());
      getContainerProperty(project.getElementId(), ProjectItemProperty.NAME.getPropertyId()).setValue(
          project.getName());
      getContainerProperty(project.getElementId(), ProjectItemProperty.DESCRIPTION.getPropertyId()).setValue(
          project.getDescription());
      getContainerProperty(project.getElementId(), ProjectItemProperty.LICENCE.getPropertyId()).setValue(
          project.getLicenceType());

      getContainerProperty(project.getElementId(), ProjectItemProperty.AUTHOR.getPropertyId()).setValue(
          PublicProjectModule.getUserPresenter().getUserProfile(project.getAuthor()));

      if (project.getOrganization() == null)
      {
        getContainerProperty(project.getElementId(), ProjectItemProperty.ORGANIZATION.getPropertyId())
            .setValue(
                PublicProjectModule.getPortalMessages().getMessage(pLocale,
                    Messages.PUBLIC_PROJECT_FIELD_ORGANIZATION_EMPTY));
      }
      else
      {
        getContainerProperty(project.getElementId(), ProjectItemProperty.ORGANIZATION.getPropertyId())
            .setValue(project.getOrganization().getName());
      }
      getContainerProperty(project.getElementId(), ProjectItemProperty.CREATEDDATE.getPropertyId()).setValue(
          project.getCreated());
      if (pProjectsRequested.contains(project))
      {
        getContainerProperty(project.getElementId(), ProjectItemProperty.REQUESTINPROGRESS.getPropertyId())
            .setValue(true);
      }
      if ((project.getImage() != null) && (project.getImage().getFile() != null))
      {
        final StreamResource imageResource = ResourceUtils.buildImageResource(project.getImage().getFile(),
            project.getName());
        imageResource.setMIMEType(project.getImage().getMimeType());
        getContainerProperty(project.getElementId(), ProjectItemProperty.ICON.getPropertyId()).setValue(
            imageResource);
      }

    }
    sort(new Object[] { ProjectItemProperty.NAME.getPropertyId() }, new boolean[] { true });
  }
}
