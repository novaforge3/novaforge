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

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Organization;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "PROJECT_ELEMENT")
@NamedQueries({
    @NamedQuery(
        name = "ProjectEntity.findByStatus",
        query = "SELECT p FROM ProjectEntity p WHERE p.status IN :status AND p.realmType = org.novaforge.forge.core.organization.model.enumerations.RealmType.USER"),
    @NamedQuery(name = "ProjectEntity.findByStatusWithSystem",
        query = "SELECT p FROM ProjectEntity p WHERE p.status IN :status"),
    @NamedQuery(name = "ProjectEntity.findByProjectId",
        query = "SELECT p FROM ProjectEntity p WHERE p.elementId = :projectId") })
@FetchGroups({
    @FetchGroup(name = "project_all", attributes = { @FetchAttribute(name = "organization"),
        @FetchAttribute(name = "image") }),
    @FetchGroup(name = "project_organization", attributes = { @FetchAttribute(name = "organization") }),
    @FetchGroup(name = "project_image", attributes = { @FetchAttribute(name = "image") }) })
public class ProjectEntity extends ProjectElementEntity implements Project
{

  /**
   * Serial version id
   */
  private static final long       serialVersionUID = -4945615531290197742L;

  @ManyToOne(targetEntity = OrganizationEntity.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE },
      fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_id", nullable = true)
  private Organization            organization;

  @Column(name = "licence_type", nullable = true)
  private String                  licenceType;

  @Column(name = "private_visibility", nullable = true)
  private boolean                 privateVisibility;

  @Column(name = "status", nullable = true)
  @Enumerated
  private ProjectStatus           status;

  @OneToMany(mappedBy = "element", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = ProjectRoleEntity.class, orphanRemoval = true)
  private List<ProjectRole>       roles            = new ArrayList<ProjectRole>();

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = GroupEntity.class, orphanRemoval = true)
  private List<Group>             groups           = new ArrayList<Group>();

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = CompositionEntity.class, orphanRemoval = true)
  private List<Composition>       compositions     = new ArrayList<Composition>();

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = MembershipRequestEntity.class, orphanRemoval = true)
  private List<MembershipRequest> requests         = new ArrayList<MembershipRequest>();

  @Column(name = "author", nullable = true)
  private String                  author;

  @Column(name = "realm_type", nullable = true)
  @Enumerated
  private RealmType               realmType;

  @OneToOne(targetEntity = BinaryFileEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private BinaryFile              image;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectId()
  {
    return super.getElementId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectId(final String pProjectId)
  {
    super.setElementId(pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Organization getOrganization()
  {
    return organization;
  }

  @Override
  public void setOrganization(final Organization pOrganization)
  {
    organization = pOrganization;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Size(min = 1)
  public String getLicenceType()
  {
    return licenceType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLicenceType(final String licenceType)
  {
    this.licenceType = licenceType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectStatus getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final ProjectStatus status)
  {
    this.status = status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthor()
  {
    return author;
  }

  @Override
  public void setAuthor(final String pAuthor)
  {
    author = pAuthor;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RealmType getRealmType()
  {
    return realmType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRealmType(final RealmType pType)
  {
    realmType = pType;

    // set the project status in function of the realm type
    if (pType != null)
    {
      if (pType.equals(RealmType.SYSTEM))
      {
        setStatus(ProjectStatus.VALIDATED);
      }
      else
      {
        setStatus(ProjectStatus.TO_BE_VALIDATED);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPrivateVisibility()
  {
    return privateVisibility;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPrivateVisibility(final boolean privateVisibility)
  {
    this.privateVisibility = privateVisibility;
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
  public void setImage(final BinaryFile pImage)
  {
    image = pImage;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Group> getGroups()
  {
    return Collections.unmodifiableList(groups);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGroups(final List<Group> pGroups)
  {
    groups = pGroups;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addGroup(final Group pGroup)
  {
    final GroupEntity groupEntity = (GroupEntity) pGroup;
    groupEntity.setProject(this);
    groups.add(pGroup);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeGroup(final Group pGroup)
  {
    final GroupEntity groupEntity = (GroupEntity) pGroup;
    groupEntity.setProject(null);
    groups.remove(pGroup);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectRole> getRoles()
  {
    return Collections.unmodifiableList(roles);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRoles(final List<ProjectRole> roles)
  {
    this.roles = roles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRole(final ProjectRole role)
  {
    final ProjectRoleEntity roleEntity = (ProjectRoleEntity) role;
    roleEntity.setElement(this);
    if (roles == null)
    {
      roles = new ArrayList<ProjectRole>();
    }
    roles.add(role);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeRole(final ProjectRole role)
  {
    roles.remove(role);
    final ProjectRoleEntity roleEntity = (ProjectRoleEntity) role;
    roleEntity.setElement(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addComposition(final Composition pComposition)
  {
    final CompositionEntity compositionEntity = (CompositionEntity) pComposition;
    compositionEntity.setProject(this);
    compositions.add(pComposition);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Composition> getCompositions()
  {
    return Collections.unmodifiableList(compositions);
  }

  @Override
  public void setCompositions(final List<Composition> pCompositions)
  {
    compositions = pCompositions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeComposition(final Composition pComposition)
  {
    final CompositionEntity compositionEntity = (CompositionEntity) pComposition;
    compositionEntity.setProject(null);
    compositions.remove(pComposition);
  }

  @Override
  public void removeRequest(final MembershipRequest pMembershipRequest)
  {
    requests.remove(pMembershipRequest);
  }

  @Override
  public List<MembershipRequest> getRequests()
  {
    return Collections.unmodifiableList(requests);
  }

  @Override
  public void setRequests(final List<MembershipRequest> pRequests)
  {
    requests = pRequests;
  }

  @Override
  public void addRequest(final MembershipRequest pMembershipRequest)
  {
    requests.add(pMembershipRequest);
  }

  @Override
  public String toString()
  {
    return "ProjectEntity [author=" + author + ", licenceType=" + licenceType + ", organization=" + organization
               + ", privateVisibility=" + privateVisibility + ", realmType=" + realmType + ", status=" + status + "]";
  }

}
