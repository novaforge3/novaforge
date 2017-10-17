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
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.validation.Validator;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.DistributionEntryPoint;
import org.novaforge.forge.ui.distribution.client.event.NoDistributionPanelEvent;
import org.novaforge.forge.ui.distribution.client.event.OpenTabEvent;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.view.mother.SubscriptionView;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeRequestDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.MotherActionEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;

import java.util.List;

/**
 * @author BILET-JC
 */
public class SubscriptionPresenter implements Presenter
{

	private final SubscriptionView	display;
	private final MotherActionEnum	actionMotherAffiliation;

	/**
	 * @param display
	 * @param rpcService
	 * @param eventBus
	 */
	public SubscriptionPresenter(final SubscriptionView display,
			final MotherActionEnum pActionMotherAffiliation)
	{
		super();
		this.display = display;
		actionMotherAffiliation = pActionMotherAffiliation;

		if (actionMotherAffiliation != MotherActionEnum.NONE)
		{
			motherAction();
			bind();
		}
	}

	private void motherAction()
	{
		display.getMother().clear();
		if (actionMotherAffiliation.equals(MotherActionEnum.DO))
		{
			getMotherList();

		}
		// else if (actionMotherAffiliation.equals(MotherActionEnum.DOING))
		// {
		// display.getMother().setEnabled(false);
		// display.getReasonTB().setEnable(false);
		// }
	}

	public void bind()
	{

		display.getCreateAffiliationDemandButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				if (display.getReasonTB().isValid())
				{
					display.getValidateDialogBox().show();
				}
				else
				{
					final InfoDialogBox info = new InfoDialogBox(Common.MESSAGES.eValidation(), InfoTypeEnum.ERROR);
					info.show();
				}
			}
		});
		display.getCancelAffiliationDemandButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				DistributionEntryPoint.getEventBus().fireEvent(new OpenTabEvent(0));
			}
		});
		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();

				final ForgeRequestDTO subscription = new ForgeRequestDTO();
				subscription.setDestinationForgeId(display.getMother().getValue(
						display.getMother().getSelectedIndex()));
				subscription.setReason(display.getReasonTB().getValue().trim());
				subscription.setType(TypeDistributionRequestEnum.SUBSCRIPTION);

				SubscriptionPresenter.this.subscription(subscription);
			}
		});
		display.getReasonTB().setValidator(getReasonValidator());

	}

	private void getMotherList()
	{
		new AbstractDistributionRPCCall<List<ForgeDTO>>()
		{

			@Override
			protected void callService(final AsyncCallback<List<ForgeDTO>> cb)
			{
				DistributionEntryPoint.getServiceAsync().getMotherList(cb);
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				DistributionEntryPoint.getEventBus().fireEvent(new NoDistributionPanelEvent(Common.MESSAGES
																																												.noDistributionText()));
			}

			@Override
			public void onSuccess(final List<ForgeDTO> pMothers)
			{
				if ((pMothers != null) && (pMothers.size() != 0))
				{
					for (final ForgeDTO mother : pMothers)
					{
						display.getMother().addItem(mother.getLabel(), mother.getForgeId());
					}
				}
				// if null, it can be the list which is empty and not necessary the distribution which is down
				else
				{
					DistributionEntryPoint.getEventBus().fireEvent(
							new NoDistributionPanelEvent(Common.MESSAGES.noDistributionText()));
				}
			}


		}.retry(0);

	}

	private void subscription(final ForgeRequestDTO pSubscriptionDTO)
	{
		new AbstractDistributionRPCCall<Void>()
		{

			@Override
			protected void callService(final AsyncCallback<Void> cb)
			{
				DistributionEntryPoint.getServiceAsync().createSubscription(pSubscriptionDTO, cb);
			}

			@Override
			public void onSuccess(final Void arg0)
			{
				final InfoDialogBox info = new InfoDialogBox(Common.MESSAGES.subscriptionSuccessful(),
						InfoTypeEnum.SUCCESS);
				info.show();

				DistributionEntryPoint.getEventBus().fireEvent(new OpenTabEvent(0));
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				Common.displayErrorMessage(pCaught);
			}
		}.retry(0);
	}

	private Validator getReasonValidator()
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

	public void setMotherMessage(final String pMessage)
	{
		display.setMotherMessage(pMessage);
	}

	public void refreshPresenter()
	{
		motherAction();
		display.getReasonTB().setValue(Common.EMPTY_TEXT);

	}

	public void displayMother(final String destinationForgeId, final String destinationForgeLabel,
			final String reason)
	{
		display.getMother().addItem(destinationForgeLabel, destinationForgeId);
		display.getReasonTB().setValue(reason);

	}
}
