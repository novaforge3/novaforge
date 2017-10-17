/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the
 * License.
 *
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have
 * received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with libraries listed in COPYRIGHT file at the
 * top-level directof of this distribution (or a modified version of
 * that libraries), containing parts covered by the terms of licenses
 * cited in the COPYRIGHT file, the licensors of this Program grant
 * you additional permission to convey the resulting work.
 */
package org.novaforge.forge.ui.novadeploy.internal.client.main.overview;

import java.math.BigDecimal;
import java.util.Locale;

import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.OrgResources;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Vincent Chenal
 */
public class OverviewViewImpl extends VerticalLayout implements OverviewView
{

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = -8751426074045820735L;

	private HorizontalLayout firstLine;
	private Panel cloudAccountInfos;
	private Panel cloudRessourcesInfos;
	private Panel eventHistory;
	private Table eventTable;
	private Chart chartDiskResources;
	private Chart chartMemoryResources;

	private Label labOrganizationName;
	private Label labOrganizationNameVal;

	// DATAS
	private float totalAvailableSpace;
	private float totalUsedSpace;
	private float totalAvailableMemory;
	private float totalUsedMemory;

	/**
	 * Default constructor.
	 */
	public OverviewViewImpl()
	{
		super();
		firstLine = new HorizontalLayout();
		cloudAccountInfos = new Panel();
		cloudRessourcesInfos = new Panel();
		eventHistory = new Panel();
		eventTable = new Table();

		chartDiskResources = new Chart(ChartType.PIE);
		chartMemoryResources = new Chart(ChartType.PIE);
		labOrganizationName = new Label();
		labOrganizationNameVal = new Label();

		initContent();
		refreshLocale(getLocale());
	}

	private void initContent()
	{
		setWidth("100%");
		setSpacing(true);
		setMargin(true);

		// CLOUD RESSOURCES INFOS PANEL
		cloudRessourcesInfos.setWidth("100%");
		cloudRessourcesInfos.setHeight("350px");
		chartDiskResources.setHeight("300px");
		chartDiskResources.setWidth("100%");
		chartMemoryResources.setHeight("300px");
		chartMemoryResources.setWidth("100%");

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.setMargin(true);
		hl.addComponent(chartDiskResources);
		hl.addComponent(chartMemoryResources);
		cloudRessourcesInfos.setContent(hl);

		// EVENT HISTORY PANEL
		eventHistory.setSizeFull();
		eventTable.addContainerProperty("Event", String.class, null);
		eventTable.setSizeFull();
		populateEventHistory();
		eventHistory.setContent(eventTable);

		this.addComponent(cloudRessourcesInfos);
		this.addComponent(eventHistory);
	}

	public void populateEventHistory()
	{
		eventTable
				.addItem(
						new Object[]
						{ "[23/04/2015 | 13:25]	[DEPLOYMENT SUCCESS] Deployment succeded from descriptor \"finalEnvironment-1.2.0.xml\"" },
						"1");
		eventTable
				.addItem(
						new Object[]
						{ "[22/04/2015 | 13:25]	[DEPLOYMENT SUCCESS] Deployment succeded from descriptor \"finalEnvironment-1.1.0.xml\"" },
						"2");
		eventTable
				.addItem(
						new Object[]
						{ "[22/04/2015 | 12:24]	[DEPLOYMENT SUCCESS] Deployment succeded from descriptor \"finalEnvironment-1.0.0.xml\"" },
						"3");
		eventTable
				.addItem(
						new Object[]
						{ "[21/04/2015 | 22:31]	[DEPLOYMENT FAILED] Deployment failed from descriptor \"finalEnvironment-0.9.0.xml\"" },
						"4");
		eventTable
				.addItem(
						new Object[]
						{ "[19/04/2015 | 06:56]	[DEPLOYMENT SUCCESS] Deployment succeded from descriptor \"finalEnvironment-0.8.0.xml\"" },
						"5");
		eventTable.addItem(new Object[]
		{ "[17/04/2015 | 13:19]	[DEPLOYMENT FAILED]Deployment failed from descriptor \"finalEnvironment-0.8.0.xml\"" },
				"6");
	}

