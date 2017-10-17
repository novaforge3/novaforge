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
package org.novaforge.forge.configuration.initialization.internal.datas;

/**
 * @author rols-p
 */
public interface ReferentielProjectConstants
{
  String REFERENTIEL_SPACE_NAME                        = "Applications partagées";

  // Applications constants
  String WIKI_CATEGORY                                 = "wiki";
  String WIKI_TYPE                                     = "Dokuwiki";
  String WIKI_READ_ROLE                                = "Observer";
  String WIKI_WRITE_ROLE                               = "Administrator";
  String WIKI_DEV_ROLE                                 = "Developper";
  String WIKI_SENIOR_DEV_ROLE                          = "Developper Senior";
  String WIKI_APPLICATION_NAME                         = "Wiki Referentiel";

  String GED_CATEGORY                                  = "ecm";
  String GED_TYPE                                      = "Alfresco";
  String GED_READ_ROLE                                 = "Consumer";
  String GED_WRITE_ROLE                                = "Manager";
  String GED_CONTRIBUTOR_ROLE                          = "Contributor";
  String GED_APPLICATION_NAME                          = "Alfresco Referentiel";

  String BUGTRACKER_CATEGORY                           = "bugtracker";
  String BUGTRACKER_TYPE                               = "Mantis";
  String BUGTRACKER_MANAGER_ROLE                       = "Manager";
  String BUGTRACKER_TEST_ROLE                          = "Tester";
  String BUGTRACKER_DEV_ROLE                           = "Developer";
  String BUGTRACKER_ADMIN_ROLE                         = "Administrator";
  String BUGTRACKER_REPORT_ROLE                        = "Reporter";
  String BUGTRACKER_GUEST_ROLE                         = "Viewer";

  String FORUM_CATEGORY                                = "forum";
  String FORUM_TYPE                                    = "PhpBB";
  String FORUM_USER_ROLE                               = "User";
  String FORUM_FULL_ROLE                               = "Full Access";
  String FORUM_STANDARD_ROLE                           = "Standard Access";
  String FORUM_NEW_USER_ROLE                           = "Newly registered User";
  String FORUM_STANDARD_POLLS_ROLE                     = "Standard Access + Polls";
  String FORUM_LIMITED_ROLE                            = "Limited Access";
  String FORUM_LIMITED_POLLS_ROLE                      = "Limited Access + Polls";
  String FORUM_READ_ROLE                               = "Read Only Access";
  String FORUM_BAN_ROLE                                = "No Access";
  String FORUM_QUEUE_ROLE                              = "On Moderation Queue";
  String FORUM_BOT_ACCESS                              = "Bot Access";

  String SUBVERSION_CATEGORY                           = "scm";
  String SUBVERSION_TYPE                               = "SVN";
  String SUBVERSION_READ_ROLE                          = "reader";
  String SUBVERSION_WRITE_ROLE                         = "writer";

  String BUILDING_CATEGORY                             = "ci";
  String BUILDING_TYPE                                 = "Jenkins";
  String BUILDING_ADMIN_ROLE                           = "administrator";
  String BUILDING_READ_ROLE                            = "viewer";
  String BUILDING_WRITE_ROLE                           = "operator";
  String BUILDING_MANAGE_ROLE                          = "SCM Manager";

  String SURVEY_CATEGORY                               = "survey";
  String SURVEY_TYPE                                   = "Limesurvey";
  String SURVEY_ADMIN_ROLE                             = "Administrator";
  String SURVEY_OBSERVER_ROLE                          = "Observer";

  String TEST_CATEGORY                                 = "testmanagement";
  String TEST_TYPE                                     = "Testlink";
  String TEST_TESTER_ROLE                              = "Tester";
  String TEST_SENIOR_TESTER_ROLE                       = "Senior tester";
  String TEST_ADMIN_ROLE                               = "Administrator";
  String TEST_LEADER_ROLE                              = "Leader";
  String TEST_GUEST_ROLE                               = "Guest";
  String TEST_TEST_DESIGN_ROLE                         = "Test designer";

