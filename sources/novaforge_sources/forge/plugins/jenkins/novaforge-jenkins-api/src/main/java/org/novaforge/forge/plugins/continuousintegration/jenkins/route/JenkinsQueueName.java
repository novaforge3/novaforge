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
package org.novaforge.forge.plugins.continuousintegration.jenkins.route;

/**
 * @author Guillaume Lamirand
 */
public class JenkinsQueueName
{
	public final static String JENKINS_PROJECT_QUEUE      = "jms:queue:jenkinsProjectQueue";
	public final static String JENKINS_USER_QUEUE         = "jms:queue:jenkinsUserQueue";
	public final static String JENKINS_MEMBERSHIPS_QUEUE  = "jms:queue:jenkinsMembershipQueue";
	public final static String JENKINS_ROLESMAPPING_QUEUE = "jms:queue:jenkinsRolesMappingQueue";

	public final static String JENKINS_PROJECT_ROUTE      = "jenkinsProjectRoute";
	public final static String JENKINS_USER_ROUTE         = "jenkinsUserRoute";
	public final static String JENKINS_MEMBERSHIPS_ROUTE  = "jenkinsMembershipRoute";
	public final static String JENKINS_ROLESMAPPING_ROUTE = "jenkinsRolesMappingRoute";
}
