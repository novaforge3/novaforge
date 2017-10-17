/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
/**
 * 
 */
package org.novaforge.forge.ui.requirements.internal.client.i18n;

import java.io.Serializable;

/**
 * @author Jeremy Casery
 */
public interface Messages extends Serializable
{

  final String GLOBAL_TITLE                            = "requirement.global.title";                            //$NON-NLS-1$
  final String GLOBAL_MENU_LIST                        = "requirement.global.list";                             //$NON-NLS-1$
  final String GLOBAL_MENU_CODE                        = "requirement.global.codeview";                         //$NON-NLS-1$
  final String GLOBAL_MENU_TESTS                       = "requirement.global.testview";                         //$NON-NLS-1$
  final String GLOBAL_MENU_SYNCHRONIZATION             = "requirement.global.synchronization";                  //$NON-NLS-1$
  final String GLOBAL_MENU_REPOSITORY                  = "requirement.global.repository";                       //$NON-NLS-1$
  final String GLOBAL_MENU_OBEOSERVER                  = "requirement.global.obeoserver";                       //$NON-NLS-1$
  final String GLOBAL_MENU_CONFIGURATION               = "requirement.global.configuration";                    //$NON-NLS-1$

  final String REQUIREMENT_FIELD_STATUS                = "requirement.field.status";                            //$NON-NLS-1$
  final String REQUIREMENT_FIELD_VERSION               = "requirement.field.version";                           //$NON-NLS-1$
  final String REQUIREMENT_FIELD_TYPE                  = "requirement.field.type";                              //$NON-NLS-1$
  final String REQUIREMENT_FIELD_REFERENCE             = "requirement.field.reference";                         //$NON-NLS-1$
  final String REQUIREMENT_FIELD_DESCRIPTION           = "requirement.field.description";                       //$NON-NLS-1$
  final String REQUIREMENT_FIELD_DESCRIPTION_LENGTH    = "requirement.field.description.length";                //$NON-NLS-1$
  final String REQUIREMENT_FIELD_ACCEPTANCE_CRITERIA   = "requirement.field.acceptance.criteria";               //$NON-NLS-1$
  final String REQUIREMENT_FIELD_NAME                  = "requirement.field.name";                              //$NON-NLS-1$
  final String REQUIREMENT_FIELD_ID                    = "requirement.field.id";                                //$NON-NLS-1$
  final String REQUIREMENT_FIELD_REPOSITORY            = "requirement.field.repository";                        //$NON-NLS-1$
  final String REQUIREMENT_FIELD_INDICATOR             = "requirement.field.indicator";                         //$NON-NLS-1$

  final String LIST_FILTER_TITLE                       = "requirement.list.filter.title";                       //$NON-NLS-1$
  final String LIST_FILTER_REQUIREMENTS_DESC           = "requirement.list.filter.requirements.description";    //$NON-NLS-1$
  final String LIST_EXPAND_TOOLTIP                     = "requirement.list.expand";                             //$NON-NLS-1$
  final String LIST_COLLAPSE_TOOLTIP                   = "requirement.list.collapse";                           //$NON-NLS-1$

  final String DETAILS_TITLE                           = "requirement.details.title";                           //$NON-NLS-1$
  final String DETAILS_REQUIREMENT_NOTFOUND            = "requirement.details.notfound";                        //$NON-NLS-1$

  final String FILTER_REQUIREMENTS_FORM                = "requirement.filter.form";                             //$NON-NLS-1$
  final String FROM_FILTER_SUBMIT                      = "requirement.form.filter.submit";                      //$NON-NLS-1$
  final String FROM_FILTER_RESET                       = "requirement.form.filter.reset";                       //$NON-NLS-1$+
  final String FILTER_REQUIREMENTS_INPUTPROMPT         = "requirement.filter.requirements.inputprompt";         //$NON-NLS-1$

  final String CODEVIEW_TABLE_HEADER_CLASS             = "requirement.codeview.table.header.class";             //$NON-NLS-1$
  final String CODEVIEW_TABLE_HEADER_CODE              = "requirement.codeview.table.header.code";              //$NON-NLS-1$
  final String CODEVIEW_TABLE_HEADER_REQUIREMENT       = "requirement.codeview.table.header.requirement";       //$NON-NLS-1$
  final String CODEVIEW_FILTER_REQUIREMENTS_DESC       = "requirement.codeview.filter.requirements.description"; //$NON-NLS-1$
  final String CODEVIEW_UNAVAILABLE                    = "requirement.codeview.unavailable";                    //$NON-NLS-1$

