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
package org.novaforge.forge.widgets.quality.coverage.timeline.internal.client;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.categories.quality.QualityCategoryService;
import org.novaforge.forge.core.plugins.categories.quality.QualityMeasureBean;
import org.novaforge.forge.core.plugins.categories.quality.QualityMetric;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetDataPresenter;
import org.novaforge.forge.widgets.quality.admin.PropertiesFactory;
import org.novaforge.forge.widgets.quality.admin.QualityResource;
import org.novaforge.forge.widgets.quality.coverage.timeline.internal.module.CoverageTimelineModule;
import org.novaforge.forge.widgets.quality.module.AbstractQualityModule;

import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.ui.Component;

/**
 * Presenter used to manage data view fo coverage timeline module
 * 
 * @author sbenoist
 */
public class DataPresenter extends AbstractWidgetDataPresenter implements Serializable
{

  /**
   * 
   */
  private static final long                                  serialVersionUID = -7166287730502076507L;
  /**
   * View associated to this presenter
   */
  private final DataView                                     view;
  private final Map<QualityMetric, List<QualityMeasureBean>> measuresMapping  = new HashMap<QualityMetric, List<QualityMeasureBean>>();

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
    measuresMapping.clear();
    try
    {
      final Map<String, List<String>> applicationsByProject = getWidgetContext().getApplicationsByProject();
      final QualityResource resource = PropertiesFactory.readProperties(getWidgetContext().getProperties());
      final String projectId = applicationsByProject.keySet().iterator().next();
      final String instanceUUID = applicationsByProject.get(projectId).get(0);

      final ProjectApplication application = AbstractQualityModule.getApplicationPresenter().getApplication(
          projectId, UUID.fromString(instanceUUID));
      final QualityCategoryService pluginCategoryService = AbstractQualityModule.getPluginsManager()
          .getPluginCategoryService(application.getPluginUUID().toString(), QualityCategoryService.class);

      final UUID forgeId = AbstractQualityModule.getForgeId();
      final String currentUser = AbstractQualityModule.getCurrentUser();
      final Set<QualityMetric> metrics = new HashSet<QualityMetric>(getCoverageMetrics());

      final Map<QualityMetric, List<QualityMeasureBean>> measures = pluginCategoryService.getMeasures(
          forgeId.toString(), instanceUUID, currentUser, resource.getId(), metrics, null, null);
      if (measures != null)
      {
        measuresMapping.putAll(measures);
      }
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
  public void refreshComponent()
  {
    try
    {
      final Configuration options = view.getChart().getConfiguration();
      options.setSeries(new ArrayList<Series>());
      for (final Entry<QualityMetric, List<QualityMeasureBean>> entry : measuresMapping.entrySet())
      {
        final QualityMetric currentMetric = entry.getKey();
        final DataSeries dataSeries = new DataSeries(currentMetric.getLabel());
        for (final QualityMeasureBean historyValue : entry.getValue())
        {
          final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT,
              getCurrentLocale());
          DataSeriesItem item;
          item = new DataSeriesItem(df.parse(df.format(historyValue.getDate())),
              (double) historyValue.getValue());

          dataSeries.add(item);
        }
        options.addSeries(dataSeries);
      }

      view.draw();
    }
    catch (final ParseException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    refreshLocalized(getCurrentLocale());

  }

  private LinkedList<QualityMetric> getCoverageMetrics()
  {
    final LinkedList<QualityMetric> metricsLinked = new LinkedList<QualityMetric>();
    metricsLinked.add(QualityMetric.COVERAGE);
    return metricsLinked;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale locale)
  {
    view.refreshLocale(locale);

    final QualityResource resource = PropertiesFactory.readProperties(getWidgetContext().getProperties());
    final Configuration options = view.getChart().getConfiguration();

    options.setTitle(CoverageTimelineModule.getPortalMessages().getMessage(locale,
        Messages.QUALITYCOVERAGETIMELINE_DATA_TITLE, resource.getName()));
    
    view.draw();
  }

  protected String getQualityMetricLabel(final QualityMetric pQualityMetric)
  {

    String label = null;

    switch (pQualityMetric)
    {
      case COVERAGE:

        label = CoverageTimelineModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.QUALITYCOVERAGETIMELINE_DATA_QUALITYMETRICLABEL_COVERAGE);
        break;

      default:
        break;
    }

    return label;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

}
