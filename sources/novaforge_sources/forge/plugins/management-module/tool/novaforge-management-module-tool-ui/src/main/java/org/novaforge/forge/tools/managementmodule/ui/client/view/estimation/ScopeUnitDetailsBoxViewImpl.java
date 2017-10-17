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
package org.novaforge.forge.tools.managementmodule.ui.client.view.estimation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitStatusEnum;

public class ScopeUnitDetailsBoxViewImpl extends PopupPanel implements ScopeUnitDetailsBoxView {

	private static ScopeUnitDetailsBoxViewImplUiBinder uiBinder = GWT.create(ScopeUnitDetailsBoxViewImplUiBinder.class);
	@UiField
	Label                              scopeUnitDetailsTitle;
	@UiField
	Panel                              scopeUnitDetails;
	@UiField
	Label                                scopeUnitNameL;
	@UiField
	TextBox                                scopeUnitName;
	@UiField
	Label                                scopeUnitParentL;
	@UiField
	TextBox                                scopeUnitParent;
	@UiField
	Label                                scopeUnitDescriptionL;
	@UiField
	TextBox                                scopeUnitDescription;
	@UiField
	Label                                scopeUnitStateL;
	@UiField
	TextBox                                scopeUnitState;
	@UiField
	Label                                scopeUnitLastUpdateL;
	@UiField
	TextBox                                scopeUnitLastUpdate;
	@UiField
	Button                                backB;
	public ScopeUnitDetailsBoxViewImpl() {

	    Common.RESOURCE.css().ensureInjected();
	    add(uiBinder.createAndBindUi(this));
	    scopeUnitDetailsTitle.setText(Common.MESSAGES_ESTIMATION.scopeUnitDetailsTitle());
		scopeUnitNameL.setText(Common.MESSAGES_ESTIMATION.scopeUnitNameL());
		scopeUnitParentL.setText(Common.MESSAGES_ESTIMATION.scopeUnitParentL());
		scopeUnitDescriptionL.setText(Common.MESSAGES_ESTIMATION.scopeUnitDescriptionL());
		scopeUnitStateL.setText(Common.MESSAGES_ESTIMATION.scopeUnitStateL());
		scopeUnitLastUpdateL.setText(Common.MESSAGES_ESTIMATION.scopeUnitLastUpdateL());
		backB.setText(Common.GLOBAL.buttonClose());
		backB.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		scopeUnitName.setEnabled(false);
		scopeUnitParent.setEnabled(false);
		scopeUnitDescription.setEnabled(false);
		scopeUnitState.setEnabled(false);
		scopeUnitLastUpdate.setEnabled(false);
		setGlassEnabled(true);

	}

	   @Override
	   public void showWidget()
	   {
	      center();
	      show();
	   }

	@Override
	public void refreshScopeUnit(ScopeUnitDTO pScopeUnit) {
		scopeUnitName.setValue(pScopeUnit.getName());
		if (pScopeUnit.getParentScopeUnit() != null) {
			scopeUnitParent.setValue(pScopeUnit.getParentScopeUnit().getName());
		}
		else {
			scopeUnitParent.setValue(Common.GLOBAL.noScopeUnitParent());
		}
		if (pScopeUnit.getDescription() != null) {
			scopeUnitDescription.setValue(pScopeUnit.getDescription());
		}
		else {
			scopeUnitParent.setValue(Common.MESSAGES_ESTIMATION.noScopeUnitDesc());
		}
		scopeUnitState.setValue(ScopeUnitStatusEnum.getEnum(pScopeUnit.getStatus()).getLabel());
		scopeUnitLastUpdate.setValue(Common.FR_DATE_FORMAT.format(pScopeUnit.getDate()));
	}

	interface ScopeUnitDetailsBoxViewImplUiBinder extends UiBinder<Widget, ScopeUnitDetailsBoxViewImpl>
	{
	}
}
