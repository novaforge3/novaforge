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
package org.novaforge.forge.core.configuration.internal.services;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.dao.ForgeConfigurationDAO;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.internal.keys.ForgeCfg;
import org.novaforge.forge.core.configuration.internal.keys.ForgeProperty;
import org.novaforge.forge.core.configuration.model.ForgeConfiguration;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.configuration.services.properties.ForgePropertiesService;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation of {@link ForgeConfigurationService}
 *
 * @author Guillaume Lamirand
 */
public class ForgeConfigurationServiceImpl implements ForgeConfigurationService
{

  private static final Log LOGGER = LogFactory.getLog(ForgeConfigurationServiceImpl.class);
  /**
   * Service reference of {@link ForgeConfigurationDAO}
   */
  private ForgeConfigurationDAO  forgeConfigurationDAO;
  /**
   * Service reference of {@link ForgeCfgService}
   */
  private ForgeCfgService        forgeCfgService;
  /**********************************************************************************
   * Server configuration
   *********************************************************************************/
  /**
   * Service reference of {@link ForgePropertiesService}
   */
  private ForgePropertiesService forgePropertiesService;
  /**
   * Public URL used to contact NovaForge
   */
  private URL                    publicUrl;
  /**
   * Default portal entry point, has to start with /
   */
  private String                 portalEntryPoint;
  /**
   * Local port of Karaf server
   */
  private String                 localPort;
  /**
   * Local hostname of server
   */
  private String                 localHostName;

  /**********************************************************************************
   * Security configuration
   *********************************************************************************/
  /**
   * The CXF path : mustn't contain any /
   */
  private String  cxfEndPoint;
  /**
   * Contains the url to contact cas server. Ex https://localhost:8443/cas
   */
  private URL     casUrl;
  /**
   * Login policy
   */
  private boolean userLoginGenerated;
  /**
   * Password life time in days
   */
  private int     passwordLifeTime;
  /**
   * Password modification time in days
   */
  private int     passwordModificationTime;

  /**********************************************************************************
   * STMP configuration
   *********************************************************************************/
  /**
   * Regular expression used to validate password
   */
  private String       passwordValidationRegex;
  /**
   * Smtp host used to send email
   */
  private String       smtpHost;
  /**
   * Smtp port used to send email
   */
  private String       smtpPort;
  /**
   * Smtp user name used to authenticate on smtp server
   */
  private String       smtpUsername;
  /**
   * Smtp user password used to authenticate on smtp server
   */
  private String       smtpPassword;
  /**
   * Email used as From
   */
  private String       smtpFrom;
  /**********************************************************************************
   * Portal configuration
   *********************************************************************************/
  /**
   * System administrator email, can be the same as smtpFrom
   */
  private String       systemAdministratorEmail;
  /**
   * Contains the portal configuration directory path
   */
  private String       portalConfDirectory;
  /**
   * Contains the portal footer information
   */
  private String       portalFooter;
  /**
   * Contains the website url used in footer
   */
  private String       portalFooterWebSite;
  /**
   * Contains the icon bytes
   */
  private byte[]       defaultIcon;
  /**
   * Contains the max size for upload content.
   */
  private Long         uploadMaxSize;
  private List<String> forbiddenLogins;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForgeConfDirectory()
  {
    return forgeCfgService.getForgeConfDirectory();
  }

