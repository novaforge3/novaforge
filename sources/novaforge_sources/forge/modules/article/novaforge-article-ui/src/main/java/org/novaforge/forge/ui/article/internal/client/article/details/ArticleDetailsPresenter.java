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
package org.novaforge.forge.ui.article.internal.client.article.details;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.client.event.OpenArticleEvent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.text.DateFormat;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class ArticleDetailsPresenter extends AbstractPortalPresenter
{
  /**
   * Content the workspace view
   */
  private final ArticleDetailsView view;
  /**
   * Current working Article UUID
   */
  private UUID                     articleUUID = null;

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view attached to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public ArticleDetailsPresenter(final ArticleDetailsView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getBackToNews().addClickListener(new Button.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 2627503630991859467L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        getEventBus().publish(new OpenArticleEvent());
      }
    });
  }

  /**
   * Set the Article UUID
   *
   * @param pArticleUUID
   *          to set
   */
  public void setArticleUUID(final UUID pArticleUUID)
  {
    this.articleUUID = pArticleUUID;
    refresh();
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
    if (articleUUID != null)
    {
      try
      {
        Article currentArticle = ArticleModule.getArticleService().getArticle(articleUUID);
        ArticleContent currentArticleContent = currentArticle.getArticleContent(getCurrentLocale());
        if (currentArticleContent == null || currentArticleContent.getTitle() == null)
        {
          currentArticleContent = currentArticle.getArticleContent(ArticleModule.getDefaultLocale());
        }
        view.getNewsTitle().setValue(currentArticleContent.getTitle());
        view.getNewsShortText().setValue(currentArticleContent.getShortText());
        view.getNewsText().setValue(currentArticleContent.getText());
        DateFormat dateFormat = DateFormat.getDateInstance();
        view.getNewsDate().setValue(dateFormat.format(currentArticle.getDate()));
        view.setCategory(currentArticle.getArticleCategory());
      }
      catch (ArticleServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
      }
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