  String REPOSITORY_CATEGORY                           = "repositorymanagement";
  String REPOSITORY_TYPE                               = "Nexus";
  String REPOSITORY_ADMIN_ROLE                         = "Administrator";
  String REPOSITORY_DEV_ROLE                           = "Developper";
  String REPOSITORY_SENIOR_DEV_ROLE                    = "Developper Senior";
  String REPOSITORY_OBSERVER_ROLE                      = "Observer";

  String QUALIMETRY_CATEGORY                           = "quality";
  String QUALIMETRY_TYPE                               = "Sonar";
  String QUALIMETRY_ADMIN_ROLE                         = "Administrators";
  String QUALIMETRY_READ_ROLE                          = "Code viewers";
  String QUALIMETRY_WRITE_ROLE                         = "Users";

  String ECM_CATEGORY                                  = "cms";
  String ECM_TYPE                                      = "Spip";
  String ECM_READ_ROLE                                 = "Visitor";
  String ECM_WRITE_ROLE                                = "Author";
  String ECM_ADMIN_ROLE                                = "Administrator";
  String ECM_RESTRICTED_ADMIN_ROLE                     = "Webmaster";

  String TEMPLATE_BASIC_PROJECT_NAME                   = "Template Projet de Base";
  String TEMPLATE_TMA_PROJECT_WITH_PIC_NAME            = "Template Projet TMA avec PIC";
  String TEMPLATE_TMA_PROJECT_WITHOUT_PIC_NAME         = "Template Projet TMA sans PIC";
  String TEMPLATE_PACKAGE_PROJECT_NAME                 = "Template Projet au Forfait";
  String TEMPLATE_BASIC_PROJECT_DESC                   = "Template Projet de Base";
  String TEMPLATE_TMA_PROJECT_WITH_PIC_DESC            = "Template Projet TMA avec PIC";
  String TEMPLATE_TMA_PROJECT_WITHOUT_PIC_DESC         = "Template Projet TMA sans PIC";
  String TEMPLATE_PACKAGE_PROJECT_DESC                 = "Template Projet au Forfait";

  String TEMPLATE_SPONSOR_ROLE_PHARE                   = "Sponsor";
  String TEMPLATE_COMMANDITAIRE_ROLE_PHARE             = "Commanditaire";
  String TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE          = "Directeur de Projet";
  String TEMPLATE_CHEF_PROJET_ROLE_PHARE               = "Chef de Projet";
  String TEMPLATE_ANALYSTE_FONC_ROLE_PHARE             = "Analyste fonctionnel";
  String TEMPLATE_ANALYSTE_METIER_ROLE_PHARE           = "Analyste m\u00e9tier";
  String TEMPLATE_ARCHITECTE_SI_ROLE_PHARE             = "Architecte SI";
  String TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE           = "Architecte technique";
  String TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE       = "Concepteur logiciel";
  String TEMPLATE_DEV_ROLE_PHARE                       = "D\u00e9veloppeur";
  String TEMPLATE_INTEGRATEUR_ROLE_PHARE               = "Int\u00e9grateur";
  String TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE      = "Gestionnaire de version";
  String TEMPLATE_RECETTEUR_ROLE_PHARE                 = "Recetteur";
  String TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE         = "Concepteur accompagnement";
  String TEMPLATE_FORMATEUR_ROLE_PHARE                 = "Formateur";
  String TEMPLATE_UTILISATEUR_ROLE_PHARE               = "Utilisateur";
  String TEMPLATE_EXPERT_METIER_ROLE_PHARE             = "Expert m\u00e9tier";
  String TEMPLATE_EXPERT_FONC_ROLE_PHARE               = "Expert fonctionnel";
  String TEMPLATE_EXPERT_TECH_ROLE_PHARE               = "Expert technique";
  String TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE           = "Expert logiciel";
  String TEMPLATE_EXPERT_METHODE_ROLE_PHARE            = "Expert m\u00e9thode";
  String TEMPLATE_EXPERT_QUALITE_ROLE_PHARE            = "Expert qualit\u00e9";
  String TEMPLATE_EXPERT_OUTILS_ROLE_PHARE             = "Expert outils";

