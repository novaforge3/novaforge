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
package org.novaforge.forge.ui.requirements.internal.client.repository.components;

import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;
import org.vaadin.easyuploads.UploadField;

/**
 * Upload field for excel files.
 * 
 * @author Gauthier Cart
 * @author B-Martinelli
 */
public class UploadFieldExcelRepository extends UploadField
{
  /** Serial version Id */
  private static final long serialVersionUID = -8426200097219397658L;
  
  /** Contains the max size of the file to upload. */
  private int               maxSize;

  /** Constructor */
  public UploadFieldExcelRepository()
  {
    setWriteThrough(true);
    setFieldType(FieldType.FILE);
    setFileDeletesAllowed(false);
    setStyleName(NovaForge.UPLOAD_FILE);
    maxSize = RequirementsModule.getRequirementConfigurationService().getExcelFileMaxSize();
  }

  /** {@inheritDoc}} */
  @Override
  public void uploadStarted(StartedEvent event)
  {
    super.uploadStarted(event);
    if (event.getContentLength() > maxSize && getUploadComponent() != null){
      getUploadComponent().interruptUpload();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void uploadFinished(final FinishedEvent event)
  {
    super.uploadFinished(event);

    if (event instanceof Upload.FailedEvent)
    {
      final TrayNotification notification = new TrayNotification(RequirementsModule.getPortalMessages()
          .getMessage(UI.getCurrent().getLocale(), Messages.REPOSITORY_ADD_EXCEL_ERROR_TITLE),
          RequirementsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
              Messages.REPOSITORY_ADD_EXCEL_ERROR_DESC), TrayNotificationType.WARNING);
      notification.show(Page.getCurrent());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getDisplayDetails()
  {
    return getLastFileName();
  }
  
  /**
   * Fetch the Upload Component within the UploadFielCustom.
   * @return the Upload Component within the UploadFielCustom
   */
  private Upload getUploadComponent()
  {
    Upload result = null;
    final Component component = getRootLayout().getComponent(0);
    if (component instanceof Upload)
    {
      result = ((Upload) component);
    }
    return result;
  }

  /**
   * Refresh the display
   */
  public void refreshDisplay()
  {
    updateDisplay();
  }

  /**
   * Return the MimeType of the file uploaded
   * 
   * @return string represents the file mimetype
   */
  public String getMimeType()
  {
    return super.getLastMimeType();
  }

  /**
   * Returns the excelFileName
   * 
   * @return the excelFileName
   */
  public String getExcelFileName()
  {
    return super.getLastFileName();
  }

}
