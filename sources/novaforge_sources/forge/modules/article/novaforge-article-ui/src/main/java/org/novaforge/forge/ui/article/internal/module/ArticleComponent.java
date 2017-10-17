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
package org.novaforge.forge.ui.article.internal.module;

import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.client.admin.AdminPresenter;
import org.novaforge.forge.ui.article.internal.client.admin.AdminViewImpl;
import org.novaforge.forge.ui.article.internal.client.article.ArticlePresenter;
import org.novaforge.forge.ui.article.internal.client.article.ArticleViewImpl;
import org.novaforge.forge.ui.article.internal.client.article.details.ArticleDetailsPresenter;
import org.novaforge.forge.ui.article.internal.client.article.details.ArticleDetailsViewImpl;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenArticleDetailsEvent;
import org.novaforge.forge.ui.article.internal.client.event.OpenArticleEvent;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalComponent;

/**
 * @author Jeremy Casery
 */
public class ArticleComponent extends AbstractPortalComponent
{

  private final ArticlePresenter        articlePresenter;

  private final AdminPresenter          adminPresenter;

  private final ArticleDetailsPresenter articleDetailsPresenter;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          the initial context
   */
  public ArticleComponent(final PortalContext pPortalContext)
  {
    super(pPortalContext);

    articlePresenter = new ArticlePresenter(new ArticleViewImpl(), pPortalContext);
    adminPresenter = new AdminPresenter(new AdminViewImpl(), pPortalContext);
    articleDetailsPresenter = new ArticleDetailsPresenter(new ArticleDetailsViewImpl(), pPortalContext);
    setContent(articlePresenter.getComponent());
    articlePresenter.refresh();
  }

  /**
   * Callback on {@link OpenUserAdminEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void showAdminNewsViewEvent(final OpenAdminEvent pEvent)
  {
    setContent(adminPresenter.getComponent());
    adminPresenter.refresh();
  }

  /**
   * Callback on {@link OpenArticleEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void showNewsViewEvent(final OpenArticleEvent pEvent)
  {
    setContent(articlePresenter.getComponent());
    articlePresenter.refresh();
  }

  /**
   * Callback on {@link OpenArticleEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void showNewsDeatilsViewEvent(final OpenArticleDetailsEvent pEvent)
  {
    setContent(articleDetailsPresenter.getComponent());
    articleDetailsPresenter.setArticleUUID(pEvent.getArticleUUID());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    articlePresenter.refresh();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return ArticleModule.getModuleId();
  }
}