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
package org.novaforge.forge.portal.i18n;

import java.io.Serializable;

/**
 * This interface refers all key message used for i18n.
 * 
 * @author Guillaume Lamirand
 */
public interface Messages extends Serializable
{
  // Portal
  String PORTAL_TITLE                                = "portal.title";                                             //$NON-NLS-1$
  String PORTAL_SYSTEM_SESSIONEXPIRED_TITLE          = "portal.system.sessionexpiredtitle";                        //$NON-NLS-1$
  String PORTAL_SYSTEM_SESSIONEXPIRED_MESSAGE        = "portal.system.sessionexpiredmessage";                      //$NON-NLS-1$
  String PORTAL_SYSTEM_COOKIEDISABLED_TITLE          = "portal.system.cookiedisabledtitle";                        //$NON-NLS-1$
  String PORTAL_SYSTEM_COOKIEDISABLED_MESSAGE        = "portal.system.cookiedisabledmessage";                      //$NON-NLS-1$
  String PORTAL_SYSTEM_COMMUNICATIONERROR_TITLE      = "portal.system.communicationerrortitle";                    //$NON-NLS-1$
  String PORTAL_SYSTEM_COMMUNICATIONERROR_MESSAGE    = "portal.system.communicationerrormessage";                  //$NON-NLS-1$
  String PORTAL_SYSTEM_INTERNALERROR_TITLE           = "portal.system.internalerrortitle";                         //$NON-NLS-1$
  String PORTAL_SYSTEM_INTERNALERROR_MESSAGE         = "portal.system.internalerrormessage";                       //$NON-NLS-1$
  String PORTAL_SYSTEM_AUTHENTIFICATIONERROR_TITLE   = "portal.system.authentificationerrortitle";                 //$NON-NLS-1$
  String PORTAL_SYSTEM_AUTHENTIFICATIONERROR_MESSAGE = "portal.system.authentificationerrormessage";               //$NON-NLS-1$
  String PORTAL_SYSTEM_OUTOFSYNCERROR_TITLE          = "portal.system.outofsyncerrortitle";                        //$NON-NLS-1$
  String PORTAL_SYSTEM_OUTOFSYNCERROR_MESSAGE        = "portal.system.outofsyncerrormessage";                      //$NON-NLS-1$

  // Errors
  String ERROR_TECHNICAL_TITLE          = "error.technical.title";                                    //$NON-NLS-1$
  String ERROR_TECHNICAL_DESC           = "error.technical.desc";                                     //$NON-NLS-1$
  String ERROR_CREDENTIAL_MATHING_TITLE = "error.credential.mathing.title";                           //$NON-NLS-1$
  String ERROR_CREDENTIAL_MATHING_DESC  = "error.credential.mathing.desc";                            //$NON-NLS-1$

  // Errors code
  String ERROR_VALIDATION_BEAN                 = "error.validation.bean";                                    //$NON-NLS-1$
  // Create project
  String ERROR_PROJECT_NAME_ALREADY_EXIST      = "error.project.name.exists";                                //$NON-NLS-1$
  String ERROR_PROJECTID_ALREADY_EXIST         = "error.project.id.exists";                                  //$NON-NLS-1$
  String ERROR_ORGANIZATION                    = "error.organization";                                       //$NON-NLS-1$
  String ERROR_AUTHOR_NOT_EXIST                = "error.author.nottexist";                                   //$NON-NLS-1$
  // Validate project
  String ERROR_PROJECT_STATUS_CHANGED          = "error.project.status.changed";                             //$NON-NLS-1$
  // Applications management
  String ERROR_SPACE_NAME_EXISTS               = "error.space.name.exists";                                  //$NON-NLS-1$
  String ERROR_APPLICATION_NAME_EXISTS         = "error.application.name.exists";                            //$NON-NLS-1$
  String ERROR_MAX_ALLOWED_PROJECT_APPLICATION = "error.application.plugin.exists";                          //$NON-NLS-1$
  String ERROR_APPLICATION_REQUEST_EXISTS      = "error.application.request.exists";                         //$NON-NLS-1$
  String ERROR_ASSOCIATION_REQUEST_EXISTS      = "error.association.request.exists";                         //$NON-NLS-1$
  String ERROR_ASSOCIATION_EXISTS              = "error.association.exists";                                 //$NON-NLS-1$
  // Memberships management
  String ERROR_MEMBERSHIP_ACTOR_NOTEXIST       = "error.membership.actor.notexist";                          //$NON-NLS-1$
  String ERROR_MEMBERSHIP_ROLE_EXISTS          = "error.membership.role.exists";                             //$NON-NLS-1$
  String ERROR_MEMBERSHIP_GROUP_NOTVISIBLE     = "error.membership.group.notvisible";                        //$NON-NLS-1$
  String ERROR_ROLE_NAME_EXISTS                = "error.role.name.exists";                                   //$NON-NLS-1$
  String ERROR_ROLE_USED                       = "error.role.used";                                          //$NON-NLS-1$
  String ERROR_ROLE_SYSTEM                     = "error.role.system";                                        //$NON-NLS-1$
  String ERROR_GROUP_NAME_EXISTS               = "error.group.name.exists";                                  //$NON-NLS-1$
  String ERROR_GROUP_MEMBERSHIP_EXISTS         = "error.group.membership.exists";                            //$NON-NLS-1$
  String ERROR_USER_LOGIN_EXISTS               = "error.user.login.exists";                                  //$NON-NLS-1$
  String ERROR_USER_EMAIL_EXISTS               = "error.user.email.exists";                                  //$NON-NLS-1$
  String ERROR_USER_FORBIDDEN_LOGIN            = "error.user.forbidden.login";                               //$NON-NLS-1$
  String ERROR_SENDING_MAIL_AUTHOR             = "error.mail.author";                                        //$NON-NLS-1$
  String ERROR_SENDING_MAIL_ADMINISTRATOR      = "error.mail.admin";                                         //$NON-NLS-1$
  String ERROR_SENDING_MAIL_MEMBER             = "error.mail.member";                                        //$NON-NLS-1$

  // MailingLists
  String ERROR_CREATE_MAILING_LIST_ALREADY_EXISTS = "error.mailinglist.already.exists";                         //$NON-NLS-1$

  // Actions
  String ACTIONS              = "actions";                                                  //$NON-NLS-1$
  String ACTIONS_REFRESH      = "actions.refresh";                                          //$NON-NLS-1$
  String ACTIONS_DELETE       = "actions.delete";                                           //$NON-NLS-1$
  String ACTIONS_CANCEL       = "actions.cancel";                                           //$NON-NLS-1$
  String ACTIONS_EDIT         = "actions.edit";                                             //$NON-NLS-1$
  String ACTIONS_SAVE         = "actions.save";                                             //$NON-NLS-1$
  String ACTIONS_CLOSE        = "actions.close";                                            //$NON-NLS-1$
  String ACTIONS_EXPORT_EXCEL = "actions.export.excel";                                     //$NON-NLS-1$
  String ACTIONS_EXPORT_PDF   = "actions.export.pdf";                                       //$NON-NLS-1$
  String ACTIONS_EXPORT_CSV   = "actions.export.csv";                                       //$NON-NLS-1$

  // Components
  String COMPONENT_IMAGE_TITLE              = "component.image.title";                                    //$NON-NLS-1$
  String COMPONENT_IMAGE_CONFIRM            = "component.image.confirm";                                  //$NON-NLS-1$
  String COMPONENT_IMAGE_HELP               = "component.image.help";                                     //$NON-NLS-1$
  String COMPONENT_IMAGE_UPLOAD_CAPTION     = "component.image.upload.caption";                           //$NON-NLS-1$
  String COMPONENT_IMAGE_UPLOAD_DESCRIPTION = "component.image.upload.description";                       //$NON-NLS-1$
  String COMPONENT_IMAGE_UPLOAD_BUTTON      = "component.image.upload.button";                            //$NON-NLS-1$
  String COMPONENT_IMAGE_ERROR_NULL         = "component.image.error.null";                               //$NON-NLS-1$
  String COMPONENT_IMAGE_ERROR_FORMAT       = "component.image.error.format";                             //$NON-NLS-1$
  String COMPONENT_IMAGE_ERROR_SIZE         = "component.image.error.size";

  // Widget Common
  String WIDGET_BUGTRACKER_VERSION = "widget.bugtracker.version";                                //$NON-NLS-1$

  // Unavailable
  String UNAVAILABLE_TITLE       = "unavailable.title";                                        //$NON-NLS-1$
  String UNAVAILABLE_DESCRIPTION = "unavailable.description";

  // NoSettings
  String NOSETTINGS_TITLE       = "nosettings.title";                                         //$NON-NLS-1$
  String NOSETTINGS_DESCRIPTION = "nosettings.description";

  // NoSettings
  String LOADING_TITLE = "loading.title";                                            //$NON-NLS-1$

  // IssueDivision
  String BUGTRACKERISSUEDIVISION_DATA_TITLE            = "bugtrackerissuedivision.data.title";                       //$NON-NLS-1$
  String BUGTRACKERISSUEDIVISION_DATA_TITLE_ANYVERSION = "bugtrackerissuedivision.data.title.any";                   //$NON-NLS-1$

  // IssueTimeline
  String BUGTRACKERISSUETIMELINE_ADMIN_PERIOD          = "bugtrackerissuetimeline.admin.period";                     //$NON-NLS-1$
  String BUGTRACKERISSUETIMELINE_DATA_TITLE            = "bugtrackerissuetimeline.data.title";                       //$NON-NLS-1$
  String BUGTRACKERISSUETIMELINE_DATA_TITLE_ANYVERSION = "bugtrackerissuetimeline.data.title.any";                   //$NON-NLS-1$
  String BUGTRACKERISSUETIMELINE_AXIS_Y                = "bugtrackerissuetimeline.axis.y";                           //$NON-NLS-1$

  // Quality Libraries
  String QUALITYLIBRARIES_ADMIN_DATASOURCE       = "qualitylibraries.admin.datasource";                        //$NON-NLS-1$
  String QUALITYLIBRARIES_ADMIN_RESOURCE         = "qualitylibraries.admin.resource";                          //$NON-NLS-1$
  String QUALITYLIBRARIES_ADMIN_RESOURCE_INPUT   = "qualitylibraries.admin.resource.input";                    //$NON-NLS-1$
  String QUALITYLIBRARIES_ADMIN_RESOURCE_REQUIRE = "qualitylibraries.admin.resource.require";                  //$NON-NLS-1$

  // ViolationTimeline
  String QUALITYVIOLATIONTIMELINE_DATA_TITLE                       = "qualityviolationtimeline.data.title";                      //$NON-NLS-1$
  String QUALITYVIOLATIONTIMELINE_DATA_QUALITYMETRICLABEL_BLOCKER  = "qualityviolationtimeline.data.qualitymetriclabel.blocker"; //$NON-NLS-1$
  String QUALITYVIOLATIONTIMELINE_DATA_QUALITYMETRICLABEL_CRITICAL = "qualityviolationtimeline.data.qualitymetriclabel.critical"; //$NON-NLS-1$
  String QUALITYVIOLATIONTIMELINE_DATA_QUALITYMETRICLABEL_MAJOR    = "qualityviolationtimeline.data.qualitymetriclabel.major";   //$NON-NLS-1$
  String QUALITYVIOLATIONTIMELINE_DATA_QUALITYMETRICLABEL_MINOR    = "qualityviolationtimeline.data.qualitymetriclabel.minor";   //$NON-NLS-1$
  String QUALITYVIOLATIONTIMELINE_AXIS_Y                           = "qualityviolationtimeline.axis.y";                          //$NON-NLS-1$

  // CoverageTimeline
  String QUALITYCOVERAGETIMELINE_DATA_TITLE                       = "qualitycoveragetimeline.data.title";
  String QUALITYCOVERAGETIMELINE_DATA_QUALITYMETRICLABEL_COVERAGE = "qualitycoveragetimeline.data.qualitymetriclabel.coverage";

