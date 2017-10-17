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

import java.util.Locale;

import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.module.IssuesTimelineModule;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsArea;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.addon.charts.model.TickmarkPlacement;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * This view define to display data view
 * 
 * @author Jeremy Casery
 */
public class DataViewImpl extends VerticalLayout implements DataView
{

  /**
   * 
   */
  private static final long serialVersionUID = 1373674785961619368L;
  private Chart             chart;
  private Configuration     conf;

  /**
   * Default constructor
   */
  public DataViewImpl()
  {
    super();
    setSizeFull();

    final Component content = initContent();

    addComponent(content);
    setComponentAlignment(content, Alignment.MIDDLE_CENTER);
  }

  private Component initContent()
  {
    chart = new Chart(ChartType.AREA);
    conf = chart.getConfiguration();

    final XAxis xAxis = new XAxis();
    xAxis.setType(AxisType.DATETIME);
    xAxis.setTickmarkPlacement(TickmarkPlacement.ON);
    conf.addxAxis(xAxis);

    final YAxis yAxis = new YAxis();
    conf.addyAxis(yAxis);

    final PlotOptionsArea plotOptions = new PlotOptionsArea();
    plotOptions.setStacking(Stacking.NORMAL);
    plotOptions.setLineWidth(1);
    plotOptions.setLineColor(SolidColor.WHITE);
    plotOptions.setShowInLegend(true);
    final Marker marker = new Marker();
    marker.setLineWidth(1);
    marker.setLineColor(SolidColor.WHITE);
    plotOptions.setMarker(marker);
    conf.setPlotOptions(plotOptions);

    chart.drawChart(conf);

    return chart;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    conf.getyAxis()
        .setTitle(
            IssuesTimelineModule.getPortalMessages().getMessage(pLocale,
                Messages.BUGTRACKERISSUETIMELINE_AXIS_Y));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void draw()
  {
    chart.drawChart(conf);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Chart getChart()
  {
    return chart;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Configuration getConfiguration()
  {
    return conf;
  }

}
