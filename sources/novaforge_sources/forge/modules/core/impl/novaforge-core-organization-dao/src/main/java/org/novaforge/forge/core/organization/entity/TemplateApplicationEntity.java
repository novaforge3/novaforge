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

import org.novaforge.forge.core.organization.model.TemplateApplication;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lamirang
 */
@Entity
@Table(name = "NODE")
public class TemplateApplicationEntity extends ApplicationEntity implements TemplateApplication
{

  /**
	 * 
	 */
  private static final long   serialVersionUID = 850354005374220324L;
  @ElementCollection(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "forge_role")
  @Column(name = "application_role", nullable = true)
  @CollectionTable(name = "TEMPLATE_ROLES_MAPPING", joinColumns = @JoinColumn(name = "application_id"))
  private Map<String, String> rolesMapping     = new HashMap<String, String>();

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "TemplateApplicationEntity [pluginUUID=" + getPluginUUID() + ", rolesMapping=" + rolesMapping + ", getUri()="
               + getUri() + ", getId()=" + getId() + ", getName()=" + getName() + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Map<String, String> getRolesMapping()
  {
    return rolesMapping;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRolesMapping(final Map<String, String> pRoleMapping)
  {
    rolesMapping = pRoleMapping;
  }

}