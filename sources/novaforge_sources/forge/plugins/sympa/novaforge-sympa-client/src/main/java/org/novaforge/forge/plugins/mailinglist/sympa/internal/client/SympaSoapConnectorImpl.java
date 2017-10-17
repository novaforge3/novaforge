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
package org.novaforge.forge.plugins.mailinglist.sympa.internal.client;

import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;
import org.novaforge.forge.plugins.mailinglist.sympa.soap.SOAPStub;

/**
 * @author sbenoist
 *
 */
public class SympaSoapConnectorImpl implements SympaSoapConnector
{
   private SOAPStub bindingStub;

   private String   trustedApplicationName;

   private String   trustedApplicationPassword;

   private String   robotName;

   private String   listmaster;

   public SympaSoapConnectorImpl(final SOAPStub pBindingStub, final String pTrustedApplicationName,
         final String pTrustedApplicationPassword, final String pRobotName, final String pListmaster)
   {
      super();
      bindingStub = pBindingStub;
      trustedApplicationName = pTrustedApplicationName;
      trustedApplicationPassword = pTrustedApplicationPassword;
      robotName = pRobotName;
      listmaster = pListmaster;
   }

   @Override
   public SOAPStub getSOAPStub()
   {
      return bindingStub;
   }

   @Override
   public String getTrustedApplicationName()
   {
      return trustedApplicationName;
   }

   @Override
   public String getTrustedApplicationPassword()
   {
      return trustedApplicationPassword;
   }

   @Override
   public String getEnvParams()
   {
      return "USER_EMAIL" + "=" + listmaster + "," + "SYMPA_ROBOT" + "=" + robotName;
   }

}
