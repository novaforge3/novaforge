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
package org.novaforge.forge.ui.portal.client.component;

import com.google.common.base.Strings;
import com.vaadin.data.Buffered;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.vaadin.easyuploads.UploadField;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/** @author Guillaume Lamirand */
public class UploadFieldCustom extends UploadField
{
  /** Define default iconsize. */
  public static final int   ICON_SIZE        = 128;
  /**
   * Serial version Id.
   */
  private static final long serialVersionUID = -7449517841895408922L;
  /** Contains the icon size. */
  private final int iconSize;

  /** Contains the message displayed on upload error. */
  private String errorSizeMessage;

  /** Contains the max size of the file to upload. */
  private int maxSize;

  /** Default constructor. */
  public UploadFieldCustom()
  {
    this(Integer.MAX_VALUE, ICON_SIZE);
  }

  /**
   * Defines specific value for maxsize
   *
   * @param pMaxSize
   *          the max size of uploaded content
   * @param pIconSize
   *          define the size of image displayed (used for widget and height)
   */
  public UploadFieldCustom(final int pMaxSize, final int pIconSize)
  {
    iconSize = pIconSize;
    setStorageMode(StorageMode.MEMORY);
    setFileDeletesAllowed(false);
    setFieldType(FieldType.BYTE_ARRAY);
    setStyleName(NovaForge.UPLOAD_LAYOUT);
    maxSize = pMaxSize;
  }

  /**
   * Defines specific value for maxsize
   *
   * @param pMaxSize
   *          the max size of uploaded content
   */
  public UploadFieldCustom(final int pMaxSize)
  {
    this(pMaxSize, ICON_SIZE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String pDescription)
  {
    final int componentCount = getRootLayout().getComponentCount();
    if (componentCount > 0)
    {
      final Component component = getRootLayout().getComponent(0);
      if (component instanceof Upload)
      {
        ((Upload) component).setDescription(pDescription);
      }
    }
  }

  /** {@inheritDoc}} */
  @Override
  public void uploadStarted(StartedEvent event)
  {
    super.uploadStarted(event);
    if (event.getContentLength() > maxSize && getUploadComponent() != null)
    {
      getUploadComponent().interruptUpload();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void uploadFinished(final FinishedEvent event)
  {
    super.uploadFinished(event);
    if (event instanceof Upload.FailedEvent)
    {
      discard();
      String message = errorSizeMessage;
      if (Strings.isNullOrEmpty(errorSizeMessage))
      {
        message = "An error occured when uploading your picture, please try again.";
      }
      final Notification notif = new Notification(message, Type.WARNING_MESSAGE);
      notif.show(Page.getCurrent());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void updateDisplay()
  {
    // Reset components
    getRootLayout().removeAllComponents();

    // Get current image information
    final byte[] pngData = (byte[]) getValue();
    final String filename = getLastFileName();
    final String mimeType = getLastMimeType();

    // Build stream resources for the new image
    final StreamResource resource = new StreamResource(getStreamSource(pngData), filename);
    resource.setMIMEType(mimeType);
    final Image embedded = new Image(filename, resource);
    embedded.setWidth(iconSize, Unit.PIXELS);
    embedded.setHeight(iconSize, Unit.PIXELS);

    // Add default layout
    buildDefaulLayout();

    // Add image
    getRootLayout().addComponent(embedded);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discard() throws Buffered.SourceException
  {
    super.discard();

    // Set empty byte
    setValue(new byte[0]);
    // Add default layout
    buildDefaulLayout();
  }

  /**
   * @param pngData
   * @return
   */
  private StreamSource getStreamSource(final byte[] pngData)
  {
    return new StreamResource.StreamSource()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7437081273300370477L;

      /**
       * {@inheritDoc}
       */
      @Override
      public InputStream getStream()
      {
        return new ByteArrayInputStream(pngData);
      }
    };
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
   * Return the MimeType of the file uploaded
   *
   * @return string represents the file mimetype
   */
  public String getMimeType()
  {
    return super.getLastMimeType();

  }

  /**
   * Set the error message
   * 
   * @param errorSizeMessage
   *          the errorSizeMessage to set
   */
  public void setErrorSizeMessage(final String errorSizeMessage)
  {
    this.errorSizeMessage = errorSizeMessage;
  }

}
