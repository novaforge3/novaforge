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

package org.novaforge.forge.ui.distribution.client.presenter.daughter;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.DistributionEntryPoint;
import org.novaforge.forge.ui.distribution.client.event.CloseInvalidationZoneEvent;
import org.novaforge.forge.ui.distribution.client.event.CloseInvalidationZoneEvent.CloseInvalidationZoneEventHandler;
import org.novaforge.forge.ui.distribution.client.event.OpenInvalidationZoneEvent;
import org.novaforge.forge.ui.distribution.client.event.OpenInvalidationZoneEvent.OpenInvalidationZoneEventHandler;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.view.daughters.DistributionManagementView;
import org.novaforge.forge.ui.distribution.client.view.daughters.InvalidationReasonViewImpl;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeRequestDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeForgeRequestEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BILET-JC
 */
public class DistributionManagementPresenter implements Presenter
{

	private final DistributionManagementView display;
	private ForgeDTO									currentForge;
	private List<ForgeRequestDTO>					subscriptionList;

	public DistributionManagementPresenter(final DistributionManagementView display)
	{
		super();
		this.display = display;

		getCurrentForge();
		createInvalidateColumn();
		bind();
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());

	}

	public void bind()
	{
		// open invalidation panel
		DistributionEntryPoint.getEventBus().addHandler(OpenInvalidationZoneEvent.TYPE,
				new OpenInvalidationZoneEventHandler()
				{

					@Override
					public void openInvalidationZone(final OpenInvalidationZoneEvent event)
					{
						final InvalidationReasonPresenter invalidationReasonPresenter = new InvalidationReasonPresenter(
								DistributionEntryPoint.getServiceAsync(), DistributionEntryPoint.getEventBus(),
								new InvalidationReasonViewImpl(Common.MESSAGES.invalidationMessage()), event
										.getForgeRequest());
						invalidationReasonPresenter.go(display.getFormZone());
					}
				});
		// close invalidation panel
		DistributionEntryPoint.getEventBus().addHandler(CloseInvalidationZoneEvent.TYPE,
				new CloseInvalidationZoneEventHandler()
				{
					@Override
					public void closeInvalidationZone(final CloseInvalidationZoneEvent event)
					{
						display.getFormZone().clear();
						DistributionManagementPresenter.this.updateSubscriptionList();
					}
				});
		// confirm validation button
		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();
				DistributionManagementPresenter.this.approve(display.getValidateDialogBox().getType(), display
						.getValidateDialogBox().getObjectId());
			}
		});

	}

	private void createInvalidateColumn()
	{
		// Invalidation column
		final SafeHtmlBuilder invalidationSHB = new SafeHtmlBuilder();
		invalidationSHB.appendHtmlConstant("<img src='" + Common.RESOURCE.ko().getURL() + "' width='"
																					 + Common.IMG_BUTTON_SIZE.toString() + "px' height='" + Common.IMG_BUTTON_SIZE
																																																			.toString()
																					 + "px' />");
		final Cell<ForgeRequestDTO> invalidationCell = new ActionCell<ForgeRequestDTO>(
				invalidationSHB.toSafeHtml(), new ActionCell.Delegate<ForgeRequestDTO>()
				{
					@Override
					public void execute(final ForgeRequestDTO pObject)
					{
						DistributionEntryPoint.getEventBus().fireEvent(new OpenInvalidationZoneEvent(pObject));
					}
				});
		final Column<ForgeRequestDTO, ForgeRequestDTO> invalidationColumn = new Column<ForgeRequestDTO, ForgeRequestDTO>(
				invalidationCell)
		{
			@Override
			public ForgeRequestDTO getValue(final ForgeRequestDTO object)
			{
				return object;
			}
		};
		display.createInvalidationColumn(invalidationColumn);

	}

	/**
	 * Validate a subscription or unsubscription demand
	 *
	 * @param pType
	 * @param pForgeRequestId
	 */
	private void approve(final TypeDistributionRequestEnum pType, final String pForgeRequestId)
	{
		new AbstractDistributionRPCCall<ForgeDTO>()
		{

			@Override
			protected void callService(final AsyncCallback<ForgeDTO> cb)
			{
				DistributionEntryPoint.getServiceAsync().approveDistributionRequest(pType, pForgeRequestId, cb);
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				Common.displayErrorMessage(pCaught);
				DistributionManagementPresenter.this.updateSubscriptionList();
			}

			@Override
			public void onSuccess(final ForgeDTO pResult)
			{
				final InfoDialogBox info = new InfoDialogBox(Common.MESSAGES.validateSuccessful(),
						InfoTypeEnum.SUCCESS);
				info.show();
				if (pResult != null)
				{
					currentForge = pResult;
					DistributionManagementPresenter.this.updateDistributionList();
					DistributionManagementPresenter.this.updateSubscriptionList();
				}
			}


		}.retry(3);
	}

	public void refreshPresenter()
	{
		getCurrentForge();
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
				display.noDistributionPanel();
			}

			@Override
			public void onSuccess(final ForgeDTO pResult)
			{
				if (pResult != null)
				{
					currentForge = pResult;
					if (Common.LOCAL == currentForge.getForgeLevel())
					{
						display.noDaughter(true);
					}
					else
					{
						display.daughter();
						DistributionManagementPresenter.this.updateDistributionList();
						DistributionManagementPresenter.this.updateSubscriptionList();
					}
				}
				else
				{
					display.noDistributionPanel();
				}
			}

		}.retry(0);
	}

	/**
	 * Update the distribution list
	 */
	public void updateDistributionList()
	{
		final List<ForgeDTO> affiliations = new ArrayList<ForgeDTO>();
		for (final ForgeDTO children : currentForge.getChildren())
		{
			affiliations.add(children);
			if ((children.getChildren() != null) && (children.getChildren().size() != 0))
			{
				for (final ForgeDTO underChildren : children.getChildren())
				{
					affiliations.add(underChildren);
				}
			}
		}
		refreshDistributionList(affiliations);
	}

	/**
	 * Update the distribution request list
	 */
	private void updateSubscriptionList()
	{
		subscriptionList = new ArrayList<ForgeRequestDTO>();
		new AbstractDistributionRPCCall<List<ForgeRequestDTO>>()
		{
			@Override
			protected void callService(final AsyncCallback<List<ForgeRequestDTO>> pCb)
			{
				DistributionEntryPoint.getServiceAsync().getForgeRequestList(TypeForgeRequestEnum.DESTINATION,
						pCb);
			}

			@Override
			public void onSuccess(final List<ForgeRequestDTO> pResult)
			{
				subscriptionList.clear();
				if (pResult != null)
				{
					subscriptionList.addAll(pResult);
				}
				DistributionManagementPresenter.this.refreshSubscriptionList(subscriptionList);
				// if null, it can be the list which is empty and not necessary the distribution which is down
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				display.noDistributionPanel();
			}

		}.retry(0);
	}

	/**
	 * Refresh distribution list
	 * 
	 * @param pList
	 */
	private void refreshDistributionList(final List<ForgeDTO> pList)
	{
		display.getDistributionProvider().getList().clear();
		display.getDistributionProvider().setList(pList);
		display.updateDistributionSortHandler();
	}

	/**
	 * Refresh subscription list
	 * 
	 * @param pList
	 */
	private void refreshSubscriptionList(final List<ForgeRequestDTO> pList)
	{
		display.getSubscriptionProvider().setList(pList);
		display.updateSubscriptionSortHandler();
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
