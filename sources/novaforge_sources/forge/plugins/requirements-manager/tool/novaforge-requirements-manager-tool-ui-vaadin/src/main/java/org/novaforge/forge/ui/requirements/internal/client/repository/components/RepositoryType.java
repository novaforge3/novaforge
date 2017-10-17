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
package org.novaforge.forge.ui.requirements.internal.client.repository.components;

import com.vaadin.server.Resource;
import org.novaforge.forge.tools.requirements.common.model.ERepositoryType;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

/**
 * @author Jeremy Casery
 */
public enum RepositoryType
{
  EXCEL
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public ERepositoryType getERepositoryType()
    {
      return ERepositoryType.EXCEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLogo()
    {
      return NovaForgeResources.LOGO_EXCEL;
    }
  },
  OBEO
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public ERepositoryType getERepositoryType()
    {
      return ERepositoryType.OBEO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLogo()
    {
      return NovaForgeResources.LOGO_OBEO;
    }
  };

  /**
   * Returns a {@link RepositoryType} from a {@link ERepositoryType}
   *
   * @param pType
   * @return
   */
  public static RepositoryType get(final ERepositoryType pType)
  {
    RepositoryType found = null;
    for (final RepositoryType repositoryType : values())
    {
      if (repositoryType.getERepositoryType().equals(pType))
      {
        found = repositoryType;
      }
    }
    return found;
  }

  /**
   * Get the type ERepository type
   *
   * @return the type id
   */
  public abstract ERepositoryType getERepositoryType();

  /**
   * Get the type logo {@link Resource}
   *
   * @return the type logo
   */
  public abstract String getLogo();
}
