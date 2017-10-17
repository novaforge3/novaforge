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
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;

/**
 * @author BILET-JC
 */
public class MotherViewImpl extends Composite implements MotherView {

   private static MotherViewImplUiBinder uiBinder = GWT.create(MotherViewImplUiBinder.class);
   @UiField
   Label affiliationTitle;
   @UiField
   Grid  affiliationGrid;
   @UiField
   VerticalPanel                  formZone;
   @UiField
   Label                           urlLabel;
   @UiField
   Label          nameLabel;
   @UiField
   Label          dateLabel;
   @UiField
   Label          levelLabel;
   @UiField
   TextBox                         urlTB;
   @UiField
   TextBox nameTB;
   @UiField
   TextBox levelTB;
   @UiField
   TextBox dateTB;
   @UiField
   Button        createUnSubscriptionMotherB;
   /**
    * This view displays the current mother's information
    */
   public MotherViewImpl() {
      initWidget(uiBinder.createAndBindUi(this));
      affiliationTitle.setText(Common.MESSAGES.doneMotherAffiliationTitle());
      createUnSubscriptionMotherB.setText(Common.MESSAGES.sendUnsubscribe());
      urlLabel.setText(Common.MESSAGES.url());
      nameLabel.setText(Common.MESSAGES.name());
      levelLabel.setText(Common.MESSAGES.level());
      dateLabel.setText(Common.MESSAGES.date());
      urlTB.setEnabled(false);
      nameTB.setEnabled(false);
      levelTB.setEnabled(false);
      dateTB.setEnabled(false);
   }

   @Override
   public void displayMotherAffiliation(ForgeDTO pMotherForge, String pAffiliationDate)
   {
      urlTB.setText(pMotherForge.getUrl());
      nameTB.setText(pMotherForge.getLabel());
      levelTB.setText(Common.getLevelLabel(pMotherForge.getForgeLevel()));
      dateTB.setText(pAffiliationDate);
   }

   @Override
   public HasClickHandlers getUnsubscriptionMotherButton()
   {
      return createUnSubscriptionMotherB;
   }

   @Override
   public HasWidgets getFormZone()
   {
      return formZone;
   }

   @Override
   public void hideButton()
   {
      createUnSubscriptionMotherB.setText(Common.MESSAGES.doingUnsubscription());
      createUnSubscriptionMotherB.setEnabled(false);

   }

   interface MotherViewImplUiBinder extends UiBinder<Widget, MotherViewImpl>
   {
   }

}