  // UnittestsBar
  String UNITTESTSBAR_DATA_TITLE                           = "unittestsbar.data.title";                                  //$NON-NLS-1$
  String UNITTESTSBAR_DATA_QUALITYMETRICLABEL_TESTS        = "unittestsbar.data.qualitymetriclabel.tests";               //$NON-NLS-1$
  String UNITTESTSBAR_DATA_QUALITYMETRICLABEL_TESTFAILURES = "unittestsbar.data.qualitymetriclabel.testfailures";        //$NON-NLS-1$
  String UNITTESTSBAR_DATA_QUALITYMETRICLABEL_TESTERRORS   = "unittestsbar.data.qualitymetriclabel.testerrors";          //$NON-NLS-1$
  String UNITTESTSBAR_ADMIN_NUMBEROFCOMMIT                 = "unittestsbar.admin.numberofcommit";                        //$NON-NLS-1$
  String UNITTESTSBAR_ADMIN_NUMBEROFCOMMIT_TOOLTIP         = "unittestsbar.admin.numberofcommit.tooltip";                //$NON-NLS-1$
  String UNITTESTSBAR_AXIS_Y                               = "unittestsbar.axis.y";                                      //$NON-NLS-1$

  // LastCommit
  String SCMLASTCOMMIT_ADMIN_NUMBEROFCOMMIT         = "scmlastcommit.admin.numberofcommit";                       //$NON-NLS-1$
  String SCMLASTCOMMIT_ADMIN_NUMBEROFCOMMIT_TOOLTIP = "scmlastcommit.admin.numberofcommit.tooltip";               //$NON-NLS-1$
  String SCMLASTCOMMIT_TABLE_COMMITS_REVISION       = "scmlastcommit.table.commits.revision";                     //$NON-NLS-1$
  String SCMLASTCOMMIT_TABLE_COMMITS_AUTHOR         = "scmlastcommit.table.commits.author";                       //$NON-NLS-1$
  String SCMLASTCOMMIT_TABLE_COMMITS_DATE           = "scmlastcommit.table.commits.date";                         //$NON-NLS-1$
  String SCMLASTCOMMIT_TABLE_COMMITS_CHANGES        = "scmlastcommit.table.commits.changes";                      //$NON-NLS-1$
  String SCMLASTCOMMIT_TABLE_COMMITS_COMMENT        = "scmlastcommit.table.commits.comment";                      //$NON-NLS-1$

  // Widget Module
  String WIDGET_I18N_PREFIX      = "widget.";                                                  //$NON-NLS-1$
  String WIDGET_I18N_NAME_SUFFIX = ".name";                                                    //$NON-NLS-1$
  String WIDGET_I18N_DESC_SUFFIX = ".desc";                                                    //$NON-NLS-1$

  // Public
  String PUBLIC_USERNAME                            = "public.username";                                          //$NON-NLS-1$
  String PUBLIC_PASSWORD                            = "public.password";                                          //$NON-NLS-1$
  String PUBLIC_LOGIN_LABEL                         = "public.login.label";                                       //$NON-NLS-1$
  String PUBLIC_REGISTER_LABEL                      = "public.register.label";                                    //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LOGIN                 = "public.register.form.login";                               //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LOGIN_TOOLTIP         = "public.register.form.login.tooltip";                       //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_FIRSTNAME             = "public.register.form.firstname";                           //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_FIRSTNAME_TOOLTIP     = "public.register.form.firstname.tooltip";                   //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LASTNAME              = "public.register.form.lastname";                            //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LASTNAME_TOOLTIP      = "public.register.form.lastname.tooltip";                    //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_EMAIL                 = "public.register.form.email";                               //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_EMAIL_TOOLTIP         = "public.register.form.email.tooltip";                       //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LANGUAGE              = "public.register.form.language";                            //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LANGUAGE_TOOLTIP      = "public.register.form.language.tooltip";                    //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LANGUAGE_FR           = "public.register.form.language.fr";                         //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LANGUAGE_EN           = "public.register.form.language.en";                         //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_LANGUAGE_ERROR        = "public.register.form.password.error";                      //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_PASSWORD              = "public.register.form.password";                            //$NON-NLS-1$
  String PUBLIC_REGISTER_FORM_PASSWORD_TOOLTIP      = "public.register.form.password.tooltip";                    //$NON-NLS-1$
  String PUBLIC_REGISTER_CONFIRM_WINDOW_TITLE       = "public.register.confirm.window.title";                     //$NON-NLS-1$
  String PUBLIC_REGISTER_CONFIRM_WINDOW_LABEL_INFO  = "public.register.confirm.window.label.info";                //$NON-NLS-1$
  String PUBLIC_REGISTER_CONFIRM_WINDOW_LABEL_LOGIN = "public.register.confirm.window.label.login";               //$NON-NLS-1$
  String PUBLIC_REGISTER_CONFIRM_WINDOW_LABEL_EMAIL = "public.register.confirm.window.label.email";               //$NON-NLS-1$
  String PUBLIC_REGISTER_CONFIRM_WINDOW_BUTTON      = "public.register.confirm.window.button";                    //$NON-NLS-1$
  String PUBLIC_NOVAFORGE_DESCRIPTION               = "public.novaforge.description";                             //$NON-NLS-1$
  String PUBLIC_LOGIN_LOSTACCOUNT                   = "public.login.lostaccount";                                 //$NON-NLS-1$

  // Private
  String WORKSPACE_HOME                       = "private.workspace.home";                                   //$NON-NLS-1$
  String PRIVATE_APPLICATION_UNAVAILABLE      = "private.application.unavailable";                          //$NON-NLS-1$
  String PRIVATE_APPLICATION_ON_ERROR         = "private.application.on_error";                             //$NON-NLS-1$
  String PRIVATE_APPLICATION_INPROGRESS       = "private.application.inprogess";                            //$NON-NLS-1$
  String PRIVATE_APPLICATION_DELETEINPROGRESS = "private.application.deleteinprogress";                     //$NON-NLS-1$
  String PRIVATE_APPLICATION_PENDING          = "private.application.pending";                              //$NON-NLS-1$

  // Common Events
  String EVENT_OPEN_PROJECT = "event.open.project";                                       //$NON-NLS-1$

  // Header
  String PROJECT_NAVIGATION         = "header.tree";                                              //$NON-NLS-1$
  String PROJECT_SELECT             = "header.select";                                            //$NON-NLS-1$
  String PROJECT_SELECT_INPUT       = "header.select.input";                                      //$NON-NLS-1$
  String PROJECT_SELECT_DESCRIPTION = "header.select.description";                                //$NON-NLS-1$
  String WARNING_TITLE              = "header.warning.title";                                     //$NON-NLS-1$
  String LOCALE_REFRESH             = "header.warning.locale.refresh";                            //$NON-NLS-1$

  // Footer
  String FOOTER_NOVAFORGE = "footer.novaforge";                                         //$NON-NLS-1$
  String FOOTER_BULL      = "footer.bull";                                              //$NON-NLS-1$

  // Dashboard
  String DASHBOARD_TITLE                          = "dashboard.title";                                          //$NON-NLS-1$
  String DASHBOARD_DESCRIPTION                    = "dashboard.description";                                    //$NON-NLS-1$
  String DASHBOARD_TAB_DEFAULTNAME                = "dashboard.tab.defaultname";                                //$NON-NLS-1$
  String DASHBOARD_TAB_TITLE                      = "dashboard.tab.title";                                      //$NON-NLS-1$
  String DASHBOARD_TAB_NAME                       = "dashboard.tab.name";                                       //$NON-NLS-1$
  String DASHBOARD_TAB_NAME_REQUIRED              = "dashboard.tab.name.required";                              //$NON-NLS-1$
  String DASHBOARD_TAB_ICON_TITLE                 = "dashboard.tab.icon.title";                                 //$NON-NLS-1$
  String DASHBOARD_TAB_DELETE                     = "dashboard.tab.delete";                                     //$NON-NLS-1$
  String DASHBOARD_TAB_DELETE_DESCRIPTION         = "dashboard.tab.delete.description";                         //$NON-NLS-1$
  String DASHBOARD_TAB_DELETE_CONFIRMLABEL        = "dashboard.tab.delete.confirmlabel";                        //$NON-NLS-1$
  String DASHBOARD_TAB_LAYOUT_TITLE               = "dashboard.tab.layout.title";                               //$NON-NLS-1$
  String DASHBOARD_TAB_LAYOUT_BOXES               = "dashboard.tab.layout.boxes";                               //$NON-NLS-1$
  String DASHBOARD_TAB_LAYOUT_SELECT_TITLE        = "dashboard.tab.layout.select.title";                        //$NON-NLS-1$
  String DASHBOARD_TAB_LAYOUT_SELECT_CONFIRMLABEL = "dashboard.tab.layout.select.confirmlabel";                 //$NON-NLS-1$
  String DASHBOARD_TAB_LAYOUT_SELECT_CONFIRM      = "dashboard.tab.layout.select.confirm";                      //$NON-NLS-1$
  String DASHBOARD_TAB_WIDGET_ADD                 = "dashboard.tab.widget.add";                                 //$NON-NLS-1$
  String DASHBOARD_TAB_WIDGET_ADD_DESCRIPTION     = "dashboard.tab.widget.add.description";                     //$NON-NLS-1$
  String DASHBOARD_WIDGET_DELETE_TITLE            = "dashboard.widget.delete.title";                            //$NON-NLS-1$
  String DASHBOARD_WIDGET_DELETE_CONFIRMLABEL     = "dashboard.widget.delete.confirmlabel";                     //$NON-NLS-1$
  String DASHBOARD_WIDGET_DELETE_CONFIRM          = "dashboard.widget.delete.confirm";                          //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_TITLE                = "dashboard.addwidget.title";                                //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_FILTER               = "dashboard.addwidget.filter";                               //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_COMBOBOX             = "dashboard.addwidget.combobox";                             //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_COMBOBOX_ALL         = "dashboard.addwidget.combobox.all";                         //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_FINISH               = "dashboard.addwidget.finish";                               //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_BUTTON               = "dashboard.addwidget.addbutton";                            //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_LAYOUT_DESCRIPTION   = "dashboard.addwidget.layout.description";                   //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_BUTTON_DESCRIPTION   = "dashboard.addwidget.addbutton.description";                //$NON-NLS-1$
  String DASHBOARD_WIDGETDETAILS_TITLE            = "dashboard.widgetdetails.title";                            //$NON-NLS-1$
  String DASHBOARD_WIDGETDETAILS_CATEGORIES       = "dashboard.widgetdetails.categories";                       //$NON-NLS-1$
  String DASHBOARD_WIDGET_NOPREVIEW               = "dashboard.widget.nopreview";                               //$NON-NLS-1$
  String DASHBOARD_WIDGET_CATEGORY_ALL            = "dashboard.widget.category.all";                            //$NON-NLS-1$
  String DASHBOARD_WIDGET_CATEGORY_NONE           = "dashboard.widget.category.none";                           //$NON-NLS-1$
  String DASHBOARD_WIDGET_DETAIL_CATEGORY_TOOLTIP = "dashboard.widget.detail.category.tooltip";                 //$NON-NLS-1$
  String DASHBOARD_ADDWIDGET_SEARCH_INPUTPROMPT   = "dashboard.addwidget.search.inputprompt";                   //$NON-NLS-1$
  String DASHBOARD_SETTINGS_MENU_TITLE            = "dashboard.settings.menu.title";                            //$NON-NLS-1$
  String DASHBOARD_SETTINGS_MENU_DATASOURCE       = "dashboard.settings.menu.datasource";                       //$NON-NLS-1$
  String DASHBOARD_SETTINGS_MENU_PROPERTIES       = "dashboard.settings.menu.properties";                       //$NON-NLS-1$

