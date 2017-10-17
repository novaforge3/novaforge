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
package org.novaforge.forge.plugins.requirements.requirementmanager.internal.handlers;

import org.novaforge.forge.core.plugins.categories.requirementsmanagement.DirectoryBean;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementBean;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsManagementServiceException;
import org.novaforge.forge.core.plugins.categories.testmanagement.CoveredRequirementBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestReferenceBean;
import org.novaforge.forge.plugins.categories.beans.DirectoryBeanImpl;
import org.novaforge.forge.plugins.categories.beans.RequirementBeanImpl;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementManagerFunctionalService;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementFactoryException;
import org.novaforge.forge.tools.requirements.common.facades.RequirementFunctionalTestService;
import org.novaforge.forge.tools.requirements.common.factories.RequirementFactory;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.ITest;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sbenoist
 */
public class RequirementManagerTestHandler
{
  private RequirementManagerFunctionalService requirementManagerFunctionalService;

  private RequirementFunctionalTestService    requirementFunctionalTestService;

  private RequirementFactory                  requirementFactory;

  private RequirementManagerService           requirementManagerService;

  public void importTests(final String pProjectId, final String pCurrentUser)
      throws RequirementsManagementServiceException
  {
    try
    {
      final List<CoveredRequirementBean> testRequirements = requirementManagerFunctionalService
          .getRequirementsWithTestCoverage(pProjectId, pCurrentUser);

      if ((testRequirements != null) && (!testRequirements.isEmpty()))
      {
        for (final CoveredRequirementBean testRequirement : testRequirements)
        {
          // clear tests for last version
          requirementFunctionalTestService
              .clearTestsForLastRequirementVersion(testRequirement.getReference());

          final List<TestReferenceBean> beans = testRequirement.getTests();
          if ((beans != null) && (!beans.isEmpty()))
          {
            final Set<ITest> tests = new HashSet<ITest>();
            for (final TestReferenceBean bean : filterOnLastVersion(beans))
            {
              tests.add(toTest(bean));
            }
            final int version = Integer.valueOf(testRequirement.getVersion());
            requirementFunctionalTestService.updateTest(testRequirement.getReference(), version, tests);
          }
        }
      }
    }
    catch (final Exception e)
    {
      throw new RequirementsManagementServiceException(
          "an error occured during importing tests from testlink", e);
    }

  }

  private Set<TestReferenceBean> filterOnLastVersion(final List<TestReferenceBean> pBeans)
  {
    final Set<TestReferenceBean> filteredBeans = new HashSet<TestReferenceBean>();
    for (final TestReferenceBean bean : pBeans)
    {
      filteredBeans.add(getLastTestVersion(bean.getReference(), pBeans));
    }
    return filteredBeans;
  }

  private ITest toTest(final TestReferenceBean pBean) throws RequirementFactoryException
  {
    final ITest test = requirementFactory.buildNewFunctionalTest();
    test.setCurrentVersion(Integer.valueOf(pBean.getVersion()));
    test.setReference(pBean.getReference());
    return test;
  }

  private TestReferenceBean getLastTestVersion(final String pReference, final List<TestReferenceBean> pBeans)
  {
    TestReferenceBean lastVersionBean = null;

    for (final TestReferenceBean bean : pBeans)
    {
      if (bean.getReference().equals(pReference))
      {
        if ((lastVersionBean == null)
            || (Integer.valueOf(lastVersionBean.getVersion()) < Integer.valueOf(bean.getVersion())))
        {
          lastVersionBean = bean;
        }
      }
    }

    return lastVersionBean;
  }

  public void exportRequirements(final String pProjectId, final String pCurrentUser)
      throws RequirementsManagementServiceException
  {
    try
    {
      final List<DirectoryBean> directories = new ArrayList<DirectoryBean>();
      final IProject project = requirementManagerService.findProjectByID(pProjectId);
      final Set<IRepository> repositories = project.getRepositories();
      for (final IRepository repository : repositories)
      {
        final Set<IDirectory> roots = requirementManagerService
            .loadAllRootDirectoryTreesByRepository(repository);
        for (final IDirectory root : roots)
        {
          directories.add(toDirectoryBean(root));
        }
      }

      requirementManagerFunctionalService.updateRequirements(pProjectId, directories, pCurrentUser);
    }
    catch (final Exception e)
    {
      throw new RequirementsManagementServiceException(
          "an error occured during exporting requirements to testlink", e);
    }
  }

  private DirectoryBean toDirectoryBean(final IDirectory pDirectory)
  {
    final DirectoryBean bean = new DirectoryBeanImpl();

    bean.setDescription(pDirectory.getDescription());
    bean.setName(pDirectory.getName());
    final Set<IDirectory> directories = pDirectory.getChildrenDirectories();
    if ((directories != null) && (!directories.isEmpty()))
    {

      for (final IDirectory directory : directories)
      {
        bean.addDirectory(toDirectoryBean(directory));
      }
    }

    final Set<IRequirement> requirements = pDirectory.getRequirements();
    addRequirements(bean, requirements);

    return bean;
  }

  private void addRequirements(final DirectoryBean pBean, final Set<IRequirement> pRequirements)
  {
    if ((pRequirements != null) && (!pRequirements.isEmpty()))
    {
      for (final IRequirement requirement : pRequirements)
      {
        pBean.addRequirement(toRequirementBean(requirement));
        addRequirements(pBean, requirement.getChildren());
      }
    }
  }

  private RequirementBean toRequirementBean(final IRequirement pRequirement)
  {
    final RequirementBean bean = new RequirementBeanImpl();
    bean.setDescription(pRequirement.getDescription());
    bean.setName(pRequirement.getName());
    bean.setReference(pRequirement.getReference());
    final IRequirementVersion version = pRequirement.findLastRequirementVersion();
    if (version != null)
    {
      bean.setLastRequirementVersionId((version.getCurrentVersion()));
    }
    return bean;
  }

  public void setRequirementManagerFunctionalService(
      final RequirementManagerFunctionalService pRequirementManagerFunctionalService)
  {
    requirementManagerFunctionalService = pRequirementManagerFunctionalService;
  }

  public void setRequirementFunctionalTestService(
      final RequirementFunctionalTestService pRequirementFunctionalTestService)
  {
    requirementFunctionalTestService = pRequirementFunctionalTestService;
  }

  public void setRequirementFactory(final RequirementFactory pRequirementFactory)
  {
    requirementFactory = pRequirementFactory;
  }

  public void setRequirementManagerService(final RequirementManagerService pRequirementManagerService)
  {
    requirementManagerService = pRequirementManagerService;
  }

}
