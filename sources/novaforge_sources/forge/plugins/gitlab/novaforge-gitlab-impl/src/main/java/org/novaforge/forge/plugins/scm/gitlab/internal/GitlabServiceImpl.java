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

package org.novaforge.forge.plugins.scm.gitlab.internal;

import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.dao.UuidDAO;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.core.plugins.domain.route.PluginAssociation;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginService;
import org.novaforge.forge.plugins.commons.services.domains.PluginMetadataImpl;
import org.novaforge.forge.plugins.commons.services.domains.PluginViewImpl;
import org.novaforge.forge.plugins.scm.constants.GitlabRole;
import org.novaforge.forge.plugins.scm.gitlab.services.GitlabConfigurationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Gauthier Cart
 */
public class GitlabServiceImpl extends AbstractPluginService implements PluginService
{
  /**
   * Default constructor
   * 
   * @param pUuidDAO
   *          uuiDAO service reference
   */
  public GitlabServiceImpl(final UuidDAO pUuidDAO)
  {
    super(pUuidDAO);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getIconPath()
  {
    return "/gitlab.png";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginAssociation getAssociationInfo()
  {
    return new GitlabCommunication();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<PluginView> buildPluginViews()
  {
    final List<PluginView> viewList = new ArrayList<PluginView>();

    viewList.add(new PluginViewImpl(PluginViewEnum.DEFAULT, getDefaultAccess()));
    final GitlabConfigurationService gitlabConfiguration = getPluginConfigurationService(GitlabConfigurationService.class);
    viewList.add(new PluginViewImpl(PluginViewEnum.ADMINISTRATION, gitlabConfiguration.getAdminAccess()));

    return viewList;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginServiceMetadata getMetadata() throws PluginServiceException
  {
    PluginServiceMetadata pluginServiceMetadata = null;
    if (getServiceUUID() != null)
    {
      pluginServiceMetadata = new PluginMetadataImpl(getServiceUUID(), getDescription(), getType(),
                                                     Category.SCM.getId(), new GitlabQueues(), getPluginViews(),
                                                     getVersion());
    }
    return pluginServiceMetadata;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> findRoles()
  {
    final Set<String> returnList = new HashSet<String>();
    for (final GitlabRole roles : GitlabRole.values())
    {
      returnList.add(roles.getLabel());
    }
    return returnList;

  }

}
