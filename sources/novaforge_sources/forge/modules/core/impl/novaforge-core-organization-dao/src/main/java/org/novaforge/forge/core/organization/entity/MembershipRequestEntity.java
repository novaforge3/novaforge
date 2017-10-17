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

import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MembershipRequestStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author lamirang
 */
@Entity
@Table(name = "MEMBERSHIP_REQUEST")
@NamedQueries({
    @NamedQuery(name = "MembershipRequestEntity.findAllRequestByProject",
        query = "SELECT p FROM MembershipRequestEntity p WHERE p.project.elementId = :projectId"),
    @NamedQuery(name = "MembershipRequestEntity.findAllRequestByUser",
        query = "SELECT p FROM MembershipRequestEntity p WHERE p.user.login = :login"),
    @NamedQuery(name = "MembershipRequestEntity.findAllRequestByUserAndStatus",
        query = "SELECT p FROM MembershipRequestEntity p WHERE p.user.login = :login AND p.status = :status"),
    @NamedQuery(
        name = "MembershipRequestEntity.findCurrentRequestByProjectAndUser",
        query = "SELECT p FROM MembershipRequestEntity p WHERE p.project.elementId = :projectId "
            + "AND  p.user.login = :login "
            + "AND p.status = org.novaforge.forge.core.organization.model.enumerations.MembershipRequestStatus.IN_PROGRESS"),
    @NamedQuery(
        name = "MembershipRequestEntity.findAllRequestByProjectAndStatus",
        query = "SELECT p FROM MembershipRequestEntity p WHERE p.project.elementId = :projectId AND  p.status = :status") })
public class MembershipRequestEntity implements MembershipRequest
{

  /**
    * 
    */
  private static final long       serialVersionUID = 4767199091658492698L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                    id;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
  @JoinColumn(name = "project_id", nullable = true)
  private Project                 project;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
  @JoinColumn(name = "user_id", nullable = false)
  private User                    user;

  @Column(name = "created", nullable = false, insertable = true, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date                    created;

  @Column(name = "message", nullable = true)
  private String                  message;

  @Column(name = "status", nullable = false)
  private MembershipRequestStatus status;

  /**
   * This will be called before a persist and flush event
   */
  @PrePersist
  public void onPersist()
  {
    created = new Date();
  }

  public Long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project getProject()
  {
    return project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProject(final Project project)
  {
    this.project = project;
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
  @Override
  public void setUser(final User pUser)
  {
    user = pUser;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage()
  {
    return message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMessage(final String message)
  {
    this.message = message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MembershipRequestStatus getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final MembershipRequestStatus pStatus)
  {
    status = pStatus;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getCreatedDate()
  {
    return created;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((created == null) ? 0 : created.hashCode());
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
    result = (prime * result) + ((message == null) ? 0 : message.hashCode());
    result = (prime * result) + ((project == null) ? 0 : project.hashCode());
    result = (prime * result) + ((status == null) ? 0 : status.hashCode());
    result = (prime * result) + ((user == null) ? 0 : user.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof MembershipRequestEntity))
    {
      return false;
    }
    final MembershipRequestEntity other = (MembershipRequestEntity) obj;
    if (created == null)
    {
      if (other.created != null)
      {
        return false;
      }
    }
    else if (!created.equals(other.created))
    {
      return false;
    }
    if (id == null)
    {
      if (other.id != null)
      {
        return false;
      }
    }
    else if (!id.equals(other.id))
    {
      return false;
    }
    if (message == null)
    {
      if (other.message != null)
      {
        return false;
      }
    }
    else if (!message.equals(other.message))
    {
      return false;
    }
    if (project == null)
    {
      if (other.project != null)
      {
        return false;
      }
    }
    else if (!project.equals(other.project))
    {
      return false;
    }
    if (status != other.status)
    {
      return false;
    }
    if (user == null)
    {
      if (other.user != null)
      {
        return false;
      }
    }
    else if (!user.equals(other.user))
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "MembershipRequestEntity [id=" + id + ", project=" + project + ", user=" + user + ", created=" + created
               + ", message=" + message + ", status=" + status + "]";
  }

}
