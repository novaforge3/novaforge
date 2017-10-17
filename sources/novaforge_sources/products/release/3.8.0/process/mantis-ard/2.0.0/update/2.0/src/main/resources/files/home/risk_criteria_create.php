<?php
/**
* NovaForge(TM) is a web-based forge offering a Collaborative Development and
* Project Management Environment.
*
* Copyright (C) 2007-2009 BULL SAS
* Copyright (C) 2017  Atos, NovaForge Version 3 and above.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/


/**
* MantisBT Core API's
*/
require_once( 'core.php' );
require_api( 'risk_criteria_api.php' );

form_security_validate( 'risk_criteria_create' );

$t_risk_criterias = risk_criteria_get_all();

$t_risk_criteria = new risk_criteria;
$t_risk_criteria->label = gpc_get_string( 'label', '' );
$t_risk_criteria->weight = 0;
$t_risk_criteria->project_id = project_get_other_type_project_id('management');
$t_risk_criteria->category_id = gpc_get_int( 'category_id', 0 );
$t_risk_criteria->evaluation1 = gpc_get_string( 'evaluation1', '' );
$t_risk_criteria->evaluation2 = gpc_get_string( 'evaluation2', '' );
$t_risk_criteria->evaluation3 = gpc_get_string( 'evaluation3', '' );
$t_risk_criteria->evaluation4 = gpc_get_string( 'evaluation4', '' );
$t_risk_criteria->evaluation5 = gpc_get_string( 'evaluation5', '' );
$t_risk_criteria->evaluation6 = gpc_get_string( 'evaluation6', '' );

# Create the risk criteria
$t_risk_criteria_id = $t_risk_criteria->create();

form_security_purge( 'risk_criteria_create' );

print_successful_redirect( 'risk_criteria_page.php' );

?>
