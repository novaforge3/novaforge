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
package org.novaforge.forge.tools.managementmodule.internal.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.reporting.exceptions.ReportingException;
import org.novaforge.forge.commons.reporting.model.OutputFormat;
import org.novaforge.forge.commons.reporting.services.ReportingService;
import org.novaforge.forge.plugins.management.managementmodule.services.ManagementModulePluginService;
import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.dao.ApplicativeFunctionDAO;
import org.novaforge.forge.tools.managementmodule.dao.ApplicativeRightsDAO;
import org.novaforge.forge.tools.managementmodule.dao.LanguageDAO;
import org.novaforge.forge.tools.managementmodule.dao.MembershipDAO;
import org.novaforge.forge.tools.managementmodule.dao.ProjectDAO;
import org.novaforge.forge.tools.managementmodule.dao.RoleDAO;
import org.novaforge.forge.tools.managementmodule.dao.UserDAO;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;
import org.novaforge.forge.tools.managementmodule.domain.Estimation;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Language;
import org.novaforge.forge.tools.managementmodule.domain.Membership;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.exceptions.ExceptionCode;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManagementModuleManagerImpl implements ManagementModuleManager
{

	private static final Log							LOG = LogFactory.getLog(ManagementModuleManagerImpl.class);

	private ProjectDAO										projectDAO;

	private UserDAO											 userDAO;

	private LanguageDAO									 languageDAO;

	private RoleDAO											 roleDAO;

	private MembershipDAO								 membershipDAO;

	private ApplicativeFunctionDAO				applicativeFunctionDAO;

	private ApplicativeRightsDAO					applicativeRightsDAO;

	private ProjectPlanManager						projectPlanManager;

	private ManagementModulePluginService managementModulePluginService;

	private ReportingService							reportingService;

	private BusinessObjectFactory				 businessObjectFactory;

	private BundleContext								 bundleContext;

	/************************* project *******************************/

	@Override
	public Project newProject()
	{
		return businessObjectFactory.getInstanceProject();
	}

	@Override
	public Project creeteProject(final String pProjectName, final String pProjectDescription,
			final String pProjectId, final EstimationComponentSimple estimationComponentSimple,
			final Transformation transformation) throws ManagementModuleException
	{
		Project projectData = businessObjectFactory.getInstanceProject();
		projectData.setName(pProjectName);
		projectData.setDescription(pProjectDescription);
		projectData.setProjectId(pProjectId);
		projectData.setEstimationComponentSimple(estimationComponentSimple);
		projectData.setTransformation(transformation);
		projectData = projectDAO.save(projectData);

		return projectData;
	}

	@Override
	public Project creeteProject(final Project toolProject) throws ManagementModuleException
	{
		return projectDAO.save(toolProject);
	}

	@Override
	public Project getProject(final String projectId) throws ManagementModuleException
	{
		return this.projectDAO.findByProjectId(projectId);
	}

	@Override
	public Project updateProject(final Project project) throws ManagementModuleException
	{
		Set<ProjectPlan> projectPlans = project.getProjectPlans();
		for (ProjectPlan projectPlan : projectPlans)
		{
			final List<Estimation> estimations = new ArrayList<Estimation>();
			List<Estimation> estimationsTmp = projectPlanManager.getEstimations(projectPlan.getId());
			for (Estimation estimation : estimationsTmp)
			{
				if (estimation.getSimple() != null
						&& !estimation.getSimple().equals(ManagementModuleConstants.FUNCTION_POINT_NONE))
				{
					estimations.add(estimation);
				}
			}
			projectPlanManager.updateEstimations(estimations, project.getEstimationComponentSimple(),
					projectPlan.getEstimationComponentDetail());
		}
		return this.projectDAO.merge(project);
	}

	@Override
	public boolean deleteProject(final String projectId) throws ManagementModuleException
	{
		Project currentProject = this.projectDAO.findByProjectId(projectId);
		// we delete the project plans from newest to oldest
		List<ProjectPlan> listeProjectPlan = new ArrayList<ProjectPlan>(currentProject.getProjectPlans());
		Collections.sort(listeProjectPlan, Collections.reverseOrder(new Comparator<ProjectPlan>()
		{
			@Override
			public int compare(final ProjectPlan o1, final ProjectPlan o2)
			{
				return o1.getVersion().compareTo(o2.getVersion());
			}
		}));
		for (final ProjectPlan projectPlan : listeProjectPlan)
		{
			projectPlanManager.deleteProjectPlan(projectPlan.getId());
		}
		this.projectDAO.delete(currentProject);

		return true;
	}

	@Override
	public boolean existProjectId(final String pProjectId) throws ManagementModuleException
	{
		return this.projectDAO.findByProjectId(pProjectId) != null;
	}

	/************************* user *******************************/

	@Override
	public User newUser()
	{
		return businessObjectFactory.getInstanceUser();
	}

	@Override
	public boolean existUserLogin(final String userLogin) throws ManagementModuleException
	{
		return userDAO.findUserByLogin(userLogin) != null;
	}

	@Override
	public User creeteUser(final String pUserFirstName, final String pUserLastName,
			final String pUserLastLogin, final String pUserLanguage) throws ManagementModuleException
	{
		final User userData = businessObjectFactory.getInstanceUser();
		userData.setFirstName(pUserFirstName);
		userData.setLastName(pUserLastName);
		userData.setLogin(pUserLastLogin);
		Language userLanguage = getLanguage(pUserLanguage);
		if (userLanguage == null)
		{
			LOG.error(ExceptionCode.ERR_USER_LANGUAGE_DOESNT_EXIST.toString());
			throw new ManagementModuleException(String.format("Language doesn't exists with [language=%s]",
					pUserLanguage));

		}
		userData.setLanguage(userLanguage);

		userDAO.save(userData);

		return userData;
	}

	@Override
	public User getUser(final String userLogin) throws ManagementModuleException
	{
		return this.userDAO.findUserByLogin(userLogin);
	}

	@Override
	public boolean updateUser(final User user) throws ManagementModuleException
	{
		this.userDAO.merge(user);
		return true;
	}

	@Override
	public boolean deleteUser(final User user) throws ManagementModuleException
	{
		this.userDAO.delete(user);
		return true;
	}

	/************************* role *******************************/

	@Override
	public Role newRole()
	{
		return businessObjectFactory.getInstanceRole();
	}

	@Override
	public Role createRole(final Role role) throws ManagementModuleException
	{
		return roleDAO.save(role);
	}

	@Override
	public Role updateRole(final Role role) throws ManagementModuleException
	{
		return roleDAO.merge(role);
	}

	@Override
	public Role getRoleByName(final String roleName) throws ManagementModuleException
	{
		return this.roleDAO.findByRoleName(roleName);
	}

	@Override
	public Role getRoleByFunctionalId(final String functionalId) throws ManagementModuleException
	{
		return this.roleDAO.findByFunctionalId(functionalId);
	}

	@Override
	public List<Role> getAllRoles() throws ManagementModuleException
	{
		return this.roleDAO.findAllRoles();
	}

	@Override
	public Membership addUserMembership(final String pProjectId, final String pLogin, final String pRoleName)
			throws ManagementModuleException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("input : projectId =" + pProjectId);
			LOG.debug("input : login =" + pLogin);
			LOG.debug("input : roles =" + pRoleName);
		}

		if (!existUserLogin(pLogin))
		{
			LOG.error(ExceptionCode.ERR_MEMBERSHIP_USER_DOESNT_EXIST.toString());
			throw new ManagementModuleException(String.format("User doesn't exists with [projectId=%s, login=%s]",
					pProjectId, pLogin));
		}

		Role userRole = getRoleByName(pRoleName);
		if (userRole == null)
		{
			LOG.error(ExceptionCode.ERR_MEMBERSHIP_ROLE_DOESNT_EXIST.toString());
			throw new ManagementModuleException(String.format("Role doesn't exists with [role=%s]", pRoleName));
		}

		if (!existProjectId(pProjectId))
		{
			LOG.error(ExceptionCode.ERR_MEMBERSHIP_PROJECT_DOESNT_EXIST.toString());
			throw new ManagementModuleException(String.format("Project doesn't exists with [projectId=%s]",
					pProjectId));
		}

		// get the user
		User user = getUser(pLogin);

		// get the project
		Project project = getFullProject(pProjectId);
		Membership currentMembership = newMembership();
		currentMembership.setUser(user);
		currentMembership.setProject(project);
		currentMembership.setRole(userRole);

		membershipDAO.save(currentMembership);

		return currentMembership;
	}

	@Override
	public void removeAllProjectMembeship(final String pProjectId) throws ManagementModuleException
	{
		if (!existProjectId(pProjectId))
		{
			LOG.error(ExceptionCode.ERR_MEMBERSHIP_PROJECT_DOESNT_EXIST.toString());
			throw new ManagementModuleException(String.format("Project doesn't exists with [projectId=%s]",
					pProjectId));
		}

		// get the project
		Project project = getFullProject(pProjectId);
		for (Membership itMembership : project.getMemberships())
		{
			deleteMembership(itMembership);
		}
	}

	/**
	 * ********************** membership ******************************
	 */

	@Override
	public Membership newMembership()
	{
		return businessObjectFactory.getInstanceMembership();
	}

	@Override
	public Membership getMembership(final String login, final String projectId) throws ManagementModuleException
	{
		return this.membershipDAO.findAllMembershipsByActorAndProject(projectId, login);
	}

	@Override
	public boolean updateMembership(final User u, final Project p, final Role newRole) throws ManagementModuleException
	{
		this.membershipDAO.updateMembership(u, p, newRole);
		return true;
	}

	@Override
	public boolean deleteMembership(final User u, final Project p) throws ManagementModuleException
	{
		this.membershipDAO.deleteMembership(u, p);
		return true;
	}

	@Override
	public void deleteMembership(final Membership m) throws ManagementModuleException
	{
		this.membershipDAO.deleteMembership(m);
	}

	/************************* language *******************************/

	@Override
	public Language newLanguage()
	{
		return languageDAO.newLanguage();
	}

	@Override
	public Language creeteLanguage(final Language language) throws ManagementModuleException
	{
		return languageDAO.save(language);
	}

	@Override
	public Language getLanguage(final String name) throws ManagementModuleException
	{
		return languageDAO.findByName(name);
	}

	@Override
	public List<Language> getAllLanguages() throws ManagementModuleException
	{
		return languageDAO.findAll();
	}

	@Override
	public Project getFullProject(final String projectId) throws ManagementModuleException
	{
		return this.projectDAO.findFullProjectByProjectId(projectId);
	}

	@Override
	public Project getProjectByInstanceId(final String instanceId) throws ManagementModuleException
	{
		return this.projectDAO.findByProjectId(managementModulePluginService.getProjectId(instanceId));
	}

	/************************* ApplicativeFunction ********************************************************************/

	@Override
	public ApplicativeFunction newApplicativeFunction()
	{
		return businessObjectFactory.getInstanceApplicativeFunction();
	}

	@Override
	public ApplicativeFunction createApplicativeFunction(final ApplicativeFunction sf)
			throws ManagementModuleException
	{
		return applicativeFunctionDAO.save(sf);
	}

	@Override
	public List<ApplicativeFunction> getAllApplicativeFunction() throws ManagementModuleException
	{
		return applicativeFunctionDAO.findAll();
	}

	@Override
	public ApplicativeFunction getApplicativeFunction(final String functionalId)
			throws ManagementModuleException
	{
		return applicativeFunctionDAO.findByFunctionalId(functionalId);
	}

	/************************* ApplicativeRights ********************************************************************/

	@Override
	public ApplicativeRights newApplicativeRights()
	{
		return businessObjectFactory.getInstanceApplicativeRights();
	}

	@Override
	public ApplicativeRights newApplicativeRights(final ApplicativeFunction applicativeFunction,
			final Role role, final String access)
	{
		final ApplicativeRights applicativeRights = newApplicativeRights();
		applicativeRights.setApplicativeFunction(applicativeFunction);
		applicativeRights.setRole(role);
		applicativeRights.setAccesRight(access);
		return applicativeRights;
	}

	@Override
	public ApplicativeRights createApplicativeRights(final ApplicativeRights s)
			throws ManagementModuleException
	{
		return applicativeRightsDAO.save(s);
	}

	@Override
	public ApplicativeRights getApplicativeRight(final String toolRoleName, final String applicativeFunctionName)
			throws ManagementModuleException
	{
		return this.applicativeRightsDAO.findRightsByRoleAndFunction(toolRoleName, applicativeFunctionName);
	}

	@Override
	public void renderReport(final String rptdesignName, final OutputFormat format,
			final Map<String, Object> parameters, final OutputStream outputStream) throws ManagementModuleException
	{

		try
		{
			InputStream inputStream = bundleContext.getBundle().getResource(rptdesignName).openStream();

			reportingService.renderReport(inputStream, format, outputStream, parameters);
		}
		catch (ReportingException e)
		{
			throw new ManagementModuleException(MessageFormat.format("Problem while generate report with name =%s",
					rptdesignName), e);
		}
		catch (IOException e)
		{
			throw new ManagementModuleException(MessageFormat.format("Problem while reading report design file",
					rptdesignName), e);
		}
	}

	public void setProjectDAO(final ProjectDAO pProjectDAO)
	{
		projectDAO = pProjectDAO;
	}

	public void setUserDAO(final UserDAO pUserDAO)
	{
		userDAO = pUserDAO;
	}

	public void setLanguageDAO(final LanguageDAO pLanguageDAO)
	{
		languageDAO = pLanguageDAO;
	}

	public void setRoleDAO(final RoleDAO pRoleDAO)
	{
		roleDAO = pRoleDAO;
	}

	public void setMembershipDAO(final MembershipDAO pMembershipDAO)
	{
		membershipDAO = pMembershipDAO;
	}

	public void setApplicativeFunctionDAO(final ApplicativeFunctionDAO pApplicativeFunctionDAO)
	{
		applicativeFunctionDAO = pApplicativeFunctionDAO;
	}

	public void setApplicativeRightsDAO(final ApplicativeRightsDAO pApplicativeRightsDAO)
	{
		applicativeRightsDAO = pApplicativeRightsDAO;
	}

	public void setProjectPlanManager(final ProjectPlanManager pProjectPlanManager)
	{
		projectPlanManager = pProjectPlanManager;
	}

	public void setManagementModulePluginService(
			final ManagementModulePluginService pManagementModulePluginService)
	{
		managementModulePluginService = pManagementModulePluginService;
	}

	public void setReportingService(final ReportingService pReportingService)
	{
		reportingService = pReportingService;
	}

	public void setBusinessObjectFactory(final BusinessObjectFactory pBusinessObjectFactory)
	{
		businessObjectFactory = pBusinessObjectFactory;
	}

	public void setBundleContext(final BundleContext pBundleContext)
	{
		bundleContext = pBundleContext;
	}

}
