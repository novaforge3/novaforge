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
package org.novaforge.forge.ui.distribution.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.security.authorization.AuthorizationService;
import org.novaforge.forge.core.security.authorization.Logical;
import org.novaforge.forge.core.security.authorization.PermissionHandler;
import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;
import org.novaforge.forge.distribution.reference.model.SynchDiffered;
import org.novaforge.forge.distribution.reference.model.TargetForge;
import org.novaforge.forge.distribution.reference.service.DiffusionService;
import org.novaforge.forge.distribution.register.domain.RequestStatus;
import org.novaforge.forge.distribution.register.domain.RequestType;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;
import org.novaforge.forge.distribution.register.exceptions.ForgeException;
import org.novaforge.forge.distribution.register.services.ForgeDistributionService;
import org.novaforge.forge.distribution.reporting.client.ForgeReportingClient;
import org.novaforge.forge.distribution.reporting.exceptions.ForgeReportingException;
import org.novaforge.forge.ui.distribution.client.service.DistributionService;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeRequestDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ForgeViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.OrganizationViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ProfilViewDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchDifferedDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.ComponentToSyncEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.RequestStatusEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.StatusDataAccessEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeForgeRequestEnum;
import org.novaforge.forge.ui.distribution.shared.exceptions.DistributionServiceException;
import org.novaforge.forge.ui.distribution.shared.exceptions.ErrorEnumeration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author BILET-JC
 */
public class DistributionServiceImpl extends RemoteServiceServlet implements DistributionService
{

  private static final Log  log              = LogFactory.getLog(DistributionServiceImpl.class);

  private static final long serialVersionUID = 6346447256283547956L;

  @Override
  public ForgeDTO getCurrentForge() throws DistributionServiceException
  {
    ForgeDTO currentForge = null;
    try
    {
      currentForge = getForge(getIdentificationManager().getForgeId().toString());
    }
    catch (final Exception e)
    {
      manageException("Unable to get the current forge", e);
    }

    return currentForge;
  }

  @Override
  public ForgeDTO approveDistributionRequest(final TypeDistributionRequestEnum pType,
      final String pAffiliationId) throws DistributionServiceException
  {
    ForgeDTO currentForge = null;
    try
    {
      if (TypeDistributionRequestEnum.SUBSCRIPTION.equals(pType))
      {
        getForgeDistributionService().approveSubription(pAffiliationId);
      }
      else if (TypeDistributionRequestEnum.UNSUBSCRIPTION.equals(pType))
      {
        getForgeDistributionService().approveUnsubscription(pAffiliationId);
      }
      currentForge = getCurrentForge();
    }
    catch (final ForgeDistributionException e)
    {
      manageException("Unable to get approve (un)subscription list", e);
    }
    return currentForge;

  }

  @Override
  public void disapproveDistributionRequest(final TypeDistributionRequestEnum pType,
      final String pAffiliationId, final String pReason) throws DistributionServiceException
  {
    try
    {
      if (TypeDistributionRequestEnum.SUBSCRIPTION.equals(pType))
      {
        getForgeDistributionService().disapproveSubription(pAffiliationId, pReason);
      }
      else if (TypeDistributionRequestEnum.UNSUBSCRIPTION.equals(pType))
      {
        getForgeDistributionService().disapproveUnsubscription(pAffiliationId, pReason);
      }
    }
    catch (final ForgeDistributionException e)
    {
      manageException("Unable to get disapprove (un)subscription list", e);
    }

  }

  @Override
  public List<ForgeRequestDTO> getForgeRequestList(final TypeForgeRequestEnum pType)
      throws DistributionServiceException
  {
    String idForge = null;
    Collection<org.novaforge.forge.distribution.register.domain.ForgeRequestDTO> forgeRequests;
    List<ForgeRequestDTO> results = new ArrayList<>();
    try
    {
      idForge = getIdentificationManager().getForgeId().toString();
      forgeRequests = getForgeDistributionService().getForgeRequests(idForge, pType.getLabel());
      if (forgeRequests != null)
      {
        for (final org.novaforge.forge.distribution.register.domain.ForgeRequestDTO request : forgeRequests)
        {
          final ForgeRequestDTO forgeRequestDTO = new ForgeRequestDTO();
          forgeRequestDTO.setType(convertRequestTypeEnum(request.getRequestType()));
          forgeRequestDTO.setForgeRequestId(request.getForgeRequestId().toString());
          forgeRequestDTO.setDestinationForgeId(request.getDestinationForge().getForgeId().toString());
          forgeRequestDTO.setDestinationForgeLabel(request.getDestinationForge().getLabel());
          forgeRequestDTO.setSourceForgeId(request.getSourceForge().getForgeId().toString());
          forgeRequestDTO.setSourceForgeLabel(request.getSourceForge().getLabel());
          if (request.getRequestDate() != null)
          {
            forgeRequestDTO.setDate(new Date(request.getRequestDate().getTime()));
          }
          forgeRequestDTO.setStatus(convertRequestStatusEnum(request.getRequestStatus()));
          forgeRequestDTO.setReason(request.getRequestComment());
          forgeRequestDTO.setForgeRequestId(request.getForgeRequestId().toString());
          results.add(forgeRequestDTO);
        }
      }
      else
      {
        results = null;
      }
    }
    catch (final ForgeDistributionException e)
    {
      manageException(String.format("Unable to get (un)subscription list with [forgeId=%s]", idForge), e);
    }
    return results;
  }

