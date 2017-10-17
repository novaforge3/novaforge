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

package org.novaforge.forge.ui.distribution.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.GlobalTabPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.distribution.client.presenter.daughter.DistributionManagementPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.diffusion.DiffusionDifferedTabPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.diffusion.DiffusionTabPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.mother.MotherPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.mother.MotherTabPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.mother.SubscriptionPresenter;
import org.novaforge.forge.ui.distribution.client.presenter.reporting.ReportingPresenter;
import org.novaforge.forge.ui.distribution.client.view.GlobalTabViewImpl;
import org.novaforge.forge.ui.distribution.client.view.daughters.DistributionManagementViewImpl;
import org.novaforge.forge.ui.distribution.client.view.diffusion.DiffusionDifferedTabViewImpl;
import org.novaforge.forge.ui.distribution.client.view.diffusion.DiffusionTabViewImpl;
import org.novaforge.forge.ui.distribution.client.view.mother.MotherTabViewImpl;
import org.novaforge.forge.ui.distribution.client.view.mother.MotherViewImpl;
import org.novaforge.forge.ui.distribution.client.view.mother.SubscriptionViewImpl;
import org.novaforge.forge.ui.distribution.client.view.reporting.ReportingViewImpl;
import org.novaforge.forge.ui.distribution.shared.enumeration.MotherActionEnum;

/**
 * @author BILET-JC
 */
public class AppController implements ValueChangeHandler<String>
{

	private GlobalTabPresenter						globalTabPresenter;

	public AppController()
	{
		bind();
	}

	private void bind()
	{
		History.addValueChangeHandler(this);

	}

	/**
	 * manage the values change in history
	 *
	 * @param history
	 *           event
	 */
	@Override
	public void onValueChange(final ValueChangeEvent<String> event)
	{
		final String token = event.getValue();
		if (token != null)
		{
			globalTabPresenter.go(RootLayoutPanel.get());
		}
	}

	public void go()
	{
		loadPresenters();
	}

	private void loadPresenters()
	{
		new AbstractDistributionRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				DistributionEntryPoint.getServiceAsync().hasUserRefProjectAdminPermission(pCb);
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				final boolean isDiffusionAllowed = pResult.booleanValue();
				loadPresenters(isDiffusionAllowed);
			}

		}.retry(0);
	}

	private void loadPresenters(final boolean isDiffusionAllowed)
	{
		// tab with all distributions and (un)subscriptions (position 1)
		final DistributionManagementPresenter daughterPresenter = new DistributionManagementPresenter(new DistributionManagementViewImpl());

		// tab with the form to subscribe a mother forge
		final SubscriptionPresenter subscriptionPresenter = new SubscriptionPresenter(new SubscriptionViewImpl(MotherActionEnum.DO),
																																									MotherActionEnum.DO);

		// tab with the not enable form already fill when subscription is already done
		final SubscriptionPresenter subscriptioningPresenter = new SubscriptionPresenter(new SubscriptionViewImpl(MotherActionEnum.DOING),
																																										 MotherActionEnum.DOING);

		// tab with the already affiliate mother forge
		final MotherPresenter motherPresenter = new MotherPresenter(new MotherViewImpl());

		// tab with the mother stuff (position 2)
		final MotherTabPresenter motherTabPresenter = new MotherTabPresenter(new MotherTabViewImpl(), subscriptionPresenter,
																																				 subscriptioningPresenter, motherPresenter);

		// tab with the reporting (position3)
		final ReportingPresenter reportingPresenter = new ReportingPresenter(new ReportingViewImpl());

		if (isDiffusionAllowed)
		{
			final DiffusionTabPresenter diffusionTabPresenter = new DiffusionTabPresenter(new DiffusionTabViewImpl());
			final DiffusionDifferedTabPresenter diffusionDifferedTabPresenter = new DiffusionDifferedTabPresenter(new DiffusionDifferedTabViewImpl());
			// global tab containing all others
			globalTabPresenter = new GlobalTabPresenter(new GlobalTabViewImpl(), daughterPresenter, motherTabPresenter,
																									reportingPresenter, diffusionTabPresenter,
																									diffusionDifferedTabPresenter);
		}
		else
		{
			globalTabPresenter = new GlobalTabPresenter(new GlobalTabViewImpl(), daughterPresenter, motherTabPresenter,
																									reportingPresenter);
		}
		globalTabPresenter.go(RootLayoutPanel.get());
	}

}