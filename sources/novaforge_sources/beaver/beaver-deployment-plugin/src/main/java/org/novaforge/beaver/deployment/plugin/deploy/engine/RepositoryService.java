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
package org.novaforge.beaver.deployment.plugin.deploy.engine;

import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Guillaume Lamirand
 */
public interface RepositoryService
{

  /**
   * Allows to install rpms from a temporary repository given in parameter.
   * <p>
   * It will :
   * <ul>
   * <li>Unpack from archive given to temporary directory</il>
   * <li>Create tempary YUM repository from it</il>
   * <li>Clean YUM cache</il>
   * <li>Execute YUM install with the rpms name given</il>
   * </ul>
   * </p>
   * 
   * @param pArchive
   *          path to the archive containing rpms to install
   * @param pRpms
   *          the list of rpm name to install
   * @throws BeaverException
   */
  void installRPMs(final String pArchive, final String... pRpms) throws BeaverException;

  /**
   * Allows to remove rpms.
   * <p>
   * It will :
   * <ul>
   * <li>Execute YUM remove with the rpms name given</il>
   * </ul>
   * </p>
   * 
   * @param pRpms
   *          the list of rpm name to install
   * @throws BeaverException
   */
  void removeRPMs(String... pRpms) throws BeaverException;

}