  String DASHBOARD_SETTINGS_WIDGET_INFO_TITLE            = "dashboard.settings.widget.info.title";                     //$NON-NLS-1$
  String DASHBOARD_SETTINGS_WIDGET_INFO_NAME             = "dashboard.settings.widget.info.name";                      //$NON-NLS-1$
  String DASHBOARD_SETTINGS_WIDGET_INFO_NAME_DESCRIPTION = "dashboard.settings.widget.info.name.description";          //$NON-NLS-1$
  String DASHBOARD_SETTINGS_WIDGET_INFO_NAME_VALIDATOR   = "dashboard.settings.widget.info.name.validator";            //$NON-NLS-1$
  String DASHBOARD_SETTINGS_WIDGET_PROPERTIES_TITLE      = "dashboard.settings.widget.properties.title";               //$NON-NLS-1$
  String DASHBOARD_SETTINGS_WIDGET_PROPERTIES_VALIDATOR  = "dashboard.settings.widget.properties.validator";           //$NON-NLS-1$
  String DASHBOARD_SETTINGS_SOURCE_FILTER_APPS           = "dashboard.settings.source.filter.apps";                    //$NON-NLS-1$
  String DASHBOARD_SETTINGS_SOURCE_FILTER_PROJECTS       = "dashboard.settings.source.filter.projects";                //$NON-NLS-1$
  String DASHBOARD_SETTINGS_SOURCE_FILTER_PROJECTS_INPUT = "dashboard.settings.source.filter.projects.input";          //$NON-NLS-1$
  String DASHBOARD_SETTINGS_SOURCE_SELECTED_TITLE        = "dashboard.settings.source.selected.title";                 //$NON-NLS-1$

  // Change Pwd
  String CHANGEPWD_TITLE                = "changepwd.title";                                          //$NON-NLS-1$
  String CHANGEPWD_VALIDITY_DESCRIPTION = "changepwd.validity.description";                           //$NON-NLS-1$
  String CHANGEPWD_RECOVERY_DESCRIPTION = "changepwd.recovery.description";                           //$NON-NLS-1$
  String CHANGEPWD_RECOVERY_LINK        = "changepwd.recovery.homelink";                              //$NON-NLS-1$
  String CHANGEPWD_RECOVERY_TICKET      = "changepwd.recovery.ticket";                                //$NON-NLS-1$
  String CHANGEPWD_RECOVERY_REDIRECT    = "changepwd.recovery.redirect";                              //$NON-NLS-1$
  String CHANGEPWD_FORM_APPLY           = "changepwd.form.apply";                                     //$NON-NLS-1$
  String CHANGEPWD_FORM_LOGIN           = "changepwd.form.login";                                     //$NON-NLS-1$
  String CHANGEPWD_FORM_LOGIN_TOOLTIP   = "changepwd.form.login.tooltip";                             //$NON-NLS-1$
  String CHANGEPWD_FORM_CURRENT         = "changepwd.form.current";                                   //$NON-NLS-1$
  String CHANGEPWD_FORM_CURRENT_TOOLTIP = "changepwd.form.current.tooltip";                           //$NON-NLS-1$
  String CHANGEPWD_FORM_NEW             = "changepwd.form.new";                                       //$NON-NLS-1$
  String CHANGEPWD_FORM_NEW_TOOLTIP     = "changepwd.form.new.tooltip";                               //$NON-NLS-1$
  String CHANGEPWD_FORM_RENEW           = "changepwd.form.renew";                                     //$NON-NLS-1$
  String CHANGEPWD_FORM_RENEW_TOOLTIP   = "changepwd.form.renew.tooltip";                             //$NON-NLS-1$
  String CHANGEPWD_FORM_RENEW_ERROR     = "changepwd.form.renew.error";                               //$NON-NLS-1$

  // Recovery
  String RECOVERY_TITLE       	= "recoverypwd.title";                                        //$NON-NLS-1$
  String RECOVERY_DESC        	= "recoverypwd.description";                                  //$NON-NLS-1$
  String RECOVERY_RESULT      	= "recoverypwd.result";                                       //$NON-NLS-1$
  String RECOVERY_APPLY       	= "recoverypwd.apply";                                        //$NON-NLS-1$
  String RECOVERY_EMAIL_TITLE 	= "recoverypwd.error.email.title";                            //$NON-NLS-1$
  String RECOVERY_EMAIL_DESC    = "recoverypwd.error.email.desc";                             //$NON-NLS-1$
  String RECOVERY_REQUEST_TITLE = "recoverypwd.warning.request.title";
  String RECOVERY_REQUEST_DESC  = "recoverypwd.warning.request.desc";

  // Projects
  String PROJECT_CREATE_APPLY                        = "project.create.apply";
  String PROJECT_UPDATE_APPLY                        = "project.update.apply";                                     //$NON-NLS-1$
  String PROJECT_CANCEL                              = "project.cancel";                                           //$NON-NLS-1$
  String PROJECT_EDIT                                = "project.edit";                                             //$NON-NLS-1$
  String PROJECT_DELETE                              = "project.delete";                                           //$NON-NLS-1$
  String PROJECT_FORM_SUCCESS                        = "project.success";                                          //$NON-NLS-1$
  String PROJECT_UPDATE_SUCCESS                      = "project.update.success";
  String PROJECT_MAIN_TITLE                          = "project.main.title";                                       //$NON-NLS-1$
  String PROJECT_MAIN_NAME_CAPTION                   = "project.main.name.caption";                                //$NON-NLS-1$
  String PROJECT_MAIN_NAME_REQUIRE                   = "project.main.name.require";                                //$NON-NLS-1$
  String PROJECT_MAIN_NAME_VALIDATION                = "project.main.name.validation";                             //$NON-NLS-1$
  String PROJECT_MAIN_ID_CAPTION                     = "project.main.id.caption";                                  //$NON-NLS-1$
  String PROJECT_MAIN_ID_REQUIRE                     = "project.main.id.require";                                  //$NON-NLS-1$
  String PROJECT_MAIN_ID_VALIDATION                  = "project.main.id.validation";                               //$NON-NLS-1$
  String PROJECT_MAIN_DESCRIPTION_CAPTION            = "project.main.description.caption";                         //$NON-NLS-1$
  String PROJECT_MAIN_DESCRIPTION_REQUIRE            = "project.main.description.require";                         //$NON-NLS-1$
  String PROJECT_MAIN_DESCRIPTION_VALIDATION         = "project.main.description.validation";                      //$NON-NLS-1$
  String PROJECT_MAIN_VISIBILITY_CAPTION             = "project.main.visibility.caption";
  String PROJECT_MAIN_ICON_TITLE                     = "project.main.icon.title";                                  //$NON-NLS-1$
  String PROJECT_MAIN_ICON_CONFIRM                   = "project.main.icon.confirm";                                //$NON-NLS-1$
  String PROJECT_MAIN_ICON_HELP                      = "project.main.icon.help";                                   //$NON-NLS-1$
  String PROJECT_MAIN_ICON_UPLOAD_CAPTION            = "project.main.icon.upload.caption";                         //$NON-NLS-1$
  String PROJECT_MAIN_ICON_UPLOAD_DESCRIPTION        = "project.main.icon.upload.description";                     //$NON-NLS-1$
  String PROJECT_ADD_TITLE                           = "project.add.title";                                        //$NON-NLS-1$
  String PROJECT_ADD_ORGANISM_CAPTION                = "project.add.organism.caption";                             //$NON-NLS-1$
  String PROJECT_ADD_ORGANISM_PROMPT                 = "project.add.organism.prompt";                              //$NON-NLS-1$
  String PROJECT_ADD_ORGANISM_TITLE                  = "project.add.organism.title";                               //$NON-NLS-1$
  String PROJECT_ADD_ORGANISM_VALIDATION             = "project.add.organism.validation";                          //$NON-NLS-1$
  String PROJECT_ADD_LICENSE_CAPTION                 = "project.add.license.caption";                              //$NON-NLS-1$
  String PROJECT_TEMPLATE_TITLE                      = "project.template.title";                                   //$NON-NLS-1$
  String PROJECT_TEMPLATE_CAPTION                    = "project.template.caption";                                 //$NON-NLS-1$
  String PROJECT_TEMPLATE_NULL                       = "project.template.null";
  // Administration projects
  String PROJECT_ADMIN_MENU_TITLE                    = "project.admin.menu.title";                                 //$NON-NLS-1$
  String PROJECT_ADMIN_MENU_PROJECTS                 = "project.admin.menu.projects";                              //$NON-NLS-1$
  String PROJECT_ADMIN_MENU_REJECTED                 = "project.admin.menu.rejected";                              //$NON-NLS-1$
  String PROJECT_ADMIN_MENU_REQUESTS                 = "project.admin.menu.requests";                              //$NON-NLS-1$
  String PROJECT_ADMIN_FILTER                        = "project.admin.filter";                                     //$NON-NLS-1$
  String PROJECT_ADMIN_PROJECT_ID                    = "project.admin.project.id";                                 //$NON-NLS-1$
  String PROJECT_ADMIN_PROJECT_NAME                  = "project.admin.project.name";                               //$NON-NLS-1$
  String PROJECT_ADMIN_PROJECT_DESCRIPTION           = "project.admin.project.description";                        //$NON-NLS-1$
  String PROJECT_ADMIN_PROJECT_AUTHOR                = "project.admin.project.author";                             //$NON-NLS-1$
  String PROJECT_ADMIN_PROJECT_DATE                  = "project.admin.project.date";                               //$NON-NLS-1$
  String PROJECT_ADMIN_WAITING_TITLE                 = "project.admin.waiting.title";                              //$NON-NLS-1$
  String PROJECT_ADMIN_VALID_TITLE                   = "project.admin.valid.title";                                //$NON-NLS-1$
  String PROJECT_ADMIN_VALID_CONFIRMLABEL            = "project.admin.valid.confirmlabel";                         //$NON-NLS-1$
  String PROJECT_ADMIN_VALID_CONFIRM                 = "project.admin.valid.confirm";                              //$NON-NLS-1$
  String PROJECT_ADMIN_INVALID_TITLE                 = "project.admin.invalid.title";                              //$NON-NLS-1$
  String PROJECT_ADMIN_INVALID_CONFIRMLABEL          = "project.admin.invalid.confirmlabel";                       //$NON-NLS-1$
  String PROJECT_ADMIN_INVALID_EXPLANATION           = "project.admin.invalid.explanation";                        //$NON-NLS-1$
  String PROJECT_ADMIN_INVALID_CONFIRM               = "project.admin.invalid.confirm";                            //$NON-NLS-1$
  String PROJECT_ADMIN_INVALID_DESCRIPTION           = "project.admin.invalid.description";                        //$NON-NLS-1$
  String PROJECT_ADMIN_REJECTED                      = "project.admin.rejected";                                   //$NON-NLS-1$
  String PROJECT_ADMIN_REJECTED_TITLE                = "project.admin.rejected.title";                             //$NON-NLS-1$
  String PROJECT_ADMIN_REJECTED_DESCRIPTION          = "project.admin.rejected.description";                       //$NON-NLS-1$
  String PROJECT_ADMIN_VALIDATED_TITLE               = "project.admin.validated.title";                            //$NON-NLS-1$
  String PROJECT_ADMIN_VALIDATED_OPEN                = "project.admin.validated.open";                             //$NON-NLS-1$
  String PROJECT_ADMIN_VALIDATED_EDIT                = "project.admin.validated.edit";                             //$NON-NLS-1$
  String PROJECT_ADMIN_VALIDATED_DELETE              = "project.admin.validated.delete";                           //$NON-NLS-1$
  String PROJECT_ADMIN_VALIDATED_DELETE_CONFIRM      = "project.admin.validated.delete.confirm";                   //$NON-NLS-1$
  String PROJECT_ADMIN_VALIDATED_DELETE_CONFIRMLABEL = "project.admin.validated.delete.confirmlabel";              //$NON-NLS-1$

