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
package org.novaforge.forge.commons.technical.historization.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author sbenoist
 */
public enum EventLevel
{

	ENTRY
	{
		@Override
		public String getColor()
		{
			return "green";
		}
	},
	EXIT
	{
		@Override
		public String getColor()
		{
			return "green";
		}
	},
	ERROR
	{
		@Override
		public String getColor()
		{
			return "red";
		}
	};

	private static final Map<String, EventLevel> levels = new HashMap<String, EventLevel>();

	static
	{
		for (final EventLevel level : values())
		{
			levels.put(level.getLabel(), level);
		}
	}

	public static EventLevel fromLabel(final String pLabel)
	{
		return levels.get(pLabel);
	}

	public abstract String getColor();

	public String getLabel()
	{
		return getLabel(EventLevelResourceBundle.DEFAULT_LOCALE);
	}

	public String getLabel(final Locale pLocale)
	{
		final ResourceBundle resourceBundle = EventLevelResourceBundle.getBundle(pLocale);
		return resourceBundle.getString(toString() + "." + EventLevelResourceBundle.LABEL);
	}

	private static final class EventLevelResourceBundle
	{
		private static final Locale DEFAULT_LOCALE = new Locale("fr");

		private static final String PROPERTY_FILE  = "eventlevel.level";

		private static final String LABEL          = "label";

		public static ResourceBundle getBundle(final Locale pLocale)
		{
			return ResourceBundle.getBundle(PROPERTY_FILE, pLocale);
		}

	}
}
