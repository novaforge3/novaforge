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
package org.novaforge.forge.commons.technical.scheduling.services;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.scheduling.TimerSchedulerService;

/**
 * Abstract base implementation of the TimerSchedulerService. The aim of this service is to update a
 * configuration file and stop/start a component that will hosts the scheduler. For instance, the component is
 * a Camel-quartz route.
 * 
 * @author rols-p, sbenoist
 */
public class DefaultTimerScheduler implements TimerSchedulerService
{
  private static final Log LOGGER = LogFactory.getLog(DefaultTimerScheduler.class);
  private boolean          active;
  private String           startHour;
  private String           startMin;
  private String           period;
  private String           routeId;
  private String           timerName;
  private Processor        processor;
  private CamelContext     camelContext;

  public void start()
  {
    LOGGER.info("start... " + this.getClass().getName());
    try
    {
      if (isActive())
      {
        addRoute();
      }
    }
    catch (final Exception e)
    {
      LOGGER.error(String.format("Error when adding the scheduling route, id= %s.", routeId), e);
    }
  }

  private void addRoute() throws Exception
  {
    // addRoute dynamically
    final RouteBuilder route = new RouteBuilder()
    {
      @Override
      public void configure() throws Exception
      {
        this.from(buildCronExpression()).id(routeId).process(processor);
      }
    };

    this.camelContext.addRoutes(route);
  }

  private String buildCronExpression()
  {
    return String.format("quartz://forgeScheduler/%s?cron=0+%s+%s/%s+*+*+?", timerName, startMin, startHour, period);
  }

  public void stop()
  {
    LOGGER.info("stopping ..." + this.getClass().getName());
    if (isActive())
    {
      try
      {
        removeRoute();
      }
      catch (final Exception e)
      {
        LOGGER.error(String.format("Error when removing the scheduling route, id= %s.", routeId), e);
      }
    }
  }

  private void removeRoute() throws Exception
  {
    final Route camelRoute = camelContext.getRoute(routeId);
    if (camelRoute == null)
    {
      LOGGER.warn(String.format("No route found with routeId=%s to stop and remove scheduler service",
          routeId));
    }
    else
    {
      final CamelContext camelContext = camelRoute.getRouteContext().getCamelContext();
      camelContext.stopRoute(routeId);
      camelContext.removeRoute(routeId);
    }
  }

  @Override
  public void disableScheduling() throws Exception
  {
    LOGGER.info("disableScheduling");
    setActive(false);
    removeRoute();
  }

  @Override
  public void configureScheduling(final String hours, final String minutes, final String period) throws Exception
  {
    LOGGER.info(String.format("configureScheduling : time=%s:%s, period=%s, scheduler class=%s", hours, minutes, period,
                              this.getClass().getName()));

    setActive(true);
    setStartHour(hours);
    setStartMin(minutes);
    setPeriod(period);

    // add new route
    removeRoute();
    addRoute();
  }

  @Override
  public boolean isActive()
  {
    return active;
  }

  public void setActive(final boolean pActive)
  {
    LOGGER.info("setActive(" + pActive + ")");
    active = pActive;
  }

  @Override
  public String getStartHour()
  {
    return startHour;
  }

  public void setStartHour(final String startHour)
  {
    LOGGER.info("startHour=" + startHour);
    this.startHour = startHour;
  }

  @Override
  public String getStartMin()
  {
    return startMin;
  }

  public void setStartMin(final String startMin)
  {
    LOGGER.info("startMin=" + startMin);
    this.startMin = startMin;
  }

  @Override
  public String getPeriod()
  {
    return period;
  }

  public void setPeriod(final String period)
  {
    LOGGER.info("period=" + period);
    this.period = period;
  }

  public void setCamelContext(final CamelContext pCamelContext)
  {
    camelContext = pCamelContext;
  }

  /**
   * @param routeId
   *          the routeId to set
   */
  public void setRouteId(final String routeId)
  {
    this.routeId = routeId;
  }

  /**
   * @param timerName
   *          the timerName to set
   */
  public void setTimerName(final String timerName)
  {
    this.timerName = timerName;
  }

  /**
   * @param processor
   *          the processor to set
   */
  public void setProcessor(final Processor processor)
  {
    this.processor = processor;
  }

}
