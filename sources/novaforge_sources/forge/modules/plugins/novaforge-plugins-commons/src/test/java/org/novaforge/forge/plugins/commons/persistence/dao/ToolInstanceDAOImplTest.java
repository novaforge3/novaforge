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
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstanceStatus;
import org.novaforge.forge.plugins.commons.persistence.entity.InstanceConfigurationEntity;
import org.novaforge.forge.plugins.commons.persistence.entity.ToolInstanceEntity;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import javax.persistence.TypedQuery;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author sbenoist
 */
public class ToolInstanceDAOImplTest extends JPATestCase
{
  private static final String TOOL_PROJECT_ID           = "tool_id";
  private static final String FORGE_ID                  = "forge_id";
  private static final String FORGE_PROJECT_ID          = "project_id";
  private static final String TOOL_INSTANCE_ALIAS       = "tool_instance_alias";
  private static final String TOOL_INSTANCE_URL_1       = "http://host1:8000/path1";
  private static final String TOOL_INSTANCE_URL_2       = "http://host2:8000/path2";
  private static final String TOOL_INSTANCE_URL_3       = "http://host3:8000/path3";
  private static final String TOOL_INSTANCE_NAME_1      = "tool_instance_name1";
  private static final String TOOL_INSTANCE_NAME_2      = "tool_instance_name2";
  private static final String TOOL_INSTANCE_NAME_3      = "tool_instance_name3";
  private static final String TOOL_INSTANCE_NEW_ALIAS   = "tool_instance_new_alias";
  private static final String INSTANCE_ID_1             = "instance_id_1";
  private static final String INSTANCE_ID_2             = "instance_id_2";
  private static final String INSTANCE_ID_3             = "instance_id_3";
  private static final UUID   TOOL_INSTANCE_ID          = UUID.randomUUID();
  private static final String INSTANCE_CONFIGURATION_ID = "instance_config_id";
  private ToolInstanceDAOImpl toolInstanceDAO;

  public ToolInstanceDAOImplTest()
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

