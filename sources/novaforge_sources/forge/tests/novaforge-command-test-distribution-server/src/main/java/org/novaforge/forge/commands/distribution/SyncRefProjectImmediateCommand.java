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
package org.novaforge.forge.commands.distribution;

import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.distribution.reference.service.DiffusionService;

/**
 * This command (launched onto CENTRAL forge) will list target forges for propagation.
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "sync-immediate-ref-project",
description = "launch immediate synchonisation")
public class SyncRefProjectImmediateCommand extends OsgiCommandSupport
{

   /**
    * Reference to implementation of {@link DiffusionService}
    */
   private DiffusionService          diffusionService;

   /**
    * Reference to implementation of {@link AuthentificationService}
    */
   private AuthentificationService   authentificationService;

   private UserService                userService;

   /**
    * Reference to implementation of {@link ForgeConfigurationService}
    */
   private ForgeConfigurationService forgeConfigurationService;

   /**
    * {@inheritDoc}
    */
   @Override
   protected Object doExecute() throws Exception
   {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userService.getUser(superAdministratorLogin);

      try
      {
         if (authentificationService.checkLogin()){
            authentificationService.logout();
         }
         authentificationService.login(superAdministratorLogin, user.getPassword());
         diffusionService.propagateReferenceProject();
      }
      finally
      {
         if (authentificationService.checkLogin()){
            authentificationService.logout();
         }
      }
      System.out.println("Reference project has been propagated.");
      return null;
   }

   /**
    * Use by container to inject {@link DiffusionService}
    * 
    * @param pDiffusionService
    *          the diffusionService to set
    */

   public void setDiffusionService(final DiffusionService pDiffusionService)
   {
      diffusionService = pDiffusionService;
   }

   /**
    * Use by container to inject {@link AuthentificationService}
    * 
    * @param pAuthentificationService
    */
   public void setAuthentificationService(final AuthentificationService pAuthentificationService)
   {
      authentificationService = pAuthentificationService;
   }

   public void setUserService(UserService userService)
   {
      this.userService = userService;
   }

   /**
    * Use by container to inject {@link ForgeConfigurationService}
    * 
    * @param pForgeConfigurationService
    */
   public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
   {
      forgeConfigurationService = pForgeConfigurationService;
   }

}
