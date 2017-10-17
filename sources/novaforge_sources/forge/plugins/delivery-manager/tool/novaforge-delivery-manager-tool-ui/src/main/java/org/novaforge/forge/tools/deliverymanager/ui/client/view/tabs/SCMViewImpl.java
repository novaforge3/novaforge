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

package org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.commons.celltree.nodesselection.NodesSelectionTrees;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;

/**
 * @author caseryj
 */
public class SCMViewImpl extends Composite implements SCMView
{

   private static SCMViewImplUiBinder uiBinder = GWT.create(SCMViewImplUiBinder.class);
   private final ValidateDialogBox cancelValidateDialogBox;
   private final InfoDialogBox     nodeUnexistDialogBox;
   private final InfoDialogBox     saveResultDialogBox;
   @UiField
   Label                           deliveryTitleLabel;
   @UiField(provided = true)
   NodesSelectionTrees             filesSelectionTrees;
   @UiField
   Button                          buttonSaveDocumentsDelivery;

   public SCMViewImpl()
   {

      Common.getResources().css().ensureInjected();

      this.filesSelectionTrees = new NodesSelectionTrees(true, true);

      this.initWidget(uiBinder.createAndBindUi(this));

      this.filesSelectionTrees.setAvailableErrorMessageLoading(DeliveryCommon.getMessages()
            .scmTreeUnavailable());
      this.deliveryTitleLabel.setText(DeliveryCommon.getMessages().scmTitle());
      this.buttonSaveDocumentsDelivery.setText(Common.getMessages().save());

      this.cancelValidateDialogBox = new ValidateDialogBox(DeliveryCommon.getMessages().cancelMessage());
      this.nodeUnexistDialogBox = new InfoDialogBox(DeliveryCommon.getMessages()
            .selectedSCMNodeHasUnexistingNode(), InfoTypeEnum.WARNING);
      this.saveResultDialogBox = new InfoDialogBox(
            DeliveryCommon.getMessages().saveSCMDocumentsSuccessfull(), InfoTypeEnum.SUCCESS, 2500);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Label deliveryTitleLabel()
   {
      return this.deliveryTitleLabel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Button getButtonSaveDocumentsDelivery()
   {
      return this.buttonSaveDocumentsDelivery;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public NodesSelectionTrees getFilesSelectionTrees()
   {
      return this.filesSelectionTrees;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ValidateDialogBox getCancelValidateDialogBox()
   {
      return this.cancelValidateDialogBox;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public InfoDialogBox getNodeUnexistDialogBox()
   {
      return this.nodeUnexistDialogBox;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public InfoDialogBox getSaveResultDialogBox()
   {
      return this.saveResultDialogBox;
   }

   interface SCMViewImplUiBinder extends UiBinder<Widget, SCMViewImpl>
   {
   }
}
