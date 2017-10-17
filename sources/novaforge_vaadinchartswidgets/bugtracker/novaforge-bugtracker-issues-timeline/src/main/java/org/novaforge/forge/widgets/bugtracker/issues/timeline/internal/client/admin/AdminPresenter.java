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
package org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.client.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerCategoryService;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetPresenter;
import org.novaforge.forge.ui.portal.client.component.DateRangeComponent.RangeType;
import org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.admin.PropertiesFactory;
import org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.module.IssuesTimelineModule;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;

/**
 * @author Jeremy Casery
 */
@SuppressWarnings("serial")
public class AdminPresenter extends AbstractWidgetPresenter implements Serializable
{

  /**
   * View associated to this presenter
   */
  private final AdminView view;

  /**
   * @param pWidgetContext
   */
  public AdminPresenter(final WidgetContext pWidgetContext, final AdminView pView)
  {
    super(pWidgetContext);
    // Init the view
    view = pView;
  }

  public void refresh()
  {
    refreshContent();
    view.refreshLocale(view.getLocale());
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

      for (final Entry<String, List<String>> applicationsByProjectEntries : applicationsByProject.entrySet())
      {
        final String projectId = applicationsByProjectEntries.getKey();

        final List<String> instances = applicationsByProjectEntries.getValue();
        for (final String instanceUUID : instances)
        {
          final ProjectApplication application = IssuesTimelineModule.getApplicationPresenter()
              .getApplication(projectId, UUID.fromString(instanceUUID));
          final BugTrackerCategoryService pluginCategoryService = IssuesTimelineModule.getPluginsManager()
              .getPluginCategoryService(application.getPluginUUID().toString(),
                  BugTrackerCategoryService.class);

          final UUID forgeId = IssuesTimelineModule.getForgeId();
          final String currentUser = IssuesTimelineModule.getCurrentUser();
          final List<String> versions = pluginCategoryService.getAllProjectVersions(forgeId.toString(),
              instanceUUID, currentUser).getVersions();

          for (final String version : versions)
          {
            view.getVersionComboBox().addItem(version);
          }
        }
      }
      view.getVersionComboBox().setValue(
          PropertiesFactory.readVersionProperties(getWidgetContext().getProperties()));
      view.getDateRangeComponent().setStartValue(
          PropertiesFactory.readStartDateProperties(getWidgetContext().getProperties()));
      view.getDateRangeComponent().setEndValue(
          PropertiesFactory.readEndDateProperties(getWidgetContext().getProperties()));
      view.getDateRangeComponent().setRangeType(
          PropertiesFactory.readRangeTypeProperties(getWidgetContext().getProperties()));
    }
    catch (final Exception e)
    {
      // TODO: handle exception
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale locale)
  {
    view.refreshLocale(locale);
  }

  /**
   * 
   */
  public void validate() throws InvalidValueException
  {
    view.getVersionComboBox().validate();
  }

  public String getVersion()
  {
    final Object value = view.getVersionComboBox().getValue();
    if (value == null)
    {
      return null;
    }
    else
    {
      return value.toString();
    }
  }

  public RangeType getRangeType()
  {
    return view.getDateRangeComponent().getRangeType();
  }

  public Date getStartDate()
  {
    return view.getDateRangeComponent().getStartValue();
  }

  public Date getEndDate()
  {
    return view.getDateRangeComponent().getEndValue();
  }

}
