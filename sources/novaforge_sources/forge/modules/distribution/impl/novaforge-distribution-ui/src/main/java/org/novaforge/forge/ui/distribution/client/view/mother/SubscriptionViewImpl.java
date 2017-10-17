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
/**
 * 
 */
package org.novaforge.forge.ui.distribution.client.view.mother;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.validation.TextAreaValidation;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.view.commons.dialogbox.ForgeValidateDialogBox;
import org.novaforge.forge.ui.distribution.shared.enumeration.MotherActionEnum;

/**
 * @author BILET-JC
 *
 */
public class SubscriptionViewImpl extends Composite implements SubscriptionView {

   private static AffiliationDemandViewImplUiBinder uiBinder = GWT
         .create(AffiliationDemandViewImplUiBinder.class);
   private final MotherActionEnum actionMotherAffiliation;
   @UiField
   VerticalPanel                   formPanel, noMotherPanel;
   @UiField
   Label                           affiliationDemandTitle, noMotherLabel;
   @UiField
   Grid  affiliationDemandGrid;
   @UiField
   Label          motherL;
   @UiField
   Label          reasonL;
   @UiField
   ListBox motherLB;
   @UiField
   TextAreaValidation affiliationReason;
   @UiField
   Button                          createAffiliationDemandB;
   @UiField
   Button                          cancelAffiliationDemandB;
   private ForgeValidateDialogBox validateDialogBox;
   /**
    * Form displayed to subscribe a mother forge or when a subscription request has already been send
    *
    * @param actionMotherAffiliation
    */
   public SubscriptionViewImpl(MotherActionEnum actionMotherAffiliation) {
      this.actionMotherAffiliation = actionMotherAffiliation;
      initWidget(uiBinder.createAndBindUi(this));

      switch (this.actionMotherAffiliation) {
      case DO:
         commonDoAndDone();
         affiliationDemandTitle.setText(Common.MESSAGES.doMotherAffiliationTitle());
         createAffiliationDemandB.setText(Common.MESSAGES.send());
         cancelAffiliationDemandB.setText(Common.MESSAGES.cancel());
         break;
      case DOING:
         commonDoAndDone();
         affiliationDemandTitle.setText(Common.MESSAGES.doingMotherAffiliationTitle());
         affiliationReason.setValue(Common.MESSAGES.reason());
         motherLB.setEnabled(false);
         affiliationReason.setEnable(false);
         createAffiliationDemandB.setVisible(false);
         cancelAffiliationDemandB.setVisible(false);
         break;
      case NONE:
         formPanel.setVisible(false);
         break;
      }

   }

   private void commonDoAndDone()
   {
      motherL.setText(Common.MESSAGES.name());
      reasonL.setText(Common.MESSAGES.reason());
      affiliationReason.setCharacterWidth(Common.TEXT_AREA_WIDTH);
      affiliationReason.setVisibleLines(Common.TEXT_AREA_HEIGHT);
      noMotherPanel.setVisible(false);

      // Initialization of validation popup
      validateDialogBox = new ForgeValidateDialogBox(Common.MESSAGES.demandValidationMessage());
      // Initialization row style
      for (int row = 0; row < affiliationDemandGrid.getRowCount(); row++)
      {
         if (row % 2 == 0)
         {
            affiliationDemandGrid.getRowFormatter().addStyleName(row,
                  Common.RESOURCE.css().gridRowPair());
         }
         affiliationDemandGrid.getCellFormatter()
         .addStyleName(row, 0, Common.RESOURCE.css().labelCell());
      }

   }

   @Override
   public HasClickHandlers getCreateAffiliationDemandButton() {
      return createAffiliationDemandB;
   }

   @Override
   public HasClickHandlers getCancelAffiliationDemandButton() {
      return cancelAffiliationDemandB;
   }

   @Override
   public ListBox getMother()
   {
      return motherLB;
   }

   @Override
   public TextAreaValidation getReasonTB() {
      return affiliationReason;
   }

   @Override
   public ForgeValidateDialogBox getValidateDialogBox() {
      return validateDialogBox;
   }

   @Override
   public void setMotherMessage(String pMessage)
   {
      noMotherLabel.setText(pMessage);
   }

   interface AffiliationDemandViewImplUiBinder extends UiBinder<Widget, SubscriptionViewImpl>
   {
   }



}
