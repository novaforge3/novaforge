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

import org.novaforge.forge.article.service.ArticleCategoryService;
import org.novaforge.forge.article.service.ArticleService;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.portal.services.PortalService;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class ArticleModule implements PortalModule
{

  public static final String NULL_REPRESENTATION = "";
  private static Locale DEFAULT_LOCALE;
  private static final String DEFAULT_LOCALE_LOCK = "DEFAULT_LOCALE_LOCK";
  private static UserPresenter           USER_PRESENTER;
  private static ProjectPresenter        PROJECT_PRESENTER;
  private static PortalService           PORTAL_SERVICE;
  private static MembershipPresenter     MEMBERSHIP_PRESENTER;
  private static PortalMessages          PORTAL_MESSAGES;
  private static AuthentificationService AUTHENTIFICATION_SERVICE;
  private static ArticleService          ARTICLE_SERVICE;
  private static ArticleCategoryService  ARTICLE_CATEGORY_SERVICE;
  private static LanguageService         LANGUAGE_SERVICE;

  public static Locale getDefaultLocale()
  {
    synchronized (DEFAULT_LOCALE_LOCK)
    {
      if ( DEFAULT_LOCALE == null )
      {
        DEFAULT_LOCALE = getLanguageService().getDefault().getLocale();  
      }
    }
    return DEFAULT_LOCALE;
  }
  
  /**
   * @return the {@link UserPresenter}
   */
  public static UserPresenter getUserPresenter()
  {
    return USER_PRESENTER;
  }

  /**
   * Use by container to inject {@link UserPresenter}
   *
   * @param pUserPresenter
   *     the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    USER_PRESENTER = pUserPresenter;
  }

  /**
   * @return the {@link ProjectPresenter}
   */
  public static ProjectPresenter getProjectPresenter()
  {
    return PROJECT_PRESENTER;
  }

  /**
   * Use by container to inject {@link ProjectPresenter}
   *
   * @param pProjectPresenter
   *     the ProjectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    PROJECT_PRESENTER = pProjectPresenter;
  }

  /**
   * @return the {@link PortalService}
   */
  public static PortalService getPortalService()
  {
    return PORTAL_SERVICE;
  }

  /**
   * Use by container to inject {@link PortalService}
   *
   * @param pPortalService
   *     the portalService to set
   */
  public void setPortalService(final PortalService pPortalService)
  {
    PORTAL_SERVICE = pPortalService;
  }

  /**
   * Get the membership presenter
   *
   * @return the {@link MembershipPresenter}
   */
  public static MembershipPresenter getMembershipPresenter()
  {
    return MEMBERSHIP_PRESENTER;
  }

  /**
   * Use by container to inject {@link MembershipPresenter}
   *
   * @param pMembershipPresenter
   *     the membershipPresenter to set
   */
  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    MEMBERSHIP_PRESENTER = pMembershipPresenter;
  }

  /**
   * @return the {@link PortalMessages}
   */
  public static PortalMessages getPortalMessages()
  {
    return PORTAL_MESSAGES;
  }

  /**
   * Use by container to inject {@link PortalMessages}
   *
   * @param pPortalMessages
   *     the portalMessages to set
   */
  public void setPortalMessages(final PortalMessages pPortalMessages)
  {
    PORTAL_MESSAGES = pPortalMessages;
  }

  /**
   * @return the {@link AuthentificationService}
   */
  public static AuthentificationService getAuthentificationService()
  {
    return AUTHENTIFICATION_SERVICE;
  }

  /**
   * Use by container to inject {@link AuthentificationService}
   *
   * @param pAuthentificationService
   *     the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    AUTHENTIFICATION_SERVICE = pAuthentificationService;
  }

  /**
   * @return the {@link ArticleService}
   */
  public static ArticleService getArticleService()
  {
    return ARTICLE_SERVICE;
  }

  /**
   * Use by container to inject {@link ArticleService}
   *
   * @param pArticleService
   *     the articleService to set
   */
  public void setArticleService(final ArticleService pArticleService)
  {
    ARTICLE_SERVICE = pArticleService;
  }

  /**
   * @return the {@link ArticleCategoryService}
   */
  public static ArticleCategoryService getArticleCategoryService()
  {
    return ARTICLE_CATEGORY_SERVICE;
  }

  /**
   * Use by container to inject {@link ArticleCategoryService}
   *
   * @param pArticleCategoryService
   *     the ArticleCategoryService to set
   */
  public void setArticleCategoryService(final ArticleCategoryService pArticleCategoryService)
  {
    ARTICLE_CATEGORY_SERVICE = pArticleCategoryService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return getModuleId().getId();
  }

  /**
   * Returns {@link PortalModuleId} for this module
   *
   * @return {@link PortalModuleId} for this module
   */
  public static PortalModuleId getModuleId()
  {
    return PortalModuleId.ARTICLE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    return new ArticleComponent(pPortalContext);
  }

  /**
   * @return the {@link LanguageService}
   */
  public static LanguageService getLanguageService()
  {
    return LANGUAGE_SERVICE;
  }

  /**
   * Use by container to inject {@link LanguageService}
   *
   * @param pLanguageService
   *     the LanguageService to set
   */
  public void setLanguageService(final LanguageService pLanguageService)
  {
    LANGUAGE_SERVICE = pLanguageService;
  }

}
