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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import gwtupload.client.HasProgress;
import gwtupload.client.IUploadStatus;
import gwtupload.client.Utils;

import org.gwtwidgets.client.ui.ProgressBar;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.resources.CommonsResources;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class ProgressUploadStatus extends Composite implements IUploadStatus {
	private static ProgressUploadStatusUiBinder UI_BINDER = GWT.create(ProgressUploadStatusUiBinder.class);
	private static CommonsResources RESSOURCES = GWT.create(CommonsResources.class);
	@UiField
	Panel panel;
	@UiField(provided = true)
	Image removeImage;
	@UiField
	Label fileName;
	ProgressBar progressBar;
	@UiField
	Label statusLabel;
	private UploadStatusConstants i18nStrs = GWT.create(Uploader.class);
	private UploadStatusChangedHandler onUploadStatusChangedHandler = null;
	private Set<CancelBehavior> cancelCfg = EnumSet.of(CancelBehavior.STOP_CURRENT);
	private IUploadStatus.Status status = Status.UNINITIALIZED;

	/**
	 * Default constructor
	 */
	public ProgressUploadStatus() {
		removeImage = new Image(RESSOURCES.ko_small());
		initWidget(UI_BINDER.createAndBindUi(this));
		progressBar = new ProgressBar(10);
		panel.add(progressBar);
	}

	@Override
	public void setProgress(final long pDone, final long pTotal) {
		setStatus(status);
		progressBar.setProgress(Utils.getPercent(pDone, pTotal));
	}

	protected void updateStatusPanel(final boolean showProgress, final String message) {
		progressBar.setVisible(showProgress);
		statusLabel.setVisible(!showProgress);

		statusLabel.setText(message);
		removeImage.setVisible(!cancelCfg.contains(CancelBehavior.DISABLED));
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	@Override
	public HandlerRegistration addCancelHandler(final UploadCancelHandler pHandler) {
		return removeImage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				pHandler.onCancel();
			}
		});
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public IUploadStatus newInstance() {
		return new ProgressUploadStatus();
	}

	@Override
	public void setCancelConfiguration(final Set<CancelBehavior> pConfig) {
		cancelCfg = pConfig;

	}

	@Override
	public void setError(final String pError) {
		setStatus(Status.ERROR);
		final InfoDialogBox info = new InfoDialogBox(pError.replaceAll("\\\\n", "\\n"), InfoTypeEnum.ERROR);
		info.show();

	}

	@Override
	public void setI18Constants(final UploadStatusConstants pStrs) {
		i18nStrs = pStrs;

	}

	@Override
	public void setStatus(final Status pStatus) {
		final String statusName = pStatus.toString().toLowerCase();
		statusLabel.removeStyleDependentName(statusName);
		statusLabel.addStyleDependentName(statusName);
		switch (pStatus) {
		case CHANGED:
		case QUEUED:
			updateStatusPanel(false, i18nStrs.uploadStatusQueued());
			break;
		case SUBMITING:
			updateStatusPanel(false, i18nStrs.uploadStatusSubmitting());
			break;
		case INPROGRESS:
			updateStatusPanel(true, i18nStrs.uploadStatusInProgress());
			if (!cancelCfg.contains(CancelBehavior.STOP_CURRENT)) {
				removeImage.setVisible(false);
			}
			break;
		case SUCCESS:
		case REPEATED:
			updateStatusPanel(false, i18nStrs.uploadStatusSuccess());
			if (!cancelCfg.contains(CancelBehavior.REMOVE_REMOTE)) {
				removeImage.setVisible(false);
			}
			break;
		case INVALID:
			getWidget().removeFromParent();
			break;
		case CANCELING:
			updateStatusPanel(false, i18nStrs.uploadStatusCanceling());
			break;
		case CANCELED:
			updateStatusPanel(false, i18nStrs.uploadStatusCanceled());
			if (cancelCfg.contains(CancelBehavior.REMOVE_CANCELLED_FROM_LIST)) {
				getWidget().removeFromParent();
			}
			break;
		case ERROR:
			updateStatusPanel(false, i18nStrs.uploadStatusError());
			break;
		case DELETED:
			updateStatusPanel(false, i18nStrs.uploadStatusDeleted());
			getWidget().removeFromParent();
			break;
		}
		if ((status != pStatus) && (onUploadStatusChangedHandler != null)) {
			status = pStatus;
			onUploadStatusChangedHandler.onStatusChanged(this);
		}
		status = pStatus;

	}

	@Override
	public void setStatusChangedHandler(final UploadStatusChangedHandler pHandler) {
		onUploadStatusChangedHandler = pHandler;

	}

	@Override
	public void setVisible(final boolean pB) {
		panel.setVisible(pB);

	}

	interface ProgressUploadStatusUiBinder extends UiBinder<Widget, ProgressUploadStatus> {
	}

	@Override
	public void setFileNames(List<String> names) {
		if (names.size() > 0) {
			fileName.setText(names.get(0));
		} else {
			fileName.setText(null);
		}

	}

	@Override
	public Set<CancelBehavior> getCancelConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}
