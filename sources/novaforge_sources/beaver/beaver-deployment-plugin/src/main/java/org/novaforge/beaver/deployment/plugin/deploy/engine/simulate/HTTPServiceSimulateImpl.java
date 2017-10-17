/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.deploy.engine.simulate;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.validator.UrlValidator;
import org.novaforge.beaver.deployment.plugin.deploy.engine.HTTPService;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Gauthier Cart
 */
public class HTTPServiceSimulateImpl implements HTTPService
{

  @Override
  public String post(String pURL, Map<String, String> pParams) throws BeaverException, IOException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Request HTTP POST with [URL=%S, Params=%s]", pURL.toString(), pParams.toString()));

    // Test if the URL is valid using UrlValidator
    UrlValidator defaultValidator = new UrlValidator();
    if (!defaultValidator.isValid(pURL))
    {
      throw new BeaverException(String.format("URL is empty with [URL=%s]", pURL));
    }
    return null;
  }

}
