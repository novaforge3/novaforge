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
package org.novaforge.forge.ui.article.internal.client.admin;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.SideBarLayout;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminViewImpl extends SideBarLayout implements AdminView
{

  /**
   * Default serial UID
   */
  private static final long   serialVersionUID      = 2686440126448978924L;
  /**
   * Back to news key
   */
  private static final String BACKTONEWS_KEY        = "backtonews";
  /**
   * Admin information key
   */
  private static final String ADMININFORMATION_KEY  = "adminInformation";
  /**
   * Admin announcement key
   */
  private static final String ADMINANNOUNCEMENT_KEY = "adminAnnouncement";
  /**
   * Admin article key
   */
  private static final String ADMINARTICLE_KEY      = "adminArticle";
  /**
   * Admin categories key
   */
  private static final String ADMINCATEGORY_KEY     = "adminCategory";
  /**
   * The return to public article button
   */
  private final Button backToNews;
  /**
   * The manage Information button
   */
  private final Button adminInformation;
  /**
   * The manage Announcement button
   */
  private final Button adminAnnouncement;
  /**
   * The manage Article button
   */
  private final Button adminArticle;
  /**
   * The manage ArticleCategory button
   */
  private final Button adminCategory;

  /**
   * Default constructor.
   */
  public AdminViewImpl()
  {
    super();
    adminArticle = addButton(ADMINARTICLE_KEY, new ThemeResource(NovaForgeResources.ICON_NEWS));
    adminAnnouncement = addButton(ADMINANNOUNCEMENT_KEY, new ThemeResource(
        NovaForgeResources.ICON_ANNOUNCEMENT));
    adminInformation = addButton(ADMININFORMATION_KEY, new ThemeResource(NovaForgeResources.ICON_INFORMATION));
    adminCategory = addButton(ADMINCATEGORY_KEY, new ThemeResource(NovaForgeResources.ICON_CATEGORY));
    backToNews = addButton(BACKTONEWS_KEY, new ThemeResource(NovaForgeResources.ICON_GO_PREVIOUS_BORDER));
    setStyleName(NovaForge.SIDEBAR_APPLICATION_CONTENT);
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
    getTitle().setValue(
        ArticleModule.getPortalMessages().getMessage(pLocale, Messages.ARTICLEMANAGEMENT_ADMIN_MENU_TITLE));
    adminInformation.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_ADMIN_MENU_INFORMATION));
    adminAnnouncement.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_ADMIN_MENU_ANNOUNCEMENT));
    adminArticle.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_ADMIN_MENU_ARTICLE));
    adminCategory.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_ADMIN_MENU_CATEGORY));
    backToNews.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
        Messages.ARTICLEMANAGEMENT_ADMIN_MENU_BACK));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAdminInformation()
  {
    return adminInformation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAdminAnnouncement()
  {
    return adminAnnouncement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAdminNews()
  {
    return adminArticle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAdminCategory()
  {
    return adminCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getBackToNews()
  {
    return backToNews;
  }

}
