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
package org.novaforge.forge.ui.distribution.client.presenter.mother;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.DistributionEntryPoint;
import org.novaforge.forge.ui.distribution.client.event.CloseUnsubscriptionZoneEvent;
import org.novaforge.forge.ui.distribution.client.event.CloseUnsubscriptionZoneEvent.CloseUnsubscriptionZoneEventHandler;
import org.novaforge.forge.ui.distribution.client.event.NoDistributionPanelEvent;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.view.mother.MotherView;
import org.novaforge.forge.ui.distribution.client.view.mother.UnsubscriptionReasonViewImpl;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;

/**
 * @author BILET-JC
 */
public class MotherPresenter implements Presenter
{

	private final MotherView					display;
	private ForgeDTO								mother;
	private UnsubscriptionReasonPresenter	unsubscriptionMotherPresenter;

	public MotherPresenter(final MotherView pDisplay)
	{
		display = pDisplay;
		getMotherForge();
		bind();
	}

	private void getMotherForge()
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
				DistributionEntryPoint.getEventBus().fireEvent(new NoDistributionPanelEvent(Common.MESSAGES
																																												.noDistributionText()));
			}

			@Override
			public void onSuccess(final ForgeDTO pResult)
			{
				display.getFormZone().clear();
				if (pResult != null)
				{
					if (pResult.getParent() != null)
					{
						mother = pResult.getParent();
						display.displayMotherAffiliation(mother, pResult.getAffiliationDate().toString());
					}
				}
				else
				{
					DistributionEntryPoint.getEventBus().fireEvent(
							new NoDistributionPanelEvent(Common.MESSAGES.noReferenceText()));
				}
			}

		}.retry(0);
	}

	public void bind()
	{

		// open unsubscription panel
		display.getUnsubscriptionMotherButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				unsubscriptionMotherPresenter = new UnsubscriptionReasonPresenter(new UnsubscriptionReasonViewImpl(Common.MESSAGES
																																																							 .unsubscriptionRaison()),
																																					mother);
				unsubscriptionMotherPresenter.go(display.getFormZone());
			}
		});
		// close unsubscription panel
		DistributionEntryPoint.getEventBus().addHandler(CloseUnsubscriptionZoneEvent.TYPE,
																										new CloseUnsubscriptionZoneEventHandler()
																										{
																											@Override
																											public void closeSubscriptionZone(final CloseUnsubscriptionZoneEvent event)
																											{
																												display.getFormZone().clear();
																											}
																										});
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
		getMotherForge();

	}

	public void disableUnsubscription()
	{
		display.getFormZone().clear();
		display.hideButton();

	}

}
