/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the
 * License.
 *
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have
 * received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with libraries listed in COPYRIGHT file at the
 * top-level directof of this distribution (or a modified version of
 * that libraries), containing parts covered by the terms of licenses
 * cited in the COPYRIGHT file, the licensors of this Program grant
 * you additional permission to convey the resulting work.
 */
package org.novaforge.forge.ui.novadeploy.internal.module;

import java.util.UUID;

import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;

/**
 * @author B-Martinelli
 */
public abstract class AbstractNovaDeployPresenter extends AbstractPortalPresenter
{

	/**
	 * This variable contains the project Id associated to this
	 * presenter
	 */
	private final String projectId;

	/**
	 * Cache used to store the Instance UUID of mailing list
	 * application of current project.
	 */
	private UUID mailingListInstance;

	/**
	 * Default constructor
	 * 
	 * @param pPortalContext
	 *            the initial context
	 */
	public AbstractNovaDeployPresenter(final PortalContext pPortalContext)
	{
		super(pPortalContext);
		projectId = pPortalContext.getAttributes().get(PortalContext.KEY.PROJECTID);
	}

	/**
	 * Retrieve the current authenticated user
	 * 
	 * @return login of authenticated user
	 */
	protected String getCurrentUser()
	{
		return NovaDeployModule.getAuthentificationService().getCurrentUser();
	}

	/**
	 * Retrieve the current project id
	 * 
	 * @return project id
	 */
	protected String getProjectId()
	{
		return projectId;
	}

	/** {@inheritDoc} */
	@Override
	protected PortalModuleId getModuleId()
	{
		return NovaDeployModule.getPortalModuleId();
	}

	/**
	 * @return the mailingListInstance
	 */
	public UUID getMailingListInstance()
	{
		return mailingListInstance;
	}

	/**
	 * @param mailingListInstance
	 *            the mailingListInstance to set
	 */
	public void setMailingListInstance(final UUID mailingListInstance)
	{
		this.mailingListInstance = mailingListInstance;
	}

}
