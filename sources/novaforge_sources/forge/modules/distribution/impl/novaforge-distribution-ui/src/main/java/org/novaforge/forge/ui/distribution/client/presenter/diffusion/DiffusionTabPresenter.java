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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.DistributionEntryPoint;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.distribution.client.view.diffusion.DiffusionTabView;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.ComponentToSyncEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author rols-p
 */
public class DiffusionTabPresenter implements Presenter
{

	private final DiffusionTabView			display;
	private final Set<ComponentToSyncEnum>	componentToSyncList;
	private List<TargetForgeDTO> targetForgesList;

	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 */
	public DiffusionTabPresenter(final DiffusionTabView display)
	{
		super();
		this.display = display;
		componentToSyncList = new HashSet<ComponentToSyncEnum>();
		bind();
	}

	private void bind()
	{
		display.getDiffuseButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				diffuse();
				setEnabledButton();
			}
		});

		display.getProjetRefCheckButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (display.getProjetRefCheckButton().getValue())
				{
					componentToSyncList.add(ComponentToSyncEnum.PROJ_REF);
				}
				else
				{
					componentToSyncList.remove(ComponentToSyncEnum.PROJ_REF);
				}
				setEnabledButton();
			}
		});
		display.getTemplateCheckButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (display.getTemplateCheckButton().getValue())
				{
					componentToSyncList.add(ComponentToSyncEnum.TEMPLATE);
				}
				else
				{
					componentToSyncList.remove(ComponentToSyncEnum.TEMPLATE);
				}
				setEnabledButton();
			}
		});
		display.getToolsCheckButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (display.getToolsCheckButton().getValue())
				{
					componentToSyncList.add(ComponentToSyncEnum.TOOLS);
				}
				else
				{
					componentToSyncList.remove(ComponentToSyncEnum.TOOLS);
				}
				setEnabledButton();
			}
		});
		display.getIndicatorCheckButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (display.getIndicatorCheckButton().getValue())
				{
					componentToSyncList.add(ComponentToSyncEnum.INDICATOR);
				}
				else
				{
					componentToSyncList.remove(ComponentToSyncEnum.INDICATOR);
				}
				setEnabledButton();
			}
		});
	}

	private void diffuse()
	{
		new AbstractDistributionRPCCall<Void>()
		{
			@Override
			protected void callService(final AsyncCallback<Void> callback)
			{
				DistributionEntryPoint.getServiceAsync().propagate(componentToSyncList, callback);
				final InfoDialogBox info = new InfoDialogBox(Common.MESSAGES.validateSyncImmediate(),
						InfoTypeEnum.SUCCESS);
				info.show();
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				// move dialogbox . trace applicatif journal
			}

			@Override
			public void onSuccess(final Void result)
			{
				// move dialogbox . trace applicatif journal
			}

		}.retry(0);

	}

	private void setEnabledButton()
	{
		if (!componentToSyncList.isEmpty())
		{
			display.getDiffuseButton().setEnabled(true);
		}
		else
		{
			display.getDiffuseButton().setEnabled(false);
		}
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
		loadTargetForges();
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
						display.getDiffuseButton().setEnabled(false);
						display.setEnabled(false);
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
