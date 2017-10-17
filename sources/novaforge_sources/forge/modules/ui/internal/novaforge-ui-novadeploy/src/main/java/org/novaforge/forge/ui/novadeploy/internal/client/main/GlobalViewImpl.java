/**
 * NovaForge(TM) is a web-based forge offering a Collaborative
 * Development and Project Management Environment.
 *
 * Copyright (C) 2007-2012 BULL SAS
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.ui.novadeploy.internal.client.main;

import java.util.Locale;

import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.SideBarLayout;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;

/**
* @author Vincent Chenal
*/
public class GlobalViewImpl extends SideBarLayout implements GlobalView
{

	private static final String OVERVIEW_KEY = "overview";
	private static final String ENVIRONMENTS_KEY = "environments";
	private static final String DESCRIPTORS_KEY = "descriptors";

	private final Button overview;
	private final Button environments;
	private final Button descriptors;

	/**
	 * Default constructor.
	 */
	public GlobalViewImpl()
	{
		super();
		overview = addButton(OVERVIEW_KEY, new ThemeResource(NovaForgeResources.ICON_USER));
		environments = addButton(ENVIRONMENTS_KEY, new ThemeResource(NovaForgeResources.ICON_GROUP));
		descriptors = addButton(DESCRIPTORS_KEY, new ThemeResource(NovaForgeResources.ICON_USER_JOIN));
		this.setStyleName(NovaForge.SIDEBAR_APPLICATION_CONTENT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach()
	{
		super.attach();
		refreshLocale(getLocale());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshLocale(final Locale pLocale)
	{
		getTitle().setValue("NovaDeploy");
		overview.setCaption("Overview");
		environments.setCaption("Environments");
		descriptors.setCaption("Descriptors");
	}

	@Override
	public Button getOverview()
	{
		return this.overview;
	}

	@Override
	public Button getEnvironments()
	{
		return this.environments;
	}

	@Override
	public Button getDescriptors()
	{
		return this.descriptors;
	}
}