  // Users Management
  String USERMANAGEMENT_USER_ROLE                        = "usermanagement.user.role";                                 //$NON-NLS-1$
  String USERMANAGEMENT_USER_ROLE_GROUP                  = "usermanagement.user.role.group";                           //$NON-NLS-1$
  String USERMANAGEMENT_ADMIN_WINDOW_TITLE               = "usermanagement.title.users.admin";                         //$NON-NLS-1$
  String USERMANAGEMENT_PROFILE_WINDOW_TITLE             = "usermanagement.title.user.profile";                        //$NON-NLS-1$
  String USERMANAGEMENT_PROFILE_TAB_TITLE                = "usermanagement.title.user.profile.1";                      //$NON-NLS-1$
  String USERMANAGEMENT_ACCOUNT_WINDOW_TITLE             = "usermanagement.title.user.account";                        //$NON-NLS-1$
  String USERMANAGEMENT_ACTION_DELETE_USER               = "usermanagement.action.delete.user";                        //$NON-NLS-1$
  String USERMANAGEMENT_ACTION_EDIT_USER                 = "usermanagement.action.edit.user";                          //$NON-NLS-1$
  String USERMANAGEMENT_ACTION_VIEW_USER                 = "usermanagement.action.view.user";                          //$NON-NLS-1$
  String USERMANAGEMENT_DELETE_USER_CONFIRM              = "usermanagement.delete.user.confirm";                       //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_LOGIN                      = "usermanagement.field.login";                               //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_CREATED_DATE               = "usermanagement.field.created.date";                        //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_FIRSTNAME                  = "usermanagement.field.firstname";                           //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_FIRSTNAME_REQUIRED         = "usermanagement.field.firstname.required";                  //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_LASTNAME                   = "usermanagement.field.lastname";                            //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_LASTNAME_REQUIRED          = "usermanagement.field.lastname.required";                   //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_EMAIL                      = "usermanagement.field.email";                               //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_ACTIONS                    = "usermanagement.field.actions";                             //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_LANGUAGE                   = "usermanagement.field.language";                            //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_PHONE_WORK                 = "usermanagement.field.phone.work";                          //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_PHONE_WORK_VALIDATION      = "usermanagement.field.phone.work.validation";               //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_PHONE_MOBILE               = "usermanagement.field.phone.mobile";                        //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_PHONE_MOBILE_VALIDATION    = "usermanagement.field.phone.mobile.validation";             //$NON-NLS-1$
  String USERMANAGEMENT_IS_FIELD_PUBLIC                  = "usermanagement.is.field.public";                           //$NON-NLS-1$
  String USERMANAGEMENT_ERROR_TECH_TITLE                 = "usermanagement.error.tech.title";
  String USERMANAGEMENT_ERROR_SAVE_USER                  = "usermanagement.error.tech.title";                          //$NON-NLS-1$
  String USERMANAGEMENT_ERROR_DELETE_USER                = "usermanagement.error.delete.user";                         //$NON-NLS-1$
  String USERMANAGEMENT_ERROR_GET_ALL_USERS              = "usermanagement.error.get.all.users";                       //$NON-NLS-1$
  String USERMANAGEMENT_FILTER_FIELDS                    = "usermanagement.filter.fields";                             //$NON-NLS-1$
  String USERMANAGEMENT_SECTION_INFOS                    = "usermanagement.section.infos";                             //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_COMPANY_NAME               = "usermanagement.field.company.name";                        //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_COMPANY_ADDRESS            = "usermanagement.field.company.address";                     //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_COMPANY_OFFICE             = "usermanagement.field.company.office";                      //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_NULL                       = "usermanagement.field.null";                                //$NON-NLS-1$
  String USERMANAGEMENT_IS_MY_PROJECTS_PUBLIC            = "usermanagement.is.myprojects.public";                      //$NON-NLS-1$
  String USERMANAGEMENT_UPDATE_PASSWD_BUTTON             = "usermanagement.update.password";                           //$NON-NLS-1$
  String USERMANAGEMENT_FIELD_EMAIL_VALIDATOR            = "usermanagement.email.validator";                           //$NON-NLS-1$
  String USERMANAGEMENT_PWD_MATCHING                     = "usermanagement.pwd.matching";                              //$NON-NLS-1$
  String USERMANAGEMENT_PWD_DONT_MATCHING                = "usermanagement.pwd.dont.matching";                         //$NON-NLS-1$
  String USERMANAGEMENT_PWD_REGEX_VALID                  = "usermanagement.pwd.regex.valid";                           //$NON-NLS-1$
  String USERMANAGEMENT_PWD_REGEX_DONT_VALID             = "usermanagement.pwd.regex.dont.valid";                      //$NON-NLS-1$
  String USERMANAGEMENT_PWD_UPDATE_SUCCESS               = "usermanagement.pwd.update.success";                        //$NON-NLS-1$
  String USERMANAGEMENT_PWD_UPDATE_OLDKO_TITLE           = "usermanagement.pwd.update.oldko.title";                    //$NON-NLS-1$
  String USERMANAGEMENT_PWD_UPDATE_OLDKO_TEXT            = "usermanagement.pwd.update.oldko.text";                     //$NON-NLS-1$
  String USERMANAGEMENT_BACK_USERS_ADMIN                 = "usermanagement.back.users.admin";                          //$NON-NLS-1$
  String USERMANAGEMENT_ADD_SECTION                      = "usermanagement.add.section";                               //$NON-NLS-1$
  String USERMANAGEMENT_ADD_SECTION_WINDOW_HELP          = "usermanagement.add.section.window.help";                   //$NON-NLS-1$
  String USERMANAGEMENT_ADD_SECTION_COMBOBOX             = "usermanagement.add.section.window.combobox";
  String USERMANAGEMENT_ADD_SECTION_COMBOBOX_INPUTPROMPT = "usermanagement.add.section.combobox.inputprompt";          //$NON-NLS-1$
  String USERMANAGEMENT_UPDATE_PICTURE                   = "usermanagement.update.picture";                            //$NON-NLS-1$
  String USERMANAGEMENT_UPDATE_PICTURE_HELP              = "usermanagement.update.picture.help";                       //$NON-NLS-1$
  String USERMANAGEMENT_UPLOAD_CAPTION                   = "usermanagement.upload.caption";                            //$NON-NLS-1$
  String USERMANAGEMENT_UPLOAD_BUTTON                    = "usermanagement.upload.button";                             //$NON-NLS-1$
  String USERMANAGEMENT_UPLOAD_DESCRIPTION               = "usermanagement.upload.description";                        //$NON-NLS-1$
  String USERMANAGEMENT_CREATE_USER                      = "usermanagement.create.user";                               //$NON-NLS-1$
  String USERMANAGEMENT_UPDATE_SECURITY_RULES            = "usermanagement.update.security.rules";                     //$NON-NLS-1$
  String USERMANAGEMENT_UPDATE_SECURITY_RULES_SUCCESS    = "usermanagement.update.security.rules.success";             //$NON-NLS-1$
  String USERMANAGEMENT_RULE_HELP                        = "usermanagement.rule.help";                                 //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_LIFETIME                = "usermanagement.rule.pwd.lifetime";                         //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_LIFETIME_CAPTION        = "usermanagement.rule.pwd.lifetime.caption";                 //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_LIFETIME_VALIDATOR      = "usermanagement.rule.pwd.lifetime.validator";               //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_ALERTTIME               = "usermanagement.rule.pwd.alerttime";                        //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_ALERTTIME_CAPTION       = "usermanagement.rule.pwd.alerttime.caption";                //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_ALERTTIME_VALIDATOR     = "usermanagement.rule.pwd.alerttime.validator";              //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_FORMAT                  = "usermanagement.rule.pwd.format";                           //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_FORMAT_CAPTION          = "usermanagement.rule.pwd.format.caption";                   //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_FORMAT_DESC_CAPTION     = "usermanagement.rule.pwd.format.desc.caption";              //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_FORMAT_SELECTONE        = "usermanagement.rule.pwd.format.selectone";                 //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_FORMAT_CUSTOM           = "usermanagement.rule.pwd.format.custom";                    //$NON-NLS-1$
  String USERMANAGEMENT_RULE_PWD_FORMAT_TESTIT           = "usermanagement.rule.pwd.format.testit";                    //$NON-NLS-1$
  String USERMANAGEMENT_RANGE_HOUR                       = "usermanagement.periodrange.hour";                          //$NON-NLS-1$
  String USERMANAGEMENT_RANGE_DAY                        = "usermanagement.periodrange.day";                           //$NON-NLS-1$
  String USERMANAGEMENT_RANGE_WEEK                       = "usermanagement.periodrange.week";                          //$NON-NLS-1$
  String USERMANAGEMENT_RANGE_MONTH                      = "usermanagement.periodrange.month";                         //$NON-NLS-1$
  String USERMANAGEMENT_RANGE_YEAR                       = "usermanagement.periodrange.year";                          //$NON-NLS-1$
  String USERMANAGEMENT_TESTPWD_UPDATEANDCLOSE           = "usermanagement.testpwd.updateandclose";                    //$NON-NLS-1$
  String USERMANAGEMENT_TESTPWD_RESULT                   = "usermanagement.testpwd.result";                            //$NON-NLS-1$
  String USERMANAGEMENT_TESTPWD_RESULT_OK                = "usermanagement.testpwd.result.ok";
  String USERMANAGEMENT_TESTPWD_RESULT_KO                = "usermanagement.testpwd.result.ko";
  String USERMANAGEMENT_TESTPWD_PASSWORDTOTEST           = "usermanagement.testpwd.passwordtotest";                    //$NON-NLS-1$
  String USERMANAGEMENT_TESTPWD_CUSTOMFORMAT             = "usermanagement.testpwd.customformat";
  String USERMANAGEMENT_ADMIN_MENU_TITLE                 = "usermanagement.admin.menu.title";
  String USERMANAGEMENT_ADMIN_MENU_USERS                 = "usermanagement.admin.menu.users";
  String USERMANAGEMENT_ADMIN_MENU_BLACKLIST             = "usermanagement.admin.menu.blacklist";
  String USERMANAGEMENT_ADMIN_TITLE_BLACKLIST            = "usermanagement.admin.title.blacklist";
  String USERMANAGEMENT_ADMIN_MENU_SECURITY              = "usermanagement.admin.menu.security";
  String USERMANAGEMENT_ADMIN_TITLE_USERS                = "usermanagement.admin.title.users";
  String USERMANAGEMENT_RULE_PWD_FORMAT_INPUTPROMPT      = "usermanagement.rule.pwd.format.inputprompt";

  String USERMANAGEMENT_ADMIN_SECURITY_USERACCOUNT             = "usermanagement.admin.security.useraccount";
  String USERMANAGEMENT_ADMIN_SECURITY_USERACCOUNT_CAPTION     = "usermanagement.admin.security.useraccount.caption";
  String USERMANAGEMENT_ADMIN_SECURITY_USERACCOUNT_DESCRIPTION = "usermanagement.admin.security.useraccount.description";

  // Public project
  String PUBLIC_ITEM_VALUE_UNKNOW                   = "public.item.value.unknow";                                 //$NON-NLS-1$
  String PUBLIC_PROJECT_WINDOW_TITLE                = "public.project.window.title";                              //$NON-NLS-1$
  String PUBLIC_PROJECT_COMBOBOX_TITLE              = "public.project.combobox.title";                            //$NON-NLS-1$
  String PUBLIC_PROJECT_COMBOBOX_EMPTY              = "public.project.combobox.empty";                            //$NON-NLS-1$
  String PUBLIC_PROJECT_DETAILS_TITLE               = "public.project.details.title";                             //$NON-NLS-1$
  String PUBLIC_PROJECT_FIELD_LICENCE               = "public.project.field.licence";                             //$NON-NLS-1$
  String PUBLIC_PROJECT_FIELD_ORGANIZATION          = "public.project.field.organization";                        //$NON-NLS-1$
  String PUBLIC_PROJECT_FIELD_ORGANIZATION_EMPTY    = "public.project.field.organization.empty";                  //$NON-NLS-1$
  String PUBLIC_PROJECT_FIELD_DATE                  = "public.project.field.date";                                //$NON-NLS-1$
  String PUBLIC_PROJECT_FIELD_AUTHOR                = "public.project.field.author";                              //$NON-NLS-1$
  String PUBLIC_PROJECT_REQUEST_ACTION              = "public.project.request.action";                            //$NON-NLS-1$
  String PUBLIC_PROJECT_REQUEST_EXIST               = "public.project.request.exist";                             //$NON-NLS-1$
  String PUBLIC_PROJECT_REQUEST_SEND_HELP1          = "public.request.send.help.1";                               //$NON-NLS-1$
  String PUBLIC_PROJECT_REQUEST_SEND_HELP2          = "public.request.send.help.2";                               //$NON-NLS-1$
  String PUBLIC_PROJECT_REQUEST_SEND_MESSAGE_TITLE  = "public.request.send.message.title";                        //$NON-NLS-1$
  String PUBLIC_PROJECT_SEND_ACTION                 = "public.request.send.action";                               //$NON-NLS-1$
  String PUBLIC_PROJECT_SEND_ACTION_SUCCESS         = "public.request.send.action.success";                       //$NON-NLS-1$
  String PUBLIC_PROJECT_SEND_ACTION_FAILURE         = "public.request.send.action.failure";                       //$NON-NLS-1$
  String PUBLIC_PROJECT_ERROR_GETPUBLICPROJECT      = "public.error.getpublicproject";                            //$NON-NLS-1$
  String PUBLIC_PROJECT_ERROR_GETUSERPROJECTREQUEST = "public.error.getuserprojectrequest";                       //$NON-NLS-1$
  String PUBLIC_PROJECT_ERROR_PROJECTSELECTED_TITLE = "public.error.projectselected.title";                       //$NON-NLS-1$
  String PUBLIC_PROJECT_ERROR_PROJECTSELECTED_DESC  = "public.error.projectselected.desc";                        //$NON-NLS-1$
  String PUBLIC_REQUEST_IN_PROGRESS                 = "public.request.in.progress";                               //$NON-NLS-1$
  String PUBLIC_PROJECT_PROFILE_TAB_TITLE           = "public.project.profile.tab.title";                         //$NON-NLS-1$
  String PUBLIC_PROJECT_REQUEST_MESSAGE_LENGTH      = "public.project.request.message.length";                    //$NON-NLS-1$

