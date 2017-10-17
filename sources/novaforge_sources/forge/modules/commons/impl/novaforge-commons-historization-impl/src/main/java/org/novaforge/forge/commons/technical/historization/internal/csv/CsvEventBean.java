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
package org.novaforge.forge.commons.technical.historization.internal.csv;

import java.util.Date;

/**
 * @author sbenoist This class is used to convert to and from CSV format
 */
public class CsvEventBean
{
  private Date   date;

  private String type;

  private String level;

  private String actor;

  private String details;

  /**
   * @inheritDoc
   */
  public Date getDate()
  {
    return date;
  }

  /**
   * @inheritDoc
   */
  public void setDate(final Date pDate)
  {
    date = pDate;
  }

  /**
   * @inheritDoc
   */
  public String getType()
  {
    return type;
  }

  /**
   * @inheritDoc
   */
  public void setType(final String pType)
  {
    type = pType;
  }

  /**
   * @inheritDoc
   */
  public String getLevel()
  {
    return level;
  }

  /**
   * @inheritDoc
   */
  public void setLevel(final String pLevel)
  {
    level = pLevel;
  }

  /**
   * @inheritDoc
   */
  public String getActor()
  {
    return actor;
  }

  /**
   * @inheritDoc
   */
  public void setActor(final String pActor)
  {
    actor = pActor;
  }

  /**
   * @inheritDoc
   */
  public String getDetails()
  {
    return details;
  }

  /**
   * @inheritDoc
   */
  public void setDetails(final String pDetails)
  {
    details = pDetails;
  }
}
