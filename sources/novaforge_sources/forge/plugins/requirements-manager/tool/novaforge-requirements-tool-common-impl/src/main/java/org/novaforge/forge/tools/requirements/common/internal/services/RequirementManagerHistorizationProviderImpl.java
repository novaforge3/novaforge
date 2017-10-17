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
package org.novaforge.forge.tools.requirements.common.internal.services;

import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;
import org.novaforge.forge.tools.requirements.common.connectors.SCMRequirementConnector;
import org.novaforge.forge.tools.requirements.common.connectors.TestRequirementConnector;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementConnectorException;
import org.novaforge.forge.tools.requirements.common.facades.RequirementManagerHistorizationProvider;
import org.novaforge.forge.tools.requirements.common.model.connectors.ConnectionRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sbenoist
 */
public class RequirementManagerHistorizationProviderImpl implements RequirementManagerHistorizationProvider
{
	private List<ExternalRepositoryRequirementConnector> extConnectors = new ArrayList<>();

	private SCMRequirementConnector								scmConnector;

	private TestRequirementConnector							 testConnector;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ConnectionRecord> getHistory(final String pProjectId, final Date pBeginDate, final Date pEndDate)
			throws RequirementConnectorException
	{
		List<ConnectionRecord> records = new ArrayList<ConnectionRecord>();

		for (ExternalRepositoryRequirementConnector extConnector : extConnectors)
		{
			records.addAll(extConnector.getHistory(pProjectId, pBeginDate, pEndDate));
		}
		records.addAll(scmConnector.getHistory(pProjectId, pBeginDate, pEndDate));
		records.addAll(testConnector.getHistory(pProjectId, pBeginDate, pEndDate));
		return records;
	}

	public void setExtConnectors(final List<ExternalRepositoryRequirementConnector> pExtConnectors)
	{
		extConnectors = pExtConnectors;
	}

	public void setScmConnector(final SCMRequirementConnector pScmConnector)
	{
		scmConnector = pScmConnector;
	}

	public void setTestConnector(final TestRequirementConnector pTestConnector)
	{
		testConnector = pTestConnector;
	}

}
