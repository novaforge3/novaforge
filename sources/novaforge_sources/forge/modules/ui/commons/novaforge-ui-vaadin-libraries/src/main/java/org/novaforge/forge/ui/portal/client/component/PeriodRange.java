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
package org.novaforge.forge.ui.portal.client.component;

import com.vaadin.ui.ComboBox;

/**
 * @author caseryj
 */
public enum PeriodRange {

	/**
	 * Hour time period range
	 */
	HOUR {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getId() {
			return "hour";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getFactorToHour() {
			return 1;
		}

	},
	/**
	 * DAY time period range
	 */
	DAY {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getId() {
			return "day";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getFactorToHour() {
			return 24;
		}

	},
	/**
	 * WEEK time period range
	 */
	WEEK {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getId() {
			return "week";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getFactorToHour() {
			return 168;
		}

	},
	/**
	 * Month time period range
	 */
	MONTH {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getId() {
			return "month";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getFactorToHour() {
			return 744;
		}

	},
	/**
	 * Year time period range
	 */
	YEAR {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getId() {
			return "year";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getFactorToHour() {
			return 8760;
		}

	};

	private final static String I18N_MESSAGE_PREFIX = "usermanagement.periodrange.";

	/**
	 * Get the period range to display for the given hours
	 *
	 * @param pValue
	 *            The period hours value
	 * @return {@link PeriodRange} the periodrange
	 */
	public static PeriodRange getForHour(final int pValue)
	{
		PeriodRange range = null;
		for (final PeriodRange periodRange : PeriodRange.values())
		{
			final int moduloValue = pValue % periodRange.getFactorToHour();
			if (moduloValue == 0)
			{
				if ((range == null) || (periodRange.getFactorToHour() > range.getFactorToHour()))
				{
					range = periodRange;
				}
			}
		}

		return range;
	}

	/**
	 * Get the period range factor to convert it to hour
	 * 
	 * @return the range factor
	 */
	public abstract int getFactorToHour();

	/**
	 * Get the I18N message id to use for internationalization
	 * 
	 * @return the message id
	 */
	public String getI18NMessageId() {
		return I18N_MESSAGE_PREFIX + getId();
	}

	/**
	 * Get Id used by {@link ComboBox}
	 *
	 * @return item id
	 */
	public abstract String getId();

}
