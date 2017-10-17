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
package org.novaforge.forge.tools.deliverymanager.ui.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.loading.LoadingPanel;

/**
 * @author CASERY-J
 */
public class DeliveryGenerateViewImpl extends Composite implements DeliveryGenerateView
{
   /**
    * Create the UiBinder for DeliveryGenerateViewImpl
    */
   private static DeliveryGenerateViewImplUiBinder uiBinder = GWT.create(DeliveryGenerateViewImplUiBinder.class);
   @UiField
   Label        deliveryGenerateTitle;
   @UiField
   Label        deliveryDetailsTitle;
   @UiField
   Panel        deliveryDetailsPanel;
   @UiField
   Label        deliveryNameLabel;
   @UiField
   Label        deliveryNameValue;
   @UiField
   Label        deliveryVersionLabel;
   @UiField
   Label        deliveryVersionValue;
   @UiField
   Label        deliveryReferenceLabel;
   @UiField
   Label        deliveryReferenceValue;
   @UiField
   Label        deliveryTypeLabel;
   @UiField
   Label        deliveryTypeValue;
   @UiField
   Label        deliveryContentTitle;
   @UiField
   Panel        deliveryContentPanel;
   @UiField
   Panel        ecmPanel;
   @UiField
   Panel        scmPanel;
   @UiField
   Panel        filesPanel;
   @UiField
   Panel        bugsPanel;
   @UiField
   Panel        notePanel;
   @UiField
   Label        deliveryContentECM;
   @UiField
   Label        deliveryContentSCM;
   @UiField
   Label        deliveryContentFILES;
   @UiField
   Label        deliveryContentBUGS;
   @UiField
   Label        deliveryContentNOTE;
   @UiField
   Label        contentECMInfosValue;
   @UiField
   Label        contentSCMInfosValue;
   @UiField
   Label        contentFilesInfosValue;
   @UiField
   Label        contentBugsInfosValue;
   @UiField
   Label        contentNoteInfosValue;
   @UiField
   Button       buttonEditDelivery;
   @UiField
   Button       buttonGenerateDelivery;
   @UiField
   Button       returnButton;
   @UiField(provided = true)
   LoadingPanel deliveryDetailsLoadingPanel;
   @UiField(provided = true)
   LoadingPanel deliveryContentLoadingPanel;
   /**
    * Create a new instance of DeliveryGenerateViewImpl
    */
   public DeliveryGenerateViewImpl()
   {
      Common.getResources().css().ensureInjected();

      this.deliveryDetailsLoadingPanel = new LoadingPanel();
      this.deliveryContentLoadingPanel = new LoadingPanel();

      this.initWidget(uiBinder.createAndBindUi(this));

      this.deliveryGenerateTitle.setText(DeliveryCommon.getMessages().generateDeliveryTitle());

      this.deliveryDetailsTitle.setText(DeliveryCommon.getMessages().summaryDeliveryTitle());
      this.deliveryNameLabel.setText(this.formStyle(DeliveryCommon.getMessages().name()));
      this.deliveryReferenceLabel.setText(this.formStyle(DeliveryCommon.getMessages().reference()));
      this.deliveryTypeLabel.setText(this.formStyle(DeliveryCommon.getMessages().type()));
      this.deliveryVersionLabel.setText(this.formStyle(DeliveryCommon.getMessages().version()));

      this.deliveryContentTitle.setText(DeliveryCommon.getMessages().deliveryContentTitle());
      this.deliveryContentECM.setText(this.formStyle(DeliveryCommon.getMessages().ecmContent()));
      this.deliveryContentSCM.setText(this.formStyle(DeliveryCommon.getMessages().scmContent()));
      this.deliveryContentFILES.setText(this.formStyle(DeliveryCommon.getMessages().binaryContent()));
      this.deliveryContentBUGS.setText(this.formStyle(DeliveryCommon.getMessages().bugContent()));
      this.deliveryContentNOTE.setText(this.formStyle(DeliveryCommon.getMessages().noteContent()));

      this.ecmPanel.setVisible(false);
      this.scmPanel.setVisible(false);
      this.filesPanel.setVisible(false);
      this.bugsPanel.setVisible(false);
      this.notePanel.setVisible(false);

      this.returnButton.setText(DeliveryCommon.getMessages().returnList());
      this.buttonEditDelivery.setText(DeliveryCommon.getMessages().edit());
      this.buttonGenerateDelivery.setText(DeliveryCommon.getMessages().generateDelivery());

   }

   /**
    * Concat " :" to given text
    *
    * @param pText
    * @return " :" concat to pText
    */
   private String formStyle(final String pText)
   {
      return pText + " : ";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Button getButtonGenerateDelivery()
   {
      return this.buttonGenerateDelivery;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Button getButtonEditDelivery()
   {
      return this.buttonEditDelivery;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getDeliveryTypeValue()
   {
      return this.deliveryTypeValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getDeliveryReferenceValue()
   {
      return this.deliveryReferenceValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getDeliveryVersionValue()
   {
      return this.deliveryVersionValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getDeliveryNameValue()
   {
      return this.deliveryNameValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Button getReturnButton()
   {
      return this.returnButton;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Panel getNotePanel()
   {
      return this.notePanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Panel getBugsPanel()
   {
      return this.bugsPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Panel getFilesPanel()
   {
      return this.filesPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Panel getScmPanel()
   {
      return this.scmPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Panel getEcmPanel()
   {
      return this.ecmPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public LoadingPanel getDeliveryDetailsLoadingPanel()
   {
      return this.deliveryDetailsLoadingPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Panel getDeliveryContentPanel()
   {
      return this.deliveryContentPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public LoadingPanel getDeliveryContentLoadingPanel()
   {
      return this.deliveryContentLoadingPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getContentNoteInfosValue()
   {
      return this.contentNoteInfosValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getContentBugsInfosValue()
   {
      return this.contentBugsInfosValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getContentFilesInfosValue()
   {
      return this.contentFilesInfosValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getContentSCMInfosValue()
   {
      return this.contentSCMInfosValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label getContentECMInfosValue()
   {
      return this.contentECMInfosValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Panel getDeliveryDetailsPanel()
   {
      return this.deliveryDetailsPanel;
   }

   /**
    * Interface for the UIBinder for DeliveryGenerateViewImpl
    *
    * @author CASERY-J
    */
   interface DeliveryGenerateViewImplUiBinder extends UiBinder<Widget, DeliveryGenerateViewImpl>
   {
   }

}
