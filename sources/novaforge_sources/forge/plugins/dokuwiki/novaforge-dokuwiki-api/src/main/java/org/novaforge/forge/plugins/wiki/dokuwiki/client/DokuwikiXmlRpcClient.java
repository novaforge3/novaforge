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
package org.novaforge.forge.plugins.wiki.dokuwiki.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuWikiUser;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachment;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachmentInfo;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPage;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPageInfo;

import java.util.List;

/**
 * @author lamirang
 */
public interface DokuwikiXmlRpcClient
{

  /**
   * Gets a Connector to the Dokuwiki Application.
   * 
   * @param pBaseUrl
   *          dokuwiki url
   * @param pUsername
   *          administrator username
   * @param pPassword
   *          administrator password
   * @param pUserAgent
   *          specify a custom user agent. If null a default value will be set
   * @return a {@link XmlRpcClient } built from the parameter given
   * @throws DokuwikiClientException
   */
  XmlRpcClient getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
      throws DokuwikiClientException;

  /**
   * @param connector
   * @param pUserName
   * @return
   * @throws DokuwikiClientException
   */
  boolean hasUser(final XmlRpcClient connector, final String pUserName) throws DokuwikiClientException;

  /**
   * @param connector
   * @param pWikiusers
   * @return
   * @throws DokuwikiClientException
   */
  boolean deleteUser(final XmlRpcClient connector, final DokuWikiUser pWikiusers)
      throws DokuwikiClientException;

  /**
   * @param connector
   * @param pWikiUser
   * @return
   * @throws DokuwikiClientException
   */
  boolean updateUser(final XmlRpcClient connector, final String pUserName, final DokuWikiUser pWikiUser,
      final boolean pNotify) throws DokuwikiClientException;

  /**
   * @param connector
   * @param pWikiusers
   * @throws DokuwikiClientException
   */
  void createUser(final XmlRpcClient connector, final DokuWikiUser pWikiusers, final boolean pNotify)
      throws DokuwikiClientException;

  /**
   * @param connector
   * @param pWikiPage
   * @return
   * @throws DokuwikiClientException
   */
  boolean updatePage(final XmlRpcClient connector, final String pPageName, final String pNewName,
      final String pNewDescription) throws DokuwikiClientException;

  /**
   * @param connector
   * @param pPageName
   * @return
   * @throws DokuwikiClientException
   */
  boolean deletePage(final XmlRpcClient connector, String pPageName) throws DokuwikiClientException;

  /**
   * @param connector
   * @param pWikiPage
   * @throws DokuwikiClientException
   */
  String createPage(final XmlRpcClient connector, DokuwikiPage pWikiPage) throws DokuwikiClientException;

  /**
   * @param connector
   * @param nameSpace
   * @param isRecursive
   * @return
   * @throws DokuwikiClientException
   */
  List<DokuwikiAttachmentInfo> getAttachments(final XmlRpcClient connector, String nameSpace,
      boolean isRecursive) throws DokuwikiClientException;

  /**
   * @param connector
   * @param attachmentId
   * @return
   * @throws DokuwikiClientException
   */
  DokuwikiAttachment getAttachment(final XmlRpcClient connector, String attachmentId)
      throws DokuwikiClientException;

  /**
   * @param connector
   * @param attachment
   * @throws DokuwikiClientException
   */
  void putAttachment(final XmlRpcClient connector, DokuwikiAttachment attachment)
      throws DokuwikiClientException;

  /**
   * Deletes a file. Fails if the file is still referenced from any page in the wiki.
   * 
   * @param connector
   * @param attachmentId
   * @throws DokuwikiClientException
   */
  void deleteAttachment(final XmlRpcClient connector, String attachmentId) throws DokuwikiClientException;

  /**
   * @param connector
   * @param pScope
   * @param pUserName
   * @return
   * @throws DokuwikiClientException
   */
  boolean deleteUserPermission(final XmlRpcClient connector, final String pScope, final String pUserName)
      throws DokuwikiClientException;

  /**
   * @param connector
   * @param pScope
   * @param pUserName
   * @param pPermissionId
   * @throws DokuwikiClientException
   */
  void setUserPermissionToNameSpace(final XmlRpcClient connector, final String pScope,
      final String pUserName, final int pPermissionId) throws DokuwikiClientException;

  /**
   * @param connector
   * @param pNameSpaceName
   * @return
   * @throws DokuwikiClientException
   */
  List<DokuwikiPageInfo> getNameSpacePages(final XmlRpcClient connector, String pNameSpaceName)
      throws DokuwikiClientException;

  /**
   * @param connector
   * @param pPageId
   * @return
   * @throws DokuwikiClientException
   */
  DokuwikiPage getPageContent(final XmlRpcClient connector, String pPageId) throws DokuwikiClientException;
}
