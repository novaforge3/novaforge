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
package org.novaforge.forge.core.organization.internal.presenter;

import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.CompositionServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.presenters.CompositionPresenter;
import org.novaforge.forge.core.organization.services.CompositionService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;
import java.util.Map;

/**
 * This is an implementation of {@link CompositionPresenter}
 * 
 * @author Guillaume Lamirand
 */
public class CompositionPresenterImpl implements CompositionPresenter
{
  /**
   * Reference to {@link CompositionService} service injected by the container
   */
  private CompositionService compositionService;
  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate   securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public Composition newComposition()
  {
    return compositionService.newComposition();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Composition createCompositionWithTemplate(final String pProjectId, final Composition pComposition,
      final String pSourceUri, final String pTargetUri, final Map<String, String> pTemplate)
      throws CompositionServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Composition.class,
        PermissionAction.CREATE));
    return compositionService.createComposition(pProjectId, pComposition, pSourceUri, pTargetUri, pTemplate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Composition createComposition(final String pProjectId, final Composition pComposition,
      final String pSourceUri, final String pTargetUri) throws CompositionServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Composition.class,
        PermissionAction.CREATE));
    return compositionService.createComposition(pProjectId, pComposition, pSourceUri, pTargetUri, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteComposition(final String pProjectId, final String pUUID) throws CompositionServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Composition.class,
                                                                             PermissionAction.DELETE));
    return compositionService.deleteComposition(pProjectId, pUUID);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Composition> getComposition(final String pProjectId) throws CompositionServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Composition.class, PermissionAction.READ));
    return compositionService.getComposition(pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Composition> getCompositionFromSource(final String pProjectId, final String pSourceInstance,
                                                    final CompositionType pSourceType, final String pSourceName)
      throws CompositionServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Composition.class, PermissionAction.READ));
    return compositionService.getCompositionFromSource(pProjectId, pSourceInstance, pSourceType, pSourceName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Composition> getCompositionFromSource(final String pProjectId, final String pSourceInstance)
      throws CompositionServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Composition.class,
        PermissionAction.READ));
    return compositionService.getCompositionFromSource(pSourceInstance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateCompositionTemplate(final String pProjectId, final String pUUID,
                                        final Map<String, String> pTemplate) throws CompositionServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Composition.class, PermissionAction.CREATE,
                                                                             PermissionAction.UPDATE));
    compositionService.updateCompositionTemplate(pProjectId, pUUID, pTemplate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCompositionStatus(final String pProjectId, final String pUUID, final boolean pStatus)
      throws CompositionServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Composition.class, PermissionAction.CREATE,
                                                                             PermissionAction.UPDATE));
    compositionService.setCompositionStatus(pProjectId, pUUID, pStatus);
  }

  /**
   * Use by container to inject {@link CompositionService} implementation
   * 
   * @param pCompositionService
   *          the compositionService to set
   */
  public void setCompositionService(final CompositionService pCompositionService)
  {
    compositionService = pCompositionService;
  }

  /**
   * Use by container to inject {@link SecurityDelegate} implementation
   * 
   * @param pSecurityDelegate
   *          the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate pSecurityDelegate)
  {
    securityDelegate = pSecurityDelegate;
  }

}
