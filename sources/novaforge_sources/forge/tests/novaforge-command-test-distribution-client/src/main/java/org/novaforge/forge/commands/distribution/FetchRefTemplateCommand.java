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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;
import org.novaforge.forge.core.organization.services.TemplateService;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

/**
 * This command (launched on CENTRAL forge) will create a template for Distribution itests
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "fetch-template-ref-project", description = "Fetch a template with ID: template_itests_project")
public class FetchRefTemplateCommand extends OsgiCommandSupport
{

   private static final Log    log                             = LogFactory.getLog(FetchRefTemplateCommand.class);
   private static final String TEMPLATE_DISTRIBUTION_ITESTS_ID = "template_itests_project";
   private ForgeIdentificationService forgeIdentificationService;
   private AuthentificationService    authentificationService;
   private UserService                userService;
   private ForgeConfigurationService  forgeConfigurationService;
   private TemplateService            templateService;

   /**
    * {@inheritDoc}
    */
   @Override
   protected Object doExecute() throws Exception
   {
      try
      {
         final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
         final User user = userService.getUser(superAdministratorLogin);
         authentificationService.login(superAdministratorLogin, user.getPassword());

         forgeIdentificationService.getForgeId().toString();

         fetchForTemplateDistributionItestsProject();

         System.out.println("template with ID: " + TEMPLATE_DISTRIBUTION_ITESTS_ID + " is available.");

      }
      finally
      {
         authentificationService.logout();
      }

      return null;
   }

   /*
    * creating the template
    */
   private void fetchForTemplateDistributionItestsProject() throws Exception, ForgeConfigurationException
   {
      if (templateService.existTemplate(TEMPLATE_DISTRIBUTION_ITESTS_ID))
      {
         final Template template = templateService.getTemplate(TEMPLATE_DISTRIBUTION_ITESTS_ID);

         TemplateProjectStatus status = template.getStatus();
         if (!status.equals(template.getStatus().ENABLE))
         {
            throw new Exception("ERROR: the template status is not enable");
         }
         log.info("Template for distribution fetched");
      }
      else
      {
         throw new Exception("ERROR: the template TEMPLATE_DISTRIBUTION_ITESTS_ID does not exist.");
      }
   }

   // ----------------- setter used by the container to inject services -------------------------------

   public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
   {
      forgeIdentificationService = pForgeIdentificationService;
   }

   public void setAuthentificationService(final AuthentificationService authentificationService)
   {
      this.authentificationService = authentificationService;
   }

   public void setUserService(UserService userService)
   {
      this.userService = userService;
   }

   public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
   {
      this.forgeConfigurationService = forgeConfigurationService;
   }

   public void setTemplateService(TemplateService templateService)
   {
      this.templateService = templateService;
   }
}
