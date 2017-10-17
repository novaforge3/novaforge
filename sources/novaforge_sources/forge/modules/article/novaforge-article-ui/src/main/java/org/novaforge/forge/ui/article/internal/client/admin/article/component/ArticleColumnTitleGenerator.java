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

import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class ArticleColumnTitleGenerator implements ColumnGenerator
{

  /**
   *
   */
  private static final long serialVersionUID = 6885432796343068133L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(Table source, Object itemId, Object columnId)
  {
    // Get Locale
    final Locale locale = UI.getCurrent().getLocale();
    // Show localized Title article
    final Label titleLabel = new Label();
    Article article;
    try
    {
      article = ArticleModule.getArticleService().getArticle((UUID) itemId);
      ArticleContent content = article.getArticleContent(locale);
      if (content == null || content.getTitle() == null)
      {
        content = article.getArticleContent(ArticleModule.getDefaultLocale());
      }
      titleLabel.setValue(content.getTitle());
      titleLabel.setDescription(content.getShortText());
    }
    catch (ArticleServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, locale);
    }

    return titleLabel;
  }

}
