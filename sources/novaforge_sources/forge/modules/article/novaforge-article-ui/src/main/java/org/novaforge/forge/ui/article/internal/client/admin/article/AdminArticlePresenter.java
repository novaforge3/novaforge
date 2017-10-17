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
package org.novaforge.forge.ui.article.internal.client.admin.article;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.article.model.ArticleType;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.client.admin.article.component.ArticleColumnActionsGenerator;
import org.novaforge.forge.ui.article.internal.client.admin.article.component.ArticleColumnCategoryGenerator;
import org.novaforge.forge.ui.article.internal.client.admin.article.component.ArticleColumnTitleGenerator;
import org.novaforge.forge.ui.article.internal.client.component.ArticleContainer;
import org.novaforge.forge.ui.article.internal.client.component.ArticleItemProperty;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminArticleCreateEvent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class AdminArticlePresenter extends AbstractPortalPresenter
{
  /**
   * Content the workspace view
   */
  private final AdminArticleView view;
  /**
   * The articles container
   */
  private final ArticleContainer articlesContainer = new ArticleContainer();
  /**
   * The article filter
   */
  private ArticleFilter          articlesFilter;

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view attached to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public AdminArticlePresenter(final AdminArticleView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
    // Initialize Article list
    initArticleList();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getCreateArticleButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -1002337868481832114L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        getEventBus().publish(new OpenAdminArticleCreateEvent());
      }
    });
    view.getFilterButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 691111209468856200L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        refreshFilter();

      }
    });
  }

  /**
   * Initialize the article list
   */
  private void initArticleList()
  {

    view.getArticleTable().setContainerDataSource(articlesContainer);
    view.getArticleTable().addGeneratedColumn(ArticleItemProperty.TITLE.getPropertyId(),
                                              new ArticleColumnTitleGenerator());
    view.getArticleTable().addGeneratedColumn(ArticleItemProperty.CATEGORY.getPropertyId(),
                                              new ArticleColumnCategoryGenerator());
    view.getArticleTable().addGeneratedColumn(ArticleItemProperty.ACTIONS.getPropertyId(),
                                              new ArticleColumnActionsGenerator(this));
    view.getArticleTable().setVisibleColumns(new String[] { ArticleItemProperty.DATE.getPropertyId(),
                                                            ArticleItemProperty.TITLE.getPropertyId(),
                                                            ArticleItemProperty.CATEGORY.getPropertyId(),
                                                            ArticleItemProperty.PUBLISHED.getPropertyId(),
                                                            ArticleItemProperty.ACTIONS.getPropertyId() });

    view.getArticleTable().setColumnWidth(ArticleItemProperty.DATE.getPropertyId(), 135);
    view.getArticleTable().setColumnWidth(ArticleItemProperty.PUBLISHED.getPropertyId(), 80);
    view.getArticleTable().setColumnWidth(ArticleItemProperty.ACTIONS.getPropertyId(), 100);
  }

  /**
   * Refresh the article filter
   */
  private void refreshFilter()
  {
    try
    {
      articlesFilter = ArticleModule.getArticleService().newArticleFilter();
      articlesFilter.setTextSearch(view.getFilterTextField().getValue());
      articlesFilter.setArticleType(ArticleType.NEWS);
      refreshContent();
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }

  }

  /**
   * Publish or unpublish an article
   *
   * @param pArticleUUID
   *          the article UUID to publish or unpublish
   * @param pIsPublished
   *          true to publish, false otherwise
   */
  public void setArticlePublished(final UUID pArticleUUID, final boolean pIsPublished)
  {
    try
    {
      Article articleToPublish = ArticleModule.getArticleService().getArticle(pArticleUUID);
      articleToPublish.setPublished(pIsPublished);
      ArticleModule.getArticleService().updateArticle(articleToPublish);
      articlesContainer.getItem(pArticleUUID).getItemProperty(ArticleItemProperty.PUBLISHED.getPropertyId())
                       .setValue(pIsPublished);
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Edit an article
   *
   * @param pArticleUUID
   *          the article UUID to edit
   */
  public void editArticle(final UUID pArticleUUID)
  {
    Article article;
    try
    {
      article = ArticleModule.getArticleService().getArticle(pArticleUUID);
      getEventBus().publish(new OpenAdminArticleCreateEvent(article));
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Delete an article
   *
   * @param pArticleUUID
   *          the article UUID to delete
   */
  public void deleteArticle(final UUID pArticleUUID)
  {
    try
    {
      ArticleModule.getArticleService().deleteArticle(pArticleUUID);
      articlesContainer.removeItem(pArticleUUID);
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent().getLocale());
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
      if (articlesFilter == null)
      {
        articlesFilter = ArticleModule.getArticleService().newArticleFilter();
        articlesFilter.setArticleType(ArticleType.NEWS);
      }
      List<Article> articles = ArticleModule.getArticleService().getArticlesFromFilter(articlesFilter);
      articlesContainer.setArticle(articles);
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    catch (LanguageServiceException e)
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
