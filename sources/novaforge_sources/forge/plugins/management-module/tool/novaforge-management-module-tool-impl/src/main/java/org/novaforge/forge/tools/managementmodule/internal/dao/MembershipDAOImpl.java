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

import org.novaforge.forge.tools.managementmodule.dao.MembershipDAO;
import org.novaforge.forge.tools.managementmodule.domain.Membership;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.entity.MembershipEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author fdemange
 */
public class MembershipDAOImpl implements MembershipDAO
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
	public List<Membership> findAllMembershipsByProject(final String projectId)
	{
		Query q = entityManager.createNamedQuery("MembershipEntity.findAllMembershipsByProject");
		q.setParameter("projectId", projectId);
		return new LinkedList<Membership>(q.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Membership> findAllMembershipsByActor(final String pLogin)
	{
		Query q = entityManager.createNamedQuery("MembershipEntity.findAllMembershipsByActor");
		q.setParameter("login", pLogin);
		return new LinkedList(q.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Membership findAllMembershipsByActorAndProject(final String projectId, final String login)

	{

		Query q = entityManager.createNamedQuery("MembershipEntity.findAllMembershipsByActorAndProject");
		q.setParameter("login", login);
		q.setParameter("projectId", projectId);

		List<Membership> results = q.getResultList();

		if (results.size() > 0)
		{
			return results.get(0);
		}
		else
		{
			return null;
		}

	}

	@Override
	public Set<Role> findAllRolesByActorAndProject(final String projectId, final String login)

	{
		Set<Role> roles = new HashSet<Role>();
		Membership memberships = findAllMembershipsByActorAndProject(projectId, login);
		if ((memberships != null))
		{

			roles.add(memberships.getRole());

		}

		return roles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllActorsByRoleAndProject(final String projectId, final String roleName)

	{
		Query q = entityManager.createNamedQuery("MembershipEntity.findAllActorByRoleAndProject");
		q.setParameter("roleName", roleName);
		q.setParameter("projectId", projectId);
		return new LinkedList(q.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllActorsByProject(final String projectId)
	{
		Query q = entityManager.createNamedQuery("MembershipEntity.findAllActorsByProject");
		q.setParameter("projectId", projectId);
		return new LinkedList(q.getResultList());
	}

	@Override
	public Membership addActor(final Project project, final User actor, final Role role)

	{
		Membership membership = new MembershipEntity(actor, role, project);
		entityManager.persist(membership);
		entityManager.flush();
		return membership;
	}

	@Override
	public void remooveActor(final Project project, final User actor)
	{
		Membership memberships = findAllMembershipsByActorAndProject(project.getProjectId(), actor.getLogin());
		if (memberships != null)
		{
			entityManager.remove(memberships);
			entityManager.flush();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllActorsByRole(final String pRoleName)
	{
		Query q = entityManager.createNamedQuery("MembershipEntity.findAllActorByRole");
		q.setParameter("roleName", pRoleName);
		return new LinkedList(q.getResultList());
	}

	@Override
	public void updateMembership(final User u, final Project p, final Role newRole)
	{
		Membership memberships = findAllMembershipsByActorAndProject(p.getProjectId(), u.getLogin());

		if (memberships != null)
		{
			p.removeMembership(memberships);
			entityManager.remove(memberships);
			entityManager.persist(new MembershipEntity(u, newRole, p));
			entityManager.flush();
		}
	}

	@Override
	public void deleteMembership(final User u, final Project p)
	{
		Membership memberships = findAllMembershipsByActorAndProject(p.getProjectId(), u.getLogin());
		if (memberships != null)
		{
			deleteMembership(memberships);
		}
	}

	@Override
	public void deleteMembership(final Membership m)
	{
		entityManager.remove(m);
		entityManager.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Membership save(final Membership pCurrentMembership)
	{
		entityManager.persist(pCurrentMembership);
		entityManager.flush();
		return pCurrentMembership;

	}

}
