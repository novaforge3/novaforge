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
import org.novaforge.forge.configuration.initialization.internal.datas.ReferentielProjectConstants;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.services.TemplateNodeService;
import org.novaforge.forge.core.organization.services.TemplateRoleService;
import org.novaforge.forge.core.organization.services.TemplateService;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This command (launched on CENTRAL forge) will create a template for Distribution itests
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "add-template-ref-project", description = "Add a template into the referential project")
public class AddRefTemplateCommand extends OsgiCommandSupport
{
   private static final Log    log                               = LogFactory.getLog(AddRefTemplateCommand.class);
   private static final String DOMAIN_NAME                       = "Domaine";
   private static final String TEMPLATE_DISTRIBUTION_ITESTS_ID   = "template_itests_project";
   private static final String TEMPLATE_DISTRIBUTION_ITESTS_NAME = "template distribution itests project";
   private static final String TEMPLATE_DISTRIBUTION_ITESTS_DESC = "template distribution itests project";
   private AuthentificationService    authentificationService;
   private UserService                userService;
   private ForgeConfigurationService  forgeConfigurationService;
   private PluginsManager             pluginsManager;
   private TemplateService            templateService;
   private TemplateNodeService        templateNodeService;
   private TemplateRoleService        templateRoleService;

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

         createTemplateDistributionItestsProject();

         System.out.println("template with ID: " + TEMPLATE_DISTRIBUTION_ITESTS_ID + " has been created.");

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
   private void createTemplateDistributionItestsProject() throws TemplateServiceException,
   ForgeConfigurationException
   {
      if (!templateService.existTemplate(TEMPLATE_DISTRIBUTION_ITESTS_ID))
      {
         final Template template = templateService.newTemplate();
         template.setTemplateId(TEMPLATE_DISTRIBUTION_ITESTS_ID);
         template.setName(TEMPLATE_DISTRIBUTION_ITESTS_NAME);
         template.setDescription(TEMPLATE_DISTRIBUTION_ITESTS_DESC);
         final Space space = templateNodeService.newSpace();
         space.setName(DOMAIN_NAME);
         templateService.createTemplate(template);
         templateNodeService.addSpace(template.getTemplateId(), space);
         addTemplateRoles(template.getTemplateId());
         addMantisTempApp(template, space);
         templateService.enableTemplate(template.getTemplateId());
         log.info("Template for distribution itests created");
      }
   }

   /*
    * adding roles to template.
    */
   private void addTemplateRoles(final String templateId) throws TemplateServiceException,
   ForgeConfigurationException
   {

      final Role roleAdmin = templateRoleService.newRole();
      roleAdmin.setName(forgeConfigurationService.getForgeAdministratorRoleName());
      templateRoleService.createSystemRole(roleAdmin, templateId);

      final Role roleDeveloppeur = templateRoleService.newRole();
      roleDeveloppeur.setName(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE);
      roleDeveloppeur.setDescription(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE_DESC);
      templateRoleService.createRole(roleDeveloppeur, templateId);
   }

   /*
    * adding mantis application with roles mapping to template.
    */
   private void addMantisTempApp(final Template template, final Space space) throws TemplateServiceException,
   ForgeConfigurationException
   {
      Map<String, String> roleMappings;
      final String templateId = template.getTemplateId();
      // MANTIS
      try
      {
         roleMappings = new HashMap<String, String>();
         roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
               ReferentielProjectConstants.BUGTRACKER_MANAGER_ROLE);
         roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
               ReferentielProjectConstants.BUGTRACKER_DEV_ROLE);

         addTemplateApplication(templateId, space.getUri(), roleMappings,
               ReferentielProjectConstants.BUGTRACKER_CATEGORY, ReferentielProjectConstants.BUGTRACKER_TYPE,
               ReferentielProjectConstants.BUGTRACKER_TYPE);
      }
      catch (final PluginManagerException e)
      {

         log.error(e);
      }
   }

   private void addTemplateApplication(final String templateId, final String spaceUri,
         final Map<String, String> roleMappings, final String appCategory, final String appType,
         final String appName) throws PluginManagerException, TemplateServiceException
         {
      final List<PluginMetadata> plugins = pluginsManager.getPluginsMetadataByCategory(appCategory);
      if ((plugins != null) && !plugins.isEmpty())
      {
         for (final PluginMetadata plugin : plugins)
         {
            if (appType.equals(plugin.getType()))
            {
               final String pluginUUID = plugin.getUUID();

               templateNodeService.addApplication(templateId, spaceUri, appName, UUID.fromString(pluginUUID),
                     roleMappings);
               log.info(String.format("Application %s created.", appType));
            }
         }
      }
      else
      {
         log.info(String.format("AddTemplateApplication: No application exists for the category %s",
               appCategory));
      }
         }

   // ----------------- setter used by the container to inject services -------------------------------

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

   public void setPluginsManager(final PluginsManager pluginsManager)
   {
      this.pluginsManager = pluginsManager;
   }

   public void setTemplateService(TemplateService templateService)
   {
      this.templateService = templateService;
   }

   public void setTemplateNodeService(TemplateNodeService templateNodeService)
   {
      this.templateNodeService = templateNodeService;
   }

   public void setTemplateRoleService(TemplateRoleService templateRoleService)
   {
      this.templateRoleService = templateRoleService;
   }
}
