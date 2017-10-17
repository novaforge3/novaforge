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
package org.novaforge.forge.widgets.bugtracker.issuedivision.internal.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerCategoryService;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetDataChartPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.widgets.bugtracker.issuedivision.internal.admin.PropertiesFactory;
import org.novaforge.forge.widgets.bugtracker.issuedivision.internal.module.IssueDivisionModule;

import com.google.common.base.Strings;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * Presenter used to manage data view for BugTracker issues division module
 * 
 * @author Guillaume Lamirand
 */
public class DataPresenter extends AbstractWidgetDataChartPresenter implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 2073316688668511310L;

  /**
   * View associated to this presenter
   */
  private final DataView    view;

  private DataSeries        dataSeries;

  /**
   * @param pWidgetContext
   */
  public DataPresenter(final WidgetContext pWidgetContext, final DataView pView)
  {
    super(pWidgetContext);
    // Init the view
    view = pView;
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
  public void refreshData()
  {
    try
    {
      // Sort issues by Status
      final Map<String, List<BugTrackerIssueBean>> issuesByStatus = new HashMap<String, List<BugTrackerIssueBean>>();

      final Map<String, List<String>> applicationsByProject = getWidgetContext().getApplicationsByProject();

      final String version = PropertiesFactory.readProperties(getWidgetContext().getProperties());

      for (final Entry<String, List<String>> applicationsByProjectEntries : applicationsByProject.entrySet())
      {
        final String projectId = applicationsByProjectEntries.getKey();

        final List<String> instances = applicationsByProjectEntries.getValue();
        for (final String instanceUUID : instances)
        {
          final ProjectApplication application = IssueDivisionModule.getApplicationPresenter()
              .getApplication(projectId, UUID.fromString(instanceUUID));
          final BugTrackerCategoryService pluginCategoryService = IssueDivisionModule.getPluginsManager()
              .getPluginCategoryService(application.getPluginUUID().toString(),
                  BugTrackerCategoryService.class);

          final UUID forgeId = IssueDivisionModule.getForgeId();
          final String currentUser = IssueDivisionModule.getCurrentUser();
          final List<BugTrackerIssueBean> issues;
          issues = pluginCategoryService.getAllProjectIssuesByVersion(forgeId.toString(), instanceUUID,
              currentUser, version, getCurrentLocale()).getIssues();

          for (final BugTrackerIssueBean issue : issues)
          {
            final String status = issue.getStatus();
            List<BugTrackerIssueBean> filteredIssues = issuesByStatus.get(status);
            if (filteredIssues == null)
            {
              filteredIssues = new ArrayList<BugTrackerIssueBean>();
              issuesByStatus.put(status, filteredIssues);
            }
            filteredIssues.add(issue);
          }

        }
      }

      DataSeriesItem toSelect = null;
      // Add issue count by status to DataSeries
      dataSeries = new DataSeries("Total");
      for (final Entry<String, List<BugTrackerIssueBean>> entry : issuesByStatus.entrySet())
      {
        final DataSeriesItem currentItem = new DataSeriesItem(entry.getKey(), entry.getValue().size());
        if ((toSelect == null) || (currentItem.getY().intValue() > toSelect.getY().intValue()))
        {
          toSelect = currentItem;
        }
        dataSeries.add(currentItem);
      }
      if (toSelect != null)
      {
        toSelect.setSelected(true);
        toSelect.setSliced(true);
      }
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(IssueDivisionModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale()); 
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshComponent(final Locale pLocale)
  {
    view.getConfiguration().setSeries(new ArrayList<Series>());
    if(dataSeries != null) {
      view.getConfiguration().addSeries(dataSeries);
    }
    view.refreshLocale(pLocale);

    final String version = PropertiesFactory.readProperties(getWidgetContext().getProperties());
    final Configuration options = view.getConfiguration();
    if (Strings.isNullOrEmpty(version))
    {
      options.setTitle(IssueDivisionModule.getPortalMessages().getMessage(pLocale,
          Messages.BUGTRACKERISSUEDIVISION_DATA_TITLE_ANYVERSION));
    }
    else
    {
      options.setTitle(IssueDivisionModule.getPortalMessages().getMessage(pLocale,
          Messages.BUGTRACKERISSUEDIVISION_DATA_TITLE, version));
    }

    view.draw();
  }

}
