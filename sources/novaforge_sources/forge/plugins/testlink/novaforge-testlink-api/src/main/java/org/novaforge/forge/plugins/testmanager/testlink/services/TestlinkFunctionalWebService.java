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
package org.novaforge.forge.plugins.testmanager.testlink.services;

import org.novaforge.forge.core.plugins.categories.commons.Attachment;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.testmanager.testlink.cxf.AttachmentAdapter;

import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * @author Gauthier Cart
 */
@WebService
public interface TestlinkFunctionalWebService
{
  /**
   * This method should be called to send a notification 'testFailed' to forge core s
   * 
   * @param pUser
   * @param pProjectId
   * @param pId
   * @param pTitle
   * @param pStatus
   * @param pTestPlan
   * @param pBuild
   * @param pPlateform
   * @param pSummary
   * @param pNotes
   * @param pAttachements
   * @return true always otherwise an exception is thrown
   * @throws PluginServiceException
   */

  boolean testFailed(String pUser, String pProjectId, String pId, String pTitle, String pStatus,
      String pTestPlan, String pBuild, String pPlateform, String pSummary, String pNotes,
      @XmlJavaTypeAdapter(value = AttachmentAdapter.class) List<Attachment> pAttachements)
      throws PluginServiceException;

}
