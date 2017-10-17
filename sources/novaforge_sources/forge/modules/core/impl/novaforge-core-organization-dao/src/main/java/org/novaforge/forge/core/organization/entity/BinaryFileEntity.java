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
package org.novaforge.forge.core.organization.entity;

import org.novaforge.forge.core.organization.model.BinaryFile;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * This entity allows to store file element into database
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "BINARY_FILE")
public class BinaryFileEntity implements BinaryFile
{
  /**
   * Serial version uid for serialization
   */
  private static final long serialVersionUID = -7948544713519979629L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "mime_type", nullable = true)
  private String            mimeType;

  @Column(name = "name", nullable = false)
  private String            name;

  @Lob
  @Basic(fetch = FetchType.EAGER)
  // to define as mediumblob
  @Column(length = 16777215, nullable = false)
  private byte[]            imageFile;

  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param pId
   *          the id to set
   */
  public void setId(final Long pId)
  {
    id = pId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMimeType(final String pMimeType)
  {
    mimeType = pMimeType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMimeType()
  {
    return mimeType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFile(final byte[] pImageFile)
  {
    imageFile = pImageFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getFile()
  {
    return imageFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    this.name = pName;
  }

}