  /**********************************************************************************
   * Default configuration
   *********************************************************************************/

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForgeProjectId()
  {
    return getValue(ForgeCfg.FORGE_PROJECT_ID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForgeProjectId(final String pProjectId)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO
                                                         .newForgeConfiguration(ForgeCfg.FORGE_PROJECT_ID.getKey(),
                                                                                pProjectId);
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSuperAdministratorLogin()
  {
    return getValue(ForgeCfg.FORGE_SUPERADMINISTRATOR_LOGIN);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSuperAdministratorLogin(final String pSuperAdministratorLogin)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO
                                                         .newForgeConfiguration(ForgeCfg.FORGE_SUPERADMINISTRATOR_LOGIN
                                                                                    .getKey(),
                                                                                pSuperAdministratorLogin);
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForgeMemberRoleName()
  {
    return getValue(ForgeCfg.FORGE_MEMBER_ROLE_NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForgeMemberRoleName(final String pForgeMemberRoleName)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO
                                                         .newForgeConfiguration(ForgeCfg.FORGE_MEMBER_ROLE_NAME
                                                                                    .getKey(), pForgeMemberRoleName);
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForgeSuperAdministratorRoleName()
  {
    return getValue(ForgeCfg.FORGE_SUPERADMINISTRATOR_ROLE_NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForgeSuperAdministratorRoleName(final String pForgeSuperAdministratorRoleName)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO
                                                         .newForgeConfiguration(ForgeCfg.FORGE_SUPERADMINISTRATOR_ROLE_NAME
                                                                                    .getKey(),
                                                                                pForgeSuperAdministratorRoleName);
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForgeAdministratorRoleName()
  {
    return getValue(ForgeCfg.FORGE_ADMINISTRATOR_ROLE_NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForgeAdministratorRoleName(final String pForgeAdministratorRoleName)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO
                                                         .newForgeConfiguration(ForgeCfg.FORGE_ADMINISTRATOR_ROLE_NAME
                                                                                    .getKey(),
                                                                                pForgeAdministratorRoleName);
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReferentielCreated()
  {
    final String isRerentielCreated = getValue(ForgeCfg.REFERENTIEL_CREATED);
    return Boolean.valueOf(isRerentielCreated);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setReferentielCreated(final boolean pReferenceCreated)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO
                                                         .newForgeConfiguration(ForgeCfg.REFERENTIEL_CREATED.getKey(),
                                                                                Boolean.toString(pReferenceCreated));
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getReferentielProjectId()
  {
    return getValue(ForgeCfg.REFERENTIEL_PROJECT_ID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setReferentielProjectId(final String pReferenceProjectId)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO
                                                         .newForgeConfiguration(ForgeCfg.REFERENTIEL_PROJECT_ID
                                                                                    .getKey(), pReferenceProjectId);
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getReferentielMemberRoleName()
  {
    return getValue(ForgeCfg.REFERENTIEL_MEMBER_ROLE_NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setReferentielMemberRoleName(final String pReferentielMemberRoleName)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO.newForgeConfiguration(ForgeCfg.REFERENTIEL_MEMBER_ROLE_NAME
                                                                                                     .getKey(),
                                                                                                 pReferentielMemberRoleName);
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getPublicUrl()
  {
    if ((publicUrl == null) || (checkReloadingRequired()))
    {
      final String publicUrlValue = forgePropertiesService.getPropertyAsString(ForgeProperty.PUBLIC_URL
          .getKey());
      buildPublicUrl(publicUrlValue);
    }
    return publicUrl;
  }

  /**
   * Util method uses to clear all cache field if reloading is required
   *
   * @return true if reloading is required
   */
  private boolean checkReloadingRequired()
  {
    final boolean reloadingRequired = forgePropertiesService.reloadingRequired();
    if (reloadingRequired)
    {
      publicUrl = null;
      portalEntryPoint = null;
      localPort = null;
      localHostName = null;
      cxfEndPoint = null;
      casUrl = null;
      userLoginGenerated = false;
      passwordLifeTime = 0;
      passwordModificationTime = 0;
      passwordValidationRegex = null;
      smtpHost = null;
      smtpPort = null;
      smtpUsername = null;
      smtpPassword = null;
      smtpFrom = null;
      systemAdministratorEmail = null;
      portalConfDirectory = null;
      portalFooter = null;
      portalFooterWebSite = null;
      defaultIcon = null;
      uploadMaxSize = null;
      forbiddenLogins = null;
    }
    return reloadingRequired;
  }

  /**********************************************************************************
   * Server configuration
   *********************************************************************************/

  /**
   * Build the public access url
   *
   * @param pPublicUrl
   *          represents the url to acces to NovaForge
   */
  private void buildPublicUrl(final String pPublicUrl)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update publicUrl from %s to %s", publicUrl, pPublicUrl));
    }
    try
    {
      publicUrl = new URL(pPublicUrl);
    }
    catch (final MalformedURLException e)
    {
      LOGGER.warn(String.format("Unable to build an URL from the string given [new_url=%s]", pPublicUrl), e);

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPortalEntryPoint()
  {
    if ((Strings.isNullOrEmpty(portalEntryPoint)) || (checkReloadingRequired()))
    {
      final String portalEntryPointValue = forgePropertiesService
          .getPropertyAsString(ForgeProperty.PORTAL_ENTRY_POINT.getKey());
      buildPortalEntryPoint(portalEntryPointValue);
    }
    return portalEntryPoint;
  }

  /**
   * build the default portal uri
   *
   * @param pPortalEntryPoint
   *          represents the portal endpoint to access portal
   */
  private void buildPortalEntryPoint(final String pPortalEntryPoint)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String
          .format("Update portalEntryPoint from %s to %s", portalEntryPoint, pPortalEntryPoint));
    }
    if (pPortalEntryPoint.startsWith("/"))
    {
      portalEntryPoint = pPortalEntryPoint;
    }
    else
    {
      portalEntryPoint = "/" + pPortalEntryPoint;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLocalPort()
  {
    if ((Strings.isNullOrEmpty(localPort)) || (checkReloadingRequired()))
    {
      final String localPortValue = forgePropertiesService.getPropertyAsString(ForgeProperty.LOCAL_PORT
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update localPort from %s to %s", localPort, localPortValue));
      }
      localPort = localPortValue;
    }
    return localPort;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLocalHostName()
  {
    if ((Strings.isNullOrEmpty(localHostName)) || (checkReloadingRequired()))
    {
      final String localHostNameValue = forgePropertiesService
          .getPropertyAsString(ForgeProperty.LOCAL_HOSTNAME.getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update localHostName from %s to %s", localHostName, localHostNameValue));
      }
      localHostName = localHostNameValue;
    }
    return localHostName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCXFEndPoint()
  {
    if ((Strings.isNullOrEmpty(cxfEndPoint)) || (checkReloadingRequired()))
    {
      final String cxfEndPointValue = forgePropertiesService.getPropertyAsString(ForgeProperty.CXF_END_POINT
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update cxfEndPoint from %s to %s", cxfEndPoint, cxfEndPointValue));
      }
      cxfEndPoint = cxfEndPointValue;
    }
    return cxfEndPoint;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getCasUrl()
  {
    if ((casUrl == null) || (checkReloadingRequired()))
    {
      final String casUrlValue = forgePropertiesService.getPropertyAsString(ForgeProperty.CAS_URL.getKey());
      buildCasUrl(casUrlValue);
    }
    return casUrl;
  }

  /**********************************************************************************
   * Security configuration
   *********************************************************************************/

  /**
   * Set the cas prefix url.
   *
   * @param pCasUrl
   *          the casUrl to set
   */
  private void buildCasUrl(final String pCasUrl)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update casUrl from %s to %s", casUrl, pCasUrl));
    }
    try
    {
      casUrl = new URL(pCasUrl);
    }
    catch (final MalformedURLException e)
    {
      LOGGER.warn(String.format("Unable to build an URL from the string given [new_url=%s]", pCasUrl), e);

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUserLoginGenerated()
  {
    if ((!userLoginGenerated) || (checkReloadingRequired()))
    {
      final boolean userLoginGeneratedValue = forgePropertiesService
          .getPropertyAsBoolean(ForgeProperty.USER_LOGIN_GENERATED.getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update userLoginGenerated from %s to %s", userLoginGenerated,
            userLoginGeneratedValue));
      }
      userLoginGenerated = userLoginGeneratedValue;
    }
    return userLoginGenerated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserLoginGenerated(final boolean pUserLoginGenerated)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update userLoginGenerated from %s to %s", userLoginGenerated,
          pUserLoginGenerated));
    }
    // Set current value
    userLoginGenerated = pUserLoginGenerated;

    // Set property value
    forgePropertiesService.setProperty(ForgeProperty.USER_LOGIN_GENERATED.getKey(), pUserLoginGenerated);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPasswordLifeTime()
  {
    if ((passwordLifeTime == 0) || (checkReloadingRequired()))
    {
      final int passwordLifeTimeValue = forgePropertiesService
          .getPropertyAsInt(ForgeProperty.PASSWORD_LIFE_TIME.getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update passwordLifeTime from %s to %s", passwordLifeTime,
            passwordLifeTimeValue));
      }
      passwordLifeTime = passwordLifeTimeValue;
    }
    return passwordLifeTime;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPasswordLifeTime(final int pPasswordLifeTime)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String
          .format("Update passwordLifeTime from %s to %s", passwordLifeTime, pPasswordLifeTime));
    }
    // Set current value
    passwordLifeTime = pPasswordLifeTime;

    // Set property value
    forgePropertiesService.setProperty(ForgeProperty.PASSWORD_LIFE_TIME.getKey(), passwordLifeTime);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPasswordModificationTime()
  {
    if ((passwordModificationTime == 0) || (checkReloadingRequired()))
    {
      final int passwordModificationTimeValue = forgePropertiesService
          .getPropertyAsInt(ForgeProperty.PASSWORD_MODIFICATION_TIME.getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update passwordModificationTimeValue from %s to %s",
            passwordModificationTimeValue, passwordModificationTimeValue));
      }
      passwordModificationTime = passwordModificationTimeValue;
    }
    return passwordModificationTime;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPasswordModificationTime(final int pPasswordModificationTime)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update passwordModificationTime from %s to %s", passwordModificationTime,
          pPasswordModificationTime));
    }
    // Set current value
    passwordModificationTime = pPasswordModificationTime;

    // Set property value
    forgePropertiesService.setProperty(ForgeProperty.PASSWORD_MODIFICATION_TIME.getKey(),
        passwordModificationTime);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPasswordValidationRegex()
  {
    if ((Strings.isNullOrEmpty(passwordValidationRegex)) || (checkReloadingRequired()))
    {
      final String passwordRegexValue = forgePropertiesService
          .getPropertyAsString(ForgeProperty.PASSWORD_VALIDATION_REGEX.getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update passwordValidationRegex from %s to %s", passwordValidationRegex,
            passwordRegexValue));
      }
      passwordValidationRegex = passwordRegexValue;
    }
    return passwordValidationRegex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPasswordValidationRegex(final String pForgePasswordRegex)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update passwordValidationRegex from %s to %s", passwordValidationRegex,
          pForgePasswordRegex));
    }

    // Reset current value
    passwordValidationRegex = pForgePasswordRegex;

    // Set property value
    forgePropertiesService.setProperty(ForgeProperty.PASSWORD_VALIDATION_REGEX.getKey(),
        escapeRegEx(passwordValidationRegex));

  }

