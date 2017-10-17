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

import java.util.ArrayList;
import java.util.List;

/**
 * @author rols-p
 */
public class TemplateSynchroResult
{
  private String                forgeIP;

  private List<TemplateInError> templatesInError = new ArrayList<TemplateInError>();

  private List<String>          templatesOK      = new ArrayList<String>();

  private String                failedMsg;

  /**
    * 
    */
  public TemplateSynchroResult()
  {
    super();
  }

  /**
   * @param forgeIP
   * @param status
   */
  public TemplateSynchroResult(final String forgeIP)
  {
    this.forgeIP = forgeIP;
  }

  /**
   * @return the forgeIP
   */
  @Historizable(label = "forge")
  public String getForgeIP()
  {
    return forgeIP;
  }

  /**
   * @param forgeIP
   *          the forgeIP to set
   */
  public void setForgeIP(final String forgeIP)
  {
    this.forgeIP = forgeIP;
  }

  /**
   * @return the status
   */
  @Historizable(label = "status")
  public SynchronizationStatus getStatus()
  {
    if ((failedMsg != null) && !failedMsg.isEmpty())
    {
      return SynchronizationStatus.FAILED;
    }
    if (templatesInError.isEmpty())
    {
      return SynchronizationStatus.SUCCESS;
    }
    else
    {
      return SynchronizationStatus.SUCCESS_WITH_ERRORS;
    }
  }

  /**
   * @return the templatesInError
   */
  @Historizable(label = "templatesInError")
  public List<TemplateInError> getTemplatesInError()
  {
    return templatesInError;
  }

  /**
   * @param templatesInError
   *          the templatesInError to set
   */
  public void setTemplatesInError(final List<TemplateInError> templatesInError)
  {
    this.templatesInError = templatesInError;
  }

  /**
   * @return the templatesOK
   */
  public List<String> getTemplatesOK()
  {
    return templatesOK;
  }

  /**
   * @param templatesOK
   *          the templatesOK to set
   */
  public void setTemplatesOK(final List<String> templatesOK)
  {
    this.templatesOK = templatesOK;
  }

  /**
   * @return the failedMsg
   */
  public String getFailedMsg()
  {
    return failedMsg;
  }

  /**
   * @param failedMsg
   *          the failedMsg to set
   */
  public void setFailedMsg(final String failedMsg)
  {
    this.failedMsg = failedMsg;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "TemplateSynchroResult [forgeIP=" + forgeIP + ", templatesInError=" + templatesInError + ", templatesOK="
               + templatesOK + ", failedMsg=" + failedMsg + "]";
  }
}
