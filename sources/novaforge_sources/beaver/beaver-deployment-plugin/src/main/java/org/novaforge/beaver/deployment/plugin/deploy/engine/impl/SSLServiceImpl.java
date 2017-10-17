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
package org.novaforge.beaver.deployment.plugin.deploy.engine.impl;

import java.util.ArrayList;
import java.util.List;

import org.novaforge.beaver.deployment.plugin.deploy.engine.SSLService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec.ExecFacade;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Guillaume Lamirand
 */
public class SSLServiceImpl implements SSLService
{
  private static final String OPENSSL_COMMAND = "openssl";
  private static final String KEYTOOL_COMMAND = "keytool";

  /**
   * {@inheritDoc}
   * 
   * @throws BeaverException
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
    final List<String> params = new ArrayList<>();
    params.add("req");
    params.add("-x509");
    params.add("-nodes");
    params.add("-days");
    params.add(String.valueOf(pDays));
    params.add("-newkey");
    params.add(String.format("rsa:%s", pKeySize));
    params.add("-keyout");
    params.add(pOutKeyFile);
    params.add("-out");
    params.add(pOutCSRFile);
    params.add("-subj");
    params.add(String.format("/C=%s/ST=Default/L=%s/O=%s/OU=%s/CN=%s/emailAddress=%s", pCountry, pLocal,
        pOrganization, pOrganizationUnit, pCommonName, pEmail));

    final int returnCode = ExecFacade.execCommandWithParams(OPENSSL_COMMAND, params);
    if (ExecFacade.isSuccess(returnCode) == false)
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
    final List<String> params = new ArrayList<>();
    params.add("pkcs12");
    params.add("-export");
    params.add("-in");
    params.add(pCSRFile);
    params.add("-inkey");
    params.add(pKeyFile);
    params.add("-out");
    params.add(pOutP12File);
    params.add("-name");
    params.add(pCommonName);
    params.add("-passin");
    params.add(String.format("pass:%s", pP12PwdIN));
    params.add("-passout");
    params.add(String.format("pass:%s", pP12PwdOUT));
    final int returnCode = ExecFacade.execCommandWithParams(OPENSSL_COMMAND, params);
    if (ExecFacade.isSuccess(returnCode) == false)
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
    extractKey(pOutKeyFile, pP12File, pP12PwdIN, pP12PwdOUT);
    extractCSR(pOutCSRFile, pP12File, pP12PwdIN, pP12PwdOUT);
  }

  private void extractKey(final String pOutKeyFile, final String pP12File, final String pP12PwdIN,
      final String pP12PwdOUT) throws BeaverException
  {
    final List<String> params = new ArrayList<>();
    params.add("pkcs12");
    params.add("-in");
    params.add(pP12File);
    params.add("-nocerts");
    params.add("-out");
    params.add(pOutKeyFile);
    params.add("-nodes");
    params.add("-passin");
    params.add(String.format("pass:%s", pP12PwdIN));
    params.add("-passout");
    params.add(String.format("pass:%s", pP12PwdOUT));
    final int returnCode = ExecFacade.execCommandWithParams(OPENSSL_COMMAND, params);
    if (ExecFacade.isSuccess(returnCode) == false)
    {
      throw new BeaverException(
          String
              .format(
                  "Unable to extract key from PKC12 with given parameters [out_key=%s, p12_file=%s, pwd_in=%s, pwd_out=%s]",
                  pOutKeyFile, pP12File, pP12PwdIN, pP12PwdOUT));
    }
  }

  private void extractCSR(final String pOutCSRFile, final String pP12File, final String pP12PwdIN,
      final String pP12PwdOUT) throws BeaverException
  {
    final List<String> params = new ArrayList<>();
    params.add("pkcs12");
    params.add("-in");
    params.add(pP12File);
    params.add("-nokeys");
    params.add("-out");
    params.add(pOutCSRFile);
    params.add("-nodes");
    params.add("-passin");
    params.add(String.format("pass:%s", pP12PwdIN));
    params.add("-passout");
    params.add(String.format("pass:%s", pP12PwdOUT));
    final int returnCode = ExecFacade.execCommandWithParams(OPENSSL_COMMAND, params);
    if (ExecFacade.isSuccess(returnCode) == false)
    {
      throw new BeaverException(
          String
              .format(
                  "Unable to extract CSR from PKC12 with given parameters [out_csr=%s, p12_file=%s, pwd_in=%s, pwd_out=%s]",
                  pOutCSRFile, pP12File, pP12PwdIN, pP12PwdOUT));
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
    BeaverLogger.getFilelogger().info(String.format("    - P12PwdOut : %s", pP12PwdOut));
    BeaverLogger.getFilelogger().info(String.format("    - Alias: %s", pAlias));
    BeaverLogger.getFilelogger().info(String.format("    - JKS: %s", pJKS));
    BeaverLogger.getFilelogger().info(String.format("    - JKSPwd: %s", pJKSPwd));
    BeaverLogger.getFilelogger().info(String.format("    - KeySize: %s", pKeySize));

    final List<String> params = new ArrayList<>();
    params.add("-importkeystore");
    params.add("-srcstoretype");
    params.add("PKCS12");
    params.add("-srckeystore");
    params.add(pP12);
    params.add("-srcstorepass");
    params.add(pP12PwdOut);
    params.add("-destkeystore");
    params.add(pJKS);
    params.add("-deststorepass");
    params.add(pJKSPwd);
    params.add("-destkeypass");
    params.add(pJKSPwd);
    params.add("-keysize");
    params.add(String.valueOf(pKeySize));
    params.add("-alias");
    params.add(pAlias);
    params.add("-noprompt");
    final int returnCode = ExecFacade.execCommandWithParams(KEYTOOL_COMMAND, params);
    if (ExecFacade.isSuccess(returnCode) == false)
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

    final List<String> params = new ArrayList<>();
    params.add("-importkeystore");
    params.add("-srckeystore");
    params.add(pSourceJKS);
    params.add("-srcstorepass");
    params.add(pSourcePwd);
    params.add("-destkeystore");
    params.add(pDestJKS);
    params.add("-deststorepass");
    params.add(pDestPwd);
    params.add("-noprompt");
    final int returnCode = ExecFacade.execCommandWithParams(KEYTOOL_COMMAND, params);
    if (ExecFacade.isSuccess(returnCode) == false)
    {
      throw new BeaverException(
          String
              .format(
                  "Unable to import JKS into JKS with given parameters [source_jks=%s, source_pwd=%s, dest_jks=%s, dest_pwd=%s]",
                  pSourceJKS, pSourcePwd, pDestJKS, pDestPwd));
    }

  }

}
