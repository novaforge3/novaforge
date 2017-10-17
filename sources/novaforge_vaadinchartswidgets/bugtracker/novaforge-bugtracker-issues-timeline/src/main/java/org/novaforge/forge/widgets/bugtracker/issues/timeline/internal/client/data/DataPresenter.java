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
package org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerCategoryService;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerHistoryValueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerStatusBean;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetDataChartPresenter;
import org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.admin.PropertiesFactory;
import org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.module.IssuesTimelineModule;

import com.google.common.base.Strings;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.ui.Component;

/**
 * Presenter used to manage data view fo violations timeline module
 * 
 * @author Jeremy Casery
 */
public class DataPresenter extends AbstractWidgetDataChartPresenter implements Serializable
{

  /**
   * 
   */
  private static final long                                                         serialVersionUID = 2630327321545976815L;
  /**
   * View associated to this presenter
   */
  private final DataView                                                            view;

  private final List<Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>> issuesData       = new ArrayList<Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>>();

  /**
   * @param pWidgetContext
   * @param pView
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
  public void refreshData()
  {
    issuesData.clear();
    try
    {

      // Retrieve date
      final Date startDate = PropertiesFactory.readStartDateProperties(getWidgetContext().getProperties());
      final Date endDate = PropertiesFactory.readEndDateProperties(getWidgetContext().getProperties());
      final long deltaDate = endDate.getTime() - startDate.getTime();
      final long deltaDateDay = deltaDate / 86400000; // Transform millisecond to days
      long dateInterval = 0;
      if (deltaDateDay > 184) // Superior to 6 month
      {
        dateInterval = 604800; // 1 week
      }
      else
      {
        dateInterval = 86400; // 1 day
      }
      final List<Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>> issuesSorted = sortIssues(getIssues(dateInterval));
      if (issuesSorted != null)
      {
        issuesData.addAll(issuesSorted);
      }
    }
    catch (final Exception e)
    {
      // TODO: handle exception
      e.printStackTrace();
    }

  }

  private List<Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>> sortIssues(
      final Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> pIssues)
  {
    final List<Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>> entries = new ArrayList<Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>>(
        pIssues.entrySet());

    Collections.sort(entries, new Comparator<Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>>()
    {
      @Override
      public int compare(final Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> e1,
          final Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> e2)
      {
        return e1.getKey().getLabel().compareTo(e2.getKey().getLabel());
      }
    });
    return entries;
  }

  private Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> getIssues(final Long valueInterval)
  {
    final Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> issuesMerged = new HashMap<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>();
    try
    {
      final Map<String, List<String>> applicationsByProject = getWidgetContext().getApplicationsByProject();

      final String version = PropertiesFactory.readVersionProperties(getWidgetContext().getProperties());
      final Date startDate = PropertiesFactory.readStartDateProperties(getWidgetContext().getProperties());
      final Date endDate = PropertiesFactory.readEndDateProperties(getWidgetContext().getProperties());
      PropertiesFactory.readRangeTypeProperties(getWidgetContext().getProperties());

      for (final Entry<String, List<String>> applicationsByProjectEntries : applicationsByProject.entrySet())
      {
        final String projectId = applicationsByProjectEntries.getKey();

        final List<String> instances = applicationsByProjectEntries.getValue();
        for (final String instanceUUID : instances)
        {
          ProjectApplication application;
          application = IssuesTimelineModule.getApplicationPresenter().getApplication(projectId,
              UUID.fromString(instanceUUID));
          final BugTrackerCategoryService pluginCategoryService = IssuesTimelineModule.getPluginsManager()
              .getPluginCategoryService(application.getPluginUUID().toString(),
                  BugTrackerCategoryService.class);

          final UUID forgeId = IssuesTimelineModule.getForgeId();
          final String currentUser = IssuesTimelineModule.getCurrentUser();
          Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> projectIssues = new HashMap<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>();
          projectIssues = pluginCategoryService.getProjectIssueHistoryByStatus(forgeId.toString(),
              instanceUUID, currentUser, startDate, endDate, valueInterval, version, getCurrentLocale());

          mergeIssuesValues(issuesMerged, projectIssues);
        }
      }
    }
    catch (final Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return issuesMerged;
  }

  private void mergeIssuesValues(
      final Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> issuesMerged,
      final Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> issuesToMerge)
  {
    for (final Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> entry : issuesToMerge.entrySet())
    {
      final BugTrackerStatusBean currentStatus = entry.getKey();
      final List<BugTrackerHistoryValueBean> currentStatusValues = entry.getValue();
      boolean alreadyMerge = false;
      for (final Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> entryMerged : issuesMerged
          .entrySet())
      {
        final BugTrackerStatusBean currentStatusMerged = entryMerged.getKey();
        final List<BugTrackerHistoryValueBean> currentStatusMergedValues = entryMerged.getValue();
        if (currentStatus.getLabel().equals(currentStatusMerged.getLabel()))
        {
          for (int i = 0; i < currentStatusValues.size(); i++)
          {
            final BugTrackerHistoryValueBean bugTrackerHistoryValueMergedBean = currentStatusMergedValues
                .get(i);
            bugTrackerHistoryValueMergedBean.setValue(bugTrackerHistoryValueMergedBean.getValue()
                + currentStatusValues.get(i).getValue());
          }
          alreadyMerge = true;
        }
      }
      if (!alreadyMerge)
      {
        issuesMerged.put(currentStatus, currentStatusValues);
      }
    }
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
  protected void refreshComponent(Locale pLocale)
  {
    final Configuration options = view.getChart().getConfiguration();
    options.setSeries(new ArrayList<Series>());

    for (final Entry<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> entry : issuesData)
    {
      final DataSeries dataSeries = new DataSeries(entry.getKey().getLabel());
      for (final BugTrackerHistoryValueBean historyValue : entry.getValue())
      {
        final DataSeriesItem item = new DataSeriesItem(historyValue.getDate(), historyValue.getValue());
        dataSeries.add(item);
      }
      options.addSeries(dataSeries);
    }

    view.refreshLocale(pLocale);

    final String version = PropertiesFactory.readVersionProperties(getWidgetContext().getProperties());
    if (Strings.isNullOrEmpty(version))
    {
      options.setTitle(IssuesTimelineModule.getPortalMessages().getMessage(pLocale,
          Messages.BUGTRACKERISSUETIMELINE_DATA_TITLE_ANYVERSION));
    }
    else
    {
      options.setTitle(IssuesTimelineModule.getPortalMessages().getMessage(pLocale,
          Messages.BUGTRACKERISSUETIMELINE_DATA_TITLE, version));
    }
    view.draw();
  }

}
