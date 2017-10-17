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
package org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.StreamResource;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;

/**
 * @author Guillaume Lamirand
 */
public class RepositoriesContainer extends IndexedContainer
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 7245265409387407802L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public RepositoriesContainer()
  {
    super();
    addContainerProperty(CommitItemProperty.ICON.getPropertyId(), StreamResource.class, null);
    addContainerProperty(CommitItemProperty.REPOSITORY.getPropertyId(), String.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add repository into container
   * 
   * @param pRepositoryName
   *          the repository name
   * @param pIcon
   *          the repository icon
   */
  public void addRepository(final String pRepositoryName, final StreamResource pIcon)
  {
    addItem(pRepositoryName);
    getContainerProperty(pRepositoryName, CommitItemProperty.REPOSITORY.getPropertyId()).setValue(
        pRepositoryName);
    getContainerProperty(pRepositoryName, CommitItemProperty.ICON.getPropertyId()).setValue(pIcon);
  }

}
