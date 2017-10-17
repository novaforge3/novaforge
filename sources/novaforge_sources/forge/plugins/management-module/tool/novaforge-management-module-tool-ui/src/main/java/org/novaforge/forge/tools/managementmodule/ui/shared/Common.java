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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.client.Window;
import org.gwtwidgets.client.util.SimpleDateFormat;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.AcronymMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.ChargePlanMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.GlobalMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.HomeMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.MonitoringMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.PilotageExecutionMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.ProjectPlanMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.TaskMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.estimation.EstimationMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.scope.ScopeMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.ressources.ManagementModuleRessources;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ChargePlanService;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ChargePlanServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.service.IterationService;
import org.novaforge.forge.tools.managementmodule.ui.client.service.IterationServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ManagementModuleService;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ManagementModuleServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ProjectPlanService;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ProjectPlanServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ReferentialService;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ReferentialServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.service.TaskService;
import org.novaforge.forge.tools.managementmodule.ui.client.service.TaskServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.service.scope.ScopeService;
import org.novaforge.forge.tools.managementmodule.ui.client.service.scope.ScopeServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.ChangePlanTableResources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;

import java.util.Map;

/**
 * @author BILET-JC
 */
public final class Common {
	/* resources */
	public static final ManagementModuleRessources RESOURCE = GWT.create(ManagementModuleRessources.class);
	/* messages */
	public static final ProjectPlanMessage MESSAGES_PROJECT_PLAN = GWT.create(ProjectPlanMessage.class);
	public static final ScopeMessage MESSAGES_SCOPE = GWT.create(ScopeMessage.class);
	public static final ChargePlanMessage MESSAGES_CHARGE_PLAN = GWT.create(ChargePlanMessage.class);
	public static final EstimationMessage MESSAGES_ESTIMATION = GWT.create(EstimationMessage.class);
	public static final PilotageExecutionMessage MESSAGES_BACKLOG = GWT
			.create(PilotageExecutionMessage.class);
	public static final MonitoringMessage MESSAGES_MONITORING = GWT.create(MonitoringMessage.class);
	public static final TaskMessage MESSAGES_TASK = GWT.create(TaskMessage.class);
	public static final AcronymMessage MESSAGES_ACRONYM = GWT.create(AcronymMessage.class);
	public static final GlobalMessage GLOBAL = GWT.create(GlobalMessage.class);
	public static final HomeMessage HOME = GWT.create(HomeMessage.class);
	public static final Resources TABLE_RESOURCES = GWT.create(TableResources.class);
	public static final Resources CHANGE_PLAN_TABLE_RESOURCES = GWT.create(ChangePlanTableResources.class);
	// the services
	public static final ManagementModuleServiceAsync COMMON_SERVICE = GWT
			.create(ManagementModuleService.class);
	public static final ProjectPlanServiceAsync PROJECT_PLAN_SERVICE = GWT.create(ProjectPlanService.class);
	public static final IterationServiceAsync ITERATION_SERVICE = GWT.create(IterationService.class);
	public static final ReferentialServiceAsync REFERENTIAL_SERVICE = GWT.create(ReferentialService.class);
	public static final ScopeServiceAsync SCOPE_SERVICE = GWT.create(ScopeService.class);
	public static final TaskServiceAsync TASK_SERVICE = GWT.create(TaskService.class);
	public static final ChargePlanServiceAsync CHARGEPLAN_SERVICE = GWT.create(ChargePlanService.class);
	/* The default event bus */
	public static final SimpleEventBus GLOBAL_EVENT_BUS = new SimpleEventBus();
	/* others */
	public static final int PAGE_SIZE = 10;
//	public static final Integer IMG_BUTTON_SIZE = 10;
	public static final String DEFAULT_FUNCTION_POINTS = "0";
	public static final String EMPTY_TEXT = "";
	// public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");
	public static final NumberFormat DECIMAL_FORMAT = NumberFormat.getDecimalFormat();
	/* date formats */
	public static final DateTimeFormat FR_DATE_FORMAT = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
	public static final SimpleDateFormat EN_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat FR_DATE_FORMAT_ONLY_DAY = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat FR_DATE_FORMAT_ONLY_DAY_SHORT = new SimpleDateFormat("dd/MM/yy");
	
