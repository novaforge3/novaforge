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
package org.novaforge.forge.distribution.reporting.dao;

import org.novaforge.forge.distribution.reporting.domain.ForgeAnalysis;
import org.novaforge.forge.distribution.reporting.domain.ProjectDimension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Bilet-jc
 */
public interface ForgeAnalysisDAO
{
	String FORGEID       = "forgeId";
	String FORGENAME     = "forgeName";
	String PROJECTNAME   = "projectName";
	String USERROLE      = "userRole";
	String NBPROJECTS    = "nbProjects";
	String NBACCOUNT     = "nbAccount";
	String NBUSERACCOUNT = "nbUserAccount";
	String ORGANIZATION  = "organization";

	List<ForgeAnalysis> getByForge(final Long pId);

	List<ForgeAnalysis> findAnalysisByForgeId(final UUID forgeId);

	List<Map<String, String>> findProjectAndAccountNumbersByForges(final String... pForgeIds);

	List<ProjectDimension> findDimensionByForgeId(UUID pForgeId);

	/**
	 * @param pForgeIds
	 * @return
	 */
	List<Map<String, String>> findProjectNumbersByOrganization(final String... pForgeIds);

	/**
	 * @param pForgeIds
	 * @return
	 */
	List<Map<String, String>> findAccountNumbersByRolesAndForges(final String... pForgeIds);
	
	
	/**
	 * @param pForgeIds
	 * @return
	 */
	List<Map<String, String>> findlastDateUpdatedByForges(final String... pForgeIds);
	
	

	/**
	 * @param pForgeAnalysis
	 */
	void delete(ForgeAnalysis pForgeAnalysis);

	/**
	 * @param pProject
	 */
	void delete(ProjectDimension pProject);

	/**
	 * @return
	 */
	ProjectDimension newProjectDimension();

	/**
	 * @param pProjectDimension
	 * @return
	 */
	ProjectDimension save(ProjectDimension pProjectDimension);

	/**
	 * @return
	 */
	ForgeAnalysis newForgeAnalysis();

	/**
	 * @param pForgeAnalysis
	 * @return
	 */
	ForgeAnalysis save(ForgeAnalysis pForgeAnalysis);

	/**
	 * @param pForgeAnalysis
	 * @return
	 */
	ForgeAnalysis update(ForgeAnalysis pForgeAnalysis);

}
