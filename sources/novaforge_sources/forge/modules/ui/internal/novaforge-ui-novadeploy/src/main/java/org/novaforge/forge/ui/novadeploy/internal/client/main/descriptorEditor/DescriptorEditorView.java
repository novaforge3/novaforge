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
package org.novaforge.forge.ui.novadeploy.internal.client.main.descriptorEditor;

import java.util.Locale;

import net.engio.mbassy.bus.MBassador;

import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorEditor;
import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentFrom;
import org.novaforge.forge.ui.novadeploy.internal.client.exceptions.DescriptorEditorException;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;

/**
 * @author Vincent Chenal
 */
public interface DescriptorEditorView extends ComponentContainer
{
	/**
	 * Should be called to refresh view according to the
	 * {@link Locale} given
	 *
	 * @param pLocale
	 *            the new locale
	 */
	void refreshLocale(Locale pLocale);

	void refreshContent();

	Button getReturnButton();

	Button getCreateButton();

	public EnvironmentFrom getEnvironmentFrom();

	public void prepareEditor(DescriptorEditor.Mode mode, String descriptorName, int descriptorVersion, String content,
			EnvironmentFrom environmentFrom) throws DescriptorEditorException;

	public String getDescriptorContent();

	public void setEventBus(MBassador<PortalEvent> eventbus);
}
