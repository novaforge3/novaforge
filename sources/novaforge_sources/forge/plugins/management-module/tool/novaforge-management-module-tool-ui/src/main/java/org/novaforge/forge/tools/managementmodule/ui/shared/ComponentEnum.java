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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.shared;

/**
 * @author BILET-JC
 * 
 *         All possible choice when estimation is set thanks to function points
 *         in SIMPLE way
 */
public enum ComponentEnum {
	NONE {
		@Override
		public String getLabel() {
			return Common.GLOBAL.none();
		}
	},
	GDI {
		@Override
		public String getLabel() {
			return Common.MESSAGES_ESTIMATION.gdi();
		}
	},
	GDE {
		@Override
		public String getLabel() {
			return Common.MESSAGES_ESTIMATION.gde();
		}
	},
	IN {
		@Override
		public String getLabel() {
			return Common.MESSAGES_ESTIMATION.in();
		}
	},
	OUT {
		@Override
		public String getLabel() {
			return Common.MESSAGES_ESTIMATION.out();
		}
	},
	INT {
		@Override
		public String getLabel() {
			return Common.MESSAGES_ESTIMATION.interrogation();
		}
	};

	/**
	 * This method return a ComponentEnum depending on the pEnum parameter
	 * 
	 * @param pEnum
	 * @return
	 */
	public static ComponentEnum getEnum(String pEnum) {
		ComponentEnum ret = null;
		if (pEnum != null) {
			if (pEnum.equals(NONE.name())) {
				ret = NONE;
			}
			if (pEnum.equals(GDI.name())) {
				ret = GDI;
			}
			if (pEnum.equals(GDE.name())) {
				ret = GDE;
			}
			if (pEnum.equals(IN.name())) {
				ret = IN;
			}
			if (pEnum.equals(OUT.name())) {
				ret = OUT;
			}
			if (pEnum.equals(INT.name())) {
				ret = INT;
			}
		} else {
			ret = GDI;
		}

		return ret;
	}

	/**
	 * Can only be called in gwt client
	 * @return the enum's label
	 */
	public abstract String getLabel();
}
