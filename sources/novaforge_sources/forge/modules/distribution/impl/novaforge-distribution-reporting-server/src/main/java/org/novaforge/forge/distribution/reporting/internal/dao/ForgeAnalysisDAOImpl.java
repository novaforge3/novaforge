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
package org.novaforge.forge.distribution.reporting.internal.dao;

import org.novaforge.forge.distribution.reporting.dao.ForgeAnalysisDAO;
import org.novaforge.forge.distribution.reporting.domain.ForgeAnalysis;
import org.novaforge.forge.distribution.reporting.domain.ProjectDimension;
import org.novaforge.forge.distribution.reporting.entity.ForgeAnalysisEntity;
import org.novaforge.forge.distribution.reporting.entity.ForgeAnalysisEntity_;
import org.novaforge.forge.distribution.reporting.entity.ForgeDimensionEntity;
import org.novaforge.forge.distribution.reporting.entity.ForgeDimensionEntity_;
import org.novaforge.forge.distribution.reporting.entity.ProjectDimensionEntity;
import org.novaforge.forge.distribution.reporting.entity.ProjectDimensionEntity_;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Bilet-jc
 */
public class ForgeAnalysisDAOImpl implements ForgeAnalysisDAO
{

  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;

  /**
   * Use by container to inject {@link EntityManager}
   *
   * @param pEntityManager
   *     the entityManager to set
   */
  public void setEntityManager(final EntityManager pEntityManager)
  {
    entityManager = pEntityManager;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ForgeAnalysis> getByForge(final Long pId)
  {
    final TypedQuery<ForgeAnalysis> q = entityManager.createNamedQuery("ForgeAnalysisEntity.findByForge",
                                                                       ForgeAnalysis.class);
    q.setParameter("forge", pId);
    return q.getResultList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ForgeAnalysis> findAnalysisByForgeId(final UUID forgeId)
  {
    if (forgeId == null)
    {
      throw new IllegalArgumentException("the given forge id should not be null");
    }
    final LinkedList<ForgeAnalysis>    result        = new LinkedList<ForgeAnalysis>();
    final CriteriaBuilder              builder       = entityManager.getCriteriaBuilder();
    final CriteriaQuery<ForgeAnalysis> query         = builder.createQuery(ForgeAnalysis.class);
    final Root<ForgeAnalysisEntity>    forgeAnalysis = query.from(ForgeAnalysisEntity.class);
    query.select(forgeAnalysis);
    final Join<ForgeAnalysisEntity, ForgeDimensionEntity> forge         = forgeAnalysis
                                                                              .join(ForgeAnalysisEntity_.forge);
    final Predicate                                       uuidPredicate = builder.equal(forge
                                                                                            .get(ForgeDimensionEntity_.forgeId),
                                                                                        forgeId.toString());
    query.where(uuidPredicate).distinct(true);

    final List<ForgeAnalysis> forgeAnalysises = entityManager.createQuery(query).getResultList();
    result.addAll(forgeAnalysises);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Map<String, String>> findProjectAndAccountNumbersByForges(final String... forgeIds)
  {
    final List<Map<String, String>> result = new ArrayList<Map<String, String>>();

    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    // Query 1
    final CriteriaQuery<Tuple> query1 = builder.createTupleQuery();
    final Root<ForgeAnalysisEntity> forgeAnalysis = query1.from(ForgeAnalysisEntity.class);

    final Join<ForgeAnalysisEntity, ForgeDimensionEntity>   forge   = forgeAnalysis.join(ForgeAnalysisEntity_.forge);
    final Join<ForgeAnalysisEntity, ProjectDimensionEntity> project = forgeAnalysis.join(ForgeAnalysisEntity_.project);

    final Predicate uuidsPredicate = forge.get(ForgeDimensionEntity_.forgeId).in(forgeIds);

    query1.select(builder.tuple(forge.get(ForgeDimensionEntity_.forgeId), forge.get(ForgeDimensionEntity_.name),
                                project.get(ProjectDimensionEntity_.name))).where(uuidsPredicate).groupBy(project
                                                                                                              .get(ProjectDimensionEntity_.name));

    final List<Tuple> resultQ1 = entityManager.createQuery(query1).getResultList();

    final HashMap<String, String> forgeNames = getForgeNames(resultQ1);
    final HashMap<String, Integer> porjectsCount = getProjectsCount(resultQ1);

    // Query 2
    final CriteriaQuery<Tuple> query2 = builder.createTupleQuery();
    final Root<ForgeAnalysisEntity> forgeAnalysis2 = query2.from(ForgeAnalysisEntity.class);

    final Join<ForgeAnalysisEntity, ForgeDimensionEntity> forge2 = forgeAnalysis2.join(ForgeAnalysisEntity_.forge);

    final Predicate uuidsPredicate2 = forge2.get(ForgeDimensionEntity_.forgeId).in((Object[]) forgeIds);

    query2.select(builder.tuple(forge2.get(ForgeDimensionEntity_.forgeId), forge2.get(ForgeDimensionEntity_.name),
                                forgeAnalysis2.get(ForgeAnalysisEntity_.accountLogin))).where(uuidsPredicate2)
          .groupBy(forgeAnalysis2.get(ForgeAnalysisEntity_.accountLogin));

    final List<Tuple> resultQ2 = entityManager.createQuery(query2).getResultList();

    final HashMap<String, Integer> accountsCount = getAccountsCount(resultQ2);

    for (final Map.Entry<String, String> entry : forgeNames.entrySet())
    {
      final String forgeId = entry.getKey();
      final String forgeName = entry.getValue();
      final int nbProjects = porjectsCount.get(forgeId) != null ? porjectsCount.get(forgeId) : 0;
      final int nbAccount = accountsCount.get(forgeId) != null ? accountsCount.get(forgeId) : 0;
      final Map<String, String> tuple = new HashMap<>();
      tuple.put(FORGEID, forgeId);
      tuple.put(FORGENAME, forgeName);
      tuple.put(NBPROJECTS, String.valueOf(nbProjects));
      tuple.put(NBACCOUNT, String.valueOf(nbAccount));
      result.add(tuple);

    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectDimension> findDimensionByForgeId(final UUID forgeId)
  {
    if (forgeId == null)
    {
      throw new IllegalArgumentException("the given forge id should not be null");
    }
    final LinkedList<ProjectDimension> result = new LinkedList<ProjectDimension>();

    final CriteriaBuilder              builder       = entityManager.getCriteriaBuilder();
    final CriteriaQuery<ForgeAnalysis> query         = builder.createQuery(ForgeAnalysis.class);
    final Root<ForgeAnalysisEntity>    forgeAnalysis = query.from(ForgeAnalysisEntity.class);
    query.select(forgeAnalysis);

    final Join<ForgeAnalysisEntity, ForgeDimensionEntity> forge = forgeAnalysis.join(ForgeAnalysisEntity_.forge);
    final Join<ForgeAnalysisEntity, ProjectDimensionEntity> project = forgeAnalysis.join(ForgeAnalysisEntity_.project);
    final Predicate uuidPredicate = builder.equal(forge.get(ForgeDimensionEntity_.forgeId), forgeId.toString());
    query.where(uuidPredicate).groupBy(project.get(ProjectDimensionEntity_.name));

    final List<ForgeAnalysis> forgeAnalysises = entityManager.createQuery(query).getResultList();
    for (final ForgeAnalysis entity : forgeAnalysises)
    {
      result.add(entity.getProject());
    }
    return result;

  }

  @Override
  public List<Map<String, String>> findProjectNumbersByOrganization(final String... forgeIds)
  {
    final List<Map<String, String>> result = new ArrayList<Map<String, String>>();

    final CriteriaBuilder      builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Tuple> query   = builder.createTupleQuery();
    final Root<ForgeAnalysisEntity> forgeAnalysis = query.from(ForgeAnalysisEntity.class);

    final Join<ForgeAnalysisEntity, ForgeDimensionEntity>   forge   = forgeAnalysis.join(ForgeAnalysisEntity_.forge);
    final Join<ForgeAnalysisEntity, ProjectDimensionEntity> project = forgeAnalysis.join(ForgeAnalysisEntity_.project);

    final Predicate uuidsPredicate = forge.get(ForgeDimensionEntity_.forgeId).in((Object[]) forgeIds);

    query.select(builder.tuple(project.get(ProjectDimensionEntity_.organization))).where(uuidsPredicate).groupBy(project
                                                                                                                     .get(ProjectDimensionEntity_.name));

    final List<Tuple>              results = entityManager.createQuery(query).getResultList();
    final HashMap<String, Integer> inc     = new HashMap<String, Integer>();

    for (final Tuple t : results)
    {
      final String organization = t.get(0).toString();
      if (!inc.containsKey(organization))
      {
        inc.put(organization, 1);
      }
      else
      {
        final int value = inc.get(organization);
        inc.put(organization, value + 1);
      }
    }

    for (final Map.Entry<String, Integer> entry : inc.entrySet())
    {
      final Map<String, String> tuple = new HashMap<>();
      tuple.put("organization", entry.getKey());
      tuple.put("nbProjects", entry.getValue().toString());
      result.add(tuple);
    }

    return result;
  }

  @Override
  public List<Map<String, String>> findAccountNumbersByRolesAndForges(final String... forgeIds)
  {
    final List<Map<String, String>> result = new ArrayList<Map<String, String>>();

    final CriteriaBuilder      builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Tuple> query   = builder.createTupleQuery();
    final Root<ForgeAnalysisEntity> forgeAnalysis = query.from(ForgeAnalysisEntity.class);

    final Join<ForgeAnalysisEntity, ForgeDimensionEntity>   forge   = forgeAnalysis.join(ForgeAnalysisEntity_.forge);
    final Join<ForgeAnalysisEntity, ProjectDimensionEntity> project = forgeAnalysis.join(ForgeAnalysisEntity_.project);

    final Predicate uuidsPredicate = forge.get(ForgeDimensionEntity_.forgeId).in((Object[]) forgeIds);

    final Expression<Long> countFARoles = builder.count(forgeAnalysis.get(ForgeAnalysisEntity_.userRole));

    query.select(builder.tuple(forge.get(ForgeDimensionEntity_.forgeId), forge.get(ForgeDimensionEntity_.name),
                               project.get(ProjectDimensionEntity_.name),
                               forgeAnalysis.get(ForgeAnalysisEntity_.userRole), countFARoles)).where(uuidsPredicate)
         .groupBy(forge.get(ForgeDimensionEntity_.name), project.get(ProjectDimensionEntity_.name),
                  forgeAnalysis.get(ForgeAnalysisEntity_.userRole));

    final List<Tuple> results = entityManager.createQuery(query).getResultList();
    for (final Tuple t : results)
    {
      final Map<String, String> tuple = new HashMap<String, String>();
      tuple.put(FORGEID, "" + t.get(0));
      tuple.put(FORGENAME, "" + t.get(1));
      tuple.put(PROJECTNAME, "" + t.get(2));
      tuple.put(USERROLE, "" + t.get(3));
      tuple.put(NBUSERACCOUNT, "" + t.get(4));

      result.add(tuple);

    }

    return result;
  }

  @Override
  public List<Map<String, String>> findlastDateUpdatedByForges(String... pForgeIds)
  {

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final ForgeAnalysis pForgeAnalysis)
  {
    entityManager.remove(pForgeAnalysis);
    entityManager.flush();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final ProjectDimension pProjectDimension)
  {
    entityManager.remove(pProjectDimension);
    entityManager.flush();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectDimension newProjectDimension()
  {
    return new ProjectDimensionEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectDimension save(final ProjectDimension pProjectDimension)
  {
    entityManager.persist(pProjectDimension);
    entityManager.flush();
    return pProjectDimension;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeAnalysis newForgeAnalysis()
  {
    return new ForgeAnalysisEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeAnalysis save(final ForgeAnalysis pForgeAnalysis)
  {
    entityManager.persist(pForgeAnalysis);
    entityManager.flush();
    return pForgeAnalysis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeAnalysis update(final ForgeAnalysis pForgeAnalysis)
  {
    entityManager.merge(pForgeAnalysis);
    entityManager.flush();
    return pForgeAnalysis;
  }

  private HashMap<String, String> getForgeNames(final List<Tuple> tuples)
  {
    final HashMap<String, String> result = new HashMap<String, String>();
    for (final Tuple t : tuples)
    {
      result.put(t.get(0).toString(), t.get(1).toString());
    }
    return result;
  }

  private HashMap<String, Integer> getProjectsCount(final List<Tuple> tuples)
  {
    final HashMap<String, Integer> result = new HashMap<>();
    for (final Tuple t : tuples)
    {
      final String forgeId = t.get(0).toString();
      if (!result.containsKey(forgeId))
      {
        result.put(forgeId, 1);
      }
      else
      {
        final int value = result.get(forgeId);
        result.put(forgeId, value + 1);
      }
    }
    return result;
  }

  private HashMap<String, Integer> getAccountsCount(final List<Tuple> tuples)
  {
    final HashMap<String, Integer> result = new HashMap<>();
    for (final Tuple t : tuples)
    {
      final String forgeId = t.get(0).toString();
      if (!result.containsKey(forgeId))
      {
        result.put(forgeId, 1);
      }
      else
      {
        final int value = result.get(forgeId);
        result.put(forgeId, value + 1);
      }
    }
    return result;
  }

}
