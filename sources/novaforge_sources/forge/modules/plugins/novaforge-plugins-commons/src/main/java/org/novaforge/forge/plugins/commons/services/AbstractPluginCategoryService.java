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
package org.novaforge.forge.plugins.commons.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.PluginCategoryService;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This abstract class define a generic way to implement PluginFunctionalService Interface. The real plugin
 * functional service should declare a way to set it's serviceUUID.
 * 
 * @author rols-p
 */
public abstract class AbstractPluginCategoryService implements PluginCategoryService
{

	/**
	 * The logger
	 */
	protected final static Log    LOGGER          = LogFactory.getLog(AbstractPluginService.class);

	/**
	 * Access Info key
	 */
	protected final static String KEY_ACCESS_INFO = "accessInfo";

	/**
	 * Returns a localized message.
	 *
	 * @param pkey
	 *          key of the message
	 * @param pLocale
	 *          Locale used to display the message
	 * @return the localized message
	 */
	protected String getMessage(final String pkey, final Locale pLocale)
	{
		return getMessage(pkey, pLocale, (Object[]) null);
	}

	/**
	 * Returns a localized message.
	 * 
	 * @param pkey
	 *          key of the message
	 * @param pLocale
	 *          Locale used to display the message
	 * @param pParams
	 *          substitution parameters
	 * @return the localized message
	 */
	protected String getMessage(final String pkey, final Locale pLocale, final Object... pParams)
	{
		final ResourceBundle resourceBundle = getBundle(pLocale, getClassLoader());
		String msg;
		if ((pParams != null) && (pParams.length > 0))
		{
			msg = MessageFormat.format(resourceBundle.getString(pkey).replace("'", "''"), pParams);
		}
		else
		{
			msg = resourceBundle.getString(pkey);
		}
		return msg;
	}

	/**
	 * Returns the Resource Bundle according to the given Locale
	 *
	 * @param pLocale
	 *          used to display the message
	 * @return the Resource Bundle
	 */
	private ResourceBundle getBundle(final Locale pLocale, final ClassLoader classLoader)
	{
		return ResourceBundle.getBundle(getPropertyFileName(), pLocale, classLoader);
	}

	/**
	 * @return the classLoader of the current bundle. Used to load a Resource.
	 */
	protected ClassLoader getClassLoader()
	{
		return this.getClass().getClassLoader();
	}

	/**
	 * Returns the Property file name used for the plugin messages.
	 *
	 * @return the Property file name.
	 */
	protected abstract String getPropertyFileName();

}
