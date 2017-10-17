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
package org.novaforge.forge.core.plugins.categories.ecm;

import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;

import java.util.Date;
import java.util.List;

/**
 * @author lamirang
 */
public interface DocumentNodeBean extends PluginExchangeableBean
{

  /**
   * @return
   */
  String getId();

  /**
   * @param pId
   *          the id to set
   */
  void setId(String pId);

  /**
   * @return
   */
  String getName();

  /**
   * @param pName
   */
  void setName(String pName);

  /**
   * @return
   */
  String getPath();

  /**
   * @param pPath
   */
  void setPath(String pPath);

  /**
   * @return
   */
  List<DocumentNodeBean> getChildren();

  /**
   * @param pChild
   */
  void setChildren(final List<DocumentNodeBean> pChild);

  /**
   * @return the author
   */
  String getAuthor();

  /**
   * @param pAuthor
   *          the author to set
   */
  void setAuthor(String pAuthor);

  /**
   * @return the createdDate
   */
  Date getCreatedDate();

  /**
   * @param pCreatedDate
   *          the createdDate to set
   */
  void setCreatedDate(Date pCreatedDate);

  /**
   * @return the lastModified
   */
  String getLastModified();

  /**
   * @param pLastModified
   *          the lastModified to set
   */
  void setLastModified(String pLastModified);

  /**
   * @return the lastModifiedDate
   */
  Date getLastModifiedDate();

  /**
   * @param pLastModifiedDate
   *          the lastModifiedDate to set
   */
  void setLastModifiedDate(Date pLastModifiedDate);

  /**
   * @return the isFile
   */
  boolean isDocument();

  /**
   * @param pIsFile
   *          the isFile to set
   */
  void setDocument(boolean pIsFile);

  /**
   * @return the parentId
   */
  String getParentId();

  /**
   * @param parentId
   *          the parentId to set
   */
  void setParentId(String parentId);

}