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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.pilotageexecution.commons;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.OnSelectionTabEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.commons.IterationContextView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.commons.IterationContextViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;

/**
 * @author Bilet-jc
 *
 */
public class IterationContextPresenter implements Presenter {

	private final IterationContextView display;
	private final SimpleEventBus localEventBus;
	
	
	/**
	 * @param localEventBus
	 * @param display
	 */
	public IterationContextPresenter(SimpleEventBus localEventBus) {
		super();
		this.localEventBus = localEventBus;
		this.display = new IterationContextViewImpl();
		bind();
	}
	
	private void bind() {
		localEventBus.addHandler(OnSelectionTabEvent.TYPE, new OnSelectionTabEvent.Handler() {
			
			@Override
			public void onSelectionTab(OnSelectionTabEvent event) {
				display.getIteration().setText(Common.EMPTY_TEXT);
				display.getLot().setText(Common.EMPTY_TEXT);
				display.getParentLot().setText(Common.EMPTY_TEXT);
				display.getStartDate().setText(Common.EMPTY_TEXT);
				display.getEndDate().setText(Common.EMPTY_TEXT);
			}
		});
	}

	/**
	 * Update informations displayed on the top : iteration, lot & sublot
	 */
	public void updateContext(IterationDTO iteration) {
		display.getIteration().setText(iteration.getLabel());
		display.getLot().setText(iteration.getLot().getName());
		if (iteration.getLot().getParentLotId() != null) {
			display.getParentLotL().setVisible(true);
			display.getParentLot().setText(iteration.getLot().getParentLotName());
		}
		/* if no parent lot, hide it */
		else {
			display.getParentLot().setText(Common.EMPTY_TEXT);
			display.getParentLotL().setVisible(false);
		}
		display.getStartDate().setText(Common.FR_DATE_FORMAT_ONLY_DAY.format(iteration.getStartDate()));
		display.getEndDate().setText(Common.FR_DATE_FORMAT_ONLY_DAY.format(iteration.getEndDate()));
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
	
	@Override
	public IsWidget getDisplay() {
		return display.asWidget();
	}

}
