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
package org.novaforge.forge.ui.article.internal.client.article;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.article.model.ArticleType;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.client.component.ArticleContainer;
import org.novaforge.forge.ui.article.internal.client.component.NewsItemGenerator;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenArticleDetailsEvent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.vaadin.addon.itemlayout.event.ItemClickEvent;
import org.vaadin.addon.itemlayout.event.ItemClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class ArticlePresenter extends AbstractPortalPresenter
{
  /**
   * Content the workspace view
   */
  private final ArticleView      view;
  /**
   * The current working Article Container
   */
  private final ArticleContainer news                 = new ArticleContainer();
  /**
   * The filter categories UUID to show
   */
  private final Set<UUID>        categoriesUuidToShow = new HashSet<UUID>();
  /**
   * The News Filter
   */
  private ArticleFilter newsFilter = null;

  /**
   * Default constructor.
   *
   * @param pView
   *          the view attached to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public ArticlePresenter(final ArticleView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
    // Initialize projectList
    initNewsList();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getAdminButton().addClickListener(new ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -5368545291250662272L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        getEventBus().publish(new OpenAdminEvent());

      }
    });
    view.getNewsGrid().addItemClickListener(new ItemClickListener()
    {

      @Override
      public void onItemClick(ItemClickEvent pEvent)
      {
        getEventBus().publish(new OpenArticleDetailsEvent(UUID.fromString(pEvent.getItemId())));
      }
    });
    view.getFilterButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 1948923280824781356L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        refreshNewsFilter();
      }
    });
    view.getFilterResetButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -2786232710052035501L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        newsFilter = null;
        view.getFilterDateFrom().setValue(null);
        view.getFilterDateTo().setValue(null);
        view.getFilterText().setValue("");

        refreshNews();
        refreshCategories();
      }
    });

  }

  /**
   * Initialize the News List
   */
  private void initNewsList()
  {
    view.getNewsGrid().setItemGenerator(new NewsItemGenerator());
    view.getNewsGrid().setContainerDataSource(news);
  }

  /**
   * Refresh the News Filter
   */
  private void refreshNewsFilter()
  {
    try
    {
      newsFilter = ArticleModule.getArticleService().newArticleFilter();
      newsFilter.setTextSearch(view.getFilterText().getValue());
      newsFilter.setArticleCategoryUuids(new ArrayList<UUID>(categoriesUuidToShow));
      newsFilter.setArticleType(ArticleType.NEWS);
      if (view.getFilterDateFrom().getValue() != null)
      {
        newsFilter.setBeginDate(view.getFilterDateFrom().getValue());
      }
      if (view.getFilterDateTo().getValue() != null)
      {
        newsFilter.setEndDate(view.getFilterDateTo().getValue());
      }
      newsFilter.setIsPublished(true);
      refreshNews();
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }

  }

  /**
   * Refresh The news list
   */
  private void refreshNews()
  {
    try
    {
      if (newsFilter == null)
      {
        newsFilter = ArticleModule.getArticleService().newArticleFilter();
        newsFilter.setArticleType(ArticleType.NEWS);
        newsFilter.setIsPublished(true);
      }
      news.setArticle(ArticleModule.getArticleService().getArticlesFromFilter(newsFilter));
    }
    catch (LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }
  }

  /**
   * Refresh the categories
   */
  private void refreshCategories()
  {
    view.getFilterCategoryLayout().removeAllComponents();
    try
    {
      List<ArticleCategory> categories = ArticleModule.getArticleCategoryService().getAllArticleCategory();
      for (final ArticleCategory category : categories)
      {
        ArticleCategoryContent categoryContent = category.getCategoryContent(getCurrentLocale());
        if (categoryContent == null || categoryContent.getTitle() == null)
        {
          categoryContent = category.getCategoryContent(ArticleModule.getDefaultLocale());
        }
        final CheckBox categoryField = new CheckBox(categoryContent.getTitle(), true);
        categoryField.addValueChangeListener(new ValueChangeListener()
        {

          /**
           *
           */
          private static final long serialVersionUID = 2898250890379388034L;

          @Override
          public void valueChange(ValueChangeEvent event)
          {
            if (categoryField.getValue())
            {
              categoriesUuidToShow.add(category.getUuid());
            }
            else
            {
              categoriesUuidToShow.remove(category.getUuid());
            }

          }
        });
        view.getFilterCategoryLayout().addComponent(categoryField);
        categoriesUuidToShow.add(category.getUuid());
      }
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
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
   * Refresh the Inforamtion
   *
   * @throws ArticleServiceException
   */
  private void refreshInformations() throws ArticleServiceException
  {

    try
    {
      /*
    The current working Information
   */
      final Article informations = ArticleModule.getArticleService().getInformation();
      ArticleContent informationContent = informations.getArticleContent(getCurrentLocale());
      if (informationContent == null || informationContent.getTitle() == null)
      {
        informationContent = informations.getArticleContent(ArticleModule.getDefaultLocale());
      }
      view.getInformationTitle().setValue(informationContent.getTitle());
      view.getInformationText().setValue(informationContent.getShortText());
    }
    catch (ArticleCategoryServiceException | LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }

  }

  /**
   * Refresh the Announcement
   *
   * @throws ArticleServiceException
   */
  private void refreshAnnouncement() throws ArticleServiceException
  {
    view.getAnnouncementCarousel().removeAllComponents();
    try
    {
      ArticleFilter articleFilter = ArticleModule.getArticleService().newArticleFilter();
      articleFilter.setArticleType(ArticleType.ANNOUCEMENT);
      articleFilter.setIsPublished(true);
      List<Article> articlesFromFilter = ArticleModule.getArticleService().getArticlesFromFilter(articleFilter);

      for (final Article announcement : articlesFromFilter)
      {
        ArticleContent announcementContent = announcement.getArticleContent(getCurrentLocale());
        if (announcementContent == null || announcementContent.getTitle() == null)
        {
          announcementContent = announcement.getArticleContent(ArticleModule.getDefaultLocale());
        }

        CssLayout announcemenCssLayout;
        if (announcement.getArticleCategory() == null)
        {
          announcemenCssLayout = new CssLayout();
          announcemenCssLayout.setStyleName(NovaForge.ARTICLE_CATEGORY_DEFAULT_COLOR);
        }
        else
        {
          final Color categoryColor = new Color(Integer.valueOf(announcement.getArticleCategory().getColor()));
          final String cssColor = categoryColor.getCSS();

          announcemenCssLayout = new CssLayout()
          {
            @Override
            protected String getCss(Component c)
            {
              if (c instanceof HorizontalLayout)
              {
                return "background: " + cssColor + ";";
              }
              return null;
            }
          };
        }

        HorizontalLayout announcementLayout = new HorizontalLayout();
        announcementLayout.setSizeFull();
        Label announcementTitle = new Label(announcementContent.getTitle());
        announcementTitle.setSizeUndefined();
        announcementTitle.setStyleName(NovaForge.LABEL_H1);

        announcementLayout.addComponent(announcementTitle);
        announcementLayout.setComponentAlignment(announcementTitle, Alignment.MIDDLE_CENTER);
        announcementLayout.setStyleName(NovaForge.CURSOR_BUTTON);
        announcemenCssLayout.addComponent(announcementLayout);

        announcementLayout.addLayoutClickListener(new LayoutClickListener()
        {

          /**
         * 
         */
          private static final long serialVersionUID = 2453462355086059239L;

          @Override
          public void layoutClick(LayoutClickEvent event)
          {
            getEventBus().publish(new OpenArticleDetailsEvent(announcement.getUuid()));
          }
        });
        announcemenCssLayout.setSizeFull();
        view.getAnnouncementCarousel().addComponent(announcemenCssLayout);
      }
      if (articlesFromFilter.size() < 2)
      {
        view.getAnnouncementCarousel().setButtonsVisible(false);
      }
      else
      {
        view.getAnnouncementCarousel().setButtonsVisible(true);
      }
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }
    catch (LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }

  }

  /**
   * Check if user has SuperAdmin rights
   */
  private void checkUserRights()
  {
    view.getAdminButton().setVisible(ArticleModule.getMembershipPresenter().isCurrentSuperAdmin());
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
      refreshInformations();
      refreshAnnouncement();
      refreshNews();
      refreshCategories();
      checkUserRights();
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
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
