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

package org.novaforge.forge.tools.managementmodule.ui.server;

import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.domain.UnitTime;
import org.novaforge.forge.tools.managementmodule.exceptions.ExceptionCode;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ManagementModuleService;
import org.novaforge.forge.tools.managementmodule.ui.shared.CDOParametersDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentSimpleDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TransformationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 * 
 * @author vvigo
 */
public class ManagementModuleServiceImpl extends SimpleServiceImpl implements ManagementModuleService
{

	/**
	 * UID
	 */
	private static final long serialVersionUID = 6346447256283547956L;

	@Override
	public String controlInstanceAndGetProjectId(final String instanceId) throws ManagementModuleException
	{
		try
		{
			final Project project = getManagementModuleManager().getProjectByInstanceId(instanceId);
			return project.getProjectId();
		}
		catch (Exception ex)
		{
			throw manageException(String.format("Problem getting project with instance id : %s", instanceId), ex);
		}
	}

	@Override
	public ProjectDTO getProject(final String idProject) throws ManagementModuleException
	{
		try
		{
			final Project project = getManagementModuleManager().getFullProject(idProject);
			final ProjectPlan lastProjectPlan = getProjectPlanManager().getLastProjectPlan(idProject);
			return BuildResources.buildProjectDTOFromProject(project, lastProjectPlan.getVersion());
		}
		catch (Exception ex)
		{
			throw manageException(String.format("Problem getting project with  id : %s", idProject), ex);
		}
	}

	@Override
	public boolean updateProject(final ProjectDTO projectDTO, final List<CDOParametersDTO> cdoParametersDTOs)
			throws ManagementModuleException
	{
		try
		{
			Project project = getManagementModuleManager().getFullProject(projectDTO.getProjectId());
			project.setDisciplines(buildSetDisciplineJointureFromListDisciplineDTO(
					projectDTO.getProjectDisciplines(), project.getDisciplines()));
			project.setEstimationComponentSimple(buildEstimationComponentSimpleFromEstimationComponentSimpleDTO(
					projectDTO.getEstimationComponentSimple(), project.getEstimationComponentSimple(), project));
			project.setTaskCategories(buildSetTaskCategoryFromListTaskCategoryDTO(projectDTO.getTaskCategories(),
					project.getTaskCategories(), project));
			project.setTransformation(buildTransformationFromTransformationDTO(projectDTO.getTransformation(),
					project.getTransformation()));
			UnitTime unitTime = getUnitTimeFromEnumValue(projectDTO.getUnitTime());
			boolean unitTimeChanged = false;
			if (!project.getUnitTime().getFunctionalId().equalsIgnoreCase(unitTime.getFunctionalId()))
			{
				unitTimeChanged = true;
				project.setUnitTime(unitTime);
			}
			getManagementModuleManager().updateProject(project);

			if (unitTimeChanged)
			{
				ProjectPlan projectPlan = getProjectPlanManager().getLastProjectPlan(projectDTO.getProjectId());
				if (projectPlan != null)
				{
					getProjectPlanManager().deleteLoadDistributionDisciplinesByProjectPlanId(projectPlan.getId());
				}
			}

			saveParametersCDO(project.getProjectId(), cdoParametersDTOs);

		}
		catch (Exception ex)
		{
			throw manageException(
					String.format("Problem updating project with  id : %s", projectDTO.getProjectId()), ex);
		}
		return true;
	}

