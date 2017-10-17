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

package org.novaforge.forge.importexport.exceptions;

/**
 * Main exception used by import servicesto throw errors
 * 
 * @author Guillaume Lamirand
 */
public class ImportExportServiceException extends Exception
{

	/**
	 * Serial version
	 */
	private static final long	serialVersionUID	= 8206115964363386738L;

	/**
	 * Constructs a new exception with <code>null</code> as its detail message.
	 * 
	 * @see Exception#Exception()
	 */
	public ImportExportServiceException()
	{
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param pMessage
	 *           the source message
	 * @see Exception#Exception(String)
	 */
	public ImportExportServiceException(final String pMessage)
	{
		super(pMessage);
	}

	/**
	 * onstructs a new exception with the specified cause and a detail
	 * message of <tt>(cause==null ? null : cause.toString())
	 * 
	 * @param pCause
	 *           the source cause
	 * @see Exception#Exception(Throwable)
	 */
	public ImportExportServiceException(final Throwable pCause)
	{
		super(pCause);
	}

	/**
	 * Constructs a new exception with the specified detail message and
	 * cause.
	 * 
	 * @param pMessage
	 *           the source message
	 * @param pCause
	 *           the source cause
	 * @see Exception#Exception(String, Throwable)
	 */
	public ImportExportServiceException(final String pMessage, final Throwable pCause)
	{
		super(pMessage, pCause);
	}

}
