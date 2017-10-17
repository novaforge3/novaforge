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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;

import java.io.IOException;
import java.util.List;

/**
 * @author blachonm
 */

public class EmptyDeliveryTest extends DeliveryBaseTests
{
  public static final int WAIT_FOR_GENERATION  = 10000;
  public static final int WAIT_FOR_PROPAGATION = 5000;
  private static final Log LOG                        = LogFactory.getLog(EmptyDeliveryTest.class);
  private static String    DELIVERY_NAME              = "Delivery1";
  private static String    DELIVERY_NAME_UPDATED      = "Delivery1_updated";
  private static String    DELIVERY_SPECIAL_NAME      = "Deliveryéàç_";
  private static String    DELIVERY_TYPE              = "Draft1";
  private static String    DELIVERY_TYPE_UPDATED      = "Draft2";
  private static String    DELIVERY_VERSION           = "1.0";
  private static String    DELIVERY_VERSION_UPDATED   = "2.0";
  private static String    DELIVERY_REFERENCE         = DELIVERY_NAME + "_" + DELIVERY_VERSION;
  private static String    DELIVERY_SPECIAL_REFERENCE = DELIVERY_SPECIAL_NAME + "_" + DELIVERY_VERSION;
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

  // standard create
  public void test01StdDeliveryCreation() throws DeliveryServiceException
  {
    // create std delivery
    createStdDelivery();

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

  private void createStdDelivery() throws DeliveryServiceException
  {
    final Delivery delivery = createDelivery(DELIVERY_NAME, DELIVERY_VERSION, IDProjetDelivery, DELIVERY_REFERENCE);
    assertNotNull("deliveryNormalCreate must not be null", delivery);

    final Delivery stdDelivery = deliveryPresenter.createDelivery(delivery, DeliveryStatus.CREATED, DELIVERY_TYPE);
    assertNotNull("delivery has not been created", stdDelivery);
    assertEquals(IDProjetDelivery, stdDelivery.getProjectId());
    assertEquals(DELIVERY_VERSION, stdDelivery.getVersion());
    assertEquals(DELIVERY_NAME, stdDelivery.getName());
    assertEquals(DELIVERY_REFERENCE, stdDelivery.getReference());
    assertEquals(DELIVERY_TYPE, stdDelivery.getType().getLabel());
    assertEquals(DeliveryStatus.CREATED.toString(), stdDelivery.getStatus().toString());
  }

  public void test02DeliveryWithSpecialCharacters() throws DeliveryServiceException
  {
    final Delivery deliverySpecialCaracters = createDelivery(DELIVERY_SPECIAL_NAME, DELIVERY_VERSION,
        IDProjetDelivery, DELIVERY_SPECIAL_REFERENCE);

    assertNotNull("deliveryNormalCreate must not be null", deliverySpecialCaracters);

    // create the delivery into the project/application
    final Delivery delivery = deliveryPresenter.createDelivery(deliverySpecialCaracters,
        DeliveryStatus.CREATED, DELIVERY_TYPE);
    assertNotNull("delivery has not been created", delivery);

    final List<Delivery> deliveries = deliveryPresenter.getDeliveries(IDProjetDelivery);
    assertNotNull(deliveries);
    assertEquals("One delivery should be created", 1, deliveries.size());

    // checks
    final Delivery deliveryFound = deliveryPresenter
        .getDelivery(IDProjetDelivery, DELIVERY_SPECIAL_REFERENCE);
    assertEquals(IDProjetDelivery, deliveryFound.getProjectId());
    assertEquals(DELIVERY_VERSION, deliveryFound.getVersion());
    assertEquals(DELIVERY_SPECIAL_NAME, deliveryFound.getName());
    assertEquals(DELIVERY_SPECIAL_REFERENCE, deliveryFound.getReference());
    assertEquals(DELIVERY_TYPE, deliveryFound.getType().getLabel());
    assertEquals(DeliveryStatus.CREATED.toString(), deliveryFound.getStatus().toString());
  }

  public void test03DeliveryCreationWithSameReference() throws DeliveryServiceException
  {
    // create 1st std delivery
    createStdDelivery();

    // checks
    final Delivery deliveryFound = deliveryPresenter.getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);
    assertEquals(IDProjetDelivery, deliveryFound.getProjectId());
    assertEquals(DELIVERY_VERSION, deliveryFound.getVersion());
    assertEquals(DELIVERY_NAME, deliveryFound.getName());
    assertEquals(DELIVERY_REFERENCE, deliveryFound.getReference());
    assertEquals(DELIVERY_TYPE, deliveryFound.getType().getLabel());
    assertEquals(DeliveryStatus.CREATED.toString(), deliveryFound.getStatus().toString());

    // try to create 2nd delivery with same reference
    final Delivery deliverySameRef = createDelivery(DELIVERY_NAME, DELIVERY_VERSION, IDProjetDelivery,
        DELIVERY_REFERENCE);

    boolean raisedException = false;
    try
    {
      deliveryPresenter.createDelivery(deliverySameRef, DeliveryStatus.CREATED, DELIVERY_TYPE);
    }
    catch (final Exception e)
    {
      raisedException = true;
    }
    assertTrue("Delivery with same reference has been created", raisedException);
  }

