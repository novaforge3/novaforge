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
package org.novaforge.forge.tools.deliverymanager.report;

import org.novaforge.forge.tools.deliverymanager.model.BugTrackerIssue;
import org.novaforge.forge.tools.deliverymanager.model.ECMDocument;
import org.novaforge.forge.tools.deliverymanager.model.FileElement;
import org.novaforge.forge.tools.deliverymanager.model.impl.BugTrackerIssueImpl;
import org.novaforge.forge.tools.deliverymanager.model.impl.ECMDocumentImpl;
import org.novaforge.forge.tools.deliverymanager.model.impl.FileElementImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by Birt Eclipse Plugin Designer to get dummy datas in order to modify the layout of the
 * default rptdesign
 * 
 * @author sbenoist
 */
public class DeliveryDataProvider
{
   private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

   /**
    * This method will return a fake list of {@link BugTrackerIssue}. It can be used to generate a example of
    * BIRT report
    * 
    * @param pProjectId
    *           represents the project id
    * @param pReference
    *           represents the delivery reference
    * @param pUser
    *           represents the user name who is generating the template
    * @return list of {@link BugTrackerIssue}
    */
   public List<BugTrackerIssue> getBugTrackerIssues(final String pProjectId, final String pReference,
         final String pUser)
   {
      final List<BugTrackerIssue> issues = new ArrayList<BugTrackerIssue>();

      final BugTrackerIssueImpl issueOne = new BugTrackerIssueImpl();
      issueOne.setId("1");
      issueOne.setTitle("Impossible de supprimer un utilisateur");
      issueOne.setCategory("NovaForgeV3");
      issueOne.setDescription("Il n'y a pas d'interface graphique permettant de supprimer un utilisateur");
      issueOne.setReporter("Sarah Vigote");
      issueOne.setSeverity("Majeur");
      issueOne.setAdditionalInfo("Le bug a été reproduit sur IE, FireFox et Chrome");
      issues.add(issueOne);

      final BugTrackerIssueImpl issueTwo = new BugTrackerIssueImpl();
      issueTwo.setId("2");
      issueTwo.setTitle("Pas d'ascenseurs verticaux pour les gadgets");
      issueTwo.setCategory("NovaForgeV3");
      issueTwo.setDescription("Il n'y a pas d'ascenseurs verticaux sur tous les gadgets");
      issueTwo.setReporter("Joséphine Baker");
      issueTwo.setSeverity("Majeur");
      issueTwo.setAdditionalInfo("Le bug a été reproduit uniquement sur IE");
      issues.add(issueTwo);

      final BugTrackerIssueImpl issueThree = new BugTrackerIssueImpl();
      issueThree.setId("3");
      issueThree.setTitle("Les bons de livraisons ne sont pas internationalisés");
      issueThree.setCategory("NovaForgeV3");
      issueThree
            .setDescription("Il n'est pas possible actuellement de générer un bon de livraison en anglais");
      issueThree.setReporter("Gérard Manjoué");
      issueThree.setSeverity("Mineur");
      issues.add(issueThree);

      return issues;
   }

   /**
    * This method will return a fake list of {@link ECMDocument}. It can be used to generate a example of BIRT
    * report
    * 
    * @param pProjectId
    *           represents the project id
    * @param pReference
    *           represents the delivery reference
    * @return list of {@link ECMDocument}
    */
   public List<ECMDocument> getECMDocuments(final String pProjectId, final String pReference)
   {
      final List<ECMDocument> documents = new ArrayList<ECMDocument>();
      try
      {
         final ECMDocumentImpl documentOne = new ECMDocumentImpl();
         documentOne.setId("1");
         documentOne.setAuthor("Gustave Flaubert");
         documentOne.setCreatedDate(dateFormat.parse("10-04-2012 14:04:58"));
         documentOne.setName("Spécifications Fonctionnelles de la Gestion des utilisateurs");
         documentOne.setLastModifiedDate(dateFormat.parse("20-05-2012 10:04:58"));
         documentOne.setLastModifiedAuthor("François Rabelais");
         documentOne.setPath("/documents/V3/specifs/Gestion_Utilisateurs.doc");
         documents.add(documentOne);

         final ECMDocumentImpl documentTwo = new ECMDocumentImpl();
         documentTwo.setId("2");
         documentTwo.setAuthor("Jean Proust");
         documentTwo.setCreatedDate(dateFormat.parse("12-03-2012 17:04:58"));
         documentTwo.setName("Document de conception de la communication inter-outils");
         documentTwo.setLastModifiedDate(dateFormat.parse("20-04-2012 05:04:58"));
         documentTwo.setLastModifiedAuthor("Friedrich Nietzsche");
         documentTwo.setPath("/documents/V3/conception/Communication_inter_outils.doc");
         documents.add(documentTwo);

         final ECMDocumentImpl documentThree = new ECMDocumentImpl();
         documentThree.setId("3");
         documentThree.setAuthor("Léon Tolstoï");
         documentThree.setCreatedDate(dateFormat.parse("12-06-2012 17:08:58"));
         documentThree.setName("Document d'architacture NovaForge V3");
         documentThree.setLastModifiedDate(dateFormat.parse("25-06-2012 20:04:58"));
         documentThree.setLastModifiedAuthor("Gustav Mahler");
         documentThree.setPath("/documents/V3/archi/Architecture_V3.doc");
         documents.add(documentThree);
      }
      catch (final Exception e)
      {
         System.out.println("an error occurred during the parsing of date for documents");
         e.printStackTrace();
      }

      return documents;
   }

   /**
    * This method will return a fake list of {@link FileElement}. It can be used to generate a example of BIRT
    * report
    * 
    * @param pProjectId
    *           represents the project id
    * @param pReference
    *           represents the delivery reference
    * @return list of {@link FileElement}
    */
   public List<FileElement> getFileElements(final String pProjectId, final String pReference)
   {
      final List<FileElement> fileElements = new ArrayList<FileElement>();

      final FileElementImpl fileElementOne = new FileElementImpl();
      fileElementOne.setFileName("CHANGELOG");
      fileElementOne.setSize("3500");
      fileElementOne.setSource("Local");
      fileElementOne.setType("Texte");
      fileElementOne.setUrl("/CHANGELOG");
      fileElements.add(fileElementOne);
      
      final FileElementImpl fileElementTwo = new FileElementImpl();
      fileElementTwo.setFileName("LICENCE");
      fileElementTwo.setSize("5500");
      fileElementTwo.setSource("Local");
      fileElementTwo.setType("Texte");
      fileElementTwo.setUrl("/LICENCE");
      fileElements.add(fileElementTwo);
      
      final FileElementImpl fileElementThree = new FileElementImpl();
      fileElementThree.setFileName("README");
      fileElementThree.setSize("18000");
      fileElementThree.setSource("Local");
      fileElementThree.setType("Texte");
      fileElementThree.setUrl("/README");
      fileElements.add(fileElementThree);
      
      return fileElements;
   }
}
