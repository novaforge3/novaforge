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
package org.novaforge.forge.tools.requirements.common.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.tools.requirements.common.entity.DirectoryEntity;
import org.novaforge.forge.tools.requirements.common.entity.ProjectEntity;
import org.novaforge.forge.tools.requirements.common.entity.RepositoryEntity;
import org.novaforge.forge.tools.requirements.common.model.ERepositoryType;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author sbenoist
 */
public class DirectoryDAOImplTest extends RequirementJPATestCase
{
  private static final String          TEST_REFERENCE       = "testRef";
  private static final String          TEST_NAME            = "testName";
  private static final String          TEST_REPOSITORY_URI  = "/test";
  private static final ERepositoryType TEST_REPOSITORY_TYPE = ERepositoryType.OBEO;
  private static final String          TEST_PROJECT_NAME    = "testProject";
  private static final String          TEST_PROJECT_ID      = "testprojectid";
  private static final String          TEST_NAME_MODIFIED   = "testRename";
  private DirectoryDAOImpl directoryDAOImpl;
  private ProjectDAOImpl   projectDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    directoryDAOImpl = new DirectoryDAOImpl();
    directoryDAOImpl.setEntityManager(em);
    projectDAOImpl = new ProjectDAOImpl();
    projectDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    directoryDAOImpl = null;
    projectDAOImpl = null;
  }

  @Test
  public void testPersist()
  {
    final DirectoryEntity entityToFound = buildDirectoryEntity();
    em.getTransaction().begin();
    directoryDAOImpl.persist(entityToFound);
    em.getTransaction().commit();
    final IDirectory entityFound = directoryDAOImpl.findDirectoryByReference(TEST_REFERENCE);
    assertNotNull(entityFound);
    assertThat(entityFound.getName(), is(TEST_NAME));

    final Set<IDirectory> directories = entityFound.getChildrenDirectories();
    assertNotNull(directories);
    assertThat(directories.size(), is(2));

    final Set<IRequirement> requirements = entityFound.getRequirements();
    assertNotNull(requirements);
    assertThat(requirements.size(), is(0));
  }

  private DirectoryEntity buildDirectoryEntity()
  {
    // persist project with repository
    buildProjectAndRepository();

    // Get the repository persisted
    final IProject project = projectDAOImpl.findProjectByID(TEST_PROJECT_ID);
    final RepositoryEntity repository = (RepositoryEntity) project.getRepository(TEST_REPOSITORY_URI);

    final DirectoryEntity directory = new DirectoryEntity();
    directory.setReference(TEST_REFERENCE);
    directory.setName(TEST_NAME);
    directory.setRepository(repository);
    fillChildrenDirectories(directory, repository);
    return directory;
  }

  private void buildProjectAndRepository()
  {
    final ProjectEntity project = new ProjectEntity();
    project.setName(TEST_PROJECT_NAME);
    project.setProjectId(TEST_PROJECT_ID);

    final RepositoryEntity repository = new RepositoryEntity();
    repository.setURI(TEST_REPOSITORY_URI);
    repository.setType(TEST_REPOSITORY_TYPE);
    project.addRepository(repository);
    em.getTransaction().begin();
    projectDAOImpl.persist(project);
    em.getTransaction().commit();
  }

  /**
   *
   */
  private void fillChildrenDirectories(final DirectoryEntity pParentDirectory,
      final RepositoryEntity pRepository)
  {
    for (int i = 0; i < 2; i++)
    {
      final DirectoryEntity directoryCh1 = new DirectoryEntity();
      directoryCh1.setReference("TestRefCh1" + i);
      directoryCh1.setName("TestNameCh1" + i);
      directoryCh1.setRepository(pRepository);
      pParentDirectory.addDirectory(directoryCh1);

      if (i == 0)
      {
        for (int j = 0; j < 10; j++)
        {
          final DirectoryEntity directoryCh2 = new DirectoryEntity();
          directoryCh2.setReference("TestRefCh2" + j);
          directoryCh2.setName("TestNameCh2" + j);
          directoryCh2.setRepository(pRepository);
          directoryCh1.addDirectory(directoryCh2);
        }
      }
    }

  }

  @Test
  public void testUpdate()
  {
    final DirectoryEntity entityToFound = buildDirectoryEntity();
    em.getTransaction().begin();
    directoryDAOImpl.persist(entityToFound);
    em.getTransaction().commit();
    IDirectory entityFound = directoryDAOImpl.findDirectoryByReference(TEST_REFERENCE);
    assertNotNull(entityFound);
    assertThat(entityFound.getName(), is(TEST_NAME));

    entityFound.setName(TEST_NAME_MODIFIED);
    em.getTransaction().begin();
    directoryDAOImpl.update(entityFound);
    em.getTransaction().commit();

    entityFound = directoryDAOImpl.findDirectoryByReference(TEST_REFERENCE);
    assertNotNull(entityFound);
    assertThat(entityFound.getName(), is(TEST_NAME_MODIFIED));

    final Set<IDirectory> directories = entityFound.getChildrenDirectories();
    assertNotNull(directories);
    assertThat(directories.size(), is(2));

    final Set<IRequirement> requirements = entityFound.getRequirements();
    assertNotNull(requirements);
    assertThat(requirements.size(), is(0));
  }

  @Test
  public void testFindAllRootDirectoriesByRepository()
  {
    final DirectoryEntity entityToFound = buildDirectoryEntity();
    em.getTransaction().begin();
    directoryDAOImpl.persist(entityToFound);
    em.getTransaction().commit();

    // Get the project
    final IProject project = projectDAOImpl.findProjectByID(TEST_PROJECT_ID);
    assertNotNull(project);
    assertThat(project.getName(), is(TEST_PROJECT_NAME));

    // Get the repository
    final Set<IRepository> repositories = project.getRepositories();
    assertNotNull(repositories);
    assertThat(repositories.size(), is(1));

    final IRepository repository = repositories.iterator().next();

    final Set<IDirectory> directories = directoryDAOImpl.findAllRootDirectoriesByRepository(repository);

    // the root directory
    assertNotNull(directories);
    assertThat(directories.size(), is(1));

    // 2 childrens
    final IDirectory rootDir = directories.iterator().next();
    final Set<IDirectory> childrenDir = rootDir.getChildrenDirectories();
    assertNotNull(childrenDir);
    assertThat(childrenDir.size(), is(2));

    for (final IDirectory directory : childrenDir)
    {
      System.out.println(directory.getName());
      if (directory.getName().equals("TestNameCh10"))
      {
        final Set<IDirectory> childrenOfChild = directory.getChildrenDirectories();
        assertNotNull(childrenOfChild);
        assertThat(childrenOfChild.size(), is(10));
      }
      else if (directory.getName().equals("TestNameCh11"))
      {
        final Set<IDirectory> childrenOfChild = directory.getChildrenDirectories();
        assertNotNull(childrenOfChild);
        assertThat(childrenOfChild.size(), is(0));
      }
    }
  }
}