	/**
	 * return a modificated List<Set<DisciplineJointure> >
	 *
	 * @param List
	 *          <DisciplineDTO>
	 * @param Set
	 *          <DisciplineJointure>
	 * @return Set<DisciplineJointure>
	 */
	private Set<ProjectDiscipline> buildSetDisciplineJointureFromListDisciplineDTO(final List<ProjectDisciplineDTO> listDisciplineDTO,
																																								 final Set<ProjectDiscipline> setOldDiscipline)
	{
		Set<ProjectDiscipline>       retour           = new HashSet<ProjectDiscipline>();
		Map<Long, ProjectDiscipline> mapOldDiscipline = new HashMap<Long, ProjectDiscipline>();
		for (final ProjectDiscipline projectDiscipline : setOldDiscipline)
		{
			mapOldDiscipline.put(projectDiscipline.getId(), projectDiscipline);
		}
		for (ProjectDisciplineDTO dDTO : listDisciplineDTO)
		{
			final ProjectDiscipline projectDiscipline = mapOldDiscipline.get(dDTO.getId());
			if (projectDiscipline != null)
			{
				projectDiscipline.setRepartition(dDTO.getDisciplinePourcentage());
				retour.add(projectDiscipline);
			}
		}
		return retour;
	}

	/**
	 * Modify a EstimationComponentSimple
	 *
	 * @param EstimationComponentSimple
	 * @param EstimationComponentSimpleDTO
	 * @return the appropriate EstimationComponentSimple
	 */
	private EstimationComponentSimple buildEstimationComponentSimpleFromEstimationComponentSimpleDTO(final EstimationComponentSimpleDTO estimationComponentSimpleDTO,
																																																	 final EstimationComponentSimple estimationComponentSimple,
																																																	 final Project project)
			throws ManagementModuleException
	{
		estimationComponentSimple.setValueENT(estimationComponentSimpleDTO.getValueENT());
		estimationComponentSimple.setValueGDE(estimationComponentSimpleDTO.getValueGDE());
		estimationComponentSimple.setValueGDI(estimationComponentSimpleDTO.getValueGDI());
		estimationComponentSimple.setValueINT(estimationComponentSimpleDTO.getValueINT());
		estimationComponentSimple.setValueSOR(estimationComponentSimpleDTO.getValueSOR());
		estimationComponentSimple.setIdProjet(project.getProjectId());
		return estimationComponentSimple;
	}

	/**
	 * return a modificated List<Set<TaskCategory> >
	 *
	 * @param List
	 *          <TaskCategoryDTO>
	 * @param Set
	 *          <TaskCategory>
	 * @param Project
	 * @return Set<TaskCategory>
	 * @throws org.novaforge.forge.plugins.managementmodule.exceptions.ManagementModuleException
	 * @throws org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException 
	 */
	private Set<TaskCategory> buildSetTaskCategoryFromListTaskCategoryDTO(
			final List<TaskCategoryDTO> listTaskCategoryDTO, final Set<TaskCategory> oldPersistentTaskCategorySet,
			final Project project)
			throws ManagementModuleException, org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException
	{
	  final Set<TaskCategory> newTaskCategorySet = new HashSet<TaskCategory>();
    final Map<Long, TaskCategory> mapOldTaskCategoriesById = new HashMap<Long, TaskCategory>();
    for (final TaskCategory taskCategory : oldPersistentTaskCategorySet)
    {
      mapOldTaskCategoriesById.put(taskCategory.getId(), taskCategory);
    }
  	  
    for (TaskCategoryDTO tDTO : listTaskCategoryDTO)
    {      
        TaskCategory taskCategory = mapOldTaskCategoriesById.get(tDTO.getId());
        // new TaskCategory
        if (taskCategory == null)
        {
          taskCategory = getTaskManager().newTaskCategory();
          taskCategory.setProject(project);
          taskCategory.setName(tDTO.getName());
        } 
        oldPersistentTaskCategorySet.remove(taskCategory);
        newTaskCategorySet.add(taskCategory);
    }
    
    //check if category to delete is used by existing task
    for (TaskCategory remainingCategory : oldPersistentTaskCategorySet)
    {
      if (remainingCategory.getId() != null)
      {
        List<Task> lstTaskByCategory = getTaskManager().findByTaskCategoryId(remainingCategory.getId()); 
        if ( lstTaskByCategory != null  && lstTaskByCategory.size() > 0 )
        {
          throw new org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException(ExceptionCode.ERR_TASK_CATEGORY_NO_DELETE_IF_HAS_TASKS);
        }
      }
    }
		return newTaskCategorySet;
	}

