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
package org.novaforge.forge.ui.commons.client.upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

/**
 * Custom component for {@link SingleUploader}
 * 
 * @author Guillaume Lamirand
 */
public class CustomSingleUploader extends SingleUploader
{
	/**
	 * Default constructor.
	 * Use a {@link ProgressUploadStatus} and a {@link Button}
	 */
	public CustomSingleUploader()
	{
		super(new ProgressUploadStatus(), new Button());

		getFileInput().setSize("100%", "25px");
		setI18Constants((UploaderConstants) GWT.create(Uploader.class));
		setUploadTimeout(600000);
		addOnCancelUploadHandler(new OnCancelUploaderHandler()
		{
			@Override
			public void onCancel(final IUploader pUploader)
			{
				pUploader.cancel();
			}
		});
	}

	/**
	 * Default constructor.
	 * Use a {@link ProgressUploadStatus} and a {@link Button}
	 */
	public CustomSingleUploader(IUploadStatus pStatus, Widget pSubmitButton)
	{
		super(pStatus, pSubmitButton);

		getFileInput().setSize("100%", "25px");
		setI18Constants((UploaderConstants) GWT.create(Uploader.class));
		setUploadTimeout(600000);
		addOnCancelUploadHandler(new OnCancelUploaderHandler()
		{
			@Override
			public void onCancel(final IUploader pUploader)
			{
				pUploader.cancel();
			}
		});
	}

	/**
	 * @return {@link Button} used to commit upload
	 */
	public Button getButton()
	{
		return (Button) button;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishUpload()
	{
		super.onFinishUpload();
		if (button != null)
		{
			((Button) button).setEnabled(false);
		}
	}

}
