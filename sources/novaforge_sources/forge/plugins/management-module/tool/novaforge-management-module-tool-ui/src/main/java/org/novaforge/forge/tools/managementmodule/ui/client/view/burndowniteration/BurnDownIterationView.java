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
package org.novaforge.forge.tools.managementmodule.ui.client.view.burndowniteration;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;

/**
 * @author falsquelle-e
 * 
 */
public interface BurnDownIterationView extends IsWidget {

   /**
    * Return the label for iteration
    * @return
    */
   Label getIterationName();

   /**
    * Return the label for the lot
    * @return
    */
   Label getLot();

   /**
    * Return the label for the sub-lot
    * @return
    */
   Label getParentLot();

   /**
    * Return the label for the startDate
    * @return
    */
   Label getStartDate();

   /**
    * Return the label for the endDate
    * @return
    */
   Label getEndDate();

   /**
    * Return the CustomListBox for the discipline
    * @return
    */
   CustomListBox<DisciplineDTO> getDisciplineLB();
   
   /**
    * Return the CustomListBox for the iteration
    * @return
    */
   CustomListBox<IterationDTO> getIterationLB();   

   /**
    * Return the Button who generates the diagrams
    * @return
    */
   Button getButtonGenerateDiagram();

   /**
    * returns the FirstPanel
    * @return
    */
   VerticalPanel getDiagramsPanel();

   /**
    * Return the Button who display the HomePage
    * @return
    */
   Button getButtonHomeReturn();
	
}
