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
package org.novaforge.forge.remote.services.internal.bean;

import org.novaforge.forge.core.plugins.categories.management.IterationBean;

import java.util.Date;

/**
 * Locale implementation of IterationBean
 * 
 * @author rols-p
 */
public class IterationBeanImpl implements IterationBean
{

  private static final long serialVersionUID = -4322551920826331251L;

  /** The business identifier of the iteration */
  private String            id;

  /** The iteration number */
  private long              iterationNumber;

  /** The start date */
  private Date              startDate;

  /** The end date */
  private Date              endDate;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final String id)
  {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getIterationNumber()
  {
    return iterationNumber;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setIterationNumber(final long iterationNumber)
  {
    this.iterationNumber = iterationNumber;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStartDate(final Date startDate)
  {
    this.startDate = startDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEndDate(final Date endDate)
  {
    this.endDate = endDate;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    final IterationBeanImpl other = (IterationBeanImpl) obj;
    if (id == null)
    {
      if (other.id != null)
      {
        return false;
      }
    }
    else if (!id.equals(other.id))
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "IterationBeanImpl [id=" + id + ", iterationNumber=" + iterationNumber + ", startDate=" + startDate
               + ", endDate=" + endDate + "]";
  }

}
