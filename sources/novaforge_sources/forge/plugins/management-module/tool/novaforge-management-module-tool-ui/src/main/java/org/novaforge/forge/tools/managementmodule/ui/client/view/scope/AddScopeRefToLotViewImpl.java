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

package org.novaforge.forge.tools.managementmodule.ui.client.view.scope;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * @author vvigo
 */
public class AddScopeRefToLotViewImpl extends PopupPanel implements AddScopeRefToLotView
{
   public static final String                        EDIT_MODE     = "EDIT_MODE";
   public static final String                        CREATE_MODE   = "CREATE_MODE";
   public static final String                        READONLY_MODE = "READONLY_MODE";

   private static DeliveryManagementViewImplUiBinder uiBinder = GWT.create(DeliveryManagementViewImplUiBinder.class);
   @UiField
   Label   addScopeRefToLotTitle;
   @UiField
   Label   scopeLotLabel;
   @UiField
   ListBox scopeLotLB;
   @UiField
   Button  buttonSave;
   @UiField
   Button  buttonCancel;
   public AddScopeRefToLotViewImpl()
   {

      Common.getResource().css().ensureInjected();
      add(uiBinder.createAndBindUi(this));

      addScopeRefToLotTitle.setText(Common.MESSAGES_SCOPE.addScopeRefToLotTitle());
      scopeLotLabel.setText(Common.MESSAGES_SCOPE.scopeLotSearchLabel());
      buttonSave.setText(Common.MESSAGES_SCOPE.buttonAddScopeUnit());
      buttonCancel.setText(Common.MESSAGES_SCOPE.cancelButton());

      setModal(true);
      setGlassEnabled(true);
   }

   @Override
   public ListBox getScopeLotLB()
   {
      return scopeLotLB;
   }

   @Override
   public void showWidget()
   {
      center();
      show();
   }

   @Override
   public void closeWidget()
   {
      hide();
   }

   @Override
   public Button getButtonSave()
   {
      return buttonSave;
   }

   @Override
   public Button getButtonCancel()
   {
      return buttonCancel;
   }

   interface DeliveryManagementViewImplUiBinder extends UiBinder<Widget, AddScopeRefToLotViewImpl>
   {
   }
}
