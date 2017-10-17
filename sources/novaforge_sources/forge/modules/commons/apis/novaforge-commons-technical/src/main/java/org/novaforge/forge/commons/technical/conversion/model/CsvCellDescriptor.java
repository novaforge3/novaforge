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
package org.novaforge.forge.commons.technical.conversion.model;

public interface CsvCellDescriptor
{
	/**
	 * returns the name of the cell (displayed on the header if required)
	 * 
	 * @return String the name of the cell
	 */
	String getName();

	/**
	 * returns the name of the cell
	 * 
	 * @param pName
	 */
	void setName(final String pName);

	/**
	 * returns the name of the javabean field binded to the cell
	 * 
	 * @return String
	 */
	String getBindName();

	/**
	 * set the name of the javabean field binded to the cell
	 * 
	 * @param pFieldName
	 */
	void setBindName(String pBindName);

	/**
	 * returns the format of the cell
	 * 
	 * @return
	 */
	String getFormat();

	/**
	 * set the format of the cell (used for Date or Decimal types)
	 * 
	 * @param pFormat
	 */
	void setFormat(final String pFormat);

	/**
	 * returns true or false if the cell is mandatory or not
	 * 
	 * @return boolean
	 */
	boolean isMandatory();

	/**
	 * defines if the cell is mandatory
	 * 
	 * @param pMandatory
	 */
	void setMandatory(final boolean pMandatory);
}