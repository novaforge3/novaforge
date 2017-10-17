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
package org.novaforge.forge.ui.article.internal.client.admin.announcement.component;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.client.admin.announcement.AdminAnnouncementPresenter;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class AnnouncementColumnActionsGenerator implements ColumnGenerator
{

  /**
   * Default serial UID
   */
  private static final long          serialVersionUID = -4615135094663394273L;
  /**
   * The admin announcement presenter
   */
  private AdminAnnouncementPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the admin announcement table
   */
  public AnnouncementColumnActionsGenerator(final AdminAnnouncementPresenter pPresenter)
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
    final UUID articleID = (UUID) itemId;
    // Get Locale
    final Locale locale = UI.getCurrent().getLocale();
    final Embedded publishArticleIcon = new Embedded();
    Article article = null;
    try
    {
      article = ArticleModule.getArticleService().getArticle(articleID);
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, locale);
    }
    final boolean isPublished = article.isPublished();
    if (isPublished)
    {
      publishArticleIcon.setSource(new ThemeResource(NovaForgeResources.ICON_ANNOUNCEMENT_UNPUBLISH));
      publishArticleIcon.setDescription(ArticleModule.getPortalMessages().getMessage(locale,
          Messages.ARTICLEMANAGEMENT_ACTION_UNPUBLISH));
    }
    else
    {
      publishArticleIcon.setSource(new ThemeResource(NovaForgeResources.ICON_ANNOUNCEMENT_PUBLISH));
      publishArticleIcon.setDescription(ArticleModule.getPortalMessages().getMessage(locale,
          Messages.ARTICLEMANAGEMENT_ACTION_PUBLISH));

    }
    publishArticleIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    publishArticleIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    publishArticleIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    publishArticleIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 7791472749358652605L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.setAnnouncementPublished(articleID, !isPublished);
      }
    });
    // ACTION edit profile
    final Embedded editArticleIcon = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_ANNOUNCEMENT_EDIT));
    editArticleIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    editArticleIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    editArticleIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    editArticleIcon.setDescription(ArticleModule.getPortalMessages().getMessage(locale,
        Messages.ARTICLEMANAGEMENT_ACTION_EDIT));
    editArticleIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -206447774201747570L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.editAnnouncement(articleID);
      }
    });

    // ACTION delete user
    final Embedded deleteDeleteIcon = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_ANNOUNCEMENT_TRASH));
    deleteDeleteIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    deleteDeleteIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    deleteDeleteIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    deleteDeleteIcon.setDescription(ArticleModule.getPortalMessages().getMessage(locale,
        Messages.ARTICLEMANAGEMENT_ACTION_DELETE));
    deleteDeleteIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -3902788076874554569L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.deleteAnnouncement(articleID);
      }
    });
    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.addComponent(publishArticleIcon);
    actionsLayout.setComponentAlignment(publishArticleIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.addComponent(editArticleIcon);
    actionsLayout.addComponent(deleteDeleteIcon);
    actionsLayout.setComponentAlignment(editArticleIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(deleteDeleteIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();

    return actionsLayout;
  }

}