    toolInstanceDAO = new ToolInstanceDAOImpl();
    toolInstanceDAO.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    toolInstanceDAO = null;
  }

  // @Test
  public void testPersist() throws MalformedURLException
  {
    final UUID uuid = UUID.randomUUID();

    final ToolInstanceEntity toolInstance = new ToolInstanceEntity();
    toolInstance.setAlias(TOOL_INSTANCE_ALIAS);
    toolInstance.setBaseURL(new URL(TOOL_INSTANCE_URL_1));
    toolInstance.setName(TOOL_INSTANCE_NAME_1);
    toolInstance.setUUID(uuid);

    em.getTransaction().begin();
    toolInstanceDAO.persist(toolInstance);
    em.getTransaction().commit();

    final ToolInstance toolInstanceFound = toolInstanceDAO.findInstanceByUUID(uuid);
    assertNotNull(toolInstanceFound);
    assertThat(toolInstanceFound.getUUID(), is(uuid));
  }

  // @Test
  public void testUpdate() throws MalformedURLException
  {
    final UUID uuid = UUID.randomUUID();

    final ToolInstanceEntity toolInstanceEntity = buildToolInstanceEntity(uuid, TOOL_INSTANCE_NAME_1,
        TOOL_INSTANCE_URL_1);
    toolInstanceEntity.setAlias(TOOL_INSTANCE_NEW_ALIAS);

    em.getTransaction().begin();
    toolInstanceDAO.update(toolInstanceEntity);
    em.getTransaction().commit();

    final ToolInstance toolInstanceFound = toolInstanceDAO.findInstanceByUUID(uuid);
    assertNotNull(toolInstanceFound);
    assertThat(toolInstanceFound.getUUID(), is(uuid));
    assertThat(toolInstanceFound.getAlias(), is(TOOL_INSTANCE_NEW_ALIAS));
  }

  private ToolInstanceEntity buildToolInstanceEntity(final UUID pToolId, final String pName, final String pUrl)
      throws MalformedURLException
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
      toolInstance.setBaseURL(new URL(pUrl));
      toolInstance.setName(pName);
      toolInstance.setUUID(pToolId);
      toolInstance.setShareable(false);
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

  // @Test
  public void testDelete() throws MalformedURLException
  {
    final UUID uuid = UUID.randomUUID();

    final ToolInstanceEntity toolInstanceEntity = buildToolInstanceEntity(uuid, TOOL_INSTANCE_NAME_1,
        TOOL_INSTANCE_URL_1);

    em.getTransaction().begin();
    toolInstanceDAO.delete(toolInstanceEntity);
    em.getTransaction().commit();

    final ToolInstance toolInstanceFound = toolInstanceDAO.findInstanceByUUID(uuid);
    assertNull(toolInstanceFound);
  }

  // @Test
  public void testFindAllInstances() throws MalformedURLException
  {
    // build 3 toolinstances
    final UUID uuid1 = UUID.randomUUID();
    final ToolInstance toolInstance1 = buildToolInstanceEntity(uuid1, TOOL_INSTANCE_NAME_1,
        TOOL_INSTANCE_URL_1);

    final UUID uuid2 = UUID.randomUUID();
    final ToolInstance toolInstance2 = buildToolInstanceEntity(uuid2, TOOL_INSTANCE_NAME_2,
        TOOL_INSTANCE_URL_2);

    final UUID uuid3 = UUID.randomUUID();
    final ToolInstance toolInstance3 = buildToolInstanceEntity(uuid3, TOOL_INSTANCE_NAME_3,
        TOOL_INSTANCE_URL_3);

    final List<ToolInstance> instances = toolInstanceDAO.findAllInstances();
    assertNotNull(instances);
    assertThat(instances.size(), is(3));
    assertTrue(isIn(instances, toolInstance1));
    assertTrue(isIn(instances, toolInstance2));
    assertTrue(isIn(instances, toolInstance3));
  }

  private boolean isIn(final List<ToolInstance> pList, final ToolInstance pElement)
  {
    boolean ret = false;

    for (final ToolInstance t : pList)
    {
      if (t.equals(pElement))
      {
        ret = true;
        break;
      }
    }

    return ret;
  }

  // @Test
  public void testFindInstanceByUUID() throws MalformedURLException
  {
    // build a toolinstance
    final UUID uuid1 = UUID.randomUUID();
    buildToolInstanceEntity(uuid1, TOOL_INSTANCE_NAME_1, TOOL_INSTANCE_URL_1);

    final ToolInstance instance = toolInstanceDAO.findInstanceByUUID(uuid1);
    assertNotNull(instance);
    assertThat(instance.getUUID(), is(uuid1));
  }

  // @Test
  public void testFindInstanceByName() throws MalformedURLException
  {
    // build a toolinstance
    final UUID uuid1 = UUID.randomUUID();
    buildToolInstanceEntity(uuid1, TOOL_INSTANCE_NAME_1, TOOL_INSTANCE_URL_1);

    final ToolInstance instance = toolInstanceDAO.findInstanceByName(TOOL_INSTANCE_NAME_1);
    assertNotNull(instance);
    assertThat(instance.getName(), is(TOOL_INSTANCE_NAME_1));
  }

  // @Test
  public void testFindInstanceByHost() throws MalformedURLException
  {
    // build a toolinstance
    final UUID uuid1 = UUID.randomUUID();
    buildToolInstanceEntity(uuid1, TOOL_INSTANCE_NAME_1, TOOL_INSTANCE_URL_1);

    final ToolInstance instance = toolInstanceDAO.findInstanceByHost("host1");
    assertNotNull(instance);
    assertThat(instance.getName(), is(TOOL_INSTANCE_NAME_1));
  }

  // @Test
  public void testGetApplicationsByName() throws MalformedURLException
  {
    // build 3 instances on the same tool instance
    buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID);
    buildInstanceConfigurationEntity(INSTANCE_ID_2, TOOL_INSTANCE_ID);
    buildInstanceConfigurationEntity(INSTANCE_ID_3, TOOL_INSTANCE_ID);

    final Set<InstanceConfiguration> applications = toolInstanceDAO
        .getApplicationsByName(TOOL_INSTANCE_NAME_1);
    assertNotNull(applications);
    assertThat(applications.size(), is(3));
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

  // @Test
  public void testGetApplicationsByUUID() throws MalformedURLException
  {
    // build 3 instances on the same tool instance
    buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID);
    buildInstanceConfigurationEntity(INSTANCE_ID_2, TOOL_INSTANCE_ID);
    buildInstanceConfigurationEntity(INSTANCE_ID_3, TOOL_INSTANCE_ID);

    final Set<InstanceConfiguration> applications = toolInstanceDAO.getApplicationsByUUID(TOOL_INSTANCE_ID);
    assertNotNull(applications);
    assertThat(new Long(applications.size()), is(new Long(3)));

    // get the applications directly
    ToolInstance toolInstance = toolInstanceDAO.findInstanceByUUID(TOOL_INSTANCE_ID);
    ToolInstanceEntity entity = (ToolInstanceEntity) toolInstance;
    assertThat(new Long(entity.getApplications().size()), is(new Long(3)));
  }

  @Test
  public void testGetApplicationsByUUIDAndStatus() throws MalformedURLException
  {
    // build one instance
    buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID);

    final Set<InstanceConfiguration> applications = toolInstanceDAO.getApplicationsByUUID(TOOL_INSTANCE_ID);
    assertNotNull(applications);
    assertThat(new Long(applications.size()), is(new Long(1)));

    // get the applications directly
    ToolInstance toolInstance = toolInstanceDAO.findInstanceByUUID(TOOL_INSTANCE_ID);
    ToolInstanceEntity entity = (ToolInstanceEntity) toolInstance;
    assertThat(new Long(entity.getApplications().size()), is(new Long(1)));

    // check the status
    assertThat(toolInstance.getToolInstanceStatus(), is(ToolInstanceStatus.BUSY));
  }

  // @Test
  public void testCountApplicationsByInstance() throws MalformedURLException
  {
    // build 3 instances on the same tool instance
    buildInstanceConfigurationEntity(INSTANCE_ID_1, TOOL_INSTANCE_ID);
    buildInstanceConfigurationEntity(INSTANCE_ID_2, TOOL_INSTANCE_ID);
    buildInstanceConfigurationEntity(INSTANCE_ID_3, TOOL_INSTANCE_ID);

    final long count = toolInstanceDAO.countApplicationsByInstance(TOOL_INSTANCE_ID);
    assertThat(count, is(new Long(3)));
  }
}
