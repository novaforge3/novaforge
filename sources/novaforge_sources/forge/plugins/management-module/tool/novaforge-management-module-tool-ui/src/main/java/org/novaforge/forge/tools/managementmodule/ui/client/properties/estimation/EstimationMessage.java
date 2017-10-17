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
package org.novaforge.forge.tools.managementmodule.ui.client.properties.estimation;

import com.google.gwt.i18n.client.Messages;

/**
 * @author BILET-JC
 * 
 */
public interface EstimationMessage extends Messages {

	String headerEstimationTab();

	String estimationTitle();

	String emptyEstimationMessage();

	String scopeUnitDetailsTitle();

	String disciplineSharingTitle();

	String lot();

	String sublot();

	String scopeUnitParent();

	String scopeUnit();

	String version();

	String weight();

	String risk();

	String participatingClasses();

	String charge();

	String lastCharge();

	String benefit();

	String injury();

	String priorizationType();

	String priorizationValue();

	String manual();

	String pf();

	String raw();

	String adjusted();

	String adjustment();

	String factor();

	String S();

	String M();

	String C();

	String gdi();

	String gde();

	String interrogation();

	String out();

	String in();

	String FPTitle();

	String FPIndicatorsTitle();

	String priorizationTitle();

	String scopeUnitsTitle();

	String totalTitle();

	String byFunctionPoints();

	String scopeUnitNameL();

	String scopeUnitDescriptionL();

	String scopeUnitStateL();

	String scopeUnitLastUpdateL();

	String scopeUnitParentL();

	String lotL();

	String subLotL();

	String totalL();

	String noParticipatingClasses();

	String detailled();

	String simplified();

	String global();

	String details();

	String HD();

	String HH();

	String acronym();

	String acronymTitle();

	String validationMessage();

	String noScopeUnitDesc();

	/*
	 * discipline sharing
	 */
	String architectureDesign();

	String businessModeling();

	String changeDriving();

	String configurationManagement();

	String implementation();

	String projectManagement();

	String qualityAssurance();

	String receipts();

	String requirementsAnalysis();

	String fpType();

}
