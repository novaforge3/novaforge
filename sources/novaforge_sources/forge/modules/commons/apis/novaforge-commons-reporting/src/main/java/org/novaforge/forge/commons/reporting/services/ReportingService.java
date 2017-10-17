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
package org.novaforge.forge.commons.reporting.services;

import org.novaforge.forge.commons.reporting.exceptions.ReportingException;
import org.novaforge.forge.commons.reporting.model.OutputFormat;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author sbenoist
 *
 */
public interface ReportingService
{
   /**
    * This method allows to render a report from a design filename in an output format with given parameters
    * 
    * @param pDesignFileName
    *           String
    * @param pOutputFormat
    *           OutputFormat (PDF, HTML, PPT, DOC, PS)
    * @param pOutputFileName
    *           String
    * @param pParameters
    *           Map<String, Object>
    * @throws ReportingException
    */
   void renderReport(String pDesignFileName, OutputFormat pOutputFormat, String pOutputFileName,
         Map<String, Object> pParameters) throws ReportingException;
   /**
    * This method allows to render a report from a design filename in an output format with given parameters
    * 
    * @param pDesignFileInputStream
    *           InputStream
    * @param pOutputFormat
    *           OutputFormat (PDF, HTML, PPT, DOC, PS)
    * @param pOutputFileName
    *           String
    * @param pParameters
    *           Map<String, Object>
    * @throws ReportingException
    */
   void renderReport(final InputStream pDesignFileInputStream, final OutputFormat pOutputFormat,
         final String pOutputFileName,
         final Map<String, Object> pParameters) throws ReportingException;
   
   /**
    * This method allows to render a report from a design filename in an output format with given parameters
    * 
    * @param pDesignFileName
    *           String
    * @param pOutputFormat
    *           OutputFormat (PDF, HTML, PPT, DOC, PS)
    * @param pOutputStream
    *           OutputStream
    * @param pParameters
    *           Map<String, Object>
    * @throws ReportingException
    */
   void renderReport(final String pDesignFileName, final OutputFormat pOutputFormat,
         final OutputStream pOutputStream,
         final Map<String, Object> pParameters) throws ReportingException;
   
   /**
    * This method allows to render a report from a design filename in an output format with given parameters
    * 
    * @param pDesignFileInputStream
    *           InputStream
    * @param pOutputFormat
    *           OutputFormat (PDF, HTML, PPT, DOC, PS)
    * @param pOutputStream
    *           OutputStream
    * @param pParameters
    *           Map<String, Object>
    * @throws ReportingException
    */
   void renderReport(final InputStream pDesignFileInputStream, final OutputFormat pOutputFormat,
         final OutputStream pOutputStream,
         final Map<String, Object> pParameters) throws ReportingException;
}
