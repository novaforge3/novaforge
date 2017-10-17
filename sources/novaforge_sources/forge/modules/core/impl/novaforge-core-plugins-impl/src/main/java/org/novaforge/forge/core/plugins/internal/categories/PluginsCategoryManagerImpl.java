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
package org.novaforge.forge.core.plugins.internal.categories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.internal.PluginsManagerImpl;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lamirang
 */
public class PluginsCategoryManagerImpl implements PluginsCategoryManager
{
  private static final Log                      log                        = LogFactory
                                                                               .getLog(PluginsManagerImpl.class);
  private final List<CategoryDefinitionService> categoryDefinitionServices = new ArrayList<CategoryDefinitionService>();

  public void addService(final CategoryDefinitionService pService)
  {
    if (pService != null)
    {
      final boolean isExisting = isExistingInLowerCase(pService.getCategory().getId());
      if (!isExisting)
      {
        categoryDefinitionServices.add(pService);
        log.debug(String.format("Adding a new category definition with [id=%s]", pService.getCategory()));
      }
      else
      {
        log.warn(String.format(
            "Unable to adding a new category definition with [id=%s] because the name is already existing.",
            pService.getCategory()));

      }
    }
  }

  /**
   * Check if id is already existing in lower case in the registry
   *
   * @param pService
   * @return true if existing
   */
  private boolean isExistingInLowerCase(final String pId)
  {
    boolean isExisting = false;
    if (pId != null)
    {
      for (final CategoryDefinitionService definition : categoryDefinitionServices)
      {
        if (definition.getCategory().getId().toLowerCase().equals(pId.toLowerCase()))
        {
          isExisting = true;
          break;
        }
      }
    }
    return isExisting;
  }

  public void removeService(final CategoryDefinitionService pService)
  {
    if (pService != null)
    {
      final boolean isExisting = isExistingInLowerCase(pService.getCategory().getId());
      if (isExisting)
      {
        log.debug(String.format("Removing a category definition with [name=%s]", pService.getCategory()));
        categoryDefinitionServices.remove(pService);
      }
      else
      {
        log.warn(String.format("Unable to remove a category definition with [id=%s] because it doesn't exist.",
                               pService.getCategory()));

      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CategoryDefinitionService getCategoryService(final String pId)
  {
    CategoryDefinitionService returnService = null;
    for (final CategoryDefinitionService service : categoryDefinitionServices)
    {
      if (service.getCategory().getId().toLowerCase().equals(pId.toLowerCase()))
      {
        returnService = service;
        break;
      }
    }
    return returnService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CategoryDefinitionService> getAllCategoryServices()
  {
    return categoryDefinitionServices;
  }

}
