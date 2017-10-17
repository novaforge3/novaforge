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
package org.novaforge.forge.ui.article.internal.client.admin.category.create;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.article.internal.client.event.OpenAdminCategoryEvent;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminCategoryCreatePresenter extends AbstractPortalPresenter
{

  /**
   * Content the workspace view
   */
  private final AdminCategoryCreateView view;

  private ArticleCategory               currentCategory;

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view attached to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public AdminCategoryCreatePresenter(final AdminCategoryCreateView pView, final PortalContext pPortalContext)
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
    view.getBackToListButton().addClickListener(new Button.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 1850199363682994048L;

      @Override
      public void buttonClick(ClickEvent event)
      {

        getEventBus().publish(new OpenAdminCategoryEvent());
      }
    });
    view.getCreateButton().addClickListener(new Button.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -7150251975963075427L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        try
        {
          for (FieldGroup binder : view.getFormBinders())
          {
            binder.commit();
          }
          view.getCategoryBinder().commit();
          currentCategory.setColor(Integer.toString(view.getColorPicker().getColor().getRGB()));
          ArticleModule.getArticleCategoryService().updateArticleCategory(currentCategory);
          TrayNotification.show(
              ArticleModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.ARTICLEMANAGEMENT_NOTIFICATION_CREATE_TITLE), ArticleModule.getPortalMessages()
                  .getMessage(getCurrentLocale(), Messages.ARTICLEMANAGEMENT_NOTIFICATION_CREATE_DESC),
              TrayNotificationType.SUCCESS);
          getEventBus().publish(new OpenAdminCategoryEvent());
        }
        catch (final CommitException e)
        {
          TrayNotification.show(e.getCause().getMessage(), TrayNotificationType.WARNING);
        }
        catch (ArticleCategoryServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, UI.getCurrent()
              .getLocale());
        }

      }
    });

  }

  /**
   * Will refresh the article list
   */
  public void refresh(final ArticleCategory pCategory)
  {
    currentCategory = pCategory;
    if (currentCategory == null)
    {
      view.setUpdateMode(false);
    }
    else
    {
      view.setUpdateMode(true);
    }
    refreshContent();
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
      if (currentCategory == null)
      {
        currentCategory = ArticleModule.getArticleCategoryService().newArticleCategory();
      }
      view.bindToCategory(currentCategory);
    }
    catch (LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ArticleModule.getPortalMessages(), e, getCurrentLocale());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(Locale pLocale)
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
