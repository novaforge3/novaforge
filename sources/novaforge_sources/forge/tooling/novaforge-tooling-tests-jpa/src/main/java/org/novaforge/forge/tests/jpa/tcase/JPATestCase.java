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
package org.novaforge.forge.tests.jpa.tcase;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.novaforge.forge.tests.jpa.naming.MemoryContextFactoryBuilder;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

/**
 * @author Guillaume Lamirand
 */
public class JPATestCase
{
  /**
   * Datasource JNDI name used to lookup into JNDI context, (ie jdbc/sample)
   */
  private final String           dataSourceName;
  /**
   * Persistence name used to retrieve persistence information into persistence.xml
   */
  private final String           persistenceUnitName;
  protected EntityManagerFactory emf;
  protected EntityManager        em;
  protected EmbeddedDataSource   ds;

  /**
   * @param pDataSourceName
   *          the datasource JNDI name used to lookup into JNDI context, (ie jdbc/sample)
   * @param pPersistenceUnitName
   *          the persistence name used to retrieve persistence information into persistence.xml
   */
  protected JPATestCase(final String pDataSourceName, final String pPersistenceUnitName)
  {
    dataSourceName = pDataSourceName;
    persistenceUnitName = pPersistenceUnitName;
  }

  /**
	 */
  @BeforeClass
  public static void setUpBeforeClass()
  {
    try
    {
      NamingManager.setInitialContextFactoryBuilder(new MemoryContextFactoryBuilder());
    }
    catch (IllegalStateException e)
    {
      // If a InitialContextFactoryBuilder has beean already set an exception is thrown
      if (!"InitialContextFactoryBuilder already set".equals(e.getMessage()))
      {
        throw new ConfigurationException(e);
      }
    }
    catch (NamingException e)
    {
      throw new ConfigurationException(e);
    }
  }

  /**
   * @see Before
   */
  @Before
  public void setUp()
  {
    ds = new EmbeddedDataSource();
    ds.setDatabaseName("memory:test");
    ds.setCreateDatabase("create");
    ds.setShutdownDatabase("true");
    try
    {
      InitialContext ic = new InitialContext();
      ic.rebind(
          String.format("osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=%s)", dataSourceName), ds);
    }
    catch (NamingException e)
    {
      throw new ConfigurationException(e);
    }

    emf = Persistence.createEntityManagerFactory(persistenceUnitName, System.getProperties());
    em = emf.createEntityManager();

  }

  /**
   * @throws SQLException
   * @throws NamingException
   *           thrown when errors occured on jndi bind
   * @see @After
   */
  @After
  public void tearDown()
  {
    if (em != null)
    {
      if (em.getTransaction().isActive())
      {
        if (em.getTransaction().getRollbackOnly())
        {
          em.getTransaction().rollback();
        }
        else
        {
          em.getTransaction().commit();
        }
      }
      dropDatabase();
      ds = null;
      em.close();
    }
    if (emf != null)
    {
      emf.close();
    }
  }

  private void dropDatabase()
  {
    ds.setCreateDatabase(null);
    ds.setConnectionAttributes("drop=true");

    try
    {
      // drop the database; not the nicest solution, but works
      ds.getConnection();
    }
    catch (SQLNonTransientConnectionException e)
    {
      // this is OK, database was dropped
    }
    catch (SQLException e)
    {
      if ("XJ004".equals(e.getSQLState()))
      {
        // attempt to drop non-existend database
        // we will ignore this error
        return;
      }
      throw new ConfigurationException(e);
    }
  }

}
