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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons.acronymbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

import java.util.Map;

/**
 * This class displays a list of acronyms and their definitions
 * 
 * @author BILET-JC
 * 
 */
public class AcronymBoxViewImpl extends PopupPanel implements AcronymBoxView {

	private static AcronymBoxViewImplUiBinder uiBinder = GWT.create(AcronymBoxViewImplUiBinder.class);
	@UiField
	Label acronymTitle;
	@UiField
	Panel acronym;
	@UiField
	Button backB;
	public AcronymBoxViewImpl(Map<String, String> acronyms) {
		Common.RESOURCE.css().ensureInjected();
		add(uiBinder.createAndBindUi(this));
		acronymTitle.setText(Common.MESSAGES_ESTIMATION.acronymTitle());

		final FlexTable flexTable = new FlexTable();
		int cpt = 0;
		for (String key : acronyms.keySet())
		{
			Label acronym = new Label();
			Label definition = new Label();
			acronym.addStyleName(Common.RESOURCE.css().acronymLabel());
			definition.addStyleName(Common.RESOURCE.css().gridLabelRight());
			String value = acronyms.get(key);
			acronym.setText(key);
			definition.setText(value);
			flexTable.setWidget(cpt, 0, acronym);
			flexTable.setWidget(cpt, 1, definition);
			cpt++;
		}
		this.acronym.add(flexTable);
		backB.setText(Common.GLOBAL.buttonClose());
		backB.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		setGlassEnabled(true);
	}

	@Override
	public void showWidget() {
		center();
		show();
	}

	interface AcronymBoxViewImplUiBinder extends UiBinder<Widget, AcronymBoxViewImpl>
	{
	}
}
