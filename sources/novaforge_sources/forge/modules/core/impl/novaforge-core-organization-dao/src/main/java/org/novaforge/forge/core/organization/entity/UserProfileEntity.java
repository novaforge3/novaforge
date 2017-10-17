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
package org.novaforge.forge.core.organization.entity;

import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Jeremy Casery
 */
@Entity
@Table(name = "USER_PROFILE")
@NamedQueries({
    @NamedQuery(name = "UserProfileEntity.findAll", query = "SELECT p FROM UserProfileEntity p"),
    @NamedQuery(name = "UserProfileEntity.findProfileByLogin",
        query = "SELECT p FROM UserProfileEntity p WHERE p.user.login = :login"),
    @NamedQuery(name = "UserProfileEntity.findProfileByEmail",
        query = "SELECT p FROM UserProfileEntity p WHERE p.user.email = :email") })
public class UserProfileEntity implements UserProfile
{

  /**
   * 
   */
  private static final long  serialVersionUID = 4839294569291088486L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long               id;

  @OneToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User               user;

  @OneToOne(fetch = FetchType.EAGER, targetEntity = AttributeEntity.class, cascade = CascadeType.ALL,
      orphanRemoval = true)
  @JoinColumn(name = "projects_public", referencedColumnName = "id")
  private Attribute          userProfileProjects;

  @OneToOne(fetch = FetchType.EAGER, targetEntity = UserProfileWorkEntity.class, cascade = CascadeType.ALL,
      orphanRemoval = true)
  @JoinColumn(name = "profile_work", referencedColumnName = "id")
  private UserProfileWork    userProfileWork;

  @OneToOne(fetch = FetchType.EAGER, targetEntity = UserProfileContactEntity.class,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_contact", referencedColumnName = "id")
  private UserProfileContact userProfileContact;

  @OneToOne(targetEntity = BinaryFileEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL,
      orphanRemoval = true)
  private BinaryFile         image;

  public UserProfileEntity()
  {
    super();
  }

  public UserProfileEntity(final User user)
  {
    super();
    this.user = user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser()
  {
    return user;
  }

  /**
   * {@inheritDoc}
   */
  public void setUser(final User user)
  {
    this.user = user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute getUserProfileProjects()
  {
    return userProfileProjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserProfileProjects(final Attribute pUserProfileProjects)
  {
    userProfileProjects = pUserProfileProjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileWork getUserProfileWork()
  {
    return userProfileWork;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserProfileWork(final UserProfileWork userProfileWork)
  {
    this.userProfileWork = userProfileWork;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileContact getUserProfileContact()
  {
    return userProfileContact;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserProfileContact(final UserProfileContact userProfileContact)
  {
    this.userProfileContact = userProfileContact;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BinaryFile getImage()
  {
    return image;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setImage(final BinaryFile image)
  {
    this.image = image;
  }

}
