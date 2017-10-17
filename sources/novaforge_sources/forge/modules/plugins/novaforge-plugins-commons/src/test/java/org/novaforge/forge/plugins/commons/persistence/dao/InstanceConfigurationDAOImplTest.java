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
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.plugins.commons.persistence.entity.InstanceConfigurationEntity;
import org.novaforge.forge.plugins.commons.persistence.entity.ToolInstanceEntity;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import javax.persistence.TypedQuery;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author sbenoist
 */
public class InstanceConfigurationDAOImplTest extends JPATestCase
{
	private static final String          TOOL_PROJECT_ID               = "tool_id";
	private static final String          FORGE_ID                      = "forge_id";
	private static final String          FORGE_PROJECT_ID              = "project_id";
	private static final String          TOOL_INSTANCE_ALIAS           = "tool_instance_alias";
	private static final String          TOOL_INSTANCE_URL             = "http://tool_instance_url";
	private static final String          TOOL_INSTANCE_NAME            = "tool_instance_name";
	private static final UUID            TOOL_INSTANCE_ID              = UUID.randomUUID();
	private static final String          INSTANCE_CONFIGURATION_ID     = "instance_config_id";
	private static final String          INSTANCE_CONFIGURATION_ID_NEW = "instance_config_id_new";
	private static final String          INSTANCE_ID_1                 = "instance_id_1";
	private static final String          INSTANCE_ID_2                 = "instance_id_2";
	private static final String          INSTANCE_ID_3                 = "instance_id_3";
	private InstanceConfigurationDAOImpl InstanceConfigurationDAO;

	public InstanceConfigurationDAOImplTest()
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

		InstanceConfigurationDAO = new InstanceConfigurationDAOImpl();
		InstanceConfigurationDAO.setEntityManager(em);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@After
	public void tearDown()
	{
		super.tearDown();
		InstanceConfigurationDAO = null;
	}

	@Test
	public void testPersist() throws MalformedURLException
	{
		// build one instance
		final InstanceConfigurationEntity instanceConfiguration = new InstanceConfigurationEntity();
		instanceConfiguration.setInstanceId(INSTANCE_ID_1);
		instanceConfiguration.setConfigurationId(INSTANCE_CONFIGURATION_ID);
		instanceConfiguration.setForgeId(FORGE_ID);
		instanceConfiguration.setForgeProjectId(FORGE_PROJECT_ID);
		instanceConfiguration.setToolProjectId(TOOL_PROJECT_ID);
		instanceConfiguration.setToolInstance(buildToolInstanceEntity(TOOL_INSTANCE_ID));

		em.getTransaction().begin();
		InstanceConfigurationDAO.persist(instanceConfiguration);
		em.getTransaction().commit();

		final InstanceConfiguration instance = InstanceConfigurationDAO.findByInstanceId(INSTANCE_ID_1);
		assertNotNull(instance);
		assertThat(instance.getInstanceId(), is(INSTANCE_ID_1));
	}

