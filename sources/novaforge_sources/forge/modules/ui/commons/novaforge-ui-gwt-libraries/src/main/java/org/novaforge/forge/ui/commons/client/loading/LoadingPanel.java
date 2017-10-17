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
package org.novaforge.forge.ui.commons.client.loading;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author caseryj
 * @author Guillaume Lamirand
 */
public class LoadingPanel extends Composite implements Loading
{

  /**
   * The LoadingPanel ressources, containing css and images
   */
  private static LoadingResources     RESSOURCES = GWT.create(LoadingResources.class);
  /**
   * The LoadingPanel messages
   */
  private static LoadingMessages      MESSAGES   = GWT.create(LoadingMessages.class);
  /**
   * The LoadingPanel UI Binder
   */
  private static LoadingPanelUiBinder UI_BINDER  = GWT.create(LoadingPanelUiBinder.class);
  @UiField
  Image           loadingImage;
  @UiField
  Label           loadingErrorLabel;
  @UiField
  Label           loadingBottomLabel;
  @UiField
  Label           loadingRightLabel;
  @UiField
  HorizontalPanel loadingImagePanel;
  @UiField
  HorizontalPanel loadingLabelPanel;
  /**
   * Initialize a new LoadingPanel, with the default {@link LoadingType} CIRCLEBIG
   */
  public LoadingPanel()
  {
    this(LoadingType.CIRCLEBIG);
  }

  /**
   * Initialize a new LoadingPanel by providing a {@link LoadingType}
   *
   * @param pType
   *          the {@link LoadingType} of the LoadingPanel
   */
  public LoadingPanel(final LoadingType pType)
  {
    this(pType.getBottomLabel(), pType.getRightLabel(), pType.getImage(), MESSAGES.errorDefault());

  }

  /**
   * Initialize a new LoadingPanel by providing the bottom and right labels to use, and the loader image to
   * use.
   *
   * @param pBottomLabel
   *          the label to show under the loader image
   * @param pRightLabel
   *          the label to show at the right of the loader image
   * @param pImage
   *          the loader image to use
   * @param pErrorMessage
   *          the label to show in case of error
   */
  public LoadingPanel(final String pBottomLabel, final String pRightLabel, final ImageResource pImage,
      final String pErrorMessage)
  {
    RESSOURCES.style().ensureInjected();
    // Generate ui
    initWidget(UI_BINDER.createAndBindUi(this));
    loadingErrorLabel.setText(pErrorMessage);
    loadingBottomLabel.setText(pBottomLabel);
    loadingRightLabel.setText(pRightLabel);
    loadingImage.setUrl(pImage.getURL());
    loadingImage.setTitle(pBottomLabel);
    loadingImagePanel.setHeight(Integer.toString(pImage.getHeight()));
  }

  /**
   * @return the ressources
   */
  public static LoadingResources getRessources()
  {
    return RESSOURCES;
  }

  /**
   * @return the messages
   */
  public static LoadingMessages getMessages()
  {
    return MESSAGES;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setText(final String pBottomText, final String pRightText)
  {
    loadingBottomLabel.setText(pBottomText);
    loadingRightLabel.setText(pRightText);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setImage(final String pImageUrl)
  {
    loadingImage.setUrl(pImageUrl);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setErrorText(final String pErrorMessage)
  {
    loadingErrorLabel.setText(pErrorMessage);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setErrorStatus(final boolean pInError)
  {
    loadingErrorLabel.setVisible(pInError);
    loadingImagePanel.setVisible(!pInError);
    loadingLabelPanel.setVisible(!pInError);

  }

  /**
   * The LoadingPanelUiBinder interface
   *
   * @author CASERY-J
   */
  interface LoadingPanelUiBinder extends UiBinder<Widget, LoadingPanel>
  {
  }

}
