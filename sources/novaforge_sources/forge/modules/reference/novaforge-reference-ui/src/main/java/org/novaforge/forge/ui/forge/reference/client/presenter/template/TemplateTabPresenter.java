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
package org.novaforge.forge.ui.forge.reference.client.presenter.template;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.template.CancelCreateTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.CancelEditTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.CreateTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.DeleteTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.DisableTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.EditTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.EnableTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.SaveCreateTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.SaveEditTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateManageViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateTabView;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplatesListViewImpl;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;

/**
 * @author lamirang
 */
public class TemplateTabPresenter implements Presenter
{

	private final TemplatesListPresenter  templateListPresenter;
	private final TemplateManagePresenter templateEditPresenter;
	private final TemplateManagePresenter templateCreatePresenter;
	private final TemplateTabView         display;
	private boolean                       isModify = false;

	public TemplateTabPresenter(final TemplateTabView display)
	{
		super();
		this.display = display;

		templateCreatePresenter = new TemplateManagePresenter(new TemplateManageViewImpl(
		    ManagePresenterType.CREATE), ManagePresenterType.CREATE);

		templateEditPresenter = new TemplateManagePresenter(
		    new TemplateManageViewImpl(ManagePresenterType.UPDATE), ManagePresenterType.UPDATE);

		templateListPresenter = new TemplatesListPresenter(new TemplatesListViewImpl());

		bind();
	}

	public void bind()
	{
		ForgeReferenceEntryPoint.getEventBus().addHandler(CreateTemplateEvent.TYPE,
		    new CreateTemplateEvent.Handler()
		    {

			    @Override
			    public void createTemplate(final CreateTemplateEvent event)
			    {

						new AbstractReferenceRPCCall<TemplateDTO>()
						{
					    @Override
					    protected void callService(final AsyncCallback<TemplateDTO> pCb)
					    {
						    ForgeReferenceEntryPoint.getServiceAsync().newTemplate(pCb);
					    }

					    @Override
							public void onFailure(final Throwable caught)
							{
								isModify = false;
								ErrorManagement.displayErrorMessage(caught);
							}

							@Override
							public void onSuccess(final TemplateDTO pResult)
					    {
						    isModify = true;
						    if (pResult != null)
						    {
							    templateCreatePresenter.setCurrentTemplate(pResult);
						    }
						    else
						    {
							    templateCreatePresenter.setCurrentTemplate(new TemplateDTO());
						    }
						    templateCreatePresenter.go(display.getTemplatePanel());

					    }



				    }.retry(0);

			    }
		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(EditTemplateEvent.TYPE, new EditTemplateEvent.Handler()
		{
			@Override
			public void editTemplate(final EditTemplateEvent pEvent)
			{
				isModify = true;
				templateEditPresenter.setCurrentTemplate(pEvent.getTemplateDTO());
				templateEditPresenter.go(display.getTemplatePanel());
			}
		});

		ForgeReferenceEntryPoint.getEventBus().addHandler(SaveCreateTemplateEvent.TYPE,
		    new SaveCreateTemplateEvent.Handler()
		    {
			    @Override
			    public void saveCreateTemplate(final SaveCreateTemplateEvent pEvent)
			    {
				    isModify = false;
				    TemplateTabPresenter.this.createTemplate(pEvent.getTemplateDTO());
			    }
		    });

		ForgeReferenceEntryPoint.getEventBus().addHandler(CancelCreateTemplateEvent.TYPE,
		    new CancelCreateTemplateEvent.Handler()
		    {
			    @Override
			    public void cancelCreateTemplate(final CancelCreateTemplateEvent pEvent)
			    {
				    isModify = false;
				    TemplateTabPresenter.this.refreshTemplateTab(false, null);
			    }
		    });

		ForgeReferenceEntryPoint.getEventBus().addHandler(SaveEditTemplateEvent.TYPE,
		    new SaveEditTemplateEvent.Handler()
		    {
			    @Override
			    public void saveUpdateTemplate(final SaveEditTemplateEvent pEvent)
			    {
				    isModify = false;
				    TemplateTabPresenter.this.updateTemplate(pEvent.getTemplateDTO());
			    }
		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(CancelEditTemplateEvent.TYPE,
		    new CancelEditTemplateEvent.Handler()
		    {
			    @Override
			    public void cancelEditTemplate(final CancelEditTemplateEvent pEvent)
			    {
				    isModify = false;
				    TemplateTabPresenter.this.refreshTemplateTab(false, null);
			    }
		    });

		ForgeReferenceEntryPoint.getEventBus().addHandler(DeleteTemplateEvent.TYPE,
		    new DeleteTemplateEvent.Handler()
		    {
			    @Override
			    public void deleteTemplate(final DeleteTemplateEvent pEvent)
			    {
				    TemplateTabPresenter.this.deleteTemplate(pEvent.getTemplateId());
			    }
		    });

		ForgeReferenceEntryPoint.getEventBus().addHandler(DisableTemplateEvent.TYPE,
		    new DisableTemplateEvent.Handler()
		    {
			    @Override
			    public void disableTemplate(final DisableTemplateEvent pEvent)
			    {
				    TemplateTabPresenter.this.disableTemplate(pEvent.getTemplateId());
			    }
		    });
		ForgeReferenceEntryPoint.getEventBus().addHandler(EnableTemplateEvent.TYPE,
		    new EnableTemplateEvent.Handler()
		    {
			    @Override
			    public void enableTemplate(final EnableTemplateEvent pEvent)
			    {
				    TemplateTabPresenter.this.enableTemplate(pEvent.getTemplateId());
			    }
		    });
	}

	public void createTemplate(final TemplateDTO pTemplate)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().createTemplate(pTemplate, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				if (pResult)
				{
					refreshTemplateTab(true, pTemplate.getName());
				}

			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	public void refreshTemplateTab(final boolean pSelectNew, final String pSelectKey)
	{
		TemplateTabPresenter.this.templateListPresenter.go(TemplateTabPresenter.this.display.getTemplatePanel());
		TemplateTabPresenter.this.templateListPresenter.refreshTemplateList(pSelectNew, pSelectKey);

	}

	private void updateTemplate(final TemplateDTO pTemplate)
	{

		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().updateTemplate(pTemplate, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				if (pResult)
				{
					refreshTemplateTab(true, pTemplate.getName());
				}

			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);
	}

	private void deleteTemplate(final String pTemplateId)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().deleteTemplate(pTemplateId, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				if (pResult)
				{
					refreshTemplateTab(true, null);
				}

			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void disableTemplate(final String pTemplateId)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().disableTemplate(pTemplateId, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				if (pResult)
				{
					refreshTemplateTab(false, null);
				}
			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void enableTemplate(final String pTemplateId)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().enableTemplate(pTemplateId, pCb);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				if (pResult)
				{
					refreshTemplateTab(false, null);
				}
			}

			@Override
			public void onFailure(final Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
	}

	public TemplateTabView getDisplay()
	{
		return display;
	}

	/**
	 * @return the isModify
	 */
	public boolean isModify()
	{
		return isModify;
	}

	/**
	 * @param isModify
	 *          the isModify to set
	 */
	public void setModify(final boolean isModify)
	{
		this.isModify = isModify;
	}
}
