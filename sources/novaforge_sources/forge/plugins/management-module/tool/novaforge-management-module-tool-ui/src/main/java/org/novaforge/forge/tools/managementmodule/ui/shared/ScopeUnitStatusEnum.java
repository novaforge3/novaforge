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
package org.novaforge.forge.tools.managementmodule.ui.shared;

public enum ScopeUnitStatusEnum {
	none {
		@Override
		public String getLabel() {
			return Common.EMPTY_TEXT;
		}
	},
	obsolete {
		@Override
		public String getLabel() {
			return Common.MESSAGES_SCOPE.statusObsolete();
		}
	},
	modified {
		@Override
		public String getLabel() {
			return Common.MESSAGES_SCOPE.statusModified();
		}
	};

	/**
	 * This method return a ScopeUnitStatusEnum depending on the pEnum parameter
	 * 
	 * @param pEnum
	 * @return the matching ScopeUnitStatusEnum
	 */
	public static ScopeUnitStatusEnum getEnum(String pEnum) {
		ScopeUnitStatusEnum ret = none;
		if (pEnum != null) {
			if (pEnum.equals(obsolete.name())) {
				ret = obsolete;
			}
			if (pEnum.equals(modified.name())) {
				ret = modified;
			}
		}
		return ret;
	}

	/**
	 * Can only be called in gwt client
	 * @return the enum's label
	 */
	public abstract String getLabel();
}
