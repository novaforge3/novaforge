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

package org.novaforge.forge.ui.distribution.client.presenter.diffusion;

import com.google.gwt.core.client.GWT;
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
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.distribution.client.properties.DiffusionMessage;
import org.novaforge.forge.ui.distribution.client.view.diffusion.DiffusionDifferedTabView;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchDifferedDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rols-p
 */
public class DiffusionDifferedTabPresenter implements Presenter
{

	private final DiffusionMessage			diffusionMessages	= (DiffusionMessage) GWT
																						.create(DiffusionMessage.class);
	private final DiffusionDifferedTabView	display;
	private final SynchDifferedDTO			synchDifferedDTO	= new SynchDifferedDTO();
	private List<TargetForgeDTO> targetForgesList;

	/**
	 * @param rpcService
	 * @param eventBus
	 * @param diffusionDifferedTabPresenter
	 */
	public DiffusionDifferedTabPresenter(final DiffusionDifferedTabView diffusionDifferedTabPresenter)
	{
		super();
		display = diffusionDifferedTabPresenter;
		bind();
	}

	private void bind()
	{
		display.getActiveCheckButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (display.getActiveCheckButton().getValue())
				{
					display.getTime().setEnable(true);
					display.getPeriod().setEnable(true);
					display.getSaveButton().setEnabled(true);
				}
				else
				{
					display.getTime().setEnable(false);
					display.getPeriod().setEnable(false);
				}
			}
		});

		display.getSaveButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (display.getActiveCheckButton().getValue())
				{
					if (display.getTime().isValid() && display.getPeriod().isValid())
					{
						final String[] time = display.getTime().getValue().split(":");
						synchDifferedDTO.setActive(display.getActiveCheckButton().getValue());
						synchDifferedDTO.setHours(time[0]);
						synchDifferedDTO.setMinutes(time[1]);
						synchDifferedDTO.setPeriod(display.getPeriod().getValue());

						display.getPropagatedDialogBox().show();
					}
					else
					{
						display.getTime().isValid();
						display.getPeriod().isValid();
						final InfoDialogBox info = new InfoDialogBox(Common.MESSAGES.eValidation(),
								InfoTypeEnum.ERROR);
						info.show();
					}
				}
				else
				// the sync is disabled
				{
					synchDifferedDTO.setActive(display.getActiveCheckButton().getValue());

					display.getPropagatedDialogBox().show();
				}
			}
		});

		display.getPropagatedDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getPropagatedDialogBox().hide();
				DiffusionDifferedTabPresenter.this.configureScheduling(synchDifferedDTO, true);
			}
		});

		display.getPropagatedDialogBox().getClose().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getPropagatedDialogBox().hide();
				DiffusionDifferedTabPresenter.this.configureScheduling(synchDifferedDTO, false);
			}
		});

		display.getPropagatedDialogBox().getCancel().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getPropagatedDialogBox().hide();
			}
		});

		display.getTime().setValidator(getValidTimeTextValidator());
		display.getPeriod().setValidator(getPeriodValidTextValidator());

	}

	private void configureScheduling(final SynchDifferedDTO pSynchDifferedDTO, final boolean canPropagate)
	{
		new AbstractDistributionRPCCall<Void>()
		{
			@Override
			protected void callService(final AsyncCallback<Void> callback)
			{
				if (pSynchDifferedDTO.isActive())
				{
					DistributionEntryPoint.getServiceAsync().configureScheduling(pSynchDifferedDTO, canPropagate,
							callback);
				}
				else
				{
					DistributionEntryPoint.getServiceAsync().disableScheduling(canPropagate, callback);
				}
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

			@Override
			public void onSuccess(final Void pVoid)
			{
				final InfoDialogBox info = new InfoDialogBox(Common.MESSAGES.validateSyncDiff(),
						InfoTypeEnum.SUCCESS);
				info.show();
			}


		}.retry(0);

	}

	private Validator getValidTimeTextValidator()
	{
		// Validator for needed fields
		return new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				return (display.getActiveCheckButton().getValue()) && (pValue.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"));
			}

			@Override
			public String getErrorMessage()
			{
				return diffusionMessages.timeValidation();
			}
		};
	}

	private Validator getPeriodValidTextValidator()
	{// Validator for needed fields
		return new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				boolean returned;
				if (pValue.matches("0*"))
				{
					returned = false;
				}
				else
				{
					returned = pValue.matches("([01]?[0-9]|2[0-4])");
				}
				return returned;
			}

			@Override
			public String getErrorMessage()
			{
				return diffusionMessages.periodValidation();
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

	public void refreshPresenter()
	{
		loadConfigDiffusion();
		loadTargetForges();
	}

	/**
	 * This method call a service to invalidate a project with a reason. Retrieve the period of diffusion
	 *
	 * @param reason
	 */
	private void loadConfigDiffusion()
	{
		new AbstractDistributionRPCCall<SynchDifferedDTO>()
		{
			@Override
			protected void callService(final AsyncCallback<SynchDifferedDTO> callback)
			{
				DistributionEntryPoint.getServiceAsync().loadSchedulingConfig(callback);
			}

			@Override
			public void onSuccess(final SynchDifferedDTO pResult)
			{
				display.getTime().clear();
				display.getPeriod().clear();
				if (pResult != null)
				{
					final String time = pResult.getHours() + ":" + pResult.getMinutes();
					display.getTime().setValue(time);
					display.getActiveCheckButton().setValue(pResult.isActive());
					display.getPeriod().setValue(String.valueOf(pResult.getPeriod()));
					if (pResult.isActive())
					{
						display.getTime().setEnable(true);
						display.getPeriod().setEnable(true);
						display.getActiveCheckButton().setEnabled(true);
						display.getSaveButton().setEnabled(true);
					}
					else
					{
						display.enableAllField();
					}
				}
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}
		}.retry(0);

	}

	/**
	 * This method call a service to invalidate a project with a reason.
	 *
	 * @param reason
	 */
	private void loadTargetForges()
	{
		targetForgesList = new ArrayList<TargetForgeDTO>();
		new AbstractDistributionRPCCall<List<TargetForgeDTO>>()
		{
			@Override
			protected void callService(final AsyncCallback<List<TargetForgeDTO>> callback)
			{
				DistributionEntryPoint.getServiceAsync().getTargetForges(callback);
			}

			@Override
			public void onSuccess(final List<TargetForgeDTO> pResult)
			{
				if (pResult != null)
				{
					targetForgesList.clear();
					targetForgesList.addAll(pResult);
					display.getTargetForgesDataProvider().setList(targetForgesList);
					if (display.getTargetForges().getRowCount() < 1)
					{
						display.disableAllField();
					}

				}
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

		}.retry(0);
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
