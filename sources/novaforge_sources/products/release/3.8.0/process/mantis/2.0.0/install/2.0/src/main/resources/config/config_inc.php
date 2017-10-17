<?php
/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * @copyright Copyright 2016  Atos, NovaForge Version 3 and above.
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

	$g_hostname 				= '@HOSTMANTIS@';
	$g_db_type 					= 'mysql';
	$g_database_name 			= '%DATABASE%';
	$g_db_username 			= '@MANTISUSER@';
	$g_db_password 			= '@PASSMANTIS@';
	
	$g_allow_signup					= OFF;
	$g_reauthentication				= OFF;
	$g_enable_email_notification	= ON;
	$g_create_user_notification	= OFF;

	/***********************
	 * Mantis CAS Settings *
	 ***********************/
	$g_login_method		= CAS_AUTH;
	$g_cas_server 			= '@CAS_HOST@';
	$g_cas_port 			=  @CAS_PORT@;
	$g_cas_uri 				= '@CAS_URI@';
	$g_cas_validate      = 'https://@CAS_HOST@@CAS_SERVICE_URI@';
	$g_cas_debug 			= '@MANTIS_HOME@/log/cas';
	

	$g_instance_wsdl		= 'http://127.0.0.1:@PORTKARAF@/cxf/mantisInstance?wsdl';
	$g_instance_method	= 'getToolProjectId';
	
	$g_smtp_host			= '@RELAISMTP@';
	$g_smtp_username 		= '@LOGINSMTP@';
	$g_smtp_password 		= '@PASSSMTP@';
	$g_smtp_connection_mode = '';
	$g_smtp_port 			= @RELAISMTPPORT@;
	
	$g_default_language		= 'french';	

	//Need because of php version used by Novaforge
	date_default_timezone_set('@DATETIMEZONE@');

	/* Taille pièce jointe max */
	$g_max_file_size = 5000000;

	/* Custom date format used  into CSV export*/
    $g_csv_export_date_format = 'd/m/Y H:i';
	
	/* path for testlink notification and email notification */ 
	$g_path = "@HTTPD_BASE_URL@/@MANTIS_DEFAULT_ALIAS@/@MANTIS_TOOL_ALIAS@/";
	
	/* enable project and user management */
	$g_enable_mantis_management = OFF;

	/* Status */
	/* values used to insert new status */
	$g_default_status_prev = "50";
    $g_default_status_next = "80";
	$g_default_status_enum_string = '10:new,20:feedback,30:acknowledged,40:confirmed,50:assigned,80:resolved,90:closed';
?>
