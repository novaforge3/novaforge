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
package org.novaforge.forge.commons.technical.mail.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.jms.MessageService;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.commons.technical.mail.Constants;
import org.novaforge.forge.commons.technical.mail.MailService;
import org.novaforge.forge.commons.technical.mail.MailServiceException;
import org.novaforge.forge.commons.technical.mail.bean.MailDataImpl;
import org.novaforge.forge.commons.technical.mail.model.MailData;

/**
 * @author sbenoist
 */
public class MailServiceImpl implements MailService
{
  private static final Log LOGGER = LogFactory.getLog(MailServiceImpl.class);
  private MessageService messageService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMail(final String pEmailTo, final String pSubject, final String pBody)
      throws MailServiceException
  {
    // build the transport mail object and publish it to the notification topic
    final MailData mailData = new MailDataImpl(pEmailTo, pSubject, pBody);
    send(mailData);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMail(final String pFrom, final String pEmailTo, final String pSubject, final String pBody)
      throws MailServiceException
  {
    // build the transport mail object and publish it to the notification topic
    final MailData mailData = new MailDataImpl(pFrom, pEmailTo, pSubject, pBody);
    send(mailData);
  }

  private void send(final MailData pMailData) throws MailServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("sending an email with datas=%s", pMailData.toString()));
    }

    try
    {
      messageService.publish(Constants.EMAIL_TOPIC_SHORT_NAME, pMailData);
    }
    catch (final MessageServiceException e)
    {
      throw new MailServiceException(
          String.format(
              "an error occured during publishing the notification with [from=%s, mailTo=%s, subject=%s, body=%s]",
              pMailData.getFrom(), pMailData.getEmailTo(), pMailData.getSubject(), pMailData.getBody()), e);
    }
  }

  public void setMessageService(final MessageService pMessageService)
  {
    messageService = pMessageService;
  }

}
