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
package org.novaforge.forge.distribution.reporting.entity;

import org.novaforge.forge.distribution.reporting.domain.ForgeAnalysis;
import org.novaforge.forge.distribution.reporting.domain.ForgeDimension;
import org.novaforge.forge.distribution.reporting.domain.ProjectDimension;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Bilet-jc
 * @author Mohamed IBN EL AZZOUZI
 */
@Entity
@Table(name = "FORGE_ANALYSIS")
public class ForgeAnalysisEntity implements ForgeAnalysis
{

  /**
    * 
    */
  private static final long      serialVersionUID = 5044660605868451070L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, updatable = false)
  private long                   id;

  @Column(name = "connexion_number")
  private int                    connexionNumber;

  @Column(name = "account_login")
  private String                 accountLogin;

  @Column(name = "user_role")
  private String                 userRole;

  @OneToOne(targetEntity = ForgeDimensionEntity.class)
  @JoinColumn(name = "forge_id")
  private ForgeDimensionEntity   forge;

  @OneToOne(targetEntity = ProjectDimensionEntity.class)
  @JoinColumn(name = "project_id")
  private ProjectDimensionEntity project;

  public long getId()
  {
    return id;
  }

  public void setId(final long id)
  {
    this.id = id;
  }

  @Override
  public int getConnexionNumber()
  {
    return connexionNumber;
  }

  @Override
  public void setConnexionNumber(final int connexionNumber)
  {
    this.connexionNumber = connexionNumber;
  }

  @Override
  public String getAccountLogin()
  {
    return accountLogin;
  }

  @Override
  public void setAccountLogin(final String accountLogin)
  {
    this.accountLogin = accountLogin;
  }

  @Override
  public String getUserRole()
  {
    return userRole;
  }

  @Override
  public void setUserRole(final String userRole)
  {
    this.userRole = userRole;
  }

  @Override
  public ForgeDimension getForge()
  {
    return forge;
  }

  @Override
  public void setForge(final ForgeDimension forge)
  {
    this.forge = (ForgeDimensionEntity) forge;
  }

  @Override
  public ProjectDimension getProject()
  {
    return project;
  }

  @Override
  public void setProject(final ProjectDimension project)
  {
    this.project = (ProjectDimensionEntity) project;
  }

}
