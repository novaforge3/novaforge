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
package org.novaforge.forge.ui.article.internal.client.admin.announcement;

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
import org.novaforge.forge.ui.article.internal.client.admin.announcement.component.AnnouncementColumnActionsGenerator;
import org.novaforge.forge.ui.article.internal.client.admin.article.component.ArticleColumnCategoryGenerator;
import org.novaforge.forge.ui.article.internal.client.admin.article.component.ArticleColumnTitleGenerator;
import org.novaforge.forge.ui.article.internal.client.component.ArticleContainer;
import org.novaforge.forge.ui.article.internal.client.component.ArticleItemProperty;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminAnnouncementCreateEvent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class AdminAnnouncementPresenter extends AbstractPortalPresenter
{
  /**
   * Content the workspace view
   */
  private final AdminAnnouncementView view;
  /**
   * Announcement container
   */
  private final ArticleContainer      announcementsContainer = new ArticleContainer();
  /**
   * Announcement filter
   */
  private ArticleFilter               announcementsFilter;

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view attached to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public AdminAnnouncementPresenter(final AdminAnnouncementView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
    // Initialize projectList
    initAnnouncementList();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getCreateAnnouncementButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -1705523154471409275L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        getEventBus().publish(new OpenAdminAnnouncementCreateEvent());
      }
    });
    view.getFilterButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 3193828954495771513L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        refreshFilter();
      }
    });
  }

  /**
   * Initialize announcement list
   */
  private void initAnnouncementList()
  {
    view.getAnnouncementTable().setContainerDataSource(announcementsContainer);
    view.getAnnouncementTable().addGeneratedColumn(ArticleItemProperty.TITLE.getPropertyId(),
                                                   new ArticleColumnTitleGenerator());
    view.getAnnouncementTable().addGeneratedColumn(ArticleItemProperty.CATEGORY.getPropertyId(),
                                                   new ArticleColumnCategoryGenerator());
    view.getAnnouncementTable().addGeneratedColumn(ArticleItemProperty.ACTIONS.getPropertyId(),
                                                   new AnnouncementColumnActionsGenerator(this));
    view.getAnnouncementTable().setVisibleColumns(new String[] { ArticleItemProperty.DATE.getPropertyId(),
                                                                 ArticleItemProperty.TITLE.getPropertyId(),
                                                                 ArticleItemProperty.CATEGORY.getPropertyId(),
                                                                 ArticleItemProperty.PUBLISHED.getPropertyId(),
                                                                 ArticleItemProperty.ACTIONS.getPropertyId() });

    view.getAnnouncementTable().setColumnWidth(ArticleItemProperty.DATE.getPropertyId(), 135);
    view.getAnnouncementTable().setColumnWidth(ArticleItemProperty.PUBLISHED.getPropertyId(), 80);
    view.getAnnouncementTable().setColumnWidth(ArticleItemProperty.ACTIONS.getPropertyId(), 100);
  }

  /**
   * Refresh the announcement filter and announcement list with this filter
   */
  private void refreshFilter()
  {
    try
    {
      announcementsFilter = ArticleModule.getArticleService().newArticleFilter();
      announcementsFilter.setTextSearch(view.getFilterTextField().getValue());
      announcementsFilter.setArticleType(ArticleType.ANNOUCEMENT);
      refreshContent();
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }

  }

  /**
   * Publish or unpublish an announcement
   *
   * @param pAnnouncementUUID
   *          the announcement UUID
   * @param pIsPublished
   *          true to publish, false otherwise
   */
  public void setAnnouncementPublished(final UUID pAnnouncementUUID, final boolean pIsPublished)
  {
    try
    {
      Article articleToPublish = ArticleModule.getArticleService().getArticle(pAnnouncementUUID);
      articleToPublish.setPublished(pIsPublished);
      ArticleModule.getArticleService().updateArticle(articleToPublish);
      announcementsContainer.getItem(pAnnouncementUUID).getItemProperty(ArticleItemProperty.PUBLISHED.getPropertyId())
                            .setValue(pIsPublished);
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Edit an announcement
   *
   * @param pAnnouncementUUID
   *          the announcement UUID to edit
   */
  public void editAnnouncement(final UUID pAnnouncementUUID)
  {
    Article announcement;
    try
    {
      announcement = ArticleModule.getArticleService().getArticle(pAnnouncementUUID);
      getEventBus().publish(new OpenAdminAnnouncementCreateEvent(announcement));
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Delete an announcement
   *
   * @param pAnnouncementUUID
   *          the announcement UUID to delete
   */
  public void deleteAnnouncement(final UUID pAnnouncementUUID)
  {
    try
    {
      ArticleModule.getArticleService().deleteArticle(pAnnouncementUUID);
      announcementsContainer.removeItem(pAnnouncementUUID);
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Refresh the presenter and the view with the given project id
   *
   * @param pProjectId
   *          the project id to display
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
      if (announcementsFilter == null)
      {
        announcementsFilter = ArticleModule.getArticleService().newArticleFilter();
        announcementsFilter.setArticleType(ArticleType.ANNOUCEMENT);
      }
      List<Article> announcements = ArticleModule.getArticleService().getArticlesFromFilter(
          announcementsFilter);
      announcementsContainer.setArticle(announcements);
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    catch (LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
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
