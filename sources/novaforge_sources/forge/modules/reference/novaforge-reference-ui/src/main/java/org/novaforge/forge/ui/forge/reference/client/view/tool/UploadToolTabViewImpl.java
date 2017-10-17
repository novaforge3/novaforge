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
package org.novaforge.forge.ui.forge.reference.client.view.tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnCancelUploaderHandler;
import gwtupload.client.IUploader.UploaderConstants;
import gwtupload.client.SingleUploader;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.commons.client.upload.CustomSingleUploader;
import org.novaforge.forge.ui.commons.client.upload.ProgressUploadStatus;
import org.novaforge.forge.ui.commons.client.upload.Uploader;
import org.novaforge.forge.ui.commons.client.validation.TextBoxBaseValidation;
import org.novaforge.forge.ui.commons.client.validation.TextBoxValidation;
import org.novaforge.forge.ui.forge.reference.client.properties.UploadMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;

public class UploadToolTabViewImpl extends Composite implements UploadToolTabView
{
	private static UploadToolTabViewImplUiBinder uiBinder   = GWT.create(UploadToolTabViewImplUiBinder.class);
	private static ReferenceResources            ressources = GWT.create(ReferenceResources.class);
	private final UploadMessage messages = (UploadMessage) GWT.create(UploadMessage.class);
	private final Button            uploaderButton;
	private final ValidateDialogBox confirmOverrideAndCopyDialogBox;
	@UiField
	Label                           informationFileTitle;
	@UiField
	Label                           directoryLabel;
	@UiField
	RadioButton                     adminDirectory;
	@UiField
	RadioButton                     userDirectory;
	@UiField
	Label                           fileNameLabel;
	@UiField
	TextBoxValidation               fileNameValidator;
	@UiField
	Label                           fileVersionLabel;
	@UiField
	TextBoxValidation               fileVersionValidator;
	@UiField
	VerticalPanel                   uploadPanel;
	IUploader                       singleUploader;
	@UiField
	Label                           filesFromURLLabel;
	@UiField
	TextBox                         filesFromUrl;
	@UiField
	Button                          sendFromURLButton;
	@UiField
	Label                           sendFileTitle;
	@UiField
	Label                           filesFromComputerLabel;

	public UploadToolTabViewImpl()
	{
		ressources.style().ensureInjected(); // Initialization of validation popup
		confirmOverrideAndCopyDialogBox = new ValidateDialogBox(messages.confirmOverideAndCopyMessage());

		initWidget(uiBinder.createAndBindUi(this));

		// ensure debug
		fileNameValidator.ensureDebugId("fileNameValidator");
		fileVersionValidator.ensureDebugId("fileVersionValidator");
		adminDirectory.ensureDebugId("adminDirectory");
		userDirectory.ensureDebugId("publicDirectory");

		informationFileTitle.setText(messages.informationFileTitle());
		adminDirectory.setText(messages.adminDirectory());
		userDirectory.setText(messages.userDirectory());
		fileNameLabel.setText(messages.fileNameLabel());
		fileVersionLabel.setText(messages.fileVersionLabel());
		directoryLabel.setText(messages.spaceLabel());
		sendFileTitle.setText(messages.sendFileTitle());
		filesFromComputerLabel.setText(messages.filesFromComputerLabel());
		filesFromURLLabel.setText(messages.filesFromURLLabel());
		sendFromURLButton.setText(messages.sendFromURLButton());

		uploaderButton = new Button();
		uploaderButton.setEnabled(false);
		singleUploader = new CustomSingleUploader(new ProgressUploadStatus(), uploaderButton);
		
		singleUploader.setAutoSubmit(false);
		singleUploader.getFileInput().setSize("100%", "25px");
		singleUploader.setI18Constants((UploaderConstants) GWT.create(Uploader.class));
		singleUploader.addOnCancelUploadHandler(new OnCancelUploaderHandler()
		{
			@Override
			public void onCancel(final IUploader pUploader)
			{
				pUploader.cancel();
			}
		});

		uploadPanel.add((SingleUploader) singleUploader);

	}

	@Override
	public IUploader getSingleUploader()
	{
		return singleUploader;
	}

	@Override
	public Button getSubmitFileButton()
	{
		return uploaderButton;
	}

	@Override
	public TextBoxBaseValidation getFileNameValidator()
	{
		return fileNameValidator;
	}

	@Override
	public Button getSendFromURLButton()
	{
		return sendFromURLButton;
	}

	@Override
	public TextBox getFilesFromUrl()
	{
		return filesFromUrl;
	}

	@Override
	public TextBoxValidation getFileVersionValidator()
	{
		return fileVersionValidator;
	}

	@Override
	public ValidateDialogBox getConfirmOverrideAndCopyDialogBox()
	{
		return confirmOverrideAndCopyDialogBox;
	}

	@Override
	public RadioButton getUserDirectory()
	{
		return userDirectory;
	}

	@Override
	public RadioButton getAdminDirectory()
	{
		return adminDirectory;
	}

	interface UploadToolTabViewImplUiBinder extends UiBinder<Widget, UploadToolTabViewImpl>
	{
	}

}
