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
package org.novaforge.forge.tools.requirements.common.internal.process.task;

import org.novaforge.forge.tool.requirements.scheduler.api.TaskImpl;
import org.novaforge.forge.tools.requirements.common.connectors.TestRequirementConnector;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementConnectorException;

/**
 * @author sbenoist
 */
public class TestlinkSynchronizationTask extends TaskImpl
{

   private final TestRequirementConnector fTestlinkConnector;
   private final String fProjectID;
   private final String fCurrentUser;

   public TestlinkSynchronizationTask(final String pName, final String pProjectID, final String pCurrentUSer, TestRequirementConnector pTestlinkConnector)
   {
      super(pName);
      fProjectID = pProjectID;
      fCurrentUser = pCurrentUSer;
      fTestlinkConnector = pTestlinkConnector;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void run()
   {
      try
      {
         fTestlinkConnector.exportRequirements(fProjectID, fCurrentUser);
         fTestlinkConnector.importTests(fProjectID, fCurrentUser);
      }
      catch (RequirementConnectorException e)
      {
         e.printStackTrace();
      }
   }

}
