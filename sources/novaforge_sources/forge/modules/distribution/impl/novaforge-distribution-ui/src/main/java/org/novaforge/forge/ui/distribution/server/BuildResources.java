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

package org.novaforge.forge.ui.distribution.server;

import org.novaforge.forge.distribution.reference.model.ApplicationInError;
import org.novaforge.forge.distribution.reference.model.DiffusionResult;
import org.novaforge.forge.distribution.reference.model.DiffusionTemplateResult;
import org.novaforge.forge.distribution.reference.model.SynchDiffered;
import org.novaforge.forge.distribution.reference.model.SynchronizationResult;
import org.novaforge.forge.distribution.reference.model.SynchronizationStatus;
import org.novaforge.forge.distribution.reference.model.TargetForge;
import org.novaforge.forge.distribution.reference.model.TemplateInError;
import org.novaforge.forge.distribution.reference.model.TemplateSynchroResult;
import org.novaforge.forge.ui.distribution.shared.diffusion.DiffusionResultDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchDifferedDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchonizationResultDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchronizationStatusEnum;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Helper class used to build/convert DTO objects.
 * 
 * @author rols-p
 */
public class BuildResources
{

	public static DiffusionResultDTO buildDiffusionResultDTO(final DiffusionResult diffusionResult,
			final DiffusionTemplateResult templateResult)
	{
		final DiffusionResultDTO diffResultDTO = new DiffusionResultDTO();
		// Project Ref result
		final List<SynchronizationResult> synchResults = diffusionResult.getSynchronizationResults();
		if ((synchResults != null) && !synchResults.isEmpty())
		{
			final List<SynchonizationResultDTO> synchResultsDTO = new ArrayList<SynchonizationResultDTO>(
					synchResults.size());
			for (final SynchronizationResult synchResult : synchResults)
			{
				final SynchonizationResultDTO synchResultDTO = buildSynchResultDTO(synchResult);
				synchResultsDTO.add(synchResultDTO);
			}
			diffResultDTO.setSynchProjectResults(synchResultsDTO);
		}

		// Templates result
		final List<TemplateSynchroResult> templateSynchroResults = templateResult.getResults();
		if ((templateSynchroResults != null) && !templateSynchroResults.isEmpty())
		{
			final List<SynchonizationResultDTO> synchResultsDTO = new ArrayList<SynchonizationResultDTO>(
					templateSynchroResults.size());
			for (final TemplateSynchroResult synchResult : templateSynchroResults)
			{
				final SynchonizationResultDTO synchResultDTO = buildSynchTemplateResultDTO(synchResult);
				synchResultsDTO.add(synchResultDTO);
			}
			diffResultDTO.setSynchTemplateResults(synchResultsDTO);
		}

		return diffResultDTO;
	}

	/**
	 * @param synchResult
	 * @return
	 */
	private static SynchonizationResultDTO buildSynchResultDTO(final SynchronizationResult synchResult)
	{
		final SynchonizationResultDTO synchResultDTO = new SynchonizationResultDTO(synchResult.getForgeIP());
		final SynchronizationStatus pStatus = synchResult.getStatus();
		SynchronizationStatusEnum status;
		switch (pStatus)
		{
			case SUCCESS:
				status = SynchronizationStatusEnum.SUCCESS;
				break;
			case SUCCESS_WITH_ERRORS:
				status = SynchronizationStatusEnum.SUCCESS_WITH_ERRORS;
				break;
			default:
				status = SynchronizationStatusEnum.FAILED;
				break;
		}
		synchResultDTO.setStatus(status);

		// set error msg (add failed Msg + Appli error msgs)
		final List<ApplicationInError> applisInError = synchResult.getApplicationsInError();
		String errorMsg;
		final StringBuilder builder = new StringBuilder();
		if (synchResult.getFailedMsg() != null)
		{
			builder.append(synchResult.getFailedMsg());
		}
		if ((applisInError != null) && !applisInError.isEmpty())
		{
			for (final ApplicationInError appliInError : applisInError)
			{
				builder.append(" [");
				builder.append(" Application=");
				builder.append(appliInError.getApplicationName());
				builder.append(", error=");
				builder.append(appliInError.getErrorMessage());
				builder.append("]");
			}
		}
		errorMsg = builder.toString();
		synchResultDTO.setFailedMsg(errorMsg);

		return synchResultDTO;
	}

	/**
	 * @param synchResult
	 * @return
	 */
	private static SynchonizationResultDTO buildSynchTemplateResultDTO(final TemplateSynchroResult synchResult)
	{
		final SynchonizationResultDTO synchResultDTO = new SynchonizationResultDTO(synchResult.getForgeIP());
		final SynchronizationStatus pStatus = synchResult.getStatus();
		SynchronizationStatusEnum status;
		switch (pStatus)
		{
			case SUCCESS:
				status = SynchronizationStatusEnum.SUCCESS;
				break;
			case SUCCESS_WITH_ERRORS:
				status = SynchronizationStatusEnum.SUCCESS_WITH_ERRORS;
				break;
			default:
				status = SynchronizationStatusEnum.FAILED;
				break;
		}
		synchResultDTO.setStatus(status);

		// set error msg (add failed Msg + Templates error msgs)
		final List<TemplateInError> templatesInError = synchResult.getTemplatesInError();
		String errorMsg;
		final StringBuilder builder = new StringBuilder();
		if (synchResult.getFailedMsg() != null)
		{
			builder.append(synchResult.getFailedMsg());
		}
		if ((templatesInError != null) && !templatesInError.isEmpty())
		{
			for (final TemplateInError templateInError : templatesInError)
			{
				builder.append(" [");
				builder.append(" Template=");
				builder.append(templateInError.getTemplateName());
				builder.append(", error=");
				builder.append(templateInError.getErrorMessage());
				builder.append("]");
			}
		}
		errorMsg = builder.toString();
		synchResultDTO.setFailedMsg(errorMsg);

		return synchResultDTO;
	}

	/**
	 * @param forges
	 * @return
	 */
	public static List<TargetForgeDTO> buildTargetForgesDTO(final Set<TargetForge> forges)
	{
		final List<TargetForgeDTO> targetForgeDTOs = new ArrayList<TargetForgeDTO>();
		if ((forges != null) && !forges.isEmpty())
		{
			for (final TargetForge forge : forges)
			{
				final TargetForgeDTO targetForgeDTO = new TargetForgeDTO(forge.getLabel(),
						forge.getDescription(), forge.getForgeLevel());
				targetForgeDTOs.add(targetForgeDTO);
			}
		}
		return targetForgeDTOs;
	}

	public static SynchDifferedDTO buildSynchDifferedDTO(final SynchDiffered schedulingConf)
	{
		final SynchDifferedDTO returned = new SynchDifferedDTO();
		returned.setActive(schedulingConf.isActive());
		returned.setHours(schedulingConf.getHours());
		returned.setMinutes(schedulingConf.getMinutes());
		returned.setPeriod(schedulingConf.getPeriod());
		return returned;
	}

}
