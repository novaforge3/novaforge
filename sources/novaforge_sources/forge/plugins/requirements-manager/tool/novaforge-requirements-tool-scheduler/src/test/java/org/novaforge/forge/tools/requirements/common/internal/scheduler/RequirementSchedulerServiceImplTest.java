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
package org.novaforge.forge.tools.requirements.common.internal.scheduler;

import junit.framework.TestCase;
import org.novaforge.forge.tool.requirements.scheduler.api.ActivityImpl;
import org.novaforge.forge.tool.requirements.scheduler.api.SequenceImpl;
import org.novaforge.forge.tool.requirements.scheduler.internal.services.RequirementCronJobServiceImpl;
import org.novaforge.forge.tool.requirements.scheduler.internal.services.RequirementSchedulerServiceImpl;
import org.novaforge.forge.tools.requirements.common.model.scheduling.SchedulingConfiguration;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Sequence;
import org.novaforge.forge.tools.requirements.common.services.RequirementCronJobService;
import org.novaforge.forge.tools.requirements.common.services.RequirementSchedulerService;

import java.util.ArrayList;

public class RequirementSchedulerServiceImplTest extends TestCase
{
  public void wait_testProcessSequence()
  {
    try
    {
      System.out.println("Test Process");
      final TasksTest task = new TasksTest("task-1");
      final TasksTest task2 = new TasksTest("task-2");
      final TasksTest task3 = new TasksTest("task-3");

      final ActivityImpl activity = new ActivityImpl();
      activity.addTasks(task);

      final ActivityImpl activity2 = new ActivityImpl();
      activity2.addTasks(task2);
      activity2.addTasks(task3);

      final Sequence sequence = new SequenceImpl("project1", new ArrayList());
      sequence.addActivity(activity);
      sequence.addActivity(activity2);

      final RequirementSchedulerService schedulerService = new RequirementSchedulerServiceImpl();
      schedulerService.sequenceExecuteAsynch(sequence);
      System.out.println("-- wait 5s ---");
      Thread.sleep(5000);
      try
      {
        schedulerService.sequenceExecuteAsynch(sequence);
      }
      catch (final Exception e)
      {
        System.out.println(e.getMessage());
      }
      System.out.println("--  10s ---");
      Thread.sleep(20000);
      schedulerService.sequenceExecuteAsynch(sequence);
      System.out.println("-- end 10s ---");
      Thread.sleep(10000);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
  }

  public void wait_testProcessSequence2()
  {
    try
    {
      System.out.println("Test Process");
      final TasksTest task = new TasksTest("task-A");
      final TasksTest task2 = new TasksTest("task-B");

      final ActivityImpl activity = new ActivityImpl();
      activity.addTasks(task);

      final ActivityImpl activity2 = new ActivityImpl();
      activity2.addTasks(task2);

      final Sequence sequence = new SequenceImpl("project1", new ArrayList());
      sequence.addActivity(activity);

      final Sequence sequence2 = new SequenceImpl("project2", new ArrayList());
      sequence2.addActivity(activity2);

      final RequirementSchedulerService schedulerService = new RequirementSchedulerServiceImpl();
      schedulerService.sequenceExecuteAsynch(sequence);
      schedulerService.sequenceExecuteAsynch(sequence2);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
  }

  public void testDummy()
  {

  }

  public void wait_testProcessSequence3()
  {
    try
    {
      System.out.println("== TEST CRON JOB ==");
      final RequirementCronJobService jobCronService = new RequirementCronJobServiceImpl();
      final SchedulingConfiguration config = new SchedulingConfigurationDummy();
      config.setActive(true);
      config.setLaunchHour(11);
      config.setLaunchMinute(5);
      config.setLaunchPeriod(1);
      config.setProjectId("myproject");
      config.setUserId("user-id-1");

      final SchedulingConfiguration config2 = new SchedulingConfigurationDummy();
      config2.setActive(true);
      config2.setLaunchHour(11);
      config2.setLaunchMinute(5);
      config2.setLaunchPeriod(1);
      config2.setProjectId("myproject_2");
      config2.setUserId("user-id-1");

      final CallableService service = new CallableService();
      jobCronService.addSchedulingConfig(config, service, "0/1 * * * * ?");
      jobCronService.addSchedulingConfig(config2, service, "0/1 * * * * ?");

      Thread.sleep(10000);
      jobCronService.removeSchedulingConfig(config);

      /***
       * to test
       */
      // jobCronService.addSchedulingConfig(new Scheduling, new CallableService());

      Thread.sleep(40000);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
  }

}
