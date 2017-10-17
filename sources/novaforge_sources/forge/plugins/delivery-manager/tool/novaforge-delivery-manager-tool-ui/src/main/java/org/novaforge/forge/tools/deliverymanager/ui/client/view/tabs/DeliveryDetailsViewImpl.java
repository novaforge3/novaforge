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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.validation.SuggestBoxValidation;
import org.novaforge.forge.ui.commons.client.validation.TextBoxValidation;

import java.util.List;

/**
 * @author FALSQUELLE-E
 * @author Guillaume Lamirand
 */
public class DeliveryDetailsViewImpl extends Composite implements DeliveryDetailsView
{

   private static DeliveryDetailsViewImplUiBinder uiBinder = GWT.create(DeliveryDetailsViewImplUiBinder.class);
   @UiField
   Label                deliveryDetailTitle;
   @UiField
   Label                deliveryInfo;
   @UiField
   Grid                 generalGrid;
   @UiField
   Label                nameLabel;
   @UiField
   TextBoxValidation    nameTB;
   @UiField
   Label                versionLabel;
   @UiField
   TextBoxValidation    versionTB;
   @UiField
   Label                typeLabel;
   @UiField(provided = true)
   SuggestBoxValidation typeSuggest;
   @UiField
   Button               buttonSaveDelivery;
   @UiField
   Label                referenceLabel;
   @UiField
   TextBoxValidation    referenceTB;
   @UiField
   Label                deliveryContentTitle;
   @UiField
   Label                contentInfo;
   @UiField
   Grid                 contentGrid;
   @UiField
   Label                ecmLabel;
   @UiField
   CheckBox             ecmCheck;
   @UiField
   TextBox              ecmDirectory;
   @UiField
   Label                ecmUnavailable;
   @UiField
   Label                scmLabel;
   @UiField
   CheckBox             scmCheck;
   @UiField
   TextBox              scmDirectory;
   @UiField
   Label                scmUnavailable;
   @UiField
   Label                binaryLabel;
   @UiField
   CheckBox             binaryCheck;
   @UiField
   TextBox              binaryDirectory;
   @UiField
   Label                binaryUnavailable;
   @UiField
   Label                bugLabel;
   @UiField
   CheckBox             bugCheck;
   @UiField
   Label                bugUnavailable;
   @UiField
   Label                noteLabel;
   @UiField
   CheckBox             noteCheck;
   @UiField
   TextBox              noteDirectory;
   @UiField
   Label                noteUnavailable;
   /**
    * Default constructor
    */
   public DeliveryDetailsViewImpl()
   {
      DeliveryCommon.getResources().css().ensureInjected();

      this.typeSuggest = new SuggestBoxValidation(DeliveryCommon.getMessages().typeSuggestBox());
      this.initWidget(uiBinder.createAndBindUi(this));

      this.nameLabel.setText(this.formStyle(DeliveryCommon.getMessages().name()));
      this.versionLabel.setText(this.formStyle(DeliveryCommon.getMessages().version()));
      this.typeLabel.setText(this.formStyle(DeliveryCommon.getMessages().type()));
      this.referenceLabel.setText(this.formStyle(DeliveryCommon.getMessages().reference()));

      this.buttonSaveDelivery.setText(Common.getMessages().save());

      this.nameTB.ensureDebugId("name");
      this.versionTB.ensureDebugId("version");

      this.ecmCheck.addValueChangeHandler(new ValueChangeHandler<Boolean>()
      {
         @Override
         public void onValueChange(final ValueChangeEvent<Boolean> pEvent)
         {
            DeliveryDetailsViewImpl.this.ecmDirectory.setValue(Common.EMPTY_STRING);
            if (pEvent.getValue())
            {
               DeliveryDetailsViewImpl.this.ecmDirectory.setText(DeliveryCommon.getMessages()
                     .ecmDefaultDirectory());
               DeliveryDetailsViewImpl.this.ecmDirectory.setVisible(true);
               DeliveryDetailsViewImpl.this.ecmCheck.setTitle(DeliveryCommon.getMessages()
                     .deleteContentToolTip());
            }
            else
            {
               DeliveryDetailsViewImpl.this.ecmDirectory.setVisible(false);
               DeliveryDetailsViewImpl.this.ecmCheck.setTitle(DeliveryCommon.getMessages()
                     .addContentToolTip());
            }
         }
      });
      this.scmCheck.addValueChangeHandler(new ValueChangeHandler<Boolean>()
      {

         @Override
         public void onValueChange(final ValueChangeEvent<Boolean> pEvent)
         {
            DeliveryDetailsViewImpl.this.scmDirectory.setValue(Common.EMPTY_STRING);
            if (pEvent.getValue())
            {
               DeliveryDetailsViewImpl.this.scmDirectory.setText(DeliveryCommon.getMessages()
                     .scmDefaultDirectory());
               DeliveryDetailsViewImpl.this.scmDirectory.setVisible(true);
               DeliveryDetailsViewImpl.this.scmCheck.setTitle(DeliveryCommon.getMessages()
                     .deleteContentToolTip());
            }
            else
            {
               DeliveryDetailsViewImpl.this.scmDirectory.setVisible(false);
               DeliveryDetailsViewImpl.this.scmCheck.setTitle(DeliveryCommon.getMessages()
                     .addContentToolTip());
            }

         }
      });
      this.binaryCheck.addValueChangeHandler(new ValueChangeHandler<Boolean>()
      {

         @Override
         public void onValueChange(final ValueChangeEvent<Boolean> pEvent)
         {
            DeliveryDetailsViewImpl.this.binaryDirectory.setValue(Common.EMPTY_STRING);
            if (pEvent.getValue())
            {
               DeliveryDetailsViewImpl.this.binaryDirectory.setText(DeliveryCommon.getMessages()
                     .binaryDefaultDirectory());
               DeliveryDetailsViewImpl.this.binaryDirectory.setVisible(true);
               DeliveryDetailsViewImpl.this.binaryCheck.setTitle(DeliveryCommon.getMessages()
                     .deleteContentToolTip());
            }
            else
            {
               DeliveryDetailsViewImpl.this.binaryDirectory.setVisible(false);
               DeliveryDetailsViewImpl.this.binaryCheck.setTitle(DeliveryCommon.getMessages()
                     .addContentToolTip());
            }

         }
      });
      this.noteCheck.addValueChangeHandler(new ValueChangeHandler<Boolean>()
      {

         @Override
         public void onValueChange(final ValueChangeEvent<Boolean> pEvent)
         {
            DeliveryDetailsViewImpl.this.noteDirectory.setValue(Common.EMPTY_STRING);
            if (pEvent.getValue())
            {
               DeliveryDetailsViewImpl.this.noteDirectory.setText(DeliveryCommon.getMessages()
                     .noteDefaultDirectory());
               DeliveryDetailsViewImpl.this.noteDirectory.setVisible(true);
               DeliveryDetailsViewImpl.this.noteCheck.setTitle(DeliveryCommon.getMessages()
                     .deleteContentToolTip());
            }
            else
            {
               DeliveryDetailsViewImpl.this.noteDirectory.setVisible(false);
               DeliveryDetailsViewImpl.this.noteCheck.setTitle(DeliveryCommon.getMessages()
                     .addContentToolTip());
            }

         }
      });

      // Initialization row style
      for (int row = 0; row < this.generalGrid.getRowCount(); row++)
      {
         if ((row % 2) == 0)
         {
            this.generalGrid.getRowFormatter().addStyleName(row, Common.getResources().css().gridRowPair());
         }
         this.generalGrid.getCellFormatter().addStyleName(row, 0, Common.getResources().css().labelCell());
      }
      for (int row = 0; row < this.contentGrid.getRowCount(); row++)
      {
         if ((row % 2) == 0)
         {
            this.contentGrid.getRowFormatter().addStyleName(row, Common.getResources().css().gridRowPair());
         }
         this.contentGrid.getCellFormatter().addStyleName(row, 0, Common.getResources().css().labelCell());
         this.contentGrid.getCellFormatter().addStyleName(row, 2,
               DeliveryCommon.getResources().css().contentDirectoryCell());
      }

   }

