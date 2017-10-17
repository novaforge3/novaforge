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
package org.novaforge.forge.ui.article.internal.client.admin.category;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.client.admin.category.component.CategoryColumnActionsGenerator;
import org.novaforge.forge.ui.article.internal.client.admin.category.component.CategoryColumnTitleGenerator;
import org.novaforge.forge.ui.article.internal.client.component.CategoryContainer;
import org.novaforge.forge.ui.article.internal.client.component.CategoryItemProperty;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminCategoryCreateEvent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class AdminCategoryPresenter extends AbstractPortalPresenter
{
  private static final Log LOGGER = LogFactory.getLog(AdminCategoryPresenter.class);
  /**
   * Content the workspace view
   */
  private final AdminCategoryView view;
  private final CategoryContainer categoryContainer = new CategoryContainer();

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view attached to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public AdminCategoryPresenter(final AdminCategoryView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
    // Initialize Article list
    initCategoriesList();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getCreateCategoryButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 6810997301152685398L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        getEventBus().publish(new OpenAdminCategoryCreateEvent());
      }
    });
  }

  private void initCategoriesList()
  {

    view.getCategoryTable().setContainerDataSource(categoryContainer);
    view.getCategoryTable().addGeneratedColumn(CategoryItemProperty.TITLE.getPropertyId(),
                                               new CategoryColumnTitleGenerator());
    view.getCategoryTable().addGeneratedColumn(CategoryItemProperty.ACTIONS.getPropertyId(),
                                               new CategoryColumnActionsGenerator(this));
    view.getCategoryTable().setVisibleColumns(new String[] { CategoryItemProperty.TITLE.getPropertyId(),
                                                             CategoryItemProperty.ACTIONS.getPropertyId() });
    view.getCategoryTable().setColumnWidth(CategoryItemProperty.ACTIONS.getPropertyId(), 70);
  }

  public void editCategory(final UUID pCategoryID)
  {
    ArticleCategory category;
    try
    {
      category = ArticleModule.getArticleCategoryService().getArticleCategory(pCategoryID);
      getEventBus().publish(new OpenAdminCategoryCreateEvent(category));
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  public void deleteCategory(final ArticleCategory pCategory)
  {
    try
    {
      ArticleModule.getArticleCategoryService().deleteArticleCategory(pCategory.getUuid());
      categoryContainer.removeItem(pCategory);
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * Refresh the presenter and the view with the given project id
   */
  public void refresh()
  {
    // Initialize News list
    refreshContent();
    // Refresh Label
    refreshLocalized(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    try
    {
      List<ArticleCategory> categories = ArticleModule.getArticleCategoryService().getAllArticleCategory();
      categoryContainer.setCategories(categories);
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return ArticleModule.getModuleId();
  }

}
