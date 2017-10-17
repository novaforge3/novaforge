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
package org.novaforge.forge.plugins.bugtracker.mantis.ard.internal.client;

import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapConnector;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.MantisConnectBindingStub;

/**
 * @author lamirang
 */
public class MantisARDSoapConnectorImpl implements MantisARDSoapConnector
{

   private final String                   username;
   private final String                   password;
   private final MantisConnectBindingStub connectBindingStub;

   /**
    * @param pUsername
    * @param pPassword
    * @param pConnectBindingStub
    */
   public MantisARDSoapConnectorImpl(String pUsername, String pPassword,
         MantisConnectBindingStub pConnectBindingStub)
   {
      username = pUsername;
      password = pPassword;
      connectBindingStub = pConnectBindingStub;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getUsername()
   {
      return username;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getPassword()
   {
      return password;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public MantisConnectBindingStub getConnectBindingStub()
   {
      return connectBindingStub;
   }

}