	/**
	 * Modify a Transformation
	 * 
	 * @param TransformationDTO
	 * @param Transformation
	 * @return the appropriate Transformation
	 */
	public Transformation buildTransformationFromTransformationDTO(final TransformationDTO transformationDTO,
			final Transformation transformation)
	{
		transformation.setNbHeuresJour(transformationDTO.getNbHeuresJour());
		transformation.setNbJoursAn(transformationDTO.getNbJoursAn());
		transformation.setNbJoursMois(transformationDTO.getNbJoursMois());
		transformation.setNbJoursNonTravail(transformationDTO.getNbJoursNonTravail());
		transformation.setNbJoursSemaine(transformationDTO.getNbJoursSemaine());
		return transformation;
	}

	/**
	 * Return the domain UnitTimeEnum from the enum client value
	 *
	 * @param unitTimeEnum
	 *          the enum client value
	 * @return the server domain value
	 * @throws ManagementModuleException
	 *           problem during recovery
	 */
	private UnitTime getUnitTimeFromEnumValue(final org.novaforge.forge.tools.managementmodule.ui.shared.UnitTimeEnum unitTimeEnum)
			throws ManagementModuleException
	{
		try
		{
			switch (unitTimeEnum)
			{
				case UNIT_TIME_WEEK:
					return getReferentielManager().getUnitTimeByIdFunctional(ManagementModuleConstants.UNIT_TIME_WEEK);
				case UNIT_TIME_MONTH:
					return getReferentielManager().getUnitTimeByIdFunctional(ManagementModuleConstants.UNIT_TIME_MONTH);
				default:
					throw new ManagementModuleException("UnitTimeEnum unknown" + unitTimeEnum, null);
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException mex)
		{
			throw manageException(String.format("Unable to get ServerValue for unittime: %s", unitTimeEnum), mex);
		}
	}

	private boolean saveParametersCDO(final String projectId, final List<CDOParametersDTO> pCDOParametersDTO)
			throws ManagementModuleException
	{
		try
		{
			final List<CDOParameters> loadedCDO = this.getProjectPlanManager().findAllCDOParameters(projectId);

			final Map<String, CDOParameters> mapLoadedCDO = new HashMap<String, CDOParameters>();

			for (CDOParameters cdo : loadedCDO)
			{
				mapLoadedCDO.put("" + cdo.getId(), cdo);
			}

			final List<CDOParameters> listToSave = new ArrayList<CDOParameters>();

			final List<CDOParameters> listToUpdate = new ArrayList<CDOParameters>();

			final List<CDOParameters> listToDelete = new ArrayList<CDOParameters>();

			for (CDOParametersDTO dto : pCDOParametersDTO)
			{
				if (dto.getcDOParametersID() < 1)
				{
					// create
					CDOParameters newParameter = this.getProjectPlanManager().newCDOParameters();

					newParameter.setHost(dto.getHost());
					newParameter.setPort(dto.getPort());
					newParameter.setRepository(dto.getRepository());
					newParameter.setProjetCdo(dto.getProjetCdo());
					newParameter.setSystemGraal(substringBeforeLast(dto.getSystemGraal(), ".graal"));
					newParameter.setCronExpression(dto.getCronExpression());
					newParameter.setProject(this.getManagementModuleManager().getFullProject(projectId));

					listToSave.add(newParameter);
				}
				else
				{
					// update
					if (mapLoadedCDO.containsKey("" + dto.getcDOParametersID()))
					{
						CDOParameters param = mapLoadedCDO.get("" + dto.getcDOParametersID());

						param.setCronExpression(dto.getCronExpression());
						param.setHost(dto.getHost());
						param.setPort(dto.getPort());
						param.setProject(this.getManagementModuleManager().getFullProject(projectId));
						param.setProjetCdo(dto.getProjetCdo());
						param.setRepository(dto.getRepository());
						param.setSystemGraal(substringBeforeLast(dto.getSystemGraal(), ".graal"));

						listToUpdate.add(param);

						// remove element to constitute the delete list.
						mapLoadedCDO.remove("" + dto.getcDOParametersID());
					}
				}
			}

			if (mapLoadedCDO.size() > 0)
			{
				listToDelete.addAll(mapLoadedCDO.values());
			}

			// create
			getProjectPlanManager().creeteCDOParameters(listToSave);

			// update
			getProjectPlanManager().updateCDOParameters(listToUpdate);

			// delete
			getProjectPlanManager().deleteCDOParameters(listToDelete);

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to creete/update/delete elements for CDOParameters.", e);
		}
		return true;
	}

	/**
	 * <p>
	 * Gets the substring before the last occurrence of a separator. The separator is not returned.
	 * </p>
	 * <p>
	 * A <code>null</code> string input will return <code>null</code>. An empty ("") string input will return
	 * the empty string. An empty or <code>null</code> separator will return the input string.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.substringBeforeLast(null, *)      = null
	 * StringUtils.substringBeforeLast("", *)        = ""
	 * StringUtils.substringBeforeLast("abcba", "b") = "abc"
	 * StringUtils.substringBeforeLast("abc", "c")   = "ab"
	 * StringUtils.substringBeforeLast("a", "a")     = ""
	 * StringUtils.substringBeforeLast("a", "z")     = "a"
	 * StringUtils.substringBeforeLast("a", null)    = "a"
	 * StringUtils.substringBeforeLast("a", "")      = "a"
	 * </pre>
	 *
	 * @param str
	 *          the String to get a substring from, may be null
	 * @param separator
	 *          the String to search for, may be null
	 * @return the substring before the last occurrence of the separator, <code>null</code> if null String input
	 * @since 2.0
	 */
	private String substringBeforeLast(final String str, final String separator)
	{
		if (isEmpty(str) || isEmpty(separator))
		{
			return str;
		}
		int pos = str.lastIndexOf(separator.toLowerCase());
		if (pos == -1)
		{
			pos = str.lastIndexOf(separator.toUpperCase());
			if (pos == -1)
			{
				return str;
			}
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the String. That functionality is
	 * available in isBlank().
	 * </p>
	 *
	 * @param str
	 *          the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	private boolean isEmpty(final String str)
	{
		return str == null || str.length() == 0;
	}

	@Override
	public List<CDOParametersDTO> loadListParametersCDO(final String projectId) throws ManagementModuleException
	{
		final List<CDOParametersDTO> retour = new ArrayList<CDOParametersDTO>();
		try
		{
			List<CDOParameters> loadList = this.getProjectPlanManager().findAllCDOParameters(projectId);
			for (CDOParameters element : loadList)
			{
				retour.add(BuildResources.buildCDOParametersDTO(element));
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to import List of Perimeter Unit from CDO.", e);
		}
		return retour;
	}

	// Empty checks
	// -----------------------------------------------------------------------

	@Override
	public boolean updateFromCDORefScopeUnit(final ProjectDTO projet_forge) throws ManagementModuleException
	{
		boolean rs = true;
		try
		{
			List<CDOParameters> list = getProjectPlanManager().findAllCDOParameters(projet_forge.getProjectId());

			for (CDOParameters cdo : list)
			{
				rs = rs && this.getProjectPlanManager().updateFromCDORefScopeUnit(cdo.getHost(), cdo.getPort().toString(),
																																					cdo.getRepository(), cdo.getProjetCdo(),
																																					cdo.getSystemGraal(), cdo.getProject(), cdo);
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			rs = false;
		}
		return rs;
	}

	@Override
	public String getUnitTimeName(final String projectId) throws ManagementModuleException
	{
		String unitTimeName = null;

		Project project;
		try
		{
			project = getManagementModuleManager().getFullProject(projectId);
			if (project != null)
			{
				unitTimeName = project.getUnitTime().getName();

			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get the name of unitTime.", e);
		}

		return unitTimeName;
	}
}