	/**
	 * set the parameter to upper case
	 * 
	 * @param filter
	 * @return
	 */
	public static String allCarUp(final String txt) {
		return txt.toUpperCase();
	}

	/**
	 * set the first letter of the parameter to upper case
	 * 
	 * @param loginLabel
	 * @return
	 */
	public static String firstCarUp(final String txt) {
		return txt.replaceFirst(".", (txt.charAt(0) + EMPTY_TEXT).toUpperCase());
	}

	public static ManagementModuleRessources getResource() {
		return RESOURCE;
	}


	/**
	 * Deprecated, should use ErrorManagement.displayErrorMessage(caught);
	 * 
	 * @param caught
	 */
	@Deprecated
	public static void displayErrorMessage(final Throwable caught) {
		ErrorManagement.displayErrorMessage(caught);
	}

	public static GlobalMessage getGlobal() {
		return GLOBAL;
	}

	public static ProjectPlanMessage getProjectPlanMessages() {
		return MESSAGES_PROJECT_PLAN;
	}

	public static ChargePlanMessage getChargePlanMessages() {
		return MESSAGES_CHARGE_PLAN;
	}

	public static SafeHtml getLinesHeader(final String... pLines) {
	   final SafeHtmlBuilder safeHtml = new SafeHtmlBuilder();
	   final StringBuilder html = new StringBuilder();
		html.append("<div style=\"text-align:center;\">");
		for (String line : pLines) {
			html.append(line).append("<br/>");
		}
		html.append("</div>");
		safeHtml.appendHtmlConstant(html.toString());
		return safeHtml.toSafeHtml();
	}

	public static boolean isInt(final String pString) {
		try {
			Integer.parseInt(pString);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	/**
	 * This method removes all non numerical character from the given String and
	 * give a round Integer back
	 * 
	 * @param value
	 * @return Integer
	 */
	public static Integer makeInt(final String value) {
		String s = "";
		final StringBuffer sb = new StringBuffer();
		final String valueModified = value.replace((char) 44, (char) 46);
		for (char c : valueModified.toCharArray()) {
			if (Character.isDigit(c) || c == (char) 46) {
				s = sb.insert(s.length(), c).toString();
			}
		}
		if (-1 != s.indexOf('.')) {
			if (s.length() - 1 > s.indexOf('.')) {
				s = s.substring(0, s.indexOf('.') + 2);
			} else if (s.length() - 1 == s.indexOf('.')) {
				s = s.substring(0, s.indexOf('.'));
			}
		}
		return Integer.valueOf(Math.round(Float.valueOf(s.trim())));
	}

	/**
	 * Return a string which has numberDecimal decimals numbers
	 * 
	 * @param value
	 * @return String
	 */
	public static Float floatFormat(final Float value, final int numberDecimal) {
	   final double multidivicatoseur = Math.pow(10, numberDecimal);
	   final double d = Math.floor(value * multidivicatoseur + 0.5);
		return new Float(((float) (d)) / multidivicatoseur);
	}

	/**
	 * Call the ExportCSVServlet, at first to create the new page and the
	 * download CSV file, in second time to open it
	 * 
	 * @param moduleBaseUrl
	 * @param parameters
	 */
	public static void exportCSV(final String moduleBaseUrl, final Map<String, String> parameters) {
		final StringBuilder sb = new StringBuilder(moduleBaseUrl);
		if (!parameters.isEmpty()) {
			sb.append("?");
		}
		for (String k : parameters.keySet()) {
		   final String vx = URL.encodeQueryString(parameters.get(k));
			sb.append(k).append("=").append(vx).append("&");
		}
		final String url = sb.toString();
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-TYPE", "application/x-www-form-urlencoded");
		try {
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(final Request request, final Response response)
				{
					Window.open(url, "_self", "");
				}

				@Override
				public void onError(final Request request, final Throwable exception)
				{
					ErrorManagement.displayErrorMessage(exception.getMessage());
				}
			});
		} catch (RequestException e) {
			ErrorManagement.displayErrorMessage(Common.GLOBAL.messageExportCSVError());
		}
	}
	
}
