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
package org.novaforge.forge.distribution.register.server.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.novaforge.forge.distribution.register.client.DistributionNotificationService;
import org.novaforge.forge.distribution.register.dao.ForgeDAO;
import org.novaforge.forge.distribution.register.dao.ForgeRequestDAO;
import org.novaforge.forge.distribution.register.domain.Forge;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.domain.ForgeRequest;
import org.novaforge.forge.distribution.register.domain.ForgeRequestDTO;
import org.novaforge.forge.distribution.register.domain.RequestStatus;
import org.novaforge.forge.distribution.register.domain.RequestType;
import org.novaforge.forge.distribution.register.exceptions.ExceptionCode;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;
import org.novaforge.forge.distribution.register.services.ForgeDistributionService;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 29 déc. 2011
 */
public class ForgeDistributionServiceImpl implements ForgeDistributionService
{

  private static final Log log = LogFactory.getLog(ForgeDistributionServiceImpl.class);
  private ForgeDAO         forgeDAO;
  private ForgeRequestDAO  forgeRequestDAO;

  @Override
  public ForgeDTO addForge(final ForgeDTO newForge) throws ForgeDistributionException
  {
    ForgeDTO result;
    try
    {
      final int forgeLevel = newForge.getForgeLevel();
      if (log.isDebugEnabled())
      {
        log.debug("addNewForge forge = " + newForge);
      }
      if (forgeLevel > LOCAL)
      {
        throw new ForgeDistributionException(ExceptionCode.ERR_LEVEL_FORGE);
      }

      Forge forge = convertForgeDTO(newForge);
      forge = forgeDAO.save(forge);
      final Forge parent = forge.getParent();
      final ForgeDTO parentDTO = convertForgeParent(parent);
      result = convertForge(forge);
      result.setParent(parentDTO);

      if (log.isDebugEnabled())
      {
        log.debug(String.format("addNewForge: forge added with id = %s", newForge.getForgeId()));
      }
    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(ExceptionCode.ERR_ADD_NEW_FORGE, e);
    }
    return result;
  }

  @Override
  public ForgeDTO updateForgeDescription(final ForgeDTO forgeDTO) throws ForgeDistributionException
  {
    ForgeDTO result;
    try
    {
      final int forgeLevel = forgeDTO.getForgeLevel();
      if (log.isDebugEnabled())
      {
        log.debug("updateForge forge = " + forgeDTO);
      }
      if (forgeLevel > LOCAL)
      {
        throw new ForgeDistributionException(ExceptionCode.ERR_LEVEL_FORGE);
      }

      Forge forge = forgeDAO.findById(forgeDTO.getForgeId());
      if (forge == null)
      {
        throw new ForgeDistributionException(ExceptionCode.ERR_LOADING_FORGE);
      }

      forge.setDescription(forgeDTO.getDescription());
      forge.setLabel(forgeDTO.getLabel());
      forge.setPortalUri(forgeDTO.getPortalUri());
      forge.setForgeUrl(forgeDTO.getForgeUrl());
      forge.setCertificatePublicKey(forgeDTO.getCertificatePublicKey());

      forge = forgeDAO.update(forge);
      final Forge parent = forge.getParent();
      final ForgeDTO parentDTO = convertForgeParent(parent);
      result = convertForge(forge);
      result.setParent(parentDTO);

      if (log.isDebugEnabled())
      {
        log.debug(String.format("addNewForge: forge added with id = %s", forge.getForgeId()));
      }
    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(ExceptionCode.ERR_UPDATE_FORGE, e);
    }
    return result;
  }

  @Override
  public ForgeDTO getForge(final String forgeId) throws ForgeDistributionException
  {
    log.info("getting forge forgeId: " + forgeId);
    if ((null == forgeId) || "".equals(forgeId))
    {
      throw new IllegalArgumentException("forgeId should not be null or empty");
    }
    Forge forge;
    ForgeDTO result = null;

    try
    {
      forge = forgeDAO.findById(UUID.fromString(forgeId));
      log.info("getted forge: " + forge);
    }
    catch (final NoResultException e)
    {
      throw new ForgeDistributionException(ExceptionCode.ERR_LOADING_FORGE);
    }
    if (forge != null)
    {
      final Forge parent = forge.getParent();
      final ForgeDTO parentDTO = convertForgeParent(parent);
      result = convertForge(forge);
      result.setParent(parentDTO);
    }
    return result;
  }

