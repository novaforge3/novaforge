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
package org.novaforge.forge.ui.article.internal.client.admin.announcement.create;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleType;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.client.component.CategoryContainer;
import org.novaforge.forge.ui.article.internal.client.component.CategoryItemProperty;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminAnnouncementEvent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminAnnouncementCreatePresenter extends AbstractPortalPresenter
{

  /**
   * Content the workspace view
   */
  private final AdminAnnouncementCreateView view;
  /**
   * The working article object
   */
  private Article                           currentAnnouncement = null;
  /**
   * The categories container
   */
  private CategoryContainer                 categoryContainer   = new CategoryContainer();

  /**
   * @param pPortalContext
   */
  public AdminAnnouncementCreatePresenter(final AdminAnnouncementCreateView pView,
      PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
    // Init Category Field
    initCategoryList();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getBackToListButton().addClickListener(new Button.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 3195934706891499435L;

      @Override
      public void buttonClick(ClickEvent event)
      {

        getEventBus().publish(new OpenAdminAnnouncementEvent());
      }
    });
    view.getCreateButton().addClickListener(new Button.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -1889929204429343226L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        try
        {
          for (FieldGroup binder : view.getFormBinders())
          {
            binder.commit();
          }
          currentAnnouncement.setArticleCategory((ArticleCategory) view.getCategoryField().getValue());
          ArticleModule.getArticleService().updateArticle(currentAnnouncement);
          TrayNotification.show(
              ArticleModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.ARTICLEMANAGEMENT_NOTIFICATION_CREATE_TITLE), ArticleModule.getPortalMessages()
                  .getMessage(getCurrentLocale(), Messages.ARTICLEMANAGEMENT_NOTIFICATION_CREATE_DESC),
              TrayNotificationType.SUCCESS);
          getEventBus().publish(new OpenAdminAnnouncementEvent());
        }
        catch (final CommitException e)
        {
          TrayNotification.show(e.getCause().getMessage(), TrayNotificationType.WARNING);
        }
        catch (ArticleServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
              .getLocale());
        }

      }
    });

  }

  /**
   * Initialize the categories list
   */
  private void initCategoryList()
  {
    view.getCategoryField().setContainerDataSource(categoryContainer);
    view.getCategoryField().setItemCaptionPropertyId(CategoryItemProperty.TITLE.getPropertyId());
  }

  /**
   * Will refresh the article list
   */
  public void refresh(final Article pAnnouncement)
  {
    currentAnnouncement = pAnnouncement;
    if (currentAnnouncement == null)
    {
      view.setUpdateMode(false);
    }
    else
    {
      view.setUpdateMode(true);
    }
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Refresh the announcement list
   */
  private void refreshAnnouncementList()
  {
    try
    {
      if (currentAnnouncement == null)
      {
        currentAnnouncement = ArticleModule.getArticleService().newArticle(ArticleType.ANNOUCEMENT);
      }
    }
    catch (LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * Refresh the categories list
   */
  private void refreshCategoryList()
  {
    try
    {
      List<ArticleCategory> articleCategories = ArticleModule.getArticleCategoryService()
          .getAllArticleCategory();
      categoryContainer.setCategories(articleCategories);
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
    refreshAnnouncementList();
    refreshCategoryList();
    view.bindToArticle(currentAnnouncement);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(Locale locale)
  {
    view.refreshLocale(locale);
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
