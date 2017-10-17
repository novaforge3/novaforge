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
package org.novaforge.forge.distribution.reference.internal.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.scheduling.TimerSchedulerService;
import org.novaforge.forge.distribution.reference.client.ForgeExtractionClient;
import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;
import org.novaforge.forge.distribution.reporting.services.ForgeExtractionService;

/**
 * @author rols-p
 */
public class ForgeExtractionClientImpl implements ForgeExtractionClient
{

  private static final Log log = LogFactory.getLog(ForgeExtractionClientImpl.class);

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.LAUNCH_EXTRACTION)
  public void startExtraction(@HistorizableParam(label = "forgeUrl") final String forgeUrl)
  {

    if (log.isDebugEnabled())
    {
      log.debug(String.format("calling startExtraction service for the forge uri=%s",
                              buildForgeExtractionServiceUri(forgeUrl)));
    }
    final ForgeExtractionService extractionManager = getForgeExtractionMgr(forgeUrl);
    extractionManager.startExtraction();
  }

  private String buildForgeExtractionServiceUri(final String forgeUrl)
  {
    return String.format("%s/cxf/%s", forgeUrl, ForgeExtractionService.REF_EXTRACTION_SVC_NAME);
  }

  /**
   * {@inheritDoc}
   */
  ForgeExtractionService getForgeExtractionMgr(final String forgeUrl)
  {
    ForgeExtractionService extractionMgr = null;
    final ClassLoader theGoodOne = ClientProxyFactoryBean.class.getClassLoader();
    final ClassLoader theOldOne = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(theGoodOne);

    try
    {
      final ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
      factory.setServiceClass(ForgeExtractionService.class);
      factory.setAddress(buildForgeExtractionServiceUri(forgeUrl));
      extractionMgr = (ForgeExtractionService) factory.create();
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(theOldOne);
    }
    return extractionMgr;
  }

  @Override
  @Historization(type = EventType.CONFIGURE_EXTRACTION)
  public void configureExtractionScheduling(@HistorizableParam(label = "url") final String forgeUrl,
                                            @HistorizableParam(label = "hours") final String hours,
                                            @HistorizableParam(label = "minutes") final String minutes,
                                            @HistorizableParam(label = "period") final String period)
      throws ReferenceServiceException
  {
    if (log.isDebugEnabled())
    {
      log.debug(String.format("calling configureScheduling service for the forge uri=%s hours=%s min=%s period=%s",
                              buildForgeExtractionServiceUri(forgeUrl), hours, minutes, period));
    }
    try
    {
      final TimerSchedulerService timerSchedulerService = getTimerSchedulerService(forgeUrl);
      timerSchedulerService.configureScheduling(hours, minutes, period);
    }
    catch (final Exception e)
    {
      log.error(String.format("Can not get ForgeExtractionService for the forge=%s", forgeUrl), e);
      throw (new ReferenceServiceException(e));
    }
  }

  /**
   * {@inheritDoc}
   */
  TimerSchedulerService getTimerSchedulerService(final String forgeUrl)
  {
    TimerSchedulerService extractionMgr = null;
    final ClassLoader theGoodOne = ClientProxyFactoryBean.class.getClassLoader();
    final ClassLoader theOldOne = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(theGoodOne);

    try
    {
      final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
      factory.setServiceClass(TimerSchedulerService.class);
      factory.setAddress(buildForgeSchedulingServiceUri(forgeUrl));
      extractionMgr = (TimerSchedulerService) factory.create();
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(theOldOne);
    }
    return extractionMgr;
  }

  private String buildForgeSchedulingServiceUri(final String forgeUrl)
  {
    return String.format("%s/cxf/%s", forgeUrl, TimerSchedulerService.REF_EXTRACTION_SCHEDULER_SVC_NAME);
  }

  @Override
  @Historization(type = EventType.DISABLE_EXTRACTION)
  public void disableExtractionScheduling(@HistorizableParam(label = "url") final String forgeUrl)
      throws ReferenceServiceException
  {
    if (log.isDebugEnabled())
    {
      log.debug(String.format("calling disableScheduling service for the forge uri=%s ",
          buildForgeSchedulingServiceUri(forgeUrl)));
    }
    try
    {
      final TimerSchedulerService timerSchedulerService = getTimerSchedulerService(forgeUrl);
      timerSchedulerService.disableScheduling();
    }
    catch (final Exception e)
    {
      log.error(String.format("Can not to disable the scheduling for the forge=%s", forgeUrl), e);
      throw (new ReferenceServiceException(e));
    }
  }
}