  public void test04NullferenceCreate() throws DeliveryServiceException
  {
    final Delivery deliveryEmptyRef = createDelivery(DELIVERY_NAME, DELIVERY_VERSION, IDProjetDelivery, "");

    boolean raisedException = false;
    try
    {
      deliveryPresenter.createDelivery(deliveryEmptyRef, DeliveryStatus.CREATED, DELIVERY_TYPE);
    }
    catch (final Exception e)
    {
      raisedException = true;
    }
    assertTrue("Exception has not been raised", raisedException);
  }

  // delete delivery
  public void test05DeleteDelivery() throws DeliveryServiceException
  {
    // create std delivery
    createStdDelivery();

    // check
    final List<Delivery> deliveries = deliveryPresenter.getDeliveries(IDProjetDelivery);
    assertNotNull(deliveries);
    assertEquals("One delivery should be created", 1, deliveries.size());

    final Delivery deliveryFoundToDelete = deliveryPresenter
        .getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);
    assertEquals(IDProjetDelivery, deliveryFoundToDelete.getProjectId());
    assertEquals(DELIVERY_VERSION, deliveryFoundToDelete.getVersion());
    assertEquals(DELIVERY_NAME, deliveryFoundToDelete.getName());
    assertEquals(DELIVERY_REFERENCE, deliveryFoundToDelete.getReference());
    assertEquals(DELIVERY_TYPE, deliveryFoundToDelete.getType().getLabel());
    assertEquals(DeliveryStatus.CREATED.toString(), deliveryFoundToDelete.getStatus().toString());

    // delete the delivery
    deliveryPresenter.deleteDelivery(IDProjetDelivery, deliveryFoundToDelete.getReference());

    // check
    final List<Delivery> deliveriesAfterDelete = deliveryPresenter.getDeliveries(IDProjetDelivery);
    assertEquals("Delivery looks not deleted", 0, deliveriesAfterDelete.size());