  @Override
  public void createSubscription(final ForgeRequestDTO pSubscriptionDTO) throws DistributionServiceException
  {

    String idForge;
    try
    {
      idForge = getIdentificationManager().getForgeId().toString();
      if (TypeDistributionRequestEnum.SUBSCRIPTION.equals(pSubscriptionDTO.getType()))
      {
        getForgeDistributionService().subscribeForge(idForge, pSubscriptionDTO.getDestinationForgeId(),
            pSubscriptionDTO.getReason());
      }
      else if (TypeDistributionRequestEnum.UNSUBSCRIPTION.equals(pSubscriptionDTO.getType()))
      {
        getForgeDistributionService().unsubscribeForge(idForge, pSubscriptionDTO.getDestinationForgeId(),
            pSubscriptionDTO.getReason());
      }
    }
    catch (final ForgeDistributionException e)
    {
      manageException("Unable to get create (un)subscription list", e);
    }
  }

  @Override
  public ForgeDTO getForge(final String pCurrentIdForge) throws DistributionServiceException
  {

    ForgeDTO currentForge = null;
    org.novaforge.forge.distribution.register.domain.ForgeDTO currentForgeTmp;
    try
    {
      currentForgeTmp = getForgeDistributionService().getForge(pCurrentIdForge);
      if (currentForgeTmp != null)
      {
        final List<ForgeDTO> childsForge = new ArrayList<ForgeDTO>();
        final List<ForgeDTO> underChildsForge = new ArrayList<ForgeDTO>();
        // current
        currentForge = convertForge(currentForgeTmp);
        // mother
        if (currentForgeTmp.getParent() != null)
        {
          currentForge.setParent(convertForge(currentForgeTmp.getParent()));
        }
        // child
        if ((currentForgeTmp.getChildren() != null) && (currentForgeTmp.getChildren().size() != 0))
        {
          for (final org.novaforge.forge.distribution.register.domain.ForgeDTO childForgeTmp : currentForgeTmp
              .getChildren())
          {
            final ForgeDTO childForge = convertForge(childForgeTmp);
            childForge.setParent(currentForge);
            childsForge.add(childForge);
            // underchild
            if ((childForgeTmp.getChildren() != null) && (childForgeTmp.getChildren().size() != 0))
            {
              for (final org.novaforge.forge.distribution.register.domain.ForgeDTO underChildForgeTmp : childForgeTmp
                  .getChildren())
              {
                final ForgeDTO underChildForge = convertForge(underChildForgeTmp);
                underChildForge.setParent(childForge);
                underChildsForge.add(underChildForge);
              }
              childForge.setChildren(underChildsForge);
            }
          }
          currentForge.setChildren(childsForge);
        }
        // existing mother subscription
        final List<ForgeRequestDTO> forgeRequests = getForgeRequestList(TypeForgeRequestEnum.SOURCE);
        if (forgeRequests != null)
        {
          for (final ForgeRequestDTO forgeRequest : forgeRequests)
          {
            if (pCurrentIdForge.equals(forgeRequest.getSourceForgeId())
                && RequestStatusEnum.IN_PROGRESS.equals(forgeRequest.getStatus()))
            {
              currentForge.setParentRequest(forgeRequest);
            }
          }
        }
      }
    }
    catch (final ForgeDistributionException e)
    {
      manageException(String.format("Unable to get forge with [forgeId=%s]", pCurrentIdForge), e);
    }
    return currentForge;

  }

