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

import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SSLService;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Guillaume Lamirand
 */
public class SSLServiceSimulateImpl implements SSLService
{

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateKeyAndCSR(final String pOutKeyFile, final String pOutCSRFile, final int pDays,
      final int pKeySize, final String pCountry, final String pLocal, final String pOrganization,
      final String pOrganizationUnit, final String pCommonName, final String pEmail) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Generate a key and CSR files with :");
    BeaverLogger.getFilelogger().info(String.format("    - Out KEY: %s", pOutKeyFile));
    BeaverLogger.getFilelogger().info(String.format("    - Out CSR : %s", pOutCSRFile));
    BeaverLogger.getFilelogger().info(String.format("    - Days: %s", pDays));
    BeaverLogger.getFilelogger().info(String.format("    - KeySize: %s", pKeySize));
    BeaverLogger.getFilelogger().info(String.format("    - Country: %s", pCountry));
    BeaverLogger.getFilelogger().info(String.format("    - Local: %s", pLocal));
    BeaverLogger.getFilelogger().info(String.format("    - Organization: %s", pOrganization));
    BeaverLogger.getFilelogger().info(String.format("    - OrganizationUnit: %s", pOrganizationUnit));
    BeaverLogger.getFilelogger().info(String.format("    - CN: %s", pCommonName));
    BeaverLogger.getFilelogger().info(String.format("    - Email: %s", pEmail));

