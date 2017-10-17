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
package org.novaforge.forge.tool.requirements.scheduler.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tool.requirements.scheduler.api.SequenceRunner;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementSchedulerServiceException;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Sequence;
import org.novaforge.forge.tools.requirements.common.services.RequirementSchedulerService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Guillaume Morin
 */
public class RequirementSchedulerServiceImpl implements RequirementSchedulerService
{

	private static final Log log = LogFactory.getLog(RequirementSchedulerServiceImpl.class);
	private static int sequenceRunnerGlobalCounter;
	private final Set<SequenceRunner> currentSequenceRunner;
	private final ExecutorService		 mainExecutorService;
	// TODO made a real factory
	private int											 maxPoolSequence;

	public RequirementSchedulerServiceImpl()
	{
		Set<SequenceRunner> s = new HashSet<SequenceRunner>();
		currentSequenceRunner = Collections.synchronizedSet(s);
		if (maxPoolSequence == 0)
		{
			log.info("MaxPoolThread used (10)");
			maxPoolSequence = 10;
		}
		mainExecutorService = Executors.newFixedThreadPool(maxPoolSequence);
	}

	public static void setfSequenceRunnerGlobalCounter(final int pFSequenceRunnerGlobalCounter)
	{
		sequenceRunnerGlobalCounter = pFSequenceRunnerGlobalCounter;
	}

	public void starting()
	{
		log.info("[SCHEDULER SERVICE]");
		log.info("Max pool in parallel : " + maxPoolSequence);
	}

	@Override
	public synchronized void sequenceExecuteAsynch(final Sequence pSequence)
			throws RequirementSchedulerServiceException
	{
		SequenceRunner sq = null;
		try
		{
			if (!checkSequenceBykey(pSequence.getSequenceID()))
			{
				sequenceRunnerGlobalCounter++;
				sq = new SequenceRunner(pSequence, currentSequenceRunner, sequenceRunnerGlobalCounter);
				log.info("[start sequence " + pSequence + "]");
				mainExecutorService.submit(sq);
				currentSequenceRunner.add(sq);
			}
			else
			{
				throw new RequirementSchedulerServiceException("the sequence is running");
			}
		}
		catch (Exception e)
		{
			currentSequenceRunner.remove(sq);
			throw new RequirementSchedulerServiceException("The sequence  " + pSequence + " can't be launched ", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sequenceShutdown(final String sequenceID) throws RequirementSchedulerServiceException
	{
		for (SequenceRunner sq : currentSequenceRunner)
		{
			if (sequenceID.equals(sq.getSequence().getSequenceID()))
			{
				sq.shutdown();
			}
		}
	}

	@Override
	public synchronized boolean sequenceIsStartable(final Sequence pSequence)
			throws RequirementSchedulerServiceException
	{
		return (!checkSequenceBykey(pSequence.getSequenceID()));
	}

	@Override
	public synchronized boolean sequenceIsStartable(final String sequenceID)
			throws RequirementSchedulerServiceException
	{
		return (!checkSequenceBykey(sequenceID));
	}

	private boolean checkSequenceBykey(final String pSequenceID)
	{
		boolean returnedValue = false;
		for (SequenceRunner sq : currentSequenceRunner)
		{
			if (pSequenceID.equals(sq.getSequence().getSequenceID()))
			{
				returnedValue = true;
				break;
			}
		}
		return returnedValue;
	}

	public void setMaxPoolSequence(final int pFMaxPoolSequence)
	{
		maxPoolSequence = pFMaxPoolSequence;
	}

}