	public Component prepareCloudAccountInfosGrid()
	{
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		GridLayout grid = new GridLayout();
		grid.setColumns(2);
		grid.setSizeFull();
		grid.setSpacing(true);
		grid.setMargin(true);

		grid.addComponent(labOrganizationName);
		grid.setComponentAlignment(labOrganizationName, Alignment.MIDDLE_RIGHT);
		grid.addComponent(labOrganizationNameVal);
		grid.setComponentAlignment(labOrganizationNameVal, Alignment.MIDDLE_LEFT);

		Label iconNovaCloud = new Label();
		iconNovaCloud.setIcon(new ThemeResource(NovaForgeResources.ICON_NOVACLOUD));
		iconNovaCloud.setStyleName("");

		hl.addComponent(grid);
		hl.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
		hl.addComponent(iconNovaCloud);
		hl.setComponentAlignment(iconNovaCloud, Alignment.MIDDLE_CENTER);

		return hl;
	}

	@Override
	public void prepareCloudRessourcesGrid()
	{
		// DISK RESOURCES
		Configuration confDisk = chartDiskResources.getConfiguration();

		confDisk.setTitle("Disk Resources");
		confDisk.setSubTitle("Total : " + (int) (this.totalAvailableSpace - totalAvailableMemory));

		PlotOptionsPie plotOptionsDisk = new PlotOptionsPie();
		plotOptionsDisk.setCursor(Cursor.POINTER);
		Labels dataLabelsDisk = new Labels(true);
		dataLabelsDisk.setFormatter("''+ this.point.name +' : '+ (this.percentage).toFixed(1) +' %'");
		plotOptionsDisk.setDataLabels(dataLabelsDisk);

		confDisk.setPlotOptions(plotOptionsDisk);

		final DataSeries seriesDisk = new DataSeries();

		float totalUsedPercentDisk = ((totalUsedSpace - totalUsedMemory) / (this.totalAvailableSpace - totalAvailableMemory)) * 100;
		totalUsedPercentDisk = round(totalUsedPercentDisk, 1);
		float totalAvailablePercentDisk = 100 - totalUsedPercentDisk;
		totalAvailablePercentDisk = round(totalAvailablePercentDisk, 1);

		int availableSpace = (int) ((this.totalAvailableSpace - totalAvailableMemory) - (totalUsedSpace - totalUsedMemory));
		int usedSpace = (int) (totalUsedSpace - totalUsedMemory);

		seriesDisk.add(new DataSeriesItem("" + usedSpace + " MB used", totalUsedPercentDisk));
		seriesDisk.add(new DataSeriesItem("" + availableSpace + " MB available", totalAvailablePercentDisk));

		confDisk.setSeries(seriesDisk);

		chartDiskResources.drawChart(confDisk);

		// MEMORY RESOURCES
		Configuration confMemory = chartMemoryResources.getConfiguration();

		confMemory.setTitle("Memory Resources");
		confMemory.setSubTitle("Total : " + (int) (this.totalAvailableMemory));

		PlotOptionsPie plotOptionsMemory = new PlotOptionsPie();
		plotOptionsMemory.setCursor(Cursor.POINTER);
		Labels dataLabelsMemory = new Labels(true);
		dataLabelsMemory.setFormatter("''+ this.point.name +' : '+ (this.percentage).toFixed(1) +' %'");
		plotOptionsMemory.setDataLabels(dataLabelsMemory);

		confMemory.setPlotOptions(plotOptionsMemory);

		final DataSeries seriesMemory = new DataSeries();

		float totalUsedPercentMemory = totalUsedMemory / totalAvailableMemory * 100;
		totalUsedPercentMemory = round(totalUsedPercentMemory, 1);
		float totalAvailablePercentMemory = 100 - totalUsedPercentMemory;
		totalAvailablePercentMemory = round(totalAvailablePercentMemory, 1);

		int availableMemory = (int) (totalAvailableMemory - totalUsedMemory);
		int usedMemory = (int) (totalUsedMemory);

		seriesMemory
				.add(new DataSeriesItem("" + usedMemory + " MB used", totalUsedPercentMemory, SolidColor.DARKGREEN));
		seriesMemory.add(new DataSeriesItem("" + availableMemory + " MB available", totalAvailablePercentMemory,
				SolidColor.CHOCOLATE));

		confMemory.setSeries(seriesMemory);

		chartMemoryResources.drawChart(confMemory);
	}

