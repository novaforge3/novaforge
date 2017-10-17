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

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminAnnouncementViewImpl extends VerticalLayout implements AdminAnnouncementView
{

  /**
   * Default serial UID
   */
  private static final long serialVersionUID         = -4644112401153968730L;
  /**
   * The announcement table
   */
  private final Table       announcementTable        = new Table();
  /**
   * The create announcement button
   */
  private final Button      createAnnouncementButton = new Button();
  /**
   * The filter textfield
   */
  private final TextField   filterTextField          = new TextField();
  /**
   * The filter announcement button
   */
  private final Button      filterButton             = new Button();

  /**
   * Default constructor
   */
  public AdminAnnouncementViewImpl()
  {
    initLayout();
  }

  /**
   * Initialize the layout
   */
  private void initLayout()
  {
    addComponent(initHeader());
    addComponent(initFilters());
    addComponent(initList());
    setSpacing(true);
    setMargin(true);
  }

  /**
   * Initialize the Header layout
   * 
   * @return the header layout component
   */
  private Component initHeader()
  {
    HorizontalLayout headerLayout = new HorizontalLayout();
    createAnnouncementButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    createAnnouncementButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    headerLayout.addComponent(createAnnouncementButton);
    headerLayout.setMargin(new MarginInfo(false, false, true, false));
    return headerLayout;
  }

  /**
   * Init the announcement filters layout
   * 
   * @return the filters layout
   */
  private Component initFilters()
  {
    HorizontalLayout filtersLayout = new HorizontalLayout();
    filtersLayout.setSpacing(true);
    filterTextField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    filterButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    filterButton.setIcon(new ThemeResource(NovaForgeResources.ICON_FILTER));
    filtersLayout.addComponent(filterTextField);
    filtersLayout.addComponent(filterButton);
    filtersLayout.setComponentAlignment(filterTextField, Alignment.BOTTOM_LEFT);
    filtersLayout.setComponentAlignment(filterButton, Alignment.BOTTOM_LEFT);
    return filtersLayout;
  }

  /**
   * Initialize the announcement list layout
   * 
   * @return the list layout component
   */
  private Component initList()
  {
    VerticalLayout listLayout = new VerticalLayout();
    announcementTable.setSelectable(true);
    announcementTable.setPageLength(10);
    announcementTable.setWidth(100, Unit.PERCENTAGE);
    listLayout.addComponent(announcementTable);
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
    createAnnouncementButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                                     Messages.ARTICLEMANAGEMENT_CREATE_ANNOUNCEMENT));
    filterTextField.setInputPrompt(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.ARTICLEMANAGEMENT_FILTER_ANNOUNCEMENTS));
    filterButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                         Messages.ARTICLEMANAGEMENT_FILTER_ANNOUNCEMENTS));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getAnnouncementTable()
  {
    return announcementTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCreateAnnouncementButton()
  {
    return createAnnouncementButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getFilterButton()
  {
    return filterButton;
  }

}
