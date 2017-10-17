/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2014  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.beaver.deployment.plugin.deploy.engine;

import java.io.IOException;
import java.util.Map;

import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Gauthier Cart
 */
public interface HTTPService
{

  /**
   * Allows to query an HTTP POST method with a given URL and POST datas.
   * 
   * @param pURL
   *          URL to query
   * @param pParams
   *          POST data into a Map
   * @throws BeaverException
   * @throws IOException
   */
  String post(final String pURL, final Map<String, String> pParams) throws BeaverException, IOException;

}
