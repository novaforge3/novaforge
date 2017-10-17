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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;

/**
 * @author Guillaume Lamirand
 */
public class ProjectsInstanceContainer extends IndexedContainer
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
  public ProjectsInstanceContainer()
  {
    super();
    addContainerProperty(ProjectInstanceItemProperty.ICON.getPropertyId(), BinaryFile.class, null);
    addContainerProperty(ProjectInstanceItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(ProjectInstanceItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(ProjectInstanceItemProperty.AUTHOR.getPropertyId(), UserProfile.class, null);
    addContainerProperty(ProjectInstanceItemProperty.AUTHOR_LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(ProjectInstanceItemProperty.APPLICATION.getPropertyId(), ProjectApplication.class,
        null);
    addContainerProperty(ProjectInstanceItemProperty.APPLICATION_NAME.getPropertyId(), String.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  public void clean()
  {
    removeAllContainerFilters();
    removeAllItems();
  }

  /**
   * Add {@link Project} into container
   * 
   * @param pProject
   *          {@link Project} to add
   * @param pProjectApplication
   * @param pInstanceId
   *          instance uuid associated
   * @return true if instnace was added
   * @throws UserServiceException
   */
  public boolean addProjectInstance(final Project pProject, final ProjectApplication pProjectApplication)
      throws UserServiceException
  {
    boolean added = false;
    if (ApplicationStatus.ACTIVE.equals(pProjectApplication.getStatus()))
    {
      final String itemId = pProjectApplication.getPluginInstanceUUID().toString();

      addItem(itemId);
      getContainerProperty(itemId, ProjectInstanceItemProperty.ICON.getPropertyId()).setValue(
          pProject.getImage());
      getContainerProperty(itemId, ProjectInstanceItemProperty.ID.getPropertyId()).setValue(
          pProject.getElementId());
      getContainerProperty(itemId, ProjectInstanceItemProperty.NAME.getPropertyId()).setValue(
          pProject.getName());
      final UserProfile userProfile = PluginsModule.getUserPresenter().getUserProfile(pProject.getAuthor());
      getContainerProperty(itemId, ProjectInstanceItemProperty.AUTHOR.getPropertyId()).setValue(userProfile);
      getContainerProperty(itemId, ProjectInstanceItemProperty.AUTHOR_LOGIN.getPropertyId()).setValue(
          userProfile.getUser().getLogin());
      getContainerProperty(itemId, ProjectInstanceItemProperty.APPLICATION.getPropertyId()).setValue(
          pProjectApplication);
      getContainerProperty(itemId, ProjectInstanceItemProperty.APPLICATION_NAME.getPropertyId()).setValue(
          pProjectApplication.getName());
      added = true;
    }
    return added;
  }

}
