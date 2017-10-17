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
package org.novaforge.forge.plugins.ecm.alfresco.internal;

import org.novaforge.forge.commons.technical.cxf.CxfConfigService;
import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.dao.UuidDAO;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.core.plugins.domain.route.PluginAssociation;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginService;
import org.novaforge.forge.plugins.commons.services.domains.PluginMetadataImpl;
import org.novaforge.forge.plugins.commons.services.domains.PluginViewImpl;
import org.novaforge.forge.plugins.ecm.alfresco.constant.AlfrescoDataConstant;
import org.novaforge.forge.plugins.ecm.alfresco.constant.AlfrescoRolePrivilege;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoConfigurationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is an implementation of PluginService for alfresco plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginService
 * @see org.novaforge.forge.core.plugins.services.PluginService
 * @author cadetr
 */
public class AlfrescoServiceImpl extends AbstractPluginService
{

  /**
   * Reference to implementation of {@link CxfConfigService} service
   */
  private CxfConfigService cxfConfigService;

  /**
   * Default constructor
   * 
   * @param pUuidDAO
   *          uuiDAO service reference
   */
  public AlfrescoServiceImpl(final UuidDAO pUuidDAO)
  {
    super(pUuidDAO);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getIconPath()
  {
    return "/alfresco.png";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginAssociation getAssociationInfo() throws PluginServiceException
  {
    return new AlfrescoAssociation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reactivate() throws PluginServiceException
  {
    throw new UnsupportedOperationException("Alfresco is not compatible with reactivate method.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void desactivate(final Map<String, String> pSuperAdmins) throws PluginServiceException
  {
    throw new UnsupportedOperationException("Alfresco is not compatible with desactivate method.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() throws PluginServiceException
  {
    throw new UnsupportedOperationException("Alfresco is not compatible with stop method.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPluginDataServiceUri()
  {
    return cxfConfigService.getWebserviceUri(AlfrescoDataConstant.ALFRESCO_DATA_SERVICE_NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<PluginView> buildPluginViews()
  {
    final List<PluginView> viewList = new ArrayList<PluginView>();

    viewList.add(new PluginViewImpl(PluginViewEnum.DEFAULT, getDefaultAccess()));
    final AlfrescoConfigurationService alfrescoConfiguration = getPluginConfigurationService(AlfrescoConfigurationService.class);
    viewList.add(new PluginViewImpl(PluginViewEnum.ADMINISTRATION, alfrescoConfiguration.getAdminAccess()));
    return viewList;
  }

  /**
   * {@inheritDoc}
   *
   * @throws PluginServiceException
   */
  @Override
  public PluginServiceMetadata getMetadata() throws PluginServiceException
  {
    PluginServiceMetadata pluginServiceMetadata = null;
    if (getServiceUUID() != null)
    {
      pluginServiceMetadata = new PluginMetadataImpl(getServiceUUID(), getDescription(), getType(),
                                                     Category.ECM.getId(), new AlfrescoQueues(), getPluginViews(),
                                                     getVersion());
    }
    return pluginServiceMetadata;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> findRoles() throws PluginServiceException
  {
    final Set<String> returnList = new HashSet<String>();
    for (final AlfrescoRolePrivilege roles : AlfrescoRolePrivilege.values())
    {
      returnList.add(roles.getLabel());
    }
    return returnList;
  }

  /**
   * @param cxfConfigService
   *          the cxfConfigService to set
   */
  public void setCxfConfigService(final CxfConfigService cxfConfigService)
  {
    this.cxfConfigService = cxfConfigService;
  }

}
