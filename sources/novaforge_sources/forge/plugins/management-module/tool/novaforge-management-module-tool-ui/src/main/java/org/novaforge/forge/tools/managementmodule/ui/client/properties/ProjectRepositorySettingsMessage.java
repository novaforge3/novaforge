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

package org.novaforge.forge.tools.managementmodule.ui.client.properties;

import com.google.gwt.i18n.client.Messages;

/**
 * @author sivan-q
 */
public interface ProjectRepositorySettingsMessage extends Messages {
	
	String projectRepositorySettingsTitle();
	
	String inputETPLabel();
	
	String uniteTempsIterationLabel();
	
	String uniteTempsJourLabel();
	
	String uniteTempsSemaineLabel();
	
	String uniteTempsMoisLabel();
	
	String transformationLabel();
	
	String inputNbrDeJourParAnLabel();
	
	String inputNbrDeJourParMoisLabel();
	
	String inputNbrDeJourParSemaineLabel();
	
	String inputNbrHeuresParJourLabel();
	
	String inputNbJoursNonTravailLabel();
	
	String pointFonctionSimpleLabel();
	
	String inputPourcentageGDILabel();
	
	String inputPourcentageENTLabel();
	
	String inputPourcentageINTLabel();
	
	String inputPourcentageSORLabel();
	
	String inputPourcentageGDELabel();
	
	String simpleLabel();
	
	String moyenLabel();
	
	String complexeLabel();
	
	String pointFonctionDetailLabel();
	
	String pointFonctionLabel();
	
	String inputPourcentageRisqueLabel();
	
	String inputAbaqueChargeHJLabel();
	
	String tauxRepartitionLabel();
	
	String tauxRepartitionColumnLabel();
	
	String coefficientAjustementLabel();
	
	String valeurFacteurAjustementColumnLabel();
	
	String categorieLabel();
	
	String libelleCategorieColumnLabel();
	
	String suppressionCategorieColumnLabel();
	
	String ajouterCategorieLabel();
	
	String projectRepositorySettingsTabHeader();
	
	String projectPlanTabHeader();
	
	String projectPlanTitle();
	
	String tacheLabel();
	
	String libelleTacheColumnLabel();
	
	String suppressionTacheColumnLabel();
	
	String ajouterTacheLabel();
	
	String saveProjectParametersSuccessfull();
	
	String saveProjectParametersFailed();
	
	String pourcentageDisciplinesFalse();
	
	String loadDiscplinesFailed();
	
	String libelledisciplineTotal();
	
	String inputCdoParameterBLabel();
  

   /**
    * Get the message of the cancel modifications button
    * @return the message
    */
   String buttonCancelModifications();
   
   /**
    * Get the message of the "return to home" button
    * @return the message
    */
   String buttonHomeReturn();

   /**
    * Get the message of the category list label
    * @return the message
    */
   String categoryListLabel();

   /**
    * Get the message of GDI label
    * @return the message
    */
   String GDI();

   /**
    * Get the message of ENT label
    * @return the message
    */
   String IN();
   
   /**
    * Get the message of INT label
    * @return the message
    */
   String INT();

   /**
    * Get the message of SOR label
    * @return the message
    */
   String OUT();

   /**
    * Get the message of GDE label
    * @return the message
    */
   String GDE();

   /**
    * Get the message for the save success action
    * @return the message
    */
   String settingsSaved();

   
   /**
    * Get the message of the import RefScopeUnit fromCDO button
    * @return the message
    */
   String buttonImportRefScopeUnit();
   
   String parametrageCDOLabel();
   
   String inputHostCDOLabel();
   
   String inputPortCDOLabel();
   
   String inputRepositoryCDOLabel();
   
   String inputProjetCDOLabel();
   
   String inputSystemGraalCDOLabel();
   
   String inputCronExpressionLabel();
   
   String connectCDOProblem();
   
   String connectCDOOk();

   
   /** Get the message to display when the total of the simple component is different from 100*/
   String percentageSimpleComponentFalse();

   /** Get the message to display when the value is equal to 0 or empty */
   String needToBeNotEmptyOrZeroInt();
   
   String changeUnitTimeMessage();

   String noRepoInDataBase();
}
