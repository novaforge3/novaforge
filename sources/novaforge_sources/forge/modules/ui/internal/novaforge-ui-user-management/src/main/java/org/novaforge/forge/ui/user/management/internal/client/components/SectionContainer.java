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
package org.novaforge.forge.ui.user.management.internal.client.components;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.util.List;
import java.util.Locale;

/**
 * @author caseryj
 */
public class SectionContainer extends IndexedContainer
{

  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = 6626275167049885686L;

  /**
   * Default constructor. It will initialize section item property
   * 
   * @see SectionItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public SectionContainer()
  {
    super();
    addContainerProperty(SectionItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(SectionItemProperty.NAME.getPropertyId(), String.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add section into container
   * 
   * @param pSections
   *          Sections to add
   * @param pLocale
   *          The locale to use for section label
   */
  public void setSection(final List<SectionType> pSections, final Locale pLocale)
  {
    removeAllItems();
    for (final SectionType section : pSections)
    {
      final String itemID = section.getId();
      addItem(itemID);
      getContainerProperty(itemID, SectionItemProperty.ID.getPropertyId()).setValue(itemID);
      getContainerProperty(itemID, SectionItemProperty.NAME.getPropertyId()).setValue(
          AdminModule.getPortalMessages().getMessage(pLocale, section.getNameKey()));
    }

    sort(new Object[] { SectionItemProperty.ID.getPropertyId() }, new boolean[] { true });
  }

}
