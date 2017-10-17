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
/**
 * MantisConnectTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.novaforge.forge.plugins.bucktracker.mantis.internal.client;

import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapClient;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapConnector;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.FilterData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHistoryStatusData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.MantisConnectLocator;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ObjectRef;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectStatusData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectVersionData;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import java.math.BigInteger;
import java.net.URL;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * To trigger this test, activate a java system property "mantis.profile".
 * 
 * @author lamirang
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MantisClientImplTest extends TestCase
{

  // private static final String ENDPOINT =
  // "http://localhost/mantis-default/mantis/api/soap/mantisconnect.php";
  private static final String     ENDPOINT               = "https://vm-infra-20/mantis-default/mantis/api/soap/mantisconnect.php";
  private static final String     USERNAME               = "administrator";
  private static final String     PASSWORD               = "root";

  private static final String     ACCOUNT_USERNAME       = "Test";
  private static final String     ACCOUNT_NAME           = "My Test User";
  private static final String     ACCOUNT_EMAIL          = "";
  private static final BigInteger ACCOUNT_STATUS         = BigInteger.valueOf(90);
  private static final String     ACCOUNT_PASSWORD       = "1234";

  private static final String     ACCOUNT_UPDATE_NAME    = "My User";
  private static final String     ACCOUNT_UPDATE_EMAIL   = "";

  private static final String     PROJECT_NAME           = "ProjectTest";
  private static final String     PROJECT_DES            = "My Test Project";
  private static final BigInteger PROJECT_ACCESS         = BigInteger.valueOf(50);
  private static final String     ISSUE_NAME             = "My Issue Test";
  private static final String     ISSUE_CATEGORY         = "General";
  private static final String     ISSUE_DES              = "My Issue Test";
  private static final String     LOCALE                 = "english";
  private static final String     PROJECT_UPDATE_DES     = "My New Project";
  private static BigInteger ISSUE_ID = new BigInteger("0000001");
  private boolean                 mantisProfileActivated = false;

  public MantisClientImplTest(final String name)
  {
    super(name);
    final String property = System.getProperty("mantis.profile");
    if ("true".equals(property))
    {
      mantisProfileActivated = true;
    }
  }

  public void test00MantisConnectPortWSDL() throws Exception
  {
    if (isMantisProfileActivated())
    {
      final ServiceFactory serviceFactory = ServiceFactory.newInstance();
      final URL url = new java.net.URL(ENDPOINT + "?wsdl");
      final Service service = serviceFactory.createService(url, new MantisConnectLocator().getServiceName());
      assertThat(service, notNullValue());
    }
  }

  /**
   * @return the mantisProfileActivated
   */
  public boolean isMantisProfileActivated()
  {
    return mantisProfileActivated;
  }

  public void test01MantisConnectPortMc_enum_access_levels() throws Exception
  {
    if (isMantisProfileActivated())
    {
      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);
      final ObjectRef[] accesses = mantisSoapClient.mc_enum_access_levels(connector);
      assertThat(accesses, notNullValue());
    }
  }

  public void test02MantisConnectPortMc_account_add() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final AccountData accountData = new AccountData();
      accountData.setName(ACCOUNT_USERNAME);
      accountData.setReal_name(ACCOUNT_NAME);
      accountData.setEmail(ACCOUNT_EMAIL);
      accountData.setAccess(ACCOUNT_STATUS);
      mantisSoapClient.mc_account_add(connector, accountData, ACCOUNT_PASSWORD);
    }
  }

  public void test03MantisConnectPortMc_account_get() throws Exception
  {
    if (isMantisProfileActivated())
    {
      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger userId = mantisSoapClient.mc_account_get(connector, ACCOUNT_USERNAME);
      assertThat(userId, notNullValue());
    }
  }

  public void test04MantisConnectPortMc_account_update() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger userId = mantisSoapClient.mc_account_get(connector, ACCOUNT_USERNAME);
      final AccountData accountData = new AccountData();
      accountData.setReal_name(ACCOUNT_UPDATE_NAME);
      accountData.setEmail(ACCOUNT_UPDATE_EMAIL);
      accountData.getId();

      final boolean success = mantisSoapClient.mc_account_update(connector, userId, accountData, null);
      assertThat(success, notNullValue());
      assertThat(success, is(true));
    }
  }

  public void test05MantisConnectPortMc_project_add() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final ProjectData project = new ProjectData();
      project.setName(PROJECT_NAME);
      project.setDescription(PROJECT_DES);
      final ObjectRef state = new ObjectRef(PROJECT_ACCESS, null);
      project.setView_state(state);
      final BigInteger id = mantisSoapClient.mc_project_add(connector, project);
      assertThat(id, notNullValue());
    }
  }

  public void test06MantisConnectPortMc_project_get_id_from_name() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger id = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);
      assertThat(id, notNullValue());
    }
  }

  public void test07MantisConnectPortMc_project_update() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger id = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);
      assertThat(id, notNullValue());
      final ProjectData project = new ProjectData();
      project.setName(PROJECT_NAME);
      project.setDescription(PROJECT_UPDATE_DES);
      final boolean success = mantisSoapClient.mc_project_update(connector, id, project);
      assertThat(success, is(true));
    }
  }

  public void test08MantisConnectPortMc_project_get_statuses() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger project_id = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);

      final ProjectStatusData[] statuses = mantisSoapClient.mc_project_get_statuses(connector, project_id,
          LOCALE);

      assertThat(statuses, notNullValue());
    }
  }

  public void test09MantisConnectPortMc_project_add_user() throws Exception
  {
    if (isMantisProfileActivated())
    {
      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger projectId = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);
      final BigInteger userId = mantisSoapClient.mc_account_get(connector, ACCOUNT_USERNAME);

      final BigInteger result = mantisSoapClient.mc_project_add_user(connector, projectId, userId,
          PROJECT_ACCESS);

      assertThat(result, notNullValue());
    }
  }

  public void test10MantisConnectPortMc_project_get_users() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger projectId = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);

      final AccountData[] users = mantisSoapClient.mc_project_get_users(connector, projectId, PROJECT_ACCESS);

      assertThat(users, notNullValue());
    }
  }

  public void test11MantisConnectPortMc_project_get_versions() throws Exception
  {
    if (isMantisProfileActivated())
    {
      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger projectId = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);

      final ProjectVersionData[] versions = mantisSoapClient.mc_project_get_versions(connector, projectId);

      assertThat(versions, notNullValue());
    }
  }

  public void test12MantisConnectPortMc_issue_add() throws Exception
  {
    if (isMantisProfileActivated())
    {
      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      // Construct the project
      final ObjectRef project = new ObjectRef();
      project.setId(mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME));
      project.setName(PROJECT_NAME);

      // Construct the reporter
      final AccountData accountData = new AccountData();
      accountData.setReal_name(ACCOUNT_UPDATE_NAME);
      accountData.setEmail(ACCOUNT_UPDATE_EMAIL);

      final IssueData issue = new IssueData();
      issue.setProject(project);
      issue.setSummary(ISSUE_NAME);
      issue.setDescription(ISSUE_DES);
      issue.setReporter(accountData);
      issue.setCategory(ISSUE_CATEGORY);

      final BigInteger issueId = mantisSoapClient.mc_issue_add(connector, issue);
      ISSUE_ID = issueId;
      assertThat(issueId, notNullValue());
    }
  }

  public void test13MantisConnectPortMc_issue_get() throws Exception
  {
    if (isMantisProfileActivated())
    {
      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final IssueData issue = mantisSoapClient.mc_issue_get(connector, ISSUE_ID);
      assertThat(issue, notNullValue());
    }
  }

  public void test14MantisConnectPortMc_project_get_issues() throws Exception
  {
    if (isMantisProfileActivated())
    {
      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger projectId = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);

      final IssueData[] issues = mantisSoapClient.mc_project_get_issues(connector, projectId,
          BigInteger.valueOf(1), BigInteger.valueOf(-1));

      assertThat(issues, notNullValue());
    }
  }

  public void test141MantisConnectMc_project_get_issues_history_by_status() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      // Construct the project
      final ObjectRef project = new ObjectRef();
      project.setId(mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME));
      project.setName(PROJECT_NAME);
      BigInteger projectId = project.getId();

      // Construct the reporter
      final AccountData accountData = new AccountData();
      accountData.setReal_name(ACCOUNT_UPDATE_NAME);
      accountData.setEmail(ACCOUNT_UPDATE_EMAIL);

      // Construct the issue
      final IssueData issue = new IssueData();
      issue.setProject(project);
      issue.setSummary(ISSUE_NAME);
      issue.setDescription(ISSUE_DES);
      issue.setReporter(accountData);
      issue.setCategory(ISSUE_CATEGORY);
      final BigInteger issueId = mantisSoapClient.mc_issue_add(connector, issue);
      ISSUE_ID = issueId;
      assertThat(issueId, notNullValue());

      final Calendar end = Calendar.getInstance();
      final Calendar start = (Calendar) end.clone();
      start.add(Calendar.DAY_OF_MONTH, -5);
      final int increment = 24 * 60 * 60;

      // Filters
      FilterData[] filters = new FilterData[1];

      FilterData filter = new FilterData();
      filter.setProject_id(projectId);
      filters[0] = filter;
      final IssueHistoryStatusData[] history = mantisSoapClient.mc_project_get_issues_history_by_status(
          connector, projectId, start, end, BigInteger.valueOf(increment), filters);

      assertThat(history, notNullValue());
      assertThat(history.length, is(1));
    }

  }

  public void test15MantisConnectPortMc_project_remove_user() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger projectId = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);
      final BigInteger userId = mantisSoapClient.mc_account_get(connector, ACCOUNT_USERNAME);

      final boolean result = mantisSoapClient.mc_project_remove_user(connector, projectId, userId);

      assertThat(result, notNullValue());
    }
  }

  public void test16MantisConnectPortMc_project_delete() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger id = mantisSoapClient.mc_project_get_id_from_name(connector, PROJECT_NAME);
      assertThat(id, notNullValue());
      final boolean success = mantisSoapClient.mc_project_delete(connector, id);
      assertThat(success, is(true));
    }
  }

  public void test17MantisConnectPortMc_account_delete() throws Exception
  {
    if (isMantisProfileActivated())
    {

      final MantisSoapClient mantisSoapClient = new MantisSoapClientImpl();
      final MantisSoapConnector connector = mantisSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final BigInteger userId = mantisSoapClient.mc_account_get(connector, ACCOUNT_USERNAME);
      mantisSoapClient.mc_account_delete(connector, userId);
    }
  }
}
