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
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;

/**
 * @author FALSQUELLE-E
 */
public class DeliveryTabViewImpl extends Composite implements DeliveryTabView
{

   private static final String                STYLE_TABULATION_PANEL = "tabulationPanel";

   private static DeliveryTabViewImplUiBinder uiBinder               = GWT.create(DeliveryTabViewImplUiBinder.class);
   @UiField
   TabLayoutPanel tabLayoutPanel;
   @UiField
   Button         returnButton;
   ValidateDialogBox returnValidation;
   public DeliveryTabViewImpl()
   {
      Common.getResources().css().ensureInjected();

      this.returnValidation = new ValidateDialogBox(DeliveryCommon.getMessages().returnMessage());

      this.initWidget(uiBinder.createAndBindUi(this));

      this.tabLayoutPanel.setHeight("700px");
      this.tabLayoutPanel.setAnimationDuration(1000);
      this.tabLayoutPanel.setStyleName(STYLE_TABULATION_PANEL);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TabLayoutPanel getTabPanel()
   {
      return this.tabLayoutPanel;
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
   public ValidateDialogBox getReturnValidation()
   {
      return this.returnValidation;
   }

   interface DeliveryTabViewImplUiBinder extends UiBinder<Widget, DeliveryTabViewImpl>
   {
   }
}
