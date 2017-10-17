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
package org.novaforge.forge.tools.managementmodule.internal.dao;

import org.novaforge.forge.tools.managementmodule.dao.CDOParametersDAO;
import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ppr
 */

public class CDOParametersDAOImpl implements CDOParametersDAO
{

  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;

  /**
   * Use by container to inject {@link EntityManager}
   * 
   * @param pEntityManager
   *          the entityManager to set
   */
  public void setEntityManager(final EntityManager pEntityManager)
  {
    entityManager = pEntityManager;
  }

  @Override
  public CDOParameters findById(final Long id)
  {
    final Query q = entityManager.createNamedQuery("CDOParametersEntity.findById");
    q.setParameter("id", id);
    final List<?> resultList = new LinkedList(q.getResultList());
    CDOParameters result = null;
    if ((resultList != null) && (resultList.size() == 1))
    {
      result = (CDOParameters) resultList.get(0);
    }
    return result;
  }

  @Override
  public List<CDOParameters> findALLByProjectId(final String pProjectId)
  {
    final Query q = entityManager.createNamedQuery("CDOParametersEntity.findByProject");
    q.setParameter("projectId", pProjectId);
    final List<?> resultList = new LinkedList(q.getResultList());
    final List<CDOParameters> result = new ArrayList<CDOParameters>();
    for (final Object cDOParameters : resultList)
    {
      result.add((CDOParameters) cDOParameters);
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CDOParameters save(final CDOParameters pCDOParameters)
  {
    entityManager.persist(pCDOParameters);
    entityManager.flush();
    return pCDOParameters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CDOParameters merge(final CDOParameters pCDOParameters)
  {
    entityManager.merge(pCDOParameters);
    entityManager.flush();
    return pCDOParameters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final CDOParameters pCdoParameters)
  {
    final CDOParameters merge = entityManager.merge(pCdoParameters);
    entityManager.remove(merge);
    entityManager.flush();
  }

}