  // Plugins management
  String PLUGINSMANAGEMENT_ERROR_TITLE                          = "pluginsmanagement.error.title";                            //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LOADING_PLUGINS                = "pluginsmanagement.error.loading.plugins";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LOADING_STATUS                 = "pluginsmanagement.error.loading.status";                   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LOADING_CATEGORY               = "pluginsmanagement.error.loading.category";                 //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_CHANGING_STATUS                = "pluginsmanagement.error.changing.status";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_NEWSTATUS_EMTPY                = "pluginsmanagement.error.newstatus.empty";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LOADING_INSTANCES              = "pluginsmanagement.error.loading.instances";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_CREATING_INSTANCE              = "pluginsmanagement.error.creating.instance";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LINKING_INSTANCE               = "pluginsmanagement.error.linking.instance";                 //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_DELETING_INSTANCE              = "pluginsmanagement.error.deleting.instance";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_UPDATING_INSTANCE              = "pluginsmanagement.error.updating.instance";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LOADING_PLUGIN_DESC            = "pluginsmanagement.error.loading.plugin.desc";              //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LOADING_REQUESTS               = "pluginsmanagement.error.loading.requests";                 //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_DELETING_REQUESTS              = "pluginsmanagement.error.deleting.requests";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_PLUGIN_ADMINURI                = "pluginsmanagement.error.plugin.adminuri";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_EDITING_INSTANCE               = "pluginsmanagement.error.instance.edit";                    //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LOADING_INSTANCES_APPLICATIONS = "pluginsmanagement.error.loading.instances.applications";   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_LOADING_INSTANCES_ADMINVIEW    = "pluginsmanagement.error.loading.instances.adminview";      //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ERROR_UNSUPPORTED_STATUS             = "pluginsmanagement.error.unsupported.status";               //$NON-NLS-1$
  String PLUGINSMANAGEMENT_WARNING_TITLE                        = "pluginsmanagement.warning.title";                          //$NON-NLS-1$
  String PLUGINSMANAGEMENT_WARNING_PIC_REMEMBER                 = "pluginsmanagement.warning.pic.remember";                   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_WINDOW_TITLE                         = "pluginsmanagement.window.title";                           //$NON-NLS-1$
  String PLUGINSMANAGEMENT_RETURN_PLUGINS_LIST                  = "pluginsmanagement.return.plugins.list";                    //$NON-NLS-1$
  String PLUGINSMANAGEMENT_GOTO_INSTANCES_LIST                  = "pluginsmanagement.goto.instances.list";                    //$NON-NLS-1$
  String PLUGINSMANAGEMENT_GOTO_REQUESTS_LIST                   = "pluginsmanagement.goto.requests.list";                     //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_NAME                           = "pluginsmanagement.field.name";                             //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_NAME_REQUIRED                  = "pluginsmanagement.field.name.required";                    //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_DESCRIPTION                    = "pluginsmanagement.field.description";                      //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_ALIAS                          = "pluginsmanagement.field.alias";                            //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_ALIAS_REQUIRED                 = "pluginsmanagement.field.alias.required";                   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_BASEURL                        = "pluginsmanagement.field.baseurl";                          //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_BASEURL_REQUIRED               = "pluginsmanagement.field.baseurl.required";                 //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_APPLICATION                    = "pluginsmanagement.field.application";                      //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_VERSION                        = "pluginsmanagement.field.version";                          //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_CATEGORY                       = "pluginsmanagement.field.category";                         //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_STATUS                         = "pluginsmanagement.field.status";                           //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_SHAREABLE                      = "pluginsmanagement.field.shareable";                        //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_SHAREABLE_UNAVAILABLE          = "pluginsmanagement.field.shareable.unavailable";            //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_STATUS_OK_DESC                 = "pluginsmanagement.field.status.ok.desc";                   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_STATUS_KO_DESC                 = "pluginsmanagement.field.status.ko.desc";                   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_ACTIONS                        = "pluginsmanagement.field.actions";                          //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_PROJECT                        = "pluginsmanagement.field.project";                          //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_CREATEDDATE                    = "pluginsmanagement.field.createddate";                      //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FIELD_LOGIN                          = "pluginsmanagement.field.login";                            //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FILTERS_INTRO                        = "pluginsmanagement.filters.intro";                          //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FILTERS_ALL                          = "pluginsmanagement.filters.combobox.all";                   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FILTER_EVENT                         = "pluginsmanagement.filter.event";                           //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FILTERS_RESET_BUTTON                 = "pluginsmanagement.filters.reset.button";                   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FILTERS_EMPTY_ITEM                   = "pluginsmanagement.filters.empty.item";                     //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FILTER_EVENT_REQUESTS                = "pluginsmanagement.filter.event.requests";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_FILTER_EVENT_INSTANCES               = "pluginsmanagement.filter.event.instances";                 //$NON-NLS-1$
  String PLUGINSMANAGEMENT_PLUGIN_STATUS_DESCRIPTION            = "pluginsmanagement.plugin.status.description";              //$NON-NLS-1$
  String PLUGINSMANAGEMENT_PLUGIN_ACTIONS_STATUS                = "pluginsmanagement.plugin.actions.status";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_PLUGIN_ACTIONS_INSTANCE              = "pluginsmanagement.plugin.actions.instance";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_PLUGIN_ACTIONS_REQUESTS              = "pluginsmanagement.plugin.actions.requests";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE                   = "pluginsmanagement.plugin.unavailable";                     //$NON-NLS-1$
  String PLUGINSMANAGEMENT_WINDOW_STATUS_TITLE                  = "pluginsmanagement.window.status.title";                    //$NON-NLS-1$
  String PLUGINSMANAGEMENT_WINDOW_STATUS_INFO                   = "pluginsmanagement.window.status.info";                     //$NON-NLS-1$
  String PLUGINSMANAGEMENT_WINDOW_STATUS_FROM                   = "pluginsmanagement.window.status.from";                     //$NON-NLS-1$
  String PLUGINSMANAGEMENT_WINDOW_STATUS_TO                     = "pluginsmanagement.window.status.to";                       //$NON-NLS-1$
  String PLUGINSMANAGEMENT_WINDOW_NEWSTATUS_TITLE               = "pluginsmanagement.window.newstatus.title";                 //$NON-NLS-1$
  String PLUGINSMANAGEMENT_NEWSTATUS_EMPTY_ITEM                 = "pluginsmanagement.newstatus.empty.item";                   //$NON-NLS-1$
  String PLUGINSMANAGEMENT_CREATE_INSTANCE_BUTTON               = "pluginsmanagement.instance.create";                        //$NON-NLS-1$
  String PLUGINSMANAGEMENT_INSTANCE_ACTIONS_EDIT                = "pluginsmanagement.instance.actions.edit";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_INSTANCE_ACTIONS_PROJECTS            = "pluginsmanagement.instance.actions.projects";              //$NON-NLS-1$
  String PLUGINSMANAGEMENT_INSTANCE_ACTIONS_CONFIGURE           = "pluginsmanagement.instance.actions.configure";             //$NON-NLS-1$
  String PLUGINSMANAGEMENT_INSTANCE_ACTIONS_DELETE              = "pluginsmanagement.instance.actions.delete";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_INSTANCES_TABLE_DESC                 = "pluginsmanagement.instance.table.desc";                    //$NON-NLS-1$
  String PLUGINSMANAGEMENT_DELETE_INSTANCE                      = "pluginsmanagement.instance.delete";                        //$NON-NLS-1$
  String PLUGINSMANAGEMENT_DELETE_INSTANCE_CONFIRM              = "pluginsmanagement.instance.delete.confirm";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_INSTANCE_LINK_REQUEST                = "pluginsmanagement.instance.link.request";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_REQUEST_INFO_TITLE                   = "pluginsmanagement.request.info.title";                     //$NON-NLS-1$
  String PLUGINSMANAGEMENT_PROJECTS_INSTANCE_UNABLE             = "pluginsmanagement.instance.projects.unable";               //$NON-NLS-1$
  String PLUGINSMANAGEMENT_PROJECTS_INSTANCE_DELETE             = "pluginsmanagement.instance.projects.delete";               //$NON-NLS-1$
  String PLUGINSMANAGEMENT_PROJECTS_INSTANCE_DELETE_CONFIRM     = "pluginsmanagement.instance.projects.delete.confirm";       //$NON-NLS-1$
  String PLUGINSMANAGEMENT_DELETE_INSTANCE_UNABLE               = "pluginsmanagement.instance.delete.unable";                 //$NON-NLS-1$
  String PLUGINSMANAGEMENT_ADMIN_INSTANCE_UNABLE                = "pluginsmanagement.instance.admin.unable";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_REQUESTS_TABLE_DESC                  = "pluginsmanagement.request.table.desc";                     //$NON-NLS-1$
  String PLUGINSMANAGEMENT_REQUESTS_ACTIONS_LINK                = "pluginsmanagement.requests.actions.link";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_REQUESTS_ACTIONS_DELETE              = "pluginsmanagement.requests.actions.delete";                //$NON-NLS-1$
  String PLUGINSMANAGEMENT_REQUESTS_LINK_AUTO                   = "pluginsmanagement.requests.link.auto";                     //$NON-NLS-1$
  String PLUGINSMANAGEMENT_DELETE_REQUEST                       = "pluginsmanagement.request.delete";                         //$NON-NLS-1$
  String PLUGINSMANAGEMENT_DELETE_REQUEST_CONFIRM               = "pluginsmanagement.request.delete.confirm";                 //$NON-NLS-1$
  String PLUGINSMANAGEMENT_REQUEST_LINK_INSTANCE                = "pluginsmanagement.request.link.instance";                  //$NON-NLS-1$
  String PLUGINSMANAGEMENT_LINK_INSTANCE                        = "pluginsmanagement.link.instance";                          //$NON-NLS-1$
  String PLUGINSMANAGEMENT_LINK_REQUEST                         = "pluginsmanagement.link.request";                           //$NON-NLS-1$
  String PLUGINSMANAGEMENT_INSTANCE_EMPTY_ITEM                  = "pluginsmanagement.instance.empty.item";                    //$NON-NLS-1$

