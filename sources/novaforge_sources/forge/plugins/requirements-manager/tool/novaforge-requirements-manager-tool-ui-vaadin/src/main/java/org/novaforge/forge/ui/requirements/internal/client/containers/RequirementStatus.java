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
package org.novaforge.forge.ui.requirements.internal.client.containers;

import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

/**
 * @author Jeremy Casery
 */
public enum RequirementStatus
{
  CODED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
      return "coded";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageId()
    {
      return "requirement.status.coded";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon()
    {
      return NovaForgeResources.PUCE_GREEN;
    }
  },
  NOT_CODED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
      return "notcoded";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageId()
    {
      return "requirement.status.notcoded";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon()
    {
      return NovaForgeResources.PUCE_RED;
    }
  },
  NOT_UPDATED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
      return "notupdated";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageId()
    {
      return "requirement.status.notupdated";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon()
    {
      return NovaForgeResources.PUCE_YELLOW;
    }
  },
  TESTED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
      return "tested";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageId()
    {
      return "requirement.status.tested";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon()
    {
      return NovaForgeResources.PUCE_GREEN;
    }
  },
  NOT_TESTED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
      return "nottested";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageId()
    {
      return "requirement.status.nottested";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon()
    {
      return NovaForgeResources.PUCE_RED;
    }
  };

  /**
   * Get the status ID
   * 
   * @return the code id
   */
  public abstract String getId();

  /**
   * Get the status message id for internationalization
   * 
   * @return
   */
  public abstract String getMessageId();

  /**
   * Get the status icon
   * 
   * @return the status icon
   */
  public abstract String getIcon();
}