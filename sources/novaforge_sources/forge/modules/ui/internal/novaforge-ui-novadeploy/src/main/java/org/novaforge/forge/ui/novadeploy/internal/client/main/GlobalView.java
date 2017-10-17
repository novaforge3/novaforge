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

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
* @author Vincent Chenal
*/
public interface GlobalView extends ComponentContainer
{
	/**
	 * Should be called to refresh view according to the
	 * {@link Locale} given
	 *
	 * @param pLocale
	 *            the new locale
	 */
	void refreshLocale(Locale pLocale);

	/**
	 * This method allows to return the {@link Button} used for user
	 * menu
	 *
	 * @return {@link Button} used for user menu
	 */
	Button getOverview();

	/**
	 * This method allows to return the {@link Button} used for group
	 * menu
	 *
	 * @return {@link Button} used for group menu
	 */
	Button getEnvironments();

	/**
	 * This method allows to return the {@link Button} used for
	 * request menu
	 *
	 * @return {@link Button} used for request menu
	 */
	Button getDescriptors();

	/**
	 * @param pComponent
	 */
	void setSecondComponent(Component pComponent);
}
