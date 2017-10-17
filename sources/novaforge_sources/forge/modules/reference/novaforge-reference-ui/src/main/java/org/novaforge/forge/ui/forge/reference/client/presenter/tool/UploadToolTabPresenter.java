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
package org.novaforge.forge.ui.forge.reference.client.presenter.tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnChangeUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.validation.Validator;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.properties.UploadMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.client.view.tool.UploadToolTabView;
import org.novaforge.forge.ui.forge.reference.shared.tools.ToolProperties;

public class UploadToolTabPresenter implements Presenter
{
	private final UploadMessage      messages   = (UploadMessage) GWT.create(UploadMessage.class);
	private final ReferenceResources ressources = GWT.create(ReferenceResources.class);

	private final UploadToolTabView  display;
	private final String             servletPath;

	public UploadToolTabPresenter(final UploadToolTabView display)
	{
		super();
		this.display = display;
		bind();
		servletPath = display.getSingleUploader().getServletPath();

	}

	private void bind()
	{

		getDisplay().getSubmitFileButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				new AbstractReferenceRPCCall<Boolean>()
				{

					@Override
					protected void callService(final AsyncCallback<Boolean> pCb)
					{
						ForgeReferenceEntryPoint.getServiceAsync().existFile(
						    UploadToolTabPresenter.this.getDisplay().getFileNameValidator().getValue(),
						    UploadToolTabPresenter.this.getDisplay().getFileVersionValidator().getValue(),
						    UploadToolTabPresenter.this.getSuffix(UploadToolTabPresenter.this.getDisplay()
						        .getSingleUploader().getFileInput().getFilename()),
						    UploadToolTabPresenter.this.isSelectedAdminDirectory(), pCb);
					}

					@Override
					public void onFailure(final Throwable caught)
					{
						ErrorManagement.displayErrorMessage(caught);
					}

					@Override
					public void onSuccess(final Boolean pResult)
					{
						if (pResult)
						{
							display.getConfirmOverrideAndCopyDialogBox().show();
						}

					}


				}.retry(0);
			}

		});

		getDisplay().getSingleUploader().addOnChangeUploadHandler(new OnChangeUploaderHandler()
		{
			@Override
			public void onChange(final IUploader pUploader)
			{
				if (!(UploadToolTabPresenter.this.getDisplay().getFileNameValidator().isValid() && UploadToolTabPresenter.this
				    .getDisplay().getFileVersionValidator().isValid()))
				{
					final InfoDialogBox box = new InfoDialogBox(messages.emptyField(), InfoTypeEnum.ERROR);
					box.show();
					UploadToolTabPresenter.this.getDisplay().getSingleUploader().reset();
				}
				else
				{
					UploadToolTabPresenter.this.getDisplay().getSubmitFileButton().setEnabled(true);
				  UploadToolTabPresenter.this.getDisplay().getSingleUploader()
		        .setServletPath(UploadToolTabPresenter.this.getUploadRequest());

				}
			}
		});

		getDisplay().getSingleUploader().addOnStartUploadHandler(new OnStartUploaderHandler()
		{

			@Override
			public void onStart(final IUploader uploader)
			{
        
				UploadToolTabPresenter.this.getDisplay().getSingleUploader()
				    .setServletPath(UploadToolTabPresenter.this.getUploadRequest());
			}
		});

		getDisplay().getSingleUploader().addOnFinishUploadHandler(new OnFinishUploaderHandler()
		{

			@Override
			public void onFinish(final IUploader pUploader)
			{
        // Check that a file has been selected before printing success message
				if (IUploadStatus.Status.SUCCESS == pUploader.getStatus())
        //if (IUploadStatus.Status.SUCCESS == pUploader.getStatus() && pUploader.getFileInput().getFilename().isEmpty() == false )
        {
					final InfoDialogBox box = new InfoDialogBox(messages.receiveValid(), InfoTypeEnum.SUCCESS);
					box.addContentStyleName(ressources.style().important());
					box.show();
					UploadToolTabPresenter.this.refreshTab();
					UploadToolTabPresenter.this.getDisplay().getSingleUploader().reset();
					UploadToolTabPresenter.this.getDisplay().getSingleUploader().getFileInput().getWidget()
					    .removeStyleName(ressources.style().importantRed());
				}
			}
		});
		display.getConfirmOverrideAndCopyDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getConfirmOverrideAndCopyDialogBox().hide();
				UploadToolTabPresenter.this.getDisplay().getSubmitFileButton().setEnabled(true);

			}
		});

		display.getConfirmOverrideAndCopyDialogBox().getClose().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getConfirmOverrideAndCopyDialogBox().hide();
				UploadToolTabPresenter.this.getDisplay().getSubmitFileButton().setEnabled(false);
			}
		});

		display.getSendFromURLButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{

				if (!(UploadToolTabPresenter.this.getDisplay().getFileNameValidator().isValid() && UploadToolTabPresenter.this
				    .getDisplay().getFileVersionValidator().isValid()))
				{
					final InfoDialogBox box = new InfoDialogBox(messages.emptyField(), InfoTypeEnum.ERROR);
					box.show();

				}
				else if (UploadToolTabPresenter.this.getDisplay().getFilesFromUrl().getValue().isEmpty())
				{
					final InfoDialogBox box = new InfoDialogBox(messages.emptyUrlField(), InfoTypeEnum.ERROR);
					box.show();
				}
				else
				{
					new AbstractReferenceRPCCall<Boolean>()
					{
						@Override
						protected void callService(final AsyncCallback<Boolean> pCb)
						{
							ForgeReferenceEntryPoint.getServiceAsync().getExternalFile(
							    UploadToolTabPresenter.this.getDisplay().getFileNameValidator().getValue(),
							    UploadToolTabPresenter.this.getDisplay().getFileVersionValidator().getValue(),
							    UploadToolTabPresenter.this.getSuffix(UploadToolTabPresenter.this.getDisplay()
							        .getFilesFromUrl().getValue()), UploadToolTabPresenter.this.isSelectedAdminDirectory(),
							    UploadToolTabPresenter.this.getDisplay().getFilesFromUrl().getValue(), pCb);
						}

						@Override
						public void onSuccess(final Boolean pResult)
						{
							if (pResult)
							{
								final InfoDialogBox box = new InfoDialogBox(messages.receiveValid(), InfoTypeEnum.SUCCESS);
								box.addContentStyleName(ressources.style().important());
								box.show();
								UploadToolTabPresenter.this.refreshTab();
								UploadToolTabPresenter.this.getDisplay().getSingleUploader().reset();
								UploadToolTabPresenter.this.getDisplay().getSingleUploader().getFileInput().getWidget()
								    .removeStyleName(ressources.style().importantRed());
							}
							else
							{
								final InfoDialogBox box = new InfoDialogBox(messages.fileNameExist(), InfoTypeEnum.ERROR);
								box.show();
							}
						}

						@Override
						public void onFailure(final Throwable caught)
						{
							ErrorManagement.displayErrorMessage(caught);
						}
					}.retry(0);
				}
			}
		});

		display.getFileNameValidator().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				return pValue.matches("[a-zA-Z0-9]+((\\_|\\.|\\-)?[a-zA-Z0-9]*)+");
			}

			@Override
			public String getErrorMessage()
			{
				return messages.fileNameValidation();
			}
		});

		display.getFileVersionValidator().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				return pValue.matches("[a-zA-Z0-9]+((\\_|\\.|\\-)?[0-9]*)+");
			}

			@Override
			public String getErrorMessage()
			{
				return messages.fileVersionValidation();
			}
		});

	}

	public UploadToolTabView getDisplay()
	{
		return display;
	}

	// retrieve the suffix file
	private String getSuffix(final String pFilename)
	{
		String    suffix = "";
		final int pos    = pFilename.lastIndexOf('.');
		if (pos != -1)
		{
			suffix = pFilename.substring(pos);
		}

		return suffix;
	}

	private boolean isSelectedAdminDirectory()
	{
		boolean ispublic = false;
		if (display.getUserDirectory().getValue())
		{
			ispublic = true;
		}
		return ispublic;
	}

	private String getUploadRequest()
	{
		final StringBuilder sb = new StringBuilder(servletPath);
		if (getDisplay().getFileNameValidator().isValid() && display.getFileVersionValidator().isValid()
		    && (getDisplay().getSingleUploader().getFileInput().getFilename().length() > 0))

		{
			final boolean ispublic = isSelectedAdminDirectory();
			sb.append("?").append(ToolProperties.NAME_PARAMETER).append("=")
			    .append(display.getFileNameValidator().getValue()).append("&")
			    .append(ToolProperties.VERSION_PARAMETER).append("=")
			    .append(display.getFileVersionValidator().getValue()).append("&")
			    .append(ToolProperties.ISPUBLIC_PARAMETER).append("=").append(ispublic).append("&")
			    .append(ToolProperties.SUFFIX_PARAMETER).append("=")
			    .append(getSuffix(UploadToolTabPresenter.this.getDisplay().getSingleUploader().getFileInput().getFilename()));
		}
		else
		{
			final InfoDialogBox box = new InfoDialogBox(messages.emptyField(), InfoTypeEnum.ERROR);
			box.show();
			display.getSingleUploader().reset();
			getDisplay().getSingleUploader().cancel();
			getDisplay().getSingleUploader().getFileInput().getWidget()
			    .removeStyleName(UploadToolTabPresenter.this.ressources.style().importantRed());

		}
		return sb.toString();
	}

	public void refreshTab()
	{
		getDisplay().getFileNameValidator().clear();
		getDisplay().getFileVersionValidator().clear();
		getDisplay().getSubmitFileButton().setEnabled(false);
		getDisplay().getFilesFromUrl().setText("");

		display.getSingleUploader().getFileInput().getWidget().removeStyleName(UploadToolTabPresenter.this.ressources
																																							 .style().importantRed());

	}

	@Override
	public void go(final HasWidgets pContainer)
	{
		pContainer.clear();
		pContainer.add(getDisplay().asWidget());
	}
}
