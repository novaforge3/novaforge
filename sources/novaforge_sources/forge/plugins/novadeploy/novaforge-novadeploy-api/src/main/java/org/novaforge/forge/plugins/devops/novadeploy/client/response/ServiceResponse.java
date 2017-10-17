/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * 
 * This file is free software: you may redistribute and/or modify it
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the
 * License.
 * 
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have
 * received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses.
 * 
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section
 * 7.
 * 
 * If you modify this Program, or any covered work, by linking or
 * combining it with libraries listed in COPYRIGHT file at the
 * top-level directory of this distribution (or a modified version of
 * that libraries), containing parts covered by the terms of licenses
 * cited in the COPYRIGHT file, the licensors of this Program grant
 * you additional permission to convey the resulting work.
 */

package org.novaforge.forge.plugins.devops.novadeploy.client.response;

import java.io.Serializable;

/**
 * @author pawlikowski-m
 * 
 *         Representation of a generic response from the web service
 */
@SuppressWarnings("serial")
public class ServiceResponse implements Serializable
{

	public static String SAX_ERROR = "0";
	public static String SERVICE_ERROR = "1";
	// private static final long serialVersionUID =
	// 2883760499709505557L;
	public String status;
	public String error;
	public String errorNumber;
	public String errorType;
	public String parserLineError;
	public String parserColumnError;
	public String parserMessageError;
	
	public boolean hasParsingError()
	{
		return getParserMessageError() != null && getParserMessageError().length() > 0;
	}
	public String getParserLineError()
	{
		return parserLineError;
	}

	public void setParserLineError(String parserLineError)
	{
		this.parserLineError = parserLineError;
	}

	public String getParserColumnError()
	{
		return parserColumnError;
	}

	public void setParserColumnError(String parserColumnError)
	{
		this.parserColumnError = parserColumnError;
	}

	public String getParserMessageError()
	{
		return parserMessageError;
	}

	public void setParserMessageError(String parserMessageError)
	{
		this.parserMessageError = parserMessageError;
	}

	public String getErrorNumber()
	{
		return errorNumber;
	}

	public void setErrorNumber(String errorNumber)
	{
		this.errorNumber = errorNumber;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getErrorType()
	{
		return errorType;
	}

	public void setErrorType(String errorType)
	{
		this.errorType = errorType;
	}

	public boolean hasError()
	{
		return this.getError() != null && this.getError().length() > 0;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

}