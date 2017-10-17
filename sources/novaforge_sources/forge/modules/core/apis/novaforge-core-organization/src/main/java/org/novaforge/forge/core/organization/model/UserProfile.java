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

import java.io.Serializable;

/**
 * @author Jeremy Casery
 */
public interface UserProfile extends Serializable
{

  /**
   * Get the user of the UserProfile
   * 
   * @return the user
   */
  User getUser();

  /**
   * Get the userprofileprojects for this userprofile
   * 
   * @return {@link Attribute} the userProfileProjects
   */
  Attribute getUserProfileProjects();

  /**
   * Set the {@link Attribute} userProfileProjects for this UserProfile
   * 
   * @param pUserProfileProjects
   *          {@link Attribute} the userProfileProjects to set
   */
  void setUserProfileProjects(Attribute pUserProfileProjects);

  /**
   * Get the userprofilework for this userprofile
   * 
   * @return {@link UserProfileWork} the userProfileWork
   */
  UserProfileWork getUserProfileWork();

  /**
   * Set the {@link UserProfileWork} of this UserProfile
   * 
   * @param userProfileWork
   *          the userProfileWork to set
   */
  void setUserProfileWork(UserProfileWork userProfileWork);

  /**
   * Get the {@link UserProfileContact} of this UserProfile
   * 
   * @return {@link UserProfileContact} the userProfileContact
   */
  UserProfileContact getUserProfileContact();

  /**
   * Set the {@link UserProfileContact} of this UserProfile
   * 
   * @param userProfileContact
   *          the userProfileContact to set
   */
  void setUserProfileContact(UserProfileContact userProfileContact);

  /**
   * Get the UserProfile image
   * 
   * @return {@link BinaryFile} the userprofile image
   */
  BinaryFile getImage();

  /**
   * Set the User Profile image
   * 
   * @param image
   *          the image to set
   */
  void setImage(BinaryFile image);

}