  // Mailing Lists                                   //$NON-NLS-1$
  String MAILING_FILTER                                 = "mailing.filter";                                           //$NON-NLS-1$
  String MAILING_GLOBAL_TITLE                           = "mailing.global.title";                                     //$NON-NLS-1$
  String MAILING_LISTS_SHOW_LISTS                       = "mailing.lists.show.lists";                                 //$NON-NLS-1$
  String MAILING_LISTS_VIEW_LIST                        = "mailing.lists.view.list";                                  //$NON-NLS-1$
  String MAILING_LISTS_EDIT_LIST                        = "mailing.lists.edit.list";                                  //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBTION                     = "mailing.lists.subscription";                               //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBE_LIST                   = "mailing.lists.subscribe.list";                             //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBE_LIST_SUCCESS           = "mailing.lists.subscribe.list.success";                     //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_LIST                 = "mailing.lists.unsubscribe.list";                           //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_LIST_SUCCESS         = "mailing.lists.unsubscribe.list.success";                   //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_LIST_PRIVATE_CONFIRM = "mailing.lists.unsubscribe.list.private.confirm";           //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_LIST_PRIVATE_TITLE   = "mailing.lists.unsubscribe.list.private.title";             //$NON-NLS-1$
  String MAILING_LISTS_DELETE_LIST                      = "mailing.lists.delete.list";                                //$NON-NLS-1$
  String MAILING_LISTS_DELETE_LIST_INPROGRESS           = "mailing.lists.delete.list.inprogress";                     //$NON-NLS-1$
  String MAILING_LISTS_DELETE_LIST_SUCCESS              = "mailing.lists.delete.list.success";                        //$NON-NLS-1$
  String MAILING_LISTS_DELETE_LIST_ERROR                = "mailing.lists.delete.list.error";                          //$NON-NLS-1$
  String MAILING_LISTS_NAME                             = "mailing.lists.name";                                       //$NON-NLS-1$
  String MAILING_LISTS_NAME_REQUIRED                    = "mailing.lists.name.required";                              //$NON-NLS-1$
  String MAILING_LISTS_NAME_DESCRIPTION                 = "mailing.lists.name.description";                           //$NON-NLS-1$
  String MAILING_LISTS_NAME_REGEXP                      = "mailing.lists.name.regexp";                                //$NON-NLS-1$
  String MAILING_LISTS_DESCRIPTION                      = "mailing.lists.description";                                //$NON-NLS-1$
  String MAILING_LISTS_DESCRIPTION_REQUIRED             = "mailing.lists.description.required";                       //$NON-NLS-1$
  String MAILING_LISTS_SUBJECT                          = "mailing.lists.subject";                                    //$NON-NLS-1$
  String MAILING_LISTS_SUBJECT_REQUIRED                 = "mailing.lists.subject.required";                           //$NON-NLS-1$
  String MAILING_LISTS_OWNERS                           = "mailing.lists.owners";                                     //$NON-NLS-1$
  String MAILING_LISTS_OWNERS_REQUIRED                  = "mailing.lists.owners.required";                            //$NON-NLS-1$
  String MAILING_LISTS_OWNERS_DESCRIPTION               = "mailing.lists.owners.description";                         //$NON-NLS-1$
  String MAILING_LISTS_OWNERS_NO_OWNER                  = "mailing.lists.owners.no.owner";                            //$NON-NLS-1$
  String MAILING_LISTS_OWNERS_NO_OWNER_DESCRIPTION      = "mailing.lists.owners.no.owner.description";                //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBERS                      = "mailing.lists.subscribers";                                //$NON-NLS-1$
  String MAILING_LISTS_ADD_BACK                         = "mailing.lists.add.back";                                   //$NON-NLS-1$
  String MAILING_LISTS_ADD_CREATE                       = "mailing.lists.add.create";                                 //$NON-NLS-1$
  String MAILING_LISTS_ADD_TITLE                        = "mailing.lists.add.title";                                  //$NON-NLS-1$
  String MAILING_LISTS_ADD_CONFIRM                      = "mailing.lists.add.confirm";                                //$NON-NLS-1$
  String MAILING_LISTS_ADD_CANCEL                       = "mailing.lists.add.cancel";                                 //$NON-NLS-1$
  String MAILING_LISTS_ADD_EXISTS                       = "mailing.lists.add.exists";                                 //$NON-NLS-1$
  String MAILING_LISTS_ADD_INPROGRESS                   = "mailing.lists.add.inprogress";                             //$NON-NLS-1$
  String MAILING_LISTS_ADD_SUCCESS                      = "mailing.lists.add.success";                                //$NON-NLS-1$
  String MAILING_LISTS_ADD_ERROR                        = "mailing.lists.add.error";                                  //$NON-NLS-1$

  String MAILING_LISTS_TYPE                              = "mailing.lists.type";                                       //$NON-NLS-1$
  String MAILING_LISTS_TYPE_REQUIRED                     = "mailing.lists.type.required";                              //$NON-NLS-1$
  String MAILING_LISTS_TYPE_INPUTPROMPT                  = "mailing.lists.type.inputprompt";                           //$NON-NLS-1$
  String MAILING_LISTS_TYPE_DESCRIPTION                  = "mailing.lists.type.description";                           //$NON-NLS-1$
  String MAILING_LISTS_TYPE_PRIVATE                      = "mailing.lists.type.private";                               //$NON-NLS-1$
  String MAILING_LISTS_TYPE_HOTLINE                      = "mailing.lists.type.hotline";                               //$NON-NLS-1$
  String MAILING_LISTS_TYPE_CUSTOM                       = "mailing.lists.type.custom";                                //$NON-NLS-1$
  //$NON-NLS-1$                        
  String MAILING_LISTS_SUBSCRIBERS_ACTION                = "mailing.lists.subscribers.action";
  String MAILING_LISTS_SUBSCRIBERS_ADD                   = "mailing.lists.subscribers.add";                            //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBERS_LIST                  = "mailing.lists.subscribers.list";                           //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBERS_FILTER                = "mailing.lists.subscribers.filter";                         //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_ACTION                = "mailing.lists.unsubscribe.action";                         //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_TITLE                 = "mailing.lists.unsubscribe.title";                          //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBE_TITLE                   = "mailing.lists.subscribe.title";                            //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_CONFIRM               = "mailing.lists.unsubscribe.confirm";                        //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_NOTIFY                = "mailing.lists.unsubscribe.notify";                         //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_CONFIRM_TITLE         = "mailing.lists.unsubscribe.confirm.title";                  //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_CONFIRM_SUCCESS       = "mailing.lists.unsubscribe.confirm.success";                //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_GROUP_CONFIRM         = "mailing.lists.unsubscribe.group.confirm";                  //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_GROUP_NOTIFY          = "mailing.lists.unsubscribe.group.notify";                   //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_GROUP_CONFIRM_TITLE   = "mailing.lists.unsubscribe.group.confirm.title";            //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_GROUP_TITLE           = "mailing.lists.unsubscribe.group.title";                    //$NON-NLS-1$
  String MAILING_LISTS_UNSUBSCRIBE_GROUP_CONFIRM_SUCCESS = "mailing.lists.unsubscribe.group.confirm.success";          //$NON-NLS-1$

  String MAILING_LISTS_CLOSE_CONFIRM = "mailing.lists.close.confirm";                              //$NON-NLS-1$

  String MAILING_LISTS_ADD_STEPONE_TITLE                = "mailing.lists.subscribtion.add.stepone.title";             //$NON-NLS-1$
  String MAILING_LISTS_ADD_STEPTWO_TITLE                = "mailing.lists.subscribtion.add.steptwo.title";             //$NON-NLS-1$
  String MAILING_LISTS_ADD_STEPLAST_TITLE               = "mailing.lists.subscribtion.add.steplast.title";            //$NON-NLS-1$
  String MAILING_LISTS_ADD_SUBSCRIBTION_TYPE            = "mailing.lists.add.subscribtion.type";                      //$NON-NLS-1$
  String MAILING_LISTS_WIZARD_CANCEL                    = "mailing.lists.subscribtion.wizard.cancel";                 //$NON-NLS-1$
  String MAILING_LISTS_WIZARD_NEXT                      = "mailing.lists.subscribtion.wizard.next";                   //$NON-NLS-1$
  String MAILING_LISTS_WIZARD_BACK                      = "mailing.lists.subscribtion.wizard.back";                   //$NON-NLS-1$
  String MAILING_LISTS_WIZARD_ADD                       = "mailing.lists.subscribtion.wizard.add";                    //$NON-NLS-1$
  String MAILING_LISTS_WIZARD_FINISH                    = "mailing.lists.subscribtion.wizard.finish";                 //$NON-NLS-1$
  String MAILING_LISTS_ADD_EXTERNAL_USER_LABEL          = "mailing.lists.add.external.user.label";                    //$NON-NLS-1$
  String MAILING_LISTS_ADD_EXTERNAL_USER_TITLE          = "mailing.lists.add.external.user.title";                    //$NON-NLS-1$
  String MAILING_LIST_ADD_INNER_USER_TITLE              = "mailing.lists.add.inner.user.title";                       //$NON-NLS-1$
  String MAILING_LIST_USERS_SELECT_DESCRIPTION          = "mailing.lists.users.select.description";                   //$NON-NLS-1$
  String MAILING_LISTS_USERS_LIST_TITLE                 = "mailing.lists.users.list.title";                           //$NON-NLS-1$
  String MAILING_LISTS_USERS_LIST_DESCRIPTION           = "mailing.lists.users.list.description";                     //$NON-NLS-1$
  String MAILING_LISTS_FILTER                           = "mailing.lists.filter";                                     //$NON-NLS-1$
  String MAILING_LISTS_USERS_LIST_CANCEL                = "mailing.lists.users.list.cancel";                          //$NON-NLS-1$
  String MAILING_LISTS_USERS_LOGIN                      = "mailing.lists.users.login";                                //$NON-NLS-1$
  String MAILING_LISTS_USERS_FIRSTNAME                  = "mailing.lists.users.firstname";                            //$NON-NLS-1$
  String MAILING_LISTS_USERS_NAME                       = "mailing.lists.users.name";                                 //$NON-NLS-1$
  String MAILING_LISTS_USERS_EMAIL                      = "mailing.lists.users.email";                                //$NON-NLS-1$
  String MAILING_LISTS_USERS_LIST_SELECTED              = "mailing.lists.users.list.selected";                        //$NON-NLS-1$
  String MAILING_LISTS_PROJECT_USER_LABEL               = "mailing.lists.project.user.label";                         //$NON-NLS-1$
  String MAILING_LISTS_FORGE_USER_LABEL                 = "mailing.lists.forge.user.label";                           //$NON-NLS-1$
  String MAILING_LISTS_EXTERNAL_USER_LABEL              = "mailing.lists.external.user.label";                        //$NON-NLS-1$
  String MAILING_LISTS_EMAIL_INVALID_REGEX              = "mailing.lists.email.validate.regex";                       //$NON-NLS-1$
  String MAILING_LISTS_EMAIL_INVALID_SEPARATOR          = "mailing.lists.email.validate.separator";                   //$NON-NLS-1$
  String MAILING_LISTS_EMAIL_EMPTY                      = "mailing.lists.email.empty";                                //$NON-NLS-1$
  String MAILING_LISTS_ADD_SUBSCRIBERS_WARNING          = "mailing.lists.add.subscribers.warning";                    //$NON-NLS-1$
  String MAILING_LISTS_ADDED_EMAILS_LABEL               = "mailing.lists.added.emails.label";                         //$NON-NLS-1$
  String MAILING_LISTS_REJECTED_EMAILS_LABEL            = "mailing.lists.rejected.emails.label";                      //$NON-NLS-1$
  String MAILING_LISTS_REJECTED_DUPLICATED_EMAILS_LABEL = "mailing.lists.rejected.duplicated.emails.label";           //$NON-NLS-1$
  String MAILING_LISTS_GROUP_USER_LABEL                 = "mailing.lists.group.user.label";                           //$NON-NLS-1$
  String MAILING_LISTS_GROUP_LIST_TITLE                 = "mailing.lists.group.list.title";                           //$NON-NLS-1$
  String MAILING_LISTS_GROUP_LIST_DESCRIPTION           = "mailing.lists.group.list.description";                     //$NON-NLS-1$
  String MAILING_LISTS_GROUP_GLOBAL_USERS               = "mailing.lists.group.global.users";                         //$NON-NLS-1$
  String MAILING_LISTS_GROUP_LIST_SELECTED              = "mailing.lists.group.list.selected";                        //$NON-NLS-1$
  String MAILING_LISTS_ADD_GROUP_SUBSCRIBERS_WARNING    = "mailing.lists.add.group.subscribers.warning";              //$NON-NLS-1$
  String MAILING_LISTS_USER_INFO_DESCRIPTION            = "mailing.lists.user.info.description";                      //$NON-NLS-1$
  String MAILING_LISTS_GROUP_INFO_DESCRIPTION           = "mailing.lists.group.info.description";                     //$NON-NLS-1$
  String MAILING_LISTS_GROUP_MEMBERS_TITLE              = "mailing.lists.group.members.title";                        //$NON-NLS-1$
  String MAILING_LISTS_GROUP_MEMBERS_COLUMN             = "mailing.lists.group.members.column";                       //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBER_TYPE_GROUP            = "mailing.lists.subscriber.type.group";                      //$NON-NLS-1$
  String MAILING_LISTS_SUBSCRIBER_TYPE_USER             = "mailing.lists.subscriber.type.user";                       //$NON-NLS-1$