  final String TESTVIEW_TABLE_HEADER_IDTEST            = "requirement.testview.table.header.idtest";            //$NON-NLS-1$
  final String TESTVIEW_TABLE_HEADER_VERSIONTEST       = "requirement.testview.table.header.versiontest";       //$NON-NLS-1$
  final String TESTVIEW_TABLE_HEADER_REQUIREMENT       = "requirement.testview.table.header.requirement";       //$NON-NLS-1$
  final String TESTVIEW_FILTER_REQUIREMENTS_DESC       = "requirement.testview.filter.requirements.description"; //$NON-NLS-1$
  final String TESTVIEW_UNAVAILABLE                    = "requirement.testview.unavailable";                    //$NON-NLS-1$

  final String SYNCHRO_SELECT_DATAS                    = "requirement.synchro.select.datas";                    //$NON-NLS-1$
  final String SYNCHRO_REQUIREMENTS                    = "requirement.synchro.requirements";                    //$NON-NLS-1$
  final String SYNCHRO_TESTS                           = "requirement.synchro.tests";                           //$NON-NLS-1$
  final String SYNCHRO_CODES                           = "requirement.synchro.codes";                           //$NON-NLS-1$
  final String SYNCHRO_REQUIREMENTS_NOTLINKED          = "requirement.synchro.requirements.notlinked";          //$NON-NLS-1$
  final String SYNCHRO_TESTS_NOTLINKED                 = "requirement.synchro.tests.notlinked";                 //$NON-NLS-1$
  final String SYNCHRO_CODES_NOTLINKED                 = "requirement.synchro.codes.notlinked";                 //$NON-NLS-1$
  final String SYNCHRO_SYNCHRONIZE                     = "requirement.synchro.synchronize";                     //$NON-NLS-1$
  final String SYNCHRONIZATION_SUCCESS_TITLE           = "requirement.synchro.success.title";                   //$NON-NLS-1$
  final String SYNCHRONIZATION_SUCCESS_DESC            = "requirement.synchro.success.desc";                    //$NON-NLS-1$
  final String SYNCHRONIZATION_ALREADY_RUNNING_TITLE   = "requirement.synchro.alreadyrunning.title";            //$NON-NLS-1$
  final String SYNCHRONIZATION_ALREADY_RUNNING_DESC    = "requirement.synchro.alreadyrunning.desc";             //$NON-NLS-1$

  final String REPOSITORY_ADD_STEPONE_TITLE            = "requirement.repository.add.stepone.title";            //$NON-NLS-1$
  final String REPOSITORY_ADD_STEPTWO_TITLE            = "requirement.repository.add.steptwo.title";            //$NON-NLS-1$
  final String REPOSITORY_ADD_REPOSITORY_TYPE          = "requirement.repository.add.repository.type";          //$NON-NLS-1$
  final String REPOSITORY_ADD_STEPLAST_TITLE           = "requirement.repository.add.steplast.title";           //$NON-NLS-1$
  final String REPOSITORY_ADD_OBEO_CONF_TITLE          = "requirement.repository.add.obeo.conf.title";          //$NON-NLS-1$
  final String REPOSITORY_ADD_OBEO_CONF_URI            = "requirement.repository.add.obeo.conf.uri";            //$NON-NLS-1$
  final String REPOSITORY_ADD_OBEO_CONF_DESC           = "requirement.repository.add.obeo.conf.desc";           //$NON-NLS-1$
  final String REPOSITORY_ADD_EXCEL_CONF_TITLE         = "requirement.repository.add.excel.conf.title";         //$NON-NLS-1$
  final String REPOSITORY_ADD_EXCEL_CONF_FILE          = "requirement.repository.add.excel.conf.file";          //$NON-NLS-1$
  final String REPOSITORY_ADD_EXCEL_UPLOAD_BUTTON      = "requirement.repository.add.excel.upload.button";      //$NON-NLS-1$
  final String REPOSITORY_ADD_EXCEL_CONF_DESC          = "requirement.repository.add.excel.conf.desc";          //$NON-NLS-1$

