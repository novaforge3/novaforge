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
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.core.plugins.domain.route.PluginQueues;

import java.util.List;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class PluginMetadataImpl implements PluginMetadata
{
  /**
	 * 
	 */
  private static final long      serialVersionUID = 6397648096456970487L;
  private final UUID             uuid;
  private final String           description;
  private final String           type;
  private final String           category;
  private final PluginQueues     queues;
  private final List<PluginView> pluginView;
  private final String           version;
  private PluginStatus status;
  private boolean      availability;

  public PluginMetadataImpl(final UUID pUuid, final String pDescription, final String pType,
      final String pCategory, final PluginQueues pPluginQueues, final List<PluginView> pPluginView,
                            final String pVersion)
  {
    this(pUuid, pDescription, pType, pCategory, pPluginQueues, pPluginView, PluginStatus.INSTALLED, false, pVersion);
  }

  public PluginMetadataImpl(final UUID pUuid, final String pDescription, final String pType, final String pCategory,
                            final PluginQueues pPluginQueues, final List<PluginView> pPluginView,
      final PluginStatus pStatus, final boolean pAvailability, final String pVersion)
  {
    uuid = pUuid;
    description = pDescription;
    type = pType;
    category = pCategory;
    queues = pPluginQueues;
    pluginView = pPluginView;
    status = pStatus;
    availability = pAvailability;
    version = pVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUUID()
  {
    return uuid.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getType()
  {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCategory()
  {
    return category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginQueues getJMSQueues()
  {
    return queues;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginView> getPluginViews()
  {
    return pluginView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion()
  {
    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginStatus getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final PluginStatus pStatus)
  {
    status = pStatus;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAvailable()
  {
    return availability;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAvailability(final boolean pAvailability)
  {
    availability = pAvailability;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "PluginMetadataImpl [uuid=" + uuid + ", description=" + description + ", type=" + type + ", category="
               + category + ", queues=" + queues + ", status=" + status + ", availability=" + availability
               + ", pluginView=" + pluginView + ", version=" + version + "]";
  }

}
