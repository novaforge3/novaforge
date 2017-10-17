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
package org.novaforge.forge.configuration.initialization.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.configuration.initialization.internal.datas.ReferentielProjectConstants;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.presenters.TemplateNodePresenter;
import org.novaforge.forge.core.organization.presenters.TemplatePresenter;
import org.novaforge.forge.core.organization.presenters.TemplateRolePresenter;
import org.novaforge.forge.core.organization.services.TemplateLoaderService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class TemplateLoaderServiceImpl implements TemplateLoaderService
{
  private static final String       TEMPLATE_TMA_WITH_PIC_ID    = "template_tma_with_pic";

  private static final String       TEMPLATE_TMA_WITHOUT_PIC    = "template_tma_without_pic";

  private static final String       TEMPLATE_PACKAGE_PROJECT_ID = "template_package_project";

  private static final String       TEMPLATE_BASIC_ID           = "template_basic";

  private static final String       DOMAIN_NAME                 = "Domaine";

  private static final Log          log                         = LogFactory
                                                                    .getLog(TemplateLoaderServiceImpl.class);

  /**
   * Injected Services
   */
  private TemplatePresenter         templatePresenter;
  private TemplateNodePresenter     templateNodePresenter;
  private TemplateRolePresenter     templateRolePresenter;
  private PluginsManager            pluginsManager;
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void loadTemplates() throws TemplateServiceException
  {
    try
    {
      createTemplateBasicProject();
      createTemplatePackageProject();
      createTemplateTMAProjectWithoutPIC();
      createTemplateTMAProjectWithPIC();
    }
    catch (final ForgeConfigurationException e)
    {
      throw new TemplateServiceException("Unable to retrieve configuration", e);
    }
    catch (final Exception e)
    {
      if (e instanceof TemplateServiceException)
      {
        throw (TemplateServiceException) e;
      }
      else
      {
        throw new TemplateServiceException("a technical error occurred", e);
      }
    }
  }

  private void createTemplateBasicProject() throws TemplateServiceException, ForgeConfigurationException
  {
    if (!templatePresenter.existTemplate(TEMPLATE_BASIC_ID))
    {
      final Template template = templatePresenter.newTemplate();
      template.setTemplateId(TEMPLATE_BASIC_ID);
      template.setName(ReferentielProjectConstants.TEMPLATE_BASIC_PROJECT_NAME);
      template.setDescription(ReferentielProjectConstants.TEMPLATE_BASIC_PROJECT_DESC);
      templatePresenter.createTemplate(template);
      addTemplateRoles(template.getTemplateId());
      templatePresenter.enableTemplate(template.getTemplateId());
      log.info("Template Base Phare created");
    }
  }

  private void createTemplateTMAProjectWithPIC() throws TemplateServiceException,
      ForgeConfigurationException, ForgeConfigurationException
  {
    if (!templatePresenter.existTemplate(TEMPLATE_TMA_WITH_PIC_ID))
    {
      final Template template = templatePresenter.newTemplate();
      template.setTemplateId(TEMPLATE_TMA_WITH_PIC_ID);
      template.setName(ReferentielProjectConstants.TEMPLATE_TMA_PROJECT_WITH_PIC_NAME);
      template.setDescription(ReferentielProjectConstants.TEMPLATE_TMA_PROJECT_WITH_PIC_DESC);
      final Space space = templateNodePresenter.newSpace();
      space.setName(DOMAIN_NAME);
      templatePresenter.createTemplate(template);
      templateNodePresenter.addSpace(template.getTemplateId(), space);
      addTemplateRoles(template.getTemplateId());
      addAlfrescoTempApp(template, space);
      addMantisTempApp(template, space);
      addSubversionTempApp(template, space);
      addTeslinkTempApp(template, space);
      addDokuwikiTempApp(template, space);
      addNexusTempApp(template, space);
      addJenkinsTempApp(template, space);
      addSonarTempApp(template, space);
      templatePresenter.enableTemplate(template.getTemplateId());
      log.info("Template TMA With PIC created");
    }
  }

  private void createTemplateTMAProjectWithoutPIC() throws TemplateServiceException,
      ForgeConfigurationException
  {
    if (!templatePresenter.existTemplate(TEMPLATE_TMA_WITHOUT_PIC))
    {
      final Template template = templatePresenter.newTemplate();
      template.setTemplateId(TEMPLATE_TMA_WITHOUT_PIC);
      template.setName(ReferentielProjectConstants.TEMPLATE_TMA_PROJECT_WITHOUT_PIC_NAME);
      template.setDescription(ReferentielProjectConstants.TEMPLATE_TMA_PROJECT_WITHOUT_PIC_DESC);
      final Space space = templateNodePresenter.newSpace();
      space.setName(DOMAIN_NAME);
      templatePresenter.createTemplate(template);
      templateNodePresenter.addSpace(template.getTemplateId(), space);
      addTemplateRoles(template.getTemplateId());
      addAlfrescoTempApp(template, space);
      addMantisTempApp(template, space);
      addSubversionTempApp(template, space);
      addTeslinkTempApp(template, space);
      addDokuwikiTempApp(template, space);
      addNexusTempApp(template, space);
      templatePresenter.enableTemplate(template.getTemplateId());
      log.info("Template TMA Without PIC created");
    }
  }

  private void createTemplatePackageProject() throws TemplateServiceException, ForgeConfigurationException
  {
    if (!templatePresenter.existTemplate(TEMPLATE_PACKAGE_PROJECT_ID))
    {
      final Template template = templatePresenter.newTemplate();
      template.setTemplateId(TEMPLATE_PACKAGE_PROJECT_ID);
      template.setName(ReferentielProjectConstants.TEMPLATE_PACKAGE_PROJECT_NAME);
      template.setDescription(ReferentielProjectConstants.TEMPLATE_PACKAGE_PROJECT_DESC);
      final Space space = templateNodePresenter.newSpace();
      space.setName(DOMAIN_NAME);
      templatePresenter.createTemplate(template);
      templateNodePresenter.addSpace(template.getTemplateId(), space);
      addTemplateRoles(template.getTemplateId());
      addAlfrescoTempApp(template, space);
      addMantisTempApp(template, space);
      addSubversionTempApp(template, space);
      addTeslinkTempApp(template, space);
      addLimesurveyTempApp(template, space);
      addDokuwikiTempApp(template, space);
      addSpipTempApp(template, space);
      addNexusTempApp(template, space);
      addPhpbbTempApp(template, space);
      addJenkinsTempApp(template, space);
      addSonarTempApp(template, space);
      templatePresenter.enableTemplate(template.getTemplateId());
      log.info("Template Forfait created");
    }
  }

  private void addSonarTempApp(final Template template, final Space space) throws TemplateServiceException,
      ForgeConfigurationException, ForgeConfigurationException

  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    // SONAR
    try
    {
      roleMappings = new HashMap<String, String>();
      roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
          ReferentielProjectConstants.QUALIMETRY_ADMIN_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.QUALIMETRY_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.QUALIMETRY_ADMIN_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
          ReferentielProjectConstants.QUALIMETRY_WRITE_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_READ_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
          ReferentielProjectConstants.QUALIMETRY_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
          ReferentielProjectConstants.QUALIMETRY_ADMIN_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
          ReferentielProjectConstants.QUALIMETRY_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.QUALIMETRY_READ_ROLE);
      }
      addTemplateApplication(template.getTemplateId(), space.getUri(), roleMappings,
          ReferentielProjectConstants.QUALIMETRY_CATEGORY, ReferentielProjectConstants.QUALIMETRY_TYPE,
          ReferentielProjectConstants.QUALIMETRY_TYPE);
    }
    catch (final PluginManagerException e)
    {

      log.error(e);
    }
  }

  private void addJenkinsTempApp(final Template template, final Space space) throws TemplateServiceException,
      ForgeConfigurationException, ForgeConfigurationException

  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    // JENKINS
    try
    {
      roleMappings = new HashMap<String, String>();
      roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
          ReferentielProjectConstants.BUILDING_ADMIN_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.BUILDING_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
          ReferentielProjectConstants.BUILDING_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
          ReferentielProjectConstants.BUILDING_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
          ReferentielProjectConstants.BUILDING_MANAGE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
          ReferentielProjectConstants.BUILDING_MANAGE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
          ReferentielProjectConstants.BUILDING_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
          ReferentielProjectConstants.BUILDING_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.BUILDING_READ_ROLE);
      }
      addTemplateApplication(template.getTemplateId(), space.getUri(), roleMappings,
          ReferentielProjectConstants.BUILDING_CATEGORY, ReferentielProjectConstants.BUILDING_TYPE,
          ReferentielProjectConstants.BUILDING_TYPE);
    }
    catch (final PluginManagerException e)
    {

      log.error(e);
    }
  }

  /**
   * @param template
   * @param space
   * @throws TemplateServiceException
   *           , ForgeConfigurationException
   */
  private void addPhpbbTempApp(final Template template, final Space space) throws TemplateServiceException,
      ForgeConfigurationException

  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
    {
      // PHPBB
      try
      {
        roleMappings = new HashMap<String, String>();
        roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
            ReferentielProjectConstants.FORUM_FULL_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_SPONSOR_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_LIMITED_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_COMMANDITAIRE_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_LIMITED_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_FULL_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_POLLS_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_POLLS_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_LIMITED_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_LIMITED_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_LIMITED_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.FORUM_STANDARD_ROLE);
        addTemplateApplication(template.getTemplateId(), space.getUri(), roleMappings,
            ReferentielProjectConstants.FORUM_CATEGORY, ReferentielProjectConstants.FORUM_TYPE,
            ReferentielProjectConstants.FORUM_TYPE);
      }
      catch (final PluginManagerException e)
      {

        log.error(e);
      }
    }
  }

  private void addNexusTempApp(final Template template, final Space space) throws TemplateServiceException,
      ForgeConfigurationException

  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    // NEXUS
    try
    {
      roleMappings = new HashMap<String, String>();
      roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
          ReferentielProjectConstants.REPOSITORY_ADMIN_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_SPONSOR_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_COMMANDITAIRE_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_DEV_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_DEV_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_SENIOR_DEV_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
          ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.REPOSITORY_OBSERVER_ROLE);
      }
      addTemplateApplication(template.getTemplateId(), space.getUri(), roleMappings,
          ReferentielProjectConstants.REPOSITORY_CATEGORY, ReferentielProjectConstants.REPOSITORY_TYPE,
          ReferentielProjectConstants.REPOSITORY_TYPE);
    }
    catch (final PluginManagerException e)
    {

      log.error(e);
    }
  }

  private void addSpipTempApp(final Template template, final Space space) throws TemplateServiceException,
      ForgeConfigurationException
  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
    {
      // SPIP
      try
      {
        roleMappings = new HashMap<String, String>();
        roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
            ReferentielProjectConstants.ECM_ADMIN_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_SPONSOR_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_COMMANDITAIRE_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
            ReferentielProjectConstants.ECM_ADMIN_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
            ReferentielProjectConstants.ECM_RESTRICTED_ADMIN_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.ECM_RESTRICTED_ADMIN_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.ECM_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.ECM_READ_ROLE);
        addTemplateApplication(template.getTemplateId(), space.getUri(), roleMappings,
            ReferentielProjectConstants.ECM_CATEGORY, ReferentielProjectConstants.ECM_TYPE,
            ReferentielProjectConstants.ECM_TYPE);
      }
      catch (final PluginManagerException e)
      {

        log.error(e);
      }
    }
  }

  private void addDokuwikiTempApp(final Template template, final Space space)
      throws TemplateServiceException, ForgeConfigurationException

  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    // DOKUWIKI
    try
    {
      roleMappings = new HashMap<String, String>();
      roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
          ReferentielProjectConstants.WIKI_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_SPONSOR_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_COMMANDITAIRE_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_DEV_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_DEV_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_DEV_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_DEV_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_SENIOR_DEV_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
          ReferentielProjectConstants.WIKI_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.WIKI_READ_ROLE);
      }
      addTemplateApplication(template.getTemplateId(), space.getUri(), roleMappings,
          ReferentielProjectConstants.WIKI_CATEGORY, ReferentielProjectConstants.WIKI_TYPE,
          ReferentielProjectConstants.WIKI_TYPE);
    }
    catch (final PluginManagerException e)
    {

      log.error(e);
    }
  }

  private void addLimesurveyTempApp(final Template template, final Space space)
      throws TemplateServiceException, ForgeConfigurationException

  {
    Map<String, String> roleMappings;
    // LIMESURVEY
    final String templateId = template.getTemplateId();
    if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
    {
      try
      {
        roleMappings = new HashMap<String, String>();
        roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
            ReferentielProjectConstants.SURVEY_ADMIN_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.SURVEY_OBSERVER_ROLE);
        addTemplateApplication(template.getTemplateId(), space.getUri(), roleMappings,
            ReferentielProjectConstants.SURVEY_CATEGORY, ReferentielProjectConstants.SURVEY_TYPE,
            ReferentielProjectConstants.SURVEY_TYPE);
      }
      catch (final PluginManagerException e)
      {

        log.error(e);
      }
    }
  }

  private void addTeslinkTempApp(final Template template, final Space space) throws TemplateServiceException,
      ForgeConfigurationException

  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    // TESTLINK
    try
    {
      roleMappings = new HashMap<String, String>();
      roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
          ReferentielProjectConstants.TEST_ADMIN_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.TEST_GUEST_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.TEST_LEADER_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
          ReferentielProjectConstants.TEST_TEST_DESIGN_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.TEST_TEST_DESIGN_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.TEST_TESTER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.TEST_TESTER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.TEST_TESTER_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
          ReferentielProjectConstants.TEST_TESTER_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
          ReferentielProjectConstants.TEST_GUEST_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
          ReferentielProjectConstants.TEST_TESTER_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
          ReferentielProjectConstants.TEST_SENIOR_TESTER_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.TEST_GUEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.TEST_GUEST_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
          ReferentielProjectConstants.TEST_TESTER_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.TEST_SENIOR_TESTER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.TEST_SENIOR_TESTER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.TEST_SENIOR_TESTER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.TEST_SENIOR_TESTER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.TEST_SENIOR_TESTER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.TEST_SENIOR_TESTER_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.TEST_SENIOR_TESTER_ROLE);
      }
      addTemplateApplication(template.getTemplateId(), space.getUri(), roleMappings,
          ReferentielProjectConstants.TEST_CATEGORY, ReferentielProjectConstants.TEST_TYPE,
          ReferentielProjectConstants.TEST_TYPE);
    }
    catch (final PluginManagerException e)
    {

      log.error(e);
    }
  }

  private void addSubversionTempApp(final Template template, final Space space)
      throws TemplateServiceException, ForgeConfigurationException

  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    // SUBVERSION
    try
    {
      roleMappings = new HashMap<String, String>();
      roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
          ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.SUBVERSION_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.SUBVERSION_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
          ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
          ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
          ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
          ReferentielProjectConstants.SUBVERSION_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
          ReferentielProjectConstants.SUBVERSION_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
          ReferentielProjectConstants.SUBVERSION_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.SUBVERSION_READ_ROLE);
      }
      addTemplateApplication(templateId, space.getUri(), roleMappings,
          ReferentielProjectConstants.SUBVERSION_CATEGORY, ReferentielProjectConstants.SUBVERSION_TYPE,
          ReferentielProjectConstants.SUBVERSION_TYPE);
    }
    catch (final PluginManagerException e)
    {

      log.error(e);
    }
  }

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
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_SPONSOR_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_GUEST_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_COMMANDITAIRE_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_GUEST_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_REPORT_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_REPORT_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_REPORT_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_REPORT_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_GUEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_GUEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_GUEST_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_DEV_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_DEV_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_REPORT_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_GUEST_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
          ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.BUGTRACKER_TEST_ROLE);
      }
      addTemplateApplication(templateId, space.getUri(), roleMappings,
          ReferentielProjectConstants.BUGTRACKER_CATEGORY, ReferentielProjectConstants.BUGTRACKER_TYPE,
          ReferentielProjectConstants.BUGTRACKER_TYPE);
    }
    catch (final PluginManagerException e)
    {

      log.error(e);
    }
  }

  private void addAlfrescoTempApp(final Template template, final Space space)
      throws TemplateServiceException, ForgeConfigurationException
  {
    Map<String, String> roleMappings;
    final String templateId = template.getTemplateId();
    // ALFRESCO
    try
    {
      roleMappings = new HashMap<String, String>();
      roleMappings.put(forgeConfigurationService.getForgeAdministratorRoleName(),
          ReferentielProjectConstants.GED_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_SPONSOR_ROLE_PHARE,
          ReferentielProjectConstants.GED_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_COMMANDITAIRE_ROLE_PHARE,
          ReferentielProjectConstants.GED_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.GED_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE,
          ReferentielProjectConstants.GED_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE,
          ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE,
          ReferentielProjectConstants.GED_WRITE_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE,
          ReferentielProjectConstants.GED_READ_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE,
          ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE,
          ReferentielProjectConstants.GED_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE,
            ReferentielProjectConstants.GED_READ_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE,
            ReferentielProjectConstants.GED_READ_ROLE);
      }
      roleMappings.put(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE,
          ReferentielProjectConstants.GED_READ_ROLE);
      if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
      {
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
        roleMappings.put(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE,
            ReferentielProjectConstants.GED_CONTRIBUTOR_ROLE);
      }
      addTemplateApplication(templateId, space.getUri(), roleMappings,
          ReferentielProjectConstants.GED_CATEGORY, ReferentielProjectConstants.GED_TYPE,
          ReferentielProjectConstants.GED_TYPE);
    }
    catch (final PluginManagerException e)
    {

      log.error(e);
    }
  }

  private void addTemplateRoles(final String templateId) throws TemplateServiceException,
      ForgeConfigurationException
  {

    final Role roleAdmin = templateRolePresenter.newRole();
    roleAdmin.setName(forgeConfigurationService.getForgeAdministratorRoleName());
    templateRolePresenter.createSystemRole(roleAdmin, templateId);

    final Role roleSponsor = templateRolePresenter.newRole();
    roleSponsor.setName(ReferentielProjectConstants.TEMPLATE_SPONSOR_ROLE_PHARE);
    roleSponsor.setDescription(ReferentielProjectConstants.TEMPLATE_SPONSOR_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleSponsor, templateId);

    final Role roleCommanditaire = templateRolePresenter.newRole();
    roleCommanditaire.setName(ReferentielProjectConstants.TEMPLATE_COMMANDITAIRE_ROLE_PHARE);
    roleCommanditaire.setDescription(ReferentielProjectConstants.TEMPLATE_COMMANDITAIRE_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleCommanditaire, templateId);

    final Role roleDirecteurDeProjet = templateRolePresenter.newRole();
    roleDirecteurDeProjet.setName(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE);
    roleDirecteurDeProjet
        .setDescription(ReferentielProjectConstants.TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleDirecteurDeProjet, templateId);

    final Role roleChefDeProjet = templateRolePresenter.newRole();
    roleChefDeProjet.setName(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE);
    roleChefDeProjet.setDescription(ReferentielProjectConstants.TEMPLATE_CHEF_PROJET_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleChefDeProjet, templateId);

    final Role roleAnalysteFonctionnel = templateRolePresenter.newRole();
    roleAnalysteFonctionnel.setName(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE);
    roleAnalysteFonctionnel
        .setDescription(ReferentielProjectConstants.TEMPLATE_ANALYSTE_FONC_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleAnalysteFonctionnel, templateId);

    if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
    {

      final Role roleAnalysteMetier = templateRolePresenter.newRole();
      roleAnalysteMetier.setName(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE);
      roleAnalysteMetier.setDescription(ReferentielProjectConstants.TEMPLATE_ANALYSTE_METIER_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleAnalysteMetier, templateId);

      final Role roleArchitecteSI = templateRolePresenter.newRole();
      roleArchitecteSI.setName(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE);
      roleArchitecteSI.setDescription(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_SI_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleArchitecteSI, templateId);

      final Role roleArchitecteTechnique = templateRolePresenter.newRole();
      roleArchitecteTechnique.setName(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE);
      roleArchitecteTechnique
          .setDescription(ReferentielProjectConstants.TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleArchitecteTechnique, templateId);

      final Role roleConcepteurLogiciel = templateRolePresenter.newRole();
      roleConcepteurLogiciel.setName(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE);
      roleConcepteurLogiciel
          .setDescription(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleConcepteurLogiciel, templateId);

    }

    final Role roleDeveloppeur = templateRolePresenter.newRole();
    roleDeveloppeur.setName(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE);
    roleDeveloppeur.setDescription(ReferentielProjectConstants.TEMPLATE_DEV_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleDeveloppeur, templateId);

    final Role roleIntegrateur = templateRolePresenter.newRole();
    roleIntegrateur.setName(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE);
    roleIntegrateur.setDescription(ReferentielProjectConstants.TEMPLATE_INTEGRATEUR_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleIntegrateur, templateId);

    final Role roleGestionnaireDeVersion = templateRolePresenter.newRole();
    roleGestionnaireDeVersion.setName(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE);
    roleGestionnaireDeVersion
        .setDescription(ReferentielProjectConstants.TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleGestionnaireDeVersion, templateId);

    final Role roleRecetteur = templateRolePresenter.newRole();
    roleRecetteur.setName(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE);
    roleRecetteur.setDescription(ReferentielProjectConstants.TEMPLATE_RECETTEUR_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleRecetteur, templateId);

    if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
    {
      final Role roleConcepteurAccompagnement = templateRolePresenter.newRole();
      roleConcepteurAccompagnement.setName(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE);
      roleConcepteurAccompagnement
          .setDescription(ReferentielProjectConstants.TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleConcepteurAccompagnement, templateId);

      final Role roleFormateur = templateRolePresenter.newRole();
      roleFormateur.setName(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE);
      roleFormateur.setDescription(ReferentielProjectConstants.TEMPLATE_FORMATEUR_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleFormateur, templateId);
    }

    final Role roleUtilisateur = templateRolePresenter.newRole();
    roleUtilisateur.setName(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE);
    roleUtilisateur.setDescription(ReferentielProjectConstants.TEMPLATE_UTILISATEUR_ROLE_PHARE_DESC);
    templateRolePresenter.createRole(roleUtilisateur, templateId);

    if (!TEMPLATE_TMA_WITH_PIC_ID.equals(templateId) && !TEMPLATE_TMA_WITHOUT_PIC.equals(templateId))
    {
      final Role roleExpertMetier = templateRolePresenter.newRole();
      roleExpertMetier.setName(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE);
      roleExpertMetier.setDescription(ReferentielProjectConstants.TEMPLATE_EXPERT_METIER_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleExpertMetier, templateId);

      final Role roleExpertFonctionnel = templateRolePresenter.newRole();
      roleExpertFonctionnel.setName(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE);
      roleExpertFonctionnel.setDescription(ReferentielProjectConstants.TEMPLATE_EXPERT_FONC_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleExpertFonctionnel, templateId);

      final Role roleExpertTechnique = templateRolePresenter.newRole();
      roleExpertTechnique.setName(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE);
      roleExpertTechnique.setDescription(ReferentielProjectConstants.TEMPLATE_EXPERT_TECH_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleExpertTechnique, templateId);

      final Role roleExpertLogiciel = templateRolePresenter.newRole();
      roleExpertLogiciel.setName(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE);
      roleExpertLogiciel.setDescription(ReferentielProjectConstants.TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleExpertLogiciel, templateId);

      final Role roleExpertMethode = templateRolePresenter.newRole();
      roleExpertMethode.setName(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE);
      roleExpertMethode.setDescription(ReferentielProjectConstants.TEMPLATE_EXPERT_METHODE_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleExpertMethode, templateId);

      final Role roleExpertQualite = templateRolePresenter.newRole();
      roleExpertQualite.setName(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE);
      roleExpertQualite.setDescription(ReferentielProjectConstants.TEMPLATE_EXPERT_QUALITE_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleExpertQualite, templateId);

      final Role roleExpertOutils = templateRolePresenter.newRole();
      roleExpertOutils.setName(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE);
      roleExpertOutils.setDescription(ReferentielProjectConstants.TEMPLATE_EXPERT_OUTILS_ROLE_PHARE_DESC);
      templateRolePresenter.createRole(roleExpertOutils, templateId);
    }
  }

  /**
   * @param projectId
   * @param spaceUri
   * @param roleMappings
   * @throws DataAccessException
   * @throws PluginManagerException
   * @throws TemplateServiceException
   *           , ForgeConfigurationException
   */
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

          templateNodePresenter.addApplication(templateId, spaceUri, appName, UUID.fromString(pluginUUID),
              roleMappings);
          log.info(String.format("Application %s created.", appType));
        }
      }
    }
    else
    {
      log.info(String
          .format("AddTemplateApplication: No application exists for the category %s", appCategory));
    }
  }

  public void setTemplatePresenter(final TemplatePresenter pTemplatePresenter)
  {
    templatePresenter = pTemplatePresenter;
  }

  public void setTemplateNodePresenter(final TemplateNodePresenter pTemplateNodePresenter)
  {
    templateNodePresenter = pTemplateNodePresenter;
  }

  public void setTemplateRolePresenter(final TemplateRolePresenter pTemplateRolePresenter)
  {
    templateRolePresenter = pTemplateRolePresenter;
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }
}
