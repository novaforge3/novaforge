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
package org.novaforge.forge.plugins.delivery.it.test;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.novaforge.forge.commons.technical.file.FileService;
import org.novaforge.forge.plugins.delivery.it.test.data.XmlData;
import org.novaforge.forge.tools.deliverymanager.entity.DeliveryEntity;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPresenter;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryRepositoryService;
import org.novaforge.forge.tools.deliverymanager.services.TemplateReportPresenter;
import org.osgi.framework.ServiceReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author blachonm
 */

public class DeliveryBaseTests extends OSGiTestCase
{
   private static final Log LOG = LogFactory.getLog(DeliveryBaseTests.class);
   protected DeliveryPresenter         deliveryPresenter;
   protected DeliveryRepositoryService deliveryRepositoryService;
   protected TemplateReportPresenter   templateReportPresenter;
   protected FileService               fileService;
   protected XmlData                   xmlData;

   @Override
   public void setUp() throws Exception
   {
      super.setUp();
      deliveryPresenterInitial();
      deliveryRepositoryServiceInitial();
      fileServiceInitial();
      TemplateReportPresenterInitial();
      xmlData = new XmlData();
   }

   @Override
   public void tearDown() throws Exception
   {
      super.tearDown();
   }

   private void deliveryPresenterInitial()
   {
      ServiceReference serviceReference = getServiceReference(DeliveryPresenter.class.getName());
      final DeliveryPresenter deliveryPresenter = (DeliveryPresenter) getServiceObject(serviceReference);
      assertNotNull("DeliveryPresenter instance should not be null", deliveryPresenter);
      this.deliveryPresenter = deliveryPresenter;

   }

   private void deliveryRepositoryServiceInitial()
   {
      ServiceReference serviceReference = getServiceReference(DeliveryRepositoryService.class.getName());
      final DeliveryRepositoryService deliveryRepositoryService = (DeliveryRepositoryService) getServiceObject(serviceReference);
      assertNotNull("deliveryRepositoryService instance should not be null", deliveryRepositoryService);
      this.deliveryRepositoryService = deliveryRepositoryService;

   }

   private void fileServiceInitial()
   {
      ServiceReference serviceReference = getServiceReference(FileService.class.getName());
      final FileService fileService = (FileService) getServiceObject(serviceReference);
      assertNotNull("FileService instance should not be null", fileService);
      this.fileService = fileService;
   }

   private void TemplateReportPresenterInitial()
   {
      ServiceReference serviceReference = getServiceReference(TemplateReportPresenter.class.getName());
      final TemplateReportPresenter templateReportPresenter = (TemplateReportPresenter) getServiceObject(serviceReference);
      assertNotNull("TemplateReportPresenter instance should not be null", templateReportPresenter);
      this.templateReportPresenter = templateReportPresenter;
   }

   public Delivery createDelivery(final String pName, final String pVersion, String pDeliveryProject,
         String pDeliveryReference) throws DeliveryServiceException
         {

      final Delivery myDeliveryCreated = new DeliveryEntity();
      myDeliveryCreated.setName(pName);
      myDeliveryCreated.setProjectId(pDeliveryProject);
      myDeliveryCreated.setReference(pDeliveryReference); // default one
      myDeliveryCreated.setVersion(pVersion);
      return myDeliveryCreated;
         }

   /**
    * Unzip it
    * 
    * @param zipFile
    *           input zip file
    * @param output
    *           zip file output folder
    */
   public void unZipIt(String zipFile, String outputFolder, boolean isZipEmpty) throws IOException
   {

      byte[] buffer = new byte[1024];

      // create output directory is not exists
      File folder = new File(outputFolder);
      if (!folder.exists())
      {
         folder.mkdir();
      }

      // get the zip file content
      ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
      // get the zipped file list entry
      ZipEntry ze = zis.getNextEntry();

      if (isZipEmpty)
      {
         assertNull("Zip is not null!", ze);
      }

      while (ze != null)
      {
         String fileName = ze.getName();
         File newFile = new File(outputFolder + File.separator + fileName);
         newFile.getPath();

         System.out.println("file unzip : " + newFile.getAbsoluteFile());

         // create all non exists folders
         // else you will hit FileNotFoundException for compressed folder
         new File(newFile.getParent()).mkdirs();

         FileOutputStream fos = new FileOutputStream(newFile);

         int len;
         while ((len = zis.read(buffer)) > 0)
         {
            fos.write(buffer, 0, len);
         }

         fos.close();
         ze = zis.getNextEntry();
      }

      zis.closeEntry();
      zis.close();

      System.out.println("Done");

   }

   /**
    * Parses the PDF using PRTokeniser
    * 
    * @param src
    *           the path to the original PDF file
    * @param dest
    *           the path to the resulting text file
    * @throws IOException
    */
   public void parsePdf(String src, String dest) throws IOException
   {
      PdfReader reader = new PdfReader(src);
      // we can inspect the syntax of the imported page
      byte[] streamBytes = reader.getPageContent(1);
      PRTokeniser tokenizer = new PRTokeniser(new RandomAccessFileOrArray(
            new RandomAccessSourceFactory().createSource(streamBytes)));
      PrintWriter out = new PrintWriter(new FileOutputStream(dest));
      while (tokenizer.nextToken())
      {
         if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING)
         {
            out.println(tokenizer.getStringValue());
         }
      }
      out.flush();
      out.close();
      reader.close();
   }

}