  final String REPOSITORY_ADD_EXCEL_ERROR_TITLE        = "requirement.repository.add.excel.error.title";        //$NON-NLS-1$
  final String REPOSITORY_ADD_EXCEL_ERROR_DESC         = "requirement.repository.add.excel.error.desc";         //$NON-NLS-1$

  final String REPOSITORY_WIZARD_CANCEL                = "requirement.repository.wizard.cancel";                //$NON-NLS-1$
  final String REPOSITORY_WIZARD_NEXT                  = "requirement.repository.wizard.next";                  //$NON-NLS-1$
  final String REPOSITORY_WIZARD_BACK                  = "requirement.repository.wizard.back";                  //$NON-NLS-1$
  final String REPOSITORY_WIZARD_ADD                   = "requirement.repository.wizard.add";                   //$NON-NLS-1$
  final String REPOSITORY_WIZARD_UPDATE                = "requirement.repository.wizard.update";                //$NON-NLS-1$
  final String REPOSITORY_ADD_REPOSITORY               = "requirement.repository.add.repository";               //$NON-NLS-1$
  final String REPOSITORY_ADD_URI_MALFORMED            = "requirement.repository.invalid.uri";                  //$NON-NLS-1$
  final String REPOSITORY_ADD_FILE_INVALID_CONTENT     = "requirement.repository.add.file.invalid.content";     //$NON-NLS-1$
  final String REPOSITORY_ACTIONS_EDIT                 = "requirement.repository.actions.edit";                 //$NON-NLS-1$
  final String REPOSITORY_ACTIONS_DELETE               = "requirement.repository.actions.delete";               //$NON-NLS-1$
  final String REPOSITORY_ADDED_TITLE                  = "requirement.repository.added.title";                  //$NON-NLS-1$
  final String REPOSITORY_ADDED_DESC                   = "requirement.repository.added.desc";                   //$NON-NLS-1$
  final String REPOSITORY_ADD_TYPE_UNKNOW              = "requirement.repository.add.type.unknow";              //$NON-NLS-1$
  final String REPOSITORY_DELETED_TITLE                = "requirement.repository.deleted.title";                //$NON-NLS-1$
  final String REPOSITORY_DELETED_DESC                 = "requirement.repository.deleted.desc";                 //$NON-NLS-1$
  final String REPOSITORY_DELETE_CONFIRM               = "requirement.repository.deleted.confirm";              //$NON-NLS-1$
  final String REPOSITORY_UPDATED_TITLE                = "requirement.repository.updated.title";                //$NON-NLS-1$
  final String REPOSITORY_UPDATED_DESC                 = "requirement.repository.updated.desc";                 //$NON-NLS-1$

  final String CONFIGURATION_CODE_PATH                 = "requirement.configuration.code.path";                 //$NON-NLS-1$
  final String CONFIGURATION_SYNCHROAUTO_ACTIVATE      = "requirement.configuration.synchroauto.activate";      //$NON-NLS-1$
  final String CONFIGURATION_SYNCHROAUTO_REPEATTIME    = "requirement.configuration.synchroauto.repeattime";    //$NON-NLS-1$
  final String CONFIGURATION_SYNCHROAUTO_LAUNCHTIME    = "requirement.configuration.synchroauto.launchtime";    //$NON-NLS-1$
  final String CONFIGURATION_SAVE                      = "requirement.configuration.save";                      //$NON-NLS-1$
  final String CONFIGURATION_SAVE_SOURCES_SUCCESS      = "requirement.configuration.save.sources.success";      //$NON-NLS-1$
  final String CONFIGURATION_SAVE_SOURCES_SUCCESS_DESC = "requirement.configuration.save.sources.success.desc"; //$NON-NLS-1$
  final String CONFIGURATION_SAVE_SYNCHRO_SUCCESS      = "requirement.configuration.save.synchro.success";      //$NON-NLS-1$
  final String CONFIGURATION_SAVE_SOURCES_SYNCHRO_DESC = "requirement.configuration.save.synchro.success.desc"; //$NON-NLS-1$

}