    boolean raisedException = false;
    try
    {
      deliveryPresenter.getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);
    }
    catch (final Exception e)
    {
      raisedException = true;
    }
    assertTrue("Exception has not been raised", raisedException);

  }

  public void test06DeliveryUpdates() throws Exception
  {

    // create std delivery
    createStdDelivery();

    // check
    final List<Delivery> deliveries = deliveryPresenter.getDeliveries(IDProjetDelivery);
    assertNotNull(deliveries);
    assertEquals("One delivery should be created", 1, deliveries.size());

    final Delivery deliveryToUpdate = deliveryPresenter.getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);
    assertEquals(IDProjetDelivery, deliveryToUpdate.getProjectId());
    assertEquals(DELIVERY_VERSION, deliveryToUpdate.getVersion());
    assertEquals(DELIVERY_NAME, deliveryToUpdate.getName());
    assertEquals(DELIVERY_REFERENCE, deliveryToUpdate.getReference());
    assertEquals(DELIVERY_TYPE, deliveryToUpdate.getType().getLabel());
    assertEquals(DeliveryStatus.CREATED.toString(), deliveryToUpdate.getStatus().toString());

    // update delivery
    deliveryToUpdate.setName(DELIVERY_NAME_UPDATED);
    deliveryToUpdate.setVersion(DELIVERY_VERSION_UPDATED);

    final Delivery deliveryUpdated = deliveryPresenter.updateDelivery(DELIVERY_REFERENCE, deliveryToUpdate,
        DELIVERY_TYPE_UPDATED);

    assertEquals(IDProjetDelivery, deliveryUpdated.getProjectId());
    assertEquals(DELIVERY_VERSION_UPDATED, deliveryUpdated.getVersion());
    assertEquals(DELIVERY_NAME_UPDATED, deliveryUpdated.getName());
    assertEquals(DELIVERY_REFERENCE, deliveryUpdated.getReference());
    assertEquals(DELIVERY_TYPE_UPDATED, deliveryUpdated.getType().getLabel());
    assertEquals(DeliveryStatus.MODIFIED.toString(), deliveryUpdated.getStatus().toString());

    // wait message propagation
    Thread.currentThread().sleep(WAIT_FOR_PROPAGATION);

    // check
    final List<Delivery> deliveriesAfterUpdate = deliveryPresenter.getDeliveries(IDProjetDelivery);
    assertNotNull(deliveriesAfterUpdate);
    assertEquals("Still one delivery should be found", 1, deliveriesAfterUpdate.size());

    final Delivery deliveryAfterUpdate = deliveryPresenter.getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);

    assertEquals(IDProjetDelivery, deliveryAfterUpdate.getProjectId());
    assertEquals(DELIVERY_VERSION_UPDATED, deliveryAfterUpdate.getVersion());
    assertEquals(DELIVERY_NAME_UPDATED, deliveryAfterUpdate.getName());
    assertEquals(DELIVERY_REFERENCE, deliveryAfterUpdate.getReference());
    assertEquals(DELIVERY_TYPE_UPDATED, deliveryAfterUpdate.getType().getLabel());
    assertEquals(DeliveryStatus.MODIFIED.toString(), deliveryAfterUpdate.getStatus().toString());
  }

  // private functions --------------------------------------------

  public void test07EmptyDeliveryGeneration() throws DeliveryServiceException, InterruptedException,
      IOException
  {
    // create std delivery
    createStdDelivery();

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

    // generate delivery
    boolean isGenerated = deliveryPresenter.generateDelivery(IDProjetDelivery, DELIVERY_REFERENCE,
        "usertest1-u");

    // wait generation
    Thread.currentThread().sleep(WAIT_FOR_GENERATION);

    assertTrue("boolean for delivery deneration should be true", isGenerated);
    Delivery deliveryGen = deliveryPresenter.getDelivery(IDProjetDelivery, DELIVERY_REFERENCE);

    // check status
    assertEquals(DeliveryStatus.GENERATED.toString(), deliveryGen.getStatus().toString());

    // get/check content
    List<Content> deliveryContent = deliveryPresenter.getContents(IDProjetDelivery, DELIVERY_REFERENCE);
    assertTrue("content of the delivey shopuld be empty", 0 == deliveryContent.size());

    // get generated archive
    String archiveFilePath = deliveryRepositoryService.getDeliveryArchivePath(IDProjetDelivery,
        DELIVERY_REFERENCE);
    assertNotNull(archiveFilePath);
    assertNotEquals("archive path is an empty string", "", archiveFilePath);

    // unzip and check empty zip (with the last parameter) because no report and file.
    final String OUTPUT_FOLDER = "/datas/safran/datas/delivery-manager/projettest24_deliveryappli/Delivery1_1.0/tmp";
    unZipIt(archiveFilePath, OUTPUT_FOLDER, true);
  }

}
