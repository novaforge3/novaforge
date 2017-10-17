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
package org.novaforge.forge.core.plugins.internal.factory;

import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceQueues;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.core.plugins.domain.route.OptionalPluginGroupQueue;
import org.novaforge.forge.core.plugins.domain.route.PluginQueues;
import org.novaforge.forge.core.plugins.services.PluginMetadataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation of {@link PluginMetadataFactory}
 * 
 * @author Guillaume Lamirand
 */
public class PluginMetadataFactoryImpl implements PluginMetadataFactory
{

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginMetadata createPluginMetadata(final PluginServiceMetadata pPluginServiceMetadata)
  {
    return new PluginMetadataImpl(UUID.fromString(pPluginServiceMetadata.getUUID()),
        pPluginServiceMetadata.getDescription(), pPluginServiceMetadata.getType(),
        pPluginServiceMetadata.getCategory(), pPluginServiceMetadata.getJMSQueues(),
        pPluginServiceMetadata.getPluginViews(), pPluginServiceMetadata.getVersion());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginMetadata createPluginMetadata(final PluginPersistenceMetadata pPluginPersistenceMetadata)
  {
    final PluginPersistenceQueues persistenceQueues = pPluginPersistenceMetadata.getJMSQueues();
    PluginQueues pluginQueues = null;
    if (persistenceQueues.getGroupQueue() != null)
    {
      pluginQueues = new PluginQueuesWithOptionalGroupQueueImpl(persistenceQueues);
    }
    else
    {
      pluginQueues = new PluginQueuesImpl(persistenceQueues);
    }

    return new PluginMetadataImpl(pPluginPersistenceMetadata.getUUID(),
        pPluginPersistenceMetadata.getDescription(), pPluginPersistenceMetadata.getType(),
        pPluginPersistenceMetadata.getCategory(), pluginQueues,
        getPluginsViewImpl(pPluginPersistenceMetadata.getViews()), pPluginPersistenceMetadata.getStatus(),
        pPluginPersistenceMetadata.isAvailable(), pPluginPersistenceMetadata.getVersion());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMetadata> createPluginMetadataList(final List<PluginPersistenceMetadata> pPluginMetadaList)
  {
    final List<PluginMetadata> plugins = new ArrayList<PluginMetadata>();
    if ((pPluginMetadaList != null) && (!pPluginMetadaList.isEmpty()))
    {
      for (final PluginPersistenceMetadata completePlugin : pPluginMetadaList)
      {
        plugins.add(this.createPluginMetadata(completePlugin));
      }
    }
    return plugins;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginPersistenceMetadata updatePluginPersistenceMetadata(final PluginPersistenceMetadata pPluginPersistenceMetadata,
                                                                   final PluginServiceMetadata pPluginServiceMetadata)
  {
    // Update global information
    pPluginPersistenceMetadata.setUUID(UUID.fromString(pPluginServiceMetadata.getUUID()));
    pPluginPersistenceMetadata.setVersion(pPluginServiceMetadata.getVersion());
    pPluginPersistenceMetadata.setDescription(pPluginServiceMetadata.getDescription());
    pPluginPersistenceMetadata.setCategory(pPluginServiceMetadata.getCategory());
    pPluginPersistenceMetadata.setType(pPluginServiceMetadata.getType());

    // Update jms queues
    final PluginPersistenceQueues jmsPersistedQueues = pPluginPersistenceMetadata.getJMSQueues();
    final PluginQueues            jmsQueues          = pPluginServiceMetadata.getJMSQueues();
    jmsPersistedQueues.setMembershipQueue(jmsQueues.getMembershipQueue());
    jmsPersistedQueues.setProjectQueue(jmsQueues.getProjectQueue());
    jmsPersistedQueues.setRolesMappingQueue(jmsQueues.getRolesMappingQueue());
    jmsPersistedQueues.setUserQueue(jmsQueues.getUserQueue());

    // Optional group jms queue
    if (jmsQueues instanceof OptionalPluginGroupQueue)
    {
      jmsPersistedQueues.setGroupQueue(((OptionalPluginGroupQueue) jmsQueues).getGroupQueue());
    }

    // Update views
    final List<PluginViewEnum> enums = new ArrayList<PluginViewEnum>();
    for (final PluginView view : pPluginServiceMetadata.getPluginViews())
    {
      enums.add(view.getViewId());
    }
    pPluginPersistenceMetadata.setViews(enums);
    return pPluginPersistenceMetadata;
  }

  private List<PluginView> getPluginsViewImpl(final List<PluginViewEnum> pPluginViews)
  {
    final List<PluginView> pluginsViewImpl = new ArrayList<PluginView>();
    for (final PluginViewEnum pluginView : pPluginViews)
    {
      pluginsViewImpl.add(new PluginViewImpl(pluginView));
    }
    return pluginsViewImpl;
  }

}
