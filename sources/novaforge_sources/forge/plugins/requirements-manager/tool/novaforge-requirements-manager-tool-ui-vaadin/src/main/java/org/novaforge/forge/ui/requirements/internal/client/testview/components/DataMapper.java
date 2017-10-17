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
package org.novaforge.forge.ui.requirements.internal.client.testview.components;

import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.ITest;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jeremy Casery
 */
public class DataMapper
{

  public static Set<TestRequirementDTO> convertRequirementToTestRequirement(
      final Set<IRequirement> pRequirements)
  {
    final Set<TestRequirementDTO> result = new HashSet<TestRequirementDTO>();

    for (final IRequirement requirement : pRequirements)
    {
      final IRequirementVersion requirementVersion = requirement.findLastRequirementVersion();
      final Set<ITest> tests = requirementVersion.getTests();
      if ((tests != null) && (!tests.isEmpty()))
      {
        for (final ITest test : tests)
        {
          final TestRequirementDTO currentDTO = new TestRequirementDTO();
          currentDTO.setRequirement(requirement);
          currentDTO.setRequirementVersion(requirementVersion);
          final Set<IRequirementVersion> versionsInTestForCurrentRequirement = new HashSet<IRequirementVersion>();
          final Set<IRequirementVersion> versionsInTest = test.getRequirementVersions();
          for (final IRequirementVersion versionInTest : versionsInTest)
          {
            if (versionInTest.getRequirement().equals(requirement))
            {
              versionsInTestForCurrentRequirement.add(versionInTest);
            }
          }
          currentDTO.setTest(test);
          currentDTO
              .setRequirementVersionInTest(getLatestRequirementVersion(versionsInTestForCurrentRequirement));
          if ((requirement.getChildren() != null) && !requirement.getChildren().isEmpty())
          {
            currentDTO.setChildren(convertRequirementToTestRequirement(requirement.getChildren()));
          }
          result.add(currentDTO);
        }
      }
      else
      {
        final TestRequirementDTO currentDTO = new TestRequirementDTO();
        currentDTO.setRequirement(requirement);
        currentDTO.setRequirementVersion(requirementVersion);
        if ((requirement.getChildren() != null) && !requirement.getChildren().isEmpty())
        {
          currentDTO.setChildren(convertRequirementToTestRequirement(requirement.getChildren()));
        }
        result.add(currentDTO);
      }
    }

    return result;
  }

  private static IRequirementVersion getLatestRequirementVersion(final Set<IRequirementVersion> pVersions)
  {
    IRequirementVersion version = null;
    for (final IRequirementVersion current : pVersions)
    {
      if ((version == null) || (current.getCurrentVersion() > version.getCurrentVersion()))
      {
        version = current;
      }
    }

    return version;
  }
}
