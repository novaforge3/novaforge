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
package org.novaforge.forge.commands.stats.csv;

import java.util.Date;

/**
 * @author Guillaume Lamirand
 */
public class CsvProject
{
  private final String name;
  private final Date   created;
  private final String contact;
  private final int    users;
  private final int    extUsers;

  // The following has to be updated depending on Application list
  private String       alfrescoUsers;
  private String       alfrescoExtUsers;
  private String       mantisUsers;
  private String       mantisExtUsers;
  private String       jiraUsers;
  private String       jiraExtUsers;
  private String       svnUsers;
  private String       svnExtUsers;
  private String       testlinkUsers;
  private String       testlinkExtUsers;

  /**
   * @param pName
   * @param pCreated
   * @param pContact
   * @param pUsers
   * @param pExtUsers
   */
  public CsvProject(final String pName, final Date pCreated, final String pContact, final int pUsers,
      final int pExtUsers)
  {
    super();
    name = pName;
    created = pCreated;
    contact = pContact;
    users = pUsers;
    extUsers = pExtUsers;
  }

  /**
   * @return the alfrescoUsers
   */
  public String getAlfrescoUsers()
  {
    return alfrescoUsers;
  }

  /**
   * @param pAlfrescoUsers
   *          the alfrescoUsers to set
   */
  public void setAlfrescoUsers(final String pAlfrescoUsers)
  {
    alfrescoUsers = pAlfrescoUsers;
  }

  /**
   * @return the alfrescoExtUsers
   */
  public String getAlfrescoExtUsers()
  {
    return alfrescoExtUsers;
  }

  /**
   * @param pAlfrescoExtUsers
   *          the alfrescoExtUsers to set
   */
  public void setAlfrescoExtUsers(final String pAlfrescoExtUsers)
  {
    alfrescoExtUsers = pAlfrescoExtUsers;
  }

  /**
   * @return the mantisUsers
   */
  public String getMantisUsers()
  {
    return mantisUsers;
  }

  /**
   * @param pMantisUsers
   *          the mantisUsers to set
   */
  public void setMantisUsers(final String pMantisUsers)
  {
    mantisUsers = pMantisUsers;
  }

  /**
   * @return the mantisExtUsers
   */
  public String getMantisExtUsers()
  {
    return mantisExtUsers;
  }

  /**
   * @param pMantisExtUsers
   *          the mantisExtUsers to set
   */
  public void setMantisExtUsers(final String pMantisExtUsers)
  {
    mantisExtUsers = pMantisExtUsers;
  }

  /**
   * @return the jiraUsers
   */
  public String getJiraUsers()
  {
    return jiraUsers;
  }

  /**
   * @param pJiraUsers
   *          the jiraUsers to set
   */
  public void setJiraUsers(final String pJiraUsers)
  {
    jiraUsers = pJiraUsers;
  }

  /**
   * @return the jiraExtUsers
   */
  public String getJiraExtUsers()
  {
    return jiraExtUsers;
  }

  /**
   * @param pJiraExtUsers
   *          the jiraExtUsers to set
   */
  public void setJiraExtUsers(final String pJiraExtUsers)
  {
    jiraExtUsers = pJiraExtUsers;
  }

  /**
   * @return the svnUsers
   */
  public String getSvnUsers()
  {
    return svnUsers;
  }

  /**
   * @param pSvnUsers
   *          the svnUsers to set
   */
  public void setSvnUsers(final String pSvnUsers)
  {
    svnUsers = pSvnUsers;
  }

  /**
   * @return the svnExtUsers
   */
  public String getSvnExtUsers()
  {
    return svnExtUsers;
  }

  /**
   * @param pSvnExtUsers
   *          the svnExtUsers to set
   */
  public void setSvnExtUsers(final String pSvnExtUsers)
  {
    svnExtUsers = pSvnExtUsers;
  }

  /**
   * @return the testlinkUsers
   */
  public String getTestlinkUsers()
  {
    return testlinkUsers;
  }

  /**
   * @param pTestlinkUsers
   *          the testlinkUsers to set
   */
  public void setTestlinkUsers(final String pTestlinkUsers)
  {
    testlinkUsers = pTestlinkUsers;
  }

  /**
   * @return the testlinkExtUsers
   */
  public String getTestlinkExtUsers()
  {
    return testlinkExtUsers;
  }

  /**
   * @param pTestlinkExtUsers
   *          the testlinkExtUsers to set
   */
  public void setTestlinkExtUsers(final String pTestlinkExtUsers)
  {
    testlinkExtUsers = pTestlinkExtUsers;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @return the created
   */
  public Date getCreated()
  {
    return created;
  }

  /**
   * @return the contact
   */
  public String getContact()
  {
    return contact;
  }

  /**
   * @return the users
   */
  public int getUsers()
  {
    return users;
  }

  /**
   * @return the extUsers
   */
  public int getExtUsers()
  {
    return extUsers;
  }

}
