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
package org.novaforge.forge.ui.article.internal.client.admin.category.component;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.ui.article.internal.client.component.CategoryItemProperty;

/**
 * @author Jeremy Casery
 */
public class CategoryColumnTitleGenerator implements ColumnGenerator
{

  /**
   * 
   */
  private static final long   serialVersionUID = -2127468781787346264L;

  private static final String COLOR_SIZE       = "20px";

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the admin article table
   */
  public CategoryColumnTitleGenerator()
  {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(Table source, Object itemId, Object columnId)
  {

    final ArticleCategory category = (ArticleCategory) itemId;

    HorizontalLayout titleLayout = new HorizontalLayout();

    final Color categoryColor = new Color(Integer.valueOf(category.getColor()));
    final String cssColor = categoryColor.getCSS();

    CssLayout colorLayout = new CssLayout()
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
    HorizontalLayout innerColor = new HorizontalLayout();
    innerColor.setSizeFull();
    colorLayout.addComponent(innerColor);
    colorLayout.setWidth(COLOR_SIZE);
    colorLayout.setHeight(COLOR_SIZE);

    Label categoryTitle = new Label((String) source.getContainerDataSource().getItem(itemId)
        .getItemProperty(CategoryItemProperty.TITLE.getPropertyId()).getValue());

    titleLayout.addComponent(colorLayout);
    titleLayout.addComponent(categoryTitle);
    titleLayout.setSpacing(true);

    titleLayout.setComponentAlignment(colorLayout, Alignment.MIDDLE_LEFT);
    titleLayout.setComponentAlignment(categoryTitle, Alignment.MIDDLE_LEFT);

    return titleLayout;

  }
}
