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
package org.novaforge.forge.core.configuration.internal.keys;

/**
 * This enum lists all property key used in properties file
 * 
 * @author Guillaume Lamirand
 */
public enum ForgeProperty
{
  PUBLIC_URL
  {
    @Override
    public String getKey()
    {
      return "publicUrl";
    }
  },
  PORTAL_ENTRY_POINT
  {
    @Override
    public String getKey()
    {
      return "portalEntryPoint";
    }
  },
  LOCAL_PORT
  {
    @Override
    public String getKey()
    {
      return "localPort";
    }
  },
  LOCAL_HOSTNAME
  {
    @Override
    public String getKey()
    {
      return "localHostName";
    }
  },
  CXF_END_POINT
  {
    @Override
    public String getKey()
    {
      return "cxfEndPoint";
    }
  },
  CAS_URL
  {
    @Override
    public String getKey()
    {
      return "casUrl";
    }
  },
  USER_LOGIN_GENERATED
  {
    @Override
    public String getKey()
    {
      return "userLoginGenerated";
    }
  },
  PASSWORD_LIFE_TIME
  {
    @Override
    public String getKey()
    {
      return "passwordLifeTime";
    }
  },
  PASSWORD_MODIFICATION_TIME
  {
    @Override
    public String getKey()
    {
      return "passwordModificationTime";
    }
  },
  PASSWORD_VALIDATION_REGEX
  {
    @Override
    public String getKey()
    {
      return "passwordValidationRegEx";
    }
  },
  SMTP_HOST
  {
    @Override
    public String getKey()
    {
      return "smtpHost";
    }
  },
  SMTP_PORT
  {
    @Override
    public String getKey()
    {
      return "smtpPort";
    }
  },
  SMTP_USERNAME
  {
    @Override
    public String getKey()
    {
      return "smtpUsername";
    }
  },
  SMTP_PASSWORD
  {
    @Override
    public String getKey()
    {
      return "smtpPassword";
    }
  },
  SMTP_FROM
  {
    @Override
    public String getKey()
    {
      return "smtpFrom";
    }
  },
  SYSTEM_ADMINISTRATOR_EMAIL
  {
    @Override
    public String getKey()
    {
      return "systemAdministratorEmail";
    }
  },
  PORTAL_CONF_DIRECTORY
  {
    @Override
    public String getKey()
    {
      return "portalConfDirectory";
    }
  },
  PORTAL_FOOTER
  {
    @Override
    public String getKey()
    {
      return "portalFooter";
    }
  },
  PORTAL_FOOTER_WEBSITE
  {
    @Override
    public String getKey()
    {
      return "portalFooterWebSite";
    }
  },
  DEFAULT_ICON
  {
    @Override
    public String getKey()
    {
      return "defaultIcon";
    }
  },
  UPLOAD_MAX_SIZE
  {
    @Override
    public String getKey()
    {
      return "uploadMaxSize";
    }
  },
  FORBIDDEN_LOGINS
  {
    @Override
    public String getKey()
    {
      return "forbiddenLogins";
    }
  };
  /**
   * Returns the property key used in configuration file
   * 
   * @return the property key used in configuration file
   */
  public abstract String getKey();
}
