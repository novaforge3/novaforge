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
package org.novaforge.forge.plugins.commons.services.domains;

import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;

/**
 * Default implementation of PluginServiceMetadata.
 * 
 * @see org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata
 * @author lamirang
 */
public class PluginViewImpl implements PluginView
{

  /**
	 * 
	 */
  private static final long    serialVersionUID = -8199118076308384410L;
  private final PluginViewEnum viewId;
  private final String         name;
  private final String         uri;

  /**
   * Constructor using a {@link PluginViewEnum} object
   * 
   * @param pPluginView
   *          the object to use
   */
  public PluginViewImpl(final PluginViewEnum pPluginView)
  {

    viewId = pPluginView;
    name = "";
    uri = "";
  }

  public PluginViewImpl(final PluginViewEnum pViewId, final String pURI)
  {
    super();
    viewId = pViewId;
    uri = pURI;
    name = "";
  }

  public PluginViewImpl(final PluginViewEnum pViewId, final String pName, final String pURI)
  {
    super();
    viewId = pViewId;
    name = pName;
    uri = pURI;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginViewEnum getViewId()
  {
    return viewId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getURI()
  {
    return uri;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

}
