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
package org.novaforge.forge.ui.article.internal.client.article;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class ArticleViewImpl extends VerticalLayout implements ArticleView
{

  /**
   * Default serial UID
   */
  private static final long        serialVersionUID     = -4644112401153968730L;
  /**
   * The admin button
   */
  private final Button             adminButton          = new Button();
  /**
   * The information layout
   */
  private final VerticalLayout     informationLayout    = new VerticalLayout();
  /**
   * The information Title Label
   */
  private final Label              informationTitle     = new Label();
  /**
   * The information Text Label
   */
  private final Label              informationText      = new Label();
  /**
   * The news Layout
   */
  private final HorizontalLayout   newsLayout           = new HorizontalLayout();
  /**
   * The News Grid
   */
  private final ItemGrid           newsGrid             = new ItemGrid(2);
  /**
   * The filter Text TextField
   */
  private final TextField          filterText           = new TextField();
  /**
   * The filter Category Layout
   */
  private final VerticalLayout     filterCategoryLayout = new VerticalLayout();
  /**
   * The filter Button
   */
  private final Button             filterButton         = new Button();
  /**
   * The filter reset Button
   */
  private final Button             filterResetButton    = new Button();
  /**
   * The filter DateForm DateField
   */
  private final DateField          filterDateFrom       = new DateField();
  /**
   * The filter DateTo DateField
   */
  private final DateField          filterDateTo         = new DateField();
  /**
   * The filter Category Label
   */
  private final Label              filterCategoryLabel  = new Label();
  /**
   * The Announcement Carousel
   */
  private final HorizontalCarousel announcementCarousel = new HorizontalCarousel();

  /**
   * Default constructor
   */
  public ArticleViewImpl()
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
    addComponent(initInformationLayout());
    addComponent(initAnnouncementLayout());
    addComponent(initNewsLayout());
  }

  /**
   * Initialize Header layout
   * 
   * @return the header layout component
   */
  private Component initHeaderLayout()
  {
    adminButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    adminButton.setIcon(new ThemeResource(NovaForgeResources.ICON_NEWS_ADMIN));
    HorizontalLayout headerLayout = new HorizontalLayout();
    headerLayout.addComponent(adminButton);

    return headerLayout;
  }

  /**
   * Initialize Information Layout
   * 
   * @return the information layout component
   */
  private Component initInformationLayout()
  {

    informationLayout.setStyleName(NovaForge.ARTICLE_INFORMATION_LAYOUT);
    informationLayout.setWidth(100, Unit.PERCENTAGE);
    informationLayout.setSpacing(true);
    informationLayout.setMargin(true);
    informationTitle.setStyleName(NovaForge.LABEL_H1);
    informationText.setContentMode(ContentMode.HTML);
    informationLayout.addComponent(informationTitle);
    informationLayout.addComponent(informationText);
    return informationLayout;
  }

  /**
   * Initialize Announcement Layout
   * 
   * @return the announcement layout component
   */
  private Component initAnnouncementLayout()
  {
    HorizontalLayout announcementLayout = new HorizontalLayout();
    announcementCarousel.setImmediate(true);
    announcementCarousel.setTransitionDuration(500);
    announcementCarousel.setStyleName(NovaForge.ARTICLE_ANNOUNCEMENT_LAYOUT);
    announcementCarousel.setMouseWheelEnabled(true);
    announcementCarousel.setMouseDragEnabled(false);
    announcementCarousel.setTouchDragEnabled(false);
    announcementCarousel.setArrowKeysMode(ArrowKeysMode.FOCUS);
    announcementCarousel.setLoadMode(CarouselLoadMode.SMART);
    announcementCarousel.setSizeFull();

    // announcementLayout.setMargin(new MarginInfo(true, false, false, false));
    announcementLayout.setStyleName(NovaForge.ARTICLE_ANNOUNCEMENT_LAYOUT);
    announcementLayout.addComponent(announcementCarousel);

    announcementLayout.setWidth(100, Unit.PERCENTAGE);
    announcementLayout.setHeight(40, Unit.PIXELS);
    return announcementLayout;
  }

  /**
   * Initialize the News Layout
   * 
   * @return the news layout component
   */
  private Component initNewsLayout()
  {
    newsGrid.setSelectable(true);
    newsLayout.setStyleName(NovaForge.ARTICLE_NEWS_LAYOUT);
    newsLayout.setSizeFull();
    newsLayout.setSpacing(true);
    newsLayout.setMargin(new MarginInfo(true, false, false, false));
    Component filtersComponent = initNewsFiltersLayout();
    newsLayout.addComponent(newsGrid);
    newsLayout.addComponent(filtersComponent);
    newsLayout.setExpandRatio(newsGrid, 0.7f);
    newsLayout.setExpandRatio(filtersComponent, 0.3f);
    return newsLayout;
  }

  /**
   * The News filter Layout
   * 
   * @return the news filter layout component
   */
  private Component initNewsFiltersLayout()
  {
    VerticalLayout filtersLayout = new VerticalLayout();
    filtersLayout.setSizeFull();
    filterText.setWidth(100, Unit.PERCENTAGE);

    VerticalLayout filtersContent = new VerticalLayout();
    filtersContent.setMargin(true);
    filtersContent.setSpacing(true);
    HorizontalLayout filtersDateLayout = new HorizontalLayout();
    filtersDateLayout.setMargin(new MarginInfo(false, false, true, false));
    filtersDateLayout.setSpacing(true);

    filterCategoryLabel.setStyleName(NovaForge.LABEL_BOLD);

    filtersDateLayout.addComponent(filterDateFrom);
    filtersDateLayout.addComponent(filterDateTo);
    filtersContent.addComponent(filtersDateLayout);
    filtersContent.addComponent(filterCategoryLabel);
    filtersContent.addComponent(filterCategoryLayout);
    filterCategoryLayout.setSpacing(true);

    filterButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    filterButton.setIcon(new ThemeResource(NovaForgeResources.ICON_FILTER));
    filterButton.setWidth(100, Unit.PERCENTAGE);

    filterResetButton.setStyleName(NovaForge.BUTTON_LINK);
    filterResetButton.setSizeUndefined();

    VerticalLayout filtersButtons = new VerticalLayout();
    filtersButtons.setSpacing(true);
    filtersButtons.setWidth(100, Unit.PERCENTAGE);
    filtersButtons.addComponent(filterButton);
    filtersButtons.addComponent(filterResetButton);
    filtersButtons.setComponentAlignment(filterButton, Alignment.MIDDLE_CENTER);
    filtersButtons.setComponentAlignment(filterResetButton, Alignment.MIDDLE_CENTER);

    filtersLayout.addComponent(filterText);
    filtersLayout.addComponent(filtersContent);
    filtersLayout.addComponent(filtersButtons);

    return filtersLayout;
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
    adminButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_ADMIN));
    filterText.setInputPrompt(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_FILTER_ARTICLES));
    filterButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_FILTER_ARTICLES));
    filterResetButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_RESET_FILTER));
    filterDateFrom.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_FILTER_FROM));
    filterDateTo.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_FILTER_TO));
    filterCategoryLabel.setValue(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_FILTER_INCATEGORY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAdminButton()
  {
    return adminButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getInformationText()
  {
    return informationText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getInformationTitle()
  {
    return informationTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemGrid getNewsGrid()
  {
    return newsGrid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getFilterText()
  {
    return filterText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VerticalLayout getFilterCategoryLayout()
  {
    return filterCategoryLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getFilterButton()
  {
    return filterButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DateField getFilterDateFrom()
  {
    return filterDateFrom;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DateField getFilterDateTo()
  {
    return filterDateTo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalCarousel getAnnouncementCarousel()
  {
    return announcementCarousel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getFilterResetButton()
  {
    return filterResetButton;
  }

}