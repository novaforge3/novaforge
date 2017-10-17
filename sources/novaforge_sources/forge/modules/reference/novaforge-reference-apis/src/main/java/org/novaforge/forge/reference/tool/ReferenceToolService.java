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
package org.novaforge.forge.reference.tool;

/**
 * Service used to get needed information is order to manage the Upload/Download of the Reference Tools.
 * 
 * @author germain-c
 */
public interface ReferenceToolService
{
  String SEPARATOR_VERSION = "--";

  /**
   * @return the full path of the directory where the administratives tools are stored.
   */
  String getUserStorageDirectory();

  /**
   * @return the full path of the directory where the users tools are stored.
   */
  String getAdminStorageDirectory();

  /**
   * @return the certificate public key file needed by the rsync over ssh command so we are not
   *         asked to enter the password.
   */
  String getCertificatePublicKeyFile();

  /**
   * @return the file containing the list of authorized certificate public keys.
   */
  String getAuthorizedCertificatePublicKeysFile();

  /**
   * Get the rsync command used to synchronize the central reference tools repository with the children
   * forges
   * 
   * @return rsync command
   */
  String getRsyncCommand();

  /**
   * Get the rsync command arguments
   * 
   * @return rsync command arguments
   */
  String getRsyncArguments();

  /**
   * Get the system user used by the rsync command
   * 
   * @return system user
   */
  String getRsyncUser();

  /**
   * Get the chmod command
   * 
   * @return chmod command
   */
  String getChmodCommand();

  /**
   * Get the chmod access that need to be enforced on the authorized_keys file
   * 
   * @return chmod access
   */
  String getAuthorizedCertificatePublicKeysFileAccess();

  /**
   * Get the a absolute pathname of the File which will be stored.
   * 
   * @param pName
   *          : name of file
   * @param pVersion
   *          : version of file
   * @param pSuffix
   *          : the suffix of file (ie doc, jpg)
   * @param pIsPublic
   *          : if the file is stored into public directory ou admin directory
   * @return file name
   */
  String getFileName(String pName, String pVersion, String pSuffix, Boolean pIsPublic);

  /**
   * Get the Full Path of the destination Directory (public=user space or admin=admin space).
   * 
   * @param pIsPublic
   * @return Full Path of the destination Directory
   */
  String getDestDirectoy(Boolean pIsPublic);
}
