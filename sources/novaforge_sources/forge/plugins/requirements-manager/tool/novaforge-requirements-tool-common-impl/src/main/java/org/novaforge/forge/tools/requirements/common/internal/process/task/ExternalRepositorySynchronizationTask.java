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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tool.requirements.scheduler.api.TaskImpl;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementConnectorException;

/**
 * @author Guillaume Morin
 */

public class ExternalRepositorySynchronizationTask extends TaskImpl
{
   private static final Log               log = LogFactory.getLog(ExternalRepositorySynchronizationTask.class);

  private final ExternalRepositoryRequirementConnector fConnector;
   private final String fProjectID;
   private final String fCurrentUser;

   public ExternalRepositorySynchronizationTask(String pName, String pProjectID,
         String pCurrentUSer,
         ExternalRepositoryRequirementConnector pConnector)
   {
      super(pName);
      fProjectID = pProjectID;
      fCurrentUser = pCurrentUSer;
      fConnector = pConnector;
   }

   @Override
   public void run()
   {
      try
      {
         fConnector.synchronize(fProjectID, fCurrentUser);
      }
      catch (RequirementConnectorException e)
      {
         log.error(String.format("An error occurs during synchronization of the project %s.", fProjectID), e);
      }
   }
}