  @Override
  public List<ForgeDTO> getAvailableMotherForgesToSubscription() throws ForgeDistributionException
  {
    final List<ForgeDTO> result = new LinkedList<ForgeDTO>();
    try
    {
      final Collection<Forge> forgeslvl0 = forgeDAO.findByLevel(0);
      for (final Forge forge : forgeslvl0)
      {
        result.add(convertForge(forge));
      }
      final Collection<Forge> forgeslvl1 = forgeDAO.findByLevel(1);
      for (final Forge forge : forgeslvl1)
      {
        result.add(convertForge(forge));
      }
    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(ExceptionCode.ERR_LOADING_LIST_FORGE, e);
    }
    return result;
  }

  @Override
  public ForgeRequestDTO subscribeForge(final String sourceForgeId, final String destinationForgeId,
      final String comment) throws ForgeDistributionException
  {
    try
    {
      return createForgeRequest(sourceForgeId, destinationForgeId, comment, RequestType.SUBSCRIBE);

    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(ExceptionCode.ERR_DEMAND_AFFILIATION, e);
    }
  }

  @Override
  public ForgeRequestDTO approveSubription(final String forgeRequestId) throws ForgeDistributionException
  {
    ForgeRequest forgeRequest;
    try
    {
      forgeRequest = forgeRequestDAO.findById(UUID.fromString(forgeRequestId));

      // daughter
      final Forge sourceForge = forgeRequest.getSourceForge();
      // mother
      final Forge destinationForge = forgeRequest.getDestinationForge();

      // Update forge level
      final boolean levelError = updateForgeLevelAfterSubscription(destinationForge, sourceForge);
      if (levelError)
      {
        throw new ForgeDistributionException(ExceptionCode.ERR_LEVEL_FORGE);
      }
      else
      {
        sourceForge.setAffiliationDate(new Date());
        destinationForge.addChild(sourceForge);
        sourceForge.setParent(destinationForge);

        forgeRequest.setRequestStatus(RequestStatus.VALIDATED);
        forgeRequest.setResponseDate(new Date());
        forgeRequest.setSourceForge(sourceForge);
        forgeRequest.setDestinationForge(destinationForge);

        forgeDAO.update(sourceForge);
        forgeDAO.update(destinationForge);
        forgeRequestDAO.update(forgeRequest);

        // Notify the forge that it is done.
        final DistributionNotificationService distribNotifSvc = getDistributionNotificationService(sourceForge);
        if (distribNotifSvc == null)
        {
          log.warn(String.format("Unable to notify the forge %s that the subscription has been approved.",
              sourceForge.getLabel()));
        }
        else
        {
          final Collection<Forge> centralForges = forgeDAO.findByLevel(CENTRAL);
          if ((centralForges == null) || (centralForges.size() != 1))
          {
            log.warn(String
                .format(
                    "Unable to get the central forge informations! subscription notification cancelled for forge %s!",
                    sourceForge.getLabel()));
          }
          else
          {
            final Forge centralForge = centralForges.iterator().next();
            distribNotifSvc.subscriptionApproved(convertForge(sourceForge),
                centralForge.getCertificatePublicKey());
          }
        }
      }
    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(String.format(
          "Exception Unable to approve the subscription [forgeRequestId=%s]", forgeRequestId), e);
    }
    return convertForgeRequest(forgeRequest);
  }

  @Override
  public ForgeRequestDTO disapproveSubription(final String forgeRequestId, final String comment)
      throws ForgeDistributionException
  {
    ForgeRequest forgeRequest;
    try
    {
      forgeRequest = forgeRequestDAO.findById(UUID.fromString(forgeRequestId));
      forgeRequest.setRequestComment(comment);
      forgeRequest.setRequestStatus(RequestStatus.REFUSED);
      forgeRequest.setResponseDate(new Date());
      forgeRequestDAO.update(forgeRequest);
    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(String.format(
          "Exception Unable to disapprove the subscription [forgeRequestId=%s]", forgeRequestId), e);
    }
    return convertForgeRequest(forgeRequest);
  }

  @Override
  public ForgeRequestDTO unsubscribeForge(final String sourceForgeId, final String destinationForgeId,
      final String comment) throws ForgeDistributionException
  {
    try
    {
      return createForgeRequest(sourceForgeId, destinationForgeId, comment, RequestType.UNSUBSCRIBE);
    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(String.format(
          "Exception Unable to unsubsribe the subscription [sourceForgeId=%s]", sourceForgeId), e);
    }
  }

