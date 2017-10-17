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

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class StatusContainer extends FilterContainer
{

  /**
   * Default Serial UID
   */
  private static final long serialVersionUID = -5580498840453825418L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public StatusContainer()
  {
    super();
  }

  /**
   * Refresh container for CodeView
   * 
   * @param pLocale
   *          the {@link Locale} to use for label
   */
  public void refreshCodeView(final Locale pLocale)
  {
    removeAllContainerFilters();
    removeAllItems();
    addItem(RequirementStatus.CODED.getId());
    getItem(RequirementStatus.CODED.getId()).getItemProperty(FilterItemProperty.ID.getPropertyId()).setValue(
        RequirementStatus.CODED.getId());
    getItem(RequirementStatus.CODED.getId()).getItemProperty(FilterItemProperty.LABEL.getPropertyId())
        .setValue(
            RequirementsModule.getPortalMessages()
                .getMessage(pLocale, RequirementStatus.CODED.getMessageId()));
    getItem(RequirementStatus.CODED.getId()).getItemProperty(FilterItemProperty.ICON.getPropertyId())
        .setValue(new ThemeResource(RequirementStatus.CODED.getIcon()));

    addItem(RequirementStatus.NOT_CODED.getId());
    getItem(RequirementStatus.NOT_CODED.getId()).getItemProperty(FilterItemProperty.ID.getPropertyId())
        .setValue(RequirementStatus.NOT_CODED.getId());
    getItem(RequirementStatus.NOT_CODED.getId()).getItemProperty(FilterItemProperty.LABEL.getPropertyId())
        .setValue(
            RequirementsModule.getPortalMessages().getMessage(pLocale,
                RequirementStatus.NOT_CODED.getMessageId()));
    getItem(RequirementStatus.NOT_CODED.getId()).getItemProperty(FilterItemProperty.ICON.getPropertyId())
        .setValue(new ThemeResource(RequirementStatus.NOT_CODED.getIcon()));

    addItem(RequirementStatus.NOT_UPDATED.getId());
    getItem(RequirementStatus.NOT_UPDATED.getId()).getItemProperty(FilterItemProperty.ID.getPropertyId())
        .setValue(RequirementStatus.NOT_UPDATED.getId());
    getItem(RequirementStatus.NOT_UPDATED.getId()).getItemProperty(FilterItemProperty.LABEL.getPropertyId())
        .setValue(
            RequirementsModule.getPortalMessages().getMessage(pLocale,
                RequirementStatus.NOT_UPDATED.getMessageId()));
    getItem(RequirementStatus.NOT_UPDATED.getId()).getItemProperty(FilterItemProperty.ICON.getPropertyId())
        .setValue(new ThemeResource(RequirementStatus.NOT_UPDATED.getIcon()));
  }

  /**
   * Refresh container for TestView
   * 
   * @param pLocale
   *          The {@link Locale} to use for label
   */
  public void refreshTestView(final Locale pLocale)
  {
    removeAllContainerFilters();
    removeAllItems();

    addItem(RequirementStatus.TESTED.getId());
    getItem(RequirementStatus.TESTED.getId()).getItemProperty(FilterItemProperty.ID.getPropertyId())
        .setValue(RequirementStatus.TESTED.getId());
    getItem(RequirementStatus.TESTED.getId()).getItemProperty(FilterItemProperty.LABEL.getPropertyId())
        .setValue(
            RequirementsModule.getPortalMessages().getMessage(pLocale,
                RequirementStatus.TESTED.getMessageId()));
    getItem(RequirementStatus.TESTED.getId()).getItemProperty(FilterItemProperty.ICON.getPropertyId())
        .setValue(new ThemeResource(RequirementStatus.TESTED.getIcon()));

    addItem(RequirementStatus.NOT_TESTED.getId());
    getItem(RequirementStatus.NOT_TESTED.getId()).getItemProperty(FilterItemProperty.ID.getPropertyId())
        .setValue(RequirementStatus.NOT_TESTED.getId());
    getItem(RequirementStatus.NOT_TESTED.getId()).getItemProperty(FilterItemProperty.LABEL.getPropertyId())
        .setValue(
            RequirementsModule.getPortalMessages().getMessage(pLocale,
                RequirementStatus.NOT_TESTED.getMessageId()));
    getItem(RequirementStatus.NOT_TESTED.getId()).getItemProperty(FilterItemProperty.ICON.getPropertyId())
        .setValue(new ThemeResource(RequirementStatus.NOT_TESTED.getIcon()));

    addItem(RequirementStatus.NOT_UPDATED.getId());
    getItem(RequirementStatus.NOT_UPDATED.getId()).getItemProperty(FilterItemProperty.ID.getPropertyId())
        .setValue(RequirementStatus.NOT_UPDATED.getId());
    getItem(RequirementStatus.NOT_UPDATED.getId()).getItemProperty(FilterItemProperty.LABEL.getPropertyId())
        .setValue(
            RequirementsModule.getPortalMessages().getMessage(pLocale,
                RequirementStatus.NOT_UPDATED.getMessageId()));
    getItem(RequirementStatus.NOT_UPDATED.getId()).getItemProperty(FilterItemProperty.ICON.getPropertyId())
        .setValue(new ThemeResource(RequirementStatus.NOT_UPDATED.getIcon()));
  }

}