  @Override
  public List<ForgeDTO> getMotherList() throws DistributionServiceException
  {
    List<ForgeDTO> results = new ArrayList<>();
    try
    {
      final List<org.novaforge.forge.distribution.register.domain.ForgeDTO> availableMothers = getForgeDistributionService()
                                                                                                   .getAvailableMotherForgesToSubscription();
      if (availableMothers != null)
      {
        final Set<org.novaforge.forge.distribution.register.domain.ForgeDTO> mothersTmp = new HashSet<>(availableMothers);
        for (final org.novaforge.forge.distribution.register.domain.ForgeDTO motherTmp : mothersTmp)
        {
          final ForgeDTO motherDTO = new ForgeDTO();
          motherDTO.setLabel(motherTmp.getLabel());
          motherDTO.setForgeId(motherTmp.getForgeId().toString());
          results.add(motherDTO);
        }
      }
      else
      {
        results = null;
      }
    }
    catch (final ForgeDistributionException e)
    {
      manageException("Unable to get mother forge list", e);
    }
    return results;
  }

  @Override
  public List<ForgeViewDTO> getIndicatorsByForgeView() throws DistributionServiceException
  {
    List<ForgeViewDTO> results = new ArrayList<ForgeViewDTO>();
    List<org.novaforge.forge.distribution.reporting.domain.ihm.ForgeViewDTO> forgeViews;
    try
    {
      forgeViews = getLoadingClient().getForgeView(getIdentificationManager().getForgeId());
      if (forgeViews != null)
      {
        for (final org.novaforge.forge.distribution.reporting.domain.ihm.ForgeViewDTO forgeView : forgeViews)
        {
          results.add(convertForgeView(forgeView));
        }
      }
      else
      {
        results = null;
      }
    }
    catch (final ForgeReportingException e)
    {
      manageException("Unable to get indicators by forge view", e);
    }
    return results;
  }

  @Override
  public List<OrganizationViewDTO> getIndicatorsByOrganizationView() throws DistributionServiceException
  {
    List<OrganizationViewDTO> results = new ArrayList<OrganizationViewDTO>();
    List<org.novaforge.forge.distribution.reporting.domain.ihm.OrganizationViewDTO> organizationViews;
    try
    {
      organizationViews = getLoadingClient().getOrganizationView(getIdentificationManager().getForgeId());
      if (organizationViews != null)
      {
        for (final org.novaforge.forge.distribution.reporting.domain.ihm.OrganizationViewDTO organizationView : organizationViews)
        {
          results.add(convertOrganizationView(organizationView));
        }
      }
      else
      {
        results = null;
      }
    }
    catch (final ForgeReportingException e)
    {
      manageException("Unable to get indicators by organization view", e);
    }
    return results;
  }

  @Override
  public List<ProfilViewDTO> getIndicatorsByProfilView() throws DistributionServiceException
  {
    List<ProfilViewDTO> results = new ArrayList<ProfilViewDTO>();
    List<org.novaforge.forge.distribution.reporting.domain.ihm.ProfilViewDTO> profilViews;
    try
    {
      profilViews = getLoadingClient().getProfilView(getIdentificationManager().getForgeId());
      if (profilViews != null)
      {
        for (final org.novaforge.forge.distribution.reporting.domain.ihm.ProfilViewDTO profilView : profilViews)
        {
          results.add(convertProfilView(profilView, false));
        }
      }
      else
      {
        results = null;
      }
    }
    catch (final ForgeReportingException e)
    {
      manageException("Unable to get indicators by profil view", e);
    }
    return results;
  }

  /**
   * Convert a {@link org.novaforge.forge.distribution.reporting.domain.ihm.ProfilViewDTO} in
   * {@link ProfilViewDTO}
   *
   * @param pProfilView
   * @return
   */
  private ProfilViewDTO convertProfilView(
      final org.novaforge.forge.distribution.reporting.domain.ihm.ProfilViewDTO pProfilView,
      final boolean pHomogeneous)
  {
    ProfilViewDTO result;
    if (pHomogeneous)
    {
      result = new ProfilViewDTO(pProfilView.getForgeName(), pProfilView.getProjectName(),
          convertRoles(pProfilView.getRoles()), convertStatusDataAccessEnum(pProfilView.getStatus()));
    }
    else
    {
      result = new ProfilViewDTO(pProfilView.getForgeName(), pProfilView.getProjectName(),
          pProfilView.getRoles(), StatusDataAccessEnum.ACCESS);
    }
    return result;
  }