	private ToolInstanceEntity buildToolInstanceEntity(final UUID pToolId) throws MalformedURLException
	{
		final TypedQuery<ToolInstanceEntity> query = em.createQuery("SELECT p FROM ToolInstanceEntity p WHERE p.toolId= :toolId",
																																ToolInstanceEntity.class);
		query.setParameter("toolId", pToolId.toString());
		final List<ToolInstanceEntity> resultList   = query.getResultList();
		ToolInstanceEntity             toolInstance = null;
		if ((resultList == null) || (resultList.isEmpty()))
		{
			toolInstance = new ToolInstanceEntity();
			toolInstance.setAlias(TOOL_INSTANCE_ALIAS);
			toolInstance.setBaseURL(new URL(TOOL_INSTANCE_URL));
			toolInstance.setName(TOOL_INSTANCE_NAME);
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
	public void testUpdate() throws MalformedURLException
	{
		// build one instance
		final InstanceConfigurationEntity instance = buildInstanceConfigurationEntity(INSTANCE_ID_1,
		    TOOL_INSTANCE_ID);

		em.getTransaction().begin();
		instance.setConfigurationId(INSTANCE_CONFIGURATION_ID_NEW);
		InstanceConfigurationDAO.update(instance);
		em.getTransaction().commit();

		final InstanceConfiguration instanceFound = InstanceConfigurationDAO.findByInstanceId(INSTANCE_ID_1);
		assertNotNull(instanceFound);
		assertThat(instanceFound.getConfigurationId(), is(INSTANCE_CONFIGURATION_ID_NEW));
	}

	private InstanceConfigurationEntity buildInstanceConfigurationEntity(final String pInstanceId,
																																			 final UUID pToolInstanceId)
			throws MalformedURLException
	{
		final TypedQuery<InstanceConfigurationEntity> query = em.createQuery("SELECT p FROM InstanceConfigurationEntity p WHERE p.instanceId= :instanceId",
																																				 InstanceConfigurationEntity.class);
		query.setParameter("instanceId", pInstanceId);
		final List<InstanceConfigurationEntity> resultList            = query.getResultList();
		InstanceConfigurationEntity             instanceConfiguration = null;
		if ((resultList == null) || (resultList.isEmpty()))
		{
			instanceConfiguration = new InstanceConfigurationEntity();
			instanceConfiguration.setForgeId(FORGE_ID);
			instanceConfiguration.setForgeProjectId(FORGE_PROJECT_ID);
			instanceConfiguration.setToolProjectId(TOOL_PROJECT_ID);
			instanceConfiguration.setConfigurationId(INSTANCE_CONFIGURATION_ID);
			instanceConfiguration.setInstanceId(pInstanceId);
			instanceConfiguration.setToolInstance(buildToolInstanceEntity(pToolInstanceId));
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

	@Test
	public void testDelete() throws MalformedURLException
	{
		// build one instance
		final InstanceConfigurationEntity instance = buildInstanceConfigurationEntity(INSTANCE_ID_1,
		    TOOL_INSTANCE_ID);

		em.getTransaction().begin();
		InstanceConfigurationDAO.delete(instance);
		em.getTransaction().commit();

		final List<InstanceConfiguration> instancesFound = InstanceConfigurationDAO
		    .findByProject(FORGE_PROJECT_ID);
		assertNotNull(instancesFound);
		assertThat(instancesFound.size(), is(0));
	}

	@Test
	public void testfindByForgeId() throws MalformedURLException
	{
		// build 3 instances
		buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID);
		buildInstanceConfigurationEntity(INSTANCE_ID_2, TOOL_INSTANCE_ID);
		buildInstanceConfigurationEntity(INSTANCE_ID_3, TOOL_INSTANCE_ID);

		final List<InstanceConfiguration> instancesFound = InstanceConfigurationDAO.findByForgeId(FORGE_ID);
		assertNotNull(instancesFound);
		assertThat(instancesFound.size(), is(3));
	}

	@Test
	public void testFindByInstanceId() throws MalformedURLException
	{
		// build one instance
		buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID);

		final InstanceConfiguration instancesFound = InstanceConfigurationDAO.findByInstanceId(INSTANCE_ID_1);
		assertNotNull(instancesFound);
		assertThat(instancesFound.getInstanceId(), is(INSTANCE_ID_1));
	}

	@Test
	public void testFindByProject() throws MalformedURLException
	{
		// build 3 instances
		buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID);
		buildInstanceConfigurationEntity(INSTANCE_ID_2, TOOL_INSTANCE_ID);
		buildInstanceConfigurationEntity(INSTANCE_ID_3, TOOL_INSTANCE_ID);

		final List<InstanceConfiguration> instancesFound = InstanceConfigurationDAO
		    .findByProject(FORGE_PROJECT_ID);
		assertNotNull(instancesFound);
		assertThat(instancesFound.size(), is(3));
	}

	@Test
	public void testFindByToolProject() throws MalformedURLException
	{
		// build one instance
		buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID);

		final InstanceConfiguration instanceFound = InstanceConfigurationDAO.findByToolProject(TOOL_PROJECT_ID);
		assertNotNull(instanceFound);
		assertThat(instanceFound.getInstanceId(), is(INSTANCE_ID_1));
	}
}
