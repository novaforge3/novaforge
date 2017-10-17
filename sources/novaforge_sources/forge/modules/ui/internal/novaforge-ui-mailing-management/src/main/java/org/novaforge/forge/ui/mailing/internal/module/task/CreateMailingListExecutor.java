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
package org.novaforge.forge.ui.mailing.internal.module.task;

import org.novaforge.forge.ui.mailing.internal.module.task.CreateMailingListCallable.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Guillaume Lamirand
 */
public class CreateMailingListExecutor
{

  /**
   * Number of thread containing in the pool
   */
  private static final int                       EXECUTOR_THREAD_POOL = 10;
  /**
   * The {@link ExecutorService} to use
   */
  private final ExecutorService                  executor;
  /**
   * List of {@link Future} used to retrieve task result
   */
  List<Future<CreateMailingListCallable.Result>> futures              = new ArrayList<Future<CreateMailingListCallable.Result>>();
  /**
   * List of {@link Callable} to submit
   */
  List<CreateMailingListCallable>                callables            = new ArrayList<CreateMailingListCallable>();

  public CreateMailingListExecutor()
  {
    executor = Executors.newFixedThreadPool(EXECUTOR_THREAD_POOL);
  }

  /**
   * Add the given {@link CreateMailingListCallable} to the queue
   * 
   * @param pMailingListCreateCallable
   *          the {@link CreateMailingListCallable} to add
   */
  public void addCallable(final CreateMailingListCallable pMailingListCreateCallable)
  {
    callables.add(pMailingListCreateCallable);
  }

  /**
   * Submit the {@link CreateMailingListCallable} in queue
   */
  public void submitAll()
  {
    for (final Callable<CreateMailingListCallable.Result> callable : callables)
    {
      futures.add(executor.submit(callable));
    }
    callables.clear();

  }

  /**
   * Shutdown the sub executor
   */
  public void shutdown()
  {
    executor.shutdown();
  }

  /**
   * Canell the {@link CreateMailingListCallable} in queue
   */
  public void cancelAll()
  {
    for (final Future<Result> future : futures)
    {
      if (!future.isDone())
      {
        future.cancel(false);
      }
    }
    futures.clear();
  }

  /**
   * Retrieve the {@link Future<MailingListCreateCallable.Result>} done
   * 
   * @return list of {@link Future<MailingListCreateCallable.Result>} done
   */
  public List<Future<CreateMailingListCallable.Result>> getDoneFutures()
  {
    final List<Future<CreateMailingListCallable.Result>> doneFutures = new ArrayList<Future<CreateMailingListCallable.Result>>();
    for (final Future<CreateMailingListCallable.Result> future : futures)
    {
      if (future.isDone())
      {
        doneFutures.add(future);
      }
    }
    return doneFutures;
  }

  /**
   * Retrieve the {@link Future<MailingListCreateCallable.Result>} done
   * 
   * @param pFuture
   *          the future to remove
   */
  public void removeFuture(final Future<CreateMailingListCallable.Result> pFuture)
  {
    futures.remove(pFuture);
  }

  /**
   * Retrieve the {@link Future<MailingListCreateCallable.Result>} done
   * 
   * @return if there is still some future in progress
   */
  public boolean hasFutures()
  {
    return !futures.isEmpty();
  }
}