  // Memberships                                   //$NON-NLS-1$
  String MEMBERSHIPS_FILTER                            = "memberships.filter";                                       //$NON-NLS-1$
  String MEMBERSHIPS_GLOBAL_TITLE                      = "memberships.global.title";                                 //$NON-NLS-1$
  String MEMBERSHIPS_GLOBAL_USERS                      = "memberships.global.users";                                 //$NON-NLS-1$
  String MEMBERSHIPS_GLOBAL_GROUPS                     = "memberships.global.groups";                                //$NON-NLS-1$
  String MEMBERSHIPS_GLOBAL_REQUESTS                   = "memberships.global.requests";                              //$NON-NLS-1$
  String MEMBERSHIPS_GLOBAL_ROLES                      = "memberships.global.roles";                                 //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_INFO                       = "memberships.groups.info";                                  //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_DESCRIPTION                = "memberships.groups.description";                           //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_DESCRIPTION_REQUIRED       = "memberships.groups.description.required";                  //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_NAME                       = "memberships.groups.name";                                  //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_NAME_REQUIRED              = "memberships.groups.name.required";                         //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_PUBLIC                     = "memberships.groups.public";                                //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_PUBLIC_DESCRIPTION         = "memberships.groups.public.description";                    //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_PUBLIC_INHERIT             = "memberships.groups.public.inherit";                        //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_PUBLIC_INHERIT_DESCRIPTION = "memberships.groups.public.inherit.description";            //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_PRIVATE_DESCRIPTION        = "memberships.groups.private.description";                   //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_ADD_BACK                   = "memberships.groups.add.back";                              //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_ADD_CREATE                 = "memberships.groups.add.create";                            //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_ADD                   = "memberships.groups.edit.add";                              //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_CONFIRM               = "memberships.groups.edit.confirm";                          //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_LIST                  = "memberships.groups.edit.list";                             //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_MAKEPUBLIC            = "memberships.groups.edit.makepublic";                       //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_DESCRIPTION           = "memberships.groups.edit.description";                      //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_USERS                 = "memberships.groups.edit.users";                            //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_USERS_TITLE           = "memberships.groups.edit.users.title";                      //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_USERS_DESCRIPTION     = "memberships.groups.edit.users.description";                //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_VIEW_USERS_DESCRIPTION     = "memberships.groups.view.users.description";                //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_USERS_CONFIRM         = "memberships.groups.edit.users.confirm";                    //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_ROLES                 = "memberships.groups.edit.roles";                            //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_ROLES_TITLE           = "memberships.groups.edit.roles.title";                      //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_ROLES_DESCRIPTION     = "memberships.groups.edit.roles.description";                //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_ROLES_CONFIRM         = "memberships.groups.edit.roles.confirm";                    //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_DELETE_TITLE          = "memberships.groups.edit.delete.title";                     //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_DELETE_DESCRIPTION    = "memberships.groups.edit.delete.description";               //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_DELETE_CONFIRM        = "memberships.groups.edit.delete.confirm";                   //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_DELETE_CONFIRMLABEL   = "memberships.groups.edit.delete.confirmlabel";              //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_DELETE_NOTIFICATION   = "memberships.groups.edit.delete.notification";              //$NON-NLS-1$
  String MEMBERSHIPS_GROUPS_EDIT_TITLE                 = "memberships.groups.edit.title";                            //$NON-NLS-1$

  String MEMBERSHIPS_REQUESTS_MESSAGE              = "memberships.requests.message";                             //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_VALID_TITLE          = "memberships.requests.valid.title";                         //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_VALID_DESCRIPTION    = "memberships.requests.valid.description";                   //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_VALID_CONFIRM        = "memberships.requests.valid.confirm";                       //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_INVALID_TITLE        = "memberships.requests.invalid.title";                       //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_INVALID_CONFIRMLABEL = "memberships.requests.invalid.confirmlabel";                //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_INVALID_EXPLANATION  = "memberships.requests.invalid.explanation";                 //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_INVALID_CONFIRM      = "memberships.requests.invalid.confirm";                     //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_INVALID_DESCRIPTION  = "memberships.requests.invalid.description";                 //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_TITLE                = "memberships.requests.title";                               //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_HISTORY              = "memberships.requests.history";                             //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_HISTORY_BACK         = "memberships.requests.history.back";                        //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_HISTORY_TITLE        = "memberships.requests.history.title";                       //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_HISTORY_DESCRIPTION  = "memberships.requests.history.description";                 //$NON-NLS-1$
  String MEMBERSHIPS_REQUESTS_HISTORY_DATE         = "memberships.requests.history.date";                        //$NON-NLS-1$

  String MEMBERSHIPS_ROLES_DESCRIPTION                = "memberships.roles.description";                            //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_DESCRIPTION_REQUIRED       = "memberships.roles.description.required";                   //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_NAME                       = "memberships.roles.name";                                   //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_NAME_REQUIRED              = "memberships.roles.name.required";                          //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_ORDER                      = "memberships.roles.order";                                  //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_NOROLE                     = "memberships.roles.norole";                                 //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_NOROLE_DESCRIPTION         = "memberships.roles.norole.description";                     //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_UNAVAILABLE                = "memberships.roles.unavailable";                            //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_ADD_BACK                   = "memberships.roles.add.back";                               //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_ADD_CONFIRM                = "memberships.roles.add.confirm";                            //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_ADD_TITLE                  = "memberships.roles.add.title";                              //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_ORDER_DOWN            = "memberships.roles.edit.order.down";                        //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_ORDER_UP              = "memberships.roles.edit.order.up";                          //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_ADD                   = "memberships.roles.edit.add";                               //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_TITLE                 = "memberships.roles.edit.title";                             //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_INFO                  = "memberships.roles.edit.info";                              //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_INFO_TITLE            = "memberships.roles.edit.info.title";                        //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_INFO_CONFIRM          = "memberships.roles.edit.info.confirm";                      //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_INFO_SYSTEM           = "memberships.roles.edit.info.system";                       //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_MAPPING               = "memberships.roles.edit.mapping";                           //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_MAPPING_TITLE         = "memberships.roles.edit.mapping.title";                     //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_MAPPING_CONFIRM       = "memberships.roles.edit.mapping.confirm";                   //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_DELETE                = "memberships.roles.edit.delete";                            //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_DELETE_TITLE          = "memberships.roles.edit.delete.title";                      //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_DELETE_CONFIRMLABEL   = "memberships.roles.edit.delete.confirmlabel";               //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_DELETE_CONFIRM        = "memberships.roles.edit.delete.confirm";                    //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_EDIT_DELETE_SYSTEM         = "memberships.roles.edit.delete.system";                     //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MEMBER_DEFAULT             = "memberships.roles.member.default";                         //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MEMBER_DEFAULT_DESCRIPTION = "memberships.roles.member.default.description";             //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MEMBER_SELECT              = "memberships.roles.member.select";                          //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MEMBER_SELECT_DESCRIPTION  = "memberships.roles.member.select.description";              //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MEMBER_TITLE               = "memberships.roles.member.title";                           //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MEMBER_CONFIRM             = "memberships.roles.member.confirm";                         //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MAPPING_TITLE              = "memberships.roles.mapping.title";                          //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MAPPING_ROLECOLUMN         = "memberships.roles.mapping.rolecolumn";                     //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MAPPING_EDIT_DESCRIPTION   = "memberships.roles.mapping.edit.description";               //$NON-NLS-1$
  String MEMBERSHIPS_ROLES_MAPPING_ADD_DESCRIPTION    = "memberships.roles.mapping.add.description";                //$NON-NLS-1$

  String MEMBERSHIPS_USERS_LOGIN                    = "memberships.users.login";                                  //$NON-NLS-1$
  String MEMBERSHIPS_USERS_FIRSTNAME                = "memberships.users.firstname";                              //$NON-NLS-1$
  String MEMBERSHIPS_USERS_NAME                     = "memberships.users.name";                                   //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EMAIL                    = "memberships.users.email";                                  //$NON-NLS-1$
  String MEMBERSHIPS_USERS_SELECT_DESCRIPTION       = "memberships.users.select.description";                     //$NON-NLS-1$
  String MEMBERSHIPS_USERS_ADD_BACK                 = "memberships.users.add.back";                               //$NON-NLS-1$
  String MEMBERSHIPS_USERS_ADD_SELECT               = "memberships.users.add.select";                             //$NON-NLS-1$
  String MEMBERSHIPS_USERS_ADD_CONFIRM              = "memberships.users.add.confirm";                            //$NON-NLS-1$
  String MEMBERSHIPS_USERS_ADD_ROLES_DESCRIPTION    = "memberships.users.add.roles.description";                  //$NON-NLS-1$
  String MEMBERSHIPS_USERS_ADD_ROLES_SELECT         = "memberships.users.add.roles.select";                       //$NON-NLS-1$
  String MEMBERSHIPS_USERS_ADD_ROLES_CONFIRM        = "memberships.users.add.roles.confirm";                      //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_ADD                 = "memberships.users.edit.add";                               //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_TITLE               = "memberships.users.edit.title";                             //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_PROFILE             = "memberships.users.edit.profile";                           //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_ROLES               = "memberships.users.edit.roles";                             //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_ROLES_COLUMN        = "memberships.users.edit.roles.column";                      //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_ROLES_TITLE         = "memberships.users.edit.roles.title";                       //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_ROLES_CONFIRM       = "memberships.users.edit.roles.confirm";                     //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_DELETE              = "memberships.users.edit.delete";                            //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_DELETE_TITLE        = "memberships.users.edit.delete.title";                      //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_DELETE_CONFIRMLABEL = "memberships.users.edit.delete.confirmlabel";               //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_DELETE_NOTIFICATION = "memberships.users.edit.delete.notification";               //$NON-NLS-1$
  String MEMBERSHIPS_USERS_EDIT_DELETE_CONFIRM      = "memberships.users.edit.delete.confirm";                    //$NON-NLS-1$
  String MEMBERSHIPS_USERS_LIST_TITLE               = "memberships.users.list.title";                             //$NON-NLS-1$
  String MEMBERSHIPS_USERS_LIST_DESCRIPTION         = "memberships.users.list.description";                       //$NON-NLS-1$
  String MEMBERSHIPS_USERS_LIST_CANCEL              = "memberships.users.list.cancel";                            //$NON-NLS-1$
  String MEMBERSHIPS_USERS_LIST_SELECTED            = "memberships.users.list.selected";                          //$NON-NLS-1$

  // Application management
  String APPLICATIONS_TREE_CREATE                   = "applications.tree.create";                                 //$NON-NLS-1$
  String APPLICATIONS_TREE_APPLICATIONS             = "applications.tree.applications";                           //$NON-NLS-1$
  String APPLICATIONS_TREE_APPLICATIONS_DESCRIPTION = "applications.tree.applications.description";               //$NON-NLS-1$
  String APPLICATIONS_GLOBAL_TITLE                  = "applications.global.title";                                //$NON-NLS-1$
  String APPLICATIONS_GLOBAL_DESCRIPTION            = "applications.global.description";                          //$NON-NLS-1$

  String APPLICATIONS_SPACES_TITLE              = "applications.spaces.title";                                //$NON-NLS-1$
  String APPLICATIONS_SPACES_NAME               = "applications.spaces.name";                                 //$NON-NLS-1$
  String APPLICATIONS_SPACES_NAME_REQUIRED      = "applications.spaces.name.required";                        //$NON-NLS-1$
  String APPLICATIONS_SPACES_DESCRIPTION        = "applications.spaces.description";                          //$NON-NLS-1$
  String APPLICATIONS_SPACES_EDIT               = "applications.spaces.edit";                                 //$NON-NLS-1$
  String APPLICATIONS_SPACES_DELETE             = "applications.spaces.delete";                               //$NON-NLS-1$
  String APPLICATIONS_SPACES_DELETE_NOTEMPTY    = "applications.spaces.delete.notempty";                      //$NON-NLS-1$
  String APPLICATIONS_SPACES_APPLICATION_CREATE = "applications.spaces.application.create";                   //$NON-NLS-1$
  String APPLICATIONS_SPACES_CREATE_CONFIRM     = "applications.spaces.create.confirm";                       //$NON-NLS-1$

