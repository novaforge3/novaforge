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
package org.novaforge.forge.ui.article.internal.client.admin.article.component;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class ArticleColumnCategoryGenerator implements ColumnGenerator
{

  /**
   * 
   */
  private static final long   serialVersionUID = 4755530724225224896L;
  private static final String COLOR_SIZE       = "20px";

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(Table source, Object itemId, Object columnId)
  {
    HorizontalLayout titleLayout = new HorizontalLayout();
    try
    {
      final ArticleCategory category = ArticleModule.getArticleService().getArticle((UUID) itemId)
          .getArticleCategory();

      CssLayout colorLayout;
      Label categoryTitle = new Label();
      if (category != null)
      {
        final Color categoryColor = new Color(Integer.valueOf(category.getColor()));
        final String cssColor = categoryColor.getCSS();

        colorLayout = new CssLayout()
        {
          @Override
          protected String getCss(Component c)
          {
            if (c instanceof HorizontalLayout)
            {
              return "background: " + cssColor + ";border: thin black solid;";
            }
            return null;
          }
        };
        ArticleCategoryContent categoryContent = category.getCategoryContent(UI.getCurrent().getLocale());
        if (categoryContent == null || categoryContent.getTitle() == null)
        {
          categoryContent = category.getCategoryContent(ArticleModule.getDefaultLocale());
        }
        categoryTitle.setValue(categoryContent.getTitle());
      }
      else
      {
        colorLayout = new CssLayout()
        {
          @Override
          protected String getCss(Component c)
          {
            if (c instanceof HorizontalLayout)
            {
              return "border: thin black solid;";
            }
            return null;
          }
        };
        colorLayout.setStyleName(NovaForge.ARTICLE_CATEGORY_DEFAULT_COLOR);
        categoryTitle.setValue(ArticleModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
            Messages.ARTICLEMANAGEMENT_CATEGORY_UNKNOW));
      }

      HorizontalLayout innerColor = new HorizontalLayout();
      innerColor.setSizeFull();
      colorLayout.addComponent(innerColor);
      colorLayout.setWidth(COLOR_SIZE);
      colorLayout.setHeight(COLOR_SIZE);

      titleLayout.addComponent(colorLayout);
      titleLayout.addComponent(categoryTitle);
      titleLayout.setSpacing(true);

      titleLayout.setComponentAlignment(colorLayout, Alignment.MIDDLE_LEFT);
      titleLayout.setComponentAlignment(categoryTitle, Alignment.MIDDLE_LEFT);

    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }

    return titleLayout;

    // HorizontalLayout titleLayout = new HorizontalLayout();
    // Article article;
    // try
    // {
    // article = ArticleModule.getArticleService().getArticle((UUID) itemId);
    // HorizontalLayout colorLayout = new HorizontalLayout();
    // colorLayout.setWidth(COLOR_SIZE);
    // colorLayout.setHeight(COLOR_SIZE);
    // Styles styles = UI.getCurrent().getPage().getStyles();
    // String cssClass = NovaForge.ARTICLE_CATEGORY_DEFAULT_COLOR;
    // Label categoryTitle = new Label(ArticleModule.getPortalMessages().getMessage(
    // UI.getCurrent().getLocale(), Messages.ARTICLEMANAGEMENT_CATEGORY_UNKNOW));
    // if (article.getArticleCategory() != null)
    // {
    // cssClass = Utils.STYLES_PREFIX + article.getArticleCategory().getUuid().toString();
    // // inject the new color as a style
    // Color categoryCssColor = new Color(Integer.valueOf(article.getArticleCategory().getColor()));
    // styles.add(cssClass + " { background: " + categoryCssColor.getCSS() + " !important; }");
    // categoryTitle.setValue(article.getArticleCategory().getCategoryContent(UI.getCurrent().getLocale())
    // .getTitle());
    // }
    // colorLayout.setStyleName(cssClass);
    //
    // titleLayout.addComponent(colorLayout);
    // titleLayout.addComponent(categoryTitle);
    // titleLayout.setSpacing(true);
    // titleLayout.setComponentAlignment(colorLayout, Alignment.MIDDLE_LEFT);
    // titleLayout.setComponentAlignment(categoryTitle, Alignment.MIDDLE_LEFT);
    // }
    // catch (ArticleServiceException e)
    // {
    // ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
    // .getLocale());
    // }
    //
    // return titleLayout;

  }
}
