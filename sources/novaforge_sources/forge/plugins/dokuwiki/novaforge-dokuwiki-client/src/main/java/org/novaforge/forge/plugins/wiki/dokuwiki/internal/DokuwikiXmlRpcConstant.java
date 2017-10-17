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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal;

/**
 * Declare constants used to communication with deokuwiki instance.
 * 
 * @author lamirang
 */
public final class DokuwikiXmlRpcConstant
{
  /**
   * Declare XML-RPC method to login to dokuwiki
   */
  public static final String DOKUWIKI_METHOD_LOGIN                  = "dokuwiki.login";
  /**
   * Declare XML-RPC method to put a page on dokuwiki
   */
  public static final String DOKUWIKI_METHOD_PUT_PAGE               = "wiki.putPage";
  /**
   * Declare XML-RPC method to delete a page on dokuwiki
   */
  public static final String DOKUWIKI_METHOD_DELETE_PAGE            = "novaforge.deletePage";
  /**
   * Declare XML-RPC method to update a page on dokuwiki
   */
  public static final String DOKUWIKI_METHOD_UPDATE_PAGE            = "novaforge.updatePage";
  /**
   * Declare XML-RPC method to get all pages metadata for a given namespace
   */
  public static final String DOKUWIKI_METHOD_GET_PAGE_LIST          = "dokuwiki.getPagelist";
  /**
   * Declare XML-RPC method to get page content
   */
  public static final String DOKUWIKI_METHOD_GET_PAGE               = "wiki.getPage";
  /**
   * Declare XML-RPC method to get all attachments of dokuwiki
   */
  public static final String DOKUWIKI_METHOD_GET_ATTACHMENT_LIST    = "wiki.getAttachments";
  /**
   * Declare XML-RPC method to download an attachment from dokuwiki
   */
  public static final String DOKUWIKI_METHOD_GET_ATTACHMENT         = "wiki.getAttachment";
  /**
   * Declare XML-RPC method to put an attachment to dokuwiki
   */
  public static final String DOKUWIKI_METHOD_PUT_ATTACHMENT         = "wiki.putAttachment";
  /**
   * Declare XML-RPC method to delete an attachment from dokuwiki
   */
  public static final String DOKUWIKI_METHOD_DELETE_ATTACHMENT      = "wiki.deleteAttachment";
  /**
   * Declare XML-RPC method to set user permission on dokuwiki
   */
  public static final String DOKUWIKI_METHOD_SET_PERMISSION         = "novaforge.setUserPermission";
  /**
   * Declare XML-RPC method to delete user permission on dokuwiki
   */
  public static final String DOKUWIKI_METHOD_DELETE_PERMISSION      = "novaforge.deleteUserPermission";
  /**
   * Declare XML-RPC method to create an user on dokuwiki
   */
  public static final String DOKUWIKI_METHOD_CREATE_USER            = "novaforge.createUser";
  /**
   * Declare XML-RPC method to update an user on dokuwiki
   */
  public static final String DOKUWIKI_METHOD_UPDATE_USER            = "novaforge.updateUser";
  /**
   * Declare XML-RPC method to delete an user on dokuwiki
   */
  public static final String DOKUWIKI_METHOD_DELETE_USER            = "novaforge.deleteUser";
  /**
   * Declare summary parameter
   */
  public static final String DOKUWIKI_SUMMARY_PARAM                 = "sum";
  /**
   * Declare id parameter for a page info
   */
  public static final String DOKUWIKI_PAGE_INFO_ID                  = "id";
  /**
   * Declare revision parameter for a page info
   */
  public static final String DOKUWIKI_PAGE_INFO_REV                 = "rev";
  /**
   * Declare mtime parameter for a page info
   */
  public static final String DOKUWIKI_PAGE_INFO_MTIME               = "mtime";
  /**
   * Declare size parameter for a page info
   */
  public static final String DOKUWIKI_PAGE_INFO_SIZE                = "size";
  /**
   * Declare id parameter for an attachment info
   */
  public static final String DOKUWIKI_ATTACHMENT_INFO_ID            = "id";
  /**
   * Declare size parameter for an attachment info
   */
  public static final String DOKUWIKI_ATTACHMENT_INFO_SIZE          = "size";
  /**
   * Declare last modified parameter for an attachment info
   */
  public static final String DOKUWIKI_ATTACHMENT_INFO_LAST_MODIFIED = "lastModified";
  /**
   * Declare isImg parameter for an attachment info
   */
  public static final String DOKUWIKI_ATTACHMENT_INFO_ISIMG         = "isimg";
  /**
   * Declare writable parameter for an attachment info
   */
  public static final String DOKUWIKI_ATTACHMENT_INFO_WRITABLE      = "writable";
  /**
   * Declare perms parameter for an attachment info
   */
  public static final String DOKUWIKI_ATTACHMENT_INFO_PERM          = "perm";
  /**
   * Declare overwrite parameter when putting a new attachment
   */
  public static final String DOKUWIKI_ATTACHMENT_OW                 = "ow";
}
