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
package org.novaforge.forge.plugins.testmanager.testlink.datamapper;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 26 juil. 2011
 */
public enum TestLinkParameter
{
	DEV_KEY("devKey"), USER_NAME("userName"), PASSWORD("password"), FIRST_NAME("firstname"), LAST_NAME(
			"lastname"), EMAIL("email"), LANGUAGE("language"), PROJECT_NAME("testprojectname"), PROJECT_TOOL_NAME(
			"name"), NOTES("notes"), ROLE_NAME("roleName"), USER_MAPPING_ROLE("userMappingRole"), ID("id"), TEST_CASE_PREFIX(
			"testcaseprefix"), ACTIVE("active"), PUBLIC("public"), TRUE("1"), FALSE("0"), ADMIN_LOGIN(
			"adminLogin"), ADMIN_PASSWORD("adminPassword"), Name("name"), PROJECT_ID("projectId"), ADMIN_ROLE(
			"adminrole"), REQUIREMENTS_XML("requirementsXml"), REQUIREMENT_ID("req_id"), DIRECTORY_NAME(
			"directoryName"), DIRECTORY_DESCRIPTION("directoryDescription"), DIRECTORY_SCOPE("directoryScope"), DIRECTORY_PARENT_ID(
			"directoryParentId"), REQUIREMENT_NAME("reqName"), REQUIREMENT_DESCRIPTION("reqDescription"), REQUIREMENT_VERSION(
			"reqVersion"), REQUIREMENT_PARENT_ID("reqParentId");

	private final String	value;

	/**
    * 
    */
	TestLinkParameter(final String value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value;
	}

}
