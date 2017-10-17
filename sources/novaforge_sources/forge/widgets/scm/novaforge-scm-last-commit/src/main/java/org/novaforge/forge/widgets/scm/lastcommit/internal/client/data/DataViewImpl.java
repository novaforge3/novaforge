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
package org.novaforge.forge.widgets.scm.lastcommit.internal.client.data;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container.CommitItemProperty;
import org.novaforge.forge.widgets.scm.lastcommit.internal.module.LastCommitModule;
import org.vaadin.addon.itemlayout.horizontal.ItemHorizontal;

import java.util.Locale;

/**
 * This view define to display data view
 * 
 * @author Guillaume Lamirand
 */
public class DataViewImpl extends VerticalLayout implements DataView
{

  /**
   * Serial version id used for serialization
   */
  private static final long serialVersionUID = 5978554504908531185L;
  /**
   * {@link ItemHorizontal} which contains instance filters
   */
  private ItemHorizontal    header;
  /**
   * The {@link Table} containing the scm commit
   */
  private Table             commitsTable;

  /**
   * Default constructor
   */
  public DataViewImpl()
  {
    super();
    setSpacing(true);

    // Init vertical content
    final Component header = initHeaders();
    final Component content = initContent();

    addComponent(header);
    setComponentAlignment(header, Alignment.MIDDLE_CENTER);
    addComponent(content);

  }

  private Component initHeaders()
  {
    header = new ItemHorizontal();
    header.setPrevIcon(new ThemeResource(NovaForgeResources.ITEM_LAYOUT_PREV));
    header.setNextIcon(new ThemeResource(NovaForgeResources.ITEM_LAYOUT_NEXT));
    return header;
  }

  private Component initContent()
  {
    commitsTable = new Table();
    commitsTable.setSelectable(false);
    commitsTable.setPageLength(5);
    commitsTable.setWidth(100, Unit.PERCENTAGE);
    commitsTable.setStyleName(Reindeer.TABLE_BORDERLESS);
    return commitsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    commitsTable.setColumnHeader(CommitItemProperty.REVISION.getPropertyId(), LastCommitModule
        .getPortalMessages().getMessage(pLocale, Messages.SCMLASTCOMMIT_TABLE_COMMITS_REVISION));

    commitsTable.setColumnHeader(CommitItemProperty.DATE.getPropertyId(), LastCommitModule
        .getPortalMessages().getMessage(pLocale, Messages.SCMLASTCOMMIT_TABLE_COMMITS_DATE));

    commitsTable.setColumnHeader(CommitItemProperty.CHANGES.getPropertyId(), LastCommitModule
        .getPortalMessages().getMessage(pLocale, Messages.SCMLASTCOMMIT_TABLE_COMMITS_CHANGES));

    commitsTable.setColumnHeader(CommitItemProperty.AUTHOR_PROFILE.getPropertyId(), LastCommitModule
        .getPortalMessages().getMessage(pLocale, Messages.SCMLASTCOMMIT_TABLE_COMMITS_AUTHOR));

    commitsTable.setColumnHeader(CommitItemProperty.COMMENT.getPropertyId(), LastCommitModule
        .getPortalMessages().getMessage(pLocale, Messages.SCMLASTCOMMIT_TABLE_COMMITS_COMMENT));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getCommitsTable()
  {
    return commitsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearInstances()
  {
    header.removeAllComponents();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemHorizontal getRepositoryLayout()
  {
    return header;
  }

}
