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
package org.novaforge.forge.tools.deliverymanager.internal.services;

import org.novaforge.forge.tools.deliverymanager.dao.ContentDAO;
import org.novaforge.forge.tools.deliverymanager.dao.DeliveryDAO;
import org.novaforge.forge.tools.deliverymanager.dao.DeliveryTypeDAO;
import org.novaforge.forge.tools.deliverymanager.dao.NodeDAO;
import org.novaforge.forge.tools.deliverymanager.entity.ArtefactEntity;
import org.novaforge.forge.tools.deliverymanager.entity.ArtefactParameterEntity;
import org.novaforge.forge.tools.deliverymanager.entity.ContentEntity;
import org.novaforge.forge.tools.deliverymanager.entity.DeliveryEntity;
import org.novaforge.forge.tools.deliverymanager.entity.DeliveryTypeEntity;
import org.novaforge.forge.tools.deliverymanager.entity.FolderEntity;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.facades.DeliveryService;
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryType;
import org.novaforge.forge.tools.deliverymanager.model.Folder;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is a concret implement of {@link DeliveryService}
 * 
 * @author Guillaume Lamirand
 */
public class DeliveryServiceImpl implements DeliveryService
{
  private ContentDAO      contentDAO;
  private NodeDAO         nodeDAO;
  private DeliveryDAO     deliveryDAO;
  private DeliveryTypeDAO deliveryTypeDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery newDelivery()
  {
    return new DeliveryEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery createDelivery(final Delivery pDelivery, final DeliveryStatus pStatus,
      final String pTypeLabel) throws DeliveryServiceException
  {
    // Setting the type to the delivery
    final DeliveryEntity deliveryEntity = (DeliveryEntity) pDelivery;
    // Getting the delivery type if existing or create a new one
    if (pTypeLabel != null)
    {
      final DeliveryType type = getDeliveryType(pDelivery.getProjectId(), pTypeLabel);
      deliveryEntity.setType(type);
    }
    // Setting status
    deliveryEntity.setStatus(pStatus);
    return deliveryDAO.update(deliveryEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery changeStatus(final String pProjectId, final String pReference, final DeliveryStatus pDeliveryStatus)
      throws DeliveryServiceException
  {
    // Get delivery for the reference
    final DeliveryEntity delivery = (DeliveryEntity) getDelivery(pProjectId, pReference);

    delivery.setStatus(pDeliveryStatus);
    if (pDeliveryStatus == DeliveryStatus.DELIVERED)
    {
      delivery.setDate(new Date());
    }
    // Save the delivery
    return deliveryDAO.update(delivery);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery getDelivery(final String pProjectId, final String pReference)
      throws DeliveryServiceException
  {
    return deliveryDAO.findByProjectAndReference(pProjectId, pReference);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Delivery> getDeliveries(final String pProjectId) throws DeliveryServiceException
  {
    return deliveryDAO.findByProject(pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getTypes(final String pProjectId) throws DeliveryServiceException
  {
    final List<String> returnList = new ArrayList<String>();

    final List<DeliveryType> findAll = deliveryTypeDAO.findByProject(pProjectId);

    for (final DeliveryType deliveryType : findAll)
    {
      returnList.add(deliveryType.getLabel());
    }

    return returnList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteDelivery(final String pProjectId, final String pReference)
      throws DeliveryServiceException
  {
    // Synchronize group with current persistence manager
    final Delivery delivery = getDelivery(pProjectId, pReference);

    // Deleting delivery
    deliveryDAO.remove(delivery);
    return true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existReference(final String pProjectId, final String pReference)
      throws DeliveryServiceException
  {
    boolean returnValue = true;
    try
    {
      // This will return a exception if the reference id is not existing
      deliveryDAO.findByProjectAndReference(pProjectId, pReference);
    }
    catch (final NoResultException e)
    {
      returnValue = false;
    }
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery updateDelivery(final Delivery pDelivery, final String pType) throws DeliveryServiceException
  {

    final DeliveryEntity deliveryEntity = (DeliveryEntity) pDelivery;
    // Getting the existing delivery
    if (pType != null)
    {
      // Getting the delivery type if existing or create a new one
      final DeliveryType type = getDeliveryType(pDelivery.getProjectId(), pType);
      deliveryEntity.setType(type);
    }

    // Pass the status to Modified
    deliveryEntity.setStatus(DeliveryStatus.MODIFIED);

    // Save the delivery
    return deliveryDAO.update(deliveryEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Content> getContents(final String pProjectId, final String pReference) throws DeliveryServiceException
  {
    return contentDAO.findByDeliveryReference(pProjectId, pReference);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Content getContent(final String pProjectId, final String pReference, final ContentType pType)
      throws DeliveryServiceException
  {
    Content content;
    try
    {
      content = contentDAO.findByDeliveryAndType(pProjectId, pReference, pType);
    }
    catch (final NoResultException e)
    {
      throw new DeliveryServiceException(String.format(
          "Unable to find the artefact with [project_id=%s, reference=%s, type=%s]", pProjectId, pReference,
          pType), e);
    }
    return content;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Content updateContentNode(final String pProjectId, final String pReference, final Content pContent)
      throws DeliveryServiceException
  {

    final Content content = getContent(pProjectId, pReference, pContent.getType());

    final Folder newFolder = (Folder) pContent.getNode();
    final Folder folder = (Folder) content.getNode();
    if (folder != null)
    {
      final FolderEntity folderEntity = (FolderEntity) folder;
      folderEntity.removeAll();
      folderEntity.setName(newFolder.getName());
      folderEntity.addAll(newFolder.getChildNodes());
      nodeDAO.update(folderEntity);
    }
    else
    {
      content.setNode(newFolder);
      contentDAO.update(content);
    }

    // pass the delivery to the status "MODIFIED"
    changeStatus(pProjectId, pReference, DeliveryStatus.MODIFIED);

    return content;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteContent(final String pProjectId, final String pReference, final ContentType pType)
      throws DeliveryServiceException
  {
    final Content content = getContent(pProjectId, pReference, pType);
    contentDAO.remove(content);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createContent(final String pProjectId, final String pReference, final ContentType pType)
      throws DeliveryServiceException
  {

    // Get persist delivery
    final DeliveryEntity delivery = (DeliveryEntity) getDelivery(pProjectId, pReference);

    // Build content entity
    final ContentEntity contentEntity = new ContentEntity();
    contentEntity.setType(pType);

    // Rattach
    contentEntity.setDelivery(delivery);
    delivery.addContent(contentEntity);

    // Merge the delivery
    deliveryDAO.update(delivery);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Artefact newArtefact()
  {
    return new ArtefactEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Folder newFolder()
  {
    return new FolderEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArtefactParameter newArtefactParameter()
  {
    return new ArtefactParameterEntity();
  }

  private DeliveryType getDeliveryType(final String pProjectId, final String pLabel) throws DeliveryServiceException
  {
    DeliveryType deliveryType;
    try
    {
      deliveryType = deliveryTypeDAO.findByProjectAndLabel(pProjectId, pLabel);
    }
    catch (final NoResultException e)
    {
      deliveryType = deliveryTypeDAO.persist(new DeliveryTypeEntity(pProjectId, pLabel));
    }

    return deliveryType;
  }

  /**
   * @param pContentDAO
   *          the contentDAO to set
   */
  public void setContentDAO(final ContentDAO pContentDAO)
  {
    contentDAO = pContentDAO;
  }

  /**
   * @param pNodeDAO
   *          the nodeDAO to set
   */
  public void setNodeDAO(final NodeDAO pNodeDAO)
  {
    nodeDAO = pNodeDAO;
  }

  /**
   * @param pDeliveryDAO
   *          the deliveryDAO to set
   */
  public void setDeliveryDAO(final DeliveryDAO pDeliveryDAO)
  {
    deliveryDAO = pDeliveryDAO;
  }

  /**
   * @param pDeliveryTypeDAO
   *          the deliveryTypeDAO to set
   */
  public void setDeliveryTypeDAO(final DeliveryTypeDAO pDeliveryTypeDAO)
  {
    deliveryTypeDAO = pDeliveryTypeDAO;
  }

}
