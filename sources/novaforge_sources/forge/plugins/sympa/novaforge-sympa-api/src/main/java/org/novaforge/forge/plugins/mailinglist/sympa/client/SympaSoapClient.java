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
package org.novaforge.forge.plugins.mailinglist.sympa.client;

import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;

import java.util.List;

/**
 * @author sbenoist
 */
public interface SympaSoapClient
{

  /**
   * This method returns a connector used to communicate with sympa in SOAP protocol
   * 
   * @param pEndpoint
   * @param pClientAdmin
   * @param pClientPass
   * @param pDomain
   * @param pListmaster
   * @return SympaSoapConnector
   * @throws SympaSoapException
   */
  SympaSoapConnector getConnector(String pEndpoint, String pClientAdmin, String pClientPass, String pDomain,
      String pListmaster) throws SympaSoapException;

  /**
   * This method allows to know if the asked email is a user in SYMPA Organization
   * 
   * @param pSympaSoapConnector
   * @param pEmail
   * @return true if this email is known of SYMPA organisation or false otherwise
   * @throws SympaSoapException
   */
  boolean isUser(SympaSoapConnector pSympaSoapConnector, String pEmail) throws SympaSoapException;

  /**
   * This method allows to create a user in SYMPA Organization
   * 
   * @param pSympaSoapConnector
   * @param pEmail
   * @param pGecos
   * @param pPassword
   * @param pLang
   * @return true is the user i successfully created
   * @throws SympaSoapException
   */
  boolean createUser(SympaSoapConnector pSympaSoapConnector, String pEmail, String pGecos, String pPassword,
      String pLang) throws SympaSoapException;

  /**
   * This method allows to delete a user in SYMPA Organization. It removes also the user off the lists he's
   * subscriber.
   * 
   * @param pSympaSoapConnector
   * @param pEmail
   * @return true is the user i successfully deleted from SYMPA Organization
   * @throws SympaSoapException
   */
  boolean deleteUser(SympaSoapConnector pSympaSoapConnector, String pEmail) throws SympaSoapException;

  /**
   * This method allows to update a user on SYMPA Organization excepts for the email (Use updateUserEmail
   * instead)
   * 
   * @param pSympaSoapConnector
   * @param pEmail
   * @param pGecos
   * @param pPassword
   * @param pLang
   * @return true if OK
   * @throws SympaSoapException
   */
  boolean updateUser(SympaSoapConnector pSympaSoapConnector, String pEmail, String pGecos, String pPassword,
      String pLang) throws SympaSoapException;

  /**
   * This method allows to update a user's email on SYMPA Organization. It updates also all the lists where
   * he is subscriber
   * 
   * @param pSympaSoapConnector
   * @param pOldEmail
   * @param pNewEmail
   * @return true if OK
   * @throws SympaSoapException
   */
  boolean updateUserEmail(SympaSoapConnector pSympaSoapConnector, String pOldEmail, String pNewEmail)
      throws SympaSoapException;

  /**
   * This method allows to create a subtopic on SYMPA. It creates also category topic if it doesn't exists
   * 
   * @param pSympaSoapConnector
   * @param pName
   * @param pVisibility
   * @return true if OK
   * @throws SympaSoapException
   */
  boolean createTopic(SympaSoapConnector pSympaSoapConnector, String pName, String pVisibility)
      throws SympaSoapException;

  /**
   * This method allows to delete a subtopic on SYMPA. It delete also all the lists contained by the subtopic
   * and the category topic if it has no more subtopics
   * 
   * @param pSympaSoapConnector
   * @param pName
   * @return true if OK
   * @throws SympaSoapException
   */
  boolean deleteTopic(SympaSoapConnector pSympaSoapConnector, String pName) throws SympaSoapException;

  /**
   * This method check if the listname given is existing
   * 
   * @param pSympaSoapConnector
   * @param pListName
   * @return <code>true</code> if exist , <code>false</code> otherwise
   * @throws SympaSoapException
   */
  boolean existList(SympaSoapConnector pSympaSoapConnector, String pListName) throws SympaSoapException;

  /**
   * This method allows to create a mailing list on SYMPA
   * 
   * @param pSympaSoapConnector
   * @param pListName
   * @param pSubject
   * @param pTemplate
   * @param pDescription
   * @param pTopic
   * @return true if OK
   * @throws SympaSoapException
   */
  boolean createList(SympaSoapConnector pSympaSoapConnector, String pListName, String pSubject,
      String pTemplate, String pDescription, String pTopic) throws SympaSoapException;

  /**
   * This method allows to close a mailing list on SYMPA
   * 
   * @param pSympaSoapConnector
   * @param pListName
   * @return
   * @throws SympaSoapException
   */
  boolean closeList(SympaSoapConnector pSympaSoapConnector, String pListName) throws SympaSoapException;

  /**
   * This method alloww to return the list of subscribers (emails) for a given list
   * 
   * @param pSympaSoapConnector
   * @param pListName
   * @return List<String>
   * @throws SympaSoapException
   */
  List<String> getSubscribers(SympaSoapConnector pSympaSoapConnector, String pListName)
      throws SympaSoapException;

  /**
   * This method allows to return if an email is subscriber of a given list
   * 
   * @param pSympaSoapConnector
   * @param pListName
   * @param pEmail
   * @return boolean
   * @throws SympaSoapException
   */
  boolean isSubscriber(SympaSoapConnector pSympaSoapConnector, String pListName, String pEmail)
      throws SympaSoapException;

  /**
   * This method allows to add a subscriber to a mailing list
   * 
   * @param pSympaSoapConnector
   * @param pListName
   * @param pEmail
   * @param pGecos
   * @param pQuietMode
   * @return true if OK
   * @throws SympaSoapException
   */
  boolean addSubscriber(SympaSoapConnector pSympaSoapConnector, String pListName, String pEmail,
      String pGecos, boolean pQuietMode) throws SympaSoapException;

  /**
   * This method allows to add a list of emails to a mailing list
   * 
   * @param pSympaSoapConnector
   * @param pListName
   * @param pSubscribers
   * @param String
   *          a semicolon separated value of emails
   * @return List<String> the list of subscribers on error when adding subcription to the list
   * @throws SympaSoapException
   */
  List<String> addSubscribers(SympaSoapConnector pSympaSoapConnector, String pListName, String pSubscribers,
      boolean pQuietMode) throws SympaSoapException;

  /**
   * This method allows to remove a subscriber to a mailing list
   * 
   * @param pSympaSoapConnector
   * @param pListName
   * @param pEmail
   * @param pQuietMode
   * @return true if OK
   * @throws SympaSoapException
   */
  boolean removeSubscriber(SympaSoapConnector pSympaSoapConnector, String pListName, String pEmail,
      boolean pQuietMode) throws SympaSoapException;

  /**
   * This method allows to get all mailing lists associated to a topic
   * 
   * @param pSympaSoapConnector
   * @param pTopicName
   * @return List<MailingListBean>
   * @throws SympaSoapException
   */
  List<MailingListBean> getMailingLists(SympaSoapConnector pSympaSoapConnector, String pTopicName)
      throws SympaSoapException;
}