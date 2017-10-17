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
package org.novaforge.forge.distribution.reference.model;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;

/**
 * @author rols-p
 */
public class TemplateInError
{
  private String templateName;

  private String errorMessage;

  /**
    * 
    */
  public TemplateInError()
  {
  }

  /**
   * @param templateName
   * @param errorMessage
   */
  public TemplateInError(final String templateName, final String errorMessage)
  {
    this.templateName = templateName;
    this.errorMessage = errorMessage;
  }

  /**
   * @return the templateName
   */
  @Historizable(label = "template")
  public String getTemplateName()
  {
    return templateName;
  }

  /**
   * @param templateName
   *          the templateName to set
   */
  public void setTemplateName(final String templateName)
  {
    this.templateName = templateName;
  }

  /**
   * @return the errorMessage
   */
  @Historizable(label = "error")
  public String getErrorMessage()
  {
    return errorMessage;
  }

  /**
   * @param errorMessage
   *          the errorMessage to set
   */
  public void setErrorMessage(final String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((templateName == null) ? 0 : templateName.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
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
    final TemplateInError other = (TemplateInError) obj;
    if (templateName == null)
    {
      if (other.templateName != null)
      {
        return false;
      }
    }
    else if (!templateName.equals(other.templateName))
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "TemplateInError [templateName=" + templateName + ", errorMessage=" + errorMessage + "]";
  }

}
