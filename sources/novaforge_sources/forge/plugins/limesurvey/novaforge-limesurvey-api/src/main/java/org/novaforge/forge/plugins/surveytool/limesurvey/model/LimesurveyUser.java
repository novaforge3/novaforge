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
package org.novaforge.forge.plugins.surveytool.limesurvey.model;

/**
 * @author vvigo
 */
public interface LimesurveyUser
{
  /**
   * Get the LimeSurveyUser username
   * 
   * @return the username
   */
  String getUserName();

  /**
   * Set the LimeSurveyUser username
   * 
   * @param pUserName
   *          the username to set
   */
  void setUserName(String pUserName);

  /**
   * Get the LimeSurveyUser fullname
   * 
   * @return the fullname
   */
  String getFullName();

  /**
   * Set the LimeSurveyUser fullname
   * 
   * @param pFullName
   *          the fullname to set
   */
  void setFullName(final String pFullName);

  /**
   * Get the LimeSurveyUser email
   *
   * @return the email
   */
  String getEmail();

  /**
   * Set the LimeSurveyUser email
   *
   * @param pEmail
   *          the email to set
   */
  void setEmail(String pEmail);

  /**
   * Get the LimeSurveyUser password
   * 
   * @return the password
   */
  String getPassword();

  /**
   * Set the LimeSurveyUser password
   * 
   * @param pPassword
   *          the password to set
   */
  void setPassword(final String pPassword);

  /**
   * Get the LimeSurveyUser language
   *
   * @return the language
   */
  String getLanguage();

  /**
   * Set the LimeSurveyUser language
   *
   * @param langue
   *          the language to set
   */
  void setLanguage(final String pLanguage);

}
