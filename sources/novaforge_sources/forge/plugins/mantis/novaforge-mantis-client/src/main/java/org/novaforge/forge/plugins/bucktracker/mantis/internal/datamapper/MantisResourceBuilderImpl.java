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
package org.novaforge.forge.plugins.bucktracker.mantis.internal.datamapper;

import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.bucktracker.mantis.datamapper.MantisResourceBuilder;
import org.novaforge.forge.plugins.bucktracker.mantis.internal.MantisConstant;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueNoteData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ObjectRef;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData;
import org.novaforge.forge.plugins.categories.beans.BugTrackerIssueBeanImpl;

import java.math.BigInteger;

/**
 * This class is used in order to build object used by mantis WS.
 * 
 * @author lamirang
 */
public class MantisResourceBuilderImpl implements MantisResourceBuilder
{
  /**
   * Constant bracket open
   */
  private static final String BRACKET_OPEN        = " (";
  /**
   * Constant bracket close
   */
  private static final String BRACKET_CLOSE       = ")";
  /**
   * Constant space
   */
  private static final String SPACE_SEPARATOR     = " ";

  /**
   * This constant is used to build {@link ObjectRef} with fake project name
   */
  private static final String UNUSED_PROJECT_NAME = "project";

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectData buildProjectData(final PluginProject pPluginProject, final String pInstanceName)
  {
    final ProjectData projectData = new ProjectData();
    projectData.setName(pPluginProject.getName() + BRACKET_OPEN + pInstanceName + BRACKET_CLOSE);
    projectData.setDescription(pPluginProject.getDescription());
    projectData.setView_state(new ObjectRef(MantisConstant.ACCESS_PRIVATE, null));

    return projectData;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AccountData buildAccountData(final PluginUser pPluginUser)
  {
    final AccountData account = new AccountData();
    account.setName(pPluginUser.getLogin());
    account.setReal_name(pPluginUser.getName() + SPACE_SEPARATOR + pPluginUser.getFirstName());
    account.setEmail(pPluginUser.getEmail());
    account.setLanguage(pPluginUser.getLanguage());

    return account;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IssueData buildIssueData(final BugTrackerIssueBean pBugBean, final String pId, final String pUserName)
  {
    final IssueData issue = new IssueData();
    issue.setSummary(pBugBean.getTitle());
    issue.setDescription(pBugBean.getDescription());
    issue.setAdditional_information(pBugBean.getAdditionalInfo());
    issue.setCategory(pBugBean.getCategory());
    final AccountData account = new AccountData();
    account.setName(pUserName);
    issue.setReporter(account);
    if ((pBugBean.getNotes() != null) && (!"".equals(pBugBean.getNotes())))
    {
      final IssueNoteData issueNoteData = new IssueNoteData();
      issueNoteData.setText(pBugBean.getNotes());
      issueNoteData.setReporter(account);
      final IssueNoteData[] notes = { issueNoteData };
      issue.setNotes(notes);
    }

    issue.setProject(new ObjectRef(new BigInteger(pId), UNUSED_PROJECT_NAME));
    return issue;
  }

  @Override
  public BugTrackerIssueBean buildBugTrackerIssueBean(final IssueData pIssueData)
  {
    final BugTrackerIssueBean bean = new BugTrackerIssueBeanImpl();
    bean.setTitle(pIssueData.getSummary());
    bean.setDescription(pIssueData.getDescription());
    bean.setAdditionalInfo(pIssueData.getAdditional_information());
    bean.setCategory(pIssueData.getCategory());
    if (pIssueData.getSeverity() != null)
    {
      bean.setSeverity(pIssueData.getSeverity().getName());
    }
    if (pIssueData.getReporter() != null)
    {
      bean.setReporter(pIssueData.getReporter().getName());
    }
    if (pIssueData.getHandler() != null)
    {
      bean.setAssignedTo(pIssueData.getHandler().getName());
    }
    if (pIssueData.getId() != null)
    {
      bean.setId(pIssueData.getId().toString());
    }
    if (pIssueData.getPriority() != null)
    {
      bean.setPriority(pIssueData.getPriority().getName());
    }
    bean.setProductVersion(pIssueData.getVersion());
    bean.setTargetVersion(pIssueData.getTarget_version());
    bean.setFixedInVersion(pIssueData.getFixed_in_version());
    if (pIssueData.getReproducibility() != null)
    {
      bean.setReproducibility(pIssueData.getReproducibility().getName());
    }
    if (pIssueData.getResolution() != null)
    {
      bean.setResolution(pIssueData.getResolution().getName());
    }
    if (pIssueData.getStatus() != null)
    {
      bean.setStatus(pIssueData.getStatus().getName());
    }

    return bean;
  }

}
