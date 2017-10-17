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
package org.novaforge.forge.commons.technical.conversion;

import org.novaforge.forge.commons.technical.conversion.model.CsvConverterDescriptor;
import org.novaforge.forge.commons.technical.conversion.model.CsvConverterResponse;

import java.util.Collection;

/**
 * @author sbenoist
 * @date Mar 10, 2010
 */
public interface CsvConverterService
{

	/**
	 * converts a csv file into java beans of T Type The T fields must have simple types to allow mapping and
	 * formatting. ie String, Integer, Double, Boolean, Short, Character, Float, Long, Byte, Date et DateTime
	 * or primitives.
	 * 
	 * @param <T>
	 * @param pFileIn
	 *          the filename to use to extract beans
	 * @param pClass
	 *          the class of beans
	 * @param pDescriptor
	 *          the descriptor
	 * @return CsvConverterResponse
	 * @throws CsvConversionException
	 */
	<T> CsvConverterResponse<T> importFromFile(final String pFileIn, final Class<T> pClass,
	    final CsvConverterDescriptor pDescriptor) throws CsvConversionException;

	/**
	 * converts a java beans collections into a formatted csv file. The T fields must have simple types to
	 * allow mapping and formatting. ie String, Integer, Double, Boolean, Short, Character, Float, Long, Byte,
	 * Date et DateTime or primitives.
	 * 
	 * @param <T>
	 * @param pFileOut
	 *          the filename to use to write beans
	 * @param pClass
	 *          the class of beans
	 * @param pBeans
	 *          the Collection to convert into file
	 * @param pDescriptor
	 *          the descriptor
	 * @throws CsvConversionException
	 */
	<T> void exportToFile(final String pFileOut, final Class<T> pClass, final Collection<T> pBeans,
	    final CsvConverterDescriptor pDescriptor) throws CsvConversionException;
}
