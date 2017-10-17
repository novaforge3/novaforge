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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import org.novaforge.forge.ui.commons.client.validation.SuggestBoxValidation;
import org.novaforge.forge.ui.commons.client.validation.TextBoxValidation;

import java.util.List;

/**
 * @author FALSQUELLE-E
 * @author Guillaume Lamirand
 */
public interface DeliveryDetailsView extends IsWidget
{
   /**
    * @return {@link TextBox} associated to the name element
    */
   TextBoxValidation getName();

   /**
    * @return {@link TextBox} associated to the version element
    */
   TextBoxValidation getVersion();

   /**
    * @return {@link TextBox} associated to the reference element
    */
   TextBoxValidation getReference();

   /**
    * @return {@link ListBox} associated to the type element
    */
   SuggestBoxValidation getType();

   /**
    * @return {@link Button} associated to the save event
    */
   Button getButtonSaveDelivery();

   HasValue<Boolean> getBugContent();

   HasValue<Boolean> getScmContent();

   /**
    * @return directory value for binary
    */
   HasValue<Boolean> getBinaryContent();

   HasValue<Boolean> getEcmContent();

   /**
    * @return directory value for ecm
    */
   HasValue<String> getEcmDirectory();

   /**
    * @return directory value for scm
    */
   HasValue<String> getScmDirectory();

   /**
    * @return directory value for binary
    */
   HasValue<String> getBinaryDirectory();

   HasValue<Boolean> getNoteContent();

   /**
    * @return directory value for note
    */
   HasValue<String> getNoteDirectory();

   /**
    * Set the parameter as suggest list
    * 
    * @param pResult
    */
   void setTypes(List<String> pResult);

   /**
    * This will reset the content panel
    */
   void cleanContent();

   /**
    * Allow to enable/disable the note content.It will :
    * <ul>
    * <li>add gray color to content label</li>
    * <li>set directory text box visibility to false</li>
    * <li>display an warning message in orange</li>
    * </ul>
    * 
    * @param pAvailable
    */
   void setBinaryAvailable(boolean pAvailable);

   /**
    * Allow to enable/disable the note content.It will :
    * <ul>
    * <li>add gray color to content label</li>
    * <li>set directory text box visibility to false</li>
    * <li>display an warning message in orange</li>
    * </ul>
    * 
    * @param pAvailable
    */
   void setNoteAvailable(boolean pAvailable);

   /**
    * Allow to enable/disable the bug content. It will :
    * <ul>
    * <li>add gray color to content label</li>
    * <li>set directory text box visibility to false</li>
    * <li>display an warning message in orange</li>
    * </ul>
    * 
    * @param pAvailable
    */
   void setBugAvailable(boolean pAvailable);

   /**
    * Allow to enable/disable the scm content. It will :
    * <ul>
    * <li>add gray color to content label</li>
    * <li>set directory text box visibility to false</li>
    * <li>display an warning message in orange</li>
    * </ul>
    * 
    * @param pAvailable
    */
   void setScmAvailable(boolean pAvailable);

   /**
    * Allow to enable/disable the note content.It will :
    * <ul>
    * <li>add gray color to content label</li>
    * <li>set directory text box visibility to false</li>
    * <li>display an warning message in orange</li>
    * </ul>
    * 
    * @param pAvailable
    */
   void setEcmAvailable(boolean pAvailable);

}
