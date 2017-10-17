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
package org.novaforge.forge.ui.dashboard.internal.client.tab.content.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Guillaume Lamirand
 */
public class WidgetRefreshExecutor
{

  /**
   * Number of thread containing in the pool
   */
  private static final int                   EXECUTOR_THREAD_POOL = 10;
  /**
   * The {@link ExecutorService} to use
   */
  private final ExecutorService              executor;
  /**
   * List of {@link Future} used to retrieve task result
   */
  List<Future<WidgetRefreshCallable.Result>> futures              = new ArrayList<Future<WidgetRefreshCallable.Result>>();
  /**
   * List of {@link Callable} to submit
   */
  List<WidgetRefreshCallable>                callables            = new ArrayList<WidgetRefreshCallable>();

  public WidgetRefreshExecutor()
  {
    executor = Executors.newFixedThreadPool(EXECUTOR_THREAD_POOL);
  }

  /**
   * Add the given {@link WidgetRefreshCallable} to the queue
   * 
   * @param pWidgetRefreshCallable
   *          the {@link WidgetRefreshCallable} to add
   */
  public void addCallable(final WidgetRefreshCallable pWidgetRefreshCallable)
  {
    callables.add(pWidgetRefreshCallable);
  }

  /**
   * Submit the {@link WidgetRefreshCallable} in queue
   */
  public void submitAll()
  {
    for (final Callable<WidgetRefreshCallable.Result> callable : callables)
    {
      futures.add(executor.submit(callable));
    }
    callables.clear();
    executor.shutdown();
  }

  /**
   * Retrieve the {@link Future<WidgetRefreshCallable.Result>} done
   * 
   * @return list of {@link Future<WidgetRefreshCallable.Result>} done
   */
  public List<Future<WidgetRefreshCallable.Result>> getDoneFutures()
  {
    final List<Future<WidgetRefreshCallable.Result>> doneFutures = new ArrayList<Future<WidgetRefreshCallable.Result>>();
    for (final Future<WidgetRefreshCallable.Result> future : futures)
    {
      if (future.isDone())
      {
        doneFutures.add(future);
      }
    }
    return doneFutures;
  }

  /**
   * Retrieve the {@link Future<WidgetRefreshCallable.Result>} done
   */
  public void removeFuture(final Future<WidgetRefreshCallable.Result> pFuture)
  {
    futures.remove(pFuture);
  }

  /**
   * Retrieve the {@link Future<WidgetRefreshCallable.Result>} done
   */
  public boolean hasFutures()
  {
    return !futures.isEmpty();
  }
}
