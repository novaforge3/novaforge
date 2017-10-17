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
package org.novaforge.forge.plugins.testmanager.testlink.internal;

import org.novaforge.forge.core.plugins.categories.testmanagement.CoveredRequirementBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.CoveredRequirementsBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestManagementCategoryService;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.categories.beans.CoveredRequirementsBeanImpl;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;
import org.novaforge.forge.plugins.testmanager.testlink.services.TestlinkFunctionalService;

import java.util.List;
import java.util.Locale;

/**
 * @author rols-p
 * @author Guillaume Lamirand
 */
public class TestLinkCategoryServiceImpl extends AbstractPluginCategoryService implements
    TestManagementCategoryService
{

	private static final String       PROPERTY_FILE = "testLink";

	/**
	 * Reference to service implementation of {@link TestlinkFunctionalService}
	 */
	private TestlinkFunctionalService testlinkFunctionalService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
	    throws PluginServiceException
	{
		return getMessage(KEY_ACCESS_INFO, pLocale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getPropertyFileName()
	{
		return PROPERTY_FILE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CoveredRequirementsBean getRequirementsCoverageByTest(final String pForgeId,
	    final String pInstanceId, final String pCurrentUser) throws PluginServiceException
	{
		final CoveredRequirementsBean bean = new CoveredRequirementsBeanImpl();
		final List<CoveredRequirementBean> requirements = testlinkFunctionalService
		    .getRequirementsCoverageByTest(pInstanceId);
		for (final CoveredRequirementBean coveredRequirementBean : requirements)
		{
			bean.addCoveredRequirementBean(coveredRequirementBean);
		}
		return bean;
	}

	/**
	 * @param pTestlinkFunctionalService
	 *          the functionalService to set
	 */
	public void setTestlinkFunctionalService(final TestlinkFunctionalService pTestlinkFunctionalService)
	{
		testlinkFunctionalService = pTestlinkFunctionalService;
	}
}