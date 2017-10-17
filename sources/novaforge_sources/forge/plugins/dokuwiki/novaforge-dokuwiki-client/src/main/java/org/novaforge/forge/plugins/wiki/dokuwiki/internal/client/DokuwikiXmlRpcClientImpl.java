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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal.client;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiClientException;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiXmlRpcClient;
import org.novaforge.forge.plugins.wiki.dokuwiki.datamapper.DokuwikiResourceBuilder;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.DokuwikiXmlRpcConstant;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuwikiAttachmentImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuwikiAttachmentInfoImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuwikiPageImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuwikiPageInfoImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuWikiUser;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachment;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachmentInfo;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPage;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPageInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used in order to instantiate new connector to dokuwiki xml-rpc server.
 * 
 * @author lamirang
 */
public class DokuwikiXmlRpcClientImpl implements DokuwikiXmlRpcClient
{
  /**
   * Reference to implemention of {@link DokuwikiResourceBuilder}
   */
  private DokuwikiResourceBuilder dokuwikiResourceBuilder;

  /**
   * {@inheritDoc}
   */
  @Override
  public XmlRpcClient getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
      throws DokuwikiClientException
  {
    final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    XmlRpcClient xmlRpcClient;
    try
    {
      config.setServerURL(new URL(pBaseUrl));
      config.setBasicUserName(pUsername);
      config.setBasicPassword(pPassword);
      config.setBasicEncoding(XmlRpcStreamConfig.UTF8_ENCODING);
    }
    catch (final MalformedURLException e)
    {
      throw new DokuwikiClientException(String.format("Unable to build server url with [baseurl=%s]",
          pBaseUrl));
    }

    xmlRpcClient = new XmlRpcClient();
    xmlRpcClient.setConfig(config);
    boolean success;
    try
    {
      final List<String> user = new ArrayList<String>();
      user.add(pUsername);
      user.add(pPassword);
      success = (Boolean) xmlRpcClient.execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_LOGIN, user);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format(
          "Unable to authentificate on server with [baseurl=%s, user=%s]", pBaseUrl, pUsername), e);
    }
    if (!success)
    {
      throw new DokuwikiClientException(String.format(
          "Authentification has failed on server with [baseurl=%s, user=%s]", pBaseUrl, pUsername));
    }
    return xmlRpcClient;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasUser(final XmlRpcClient connector, final String pUserName) throws DokuwikiClientException
  {
    boolean returnSucces;
    try
    {
      final List<String> user = new ArrayList<String>();
      user.add(pUserName);
      returnSucces = (Boolean) getXmlRpcClient(connector).execute("novaforge.hasUser", user);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to check if user [username=%s] is exsting.", pUserName),
                                        e);
    }
    return returnSucces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteUser(final XmlRpcClient connector, final DokuWikiUser wikiusers)
      throws DokuwikiClientException
  {
    try
    {
      final List<String> user = new ArrayList<String>();
      user.add(wikiusers.getUserName());
      getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_DELETE_USER, user);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to delete user on dokuwiki with [username=%s]",
                                                      wikiusers.getUserName()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateUser(final XmlRpcClient connector, final String pUserName, final DokuWikiUser pWikiUser,
                            final boolean pNotify) throws DokuwikiClientException
  {
    try
    {
      final List<Object> user = new ArrayList<Object>();
      user.add(pUserName);
      user.add(pWikiUser.getUserName());
      user.add(pWikiUser.getFisrtName());
      user.add(pWikiUser.getLastName());
      user.add(pWikiUser.getEmail());
      user.add(pWikiUser.getPassword());
      // Les groupes ne doivent pas etre modifiés à l'issu de la creation
      // du compte utilisateur.
      // pb : admin1 est seul à apartenir aux groupe Admin (groupe de dokuwiki)
      // cette méthode écrasait cette affectation.
      // user.add(pWikiUser.getGroups());
      // NOVAFORGE-411 : To avoid number of argument exception, we set a empty list
      user.add(new ArrayList<String>());

      user.add(pNotify);

      getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_UPDATE_USER, user);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to update user information on dokuwiki with [old_username=%s, new_user=%s]",
                                                      pUserName, pWikiUser.toString()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createUser(final XmlRpcClient connector, final DokuWikiUser wikiusers, final boolean pNotify)
      throws DokuwikiClientException
  {
    try
    {
      final List<Object> user = new ArrayList<Object>();
      user.add(wikiusers.getUserName());
      user.add(wikiusers.getFisrtName());
      user.add(wikiusers.getLastName());
      user.add(wikiusers.getEmail());
      user.add(wikiusers.getPassword());
      user.add(wikiusers.getGroups());
      user.add(pNotify);

      getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_CREATE_USER, user);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to add user on dokuwiki with [user=%s]",
                                                      wikiusers.toString()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updatePage(final XmlRpcClient connector, final String pPageName, final String pNewName,
      final String pNewDescription) throws DokuwikiClientException
  {

    /*
     * boolean success = false;
     * try
     * {
     * final List<String> page = new ArrayList<String>();
     * page.add(pPageName);
     * page.add(pNewName);
     * page.add(pNewDescription);
     * success = (Boolean) getXmlRpcClient(connector).execute(
     * DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_UPDATE_PAGE, page);
     * }
     * catch (final XmlRpcException e)
     * {
     * throw new DokuwikiClientException(String.format(
     * "Unable to update wiki page with [old_name=%s, new_name=%s, description=%s]", pPageName, pNewName,
     * pNewDescription), e);
     * }
     * return success;
     */
    // this is not yet implemented
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deletePage(final XmlRpcClient connector, final String pPageName) throws DokuwikiClientException
  {
    boolean success;
    try
    {
      final List<String> page = new ArrayList<String>();
      page.add(pPageName);

      success = (Boolean) getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_DELETE_PAGE, page);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to delete wiki page with [name=%s]", pPageName), e);
    }
    return success;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String createPage(final XmlRpcClient connector, final DokuwikiPage pWikiPage) throws DokuwikiClientException
  {
    String                    returnPageId = "";
    final Map<String, String> param        = new HashMap<String, String>();
    param.put(DokuwikiXmlRpcConstant.DOKUWIKI_SUMMARY_PARAM, pWikiPage.getDescription());
    try
    {
      returnPageId = (String) getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_PUT_PAGE,
                                                                 new Object[] { pWikiPage.getName(),
                                                                                pWikiPage.getContent(), param });
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String
                                            .format("Unable to create wiki page with [name=%s, content=%s, summary=%s]",
                                                    pWikiPage.getName(), pWikiPage.getContent(),
                                                    pWikiPage.getDescription()));
    }
    return returnPageId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<DokuwikiAttachmentInfo> getAttachments(final XmlRpcClient connector, final String nameSpace,
      final boolean isRecursive) throws DokuwikiClientException
  {
    final List<DokuwikiAttachmentInfo> attachments = new ArrayList<DokuwikiAttachmentInfo>();
    try
    {
      final List<Object> args = new ArrayList<Object>();
      args.add(nameSpace);
      args.add(Collections.singletonList(isRecursive));

      final Object[] result = (Object[]) getXmlRpcClient(connector).execute(
          DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_GET_ATTACHMENT_LIST, args);

      for (final Object object : result)
      {
        if (object instanceof HashMap)
        {
          final DokuwikiAttachmentInfo info = new DokuwikiAttachmentInfoImpl();
          final Map<Object, Object> element = (HashMap<Object, Object>) object;
          info.setId(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_ATTACHMENT_INFO_ID).toString());
          info.setSize(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_ATTACHMENT_INFO_SIZE).toString());
          info.setLastModified(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_ATTACHMENT_INFO_LAST_MODIFIED)
              .toString());
          info.setIsImg(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_ATTACHMENT_INFO_ISIMG).toString());
          info.setIsWritable(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_ATTACHMENT_INFO_WRITABLE).toString());
          info.setPermissions(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_ATTACHMENT_INFO_PERM).toString());
          attachments.add(info);
        }
      }
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format(
          "Unable to get namespace's attachments with [namepsace=%s]", nameSpace), e);
    }
    return attachments;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DokuwikiAttachment getAttachment(final XmlRpcClient connector, final String attachmentId)
      throws DokuwikiClientException
  {
    final DokuwikiAttachment attachment = new DokuwikiAttachmentImpl();
    try
    {
      final List<Object> args = new ArrayList<Object>();
      args.add(attachmentId);

      final Object result = getXmlRpcClient(connector).execute(
          DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_GET_ATTACHMENT, args);

      if (result != null)
      {
        attachment.setId(attachmentId);
        attachment.setContent(result.toString());
      }
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to get attachment with [id=%s]", attachmentId),
          e);
    }
    return attachment;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void putAttachment(final XmlRpcClient connector, final DokuwikiAttachment attachment)
      throws DokuwikiClientException
  {
    try
    {
      final List<Object> args = new ArrayList<Object>();
      args.add(attachment.getId());
      args.add(attachment.getContent());

      final Map<String, String> params = new HashMap<String, String>();
      params.put("ow", "true");
      args.add(params);

      getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_PUT_ATTACHMENT, args);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to put attachment with [id=%s]",
          attachment.getId()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteAttachment(final XmlRpcClient connector, final String attachmentId)
      throws DokuwikiClientException
  {
    try
    {
      final List<Object> args = new ArrayList<Object>();
      args.add(attachmentId);

      getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_DELETE_ATTACHMENT, args);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to delete attachment with [id=%s]",
          attachmentId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteUserPermission(final XmlRpcClient connector, final String pNameSpace, final String pUserName)
      throws DokuwikiClientException
  {
    boolean success;
    try
    {
      final List<Object> perm = new ArrayList<Object>();
      perm.add(dokuwikiResourceBuilder.buildStartingPage(pNameSpace));
      perm.add(pUserName);

      success = (Boolean) getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_DELETE_PERMISSION,
                                                             perm);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String
                                            .format("Unable to delete permission to namespace with [namespace=%s, username=%s]",
                                                    pNameSpace, pUserName), e);
    }
    return success;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserPermissionToNameSpace(final XmlRpcClient connector, final String pNameSpace,
                                           final String pUserName, final int pPermissionId)
      throws DokuwikiClientException
  {
    try
    {
      final List<Object> perm = new ArrayList<Object>();
      perm.add(dokuwikiResourceBuilder.buildStartingPage(pNameSpace));
      perm.add(pUserName);
      perm.add(pPermissionId);

      getXmlRpcClient(connector).execute(DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_SET_PERMISSION, perm);
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String
                                            .format("Unable to set permission to namespace with [namespace=%s, username=%s, permission=%s]",
                                                    pNameSpace, pUserName, pPermissionId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<DokuwikiPageInfo> getNameSpacePages(final XmlRpcClient connector, final String pNameSpaceName)
      throws DokuwikiClientException
  {

    final List<DokuwikiPageInfo> retList = new ArrayList<DokuwikiPageInfo>();
    try
    {
      final List<Object> args = new ArrayList<Object>();
      args.add(pNameSpaceName);
      args.add(new ArrayList<String>());

      final Object[] obj = (Object[]) getXmlRpcClient(connector).execute(
          DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_GET_PAGE_LIST, args);
      for (final Object object : obj)
      {
        if (object instanceof HashMap)
        {
          final DokuwikiPageInfo info = new DokuwikiPageInfoImpl();
          final Map<Object, Object> element = (HashMap<Object, Object>) object;
          info.setId(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_PAGE_INFO_ID).toString());
          info.setRevision(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_PAGE_INFO_REV).toString());
          info.setLastChange(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_PAGE_INFO_MTIME).toString());
          info.setSize(element.get(DokuwikiXmlRpcConstant.DOKUWIKI_PAGE_INFO_SIZE).toString());
          retList.add(info);
        }

      }
    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(String.format("Unable to get namespace's pages with [namepsace=%s]",
          pNameSpaceName), e);
    }
    return retList;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DokuwikiPage getPageContent(final XmlRpcClient connector, final String pPageId)
      throws DokuwikiClientException
  {
    final DokuwikiPage returnPage = new DokuwikiPageImpl();
    try
    {
      final List<Object> args = new ArrayList<Object>();
      args.add(pPageId);

      final String content = (String) getXmlRpcClient(connector).execute(
          DokuwikiXmlRpcConstant.DOKUWIKI_METHOD_GET_PAGE, args);
      returnPage.setName(pPageId);
      returnPage.setContent(content);
      returnPage.setDescription(content);

    }
    catch (final XmlRpcException e)
    {
      throw new DokuwikiClientException(
          String.format("Unable to get page content with [page_id=%s]", pPageId), e);
    }
    return returnPage;

  }

  /**
   * Check if a connection is enable otherwise return an exception.
   * 
   * @return XmlRpcClient instance
   * @throws DokuwikiClientException
   *           occured if any connection was active.
   */
  private XmlRpcClient getXmlRpcClient(final XmlRpcClient xmlRpcClient) throws DokuwikiClientException
  {
    if (xmlRpcClient != null)
    {
      return xmlRpcClient;
    }
    else
    {
      throw new DokuwikiClientException("Not connected to a Dokuwiki instance.");
    }
  }

  /**
   * Used by container to inject service implementation of {@link DokuwikiResourceBuilder}
   * 
   * @param pDokuwikiResourceBuilder
   *          the dokuwikiResourceBuilder to set
   */
  public void setDokuwikiResourceBuilder(final DokuwikiResourceBuilder pDokuwikiResourceBuilder)
  {
    dokuwikiResourceBuilder = pDokuwikiResourceBuilder;
  }

}