  @Override
  public ForgeRequestDTO approveUnsubscription(final String forgeRequestId) throws ForgeDistributionException
  {
    ForgeRequest forgeRequest;
    try
    {
      forgeRequest = forgeRequestDAO.findById(UUID.fromString(forgeRequestId));
      // daughter
      final Forge sourceForge = forgeRequest.getSourceForge();
      // mother
      final Forge destinationForge = forgeRequest.getDestinationForge();
      destinationForge.removeChild(sourceForge);

      sourceForge.setForgeLevel(SUSPENDED);

      forgeRequest.setRequestStatus(RequestStatus.VALIDATED);
      forgeRequest.setResponseDate(new Date());

      forgeRequest.setSourceForge(sourceForge);
      forgeRequest.setDestinationForge(destinationForge);

      forgeDAO.update(sourceForge);
      forgeDAO.update(destinationForge);
      forgeRequestDAO.update(forgeRequest);

      // Notify the forge that it is done.
      final DistributionNotificationService distribNotifSvc = getDistributionNotificationService(sourceForge);
      if (distribNotifSvc == null)
      {
        log.warn(String.format("Unable to notify the forge %s that the unsubscription has been approved.",
            sourceForge.getLabel()));
      }
      else
      {
        distribNotifSvc.unsubscriptionApproved(convertForge(sourceForge),
            destinationForge.getCertificatePublicKey());
      }

    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(String.format(
          "Exception Unable to approve the unsubscription [forgeRequestId=%s]", forgeRequestId), e);
    }
    return convertForgeRequest(forgeRequest);
  }

  @Override
  public ForgeRequestDTO disapproveUnsubscription(final String forgeRequestId, final String comment)
      throws ForgeDistributionException
  {
    ForgeRequest forgeRequest;
    try
    {
      forgeRequest = forgeRequestDAO.findById(UUID.fromString(forgeRequestId));
      forgeRequest.setRequestComment(comment);
      forgeRequest.setRequestStatus(RequestStatus.REFUSED);
      forgeRequest.setResponseDate(new Date());
      forgeRequestDAO.update(forgeRequest);
    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(String.format(
          "Exception Unable to disapprove the unsubscription [forgeRequestId=%s]", forgeRequestId), e);
    }
    return convertForgeRequest(forgeRequest);
  }

  @Override
  public List<ForgeRequestDTO> getForgeRequests(final String forgeId, final String type)
      throws ForgeDistributionException
  {
    final List<ForgeRequestDTO> result = new ArrayList<ForgeRequestDTO>();
    try
    {
      final List<ForgeRequest> forgeRequests = forgeRequestDAO.findByForgeIdAndStatus(
          UUID.fromString(forgeId), RequestStatus.IN_PROGRESS, type);

      for (final ForgeRequest forgeRequest : forgeRequests)
      {
        result.add(convertForgeRequest(forgeRequest));
      }

    }
    catch (final Exception e)
    {
      throw new ForgeDistributionException(
          String.format("Unable to get forge requests [forgeId=%s]", forgeId), e);
    }
    return result;
  }

  /**
   * @param newForge
   *
   * @return
   */
  private Forge convertForgeDTO(final ForgeDTO newForge)
  {
    final Forge forge = forgeDAO.newForge();
    forge.setForgeId(newForge.getForgeId());
    forge.setDescription(newForge.getDescription());
    forge.setLabel(newForge.getLabel());
    forge.setForgeLevel(newForge.getForgeLevel());
    forge.setPortalUri(newForge.getPortalUri());
    forge.setParent(null);
    forge.setForgeUrl(newForge.getForgeUrl());
    forge.setCertificatePublicKey(newForge.getCertificatePublicKey());
    return forge;
  }

  private ForgeDTO convertForgeParent(final Forge parent)
  {
    if (parent == null)
    {
      return null;
    }
    final ForgeDTO result = new ForgeDTO();
    result.setForgeId(parent.getForgeId());
    result.setDescription(parent.getDescription());
    result.setLabel(parent.getLabel());
    result.setForgeLevel(parent.getForgeLevel());
    result.setPortalUri(parent.getPortalUri());
    result.setCertificatePublicKey(parent.getCertificatePublicKey());
    result.setAffiliationDate(parent.getAffiliationDate());
    result.setForgeUrl(parent.getForgeUrl());
    return result;
  }

