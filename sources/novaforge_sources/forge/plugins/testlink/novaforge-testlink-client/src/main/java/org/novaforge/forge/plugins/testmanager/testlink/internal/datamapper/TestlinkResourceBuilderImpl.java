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
package org.novaforge.forge.plugins.testmanager.testlink.internal.datamapper;

import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkParameter;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestlinkResourceBuilder;

import java.util.HashMap;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 26 juil. 2011
 */
public class TestlinkResourceBuilderImpl implements TestlinkResourceBuilder
{

	/**
	 * Constant bracket open
	 */
	private static final String	BRACKET_OPEN	= "(";
	/**
	 * Constant bracket close
	 */
	private static final String	BRACKET_CLOSE	= ")";

	/**
	 * Constant space
	 */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, Object> buildAccountData(final PluginUser user)
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		putIfNotNull(result, TestLinkParameter.USER_NAME.toString(), user.getLogin());
		putIfNotNull(result, TestLinkParameter.PASSWORD.toString(), user.getPassword());
		putIfNotNull(result, TestLinkParameter.FIRST_NAME.toString(), user.getFirstName());
		putIfNotNull(result, TestLinkParameter.LAST_NAME.toString(), user.getName());
		putIfNotNull(result, TestLinkParameter.EMAIL.toString(), user.getEmail());
		if (user.getLanguage() != null)
		{
			putIfNotNull(result, TestLinkParameter.LANGUAGE.toString(), user.getLanguage().toLowerCase());
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, Object> buildProjectData(final PluginProject pluginProject,
			final String configurationId)
	{

		final HashMap<String, Object> result = new HashMap<String, Object>();

		StringBuilder projectName = new StringBuilder();
		projectName.append(pluginProject.getName()).append(BRACKET_OPEN).append(configurationId)
				.append(BRACKET_CLOSE);
		putIfNotNull(result, TestLinkParameter.TEST_CASE_PREFIX.toString(), projectName.toString());
		putIfNotNull(result, TestLinkParameter.PROJECT_NAME.toString(), projectName.toString());
		putIfNotNull(result, TestLinkParameter.NOTES.toString(), pluginProject.getDescription());

		return result;
	}

	private HashMap<String, Object> putIfNotNull(HashMap<String, Object> map, String key, Object value)
	{
		if (value != null)
		{
			map.put(key, value);
		}
		return map;
	}
}
