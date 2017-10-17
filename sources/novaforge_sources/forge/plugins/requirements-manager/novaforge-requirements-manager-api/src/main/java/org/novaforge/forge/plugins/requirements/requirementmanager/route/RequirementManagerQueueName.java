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
package org.novaforge.forge.plugins.requirements.requirementmanager.route;

/**
 * @author Guillaume Lamirand
 */
public class RequirementManagerQueueName
{
	public final static String REQUIREMENT_PROJECT_QUEUE										= "jms:queue:requirementProjectQueue";
	public final static String REQUIREMENT_USER_QUEUE											 = "jms:queue:requirementUserQueue";
	public final static String REQUIREMENT_MEMBERSHIPS_QUEUE								= "jms:queue:requirementMembershipQueue";
	public final static String REQUIREMENT_ROLESMAPPING_QUEUE							 = "jms:queue:requirementRolesMappingQueue";

	public final static String REQUIREMENT_PROJECT_ROUTE										= "requirementProjectRoute";
	public final static String REQUIREMENT_USER_ROUTE											 = "requirementUserRoute";
	public final static String REQUIREMENT_MEMBERSHIPS_ROUTE								= "requirementMembershipRoute";
	public final static String REQUIREMENT_ROLESMAPPING_ROUTE							 = "requirementRolesMappingRoute";

	// Topics published by tool requirements
	/**
	 * This is the full name used by Camel Endpoints
	 */
	public static final String REQUIREMENT_SOURCES_SYNCHRO_TOPIC_FULL_NAME	= "jms:topic:requirementsSourcesSynchroTopic";
	public static final String REQUIREMENT_TEST_IMPORT_TOPIC_FULL_NAME			= "jms:topic:requirementsTestImportTopic";
	public static final String REQUIREMENT_TEST_EXPORT_TOPIC_FULL_NAME			= "jms:topic:requirementsTestExportTopic";

	/**
	 * This is the short name used by senders (ie messageService)
	 */
	public static final String REQUIREMENT_SOURCES_SYNCHRO_TOPIC_SHORT_NAME = "requirementsSourcesSynchroTopic";
	public static final String REQUIREMENT_TEST_IMPORT_TOPIC_SHORT_NAME		 = "requirementsTestImportTopic";
	public static final String REQUIREMENT_TEST_EXPORT_TOPIC_SHORT_NAME		 = "requirementsTestExportTopic";

	/**
	 * Tool's route's id
	 */
	public static final String REQUIREMENT_SOURCES_SYNCHRO_ROUTE						= "requirementsSourcesSynchroRoute";
	public static final String REQUIREMENT_TEST_IMPORT_ROUTE								= "requirementsTestImportRoute";
	public static final String REQUIREMENT_TEST_EXPORT_ROUTE								= "requirementsTestExportRoute";

}
