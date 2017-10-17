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
package org.novaforge.forge.core.configuration.services;

import java.net.URL;
import java.util.List;

/**
 * Main service of forge configuration
 * 
 * @author Guillaume Lamirand
 */
public interface ForgeConfigurationService
{

  /**********************************************************************************
   * CFG configuration
   *********************************************************************************/
  /**
   * Returns path to forge configuraton folder
   * 
   * @return as {@link String} path to forge configuraton folder
   */
  String getForgeConfDirectory();

  /**********************************************************************************
   * Database configuration
   *********************************************************************************/
  /**
   * Returns the Forge projectId
   * 
   * @return forge projectId
   */
  String getForgeProjectId();

  /**
   * Sets forge project Id
   * 
   * @param pProjectId
   *          the project id to set
   */
  void setForgeProjectId(final String pProjectId);

  /**
   * Returns the Super Administrator Login
   * 
   * @return Super Administrator Login
   */
  String getSuperAdministratorLogin();

  /**
   * Sets administrator login property
   * 
   * @param pSuperAdministratorLogin
   *          the login to set
   */
  void setSuperAdministratorLogin(final String pSuperAdministratorLogin);

  /**
   * This method returns the Forge Member role name
   * 
   * @return the Forge Member role name
   */
  String getForgeMemberRoleName();

  /**
   * Sets Forge Member role name property
   * 
   * @param pSuperAdministratorLogin
   *          the login to set
   */
  void setForgeMemberRoleName(final String pSuperAdministratorLogin);

  /**
   * This method returns the Forge Super Administrator role name
   * 
   * @return the Forge Super Administrator role name
   */
  String getForgeSuperAdministratorRoleName();

  /**
   * Sets the Forge Super Administrator role name property
   * 
   * @param pForgeSuperAdministratorRoleName
   *          the login to set
   */
  void setForgeSuperAdministratorRoleName(final String pForgeSuperAdministratorRoleName);

  /**
   * This method returns the Forge Administrator role name
   * 
   * @return the Forge Administrator role name
   */
  String getForgeAdministratorRoleName();

  /**
   * Sets Forge Administrator role name
   * 
   * @param pForgeAdministratorRoleName
   *          the role name to set
   */
  void setForgeAdministratorRoleName(final String pForgeAdministratorRoleName);

  /**
   * Returns true if referentiel project is setd
   * 
   * @return true if referentiel project is setd, false otherwise
   */
  boolean isReferentielCreated();

  /**
   * Sets value to verify if referentiel project is created
   * 
   * @param pReferenceCreated
   *          the value to set
   */
  void setReferentielCreated(final boolean pReferenceCreated);

  /**
   * Returns the referentiel project id
   * 
   * @return referentiel project projectId
   */
  String getReferentielProjectId();

  /**
   * Sets the referentiel project id
   * 
   * @param pReferenceProjectId
   *          the id to set
   */
  void setReferentielProjectId(final String pReferenceProjectId);

  /**
   * Returns the name of role member for referentiel project
   * 
   * @return name of role member for referentiel project
   */
  String getReferentielMemberRoleName();

  /**
   * Sets name of role member for referentiel project
   * 
   * @param pReferentielMemberRoleName
   *          the role name to set
   */
  void setReferentielMemberRoleName(final String pReferentielMemberRoleName);

  /**********************************************************************************
   * Server configuration
   *********************************************************************************/
  /**
   * Returns public URL used to contact NovaForge
   * 
   * @return public URL
   */
  URL getPublicUrl();

  /**
   * Returns default portal entry point, has to start with /
   * 
   * @return default portal entry point
   */
  String getPortalEntryPoint();

  /**
   * Returns local port of Karaf server
   * 
   * @return local port of Karaf server
   */
  String getLocalPort();

  /**
   * Returns local hostname of server
   * 
   * @return local hostname of server
   */
  String getLocalHostName();

  /**
   * Returns the CXF path : mustn't contain any /, used for example to expose webservice
   * 
   * @return the CXF path
   */
  String getCXFEndPoint();

  /**********************************************************************************
   * Security configuration
   *********************************************************************************/
  /**
   * Returns the URL to contact CAS serveur
   * 
   * @return cas URL
   */
  URL getCasUrl();

  /**
   * Returns if the user's login are generated or not
   * 
   * @return <code>true</code> if login is generated by system, <code>false</code> if login can be customized
   *         by user
   */
  boolean isUserLoginGenerated();

  /**
   * Sets if the user's login are generated or not
   * 
   * @param pUserLoginGenerated
   *          <code>true</code> if login has to be generated by system, <code>false</code> if login has to be
   *          customized by user
   */
  void setUserLoginGenerated(final boolean pUserLoginGenerated);

  /**
   * Returns life time in days of user's password
   * 
   * @return life time in days of user's password
   */
  int getPasswordLifeTime();

  /**
   * Sets life time <b>(in days)</b> of user's password.
   * 
   * @param pPasswordLifeTime
   *          life time of user's password
   */
  void setPasswordLifeTime(final int pPasswordLifeTime);

  /**
   * Returns modification time (in days) during which one user can still update his password
   * 
   * @return modification time (in days) during which one user can still update his password
   */
  int getPasswordModificationTime();

  /**
   * Sets modification time (in days) during which one user can still update his password
   * 
   * @param pPasswordModificationTime
   *          modification time of user's password
   */
  void setPasswordModificationTime(int pPasswordModificationTime);

  /**
   * Returns regular expression used to validate password
   * 
   * @return regular expression used to validate password
   */
  String getPasswordValidationRegex();

  /**
   * Sets the regular expression used to validate password.
   * <p>
   * The expression should be well escaped (double /)
   * </p>
   * 
   * @param pForgePasswordRegex
   *          the new regular expression
   */
  void setPasswordValidationRegex(String pForgePasswordRegex);

  /**********************************************************************************
   * SMTP configuration
   *********************************************************************************/
  /**
   * Returns smtp host used to send email
   * 
   * @return smtp host used to send email
   */
  String getSmtpHost();

  /**
   * Returns smtp port used to send email
   * 
   * @return smtp port used to send email
   */
  String getSmtpPort();

  /**
   * Returns smtp user name used to authenticate on smtp server
   * 
   * @return smtp user name used to authenticate on smtp server
   */
  String getSmtpUsername();

  /**
   * Returns smtp user password used to authenticate on smtp server
   * 
   * @return smtp user password used to authenticate on smtp server
   */
  String getSmtpPassword();

  /**
   * Returns email used as From when sending email
   * 
   * @return email used as From when sending email
   */
  String getSmtpFrom();

  /**
   * Returns the system administrator email
   * 
   * @return the email
   */
  String getSystemAdministratorEMail();

  /**********************************************************************************
   * Portal configuration
   *********************************************************************************/

  /**
   * Returns the portal configuration directory
   * 
   * @return portal configuration directory path as {@link String}
   */
  String getPortalConfDirectory();

  /**
   * Returns the portal footer with version number
   * 
   * @return the NovaForge Footer with version number
   */
  String getPortalFooter();

  /**
   * Returns the website url used in portal footer
   * 
   * @return website url used in portal footer
   */
  String getPortalFooterWebSite();

  /**
   * Returns default icon content used for home view, default project
   * 
   * @return default icon content
   */
  byte[] getDefaultIcon();

  /**
   * Returns max size of element uploaded
   * 
   * @return {@link long} representing max size of uploaded content
   */
  Long getUploadMaxSize();

  /**
   * Returns a list of forbidden logins
   * 
   * @return {@link List<String>} a list of forbidden logins
   */
  List<String> getForbiddenLogins();

}
