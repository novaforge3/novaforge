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
package org.novaforge.forge.ui.article.internal.client.admin.category.component;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.client.admin.category.AdminCategoryPresenter;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class CategoryColumnActionsGenerator implements ColumnGenerator
{

  /**
   * 
   */
  private static final long      serialVersionUID = -429111388594042957L;
  private AdminCategoryPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the admin article table
   */
  public CategoryColumnActionsGenerator(final AdminCategoryPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(Table source, Object itemId, Object columnId)
  {

    final ArticleCategory category = (ArticleCategory) itemId;
    // Get Locale
    final Locale locale = UI.getCurrent().getLocale();
    // ACTION edit profile
    final Embedded editCateoryIcon = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_CATEGORY_EDIT));
    editCateoryIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    editCateoryIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    editCateoryIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    editCateoryIcon.setDescription(ArticleModule.getPortalMessages().getMessage(locale,
        Messages.ARTICLEMANAGEMENT_ACTION_EDIT));
    editCateoryIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 6364752716123774576L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.editCategory(category.getUuid());
      }
    });

    // ACTION delete user
    final Embedded deleteCategoryIcon = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_CATEGORY_TRASH));
    deleteCategoryIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    deleteCategoryIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    deleteCategoryIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    deleteCategoryIcon.setDescription(ArticleModule.getPortalMessages().getMessage(locale,
        Messages.ARTICLEMANAGEMENT_ACTION_DELETE));
    deleteCategoryIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -1396007275682976184L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.deleteCategory(category);
      }
    });
    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.addComponent(editCateoryIcon);
    actionsLayout.addComponent(deleteCategoryIcon);
    actionsLayout.setComponentAlignment(editCateoryIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(deleteCategoryIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();

    return actionsLayout;

  }
}
