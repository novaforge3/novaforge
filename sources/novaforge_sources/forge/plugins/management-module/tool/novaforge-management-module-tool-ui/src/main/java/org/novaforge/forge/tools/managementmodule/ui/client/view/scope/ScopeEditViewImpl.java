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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

/**
 * @author vvigo
 */
public class ScopeEditViewImpl extends PopupPanel implements ScopeEditView
{

   private static DeliveryManagementViewImplUiBinder uiBinder = GWT.create(DeliveryManagementViewImplUiBinder.class);
   @UiField
   Label   scopeManualCreationTitle;
   @UiField
   Label   scopeNameLabel;
   @UiField
   Label    scopeDescriptionLabel;
   @UiField
   Label   scopeVersionLabel;
   @UiField
   Label   scopeTypeLabel;
   @UiField
   Label   scopeLotLabel;
   @UiField
   Label   parentScopeLabel;
   @UiField
   TextBox scopeNameTB;
   @UiField
   TextArea scopeDescriptionTB;
   @UiField
   TextBox scopeVersionTB;
   @UiField
   ListBox scopeTypeLB;
   @UiField
   ListBox scopeLotLB;
   @UiField
   CustomListBox<ScopeUnitDTO> parentScopeLB;
   @UiField
   Button  buttonSave;
   @UiField
   Button  buttonCancel;
   private ViewEnum mode;

   public ScopeEditViewImpl(ViewEnum pMode)
   {
      Common.getResource().css().ensureInjected();
      add(uiBinder.createAndBindUi(this));

      scopeNameLabel.setText(Common.getGlobal().name());
      scopeDescriptionLabel.setText(Common.getGlobal().description());
      scopeVersionLabel.setText(Common.getGlobal().version());
      scopeTypeLabel.setText(Common.getGlobal().type());
      scopeLotLabel.setText(Common.MESSAGES_SCOPE.scopeLotSearchLabel());
      parentScopeLabel.setText(Common.MESSAGES_SCOPE.parentScopeLabel());
      parentScopeLB.setEnabled(false);

      setMode(pMode, true);

      buttonCancel.setText(Common.MESSAGES_SCOPE.cancelButton());

      setModal(true);
      setGlassEnabled(true);
   }

   @Override
   public TextBox getScopeNameTB()
   {
      return scopeNameTB;
   }

   @Override
   public TextArea getScopeDescriptionTB()
   {
      return scopeDescriptionTB;
   }

   @Override
   public ListBox getScopeTypeLB()
   {
      return scopeTypeLB;
   }

   @Override
   public TextBox getScopeVersionTB()
   {
      return scopeVersionTB;
   }

   @Override
   public ListBox getScopeLotLB()
   {
      return scopeLotLB;
   }

   @Override
   public CustomListBox<ScopeUnitDTO> getParentScopeLB()
   {
      return parentScopeLB;
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

   @Override
   public void setMode(ViewEnum pMode, Boolean isManual)
   {
      this.mode = pMode;
      if (ViewEnum.ADD.equals(mode))
      {
         scopeManualCreationTitle.setText(Common.MESSAGES_SCOPE.scopeManualCreationTitle());
         buttonSave.setText(Common.MESSAGES_SCOPE.saveButtonCreate());
      }
      else
      {
         buttonSave.setText(Common.MESSAGES_SCOPE.saveButtonEdit());
         if (isManual)
         {
            scopeManualCreationTitle.setText(Common.MESSAGES_SCOPE.scopeManualEditTitle());
         }
         else
         {
            scopeManualCreationTitle.setText(Common.MESSAGES_SCOPE.scopeEditTitle());
         }
      }
   }

   interface DeliveryManagementViewImplUiBinder extends UiBinder<Widget, ScopeEditViewImpl>
   {
   }
}