    if ((StringUtils.isBlank(pOutKeyFile)) || (StringUtils.isBlank(pOutCSRFile)) || (pDays == 0)
        || (pKeySize == 0) || (StringUtils.isBlank(pCountry)) || (StringUtils.isBlank(pLocal))
        || (StringUtils.isBlank(pOrganization)) || (StringUtils.isBlank(pOrganizationUnit))
        || (StringUtils.isBlank(pCommonName)) || (StringUtils.isBlank(pEmail)))
    {

      throw new BeaverException(
          String
              .format(
                  "Unable to build CSR and key files with given parameters [out_key=%s, out_csr=%s, Days=%s, KeySize=%s, Country=%s, Local=%s, Organization=%s, OrganizationUnit=%s, CommonName=%s, Email=%s]",
                  pOutKeyFile, pOutCSRFile, pDays, pKeySize, pCountry, pLocal, pOrganization,
                  pOrganizationUnit, pCommonName, pEmail));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void buildPKCS12(final String pOutP12File, final String pKeyFile, final String pCSRFile,
      final String pCommonName, final String pP12PwdIN, final String pP12PwdOUT) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Build PKCS12 file with :");
    BeaverLogger.getFilelogger().info(String.format("    - Out P12: %s", pOutP12File));
    BeaverLogger.getFilelogger().info(String.format("    - KeyFile: %s", pKeyFile));
    BeaverLogger.getFilelogger().info(String.format("    - CSRFile: %s", pCSRFile));
    BeaverLogger.getFilelogger().info(String.format("    - CN: %s", pCommonName));
    BeaverLogger.getFilelogger().info(String.format("    - P12PwdIN: %s", pP12PwdIN));
    BeaverLogger.getFilelogger().info(String.format("    - P12PwdOUT: %s", pP12PwdOUT));

    if ((StringUtils.isBlank(pOutP12File)) || (StringUtils.isBlank(pKeyFile))
        || (StringUtils.isBlank(pCSRFile)) || (StringUtils.isBlank(pCommonName))
        || (StringUtils.isBlank(pP12PwdIN)) || (StringUtils.isBlank(pP12PwdOUT)))
    {

      throw new BeaverException(
          String
              .format(
                  "Unable to build PKC12 with given parameters [out_p12=%s, key_file=%s, csr_file=%s, cn=%s, pwd_in=%s, pwd_out=%s]",
                  pOutP12File, pKeyFile, pCSRFile, pCommonName, pP12PwdIN, pP12PwdOUT));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void extractKeyAndCSR(final String pOutKeyFile, final String pOutCSRFile, final String pP12File,
      final String pP12PwdIN, final String pP12PwdOUT) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Extract Key and CSR files from PKCS12 with :");
    BeaverLogger.getFilelogger().info(String.format("    - Out KEY: %s", pOutKeyFile));
    BeaverLogger.getFilelogger().info(String.format("    - Out CSR : %s", pOutCSRFile));
    BeaverLogger.getFilelogger().info(String.format("    - P12File: %s", pP12File));
    BeaverLogger.getFilelogger().info(String.format("    - P12PwdIN: %s", pP12PwdIN));
    BeaverLogger.getFilelogger().info(String.format("    - P12PwdOUT: %s", pP12PwdOUT));

    if ((StringUtils.isBlank(pOutKeyFile)) || (StringUtils.isBlank(pOutCSRFile))
        || (StringUtils.isBlank(pP12File)) || (StringUtils.isBlank(pP12PwdIN))
        || (StringUtils.isBlank(pP12PwdOUT)))
    {

      throw new BeaverException(
          String
              .format(
                  "Unable to extract key or CSR from PKC12 with given parameters [out_key=%s, out_csr=%s, p12_file=%s, pwd_in=%s, pwd_out=%s]",
                  pOutKeyFile, pOutCSRFile, pP12File, pP12PwdIN, pP12PwdOUT));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void importPKCS12toJKS(final String pP12, final String pP12PwdOut, final String pAlias,
      final String pJKS, final String pJKSPwd, final int pKeySize) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Import PCKS12 to JKS with :");
    BeaverLogger.getFilelogger().info(String.format("    - P12: %s", pP12));
    BeaverLogger.getFilelogger().info(String.format("    - P2PwdOut : %s", pP12PwdOut));
    BeaverLogger.getFilelogger().info(String.format("    - Alias: %s", pAlias));
    BeaverLogger.getFilelogger().info(String.format("    - JKS: %s", pJKS));
    BeaverLogger.getFilelogger().info(String.format("    - JKSPwd: %s", pJKSPwd));
    BeaverLogger.getFilelogger().info(String.format("    - KeySize: %s", pKeySize));

    if ((StringUtils.isBlank(pP12)) || (StringUtils.isBlank(pP12PwdOut)) || (StringUtils.isBlank(pAlias))
        || (StringUtils.isBlank(pJKS)) || (StringUtils.isBlank(pJKSPwd)) || (pKeySize == 0))
    {
      throw new BeaverException(
          String
              .format(
                  "Unable to import P12 into JKS with given parameters [P12=%s, P12PwdOut=%s, Alias=%s, pJKS=%s, pJKSPwd=%s, KeySize=%s]",
                  pP12, pP12PwdOut, pAlias, pJKS, pJKSPwd, pKeySize));
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void importJKStoJKS(final String pSourceJKS, final String pSourcePwd, final String pDestJKS,
      final String pDestPwd) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Import JKS to JKS with :");
    BeaverLogger.getFilelogger().info(String.format("    - pSourceJKS: %s", pSourceJKS));
    BeaverLogger.getFilelogger().info(String.format("    - pSourcePwd : %s", pSourcePwd));
    BeaverLogger.getFilelogger().info(String.format("    - pDestJKS: %s", pDestJKS));
    BeaverLogger.getFilelogger().info(String.format("    - pDestPwd: %s", pDestPwd));

    if ((StringUtils.isBlank(pSourceJKS)) || (StringUtils.isBlank(pSourcePwd))
        || (StringUtils.isBlank(pDestJKS)) || (StringUtils.isBlank(pDestPwd)))
    {
      throw new BeaverException(
          String
              .format(
                  "Unable to import JKS into JKS with given parameters [source_jks=%s, source_pwd=%s, dest_jks=%s, dest_pwd=%s]",
                  pSourceJKS, pSourcePwd, pDestJKS, pDestPwd));
    }

  }
}
