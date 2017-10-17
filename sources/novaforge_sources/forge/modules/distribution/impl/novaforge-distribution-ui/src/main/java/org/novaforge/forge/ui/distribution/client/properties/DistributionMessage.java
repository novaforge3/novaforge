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

package org.novaforge.forge.ui.distribution.client.properties;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface DistributionMessage extends Messages
{
   String forgeAffiliationListTitle();

   String emptyForgeAffiliationMessage();

   String nameColumn();

   String typeColumn();

   String levelColumn();

   String dateColumn();

   String linkColumn();

   String validationColumn();

   String invalidationColumn();

   String forgeAffiliationDemandListTitle();

   String emptyForgeAffiliationDemandMessage();

   String emptyForgeViewType();

   String emptyOrganizationViewType();

   String emptyProfilViewType();

   String doMotherAffiliation();

   String doingMotherAffiliation();

   String doneMotherAffiliation();

   String doMotherAffiliationTitle();

   String doingMotherAffiliationTitle();

   String doneMotherAffiliationTitle();

   String forgeViewTypeTitle();

   String organizationViewTypeTitle();

   String profilViewTypeTitle();

   String url();

   String reason();

   String name();

   String level();

   String date();

   String total();

   String viewType();

   String send();

   String cancel();

   String buttonClose();

   String popupInfo();

   String popupValidate();

   SafeHtml buttonValidate();

   SafeHtml buttonUnValidate();

   String demandValidationMessage();

   String sendUnsubscribe();

   String addValidationMessage();

   String invalidationMessage();

   String disaffiliationMessage();

   String invalidateSuccessful();

   String validateSuccessful();

   String validateSyncImmediate();

   String validateSyncDiff();

   String dateDemandColumn();

   String reasonColumn();

   String confirm();

   String unsubscriptionRaison();

   String unsubscriptionSuccessful();

   String subscriptionSuccessful();

   String disaffiliateSuccessful();

   String disaffiliationColumn();

   String forgeColumn();

   String organizationColumn();

   String projectColumn();

   String nbProjectsColumn();

   String nbAccountsColumn();

   String subscriptionEnum();

   String unsubscriptionEnum();

   String orphanEnum();

   String suspendedEnum();

   String localEnum();

   String zonalEnum();

   String centralEnum();

   String forgeEnum();

   String organizationEnum();

   String profilEnum();

   String headerDaughterTab();

   String headerMotherTab();

   String headerReportingTab();

   String noMotherText();

   String noDaughterText();

   String noReferenceText();

   String noReportingText();

   String doingUnsubscription();

   String eAffiliationUrlValidation();

   String eMaxLenght();

   String eNoMother();

   String eValidation();

   String eLoadingListForge();

   String eLoadingListMother();

   String eLoadingDistribution();

   String eLoadingSubscription();

   String eTechnique();

   String eLevel();

   String eSubscriptionDemand();

   String eDateDisplay();

   String eMother();

   String eLoadingForgeView();

   String eLoadingOrganizationView();

   String eLoadingProfilView();

   String noDistributionText();

   String noMotherListText();

   String noDistributionForReportingText();

   String eDistributionDown();

   String headerDiffusionTab();

   String headerDiffusionDiffTab();
   
   String noUpdateFound (); 
   
   String updatedDistributionForgeNameColumn ();
   
   String updatedDistributionForgeDateColumn (); 
   
}
