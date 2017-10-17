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

import org.novaforge.forge.tools.managementmodule.dao.TransformationDAO;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.entity.TransformationEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ppr
 */
public class TransformationDAOImpl implements TransformationDAO
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
		this.entityManager = pEntityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transformation> findAllTransformation()
	{
		Query q = entityManager.createNamedQuery("TransformationEntity.findAll");
		return new LinkedList<Transformation>(q.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Transformation getTransformationOfProject(final String idProject)
	{
		Query q = entityManager.createNamedQuery("TransformationEntity.findByIdProject");
		q.setParameter("idProject", idProject);

		List<Transformation> results = q.getResultList();

		if (results.size() > 0)
		{
			return results.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transformation findById(final Long pId)
	{
		return entityManager.find(TransformationEntity.class, pId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transformation> findAll()
	{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Transformation> countCriteria = builder.createQuery(Transformation.class);
		Root<TransformationEntity> entityRoot = countCriteria.from(TransformationEntity.class);
		countCriteria.select(entityRoot);
		return new LinkedList<Transformation>(entityManager.createQuery(countCriteria).getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transformation save(final Transformation pTransformation)
	{
		entityManager.persist(pTransformation);
		entityManager.flush();
		return pTransformation;
	}

}
