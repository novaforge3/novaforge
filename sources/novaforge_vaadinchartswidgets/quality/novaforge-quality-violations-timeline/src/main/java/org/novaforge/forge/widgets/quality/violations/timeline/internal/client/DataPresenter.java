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
package org.novaforge.forge.widgets.quality.violations.timeline.internal.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.categories.quality.QualityCategoryService;
import org.novaforge.forge.core.plugins.categories.quality.QualityMeasureBean;
import org.novaforge.forge.core.plugins.categories.quality.QualityMetric;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.dashboard.client.model.WidgetColor;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetDataChartPresenter;
import org.novaforge.forge.widgets.quality.admin.PropertiesFactory;
import org.novaforge.forge.widgets.quality.admin.QualityResource;
import org.novaforge.forge.widgets.quality.module.AbstractQualityModule;
import org.novaforge.forge.widgets.quality.violations.timeline.internal.module.ViolationsTimelineModule;

import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsArea;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.Component;

/**
 * Presenter used to manage data view fo violations timeline module
 * 
 * @author Guillaume Lamirand
 */
public class DataPresenter extends AbstractWidgetDataChartPresenter implements Serializable
{
  /**
   * 
   */
  private static final long                                  serialVersionUID = 2073316688668511310L;

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
      final Set<QualityMetric> metrics = new HashSet<QualityMetric>(getViolationsMetrics());

      final Map<QualityMetric, List<QualityMeasureBean>> resources = pluginCategoryService.getMeasures(
          forgeId.toString(), instanceUUID, currentUser, resource.getId(), metrics, null, null);
      if (resources != null)
      {
        measuresMapping.putAll(resources);
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
  protected void refreshComponent(Locale pLocale)
  {

    // Remove existing series
    view.getConfiguration().setSeries(new ArrayList<Series>());
    // Build dataseries with new value
    final List<QualityMetric> metricsLinked = getViolationsMetrics();
    for (final QualityMetric qualityMetric : metricsLinked)
    {
      final List<QualityMeasureBean> metricValues = measuresMapping.get(qualityMetric);
      if ((metricValues != null) && !metricValues.isEmpty())
      {
        final DataSeries serie = new DataSeries(getQualityMetricLabel(qualityMetric));
        final PlotOptionsArea seriePlotOption = new PlotOptionsArea();
        seriePlotOption.setColor(getQualityMetricColor(QualityMetric.TESTS));
        serie.setPlotOptions(seriePlotOption);
        serie.getPlotOptions().setColor(getQualityMetricColor(qualityMetric));
        for (final QualityMeasureBean qualityMeasureBean : metricValues)
        {
          final DataSeriesItem item = new DataSeriesItem(qualityMeasureBean.getDate(),
              Long.valueOf(qualityMeasureBean.getValue().toString()));
          serie.add(item);
        }
        view.getConfiguration().addSeries(serie);
      }
    }

    view.refreshLocale(pLocale);

    final QualityResource resource = PropertiesFactory.readProperties(getWidgetContext().getProperties());
    final Configuration options = view.getConfiguration();
    options.setTitle(ViolationsTimelineModule.getPortalMessages().getMessage(pLocale,
        Messages.QUALITYVIOLATIONTIMELINE_DATA_TITLE, resource.getName()));
    options.getyAxis().setTitle(
        ViolationsTimelineModule.getPortalMessages().getMessage(pLocale,
            Messages.QUALITYVIOLATIONTIMELINE_AXIS_Y));

    view.draw();

  }

  private List<QualityMetric> getViolationsMetrics()
  {
    final List<QualityMetric> metricsLinked = new LinkedList<QualityMetric>();
    metricsLinked.add(QualityMetric.BLOCKER_VIOLATIONS);
    metricsLinked.add(QualityMetric.CRITICAL_VIOLATIONS);
    metricsLinked.add(QualityMetric.MAJOR_VIOLATIONS);
    metricsLinked.add(QualityMetric.MINOR_VIOLATIONS);
    return metricsLinked;
  }

  protected SolidColor getQualityMetricColor(final QualityMetric pQualityMetric)
  {

    SolidColor color = null;

    switch (pQualityMetric)
    {
      case BLOCKER_VIOLATIONS:

        color = WidgetColor.RED.getColor();
        break;

      case CRITICAL_VIOLATIONS:

        color = WidgetColor.ORANGE.getColor();
        break;

      case MAJOR_VIOLATIONS:

        color = WidgetColor.YELLOW.getColor();
        break;

      case MINOR_VIOLATIONS:

        color = WidgetColor.BLUE.getColor();
        break;

      default:
        break;
    }

    return color;
  }

  protected String getQualityMetricLabel(final QualityMetric pQualityMetric)
  {

    String label = null;

    switch (pQualityMetric)
    {
      case BLOCKER_VIOLATIONS:

        label = ViolationsTimelineModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.QUALITYVIOLATIONTIMELINE_DATA_QUALITYMETRICLABEL_BLOCKER);
        break;

      case CRITICAL_VIOLATIONS:

        label = ViolationsTimelineModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.QUALITYVIOLATIONTIMELINE_DATA_QUALITYMETRICLABEL_CRITICAL);
        break;

      case MAJOR_VIOLATIONS:

        label = ViolationsTimelineModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.QUALITYVIOLATIONTIMELINE_DATA_QUALITYMETRICLABEL_MAJOR);
        break;

      case MINOR_VIOLATIONS:

        label = ViolationsTimelineModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.QUALITYVIOLATIONTIMELINE_DATA_QUALITYMETRICLABEL_MINOR);
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
