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
package org.novaforge.forge.plugins.commons.persistence.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.plugins.commons.persistence.entity.InstanceConfigurationEntity;
import org.novaforge.forge.plugins.commons.persistence.entity.RolesMappingEntity;
import org.novaforge.forge.plugins.commons.persistence.entity.ToolInstanceEntity;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import javax.persistence.TypedQuery;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author sbenoist
 */
public class RolesMappingDAOImplTest extends JPATestCase
{
	private static final String FORGE_ROLE_ID_1           = "forge_role_id_1";
	private static final String TOOL_ROLE_ID_1            = "tool_role_id_1";
	private static final String FORGE_ROLE_ID_2           = "forge_role_id_2";
	private static final String TOOL_ROLE_ID_2            = "tool_role_id_2";
	private static final String FORGE_ROLE_ID_3           = "forge_role_id_3";
	private static final String TOOL_ROLE_ID_3            = "tool_role_id_3";
	private static final String TOOL_PROJECT_ID           = "tool_id";
	private static final String FORGE_ID                  = "forge_id";
	private static final String FORGE_PROJECT_ID          = "project_id";
	private static final String TOOL_INSTANCE_ALIAS       = "tool_instance_alias";
	private static final String TOOL_INSTANCE_URL_1       = "http://host1:8000/path1";
	private static final String TOOL_INSTANCE_NAME_1      = "tool_instance_name1";
	private static final String INSTANCE_ID_1             = "instance_id_1";
	private static final UUID   TOOL_INSTANCE_ID          = UUID.randomUUID();
	private static final String INSTANCE_CONFIGURATION_ID = "instance_config_id";
	private RolesMappingDAOImpl rolesMappingDAO;

	public RolesMappingDAOImplTest()
	{
		super("jdbc/novaforge", "plugins.commons.test");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@Before
	public void setUp()
	{
		super.setUp();

		rolesMappingDAO = new RolesMappingDAOImpl();
		rolesMappingDAO.setEntityManager(em);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@After
	public void tearDown()
	{
		super.tearDown();
		rolesMappingDAO = null;
	}

	@Test
	public void testPersist() throws MalformedURLException
	{
		final RolesMappingEntity rolesMapping = new RolesMappingEntity();
		rolesMapping.setForgeRole(FORGE_ROLE_ID_1);
		rolesMapping.setToolRole(TOOL_ROLE_ID_1);
		rolesMapping.setInstance(buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID));

		em.getTransaction().begin();
		rolesMappingDAO.persist(rolesMapping);
		em.getTransaction().commit();

		final Map<String, String> resultMap = rolesMappingDAO.findByInstance(INSTANCE_ID_1);
		assertNotNull(resultMap);
		assertThat(resultMap.size(), is(1));
	}

	private InstanceConfigurationEntity buildInstanceConfigurationEntity(final String pInstanceId,
	    final UUID pToolInstanceId) throws MalformedURLException
	{
		final TypedQuery<InstanceConfigurationEntity> query = em.createQuery(
		    "SELECT p FROM InstanceConfigurationEntity p WHERE p.instanceId= :instanceId",
		    InstanceConfigurationEntity.class);
		query.setParameter("instanceId", pInstanceId);
		final List<InstanceConfigurationEntity> resultList = query.getResultList();
		InstanceConfigurationEntity instanceConfiguration = null;
		if ((resultList == null) || (resultList.isEmpty()))
		{
			instanceConfiguration = new InstanceConfigurationEntity();
			instanceConfiguration.setForgeId(FORGE_ID);
			instanceConfiguration.setForgeProjectId(FORGE_PROJECT_ID);
			instanceConfiguration.setToolProjectId(TOOL_PROJECT_ID);
			instanceConfiguration.setConfigurationId(INSTANCE_CONFIGURATION_ID);
			instanceConfiguration.setInstanceId(pInstanceId);
			instanceConfiguration.setToolInstance(buildToolInstanceEntity(pToolInstanceId, TOOL_INSTANCE_NAME_1,
			    TOOL_INSTANCE_URL_1));
			if (!em.getTransaction().isActive())
			{
				em.getTransaction().begin();
			}
			em.persist(instanceConfiguration);
			em.getTransaction().commit();
		}
		else
		{
			instanceConfiguration = resultList.get(0);
		}

		return instanceConfiguration;
	}

	private ToolInstanceEntity buildToolInstanceEntity(final UUID pToolId, final String pName, final String pUrl)
	    throws MalformedURLException
	{
		final TypedQuery<ToolInstanceEntity> query = em.createQuery(
		    "SELECT p FROM ToolInstanceEntity p WHERE p.toolId= :toolId", ToolInstanceEntity.class);
		query.setParameter("toolId", pToolId.toString());
		final List<ToolInstanceEntity> resultList = query.getResultList();
		ToolInstanceEntity toolInstance = null;
		if ((resultList == null) || (resultList.isEmpty()))
		{
			toolInstance = new ToolInstanceEntity();
			toolInstance.setAlias(TOOL_INSTANCE_ALIAS);
			toolInstance.setBaseURL(new URL(pUrl));
			toolInstance.setName(pName);
			toolInstance.setUUID(pToolId);
			if (!em.getTransaction().isActive())
			{
				em.getTransaction().begin();
			}
			em.persist(toolInstance);
			em.getTransaction().commit();
		}
		else
		{
			toolInstance = resultList.get(0);
		}

		return toolInstance;
	}

	@Test
	public void testFindByInstance() throws MalformedURLException
	{
		buildRolesMappingEntity(FORGE_ROLE_ID_1, TOOL_ROLE_ID_1, INSTANCE_ID_1, TOOL_INSTANCE_ID);
		final Map<String, String> resultMap = rolesMappingDAO.findByInstance(INSTANCE_ID_1);
		assertNotNull(resultMap);
		assertThat(resultMap.size(), is(1));
		assertThat(resultMap.containsKey(FORGE_ROLE_ID_1), is(true));
		assertThat(resultMap.containsValue(TOOL_ROLE_ID_1), is(true));
	}

	private RolesMappingEntity buildRolesMappingEntity(final String pForgeRole, final String pToolRole,
																										 final String pInstanceId, final UUID pToolInstanceId)
			throws MalformedURLException
	{
		final RolesMappingEntity rolesMapping = new RolesMappingEntity();
		rolesMapping.setForgeRole(pForgeRole);
		rolesMapping.setToolRole(pToolRole);
		rolesMapping.setInstance(buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID));
		em.getTransaction().begin();
		rolesMappingDAO.persist(rolesMapping);
		em.getTransaction().commit();
		return rolesMapping;
	}

