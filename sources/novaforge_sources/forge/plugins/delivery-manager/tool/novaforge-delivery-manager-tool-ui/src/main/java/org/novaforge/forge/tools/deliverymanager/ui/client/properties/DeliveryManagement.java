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
package org.novaforge.forge.tools.deliverymanager.ui.client.properties;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

/**
 * @author FALSQUELLE-E
 * @author Guillaume Lamirand
 */
@DefaultLocale("en")
public interface DeliveryManagement extends Messages
{
  String headerGeneralTab();

  String headerGCLTab();

  String headerGEDTab();

  String headerBinariesTab();

  String headerBugsTab();

  String headerNoteTab();

  String type();

  String name();

  String size();

  String buttonCreateDelivery();

  String buttonManageDeliveryNote();

  String validateDeliveryCreateMessage();

  String version();

  String status();

  String deliveryDate();

  String deliveryManagementTitle();

  String emptyDeliveryFilterMessage();

  String saveSuccess();

  String filterTitle();

  String resultTitle();

  String modify();

  String deleteColumn();

  String reference();

  String deliveryCreated();

  String deliveryModified();

  String deliveryGenerating();

  String deliveryGenerated();

  String deliveryDeployed();

  String deliveryDelivered();

  String delivery();

  String patch();

  String actions();

  String download();

  String delete();

  String edit();

  String generate();

  String generateDeliveryTitle();

  String summaryDeliveryTitle();

  String generationSuccess();

  String generationFailed();

  String returnList();

  String returnTemplateList();

  String returnGeneration();

  String manageDeliveryTitle();

  String deliveryContentTitle();

  String deliveryInfo();

  String ecmContent();

  String ecmTitle();

  String ecmDefaultDirectory();

  String scmTitle();

  String scmContent();

  String scmDefaultDirectory();

  String binaryContent();

  String binaryDefaultDirectory();

  String bugContent();

  String noteContent();

  String noteDefaultDirectory();

  String contentInfo();

  String directoryToolTip();

  String addContentToolTip();

  String deleteContentToolTip();

  String deliveryBinariesTitle();

  String deliveryBinariesUpload();

  String deliveryBinariesList();

  String emptyBinariesMessage();

  String fileNameExist();

  String deliveryBinariesExt();

  String deliveryBinariesExtFileName();

  String uploadButton();

  String cancelMessage();

  String deleteBinary();

  String receiveValid();

  String sizeO();

  String sizeKo();

  String sizeMo();

  String source();

  String locale();

  String loading();

  String remote();

  String urlUnknown();

  String editFileName();

  String versionTitle();

  String versionInfo();

  String title();

  String description();

  String severity();

  String category();

  String reporter();

  String bugsTitle();

  String bugsInfo();

  String displayBugs();

  String typeBox();

  String statusBox();

  String issueVersionBox();

  String noteTemplateNameBox();

  String emptyBugsMessage();

  String versionLabel();

  String versionSaveSuccess();

  String selectedECMNodeHasUnexistingNode();

  String saveECMDocumentsSuccessfull();

  String saveSCMDocumentsSuccessfull();

  String selectedSCMNodeHasUnexistingNode();

  String noteTitle();

  String noteInfo();

  String deliveryManagementNotesTitle();

  String createDeliveryNoteTemplateButton();

  String deliveryNotesTitle();

  String emptyDeliveryNoteTemplateMessage();

  String createNoteTemplateTitle();

  String editNoteTemplateTitle();

  String templateFile();

  String noteTemplateFieldsTitle();

  String noteTemplateFieldsInfo();

  String addField();

  String noteTemplateFieldTypeChoice();

  String createFormNoteTemplate();

  String templateName();

  String templateNameValidation();

  String templateDescription();

  String templateDescriptionValidation();

  String emptyFieldMessage();

  String textArea();

  String textBox();

  String emptyForm();

  String saveMessage();

  String returnListMessage();

  String formErrorValidation();

  String uploadTemplateFile();

  String noteTemplate();

  String templateTitle();

  String templateInfo();

  String noteTemplateName();

  String noteTemplateDescription();

  String noteTemplateValue();

  String noteTemplateValidation();

  String generateDelivery();

  String deliveryGeneratedFileName();

  String generateContentECMInfo(String paramString);

  String generateContentSCMInfo(String paramString);

  String generateContentFilesInfo(String paramString);

  String generateContentBugsInfo(String paramString);

  String generateContentNoteInfo(String paramString);

  String editNoteTemplate();

  String deleteNoteTemplate();

  String saveNoteTemplate();

  String lockDelivery();

  String nameValidation();

  String versionValidation();

  String referenceValidation();

  String typeValidation();

  String saveDelivery();

  String deleteFileMessage();

  String typeSuggestBox();

  String binaryUnavailable();

  String scmUnavailable();

  String scmTreeUnavailable();

  String ecmUnavailable();

  String ecmTreeUnavailable();

  String bugUnavailable();

  String bugListUnavailable();

  String noteUnavailable();

  String deliveredConfirm();

  String manageLivraisonButton();

  String noteTemplateEmptyCustomField();

  String returnMessage();

  String buttonDownloadDeliveryNoteSample();

  String deliveryNoteSample();

  String deliveryNoteSampleTitle();

  String deleteConfirm();

  String selectBugContentWithoutNote();

}
