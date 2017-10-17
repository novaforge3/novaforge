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
package org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.component;

import com.vaadin.data.Item;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.DataPresenter;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container.CommitItemProperty;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;

/**
 * @author Guillaume Lamirand
 */
public class RepositoryItemGenerator implements ItemGenerator
{

  /**
   * Serial version id
   */
  private static final long   serialVersionUID = -6658902338261292529L;
  private final DataPresenter dataPresenter;

  /**
   * @param pDataPresenter
   */
  public RepositoryItemGenerator(final DataPresenter pDataPresenter)
  {
    dataPresenter = pDataPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(final AbstractItemLayout pSource, final Object pItemId)
  {
    final Item item = pSource.getContainerDataSource().getItem(pItemId);
    final String name = (String) item.getItemProperty(CommitItemProperty.REPOSITORY.getPropertyId())
        .getValue();
    final Button button = new Button(name);
    button.setStyleName(NovaForge.BUTTON_LINK);
    final StreamResource icon = (StreamResource) item
        .getItemProperty(CommitItemProperty.ICON.getPropertyId()).getValue();
    button.setIcon(icon);
    button.addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 8687565936535994866L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        dataPresenter.onRepositoryClick(name, pEvent.getButton());

      }
    });
    return button;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeGenerated(final AbstractItemLayout pSource, final Object pItemId,
      final Object pPropertyChanged)
  {
    boolean isNeeded = false;

    if ((CommitItemProperty.REPOSITORY.getPropertyId().equals(pPropertyChanged))
        || (CommitItemProperty.ICON.getPropertyId().equals(pPropertyChanged)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object itemRepo = item.getItemProperty(CommitItemProperty.REPOSITORY.getPropertyId()).getValue();
      final Object itemIcon = item.getItemProperty(CommitItemProperty.ICON.getPropertyId()).getValue();

      isNeeded = (itemRepo != null) && (itemIcon != null);
    }
    return isNeeded;
  }

}