  /**
   * Use to apply particular transformation on role name
   *
   * @param pRoles
   * @return Map<String, Integer>
   */
  private Map<String, Integer> convertRoles(final Map<String, Integer> pRoles)
  {
    final Map<String, Integer> result = new HashMap<>();

    final Set<Map.Entry<String, Integer>> key = pRoles.entrySet();
    for (final Map.Entry<String, Integer> keyEntry : key)
    {
      final String role = keyEntry.getKey().toLowerCase();
      final int account = keyEntry.getValue();
      result.put(role, account);
    }
    return result;

  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public void propagate(final Set<ComponentToSyncEnum> pComponent) throws DistributionServiceException
  {
    final DiffusionService diffusionService = getService(DiffusionService.class);
    for (final ComponentToSyncEnum component : pComponent)
    {
      switch (component)
      {
        case PROJ_REF:
          try
          {
            diffusionService.propagateReferenceProject();
          }
          catch (final Exception e)
          {
            log.error("Unable to propagate the reference project", e);
          }
          break;
        case TEMPLATE:
          try
          {
            diffusionService.propagateTemplates();
          }
          catch (final Exception e)
          {
            log.error("Unable to propagate the templates", e);
          }
          break;
        case TOOLS:
          try
          {
            diffusionService.propagateReferenceTools();
          }
          catch (final ReferenceServiceException e)
          {
            manageException("Unable to propagate the tools", e);
          }
          break;
        case INDICATOR:
          try
          {
            diffusionService.launchReportingExtraction();
          }
          catch (final Exception e)
          {
            log.error("Unable to propagate the templates", e);
          }
          break;
        default:
          break;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TargetForgeDTO> getTargetForges() throws DistributionServiceException
  {
    final DiffusionService diffusionService = getService(DiffusionService.class);
    Set<TargetForge> forges = null;
    try
    {
      forges = diffusionService.getTargetForges();
    }
    catch (final ReferenceServiceException e)
    {
      manageException("Unable to propagate the reference project", e);
    }

    return BuildResources.buildTargetForgesDTO(forges);
  }

  @Override
  public void configureScheduling(final SynchDifferedDTO psynchDifferedDTO, final boolean canPropagate)
      throws DistributionServiceException
  {
    final DiffusionService diffusionService = getService(DiffusionService.class);
    try
    {
      diffusionService.configureScheduling(String.valueOf(psynchDifferedDTO.getHours()),
          String.valueOf(psynchDifferedDTO.getMinutes()), String.valueOf(psynchDifferedDTO.getPeriod()),
          canPropagate);
    }
    catch (final ReferenceServiceException e)
    {
      manageException("Unable to update the scheduling  to the child forges", e);
    }

  }

  @Override
  public void disableScheduling(final boolean canPropagate) throws DistributionServiceException
  {
    final DiffusionService diffusionService = getService(DiffusionService.class);
    try
    {
      diffusionService.disableScheduling(canPropagate);
    }
    catch (final ReferenceServiceException e)
    {
      manageException("Unable to update the scheduling  to the child forges", e);
    }
  }

  @Override
  public SynchDifferedDTO loadSchedulingConfig()
  {
    final DiffusionService diffusionService = getService(DiffusionService.class);
    SynchDiffered schedulingConf = diffusionService.getSchedulingConfig();
    return BuildResources.buildSynchDifferedDTO(schedulingConf);
  }

  @Override
  public boolean hasUserRefProjectAdminPermission()
  {
    final String refProjectId = getForgeManager().getReferentielProjectId();
    final Set<String> permissions = new HashSet<>(1);
    permissions.add(getPermissionHandler().buildProjectAdminPermission(refProjectId));
    return getAuthorizationService().isExplicitlyPermitted(permissions, Logical.AND);
  }

  private ForgeConfigurationService getForgeManager()
  {
    return getService(ForgeConfigurationService.class);
  }

  /*
   * OSGI SERVICES MANAGEMENT
   */

  private PermissionHandler getPermissionHandler()
  {
    return getService(PermissionHandler.class);
  }

  private AuthorizationService getAuthorizationService()
  {
    return getService(AuthorizationService.class);
  }

  /**
   * Convert a {@link org.novaforge.forge.distribution.reporting.domain.ihm.OrganizationViewDTO} in
   * {@link OrganizationViewDTO}
   *
   * @param pOrganizationView
   *
   * @return
   */
  private OrganizationViewDTO convertOrganizationView(final org.novaforge.forge.distribution.reporting.domain.ihm.OrganizationViewDTO pOrganizationView)
  {
    return new OrganizationViewDTO(pOrganizationView.getOrganizationName(), pOrganizationView.getNumberProject(),
                                   convertStatusDataAccessEnum(pOrganizationView.getStatus()));
  }

  /**
   * @return ForgeReportingClient service
   */
  private ForgeReportingClient getLoadingClient()
  {
    return getService(ForgeReportingClient.class);
  }

  /**
   * Convert a {@link org.novaforge.forge.distribution.reporting.domain.ihm.ForgeViewDTO} in
   * {@link ForgeViewDTO}
   *
   * @param pForgeView
   *
   * @return ForgeViewDTO
   */
  private ForgeViewDTO convertForgeView(final org.novaforge.forge.distribution.reporting.domain.ihm.ForgeViewDTO pForgeView)
  {
    ForgeViewDTO f = new ForgeViewDTO(pForgeView.getForgeName(), pForgeView.getNumberProject(),
                                      pForgeView.getNumberAccount(), convertStatusDataAccessEnum(pForgeView
                                                                                                     .getStatus()));

    f.setLastUpdated(pForgeView.getLastUpdated());
    return f;
  }

  private StatusDataAccessEnum convertStatusDataAccessEnum(final org.novaforge.forge.distribution.reporting.domain.ihm.StatusDataAccessEnum pStatusDataAccessEnum)
  {
    StatusDataAccessEnum ret = null;
    switch (pStatusDataAccessEnum)
    {
      case ACCESS:
        ret = StatusDataAccessEnum.ACCESS;
        break;
      case DISTRIBUTION_DOWN:
        ret = StatusDataAccessEnum.DISTRIBUTION_DOWN;
        break;
    }
    return ret;
  }

  /**
   * @return IdentificationManager service
   */
  private ForgeIdentificationService getIdentificationManager()
  {
    return getService(ForgeIdentificationService.class);
  }

  /**
   * @return DistributionClient service
   */
  private ForgeDistributionService getForgeDistributionService()
  {
    return getService(ForgeDistributionService.class);
  }

  /**
   * This method manages exceptions in order to write log error and get ForgeCodeException
   *
   * @param pMessage
   * @param e
   *
   * @throws UserServiceException
   */
  private void manageException(final String pMessage, final Exception e) throws DistributionServiceException
  {
    // handle functional exceptions
    log.error(pMessage, e);
    if ((e instanceof ForgeException) && (((ForgeException) e).getCode() != null))
    {
      final ForgeException fe = (ForgeException) e;
      final ErrorEnumeration error = ErrorEnumeration.valueOf(fe.getCode().name());
      throw new DistributionServiceException(error, e);
    }
    else
    {
      throw new DistributionServiceException(ErrorEnumeration.TECHNICAL_ERROR, e);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T getService(final Class<T> pClassService)
  {

    final String canonicalName = pClassService.getCanonicalName();
    T service;
    try
    {
      final InitialContext initialContext = new InitialContext();
      service = (T) initialContext.lookup(String.format("osgi:service/%s", canonicalName));

    }
    catch (final NamingException e)
    {
      throw new IllegalArgumentException(String.format("Unable to get OSGi service with [interface=%s]", canonicalName),
                                         e);
    }
    return service;
  }

  private TypeDistributionRequestEnum convertRequestTypeEnum(final RequestType requestType)
  {
    TypeDistributionRequestEnum ret = null;
    switch (requestType)
    {
      case SUBSCRIBE:
        ret = TypeDistributionRequestEnum.SUBSCRIPTION;
        break;
      case UNSUBSCRIBE:
        ret = TypeDistributionRequestEnum.UNSUBSCRIPTION;
        break;
    }
    return ret;
  }

  private RequestStatusEnum convertRequestStatusEnum(final RequestStatus requestStatus)
  {
    RequestStatusEnum ret = null;
    switch (requestStatus)
    {
      case VALIDATED:
        ret = RequestStatusEnum.VALIDATED;
        break;
      case IN_PROGRESS:
        ret = RequestStatusEnum.IN_PROGRESS;
        break;
      case REFUSED:
        ret = RequestStatusEnum.REFUSED;
        break;
    }
    return ret;
  }

  private ForgeDTO convertForge(final org.novaforge.forge.distribution.register.domain.ForgeDTO currentForgeTmp)
  {
    final ForgeDTO ret = new ForgeDTO();
    if (currentForgeTmp.getAffiliationDate() != null)
    {
      ret.setAffiliationDate(new Date(currentForgeTmp.getAffiliationDate().getTime()));
    }
    ret.setDescription(currentForgeTmp.getDescription());
    ret.setForgeId(currentForgeTmp.getForgeId().toString());
    ret.setForgeLevel(currentForgeTmp.getForgeLevel());
    ret.setLabel(currentForgeTmp.getLabel());
    ret.setUrl(currentForgeTmp.getForgeUrl().toExternalForm() + currentForgeTmp.getPortalUri());

    return ret;
  }

}
