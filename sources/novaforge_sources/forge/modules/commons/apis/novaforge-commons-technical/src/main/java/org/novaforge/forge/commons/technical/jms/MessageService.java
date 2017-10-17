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
package org.novaforge.forge.commons.technical.jms;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sbenoist
 */
public interface MessageService
{
	/**
	 * This method allows to send a text message on the referenced queue
	 * 
	 * @param pQueueName
	 * @param pMessage
	 * @param pHeaders
	 *          (optional)
	 * @throws MessageServiceException
	 */
	void sendMessage(String pQueueName, String pMessage, Map<String, String> pHeaders)
	    throws MessageServiceException;

	/**
	 * This method allows to send an serializable object on the referenced queue
	 * 
	 * @param pQueueName
	 * @param pMessage
	 * @param pHeaders
	 *          (optional)
	 * @throws MessageServiceException
	 */
	void sendMessage(String pQueueName, Serializable message, Map<String, String> pHeaders)
	    throws MessageServiceException;

	/**
	 * This method allows to browse a JMS Queue without consuming the messages
	 * 
	 * @param pQueueName
	 * @throws MessageServiceException
	 */
	void browseQueue(String pQueueName) throws MessageServiceException;

	/**
	 * This method allows to publish a text message on a topic
	 * 
	 * @param pTopicName
	 * @param pMessage
	 * @throws MessageServiceException
	 */
	void publish(String pTopicName, String pTextMessage) throws MessageServiceException;

	/**
	 * This method allows to publish a serializable object on a topic
	 * 
	 * @param pTopicName
	 * @param pMessage
	 * @throws MessageServiceException
	 */
	void publish(String pTopicName, Serializable pObjectMessage) throws MessageServiceException;

	/**
	 * This method allows to publish a map (String, String) on a topic
	 * 
	 * @param pTopicName
	 * @param pMessage
	 * @throws MessageServiceException
	 */
	void publish(String pTopicName, Map<String, String> pMessage) throws MessageServiceException;
}
