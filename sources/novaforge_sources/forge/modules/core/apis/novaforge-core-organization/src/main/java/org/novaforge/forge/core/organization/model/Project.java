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


package org.novaforge.forge.core.organization.model;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import java.util.Date;
import java.util.List;

/**
 * This is the behavior of a project into the forge
 * 
 * @author sbenoist
 */
public interface Project extends ProjectElement
{

  /**
   * This method returns the projectId of the project
   * 
   * @return String
   */
  @Historizable(label = "projectId")
  String getProjectId();

  /**
   * This method allows to set the projectId of the project
   * 
   * @param projectId
   */
  void setProjectId(String projectId);

  /**
   * This method returns the organization of the project
   * 
   * @return {@link Organization}
   */
  Organization getOrganization();

  /**
   * This method allows to set the organization of the project
   * 
   * @param pOrganization
   */
  void setOrganization(Organization pOrganization);

  /**
   * This method returns the licence of the project
   * 
   * @return String
   */
  String getLicenceType();

  /**
   * This method allows to set the licence of the project
   * 
   * @param licenceType
   */
  void setLicenceType(String licenceType);

  /**
   * This method returns the status of the project
   * 
   * @return ProjectStatus
   */
  ProjectStatus getStatus();

  /**
   * This method set the status of the project
   * 
   * @param pNewStatus
   *          the status to set
   */
  void setStatus(ProjectStatus pNewStatus);

  /**
   * This method returns the username of the author of the project
   * 
   * @return author
   */
  String getAuthor();

  /**
   * This method allows to set the author of the project
   * 
   * @param author
   */
  void setAuthor(String author);

  /**
   * This method returns the realm type of the project
   * 
   * @return realm type
   */
  RealmType getRealmType();

  /**
   * Set project type
   * 
   * @param pType
   *          the type to set
   */
  void setRealmType(final RealmType pType);

  /**
   * This method returns the date of creation of the project
   * 
   * @return Date
   */
  @Override
  Date getCreated();

  /**
   * This method returns the date of the last modification of the project
   * 
   * @return Date
   */
  @Override
  Date getLastModified();

  /**
   * This method returns if project is private
   *
   * @return true if private otherwise false
   */
  boolean isPrivateVisibility();

  /**
   * This method allows to set if project is private
   *
   * @param pPrivateVisibility
   */
  void setPrivateVisibility(boolean pPrivateVisibility);

  /**
   * Get the image content
   *
   * @return the image content
   */
  BinaryFile getImage();

  /**
   * Set the image content.
   *
   * @param pImage
   *          image to set
   */
  void setImage(BinaryFile pImage);

  /**
   * Returns {@link List} of {@link Group}
   *
   * @return {@link List} of {@link Group}
   */
  List<Group> getGroups();

  /**
   * Sets groups list
   *
   * @param pGroup
   *          the list to set
   */
  void setGroups(final List<Group> pGroup);

  /**
   * Add a group to project element
   * 
   * @param pGroup
   *          the group to add
   */
  void addGroup(final Group pGroup);

  /**
   * Remove a group from project element
   * 
   * @param pGroup
   *          the group to remove
   */
  void removeGroup(final Group pGroup);

  /**
   * Returns {@link List} of {@link ProjectRole}
   *
   * @return {@link List} of {@link ProjectRole}
   */
  List<ProjectRole> getRoles();

  /**
   * Sets roles list
   *
   * @param pRoles
   *          the list to set
   */
  void setRoles(final List<ProjectRole> pRoles);

  /**
   * Add a role to project element
   * 
   * @param pRole
   *          the role to add
   */
  void addRole(final ProjectRole pRole);

  /**
   * Remove a role from project element
   * 
   * @param pRole
   *          the role to remove
   */
  void removeRole(final ProjectRole pRole);

  /**
   * Add a composition from a project
   * 
   * @param pComposition
   */
  void addComposition(Composition pComposition);

  /**
   * Returns {@link List} of {@link Composition}
   * 
   * @return {@link List} of {@link Composition}
   */
  List<Composition> getCompositions();

  /**
   * Sets compositions list
   *
   * @param pCompositions
   *          the list to set
   */
  void setCompositions(List<Composition> pCompositions);

  /**
   * Remove a composition from a project
   *
   * @param pComposition
   *          the composition to remove
   */
  void removeComposition(Composition pComposition);

  /**
   * Remove a request from project element
   * 
   * @param pMembershipRequest
   *          the request to remove
   */
  void removeRequest(MembershipRequest pMembershipRequest);

  /**
   * Returns {@link List} of {@link MembershipRequest}
   *
   * @return {@link List} of {@link MembershipRequest}
   */
  List<MembershipRequest> getRequests();

  /**
   * Sets requests list
   *
   * @param pRequests
   *          the list to set
   */
  void setRequests(List<MembershipRequest> pRequests);

  /**
   * Add a request from project element
   * 
   * @param pMembershipRequest
   *          the request to add
   */
  void addRequest(MembershipRequest pMembershipRequest);

}
