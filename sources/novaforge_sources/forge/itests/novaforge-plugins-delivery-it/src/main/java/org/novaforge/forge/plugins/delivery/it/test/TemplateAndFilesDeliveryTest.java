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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;
import org.novaforge.forge.tools.deliverymanager.model.Folder;
import org.novaforge.forge.tools.deliverymanager.model.Node;
import org.novaforge.forge.tools.deliverymanager.model.SourceFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author blachonm
 */

public class TemplateAndFilesDeliveryTest extends DeliveryBaseTests
{
  public static final int     WAIT_FOR_GENERATION        = 5000;
  public static final int     WAIT_FOR_PROPAGATION       = 2000;
  private static final Log    LOG                = LogFactory.getLog(EmptyDeliveryTest.class);
  private static final String REPORT_NODE_FOLDER_PATH    = "/delivery_report_path";
  private static final String REPORT_NODE_FOLDER_NAME    = "delivery_report_dir";
  private static final String FILE_NODE_FOLDER_PATH      = "/delivery_file_path";
  private static final String FILE_NODE_FOLDER_NAME      = "delivery_file_dir";
  private static final String REPORT_PDF_FILE            = "Delivery_with_content_1.0.pdf";
  private static final String REPORT_TXT_FILE            = "Delivery_with_content_1.0.txt";
  private static final String INITIAL_UPLOADED_FILE_NAME = "mySampleDeliveryFile.txt";
  private static final String REPORT_TITLE               = "Bon de livraison";
  private static final String DOCUMENTS_TITLE            = "Liste des documents issus de la GED";
  private static final String FILES_ANNEXES_TITLE        = "Liste des fichiers annexes";
  private static final String SOLVED_BUGS_TITLE          = "Liste des anomalies corrigées";
  private static final String FILE_SEPARATOR             = System.getProperty("file.separator");
  private static final String KARAF_TMP_DIR              = System.getProperty("java.io.tmpdir");
  private static final String PAGE_NAME                  = "portal";
  private static       String DELIVERY_NAME      = "Delivery_with_content";
  private static       String DELIVERY_TYPE      = "Draft1";
  private static       String DELIVERY_VERSION   = "1.0";
  private static       String DELIVERY_REFERENCE = DELIVERY_NAME + "_" + DELIVERY_VERSION;
  private String IDProjetDelivery;

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    final String projectId = xmlData.getProjects().keySet().iterator().next();
    String applicationId = xmlData.getApplicationName(xmlData.getProjects().keySet().iterator().next(),
        "Deliveries");
    applicationId = applicationId.replace(" ", "");
    IDProjetDelivery = projectId + "_" + applicationId;
    LOG.info("************* IDProjetDelivery = " + IDProjetDelivery);
    // delete created deliveries
    final List<Delivery> deliveries = deliveryPresenter.getDeliveries(IDProjetDelivery);
    for (final Delivery delivery : deliveries)
    {
      deliveryPresenter.deleteDelivery(IDProjetDelivery, delivery.getReference());
    }
  }

  @Override
  public void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void test01GenerateDeliveryWithOnlyReport() throws Exception

  {

    // create std delivery
    createStdDeliveryAndCheck();

    // adding report content to the delivey and do checks
    addReportContentNodeAndCheck();

    // call generation and checking function
    generateDeliveryAndCheck();

    // get pdf from archive
    File fileReport = getFileFromGeneratedArchive(REPORT_NODE_FOLDER_NAME, REPORT_PDF_FILE);
    assertNotNull(fileReport);

    // PDF parsing and getting the content into a string
    String everything = getPdfContent(REPORT_NODE_FOLDER_NAME);

    // check main element of the content of the PDF file!!
    assertTrue(everything.contains(REPORT_TITLE));
    assertTrue(everything.contains(DELIVERY_REFERENCE));
    assertTrue(everything.contains(DOCUMENTS_TITLE));
    assertTrue(everything.contains(FILES_ANNEXES_TITLE));
    assertTrue(everything.contains(SOLVED_BUGS_TITLE));

  }

  private void createStdDeliveryAndCheck() throws DeliveryServiceException
  {
    final Delivery delivery = createDelivery(DELIVERY_NAME, DELIVERY_VERSION, IDProjetDelivery,
        DELIVERY_REFERENCE);
    assertNotNull("deliveryNormalCreate must not be null", delivery);

    final Delivery stdDelivery = deliveryPresenter.createDelivery(delivery, DeliveryStatus.CREATED,
        DELIVERY_TYPE);
    assertNotNull("delivery has not been created", stdDelivery);
    assertEquals(IDProjetDelivery, stdDelivery.getProjectId());
    assertEquals(DELIVERY_VERSION, stdDelivery.getVersion());
    assertEquals(DELIVERY_NAME, stdDelivery.getName());
    assertEquals(DELIVERY_REFERENCE, stdDelivery.getReference());
    assertEquals(DELIVERY_TYPE, stdDelivery.getType().getLabel());
    assertEquals(DeliveryStatus.CREATED.toString(), stdDelivery.getStatus().toString());

    // create std delivery
    // createStdDelivery();

    // check
    final List<Delivery> deliveries = deliveryPresenter.getDeliveries(IDProjetDelivery);
    assertNotNull(deliveries);
    assertEquals("One delivery should be created", 1, deliveries.size());

    final Delivery deliveryFound = deliveryPresenter.getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);
    assertEquals(IDProjetDelivery, deliveryFound.getProjectId());
    assertEquals(DELIVERY_VERSION, deliveryFound.getVersion());
    assertEquals(DELIVERY_NAME, deliveryFound.getName());
    assertEquals(DELIVERY_REFERENCE, deliveryFound.getReference());
    assertEquals(DELIVERY_TYPE, deliveryFound.getType().getLabel());
    assertEquals(DeliveryStatus.CREATED.toString(), deliveryFound.getStatus().toString());
  }

  private void addReportContentNodeAndCheck() throws Exception
  {
    // get/check content without report
    final List<Content> emptyDeliveryContent = deliveryPresenter.getContents(IDProjetDelivery,
        DELIVERY_REFERENCE);
    assertTrue("content of the delivey shopuld be empty", 0 == emptyDeliveryContent.size());

    // adding report
    final ArrayList<ContentType> contentTypes = new ArrayList<ContentType>();
    contentTypes.add(ContentType.NOTE);

    final boolean isUpdated = deliveryPresenter.updateDeliveryContents(IDProjetDelivery, DELIVERY_REFERENCE,
        contentTypes);
    assertTrue("contents update for NOTE is note true", isUpdated);

    // get/check content
    final List<Content> deliveryContent = deliveryPresenter.getContents(IDProjetDelivery, DELIVERY_REFERENCE);
    assertTrue("content of the delivey should be empty", 1 == deliveryContent.size());
    LOG.info("****************** content= " + deliveryContent.get(0).toString());

    final Content content = deliveryContent.get(0);
    assertTrue("content node type is not correct", ContentType.NOTE.equals(content.getType()));
    assertNull("content node should be null", content.getNode());

    // setting node for report
    final String oldName = null;
    if (content.getNode() == null)
    {
      final Folder newFolder = deliveryPresenter.newFolder();
      newFolder.setName(REPORT_NODE_FOLDER_NAME);
      newFolder.setPath(REPORT_NODE_FOLDER_PATH);
      content.setNode(newFolder);
    }

    deliveryPresenter.updateContentNode(IDProjetDelivery, DELIVERY_REFERENCE, oldName, content);

    Thread.currentThread().sleep(WAIT_FOR_PROPAGATION);

    // check gotContent
    final Content updatedReportContent = deliveryPresenter.getContent(IDProjetDelivery, DELIVERY_REFERENCE,
        ContentType.NOTE);
    assertNotNull("Content after updating report type and node should not be null", updatedReportContent);
    assertEquals("name of content node folder path is KO", REPORT_NODE_FOLDER_PATH, updatedReportContent
        .getNode().getPath());
    assertEquals("name of content node folder path is KO", REPORT_NODE_FOLDER_NAME, updatedReportContent
        .getNode().getName());
  }

  private void generateDeliveryAndCheck() throws Exception
  {
    // generate delivery
    final boolean isGenerated = deliveryPresenter.generateDelivery(IDProjetDelivery, DELIVERY_REFERENCE, "usertest1-u");

    // wait generation
    Thread.currentThread().sleep(WAIT_FOR_GENERATION);

    assertTrue("boolean for delivery deneration should be true", isGenerated);
    final Delivery deliveryGen = deliveryPresenter.getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);

    // check status
    assertEquals(DeliveryStatus.GENERATED.toString(), deliveryGen.getStatus().toString());
  }

  // ------------------- private functions --------------------------------------------

  private File getFileFromGeneratedArchive(String pFolderNameForReportOrFile, String pFileName)
      throws Exception
  {
    File newTmpFolder = null;
    // get generated archive
    final String archiveFilePath = deliveryRepositoryService.getDeliveryArchivePath(IDProjetDelivery,
        DELIVERY_REFERENCE);
    assertNotNull(archiveFilePath);
    assertNotEquals("archive path is an empty string", "", archiveFilePath);
    LOG.info("****************************** archiveFilePath = " + archiveFilePath + "***************");
    unZipIt(archiveFilePath,
        deliveryRepositoryService.getDeliveryTemporaryPath(IDProjetDelivery, DELIVERY_REFERENCE), false);
    newTmpFolder = new File(
        getTemporaryPath(IDProjetDelivery, DELIVERY_REFERENCE, pFolderNameForReportOrFile));
    LOG.info("************** newTmpFolder = " + newTmpFolder);
    File foundFile = null;
    for (final File fileEntry : newTmpFolder.listFiles())
    {
      if ((fileEntry.isFile()) && (pFileName.equals(fileEntry.getName())))
      {
          foundFile = fileEntry;
          break;
      }
    }
    LOG.info("************************* File = " + foundFile);
    assertNotNull("No file has been found", foundFile);
    return foundFile;
  }

  private String getPdfContent(String pFolderName) throws Exception
  {
    LOG.info("****************** tmp report folder = "
        + getTemporaryPath(IDProjetDelivery, DELIVERY_REFERENCE, pFolderName));
    String src = getTemporaryPath(IDProjetDelivery, DELIVERY_REFERENCE, pFolderName) + FILE_SEPARATOR
        + REPORT_PDF_FILE;
    String dest = getTemporaryPath(IDProjetDelivery, DELIVERY_REFERENCE, pFolderName) + FILE_SEPARATOR
        + REPORT_TXT_FILE;
    parsePdf(src, dest);

    new ArrayList<String>();
    String everything;
    BufferedReader br = new BufferedReader(new FileReader(dest));
    try
    {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null)
      {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
      }
      everything = sb.toString();
    }
    finally
    {
      br.close();
    }
    return everything;
  }

  private String getTemporaryPath(final String pProjectId, final String pReference, final String pContentFolder)
      throws Exception
  {
    final StringBuilder tmp = new StringBuilder();

    tmp.append(deliveryRepositoryService.getDeliveryTemporaryPath(pProjectId, pReference));
    if (!tmp.toString().endsWith(FILE_SEPARATOR))
    {
      tmp.append(FILE_SEPARATOR);
    }
    tmp.append(pContentFolder);

    return tmp.toString();
  }

  public void test02GenerateDeliveryWithUploadedFileAndReport() throws Exception
  {
    // create std delivery
    createStdDeliveryAndCheck();

    // adding report content to the delivey (types and folder) and do checks
    addReportContentNodeAndCheck();

    // adding content type and Folder (content node) for FILE
    addFileContentNodeAndCheck();

    // adding uploaded file and store into the delivery tmp.
    FileItem            fileItem  = buildFileItem("/" + INITIAL_UPLOADED_FILE_NAME, KARAF_TMP_DIR);
    ArrayList<FileItem> fileItems = new ArrayList<FileItem>();
    fileItems.add(fileItem);
    manageDeliveryContentForUploadedFile(fileItems, IDProjetDelivery, DELIVERY_REFERENCE);

    // call generation and checking function
    generateDeliveryAndCheck();

    // get pdf from archive
    File fileReport = getFileFromGeneratedArchive(REPORT_NODE_FOLDER_NAME, REPORT_PDF_FILE);
    assertNotNull(fileReport);

    // PDF parsing and getting the content into a string
    String everything = getPdfContent(REPORT_NODE_FOLDER_NAME);

    // check main element of the content of the PDF file and specially the uploaded file.
    assertTrue(everything.contains(REPORT_TITLE));
    assertTrue(everything.contains(DELIVERY_REFERENCE));
    assertTrue(everything.contains(INITIAL_UPLOADED_FILE_NAME));

    // check the uploaded file is well provided within the archive
    File file = getFileFromGeneratedArchive(FILE_NODE_FOLDER_NAME, INITIAL_UPLOADED_FILE_NAME);

  }

  private void addFileContentNodeAndCheck() throws Exception
  {
    // adding file content types (need to set also NOTE one)
    final ArrayList<ContentType> contentTypes = new ArrayList<ContentType>();
    contentTypes.add(ContentType.NOTE);
    contentTypes.add(ContentType.FILE);

    final boolean isUpdated = deliveryPresenter.updateDeliveryContents(IDProjetDelivery, DELIVERY_REFERENCE,
                                                                       contentTypes);
    assertTrue("contents update for NOTE is note true", isUpdated);

    // get/check content
    final List<Content> deliveryContent = deliveryPresenter.getContents(IDProjetDelivery, DELIVERY_REFERENCE);
    assertEquals("content of the delivey should be 2", 2, deliveryContent.size());
    LOG.info("****************** content(0)= " + deliveryContent.get(0).getType().toString());
    LOG.info("****************** content(1)= " + deliveryContent.get(1).getType().toString());

    // check the FILE content type
    final Content fileContent = deliveryContent.get(1);
    assertTrue("content node type is not correct", ContentType.FILE.equals(fileContent.getType()));
    assertNull("content node should be null", fileContent.getNode());

    // setting folder name and path for FILE type
    final String oldFileName = null;
    if (fileContent.getNode() == null)
    {
      final Folder newFolder = deliveryPresenter.newFolder();
      newFolder.setName(FILE_NODE_FOLDER_NAME);
      newFolder.setPath(FILE_NODE_FOLDER_PATH);
      fileContent.setNode(newFolder);
    }

    // updating delivery
    deliveryPresenter.updateContentNode(IDProjetDelivery, DELIVERY_REFERENCE, oldFileName, fileContent);

    Thread.currentThread().sleep(WAIT_FOR_PROPAGATION);

    // check for the file content node (name, path)
    final Content updatedContent = deliveryPresenter.getContent(IDProjetDelivery, DELIVERY_REFERENCE, ContentType.FILE);
    assertNotNull("Content after updating report type and node should not be null", updatedContent);
    assertEquals("name of content node folder path is KO", FILE_NODE_FOLDER_PATH, updatedContent.getNode().getPath());
    assertEquals("name of content node folder path is KO", FILE_NODE_FOLDER_NAME, updatedContent.getNode().getName());
  }

  private FileItem buildFileItem(String pInFilePath, String pOutFilePath) throws Exception
  {
    InputStream inputStream = this.getClass().getResourceAsStream(pInFilePath);
    int availableBytes = inputStream.available();

    // Write the inputStream to a FileItem
    File outFile = new File(pOutFilePath); // This is your tmp file, the code stores the file here in order
    // to avoid storing it in memory
    FileItem fileItem = new DiskFileItem("fileUpload", "plain/text", false, INITIAL_UPLOADED_FILE_NAME,
        availableBytes, outFile); // You link FileItem to the tmp outFile
    OutputStream outputStream = fileItem.getOutputStream(); // Last step is to get FileItem's output stream,
    // and write your inputStream in it. This is the
    // way to write to your FileItem.

    int read = 0;
    byte[] bytes = new byte[1024];
    while ((read = inputStream.read(bytes)) != -1)
    {
      outputStream.write(bytes, 0, read);
    }
    inputStream.close();
    outputStream.flush(); // This actually causes the bytes to be written.
    outputStream.close();
    return fileItem;
  }

  private void manageDeliveryContentForUploadedFile(final List<FileItem> files, final String projectId,
                                                    final String reference) throws Exception
  {

    // final Content content = getContent(projectId, reference);
    final Content content = deliveryPresenter.getContent(projectId, reference, ContentType.FILE);
    final Node    node    = content.getNode();
    for (final FileItem fileItem : files)
    {
      final Artefact newArtefact = buildArtefact(node, fileItem);
      final Folder folder = (Folder) node;
      folder.addChildNode(newArtefact);
      writeFileItem(projectId, fileItem, getTemporaryPath(projectId, reference, folder.getName()));
    }
    deliveryPresenter.updateContentNode(projectId, reference, null, content);
  }

  private Artefact buildArtefact(final Node node, final FileItem fileItem)
  {
    // Builder artefact object
    final Artefact newArtefact = deliveryPresenter.newArtefact();
    newArtefact.setIdentifiant(fileItem.getName());
    newArtefact.setName(fileItem.getName());

    final List<ArtefactParameter> fields = new ArrayList<ArtefactParameter>();

    final ArtefactParameter type = deliveryPresenter.newArtefactParameter();
    type.setKey(ParameterKey.TYPE.getKey());
    type.setValue(fileItem.getContentType());
    final ArtefactParameter size = deliveryPresenter.newArtefactParameter();
    size.setKey(ParameterKey.SIZE.getKey());
    size.setValue(String.valueOf(fileItem.getSize()));
    final ArtefactParameter source = deliveryPresenter.newArtefactParameter();
    source.setKey(ParameterKey.SOURCE.getKey());
    source.setValue(String.valueOf(SourceFile.LOCALE.getId()));

    fields.add(type);
    fields.add(size);
    fields.add(source);
    newArtefact.setParameters(fields);
    // Building artefact path
    final StringBuilder path = new StringBuilder(node.getPath());
    if (!path.toString().endsWith(FILE_SEPARATOR))
    {
      path.append(FILE_SEPARATOR);
    }
    path.append(node.getName());
    newArtefact.setPath(path.toString());

    return newArtefact;
  }

  private void writeFileItem(final String projectId, final FileItem fileItem, final String pDirectory)
      throws Exception
  {
    final String fileName = getFileName(fileItem);
    final File uploadedFile = new File(pDirectory, fileName);

    if (fileService.existFile(uploadedFile))
    {
      fileService.deleteFile(uploadedFile);
    }
    fileService.storeFile(fileItem, uploadedFile);

  }

  private String getFileName(final FileItem fileItem)
  {
    String fileName = fileItem.getName();
    if (fileName != null)
    {
      fileName = FilenameUtils.getName(fileName);
    }
    return fileName;

  }

  public void test03GenerateDeliveryWithExternalLinkFileAndReport() throws Exception
  {
    // create std delivery
    createStdDeliveryAndCheck();

    // adding report content to the delivey (types and folder) and do checks
    addReportContentNodeAndCheck();

    // adding content type and Folder (content node) for FILE
    addFileContentNodeAndCheck();

    String URL_EXTERNAL_LINK = "https://" + InetAddress.getLocalHost().getHostName() + "/portal";
    LOG.info("*************** localhost name = " + URL_EXTERNAL_LINK);

    // add file with external link
    boolean isExternalLinkOK = deliveryPresenter.getExternalFile(IDProjetDelivery, DELIVERY_REFERENCE,
                                                                 URL_EXTERNAL_LINK, PAGE_NAME);
    assertTrue("external Link File has not been obtained", isExternalLinkOK);

    // call generation and checking function
    generateDeliveryAndCheck();

    // get pdf from archive
    File fileReport = getFileFromGeneratedArchive(REPORT_NODE_FOLDER_NAME, REPORT_PDF_FILE);
    assertNotNull(fileReport);

    // PDF parsing and getting the content into a string
    String everything = getPdfContent(REPORT_NODE_FOLDER_NAME);

    // check main element of the content of the PDF file and specially the uploaded file.
    assertTrue(everything.contains(REPORT_TITLE));
    assertTrue(everything.contains(DELIVERY_REFERENCE));
    assertTrue(everything.contains(URL_EXTERNAL_LINK));
    assertTrue(everything.contains(PAGE_NAME));

  }

  public void test04ReGenerateDeliveryAfterModifiedContent() throws Exception
  {
    // create std delivery
    createStdDeliveryAndCheck();

    // adding report content to the delivey (types and folder) and do checks
    addReportContentNodeAndCheck();

    // call generation and checking function
    generateDeliveryAndCheck();

    // get pdf from archive
    File fileReport = getFileFromGeneratedArchive(REPORT_NODE_FOLDER_NAME, REPORT_PDF_FILE);
    assertNotNull(fileReport);

    // PDF parsing and getting the content into a string
    String everything = getPdfContent(REPORT_NODE_FOLDER_NAME);

    // check main element of the content of the PDF file and specially the uploaded file.
    assertTrue(everything.contains(REPORT_TITLE));
    assertTrue(everything.contains(DELIVERY_REFERENCE));

    // adding content type and Folder (content node) for FILE
    addFileContentNodeAndCheck();

    // adding uploaded file and store into the delivery tmp.
    FileItem            fileItem  = buildFileItem("/" + INITIAL_UPLOADED_FILE_NAME, KARAF_TMP_DIR);
    ArrayList<FileItem> fileItems = new ArrayList<FileItem>();
    fileItems.add(fileItem);
    manageDeliveryContentForUploadedFile(fileItems, IDProjetDelivery, DELIVERY_REFERENCE);

    // check delivery status after changing the delivery content (expected status = modified)
    Delivery deliveryGen2 = deliveryPresenter.getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);

    // check status
    assertEquals(DeliveryStatus.MODIFIED.toString(), deliveryGen2.getStatus().toString());

    // call generation and checking function
    generateDeliveryAndCheck();

    // get pdf from archive
    File uploadedFile = getFileFromGeneratedArchive(REPORT_NODE_FOLDER_NAME, REPORT_PDF_FILE);
    assertNotNull(uploadedFile);

    // PDF parsing and getting the content into a string
    String everythingAfterModification = getPdfContent(REPORT_NODE_FOLDER_NAME);

    // check main element of the content of the PDF file and specially the uploaded file.
    assertTrue(everythingAfterModification.contains(REPORT_TITLE));
    assertTrue(everythingAfterModification.contains(DELIVERY_REFERENCE));
    assertTrue(everythingAfterModification.contains(INITIAL_UPLOADED_FILE_NAME));

    // check the uploaded file is well provided within the archive
    File file = getFileFromGeneratedArchive(FILE_NODE_FOLDER_NAME, INITIAL_UPLOADED_FILE_NAME);
    assertNotNull(file);

  }

}
