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
package org.novaforge.forge.tools.requirements.common.internal.services;

import org.novaforge.forge.tools.requirements.common.business.rules.BusinessStatusException;
import org.novaforge.forge.tools.requirements.common.business.rules.BusinessStatusService;
import org.novaforge.forge.tools.requirements.common.model.ECodeStatus;
import org.novaforge.forge.tools.requirements.common.model.ETestStatus;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.IResourceOOCode;
import org.novaforge.forge.tools.requirements.common.model.ITest;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Guillaume Morin
 */
public class BusinessStatusServiceImpl implements BusinessStatusService
{

   @Override
   public ETestStatus evaluateTestStatus(final IRequirement pRequirement) throws BusinessStatusException
   {
      try
      {
         ETestStatus status = ETestStatus.NOT_COVERED;
         Set<IRequirementVersion> versions = new TreeSet<IRequirementVersion>(pRequirement.getRequirementVersions());
         IRequirementVersion[] versionsArray = versions.toArray(new IRequirementVersion[versions.size()]);
         for (int i = 0; i < versionsArray.length; i++)
         {
            IRequirementVersion version = versionsArray[i];
            Set<ITest> tests = version.getTests();
            if (!tests.isEmpty())
            {
               status = ETestStatus.COVERED;
               if (((i + 1) < versionsArray.length) && versionsArray[i + 1].getTests().isEmpty())
               {
                  status = ETestStatus.NOT_UPDATED;
               }
               break;
            }
         }
         return status;
      }
      catch (Exception e)
      {
         throw new BusinessStatusException("Error during the status evaluation", e);
      }
   }

   @Override
   public ECodeStatus evaluateCodeStatus(final IRequirement pRequirement) throws BusinessStatusException
   {
      try
      {
         ECodeStatus status = ECodeStatus.NO_REQUIREMENT_FOUND;
         Set<IRequirementVersion> versions = new TreeSet<IRequirementVersion>(
               pRequirement.getRequirementVersions());
         IRequirementVersion[] versionsArray = versions.toArray(new IRequirementVersion[versions.size()]);
         for (int i = 0; i < versionsArray.length; i++)
         {
            IRequirementVersion version = versionsArray[i];
            Set<IResourceOOCode> codes = version.getResourcesOOCode();
            if (!codes.isEmpty())
            {
               status = ECodeStatus.ACHIEVED;
               if (((i + 1) < versionsArray.length) && versionsArray[i + 1].getResourcesOOCode().isEmpty())
               {
                  status = ECodeStatus.WAIT_TO_PRODUCE;
               }
               break;
            }
         }
         return status;
      }
      catch (Exception e)
      {
         throw new BusinessStatusException("Error during the status evaluation", e);
      }
   }

}
