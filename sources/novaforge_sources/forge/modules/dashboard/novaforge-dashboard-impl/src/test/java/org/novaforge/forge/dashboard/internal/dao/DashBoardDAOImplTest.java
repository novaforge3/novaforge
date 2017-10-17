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
package org.novaforge.forge.dashboard.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.dashboard.entity.DashBoardEntity;
import org.novaforge.forge.dashboard.entity.TabEntity;
import org.novaforge.forge.dashboard.entity.WidgetEntity;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.DashBoard.Type;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import javax.persistence.TypedQuery;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Guillaume Lamirand
 */
public class DashBoardDAOImplTest extends JPATestCase
{

	private DashBoardDAOImpl dashboardDAOImpl;

	/**
	 * Default constructor
	 */
	public DashBoardDAOImplTest()
	{
		super("jdbc/novaforge", "dashboard.test");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		dashboardDAOImpl = new DashBoardDAOImpl();
		dashboardDAOImpl.setEntityManager(em);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@After
	public void tearDown()
	{
		super.tearDown();
		dashboardDAOImpl = null;
	}

	@Test
	public void testNewDashboard()
	{
		final DashBoard userDashboard = dashboardDAOImpl.newDashboard(Type.USER, "user");
		assertNotNull(userDashboard);
		assertThat(userDashboard.getType(), is(Type.USER));
		assertThat(userDashboard.getTypeId(), is("user"));

		final DashBoard projectDashboard = dashboardDAOImpl.newDashboard(Type.PROJECT, "project");
		assertNotNull(projectDashboard);
		assertThat(projectDashboard.getType(), is(Type.PROJECT));
		assertThat(projectDashboard.getTypeId(), is("project"));
	}

	@Test
	public void testPersist()
	{
		final DashBoard userDashboard = new DashBoardEntity(Type.USER, "user");

		em.getTransaction().begin();
		final DashBoard persist = dashboardDAOImpl.persist(userDashboard);
		em.getTransaction().commit();
		assertNotNull(persist);
		assertThat(persist.getType(), is(Type.USER));
		assertThat(persist.getTypeId(), is("user"));
		assertThat(persist.getTabs().size(), is(0));

		final TypedQuery<DashBoard> query = em.createQuery("SELECT e FROM DashBoardEntity e", DashBoard.class);
		final DashBoard singleResult = query.getSingleResult();
		assertNotNull(singleResult);
		assertThat(singleResult.getType(), is(Type.USER));
		assertThat(singleResult.getTypeId(), is("user"));
		assertThat(singleResult.getTabs().size(), is(0));
	}

	@Test
	public void testUpdate()
	{
		final DashBoard userDashboard = new DashBoardEntity(Type.USER, "user");

		em.getTransaction().begin();
		em.persist(userDashboard);
		em.getTransaction().commit();

		final TabEntity tab = buildTab();
		userDashboard.addTab(tab);
		em.getTransaction().begin();
		dashboardDAOImpl.update(userDashboard);
		em.getTransaction().commit();

		final TypedQuery<DashBoard> query = em.createQuery("SELECT e FROM DashBoardEntity e", DashBoard.class);
		final DashBoard singleResult = query.getSingleResult();
		assertNotNull(singleResult);
		assertThat(singleResult.getType(), is(Type.USER));
		assertThat(singleResult.getTypeId(), is("user"));
		assertThat(singleResult.getTabs().size(), is(1));
	}

	private TabEntity buildTab()
	{
		final TabEntity tab = new TabEntity();
		tab.setLayoutKey("test");
		tab.setName("firsttab");
		tab.setIndex(0);
		return tab;
	}

	@Test
	public void testRemove()
	{
		final DashBoard userDashboard = new DashBoardEntity(Type.USER, "user");

		em.getTransaction().begin();
		em.persist(userDashboard);
		em.getTransaction().commit();

		em.getTransaction().begin();
		dashboardDAOImpl.remove(userDashboard);
		em.getTransaction().commit();

		final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM DashBoardEntity e", Long.class);
		final Long count = query.getSingleResult();
		assertNotNull(count);
		assertThat(count, is(Long.valueOf(0)));
	}

	@Test
	public void testfindDashboardByType()
	{
		final DashBoard userDashboard = new DashBoardEntity(Type.USER, "user");
		final DashBoard user2Dashboard = new DashBoardEntity(Type.USER, "user2");
		final DashBoard projectDashboard = new DashBoardEntity(Type.PROJECT, "project");

		em.getTransaction().begin();
		em.persist(userDashboard);
		em.persist(user2Dashboard);
		em.persist(projectDashboard);
		em.getTransaction().commit();

		final DashBoard user = dashboardDAOImpl.findDashboardByType(Type.USER, "user");
		assertNotNull(user);
		assertThat(user.getType(), is(Type.USER));
		assertThat(user.getTypeId(), is("user"));
		assertThat(user.getTabs().size(), is(0));

		final DashBoard project = dashboardDAOImpl.findDashboardByType(Type.PROJECT, "project");
		assertNotNull(project);
		assertThat(project.getType(), is(Type.PROJECT));
		assertThat(project.getTypeId(), is("project"));
		assertThat(project.getTabs().size(), is(0));
	}

	@Test
	public void testNewTab()
	{
		final Tab tab = dashboardDAOImpl.newTab();
		assertNotNull(tab);
		assertNotNull(tab.getUUID());
	}

	@Test
	public void testFindTabByUUID()
	{
		final DashBoard userDashboard = new DashBoardEntity(Type.USER, "user");
		final TabEntity tab = buildTab();
		userDashboard.addTab(tab);

		em.getTransaction().begin();
		em.persist(userDashboard);
		em.getTransaction().commit();

		final UUID uuid = tab.getUUID();
		final Tab findTabByUUID = dashboardDAOImpl.findTabByUUID(uuid);

		assertNotNull(findTabByUUID);
		assertThat(findTabByUUID.getLayoutKey(), is("test"));
		assertThat(findTabByUUID.getUUID(), is(uuid));
		assertThat(findTabByUUID.getIndex(), is(0));
		assertThat(findTabByUUID.getName(), is("firsttab"));
	}

	@Test
	public void testFindTabByIndex()
	{
		final DashBoard userDashboard = new DashBoardEntity(Type.USER, "user");
		final TabEntity tab = buildTab();
		userDashboard.addTab(tab);

		em.getTransaction().begin();
		em.persist(userDashboard);
		em.getTransaction().commit();

		final UUID uuid = tab.getUUID();
		final Tab findTabByUUID = dashboardDAOImpl.findTabByIndex(0);

		assertNotNull(findTabByUUID);
		assertThat(findTabByUUID.getLayoutKey(), is("test"));
		assertThat(findTabByUUID.getIndex(), is(0));
		assertThat(findTabByUUID.getUUID(), is(uuid));
		assertThat(findTabByUUID.getName(), is("firsttab"));
	}

	@Test
	public void testNewWidget()
	{
		final Widget widget = dashboardDAOImpl.newWidget();
		assertNotNull(widget);
		assertNotNull(widget.getUUID());
	}

	@Test
	public void testFindWidgetByUUID()
	{
		final DashBoard userDashboard = new DashBoardEntity(Type.USER, "user");
		final TabEntity tab = buildTab();
		userDashboard.addTab(tab);
		final Widget widget = new WidgetEntity();
		widget.setAreaId(1);
		widget.setAreaIndex(0);
		widget.setKey("test");
		widget.setName("widget");
		tab.addWidget(widget);

		em.getTransaction().begin();
		em.persist(userDashboard);
		em.getTransaction().commit();

		final UUID uuid = widget.getUUID();
		final Widget findWidgetByUUID = dashboardDAOImpl.findWidgetByUUID(uuid);

		assertNotNull(findWidgetByUUID);
		assertThat(findWidgetByUUID.getName(), is("widget"));
		assertThat(findWidgetByUUID.getUUID(), is(uuid));
		assertThat(findWidgetByUUID.getAreaId(), is(1));
		assertThat(findWidgetByUUID.getAreaIndex(), is(0));
	}

}
