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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.validation.Validator;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.DistributionEntryPoint;
import org.novaforge.forge.ui.distribution.client.event.CloseUnsubscriptionZoneEvent;
import org.novaforge.forge.ui.distribution.client.event.OpenTabEvent;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.view.mother.UnsubscriptionReasonView;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeRequestDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;

/**
 * @author BILET-JC
 */
public class UnsubscriptionReasonPresenter implements Presenter
{
	private final UnsubscriptionReasonView	display;
	private final ForgeDTO						mother;

	/**
	 * @param pRpcService
	 * @param pEventBus
	 * @param pDisplay
	 */
	public UnsubscriptionReasonPresenter(final UnsubscriptionReasonView pDisplay, final ForgeDTO pMother)
	{
		super();
		display = pDisplay;
		mother = pMother;
		display.getUnsubscriptionReasonTA().ensureDebugId("unsubscriptionReasonId-" + mother.getForgeId());
		bind();
	}

	public void bind()
	{
		// cancel unsubscription button
		display.getCancelUnsubscriptionReasonB().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				DistributionEntryPoint.getEventBus().fireEvent(new CloseUnsubscriptionZoneEvent());
			}
		});
		// confirm subscription button
		display.getCreateUnsubscriptionReasonB().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				final ForgeRequestDTO unsubscriptionMother = new ForgeRequestDTO();
				unsubscriptionMother.setReason(display.getUnsubscriptionReasonTA().getValue());
				unsubscriptionMother.setDestinationForgeId(mother.getForgeId());
				unsubscriptionMother.setType(TypeDistributionRequestEnum.UNSUBSCRIPTION);
				unsubscription(unsubscriptionMother);
			}
		});
		// reason conditions
		display.getUnsubscriptionReasonTA().setValidator(getValidReasonTextValidator());
	}

	private void unsubscription(final ForgeRequestDTO pUnsubscriptionMother)
	{
		new AbstractDistributionRPCCall<Void>()
		{

			@Override
			protected void callService(final AsyncCallback<Void> cb)
			{
				DistributionEntryPoint.getServiceAsync().createSubscription(pUnsubscriptionMother, cb);
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				Common.displayErrorMessage(pCaught);
			}

			@Override
			public void onSuccess(final Void arg0)
			{
				final InfoDialogBox info = new InfoDialogBox(Common.MESSAGES.unsubscriptionSuccessful(),
						InfoTypeEnum.SUCCESS);
				info.show();
				DistributionEntryPoint.getEventBus().fireEvent(new OpenTabEvent(0));
			}
		}.retry(0);
	}

	private Validator getValidReasonTextValidator()
	{
		// Validator for needed fields
		return new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				boolean ret = false;
				if (pValue.length() < 256)
				{
					ret = true;
				}
				return ret;
			}

			@Override
			public String getErrorMessage()
			{
				return Common.MESSAGES.eMaxLenght();
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());

	}

	public String getReason()
	{
		return display.getUnsubscriptionReasonTA().getValue();
	}

	public HasClickHandlers getCreateInvalidationReasonB()
	{
		return display.getCreateUnsubscriptionReasonB();

	}
}
