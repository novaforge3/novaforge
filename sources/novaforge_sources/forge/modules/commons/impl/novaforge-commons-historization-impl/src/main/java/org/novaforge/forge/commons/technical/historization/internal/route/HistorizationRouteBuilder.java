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
/**
 * 
 */
package org.novaforge.forge.commons.technical.historization.internal.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.novaforge.forge.commons.technical.constants.Constants;
import org.novaforge.forge.commons.technical.historization.dao.EventDAO;
import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.HistorizableEvent;
import org.novaforge.forge.commons.technical.historization.model.HistorizableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Lamirand
 */
public class HistorizationRouteBuilder extends RouteBuilder
{

  private static final Logger LOGGER             = LoggerFactory.getLogger(HistorizationRouteBuilder.class);

  private static final int    MAX_LENGTH_DETAILS = 2048;

  private EventDAO            eventDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure() throws Exception
  {
    from(Constants.HISTORIZATION_TOPIC_FULL_NAME).id(Constants.HISTORIZATION_ROUTE_NAME).process(
        new Processor()
        {

          @Override
          public void process(final Exchange pExchange) throws Exception
          {
            if (LOGGER.isDebugEnabled())
            {
              LOGGER.debug("Historization topic message received.");
            }
            final StringBuilder details = new StringBuilder();
            // Get the informations
            final HistorizableEvent historizableEvent = pExchange.getIn().getBody(HistorizableEvent.class);
            for (final HistorizableObject historizableObject : historizableEvent.getObjects())
            {
              details.append(historizableObject.getDetails());
            }
            // check the length of the string pDetails and truncate if necessary
            if (details.length() > MAX_LENGTH_DETAILS)
            {
              details.setLength(MAX_LENGTH_DETAILS - 1);
            }

            final Event event = eventDAO.createEvent(historizableEvent.getAuthor(),
                historizableEvent.getType(), historizableEvent.getLevel(), details.toString());
            eventDAO.save(event);
          }
        });
  }

  /**
   * @param pEventDAO
   *          the eventDAO to set
   */
  public void setEventDAO(final EventDAO pEventDAO)
  {
    eventDAO = pEventDAO;
  }

}