  String TEMPLATE_SPONSOR_ROLE_PHARE_DESC              = "Le sponsor n'est pas le demandeur v\u00E9ritable, c'est le commanditaire du projet. Mais il a par son niveau de responsabilit\u00E9 dans l'organisation l'\u00E9tendue des pouvoirs n\u00E9cessaires pour arbitrer des d\u00E9cisions en faveur du projet, lorsque l'on sort du p\u00E9rim\u00E8tre de manoeuvre du directeur de projet et du commanditaire.";
  String TEMPLATE_COMMANDITAIRE_ROLE_PHARE_DESC        = "Le commanditaire est le manager demandeur du projet. Il est le garant de la vision cible m\u00E9tier du projet. Il est porteur de la d\u00E9finition des enjeux m\u00E9tiers et de sa d\u00E9clinaison en objectifs m\u00E9tiers pour le projet.";
  String TEMPLATE_DIRECTEUR_PROJET_ROLE_PHARE_DESC     = "Quand un projet de grande taille n\u00E9cessite l'intervention de plusieurs chefs de projet, un directeur de projet est nomm\u00E9 pour assumer la direction et la responsabilit\u00E9 op\u00E9rationnelle de l'ensemble. Un directeur de projet est n\u00E9cessairement un manager de projet exp\u00E9riment\u00E9. Il n'est pas seulement le garant du besoin, il est responsable du r\u00E9sultat produit par le projet. Tous les acteurs du projet sont plac\u00E9s sous sa responsabilit\u00E9, le temps du projet et pour toutes les activit\u00E9s li\u00E9es au projet, sans fronti\u00E8re MOA-MOE.";
  String TEMPLATE_CHEF_PROJET_ROLE_PHARE_DESC          = "Le chef de projet est responsable de : l\u2019 \u00E9valuation et la planification des t\u00E2ches, la gestion et l\u2019affectation des ressources, l\u2019identification des priorit\u00E9s, l\u2019interaction avec les utilisateurs, la qualit\u00E9 et l'int\u00E9grit\u00E9 des livrables du projet.";
  String TEMPLATE_ANALYSTE_FONC_ROLE_PHARE_DESC        = "R\u00E9alise l'analyse des exigences de mani\u00E8re \u00E0 partager une d\u00E9finition pr\u00E9cise avec les utilisateurs et \u00E0 arriver \u00E0 un niveau de d\u00E9tail suffisant pour passer \u00E0 la conception du syst\u00E8me.";
  String TEMPLATE_ANALYSTE_METIER_ROLE_PHARE_DESC      = "L'analyste m\u00E9tier connait l'activit\u00E9 de l'organisation concern\u00E9 par le p\u00E9rim\u00E8tre du projet. Il intervient pour aligner les besoins m\u00E9tier en identifiant les fonctionnalit\u00E9s que le projet devra r\u00E9aliser. Il analyse \u00E9galement les processus et l'organisation existante et propose une nouvelle cible pour tirer le maximum de b\u00E9n\u00E9fice pour le m\u00E9tier du nouveau projet.";
  String TEMPLATE_ARCHITECTE_SI_ROLE_PHARE_DESC        = "D\u00E9finit la cible syst\u00E8me d'information (SI) du projet. Il r\u00E9pond \u00E0 la question de savoir comment on doit int\u00E9grer l'architecture du projet dans le cadre de l'architecture du SI global.";
  String TEMPLATE_ARCHITECTE_TECH_ROLE_PHARE_DESC      = "Responsable de la partie technique de l'architecture : plateformes d'ex\u00E9cution, frameworks, int\u00E9gration, infrastructure, d\u00E9ploiement.";
  String TEMPLATE_CONCEPTEUR_LOGICIEL_ROLE_PHARE_DESC  = "Responsable de la conception des composants logiciels \u00E0 d\u00E9velopper pour r\u00E9aliser une partie du syst\u00E8me.";
  String TEMPLATE_DEV_ROLE_PHARE_DESC                  = "D\u00E9veloppe et teste une partie du logiciel en respectant la conception et les standards techniques d\u00E9finis.";
  String TEMPLATE_INTEGRATEUR_ROLE_PHARE_DESC          = "Assemble dans un espace d'int\u00E9gration les composants impl\u00E9ment\u00E9s par les d\u00E9veloppeurs dans leurs espaces de travail.";
  String TEMPLATE_GESTIONNAIRE_VERSION_ROLE_PHARE_DESC = "D\u00E9finit et supervise la mani\u00E8re dont les demandes de changement (\u00E9volutions des exigences ou erreurs d\u00E9tect\u00E9es lors des tests) sont trait\u00E9es par le projet. Met en place l'outillage n\u00E9cessaire et coordonne la production de versions coh\u00E9rentes du syst\u00E8me.";
  String TEMPLATE_RECETTEUR_ROLE_PHARE_DESC            = "Ex\u00E9cute les sc\u00E9narios de test, analyse les r\u00E9sultats obtenus et r\u00E9dige les fiches de demande de changement si n\u00E9cessaire.";
  String TEMPLATE_CONCEPTEUR_ACCOMP_ROLE_PHARE_DESC    = "Pr\u00E9pare la livraison du produit aux utilisateurs (la mise en production) et r\u00E9duit les risques de r\u00E9sistance au changement en d\u00E9roulant un plan de formation et de communication. Assure le support op\u00E9rationnel pendant la p\u00E9riode de garantie suivant la mise en production.";
  String TEMPLATE_FORMATEUR_ROLE_PHARE_DESC            = "Responsable de la cr\u00E9ation des supports et de l'animation d'une partie du plan de communication ou de formation.";
  String TEMPLATE_UTILISATEUR_ROLE_PHARE_DESC          = "Client final du syst\u00E8me, peut jouer le r\u00F4le d'expert m\u00E9tier en intervenant sur les t\u00E2ches de mod\u00E9lisation m\u00E9tier, d'identification et d'analyse des exigences, de recette.";
  String TEMPLATE_EXPERT_METIER_ROLE_PHARE_DESC        = "A pour r\u00F4le principal d'apporter du support \u00E0 la ma\u00EEtrise d'ouvrage (AMOA) dans ses domaines d'expertise.";
  String TEMPLATE_EXPERT_FONC_ROLE_PHARE_DESC          = "Dans le cadre d'un projet, a pour r\u00F4le principal d'apporter du support \u00E0 la maitrise d'ouvrage (AMOA) dans ses domaines d'expertise.";
  String TEMPLATE_EXPERT_TECH_ROLE_PHARE_DESC          = "L'expert technique intervient en support du projet sur l'architecture technique. C'est un r\u00F4le de support en ma\u00EEtrise d'oeuvre (AMOE).";
  String TEMPLATE_EXPERT_LOGICIEL_ROLE_PHARE_DESC      = "Intervient en tant que support des \u00E9quipes projet en ma\u00EEtrise d'oeuvre (AMOE) principalement autour de la d\u00E9finition de l'architecture cible et des technologies d'impl\u00E9mentation \u00E0 mettre en oeuvre sur le projet.";
  String TEMPLATE_EXPERT_METHODE_ROLE_PHARE_DESC       = "Il apporte une aide m\u00E9thodologique \u00E0 tous les acteurs projet sur l'ensemble des activit\u00E9s. Il capitalise \u00E9galement les bonnes pratiques qui enrichiront la m\u00E9thode.";
  String TEMPLATE_EXPERT_QUALITE_ROLE_PHARE_DESC       = "Assiste le chef de projet en d\u00E9finissant les t\u00E2ches et livrables \u00E0 produire, ainsi que les proc\u00E9dures d'assurance qualit\u00E9 (circuits de validation de chaque livrable). S'assure que le processus projet et les proc\u00E9dures d'assurance qualit\u00E9 sont suivies.";
  String TEMPLATE_EXPERT_OUTILS_ROLE_PHARE_DESC        = "Expert sur des outils n\u00E9cessaires au projet (environnement de conception, de d\u00E9veloppement, etc...).";
}
