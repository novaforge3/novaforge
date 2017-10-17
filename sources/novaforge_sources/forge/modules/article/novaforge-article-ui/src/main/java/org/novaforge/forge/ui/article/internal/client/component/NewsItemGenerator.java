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
package org.novaforge.forge.ui.article.internal.client.component;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;

import java.text.DateFormat;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class NewsItemGenerator implements ItemGenerator
{

  /**
   * Dafault serial UID
   */
  private static final long   serialVersionUID = -7949790291615981729L;

  /**
   * Width of the news box
   */
  private static final String NEWS_WIDTH       = "400px";
  /**
   * The current working Article
   */
  private Article             news;

  /**
   * Default constructor
   */
  public NewsItemGenerator()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(AbstractItemLayout pSource, Object pItemId)
  {
    VerticalLayout newsLayout = new VerticalLayout();
    newsLayout.setWidth(NEWS_WIDTH);
    try
    {
      news = ArticleModule.getArticleService().getArticle((UUID) pItemId);
      newsLayout.setStyleName(NovaForge.ARTICLE_NEWS_BOX);
      newsLayout.addStyleName(NovaForge.CURSOR_BUTTON);
      // Category Layout
      Label categoryName = new Label();
      CssLayout categoryLayout;
      if (news.getArticleCategory() == null)
      {
        categoryLayout = new CssLayout();
        categoryLayout.setStyleName(NovaForge.ARTICLE_CATEGORY_DEFAULT_COLOR);
        categoryName.setValue(ArticleModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
            Messages.ARTICLEMANAGEMENT_CATEGORY_UNKNOW));
      }
      else
      {
        final Color categoryColor = new Color(Integer.valueOf(news.getArticleCategory().getColor()));
        final String cssColor = categoryColor.getCSS();

        categoryLayout = new CssLayout()
        {
          @Override
          protected String getCss(Component c)
          {
            if (c instanceof HorizontalLayout)
            {
              return "background: " + cssColor + ";color: white;";
            }
            return null;
          }
        };
        categoryName.setValue(news.getArticleCategory().getCategoryContent(UI.getCurrent().getLocale())
            .getTitle());
      }
      categoryLayout.setWidth(100, Unit.PERCENTAGE);
      categoryLayout.setHeight(20, Unit.PIXELS);

      categoryName.setSizeUndefined();
      HorizontalLayout innerCategory = new HorizontalLayout();
      innerCategory.setSizeFull();
      innerCategory.addComponent(categoryName);
      innerCategory.setComponentAlignment(categoryName, Alignment.MIDDLE_RIGHT);
      innerCategory.setMargin(new MarginInfo(false, true, false, false));

      categoryLayout.addComponent(innerCategory);
      // Content Layout
      VerticalLayout contentLayout = new VerticalLayout();
      contentLayout.setStyleName(NovaForge.CURSOR_BUTTON);
      ArticleContent newsContent = news.getArticleContent(UI.getCurrent().getLocale());
      if (newsContent == null || newsContent.getTitle() == null)
      {
        newsContent = news.getArticleContent(ArticleModule.getDefaultLocale());
      }
      Label title = new Label(newsContent.getTitle());
      title.setContentMode(ContentMode.HTML);
      title.setStyleName(NovaForge.LABEL_H2);
      title.addStyleName(NovaForge.CURSOR_BUTTON);

      Label summary = new Label(newsContent.getShortText());
      summary.setContentMode(ContentMode.HTML);
      summary.setStyleName(NovaForge.CURSOR_BUTTON);

      HorizontalLayout footer = new HorizontalLayout();
      footer.setStyleName(NovaForge.ARTICLE_NEWS_FOOTER);
      footer.addStyleName(NovaForge.CURSOR_BUTTON);
      footer.setWidth(100, Unit.PERCENTAGE);
      DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, UI.getCurrent().getLocale());
      Label dateLabel = new Label(dateFormat.format(news.getDate()));
      dateLabel.setSizeUndefined();
      dateLabel.addStyleName(NovaForge.CURSOR_BUTTON);
      footer.addComponent(dateLabel);
      footer.setComponentAlignment(dateLabel, Alignment.MIDDLE_CENTER);

      contentLayout.addComponent(title);
      contentLayout.addComponent(summary);
      contentLayout.addComponent(footer);
      contentLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
      contentLayout.setComponentAlignment(summary, Alignment.MIDDLE_LEFT);

      newsLayout.addComponent(categoryLayout);
      newsLayout.addComponent(contentLayout);
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    return newsLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeGenerated(AbstractItemLayout pSource, Object pItemId, Object pPropertyChanged)
  {
    boolean isNeeded = false;

    if ((ArticleItemProperty.CATEGORY.getPropertyId().equals(pPropertyChanged))
        || (ArticleItemProperty.CONTENT.getPropertyId().equals(pPropertyChanged))
        || (ArticleItemProperty.DATE.getPropertyId().equals(pPropertyChanged)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object date = item.getItemProperty(ArticleItemProperty.DATE.getPropertyId()).getValue();
      final Object content = item.getItemProperty(ArticleItemProperty.CONTENT.getPropertyId()).getValue();
      isNeeded = (date != null) && (content != null);
    }
    return isNeeded;
  }

}
