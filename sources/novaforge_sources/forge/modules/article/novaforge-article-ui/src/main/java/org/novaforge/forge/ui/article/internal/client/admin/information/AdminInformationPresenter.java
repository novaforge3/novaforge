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
package org.novaforge.forge.ui.article.internal.client.admin.information;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.Date;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminInformationPresenter extends AbstractPortalPresenter
{
  /**
   * Content the workspace view
   */
  private final AdminInformationView view;
  /**
   * The current working Information
   */
  private Article                    information;

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view attached to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public AdminInformationPresenter(final AdminInformationView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getSaveButton().addClickListener(new Button.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 2586170461237609551L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        try
        {
          for (FieldGroup binder : view.getFormBinders())
          {
            binder.commit();
          }
          information.setDate(new Date());
          information.setPublished(true);
          ArticleModule.getArticleService().updateArticle(information);
          TrayNotification.show(
              ArticleModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.ARTICLEMANAGEMENT_INFORMATION_UPDATE_TITLE), ArticleModule.getPortalMessages()
                  .getMessage(getCurrentLocale(), Messages.ARTICLEMANAGEMENT_INFORMATION_UPDATE_DESC),
              TrayNotificationType.SUCCESS);
        }
        catch (final CommitException e)
        {
          TrayNotification.show(e.getCause().getMessage(), TrayNotificationType.WARNING);
        }
        catch (ArticleServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
              .getLocale());
        }

      }
    });
  }

  /**
   * Refresh the presenter and the view with the given project id
   * 
   * @param pProjectId
   *          the project id to display
   */
  public void refresh()
  {
    // Initialize News list
    refreshContent();
    // Refresh Label
    refreshLocalized(getCurrentLocale());
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
    try
    {
      information = ArticleModule.getArticleService().getInformation();
      view.bindToInformation(information);
    }
    catch (LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }
    catch (ArticleCategoryServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

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
