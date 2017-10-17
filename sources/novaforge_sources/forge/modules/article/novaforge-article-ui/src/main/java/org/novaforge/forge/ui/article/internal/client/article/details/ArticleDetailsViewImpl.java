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

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class ArticleDetailsViewImpl extends VerticalLayout implements ArticleDetailsView
{

  /**
   * Default serial UID
   */
  private static final long    serialVersionUID   = -4644112401153968730L;
  /**
   * Back to News button
   */
  private final Button         backToNews         = new Button();
  /**
   * News Title Label
   */
  private final Label          newsTitle          = new Label();
  /**
   * News ShortText Label
   */
  private final Label          newsShortText      = new Label();
  /**
   * News Text Label
   */
  private final Label          newsText           = new Label();
  /**
   * News Layout
   */
  private final VerticalLayout newsLayout         = new VerticalLayout();
  /**
   * News Date Label
   */
  private final Label          newsDate           = new Label();
  /**
   * News Category Layout
   */
  private HorizontalLayout newsCategoryLayout = new HorizontalLayout();

  /**
   * Default constructor
   */
  public ArticleDetailsViewImpl()
  {
    initLayout();
    setMargin(true);
    setSpacing(true);
    setStyleName(NovaForge.NOVAFORGE_APPLICATION_CONTENT);
  }

  /**
   * Initialize the layout
   */
  private void initLayout()
  {
    addComponent(initHeaderLayout());
    addComponent(initNewsLayout());
  }

  /**
   * Initialize the header layout
   * 
   * @return the header layout component
   */
  private Component initHeaderLayout()
  {
    backToNews.setStyleName(NovaForge.BUTTON_PRIMARY);
    backToNews.setIcon(new ThemeResource(NovaForgeResources.ICON_GO_PREVIOUS_DARK));
    HorizontalLayout headerLayout = new HorizontalLayout();
    headerLayout.addComponent(backToNews);

    return headerLayout;
  }

  /**
   * Initialize the news layout
   * 
   * @return the news layout component
   */
  private Component initNewsLayout()
  {

    newsLayout.setSpacing(true);
    newsTitle.setStyleName(NovaForge.LABEL_H1);
    newsShortText.setContentMode(ContentMode.HTML);
    newsText.setContentMode(ContentMode.HTML);
    newsCategoryLayout.setWidth(100, Unit.PERCENTAGE);
    newsCategoryLayout.setHeight(20, Unit.PIXELS);
    newsLayout.addComponent(newsCategoryLayout);
    newsLayout.addComponent(newsTitle);
    newsLayout.addComponent(newsShortText);
    newsLayout.addComponent(newsText);
    newsLayout.addComponent(newsDate);
    return newsLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    backToNews.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                       Messages.ARTICLEMANAGEMENT_ARTICLE_DETAILS_BACK));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getBackToNews()
  {
    return backToNews;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getNewsText()
  {
    return newsText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getNewsShortText()
  {
    return newsShortText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getNewsTitle()
  {
    return newsTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCategory(ArticleCategory pCategory)
  {
    newsCategoryLayout.removeAllComponents();
    CssLayout newsCategoryCssLayout;
    Label categoryTitle = new Label();
    if (pCategory != null)
    {
      final Color categoryColor = new Color(Integer.valueOf(pCategory.getColor()));
      final String cssColor = categoryColor.getCSS();

      newsCategoryCssLayout = new CssLayout()
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

      ArticleCategoryContent categoryContent = pCategory.getCategoryContent(UI.getCurrent().getLocale());
      if (categoryContent == null || categoryContent.getTitle() == null)
      {
        categoryContent = pCategory.getCategoryContent(ArticleModule.getDefaultLocale());
      }
      categoryTitle.setValue(categoryContent.getTitle());

    }
    else
    {
      newsCategoryCssLayout = new CssLayout();
      newsCategoryCssLayout.setStyleName(NovaForge.ARTICLE_CATEGORY_DEFAULT_COLOR);
      categoryTitle.setValue(ArticleModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
          Messages.ARTICLEMANAGEMENT_CATEGORY_UNKNOW));

    }
    categoryTitle.setSizeUndefined();
    HorizontalLayout innerColor = new HorizontalLayout();
    innerColor.setSizeFull();
    innerColor.addComponent(categoryTitle);
    innerColor.setComponentAlignment(categoryTitle, Alignment.MIDDLE_RIGHT);
    innerColor.setMargin(new MarginInfo(false, true, false, false));

    newsCategoryCssLayout.addComponent(innerColor);

    newsCategoryCssLayout.setSizeFull();

    newsCategoryLayout.addComponent(newsCategoryCssLayout);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getNewsDate()
  {
    return newsDate;
  }

}