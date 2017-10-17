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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.List;
import java.util.Locale;

/**
 * This component describes a specific {@link IndexedContainer} used to build projects combobox.
 * 
 * @author Jeremy Casery
 */
public class StatusContainer extends IndexedContainer
{

  /**
   * SerialUID
   */
  private static final long     serialVersionUID = -2561936415328606638L;

  private final static Resource puceGreen        = new ThemeResource(NovaForgeResources.PUCE_GREEN);
  private final static Resource puceYellow       = new ThemeResource(NovaForgeResources.PUCE_YELLOW);
  private final static Resource puceRed          = new ThemeResource(NovaForgeResources.PUCE_RED);
  private final static Resource puceOrange       = new ThemeResource(NovaForgeResources.PUCE_ORANGE);
  private final static Resource puceGrey         = new ThemeResource(NovaForgeResources.PUCE_GREY);
  private final static Resource puceBlue         = new ThemeResource(NovaForgeResources.PUCE_BLUE);

  /**
   * Default constructor. It will initialize Status item property
   * 
   * @see StatusItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public StatusContainer()
  {
    super();
    addContainerProperty(StatusItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(StatusItemProperty.LABEL.getPropertyId(), String.class, null);
    addContainerProperty(StatusItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
  }

  /**
   * Get the icon for the given status id
   *
   * @param pStatusID
   *          the id of the status
   * @return {@link Resource} the status icon
   */
  public static Resource getStatusIcon(final String pStatusID)
  {
    if (pStatusID.equals(PluginStatus.ACTIVATED.getLabel()))
    {
      return puceGreen;
    }
    else if (pStatusID.equals(PluginStatus.DEPRECATED.getLabel()))
    {
      return puceYellow;
    }
    else if (pStatusID.equals(PluginStatus.DESACTIVATED.getLabel()))
    {
      return puceRed;
    }
    else if (pStatusID.equals(PluginStatus.STOPPED.getLabel()))
    {
      return puceOrange;
    }
    else if (pStatusID.equals(PluginStatus.INSTALLED.getLabel()))
    {
      return puceBlue;
    }
    else
    {
      return puceGrey;
    }
  }

  /**
   * Add status into container
   *
   * @param pStatus
   *     status to add
   * @param pLocale
   *     user's locale
   */
  public void setStatus(final List<PluginStatus> pStatus, final Locale pLocale)
  {
    removeAllItems();
    for (final PluginStatus status : pStatus)
    {
      addItem(status.getLabel());
      getContainerProperty(status.getLabel(), StatusItemProperty.ID.getPropertyId()).setValue(status.getLabel());
      getContainerProperty(status.getLabel(), StatusItemProperty.LABEL.getPropertyId()).setValue(status
                                                                                                     .getLabel(pLocale));
      getContainerProperty(status.getLabel(), StatusItemProperty.DESCRIPTION.getPropertyId()).setValue(status
                                                                                                           .getDescription(pLocale));
    }
    sort(new Object[] { StatusItemProperty.LABEL.getPropertyId() }, new boolean[] { true });
  }
}