  String APPLICATIONS_APPLICATION_TITLE                = "applications.application.title";                           //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_CATEGORY             = "applications.application.category";                        //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_CATEGORY_INPUTPROMPT = "applications.application.category.inputprompt";            //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_CATEGORY_REQUIRED    = "applications.application.category.required";               //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_TYPE                 = "applications.application.type";                            //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_TYPE_INPUTPROMPT     = "applications.application.type.inputprompt";                //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_TYPE_REQUIRED        = "applications.application.type.required";                   //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_NAME                 = "applications.application.name";                            //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_NAME_REQUIRED        = "applications.application.name.required";                   //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_DESCRIPTION          = "applications.application.description";                     //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_ROLES                = "applications.application.roles";                           //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_ROLES_DESCRIPTION    = "applications.application.roles.description";               //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_ROLES_NONE           = "applications.application.roles.none";                      //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_ROLES_REQUIRED       = "applications.application.roles.required";                  //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_LINK                 = "applications.application.link";                            //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_EDIT                 = "applications.application.edit";                            //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_CREATE               = "applications.application.create";                          //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_DELETE               = "applications.application.delete";                          //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_DELETE_CONFIRMLABEL  = "applications.application.delete.confirmlabel";             //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_UNAVAILABLE          = "applications.application.unavailable";                     //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_PENDING              = "applications.application.pending";                         //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_CREATE_IN_PROGRESS   = "applications.application.create_inprogress";               //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_CREATE_ERROR         = "applications.application.create_error";                    //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_DELETE_IN_PROGRESS   = "applications.application.delete_inprogress";               //$NON-NLS-1$
  String APPLICATIONS_APPLICATION_DELETE_ERROR         = "applications.application.delete_error";                    //$NON-NLS-1$
  String APPLICATIONS_LINK_RETURN                      = "applications.link.return";                                 //$NON-NLS-1$
  String APPLICATIONS_LINK_TITLE                       = "applications.link.title";                                  //$NON-NLS-1$
  String APPLICATIONS_LINK_DESCRIPTION                 = "applications.link.description";                            //$NON-NLS-1$
  String APPLICATIONS_LINK_NAME                        = "applications.link.name";                                   //$NON-NLS-1$
  String APPLICATIONS_LINK_ENABLE                      = "applications.link.enable";                                 //$NON-NLS-1$
  String APPLICATIONS_LINK_ENABLE_DESCRIPTION          = "applications.link.enable.description";                     //$NON-NLS-1$
  String APPLICATIONS_LINK_SETUP                       = "applications.link.setup";                                  //$NON-NLS-1$
  String APPLICATIONS_LINK_NONE                        = "applications.link.none";                                   //$NON-NLS-1$
  String APPLICATIONS_LINK_NOTIFICATION_TITLE          = "applications.link.notification.title";                     //$NON-NLS-1$
  String APPLICATIONS_LINK_NOTIFICATION_DESCRIPTION    = "applications.link.notification.description";               //$NON-NLS-1$
  String APPLICATIONS_LINK_NOTIFICATION_SOURCE         = "applications.link.notification.source";                    //$NON-NLS-1$
  String APPLICATIONS_LINK_NOTIFICATION_CONFIGURE      = "applications.link.notification.configure";                 //$NON-NLS-1$
  String APPLICATIONS_LINK_NOTIFICATION_TARGET         = "applications.link.notification.target";                    //$NON-NLS-1$
  String APPLICATIONS_LINK_NOTIFICATION_ENABLE         = "applications.link.notification.enable";                    //$NON-NLS-1$
  String APPLICATIONS_LINK_NOTIFICATION_DISABLE        = "applications.link.notification.disable";                   //$NON-NLS-1$

  String APPLICATIONS_LINK_CONFIGURE_TITLE             = "applications.link.configure.title";                        //$NON-NLS-1$
  String APPLICATIONS_LINK_CONFIGURE_DESCRIPTION       = "applications.link.configure.description";                  //$NON-NLS-1$
  String APPLICATIONS_LINK_CONFIGURE_PREVIEW           = "applications.link.configure.preview";                      //$NON-NLS-1$
  String APPLICATIONS_LINK_CONFIGURE_CONFIRM           = "applications.link.configure.confim";                       //$NON-NLS-1$
  String APPLICATIONS_LINK_CONFIGURE_PARAM_TITLE       = "applications.link.configure.param.title";                  //$NON-NLS-1$
  String APPLICATIONS_LINK_CONFIGURE_PARAM_NAME        = "applications.link.configure.param.name";                   //$NON-NLS-1$
  String APPLICATIONS_LINK_CONFIGURE_PARAM_DESCRIPTION = "applications.link.configure.param.description";            //$NON-NLS-1$
  String APPLICATIONS_LINK_CONFIGURE_MAPPING_TITLE     = "applications.link.configure.mapping.title";                //$NON-NLS-1$
  String APPLICATIONS_LINK_CONFIGURE_MAPPING_MANDATORY = "applications.link.configure.mapping.mandatory";            //$NON-NLS-1$

  String APPLICATIONS_LINK_PREVIEW_TITLE         = "applications.link.preview.title";                          //$NON-NLS-1$
  String APPLICATIONS_LINK_PREVIEW_DESCRIPTION   = "applications.link.preview.description";                    //$NON-NLS-1$
  String APPLICATIONS_LINK_PREVIEW_PARAM_TITLE   = "applications.link.preview.param.title";                    //$NON-NLS-1$
  String APPLICATIONS_LINK_PREVIEW_PARAM_NAME    = "applications.link.preview.param.name";                     //$NON-NLS-1$
  String APPLICATIONS_LINK_PREVIEW_PARAM_VALUE   = "applications.link.preview.param.value";                    //$NON-NLS-1$
  String APPLICATIONS_LINK_PREVIEW_MAPPING_TITLE = "applications.link.preview.mapping.title";                  //$NON-NLS-1$

  String APPLICATIONS_LINK_DELETE_TITLE        = "applications.link.delete.title";                           //$NON-NLS-1$
  String APPLICATIONS_LINK_DELETE_CONFIRMLABEL = "applications.link.delete.confirmlabel";                    //$NON-NLS-1$
  String APPLICATIONS_LINK_DELETE_CONFIRM      = "applications.link.delete.confirm";                         //$NON-NLS-1$

  // News Management Module
  String ARTICLEMANAGEMENT_ADMIN_MENU_TITLE          = "articlemanagement.admin.menu.title";                       //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ADMIN_MENU_INFORMATION    = "articlemanagement.admin.menu.information";                 //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ADMIN_MENU_ANNOUNCEMENT   = "articlemanagement.admin.menu.announcement";                //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ADMIN_MENU_ARTICLE        = "articlemanagement.admin.menu.article";                     //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ADMIN_MENU_CATEGORY       = "articlemanagement.admin.menu.category";                    //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ADMIN_MENU_BACK           = "articlemanagement.admin.menu.back";                        //$NON-NLS-1$
  String ARTICLEMANAGEMENT_CREATE_NEWS               = "articlemanagement.create.news";                            //$NON-NLS-1$
  String ARTICLEMANAGEMENT_CREATE_BACKTOLIST         = "articlemanagement.create.backtolist";                      //$NON-NLS-1$
  String ARTICLEMANAGEMENT_NOTIFICATION_CREATE_TITLE = "articlemanagement.notification.create.title";              //$NON-NLS-1$
  String ARTICLEMANAGEMENT_NOTIFICATION_CREATE_DESC  = "articlemanagement.notification.create.desc";               //$NON-NLS-1$

  String ARTICLEMANAGEMENT_CREATE_ANNOUNCEMENT      = "articlemanagement.create.annoucement";                     //$NON-NLS-1$
  String ARTICLEMANAGEMENT_INFORMATION_UPDATE_TITLE = "articlemanagement.information.update.title";               //$NON-NLS-1$
  String ARTICLEMANAGEMENT_INFORMATION_UPDATE_DESC  = "articlemanagement.information.update.desc";                //$NON-NLS-1$

  String ARTICLEMANAGEMENT_ACTION_PUBLISH   = "articlemanagement.action.publish";                         //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ACTION_UNPUBLISH = "articlemanagement.action.unpublish";                       //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ACTION_EDIT      = "articlemanagement.action.edit";                            //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ACTION_DELETE    = "articlemanagement.action.delete";                          //$NON-NLS-1$

  String ARTICLEMANAGEMENT_FIELD_TITLE              = "articlemanagement.field.title";                            //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_SHORTTEXT          = "articlemanagement.field.shorttext";                        //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_TEXT               = "articlemanagement.field.text";                             //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_DATE               = "articlemanagement.field.date";                             //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_PUBLISH            = "articlemanagement.field.publish";                          //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_CONTENT            = "articlemanagement.field.content";                          //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_CATEGORIES         = "articlemanagement.field.category";                         //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_NAME               = "articlemanagement.field.name";                             //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FILTER_ARTICLES          = "articlemanagement.filter.articles";                        //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FILTER_ANNOUNCEMENTS     = "articlemanagement.filter.annoucements";                    //$NON-NLS-1$
  String ARTICLEMANAGEMENT_CREATE_CATEGORY          = "articlemanagement.category.create";                        //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_COLOR              = "articlemanagement.field.color";                            //$NON-NLS-1$
  String ARTICLEMANAGEMENT_CATEGORY_UNKNOW          = "articlemanagement.category.unknow";                        //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FILTER_FROM              = "articlemanagement.filter.from";                            //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FILTER_TO                = "articlemanagement.filter.to";                              //$NON-NLS-1$
  String ARTICLEMANAGEMENT_RESET_FILTER             = "articlemanagement.reset.filters";                          //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FILTER_INCATEGORY        = "articlemanagement.filter.incategory";                      //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ADMIN                    = "articlemanagement.admin";                                  //$NON-NLS-1$
  String ARTICLEMANAGEMENT_ARTICLE_DETAILS_BACK     = "articlemanagement.article.details.back";                   //$NON-NLS-1$
  String ARTICLEMANAGEMENT_UPDATE_NEWS              = "articlemanagement.update.news";                            //$NON-NLS-1$
  String ARTICLEMANAGEMENT_UPDATE_ANNOUNCEMENT      = "articlemanagement.update.announcement";                    //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_NAME_REQUIRED      = "articlemanagement.field.name.required";                    //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_TITLE_REQUIRED     = "articlemanagement.field.title.required";                   //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_SHORTTEXT_REQUIRED = "articlemanagement.field.shorttext.required";               //$NON-NLS-1$
  String ARTICLEMANAGEMENT_FIELD_TEXT_REQUIRED      = "articlemanagement.field.text.required";                    //$NON-NLS-1$
  String ARTICLEMANAGEMENT_UPDATE_CATEGORY          = "articlemanagement.update.category";                        //$NON-NLS-1$

  //$NON-NLS-1$
  // Common Form
  String FORM_INVALID_VALUE_TITLE   = "form.invalid.value.title";                                 //$NON-NLS-1$
  String FORM_INVALID_VALUE_GENERIC = "form.invalid.value.generic";                               //$NON-NLS-1$
  String FORM_REQUIRED_VALUE        = "form.required.value";                                      //$NON-NLS-1$
  String FORM_INVALID_VALUE_HELP    = "form.invalid.value.help";                                  //$NON-NLS-1$

  String DATE_RANGE_START      = "date.range.start";                                         //$NON-NLS-1$
  String DATE_RANGE_END        = "date.range.end";                                           //$NON-NLS-1$
  String DATE_RANGE_LAST_WEEK  = "date.range.last.week";                                     //$NON-NLS-1$
  String DATE_RANGE_LAST_MONTH = "date.range.last.month";                                    //$NON-NLS-1$
  String DATE_RANGE_CUSTOM     = "date.range.custom";                                        //$NON-NLS-1$

  String DELETE_WINDOW_NO           = "delete.window.no";                                         //$NON-NLS-1$
  String DELETE_WINDOW_YES          = "delete.window.yes";                                        //$NON-NLS-1$
  String DELETE_WINDOW_TITLE        = "delete.window.title";                                      //$NON-NLS-1$
  String DELETE_WINDOW_DEFAULT_TEXT = "delete.window.default.text";                               //$NON-NLS-1$

}
