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

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.client.admin.announcement.AdminAnnouncementPresenter;
import org.novaforge.forge.ui.article.internal.client.admin.announcement.AdminAnnouncementViewImpl;
import org.novaforge.forge.ui.article.internal.client.admin.announcement.create.AdminAnnouncementCreatePresenter;
import org.novaforge.forge.ui.article.internal.client.admin.announcement.create.AdminAnnouncementCreateViewImpl;
import org.novaforge.forge.ui.article.internal.client.admin.article.AdminArticlePresenter;
import org.novaforge.forge.ui.article.internal.client.admin.article.AdminArticleViewImpl;
import org.novaforge.forge.ui.article.internal.client.admin.article.create.AdminArticleCreatePresenter;
import org.novaforge.forge.ui.article.internal.client.admin.article.create.AdminArticleCreateViewImpl;
import org.novaforge.forge.ui.article.internal.client.admin.category.AdminCategoryPresenter;
import org.novaforge.forge.ui.article.internal.client.admin.category.AdminCategoryViewImpl;
import org.novaforge.forge.ui.article.internal.client.admin.category.create.AdminCategoryCreatePresenter;
import org.novaforge.forge.ui.article.internal.client.admin.category.create.AdminCategoryCreateViewImpl;
import org.novaforge.forge.ui.article.internal.client.admin.information.AdminInformationPresenter;
import org.novaforge.forge.ui.article.internal.client.admin.information.AdminInformationViewImpl;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminAnnouncementCreateEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminAnnouncementEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminArticleCreateEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminArticleEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminCategoryCreateEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminCategoryEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminInformationEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenArticleEvent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;

import java.io.Serializable;
import java.util.Locale;

/**
 * This presenter handles main view.
 * 
 * @author Jeremy Casery
 */
public class AdminPresenter extends AbstractPortalPresenter implements Serializable
{
  /**
   * Default serial version UID
   */
  private static final long                serialVersionUID = 3111732866599804993L;
  /**
   * Content of project view
   */
  private final AdminView view;
  private AdminAnnouncementPresenter       announcementPresenter;
  private AdminInformationPresenter        informationPresenter;
  private AdminArticlePresenter            newsPresenter;
  private AdminArticleCreatePresenter      createNewsPresenter;
  private AdminAnnouncementCreatePresenter createAnnouncementPresenter;
  private AdminCategoryPresenter           categoryPresenter;
  private AdminCategoryCreatePresenter     categoryCreatePresenter;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context
   */
  public AdminPresenter(final AdminView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    announcementPresenter = new AdminAnnouncementPresenter(new AdminAnnouncementViewImpl(), pPortalContext);
    informationPresenter = new AdminInformationPresenter(new AdminInformationViewImpl(), pPortalContext);
    newsPresenter = new AdminArticlePresenter(new AdminArticleViewImpl(), pPortalContext);
    createNewsPresenter = new AdminArticleCreatePresenter(new AdminArticleCreateViewImpl(), pPortalContext);
    createAnnouncementPresenter = new AdminAnnouncementCreatePresenter(new AdminAnnouncementCreateViewImpl(),
        pPortalContext);
    categoryPresenter = new AdminCategoryPresenter(new AdminCategoryViewImpl(), pPortalContext);
    categoryCreatePresenter = new AdminCategoryCreatePresenter(new AdminCategoryCreateViewImpl(),
        pPortalContext);

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getAdminInformation().addClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 1241839566091722696L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(informationPresenter.getComponent());
        informationPresenter.refresh();
      }
    });
    view.getAdminAnnouncement().addClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -4226503525314987167L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(announcementPresenter.getComponent());
        announcementPresenter.refresh();
      }
    });
    view.getAdminNews().addClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -4142434361225421512L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(newsPresenter.getComponent());
        newsPresenter.refresh();
      }
    });
    view.getAdminCategory().addClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -1409574417881123635L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(categoryPresenter.getComponent());
        categoryPresenter.refresh();
      }
    });
    view.getBackToNews().addClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 3794025324802340863L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getBackToNews().removeStyleName(NovaForge.SELECTED);
        getEventBus().publish(new OpenArticleEvent());
      }
    });
  }

  /**
   * Will refresh the projects list
   */
  public void refresh()
  {
    view.getAdminNews().addStyleName(NovaForge.SELECTED);
    view.setSecondComponent(newsPresenter.getComponent());
    newsPresenter.refresh();
  }

  /**
   * Callback on {@link OpenAdminArticleEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showAdminNewsViewEvent(final OpenAdminArticleEvent pEvent)
  {
    view.setSecondComponent(newsPresenter.getComponent());
    newsPresenter.refresh();
  }

  /**
   * Callback on {@link OpenAdminAnnouncementEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showAdminAnnouncementViewEvent(final OpenAdminAnnouncementEvent pEvent)
  {
    view.setSecondComponent(announcementPresenter.getComponent());
    announcementPresenter.refresh();
  }

  /**
   * Callback on {@link OpenAdminInformationEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showAdminInformationViewEvent(final OpenAdminInformationEvent pEvent)
  {
    view.setSecondComponent(informationPresenter.getComponent());
    informationPresenter.refresh();
  }

  /**
   * Callback on {@link OpenAdminArticleCreateEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showAdminNewsCreateViewEvent(final OpenAdminArticleCreateEvent pEvent)
  {
    view.setSecondComponent(createNewsPresenter.getComponent());
    createNewsPresenter.refresh(pEvent.getArticle());
  }

  /**
   * Callback on {@link OpenAdminAnnouncementCreateEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showAdminAnnouncementCreateViewEvent(final OpenAdminAnnouncementCreateEvent pEvent)
  {
    view.setSecondComponent(createAnnouncementPresenter.getComponent());
    createAnnouncementPresenter.refresh(pEvent.getAnnouncement());
  }

  /**
   * Callback on {@link OpenAdminCategoryEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showAdminCategoryViewEvent(final OpenAdminCategoryEvent pEvent)
  {
    view.setSecondComponent(categoryPresenter.getComponent());
    categoryPresenter.refresh();
  }

  /**
   * Callback on {@link OpenAdminCategoryCreateEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void showOpenAdminCategoryCreateEvent(final OpenAdminCategoryCreateEvent pEvent)
  {
    view.setSecondComponent(categoryCreatePresenter.getComponent());
    categoryCreatePresenter.refresh(pEvent.getCategory());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return ArticleModule.getModuleId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    // Handle by each sub presenter
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
