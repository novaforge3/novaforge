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
package org.novaforge.forge.tools.managementmodule.initialization.internal;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.osgi.framework.BundleContext;

/**
 * @author s267533
 */
public class ManagementModuleUpgrade370To380
{

	/**
	 * 
	 */
	private static final Log						LOG												 = LogFactory
																																			.getLog(ManagementModuleUpgrade370To380.class);
	private static final String LOC_FILE = "upgrade3.7.0-3.8.0.lock";
	private File												workingDir;
	private BundleContext							 bundleContext;
	private ManagementModuleManager		 managementModuleManager;
	
	public void starting()
	{
		workingDir = bundleContext.getDataFile(LOC_FILE);
		try
		{
			if (!workingDir.exists())
			{
				boolean init = true;
				if (init)
				{
					LOG.info("upgrade data to 3.8.0 starting...");
					createApplicativeFunctions();
					upgradeApplicativeRights();

				}

				LOG.info("upgrade done.");
				workingDir.createNewFile();
			}
			else
			{
				LOG.info("Initialize Management Module datas already done.");
			}
		}
		catch (Exception e)
		{
			LOG.error("Unable to initialize Management Module datas", e);
		}
	}


	private void createApplicativeFunctions()
	{
		ApplicativeFunction sf7 = managementModuleManager.newApplicativeFunction();
		sf7.setName(ManagementModuleConstants.FUNCTION_GLOBAL_SUPERVISION);
		sf7.setValue("Suivi global du projet");
	    ApplicativeFunction sf8 = managementModuleManager.newApplicativeFunction();
	    sf8.setName(ManagementModuleConstants.FUNCTION_ADMINISTRATION);
	    sf8.setValue("Administration");
	    ApplicativeFunction sf9 = managementModuleManager.newApplicativeFunction();
	    sf9.setName(ManagementModuleConstants.FUNCTION_PROJECT_PLAN);
	    sf9.setValue("Plan projet");
	    ApplicativeFunction sf10 = managementModuleManager.newApplicativeFunction();
	    sf10.setName(ManagementModuleConstants.FUNCTION_BURNDOWN);
	    sf10.setValue("Burndown");		
		try
		{
			managementModuleManager.createApplicativeFunction(sf8);
			managementModuleManager.createApplicativeFunction(sf9);
			managementModuleManager.createApplicativeFunction(sf10);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management createApplicativeFunctions" , e);
		}
	}
	
	
	/**
	 * This method creates the application rights
	 */
	private void upgradeApplicativeRights()
	{
		try
		{
      final ApplicativeFunction administrateFunction = managementModuleManager.getApplicativeFunction(ManagementModuleConstants.FUNCTION_ADMINISTRATION);
      final ApplicativeFunction planProjectFunction = managementModuleManager.getApplicativeFunction(ManagementModuleConstants.FUNCTION_PROJECT_PLAN);
      final ApplicativeFunction burndownFunction = managementModuleManager.getApplicativeFunction(ManagementModuleConstants.FUNCTION_BURNDOWN);

      
			final String accessRightsRead = ManagementModuleConstants.ACCESS_RIGHTS_READ;
			final String accessRightsWrite = ManagementModuleConstants.ACCESS_RIGHTS_WRITE;

			// admin rights
			final Role roleAdmin = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_ADMIN);
			createApplicationRight(roleAdmin, administrateFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, planProjectFunction, accessRightsWrite);
			createApplicationRight(roleAdmin,burndownFunction, accessRightsRead);
			    
			// sponsor rights
			final Role roleSponsor = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_SPONSOR);
			createApplicationRight(roleSponsor, administrateFunction, accessRightsRead);
			createApplicationRight(roleSponsor, planProjectFunction, accessRightsRead);
			createApplicationRight(roleSponsor,burndownFunction, accessRightsRead);
			
			// customer rights
			final Role roleCustomer = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_CUSTOMER);
			createApplicationRight(roleCustomer, administrateFunction, accessRightsRead);
			createApplicationRight(roleCustomer, planProjectFunction, accessRightsRead);
			createApplicationRight(roleCustomer,burndownFunction, accessRightsRead);
			
			// director project rights
			final Role roleProjectDirector = managementModuleManager
																					 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_PROJECT_DIRECTOR);
			createApplicationRight(roleProjectDirector, administrateFunction, accessRightsRead);
			createApplicationRight(roleProjectDirector, planProjectFunction, accessRightsWrite);
			createApplicationRight(roleProjectDirector,burndownFunction, accessRightsRead);

			
			// project manager rights
			final Role roleProjectManager = managementModuleManager
																					.getRoleByFunctionalId(ManagementModuleConstants.ROLE_PROJECT_MANAGER);
			createApplicationRight(roleProjectManager, administrateFunction, accessRightsRead);
			createApplicationRight(roleProjectManager, planProjectFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager,burndownFunction, accessRightsRead);
			
			// MOE leader
			final Role roleMOELeader = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOE_LEADER);
			createApplicationRight(roleMOELeader, administrateFunction, accessRightsRead);
			createApplicationRight(roleMOELeader, planProjectFunction, accessRightsRead);
			createApplicationRight(roleMOELeader,burndownFunction, accessRightsRead);
			
			// MOA leader
			final Role roleMOALeader = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOA_LEADER);
			createApplicationRight(roleMOALeader, administrateFunction, accessRightsRead);
			createApplicationRight(roleMOALeader, planProjectFunction, accessRightsRead);
			createApplicationRight(roleMOALeader,burndownFunction, accessRightsRead);
			
			// MOE member
			final Role roleMOEMember = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOE_MEMBER);
			createApplicationRight(roleMOEMember,burndownFunction, accessRightsRead);
			
			// MOA member
			final Role roleMOAMember = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOA_MEMBER);
			createApplicationRight(roleMOAMember,burndownFunction, accessRightsRead);
			
			// observer
			final Role roleObserver = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_OBSERVER);
			createApplicationRight(roleObserver, administrateFunction, accessRightsRead);
			createApplicationRight(roleObserver, planProjectFunction, accessRightsRead);
			createApplicationRight(roleObserver,burndownFunction, accessRightsRead);
		}
		catch (Exception ex)
		{
			LOG.error("Unable to upgrade applicative rights", ex);
		}

	}

	/**
	 * This method build a new ApplicativeRights with arguments and call its creation on managementModuleManager
	 *
	 * @param role
	 *     the Role of the ApplicativeRights to build
	 * @param applicativeFunction
	 *     the ApplicativeFunction of the ApplicativeRights to build
	 * @param accessRight
	 *     the right of the role on the applicativeFunction
	 *
	 * @throws ManagementModuleException
	 *     error during creation
	 */
	private void createApplicationRight(final Role role, final ApplicativeFunction applicativeFunction,
																			final String accessRight) throws ManagementModuleException
	{
		ApplicativeRights applicativeRights = managementModuleManager.newApplicativeRights();
		applicativeRights.setRole(role);
		applicativeRights.setApplicativeFunction(applicativeFunction);
		applicativeRights.setAccesRight(accessRight);
		managementModuleManager.createApplicativeRights(applicativeRights);
	}

	public void setBundleContext(final BundleContext pBundleContext)
	{
		bundleContext = pBundleContext;
	}

	public void setManagementModuleManager(final ManagementModuleManager pManagementModuleManager)
	{
		managementModuleManager = pManagementModuleManager;
	}
}
