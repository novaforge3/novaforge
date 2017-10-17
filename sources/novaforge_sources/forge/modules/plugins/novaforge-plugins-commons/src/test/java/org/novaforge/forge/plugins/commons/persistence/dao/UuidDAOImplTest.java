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
import org.novaforge.forge.core.plugins.domain.plugin.Uuid;
import org.novaforge.forge.plugins.commons.persistence.entity.UuidEntity;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author sbenoist
 */
public class UuidDAOImplTest extends JPATestCase
{

	private UuidDAOImpl uuidDAO;

	public UuidDAOImplTest()
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

		uuidDAO = new UuidDAOImpl();
		uuidDAO.setEntityManager(em);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@After
	public void tearDown()
	{
		super.tearDown();
		uuidDAO = null;
	}

	@Test
	public void testGenerateUUID()
	{
		final UUID uuid = uuidDAO.generateUUID();
		assertNotNull(uuid);
	}

	@Test
	public void testFindAll()
	{
		// build 3 Uuid's
		final UUID uuid1 = UUID.randomUUID();
		buildEntity(uuid1);

		final UUID uuid2 = UUID.randomUUID();
		buildEntity(uuid2);

		final UUID uuid3 = UUID.randomUUID();
		buildEntity(uuid3);

		// find all the built entities
		final List<Uuid> uuids = uuidDAO.findAll();
		assertNotNull(uuids);
		assertThat(uuids.size(), is(3));
		assertTrue(isIn(uuids, uuid1));
		assertTrue(isIn(uuids, uuid2));
		assertTrue(isIn(uuids, uuid3));
	}

	private UuidEntity buildEntity(final UUID pUUID)
	{
		UuidEntity uuid = null;

		if (!em.getTransaction().isActive())
		{
			em.getTransaction().begin();
		}
		uuid = new UuidEntity();
		uuid.setPluginUUID(pUUID);
		em.persist(uuid);
		em.getTransaction().commit();

		return uuid;
	}

	private boolean isIn(final List<Uuid> pList, final UUID pElement)
	{
		boolean ret = false;

		for (final Uuid t : pList)
		{
			if (t.getPluginUUID().equals(pElement))
			{
				ret = true;
				break;
			}
		}

		return ret;
	}

	@Test
	public void testCountAll()
	{
		// build 3 Uuid's
		final UUID uuid1 = UUID.randomUUID();
		buildEntity(uuid1);

		final UUID uuid2 = UUID.randomUUID();
		buildEntity(uuid2);

		final UUID uuid3 = UUID.randomUUID();
		buildEntity(uuid3);

		// count all the built entities
		final long count = uuidDAO.countAll();
		assertThat(count, is(new Long(3)));
	}
}
