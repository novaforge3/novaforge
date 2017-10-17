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
package org.novaforge.forge.ui.article.internal.client.admin.category;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminCategoryViewImpl extends VerticalLayout implements AdminCategoryView
{

  /**
   * 
   */
  private static final long serialVersionUID     = -4644112401153968730L;

  private final Table       categoryTable        = new Table();
  private final Button      createCategoryButton = new Button();

  public AdminCategoryViewImpl()
  {
    initLayout();
  }

  private void initLayout()
  {
    addComponent(initHeader());
    addComponent(initList());
    setSpacing(true);
    setMargin(true);
  }

  private Component initHeader()
  {
    HorizontalLayout headerLayout = new HorizontalLayout();
    createCategoryButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    createCategoryButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    headerLayout.addComponent(createCategoryButton);
    headerLayout.setMargin(new MarginInfo(false, false, true, false));
    return headerLayout;
  }

  private Component initList()
  {
    VerticalLayout listLayout = new VerticalLayout();
    categoryTable.setSelectable(true);
    categoryTable.setPageLength(10);
    categoryTable.setWidth(100, Unit.PERCENTAGE);
    listLayout.addComponent(categoryTable);
    return listLayout;
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
    createCategoryButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                                 Messages.ARTICLEMANAGEMENT_CREATE_CATEGORY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getCategoryTable()
  {
    return categoryTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCreateCategoryButton()
  {
    return createCategoryButton;
  }

}
