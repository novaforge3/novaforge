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
package org.novaforge.forge.reference.internal;

import org.novaforge.forge.reference.tool.ReferenceToolService;

import java.io.File;

public class ReferenceToolServiceImpl implements ReferenceToolService
{

  private String adminStorageDirectory                     = "/datas/novaforge3/data/referenceTool/admin/";
  private String userStorageDirectory                      = "/datas/novaforge3/data/referenceTool/public/";
  private String rsyncCommand                              = "/usr/bin/rsync";
  private String rsyncArguments                            = "-avzr -e ssh --delete";
  private String rsyncUser                                 = "novaforge";
  private String certificatePublicKeyFile                  = "/home/novaforge/.ssh/id_rsa.pub";
  private String authorizedCertificatePublicKeysFile       = "/home/novaforge/.ssh/authorized_keys";
  private String chmodCommand                              = "/bin/chmod";
  private String authorizedCertificatePublicKeysFileAccess = "600";

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRsyncCommand()
  {
    return rsyncCommand;
  }

  /**
   * @param pRsyncCommand
   */
  public void setRsyncCommand(final String pRsyncCommand)
  {
    rsyncCommand = pRsyncCommand;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRsyncArguments()
  {
    return rsyncArguments;
  }

  /**
   * @param pRsyncArguments
   */
  public void setRsyncArguments(final String pRsyncArguments)
  {
    rsyncArguments = pRsyncArguments;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRsyncUser()
  {
    return rsyncUser;
  }

  /**
   * @param pRsyncUser
   */
  public void setRsyncUser(final String pRsyncUser)
  {
    rsyncUser = pRsyncUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCertificatePublicKeyFile()
  {
    return certificatePublicKeyFile;
  }

  /**
   * @param pCertificatePublicKeyFile
   */
  public void setCertificatePublicKeyFile(final String pCertificatePublicKeyFile)
  {
    certificatePublicKeyFile = pCertificatePublicKeyFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthorizedCertificatePublicKeysFile()
  {
    return authorizedCertificatePublicKeysFile;
  }

  /**
   * @param pAuthorizedCertificatePublicKeysFile
   */
  public void setAuthorizedCertificatePublicKeysFile(final String pAuthorizedCertificatePublicKeysFile)
  {
    authorizedCertificatePublicKeysFile = pAuthorizedCertificatePublicKeysFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getChmodCommand()
  {
    return chmodCommand;
  }

  /**
   * @param pChmodCommand
   */
  public void setChmodCommand(final String pChmodCommand)
  {
    chmodCommand = pChmodCommand;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthorizedCertificatePublicKeysFileAccess()
  {
    return authorizedCertificatePublicKeysFileAccess;
  }

  /**
   * @param pAuthorizedCertificatePublicKeysFileAccess
   */
  public void setAuthorizedCertificatePublicKeysFileAccess(
      final String pAuthorizedCertificatePublicKeysFileAccess)
  {
    authorizedCertificatePublicKeysFileAccess = pAuthorizedCertificatePublicKeysFileAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAdminStorageDirectory()
  {
    return adminStorageDirectory;
  }

  /**
   * @param pAdminStorageDirectory
   */
  public void setAdminStorageDirectory(final String pAdminStorageDirectory)
  {
    if (!(pAdminStorageDirectory.endsWith(File.separator)))
    {
      adminStorageDirectory = pAdminStorageDirectory.concat(File.separator);
    }
    else
    {
      adminStorageDirectory = pAdminStorageDirectory;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserStorageDirectory()
  {
    return userStorageDirectory;
  }

  /**
   * @param pUserStorageDirectory
   */
  public void setUserStorageDirectory(final String pUserStorageDirectory)
  {
    if (!(pUserStorageDirectory.endsWith(File.separator)))
    {
      userStorageDirectory = pUserStorageDirectory.concat(File.separator);
    }
    else
    {
      userStorageDirectory = pUserStorageDirectory;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFileName(final String pName, final String pVersion, final String pSuffix,
      final Boolean pIsPublic)
  {
    StringBuilder returned = new StringBuilder();
    returned = returned.append(getDestDirectoy(pIsPublic)).append(pName)
        .append(ReferenceToolService.SEPARATOR_VERSION).append(pVersion).append(pSuffix);
    return returned.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDestDirectoy(final Boolean pIsPublic)
  {
    if (pIsPublic)
    {
      return getUserStorageDirectory();
    }
    else
    {
      return getAdminStorageDirectory();
    }
  }

}
