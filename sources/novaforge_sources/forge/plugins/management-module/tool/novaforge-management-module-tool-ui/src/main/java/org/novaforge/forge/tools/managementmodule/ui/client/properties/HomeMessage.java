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
package org.novaforge.forge.tools.managementmodule.ui.client.properties;

import com.google.gwt.i18n.client.Messages;

/**
 * @author BILET-JC
 *
 */
public interface HomeMessage extends Messages {
	
	String administration();
	String projectPlan();
	String refIteration();
	String ard();
	
	/* project plan */
	String lot();
	String iteration();
	String estimation();
	String projectPlanList();
	String marker();
	String scope();
	/**
	 * Get the message for chargePlan link
	 * @return the appropriate string
	 */
	String chargePlan();
	
	/* iteration */
	String backlogList();
	String scopeUnitDiscipline();
	String preparation();
	String globalMonitoring();
	String refIt();
	String reportIt();
	String burndown();
	String reportDraw();
	/**
    * Get the message for initialSettings link
    * @return the appropriate string
    */
   String initialSettings();
   /**
    * Get the message for projectPlanSettings link
    * @return the appropriate string
    */
   String projectPlanSettings();
   
}
