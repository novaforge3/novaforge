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

package org.novaforge.forge.ui.distribution.client.view.daughters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.validation.TextAreaValidation;
import org.novaforge.forge.ui.distribution.client.Common;

/**
 * @author BILET-JC
 *
 */
public class InvalidationReasonViewImpl extends Composite implements InvalidationReasonView
{

   private static invalidationReasonViewImplUiBinder uiBinder = GWT.create(invalidationReasonViewImplUiBinder.class);
   @UiField
   Label              invalidationReasonTitle;
   @UiField
   Grid               invalidationReasonGrid;
   @UiField
   Label              invalidationReasonL;
   @UiField
   TextAreaValidation invalidationReasonTA;
   @UiField
   Button             createInvalidationReasonB;
   @UiField
   Button             cancelInvalidationReasonB;
   public InvalidationReasonViewImpl(String pTitle)
   {
      initWidget(uiBinder.createAndBindUi(this));
      invalidationReasonTitle.setText(pTitle);
      invalidationReasonL.setText(Common.MESSAGES.reason());
      invalidationReasonTA.setCharacterWidth(Common.TEXT_AREA_WIDTH);
      invalidationReasonTA.setVisibleLines(Common.TEXT_AREA_HEIGHT);
      createInvalidationReasonB.setText(Common.MESSAGES.send());
      cancelInvalidationReasonB.setText(Common.MESSAGES.cancel());
   }

   @Override
   public HasClickHandlers getCancelInvalidationReasonB()
   {
      return cancelInvalidationReasonB;
   }

   @Override
   public HasClickHandlers getCreateInvalidationReasonB()
   {
      return createInvalidationReasonB;
   }

   @Override
   public TextAreaValidation getInvalidationReasonTA()
   {
      return invalidationReasonTA;
   }

   interface invalidationReasonViewImplUiBinder extends UiBinder<Widget, InvalidationReasonViewImpl>
   {

   }



}
