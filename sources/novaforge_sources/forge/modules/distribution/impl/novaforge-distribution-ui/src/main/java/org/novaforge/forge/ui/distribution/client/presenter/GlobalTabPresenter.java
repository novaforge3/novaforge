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

package org.novaforge.forge.ui.distribution.client.presenter;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.DistributionEntryPoint;
import org.novaforge.forge.ui.distribution.client.event.OpenTabEvent;
import org.novaforge.forge.ui.distribution.client.event.OpenTabEvent.OpenTabEventHandler;
import org.novaforge.forge.ui.distribution.client.presenter.daughter.DistributionManagementPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.diffusion.DiffusionDifferedTabPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.diffusion.DiffusionTabPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.mother.MotherTabPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.reporting.ReportingPresenter;
import org.novaforge.forge.ui.distribution.client.view.GlobalTabView;
import org.novaforge.forge.ui.distribution.client.view.GlobalTabViewImpl;

/**
 * @author BILET-JC
 */
public class GlobalTabPresenter implements Presenter
{
	public static final Integer							TAB_DAUGHTERS			= 0;
	public static final Integer							TAB_MOTHER				= 1;

	private final GlobalTabView							display;
	private final DistributionManagementPresenter	daughterPresenter;
	private final MotherTabPresenter						motherTabPresenter;
	private final ReportingPresenter						reportingPresenter;
	private final DiffusionTabPresenter					diffusionTabPresenter;
	private final DiffusionDifferedTabPresenter		diffusionDifferedTabPresenter;
	private boolean											isDiffusionAllowed	= true;

	public GlobalTabPresenter(final GlobalTabViewImpl pDisplay,
			final DistributionManagementPresenter pDaughterPresenter,
			final MotherTabPresenter pMotherTabPresenter, final ReportingPresenter pReportingPresenter,
			final DiffusionTabPresenter pDiffusionTabPresenter,
			final DiffusionDifferedTabPresenter pDiffusionDifferedTabPresenter)
	{

		super();
		display = pDisplay;
		daughterPresenter = pDaughterPresenter;
		motherTabPresenter = pMotherTabPresenter;
		reportingPresenter = pReportingPresenter;
		diffusionTabPresenter = pDiffusionTabPresenter;
		diffusionDifferedTabPresenter = pDiffusionDifferedTabPresenter;
		bind();
	}

	public void bind()
	{
		// to change tab event
		DistributionEntryPoint.getEventBus().addHandler(OpenTabEvent.TYPE, new OpenTabEventHandler()
		{

			@Override
			public void OpenTab(final OpenTabEvent event)
			{
				display.getTabPanel().selectTab(event.getTabIndex());
			}
		});
		// when other tab selected
		display.getTabPanel().addSelectionHandler(new SelectionHandler<Integer>()
		{

			@Override
			public void onSelection(final SelectionEvent<Integer> pEvent)
			{
				switch (pEvent.getSelectedItem())
				{
					case 0:
						daughterPresenter.refreshPresenter();
						break;
					case 1:
						motherTabPresenter.refreshPresenter();
						break;
					case 2:
						reportingPresenter.refreshPresenter();
						break;
					case 3:
						if (diffusionTabPresenter != null)
						{
							diffusionTabPresenter.refreshPresenter();
						}
						break;
					case 4:
						if (diffusionDifferedTabPresenter != null)
						{
							diffusionDifferedTabPresenter.refreshPresenter();
						}
						break;
				}

			}
		});
	}

	public GlobalTabPresenter(final GlobalTabViewImpl pDisplay, final DistributionManagementPresenter pDaughterPresenter,
														final MotherTabPresenter pMotherTabPresenter, final ReportingPresenter pReportingPresenter)
	{
		super();
		display = pDisplay;
		daughterPresenter = pDaughterPresenter;
		motherTabPresenter = pMotherTabPresenter;
		reportingPresenter = pReportingPresenter;
		diffusionTabPresenter = null;
		diffusionDifferedTabPresenter = null;
		isDiffusionAllowed = false;
		bind();
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
		display.getTabPanel().insert(daughterPresenter.getDisplay(), Common.MESSAGES.headerDaughterTab(), 0);
		display.getTabPanel().insert(motherTabPresenter.getDisplay(), Common.MESSAGES.headerMotherTab(), 1);
		display.getTabPanel().insert(reportingPresenter.getDisplay(), Common.MESSAGES.headerReportingTab(), 2);

		if (isDiffusionAllowed)
		{
			display.getTabPanel().insert(diffusionTabPresenter.getDisplay(), Common.MESSAGES.headerDiffusionTab(), 3);
			display.getTabPanel().insert(diffusionDifferedTabPresenter.getDisplay(), Common.MESSAGES.headerDiffusionDiffTab(),
																	 4);
		}
	}
}
