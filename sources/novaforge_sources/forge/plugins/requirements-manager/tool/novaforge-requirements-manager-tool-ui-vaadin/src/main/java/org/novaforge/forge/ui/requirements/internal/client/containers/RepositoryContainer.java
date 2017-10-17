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

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;
import org.novaforge.forge.ui.requirements.internal.client.repository.components.RepositoryType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * @author Jeremy Casery
 */
public class RepositoryContainer extends IndexedContainer
{

  /**
   * Default serial UID
   */
  private static final long serialVersionUID = 1219345631389539485L;

  /**
   * Default constructor. It will initialize repository item property
   * 
   * @see RepositoryItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public RepositoryContainer()
  {
    super();
    addContainerProperty(RepositoryItemProperty.URI.getPropertyId(), String.class, null);
    addContainerProperty(RepositoryItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(RepositoryItemProperty.TYPE.getPropertyId(), RepositoryType.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add {@link IRepository} into container
   * 
   * @param pRepositories
   *          {@link IRepository} to add
   */
  @SuppressWarnings("unchecked")
  public void setRepositories(final Set<IRepository> pRepositories)
  {
    removeAllContainerFilters();
    removeAllItems();
    for (final IRepository repository : pRepositories)
    {
      final String itemID = repository.getURI();
      addItem(itemID);

      // For Obeo => Classic URI. For Excel => Only file name
      String uri = "";
      if (RepositoryType.EXCEL.getERepositoryType().equals(repository.getType()))
      {
        // Substrings only to get Excel file name
        final Path path = Paths.get(repository.getURI());
        uri = path.getFileName().toString();
      }
      else
      {
        uri = itemID;
      }
      getItem(itemID).getItemProperty(RepositoryItemProperty.URI.getPropertyId()).setValue(uri);

      getItem(itemID).getItemProperty(RepositoryItemProperty.DESCRIPTION.getPropertyId()).setValue(
          repository.getDescription());
      getItem(itemID).getItemProperty(RepositoryItemProperty.TYPE.getPropertyId()).setValue(
          RepositoryType.get(repository.getType()));
    }

    sort(new Object[] { RepositoryItemProperty.URI.getPropertyId() }, new boolean[] { true });
  }
}
