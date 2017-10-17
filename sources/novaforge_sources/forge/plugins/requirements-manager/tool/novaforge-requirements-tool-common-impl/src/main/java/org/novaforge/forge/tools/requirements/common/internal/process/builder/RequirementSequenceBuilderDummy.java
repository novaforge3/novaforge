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
package org.novaforge.forge.tools.requirements.common.internal.process.builder;

import org.novaforge.forge.tool.requirements.scheduler.api.ActivityImpl;
import org.novaforge.forge.tool.requirements.scheduler.api.SequenceImpl;
import org.novaforge.forge.tools.requirements.common.internal.process.task.DummyTask;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Activity;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Sequence;
import org.novaforge.forge.tools.requirements.common.services.RequirementSequenceBuilderService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guillaume Morin
 */
public class RequirementSequenceBuilderDummy implements RequirementSequenceBuilderService
{
   @Override
   public Sequence createNewSequence(final String pProjectId, final String pCodeRepositoryPath, final String pUserId, final List<String> pItems) throws Exception
   {
      System.out.println("#===============================================================#");
      System.out.println("DUMMY builder please use that in testing mode");
      System.out.println("#===============================================================#");

      if (pItems.size() > 0)
      {
         /**
          * Values are hard coded later change to use variables injected with IPOJO for example
          */
         List<Activity> activityCol = new ArrayList<Activity>();
         Sequence sequence = new SequenceImpl(pProjectId, activityCol);

         if (pItems.contains("repositories"))
         {
            DummyTask task = new DummyTask("[task-repositories]");
            Activity activityOne = new ActivityImpl();
            activityOne.addTasks(task);
            sequence.addActivity(activityOne);
         }

         ActivityImpl activityTwo = new ActivityImpl();
         if (pItems.contains("testlink"))
         {

            DummyTask task = new DummyTask("[task-testink]");
            activityTwo.addTasks(task);

         }
         if (pItems.contains("code"))
         {

            DummyTask task = new DummyTask("[task-code]");
            activityTwo.addTasks(task);
         }

         if (activityTwo.iterator().hasNext())
         {
            sequence.addActivity(activityTwo);
         }
         return sequence;
      }
      else
      {
         throw new Exception("List items must contain at least 1 token");
      }
   }
}
