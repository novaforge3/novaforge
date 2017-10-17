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

import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "PROJECT_ELEMENT")
@NamedQuery(name = "TemplateEntity.findByStatus",
    query = "SELECT p FROM TemplateEntity p WHERE p.status = :status")
public class TemplateEntity extends ProjectElementEntity implements Serializable, Template
{
  private static final long     serialVersionUID = -4945615531290197742L;

  @Column(name = "status", nullable = true)
  @Enumerated
  private TemplateProjectStatus status;

  @OneToMany(mappedBy = "element", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = RoleEntity.class, orphanRemoval = true)
  private List<Role>            roles            = new ArrayList<Role>();

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTemplateId()
  {
    return getElementId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTemplateId(final String pTemplateId)
  {
    super.setElementId(pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateProjectStatus getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final TemplateProjectStatus status)
  {
    this.status = status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Role> getRoles()
  {
    return Collections.unmodifiableList(roles);
  }

  /**
   * @param pRoles
   *          the role list to set
   */
  public void setRoles(final List<Role> pRoles)
  {
    roles = pRoles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeRole(final Role role)
  {
    roles.remove(role);
    final RoleEntity roleEntity = (RoleEntity) role;
    roleEntity.setElement(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRole(final Role role)
  {
    final RoleEntity roleEntity = (RoleEntity) role;
    roleEntity.setElement(this);
    roles.add(role);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "TemplateProjectEntity [id=" + getId() + ", templateId=" + getElementId() + ", name=" + getName()
               + ", description=" + getDescription() + ", status=" + status + ", created=" + getCreated()
               + ", lastModified=" + getLastModified() + "]";
  }

}