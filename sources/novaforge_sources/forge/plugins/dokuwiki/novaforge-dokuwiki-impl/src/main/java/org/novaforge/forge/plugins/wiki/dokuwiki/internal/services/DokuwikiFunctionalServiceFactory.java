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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal.services;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiFunctionalService;

public class DokuwikiFunctionalServiceFactory
{
  /**
   * Reference to service implementation of {@link DokuwikiFunctionalService}
   */
  private DokuwikiFunctionalService dokuwikiPageFunctionalService;
  /**
   * Reference to service implementation of {@link DokuwikiFunctionalService}
   */
  private DokuwikiFunctionalService dokuwikiAttachmentFunctionalService;

  public DokuwikiFunctionalService getService(final String itemType)
  {
    if (StringUtils.equalsIgnoreCase(itemType, DokuwikiItemType.ATTACHMENT.name()))
    {
      return dokuwikiAttachmentFunctionalService;
    }
    else
    {
      return dokuwikiPageFunctionalService;
    }
  }

  public DokuwikiFunctionalService getPageService()
  {
    return dokuwikiPageFunctionalService;
  }

  public DokuwikiFunctionalService getAttachmentService()
  {
    return dokuwikiAttachmentFunctionalService;
  }

  /**
   * Use by container to inject {@link DokuwikiFunctionalService}
   *
   * @param pDokuwikiPageFunctionalService
   *          the dokuwikiPageFunctionalService to set
   */
  public void setDokuwikiPageFunctionalService(final DokuwikiFunctionalService pDokuwikiPageFunctionalService)
  {
    dokuwikiPageFunctionalService = pDokuwikiPageFunctionalService;
  }

  /**
   * Use by container to inject {@link DokuwikiFunctionalService}
   *
   * @param pDokuwikiAttachmentFunctionalService
   *          the dokuwikiAttachmentFunctionalService to set
   */
  public void setDokuwikiAttachmentFunctionalService(
      final DokuwikiFunctionalService pDokuwikiAttachmentFunctionalService)
  {
    dokuwikiAttachmentFunctionalService = pDokuwikiAttachmentFunctionalService;
  }

  private enum DokuwikiItemType
  {
    PAGE, ATTACHMENT
  }
}