   private String formStyle(final String pText)
   {
      return pText + " : ";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TextBoxValidation getName()
   {
      return this.nameTB;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TextBoxValidation getVersion()
   {
      return this.versionTB;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TextBoxValidation getReference()
   {
      return this.referenceTB;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public SuggestBoxValidation getType()
   {
      return this.typeSuggest;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Button getButtonSaveDelivery()
   {
      return this.buttonSaveDelivery;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public CheckBox getBugContent()
   {
      return this.bugCheck;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public CheckBox getScmContent()
   {
      return this.scmCheck;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public CheckBox getBinaryContent()
   {
      return this.binaryCheck;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public CheckBox getEcmContent()
   {
      return this.ecmCheck;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TextBox getEcmDirectory()
   {
      return this.ecmDirectory;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TextBox getScmDirectory()
   {
      return this.scmDirectory;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TextBox getBinaryDirectory()
   {
      return this.binaryDirectory;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HasValue<Boolean> getNoteContent()
   {
      return this.noteCheck;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HasValue<String> getNoteDirectory()
   {
      return this.noteDirectory;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setTypes(final List<String> pTypes)
   {
      final SuggestOracle suggestOracle = this.typeSuggest.getSuggextBox().getSuggestOracle();
      if (suggestOracle instanceof MultiWordSuggestOracle)
      {
         for (final String suggestion : pTypes)
         {
            ((MultiWordSuggestOracle) suggestOracle).add(suggestion);
         }
      }
      ((MultiWordSuggestOracle) suggestOracle).setDefaultSuggestionsFromText(pTypes);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void cleanContent()
   {
      this.ecmCheck.setValue(false, true);
      this.ecmCheck.setEnabled(false);
      this.ecmDirectory.setValue("");
      this.setEcmAvailable(false);
      this.scmCheck.setValue(false, true);
      this.scmCheck.setEnabled(false);
      this.scmDirectory.setValue("");
      this.setScmAvailable(false);
      this.binaryCheck.setValue(false, true);
      this.binaryCheck.setEnabled(false);
      this.binaryDirectory.setValue("");
      this.setBinaryAvailable(false);
      this.bugCheck.setValue(false, true);
      this.bugCheck.setEnabled(false);
      this.setBugAvailable(false);
      this.noteCheck.setValue(false, true);
      this.noteCheck.setEnabled(false);
      this.noteDirectory.setValue("");
      this.setNoteAvailable(false);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setBinaryAvailable(final boolean pAvailable)
   {
      this.binaryCheck.setEnabled(pAvailable);
      this.binaryDirectory.setEnabled(pAvailable);
      this.binaryUnavailable.setVisible(!pAvailable);
      if (pAvailable)
      {
         this.binaryLabel.removeStyleName(Common.getResources().css().emptyLabel());
         if (this.binaryCheck.getValue())
         {
            this.binaryDirectory.setVisible(pAvailable);
         }
      }
      else
      {
         this.binaryDirectory.setVisible(pAvailable);
         this.binaryLabel.addStyleName(Common.getResources().css().emptyLabel());

      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setNoteAvailable(final boolean pAvailable)
   {
      this.noteCheck.setEnabled(pAvailable);
      this.noteDirectory.setEnabled(pAvailable);
      this.noteUnavailable.setVisible(!pAvailable);
      if (pAvailable)
      {
         this.noteLabel.removeStyleName(Common.getResources().css().emptyLabel());
         if (this.noteCheck.getValue())
         {
            this.noteDirectory.setVisible(pAvailable);
         }
      }
      else
      {
         this.noteDirectory.setVisible(pAvailable);
         this.noteLabel.addStyleName(Common.getResources().css().emptyLabel());

      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setBugAvailable(final boolean pAvailable)
   {
      this.bugCheck.setEnabled(pAvailable);
      this.bugUnavailable.setVisible(!pAvailable);
      if (pAvailable)
      {
         this.bugLabel.removeStyleName(Common.getResources().css().emptyLabel());
      }
      else
      {
         this.bugLabel.addStyleName(Common.getResources().css().emptyLabel());

      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setScmAvailable(final boolean pAvailable)
   {
      this.scmCheck.setEnabled(pAvailable);
      this.scmDirectory.setEnabled(pAvailable);
      this.scmUnavailable.setVisible(!pAvailable);
      if (pAvailable)
      {
         this.scmLabel.removeStyleName(Common.getResources().css().emptyLabel());
         if (this.scmCheck.getValue())
         {
            this.scmDirectory.setVisible(pAvailable);
         }
      }
      else
      {
         this.scmDirectory.setVisible(pAvailable);
         this.scmLabel.addStyleName(Common.getResources().css().emptyLabel());

      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setEcmAvailable(final boolean pAvailable)
   {
      this.ecmCheck.setEnabled(pAvailable);
      this.ecmDirectory.setEnabled(pAvailable);
      this.ecmUnavailable.setVisible(!pAvailable);
      if (pAvailable)
      {
         this.ecmLabel.removeStyleName(Common.getResources().css().emptyLabel());
         if (this.ecmCheck.getValue())
         {
            this.ecmDirectory.setVisible(pAvailable);
         }
      }
      else
      {
         this.ecmDirectory.setVisible(pAvailable);
         this.ecmLabel.addStyleName(Common.getResources().css().emptyLabel());

      }
   }

   interface DeliveryDetailsViewImplUiBinder extends UiBinder<Widget, DeliveryDetailsViewImpl>
   {
   }

}