	@Test
	public void testRemoveByInstanceAndForgeRole() throws MalformedURLException
	{
		// build 3 rolesmapping
		buildRolesMappingEntity(FORGE_ROLE_ID_1, TOOL_ROLE_ID_1, INSTANCE_ID_1, TOOL_INSTANCE_ID);
		buildRolesMappingEntity(FORGE_ROLE_ID_2, TOOL_ROLE_ID_2, INSTANCE_ID_1, TOOL_INSTANCE_ID);
		buildRolesMappingEntity(FORGE_ROLE_ID_3, TOOL_ROLE_ID_3, INSTANCE_ID_1, TOOL_INSTANCE_ID);

		em.getTransaction().begin();
		rolesMappingDAO.removeByInstanceAndForgeRole(INSTANCE_ID_1, FORGE_ROLE_ID_1);
		em.getTransaction().commit();

		final Map<String, String> resultMap = rolesMappingDAO.findByInstance(INSTANCE_ID_1);
		assertNotNull(resultMap);
		assertThat(resultMap.size(), is(2));
		assertThat(resultMap.containsKey(FORGE_ROLE_ID_1), is(false));
		assertThat(resultMap.containsValue(TOOL_ROLE_ID_1), is(false));
	}

	@Test
	public void testFindByInstanceAndForgeRole() throws MalformedURLException
	{
		buildRolesMappingEntity(FORGE_ROLE_ID_1, TOOL_ROLE_ID_1, INSTANCE_ID_1, TOOL_INSTANCE_ID);
		final String toolRole = rolesMappingDAO.findByInstanceAndForgeRole(INSTANCE_ID_1, FORGE_ROLE_ID_1);
		assertNotNull(toolRole);
		assertThat(toolRole, is(TOOL_ROLE_ID_1));
	}

	@Test
	public void testDeleteByInstance() throws MalformedURLException
	{
		// build 3 rolesmapping
		buildRolesMappingEntity(FORGE_ROLE_ID_1, TOOL_ROLE_ID_1, INSTANCE_ID_1, TOOL_INSTANCE_ID);
		buildRolesMappingEntity(FORGE_ROLE_ID_2, TOOL_ROLE_ID_2, INSTANCE_ID_1, TOOL_INSTANCE_ID);
		buildRolesMappingEntity(FORGE_ROLE_ID_3, TOOL_ROLE_ID_3, INSTANCE_ID_1, TOOL_INSTANCE_ID);

		em.getTransaction().begin();
		rolesMappingDAO.deleteByInstance(INSTANCE_ID_1);
		em.getTransaction().commit();

		final TypedQuery<InstanceConfigurationEntity> query = em.createQuery("SELECT p FROM InstanceConfigurationEntity p WHERE p.instanceId= :instanceId",
																																				 InstanceConfigurationEntity.class);
		query.setParameter("instanceId", INSTANCE_ID_1);
		final List<InstanceConfigurationEntity> resultList = query.getResultList();
		System.out.println(resultList.size());
		final Map<String, String> resultMap = rolesMappingDAO.findByInstance(INSTANCE_ID_1);
		assertNotNull(resultMap);
		assertThat(resultMap.size(), is(0));
	}
}