	private static float round(float d, int decimalPlace)
	{
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_DOWN);
		return bd.floatValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach()
	{
		super.attach();
		refreshLocale(getLocale());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshLocale(final Locale pLocale)
	{
		cloudAccountInfos.setCaption("Cloud Account Infos");
		cloudRessourcesInfos.setCaption("Cloud Ressources");
		eventHistory.setCaption("Event History");

		labOrganizationName.setCaption("Organization Name");
		labOrganizationNameVal.setCaption("NovaCloud");

		// To implement
		/*
		 * returnButton.setCaption(MailingModule.getPortalMessages().
		 * getMessage(pLocale, Messages.MAILING_LISTS_ADD_BACK));
		 * 
		 * mailingListForm.setCaption(MailingModule.getPortalMessages()
		 * .getMessage(pLocale, Messages.MAILING_LISTS_ADD_TITLE)); if
		 * (mailingListFormFieldFactory.getName() != null) {
		 * mailingListFormFieldFactory.getName().setCaption(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME));
		 * mailingListFormFieldFactory.getName().setDescription(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_DESCRIPTION));
		 * mailingListFormFieldFactory.getName().setRequiredError(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_REQUIRED) + " " +
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_REGEXP));
		 * mailingListFormFieldFactory
		 * .getName().removeAllValidators();
		 * mailingListFormFieldFactory.getName().addValidator( new
		 * ListnameValidator
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_REGEXP)));
		 * mailingListFormFieldFactory.getName().addValidator( new
		 * StringLengthValidator
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_REQUIRED), 3, 40, false)); } if
		 * (mailingListFormFieldFactory.getSubject() != null) {
		 * mailingListFormFieldFactory.getSubject().setCaption(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_SUBJECT));
		 * mailingListFormFieldFactory.getSubject().setRequiredError(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_SUBJECT_REQUIRED));
		 * mailingListFormFieldFactory
		 * .getSubject().removeAllValidators();
		 * mailingListFormFieldFactory.getSubject().addValidator( new
		 * StringLengthValidator
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_SUBJECT_REQUIRED), 3, 40, false)); }
		 * if (mailingListFormFieldFactory.getDescription() != null) {
		 * mailingListFormFieldFactory.getDescription().setCaption(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MEMBERSHIPS_ROLES_DESCRIPTION));
		 * mailingListFormFieldFactory
		 * .getDescription().setRequiredError(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_DESCRIPTION_REQUIRED));
		 * mailingListFormFieldFactory
		 * .getDescription().removeAllValidators();
		 * mailingListFormFieldFactory.getDescription().addValidator(
		 * new
		 * StringLengthValidator(MailingModule.getPortalMessages().
		 * getMessage(pLocale,
		 * Messages.MAILING_LISTS_DESCRIPTION_REQUIRED), 3, 250,
		 * false)); } if (mailingListFormFieldFactory.getType() !=
		 * null) { final ComboBox typeBox =
		 * mailingListFormFieldFactory.getType();
		 * typeBox.setCaption(MailingModule
		 * .getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE));
		 * 
		 * typeBox.addItem(MailingListType.HOTLINE_LIST);
		 * typeBox.setItemCaption(MailingListType.HOTLINE_LIST,
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_HOTLINE));
		 * typeBox.addItem(MailingListType.PRIVATE_LIST);
		 * typeBox.setItemCaption(MailingListType.PRIVATE_LIST,
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_PRIVATE));
		 * typeBox.setInputPrompt
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_INPUTPROMPT));
		 * typeBox.setRequiredError
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_REQUIRED));
		 * typeBox.setDescription
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_DESCRIPTION)); }
		 * 
		 * addButton.setCaption(MailingModule.getPortalMessages().
		 * getMessage(pLocale, Messages.MAILING_LISTS_ADD_CONFIRM));
		 * cancelButton
		 * .setCaption(MailingModule.getPortalMessages().getMessage
		 * (pLocale, Messages.MAILING_LISTS_ADD_CANCEL));
		 */
	}

	@Override
	public void refreshContent()
	{

	}

	@Override
	public void setResources(OrgResources orgResources)
	{
		this.totalAvailableSpace = orgResources.getStorageLimitMB();
		this.totalUsedSpace = orgResources.getStorageUsedMB();
		this.totalAvailableMemory = orgResources.getMemoryLimit().intValue();
		this.totalUsedMemory = orgResources.getMemoryUsed().intValue();
	}
}
