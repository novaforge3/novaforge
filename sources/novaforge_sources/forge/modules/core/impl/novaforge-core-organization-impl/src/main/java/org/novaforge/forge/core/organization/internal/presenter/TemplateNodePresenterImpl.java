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

import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.presenters.TemplateNodePresenter;
import org.novaforge.forge.core.organization.services.TemplateNodeService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This is an implementation of {@link TemplateNodePresenter}
 * 
 * @see TemplateNodePresenter
 * @author Guillaume Lamirand
 */
public class TemplateNodePresenterImpl implements TemplateNodePresenter
{
  // FIXME We should manage some authorizations
  /**
   * Reference to {@link TemplateNodeService} service injected by the container
   */
  private TemplateNodeService templateNodeService;

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateApplication addApplication(final String pTemplateId, final String pParentNodeUri,
      final String pApplicationLabel, final UUID pPluginUUID, final Map<String, String> pRolesMapping)
      throws TemplateServiceException
  {
    return templateNodeService.addApplication(pTemplateId, pParentNodeUri, pApplicationLabel, pPluginUUID,
        pRolesMapping);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeApplication(final String pTemplateId, final String pParentNodeUri, final String pApplicationUri)
      throws TemplateServiceException
  {

    templateNodeService.deleteApplication(pTemplateId, pParentNodeUri, pApplicationUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateApplication getApplication(final String pTemplateId, final String pApplicationUri)
      throws TemplateServiceException
  {
    return templateNodeService.getApplication(pTemplateId, pApplicationUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TemplateApplication> getAllTemplateApplications(final String pTemplateId)
      throws TemplateServiceException
  {
    return templateNodeService.getAllTemplateApplications(pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TemplateApplication> getAllSpaceApplications(final String pSpaceNodeUri,
      final String pTemplateId) throws TemplateServiceException
  {
    return templateNodeService.getAllSpaceApplications(pSpaceNodeUri, pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Space> getAllSpaces(final String pTemplateId) throws TemplateServiceException
  {
    return templateNodeService.getAllSpaces(pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space getSpace(final String pTemplateId, final String pSpaceNodeUri) throws TemplateServiceException
  {
    return templateNodeService.getSpace(pTemplateId, pSpaceNodeUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateSpace(final String pTemplateId, final Space pSpace) throws TemplateServiceException
  {
    templateNodeService.updateSpace(pTemplateId, pSpace);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSpace(final String pTemplateId, final String pSpaceNodeUri) throws TemplateServiceException
  {
    templateNodeService.deleteSpace(pTemplateId, pSpaceNodeUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space addSpace(final String pTemplateId, final Space pSpace) throws TemplateServiceException
  {
    return templateNodeService.addSpace(pTemplateId, pSpace);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space newSpace()
  {
    return templateNodeService.newSpace();
  }

  /**
   * Use by container to inject {@link TemplateNodeService} implementation
   * 
   * @param pTemplateNodeService
   *          the templateNodeService to set
   */
  public void setTemplateNodeService(final TemplateNodeService pTemplateNodeService)
  {
    templateNodeService = pTemplateNodeService;
  }

}
