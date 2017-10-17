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
package org.novaforge.forge.ui.portal.servlet.handlers;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.novaforge.forge.ui.portal.shared.PortalUI;

import java.io.IOException;

/**
 * This {@link RequestHandler} will redirect user to public content
 * 
 * @author Guillaume Lamirand
 */
public class ModulesRequestHandler implements RequestHandler
{
	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 8256230748119180389L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean handleRequest(final VaadinSession pSession, final VaadinRequest pRequest,
	    final VaadinResponse pResponse) throws IOException
	{
		UI uI = pSession.getService().findUI(pRequest);
		if ((uI != null) && (uI instanceof PortalUI))
		{
			PortalUI portalUI = (PortalUI) uI;
			portalUI.handleModulesQueue();
		}
		return false;

	}
}