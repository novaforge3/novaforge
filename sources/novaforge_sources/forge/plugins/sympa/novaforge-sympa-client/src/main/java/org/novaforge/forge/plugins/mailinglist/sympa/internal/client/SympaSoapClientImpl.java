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
package org.novaforge.forge.plugins.mailinglist.sympa.internal.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.plugins.categories.beans.MailingListBeanImpl;
import org.novaforge.forge.plugins.categories.beans.MailingListUserImpl;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapClient;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapException;
import org.novaforge.forge.plugins.mailinglist.sympa.internal.SympaConstant;
import org.novaforge.forge.plugins.mailinglist.sympa.soap.SOAPStub;
import org.novaforge.forge.plugins.mailinglist.sympa.soap.SympaSOAPLocator;
import org.novaforge.forge.plugins.mailinglist.sympa.utils.Utils;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sbenoist
 */
public class SympaSoapClientImpl implements SympaSoapClient
{
  private static final Log    LOGGER                = LogFactory.getLog(SympaSoapClientImpl.class);

  private static final String LIST_ADDRESS_PROPERTY = "listAddress";

  private static final String TEMPLATE_PROPERTY     = "template";

  private static final String DESCRIPTION_PROPERTY  = "description";

  private static final String SUBJECT_PROPERTY      = "subject";

  private static final String SUBSCRIBERS_PROPERTY  = "subscribers";

  private static final String OWNERS_PROPERTY       = "owners";

  @Override
  public SympaSoapConnector getConnector(final String pEndpoint, final String pClientAdmin, final String pClientPass,
                                         final String pDomain, final String pListmaster) throws SympaSoapException
  {
    if ((pEndpoint == null) || (pEndpoint.trim().length() == 0))
    {
      throw new SympaSoapException("Unable to get the Sympa SOAP Connector because the endpoint url is not set.");
    }

    if ((pClientAdmin == null) || (pClientAdmin.trim().length() == 0))
    {
      throw new SympaSoapException("Unable to get the Sympa SOAP Connector because the clientAdmin is not set.");
    }

    if ((pClientPass == null) || (pClientPass.trim().length() == 0))
    {
      throw new SympaSoapException("Unable to get the Sympa SOAP Connector because the clientPass is not set.");
    }

    if ((pDomain == null) || (pDomain.trim().length() == 0))
    {
      throw new SympaSoapException("Unable to get the Sympa SOAP Connector because the domain is not set.");
    }

    if ((pListmaster == null) || (pListmaster.trim().length() == 0))
    {
      throw new SympaSoapException("Unable to get the Sympa SOAP Connector because the listmaster is not set.");
    }

    SOAPStub connectBindingStub = null;
    URL      url                = null;
    try
    {
      url = new URL(pEndpoint);
      connectBindingStub = (SOAPStub) new SympaSOAPLocator().getSympaPort(url);
      connectBindingStub.setTimeout(SympaConstant.SOAP_TIMEOUT);

      return new SympaSoapConnectorImpl(connectBindingStub, pClientAdmin, pClientPass,
                                                                          pDomain, pListmaster);
    }
    catch (final MalformedURLException e)
    {
      throw new SympaSoapException(String.format("Unable to build URL object with [value=%s]", pEndpoint), e);
    }
    catch (final ServiceException e)
    {
      throw new SympaSoapException(String.format("Unable to get the Sympa SOAP Connector Binding with [URL=%s]",
                                                 url.toString()), e);
    }
  }

