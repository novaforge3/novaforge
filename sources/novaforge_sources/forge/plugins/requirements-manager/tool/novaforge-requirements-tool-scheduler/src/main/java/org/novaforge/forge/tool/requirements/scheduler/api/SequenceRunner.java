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
package org.novaforge.forge.tool.requirements.scheduler.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Activity;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Sequence;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SequenceRunner implements Runnable
{
   private static final Log log = LogFactory.getLog(SequenceRunner.class);
   private final Sequence fSequence;
   private final ExecutorService fExecutorService;
   private final Set<SequenceRunner> fCurrentSequence;
   private final int fSequenceCounter;

   public SequenceRunner(final Sequence pSequence, final Set<SequenceRunner> pCurrentSequence, int pSequenceCounter)
   {
      fSequence = pSequence;
      /**
       * Evaluate max parallel tasks allowed by activity
       */
      int maxPoolSize = evaluateMaxFixedPoolCurrentExecutor(pSequence);
      fExecutorService = Executors.newFixedThreadPool(maxPoolSize);
      fCurrentSequence = pCurrentSequence;
      fSequenceCounter = pSequenceCounter;
   }

   private int evaluateMaxFixedPoolCurrentExecutor(final Sequence pSequence)
   {
      int count = 1;
      for (Activity a : fSequence)
      {
         int currentCount = 0;
         for (Task t : a)
         {
            currentCount++;
         }
         if (currentCount > count)
         {
            count = currentCount;
         }
      }
      return count;
   }

   public Sequence getSequence()
   {
      return fSequence;
   }

   public int getSequenceCounter()
   {
      return fSequenceCounter;
   }

   @Override
   public void run()
   {
      try
      {
         Thread.currentThread().setName("Sequence-runner-[" + fSequenceCounter + "]" + fSequence.getSequenceID());
         log.info("Sequence runner [" + fSequenceCounter + "] start the sequence : " + fSequence.getSequenceID());

         Collection<Future<?>> listTaskRunning = new ArrayList<Future<?>>();
         for (Activity activity : fSequence)
         {
            for (Task task : activity)
            {
               try
               {
                  Future<?> taskRunning = fExecutorService.submit(task);
                  listTaskRunning.add(taskRunning);
               }
               catch (Exception e)
               {
                  log.error("An error occurs during task execution " + e.getMessage(), e);
               }
            }
            /**
             * <pre>
             * Join when all tasks contained in current activity are completed
             * We do not have limit to perform this.
             *
             * <pre>
             */
            joinOnCompletedActivity(listTaskRunning);
         }

         // prepare set callback
         // callOnCompletedEvent(fSequence.getSequenceFinishListener());
      }
      catch (Exception e)
      {
         log.error("Sequence runner [" + fSequenceCounter + "] makes a mistake " + e.getMessage(), e);
      }
      finally
      {
         /**
          * <pre>
          * Main executor remains N actives threads named here "Sequence-runner"
          * Each thread are availables to start a sequence. In other terms,
          * the scheduler can execute N sequences in parallel.
          * If an other sequence arrives, this one will be delayed in queue
          *
          * TODO : Put timer to control terminal phase (during shutdownNow)
          * to notify service consumers.
          *
          * <pre>
          */
         fExecutorService.shutdownNow();
         fCurrentSequence.remove(this);
         Thread.currentThread().setName("Sequence-runner-[pool wait activity]");
         log.info("Sequence runner [" + fSequenceCounter + "] that handle the sequence " + fSequence + " is now completed");
      }
   }

   private void joinOnCompletedActivity(final Collection<Future<?>> listTaskRunning)
   {
      for (Future<?> tr : listTaskRunning)
      {
         try
         {
            tr.get();
         }
         catch (Exception e)
         {
            log.error("An error occurs with the sequence runner [" + fSequenceCounter + "] when the task compute " + e.getMessage(), e);
         }
      }
   }

   public void shutdown()
   {
      fExecutorService.shutdownNow();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((fSequence == null) ? 0 : fSequence.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      SequenceRunner other = (SequenceRunner) obj;
      if (fSequence == null)
      {
         if (other.fSequence != null)
         {
            return false;
         }
      }
      else if (!fSequence.equals(other.fSequence))
      {
         return false;
      }
      return true;
   }

   @Override
   protected void finalize() throws Throwable
   {
      log.info("Sequence runner [" + fSequenceCounter + "] " + fSequence + " is cleaned");
      super.finalize();
   }
}
