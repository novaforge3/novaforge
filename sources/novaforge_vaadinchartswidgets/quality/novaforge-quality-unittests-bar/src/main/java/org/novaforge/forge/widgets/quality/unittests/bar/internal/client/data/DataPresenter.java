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
package org.novaforge.forge.widgets.quality.unittests.bar.internal.client.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.novaforge.forge.widgets.quality.admin.QualityResource;
import org.novaforge.forge.widgets.quality.module.AbstractQualityModule;
import org.novaforge.forge.widgets.quality.unittests.bar.internal.admin.PropertiesFactory;
import org.novaforge.forge.widgets.quality.unittests.bar.internal.module.UnittestsBarModule;

import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.Component;

/**
 * Presenter used to manage data view fo violations timeline module
 * 
 * @author Gauthier Cart
 */
public class DataPresenter extends AbstractWidgetDataChartPresenter implements Serializable
{

  /**
   * Serial version id
   */
  private static final long                    serialVersionUID = -3914736728082518937L;

  /**
   * View associated to this presenter
   */
  private final DataView                       view;

  Map<QualityMetric, List<QualityMeasureBean>> measuresMapping  = new HashMap<QualityMetric, List<QualityMeasureBean>>();
  private static final int                     BARS_NUMBER      = 10;

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
      final Set<QualityMetric> metrics = new HashSet<QualityMetric>(getTestsMetrics());

      final Map<QualityMetric, List<QualityMeasureBean>> resources = pluginCategoryService.getMeasures(
          forgeId.toString(), instanceUUID, currentUser, resource.getId(), metrics, null, null);
      if (resource != null)
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

    final Integer numberOfBuild = PropertiesFactory.readPropertiesNumberOfBuild(getWidgetContext()
        .getProperties());

    final Configuration options = view.getChart().getConfiguration();
    options.setSeries(initDataSeries(measuresMapping, numberOfBuild));

    final QualityResource resource = PropertiesFactory.readProperties(getWidgetContext().getProperties());

    view.refreshLocale(pLocale);

    options.setTitle(UnittestsBarModule.getPortalMessages().getMessage(pLocale,
        Messages.UNITTESTSBAR_DATA_TITLE, resource.getName()));

    options.getyAxis().setTitle(
        UnittestsBarModule.getPortalMessages().getMessage(pLocale, Messages.UNITTESTSBAR_AXIS_Y));

    view.draw();

  }

  private List<Series> initDataSeries(final Map<QualityMetric, List<QualityMeasureBean>> resources,
      final Integer pNumberOfBuild)
  {
    // Build dataseries with new value
    final List<Series> dataSeries = new ArrayList<Series>();

    final Map<Date, Long> testsNumberByDates = new HashMap<Date, Long>();
    final Map<Date, Long> testsSuccesByDates = new HashMap<Date, Long>();
    final LinkedList<Date> datesOrders = new LinkedList<Date>();

    final List<QualityMetric> metricsLinked = getTestsMetrics();
    metricsLinked.remove(QualityMetric.TESTS);

	if(resources.get(QualityMetric.TESTS) != null)
	{
		for (final QualityMeasureBean qualityMeasureBean : resources.get(QualityMetric.TESTS))
		{

		  testsNumberByDates.put(qualityMeasureBean.getDate(),
			  Long.valueOf(qualityMeasureBean.getValue().toString()));

		  testsSuccesByDates.put(qualityMeasureBean.getDate(),
			  Long.valueOf(qualityMeasureBean.getValue().toString()));

		  // Build a list of the 10 dates to print
		  if (datesOrders.size() >= pNumberOfBuild)
		  {
			datesOrders.removeFirst();
			datesOrders.add(qualityMeasureBean.getDate());
		  }
		  else
		  {
			datesOrders.add(qualityMeasureBean.getDate());
		  }
		}
	}
    // Build data series
    for (final QualityMetric qualityMetric : metricsLinked)
    {
      final List<QualityMeasureBean> metricValues = resources.get(qualityMetric);
      if ((metricValues != null) && !metricValues.isEmpty())
      {
        final ListSeries series = new ListSeries(getQualityMetricLabel(qualityMetric));
        final PlotOptionsColumn seriePlotOption = new PlotOptionsColumn();
        seriePlotOption.setColor(getQualityMetricColor(qualityMetric));
        series.setPlotOptions(seriePlotOption);
        for (final QualityMeasureBean qualityMeasureBean : metricValues)
        {
          if (datesOrders.contains(qualityMeasureBean.getDate()))
          {
            Long measureValue = Long.valueOf("0");
            if (qualityMeasureBean.getValue() != null)
            {
              measureValue = Long.valueOf(qualityMeasureBean.getValue().toString());
            }
            // Add data to the current data series
            series.addData(measureValue);

            // Calculate the new success data
            final Long success = testsSuccesByDates.get(qualityMeasureBean.getDate()) - measureValue;

            // Set the success data
            testsSuccesByDates.put(qualityMeasureBean.getDate(), success);
          }
        }
        dataSeries.add(series);
      }
    }

    // Build Success data series
    final ListSeries succesSeries = new ListSeries(getQualityMetricLabel(QualityMetric.TESTS));
    final PlotOptionsColumn succesPlotOption = new PlotOptionsColumn();
    succesPlotOption.setColor(getQualityMetricColor(QualityMetric.TESTS));
    succesSeries.setPlotOptions(succesPlotOption);
    succesSeries.getPlotOptions().setColor(getQualityMetricColor(QualityMetric.TESTS));
    final String[] categories = new String[datesOrders.size()];
    int i = 0;
    final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT,
        getCurrentLocale());
    for (final Date date : datesOrders)
    {
      succesSeries.addData(testsSuccesByDates.get(date));
      categories[i] = df.format(date);
      i++;
    }
    dataSeries.add(succesSeries);

    view.getConfiguration().getxAxis().setCategories(categories);
    return dataSeries;
  }

  private List<QualityMetric> getTestsMetrics()
  {
    final List<QualityMetric> metricsLinked = new LinkedList<QualityMetric>();
    metricsLinked.add(QualityMetric.TEST_ERRORS);
    metricsLinked.add(QualityMetric.TEST_FAILURES);
    metricsLinked.add(QualityMetric.TESTS);

    return metricsLinked;
  }

  protected SolidColor getQualityMetricColor(final QualityMetric pQualityMetric)
  {

    SolidColor metricColor = null;
    switch (pQualityMetric)
    {
      case TESTS:
        metricColor = WidgetColor.GREEN.getColor();
        break;

      case TEST_ERRORS:
        metricColor = WidgetColor.RED.getColor();
        break;

      case TEST_FAILURES:
        metricColor = WidgetColor.YELLOW.getColor();
        break;

      default:
        break;
    }

    return metricColor;

  }

  protected String getQualityMetricLabel(final QualityMetric pQualityMetric)
  {

    String label = null;

    switch (pQualityMetric)
    {
      case TESTS:

        label = UnittestsBarModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.UNITTESTSBAR_DATA_QUALITYMETRICLABEL_TESTS);
        break;

      case TEST_FAILURES:

        label = UnittestsBarModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.UNITTESTSBAR_DATA_QUALITYMETRICLABEL_TESTFAILURES);
        break;

      case TEST_ERRORS:

        label = UnittestsBarModule.getPortalMessages().getMessage(view.getLocale(),
            Messages.UNITTESTSBAR_DATA_QUALITYMETRICLABEL_TESTERRORS);
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
