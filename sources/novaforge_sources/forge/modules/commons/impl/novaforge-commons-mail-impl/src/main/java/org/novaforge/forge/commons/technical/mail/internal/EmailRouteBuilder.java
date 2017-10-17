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
package org.novaforge.forge.commons.technical.mail.internal;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.mail.Constants;
import org.novaforge.forge.commons.technical.mail.model.MailData;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sbenoist
 */
public class EmailRouteBuilder extends RouteBuilder
{
  private static final Log          LOGGER = LogFactory.getLog(EmailRouteBuilder.class);

  /**
   * Reference to {@link ForgeCfgService} service injected by the
   * container
   */
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure() throws Exception
  {
    from(Constants.EMAIL_TOPIC_FULL_NAME).id(Constants.EMAIL_ROUTE_NAME).process(new Processor()
    {
      @Override
      public void process(final Exchange pExchange) throws Exception
      {
        if (LOGGER.isDebugEnabled())
        {
          LOGGER.debug("a mail message is coming from mail topic...");
        }

        // Get the mail informations
        final MailData mailData = pExchange.getIn().getBody(MailData.class);

        // fill the headers
        final Map<String, Object> headers = new HashMap<String, Object>();
        headers.put(Exchange.CONTENT_TYPE, "text/html; charset=UTF-8");
        headers.put("To", mailData.getEmailTo());
        if (mailData.getFrom() != null)
        {
          headers.put("From", mailData.getFrom());
        }
        else
        {
          headers.put("From", forgeConfigurationService.getSmtpFrom());
        }

        headers.put("Subject", mailData.getSubject());

        pExchange.getOut().setBody(mailData.getBody());
        pExchange.getOut().setHeaders(headers);
      }
    }).to(getEndpoint());
  }

  /**
   * fill the endpoint as the following template :
   * // smtp://host[:port]?password=somepwd&username=someuser
   * 
   * @return smtp endpoint
   */
  private String getEndpoint()
  {
    final StringBuilder endpoint = new StringBuilder("smtp://");
    endpoint.append(forgeConfigurationService.getSmtpHost()).append(":")
        .append(forgeConfigurationService.getSmtpPort());
    final String username = forgeConfigurationService.getSmtpUsername();
    final String password = forgeConfigurationService.getSmtpPassword();
    if ((username != null) && (username.trim().length() > 0) && (password != null)
        && (password.trim().length() > 0))
    {
      endpoint.append("?").append("password=").append(password).append("&").append("username=")
          .append(username);
    }

    LOGGER.info(String.format("build the following smtp endpoint=%s", endpoint.toString()));
    return endpoint.toString();
  }

  /**
   * Use by container to inject {@link ForgeCfgService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }
}
