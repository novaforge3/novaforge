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
package org.novaforge.forge.ui.historization.shared;

import java.io.Serializable;

public class FunctionalLogDTO implements Serializable
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private String            logDate;
  private String            logLevel;
  private String            logType;
  private String            logUser;
  private String            logMessage;
  private String            logLevelColor;

  public String getLogDate()
  {
    return logDate;
  }

  public void setLogDate(final String logDate)
  {
    this.logDate = logDate;
  }

  public String getLogLevel()
  {
    return logLevel;
  }

  public void setLogLevel(final String logLevel)
  {
    this.logLevel = logLevel;
  }

  public String getLogUser()
  {
    return logUser;
  }

  public void setLogUser(final String logUser)
  {
    this.logUser = logUser;
  }

  public String getLogType()
  {
    return logType;
  }

  public void setLogType(final String logType)
  {
    this.logType = logType;
  }

  public String getLogMessage()
  {
    return logMessage;
  }

  public void setLogMessage(final String logMessage)
  {
    this.logMessage = logMessage;
  }

  public String getLogLevelColor()
  {
    return logLevelColor;
  }

  public void setLogLevelColor(final String logLevelColor)
  {
    this.logLevelColor = logLevelColor;
  }
}
