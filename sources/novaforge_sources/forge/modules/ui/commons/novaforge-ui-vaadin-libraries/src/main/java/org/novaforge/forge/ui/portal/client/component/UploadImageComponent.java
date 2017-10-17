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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class UploadImageComponent extends VerticalLayout
{
  /**
   * Define default max size for image. (15728640 octets for 1.5mo)
   */
  public final static  int  MAX_SIZE         = 15728640;
  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = -5231197448997190414L;
  /**
   * Define default popup window size
   */
  private final static int  WINDOW_SIZE      = 300;
  /**
   * Contains the icon width
   */
  private final int               iconSize;
  /**
   * Contains the icon max size
   */
  private final int               iconMaxSize;
  /**
   * The project icon
   */
  private       Image             icon;
  /**
   * The update picture button
   */
  private       Button            updateIconButton;
  /**
   * The delete picture button
   */
  private       Button            deleteIconButton;
  /**
   * The update picture window
   */
  private       Window            updateIconWindow;
  /**
   * The update picture help label
   */
  private       Label             updateIconHelpLabel;
  /**
   * The confirm update picture button
   */
  private       Button            confirmUpdateIconButton;
  /**
   * The upload picture field
   */
  private       UploadFieldCustom uploadField;
  private String windowTitleKeyMessage             = Messages.COMPONENT_IMAGE_TITLE;
  private String helpCaptionKeyMessage             = Messages.COMPONENT_IMAGE_HELP;
  private String uploadCaptionKeyMessage           = Messages.COMPONENT_IMAGE_UPLOAD_CAPTION;
  private String uploadButtonCaptionKeyMessage     = Messages.COMPONENT_IMAGE_UPLOAD_BUTTON;
  private String uploadButtonDescriptionKeyMessage = Messages.COMPONENT_IMAGE_UPLOAD_DESCRIPTION;
  private String confirmButtonCaptionKeyMessage    = Messages.COMPONENT_IMAGE_CONFIRM;

  private Button cancelUpdateIconButton;

  /**
   * Default constructor.
   * <p>
   * It will use the default value for icon size
   * </p>
   */
  public UploadImageComponent()
  {
    this(UploadFieldCustom.ICON_SIZE, MAX_SIZE);
  }

  /**
   * Constructor used to define the size of icon
   *
   * @param pIconSize
   *     define the icon size
   * @param pIconMaxSize
   *     define the max size of the icon file
   */
  public UploadImageComponent(final int pIconSize, final int pIconMaxSize)
  {
    // Init layouts
    iconSize = pIconSize;
    iconMaxSize = pIconMaxSize;
    initIconLayout();
    initUpdatePictureWindow();

    // Add listener
    addListener();
  }

  private void initIconLayout()
  {
    updateIconButton = new Button();
    deleteIconButton = new Button();
    updateIconButton.setStyleName(NovaForge.BUTTON_LINK);
    deleteIconButton.setStyleName(NovaForge.BUTTON_LINK);
    final HorizontalLayout pictureButtonLayout = new HorizontalLayout();
    pictureButtonLayout.setSpacing(true);
    pictureButtonLayout.addComponent(updateIconButton);
    pictureButtonLayout.addComponent(deleteIconButton);
    icon = new Image();
    icon.setWidth(iconSize, Unit.PIXELS);
    icon.setHeight(iconSize, Unit.PIXELS);
    addComponent(icon);
    addComponent(pictureButtonLayout);
    setComponentAlignment(icon, Alignment.TOP_CENTER);
    setComponentAlignment(pictureButtonLayout, Alignment.TOP_CENTER);
  }

  /**
   * Initialize update picture window
   */
  private void initUpdatePictureWindow()
  {
    updateIconWindow = new Window();
    updateIconWindow.setModal(true);
    updateIconWindow.setResizable(false);
    updateIconWindow.setWindowMode(WindowMode.NORMAL);

    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(new MarginInfo(true, true, true, true));
    windowLayout.setWidth(WINDOW_SIZE, Unit.PIXELS);
    windowLayout.setSpacing(true);

    updateIconHelpLabel = new Label();
    updateIconHelpLabel.setSizeUndefined();
    uploadField = new UploadFieldCustom(iconMaxSize);
    final HorizontalLayout footer = new HorizontalLayout();
    footer.setSpacing(true);
    confirmUpdateIconButton = new Button();
    confirmUpdateIconButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    confirmUpdateIconButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    cancelUpdateIconButton = new Button();
    cancelUpdateIconButton.setStyleName(NovaForge.BUTTON_LINK);
    footer.addComponent(cancelUpdateIconButton);
    footer.addComponent(confirmUpdateIconButton);
    footer.setComponentAlignment(cancelUpdateIconButton, Alignment.MIDDLE_CENTER);
    footer.setComponentAlignment(confirmUpdateIconButton, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(updateIconHelpLabel);
    windowLayout.addComponent(uploadField);
    windowLayout.addComponent(footer);
    windowLayout.setComponentAlignment(updateIconHelpLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(uploadField, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);
    updateIconWindow.setContent(windowLayout);
  }

  private void addListener()
  {
    updateIconButton.addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -3199265339066502254L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().addWindow(updateIconWindow);
      }
    });
    deleteIconButton.addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -7861649120755681975L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        uploadField.discard();
      }
    });
    cancelUpdateIconButton.addClickListener(new ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -8661597347693052359L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        UI.getCurrent().removeWindow(updateIconWindow);

      }
    });
    updateIconWindow.addCloseListener(new CloseListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 693987582458831627L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void windowClose(final CloseEvent pEvent)
      {
        uploadField.discard();
      }
    });
    uploadField.addListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -1650971247688119334L;

      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {

        if ((uploadField.getMimeType() != null) && (!uploadField.getMimeType().startsWith("image/")))
        {
          uploadField.discard();
          final Locale locale = getLocale();
          final Notification notification = new Notification(OSGiHelper.getPortalMessages().getMessage(locale,
                                                                                                       Messages.COMPONENT_IMAGE_ERROR_FORMAT),
                                                             Type.WARNING_MESSAGE);
          notification.show(Page.getCurrent());
        }

      }
    });

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    final PortalMessages portalMessages = OSGiHelper.getPortalMessages();
    refreshLocale(portalMessages, getLocale());
  }

  /**
   * Should be called to refresh all messages
   *
   * @param pPortalMessages
   *     should refere to service implementation
   * @param pLocale
   *     the locale userd to localized messages
   */
  public void refreshLocale(final PortalMessages pPortalMessages, final Locale pLocale)
  {
    if ((pPortalMessages != null) && (pLocale != null))
    {
      updateIconButton.setCaption(pPortalMessages.getMessage(pLocale, Messages.ACTIONS_EDIT));
      deleteIconButton.setCaption(pPortalMessages.getMessage(pLocale, Messages.ACTIONS_DELETE));
      cancelUpdateIconButton.setCaption(pPortalMessages.getMessage(pLocale, Messages.ACTIONS_CANCEL));

      // Update picture
      updateIconWindow.setCaption(pPortalMessages.getMessage(pLocale, windowTitleKeyMessage));
      uploadField.setErrorSizeMessage(pPortalMessages.getMessage(pLocale, Messages.COMPONENT_IMAGE_ERROR_SIZE,
                                                                 getSizeMessage(iconMaxSize)));
      uploadField.setCaption(pPortalMessages.getMessage(pLocale, uploadCaptionKeyMessage));
      uploadField.setButtonCaption(pPortalMessages.getMessage(pLocale, uploadButtonCaptionKeyMessage));
      uploadField.setDescription(pPortalMessages.getMessage(pLocale, uploadButtonDescriptionKeyMessage));
      confirmUpdateIconButton.setCaption(pPortalMessages.getMessage(pLocale, confirmButtonCaptionKeyMessage));
      updateIconHelpLabel.setValue(pPortalMessages.getMessage(pLocale, helpCaptionKeyMessage,
                                                              getSizeMessage(iconMaxSize)));
    }

  }

  private String getSizeMessage(final long pMaxSize)
  {
    final StringBuilder sizeBuilder = new StringBuilder();
    final BigDecimal bigDecimal = new BigDecimal(pMaxSize);
    if (pMaxSize < 1024)
    {
      sizeBuilder.append(String.valueOf(pMaxSize));
      sizeBuilder.append(" octets");
    }
    else if ((pMaxSize >= 1024) && (pMaxSize < 1048576))
    {
      final BigDecimal result = bigDecimal.divide(new BigDecimal(1024));
      sizeBuilder.append(NumberFormat.getInstance().format(result));
      sizeBuilder.append(" Ko");
    }
    else
    {
      final BigDecimal result = bigDecimal.divide(new BigDecimal(1048576));
      sizeBuilder.append(NumberFormat.getInstance().format(result));
      sizeBuilder.append(" Mo");
    }
    return sizeBuilder.toString();
  }

  /**
   * Get the image
   *
   * @return {@link Embedded} the image
   */
  public Image getImage()
  {
    return icon;
  }

  /**
   * Get the update image button
   *
   * @return {@link Button}
   */
  public Button getUpdateImageButton()
  {
    return updateIconButton;
  }

  /**
   * Get the update image window
   *
   * @return the {@link Window}
   */
  public Window getUpdateImageWindow()
  {
    return updateIconWindow;
  }

  /**
   * Get the delete image button
   *
   * @return {@link Button}
   */
  public Button getDeleteImageButton()
  {
    return deleteIconButton;
  }

  /**
   * Get the confirm update image button
   *
   * @return the {@link Button}
   */
  public Button getUpdateImageConfirmButton()
  {
    return confirmUpdateIconButton;
  }

  /**
   * Get the cancel update image button
   *
   * @return the {@link Button}
   */
  public Button getUpdateImageCancelButton()
  {
    return cancelUpdateIconButton;
  }

  /**
   * Get the upload image field
   *
   * @return the {@linkplain UploadFieldCustom} field
   */
  public UploadFieldCustom getUploadImageField()
  {
    return uploadField;
  }

  /**
   * Set the messages key of window title used to retrieve localized message
   *
   * @param pWindowTitleKeyMessage
   *     the windowTitleKeyMessage to set
   */
  public void setWindowTitleKeyMessage(final String pWindowTitleKeyMessage)
  {
    windowTitleKeyMessage = pWindowTitleKeyMessage;
  }

  /**
   * Set the messages key of help caption used to retrieve localized message
   *
   * @param pHelpCaptionKeyMessage
   *     the helpCaptionKeyMessage to set
   */
  public void setHelpCaptionKeyMessage(final String pHelpCaptionKeyMessage)
  {
    helpCaptionKeyMessage = pHelpCaptionKeyMessage;
  }

  /**
   * Set the messages key of upload caption used to retrieve localized message
   *
   * @param pUploadCaptionKeyMessage
   *     the uploadCaptionKeyMessage to set
   */
  public void setUploadCaptionKeyMessage(final String pUploadCaptionKeyMessage)
  {
    uploadCaptionKeyMessage = pUploadCaptionKeyMessage;
  }

  /**
   * Set the messages key of upload button used to retrieve localized message
   *
   * @param pUploadButtonCaptionKeyMessage
   *     the uploadButtonCaptionKeyMessage to set
   */
  public void setUploadButtonCaptionKeyMessage(final String pUploadButtonCaptionKeyMessage)
  {
    uploadButtonCaptionKeyMessage = pUploadButtonCaptionKeyMessage;
  }

  /**
   * Set the messages key of upload description used to retrieve localized message
   *
   * @param pUploadButtonDescriptionKeyMessage
   *     the uploadButtonDescriptionKeyMessage to set
   */
  public void setUploadButtonDescriptionKeyMessage(final String pUploadButtonDescriptionKeyMessage)
  {
    uploadButtonDescriptionKeyMessage = pUploadButtonDescriptionKeyMessage;
  }

  /**
   * Set the messages key of confirm button used to retrieve localized message
   *
   * @param pConfirmButtonCaptionKeyMessage
   *     the confirmButtonCaptionKeyMessage to set
   */
  public void setConfirmButtonCaptionKeyMessage(final String pConfirmButtonCaptionKeyMessage)
  {
    confirmButtonCaptionKeyMessage = pConfirmButtonCaptionKeyMessage;
  }

}