  private String escapeRegEx(final String pRegEx)
  {
    // Escape , because it is used as separator in properties file
    return pRegEx.replace(",", "\\,");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSmtpHost()
  {
    if ((Strings.isNullOrEmpty(smtpHost)) || (checkReloadingRequired()))
    {
      final String smtpHostValue = forgePropertiesService.getPropertyAsString(ForgeProperty.SMTP_HOST
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update smtpHost from %s to %s", smtpHost, smtpHostValue));
      }
      smtpHost = smtpHostValue;
    }
    return smtpHost;
  }

  /**********************************************************************************
   * STMP configuration
   *********************************************************************************/

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSmtpPort()
  {
    if ((Strings.isNullOrEmpty(smtpPort)) || (checkReloadingRequired()))
    {
      final String smtpPortValue = forgePropertiesService.getPropertyAsString(ForgeProperty.SMTP_PORT
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update smtpPort from %s to %s", smtpPort, smtpPortValue));
      }
      smtpPort = smtpPortValue;
    }
    return smtpPort;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSmtpUsername()
  {
    if ((Strings.isNullOrEmpty(smtpUsername)) || (checkReloadingRequired()))
    {
      final String smtpUsernameValue = forgePropertiesService.getPropertyAsString(ForgeProperty.SMTP_USERNAME
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update smtpUsername from %s to %s", smtpUsername, smtpUsernameValue));
      }
      smtpUsername = smtpUsernameValue;
    }
    return smtpUsername;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSmtpPassword()
  {
    if ((Strings.isNullOrEmpty(smtpPassword)) || (checkReloadingRequired()))
    {
      final String smtpPasswordValue = forgePropertiesService.getPropertyAsString(ForgeProperty.SMTP_PASSWORD
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update smtpPassword from %s to %s", smtpPassword, smtpPasswordValue));
      }
      smtpPassword = smtpPasswordValue;
    }
    return smtpPassword;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSmtpFrom()
  {
    if ((Strings.isNullOrEmpty(smtpFrom)) || (checkReloadingRequired()))
    {
      final String smtpFromValue = forgePropertiesService.getPropertyAsString(ForgeProperty.SMTP_FROM
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update smtpFrom from %s to %s", smtpFrom, smtpFromValue));
      }
      smtpFrom = smtpFromValue;
    }
    return smtpFrom;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSystemAdministratorEMail()
  {
    if ((Strings.isNullOrEmpty(systemAdministratorEmail)) || (checkReloadingRequired()))
    {
      final String systemAdministratorEmailValue = forgePropertiesService
          .getPropertyAsString(ForgeProperty.SYSTEM_ADMINISTRATOR_EMAIL.getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update systemAdministratorEmail from %s to %s", systemAdministratorEmail,
            systemAdministratorEmailValue));
      }
      systemAdministratorEmail = systemAdministratorEmailValue;
    }
    return systemAdministratorEmail;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPortalConfDirectory()
  {
    if ((Strings.isNullOrEmpty(portalConfDirectory)) || (checkReloadingRequired()))
    {
      final String newValue = forgePropertiesService.getPropertyAsString(ForgeProperty.PORTAL_CONF_DIRECTORY
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER
            .debug(String.format("Update portalConfDirectory from %s to %s", portalConfDirectory, newValue));
      }
      portalConfDirectory = newValue;
    }
    return portalConfDirectory;
  }

  /**********************************************************************************
   * Portal configuration
   *********************************************************************************/

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPortalFooter()
  {
    if ((Strings.isNullOrEmpty(portalFooter)) || (checkReloadingRequired()))
    {
      final String newValue = forgePropertiesService
          .getPropertyAsString(ForgeProperty.PORTAL_FOOTER.getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update portalFooter from %s to %s", portalFooter, newValue));
      }
      portalFooter = newValue;
    }
    return portalFooter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPortalFooterWebSite()
  {
    if ((Strings.isNullOrEmpty(portalFooterWebSite)) || (checkReloadingRequired()))
    {
      final String newValue = forgePropertiesService.getPropertyAsString(ForgeProperty.PORTAL_FOOTER_WEBSITE
          .getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER
            .debug(String.format("Update portalFooterWebSite from %s to %s", portalFooterWebSite, newValue));
      }
      portalFooterWebSite = newValue;
    }
    return portalFooterWebSite;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getDefaultIcon()
  {
    if ((defaultIcon == null) || (checkReloadingRequired()))
    {
      final String newValue = forgePropertiesService.getPropertyAsString(ForgeProperty.DEFAULT_ICON.getKey());
      buildDefaultIcon(newValue);
    }
    return defaultIcon;
  }

  /**
   * Build the default icon content from new path
   *
   * @param pDefaultIcon
   *          represents the path to the default home logo
   */
  private void buildDefaultIcon(final String pDefaultIcon)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update defaultIcon to %s", pDefaultIcon));
    }
    try
    {
      final FileInputStream resource = new FileInputStream(new File(pDefaultIcon));

      defaultIcon = IOUtils.toByteArray(resource);
    }
    catch (final IOException e)
    {
      LOGGER.error("Unable to build a byte array from inputstream given", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getUploadMaxSize()
  {
    if ((uploadMaxSize == null) || (checkReloadingRequired()))
    {
      final long newValue = forgePropertiesService.getPropertyAsLong(ForgeProperty.UPLOAD_MAX_SIZE.getKey());
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Update uploadMaxSize from %s to %s", uploadMaxSize, newValue));
      }
      uploadMaxSize = newValue;
    }
    return uploadMaxSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getForbiddenLogins()
  {
    if ((forbiddenLogins == null) || (forbiddenLogins.isEmpty()) || (checkReloadingRequired()))
    {
      final List<String> forbiddenLoginsValue = convertToStrList(forgePropertiesService
                                                                     .getList(ForgeProperty.FORBIDDEN_LOGINS.getKey()));
      if (LOGGER.isDebugEnabled())
      {
        if (forbiddenLogins != null && !forbiddenLogins.isEmpty())
        {
          LOGGER.debug(String.format("Update forbiddenLogins from %s to %s", forbiddenLogins.toString(),
                                     forbiddenLoginsValue.toString()));
        }
        else
        {
          LOGGER.debug(String.format("Update forbiddenLogins from null to %s", forbiddenLoginsValue.toString()));
        }
      }
      forbiddenLogins = forbiddenLoginsValue;
    }
    return forbiddenLogins;
  }

  private List<String> convertToStrList(final List<Object> pList)
  {
    final List<String> list = new ArrayList<String>();
    for (final Object object : pList)
    {
      list.add(object != null ? object.toString() : null);
    }
    return list;
  }

  /**
   * Returns from persistence value of the key given
   *
   * @param pForgeCfg
   *     the key
   *
   * @return the value found
   *
   * @throws ForgeConfigurationException
   *     thrown if key is not found
   */
  private String getValue(final ForgeCfg pForgeCfg)
  {
    String value = pForgeCfg.getDefaultValue();
    try
    {
      final ForgeConfiguration forgeConfiguration = forgeConfigurationDAO.findByKey(pForgeCfg.getKey());
      value = forgeConfiguration.getValue();
    }
    catch (final NoResultException e)
    {
      LOGGER.warn(String.format("Unable to find property with [key=%s] so default value will be used.", pForgeCfg));
    }
    catch (final Exception e)
    {
      LOGGER.error(String.format("An error occurred during finding property with [key=%s]", pForgeCfg), e);
    }
    return value;
  }

  /**
   * Bind method used by the container to inject {@link ForgeConfigurationDAO} service.
   *
   * @param pForgeConfigurationDAO
   *          the forgeConfigurationDAO to set
   */
  public void setForgeConfigurationDAO(final ForgeConfigurationDAO pForgeConfigurationDAO)
  {
    forgeConfigurationDAO = pForgeConfigurationDAO;
  }

  /**
   * Bind method used by the container to inject {@link ForgeCfgService} service.
   *
   * @param pForgeCfgService
   *          the forgeCfgService to set
   */
  public void setForgeCfgService(final ForgeCfgService pForgeCfgService)
  {
    forgeCfgService = pForgeCfgService;
  }

  /**
   * Bind method used by the container to inject {@link ForgePropertiesService} service.
   *
   * @param pForgePropertiesService
   *          the forgePropertiesService to set
   */
  public void setForgePropertiesService(final ForgePropertiesService pForgePropertiesService)
  {
    forgePropertiesService = pForgePropertiesService;
  }

}
