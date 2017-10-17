/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.widgets.quality.client.admin;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.categories.quality.QualityCategoryService;
import org.novaforge.forge.core.plugins.categories.quality.QualityResourceBean;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetPresenter;
import org.novaforge.forge.widgets.quality.admin.PropertiesFactory;
import org.novaforge.forge.widgets.quality.admin.QualityResource;
import org.novaforge.forge.widgets.quality.module.AbstractQualityModule;

import com.google.common.base.Strings;
import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * @author Cart Gauthier
 */
public class AdminPresenter extends AbstractWidgetPresenter implements Serializable
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -3883857675153083169L;
  /**
   * View associated to this presenter
   */
  private final AdminView   view;
  private boolean           onError;

  /**
   * @param pWidgetContext
   * @param pView
   */
  public AdminPresenter(final WidgetContext pWidgetContext, final AdminView pView)
  {
    super(pWidgetContext);
    // Init the view
    view = pView;
    onError = false;
  }

  public void refresh()
  {
    refreshContent();
    refreshLocalized(view.getLocale());
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
      final Map<String, List<String>> applicationsByProject = getWidgetContext().getApplicationsByProject();

      if (applicationsByProject.isEmpty() == false)
      {
        final String projectId = applicationsByProject.keySet().iterator().next();

        final List<String> projectApplications = applicationsByProject.get(projectId);

        if ((projectApplications != null) && (projectApplications.isEmpty() == false))
        {
          final String instanceUUID = projectApplications.get(0);

          final ProjectApplication application = AbstractQualityModule.getApplicationPresenter()
              .getApplication(projectId, UUID.fromString(instanceUUID));
          final QualityCategoryService pluginCategoryService = AbstractQualityModule.getPluginsManager()
              .getPluginCategoryService(application.getPluginUUID().toString(), QualityCategoryService.class);

          final UUID forgeId = AbstractQualityModule.getForgeId();
          final String currentUser = AbstractQualityModule.getCurrentUser();
          final List<QualityResourceBean> resources = pluginCategoryService.getResourcesByProject(
              forgeId.toString(), instanceUUID, currentUser);

          for (final QualityResourceBean resource : resources)
          {
            view.getResourceComboBox().addItem(resource.getId());
            view.getResourceComboBox().setItemCaption(resource.getId(), resource.getName());
          }

          final QualityResource selectedResourceId = PropertiesFactory.readProperties(getWidgetContext()
              .getProperties());
          if (Strings.isNullOrEmpty(selectedResourceId.getId()) == false)
          {
            view.getResourceComboBox().select(selectedResourceId.getId());
          }
        }
        else
        {
          onError = true;
        }
      }
      else
      {
        onError = true;
      }
      view.setOnError(onError);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
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

  public QualityResource getResource()
  {
    QualityResource resource = new QualityResource();

    final Object resourceId = view.getResourceComboBox().getValue();
    if (resourceId != null)
    {
      resource.setId(resourceId.toString());
      resource.setName(view.getResourceComboBox().getItemCaption(resourceId));
    }

    return resource;
  }

  public boolean isValidResource()
  {
    final boolean valid = true;
    if (onError == false)
    {
      try
      {
        view.getResourceComboBox().validate();
      }
      catch (final Validator.InvalidValueException e)
      {
        Notification.show(e.getLocalizedMessage(), Type.WARNING_MESSAGE);
      }
    }
    else
    {
      Notification.show(
          AbstractQualityModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.QUALITYLIBRARIES_ADMIN_DATASOURCE), Type.WARNING_MESSAGE);
    }

    return valid;
  }
}
