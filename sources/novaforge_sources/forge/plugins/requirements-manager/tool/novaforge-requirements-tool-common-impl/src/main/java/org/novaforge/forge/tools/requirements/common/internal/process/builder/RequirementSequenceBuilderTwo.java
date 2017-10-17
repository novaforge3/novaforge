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
package org.novaforge.forge.tools.requirements.common.internal.process.builder;

import org.novaforge.forge.tool.requirements.scheduler.api.ActivityImpl;
import org.novaforge.forge.tool.requirements.scheduler.api.SequenceImpl;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;
import org.novaforge.forge.tools.requirements.common.connectors.SCMRequirementConnector;
import org.novaforge.forge.tools.requirements.common.connectors.TestRequirementConnector;
import org.novaforge.forge.tools.requirements.common.internal.process.task.ExportTestlinkSynchronizationTask;
import org.novaforge.forge.tools.requirements.common.internal.process.task.ExternalRepositorySynchronizationTask;
import org.novaforge.forge.tools.requirements.common.internal.process.task.ImportTestlinkSynchronizationTask;
import org.novaforge.forge.tools.requirements.common.internal.process.task.SVNSynchronizationTask;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Activity;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Sequence;
import org.novaforge.forge.tools.requirements.common.services.RequirementSequenceBuilderService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sbenoist
 */
public class RequirementSequenceBuilderTwo implements RequirementSequenceBuilderService
{

	private TestRequirementConnector                     testRequirementConnector;
	private SCMRequirementConnector                      scmRequirementConnector;
	private List<ExternalRepositoryRequirementConnector> extRepositoryRequirementConnectors = new ArrayList<>();

	@Override
	public Sequence createNewSequence(final String pProjectId, final String pCodeRepositoryPath,
	    final String pUserId, final List<String> pItems) throws Exception
	{
		if (pItems.size() > 0)
		{
			/**
			 * Values are hard coded later change to use variables injected with IPOJO for exemple
			 */
			List<Activity> activityCol = new ArrayList<Activity>();
			SequenceImpl sequence = new SequenceImpl(pProjectId, activityCol);

			if (pItems.contains("repositories"))
			{
				ActivityImpl activityOne = new ActivityImpl();
				for (ExternalRepositoryRequirementConnector extConnector : extRepositoryRequirementConnectors)
				{
					ExternalRepositorySynchronizationTask extTask = new ExternalRepositorySynchronizationTask(
					    "EXT TASK", pProjectId, pUserId, extConnector);
					activityOne.addTasks(extTask);
				}
				sequence.addActivity(activityOne);
			}

			ActivityImpl activityTwo = new ActivityImpl();
			ActivityImpl activityThree = new ActivityImpl();

			if (pItems.contains("testlink"))
			{
				ExportTestlinkSynchronizationTask exportTestTask = new ExportTestlinkSynchronizationTask(
				    "EXPORT TEST TASK", pProjectId, pUserId, testRequirementConnector);
				activityTwo.addTasks(exportTestTask);

				ImportTestlinkSynchronizationTask importTestTask = new ImportTestlinkSynchronizationTask(
				    "IMPORT TEST TASK", pProjectId, pUserId, testRequirementConnector);
				activityThree.addTasks(importTestTask);
			}

			if (pItems.contains("code"))
			{
				SVNSynchronizationTask SVNtask = new SVNSynchronizationTask("SVN TASK", pProjectId,
				    pCodeRepositoryPath, pUserId, scmRequirementConnector);
				activityThree.addTasks(SVNtask);
			}

			if (activityTwo.iterator().hasNext())
			{
				sequence.addActivity(activityTwo);
			}

			if (activityThree.iterator().hasNext())
			{
				sequence.addActivity(activityThree);
			}
			return sequence;
		}
		else
		{
			throw new Exception("List items must contain at least 1 token");
		}
	}

	public void setTestRequirementConnector(final TestRequirementConnector pTestRequirementConnector)
	{
		testRequirementConnector = pTestRequirementConnector;
	}

	public void setScmRequirementConnector(final SCMRequirementConnector pScmRequirementConnector)
	{
		scmRequirementConnector = pScmRequirementConnector;
	}

	public void setExternalRepositoryRequirementConnectors(
	    final List<ExternalRepositoryRequirementConnector> pExternalRepositoryRequirementConnectors)
	{
		extRepositoryRequirementConnectors = pExternalRepositoryRequirementConnectors;
	}

}
