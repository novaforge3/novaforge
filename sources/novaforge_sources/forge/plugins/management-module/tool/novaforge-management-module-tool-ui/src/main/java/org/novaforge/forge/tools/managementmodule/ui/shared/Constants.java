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
package org.novaforge.forge.tools.managementmodule.ui.shared;

public interface Constants {

   /**
    * Constant for identified that a refScopeUnit is a CDORepository in CellTree
    */
   String ROOT_REPOSITORY_KEY = "_REPO_ROOT_";

   /**
    * Constant Id for the first line of ChargePlan tab, total of the other lines
    */
   String CHARGE_PLAN_FIRST_LINE_ID   = "UnitTimeEnum";
   /**
    * Constant Name for the first line of ChargePlan tab, total of the other lines
    */
   String CHARGE_PLAN_FIRST_LINE_NAME = "Disciplines";

   /**
    * Constant Id and Name for the last line of ChargePlan tab, total of the other lines
    */
   String CHARGE_PLAN_TOTAL_LINE_ID = "TOTAL";

   /**
    * CONSTANTS CSV
    */

   /**
    * the servlet Name for CSV export
    */
   String EXPORT_CSV_SERVLET_NAME   = "exportcsv";
   /**
    * Define the data to put in CSV file
    */
   String EXPORT_CSV_NAME_PARAMETER = "filename";

   String SESSION_EXPORT_CSV_PARAMS = "exportCSVParams";
   String CSV_COLUMN_SEPARATOR      = ",";
   String CSV_EMPTY                 = "";

   /**
    * CONSTANTS BIRT
    */

   /**
    * the servlet Name for report generation
    */
   String REPORT_SERVLET_NAME = "report";

   /**
    * REPORT NAMES
    */

   /**
    * The name for the chargePlan.rptdesign
    */
   String BIRT_CHARGE_PLAN_REPORT_NAME = "chargePlan";

   /**
    * The name for the ganttDiagramOfLots.rptdesign
    */
   String BIRT_GANTT_LOT_REPORT_NAME = "ganttDiagramOfLots";

   /**
    * The name for the burnDownChart.rptdesign
    */
   String BIRT_BURNDOWNCHART_REPORT_NAME = "burnDownChart";

   /**
    * The name for the taskCategoriesPie.rptdesign
    */
   String BIRT_TASKCATEGORIESPIE_REPORT_NAME = "taskCategoriesPie";

   /**
    * The name for the focusingFactor.rptdesign
    */
   String BIRT_FOCUSINGFACTOR_REPORT_NAME = "focusingFactor";

   /**
    * The name for the idealWorkingPoint.rptdesign
    */
   String BIRT_IDEALWORKINGPOINT_REPORT_NAME = "idealWorkingPoint";

   /**
    * The name for the standardDevation.rptdesign
    */
   String BIRT_STANDARDDEVIATION_REPORT_NAME = "standardDeviation";

   /**
    * TECHNICAL PARAMETERS NAME
    */

   /**
    * The parameter for the  rptdesign file's name for report generation
    */
   String BIRT_REPORT_NAME_PARAMETER   = "birt_report_name";
   /**
    * Define if the result must be displayed in the page or downloaded
    */
   String BIRT_DOWNLOAD_FILE_PARAMETER = "birt_download_file";
   /**
    * Constant true
    */
   String TRUE                         = "true";
   /**
    * Constant false
    */
   String FALSE                        = "false";
   /**
    * Constant all
    */
   String ALL                          = "all";
   /**
    * FUNCTIONAL PARAMETERS NAME
    */

   /**
    * Parameter name for projectPlanId for BIRT
    */
   String BIRT_PROJECT_PLAN_ID_PARAMETER          = "birt_projectPlanId";
   /**
    * Parameter name for projectPlanId for BIRT
    */
   String BIRT_UNIT_TIME_NAME_PARAMETER           = "birt_UnitTimeName";
   /**
    * Parameter name for projectName for BIRT
    */
   String BIRT_PROJECT_NAME_PARAMETER             = "birt_projectName";
   /**
    * Parameter name for projectPlanVersion for BIRT
    */
   String BIRT_PROJECT_PLAN_VERSION_PARAMETER     = "birt_projectPlanVersion";
   /**
    * Parameter name for taskCategoriesPieTaskUnit for BIRT
    */
   String BIRT_BURNDOWNCHART_TASKUNIT_PARAMETER   = "birt_burnDownChartTaskUnit";
   /**
    * Parameter name for iterationId for BIRT
    */
   String BIRT_ITERATIONID_PARAMETER              = "birt_iterationId";
   /**
    * Parameter name for disciplineFunctionalId for BIRT
    */
   String BIRT_DISCIPLINE_FUNCTIONAL_ID_PARAMETER = "birt_disciplineFunctionalId";
   /**
    * Parameter name for lotId for BIRT
    */
   String BIRT_LOTID_PARAMETER                    = "birt_lotId";
   /**
    * Parameter name for subLotId for BIRT
    */
   String BIRT_SUBLOTID_PARAMETER                 = "birt_subLotId";

   /** GDI name */
   String  GDI              = "GDI";
   /** IN name */
   String  IN               = "IN";
   /** INT name */
   String  INT              = "INT";
   /** OUT name */
   String  OUT              = "OUT";
   /** GDE name */
   String  GDE              = "GDE";
   /** CDO default port */
   Integer CDO_DEFAULT_PORT = 2036;
}
