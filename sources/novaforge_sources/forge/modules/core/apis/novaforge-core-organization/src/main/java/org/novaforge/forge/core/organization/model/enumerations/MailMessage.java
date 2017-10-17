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
package org.novaforge.forge.core.organization.model.enumerations;

/**
 * The followings define all key used for mail i18n
 * 
 * @author Guillaume Lamirand
 */
public enum MailMessage
{
  /**
   * Project created subject
   */
  PROJECT_CREATED__SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "projectCreatedSubject";
    }
  },

  /**
   * Project created body
   */
  PROJECT_CREATED__BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "projectCreatedBody";
    }
  },

  /**
   * Project to validate subject
   */
  PROJECT_TOVALIDATE__SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "projectToValidateSubject";
    }
  },

  /**
   * Project to validate body
   */
  PROJECT_TOVALIDATE_BODY
  {
    @Override
    public String getKey()
    {
      return "projectToValidateBody";
    }
  },

  /**
   * Project valid subject
   */
  PROJECT_VALID_SUBJECT
  {
    @Override
    public String getKey()
    {
      return "projectValidSubject";
    }
  },

  /**
   * Project valid body
   */
  PROJECT_VALID_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "projectValidBody";
    }
  },

  /**
   * Template error subject
   */
  TEMPLATE_ERROR_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "templateErrorSubject";
    }
  },

  /**
   * Template error body
   */
  TEMPLATE_ERROR_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "templateErrorBody";
    }
  },

  /**
   * Password recovery subject
   */
  PASSWORD_RECOVERY_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "passwordRecoverySubject";
    }
  },

  /**
   * Password recovery body
   */
  PASSWORD_RECOVERY_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "passwordRecoveryBody";
    }
  },

  /**
   * Membership subject
   */
  MEMBERSHIP_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "membershipSubject";
    }
  },

  /**
   * Add membership body
   */
  ADD_MEMBERSHIP_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "addMembershipBody";
    }
  },

  /**
   * Update membership body
   */
  UPDATE_MEMBERSHIP_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "updateMembershipBody";
    }
  },

  /**
   * Remove membership body
   */
  REMOVE_MEMBERSHIP_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "removeMembershipBody";
    }
  },

  /**
   * Project validation rejected subject
   */
  PROJECT_REJECT_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "projectRejectSubject";
    }
  },

  /**
   * Project validation rejected body
   */
  PROJECT_REJECT_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "projectRejectBody";
    }
  },

  /**
   * Memebership request created body
   */
  MEMBERSHIP_REQUEST_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "membershipRequestBody";
    }
  },

  /**
   * Memberhsip request accepted body
   */
  VALIDATE_MEMBERSHIP_REQUEST_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "validateMembershipRequestBody";
    }
  },

  /**
   * Memberhsip request rejected body
   */
  INVALIDATE_MEMBERSHIP_REQUEST_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "invalidateMembershipRequestBody";
    }
  },

  /**
   * Memberhsip request subject
   */
  MEMBERSHIP_REQUEST_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "membershipRequestSubject";
    }
  },

  /**
   * No more memberhsip on project subject
   */
  NO_MORE_MEMBERSHIPS_LABEL
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "noMoreMembershipsLabel";
    }
  },
  /**
   * New account subject
   */
  NEW_USER_ACCOUNT_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "newUserAccountSubject";
    }
  },
  /**
   * New account body
   */
  NEW_USER_ACCOUNT_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "newUserAccountBody";
    }
  },
  /**
   * Life cycle change subject
   */
  LIFE_CYCLE_CHANGE_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "lifeCycleChangeSubject";
    }
  },
  /**
   * Life cycle change body
   */
  LIFE_CYCLE_CHANGE_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "lifeCycleChangeBody";
    }
  },
  /**
   * Request stop subject
   */
  REQUEST_STOP_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "requestStopSubject";
    }
  },
  /**
   * Request stop body
   */
  REQUEST_STOP_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "requestStopBody";
    }
  },
  /**
   * Request uninstall subject
   */
  REQUEST_UNINSTALL_SUBJECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "requestUninstallSubject";
    }
  },
  /**
   * Request uninstall body
   */
  REQUEST_UNINSTALL_BODY
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "requestUninstallBody";
    }
  },

  REQUEST_APPLICATION_SUBJECT
  {
    @Override
    public String getKey()
    {
      return "requestApplicationSubject";
    }
  },

  REQUEST_APPLICATION_BODY
  {
    @Override
    public String getKey()
    {
      return "requestApplicationBody";
    }
  },

  NEW_EMAIL_ADMIN_NOTIFICATION_SUBJECT
  {
    @Override
    public String getKey()
    {
      return "newEmailAdminNotificationSubject";
    }
  },

  NEW_EMAIL_ADMIN_NOTIFICATION_BODY
  {
    @Override
    public String getKey()
    {
      return "newEmailAdminNotificationBody";
    }
  };

  /**
   * @return the key used on resource bundle
   */
  public abstract String getKey();
}