  @Override
  public boolean isUser(final SympaSoapConnector pSympaSoapConnector, final String pEmail)
      throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pEmail };
      ret = call(pSympaSoapConnector, "isUser", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format(
          "Unable to know if this email is user into SYMPA organization with [email=%s]", pEmail), e);
    }
    return ret;
  }

  /**
   * @inheritDoc
   */
  @Override
  public boolean createUser(final SympaSoapConnector pSympaSoapConnector, final String pEmail,
      final String pGecos, final String pPassword, final String pLang) throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pEmail, pGecos, pPassword, pLang };
      ret = call(pSympaSoapConnector, "createUser", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format(
          "Unable to create user into SYMPA organization with [email=%s, gecos=%s, lang=%s]", pEmail, pGecos,
          pLang), e);
    }
    return ret;
  }

  /**
   * @inheritDoc
   */
  @Override
  public boolean deleteUser(final SympaSoapConnector pSympaSoapConnector, final String pEmail)
      throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pEmail };
      ret = call(pSympaSoapConnector, "deleteUser", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format(
          "Unable to delete user into SYMPA organization with [email=%s]", pEmail), e);
    }
    return ret;
  }

  /**
   * @inheritDoc
   */
  @Override
  public boolean updateUser(final SympaSoapConnector pSympaSoapConnector, final String pEmail,
      final String pGecos, final String pPassword, final String pLang) throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pEmail, pGecos, pPassword, pLang };
      ret = call(pSympaSoapConnector, "updateUser", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format(
          "Unable to update user on SYMPA organization with [email=%s, gecos=%s, lang=%s]", pEmail, pGecos,
          pLang), e);
    }
    return ret;
  }

  /**
   * @inheritDoc
   */
  @Override
  public boolean updateUserEmail(final SympaSoapConnector pSympaSoapConnector, final String pOldEmail,
      final String pNewEmail) throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pOldEmail, pNewEmail };
      ret = call(pSympaSoapConnector, "updateUserEmail", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format(
          "Unable to update user's email on SYMPA organization with [old email=%s, new email=%s]", pOldEmail,
          pNewEmail), e);
    }
    return ret;
  }

  /**
   * @inheritDoc
   */
  @Override
  public boolean createTopic(final SympaSoapConnector pSympaSoapConnector, final String pName,
      final String pVisibility) throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pName, pVisibility };
      ret = call(pSympaSoapConnector, "createTopic", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format(
          "Unable to create topic into SYMPA with [name=%s, visibility=%s]", pName, pVisibility), e);
    }
    return ret;
  }

  /**
   * @inheritDoc
   */
  @Override
  public boolean deleteTopic(final SympaSoapConnector pSympaSoapConnector, final String pName)
      throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pName };
      ret = call(pSympaSoapConnector, "deleteTopic", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format("Unable to delete topic into SYMPA with [name=%s]", pName),
          e);
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existList(final SympaSoapConnector pSympaSoapConnector, final String pListName)
      throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pListName };
      ret = call(pSympaSoapConnector, "existList", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format(
          "Unable to check if listname exists into SYMPA with [name=%s]", pListName), e);
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createList(final SympaSoapConnector pSympaSoapConnector, final String pListName,
      final String pSubject, final String pTemplate, final String pDescription, final String pTopic)
      throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pListName, pSubject, pTemplate, pDescription, pTopic };
      ret = call(pSympaSoapConnector, "createList", params);
    }
    catch (final RemoteException e)
    {
      // FIXME because of the timeout of fastCGI wrapper in SYMPA, we systematically get a 500 HTTP ERROR
      if (e.getMessage().trim().equals("(500)Internal Server Error"))
      {
        ret = true;
      }
      else if (e.getMessage().trim().equals("List already exists"))
      {
        throw new SympaSoapException("List already exists", e);
      }
      else
      {
        throw new SympaSoapException(String.format("Unable to create list into SYMPA with [name=%s]",
            pListName), e);
      }
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean closeList(final SympaSoapConnector pSympaSoapConnector, final String pListName)
      throws SympaSoapException
  {
    boolean ret = false;
    try
    {
      final String[] params = { pListName };
      ret = call(pSympaSoapConnector, "closeList", params);
    }
    catch (final RemoteException e)
    {
      // FIXME because of the timeout of fastCGI wrapper in SYMPA, we systematically get a 500 HTTP ERROR
      if (e.getMessage().trim().equals("(500)Internal Server Error"))
      {
        ret = true;
      }
      else
      {
        throw new SympaSoapException(String.format("Unable to close list into SYMPA with [name=%s]",
            pListName), e);
      }
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getSubscribers(final SympaSoapConnector pSympaSoapConnector, final String pListName)
      throws SympaSoapException
  {
    try
    {
      final String[] params = { pListName };
      final String[] results = get(pSympaSoapConnector, "review", params);
      return Arrays.asList(results);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format("Unable to get subscribers in SYMPA with [listname=%s]", pListName),
                                   e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSubscriber(final SympaSoapConnector pSympaSoapConnector, final String pListName, final String pEmail)
      throws SympaSoapException
  {
    return getSubscribers(pSympaSoapConnector, pListName).contains(pEmail);
  }

  @Override
  public boolean addSubscriber(final SympaSoapConnector pSympaSoapConnector, final String pListName,
                               final String pEmail, final String pGecos, final boolean pQuietMode)
      throws SympaSoapException
  {
    boolean ret = false;
    LOGGER.info(String.format("Add subscriber with [listname=%s, email=%s]", pListName, pEmail));
    try
    {
      final String[] params = { pListName, pEmail, pGecos, String.valueOf(pQuietMode) };
      ret = call(pSympaSoapConnector, "add", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(
          String.format("Unable to create list into SYMPA with [name=%s]", pListName), e);
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> addSubscribers(final SympaSoapConnector pSympaSoapConnector, final String pListName,
      final String pSubscribers, final boolean pQuietMode) throws SympaSoapException
  {
    LOGGER.info(String.format("Add subscribers with [listname=%s, emails=%s]", pListName, pSubscribers));
    try
    {
      final String[] params = { pListName, pSubscribers, String.valueOf(pQuietMode) };
      final String[] results = get(pSympaSoapConnector, "addSubscribers", params);
      return Arrays.asList(results);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format("Unable to add subscribers in SYMPA with [listname=%s, subscribers=%s]",
                                                 pListName, pSubscribers), e);
    }
  }

  @Override
  public boolean removeSubscriber(final SympaSoapConnector pSympaSoapConnector, final String pListName,
                                  final String pEmail, final boolean pQuietMode) throws SympaSoapException
  {
    boolean ret = false;
    LOGGER.info(String.format("Remove subscriber with [listname=%s, email=%s]", pListName, pEmail));
    try
    {
      final String[] params = { pListName, pEmail, String.valueOf(pQuietMode) };
      ret = call(pSympaSoapConnector, "del", params);
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format("Unable to create list into SYMPA with [name=%s]", pListName), e);
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MailingListBean> getMailingLists(final SympaSoapConnector pSympaSoapConnector,
      final String pTopicName) throws SympaSoapException
  {
    final List<MailingListBean> mailinglists = new ArrayList<MailingListBean>();
    try
    {
      final String[] params = { pTopicName };
      final String[] results = get(pSympaSoapConnector, "lists", params);
      for (final String result : results)
      {
        mailinglists.add(toMailinglist(result.split(";")));
      }
    }
    catch (final RemoteException e)
    {
      throw new SympaSoapException(String.format(
          "Unable to get all mailing lists in SYMPA with [topicName=%s]", pTopicName), e);
    }
    return mailinglists;
  }

  private MailingListBean toMailinglist(final String[] pTab)
  {
    final MailingListBean listBean = new MailingListBeanImpl();
    listBean.setName(getProperty(LIST_ADDRESS_PROPERTY, pTab));
    listBean.setDescription(getProperty(DESCRIPTION_PROPERTY, pTab));
    listBean.setSubject(getProperty(SUBJECT_PROPERTY, pTab));
    listBean.setType(Utils.toMailingListType(getProperty(TEMPLATE_PROPERTY, pTab)));
    listBean.setOwners(toOwners(getProperty(OWNERS_PROPERTY, pTab)));
    listBean.setSubscribers(toUsers(getProperty(SUBSCRIBERS_PROPERTY, pTab)));
    return listBean;
  }

  private String getProperty(final String pProperty, final String[] pTab)
  {
    String prop = null;
    for (final String str : pTab)
    {
      if (str.startsWith(pProperty))
      {
        final String[] res = str.split("=");
        if (res.length == 2)
        {
          prop = str.split("=")[1];
        }
        break;
      }
    }
    return prop;
  }

  private List<MailingListUser> toOwners(final String pUsers)
  {
    final List<MailingListUser> users = new ArrayList<MailingListUser>();
    if (pUsers != null)
    {
      final String[] tab = pUsers.split(",");
      for (final String email : tab)
      {
        users.add(new MailingListUserImpl(email));
      }
    }
    return users;
  }

  private List<MailingListSubscriber> toUsers(final String pUsers)
  {
    final List<MailingListSubscriber> users = new ArrayList<MailingListSubscriber>();
    if (pUsers != null)
    {
      final String[] tab = pUsers.split(",");
      for (final String email : tab)
      {
        users.add(new MailingListUserImpl(email));
      }
    }
    return users;
  }

  private String[] get(final SympaSoapConnector pSympaSoapConnector, final String pMethod, final String[] pParams)
      throws RemoteException, SympaSoapException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.info(String.format("call request sympa soap for method=%s with params=%s", pMethod,
                                Arrays.toString(pParams)));
    }
    return pSympaSoapConnector.getSOAPStub().authenticateRemoteAppAndRun(pSympaSoapConnector
                                                                                           .getTrustedApplicationName(),
                                                                                       pSympaSoapConnector
                                                                                           .getTrustedApplicationPassword(),
                                                                                       pSympaSoapConnector
                                                                                           .getEnvParams(), pMethod,
                                                                                       pParams);
  }

  private boolean call(final SympaSoapConnector pSympaSoapConnector, final String pMethod,
      final String[] pParams) throws RemoteException, SympaSoapException
  {
    boolean ret = false;
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("call request sympa soap for method=%s with params=%s", pMethod,
          Arrays.toString(pParams)));
    }

    final String[] tab = pSympaSoapConnector.getSOAPStub().authenticateRemoteAppAndRun(
        pSympaSoapConnector.getTrustedApplicationName(), pSympaSoapConnector.getTrustedApplicationPassword(),
        pSympaSoapConnector.getEnvParams(), pMethod, pParams);

    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("call response sympa soap result=%s", tab[0]));
    }
    if (tab[0].equals("OK"))
    {
      ret = true;
    }

    return ret;
  }

}
