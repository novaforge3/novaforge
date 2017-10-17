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

package org.novaforge.forge.ui.distribution.client.presenter.mother;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.DistributionEntryPoint;
import org.novaforge.forge.ui.distribution.client.event.NoDistributionPanelEvent;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.view.mother.MotherTabView;
import org.novaforge.forge.ui.distribution.client.view.mother.MotherTabViewImpl;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;

/**
 * @author BILET-JC
 */
public class MotherTabPresenter implements Presenter
{

	private final MotherTabView	display;
	private final SubscriptionPresenter	subscriptionPresenter, subscriptioningPresenter;
	private final MotherPresenter			motherPresenter;

	/**
	 * The goal of this presenter is to display the correct view in the mother tab depending of the current
	 * forge
	 * 
	 * @param pService
	 * @param pDisplay
	 * @param pSubscriptionPresenter
	 * @param pSubscriptioningPresenter
	 * @param pMotherPresenter
	 */
	public MotherTabPresenter(final MotherTabViewImpl pDisplay,
			final SubscriptionPresenter pSubscriptionPresenter,
			final SubscriptionPresenter pSubscriptioningPresenter, final MotherPresenter pMotherPresenter)
	{
		display = pDisplay;
		subscriptionPresenter = pSubscriptionPresenter;
		subscriptioningPresenter = pSubscriptioningPresenter;
		motherPresenter = pMotherPresenter;
		getCurrentForge();
		bind();
	}

	private void getCurrentForge()
	{
		new AbstractDistributionRPCCall<ForgeDTO>()
		{
			@Override
			protected void callService(final AsyncCallback<ForgeDTO> pCb)
			{
				DistributionEntryPoint.getServiceAsync().getCurrentForge(pCb);
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				display.noDistributionPanel(Common.MESSAGES.noDistributionText());
			}

			@Override
			public void onSuccess(final ForgeDTO pResult)
			{
				display.getMotherPanel().clear();
				if (pResult != null)
				{
					displayMotherByLevel(pResult);
				}
				else
				{
					display.noDistributionPanel(Common.MESSAGES.noReportingText());
				}
			}

		}.retry(0);
	}

	public void bind()
	{
		DistributionEntryPoint.getEventBus().addHandler(NoDistributionPanelEvent.TYPE,
																										new NoDistributionPanelEvent.Handler()
																										{

																											@Override
																											public void refreshView(final NoDistributionPanelEvent event)
																											{
																												display.noDistributionPanel(event.getMessage());
																											}
																										});
	}

	private void displayMotherByLevel(final ForgeDTO pResult)
	{
		switch (pResult.getForgeLevel())
		{
			case Common.ORPHAN:
			case Common.SUSPENDED:
				if (pResult.getParentRequest() != null)
				{
					subscriptioningPresenter.displayMother(pResult.getParentRequest().getDestinationForgeId(),
							pResult.getParentRequest().getDestinationForgeLabel(), pResult.getParentRequest()
									.getReason());
					display.getMotherPanel().add(subscriptioningPresenter.getDisplay());
				}
				else
				{
					display.getMotherPanel().add(subscriptionPresenter.getDisplay());
				}
				break;
			case Common.LOCAL:
			case Common.ZONAL:
				if (pResult.getParentRequest() != null)
				{
					if (TypeDistributionRequestEnum.SUBSCRIPTION.equals(pResult.getParentRequest().getType()))
					{
						subscriptioningPresenter.displayMother(pResult.getParentRequest().getDestinationForgeId(),
								pResult.getParentRequest().getDestinationForgeLabel(), pResult.getParentRequest()
										.getReason());
						display.getMotherPanel().add(subscriptioningPresenter.getDisplay());
					}
					else if (TypeDistributionRequestEnum.UNSUBSCRIPTION.equals(pResult.getParentRequest()
							.getType()))
					{
						motherPresenter.disableUnsubscription();
						display.getMotherPanel().add(motherPresenter.getDisplay());
					}

				}
				else
				{
					display.getMotherPanel().add(motherPresenter.getDisplay());
				}
				break;
			case Common.CENTRAL:
				display.noDistributionPanel(Common.MESSAGES.noMotherText());
				break;
			default:
				display.noDistributionPanel(Common.MESSAGES.eMother());
				break;
		}

	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());

	}

	public IsWidget getDisplay()
	{
		return display;
	}

	public void refreshPresenter()
	{
		subscriptionPresenter.refreshPresenter();
		motherPresenter.refreshPresenter();
		getCurrentForge();

	}
}