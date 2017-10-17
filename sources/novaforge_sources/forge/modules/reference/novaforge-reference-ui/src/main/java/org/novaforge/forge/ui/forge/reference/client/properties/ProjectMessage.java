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
package org.novaforge.forge.ui.forge.reference.client.properties;

import com.google.gwt.i18n.client.Messages;

public interface ProjectMessage extends Messages
{

	String buttonAddSpace();

	String buttonUpdate();

	String infoTitle();

	String editTitle();

	String projectName();

	String projectIdentifiant();

	String projectDescription();

	String spaceInfoTitle();

	String spaceEditTitle();

	String spaceAddTitle();

	String spaceName();

	String editionSpaceActive();

	String buttonUpdateSpace();

	String buttonDeleteSpace();

	String buttonAddApplication();

	String buttonUpdateApplication();

	String buttonDeleteApplication();

	String editionAppActive();

	String editionProjectActive();

	String appInfoTitle();

	String appEditTitle();

	String appAddTitle();

	String appCategory();

	String appType();

	String appName();

	String emptyRolesMessage();

	String projectRoleColumn();

	String appRoleColumn();

	String noRole();

	String emptyTypeMessage();

	String categoryList();

	String typeList();

	String applicationRolesTitle();

	String projectNameValidation();

	String applicationNameValidation();

	String projectIdentifiantValidation();

	String projectDescriptionValidation();

	String projectErrorValidation();

	String deleteAppValidationMessage();

	String addAppValidationMessage();

	String editAppValidationMessage();

	String deleteSpaceValidationMessage();

	String addSpaceValidationMessage();

	String editSpaceValidationMessage();

	String editValidationMessage();

	String errorTypeRefresh();

	String mandatoryRoleMapping();
}
