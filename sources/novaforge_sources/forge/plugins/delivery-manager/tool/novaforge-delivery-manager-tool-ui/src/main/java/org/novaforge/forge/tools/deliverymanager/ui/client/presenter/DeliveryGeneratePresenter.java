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
package org.novaforge.forge.tools.deliverymanager.ui.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.EditDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.GenerateDeliveryEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.ShowDeliveriesEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.TabPresenterSource;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.DeliveryGenerateView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentTypeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryDTO;

/**
 * @author CASERY-J
 */
public class DeliveryGeneratePresenter implements Presenter
{

	private final DeliveryGenerateView	display;
	private DeliveryDTO						delivery;

	/**
	 * Create a new DeliveryGeneratePresenter instance
	 * 
	 * @param {@link DeliveryGenerateView} the display to link
	 */
	public DeliveryGeneratePresenter(final DeliveryGenerateView display)
	{
		super();
		this.display = display;

		bind();
	}

	public void bind()
	{
		DeliveryGeneratePresenter.this.display.getReturnButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent pEvent)
			{
				DeliveryManagement.get().getEventBus().fireEvent(new ShowDeliveriesEvent());
			}
		});

		DeliveryGeneratePresenter.this.display.getButtonEditDelivery().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent pEvent)
			{
				DeliveryManagement.get().getEventBus()
						.fireEvent(new EditDeliveryEvent(delivery.getReference(), TabPresenterSource.GENERATE));
			}
		});

		DeliveryGeneratePresenter.this.display.getButtonGenerateDelivery().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent pEvent)
			{
				DeliveryGeneratePresenter.this.generateDelivery(delivery.getReference());
			}
		});

	}

	private void generateDelivery(final String pReference)
	{
		new AbstractRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				DeliveryManagement.get().getServiceAsync().generateDeliveryStatus(DeliveryManagement.get().getProjectId(),
																																					pReference, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				DeliveryManagement.get().getEventBus().fireEvent(new ShowDeliveriesEvent());
				DeliveryGeneratePresenter.this.generateDeliveryContent(pReference);
			}

			@Override
			public void onFailure(final Throwable caught)
			{
				DeliveryManagement.get().getEventBus()
						.fireEvent(new GenerateDeliveryEvent(delivery.getReference(), false));
			}
		}.retry(0);
	}

	private void generateDeliveryContent(final String pReference)
	{
		new AbstractRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				DeliveryManagement.get().getServiceAsync().generateDelivery(DeliveryManagement.get().getProjectId(), pReference,
																																		pCb);
			}

			@Override
			public void onFailure(final Throwable caught)
			{
				DeliveryManagement.get().getEventBus()
						.fireEvent(new GenerateDeliveryEvent(delivery.getReference(), false));
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				DeliveryManagement.get().getEventBus().fireEvent(new GenerateDeliveryEvent(delivery.getReference(), pResult));
			}

		}.retry(0);
	}

	public void refreshView(final String pDeliveryReference)
	{
		getDelivery(pDeliveryReference);
	}

	private void getDelivery(final String pReference)
	{
		new AbstractRPCCall<DeliveryDTO>()
		{
			@Override
			protected void callService(final AsyncCallback<DeliveryDTO> pCb)
			{
				DeliveryManagement.get().getServiceAsync()
						.getDelivery(DeliveryManagement.get().getProjectId(), pReference, pCb);
			}

			@Override
			public void onSuccess(final DeliveryDTO pResult)
			{
				if (pResult != null)
				{
					delivery = pResult;
					DeliveryGeneratePresenter.this.initDeliveryDetailsValue();
					DeliveryGeneratePresenter.this.initDeliveryContentValue();
				}
			}

			@Override
			public void onFailure(final Throwable caught)
			{

				DeliveryCommon.displayErrorMessage(caught);
			}
		}.retry(0);
	}

	/**
	 * Fill delivery details informations, i.e. name, reference, type and version
	 */
	private void initDeliveryDetailsValue()
	{
		setDeliveryDetailsPanelLoading(true);
		display.getDeliveryNameValue().setText(delivery.getName());
		display.getDeliveryReferenceValue().setText(delivery.getReference());
		display.getDeliveryTypeValue().setText(delivery.getType());
		display.getDeliveryVersionValue().setText(delivery.getVersion());
		setDeliveryDetailsPanelLoading(false);
	}

	/**
	 * Fill delivery content informations
	 */
	private void initDeliveryContentValue()
	{
		setDeliveryContentPanelLoading(true);
		initDeliveryContentECM();
		initDeliveryContentSCM();
		initDeliveryContentFILES();
		initDeliveryContentBUGS();
		initDeliveryContentNOTE();
		setDeliveryContentPanelLoading(false);
	}

	private void setDeliveryDetailsPanelLoading(final boolean pValue)
	{
		display.getDeliveryDetailsPanel().setVisible(!pValue);
		display.getDeliveryDetailsLoadingPanel().setVisible(pValue);
	}

	private void setDeliveryContentPanelLoading(final boolean pValue)
	{
		display.getDeliveryContentPanel().setVisible(!pValue);
		display.getDeliveryContentLoadingPanel().setVisible(pValue);
	}

	/**
	 * Fill delivery ECM content informations
	 */
	private void initDeliveryContentECM()
	{
		if (delivery.getContents().containsKey(ContentTypeDTO.ECM))
		{
			display.getContentECMInfosValue().setText(
					DeliveryCommon.getMessages().generateContentECMInfo(
							delivery.getContents().get(ContentTypeDTO.ECM)));
			display.getEcmPanel().setVisible(true);
		}
		else
		{
			display.getEcmPanel().setVisible(false);
		}
	}

	private void initDeliveryContentSCM()
	{
		if (delivery.getContents().containsKey(ContentTypeDTO.SCM))
		{
			display.getContentSCMInfosValue().setText(
					DeliveryCommon.getMessages().generateContentSCMInfo(
							delivery.getContents().get(ContentTypeDTO.SCM)));
			display.getScmPanel().setVisible(true);
		}
		else
		{
			display.getScmPanel().setVisible(false);
		}
	}

	private void initDeliveryContentFILES()
	{
		if (delivery.getContents().containsKey(ContentTypeDTO.FILE))
		{
			display.getContentFilesInfosValue().setText(
					DeliveryCommon.getMessages().generateContentFilesInfo(
							delivery.getContents().get(ContentTypeDTO.FILE)));
			display.getFilesPanel().setVisible(true);
		}
		else
		{
			display.getFilesPanel().setVisible(false);
		}
	}

	private void initDeliveryContentBUGS()
	{
		if (delivery.getContents().containsKey(ContentTypeDTO.BUG))
		{
			display.getContentBugsInfosValue().setText(
					DeliveryCommon.getMessages().generateContentBugsInfo(
							delivery.getContents().get(ContentTypeDTO.BUG)));
			display.getBugsPanel().setVisible(true);
		}
		else
		{
			display.getBugsPanel().setVisible(false);
		}
	}

	private void initDeliveryContentNOTE()
	{
		if (delivery.getContents().containsKey(ContentTypeDTO.NOTE))
		{
			display.getContentNoteInfosValue().setText(
					DeliveryCommon.getMessages().generateContentNoteInfo(
							delivery.getContents().get(ContentTypeDTO.NOTE)));
			display.getNotePanel().setVisible(true);
		}
		else
		{
			display.getNotePanel().setVisible(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void go(final HasWidgets pContainer)
	{
		pContainer.clear();
		pContainer.add(display.asWidget());

	}

}
