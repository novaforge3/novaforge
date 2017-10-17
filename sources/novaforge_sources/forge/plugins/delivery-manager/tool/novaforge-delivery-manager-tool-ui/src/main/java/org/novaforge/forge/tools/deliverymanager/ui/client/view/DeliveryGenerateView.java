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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import org.novaforge.forge.ui.commons.client.loading.LoadingPanel;

/**
 * @author CASERY-J
 */
public interface DeliveryGenerateView extends IsWidget
{

   /**
    * Get the generate delivery button
    * 
    * @return {@link Button} the buttonGenerateDelivery
    */
   Button getButtonGenerateDelivery();

   /**
    * Get the edit delivery button
    * 
    * @return {@link Button} the buttonEditDelivery
    */
   Button getButtonEditDelivery();

   /**
    * Get the delivery type value label
    * 
    * @return {@link Label} the deliveryTypeValue
    */
   Label getDeliveryTypeValue();

   /**
    * Get the delivery reference value label
    * 
    * @return {@link Label} the deliveryReferenceValue
    */
   Label getDeliveryReferenceValue();

   /**
    * Get the delivery version value label
    * 
    * @return {@link Label} the deliveryVersionValue
    */
   Label getDeliveryVersionValue();

   /**
    * Get the delivery name value label
    * 
    * @return {@link Label} the deliveryNameValue
    */
   Label getDeliveryNameValue();

   /**
    * Get the return to delivery list button
    * 
    * @return {@link Button} the return to list button
    */
   Button getReturnButton();

   /**
    * Get the Note content panel
    * 
    * @return {@link Panel} the note content panel
    */
   Panel getNotePanel();

   /**
    * Get the Bugs content panel
    * 
    * @return {@link Panel} the bugs content panel
    */
   Panel getBugsPanel();

   /**
    * Get the Files content panel
    * 
    * @return {@link Panel} the files content panel
    */
   Panel getFilesPanel();

   /**
    * Get the SCM content panel
    * 
    * @return {@link Panel} the scm content panel
    */
   Panel getScmPanel();

   /**
    * Get the ECM content panel
    * 
    * @return {@link Panel} the ecm content panel
    */
   Panel getEcmPanel();

   /**
    * Get the delivery details loading panel
    * 
    * @return {@link LoadingPanel} the delivery details loading panel
    */
   LoadingPanel getDeliveryDetailsLoadingPanel();

   /**
    * Get the Delivery Content Panel
    * 
    * @return {@link Panel} the delivery content panel
    */
   Panel getDeliveryContentPanel();

   /**
    * Get the Delivery Content Loading Panel
    * 
    * @return {@link LoadingPanel} the delivery content loading panel
    */
   LoadingPanel getDeliveryContentLoadingPanel();

   /**
    * @return
    */
   Label getContentNoteInfosValue();

   Label getContentBugsInfosValue();

   Label getContentFilesInfosValue();

   Label getContentSCMInfosValue();

   Label getContentECMInfosValue();

   Panel getDeliveryDetailsPanel();

}
