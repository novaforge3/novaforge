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
public interface SSLService
{

  void generateKeyAndCSR(final String pOutKeyFile, final String pOutCSRFile, final int pDays,
      final int pKeySize, final String pCountry, final String pLocal, final String pOrganization,
      final String pOrganizationUnit, final String pCommonName, final String pEmail) throws BeaverException;

  void buildPKCS12(final String pOutP12File, final String pKeyFile, final String pCSRFile,
      final String pCommonName, final String pP12PwdIN, final String pP12PwdOUT) throws BeaverException;

  void extractKeyAndCSR(final String pOutKeyFile, final String pOutCSRFile, final String pP12File,
      final String pP12PwdIN, final String pP12PwdOUT) throws BeaverException;

  /**
   * @param pP12
   * @param pP12PwdOut
   * @param pAlias
   * @param pJKS
   * @param pJKSPwd
   * @param pKeySize
   * @throws BeaverException
   */
  void importPKCS12toJKS(String pP12, String pP12PwdOut, String pAlias, String pJKS, String pJKSPwd,
      int pKeySize) throws BeaverException;

  /**
   * @param pSourceJKS
   * @param pSourcePwd
   * @param pDestJKS
   * @param pDestPwd
   * @throws BeaverException
   */
  void importJKStoJKS(String pSourceJKS, String pSourcePwd, String pDestJKS, String pDestPwd)
      throws BeaverException;

}