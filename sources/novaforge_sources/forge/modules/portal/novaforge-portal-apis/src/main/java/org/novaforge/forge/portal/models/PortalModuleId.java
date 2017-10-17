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
package org.novaforge.forge.portal.models;

/**
 * This enum describes the application id shared between portal ui and
 * portal component
 *
 * @author Guillaume Lamirand
 */
public enum PortalModuleId
{
  /**
   * Describes the internal application for unavailable
   */
  UNAVAILABLE
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "unavailable";
        }

      },
  /**
   * Describes the internal application for recovery password
   */
  RECOVERY_PWD
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "recoverypwd";
        }
      },
  /**
   * Describes the internal application for change password
   */
  CHANGE_PWD
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "changepwd";
        }
      },
  /**
   * Describes the internal application for logout
   */
  LOGOUT
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "logout";
        }
      },
  /**
   * Describes the portal header module
   */
  HEADER
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "header";
        }
      },
  /**
   * Describes the portal footer module
   */
  FOOTER
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "footer";
        }
      },
  /**
   * Describes the portal public module
   */
  PUBLIC
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "public";
        }
      },
  /**
   * Describes the portal private module
   */
  PRIVATE
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "private";
        }
      },
  /**
   * Describes the internal application for dash board
   */
  DASHBOARD
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "dashboard";
        }
      },
  /**
   * Describes the internal application for create project
   */
  CREATE_PROJECT
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "createproject";
        }
      },
  /**
   * Describes the internal application for update project
   */
  UPDATE_PROJECT
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "updateproject";
        }
      },
  /**
   * Describes the internal application for applications managements
   */
  APPLICATIONS
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "applications";
        }
      },
  /**
   * Describes the internal application for memberships managements
   */
  MEMBERSHIPS
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "memberships";
        }
      },
  /**
   * Describes the internal application for mailing lists managements
   */
  MAILING
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "mailing";
        }
      },
  /**
   * Describes the internel application for public project application
   */
  PUBLIC_PROJECT
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "publicproject";
        }
      },
  /**
   * Describes the internel application for plugins management application
   */
  PLUGINS_MANAGEMENT
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "pluginsmanagement";
        }
      },
  /**
   * Describes the first page shown
   */
  FIRST_PAGE
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "firstpage";
        }
      },
  /**
   * Describes the internal application for news management
   */
  ARTICLE
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "article";
        }
      },
  /**
   * Describes the internel application for user management admin application
   */
  USERMANAGEMENT_ADMIN
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "usermanagementadmin";
        }
      },
  /**
   * Describes the internel application for user management private application
   */
  USERMANAGEMENT_PRIVATE
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "usermanagementprivate";
        }
      },
  /**
   * Describes the internel application for user management public application
   */
  USERMANAGEMENT_PUBLIC
      {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId()
        {
          return "usermanagementpublic";
        }
      },
  /**
   * Describes the internel application for projects administration application
   */
  ADMIN_PROJECTS
      {
        @Override
        public String getId()
        {
          return "adminprojects";
        }
      },
  /**
   * Describes the internel application for requirement management application
   */
  REQUIREMENTS_MANAGEMENT
      {
        @Override
        public String getId()
        {
          return "requirements-management";
        }
      },
  /**
   * Describes the internel application for devops management application
   */
  DEVOPS_MANAGEMENT
      {
        @Override
        public String getId()
        {
          return "novadeploy";
        }
      };

  /**
   * Check if the given id is an known internal application.
   *
   * @param pId
   *     the id to check
   *
   * @return true or false
   */
  public static boolean isExist(final String pId)
  {
    boolean                returnValue = false;
    final PortalModuleId[] values      = PortalModuleId.values();
    if (pId != null)
    {
      for (final PortalModuleId intApp : values)
      {
        if (intApp.getId().equals(pId))
        {
          returnValue = true;
          break;
        }
      }
    }
    return returnValue;

  }

  /**
   * Get the module id.
   *
   * @return id as a string object
   */
  public abstract String getId();

  /**
   * Get a {@link PortalModuleId} from the id given.
   *
   * @param pId
   *     the original application id
   *
   * @return {@link PortalModuleId} found or <code>null</code>
   */
  public static PortalModuleId getFromId(final String pId)
  {
    PortalModuleId returnModuleId = null;

    if ((pId != null) && (!"".equals(pId)))
    {
      final PortalModuleId[] values = PortalModuleId.values();
      for (final PortalModuleId privateApplication : values)
      {
        if (privateApplication.getId().equals(pId))
        {
          returnModuleId = privateApplication;
          break;
        }
      }
    }
    return returnModuleId;

  }
}