  private ForgeDTO convertForge(final Forge forge)
  {
    if (forge == null)
    {
      return null;
    }
    final ForgeDTO result = new ForgeDTO();
    result.setForgeId(forge.getForgeId());
    result.setDescription(forge.getDescription());
    result.setLabel(forge.getLabel());
    result.setForgeLevel(forge.getForgeLevel());
    result.setPortalUri(forge.getPortalUri());
    result.setAffiliationDate(forge.getAffiliationDate());
    result.setForgeUrl(forge.getForgeUrl());
    result.setCertificatePublicKey(forge.getCertificatePublicKey());
    final Set<Forge> children = forge.getChildren();

    if (children != null)
    {
      for (final Forge child : children)
      {
        result.addChild(convertForge(child));
      }
    }
    return result;
  }

  private ForgeRequestDTO convertForgeRequest(final ForgeRequest forgeRequest)
  {

    if (forgeRequest == null)
    {
      return null;
    }

    final ForgeRequestDTO result = new ForgeRequestDTO();
    result.setForgeRequestId(forgeRequest.getForgeRequestId());
    result.setRequestType(forgeRequest.getRequestType());
    result.setRequestStatus(forgeRequest.getRequestStatus());
    result.setRequestComment(forgeRequest.getRequestComment());
    result.setRequestDate(forgeRequest.getRequestDate());
    result.setResponseDate(forgeRequest.getResponseDate());
    result.setSourceForge(convertForge(forgeRequest.getSourceForge()));
    result.setDestinationForge(convertForge(forgeRequest.getDestinationForge()));

    return result;

  }

  private ForgeRequestDTO createForgeRequest(final String sourceForgeId, final String destinationForgeId,
                                             final String comment, final RequestType requestType)
      throws NoResultException
  {

    final Forge sourceForge      = forgeDAO.findById(UUID.fromString(sourceForgeId));
    final Forge destinationForge = forgeDAO.findById(UUID.fromString(destinationForgeId));

    ForgeRequest forgeRequest = forgeRequestDAO.newForgeRequest();
    forgeRequest.setForgeRequestId(UUID.randomUUID());
    forgeRequest.setRequestComment(comment);
    forgeRequest.setRequestDate(new Date());
    forgeRequest.setRequestStatus(RequestStatus.IN_PROGRESS);
    forgeRequest.setRequestType(requestType);

    forgeRequest = forgeRequestDAO.save(forgeRequest);

    forgeRequest.setSourceForge(sourceForge);
    forgeRequest.setDestinationForge(destinationForge);

    final ForgeRequest result = forgeRequestDAO.update(forgeRequest);
    return convertForgeRequest(result);

  }

  private boolean updateForgeLevelAfterSubscription(final Forge motherForge, final Forge daughterForge)
  {
    boolean levelError = false;
    // only one parent is possible
    if ((daughterForge.getParent() != null) && (daughterForge.getForgeLevel() != SUSPENDED))
    {
      levelError = true;
    }
    else
    {
      int levelMotherForge = motherForge.getForgeLevel();
      if ((levelMotherForge == SUSPENDED) || (levelMotherForge == ORPHAN))
      {
        levelMotherForge = CENTRAL;
      }
      final int level = levelMotherForge + 1;
      if (level < MAX_LEVEL)
      {
        daughterForge.setForgeLevel(level);
        motherForge.setForgeLevel(levelMotherForge);
      }
    }
    return levelError;
  }

  private DistributionNotificationService getDistributionNotificationService(final Forge forge)
  {
    final String forgeUrl = forge.getForgeUrl().toExternalForm();
    DistributionNotificationService synchService = null;
    final ClassLoader theGoodOne = ClientProxyFactoryBean.class.getClassLoader();
    final ClassLoader theOldOne = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(theGoodOne);

    try
    {
      final ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
      factory.setServiceClass(DistributionNotificationService.class);
      factory.setAddress(String.format("%s/cxf/%s", forgeUrl,
          DistributionNotificationService.DISTRIB_NOTIF_ENDPOINT_NAME));
      synchService = (DistributionNotificationService) factory.create();
    }
    catch (final Exception e)
    {
      log.warn(String.format("Can not get a webservice for the service %s on the forge %s",
          DistributionNotificationService.class.getName(), forgeUrl));
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(theOldOne);
    }
    return synchService;
  }

  /**
   * @param pForgeDAO
   *          the forgeDAO to set
   */
  public void setForgeDAO(final ForgeDAO pForgeDAO)
  {
    forgeDAO = pForgeDAO;
  }

  /**
   * @param pForgeRequestDAO
   *          the forgeRequestDAO to set
   */
  public void setForgeRequestDAO(final ForgeRequestDAO pForgeRequestDAO)
  {
    forgeRequestDAO = pForgeRequestDAO;
  }

}
