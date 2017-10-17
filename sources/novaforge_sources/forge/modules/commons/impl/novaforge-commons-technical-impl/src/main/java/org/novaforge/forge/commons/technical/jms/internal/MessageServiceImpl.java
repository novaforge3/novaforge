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
package org.novaforge.forge.commons.technical.jms.internal;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.jms.MessageService;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;

/** @author sbenoist */
public class MessageServiceImpl implements MessageService
{
  private static final Log          log = LogFactory.getLog(MessageServiceImpl.class);

  // Injected by Blueprint
  private ActiveMQConnectionFactory connectionFactory;

  @Override
  public void sendMessage(final String pQueueName, final String pTextMessage, final Map<String, String> pHeaders)
      throws MessageServiceException
  {
    send(pQueueName, pTextMessage, pHeaders);
  }

  @Override
  public void sendMessage(final String pQueueName, final Serializable pMessage, final Map<String, String> pHeaders)
      throws MessageServiceException
  {
    send(pQueueName, pMessage, pHeaders);
  }

  private void send(final String pQueueName, final Object pObjectMessage, final Map<String, String> pHeaders)
      throws MessageServiceException
  {
    QueueConnection queueConnection = null;
    QueueSession queueSession = null;
    QueueSender queueSender = null;
    try
    {
      // Create queue connection
      queueConnection = connectionFactory.createQueueConnection();

      // Create session
      queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

      // create Queue
      final Queue queue = queueSession.createQueue(pQueueName);

      // Create sender
      queueSender = queueSession.createSender(queue);

      // Create and send the message
      final Message message = createMessage(queueSession, pObjectMessage, pHeaders);
      queueSender.send(message);
    }
    catch (final JMSException e)
    {
      throw new MessageServiceException("unable to send JMS message for queue:" + pQueueName, e);
    }
    finally
    {
      if (queueConnection != null)
      {
        try
        {
          queueConnection.close();
        }
        catch (final Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  private Message createMessage(final Session pSession, final Object pObject, final Map<String, String> pHeaders)
      throws JMSException
  {
    Message message = null;
    if (pObject instanceof String)
    {
      message = pSession.createTextMessage((String) pObject);
    }
    else if (pObject instanceof Serializable)
    {
      message = pSession.createObjectMessage((Serializable) pObject);
    }
    else if (pObject instanceof Map)
    {
      final Map<String, String> map = (Map) pObject;
      message = createMessageForMap(map, pSession);
    }

    // Add headers to message
    if (pHeaders != null)
    {
      for (final Map.Entry<String, String> e : pHeaders.entrySet())
      {
        message.setStringProperty(e.getKey(), e.getValue());
      }
    }

    return message;
  }

  private MapMessage createMessageForMap(final Map<String, String> map, final Session session) throws JMSException
  {
    final MapMessage message = session.createMapMessage();
    for (final Map.Entry<String, String> entry : map.entrySet())
    {
      if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof String))
      {
        throw new IllegalArgumentException("Cannot convert non-String key/value to JMS MapMessage entry");
      }
      message.setObject(entry.getKey(), entry.getValue());
    }
    return message;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void browseQueue(final String pQueueName) throws MessageServiceException
  {
    QueueConnection queueConnection = null;
    QueueSession queueSession = null;
    try
    {
      // Create queue connection
      queueConnection = connectionFactory.createQueueConnection();

      // Create session
      queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

      // Create Queue
      final Queue queue = queueSession.createQueue(pQueueName);

      // create a queue browser
      final QueueBrowser queueBrowser = queueSession.createBrowser(queue);

      // start the connection
      queueConnection.start();

      // browse the messages
      final Enumeration e = queueBrowser.getEnumeration();
      int count = 0;
      while (e.hasMoreElements())
      {
        count++;
      }

      log.info(String.format("%s message(s) is(are) waiting in the queue for being consumed", count));
      while (e.hasMoreElements())
      {
        final Message message = (Message) e.nextElement();
        log.info("JMS_MESSAGE_ID = " + message.getJMSMessageID());
      }

      queueBrowser.close();
    }
    catch (final JMSException e)
    {
      throw new MessageServiceException("unable to browse and filter JMS message for queue:" + pQueueName, e);
    }
    finally
    {
      if (queueConnection != null)
      {
        try
        {
          queueConnection.close();
        }
        catch (final Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public void publish(final String pTopicName, final String pMessage) throws MessageServiceException
  {
    publishObject(pTopicName, pMessage);
  }

  @Override
  public void publish(final String pTopicName, final Serializable pMessage) throws MessageServiceException
  {
    publishObject(pTopicName, pMessage);
  }

  @Override
  public void publish(final String pTopicName, final Map<String, String> pMessage) throws MessageServiceException
  {
    publishObject(pTopicName, pMessage);
  }

  private void publishObject(final String pTopicName, final Object pObjectMessage) throws MessageServiceException
  {
    TopicConnection topicConnection = null;
    TopicSession topicSession = null;

    try
    {
      log.info(String.format("Publish object message to %s topic", pTopicName));

      // Create Topic connection
      topicConnection = connectionFactory.createTopicConnection();

      // create Session
      topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

      // CreateTopic
      final Topic topic = topicSession.createTopic(pTopicName);

      // Create a publish
      final TopicPublisher publisher = topicSession.createPublisher(topic);

      // Create and publish the message
      final Message message = createMessage(topicSession, pObjectMessage, null);
      publisher.publish(message);
    }
    catch (final Exception e)
    {
      throw new MessageServiceException(String.format("unable to publish JMS message with [topic=%s]", pTopicName), e);
    }
    finally
    {
      if (topicConnection != null)
      {
        try
        {
          topicConnection.close();
        }
        catch (final Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  public void setConnectionFactory(final ActiveMQConnectionFactory pConnectionFactory)
  {
    connectionFactory = pConnectionFactory;
  }

}